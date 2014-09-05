/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_3;

import solarbeam_2.XboxController;

/**
 * @author Luc Bettaieb
 * This is software to run the PanTilt with the Xbox controller using the Arduino interface.
 * Currently, there's an issue with getting the Arduino drivers running on the cRIO, so
 * a different control solution for the PanTilt is being sought out (Poulu Servo Controller)
 * 
 * To use this, connect an Arduino UNO with the correct software on it and have the pan PWM
 * signal wire connected to 9 and the tilt signal connected to 10.
 */
public class Pantilt_Moving_Test {
    public static void main(String[] args){
        XboxController controller = new XboxController();
        PanTilt_Arduino pt = new PanTilt_Arduino();
        System.out.println("Solarbeam Testing Program");
        
        boolean xstop = false;
        boolean ystop = false;
        
        if(!controller.isControllerConencted()){
                System.out.println("No controller found!");
                System.out.println("Connect controller and try again.");
                System.exit(0);
            }
        if(!controller.pollController()){ //Initial Controller Poll 
            System.out.println("Controller disconnected!");
            System.out.println("Reconnect controller and try again.");
            System.exit(0);
        }
        
        while(!controller.getButtonValue(6)){
            if(!controller.pollController()){ //Initial Controller Poll 
                System.out.println("Controller disconnected!");
                System.out.println("Reconnect controller and try again.");
                System.exit(0);
            }
            if(xstop && controller.getXAxisPercentageRIGHT() > 35 && controller.getXAxisPercentageRIGHT() < 65){
                pt.stopX();
                xstop = false;
                //System.out.println("mid");
            }
            else if(controller.getXAxisPercentageRIGHT() >= 65 ){
                pt.setMaxPanRight();
                xstop = true;
                System.out.println("Right");
            }
            else if(controller.getXAxisPercentageRIGHT() <= 35 ){
                pt.setMaxPanLeft();
                xstop = true;
                System.out.println("Left");
            }


            if(ystop && controller.getYAxisPercentageRIGHT() > 35 && controller.getYAxisPercentageRIGHT() < 65){
                pt.stopY();
                ystop = false;
            }
            else if(controller.getYAxisPercentageRIGHT() >= 65 ){
                pt.setMaxTiltDown();
                ystop = true;
                System.out.println("down");
                
            }
            else if(controller.getYAxisPercentageRIGHT() <= 35 ){
                pt.setMaxTiltUp();
                ystop = true;
                System.out.println("up");
            }
            
        }
    }
}

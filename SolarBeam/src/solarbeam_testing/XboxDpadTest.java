/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import solarbeam_2.XboxController;

/**
 *
 * @author Luc
 */
public class XboxDpadTest {
    private static XboxController controller = new XboxController();
    
    public static void main(String[] args){
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
            
            while(!controller.getButtonValue(6)){ //Pressing the back button kills loop.
                if(!controller.pollController()){
                    System.out.println("Controller disconnected!");
                    System.out.println("Reconnect controller and try again.");
                    System.exit(0);
                }
                
                //GIMBAL CONTROL
                if(controller.getXAxisPercentageLEFT() > 35 && controller.getXAxisPercentageLEFT() < 65){
                   // System.out.println("StopX");
                   // client.writeByte(STOPX);
                }
                else if(controller.getXAxisPercentageLEFT() >= 65 ){
                    System.out.println("setX max right");
                    //client.writeByte(SETX_MAX_RIGHT);
                }
                else if(controller.getXAxisPercentageLEFT() <= 35 ){
                    System.out.println("setX max left");
                    //client.writeByte(SETX_MAX_LEFT);
                }

                if(controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65){
                   // System.out.println("stop y");
                   // client.writeByte(STOPY);
                }
                else if(controller.getYAxisPercentageLEFT() >= 65 ){
                    System.out.println("setY max up");
                   // client.writeByte(SETY_MAX_UP);
                }
                else if(controller.getYAxisPercentageLEFT() <= 35 ){
                    System.out.println("setY max down");
                    //client.writeByte(SETY_MAX_DOWN);
                    
                }

                if(controller.isRightDPad()){
                    //client.writeByte(SPEED_UP_X);
                    System.out.println("RIGHT");
                }
                else if(controller.isLeftDPad()){
                    System.out.println("left");
                }
                else if(controller.isUpDPad()){
                    //client.writeByte(SPEED_UP_Y);
                }
                else if(controller.isDownDPad()){
                    //client.writeByte(SPEED_DOWN_Y);
                }
                
    }
    }}
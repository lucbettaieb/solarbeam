package solarbeam_3;

import java.util.logging.Level;
import java.util.logging.Logger;
import solarbeam_2.Communication;
import solarbeam_2.XboxController;

/**
 * @author Luc Bettaieb and Andrew Powell
 * 
 * Controls:
 * Left Stick: Top Gimbal
 * Right Stick: PanTilt
 * DPad X: Gimbal X Speed
 * DPad Y: Gimbal Y Speed
 * Start: Toggle Auto tracking
 * Back: Quit 
 */
public class DriverStation implements Commands {
    private static final int MODE = 0;
    private static final String SERVER_IP_ADDRESS  = "localhost";
    private static final String WIRELESS_SERVER_IP_ADDRESS = "169.254.101.15";
    private static final String LOCAL = "localhost";
    private static final int PORT = 6066;
    
    private static boolean AUTO_TRACKING = true;
    
    public static void main(String[] args){
        if(MODE > 0){
            //GUI MODE
        }
        else{
            try {
                //NO GUI, Xbox Controller only.
                nogui();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            
        }
    }
    
    public static void nogui() throws Throwable{
        XboxController controller = new XboxController();
        Communication client = null;
        
        try {
            System.out.println("Attempting to connect to server...");
            client = new Communication(SERVER_IP_ADDRESS, PORT);
            System.out.println("...done!");
            
            System.out.println("Solarbeam v3");
            
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
                    System.out.println("StopX");
                    client.writeByte(STOPX);
                }
                else if(controller.getXAxisPercentageLEFT() >= 65 ){
                    System.out.println("setX max right");
                    client.writeByte(SETX_MAX_RIGHT);
                }
                else if(controller.getXAxisPercentageLEFT() <= 35 ){
                    System.out.println("setX max left");
                    client.writeByte(SETX_MAX_LEFT);
                }

                if(controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65){
                    System.out.println("stop y");
                    client.writeByte(STOPY);
                }
                else if(controller.getYAxisPercentageLEFT() >= 65 ){
                    System.out.println("setY max up");
                    client.writeByte(SETY_MAX_UP);
                }
                else if(controller.getYAxisPercentageLEFT() <= 35 ){
                    System.out.println("setY max down");
                    client.writeByte(SETY_MAX_DOWN);
                    
                }

                if(controller.isRightDPad()){
                    client.writeByte(SPEED_UP_X);
                }
                else if(controller.isLeftDPad()){
                    client.writeByte(SPEED_DOWN_X);
                }
                else if(controller.isUpDPad()){
                    client.writeByte(SPEED_UP_Y);
                }
                else if(controller.isDownDPad()){
                    client.writeByte(SPEED_DOWN_Y);
                }
                
                
                //Pantilt Manual Control
                if(!AUTO_TRACKING){ //Can't control X if auto tracking.
                    if(controller.getXAxisPercentageRIGHT() > 35 && controller.getXAxisPercentageRIGHT() < 65){
                        client.writeByte(PT_STOPX);
                    }
                    else if(controller.getXAxisPercentageRIGHT() >= 65 ){
                        client.writeByte(PT_SETX_MAX_RIGHT);
                    }
                    else if(controller.getXAxisPercentageRIGHT() <= 35 ){
                        client.writeByte(PT_SETX_MAX_LEFT);
                    }
                }

                if(controller.getYAxisPercentageRIGHT() > 35 && controller.getYAxisPercentageRIGHT() < 65){
                    client.writeByte(PT_STOPY);
                }
                else if(controller.getYAxisPercentageRIGHT() >= 65 ){
                    client.writeByte(PT_SETY_MAX_UP);
                }
                else if(controller.getYAxisPercentageRIGHT() <= 35 ){
                    client.writeByte(PT_SETY_MAX_DOWN);
                }

                if(controller.getButtonValue(7)){ //Start Button
                    if(AUTO_TRACKING){
                        client.writeByte(PT_TOGGLE_AUTO);
                        AUTO_TRACKING = false;
                    }
                    else{
                        client.writeByte(PT_TOGGLE_AUTO);
                        AUTO_TRACKING = true;
                    }
                }
                
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if (client != null) {
                System.out.println("Deconstructing client");
                client.finalize(); client = null;
            }
            if (controller != null) {
                System.out.println("Deconstructing controller");
                //controller.finalize(); //TODO: See if necessary to fix.
                controller = null;
            }
        }
        
    }
    
}

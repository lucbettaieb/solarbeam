package solarbeam_3;

import java.io.IOException;
import solarbeam_2.Communication;

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
public class DriverStationKeyboardSupport implements Commands {
    private static final String SERVER_IP_ADDRESS  = "169.254.119.1";
    private static final String WIRELESS_SERVER_IP_ADDRESS = "169.254.101.15";
    private static final String LOCAL = "localhost";
    private static final int PORT = 6066;
    
    private static ControlContainer.Control controller = null;
    private static Communication client = null;
    private static boolean AUTO_TRACKING = false;
    
    
    private static void pln(String string) {
        System.out.println(string);
    }
    
    private static void peln(String string) {
        System.err.println(string);
    }
    
    private static ControlContainer.Control getController() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int input;

        input = scanner.nextInt();
        
        if (input == 0) {
            return new ControlContainer.XboxControl();
        } 
        return new ControlContainer.KeyboardControl();
    }
    
    private static Communication getCommunication() throws IOException {
         java.util.Scanner scanner = new java.util.Scanner(System.in);
        int input;

        input = scanner.nextInt();
        pln("Attempting to connect...");
        if (input == 0) {
            return new Communication(SERVER_IP_ADDRESS, PORT);
        } 
        return new Communication(WIRELESS_SERVER_IP_ADDRESS, PORT);
    }
    
    private static class TrackMovement {
        public boolean xIsMoving = false;
        public boolean yIsMoving = false;
        public TrackMovement () { 
        }
    }
    
    public static void main(String[] args) throws Throwable {
        TrackMovement gimbalMovement = new TrackMovement();
        TrackMovement panTiltMovement = new TrackMovement();
        try {
            pln("Type in 0 to connect to the cRIO's wired IP Address: " + SERVER_IP_ADDRESS);
            pln("Type any other number to connect to its wireless IP Address: " + WIRELESS_SERVER_IP_ADDRESS);
            client = getCommunication();
            pln("...done!");
            
            pln("Type in 0 for xbox controller or any other number for keyboard.\n" 
                + "Hit enter once selection has been made.");
            controller = getController();
            controller.setup();
            
            pln("Solarbeam v3");
            while (true) {
                if (controller.isDisconnected()) {
                    pln("Controller has been disconnected.");
                    client.writeByte(DISCONNECT);
                    break;
                }
                
                
                 if (controller.isStopXGimbal() && gimbalMovement.xIsMoving) {
                    client.writeByte(STOPX);
                    gimbalMovement.xIsMoving = false;
                } else if (controller.isSetXMaxRightGimbal() && !gimbalMovement.xIsMoving) {
                    client.writeByte(SETX_MAX_RIGHT);
                    gimbalMovement.xIsMoving = true;
                } else if (controller.isSetXMaxLeftGimbal() && !gimbalMovement.xIsMoving) {
                    client.writeByte(SETX_MAX_LEFT);
                    gimbalMovement.xIsMoving = true;
                }  

                if (controller.isStopYGimbal() && gimbalMovement.yIsMoving) {
                    client.writeByte(STOPY);
                    gimbalMovement.yIsMoving = false;
                } else if (controller.isSetYMaxUpGimbal() && !gimbalMovement.yIsMoving) {
                    client.writeByte(SETY_MAX_UP);
                    gimbalMovement.yIsMoving = true;
                } else if (controller.isSetYMaxDownGimbal() && !gimbalMovement.yIsMoving) {
                    System.out.println("Is loop for movement?");
                    client.writeByte(SETY_MAX_DOWN);
                    gimbalMovement.yIsMoving = true;
                }  

                if (controller.isSpeedXUpGimbal()) {
                    
                    client.writeByte(SPEED_UP_X);
                    
                } else if (controller.isSpeedXDownGimbal()) {
                    
                    client.writeByte(SPEED_DOWN_X);
                } 
                
                if (controller.isSpeedYUpGimbal() ) {
                    
                    client.writeByte(SPEED_UP_Y);
                    
                } else if (controller.isSPeedYDownGimbal()) {
                    
                    client.writeByte(SPEED_DOWN_Y);
                }                
                
               
                if (controller.isStopXPanTilt() && panTiltMovement.xIsMoving) {
                    client.writeByte(PT_STOPX);
                    panTiltMovement.xIsMoving = false;
                } else if (controller.isSetXMaxRightPanTilt() && !panTiltMovement.xIsMoving) {
                    client.writeByte(PT_SETX_MAX_RIGHT);
                    panTiltMovement.xIsMoving = true;
                } else if (controller.isSetXMaxLeftPanTilt() && !panTiltMovement.xIsMoving) {
                    client.writeByte(PT_SETX_MAX_LEFT);
                    panTiltMovement.xIsMoving = true;
                }
                
                
                if (controller.isStopYPanTilt() && panTiltMovement.yIsMoving) {
                    client.writeByte(PT_STOPY);
                    panTiltMovement.yIsMoving = false;
                } else if (controller.isSetYMaxUpPanTilt() && !panTiltMovement.yIsMoving) {
                    client.writeByte(PT_SETY_MAX_UP);
                    panTiltMovement.yIsMoving = true;
                } else if (controller.isSetYMaxDownPanTilt() && !panTiltMovement.yIsMoving) {
                    client.writeByte(PT_SETY_MAX_DOWN);
                    panTiltMovement.yIsMoving = true;
                }
                
                if (controller.isToggleAuto()) {
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
        } catch (IOException e) {
            if (client == null) {
                peln("client object was not instantiated!");
            }
            if (controller == null) {
                peln("controller object was not instantiated!");
            }
            e.printStackTrace();
        } finally {
            if (client != null) {
                pln("Deconstructing client");
                client.finalize(); client = null;
            }
            if (controller != null) {
                pln("Deconstructing controller");
                controller.finalize(); //TODO: See if necessary to fix.
                controller = null;
            }
            System.exit(0);
        }
    }
}

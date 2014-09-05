package solarbeam_3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import solarbeam_2.Communication;
import solarbeam_2.easydevices.EasyGimbal;
import solarbeam_2.easydevices.jni.LightSensor;

/**
 * @Author Luc Bettaieb and Andrew Powell
 * 
 * This class is intended to be run on the cRIO at startup.  It waits for commands via network
 * and performs functions on the tower accordingly.
 * 
 * Two threads, one for the top gimbal.  One for the bottom pan/tilt.
 * Each thread waits for its own commands from its respective "command sending" thread
 * from the DriverStation.
 */

public class Solarbeam_Local_Testing extends Thread implements Commands{
    public static final int PORT = 6066; //TODO: May have to change ports for different threads..?
    
    
    //PanTilt pantilt  = new PanTilt();
//    LightSensor left = new LightSensor(8,9), right = new LightSensor(10,11), back = new LightSensor(12,13);
                
                
    boolean tracking = true;
    
    public Solarbeam_Local_Testing(String str){
        super(str); //naming the thread for differentiating between the top gimbal and the bottom pantilt
    }
    
    public void primary() throws Throwable{ //GIMBAL and MANUAL PANTILT CONTROL
        //Top gimbal communication stuff
        
        EasyGimbal primary = null;
        Communication server = null;
        byte input;
        
        while(true){
            try {
                System.out.println("Setting up primary gimbal...");
                //primary = new EasyGimbal(0);
                System.out.println("...complete");

                System.out.println("Waiting for client...");
                server = new Communication(PORT);
                System.out.println("...complete");

                while(true) {
                    if(server.available() > 0) {
                        input = server.readByte();
                        System.out.println("Input: "+ input);

                        if(input == DISCONNECT) {
                            break;
                        }

                        //Switch statement for functions
                        switch(input){
                            case STOPX:
                                primary.stopX();
                                break;
                            case SETX_MAX_RIGHT:
                                primary.setXMaxRight();
                                break;
                            case SETX_MAX_LEFT:
                                primary.setXMaxLeft();
                                break;
                            case STOPY:
                                primary.stopY();
                                break;
                            case SETY_MAX_UP:
                                primary.setYMaxUp();
                                break;
                            case SPEED_UP_X:
                                primary.setSpeed(primary.getSpeed(1)+100,1);
                                server.writeLong(primary.getSpeed(1));
                                break;
                            case SPEED_DOWN_X:
                                primary.setSpeed(primary.getSpeed(1)-100,1);
                                server.writeLong(primary.getSpeed(1));
                                break;
                            case SPEED_UP_Y:
                                primary.setSpeed(primary.getSpeed(2)+100,2);
                                server.writeLong(primary.getSpeed(2));
                                break;
                            case SPEED_DOWN_Y:
                                primary.setSpeed(primary.getSpeed(2)-100,2);
                                server.writeLong(primary.getSpeed(2));
                                break;
                                
                                
                            case PT_STOPX:
                                System.out.println("panilt stop x");
                                
                                break;
                            case PT_SETX_MAX_RIGHT:
                                System.out.println("pantilt setmax right");
                                
                                break;
                            case PT_SETX_MAX_LEFT:
                                System.out.println("pantilt setmax left");
                                
                                break;
                            case PT_STOPY:
                                System.out.println("pantilt stop y");
                                
                                break;
                            case PT_SETY_MAX_UP:
                                System.out.println("pantilt setmax up");
                                
                                break;
                            case PT_SETY_MAX_DOWN:
                                System.out.println("pantilt setmax down");
                                
                                break;
                            case PT_TOGGLE_AUTO:
                                if(tracking == true){
                                    tracking = false;
                                }else if(tracking == false){
                                    tracking = true;
                                }
                                break;
                        }
                    }
                }
            } catch(java.io.IOException e){
                if (server == null){
                    System.err.println("Error initializing server.");
                } else if(primary == null){
                    System.err.println("Error initializing gimbal.");
                    System.out.println("Waiting 5 seconds before reattempt.");
                    Thread.sleep(5000);
                }
            } finally {
                if(server != null){
                    System.out.println("Deconstructing server.");
                    server.finalize();
                    server = null;
                } if(primary != null){
                    System.out.println("Deconstruting gimbal.");
                    primary.finalize();
                    primary = null;
                }
            }
        }
    }
    
    public void secondary() throws Throwable{ //AUTONOMOUS TRACKING
        
        while(true){
            try { 
              
                while(true){
                    
                    if(tracking){
                       // track();
                        //THIS WILL NEED TO BE TESTED THOROUGHLY
                    }

                        
                    
                }
                
            } catch(Exception e){
               
            } finally{
                
            }
        
        
        
        } 
    }
    public void track(){
        System.out.println("tracking");
    }

    public void run(){
        if(this.getName().equals("primary")){
            try {
                primary();
            } catch (Throwable ex) {
                System.err.println("Error with Primary");
            }
        }
        else if(this.getName().equals("secondary")){
            try {
                secondary();
            } catch (Throwable ex) {
                System.err.println("Error with Secondary");
            }
        }
    }
   
    public static void main(String[] args){
        System.out.println("Welcome to Solarbeam v3");
        
        System.out.println("Starting primary control thread...");
        
        Solarbeam_Local_Testing primary = new Solarbeam_Local_Testing("primary");
        
        System.out.println("...complete.");
       
        System.out.println("Starting secondary control thread...");
        Solarbeam_Local_Testing secondary = new Solarbeam_Local_Testing("secondary");
        //thread for pantilt
        
        primary.start();
        secondary.start();
        
        
    }
}
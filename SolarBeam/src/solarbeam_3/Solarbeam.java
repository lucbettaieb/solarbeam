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

public class Solarbeam extends Thread implements Commands{
    public static final int PORT = 6066; //TODO: May have to change ports for different threads..?
    
    
    PanTilt pantilt  = new PanTilt();
    LightSensor left = new LightSensor(8,9), right = new LightSensor(10,11), back = new LightSensor(12,13);
                
                
    boolean tracking = true;
    
    public Solarbeam(String str){
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
                primary = new EasyGimbal(0);
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
                                pantilt.stopX();
                                break;
                            case PT_SETX_MAX_RIGHT:
                                pantilt.setMaxPanRight();
                                break;
                            case PT_SETX_MAX_LEFT:
                                pantilt.setMaxPanLeft();
                                break;
                            case PT_STOPY:
                                pantilt.stopY();
                                break;
                            case PT_SETY_MAX_UP:
                                pantilt.setMaxTiltUp();
                                break;
                            case PT_SETY_MAX_DOWN:
                                pantilt.setMaxTiltDown();
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
                left.powerOn();
                right.powerOn();
                while(true){
                    
                    if(tracking){
                        track(pantilt, left, right, back);
                        //THIS WILL NEED TO BE TESTED THOROUGHLY
                    }

                        
                    
                }
                
            } catch(Exception e){
               if(left == null || right == null || back == null){
                    System.err.println("Error initializing a light sensor");
               } if(pantilt == null){
                    System.err.println("Error initializing the PanTilt");
               }
            } finally{
                if (left != null){
                    System.out.println("Deconstructing left light sensor");
                    left.finalize(); left = null;
                }
                if (right != null){
                    System.out.println("Deconstructing right light sensor");
                    right.finalize(); left = null;
                }
                if (back != null){
                    System.out.println("Deconstructing back light sensor");
                    back.finalize(); left = null;
                }
                if (pantilt != null){
                    System.out.println("Deconstructing left light sensor");
                    pantilt.finalize(); pantilt = null;
                }
                
            }
        
        
        
        } 
    }
    public double track(PanTilt pt, LightSensor L, LightSensor R, LightSensor back){
        int ambient = back.read();
        int lef = L.read() - ambient;
        int righ = R.read() - ambient;
        
        double x; //Tracking factor, its goal is to become 0.
        
        x = (lef-righ); //Get factor
        x /= 10000; //Get fraction
        x *= 100; //Get percentage
        
        if(Math.abs(x) >= 0 && Math.abs(x) <=1){ //Base Case
            System.out.println("On target.");
            return x;
        }
        
        if(x >= 0){ //If factor is positive, increase right and decrease left.
            if(Math.abs(x) >= 1 && Math.abs(x) < 12){
                System.out.println("T1");
                pt.setPanDeg(pt.getPanPos() - 5); //go right (NEGATIVE)
            } 
            else if(Math.abs(x) >= 12 &&  Math.abs(x) < 31){
                System.out.println("T2");
                pt.setPanDeg(pt.getPanPos() - 10);
            }
            else if(Math.abs(x) >= 31 &&  Math.abs(x) < 61){ //this
                System.out.println("T3");
                pt.setPanDeg(pt.getPanPos() - 20);
            }
            else if(Math.abs(x) >= 61 &&  Math.abs(x) < 81){
                System.out.println("T4");
                pt.setPanDeg(pt.getPanPos() - 40);
            }
            else if(Math.abs(x) >= 81 &&  Math.abs(x) <= 100){
                System.out.println("T5");
                pt.setPanDeg(pt.getPanPos() - 80);
            }
        }
        else { //If negative, increase left and decrease right.
            if(Math.abs(x) >= 1 && Math.abs(x) < 12){
                System.out.println("T1");
                pt.setPanDeg(pt.getPanPos() + 5);
            } 
            else if(Math.abs(x) >= 12 &&  Math.abs(x) < 31){
                System.out.println("T2");
                pt.setPanDeg(pt.getPanPos() + 10);
            }
            else if(Math.abs(x) >= 31 &&  Math.abs(x) < 61){
                System.out.println("T3");
                pt.setPanDeg(pt.getPanPos() + 20);
            }
            else if(Math.abs(x) >= 61 &&  Math.abs(x) < 81){
                System.out.println("T4");
                pt.setPanDeg(pt.getPanPos() + 40);
            }
            else if(Math.abs(x) >= 81 &&  Math.abs(x) <= 100){
                System.out.println("T5");
                pt.setPanDeg(pt.getPanPos() + 80);
            }
        }
        
        return track(pt, L, R, back);
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
        
        Solarbeam primary = new Solarbeam("primary");
        
        System.out.println("...complete.");
       
        System.out.println("Starting secondary control thread...");
        Solarbeam secondary = new Solarbeam("secondary");
        //thread for pantilt
        
        primary.start();
        secondary.start();
        
        
    }
}
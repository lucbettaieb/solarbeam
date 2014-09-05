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

public class SolarbeamServer implements Commands{
    public static final int PORT = 6066; //TODO: May have to change ports for different threads..?
    
    private static EasyGimbal primary = null;
    private static Communication server = null;
    private static PanTilt pantilt = null;
    private static LightSensor left = null;
    private static LightSensor right = null;
    private static LightSensor back = null;
    private static Primary primaryThread = null;
    private static Secondary secondaryThread = null;
    
    private static boolean tracking = false;
    
    private static void pln(String string) { System.out.println(string); }
    private static void peln(String string) { System.err.println(string); }

    private static void interruptThreads() { primaryThread.interrupt(); secondaryThread.interrupt(); }
    
    public static class Primary extends Thread {
        public Primary() {
        }
        
        @Override public void run() {
            try {
                primary();
            } catch (Throwable e) { }
        }

        private void primary() throws Throwable { //GIMBAL and MANUAL PANTILT CONTROL
            //Top gimbal communication stuff
            byte input;
            int pty_dydegrees = 180;
            int pty_change = 0;
            int ptx_dydegrees = 180;
            int ptx_change = 0;

            pln("Beginning primary thread");

            try {
                while(true) {
                    if (this.isInterrupted()) {
                        break;
                    }
                    
//                    if (pty_change != 0) {
//                        pty_dydegrees += pty_change;
//                        pantilt.setTiltDeg(pty_dydegrees);
//                        Thread.sleep(100);
//                    } else {
//                        pantilt.setTiltDeg(pty_dydegrees);
//                    }
//                    
//                    if (tracking) {
//                    }
//                    else if (ptx_change != 0) {
//                        ptx_dydegrees += ptx_change;
//                        pantilt.setPanDeg(ptx_dydegrees);
//                        Thread.sleep(100);
//                    } else {
//                        pantilt.setPanDeg(ptx_dydegrees);
//                    }

                    if(server.available() > 0) {
                        input = server.readByte();
                        pln("Input: "+ input);

                        if(input == DISCONNECT) {
                            pln("Primary thread disconnected");
                            break;
                        }

                        //Switch statement for functions
                        switch(input) {
                            case STOPX:
                                pln("stop x gimbal");
                                primary.stopX();
                                break;
                            case SETX_MAX_RIGHT:
                                pln("Set max right x gimbal");
                                primary.setXMaxRight();
                                break;
                            case SETX_MAX_LEFT:
                                pln("set x max left gimbal");
                                primary.setXMaxLeft();
                                break;
                            case STOPY:
                                pln("stop y gimbal");
                                primary.stopY();
                                break;
                            case SETY_MAX_UP:
                                pln("set y max up gimbal");
                                primary.setYMaxUp();
                                break;
                            case SETY_MAX_DOWN:
                                pln("set y max down gimbal");
                                primary.setYMaxDown();
                                break;
                            case SPEED_UP_X:
                                pln("speed up x gimbal: " + primary.getSpeed(1));
                                primary.setSpeed(primary.getSpeed(1)+100,1);
                                break;
                            case SPEED_DOWN_X:
                                pln("speed down x gimbal: " + primary.getSpeed(1));
                                primary.setSpeed(primary.getSpeed(1)-100,1);
                                break;
                            case SPEED_UP_Y:
                                pln("speed up y gimbal: " + primary.getSpeed(2));
                                primary.setSpeed(primary.getSpeed(2)+100,2);
                                break;
                            case SPEED_DOWN_Y:
                                pln("speed down y gimbal: " + primary.getSpeed(2));
                                primary.setSpeed(primary.getSpeed(2)-100,2);
                                break;
                            case PT_STOPX:
                                if (tracking) { break; }
                                pln("stop x pantilt");
                                pantilt.stopX();
                                //ptx_change = 0;
                                break;
                            case PT_SETX_MAX_RIGHT:
                                if (tracking) { break; }
                                pln("set x max right pantilt");
                                pantilt.setMaxPanRight();
                                //ptx_change = 10;
                                break;
                            case PT_SETX_MAX_LEFT:
                                if (tracking) { break; }
                                pln("set x max left pantilt");
                                pantilt.setMaxPanLeft();
                                //ptx_change = -10;
                                break;
                            case PT_STOPY:
                                pln("stop y pantilt");
                                pantilt.stopY();
                                //pty_change = 0;
                                break;
                            case PT_SETY_MAX_UP:
                                pln("set y max up");
                                //pty_change = 10;
                                pantilt.setMaxTiltUp();
                                break;
                            case PT_SETY_MAX_DOWN:
                                pln("set y max down");
                                //pty_change -= 10;
                                pantilt.setMaxTiltDown();
                                break;
                            case PT_TOGGLE_AUTO:
                                if(tracking == true){
                                    tracking = false;
                                }else if(tracking == false){
                                    tracking = true;
                                }
                                pln("auto has been toggled: " + tracking);
                                break;
                        }
                    }
                }
            } finally {
                pln("Primary thread has been broken.");
                interruptThreads();
            }
        }
    }
    
    public static class Secondary extends Thread {
        public Secondary() {
        }
        
        @Override public void run() {
            try {
                secondary();
            } catch (Throwable e) { e.printStackTrace(); }
        }
    
        private void secondary() throws Throwable { //AUTONOMOUS TRACKING
            pln("Beginning secondary thread.");

            try { //IF THERE ARE PROBLEMS PERHAPS MOVE THIS INITIALIZATION CODE TO THE BEGINNING
                System.out.println("Setting up light sensors...");
                left = new LightSensor(8, 9); //LEFT
                right = new LightSensor(10, 11); //RIGHT
                back = new LightSensor(12, 13); //BACK
                System.out.println("...complete");

                while(true){
                    if (this.isInterrupted()) {
                        pln("Secondary thread disconnected.");
                        break;
                    }
                    if(tracking) {
                        Thread.sleep(100);
                        track(pantilt, left, right, back);
                        //THIS WILL NEED TO BE TESTED THOROUGHLY
                    }
                }  
            } catch(Exception e) {
               if(left == null || right == null || back == null){
                    System.err.println("Error initializing a light sensor");
               } if(pantilt == null){
                    System.err.println("Error initializing the PanTilt");
               }
            } finally {
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
                pln("Secondary thread has been broken.");
                interruptThreads();
            }
        }

        public static double track(PanTilt pt, LightSensor L, LightSensor R, LightSensor back){
            if (!tracking) // Andrew Powell: I added this line to shutdown the autonomy immediately if disabled 
                return 0;
            
            int ambient = back.read();
            int lef = L.read() - ambient;
            int righ = R.read() - ambient;

            double x; //Tracking factor, its goal is to become 0.

            x = (lef-righ); //Get factor
            x /= 10000; //Get fraction
            x *= 100; //Get percentage

            System.out.println("Left Read: " + lef + "\tRight Read: " + righ + "\tX: " + x);

            if(Math.abs(x) >= 0 && Math.abs(x) <=2.5){ //Base Case
                System.out.println("On target.");
                return x;
            }
    //        if(pt.getPanPos() > 55 && pt.getPanPos() < 65){
    //            pt.setPanDeg(pt.getPanPos()+30);
    //            
    //        }
    //        if(pt.getPanPos() < 225 && pt.getPanPos() < 235){
    //            pt.setPanDeg(pt.getPanPos()-30);
    //        }
            //System.out.println("Left Read: " + lef + "\tRight Read: " + righ);

            if(x >= 0){ //If factor is positive, increase right and decrease left.
                if(Math.abs(x) >= 2.6 && Math.abs(x) < 12){
                    System.out.println("T1");
                    pt.setPanDeg(pt.getPanPos() - 3); //go right (NEGATIVE)
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
    }
   
    public static void main(String[] args) throws Throwable {
        while (true) {
            try {
                System.out.println("Welcome to Solarbeam v3");
                    
                System.out.println("Waiting for client...");
                server = new Communication(PORT);
                System.out.println("...complete");
                
                System.out.println("Setting up pantilt...");
                pantilt = new PanTilt();
                System.out.println("...complete");
                
                System.out.println("Setting up primary gimbal...");
                primary = new EasyGimbal(0);
                System.out.println("...complete");

                System.out.println("Starting primary control thread...");
                primaryThread = new Primary();
                primaryThread.start();
                System.out.println("...complete.");
                
                //thread for pantilt
                System.out.println("Starting secondary control thread...");
                secondaryThread = new Secondary();
                secondaryThread.start();
                System.out.println("...complete.");
                
                // until the loops exits
                primaryThread.join();
                secondaryThread.join();
                
                
            } catch (java.io.IOException e) {
                if (server == null) {
                    peln("Server object didn't instantiate.");
                }
                if (primary == null) {
                    peln("Gimbal object didn't instantiate.");
                    peln("Waiting 5 seconds before reattempt.");
                    Thread.sleep(5000);
                }
            } finally {
                if (server != null) {
                    pln("Deconstructing server");
                    server.finalize(); server = null; 
                }
                if (primary != null) {
                    pln("Deconstructing Gimbal");
                    primary.finalize(); primary = null;
                }
                if (pantilt != null) {
                    pln("Deconstructing Pan and Tilt");
                    pantilt.finalize(); pantilt = null;
                }
            }
        }
    }
}
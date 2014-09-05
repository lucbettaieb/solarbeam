/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import solarbeam_2.Communication;
import solarbeam_2.easydevices.EasyGimbal;

/**
 *
 * @author Andrew Powell
 */
public class Main2_SolarBeamServer implements Main2_Commands {
    public static final int PORT = 6066;
    
    public static void print(String str) { System.out.println(str); }
    public static void printe(String str) { System.err.println(str); }
    
    public static void main(String[] args) throws Throwable {
        EasyGimbal primary = null;
        Communication server = null;
        byte input;
        
        while (true) {
            try {
                // set up the Gimbal
                print("Setting up Gimbal");
                primary = new EasyGimbal(0);
                print("Gimbal is now ready");

                // continuously try to reach out to the client
                print("Waiting for client");
                server = new Communication(PORT);
                print("Connection with client has been made");

                // main loop
                while (true) {
                    // check if data is available
                    if (server.available() > 0) {
                        //receive data
                        input = server.readByte();
                        print("Received input: " + input);

                        // perform function
                        if (input == DISCONNECT) {
                            break;
                        }
                        switch (input)  {
                            case STOPX:
                                primary.stopX();
                                print("stop x");
                                break;
                            case SETX_MAX_RIGHT:
                                primary.setXMaxRight();
                                print("going right");
                                break;
                            case SETX_MAX_LEFT:
                                primary.setXMaxLeft();
                                print("going left");
                                break;
                            case STOPY:
                                primary.stopY();
                                print("stop y");
                                break;
                            case SETY_MAX_UP:
                                primary.setYMaxUp();
                                print("going up");
                                break;
                            case SETY_MAX_DOWN:
                                primary.setYMaxDown();
                                print("going down");
                                break;
                            case SPEED_UP:
                                primary.setSpeed(primary.getSpeed(1)+100,1);
                                server.writeLong(primary.getSpeed(1));
                                break;
                            case SPEED_DOWN:
                                primary.setSpeed(primary.getSpeed(1)-100,1);
                                server.writeLong(primary.getSpeed(1));
                                break;
                        }  
                    }
                }
            } catch (java.io.IOException e) {
                if (server == null) {
                    printe("Error wih server");
                }
                if (primary == null) {
                    printe("Error with Gimbal");
                    print("Waiting 5 seconds before reattempt");
                    Thread.sleep(5000);
                }
            } finally {
                if (server != null) {
                    print("Deconstructing server");
                    server.finalize(); server = null; 
                }
                if (primary != null) {
                    print("Deconstructing Gimbal");
                    primary.finalize(); primary = null;
                }
            }
        }
    }  
}

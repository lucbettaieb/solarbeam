package solarbeam_testing;

import solarbeam_2.easydevices.EasyGimbal;
import solarbeam_2.Communication;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 *
 * @author Luc
 */
public class MainSolarBeamServer_1 extends Main1_SolarBeamBase { 
    
    public static final int PORT = 6066;
    
    public static void print(String str) { System.out.println(str); }
    
    public static void main(String[] args) throws IOException, SocketTimeoutException {

        // declare the data
        EasyGimbal primary;
        int input;
        
        // continuously try to reach out to the client
        print("Attempting to connect to port");
        Communication server = new Communication(PORT);
        print("Port is connected");
        
        // create the data
        print("Setting up the Gimbal");
        primary = new EasyGimbal(1);
        print("Gimbal is ready");
        
        // main loop
        while(true){
            
            // check if data is available
            if (server.available() > 0) {  

                // receive data
                input = server.readInt();

                // print received data
                print("Input: " + input);

                if(input == M.finalize.get()) {
                    System.out.println("Program shutting down");
                    break;
                }

                if (input == M.stopX.get()) {
                    primary.stopX();
                } else if(input == M.setXMaxRight.get()){
                    primary.setXMaxRight();
                }
                else if(input == M.setXMaxLeft.get()){
                    primary.setXMaxLeft();
                }

                if(input == M.stopY.get()) {
                    primary.stopY();
                }
                else if(input == M.setYMaxUp.get()){
                    primary.setYMaxUp();
                }
                else if(input == M.setYMaxDown.get()){
                    primary.setYMaxDown();
                }

                if(input == M.speedUpX.get()){
                    System.out.println("speed up");
                    primary.setSpeed(primary.getSpeed(1)+100,1);
                    server.writeLong(primary.getSpeed(1));
                }
                if(input == M.speedDownX.get()){
                    System.out.println("speed down");
                    primary.setSpeed(primary.getSpeed(1)-100,1);
                    server.writeLong(primary.getSpeed(1));
                }
            }
        }
        
        // let the client know the server has been finalized
        server.writeInt(M.handshake.get());
        
        // finish program
        System.out.println("Data finalizing...");
        try {
            primary.finalize();
            server.finalize();
            //secondary.finalize();
            System.out.println("..finalized!");
        } catch(Throwable ex){
            System.err.println("Error finalizing data.");
        }
        System.out.println("System exiting."); 
        
        // end program
        System.exit(0);
    } 
}

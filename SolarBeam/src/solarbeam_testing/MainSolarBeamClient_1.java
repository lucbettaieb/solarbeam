package solarbeam_testing;

import solarbeam_2.XboxController;
import solarbeam_2.Communication;
import java.io.IOException;

/**
 *
 * @author Luc
 */
public class MainSolarBeamClient_1 extends Main1_SolarBeamBase { 
    
    public static final String SERVER_IP_ADDRESS = "169.254.119.1";
    public static final int PORT = 6066;
    
    public static void main(String[] args) throws IOException {
        
        // declare the data
        XboxController controller;
        Communication client;
        
        // create controller object
        controller = new XboxController();
        
        // check for controller
        if( !controller.isControllerConencted() ){
            System.out.println("No controller found!");
            System.exit(0);
        }
        if( !controller.pollController() ) {
            System.out.println("Controller disconnected!");
            System.exit(0);
        }
        
        // attempt to form a connetion
        client = new Communication(SERVER_IP_ADDRESS, PORT);
        
        // main loop
        while(!controller.getButtonValue(6)){
            if( !controller.pollController() ) {
                client.writeInt(M.finalize.i);
                System.out.println("Controller disconnected!");
                System.exit(0);
            }
            //System.out.println(controller.getXAxisPercentageLEFT());
            if(controller.getXAxisPercentageLEFT() > 35 && controller.getXAxisPercentageLEFT() < 65){
                client.writeInt(M.stopX.i);
            }
            
            else if(controller.getXAxisPercentageLEFT() >= 65 ) {
                client.writeInt(M.setXMaxRight.i);
            }
            
            else if(controller.getXAxisPercentageLEFT() <= 35 ) {
                client.writeInt(M.setXMaxLeft.i);
            }
            
            if(controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65) {
                client.writeInt(M.stopY.i);
            }
            else if(controller.getYAxisPercentageLEFT() >= 65 ){
                client.writeInt(M.setYMaxUp.i);
            }
            else if(controller.getYAxisPercentageLEFT() <= 35 ){
                client.writeInt(M.setYMaxDown.i);
            }
            
            if(controller.getButtonValue(5)){
                System.out.println("speed up");
                client.writeInt(M.speedUpX.i);
                System.out.println(client.readLong());
            }
            else if(controller.getButtonValue(4)){
                System.out.println("speed up");
                client.writeInt(M.speedDownX.i);
                System.out.println(client.readLong());
            }
        }
        
        // exit test program
        System.out.println("Gimbals finalizing...");
            client.writeInt(M.finalize.i);
            System.out.println("System exiting.");
            System.exit(0);
        
    }
    
}

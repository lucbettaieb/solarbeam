/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import java.io.IOException;
import java.util.Scanner;
import solarbeam_2.Communication;
import solarbeam_2.XboxController;

/**
 
 * @author Andrew Powell
 */
public class MainSolarBeamClientKeyboard_1 extends Main1_SolarBeamBase {
   
    public static final String SERVER_IP_ADDRESS = "169.254.119.1";
    public static final int PORT = 6066;
    
    public static void print(String str) { System.out.println(str); }
    
    public static void main(String[] args) throws IOException, Throwable {
        
        // declare the data
        Scanner scanner;
        Communication client;
        int input;
       
        // attempt to form a connetion
        client = new Communication(SERVER_IP_ADDRESS, PORT);
        
        // setup scanner
        scanner = new Scanner(System.in);
        
        // main loop
        while(true){
            input = scanner.nextInt();
            
            if(input == Main1_SolarBeamBase.M.finalize.get()) {
                client.writeInt(Main1_SolarBeamBase.M.finalize.get());
                break;
            }
            //System.out.println(controller.getXAxisPercentageLEFT());
            if(input == M.stopX.get()){
                client.writeInt(M.stopX.get());
            }
            
            else if(input == M.setXMaxRight.get()) {
                client.writeInt(M.setXMaxRight.get());
            }
            
            else if(input == M.setXMaxLeft.get()) {
                client.writeInt(M.setXMaxLeft.get());
            }
            
            if(input == M.stopY.get()) {
                client.writeInt(M.stopY.get());
            }
            else if(input == M.setYMaxUp.get()){
                client.writeInt(M.setYMaxUp.get());
            }
            else if(input == M.setYMaxDown.get()){
                client.writeInt(M.setYMaxDown.get());
            }
            
            if(input == M.speedUpX.get()){
                System.out.println("speed up");
                client.writeInt(M.speedUpX.get());
                System.out.println(client.readLong());
            }
            else if(input == M.speedDownX.get()){
                System.out.println("speed up");
                client.writeInt(M.speedDownX.get());
                System.out.println(client.readLong());
            }
        }
        
        // exit test program
        print("Waiting for server to finalize");
        while (client.available() == 0 || client.readInt() != M.handshake.get()) { continue; }
        
        // finalize client
        client.finalize();
    }
    
}

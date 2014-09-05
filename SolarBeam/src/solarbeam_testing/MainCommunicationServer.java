/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import java.io.IOException;
import java.net.SocketTimeoutException;
import solarbeam_2.Communication;
/**
 *
 * @author Andrew Powell
 */
public class MainCommunicationServer {
    public static void main(String[] arg) {
        
        // get the ip address and port
         int port = 6066;
         
         // set up the client
         try {
            Communication server = new Communication(port);
            
            // create the data
            String data = "Well, I'm the server!\n";
            byte[] data2 = new byte[data.length()];
            
            // receive data
            while (server.available() == 0) { 
                continue;
            }
            server.read(data2);
            
            // print response
            System.out.println(new String(data2, "UTF8"));
            
            // send the data
            server.write(data.getBytes());
            
            try {
                server.finalize();
            } catch (Throwable e) {
                throw e;
            }
         } catch (SocketTimeoutException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (Throwable e) {
             e.printStackTrace();
         }
    }
}

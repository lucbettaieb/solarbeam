/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import solarbeam_2.Communication;
import java.io.*;
import java.net.SocketTimeoutException;

/**
 *
 * @author Andrew Powell
 */
public class MainCommunicationClient {
     final static int BUFFER_SIZE = 4;
    
     public static void main(String[] arg) {
         
        // get the ip address and port
         String serverIPAddress = "169.254.119.1";
         int port = 6066;
         
         // set up the client
         try {
            Communication client = new Communication(serverIPAddress, port);
            
            // create the data
            String data = "Hello world! I am the client!\n";
            byte[] data2 = new byte[20];

            // send the data
            client.write(data.getBytes());
            
            // receive data
            while (client.available() == 0) {
                continue;
            }
            client.read(data2);
            System.out.println(new String(data2,"UTF8"));

            try {
                client.finalize();
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

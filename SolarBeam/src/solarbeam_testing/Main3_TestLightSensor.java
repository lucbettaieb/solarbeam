/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;
import solarbeam_2.easydevices.jni.LightSensor;
/**
 *
 * @author Andrew Powell
 */
public class Main3_TestLightSensor {
    int sdapin = 14;
    int sclpin = 29;
    
    public static void main(String[] argc) throws Throwable {
        LightSensor ls = null;
        LightSensor ls2 = null;
    
        try {
        // create new light sensor objecys
        ls = new LightSensor(14, 29);
        ls2 = new LightSensor(28, 13);
        
        // continuously read stuff from light sensor
        while (true) {
            System.out.println("Reading ls1: " + ls.read() + "\tls2: " + ls2.read());
        }
        
        } finally {
            // kill stuff
            if (ls != null) {
                ls.finalize(); ls = null;
            }
            if (ls2 != null) {
                ls2.finalize(); ls2 = null;
            }
        }
    }
    
}

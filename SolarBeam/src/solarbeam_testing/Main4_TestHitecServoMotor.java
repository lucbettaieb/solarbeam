/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import solarbeam_2.easydevices.jni.LightSensor;
import solarbeam_2.easydevices.jni.HitecServoMotor;
/**
 *
 * @author Andrew Powell
 */
public class Main4_TestHitecServoMotor {
    public static void main(String args[])throws Throwable{
        HitecServoMotor[] hsm = new HitecServoMotor[2];
        HitecServoMotor selectHsm = null;
        String input = null;
        BufferedReader br = null; 
        
        try {
            hsm[0] = new HitecServoMotor((short)0, 180);
            hsm[1] = new HitecServoMotor((short)1, 180);
            hsm[0].enable(true);
            hsm[1].enable(true);
            selectHsm = hsm[0];
            br = new BufferedReader(new InputStreamReader(System.in));
            
            while (true) {
                input = br.readLine();
                if (input.equals("quit")) {
                    break;
                }
                
                if (input.equals("switch")) {
                    if (selectHsm == hsm[0]) {
                        selectHsm = hsm[1];
                    } else {
                        selectHsm = hsm[0];
                    }
                    continue;
                }

                if (input.equals("s")) {
                    selectHsm.enable(false);
                    continue;
                }
                
                selectHsm.setPositionDegrees(Integer.parseInt(input));
                
            }
            
        } finally {
            for (int i = 0; i < 2; i++) {
                if (hsm[i] != null) {
                    hsm[i].finalize(); hsm[i] = null;
                }
            }
        }   
    }
}

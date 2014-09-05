/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import solarbeam_2.easydevices.jni.LightSensor;
import solarbeam_2.easydevices.jni.HitecServoMotor;
import solarbeam_3.PanTilt;
/**
 *
 * @author Andrew Powell
 */
public class Main5_TestEverything {
    public static void main(String argsp[])throws Throwable{
        LightSensor[] ls = new LightSensor[3];
        HitecServoMotor[] hsm = new HitecServoMotor[2];
        HitecServoMotor selectHsm = null;
        String input = null;
        BufferedReader br = null; 
        
        try {
            ls[0] = new LightSensor(8, 9);
            ls[1] = new LightSensor(10, 11);
            ls[2] = new LightSensor(12, 13);
            hsm[0] = new HitecServoMotor((short)0, 180, 400);
            hsm[1] = new HitecServoMotor((short)1, 180);
            PanTilt pt = new PanTilt(hsm[0], hsm[1]);
            hsm[0].enable(true);
            hsm[1].enable(true);
            selectHsm = hsm[1];
            br = new BufferedReader(new InputStreamReader(System.in));
            
            while (true) {
                input = br.readLine();
                
                for (byte i = 0; i < 3; i++) {
                    System.out.print("ls" + i + ": " + ls[i].read() + "\t");
                }
                System.out.println();
                
                if (input.equals("q")) {
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
                
                if (input.equals("t")) {
                    track(pt, ls[0], ls[1], ls[2]);
                    continue;
                }
                
                selectHsm.setPositionDegrees(Integer.parseInt(input));
            }
            
        } finally {
            for (int i = 0; i < 3; i++) {
                if (ls[i] != null) { 
                    ls[i].finalize(); ls[i] = null; 
                }
            }
            for (int i = 0; i < 2; i++) {
                if (hsm[i] != null) {
                    hsm[i].finalize(); hsm[i] = null;
                }
            }
        }   
    }
    
    public static double track(PanTilt pt, LightSensor L, LightSensor R, LightSensor back){
        int ambient = back.read();
        int lef = L.read() - ambient;
        int righ = R.read() - ambient;
        
        double x; //Tracking factor, its goal is to become 0.
        
        x = (lef-righ); //Get factor
        x /= 10000; //Get fraction
        x *= 100; //Get percentage
        
        System.out.println("Left Read: " + lef + "\tRight Read: " + righ + "\tX: " + x);
        
        if(Math.abs(x) >= 0 && Math.abs(x) <=1){ //Base Case
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
}

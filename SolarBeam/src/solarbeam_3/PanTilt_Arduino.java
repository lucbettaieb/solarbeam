package solarbeam_3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import solarbeam_2.easydevices.jni.HitecServoMotor;
import solarbeam_2.easydevices.jni.HitecServoMotor_Arduino;

/**
 * @author Luc Bettaieb
 */
public class PanTilt_Arduino {
    private static final int MAX = 360;
    private HitecServoMotor_Arduino pan;
    private HitecServoMotor_Arduino tilt;
    
        
    
    
    public PanTilt_Arduino(){
        try {
            tilt = new HitecServoMotor_Arduino(3,'T');
        } catch (IOException ex) {
            Logger.getLogger(PanTilt.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            pan = new HitecServoMotor_Arduino(3,'P');
        } catch (IOException ex) {
            Logger.getLogger(PanTilt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pan.enable(true);
        tilt.enable(true);
        
        pan.setPositionDegrees(0);
        tilt.setPositionDegrees(0);
    }
    
    public void setPanDeg(int deg){
        pan.enable(true);
        pan.setPositionDegrees(deg);
    }
    
    public void setTiltDeg(int deg){
        tilt.enable(true);
        tilt.setPositionDegrees(deg);
    }
    
//    public int getPanPos(){
//        pan.enable(true);
//        return pan.getPosition();
//    }
//    
//    public int getTiltPos(){
//        tilt.enable(true);
//        return tilt.getPosition();
//    }
    
    public void stopX(){
        pan.enable(false);
    }
    
    public void stopY(){
        tilt.enable(false);
    }
    
    //Max Setters for Manual Control
    
    public void setMaxPanLeft(){
        pan.enable(true);
        setPanDeg(0);
    }
    
    public void setMaxPanRight(){
        pan.enable(true);
        setPanDeg(MAX);
    }
    
    public void setMaxTiltUp(){
        tilt.enable(true);
        setTiltDeg(MAX);
    }
    
    public void setMaxTiltDown(){
        tilt.enable(true);
        setTiltDeg(0);
    }
    
    public void finalize() throws Throwable{
        pan.finalize();
        tilt.finalize();
    }
    
    
    
}

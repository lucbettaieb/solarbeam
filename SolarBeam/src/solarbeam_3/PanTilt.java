package solarbeam_3;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import solarbeam_2.easydevices.jni.HitecServoMotor;
import solarbeam_2.easydevices.jni.HitecServoMotor_Arduino;

/**
 * @author Luc Bettaieb
 */
public class PanTilt {
    private static final int MAX = 360;
    private HitecServoMotor pan;
    private HitecServoMotor tilt;
    
        
    public PanTilt(HitecServoMotor tilt, HitecServoMotor pan) {
        this.tilt = tilt;
        this.pan = pan;
        pan.enable(true);
        tilt.enable(true);
    }
    
    public PanTilt(){
        tilt = new HitecServoMotor((short)0,180, 400);
        pan = new HitecServoMotor((short)1,180);
        
        pan.enable(true);
        tilt.enable(true);
        
  
    }
    
    public void setPanDeg(int deg){
        pan.enable(true);
        pan.setPositionDegrees(deg);
    }
    
    public void setTiltDeg(int deg){
        tilt.enable(true);
        tilt.setPositionDegrees(deg);
    }
    
    public int getPanPos(){
        pan.enable(true);
        return pan.getPositionDegrees();
    }
    
    public int getTiltPos(){
        tilt.enable(true);
        return tilt.getPositionDegrees();
    }
    
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

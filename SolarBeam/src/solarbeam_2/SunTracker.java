package solarbeam_2;

import solarbeam_2.easydevices.EasySunSensor;

/**
 * @author Luc Bettaieb
 * 
 * Am I a person?
 */
public class SunTracker {
    
    //X SunSensors
    private EasySunSensor leftSensor = new EasySunSensor("left", 1);
    private EasySunSensor rightSensor = new EasySunSensor("right", 2);
    
    //Y SunSensors
    private EasySunSensor topSensor = new EasySunSensor("top", 3);
    private EasySunSensor bottomSensor = new EasySunSensor("bottom", 4);
    
    
    public SunTracker(){
        
    }
    
    /*
     * Makes 'factors' for how much to move by. Negative factors indicate moving either the LEFT or TOP direction
     * while positive factors indicate moving in the RIGHT or DOWN direction.  The magnitude of these factors
     * also indicates how far off from zero (on target) the gimbal is.
     * 
     * Note that the gimbal should not attempt to make the entire movement to the 'zero point' without first
     * running this method again.  This method should produce new factors after a distance which corresponds
     * to half of the previous factor has been gained.
     * 
     * Once the returned factor is within some predetermined threshold, it can be said that the gimbal is on
     * target.
     * 
     * A speed factor is also included which determines how fast the gimbal should move.  The greater away from
     * being on target the gimbal is, the faster it should move to be on target.  Ideally, the gimbal should
     * slow down as it gets closer to being on target.
     */
    public Vector track(){
        long spdFactor, xFactor, yFactor;
        
        xFactor = normalizeFactor(leftSensor.getIntensity(), rightSensor.getIntensity());
        yFactor = normalizeFactor(topSensor.getIntensity(), bottomSensor.getIntensity());
        
        if(xFactor > yFactor){
            spdFactor = Math.abs(yFactor);
        }
        else{
            spdFactor = Math.abs(xFactor);
        }
        
        
        return new Vector(spdFactor, xFactor, yFactor);
    }
    
    /*
     * This method will take values directly from the Sun Sensors and normalize it so that:
     *  A) The values will be between 0 and 100
     *  B) The values will add to be 100 always
     */
    private long normalizeFactor(long val1, long val2){
        long sum = val1+val2;
        
        val1 = (val1/sum) * 100;
        val2 = (val2/sum) * 100;
        
        return val1-val2;
    }
}
package solarbeam_2;

import solarbeam_2.easydevices.EasyGimbal;

/**
 * @author Luc Bettaieb
 */
public class SolarSyncer {
    private EasyGimbal primary = new EasyGimbal(0);
    private EasyGimbal secondary = new EasyGimbal(1);
    
    private SunTracker tracker = new SunTracker();
    private Vector curVector;
    
    private long xFactor;
    private long yFactor;
    private long spdFactor;
    
    public SolarSyncer(){
        
    }
    
    public void sync(){
        //Get the necessary vector of factors and speed.
        curVector = tracker.track();
        
        xFactor = curVector.getXMove();
        yFactor = curVector.getYMove();
        spdFactor = curVector.getSpeed(); //This still needs smart implementation.
        
        //To put in thread A (X)
        if(xFactor > 0){
            
            primary.setX((long) (primary.getX() * .1 * Math.abs(xFactor) + (primary.getX())/2));
        }
        else if(xFactor < 0){
            primary.setX((long) (primary.getX() * .1 * Math.abs(xFactor) - (primary.getX())/2));
        }
        else{
            //Gimbal on target for X
        }
        
        //To put in thread B (Y)
        if(yFactor > 0){
            primary.setY((long) (primary.getY() * .1 * Math.abs(yFactor) + (primary.getY())/2));
        }
        else if(yFactor < 0){
            primary.setY((long) (primary.getY() * .1 * Math.abs(yFactor) - (primary.getY())/2));
        }
        else{
            //Gimbal on target for Y
        }
    }
}

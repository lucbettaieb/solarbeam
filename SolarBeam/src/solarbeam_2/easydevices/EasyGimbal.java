package solarbeam_2.easydevices;

import solarbeam_2.easydevices.jni.Gimbal;

/**
 * @author Luc Bettaieb
 * 
 */
public class EasyGimbal extends Gimbal {
    private static final long MAX_SPEED = 10000;
    //private static final long SMALLEST_HIGH_SPEED = 
    private static final long LOW_SPEED = 1000;
    private static final long ACCELERATION = 300;
    private static final long MAX_POS = 76000;
    
    //Constructor initializes EasyGimbal and does gimbal setup, checking for errors.
    public EasyGimbal(int idNum) throws java.io.IOException {
        super();
        if(!setup(gimbalPointer, idNum)){
            System.err.println("Setup failed to initialize EasyGimbal object using setup(gimbalPointer, idNum);");
            throw new java.io.IOException();
        }
    }
    
    /*
     * ---------------------------
     * |  Global Gimbal Methods  |
     * ---------------------------
     */
    
    //Sets gimbal motor power to
    public void power(boolean pwr){
        setMotorPower(gimbalPointer, pwr);
    }
    
    //Manually sets position.
    //Checks to see if params are too big or small, and adjusts accordingly.
    public void setPosition(long x, long y){
        if(x > MAX_POS){
            x = MAX_POS;
        }
        else if(x < (-MAX_POS)){
            x = (-MAX_POS);
        }
        
        if(y > MAX_POS){
            y = MAX_POS;
        }
        else if(y < (-MAX_POS)){
            y = MAX_POS;
        }

        setMotorPosition(gimbalPointer, x, y, 3);
    }
    
    //Manually sets gimbal speed.
    //Ignores negative numbers. Checks to see if speed is too great, adjusts accordingly.
    public void setSpeed(long spd, int select){
        if(spd > MAX_SPEED){
            spd = MAX_SPEED;
        } else if (spd < 100) {
            spd = 100;
        }
        setMotorSpeed(gimbalPointer, spd, LOW_SPEED, ACCELERATION, select);
    }
    
    //Max setters for extreme movement.
    //Previously was used to facilitate keyboard movement (under evaulation).
    public void setXMaxRight(){
        setX(MAX_POS);
    }
    
    public void setYMaxUp(){
        setY(MAX_POS);
    }
    
    public void setXMaxLeft(){
        setX(-MAX_POS);
    }
    
    public void setYMaxDown(){
        setY(-MAX_POS);
    }
    
    //Returns global gimbal speed.
    public long getSpeed(int select){
        return getMotorSpeedHighspeed(gimbalPointer, select);
    }
    
    //Stops all motors.
    public void stopAll(){
        setMotorSpeedAbort(gimbalPointer, 3);
    }
    
    /*
     * ---------------------------
     * |    X Gimbal Methods     |
     * ---------------------------
     */
    
    public void setX(long x){ //Sets only the X position
        if(x > MAX_POS) {
            x = MAX_POS;
        }
        else if(x < (-MAX_POS)){
            x = (-MAX_POS);
        }
        setMotorPosition(gimbalPointer, x, 0, 1);
    }
    public long getX(){
        return getMotorPosition(gimbalPointer, 1);
    }
    
    public void stopX(){ //Stops X Movement
        //System.out.println(gimbalPointer);
        setMotorSpeedAbort(gimbalPointer, 1);
    }
    
    
    /*
     * ---------------------------
     * |    Y Gimbal Methods     |
     * ---------------------------
     */
    
    public void setY(long y){ //Sets only the Y position
        if(y > MAX_POS) {
            y = MAX_POS;
        }
        else if(y < (-MAX_POS)){
            y = (-MAX_POS);
        }
        setMotorPosition(gimbalPointer, 0, y, 2);
    }
    
    public long getY(){
        return getMotorPosition(gimbalPointer, 2);
    }
    public void stopY(){ //Stops Y movement
        setMotorSpeedAbort(gimbalPointer, 2);
    }
} 
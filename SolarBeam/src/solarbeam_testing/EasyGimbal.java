package solarbeam_testing;

import java.io.IOException;
/*
 * @author Luc Bettaieb
 */
public class EasyGimbal extends Gimbal {

    private static final long MAX_SPEED = 20000;
    private static final long LOW_SPEED = 1000;
    private static final long ACCELERATION = 300;
    private static final long MAX_POS = 76000;

    public EasyGimbal(int id_num) throws java.io.IOException {
        super();
        if(!setup(gimbalPointer, id_num)){
            System.err.println("Setup failed.");
            throw new java.io.IOException();
        }
    }
    
    public void togglePwr(boolean pwr){ //Toggles motor power
        setMotorPower(gimbalPointer, pwr);
    }    
    public void setPos(long x, long y){ //Sets position dynamically for both motors.
        if(x > MAX_POS) {
            x = MAX_POS;
        }
        else if(x < -MAX_POS) {
            x = -MAX_POS;
        }
        if(y > MAX_POS) {
            y = MAX_POS;
        }
        else if(y < -MAX_POS) {
            y = -MAX_POS;
        }
         
    }
    
    public long getSpeed(){
        return getMotorSpeedHighspeed(gimbalPointer);
    }
    public void setSpeed(long spd){ //Sets the speed for the X motor
        if (spd > MAX_SPEED) { //Checks to see if the speed is greater than the max speed.
            spd = MAX_SPEED; //Assigns speed to max.
        }
       
        setMotorSpeed(gimbalPointer, spd, LOW_SPEED, ACCELERATION);
        
    }
    
    public void stopAll(){ //Stop all movement
        setMotorSpeedAbort(gimbalPointer, 3);
    }
    
    //----------X MOTOR----------
    public void setX(long x){ //Sets only the X position
        if(x > MAX_POS) {
            x = MAX_POS;
        }
        if(x < -MAX_POS) {
            x = -MAX_POS;
        }
        setMotorPosition(gimbalPointer, x, 0, 1);
    }
    public long getX(){
        return getMotorPosition(gimbalPointer, 1);
    }
    
    public void stopX(){ //Stops X Movement
        System.out.println(gimbalPointer);
        //setMotorSpeedAbort(gimbalPointer, 1);
    }
    
    //----------Y MOTOR----------
    public void setY(long y){ //Sets only the Y position
        if(y > MAX_POS) {
            y = MAX_POS;
        }
        if(y < -MAX_POS) {
            y = -MAX_POS;
        }
        setMotorPosition(gimbalPointer, 0, y, 2);
    }
    
    public long getY(){
        return getMotorPosition(gimbalPointer, 2);
    }
    public void stopY(){ //Stops Y movement
        setMotorSpeedAbort(gimbalPointer, 2);
    }
    
    //--------MAX SETTERS-------
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
    
}

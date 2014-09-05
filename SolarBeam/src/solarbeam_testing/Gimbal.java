package solarbeam_testing;

/**
 * @author Luc Bettaieb, Andrew Powell
 * 
 * JNI Connector class to provide access to the native C library written
 * by Andrew.  Links to the EasyGimbal class in order to facilitate easier
 * use of the methods.
 */
public class Gimbal {
    protected long gimbalPointer = 0;
    
    static{
        System.loadLibrary("SolarBeam_NSC_A2L");
        //Be sure to put the DLL in your ProgramFiles/java/jdk/bin folder.
    }
    
    public Gimbal(){
        gimbalPointer = construct_gimbalPointer(gimbalPointer);
    }
    private native long construct_gimbalPointer(long gimbalPointer);
    private native long destroy_gimbalPointer(long gimbalPointer);
     
    public native boolean connect(long gimbalPointer, int devicenumber);
    public native boolean disconnect(long gimbalPointer);
    public native boolean flush(long gimbalPointer);
    public native boolean setup(long gimbalPointer, int devicenumber);
    public native void setDeviceNumber(long gimbalPointer, int devicenumber);
    public native int getDeviceNumber(long gimbalPointer);
    public native void setMotorPower(long gimbalPointer, boolean on);
    public native void setMotorSpeed(long gimbalPointer, long highspeed, long lowspeed, long accelerate);
    public native void setMotorSpeedAbort(long gimbalPointer, int select);
    public native void setMotorPosition(long gimbalPointer, long xposition, long yposition, int select);
    public native void setMode(long gimbalPointer, int mode);
    public native void setBaudrate(long gimbalPointer, int select); //X-1, Y-2, XY-3
    public native void calibrateMotors(long gimbalPointer);
    public native long getMotorPosition(long gimbalPointer, int select);
    public native long getMotorSpeedHighspeed(long gimbalPointer);
    
    //Makes sure the memory allocated for the gimbalPointer is cleared.
    @Override
    public void finalize() throws Throwable
    {
        gimbalPointer = destroy_gimbalPointer(gimbalPointer);
        super.finalize();
    }
}
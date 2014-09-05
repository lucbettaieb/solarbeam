package solarbeam_testing;

/**
 * @author Luc Bettaieb, Andrew Powell
 * 
 * Class to implement SunSensor functionality using a direct connection
 * to the C library written by Andrew to talk to the LabView DLL to get input
 * from the SunSensors.
 */
public class SunSensor {
    protected long sunSensorPointer = 0;
    //May or may not be used, basing this off of gimbalPointer
    
    static {
        //System.loadLibrary("LabView_C_DLL_Thing");
        //This is to be defined.
    }
    
    public SunSensor(){
        sunSensorPointer = construct_sunSensorPointer(sunSensorPointer);
    }
    private native long construct_sunSensorPointer(long sunSensorPointer);
    private native long destroy_sunSensorPointer(long sunSensorPointer);
    
    public native long getIntensity(long sunSensorPointer);
    
    
    //Makes sure the memory previously allocated for the sunSunsenorPointer is cleared.
    @Override
    public void finalize() throws Throwable {
        sunSensorPointer = destroy_sunSensorPointer(sunSensorPointer);
        super.finalize();
    }
    
}

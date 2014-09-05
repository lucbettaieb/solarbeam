package solarbeam_2.easydevices.jni;

/**
 * @author Luc Bettaieb, Andrew Powell
 * 
 * Class to implement SunSensor functionality using a direct connection
 * to the C library written by Andrew to talk to the LabView DLL to get input
 * from the SunSensors.
 */
public class SunSensor extends CRIOModule {
    protected int analogInput;
    
    public SunSensor(int analogInput) {
        FPGASetup();
        this.analogInput = analogInput;
    }
    
    //Makes sure the memory previously allocated for the sunSunsenorPointer is cleared.
    @Override
    public void finalize() throws Throwable {
        FPGAClose();
        super.finalize();
    }
    
}
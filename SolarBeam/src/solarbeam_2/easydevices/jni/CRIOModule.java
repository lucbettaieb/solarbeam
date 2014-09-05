package solarbeam_2.easydevices.jni;

/**
 *
 * @author Andrew Powell
 */
public class CRIOModule {
    static {
        System.loadLibrary("SolarBeam_CRIO_Module");
    }
    protected native void FPGASetup();
    protected native void FPGAClose();
    protected native int readAnalog(int analogInput);
    protected native int readDigital();
    protected native void setDirectionDigital(int enableDigitalOutput);
    protected native void writeDigital(int digitalOutput);
}

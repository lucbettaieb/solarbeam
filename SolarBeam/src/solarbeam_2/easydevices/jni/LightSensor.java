/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_2.easydevices.jni;

/**
 *
 * @author Andrew Powell
 */
public class LightSensor {
    private long lightSensorPointer = 0;
 
    static { System.loadLibrary("SolarBeam_CRIO_Module"); }
    
    private native long constructLightSensorJni(long lightSensorPointer, int sdapin, int sclpin);
    private native long destroyLightSensorJni(long lightSensorPointer);
    private native boolean isDevicePresentJni(long lightSensorPointer);
    private native void powerOnJni(long lightSensorPointer);
    private native void powerOffJni(long lightSensorPointer);
    private native int readJni(long lightSensorPointer);
    
    public LightSensor(int sdapin, int sclpin) {
        lightSensorPointer = constructLightSensorJni(lightSensorPointer, sdapin, sclpin);
        
    }
    
    public boolean isDevicePresent() { return isDevicePresentJni(lightSensorPointer); }
    
    public void powerOn() { powerOnJni(lightSensorPointer); }
    
    public void powerOff() { powerOffJni(lightSensorPointer); }
    
    public int read() { return readJni(lightSensorPointer); }
    
    @Override public void finalize() throws Throwable {
        lightSensorPointer = destroyLightSensorJni(lightSensorPointer);
    }
}

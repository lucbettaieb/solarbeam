package solarbeam_2.easydevices;

import solarbeam_2.easydevices.jni.SunSensor;

/**
 * @author Luc Bettaieb
 */
public class EasySunSensor extends SunSensor {
    private String type;
    
    public EasySunSensor(String type, int analogInput){
        super(analogInput);
        this.type = type;
    }
    
    public int getIntensity(){
        if(readAnalog(analogInput) < 0) {
            return 0;
        }
        else {
            return readAnalog(analogInput);
        }
    }
    
    public String getType(){
        return type;
    }
}
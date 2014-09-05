package solarbeam_testing;

/**
 * @author Luc Bettaieb
 * 
 * EasySunSensor provides an easy access to the SunSensor class which
 * directly accesses the C library written by Andrew using JNI.
 * 
 * Field descriptions:
 *  id_num is a number for identifying the individual SunSensor
 *  type is what type of SunSensor will be used, either vertical-bias or horizontal-bias.
 * 
 */
public class EasySunSensor extends SunSensor {
    private long id_num;
    private String type;
    
    public EasySunSensor(long id_num, String type){
        this.id_num = id_num;
        this.type = type;
    }

    public long getIntensity(){
        return getIntensity(sunSensorPointer);
    }
    
    public String getType(){
        return type;
    }
    
    public long getId_num(){
        return id_num;
    }
    
}

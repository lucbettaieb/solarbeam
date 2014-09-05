package solarbeam_3;

/**
 *
 * @author Luc Bettaieb and Andrew Powell
 */
public interface Commands {
    public final static byte DISCONNECT = 0;
    
    //Gimbal
    public final static byte STOPX = 1;
    public final static byte SETX_MAX_RIGHT = 2;
    public final static byte SETX_MAX_LEFT = 3;
    public final static byte STOPY = 4;
    public final static byte SETY_MAX_UP = 5;
    public final static byte SETY_MAX_DOWN = 6;
    public final static byte SPEED_UP_X = 7;
    public final static byte SPEED_DOWN_X = 8;
    public final static byte SPEED_UP_Y = 9;
    public final static byte SPEED_DOWN_Y = 10;
    public final static byte READY = 11;
    
    //PantiltT
    public final static byte PT_STOPX = 12;
    public final static byte PT_SETX_MAX_RIGHT = 13;
    public final static byte PT_SETX_MAX_LEFT = 14;
    public final static byte PT_STOPY = 15;
    public final static byte PT_SETY_MAX_UP = 16;
    public final static byte PT_SETY_MAX_DOWN = 17;
    
    public final static byte PT_TOGGLE_AUTO = 18;

}

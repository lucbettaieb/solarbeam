/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_2.easydevices.jni;
import jssc.SerialPort;
import jssc.SerialPortException;
import java.io.IOException;
/**
 *
 * @author Andrew Powell
 */
public class HitecServoMotor {
    private Base base = null;
    private static SerialPort serialPort = null;
    
    static { 
        //System.loadLibrary("SolarBeam_CRIO_Module"); 
        System.loadLibrary("SolarBeam_Pololu_CLR");
    }
    
    // 'P' pan, 'T' is tilt
    public HitecServoMotor(int port, char motorSelect, int offset) throws IOException {
        try {
            setupSerial(port);
            base = new Arduino(port, (byte)motorSelect, offset);
        } catch (SerialPortException e) { e.printStackTrace(); throw new IOException(); }
    }
    
    public HitecServoMotor(short channel, int initialPositionDegrees) {
        base = new Pololu(channel, initialPositionDegrees);
    }
    
    public HitecServoMotor(short channel, int initialPositionDegrees, int offset) {
        base = new Pololu(channel, initialPositionDegrees, offset);
    }
    
    public HitecServoMotor(int port, char motorSelect) throws IOException {
        this(port, motorSelect, 0);
    }
    
    public static void setupSerial(int port) throws SerialPortException {
        if (serialPort == null) {
            serialPort = new SerialPort("COM" + port);
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            waitTillReady();
        }
    }
    
    public static void closeSerial() throws SerialPortException {
        if (serialPort != null) {
            serialPort.closePort();
            serialPort = null;
        }
    }
    
    private static void waitTillReady() throws SerialPortException {
        byte receive[];
        receive = serialPort.readBytes(1);
        while (receive[0] != (byte)'R') {
            receive = serialPort.readBytes(1);
        } 
    }
    
    public void enable(boolean isOn) {
        base.enable(isOn);
    }
    
    public void setPositionDegrees(int degrees) {
        base.setPositionDegrees(degrees);
    }
    
    public int getPositionDegrees() {
        return base.getPositionDegrees();
    }
    
    
    
    @Override public void finalize() throws Throwable {
        base.finalize();
        super.finalize();  
    } 
    
    private abstract class Base {
        public abstract void enable(boolean isOn);
        public abstract void setPositionDegrees(int degrees);
        public abstract int getPositionDegrees();
        public abstract void setIncrement(int increment);
        @Override public void finalize() throws Throwable { super.finalize(); }
    }
    
    private class Arduino extends Base {
        private final static byte MOTOR_PAN = 'P';
        private final static byte MOTOR_TILT = 'T';  
        private final static byte COMMAND_ATTACH = 'A';
        private final static byte COMMAND_DETACH = 'D';
        private final static byte COMMAND_WRITE = 'W';
        private byte motorSelect;
        private int offset;
        private int degrees;
        
        public Arduino(int port, byte motorSelect, int offset) throws IOException {
            if (motorSelect != MOTOR_PAN && motorSelect != MOTOR_TILT) {
                throw new IOException();
            }
            this.motorSelect = motorSelect;
            this.offset = offset;
            degrees = -1;
        }
        
        public Arduino(int port, byte motorSelect) throws IOException {
            this(port, motorSelect, 0);
        }
                  
        @Override public void enable(boolean isOn) {
            try {
                serialPort.writeByte(motorSelect);
                if (isOn == true) {
                    serialPort.writeByte(COMMAND_ATTACH);    
                } else {
                    serialPort.writeByte(COMMAND_DETACH);
                }
                serialPort.writeByte((byte)0);
            } catch (SerialPortException e) { e.printStackTrace();}
        }
        
        @Override public void setPositionDegrees(int degrees) {
            int modifiedDegrees = (degrees+offset)*42/360 + 69; // 69 -> 0; 111 -> 360
            this.degrees = modifiedDegrees;
            try {
                serialPort.writeByte(motorSelect);
                serialPort.writeByte(COMMAND_WRITE);  
                serialPort.writeByte((byte)modifiedDegrees);
            } catch (SerialPortException e) { e.printStackTrace();}
        }
        
        @Override public int getPositionDegrees() {
            return degrees;
        }
        
        @Override public void setIncrement(int increment) {
            // increment is not defined for the Arduino
        }
        
        @Override public void finalize() throws Throwable { 
            enable(false);
            super.finalize(); 
        }
    }
    
    private class Pololu extends Base {
        private final static int MAX_POSITION = 2000;
        private final static int MIN_POSITION = 1000;
        private long pointer = 0;
        private int positionDegrees = 0;
        private int positionOffset = 0;
        
        private native long constructPololuServoJni(long pointer, short channel, int initialPosition);
        private native long destructPololuServoJni(long pointer);
        private native void disableJni(long pointer);
        private native void setPositionJni(long pointer, int microseconds);
        private native int  getPositionJni(long pointer);
        private native void setIncrementJni(long pointer, int increment);
        private native void stopJni(long pointer);
        
        public Pololu(short channel, int initialPositionDegrees, int offset) {
            positionDegrees = initialPositionDegrees;
            pointer = constructPololuServoJni(pointer, channel, convertDegreesToMicroseconds(initialPositionDegrees));
            positionOffset = offset;
        }
        
        public Pololu(short channel, int initialPositionDegrees) {
            this(channel, initialPositionDegrees, 0);
        }
        
        private int convertDegreesToMicroseconds(int degrees) {
            return (degrees*117/180) + 1383;
        }
        
        private int convertMicrosecondsToDegrees(int microseconds) {
            return (microseconds - 1383)*180/117;
        }
        
        @Override public void enable(boolean isOn) {
            if (isOn == true) {
                setPositionDegrees(positionDegrees);
            } else {
                stopJni(pointer);
            }
        }
        
        @Override public void setPositionDegrees(int degrees) {
            int position = convertDegreesToMicroseconds(degrees)+positionOffset;
            if (position > MAX_POSITION) {
                position = MAX_POSITION;
                positionDegrees = convertMicrosecondsToDegrees(position);
            } else if (position < MIN_POSITION) {
                position = MIN_POSITION;
                positionDegrees = convertMicrosecondsToDegrees(position);
            } else {
                positionDegrees = degrees;
            }
            System.err.println("Piosition: " + position);
            setPositionJni(pointer, position);
        }
        
        @Override public int getPositionDegrees() {
            return positionDegrees;
        }
        
        @Override public void setIncrement(int increment) {
            setIncrementJni(pointer, increment);
        }
        
        @Override public void finalize() throws Throwable {
            pointer = destructPololuServoJni(pointer);
            super.finalize(); 
        }
    }
}

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
public class HitecServoMotor_Arduino {
    private Base base = null;
    private static SerialPort serialPort = null;
    
    //static { System.loadLibrary("SolarBeam_CRIO_Module"); }
    
    private native long constructHS785HBJni(long pointer, int address, int netDutyMicroseconds);
    private native long destroyHS785HBJni(long pointer);
    private native void enableJni(long pointer, boolean turnOn);
    private native void setPositionDegreesJni(long pointer, int degrees);
    private native int getPositionDegreesJni(long pointer);
    
    public HitecServoMotor_Arduino(int address) {
        base = new CRIO(address);
    }
    
    // 'P' pan, 'T' is tilt
    public HitecServoMotor_Arduino(int port, char motorSelect, int offset) throws IOException {
        try {
            setupSerial(port);
            base = new Arduino(port, (byte)motorSelect, offset);
        } catch (SerialPortException e) { e.printStackTrace(); throw new IOException(); }
    }
    
    public HitecServoMotor_Arduino(int port, char motorSelect) throws IOException {
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
        if (serialPort != null) {
            serialPort.closePort();
            serialPort = null;
        }
        base.finalize();
        super.finalize();  
    } 
    
    private abstract class Base {
        public abstract void enable(boolean isOn);
        public abstract void setPositionDegrees(int degrees);
        public abstract int getPositionDegrees();
        @Override public void finalize() throws Throwable { super.finalize(); }
    }
    
    private class CRIO extends Base {
        private final static int DEFAULT_NET_DUTY_MICROSECONDS = 1500;
        private final static int LIMIT_DEGREES = 640;
        long pointer = 0;

        public CRIO(int address) {
            this(address, DEFAULT_NET_DUTY_MICROSECONDS);
        }

        public CRIO(int address, int netDutyMicroseconds) {
            pointer = constructHS785HBJni(pointer, address, netDutyMicroseconds);
        }

        @Override public void enable(boolean turnOn) {
            enableJni(pointer, turnOn);
        }

        @Override public void setPositionDegrees(int degrees) {
            if (degrees > LIMIT_DEGREES) {
                degrees = LIMIT_DEGREES;
            } else if (degrees < -LIMIT_DEGREES) {
                degrees = -LIMIT_DEGREES;
            }
            setPositionDegreesJni(pointer, degrees);
        }

        @Override public int getPositionDegrees() {
            return getPositionDegreesJni(pointer);
        }

        @Override public void finalize() throws Throwable { 
            super.finalize(); 
            pointer = destroyHS785HBJni(pointer);
        }
    }
    
    private class Arduino extends Base {
        private final static byte MOTOR_PAN = 'P';
        private final static byte MOTOR_TILT = 'T';  
        private final static byte COMMAND_ATTACH = 'A';
        private final static byte COMMAND_DETACH = 'D';
        private final static byte COMMAND_WRITE = 'W';
        private byte motorSelect;
        private int offset;
        
        public Arduino(int port, byte motorSelect, int offset) throws IOException {
            if (motorSelect != MOTOR_PAN && motorSelect != MOTOR_TILT) {
                throw new IOException();
            }
            this.motorSelect = motorSelect;
            this.offset = offset;
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
            System.out.println("Cal1: " + (degrees));
            System.out.println("Cal1: " + (degrees - 180));
            System.out.println("Cal1: " + ((degrees - 180)/7));
            System.out.println("Cal1: " + ((degrees - 180)/7+90));
            System.out.println("Modified Degrees: " + modifiedDegrees);
            try {
                serialPort.writeByte(motorSelect);
                serialPort.writeByte(COMMAND_WRITE);  
                serialPort.writeByte((byte)modifiedDegrees);
            } catch (SerialPortException e) { e.printStackTrace();}
        }
        
        @Override public int getPositionDegrees() {
            return 0;
        }
        
        @Override public void finalize() throws Throwable { 
            enable(false);
            super.finalize(); 
        }
    }
}

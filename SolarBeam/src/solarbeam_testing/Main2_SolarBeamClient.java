/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import solarbeam_2.Communication;
import solarbeam_2.XboxController;

/**
 *
 * @author Andrew Powell
 */
public class Main2_SolarBeamClient implements Main2_Commands {
    public static final String SERVER_IP_ADDRESS = "169.254.101.15";
    public static final String WIRELSSS_SERVER_IP_ADDRESS = "169.254.101.15";
    public static final String LOCAL = "localhost";
    public static final int PORT = 6066;
    
    public static void print(String str) { System.out.println(str); }
    public static void printe(String str) { System.err.println(str); }
    public static ControlContainer.Control getControl(int input) {
        if (input == 0) {
            return new ControlContainer.XboxControl();
        } else if (input == 1) {
            return new ControlContainer.KeyboardControl();
        }
        return null;
    }
    public static byte askUserForController() {
        Scanner scan = new Scanner(System.in);
        byte input = -1;       
        
        print("type either 0 for Xbox Controller or 1 for keyboard\n"
                + "and then hit enter to continue");
        while (input != 0 && input != 1) {
            input = scan.nextByte();
        }
        return input;
    }
    
    public static void main(String[] args) throws Throwable {
        Communication client = null;
        ControlContainer.Control controller = null;
        boolean xIsMoving = false, yIsMoving = false;
        
        try {
            // ask user whether he / she wants to use the xbox controller or the keyboard
            // then, set up the controller
            controller = getControl(askUserForController());
            controller.setup();

            // attempt to form a connection
            print("Attempt to connect to server");
            client = new Communication(SERVER_IP_ADDRESS, PORT);
            print("Connection made");

            // main loop
            print("Starting main loop");
            while (true) {

                // check to see if the controller is still connected
                if (controller.isDisconnected()) {
                    print("Controller disconnected!");
                    client.writeByte(DISCONNECT);
                    break;
                } 

                if (controller.isStopX() && xIsMoving) {
                    client.writeByte(STOPX);
                    xIsMoving = false;
                } else if (controller.isSetXMaxRight() && !xIsMoving) {
                    client.writeByte(SETX_MAX_RIGHT);
                    xIsMoving = true;
                } else if (controller.isSetXMaxLeft() && !xIsMoving) {
                    client.writeByte(SETX_MAX_LEFT);
                    xIsMoving = true;
                }  

                if (controller.isStopY() && yIsMoving) {
                    client.writeByte(STOPY);
                    yIsMoving = false;
                } else if (controller.isSetYMaxUp() && !yIsMoving) {
                    client.writeByte(SETY_MAX_UP);
                    yIsMoving = true;
                } else if (controller.isSetYMaxDown() && !yIsMoving) {
                    client.writeByte(SETY_MAX_DOWN);
                    yIsMoving = true;
                }  

                if (controller.isSpeedXUp()) {
                    client.writeByte(SPEED_UP);
                    print("Speed: " + client.readLong());
                } else if (controller.isSpeedXDown()) {
                    client.writeByte(SPEED_DOWN);
                    print("Speed: " + client.readLong());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                print("Deconstructing client");
                client.finalize(); client = null;
            }
            if (controller != null) {
                 print("Deconstructing controller");
                controller.finalize(); controller = null;
            }
            System.exit(0);
        }   
    }
    
    public static class ControlContainer {
        
        private static void print(String str) { System.out.println(str); }
        private static void printe(String str) { System.err.println(str); }
        
        public static abstract class Control {
            public abstract void setup() throws IOException;
            public abstract boolean isDisconnected() throws IOException;
            public abstract boolean isStopX();
            public abstract boolean isSetXMaxRight();
            public abstract boolean isSetXMaxLeft();
            public abstract boolean isStopY();
            public abstract boolean isSetYMaxUp();
            public abstract boolean isSetYMaxDown();
            public abstract boolean isSpeedXUp();
            public abstract boolean isSpeedXDown();
            public abstract boolean isGimbalSwitched();
            @Override public void finalize() throws Throwable { super.finalize(); }
        }
        
        public static class XboxControl extends Control { 
            private XboxController controller;
            
            public XboxControl() { }
            
            @Override public void setup() throws IOException {
                print("Setting up the controller");
                controller = new XboxController();

                if( !controller.isControllerConencted() ) {
                    printe("No controller found!");
                    throw new IOException();
                }

                if( !controller.isControllerConencted() ) {
                    System.out.println("No controller found!");
                    throw new IOException();
                }
                print("Controller is ready");
            }
            
            @Override public boolean isDisconnected() throws IOException {
                return !controller.pollController() || controller.getButtonValue(6);
            }
            
            @Override public boolean isStopX() {
                return controller.getXAxisPercentageLEFT() > 35 && controller.getXAxisPercentageLEFT() < 65;
            }
            
            @Override public boolean isSetXMaxRight() {
                return controller.getXAxisPercentageLEFT() >= 65;
            }
            
            @Override public boolean isSetXMaxLeft() {
                return controller.getXAxisPercentageLEFT() <= 35;
            }
            
            @Override public boolean isStopY() {
                return controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65;
            }
            
            @Override public boolean isSetYMaxUp() {
                return controller.getYAxisPercentageLEFT() >= 65;
            }
            
            @Override public boolean isSetYMaxDown() {
                return controller.getYAxisPercentageLEFT() <= 35;
            }
            
            @Override public boolean isSpeedXUp() {
                return controller.getButtonValue(5);
            }
            
            @Override public boolean isSpeedXDown() {
                return controller.getButtonValue(4);
            }
            
            @Override public boolean isGimbalSwitched() {
                // NEEDS COMPLETION
                return false;
            }
        }
        
        public static class KeyboardControl extends Control {
            private static boolean disconnected;
            private static boolean stopX;
            private static boolean setXMaxRight;
            private static boolean setXMaxLeft;      
            private static boolean stopY;
            private static boolean setYMaxUp;
            private static boolean setYMaxDown;   
            private static boolean speedXUp;
            private static boolean speedXDown;
            private static boolean gimbalSwitched;
            
            public KeyboardControl() { 
                disconnected = false;
                stopX = false;
                setXMaxRight = false;
                setXMaxLeft = false; 
                stopY = false;
                setYMaxUp = false;
                setYMaxDown = false;
                speedXUp = false;
                speedXDown = false;
                gimbalSwitched = false;
            }
            
            @Override public boolean isDisconnected() throws IOException {
                if (disconnected) {
                    disconnected = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isStopX() {
                if (stopX) {
                    stopX = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxRight() {
                if (setXMaxRight) {
                    setXMaxRight = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxLeft() {
                if (setXMaxLeft) {
                    setXMaxLeft = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isStopY() {
                if (stopY) {
                    stopY = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxUp() {
                if (setYMaxUp) {
                    setYMaxUp = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxDown() {
                if (setYMaxDown) {
                    setYMaxDown = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSpeedXUp() {
                if (speedXUp) {
                    speedXUp = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSpeedXDown() {
                if (speedXDown) {
                    speedXDown = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isGimbalSwitched() {
                return false;
            }
            
            @Override public void setup() throws IOException {
                //Schedule a job for event dispatch thread:
                //creating and showing this application's GUI.
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        createAndShowGUI();
                    }
                });
            }
            
            private void createAndShowGUI() {
                // creates a new frame from a class that extends JFrame
                Frame frame = new Frame("Keyboard Control");
                
                // sets the default way the frame (or GUI) closes when the close
                // button is selected
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                // overrides what action is taken when the frame is closing
                frame.addWindowListener(new WindowAdapter(){
                    @Override public void windowClosing(WindowEvent e) {
                        disconnected = true;
                    }
                });
                
                frame.addComponentsToPane();
                frame.pack(); // shrinks the size of the frame to neatly hold the frame's components
                frame.setVisible(true);
            }
            
            private static class Frame extends JFrame
                implements KeyListener, ActionListener {
                JLabel messageArea;
                private static final int ARROW_UP = 38;
                private static final int ARROW_DOWN = 40;
                private static final int ARROW_LEFT = 37;
                private static final int ARROW_RIGHT = 39;
                private static final int KEY_Q = 81;
                private static final int KEY_X = 88;
                private static final int KEY_Z = 90;
                
                public Frame(String name) { super(name); }
                
                public void addComponentsToPane() {
                    messageArea = new JLabel("<html>Up and down arrow changes the y Gimbal<br>"
                            + "the left and right arrow changes the x Gimbal<br>"
                            + "type \"z\" to lower speed and type \"x\" to raise speed<br>"
                            + "type \"q\" or hit the close button to end program</html>");
                    messageArea.setFocusable(true);     // required to make the keylistener functional
                    messageArea.addKeyListener(this);   // enables the KeyListener functions

                    getContentPane().add(messageArea, BorderLayout.CENTER);
                }
                
                @Override public void keyTyped(KeyEvent e) { }
                
                @Override public void keyPressed(KeyEvent e) {
                    print("Key: " + e.getKeyCode());
                    switch (e.getKeyCode()) {
                        case ARROW_UP: 
                            setYMaxUp = true;
                            break;
                        case ARROW_DOWN:
                            setYMaxDown = true;
                            break;
                        case ARROW_LEFT:
                            setXMaxLeft = true;
                            break;
                        case ARROW_RIGHT:
                            setXMaxRight = true;
                            break;
                        case KEY_Q:
                            disconnected = true;
                            break;
                        case KEY_X:
                            speedXUp = true;
                            break;
                        case KEY_Z:
                            speedXDown = true;
                            break;
                        default:
                    }
                }
                
                @Override public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case ARROW_UP: 
                            stopY = true;
                            break;
                        case ARROW_DOWN:
                            stopY = true;
                            break;
                        case ARROW_LEFT:
                            stopX = true;
                            break;
                        case ARROW_RIGHT:
                            stopX = true;
                            break;
                        case KEY_Q:
                            break;
                        case KEY_X:
                            break;
                        case KEY_Z:
                            break;
                        default:
                    }
                }
                
                @Override public void actionPerformed(ActionEvent e) { }
            }
        }
    }
}

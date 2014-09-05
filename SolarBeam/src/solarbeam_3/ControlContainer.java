/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_3;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import solarbeam_2.XboxController;

/**
 *
 * @author Andrew Powell
 */
public class ControlContainer {
        
        private static void print(String str) { System.out.println(str); }
        private static void printe(String str) { System.err.println(str); }
        
        public static abstract class Control {
            public abstract void setup() throws IOException;
            public abstract boolean isDisconnected() throws IOException;
    
            public abstract boolean isStopXGimbal();
            public abstract boolean isSetXMaxRightGimbal();
            public abstract boolean isSetXMaxLeftGimbal();
            public abstract boolean isStopYGimbal();
            public abstract boolean isSetYMaxUpGimbal();
            public abstract boolean isSetYMaxDownGimbal();
            public abstract boolean isSpeedXUpGimbal();
            public abstract boolean isSpeedXDownGimbal();
            public abstract boolean isSpeedYUpGimbal();
            public abstract boolean isSPeedYDownGimbal();
 
            public abstract boolean isStopXPanTilt();
            public abstract boolean isSetXMaxRightPanTilt();
            public abstract boolean isSetXMaxLeftPanTilt();
            public abstract boolean isStopYPanTilt();
            public abstract boolean isSetYMaxUpPanTilt();
            public abstract boolean isSetYMaxDownPanTilt();
            
            public abstract boolean isToggleAuto();
 
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
                    printe("No controller found!");
                    throw new IOException();
                }
                print("Controller is ready");
            }
            
            @Override public boolean isDisconnected() throws IOException {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println("Problem sleeping thread.");
                }
                return !controller.pollController() || controller.getButtonValue(6);
            }
            
            @Override public boolean isStopXGimbal() {
                return controller.getXAxisPercentageLEFT() > 35 && controller.getXAxisPercentageLEFT() < 65;
            }
            
            @Override public boolean isSetXMaxRightGimbal() {
                return controller.getXAxisPercentageLEFT() >= 65;
            }
            
            @Override public boolean isSetXMaxLeftGimbal() {
                return controller.getXAxisPercentageLEFT() <= 35;
            }
            
            @Override public boolean isStopYGimbal() {
                return controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65;
            }
            
            @Override public boolean isSetYMaxUpGimbal() {
                return controller.getYAxisPercentageLEFT() >= 65;
            }
            
            @Override public boolean isSetYMaxDownGimbal() {
                return controller.getYAxisPercentageLEFT() <= 35;
            }
            
            @Override public boolean isSpeedXUpGimbal() {
                return controller.isRightDPad();
            }
            
            @Override public boolean isSpeedXDownGimbal() {
                return controller.isLeftDPad();
            }
            
            @Override public boolean isSpeedYUpGimbal() {
                return controller.isUpDPad();
            }
            
            @Override public boolean isSPeedYDownGimbal() {
                return controller.isDownDPad();
            }
            
            @Override public boolean isStopXPanTilt() { 
                return controller.getXAxisPercentageRIGHT() > 35 && controller.getXAxisPercentageRIGHT() < 65; 
            }
            
            @Override public boolean isSetXMaxRightPanTilt() { 
                return controller.getXAxisPercentageRIGHT() >= 65; 
            }
            
            @Override public boolean isSetXMaxLeftPanTilt() { 
                return controller.getXAxisPercentageRIGHT() <= 35; 
            }
                
            @Override public boolean isStopYPanTilt() { 
                return controller.getYAxisPercentageRIGHT() > 35 && controller.getYAxisPercentageRIGHT() < 65; 
            }
            
            @Override public boolean isSetYMaxUpPanTilt() { 
                return controller.getYAxisPercentageRIGHT() >= 65; 
            }
            
            @Override public boolean isSetYMaxDownPanTilt() { 
                return controller.getYAxisPercentageRIGHT() <= 35; 
            }
            
            @Override public boolean isToggleAuto() {
                return controller.getButtonValue(7);
            }
            
            @Override public void finalize() throws Throwable { super.finalize(); }
        }
        
        public static class KeyboardControl extends Control {
            private static boolean disconnected = false;
            private static boolean stopXGimbal = false;
            private static boolean setXMaxRightGimbal = false;
            private static boolean setXMaxLeftGimbal = false;      
            private static boolean stopYGimbal = false;
            private static boolean setYMaxUpGimbal = false;
            private static boolean setYMaxDownGimbal = false;   
            private static boolean speedXUpGimbal = false;
            private static boolean speedXDownGimbal = false;
            private static boolean speedYUpGimbal = false;
            private static boolean speedYDownGimbal = false;
            private static boolean stopXPanTilt = false;
            private static boolean setXMaxRightPanTilt = false;
            private static boolean setXMaxLeftPanTilt = false;
            private static boolean stopYPanTilt = false;
            private static boolean setYMaxUpPanTilt = false;
            private static boolean setYMaxDownPanTilt = false;
            private static boolean toggleAuto = false;
            
            public KeyboardControl() { 
            }
            
            @Override public boolean isDisconnected() throws IOException {
                if (disconnected) {
                    disconnected = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isStopXGimbal() {
                if (stopXGimbal) {
                    stopXGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxRightGimbal() {
                if (setXMaxRightGimbal) {
                    setXMaxRightGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxLeftGimbal() {
                if (setXMaxLeftGimbal) {
                    setXMaxLeftGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isStopYGimbal() {
                if (stopYGimbal) {
                    stopYGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxUpGimbal() {
                if (setYMaxUpGimbal) {
                    setYMaxUpGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxDownGimbal() {
                if (setYMaxDownGimbal) {
                    System.out.println("???");
                    setYMaxDownGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSpeedXUpGimbal() {
                if (speedXUpGimbal) {
                    speedXUpGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSpeedXDownGimbal() {
                if (speedXDownGimbal) {
                    speedXDownGimbal = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSpeedYUpGimbal() { 
                if (speedYUpGimbal) {
                    speedYUpGimbal = false;
                    return true;
                }
                return false; 
            }
            
            @Override public boolean isSPeedYDownGimbal() { 
                if (speedYDownGimbal) {
                    speedYDownGimbal = false;
                    return true;
                }
                return false; 
            }
            
            @Override public boolean isStopXPanTilt() {
                if (stopXPanTilt) {
                    stopXPanTilt = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxRightPanTilt() {
                if (setXMaxRightPanTilt) {
                    setXMaxRightPanTilt = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetXMaxLeftPanTilt() {
                if (setXMaxLeftPanTilt) {
                    setXMaxLeftPanTilt = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isStopYPanTilt() {
                if (stopYPanTilt) {
                    stopYPanTilt = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxUpPanTilt() { 
                if (setYMaxUpPanTilt) {
                    setYMaxUpPanTilt = false;
                    return true;
                }
                return false;
            }
            
            @Override public boolean isSetYMaxDownPanTilt() { 
                if (setYMaxDownPanTilt) {
                    setYMaxDownPanTilt = false;
                    return true;
                }
                return false; 
            }
            
            @Override public boolean isToggleAuto() {
                if (toggleAuto) {
                    toggleAuto = false;
                    return true;
                }
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
                ControlContainer.KeyboardControl.Frame frame = new ControlContainer.KeyboardControl.Frame("Keyboard Control");
                
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
                private static final int ARROW_UP = 38;     // turn gimbal y up
                private static final int ARROW_DOWN = 40;   // turn gimbal y down
                private static final int ARROW_LEFT = 37;   // turn gimbal x left
                private static final int ARROW_RIGHT = 39;  // turn gimbal x right
                private static final int KEY_W = 87;        // turn pan and tilt y up
                private static final int KEY_S = 83;        // turn pan and tilt y down
                private static final int KEY_A = 65;        // turn pan and tilt x left
                private static final int KEY_D = 68;        // turn pan and tilt y right
                private static final int KEY_Q = 81;        // disconnect
                private static final int KEY_X = 88;        // speed down gimbal x
                private static final int KEY_Z = 90;        // speed up gimbal x
                private static final int KEY_C = 67;        // speed down gimbal y
                private static final int KEY_V = 86;        // speed up gimbal y
                private static final int KEY_E = 69;        // toggle auto mode
                
                public Frame(String name) { super(name); }
                
                public void addComponentsToPane() {
                    messageArea = new JLabel("<html>Up and down arrow changes the postion of gimbal's y motor<br>"
                            + "the left and right arrow changes the postion of gimbal's x motor<br>"
                            + "type \"z\" to lower speed and type \"x\" to raise speed of gimbal's x motor<br>"
                            + "type \"c\" to lower speed and type \"v\" to raise speed of gimbal's y motor<br>"
                            + "\"w\" and \"s\" keys changes the postion of the pan and tilt gimbal's y motor<br>"
                            + "\"a\" and \"d\" keys changes the postion of the pan and tilt gimbal's x motor<br>"
                            + "type \"e\" to toggle auto mode<br>"
                            + "type \"q\" or hit the close button to end program</html>");
                    messageArea.setFocusable(true);     // required to make the keylistener functional
                    messageArea.addKeyListener(this);   // enables the KeyListener functions

                    getContentPane().add(messageArea, BorderLayout.CENTER);
                }
                
                @Override public void keyTyped(KeyEvent e) { }
                
                @Override public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case ARROW_UP: 
                            setYMaxUpGimbal = true;
                            break;
                        case ARROW_DOWN:
                            setYMaxDownGimbal = true;
                            break;
                        case ARROW_LEFT:
                            setXMaxLeftGimbal = true;
                            break;
                        case ARROW_RIGHT:
                            setXMaxRightGimbal = true;
                            break;
                        case KEY_W:
                            setYMaxUpPanTilt = true;
                            break;
                        case KEY_S:
                            setYMaxDownPanTilt = true;
                            break;
                        case KEY_A:
                            setXMaxLeftPanTilt = true;
                            break;
                        case KEY_D:
                            setXMaxRightPanTilt = true;
                            break;
                        case KEY_Q:
                            disconnected = true;
                            break;
                        case KEY_X:
                            speedXUpGimbal = true;
                            break;
                        case KEY_Z:
                            speedXDownGimbal = true;
                            break;
                        case KEY_C:
                            speedYDownGimbal = true;
                            break;
                        case KEY_V:
                            speedYUpGimbal = true;
                            break;
                        case KEY_E:
                            toggleAuto = true;
                            break;
                        default:
                    }
                }
                
                @Override public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case ARROW_UP: 
                            stopYGimbal = true;
                            break;
                        case ARROW_DOWN:
                            stopYGimbal = true;
                            break;
                        case ARROW_LEFT:
                            stopXGimbal = true;
                            break;
                        case ARROW_RIGHT:
                            stopXGimbal = true;
                            break;
                        case KEY_W:
                            stopYPanTilt = true;
                            break;
                        case KEY_S:
                            stopYPanTilt = true;
                            break;
                        case KEY_A:
                            stopXPanTilt = true;
                            break;
                        case KEY_D:
                            stopXPanTilt = true;
                            break;
                        case KEY_Q:
                            break;
                        case KEY_X:
                            break;
                        case KEY_Z:
                            break;
                        case KEY_C:
                            break;
                        case KEY_V:
                            break;
                        default:
                    }
                }
                
                @Override public void actionPerformed(ActionEvent e) { }
            }
        }
    }

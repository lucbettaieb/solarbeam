package solarbeam_2;


/**
 * @author Luc Bettaieb
 * Code based on JInputJoystick.java created by TheUzo007 (theuzo007.wordpress.com)
 * 
 * Requirements for use:
 * jinput.jar library
 * jinput DLLs in your system library
 */
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class XboxController {
    private Controller controller;
    
    private ArrayList<Boolean> buttonsValues;
    
    public XboxController(){
        initialize();
        initController(Controller.Type.STICK, Controller.Type.GAMEPAD);
    }
    private void initialize(){
        this.controller = null;
        this.buttonsValues = new ArrayList<Boolean>();
    }
    private void initController(Controller.Type ct1, Controller.Type ct2){
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        
        for(int i = 0; i < controllers.length && controller == null; i++){
            if(controllers[i].getType() == ct1 || controllers[i].getType() == ct2){
                controller = controllers[i];
                break;
            }
        }
    }
    public boolean isControllerConencted(){
        try {
            return controller.poll();
        } catch(Exception e){
            System.err.println("Controller not connected!");
            e.printStackTrace();
            return false;
        }
    }
    
    public Controller.Type getControllerType(){
        return controller.getType();
    }
    
    public String getControllerName(){
        return controller.getName();
    }
    
    public boolean pollController(){
         boolean isControllerValid;
        
        // Clear previous values of buttons.
        buttonsValues.clear();
        
        isControllerValid = controller.poll();
        if(!isControllerValid){
            return false;
        }
        
        Component[] components = controller.getComponents();
        
        for(int i=0; i < components.length; i++) {
            Component component = components[i];
            
            // Add states of the buttons
            if(component.getName().contains("Button")){
                if(component.getPollData() == 1.0f){
                    buttonsValues.add(Boolean.TRUE);
                }
                else{
                    buttonsValues.add(Boolean.FALSE);
                }
            }
        }
        
        return isControllerValid;
    }
    
    
    /*
     * General method for determining status of a button on the Xbox Controller
     * Button Values:
     * 0 - A
     * 1 - B
     * 2 - X
     * 3 - Y
     * 4 - Left Bumper
     * 5 - Right Bumper
     * 6 - Back
     * 7 - Start
     * 8 - Left Stick
     * 9 - Right Stick
     */
    public boolean getButtonValue(int index){
        return buttonsValues.get(index);
    }
    
    //Left Stick functions
    public float getXAxisValueLEFT(){
        Identifier identifier = Component.Identifier.Axis.X;
        return controller.getComponent(identifier).getPollData();
    }
    public int getXAxisPercentageLEFT(){
        float xAxisValue = this.getXAxisValueLEFT();
        int xAxisValuePercentage = (int)((2 - (1 - xAxisValue)) * 100) / 2;
        
        return xAxisValuePercentage;
    }
    
    public float getYAxisValueLEFT(){
        Identifier identifier = Component.Identifier.Axis.Y;
        return controller.getComponent(identifier).getPollData();
    }
    public int getYAxisPercentageLEFT(){
        float yAxisValue = this.getYAxisValueLEFT();
        int yAxisValuePercentage = (int)((2 - (1 - yAxisValue)) * 100) / 2;
        
        return yAxisValuePercentage;
    }
    
    //Trigger functions Left trigger is < 50%, right trigger is > 50%
    public float getTriggerValue(){
        Identifier identifier = Component.Identifier.Axis.Z;
        return controller.getComponent(identifier).getPollData();
    }
    public int getTriggerPercentage(){
        float zAxisValue = this.getTriggerValue();
        int zAxisValuePercentage = (int)((2 - (1 - zAxisValue)) * 100) / 2;
        
        return zAxisValuePercentage;
    }
    
    //Right stick functions
    public float getXAxisValueRIGHT(){
        Identifier identifier = Component.Identifier.Axis.RX;
        return controller.getComponent(identifier).getPollData();
    }
    public int getXAxisPercentageRIGHT(){
        float xRotationValue = this.getXAxisValueRIGHT();
        int xRotationValuePercentage = (int)((2 - (1 - xRotationValue)) * 100) / 2;
        
        return xRotationValuePercentage;
    }
    
    public float getYAxisValueRIGHT(){
        Identifier identifier = Component.Identifier.Axis.RY;
        return controller.getComponent(identifier).getPollData();
    }
    
    public int getYAxisPercentageRIGHT(){
        float yRotationValue = this.getYAxisValueRIGHT();
        int yRotationValuePercentage = (int)((2 - (1 - yRotationValue)) * 100) / 2;
        
        return yRotationValuePercentage;
    }
    /*
     * D-Pad Functions.  getDPadPosition returns the following:
     * Up: .25
     * Left: 1.0
     * Right: .5
     * Down: .75
     */
    public float getDPadPosition(){
        Identifier identifier = Component.Identifier.Axis.POV;
        return controller.getComponent(identifier).getPollData();
    }
    public boolean isRightDPad(){
        if(getDPadPosition() == .5){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isLeftDPad(){
        if(getDPadPosition() == 1.0){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isUpDPad(){
        if(getDPadPosition() == .25){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isDownDPad(){
        if(getDPadPosition() == .75){
            return true;
        }
        else{
            return false;
        }
    }
}
package solarbeam_2;

import solarbeam_2.easydevices.EasyGimbal;

/**
 *
 * @author Luc
 */
public class GimbalXboxTest { 
    
    public static void main(String[] args){
        XboxController controller = new XboxController();
        EasyGimbal primary = new EasyGimbal(1);
        //EasyGimbal secondary = new EasyGimbal(1);
        
        if( !controller.isControllerConencted() ){
            System.out.println("No controller found!");
            System.exit(0);
        }
        if( !controller.pollController() ) {
                System.out.println("Controller disconnected!");
                System.exit(0);
            }
        
        while(!controller.getButtonValue(6)){
            if( !controller.pollController() ) {
                System.out.println("Controller disconnected!");
                System.exit(0);
            }
            //System.out.println(controller.getXAxisPercentageLEFT());
            if(controller.getXAxisPercentageLEFT() > 35 && controller.getXAxisPercentageLEFT() < 65){
                primary.stopX();
            }
            else if(controller.getXAxisPercentageLEFT() >= 65 ){
                primary.setXMaxRight();
            }
            else if(controller.getXAxisPercentageLEFT() <= 35 ){
                primary.setXMaxLeft();
            }
            
            if(controller.getYAxisPercentageLEFT() > 35 && controller.getYAxisPercentageLEFT() < 65){
                primary.stopY();
            }
            else if(controller.getYAxisPercentageLEFT() >= 65 ){
                primary.setYMaxUp();
            }
            else if(controller.getYAxisPercentageLEFT() <= 35 ){
                primary.setYMaxDown();
            }
            
            if(controller.getButtonValue(5)){
                System.out.println("speed up");
                primary.setSpeed(primary.getSpeed(1)+100,1);
                System.out.println(primary.getSpeed(1));
            }
            else if(controller.getButtonValue(4)){
                System.out.println("speed down");
                primary.setSpeed(primary.getSpeed(1)-100,1);
                System.out.println(primary.getSpeed(1));
            }
            
            
            
        }
        
        System.out.println("Gimbals finalizing...");
            try {
                primary.finalize();
                //secondary.finalize();
                System.out.println("..finalized!");
            } catch(Throwable ex){
                System.err.println("Error finalizing gimbals.");
            }
            System.out.println("System exiting.");
            System.exit(0);
        
    }
    
}

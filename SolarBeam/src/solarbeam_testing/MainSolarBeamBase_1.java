/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarbeam_testing;

/**
 *
 * @author Andrew Powell
 */
public class MainSolarBeamBase_1 {

    public enum M {
        stopX(0), 
        setXMaxRight(1), 
        setXMaxLeft(2), 
        stopY(3), 
        setYMaxUp(4),
        setYMaxDown(5),
        speedUpX(6),
        speedDownX(7),
        finalize(8),
        handshake(0);
        
        public final int i;
        private M(int i) {
            this.i = i;
        }
        public int get() {
            return i;
        }
    }
}

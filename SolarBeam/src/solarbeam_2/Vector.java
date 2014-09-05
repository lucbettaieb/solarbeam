package solarbeam_2;

/**
 * @author Luc Bettaieb
 */
public class Vector {
    private long speed;
    private long xMove;
    private long yMove;
    
    public Vector(long spd, long x, long y){
        speed = spd;
        xMove = x;
        yMove = y;
    }
    
    public long getSpeed(){
        return speed;
    }
    
    public long getXMove(){
        return xMove;
    }
    
    public long getYMove(){
        return yMove;
    }
    
}

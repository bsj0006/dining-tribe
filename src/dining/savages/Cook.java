package dining.savages;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Christopher McClurg
 */
public class Cook implements Runnable{
    private final Pot sharedPot;
    private Thread cook;
    private int servings;
    
    public Cook(Pot pot, int servings) {
        this.sharedPot = pot;
        this.servings = servings;
    }
    
    @Override
    public void run() {
        waitForEmpty();
    }
        
    public void start () {
        System.out.println("Starting Cook Thread");
        if (cook==null){
            cook = new Thread (this, "Cook");
            cook.start();
        }
    }

    public void waitForEmpty (){
        while (sharedPot.cont){
            synchronized (sharedPot.Empty) {
                while (sharedPot.servings > 0) {
                    try {
                        sharedPot.Empty.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Cook.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            sharedPot.pot.lock();
            Refill();
            sharedPot.pot.unlock();
            synchronized(sharedPot.NotEmpty) {
                sharedPot.NotEmpty.notifyAll();
            }
        }
    }
    
    public int Refill() {
       try {
           sharedPot.servings += servings;
           System.out.print("The Cook has refilled the pot. There are now " + sharedPot.servings + " servings.\n");
       } finally {
       }
       return sharedPot.servings;
    }


}

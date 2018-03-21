package dining.savages;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;


/**
 *
 * @author Christopher McClurg
 */
public class Cook implements Runnable{
    private final Pot sharedPot;
    private Thread cook;
    private int servings;
    private final int numTribe;
    private Random rand;
    
    public Cook(Pot pot, int numTribe) {
        this.sharedPot = pot;
        this.numTribe = numTribe;
        rand = new Random();
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
           
           servings = (rand.nextInt(numTribe*5/2) + (numTribe / 2) + 1);
           
           sharedPot.servings += servings;
           System.out.print("The Cook has refilled the pot. There are now " + sharedPot.servings + " servings.\n");
       } finally {
       }
       return sharedPot.servings;
    }


}

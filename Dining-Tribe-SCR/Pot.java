package dining.savages;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class Pot {
    ReentrantLock pot = new ReentrantLock();
    Semaphore Empty = new Semaphore(1);
    Semaphore NotEmpty = new Semaphore(1);
    Semaphore ActiveTribesman = new Semaphore(1);
    boolean cont = true;
    int servings;    

    
    public Pot(int servings) {
        this.servings = servings;
        if (this.servings <= 0) {
            this.servings = 0;
        }
        else {
            this.servings = servings;
        }
    }
}

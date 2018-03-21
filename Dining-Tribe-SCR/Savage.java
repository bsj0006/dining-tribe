/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dining.savages;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Savage implements Runnable{
    private final Pot sharedPot;
    private Thread s;
    private String id;
    private int time;
    
    public Savage (Pot pot, String id, int time) {
        this.sharedPot = pot;
        this.id = id;
        this.time = time;
    }
    
    @Override
    public void run() {
        System.out.print(id + " is activated.\n");
        while (sharedPot.cont){            
            try {
                Thread.sleep(time*1000);
                getServing();
            } catch (InterruptedException ex) {
                System.out.print(id + " failed to sleep at line 28.\n");
            }            
        }
    }
    
    public void start () {
        if(s==null) {
            s = new Thread (this, id);
            s.start();
        }
    }
    
    public  void getServing() throws InterruptedException {
        try {
            sharedPot.ActiveTribesman.acquire();
            if (sharedPot.servings<=0) {
                try {
                    synchronized (sharedPot.Empty) {
                        sharedPot.Empty.notifyAll();
                    }
                    synchronized (sharedPot.NotEmpty){
                        sharedPot.NotEmpty.wait();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Savage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sharedPot.pot.lock();
            if (sharedPot.pot.isHeldByCurrentThread())
                if (sharedPot.servings>0) sharedPot.servings--;
            else {
                System.out.print("There is an error in which a thread did not retain it's lock properly.\n");
            }
            System.out.print(id + " removed 1 serving. There are " + sharedPot.servings + " remaining.\n");
            sharedPot.pot.unlock();
        } finally {
            sharedPot.ActiveTribesman.release();
        }
    }
}

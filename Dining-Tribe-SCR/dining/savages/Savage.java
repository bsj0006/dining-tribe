/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dining.savages;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a tribe member or savage in the Dining Tribe problem.
 * This object takes servings from the {@link Pot}.
 */
public class Savage implements Runnable
{
  private final Pot sharedPot;
  private Thread savageThread;
  private String id;
  private int time;

  /**
   * Creates a new savage.
   *
   * @param pot  The shared pot of servings.
   * @param id   The id designated for the tribe member.
   * @param time The time in seconds to sleep between trying to eat.
   */
  Savage(Pot pot, String id, int time)
  {
    this.sharedPot = pot;
    this.id = id;
    this.time = time;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    System.out.println(id + " is activated.");
    while (sharedPot.cont) {
      try {
        //Sleep then eat, just like real life
        Thread.sleep(time * 1000);
        getServing();
      } catch (InterruptedException ex) {
        System.out.println(id + " failed to sleep at line 28.");
      }
    }
  }

  /**
   * Runs a savage on its own thread.
   */
  public void start()
  {
    if (savageThread == null) {
      savageThread = new Thread(this, id);
      savageThread.start();
    }
  }

  /**
   * Attempts to get a serving from the {@link Pot} and waits for the
   * <code>Pot</code> to be refilled if its empty.
   *
   * @throws InterruptedException
   */
  private void getServing() throws InterruptedException
  {
    try {
      //Try to get access to the pot
      sharedPot.ActiveTribesman.acquire();
      //If empty, notify the cook
      if (sharedPot.servings <= 0) {
        try {
          synchronized (sharedPot.Empty) {
            sharedPot.Empty.notifyAll();
          }
          synchronized (sharedPot.NotEmpty) {
            sharedPot.NotEmpty.wait();
          }
        } catch (InterruptedException ex) {
          Logger.getLogger(Savage.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      //Take a serving
      sharedPot.pot.lock();
      if (sharedPot.pot.isHeldByCurrentThread())
        if (sharedPot.servings > 0)
          sharedPot.servings--;
        else {
          System.out.println("There is an error in which a thread did not retain it'savageThread lock properly.");
        }
      System.out.println(id + " removed 1 serving. There are " + sharedPot.servings + " remaining.");
      sharedPot.pot.unlock();
    } finally {
      //Release access to the pot
      sharedPot.ActiveTribesman.release();
    }
  }
}

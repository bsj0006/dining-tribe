package dining.savages;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a cook in the dining savages problem. The cook is responsible for restoring serving in the
 * {@link Pot}
 */
public class Cook implements Runnable
{
  private final Pot sharedPot;
  private Thread cookThread;
  private final int numTribe;
  private Random rand;

  /**
   * Creates a new cook for the dining savages.
   *
   * @param pot      The pot used by the tribe.
   * @param numTribe The number of members in the tribe.
   */
  Cook(Pot pot, int numTribe)
  {
    this.sharedPot = pot;
    this.numTribe = numTribe;
    rand = new Random();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run()
  {
    waitForEmpty();
  }

  /**
   * Creates a new thread and runs the cook.
   */
  public void start()
  {
    System.out.println("Starting Cook Thread");
    if (cookThread == null) {
      cookThread = new Thread(this, "Cook");
      cookThread.start();
    }
  }

  /**
   * Waits for the pot to be empty, refills the pot, then loops again.
   */
  private void waitForEmpty()
  {
    while (sharedPot.cont) {
      //Waits for the pot to be empty
      synchronized (sharedPot.Empty) {
        while (sharedPot.servings > 0) {
          try {
            sharedPot.Empty.wait();
          } catch (InterruptedException ex) {
            Logger.getLogger(Cook.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
      //The pot is empty, refill it
      sharedPot.pot.lock();
      refill();
      sharedPot.pot.unlock();
      synchronized (sharedPot.NotEmpty) {
        sharedPot.NotEmpty.notifyAll();
      }
    }
  }

  /**
   * Refills the pot for a random amount based on the number of members in the tribe.
   *
   * @return The number of servings placed into the {@link Pot}.
   */
  private int refill()
  {
    //Calculate servings
    int servings = (rand.nextInt(numTribe * 5 / 2) + (numTribe / 2) + 1);

    //Add the servings
    sharedPot.servings += servings;
    System.out.print("The Cook has refilled the pot. There are now " + sharedPot.servings + " servings.\n");
    return sharedPot.servings;
  }
}

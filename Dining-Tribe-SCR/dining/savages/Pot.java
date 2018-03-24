package dining.savages;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This represents the pot in the Dining Tribe problem.
 * It contains the semaphores and locks as well as keeps track of the servings.
 */
public class Pot
{
  ReentrantLock pot = new ReentrantLock();
  final Semaphore Empty = new Semaphore(1);
  final Semaphore NotEmpty = new Semaphore(1);
  Semaphore ActiveTribesman = new Semaphore(1);
  boolean cont = true;
  int servings;

  /**
   * Creates a new pot.
   *
   * @param servings The number of servings to put in the pot.
   */
  Pot(int servings)
  {
    //Servings shouldn't be negative
    this.servings = servings;
    if (this.servings < 0) {
      this.servings = 0;
    } else {
      this.servings = servings;
    }
  }
}

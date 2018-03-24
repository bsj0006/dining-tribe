package dining.savages;

import java.util.Scanner;

/**
 * This is the driver class that starts the program. It takes user input for a number (1 - 99) and creates the
 * {@link Pot}, {@link Cook}, and {@link Savage} objects.
 */
public class DiningSavages
{

  /**
   * Runs the application.
   *
   * @param args Default parameter. Args is not used.
   */
  public static void main(String[] args)
  {
    int numSavages;
    Scanner in = new Scanner(System.in);

    //Get the number of tribe members and validate input
    System.out.print("How many tribe members are in this tribe (Less than 100): ");
    numSavages = in.nextInt();

    while (numSavages < 1 || numSavages > 100) {
      System.out.print("\nInvalid number of tribe members. Try again (Less than 100) : ");
      numSavages = in.nextInt();
      System.out.println();
    }


    //Create the objects and run them
    Pot pot = new Pot(0);
    Cook cook = new Cook(pot, numSavages);
    cook.start();
    Savage[] Savages = new Savage[numSavages];

    for (int i = 0; i < numSavages; i++) {
      Savages[i] = new Savage(pot, ("Tribe Member " + (i + 1)), 5 + (i % 10));
      Savages[i].start();
    }
  }
}
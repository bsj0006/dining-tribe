
package dining.savages;

import java.util.Scanner;

public class DiningSavages {

    public static void main(String[] args) {
        int numSavages = 0;
        Scanner in = new Scanner (System.in);
        
        System.out.print("How many tribe members are in this tribe (Less than 100): ");
        numSavages = in.nextInt();
        
        while (numSavages < 0 || numSavages > 100) {
            System.out.print("\nInvalid number of tribe members. Try again (Less than 100) : ");
            numSavages = in.nextInt();
            System.out.print("\n");
        }

        
        Pot pot = new Pot(0);
        Cook cook = new Cook(pot, numSavages);
        cook.start();
        Savage[] Savages = new Savage[numSavages];
        
        for (int i = 0; i < numSavages; i++) {
            Savages[i] = new Savage(pot, ("Tribe Member " + (i+1)), 5 + (i%10));
            Savages[i].start();
        }
        
        
        
    }
    
}
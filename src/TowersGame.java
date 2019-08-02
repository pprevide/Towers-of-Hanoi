/**
 * Created by p on 8/1/19.
 */

import java.util.Scanner;
import java.util.Stack;

class Disk {

    private int diskNumber; // also represents disk thickness
    Disk () {};
    Disk (int number) {
        diskNumber = number;
    }

    public int getNumber () {
        return diskNumber;
    }

} // end of class Disk

class Peg {

    private String pegName;
    private Stack<Disk> pegContents;
    Peg () {}
    Peg (String s) {
        pegName=s;
        pegContents = new Stack<>();
    }

    public void addDisk (Disk a) {
        pegContents.push(a);
    }

    public Disk removeDisk () {
        Disk removedDisk = pegContents.pop();
        return removedDisk;
    }

    /* add disks to the source peg to start the game
     * the highest-numbered disk gets added first, to be at the bottom  */
    public void addAllDisksToStartGame(int numberOfDisks) {
        while (numberOfDisks>0) {
            Disk d = new Disk(numberOfDisks);
            addDisk(d);
            numberOfDisks--;
        }
    }

    public String getName () {
        return pegName;
    }

    /* concatenates the numbers of the Disks on the calling Peg,
     * space-delimited, into a single String for easier printing */
    public String pegToString () {
        String pegDisksAsString = "";
        String s;     // temporary placeholder string
        // concatenate each Disk number on the peg, followed by a space
        for (Disk d : pegContents) {
            int i = d.getNumber();
            s=String.valueOf(i);
            pegDisksAsString = pegDisksAsString.concat(s+ " ");
        }
        return pegDisksAsString;
    }

} // end of class Peg



public class TowersGame {
    private int numberPegs = 3;
    private int numberMoves;
    private int numberOfDisks;
    // the set of Pegs is stored as an array of Peg objects
    private Peg[] pegArray = new Peg[numberPegs];
    private String[] namesOfPegs={"A","B","C"};
    public TowersGame () {};
    public TowersGame (int number) {
        numberOfDisks = number;
    }

    public int getNumberOfDisks () {
        return numberOfDisks;
    }

    public void setNumberOfDisks(int number) {
        numberOfDisks = number;
    }

    /* create the correct number of pegs and add all disks to
     * the source peg  */
    public void initializeGame () {
        int i=0;
        while (i<numberPegs) {
            pegArray[i]= new Peg(namesOfPegs[i]);
            i++;
        }
        pegArray[0].addAllDisksToStartGame(numberOfDisks);
    }

    // print formatted header lines
    public void printOutputHeader () {
        System.out.printf("%-20s %-20s %n", "Move", "Peg Configuration");
        System.out.printf("%-21s", "");
        int i=0;
        // print peg names
        while (i<numberPegs) {
            System.out.printf("%-18s ", namesOfPegs[i]);
            i++;
        }
        System.out.print('\n');
        // print the row showing the start of the game
        System.out.printf("%-21s", "start");
        i=numberOfDisks;
        while (i>0) {
            System.out.print(i + " ");
            i--;
        }
        System.out.print('\n');
    }

    // for each peg, print the pegs' contents after a move
    public void displayGameState () {
        int i=0;
        while (i<numberPegs) {
            String next = pegArray[i].pegToString();
            System.out.printf("%-19s",next);
            i++;
        }
        System.out.print('\n');
    }

    /* recursive method to move the disks from peg A to peg C
     * while printing each move and the peg contents   */
    void move(int numberDisks, Peg source, Peg dest, Peg utility) {
        if (numberDisks==1) {
            Disk lastDisk = source.removeDisk();
            dest.addDisk(lastDisk);
            System.out.printf("%-21s", lastDisk.getNumber() + " from " +
                    source.getName() + " to " + dest.getName());
            displayGameState();
        }
        else {
            move (numberDisks-1, source, utility, dest);
            Disk nextDisk = source.removeDisk();
            dest.addDisk(nextDisk);
            System.out.printf("%-21s", nextDisk.getNumber() + " from " +
                    source.getName() + " to " + dest.getName());
            displayGameState();
            move (numberDisks-1, utility, dest, source);
        }
        numberMoves++;
    }

    /* gets number of disks from command line argument
     * if no arguments, prompts user for number of disks   */
    public int readNumberOfDisks (String[] argArray) {
        int disks=0;
        if (argArray.length==1) {
            try {
                disks = Integer.parseInt(argArray[0]);
            } catch (NumberFormatException e) {
                System.err.println("Command line argument must be an integer.");
                System.exit(1);
            }
        }
        else {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the number of disks: ");
            try {
                disks = input.nextInt();
            } catch (Exception e) {
                System.err.println("Number of disks must be an integer.");
                System.exit(1);
            }
        }

        if (disks<=0) {
            System.err.println("Number of disks must be at least 1.");
            System.exit(1);
        }
        return disks;
    }

    public void printNumberOfMoves() {
        System.out.println("Number of moves required to complete the game: " + numberMoves);
    }

    public static void main(String[] args) {
        TowersGame newGame = new TowersGame();
        newGame.setNumberOfDisks(newGame.readNumberOfDisks(args));
        newGame.printOutputHeader();
        newGame.initializeGame();
        newGame.move(newGame.getNumberOfDisks(), newGame.pegArray[0],
                newGame.pegArray[2], newGame.pegArray[1]);
        newGame.printNumberOfMoves();
    }


}

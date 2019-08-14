/**
 * This project displays solutions to a 3-peg Towers of Hanoi game and shows the number of moves.
 * Towers of Hanoi is a commonly-used example of recursive problem-solving.  The solution to an n-disk problem
 * can be viewed in terms of solving an (n-1)-disk problem.  The move() method of class TowersGame below
 * uses this approach.
 * When run from the command line, the user is asked to indicate the number of disks.
 */

import java.util.Scanner;
import java.util.Stack;

class Disk {

    private int diskNumber; // higher disk number indicates wider disk
    Disk(int number) {
        this.diskNumber = number;
    }

    int getNumber() {
        return this.diskNumber;
    }

}

class Peg {

    private String pegName;
    private Stack<Disk> pegContents;

    Peg(String pegName) {
        this.pegName = pegName;
        this.pegContents = new Stack<>();
    }

    void addDisk(Disk a) {
        pegContents.push(a);
    }

    Disk removeDisk() {
        return pegContents.pop();
    }

    /*
    * Add disks to the source peg to start the game.
    * The highest-numbered disk is the widest.
    * This disk gets added first, so that it is at the bottom of a stack.
    */
    void addAllDisksToStartGame(int numberOfDisks) {
        while (numberOfDisks > 0) {
            Disk d = new Disk(numberOfDisks);
            addDisk(d);
            numberOfDisks--;
        }
    }

    String getName() {
        return pegName;
    }

    /*
     * Concatenates the numbers of the disks on the calling peg,
     * space-delimited, into a single string for easier printing.
     * Each disk name is separated by a space.
     */
    String pegToString() {
        String pegDisksAsString = "";
        String s;
        for (Disk d : pegContents) {
            int i = d.getNumber();
            s = String.valueOf(i);
            pegDisksAsString = pegDisksAsString.concat(s + " ");
        }
        return pegDisksAsString;
    }

}


public class TowersGame {
    private final int NUMBER_PEGS = 3;
    private int numberMoves;
    private int numberDisks;
    private Peg[] pegArray = new Peg[NUMBER_PEGS];
    private String[] namesOfPegs = {"A", "B", "C"};

    public TowersGame() {
    }

    public TowersGame(int numberDisks) {
        this.numberDisks = numberDisks;
    }

    public int getNumberDisks() {
        return numberDisks;
    }

    public void setNumberDisks(int number) {
        this.numberDisks = number;
    }

    /* Create the correct number of pegs and add all disks to
     * the source peg.
     */
    public void initializeGame() {
        printOutputHeader();
        int i = 0;
        while (i < NUMBER_PEGS) {
            pegArray[i] = new Peg(namesOfPegs[i]);
            i++;
        }
        pegArray[0].addAllDisksToStartGame(numberDisks);
    }


    private void printOutputHeader() {
        System.out.printf("%-20s %-20s %n", "Move", "Peg Configuration");
        System.out.printf("%-21s", "");
        int i = 0;
        // print peg names
        while (i < NUMBER_PEGS) {
            System.out.printf("%-18s ", namesOfPegs[i]);
            i++;
        }
        System.out.print('\n');
        // print the row showing the start of the game
        System.out.printf("%-21s", "start");
        i = numberDisks;
        while (i > 0) {
            System.out.print(i + " ");
            i--;
        }
        System.out.print('\n');
    }

    // for each peg, print the pegs' contents after a move
    private void displayGameState() {
        int i = 0;
        while (i < NUMBER_PEGS) {
            String next = pegArray[i].pegToString();
            System.out.printf("%-19s", next);
            i++;
        }
        System.out.print('\n');
    }

    /* recursive method to move the disks from peg A to peg C
     * while printing each move and the peg contents   */
    void move(int numberDisks, Peg source, Peg dest, Peg utility) {
        if (numberDisks == 1) {
            Disk lastDisk = source.removeDisk();
            dest.addDisk(lastDisk);
            System.out.printf("%-21s", lastDisk.getNumber() + " from " +
                    source.getName() + " to " + dest.getName());
            displayGameState();
        } else {
            move(numberDisks - 1, source, utility, dest);
            Disk nextDisk = source.removeDisk();
            dest.addDisk(nextDisk);
            System.out.printf("%-21s", nextDisk.getNumber() + " from " +
                    source.getName() + " to " + dest.getName());
            displayGameState();
            move(numberDisks - 1, utility, dest, source);
        }
        numberMoves++;
    }

    /* gets number of disks from command line argument
     * if no arguments, prompts user for number of disks   */
    private int readNumberOfDisks(String[] argArray) {
        int disks = 0;
        if (argArray.length == 1) {
            try {
                disks = Integer.parseInt(argArray[0]);
            } catch (NumberFormatException e) {
                System.err.println("Command line argument must be an integer.");
                System.exit(1);
            }
        } else {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter the number of disks: ");
            try {
                disks = input.nextInt();
            } catch (Exception e) {
                System.err.println("Number of disks must be an integer.");
                System.exit(1);
            }
        }

        if (disks <= 0) {
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
        newGame.setNumberDisks(newGame.readNumberOfDisks(args));
        newGame.initializeGame();
        newGame.move(newGame.getNumberDisks(), newGame.pegArray[0],
                newGame.pegArray[2], newGame.pegArray[1]);
        newGame.printNumberOfMoves();
    }


}

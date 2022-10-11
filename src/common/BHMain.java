package common;

import functions.Checker;
import functions.Solver;
import layouts.BHLayout;
import layouts.WHLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Starter by Ian Gent, Sep 2021
// This class is provided to save you writing some of the basic parts of the code
// Also to provide a uniform command line structure

// You may freely edit this code if you wish, e.g. adding methods to it.
// Obviously we are aware the starting point is provided so there is no need to explicitly credit us
// Please clearly mark any new code that you have added/changed to make finding new bits easier for us

public class BHMain {

    public static final String s = File.separator;
    public static final String path = "."+s+"files"+s+"DataTests"+s;


    public static void printUsage() {
        System.out.println("Input not recognised.  Usage is:");
        System.out.println("java BHmain GEN|CHECK|SOLVE|CHECKWORM|SOLVEWORM <arguments>"  );
        System.out.println("     GEN arguments are seed [numcards=51] [numranks=13] [numsuits=4] [numpiles=17]");
        System.out.println("                       all except seed may be omitted, defaults shown");
        System.out.println("     CHECK/CHECKWORM argument is file1 [file2]");
        System.out.println("                     if file1 - then stdin is used");
        System.out.println("                     if file2 is ommitted or is - then stdin is used");
        System.out.println("                     at least one of file1/file2 must be a filename and not stdin");
        System.out.println("     SOLVE/SOLVEWORM argument is [file]");
        System.out.println("                     if file is ommitted or is - then stdin is used");
    }


    public static ArrayList<Integer> readIntArray(String filename) {
        // File opening sample code from
        // https://www.w3schools.com/java/java_files_read.asp
        ArrayList<Integer> result  ;
        Scanner reader;
        try {
            File file = new File(filename);
            reader = new Scanner(file);
            result=readIntArray(reader);
            reader.close();
            return result;
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        // drop through case
        return new ArrayList<Integer>(0);

    }


    public static ArrayList<Integer> readIntArray(Scanner reader) {
        ArrayList<Integer> result = new ArrayList<Integer>(0);
        while( reader.hasNextInt()  ) {
            result.add(reader.nextInt());
        }
        return result;
    }


    public static void main(String[] args) {

        Scanner stdInScanner = new Scanner(System.in);
        ArrayList<Integer> workingList;

        BHLayout layout;

        int seed ;
        int numcards ;
        int ranks ;
        int suits ;
        int piles ;

        if(args.length < 1) { printUsage(); return; };

        switch (args[0].toUpperCase()) {
            //
            // Add additional commands if you wish for your own testing/evaluation
            //

            case "GEN":
                if(args.length < 2) { printUsage(); return; };
                seed = Integer.parseInt(args[1]);
                numcards = (args.length < 3 ? 51 : Integer.parseInt(args[2])) ;
                ranks = (args.length < 4 ? 13 : Integer.parseInt(args[3])) ;
                suits = (args.length < 5 ? 4 : Integer.parseInt(args[4])) ;
                piles = (args.length < 6 ? 17 : Integer.parseInt(args[5])) ;
                layout = new BHLayout(ranks,suits,piles);
                layout.randomise(seed,numcards);
                layout.print();
                stdInScanner.close();
                return;

            case "CHECK":
                if (args.length < 2 ||
                        ( args[1].equals("-") && args.length < 3) ||
                        ( args[1].equals("-") && args[2].equals("-"))
                )
                { printUsage(); return; };
                if (args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }
                if (args.length < 3 || args[2].equals("-")) {
                    workingList = readIntArray(stdInScanner);
                }
                else {
                    workingList = readIntArray(args[2]);
                }

                // CODE FOR CHECKING BLACK HOLE SOLUTIONS

                Checker checker = new Checker(layout, workingList, false, false);
                checker.run();

                stdInScanner.close();
                return;

            case "CHECKWORM":
                if (args.length < 2 ||
                        ( args[1].equals("-") && args.length < 3) ||
                        ( args[1].equals("-") && args[2].equals("-"))
                )
                { printUsage(); return; };
                if (args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }
                if (args.length < 3 || args[2].equals("-")) {
                    workingList = readIntArray(stdInScanner);
                }
                else {
                    workingList = readIntArray(args[2]);
                }

                // CODE FOR CHECKING WORM HOLE SOLUTIONS

                WHLayout whLayout = new WHLayout(layout);

                checker = new Checker(whLayout, workingList, true, false);
                checker.run();

                stdInScanner.close();
                return;

            case "SOLVE":
                if (args.length<2 || args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }

                /// CODE FOR SOLVING BLACK HOLE

                Solver solver = new Solver(layout, false, false);
                solver.run();

                stdInScanner.close();
                return;

            case "SOLVEWORM":
                if (args.length<2 || args[1].equals("-")) {
                    layout = new BHLayout(readIntArray(stdInScanner));
                }
                else {
                    layout = new BHLayout(readIntArray(args[1]));
                }

                /// CODE FOR SOLVING WORM HOLE
                WHLayout wh = new WHLayout(layout);
                solver = new Solver(wh, true, false);
                solver.run();

                stdInScanner.close();
                return;

            default :
                printUsage();
                return;

        }


    }
}
package com.project;
import java.util.Scanner;
public class BoxMaker 
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        /*System.out.print("Enter dimensions (width,height,depth): ");

        String[] dimensions = scanner.nextLine().trim().split("\\s*,\\s*|\\s+"); // Split by comma or whitespace
        float width = Float.parseFloat(dimensions[0]) * 96f, height = Float.parseFloat(dimensions[1]) * 96f, depth = Float.parseFloat(dimensions[2]) * 96f; // Convert inches to pixels

        System.out.print("Enter number of tabs: ");
        int numTabs = scanner.nextInt();  //will need to check if input is correct type

        System.out.print("Enter two letter engraving: ");
        String engraving = scanner.next();  //will need to check if input is correct type

        System.out.print("Enter the file name: ");
        String fileName = scanner.next();  //will need to check if input is correct type

        String font = ""; // Will need to get from user input later
        Box test = new Box(width, height, depth, numTabs, engraving, font, fileName);
        scanner.close();
        test.print();*/

        Box test = new Box(1*96,1*96,1*96,5,"FA","Arial","test");
        test.printSingleSide();
    }
}
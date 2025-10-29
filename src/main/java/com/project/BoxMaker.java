package com.project;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoxMaker 
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        int measurement = MeasurementChooser(scanner); // Lets the user choose between different measurement units
        int boxType = BoxChooser(scanner); // Chooses the type of box to be created

        float conversion = switch (measurement) {
            case 1 -> 96f;
            case 2 -> 3.78f;
            default -> throw new IllegalArgumentException("Invalid measurement: " + measurement);
        };

        System.out.print("Enter the box's dimensions (width, height, depth): ");

        String[] dimensions = scanner.nextLine().trim().split("\\s*,\\s*|\\s+"); // Split by comma or whitespace
        
        // Convert pixels to the selected measurement system
        float width = Float.parseFloat(dimensions[0]) * conversion;
        float height = Float.parseFloat(dimensions[1]) * conversion;
        float depth = Float.parseFloat(dimensions[2]) * conversion;

        int numTabs = TabChooser(scanner); // Lets the user choose the number of tabs to be used

        String engraving = engravingWriter(scanner); // Lets the user choose any two alphebetical characters

        System.out.print("Enter the file name: ");
        String fileName = scanner.next();

        String font = ""; // Will need to get from user input later
        Box test = new Box(boxType, width, height, depth, numTabs, engraving, font, fileName);
        scanner.close();
        test.print();
    }

    private static int BoxChooser(Scanner scanner)
    {
        while(true)
        {
            System.out.print("Choose Box Type:\n" +
            "[1] Based Box\n" +
            "[2] Closed Box\n");

            int chosenBox = scanner.nextInt();
            scanner.nextLine();

            if(chosenBox > 2)
                System.err.println("Please use a valid number");
            else
                return chosenBox;
        }
    }

    private static int MeasurementChooser(Scanner scanner)
    {
        while (true) 
        { 
            System.out.print("Choose Measurement System:\n" +
            "[1] Inches\n" +
            "[2] Millimeters\n");

            int measurement = scanner.nextInt();
            scanner.nextLine(); // "Consume" enter key input

            if(measurement > 2)
                System.err.println("Please use a valid number");
            else
                return measurement;
        }
    }

    private static int TabChooser(Scanner scanner)
    {
        System.out.print("Enter number of tabs: ");

        int tabNum;
        try 
        {
            tabNum = scanner.nextInt();
        } 
        catch (Exception e) {
            System.err.println("Please use a valid whole number");
            scanner.nextLine();
            tabNum = TabChooser(scanner);
        }
        
        return tabNum;
    }

    private static String engravingWriter(Scanner scanner)
    {
        while (true) 
        { 
            System.out.print("Enter two letter engraving: ");
            String engraving = scanner.next();
            Pattern pattern = Pattern.compile(".*\\d.*");
            Matcher matcher = pattern.matcher(engraving);

            if(engraving.length() > 2 || matcher.matches()) // Ensures that the engraving is two characters and has no numbers
                System.err.println("Please use two characters");
            else
                return engraving;
        }
    }
}
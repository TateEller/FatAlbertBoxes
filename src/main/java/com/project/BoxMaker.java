package com.project;

import java.util.Scanner;

public class BoxMaker 
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter width: ");
        float width = scanner.nextFloat();  //will need to check if input is correct type

        System.out.print("Enter height: ");
        float height = scanner.nextFloat(); //will need to check if input is correct type

        System.out.print("Enter two letter engraving: ");
        String engraving = scanner.next();  //will need to check if input is correct type

        System.out.print("Enter the file name: ");
        String fileName = scanner.next();  //will need to check if input is correct type

        //float width = 50 , height = 50;
        //String engraving = "Fat A."; 
        String font = "";
        Box test = new Box(width, height, engraving, font, fileName);
        scanner.close();
        test.print();
    }
}

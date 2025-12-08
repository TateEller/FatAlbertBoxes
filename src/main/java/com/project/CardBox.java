package com.project;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; //Change standard settings to America

public class CardBox {


    public float width = 0.0f, height = 0.0f, depth = 0.0f;
    public String engraving = "", font, fileName;
    public float fontSize;
    public float conversion;
    private final float MinimumSize = 25.0f;
    private float strokeWidth = 0.1f * conversion, widthOfTabs = 10, heightOfTabs = 5, tabDepth = 11.0f; // Debug values

    // Create two variables to track the position of every piece we add
    private float positionX = 10;
    private float positionY = 10;

    // Create a variable to track the pieces created
    private int pieces = 0;
    
    // Create a variable for the space between the pieces
    private float spaceBetween;

    public CardBox(float width, float height, float depth, String engraving, String font, String fileName, float conversion)
    {
        if(width < MinimumSize || height < MinimumSize || depth < MinimumSize)
            throw new IllegalArgumentException("Width and Height must be at least " + MinimumSize);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.engraving = engraving;
        setFontSize(engraving);
        this.font = font;
        this.fileName = fileName;
        this.conversion = conversion;
        this.spaceBetween = 5 + 2*this.widthOfTabs * conversion;

        //measurements
        strokeWidth = 0.1f * conversion;
        widthOfTabs = 10 * conversion;
        heightOfTabs = 5 * conversion;
        tabDepth = 11.0f * conversion;
        positionX = 10 * conversion;
        positionY = 10 * conversion;
    }

    // Width Setter
    public void setWidth(float width)
    {
        if(width < MinimumSize)
            throw new IllegalArgumentException("Width must be at least " + MinimumSize);
        this.width = width;
    }

    // Height Setter
    public void setHeight(float height)
    {
        if(height < MinimumSize)
            throw new IllegalArgumentException("Height must be at least " + MinimumSize);
        this.height = height;
    }

    // Depth Setter
    public void setDepth(float depth)
    {
        if(depth < MinimumSize)
            throw new IllegalArgumentException("Depth must be at least " + MinimumSize);
        this.depth = depth;
    }

    // Engraving Setter
    public void setEngraving(String engraving)
    {
        this.engraving = engraving;
    }

    // Font Setter
    public void setFont(String font)
    {
        this.font = font;
    }

    // Font Size Setter
    public void setFontSize(String engraving)
    {
        float font_width = width < height ? width/2 : height/2;
        this.fontSize = Math.min(font_width / engraving.length(), this.height);
    }

    // File Name Setter
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    // Conversion Setter
    public void setConversion(float conversion)
    {
        this.conversion = conversion;

        strokeWidth = 0.1f * conversion;
        widthOfTabs = 10 * conversion;
        heightOfTabs = 5 * conversion;
        tabDepth = 11.0f * conversion;
        positionX = 10 * conversion;
        positionY = 10 * conversion;
        spaceBetween = 5 + 2*this.widthOfTabs * conversion;
    }

    public CardBox build()
    {
        return new CardBox(width, height, depth, engraving, font, fileName, conversion);
    }

    public void print()
    {
        Locale.setDefault(Locale.US);   // My standard settings are European settings, so please dont remove
        float padding = 10 * conversion; // Create a variable for padding
        
        // Base slightly larger than the rest
        float widthBase = width + 5 * heightOfTabs;
        float depthBase = depth + 5 * widthOfTabs;

        // Create a variable for the file_width and file_height
        float file_width = widthBase + 2*width + 2 * spaceBetween + 2 * padding;
        float file_height;
        if(depthBase > height)
        {
            file_height = depthBase * 2 + spaceBetween + 2 * padding;
        }
        else
            file_height = height * 2 + spaceBetween + 2 * padding;
        // Break the string down
        String svgOpener = String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" 
            width="%f" height="%f"
            viewBox="0 0 %f %f">
            <rect width="100%%" height="100%%" fill="white"/>
            """, file_width, file_height, file_width, file_height);

        String svgCloser = """
            </svg>
            """;
        String svgContent = "";
        
        //First Row
        
        //Second Row

        //Third Row

        //Fourth Row

        //Fifth Row
        
        
        // Create the svg file
        String svgWhole = svgOpener + svgContent + svgCloser;
        File file = new File("exports/" + fileName + ".svg");
        try (FileWriter writer = new FileWriter(file)) 
        {
            writer.write(svgWhole);
            System.out.println("SVG file created:" + fileName + ".svg");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private String AddDividers(float sideX, float sideY){
        /*
        StringBuilder path = new StringBuilder();
        svgContent += String.format("""
                <path d="M %.2f %.2f
                        a 1 1 0 1 %f %f 0"
                    fill="none" stroke="black" stroke-width="%.2f"/>
                """,
                positionX,
                positionY,
                100.0f,
                500.0f,
                strokeWidth
            );
        svgContent += String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                """, path.toString(), 0.1); */
        
        return "";
    }



    public static void main(String[] args) {
        CardBox box = new CardBox(100, 150, 50, "AB", "Arial", "cardbox_test", 2.83465f);
        box.print();
    }
}

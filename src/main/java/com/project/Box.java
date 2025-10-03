package com.project;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Box
{
    private float width = 0.0f, height = 0.0f;
    private String engraving, font, fileName;
    private final float MinimumSize = 25.0f, strokeWidth = 2.0f; // Debug values

    //create two variables to track the position of every piece we add
    private float positionX = 10;
    private float positionY = 10;
    //create a variable to track the pieces created
    private int pieces = 0;
    //create a variable for the space between the pieces
    private final int spaceBetween = 10;

    public Box(float width, float height, String engraving, String font, String fileName)
    {
        if(width < MinimumSize || height < MinimumSize)
            throw new IllegalArgumentException("Width and Height must be at least " + MinimumSize);
        this.width = width;
        this.height = height;
        this.engraving = engraving;
        this.font = font;
        this.fileName = fileName;
    }
    
    public void setWidth(float width)
    {
        if(width < MinimumSize)
            throw new IllegalArgumentException("Width must be at least " + MinimumSize);
        this.width = width;
    }

    public void setHeight(float height)
    {
        if(height < MinimumSize)
            throw new IllegalArgumentException("Height must be at least " + MinimumSize);
        this.height = height;
    }

    public void setEngraving(String engraving)
    {
        this.engraving = engraving;
    }

    public void setFont(String font)
    {
        this.font = font;
    }

    public Box build()
    {
        return new Box(width, height, engraving, font, fileName);
    }

    public void print()
    {
        //create a variable for padding
        float padding = 10;
        //create a variable for the file_width and file_height
        float file_width = width*3 + 2*spaceBetween + 2*padding; //padding is for the space between the pieces and
        float file_height = height*2 + spaceBetween + 2*padding;
        //break the string down
        String svgOpener = String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="%f" height="%f">
            """, file_width, file_height);
        String svgCloser = """
            </svg>
            """;
        String svgContent = "";
        //we need to add one base
        svgContent += addBase();
        //we need to add four sides
        svgContent += addSide1();
        svgContent += addSide1();
        svgContent += addSide2();
        svgContent += addSide2();
        //we need to add the top
        svgContent += addTop();
        //creating the svg file
        String svgWhole = svgOpener + svgContent + svgCloser;
        File file = new File("exports/" + fileName + ".svg");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(svgWhole);
            System.out.println("SVG file created:" + fileName + ".svg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String addBase()
    {
        pieces += 1;
        //look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String svgContent = String.format("""
            <rect x="%f" y="%f" width="%.1f" height="%.1f" fill="lightblue" stroke="navy" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, positionX, positionY, width, height, strokeWidth, xCenter, yCenter, engraving); //add the variables with %f (for floats), %s (for strings)

        if(pieces < 3)
            positionX += spaceBetween + width;
        else
        {
            positionY += spaceBetween + height;
            positionX = 10;
            pieces = 0;
        }

        return svgContent;
    }

    private String addSide1()
    {
        pieces += 1;
        //look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String svgContent = String.format("""
            <rect x="%f" y="%f" width="%.1f" height="%.1f" fill="lightblue" stroke="navy" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, positionX, positionY, width, height, strokeWidth, xCenter, yCenter, engraving); //add the variables with %f (for floats), %s (for strings)

        if(pieces < 3)
            positionX += spaceBetween + width;
        else
        {
            positionY += spaceBetween + height;
            positionX = 10;
            pieces = 0;
        }

        return svgContent;
    }

    private String addSide2()
    {
        pieces += 1;
        //look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String svgContent = String.format("""
            <rect x="%f" y="%f" width="%.1f" height="%.1f" fill="lightblue" stroke="navy" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, positionX, positionY, width, height, strokeWidth, xCenter, yCenter, engraving); //add the variables with %f (for floats), %s (for strings)

        if(pieces < 3)
            positionX += spaceBetween + width;
        else
        {
            positionY += spaceBetween + height;
            positionX = 10;
            pieces = 0;
        }

        return svgContent;
    }

    private String addTop()
    {
        pieces += 1;
        //look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String svgContent = String.format("""
            <rect x="%f" y="%f" width="%.1f" height="%.1f" fill="lightblue" stroke="navy" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, positionX, positionY, width, height, strokeWidth, xCenter, yCenter, engraving); //add the variables with %f (for floats), %s (for strings)

        if(pieces < 3)
            positionX += spaceBetween + width;
        else
        {
            positionY += spaceBetween + height;
            positionX = 10;
            pieces = 0;
        }

        return svgContent;
    }
}
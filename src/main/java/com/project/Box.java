package com.project;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Box
{
    private float width = 0.0f, height = 0.0f;
    private String engraving, font;
    private final float MinumumSize = 25.0f, strokeWidth = 2.0f; // Debug values

    public Box(float width, float height, String engraving, String font)
    {
        if(width < MinumumSize || height < MinumumSize)
            throw new IllegalArgumentException("Width and Height must be at least " + MinumumSize);
        this.width = width;
        this.height = height;
        this.engraving = engraving;
        this.font = font;
    }
    
    public void setWidth(float width)
    {
        if(width < MinumumSize)
            throw new IllegalArgumentException("Width must be at least " + MinumumSize);
        this.width = width;
    }

    public void setHeight(float height)
    {
        if(height < MinumumSize)
            throw new IllegalArgumentException("Height must be at least " + MinumumSize);
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
        return new Box(width, height, engraving, font);
    }

    public void print()
    {
        //break the string down
        String svgOpener = String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="400" height="400">
            """);
        String svgCloser = """
            </svg>
            """;

        //look for the center of the box
        float xCenter = 10 + width / 2;
        float yCenter = 10 + height / 2;

        String svgContent = String.format("""
            <rect x="10" y="10" width="%.1f" height="%.1f" fill="lightblue" stroke="navy" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, width, height, strokeWidth, xCenter, yCenter, engraving);
        //add the variables with %f (for floats), %s (for strings)
        String svgWhole = svgOpener + svgContent + svgCloser;
        File file = new File("exports/example.svg");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(svgWhole);
            System.out.println("SVG file created: example.svg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String addBase()
    {

        return "";
    }

    private String addSide1()
    {

        return "";
    }

    private String addSide2()
    {
        return "";
    }

    private String top()
    {

        return "";
    }
}
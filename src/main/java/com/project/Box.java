package com.project;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; //change standard settings to America

public class Box
{
    public float width = 0.0f, height = 0.0f, depth = 0.0f;
    public int numTabs;
    public String engraving, font, fileName;
    private final float MinimumSize = 25.0f, strokeWidth = 0.05f, widthOfKnobs = 4, heightOfKnobs = 1; // Debug values

    // Create two variables to track the position of every piece we add
    private float positionX = 10;
    private float positionY = 10;
    // Create a variable to track the pieces created
    private int pieces = 0;
    // Create a variable for the space between the pieces
    private final int spaceBetween = 10;

    public Box(float width, float height, float depth, int numTabs, String engraving, String font, String fileName)
    {
        if(width < MinimumSize || height < MinimumSize)
            throw new IllegalArgumentException("Width and Height must be at least " + MinimumSize);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.numTabs = numTabs;
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

    public void setDepth(float depth)
    {
        if(depth < MinimumSize)
            throw new IllegalArgumentException("Depth must be at least " + MinimumSize);
        this.depth = depth;
    }

    public void setEngraving(String engraving)
    {
        this.engraving = engraving;
    }

    public void setFont(String font)
    {
        this.font = font;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Box build()
    {
        return new Box(width, height, depth, numTabs, engraving, font, fileName);
    }

    public void print()
    {
        Locale.setDefault(Locale.US);   //my standard settings are European settings, so please dont remove
        //create a variable for padding
        float padding = 10;
        // Create a variable for the file_width and file_height
        float file_width = width * 3 + 2 * spaceBetween + 2 * padding; // Padding is for the space between the pieces
        float file_height = height * 2 + spaceBetween + 2 * padding;
        // Break the string down
        String svgOpener = String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="%f" height="%f">
            <rect width="100%%" height="100%%" fill="white"/>
            """, file_width, file_height);

        String svgCloser = """
            </svg>
            """;
        String svgContent = "";
        // One base
        svgContent += addBase();
        // Add four sides
        svgContent += addSideA();
        svgContent += addSideA();
        svgContent += addSideB();
        svgContent += addSideB();
        // Add the top
        svgContent += addTop();
        // Creating the svg file
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

    private String addBase()
    {
        pieces += 1;

        // Base slightly larger than the rest
        float widthBase = width + 2 * widthOfKnobs;
        float heightBase = height + 2 * widthOfKnobs;
        float xCenter = positionX + widthBase / 2;
        float yCenter = positionY + heightBase / 2;

        // Main base rectangle
        StringBuilder svgContent = new StringBuilder(String.format("""
            <rect x="%f" y="%f" width="%f" height="%f" fill="none" stroke="black" stroke-width="%f" />
            <text x="%f" y="%f" text-anchor="middle" font-size="10" dominant-baseline="middle">%s</text>
            """, positionX, positionY, widthBase, heightBase, strokeWidth, xCenter, yCenter, engraving));

        // --- Number of holes per side ---
        int nHorizontal = (int)(widthBase / (2*widthOfKnobs)); //subtract by 2 becuase of the edges
        int nVertical = (int)(heightBase / (2*widthOfKnobs));
        //caculate the space we need no the sides
        float margin = (widthBase - (nHorizontal*widthOfKnobs*2)) /2;
        nHorizontal -= 2;
        nVertical -= 2;
        margin += 2*widthOfKnobs + widthOfKnobs/2; //important
        System.out.println("Margin: " + margin + " nHorizontal: " + nHorizontal + "widthBase: " + widthBase);


        // --- Top edge (inside the base) ---
        float yTop = positionY + widthOfKnobs;
        float xTop = positionX + margin;
        for (int i = 0; i < nHorizontal; i++) {
            svgContent.append(basePrintSquares(xTop, yTop, widthOfKnobs, heightOfKnobs));
            xTop += 2*widthOfKnobs;
        }

        // --- Bottom edge (inside the base) ---
        float yBottom = positionY + heightBase - heightOfKnobs - widthOfKnobs;
        float xBottom = positionX + margin;
        while (xBottom < positionX + widthBase - margin) {
            svgContent.append(basePrintSquares(xBottom, yBottom, widthOfKnobs, heightOfKnobs));
            xBottom += 2*widthOfKnobs;
        }

        // --- Left edge (inside the base) ---
        float xLeft = positionX + widthOfKnobs; // move inside
        float yLeft = positionY + margin;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xLeft, yLeft, heightOfKnobs, widthOfKnobs));
            yLeft += 2*widthOfKnobs;
        }

        // --- Right edge (inside the base) ---
        float xRight = positionX + widthBase - heightOfKnobs - widthOfKnobs; // move left a bit inside
        float yRight = positionY + margin;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xRight, yRight, heightOfKnobs, widthOfKnobs));
            yRight += 2*widthOfKnobs;
        }

        // Move position for next piece
        if (pieces < 3)
            positionX += spaceBetween + widthBase;
        else {
            positionY += spaceBetween + heightBase;
            positionX = 10;
            pieces = 0;
        }

        return svgContent.toString();
    }

    private String addSideA()
    {
        pieces += 1;
        // Look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String pathData = GenerateRectanglePath(positionX, positionY, width, height, 4.0f, numTabs);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%.1f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, engraving);

        if (pieces < 3) 
        {
            positionX += spaceBetween + width;
        } 
        else 
        {
            positionY += spaceBetween + height;
            positionX = 10;
            pieces = 0;
        }
        return svgContent;
    }

    private String addSideB()
    {
        pieces += 1;
        // Look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + height / 2;

        String pathData = GenerateRectanglePath(positionX, positionY, width, height, -4.0f, numTabs);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%.1f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, engraving);

        if (pieces < 3) 
        {
            positionX += spaceBetween + width;
        } 
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
        // Look for the center of the box
        float xCenter = positionX + width / 2;
        float yCenter = positionY + depth / 2;

        String svgContent = String.format("""
            <rect x="%.1f" y="%.1f" width="%.1f" height="%.1f" fill="none" stroke="black" stroke-width="%.1f"/>
            <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, positionX, positionY, width, depth, strokeWidth, xCenter, yCenter, engraving); //add the variables with %f (for floats), %s (for strings)

        if (pieces < 3)
            positionX += spaceBetween + width;
        else
        {
            positionY += spaceBetween + depth;
            positionX = 10;
            pieces = 0;
        }

        return svgContent;
    }

    public String GenerateRectanglePath(float x, float y, float width, float height, float tabDepth, int numTabs)
    {
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", x, y)); // Start at top-left corner

        float seg = width / numTabs;

        // Top edge
        for (int i = 0; i < numTabs; i++) 
        {
            if (i % 2 == 0) // This is a gap
            { 
                path.append(String.format("h %f ", seg));
            } 
            else // This is a tab
            { 
                path.append(String.format("v %f h %f v %f ", -tabDepth, seg, tabDepth));
            }
        }

        // Right edge
        for (int i = 0; i < numTabs; i++) 
        {
            if (i % 2 == 0) 
            {
                path.append(String.format("v %f ", seg));
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, seg, -tabDepth));
            }
        }

        // Bottom edge
        for (int i = 0; i < numTabs; i++) {
            if (i % 2 == 0) 
            {
                path.append(String.format("h %f ", -seg));
            } 
            else 
            {
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg, -tabDepth));
            }
        }

        // Left edge
        for (int i = 0; i < numTabs; i++) 
        {
            if (i % 2 == 0) 
            {
                path.append(String.format("v %f ", -seg));
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -seg, tabDepth));
            }
        }
        path.append("Z"); // Close the path
        return path.toString(); // Return the constructed path
    }

    // Add this method to fix the compilation error
    private String basePrintSquares(float x, float y, float w, float h) {
        return String.format(
            "<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
            x, y, w, h, strokeWidth
        );
    }
}
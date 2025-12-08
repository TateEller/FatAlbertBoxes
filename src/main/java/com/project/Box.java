package com.project;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; //Change standard settings to America

public class Box
{
    public float width, height, depth;
    public int numTabs, boxType;
    public String engraving = "", font, fileName;
    public float fontSize;
    public int engSides;
    public float conversion;
    private final float MinimumSize = 1f;
    private float strokeWidth, widthOfTabs, heightOfTabs; // Debug values
    private float tightness;

    // Create two variables to track the position of every piece we add
    private float positionX = 10;
    private float positionY = 10;

    // Create a variable to track the pieces created
    private int pieces = 0;
    
    // Create a variable for the space between the pieces
    private float spaceBetween;

    public Box(int boxType, float width, float height, float depth, int numTabs, String engraving, String font, int engSides, String fileName, float conversion, float tightness)
    {
        this.conversion = conversion;

        if(width < MinimumSize || height < MinimumSize || depth < MinimumSize)  //should already be passed above min value
            throw new IllegalArgumentException("Width and Height must be at least " + MinimumSize);
        this.boxType = boxType;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.numTabs = numTabs;
        this.engraving = engraving;
        setFontSize(engraving);
        this.engSides = engSides;
        this.font = font;
        this.fileName = fileName;
        this.tightness = tightness;

        //measurements
        if(conversion == 72f) //inch specific adjustment
        {
            //strokeWidth = 0.001f * conversion;
            strokeWidth = 0.003937008f * conversion;
            heightOfTabs = 0.17716535f * conversion;  // should be the thickness of wood
            widthOfTabs = 0.5f * conversion;
            positionX = 0.4f * conversion;
            positionY = 0.4f * conversion;
            this.spaceBetween = 1.37f * conversion;
        }
        else
        {
            //strokeWidth = 0.00254f * conversion;
            strokeWidth = 0.1f * conversion;
            heightOfTabs = 4.5f * conversion;  // should be the thickness of wood
            widthOfTabs = 12.7f * conversion;
            positionX = 10 * conversion;
            positionY = 10 * conversion;
            this.spaceBetween = 35 * conversion;
        }
    }

    // Width Setter
    public void setWidth(float width)
    {
        if(conversion == 72f)
        {
            if(width < 1.9f)
                throw new IllegalArgumentException("Width must be at least 1.9 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(width < 49f)
                throw new IllegalArgumentException("Width must be at least 49 millimeters");
        }

        this.width = width;
    }

    // Height Setter
    public void setHeight(float height)
    {
        if(conversion == 72f)
        {
            if(height < 1.9f)
                throw new IllegalArgumentException("Height must be at least 1.9 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(height < 49f)
                throw new IllegalArgumentException("Height must be at least 49 millimeters");
        }
        this.height = height;
    }

    // Depth Setter
    public void setDepth(float depth)
    {
        if(conversion == 72f)
        {
            if(depth < 1.9f)
                throw new IllegalArgumentException("Depth must be at least 1.9 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(depth < 49f)
                throw new IllegalArgumentException("Depth must be at least 49 millimeters");
        }
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

    public void setEngravingSides(int engSides)
    {
        this.engSides = engSides;
    }

    // File Name Setter
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    // Tab Setter
    public void setTabs(int numTabs)
    {
        this.numTabs = numTabs;
        //this.widthOfKnobs = 12;
    }

    // Conversion Setter
    public void setConversion(float conversion)
    {
        this.conversion = conversion;

        strokeWidth = 0.1f * conversion;
        heightOfTabs = 3 * conversion;  // should be the thickness of wood 
        widthOfTabs = 10 * conversion;
        positionX = 10 * conversion;
        positionY = 10 * conversion;
        spaceBetween = 15 * conversion;
    }

    public Box build()
    {
        return new Box(boxType, width, height, depth, numTabs, engraving, font, engSides, fileName, conversion, tightness);
    }

    public void print()
    {
        this.width = width - (2 * heightOfTabs);
        this.depth = depth - (2 * heightOfTabs);
        this.height = height - (2.5f * heightOfTabs);


        System.out.println("Measurments: " + width + "x" + height + "x" + depth);
        Locale.setDefault(Locale.US);   // My standard settings are European settings, so please dont remove
        
         float padding;
        if(conversion == 72f)
            padding = 0.4f * conversion; // Create a variable for padding
        else
            padding = 10 * conversion; // Create a variable for padding
        
        // Base slightly larger than the rest
        float widthBase = width + 7 * heightOfTabs;
        float depthBase = depth + 7 * heightOfTabs;
        float heightBase = height + 7 * heightOfTabs;

        // Create a variable for the file_width and file_height
        float file_width = Math.max(3*widthBase, widthBase + 2*depthBase) + 6 * padding;
        float file_height = Math.max(2*heightBase, depthBase + heightBase) + 6 * padding;
            
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
        
        // Add the base
        if(boxType == 1){
            System.out.println("Adding base... ");
            svgContent += addBase();
        } // Based box
            
        else if(boxType == 2){
            System.out.println("No base added... ");
            svgContent += addSide(true, false, true); // Add bottom side without engraving
        } // Closed box (no base)

        
        // Add the top
        svgContent += addTop();

        // Add four sides with engraving switch/case
        switch (engSides) {
            case 1:
                svgContent += addSide(true, true, false);
                svgContent += addSide(true, false, false);
                svgContent += addSide(false, false, false);
                svgContent += addSide(false, false ,false);
                break;
            case 2:
                svgContent += addSide(true, true, false);
                svgContent += addSide(true, true, false);
                svgContent += addSide(false, false, false);
                svgContent += addSide(false, false ,false);
                break;
            case 3:
                svgContent += addSide(true, true, false);
                svgContent += addSide(true, true, false);
                svgContent += addSide(false, true, false);
                svgContent += addSide(false, false ,false);
                break;
            case 4:
            default:
                svgContent += addSide(true, true, false);
                svgContent += addSide(true, true, false);
                svgContent += addSide(false, true, false);
                svgContent += addSide(false, true ,false);
                break;
        }
        
        // Create the svg file
        String svgWhole = svgOpener + svgContent + svgCloser;

        //Choose where to save the file
        //File file = new File("exports/" + fileName + ".svg");   //Save to exports folder in project
        File file = new File(getDownloadsFolder(), fileName + ".svg");   //Save to user downloads folder

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

    public static File getDownloadsFolder(){
        //Get user downloads folder
        String home = System.getProperty("user.home");
        
        // Windows
        File downloads = new File(home + "Downloads");
        if (downloads.exists()) { return downloads; }

        //Mac & Linux
        downloads = new File(home + "/Downloads");
        if (downloads.exists()) { return downloads; }

        return new File(home); // if no downloads folder found, return home
    }

    private String addSide(boolean isSideA, boolean hasEngraving, boolean isBottom)
    {
        System.out.println("width: " + width);
        pieces += 1;

        float dimension;
        float side;
        if(isBottom)
        {
            // Bottom always uses width × depth no matter what isSideA is
            dimension = width;
            side = depth;
        }
        else
        {
            // Normal sides
            dimension = isSideA ? width : depth;
            side = height;
        }
        System.out.print("Dimension: " + dimension + " WidthofTabs: " + widthOfTabs + "\n");
        String sideEngraving = hasEngraving ? engraving : ""; // Removes the engraving if hasEngraving is false

        // Look for the center of the box
        float xCenter = positionX + dimension / 2 + 2*heightOfTabs;
        float yCenter = positionY + side / 2 + 2*heightOfTabs;

        // Number of tabs per side
        int nHorizontal = (int)(dimension*2 / (2*widthOfTabs));
        System.out.println("nHorizontal " + nHorizontal);
        int nVertical = (int)(side*2 / (2*widthOfTabs));

        // Caculate the space we need on the sides
        float marginX = heightOfTabs + (dimension*2 - (nHorizontal*widthOfTabs*2)) / 2;
        float marginY = heightOfTabs + (side*2 - (nVertical*widthOfTabs*2)) / 2;
        
        if (!isSideA) { // This block is only for Side B
            if(marginX - heightOfTabs >= 0)
                marginX = marginX - heightOfTabs;
        }

        // Ensures an odd number of tabs
        if(nHorizontal % 2 == 0)
        {
            nHorizontal -= 1;
            xCenter -= heightOfTabs;
            System.out.println("X_Center " + xCenter);
        }
        if(nVertical % 2 == 0)
        {
            nVertical -= 1;
            yCenter -= heightOfTabs;
        }

        // Choose the respective side generator based off the isSideA bool
        String pathData;
        if(isSideA)
        {
            pathData = GenerateRectanglePathSideA(positionX, positionY, dimension, side, heightOfTabs, nHorizontal, nVertical, marginX, marginY, isBottom);
        }
        else
        {
            pathData = GenerateRectanglePathSideB(positionX, positionY, dimension, side, heightOfTabs, nHorizontal, nVertical, marginX, marginY);
        }

        String svgContent = String.format("""
            <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
            <text x="%.1f" y="%.1f" font-size="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
            """, pathData, strokeWidth, xCenter, yCenter, fontSize, sideEngraving);

        // Update the position of the next piece
        if (pieces < 3) 
        {
            positionX += spaceBetween + dimension;
        } 
        else 
        {
            // This 'else' block has different logic for A and B
            if (isSideA) 
            {
                float depthBase = depth + 2 * widthOfTabs;
                if(depthBase > side)
                    positionY += spaceBetween + depthBase;
                else
                    positionY += spaceBetween + side;
            }
            else 
            { // This is Side B's simpler logic
                positionY += spaceBetween + side;
            }
            if(conversion == 72f)
                positionX = 0.4f * conversion;
            else
                positionX = 10 * conversion;
            pieces = 0;
        }

        return svgContent;
    }

    private String addBase()
    {
        pieces += 1;

        // --- Number of tabs per side ---
        int nHorizontal = (int)(width*2 / (2*widthOfTabs));
        int nVertical = (int)(depth*2 / (2*widthOfTabs));

        //caculate the space we need on the sides
        if(nHorizontal % 2 == 0)
            nHorizontal -= 1;
        if(nVertical % 2 == 0)
            nVertical -= 1;
        nVertical /=2;

        nHorizontal /= 2;

        // Base slightly larger than the rest
        float widthBase = width + 2*widthOfTabs - (tightness-1) * nHorizontal + widthOfTabs/9.5f;
        float depthBase = depth + 2*widthOfTabs - (tightness-1) * nVertical + widthOfTabs/9.5f;
        float xCenter = positionX + widthBase / 2;
        float yCenter = positionY + depthBase / 2;

        // Main base rectangle
        StringBuilder svgContent = new StringBuilder(String.format("""
            <rect x="%f" y="%f" width="%f" height="%f" fill="none" stroke="black" stroke-width="%f"/>
            <text x="%f" y="%f" text-anchor="middle" font-size="10" dominant-baseline="middle">%s</text>
            """, positionX, positionY, widthBase, depthBase, strokeWidth, xCenter, yCenter, ""));  //having ("") at the end stops it from engraving on the bottom

        
        //caculate the space we need no the sides
        float marginX = (widthBase - (nHorizontal*widthOfTabs*2)+ widthOfTabs) /2;
        float marginY = (depthBase - (nVertical*widthOfTabs*2)+ widthOfTabs) /2;

        // --- Top edge (inside the base) ---
        float yTop = positionY + widthOfTabs - heightOfTabs;
        float xTop = positionX + marginX;
        for (int i = 0; i < nHorizontal; i++) {
            svgContent.append(basePrintSquares(xTop, yTop, widthOfTabs, heightOfTabs));
            xTop += 2*widthOfTabs;
        }

        // --- Bottom edge (inside the base) ---
        float yBottom = positionY + depthBase - widthOfTabs;
        float xBottom = positionX + marginX;
        for (int i = 0; i < nHorizontal; i++) {
            svgContent.append(basePrintSquares(xBottom, yBottom, widthOfTabs, heightOfTabs));
            xBottom += 2*widthOfTabs;
        }

        // --- Left edge (inside the base) ---
        float xLeft = positionX + widthOfTabs - heightOfTabs; // move inside
        float yLeft = positionY + marginY;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xLeft, yLeft, heightOfTabs, widthOfTabs));
            yLeft += 2*widthOfTabs;
        }

        // --- Right edge (inside the base) ---
        float xRight = positionX + widthBase - widthOfTabs; // move left a bit inside
        float yRight = positionY + marginY;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xRight, yRight, heightOfTabs, widthOfTabs));
            yRight += 2*widthOfTabs;
        }

        // Move position for next piece
        if (pieces < 3)
            positionX += spaceBetween + widthBase - widthOfTabs;

        return svgContent.toString();
    }

    private String addTop()
    {
        pieces += 1;
        // Look for the center of the box

        // Number of tabs per side
        int nHorizontal = (int)(width*2 / (2*widthOfTabs));
        int nVertical = (int)(depth*2 / (2*widthOfTabs));

        // Caculate the space we need on the sides
        float marginX = (width*2 - (nHorizontal*widthOfTabs*2)) / 2;
        float marginY = (depth*2 - (nVertical*widthOfTabs*2)) / 2;

        if(nHorizontal % 2 == 0)
            nHorizontal -= 1;
        if(nVertical % 2 == 0)
            nVertical -= 1;
        //System.out.println(marginX + " " + nHorizontal + " " + nVertical);
        // Look for the center of the box
        float xCenter = positionX + ((nHorizontal*widthOfTabs)+marginX*2) / 2;
        float yCenter = positionY + ((nVertical*widthOfTabs)+marginY*2) / 2;

        //String pathData = GenerateRectanglePath(positionX, positionY, width, height, -tabDepth, numTabs);
        String pathData = GenerateRectanglePathTop(positionX, positionY, width, depth, heightOfTabs, nHorizontal, nVertical, marginX, marginY);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" font-size="10" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, ""); //having ("") and the end stops it engraving on the top

        if (pieces < 3) 
        {
            positionX += spaceBetween + width;
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

    // Prints holes for the base plate
    private String basePrintSquares(float x, float y, float w, float h) {
        return String.format(
            "<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
            x, y, w, h, strokeWidth
        );
    }

    // Debug function for printing a single side
    public void printSingleSide() 
    {
        Locale.setDefault(Locale.US);
        float padding = 10;
        float file_width = width + 2 * padding;
        float file_height = height + 2 * padding;

        String svgOpener = String.format("""
            <svg xmlns="http://www.w3.org/2000/svg" width="%.1f" height="%.1f">
            <rect width="100%%" height="100%%" fill="white"/>
            """, file_width, file_height);

        // Reset position for single side
        float sideX = padding;
        float sideY = padding;
        float xCenter = sideX + width / 2;
        float yCenter = sideY + height / 2;

        String pathData = GenerateRectanglePath(sideX, sideY, width, height, 4.0f, numTabs);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, engraving);

        String svgCloser = "</svg>";

        String svgWhole = svgOpener + svgContent + svgCloser;
        File file = new File("exports/" + fileName + "_sideA.svg");
        try (FileWriter writer = new FileWriter(file)) 
        {
            writer.write(svgWhole);
            System.out.println("SVG file created: " + fileName + "_sideA.svg");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    //Generates a side with the right measurements
    public String GenerateRectanglePathSideA(float x, float y, float width, float height, float tabDepth, int nHorizontal, int nVertical, float marginX, float marginY, boolean bottom)
    {
        System.out.println(x + " " + y + " " + width + " " + height + " " + marginX + " " + nHorizontal + " " + nVertical);
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", x, y)); // Start at top-left corner

        float seg = widthOfTabs;

        //path.append(String.format("v %fv %f", 1000.0f, -1000.0f)); //debug line
        // Top edge
        //always starts with a tab
        path.append(String.format("h %f ", marginX+seg)); //margin+seg
        //path.append(String.format("v %fv %f", 1000.0f, -1000.0f)); //debug line
        for (int i = 0; i < nHorizontal-2; i++)
        {
            if (i % 2 == 0) // This is a gab
            {
                path.append(String.format("v %f h %f v %f ", tabDepth, seg, -tabDepth));
            } 
            else // This is a tab
            {
                path.append(String.format("h %f ", seg*tightness));
            }
        }
        //end with a tap
        path.append(String.format("h %f ",marginX+seg)); //margin+seg
        

        // Right edge
        //always start with a tab
        path.append(String.format("v %f ", marginY));
        for (int i = 0; i < nVertical; i++)
        {
            if (i % 2 == 0)
            {
                path.append(String.format("v %f ", seg*tightness)); //this is a tab
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, seg, tabDepth));
            }
        }
        //add the margin in the end
        path.append(String.format("v %f ", marginY));

        // Bottom edge
        //add the margin
        path.append(String.format("h %f ", -marginX));
        for (int i = 0; i < nHorizontal; i++)
        {
            if (i % 2 == 0) //always starts with a gap
            {
                path.append(String.format("h %f ", -seg));
            } 
            else 
            {
                if(!bottom)
                    path.append(String.format("v %f h %f v %f ", tabDepth, -seg*tightness, -tabDepth));
                else
                    path.append(String.format("v %f h %f v %f ", -tabDepth, -seg*tightness, tabDepth));
                //System.out.println(i);
            }
        }
        //add the margin at the end
        path.append(String.format("h %f ", -marginX));


        // Left edge

        //add margin
        path.append(String.format("v %f ", -marginY));
        for (int i = 0; i < nVertical; i++) 
        {
            if (i % 2 == 0) //always starts with a gap
            {
                path.append(String.format("v %f ", -seg*tightness));
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, -seg, -tabDepth));
            }
        }

        //add margin in the end
        path.append(String.format("v %f ", -marginY));


        path.append("Z"); // Close the path
        return path.toString(); // Return the constructed path
    }

    //almost same function as above, the only thing I changed
    //were the direction of the tabs (I changed the minus sign)
    //and I reduced the width with two heightOfKnobs
    public String GenerateRectanglePathSideB(float x, float y, float width, float height, float tabDepth, int nHorizontal, int nVertical, float marginX, float marginY)
    {
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", x+heightOfTabs, y)); // Start at top-left corner
        //move it one heightOfKnobs so it fits with the other tiles

        float seg = widthOfTabs;

        // Top edge
        //always starts with a tab
        path.append(String.format("h %f ", marginX+seg)); //margin+seg
        for (int i = 0; i < nHorizontal-2; i++)
        {
            if (i % 2 == 0) // This is a gab
            {
                path.append(String.format("v %f h %f v %f ", tabDepth, seg, -tabDepth));
            } 
            else // This is a tab
            {
                path.append(String.format("h %f ", seg*tightness));
            }
        }
        //end with a tap
        path.append(String.format("h %f ",marginX+seg)); //margin+seg

        // Right edge
        //always start with a tab
        path.append(String.format("v %f ", marginY));
        for (int i = 0; i < nVertical; i++)
        {
            if (i % 2 == 0)
            {
                path.append(String.format("v %f ", seg)); //this is a tab
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, seg*tightness, -tabDepth));
            }
        }
        //add the margin in the end
        path.append(String.format("v %f ", marginY));

        // Bottom edge
        //add the margin
        path.append(String.format("h %f ", -marginX));
        for (int i = 0; i < nHorizontal; i++)
        {
            if (i % 2 == 0 || i == 0) //always starts with a gap
            {
                path.append(String.format("h %f ", -seg));
            } 
            else 
            {
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg*tightness, -tabDepth));
            }
        }
        //add the margin at the end
        path.append(String.format("h %f ", -marginX));

        // Left edge

        //add margin
        path.append(String.format("v %f ", -marginY));
        for (int i = 0; i < nVertical; i++) 
        {
            if (i % 2 == 0) //always starts with a gap
            {
                path.append(String.format("v %f ", -seg));
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -seg*tightness, tabDepth));
            }
        }

        //add margin in the end
        path.append(String.format("v %f ", -marginY));


        path.append("Z"); // Close the path
        return path.toString(); // Return the constructed path
    }












    //I created a function to test some things out
    public String GenerateRectanglePathTop(float x, float y, float width, float height, float tabDepth, int nHorizontal, int nVertical, float marginX, float marginY)
    {
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", x+heightOfTabs, y+heightOfTabs)); // Start at top-left corner

        float seg = widthOfTabs;

        // Top edge
        //always starts with a tab
        path.append(String.format("h %f ", marginX+seg)); //margin+seg
        for (int i = 0; i < nHorizontal-2; i++)
        {
            if (i % 2 == 0) // This is a gab
            {
                path.append(String.format("v %f h %f v %f ", -tabDepth, seg*tightness, tabDepth));
            } 
            else // This is a tab
            {
                path.append(String.format("h %f ", seg));
            }
        }
        //end with a tap
        path.append(String.format("h %f ",marginX+seg)); //margin+seg
        

        // Right edge
        //always start with a gab
        path.append(String.format("v %f ", marginY));
        for (int i = 0; i < nVertical; i++)
        {
            if (i % 2 == 0)
            {
                path.append(String.format("v %f ", seg)); //this is a tab
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, seg*tightness, -tabDepth));
            }
        }
        //add the margin in the end
        path.append(String.format("v %f ", marginY));

        // Bottom edge
        //add the margin
        path.append(String.format("h %f ", -marginX));
        for (int i = 0; i < nHorizontal; i++)
        {
            if (i % 2 == 0 || i == 0) //always starts with a gap
            {
                path.append(String.format("h %f ", -seg));
            } 
            else 
            {
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg*tightness, -tabDepth));
            }
        }
        //add the margin at the end
        path.append(String.format("h %f ", -marginX));


        // Left edge

        //add margin
        path.append(String.format("v %f ", -marginY));
        for (int i = 0; i < nVertical; i++) 
        {
            if (i % 2 == 0) //always starts with a gap
            {
                path.append(String.format("v %f ", -seg));
            } 
            else 
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -seg*tightness, tabDepth));
            }
        }

        //add margin in the end
        path.append(String.format("v %f ", -marginY));


        path.append("Z"); // Close the path
        return path.toString(); // Return the constructed path
    }
}
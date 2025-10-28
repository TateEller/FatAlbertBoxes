package com.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; //Change standard settings to America

public class Box
{
    public float width = 0.0f, height = 0.0f, depth = 0.0f;
    public int numTabs;
    public String engraving, font, fileName;
    public float fontSize;
    private final float MinimumSize = 25.0f, strokeWidth = 0.1f, widthOfTabs = 10, heightOfTabs = 5, tabDepth = 11.0f; // Debug values

    // Create two variables to track the position of every piece we add
    private float positionX = 10;
    private float positionY = 10;
    // Create a variable to track the pieces created
    private int pieces = 0;
    // Create a variable for the space between the pieces
    private int spaceBetween;

    public Box(float width, float height, float depth, int numTabs, String engraving, String font, String fileName)
    {
        if(width < MinimumSize || height < MinimumSize || depth < MinimumSize)
            throw new IllegalArgumentException("Width and Height must be at least " + MinimumSize);
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.numTabs = numTabs;
        this.engraving = engraving;
        setFontSize(engraving);
        this.font = font;
        this.fileName = fileName;
        this.spaceBetween = 5 + 2*(int)this.widthOfTabs;
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
        this.fontSize = Math.min(this.width / engraving.length(), this.height);
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

    public Box build()
    {
        return new Box(width, height, depth, numTabs, engraving, font, fileName);
    }

    public void print()
    {
        Locale.setDefault(Locale.US);   // My standard settings are European settings, so please dont remove
        float padding = 10; // Create a variable for padding
        
        // Base slightly larger than the rest
        float widthBase = width + 2 * widthOfTabs;
        float depthBase = depth + 2 * widthOfTabs;

        // Create a variable for the file_width and file_height
        float file_width = widthBase + width * 3 + 2 * spaceBetween + 2 * padding;
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
            width="%fmm" height="%fmm"
            viewBox="0 0 %f %f">
            <rect width="100%%" height="100%%" fill="white"/>
            """, file_width, file_height, file_width, file_height);

        String svgCloser = """
            </svg>
            """;
        String svgContent = "";
        // One base
        svgContent += addBase();

        // Add the top
        svgContent += addTop();

        // Add four sides
        svgContent += addSideA();
        svgContent += addSideA();
        svgContent += addSideB();
        svgContent += addSideB();
        
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
        float widthBase = width + 5 * heightOfTabs;
        float depthBase = depth + 5 * heightOfTabs;
        float xCenter = positionX + widthBase / 2;
        float yCenter = positionY + depthBase / 2;

        // Main base rectangle
        StringBuilder svgContent = new StringBuilder(String.format("""
            <rect x="%f" y="%f" width="%f" height="%f" fill="none" stroke="black" stroke-width="%f"/>
            <text x="%f" y="%f" text-anchor="middle" font-size="10" dominant-baseline="middle">%s</text>
            """, positionX, positionY, widthBase, depthBase, strokeWidth, xCenter, yCenter, ""));  //having ("") at the end stops it from engraving on the bottom

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
        
        
        //caculate the space we need no the sides
        float marginX = (widthBase - (nHorizontal*widthOfTabs*2)+ widthOfTabs) /2;
        float marginY = (depthBase - (nVertical*widthOfTabs*2)+ widthOfTabs) /2;

        //create the size of the original box just for refernce
        //float originalBoxX = positionX + 2.5f*heightOfKnobs;
        //float originalBoxY = positionY + 2.5f*heightOfKnobs;
        //svgContent.append(basePrintSquares(originalBoxX, originalBoxY, width, depth));



        // --- Top edge (inside the base) ---
        float yTop = positionY + widthOfTabs;
        float xTop = positionX + marginX;
        for (int i = 0; i < nHorizontal; i++) {
            svgContent.append(basePrintSquares(xTop, yTop, widthOfTabs, heightOfTabs));
            xTop += 2*widthOfTabs;
        }

        // --- Bottom edge (inside the base) ---
        float yBottom = positionY + depthBase - heightOfTabs - widthOfTabs;
        float xBottom = positionX + marginX;
        for (int i = 0; i < nHorizontal; i++) {
            svgContent.append(basePrintSquares(xBottom, yBottom, widthOfTabs, heightOfTabs));
            xBottom += 2*widthOfTabs;
        }

        // --- Left edge (inside the base) ---
        float xLeft = positionX + widthOfTabs; // move inside
        float yLeft = positionY + marginY;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xLeft, yLeft, heightOfTabs, widthOfTabs));
            yLeft += 2*widthOfTabs;
        }

        // --- Right edge (inside the base) ---
        float xRight = positionX + widthBase - heightOfTabs - widthOfTabs; // move left a bit inside
        float yRight = positionY + marginY;
        for (int i = 0; i < nVertical; i++) {
            svgContent.append(basePrintSquares(xRight, yRight, heightOfTabs, widthOfTabs));
            yRight += 2*widthOfTabs;
        }

        // Move position for next piece
        if (pieces < 3)
            positionX += spaceBetween + widthBase;
        else {
            positionY += spaceBetween + depthBase;
            positionX = 10;
            pieces = 0;
        }

        return svgContent.toString();
    }

    public String addSideA()
    {
        pieces += 1;
        // Look for the center of the box
        float xCenter = positionX + depth / 2 + heightOfTabs;
        float yCenter = positionY + height / 2 + heightOfTabs; 

        // --- Number of tabs per side ---
        int nHorizontal = (int)(depth*2 / (2*widthOfTabs));
        int nVertical = (int)(height*2 / (2*widthOfTabs));

        // Caculate the space we need on the sides
        float marginX = heightOfTabs+(depth*2 - (nHorizontal*widthOfTabs*2)) /2;
        float marginY = heightOfTabs+(height*2 - (nVertical*widthOfTabs*2)) /2;
        if(nHorizontal % 2 == 0)
            nHorizontal -= 1;
        if(nVertical % 2 == 0)
            nVertical -= 1;
        // System.out.println(marginX + " " + nHorizontal + " " + nVertical);

        // String pathData = GenerateRectanglePath(positionX, positionY, width, height, tabDepth, numTabs);
        String pathData = GenerateRectanglePathSideA(positionX, positionY, width, height, heightOfTabs, nHorizontal, nVertical, marginX, marginY);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" font-size="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, fontSize, engraving);

        float depthBase = depth + 2 * widthOfTabs;
        
        if (pieces < 3) 
        {
            positionX += spaceBetween + depth;
        } 
        else 
        {
            if(depthBase > height)
                positionY += spaceBetween + depthBase;
            else
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
        float xCenter = positionX + width / 2 + heightOfTabs;
        float yCenter = positionY + height / 2 + heightOfTabs;

        // --- Number of knobs per side ---
        int nHorizontal = (int)(width*2 / (2*widthOfTabs));
        int nVertical = (int)(height*2 / (2*widthOfTabs));
        //caculate the space we need on the sides
        float marginX = heightOfTabs+(width*2 - (nHorizontal*widthOfTabs*2)) /2;
        float marginY = heightOfTabs+(height*2 - (nVertical*widthOfTabs*2)) /2;

        if(marginX - heightOfTabs >= 0)
            marginX = marginX - heightOfTabs;

        if(nHorizontal % 2 == 0)
            nHorizontal -= 1;
        if(nVertical % 2 == 0)
            nVertical -= 1;
        //System.out.println(marginX + " " + nHorizontal + " " + nVertical);

        //String pathData = GenerateRectanglePath(positionX, positionY, width, height, tabDepth, numTabs);
        String pathData = GenerateRectanglePathSideB(positionX, positionY, width, height, heightOfTabs, nHorizontal, nVertical, marginX, marginY);

        //String pathData = GenerateRectanglePath(positionX, positionY, width, height, -tabDepth, numTabs);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" font-size="%.1f" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, fontSize, engraving);

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
        float xCenter = positionX + width / 2 + heightOfTabs;
        float yCenter = positionY + depth / 2 + heightOfTabs;

        // --- Number of tabs per side ---
        int nHorizontal = (int)(width*2 / (2*widthOfTabs));
        int nVertical = (int)(depth*2 / (2*widthOfTabs));
        //caculate the space we need on the sides
        float marginX = (width*2 - (nHorizontal*widthOfTabs*2)) /2;
        float marginY = (depth*2 - (nVertical*widthOfTabs*2)) /2;
        

        if(nHorizontal % 2 == 0)
            nHorizontal -= 1;
        if(nVertical % 2 == 0)
            nVertical -= 1;
        //System.out.println(marginX + " " + nHorizontal + " " + nVertical);

        //String pathData = GenerateRectanglePath(positionX, positionY, width, height, -tabDepth, numTabs);
        String pathData = GenerateRectanglePathTop(positionX, positionY, width, depth, heightOfTabs, nHorizontal, nVertical, marginX, marginY);

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" font-size="10" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, ""); //having ("") and the end stops it engraving on the top

        if (pieces < 3) 
        {
            positionX += 2*spaceBetween + width;
        } 
        else 
        {
            positionY += spaceBetween + height;
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


    //prints holes for the base plate
    private String basePrintSquares(float x, float y, float w, float h) {
        return String.format(
            "<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
            x, y, w, h, strokeWidth
        );
    }

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













    //I created a function to test some things out
    public String GenerateRectanglePathSideA(float x, float y, float width, float height, float tabDepth, int nHorizontal, int nVertical, float marginX, float marginY)
    {
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", x, y)); // Start at top-left corner

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
                path.append(String.format("h %f ", seg));
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
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg, -tabDepth));
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
                path.append(String.format("v %f ", -seg));
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
                path.append(String.format("h %f ", seg));
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
                path.append(String.format("h %f v %f h %f ", tabDepth, seg, -tabDepth));
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
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg, -tabDepth));
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
                path.append(String.format("h %f v %f h %f ", -tabDepth, -seg, tabDepth));
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
                path.append(String.format("v %f h %f v %f ", -tabDepth, seg, tabDepth));
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
                path.append(String.format("h %f v %f h %f ", tabDepth, seg, -tabDepth));
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
                path.append(String.format("v %f h %f v %f ", tabDepth, -seg, -tabDepth));
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
                path.append(String.format("h %f v %f h %f ", -tabDepth, -seg, tabDepth));
            }
        }

        //add margin in the end
        path.append(String.format("v %f ", -marginY));


        path.append("Z"); // Close the path
        return path.toString(); // Return the constructed path
    }
}
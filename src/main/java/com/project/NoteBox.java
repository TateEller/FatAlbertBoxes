package com.project;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale; //Change standard settings to America

public class NoteBox{

    public float width = 0.0f, height = 0.0f, depth = 0.0f;     //Width and depth are the size of the note cards
    public String fileName = "Untitled";
    public float conversion = 2.83465f; // Conversion from mm to points
    public float tabWidth = 12.7f * conversion, tabDepth = 3.175f;
    public float strokeWidth = 1f;

    
    //The space between the pieces
    private float padding = 10f;
    // Create two variables to track the position of every piece we add
    private float positionX = padding;
    private float positionY = padding;

    // Create a variable to track the pieces created
    private int pieces = 0;


    public NoteBox(float w, float h, float d, String fN, float conv){
        this.width = w;
        this.height = h;
        this.depth = d;
        this.fileName = fN;
        this.conversion = conv;
    }

    public NoteBox build(){
        return new NoteBox(width, height, depth, fileName, conversion);
    }

    public void setWidth(float width){
        if(conversion == 72f)
        {
            if(width < 2.99f)
                throw new IllegalArgumentException("Width must be at least 2.99 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(width < 76.1f)
                throw new IllegalArgumentException("Width must be at least 76.1 millimeters");
        }
        this.width = width;
    }
    public void setHeight(float height){
        if(conversion == 72f)
        {
            if(height < 2.99f)
                throw new IllegalArgumentException("Height must be at least 2.99 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(height < 76.1f)
                throw new IllegalArgumentException("Height must be at least 76.1 millimeters");
        }
        this.height = height;
    }
    public void setDepth(float depth){
        if(conversion == 72f)
        {
            if(depth < 2.99f)
                throw new IllegalArgumentException("Depth must be at least 2.99 inches");
        }
        else if (conversion == 2.83465f)
        {
            if(depth < 76.1f)
                throw new IllegalArgumentException("Depth must be at least 76.1 millimeters");
        }
        this.depth = depth;
    }


    public void ConvertMeasurments(float conv){
        //width = width * conv;
        //height = height * conv;
        //depth = depth * conv;
        if(conv == 72f){
            tabDepth = 0.12204724f * conv;
            strokeWidth = 0.003937008f * conv;
        }
        else{
            tabDepth = tabDepth * conv;
            strokeWidth = 0.1f * conversion;
        }
    }

    public void print(){
        //Dimensions should be inputed as desired size and convert here
        ConvertMeasurments(conversion);
        System.out.println(width + " " + height + " " + depth);

        //Calculate the size of the SVG file
        float file_width = 2 * width + height; //width + height + 4 * tabDepth + 3 * padding;
        float file_height = 2 * depth + 2 * height; //depth + 2 * height + 2 * tabDepth + 4 * padding;


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

        
        //svgContent += AddTestSquare(width, depth, "blue");
        svgContent += AddBase(width,depth);
        //svgContent += AddTestSquare(depth, height, "lightgreen");
        svgContent += AddSide(depth,height);
        //svgContent += AddTestSquare(width, height, "red");
        svgContent += AddBack(width,height);
        svgContent += AddSide(depth,height);
        svgContent += AddFront(width/3,height);
        svgContent += AddFront(width/3,height);


        // Create the svg file
        String svgWhole = svgOpener + svgContent + svgCloser;

        //System.out.println(svgWhole);

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

    private String AddBase(float sideX, float sideY){
        pieces++;        

        //Find starting position
        positionX += padding + tabDepth;
        positionY += padding + tabDepth;

        //Calculate tabs width
        float tabX = CalculateTabs(sideX);
        float tabY = CalculateTabs(sideY);
        //Number of tabs
        float numX = sideX / tabX;
        float numY = sideY / tabY;

        //Find center
        float xCenter = positionX + (sideX / 2);
        float yCenter = positionY + (sideY / 2);

        //Generate path
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", positionX, positionY)); // Start at top-left corner

        // Top edge
        // Start with gap
        path.append(String.format("h %f ", tabX));
        for (int i = 0; i < numX-2; i++)
        {
            if (i % 2 == 0) // Tab
            {
                path.append(String.format("v %f h %f v %f ", -tabDepth, tabX, tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("h %f ", tabX));
            }
        }   
        //End with gap
        path.append(String.format("h %f ", tabX));

        // Right edge
        // Start with gap
        path.append(String.format("v %f ", tabY));
        for (int i = 0; i < numY-2; i++)
        {
            if (i % 2 == 0) // Tab
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, tabY, -tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("v %f ", tabY));
            }
        }
        // End with gap
        path.append(String.format("v %f ", tabY));

        // Bottom edge  
        path.append(String.format("h %f ", -tabX)); // Gap
        path.append(String.format("v %f h %f v %f ", tabDepth, -tabX, -tabDepth)); // Tab
        path.append(String.format("h %f ", -tabX)); // Gap
        path.append(String.format("v %f ", -(2 * tabY)));
        for (int i = 0; i < numX-6; i++)
        {
            path.append(String.format("h %f ", -tabX));
        }
        path.append(String.format("v %f ", 2 * tabY));
        path.append(String.format("h %f ", -tabX)); // Gap
        path.append(String.format("v %f h %f v %f ", tabDepth, -tabX, -tabDepth)); // Tab
        path.append(String.format("h %f ", -tabX)); // Gap

        // Left edge       
        // Start with gap
        path.append(String.format("v %f ", -tabY));
        for (int i = 0; i < numY-2; i++) 
        {
            if (i % 2 == 0) //Tab
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -tabY, tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("v %f ", -tabY));
            }
        }
        // End with gap
        path.append(String.format("v %f ", -tabY));


        path.append("Z"); // Close the path
        String pathData = path.toString(); // Return the constructed path

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                <text x="%.1f" y="%.1f" font-size="10" text-anchor="middle" dominant-baseline="middle">%s</text>
                """, pathData, strokeWidth, xCenter, yCenter, ""); //having ("") and the end stops it engraving on the top
        
        // Move start of nex piece
        positionX -= tabDepth;
        positionY += sideY + tabY + padding; 

        return svgContent;
    }

    private String AddSide(float sideX, float sideY){
        pieces++;        

        //Calculate tabs width
        float tabX = CalculateTabs(sideX);
        float tabY = CalculateTabs(sideY);
        //Number of tabs
        float numX = sideX / tabX;
        float numY = sideY / tabY;

        //Find center
        float xCenter = positionX + (sideX / 2);
        float yCenter = positionY + (sideY / 2);

        //Generate path
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", positionX, positionY)); // Start at top-left corner

        // Top edge (not tabbed)
        path.append(String.format("h %f ", sideX));
        
        // Right edge
        for (int i = 0; i < numY; i++)
        {
            if (i % 2 == 0) // Tab
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, tabY, -tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("v %f ", tabY));
            }
        }

        // Bottom edge (not tabbed)
        path.append(String.format("h %f ", -sideX));
        

        // Left edge     
        for (int i = 0; i < numY; i++) 
        {
            if (i % 2 == 0) // Tab
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -tabY, tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("v %f ", -tabY));
            }
        }


        path.append("Z"); // Close the path
        
        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                """, path.toString(), strokeWidth);
        

        //Create cutout holes
        float holeX = positionX + tabX;
        float holeY = positionY + sideY - tabY;

        for (int i = 0; i < numX-2; i++){
            if (i % 2 == 0) // Hole
            {
                svgContent += String.format("<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
                holeX, holeY, tabWidth, tabDepth, strokeWidth);
            }
            holeX += tabX;
        }

        //Add engraving (optional)
        svgContent += String.format(
            "<text x=\"%.1f\" y=\"%.1f\" font-size=\"10\" text-anchor=\"middle\" dominant-baseline=\"middle\">%s</text>\r\n",
            xCenter, yCenter, "");  //having ("") and the end stops it engraving on the top
        
        positionX += sideX + tabX + padding; 
        
        
        return svgContent;
    }

    private String AddBack(float sideX, float sideY){
        pieces++;

        //Calculate tabs width
        float tabX = CalculateTabs(sideX);
        float tabY = CalculateTabs(sideY);
        //Number of tabs
        float numX = sideX / tabX;
        float numY = sideY / tabY;

        //Find center
        float xCenter = positionX + (sideX / 2);
        float yCenter = positionY + (sideY / 2);

        //Generate path
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", positionX, positionY)); // Start at top-left corner

        // Top edge (not tabbed)
        path.append(String.format("h %f ", sideX));
        
        // Right edge
        for (int i = 0; i < numY; i++)
        {
            if (i % 2 == 0) // Gap
            {
                path.append(String.format("v %f ", tabY));
            } 
            else // Tab
            {
                path.append(String.format("h %f v %f h %f ", tabDepth, tabY, -tabDepth));
            }
        }

        // Bottom edge (not tabbed)
        path.append(String.format("h %f ", -sideX));
        

        // Left edge     
        for (int i = 0; i < numY; i++) 
        {
            if (i % 2 == 0) // Gap
            {
                path.append(String.format("v %f ", -tabY));
            } 
            else // Tab
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -tabY, tabDepth));
            }
        }

        path.append("Z"); // Close the path

        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                """, path.toString(), strokeWidth);
        
        //Create cutout holes
        float holeX = positionX + tabX;
        float holeY = positionY + sideY - tabY;

        for (int i = 0; i < numX-2; i++){
            if (i % 2 == 0) // Hole
            {
                svgContent += String.format("<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
                holeX, holeY, tabWidth, tabDepth, strokeWidth);
            }
            holeX += tabX;
        }

        //Add engraving (optional)
        svgContent += String.format(
            "<text x=\"%.1f\" y=\"%.1f\" font-size=\"10\" text-anchor=\"middle\" dominant-baseline=\"middle\">%s</text>\r\n",
            xCenter, yCenter, "");  //having ("") and the end stops it engraving on the top

        // Move start of next piece
        positionX -= depth + tabX + padding;
        positionY += sideY + padding; 

        return svgContent;
    }

    private String AddFront(float sideX, float sideY){
        pieces++;

        //Calculate tabs width
        float tabX = CalculateTabs(sideX);
        float tabY = CalculateTabs(sideY);
        //Number of tabs
        float numX = sideX / tabX;
        float numY = sideY / tabY;

        //Find center
        float xCenter = positionX + (sideX / 2);
        float yCenter = positionY + (sideY / 2);

        //Generate path
        StringBuilder path = new StringBuilder();
        path.append(String.format("M %f %f ", positionX, positionY)); // Start at top-left corner

        // Top edge (not tabbed)
        path.append(String.format("h %f ", sideX));

        // Right edge (not tabbed)
        path.append(String.format("v %f ", sideY));


        // Bottom edge  (not tabbed)
        path.append(String.format("h %f ", -sideX));

        // Left edge       
        // Start with gap
        path.append(String.format("v %f ", -tabY));
        float fLeftEdge = 0;
        for (int i = 0; i < numY-2; i++) 
        {
            if (i % 2 == 0) //Tab
            {
                path.append(String.format("h %f v %f h %f ", -tabDepth, -tabY, tabDepth));
            } 
            else // Gap
            {
                path.append(String.format("v %f ", -tabY));
            }
            fLeftEdge += tabY;
        }
        // End with gap
        path.append(String.format("v %f ", -tabY));


        path.append("Z"); // Close the path
        
        String svgContent = String.format("""
                <path d="%s" stroke-width="%f" fill="none" stroke="black"/>
                """, path.toString(), strokeWidth);
        
        //Create cutout holes
        float holeX = positionX + tabX;
        float holeY = positionY + sideY - tabY;

        for (int i = 0; i < numX-2; i++){
            if (i % 2 == 0) // Hole
            {
                svgContent += String.format("<rect x=\"%.2f\" y=\"%.2f\" width=\"%.2f\" height=\"%.2f\" fill=\"none\" stroke=\"black\" stroke-width=\"%.2f\" />\n",
                holeX, holeY, tabWidth, tabDepth, strokeWidth);
            }
            holeX += tabX;
        }

        //Add engraving (optional)
        svgContent += String.format(
            "<text x=\"%.1f\" y=\"%.1f\" font-size=\"10\" text-anchor=\"middle\" dominant-baseline=\"middle\">%s</text>\r\n",
            xCenter, yCenter, "");  //having ("") and the end stops it engraving on the top

        // Move start of next piece
        positionX += sideX + tabX + 3 * padding;

        System.out.println("Left Edge: " + fLeftEdge + " Right Edge: " + sideY);
        return svgContent;
    }

    public float CalculateTabs(float sideLength){
        
        //Calculate tabs
        int tabNum = (int)(sideLength / tabWidth);
        if(tabNum % 2 == 0) { tabNum += 1; }

        //Prevent minimum of 3 tabs
        if(tabNum < 3) { tabNum = 3; }
        
        return sideLength / tabNum;
    }
    /*
    public static void main(String[] args) {
        NoteBox box = new NoteBox(76.1f, 50, 100, "Hi", "NoteBox", 3.175f);
        box.print();
    }    */

    public String AddTestSquare(float sideX, float sideY, String color){
        String svgContent = String.format("""
                <rect x="%.2f" y="%.2f" width="%.2f" height="%.2f" fill="%s" stroke="none" stroke-width="%.2f" />
                """, positionX, positionY, sideX, sideY, color, strokeWidth);
        return svgContent;
    }
}
import org.junit.Test;

import com.project.Box;
import com.project.NoteBox;

public class TestBox
{
    @Test
    public void testBoxCreation()
    {
        Box box = new Box(1, 30.0f, 30.0f, 30.0f, 0, "Test", "Arial", 4, "TestBox",1f, 1f);
        assert box != null : "Box creation failed";
    }

    @Test
    public void testHeightInput()
    {
        Box heightBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", 4, "TestBox",2.83465f, 1f);
        try
        {
            heightBox.setHeight(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for width below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Height must be at least");
        }

        // Test 1
        heightBox.setHeight(50); // Valid size
        assert heightBox.height == 50 : "Height not set correctly";
        
        // Test 2
        heightBox.setHeight(100); // Valid size
        assert heightBox.height == 100 : "Height not set correctly";

        // Test 3
        heightBox.setHeight(49); // Edge case (minimum size)
        assert heightBox.height == 49 : "Height not set correctly";
    }

    @Test
    public void testWidthInput()
    {
        Box heightBox = new Box(1, 50,50,50,5, "Test", "Arial", 4, "TestBox",2.83465f,1f);
        try
        {
            heightBox.setWidth(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for width below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Width must be at least");
        }

        // Test 1
        heightBox.setWidth(50); // Valid size
        assert heightBox.width == 50 : "Width not set correctly";
        
        // Test 2
        heightBox.setWidth(100); // Valid size
        assert heightBox.width == 100 : "Width not set correctly";

        // Test 3
        heightBox.setWidth(49); // Edge case (minimum size)
        assert heightBox.width == 49 : "Width not set correctly";
    } 

    @Test
    public void testDepthInput()
    {
        Box depthBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", 4, "TestBox",2.83465f,1f);
        try
        {
            depthBox.setDepth(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for width below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Depth must be at least");
        }

        // Test 1
        depthBox.setDepth(50); // Valid size
        assert depthBox.depth == 50 : "Depth not set correctly";
        
        // Test 2
        depthBox.setDepth(100); // Valid size
        assert depthBox.depth == 100 : "Depth not set correctly";

        // Test 3
        depthBox.setDepth(49); // Edge case (minimum size)
        assert depthBox.depth == 49 : "Depth not set correctly";
    }

    @Test
    public void testEngravingInput()
    {
        Box engravingBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", 4, "TestBox",1f,1f);
        
        // Test 1
        engravingBox.setEngraving("NE");
        assert engravingBox.engraving.equals("NE") : "Engraving not set correctly";

        // Test 2
        engravingBox.setEngraving("HW");
        assert engravingBox.engraving.equals("HW") : "Engraving not set correctly";

        // Test 3
        engravingBox.setEngraving("LE");
        assert engravingBox.engraving.equals("LE") : "Engraving not set correctly";
    }

    @Test
    public void testFontInput()
    {
        Box fontBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", 4, "TestBox",1f,1f);
        
        // Test 1
        fontBox.setFont("Arial");
        assert fontBox.font.equals("Arial") : "Font not set correctly";

        // Test 2
        fontBox.setFont("Times New Roman");
        assert fontBox.font.equals("Times New Roman") : "Font not set correctly";

        // Test 3
        fontBox.setFont("Courier New");
        assert fontBox.font.equals("Courier New") : "Font not set correctly";
    }

    @Test
    public void testTabInput()
    {
        Box tabBox = new Box(1, 50, 50, 50, 5, "Test", "Arial", 4, "TestBox",1f,1f);

        // Test 1
        tabBox.setTabs(5);
        assert tabBox.numTabs == 5;

        // Test 2
        tabBox.setTabs(10);
        assert  tabBox.numTabs == 10;

        // Test 3
        tabBox.setTabs(20);
        assert tabBox.numTabs == 20;
    }

    @Test
    public void testFileName()
    {
        Box fileBox = new Box(1, 50, 50, 50, 5, "Test", "Arial", 4, "TestBox",1f,1f);

        // Test 1
        fileBox.setFileName("Test1");
        assert fileBox.fileName.equals("Test1");

        // Test 2
        fileBox.setFileName("Test2");
        assert fileBox.fileName.equals("Test2");

        // Test 3
        fileBox.setFileName("Test3");
        assert fileBox.fileName.equals("Test3");
    }

    @Test
    public void testBuildAndClone()
    {
        Box original = new Box(2, 80.0f, 60.0f, 40.0f, 3, "ENG", "Verdana", 4, "Orig",2.83465f,1f);
        Box built = original.build();

        // Assert built object is not null
        assert built != null : "Built box is null";

        // Assert built has same dimensions
        assert built.width == original.width : "Built width mismatch";
        assert built.height == original.height : "Built height mismatch";

        // Assert built is a different instance
        assert built != original : "Build should return a new Box instance";
    }

    @Test
    public void testConversionAndBuild()
    {
        Box convBox = new Box(1, 100.0f, 50.0f, 30.0f, 2, "E", "Arial", 4, "Conv",2.83465f,1f);
        convBox.setConversion(0.5f);

        // Test conversion updated
        assert convBox.conversion == 0.5f : "Conversion not updated";

        Box b2 = convBox.build();
        // Test built box preserved conversion
        assert b2.conversion == 0.5f : "Built box conversion mismatch";

        // Test built box preserved width
        assert b2.width == convBox.width : "Built box width mismatch";

        // Test built returns a new instance
        assert b2 != convBox : "Build should return a new instance";
    }

    @Test
    public void testGeneratePathNonEmpty()
    {
        Box pBox = new Box(1, 60.0f, 40.0f, 30.0f, 4, "", "Arial", 4,"PathTest",1f,1f);
        String path = pBox.GenerateRectanglePath(0f, 0f, 60f, 40f, 4.0f, 4);

        // Test non-null
        assert path != null : "Path should not be null";

        // Test not empty
        assert path.length() > 0 : "Path should not be empty";

        // Test contains move/close commands (basic sanity)
        assert path.contains("M ") || path.contains("Z") : "Path seems malformed";

        // Test contains at least one space (simple structural check)
        assert path.contains(" ") : "Path missing expected spacing";
    }

    @Test
    public void testEngravingFontSize()
    {
        Box efBox = new Box(1, 120.0f, 60.0f, 40.0f, 1, "", "Arial", 4,"EFTest",1f,1f);

        efBox.setEngraving("Hello");
        assert efBox.engraving.equals("Hello") : "Engraving not set";

        efBox.setFontSize(efBox.engraving);
        // Font size should be positive
        assert efBox.fontSize > 0 : "Font size should be greater than zero";

        // Font size should not exceed box height
        assert efBox.fontSize <= efBox.height : "Font size should not exceed box height";

        // Setting a different engraving updates engraving
        efBox.setEngraving("Hi");
        assert efBox.engraving.equals("Hi") : "Engraving did not update";
    }

    @Test
    public void testFileNameTabsAndBuild()
    {
        Box ftBox = new Box(2, 90.0f, 70.0f, 50.0f, 0, "", "Arial", 4,"Start",1f,1f);

        ftBox.setFileName("MyBox");
        assert ftBox.fileName.equals("MyBox") : "File name not set";

        ftBox.setTabs(7);
        assert ftBox.numTabs == 7 : "Tabs not set correctly";

        Box clone = ftBox.build();
        assert clone.fileName.equals("MyBox") : "Built fileName mismatch";

        assert clone.numTabs == 7 : "Built numTabs mismatch";
    }
    
    @Test
    public void testMeasurementConversion()
    {
        float pixelsToInches = 1 / 96f;
        float pixelsToCm = 1 / 3.78f;

        // Test 1
        float inches = 192 * pixelsToInches; // 2 inches
        assert Math.abs(inches - 2.0f) < 0.0001 : "Pixel to inch conversion failed";

        // Test 2
        float cm = 378 * pixelsToCm; // 100 cm
        assert Math.abs(cm - 100.0f) < 0.0001 : "Pixel to cm conversion failed";

        // Test 3
        float inches2 = 96 * pixelsToInches; // 1 inch
        assert Math.abs(inches2 - 1.0f) < 0.0001 : "Pixel to inch conversion failed";
    }

    @Test
    public void testBoxTypeSelection()
    {
        Box box1 = new Box(1, 50, 50, 50, 5, "Test", "Arial", 4,"TestBox1",1f,1f);
        assert box1.boxType == 1 : "Box type 1 selection failed";

        Box box2 = new Box(2, 50, 50, 50, 5, "Test", "Arial", 4,"TestBox2",1f,1f);
        assert box2.boxType == 2 : "Box type 2 selection failed";
    }

    @Test
    public void testGUIboxes()
    {
        com.project.BoxGUI gui = new com.project.BoxGUI();
        assert gui != null : "BoxGUI creation failed";
    }

    @Test
    public void noteWidthTest()
    {
        NoteBox noteBox = new NoteBox(76.1f, 50.0f, 100.0f, "NoteBox", 2.83465f);
        assert noteBox != null : "NoteBox creation failed";

        try
        {
            noteBox.setWidth(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for width below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Width must be at least");
        }

        noteBox.setWidth(100);
        assert noteBox.width == 100;

        noteBox.setWidth(90);
        assert noteBox.width == 90;

        noteBox.setWidth(76.1f);   //edge case
        assert noteBox.width == 76.1f;
    }

    @Test
    public void noteHeightTest()
    {
        NoteBox noteBox = new NoteBox(76.1f, 50.0f, 100.0f, "NoteBox", 2.83465f);
        assert noteBox != null : "NoteBox creation failed";

        try
        {
            noteBox.setHeight(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for height below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Height must be at least");
        }

        noteBox.setHeight(100);
        assert noteBox.height == 100;

        noteBox.setHeight(90);
        assert noteBox.height == 90;

        noteBox.setHeight(76.1f);   //edge case
        assert noteBox.height == 76.1f;
    }

    @Test
    public void noteDepthTest()
    {
        NoteBox noteBox = new NoteBox(76.1f, 50.0f, 100.0f, "NoteBox", 2.83465f);
        assert noteBox != null : "NoteBox creation failed";

        try
        {
            noteBox.setDepth(10); // Below minimum size
            assert false : "Expected IllegalArgumentException for depth below minimum size";
        }
        catch(IllegalArgumentException e)
        {
            assert e.getMessage().contains("Depth must be at least");
        }

        noteBox.setDepth(100);
        assert noteBox.depth == 100;

        noteBox.setDepth(90);
        assert noteBox.depth == 90;

        noteBox.setDepth(76.1f);   //edge case
        assert noteBox.depth == 76.1f;
    }
}
import org.junit.Test;

import com.project.Box;

public class TestBox
{
    @Test
    public void testBoxCreation()
    {
        Box box = new Box(1, 30.0f, 30.0f, 30.0f, 0, "Test", "Arial", "TestBox");
        assert box != null : "Box creation failed";
    }

    @Test
    public void testHeightInput()
    {
        Box heightBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", "TestBox");
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
        heightBox.setHeight(30); // Valid size
        assert heightBox.height == 30 : "Height not set correctly";
        
        // Test 2
        heightBox.setHeight(100); // Valid size
        assert heightBox.height == 100 : "Height not set correctly";

        // Test 3
        heightBox.setHeight(25); // Edge case (minimum size)
        assert heightBox.height == 25 : "Height not set correctly";
    }

    @Test
    public void testWidthInput()
    {
        Box heightBox = new Box(1, 50,50,50,5, "Test", "Arial", "TestBox");
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
        heightBox.setWidth(30); // Valid size
        assert heightBox.width == 30 : "Width not set correctly";
        
        // Test 2
        heightBox.setWidth(100); // Valid size
        assert heightBox.width == 100 : "Width not set correctly";

        // Test 3
        heightBox.setWidth(25); // Edge case (minimum size)
        assert heightBox.width == 25 : "Width not set correctly";
    } 

    @Test
    public void testDepthInput()
    {
        Box depthBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", "TestBox");
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
        depthBox.setDepth(30); // Valid size
        assert depthBox.depth == 30 : "Depth not set correctly";
        
        // Test 2
        depthBox.setDepth(100); // Valid size
        assert depthBox.depth == 100 : "Depth not set correctly";

        // Test 3
        depthBox.setDepth(25); // Edge case (minimum size)
        assert depthBox.depth == 25 : "Depth not set correctly";
    }

    @Test
    public void testEngravingInput()
    {
        Box engravingBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", "TestBox");
        
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
        Box fontBox = new Box(1, 50.0f, 50.0f, 50.0f, 0, "Test", "Arial", "TestBox");
        
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
        Box tabBox = new Box(1, 50, 50, 50, 5, "Test", "Arial", "TestBox");

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
        Box fileBox = new Box(1, 50, 50, 50, 5, "Test", "Arial", "TestBox");

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
}
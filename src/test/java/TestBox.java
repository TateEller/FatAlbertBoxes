import org.junit.Test;

import com.project.Box;

public class TestBox
{
    /*@Test
    public void testTextInput()
    {
    // Placeholder for implementation
    }*/

    
    @Test
    public void testBoxCreation()
    {
        Box box = new Box(30,30, "Test", "Arial");
        assert box != null : "Box creation failed";
    }
}

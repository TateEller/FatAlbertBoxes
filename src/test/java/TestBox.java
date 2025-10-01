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

    /******
     * User input from terminal
     * 
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter width: ");
        float width = scanner.nextFloat();  //will need to check if input is correct type

        System.out.print("Enter height: ");
        float height = scanner.nextFloat(); //will need to check if input is correct type

        System.out.print("Enter two letter engraving: ");
        String engraving = scanner.next();  //will need to check if input is correct type

        //float width = 50 , height = 50;
        //String engraving = "Fat A."; 
        String font = "";
        Box test = new Box(width, height, engraving, font);
        scanner.close();
        test.print();
    }
    ******/
}

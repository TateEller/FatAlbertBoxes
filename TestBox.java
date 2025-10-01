public class TestBox
{
    public static void main(String[] args)
    {
        float width = 50 , height = 50;
        String engraving = "Fat A.", font = "";
        Box test = new Box(width, height, engraving, font);
        test.print();
    }
}

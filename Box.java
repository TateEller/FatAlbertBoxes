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
}
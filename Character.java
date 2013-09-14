
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class Character
{
    boolean isInvisible = false;
    int xcoord = 0;
    int ycoord = 0;
    BufferedImage charImage;
    boolean isAlive = true;

    public Character(String imageString)
    {
        try
        {
            charImage = ImageIO.read(new File(imageString));
        }
        catch(IOException i)
        {
            JOptionPane.showConfirmDialog(null, JOptionPane.ERROR_MESSAGE, "Image Not Found", 1);
            charImage = null;
        }
    }
    
    public void moveRight()
    {
        xcoord+=10;
    }

    public void moveLeft()
    {
        if(xcoord - 10 >= 0)
            xcoord-=10;
    }
    
    public void moveUp()
    {
        if(ycoord - 10 >= 0)
            ycoord-=10;
    }
    
    public void moveDown()
    {
        ycoord+=10;
    }
    
    public BufferedImage getCharImage()
    {
        return charImage;
    }

    public int getXcoord()
    {
        return xcoord;
    }

    public int getYcoord()
    {
        return ycoord;
    }
    
    public void setInvisible(boolean invisible)
    {
        isInvisible = invisible;
    }
    
    public boolean getInvisible()
    {
        return isInvisible;
    }
}
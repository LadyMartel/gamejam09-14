
import javax.swing.JOptionPane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class Character
{
    boolean isInvisible = false;
    int xcoord = 400;
    int ycoord = 0;
    int v = 10;
    BufferedImage charImage;

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
        xcoord += v;
    }

    public void moveLeft()
    {
        if(xcoord - v >= 0)
            xcoord -= v;
    }
    
    public void moveUp()
    {
        if(ycoord - v >= 0)
            ycoord -= v;
    }
    
    public void moveDown()
    {
        ycoord += v;
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
    
    public void setV(int nv)
    {
    	v = nv;
    }
}
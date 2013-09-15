
import javax.swing.JOptionPane;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class Character
{
    boolean isInvisible = false;
    int xcoord = 100;
    int ycoord = 0;
    int vx = 10;
    int vy = 10;
    BufferedImage charImage;

    public Character(String imageString)
    {
        try
        {
            charImage = ImageIO.read(this.getClass().getResourceAsStream(imageString));
        }
        catch(IOException i)
        {
            JOptionPane.showConfirmDialog(null, JOptionPane.ERROR_MESSAGE, "Image Not Found", 1);
            charImage = null;
        }
    }
    
    public void moveRight()
    {
        xcoord += vx;
    }

    public void moveLeft()
    {
        if(xcoord - vx >= 0)
            xcoord -= vx;
    }
    
    public void moveUp()
    {
        if(ycoord - vy >= 0)
            ycoord -= vy;
    }
    
    public void moveDown()
    {
        ycoord += vy;
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
    
    public void setVX(int nv)
    {
    	vx = nv;
    }
    
    public void setVY(int nv)
    {
    	vy = nv;
    }
    
    public int getVX()
    {
    	return vx;
    }
    
    public int getVY()
    {
    	return vy;
    }
    
    public void setCoord(int x, int y)
    {
    	xcoord = x;
    	ycoord = y;
    }
    
    
}
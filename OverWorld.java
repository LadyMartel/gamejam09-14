import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.Timer;

import java.io.*;
import javax.imageio.ImageIO;


public class OverWorld implements KeyListener, ActionListener, MouseListener
{
    public WorldPainter world1, world2, currentWorld;
    public JFrame overWorldFrame;
    public Container overWorldPanel;
    private Character char1, char2, currentChar;
    private boolean isAlive = true;
    //private ArrayList<Light> lights1 = new ArrayList<Light>();
    //private ArrayList<Light> lights2 = new ArrayList<Light>();
    
    private Timer timer;
    private int offset = 0;

    public static void main(String[] args)
    {
        OverWorld overworld = new OverWorld();
    }
    
    public OverWorld()
    {
        overWorldFrame = new JFrame();
        overWorldPanel = overWorldFrame.getContentPane();
        char1 = new Character("./resource/charimage.png");
        char2 = new Character("./resource/charimage_inverted.png");
        char1.setInvisible(false);
        char2.setInvisible(true);
        world1 = new WorldPainter();
        world2 = new WorldPainter();
        world1.setBackground(Color.BLACK);
        world2.setBackground(Color.BLACK);
        
        overPaintWorlds("./resource/top1.png", "./resource/bottom1.png", "./resource/top1mask.png", "./resource/bottom1mask.png");
        
        overWorldPanel.setLayout(new BoxLayout(overWorldPanel, BoxLayout.Y_AXIS));
        overWorldFrame.setTitle("Over World");
        //overWorldFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        overWorldFrame.setSize(1250,600);
        //overWorldFrame.setResizable(false);
        overWorldFrame.setVisible(true);
        overWorldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        overWorldPanel.add(world1);
        overWorldPanel.add(world2);
        
        overWorldFrame.setFocusable(true);
        overWorldPanel.setFocusable(true);
        world1.setFocusable(true);
        world2.setFocusable(true);
        
        overWorldFrame.addKeyListener(this);
        overWorldPanel.addKeyListener(this);
        world1.addKeyListener(this);
        world2.addKeyListener(this);
        
        //lights1.add(new Light(300,0));
        timer = new Timer(30, this);
        timer.start();
       
        currentWorld = world1;
        currentChar = char1;
        overWorldFrame.requestFocus();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        overPaintChars();
        checkIsDead();
        //paintLight();
        if(!isAlive)
        {
            world1.setBackground(Color.WHITE);
            world2.setBackground(Color.WHITE);
            timer.stop();
        }
    }
    
    private boolean checkIsDead()
    {
        for(int i = currentChar.xcoord; i < currentChar.xcoord + currentChar.getCharImage().getWidth(); i++)
        {
        	for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
        		if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.YELLOW.getRGB())
        			isAlive = false;
        	}
        }
        if (currentChar.xcoord - offset <= 0)
        {
        	isAlive = false;
        }
        return isAlive;
    }
    
    private boolean checkTeleport()
    {
    	WorldPainter op = world1;
    	if (currentWorld == world1)
    	{
    		op = world2;
    	}
    	for(int i = currentChar.xcoord; i < currentChar.xcoord + currentChar.getCharImage().getWidth(); i++)
        {
        	for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
        		if(op.imageWorld_mask.getRGB(i,j) == Color.WHITE.getRGB())
        			return false;
        	}
        }
    	return true;
    }
    
    
    private boolean checkWall(char s)
    {
    	if (s == 'r'){
    		int i = currentChar.xcoord + currentChar.getCharImage().getWidth();
	        for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
	        	if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.WHITE.getRGB())
	        		return true;
	        }
    	}
    	else if (s == 'l'){
    		int i = currentChar.xcoord;
	        for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
	        	if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.WHITE.getRGB())
	        		return true;
	        }
    	}
    	else if (s == 'u'){
    		int i = currentChar.ycoord;
	        for(int j = currentChar.xcoord; j < currentChar.xcoord + currentChar.getCharImage().getWidth(); j++){
	        	if(currentWorld.imageWorld_mask.getRGB(j,i) == Color.WHITE.getRGB())
	        		return true;
	        }
    	}
    	else {
    		int i = currentChar.ycoord + currentChar.getCharImage().getHeight();
	        for(int j = currentChar.xcoord; j < currentChar.xcoord + currentChar.getCharImage().getWidth(); j++){
	        	if(currentWorld.imageWorld_mask.getRGB(j,i) == Color.WHITE.getRGB())
	        		return true;
	        }
    	}
    	return false;
    }
    
    
    public void overPaintChars()
    {
        overWorldFrame.requestFocus();
        if(currentChar == char1)
        {
            offset++;
            world1.paintChar(char1.getCharImage(), char1.getXcoord(), char1.getYcoord());
            world1.setShouldPaintChars(true);
            world2.setShouldPaintChars(false);
            world1.repaint();
            world2.repaint();
        }
        else 
        {
            offset++;
            world2.paintChar(char2.getCharImage(), char2.getXcoord(), char2.getYcoord());
            world2.setShouldPaintChars(true);
            world1.setShouldPaintChars(false);
            world2.repaint();
            world1.repaint();
        }
    }
    
    public void overPaintWorlds(String top, String bottom, String top_mask, String bottom_mask)
    {
        try
        {
            world1.imageWorld = ImageIO.read(new File(top));
            world2.imageWorld = ImageIO.read(new File(bottom));
            world1.imageWorld_mask = ImageIO.read(new File(top_mask));
            world2.imageWorld_mask = ImageIO.read(new File(bottom_mask));
        }
        catch(IOException i)
        {
            JOptionPane.showConfirmDialog(null, JOptionPane.ERROR_MESSAGE, "Image Not Found", 1);
            world1.imageWorld = null;
            world2.imageWorld = null;
        }
    }
    


    private class WorldPainter extends JPanel
    {
        private BufferedImage imageChar;
        public BufferedImage imageWorld, imageWorld_mask;
        private int xChar, yChar;
        private boolean shouldPaintChars = false;
        //private ArrayList<Light> lights = new ArrayList<Light>();
        
        public WorldPainter ()
        {
            super();
        }

        public void setShouldPaintChars(boolean shouldPaintChars)
        {
            this.shouldPaintChars = shouldPaintChars;
        }
        
        public void paintChar(BufferedImage imageChar, int xChar, int yChar)
        {
            this.imageChar = imageChar;
            this.xChar = xChar;
            this.yChar = yChar;
        }
        
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.drawImage(imageWorld,-offset, 0, this);
            if(shouldPaintChars) {g.drawImage(imageChar, xChar - offset, yChar, this);}
        }
    }
    
    
    public void keyPressed(KeyEvent e) {
        if(isAlive)
        {
            char keyChar = e.getKeyChar();
            if(keyChar == 'd')
            {
            	if(!checkWall('r'))
                if(char1.getXcoord() + char1.getVX() <= world1.getWidth() + offset - char1.getCharImage().getWidth())
                {
                    char1.moveRight();
                    char2.moveRight();
                }
            }
            else if(keyChar == 'a')
            {
            	if(!checkWall('l')){
	                char1.moveLeft();
	                char2.moveLeft();
            	}
            }
            else if(keyChar == 's')
            {
            	if(!checkWall('d')){
	                if(char1.getYcoord() + char1.getVY() <= world1.getHeight() - char1.getCharImage().getHeight())
	                {
	                    char1.moveDown();
	                    char2.moveDown();
	                }
            	}
            }
            else if(keyChar == 'w')
            {
            	if(!checkWall('u')){
	                char1.moveUp();
	                char2.moveUp();
            	}
            }
            else if(keyChar == ' ')
            {
                if(currentChar == char1)
                {
                    char1.setInvisible(true);
                    char2.setInvisible(false);
                    currentChar = char2;
                    currentWorld = world2;
                }
                else
                {
                    char1.setInvisible(false);
                    char2.setInvisible(true);
                    currentChar = char1;
                    currentWorld = world1;
                }
            } 
        }
    }
    
    public void mouseClicked(MouseEvent e)
    {
        overWorldFrame.requestFocus();
    }
    
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e) {}
    public void mouseEntered(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
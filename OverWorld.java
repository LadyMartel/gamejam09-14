import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private int levelCounter = 1;
    public static final int slow = 5;
    public static final int normal = 10;
    public static final int fast = 20;
    private boolean done = false;
    
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
        world1.setBackground(Color.WHITE);
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
        if(checkLeaf())
        {
        	done = false;
            timer.stop();
            levelCounter++;
            offset = 0;
            overPaintWorlds("./resource/top" + levelCounter + ".png", "./resource/bottom" + levelCounter + ".png", "./resource/top" + levelCounter + "mask.png", "./resource/bottom" + levelCounter + "mask.png");
            char1.setCoord(100, 0);
            char2.setCoord(100, 0);
            char1.setInvisible(false);
            char2.setInvisible(true);
            currentChar = char1;
            currentWorld = world1;
            int someNum = JOptionPane.showConfirmDialog(null, "Congratulations, you beat the level! Now to the next one :", "Congratulations!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(someNum > Integer.MIN_VALUE) {timer.start();}
        }
        if(!isAlive)
        {
            world1.setBackground(Color.WHITE);
            world2.setBackground(Color.WHITE);
            timer.stop();
            int n = JOptionPane.showConfirmDialog(overWorldFrame, "You Died! Do you want to start over?", "Dead Bug", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (n == JOptionPane.YES_OPTION)
            {
            	isAlive = true;
            	offset = 0;
                overPaintWorlds("./resource/top" + levelCounter + ".png", "./resource/bottom" + levelCounter + ".png", "./resource/top" + levelCounter + "mask.png", "./resource/bottom" + levelCounter + "mask.png");
                char1.setCoord(100, 0);
                char2.setCoord(100, 0);
                char1.setInvisible(false);
                char2.setInvisible(true);
                currentChar = char1;
                currentWorld = world1;
                timer.start();
            }
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
    
    private boolean isSludge()
    {
    	for(int i = currentChar.xcoord; i < currentChar.xcoord + currentChar.getCharImage().getWidth(); i++)
        {
        	for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
        		if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.RED.getRGB())
        			return true;
        	}
        }
    	return false;
    }
    
    private boolean isSpeed()
    {
    	for(int i = currentChar.xcoord; i < currentChar.xcoord + currentChar.getCharImage().getWidth(); i++)
        {
        	for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
        		if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.BLUE.getRGB())
        			return true;
        	}
        }
    	return false;
    }
    
    
    
    private boolean checkLeaf()
    {
    	for(int i = currentChar.xcoord; i < currentChar.xcoord + currentChar.getCharImage().getWidth(); i++)
        {
        	for(int j = currentChar.ycoord; j < currentChar.ycoord + currentChar.getCharImage().getHeight(); j++){
        		if(currentWorld.imageWorld_mask.getRGB(i,j) == Color.GREEN.getRGB())
        			done = true;
        	}
        }
    	return done;
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
            JOptionPane.showConfirmDialog(null, "Image Not Found", "Image Not Found", JOptionPane.ERROR_MESSAGE);
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
            int keyCode = e.getKeyCode();
            if(isSludge())
        	{
        		char1.setVX(slow);
        		char1.setVY(slow);
        		char2.setVX(slow);
        		char2.setVY(slow);
        	}
        	else if(isSpeed())
        	{
        		char1.setVX(fast);
        		char2.setVX(fast);
        	}
        	else
        	{
        		char1.setVX(normal);
        		char1.setVY(normal);
        		char2.setVX(normal);
        		char2.setVY(normal);
        	}
            if(keyChar == 'd' || keyCode == KeyEvent.VK_RIGHT)
            {
            	if(!checkWall('r'))
                if(char1.getXcoord() + char1.getVX() <= world1.getWidth() + offset - char1.getCharImage().getWidth())
                {
                    char1.moveRight();
                    char2.moveRight();
                }
            }
            else if(keyChar == 'a' || keyCode == KeyEvent.VK_LEFT)
            {
            	if(!checkWall('l')){
	                char1.moveLeft();
	                char2.moveLeft();
            	}
            }
            else if(keyChar == 's' || keyCode == KeyEvent.VK_DOWN)
            {
            	if(!checkWall('d')){
	                if(char1.getYcoord() + char1.getVY() <= world1.getHeight() - char1.getCharImage().getHeight())
	                {
	                    char1.moveDown();
	                    char2.moveDown();
	                }
            	}
            }
            else if(keyChar == 'w'|| keyCode == KeyEvent.VK_UP)
            {
            	if(!checkWall('u')){
	                char1.moveUp();
	                char2.moveUp();
            	}
            }
            else if(keyChar == ' ')
            {
            	if(checkTeleport())
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
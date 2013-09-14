import javax.swing.*;

import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.Timer;


public class OverWorld implements KeyListener, ActionListener, MouseListener
{
    public WorldPainter world1;
    public WorldPainter world2;
    public JFrame overWorldFrame;
    public Container overWorldPanel;
    private Character char1;
    private Character char2;
    private Character currentChar;
    //private ArrayList<Light> lights1 = new ArrayList<Light>();
    //private ArrayList<Light> lights2 = new ArrayList<Light>();
    
    private Timer timer;
    private int currentX = 0;

    public static void main(String[] args)
    {
        OverWorld overworld = new OverWorld();
    }
    
    public OverWorld()
    {
        overWorldFrame = new JFrame();
        overWorldPanel = overWorldFrame.getContentPane();
        char1 = new Character("./resource/charimage.png");
        char2 = new Character("./resource/charimage.png");
        char1.setInvisible(false);
        char2.setInvisible(true);
        world1 = new WorldPainter();
        world2 = new WorldPainter();
        world1.setBackground(Color.WHITE);
        world2.setBackground(Color.WHITE);
        
        overWorldPanel.setLayout(new BoxLayout(overWorldPanel, BoxLayout.Y_AXIS));
        overWorldFrame.setTitle("Over World");
        overWorldFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
       
        currentChar = char1;
        overWorldFrame.requestFocus();
    }
    
    public void overPaintChars()
    {
        overWorldFrame.requestFocus();
        if(currentChar == char1)
        {
            currentX++;
            world1.paintChar(char1.getCharImage(), char1.getXcoord(), char1.getYcoord());
            world1.setShouldPaintChars(true);
            world2.setShouldPaintChars(false);
            world1.repaint();
            world2.repaint();
        }
        else 
        {
            currentX++;
            world2.paintChar(char2.getCharImage(), char2.getXcoord(), char2.getYcoord());
            world2.setShouldPaintChars(true);
            world1.setShouldPaintChars(false);
            world2.repaint();
            world1.repaint();
        }
    }
    
    /*
    public void paintLight()
    {
    	currentX++;
    	world1.paintLight(lights1);
    	world2.paintLight(lights2);
    }
    */
    
    public void keyTyped(KeyEvent e)
    {
   
    }

    public void keyPressed(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if(keyChar == 'd')
        {
            if(char1.getXcoord() + 10 <= world1.getWidth() + currentX - char1.getCharImage().getWidth())
            {
                char1.moveRight();
                char2.moveRight();
            }
        }
        else if(keyChar == 'a')
        {
            char1.moveLeft();
            char2.moveLeft();
        }
        else if(keyChar == 's')
        {
            if(char1.getYcoord() + 10 <= world1.getHeight() - char1.getCharImage().getHeight())
            {
                char1.moveDown();
                char2.moveDown();
            }
        }
        else if(keyChar == 'w')
        {
            char1.moveUp();
            char2.moveUp();
        }
        else if(keyChar == ' ')
        {
            if(currentChar == char1)
            {
                char1.setInvisible(true);
                char2.setInvisible(false);
                currentChar = char2;
            }
            else
            {
                char1.setInvisible(false);
                char2.setInvisible(true);
                currentChar = char1;
            }
        } 
    }
    public void keyReleased(KeyEvent e) {}
    
    public void actionPerformed(ActionEvent e)
    {
        overPaintChars();
        //paintLight();
        if(currentChar.xcoord - currentX < -char1.getCharImage().getWidth())
        {
        	currentChar.isAlive = false;
        }
        if(!currentChar.isAlive)
        {
        	world1.setBackground(Color.BLACK);
            world2.setBackground(Color.BLACK);
        }
    }
    
    public void mouseClicked(MouseEvent e)
    {
        overWorldFrame.requestFocus();
    }
    public void mouseEntered(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}

    private class WorldPainter extends JPanel
    {
        private BufferedImage imageChar;
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
        
        /*
        public void paintLight(ArrayList<Light> lights)
        {
            this.lights = lights;
        }*/

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            if(shouldPaintChars) {g.drawImage(imageChar, xChar - currentX, yChar, this);}
            /*
            Graphics2D g2 = (Graphics2D) g;
            for (Light light:lights)
            {
            	int x = light.getX() - currentX;
            	int y = light.getY();
            	int[] xp = {x,x-60,x+60};
            	int[] yp = {y,y+50,y+50};
            	Polygon p = new Polygon(xp,yp,3);
            	g2.draw(p);
            	g2.draw(new Rectangle2D.Double(x-60,y+50,120,250));
            }*/
            g.translate(-currentX, 0);
            //g2.translate(-currentX, 0);
        }
    }
}
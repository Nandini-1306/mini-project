// A game called Brick Braker by GUI framework like Swing and AWT 

// imported the essential packages/module .

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.BasicStroke;

//class JPanel implements the inbuilt interfaces KeyListener and ActionListener from awt.event package

 class play extends JPanel implements KeyListener , ActionListener 
{
           private boolean play = false;
           private int score =0;

           private int totalBricks = 21;

           private Timer timer;  //Timer generates action at periodic interval which is used to trigger an event 
           private int delay =9; //9 seconds 

           private int playerX = 310;

           private int ballposX = 10;   
           private int ballposY = 350;
           private int ballXdir = -1;
           private int ballYdir =-2;

           private MapGenerator map;

         public play()
           {
               map = new MapGenerator(3,7);

               // Register the KeyListener with the component .IT adds input from keyboard
               addKeyListener(this);
               // Ensure that the component is focusable and request focus to receive keyboard input
               setFocusable(true);
               setFocusTraversalKeysEnabled(false);
               timer = new Timer(delay , this);
               timer.start();
           }

     public void paint(Graphics g)
           {
            //Details of  background 
            g.setColor(Color.black);
            g.fillRect(1,1,692,592);

            //drawing map 
            map.draw((Graphics2D) g); //Graphics2D extends the graphics class which focuses on geometry and color managment.

            // Details of borders
            g.setColor(Color.white);
            g.fillRect(0,0,3,592);
            g.fillRect(0,0,692,3);
            g.fillRect(691,0,3,592);

            //Layout of scores 
            g.setColor(Color.RED);
            g.setFont(new Font("serif " , Font.ITALIC , 25)  );
            g.drawString("Current Score : "+score ,50,30);   //draw string specifies the graphics context's coordinate system.

            // paddle 
            g.setColor(Color.red);
            g.fillRect(playerX , 550 , 100 , 8);

            // the ball

            g.setColor(Color.blue);
            g.fillOval(ballposX ,ballposY , 20 , 20);

            if(totalBricks <=0)
            {
                play = false;
                ballXdir = 0;
                ballYdir = 0;
                g.setColor(Color.RED);
                g.setFont(new Font("serif " , Font.ITALIC , 25));
                g.drawString("YOU WON!" , 260 , 300);

                g.setFont(new Font("serif " , Font.ITALIC , 25));
                g.drawString("Press Enter to Restart ", 230 , 350);
                
            }

            if(ballposY >570)
            {
                play = false;
                ballXdir = 0;
                ballYdir = 0;
                g.setColor(Color.RED);
                g.setFont(new Font("serif " , Font.ITALIC , 25));
                g.drawString("Game Over ,Scores :" , 190 , 300);

                g.setFont(new Font("serif " , Font.ITALIC , 25));
                g.drawString("Press Enter to Restart ", 230 , 350);
            }

            g.dispose();
            /*Disposes of this graphics context 
             releases any system resources that it is using Graphics object
             A Graphics object cannot be used after dispose has been called.
             */
           }

    
    public void actionPerformed(ActionEvent e) //action performed is invoked when action occurs
    {  
        timer.start();  //starts the events sending start signal to listener
        
        if(play)
        {
            if(new Rectangle(ballposX , ballposY , 20,20).intersects(new Rectangle(playerX , 550 , 100 , 8)))
            {
                ballYdir = - ballYdir;
            }
          A :  for(int i=0;i<map.map.length;i++)
            {
                for(int j=0; j<map.map[0].length;j++)
                {
                    if(map.map[i][j] > 0)
                    {
                        int brickX = j*map.brickwidth +80;
                        int brickY = i* map.brickheight + 50;
                        int brickwidth = map.brickwidth;
                        int brickheight = map.brickheight;

                        Rectangle rect = new Rectangle(brickX , brickY , brickwidth , brickheight);
                        Rectangle ballRect = new Rectangle(ballposX , ballposY , 20 , 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score +=5;

                            if(ballposX + 19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = - ballXdir;
                            } 
                            else
                            {
                                ballYdir = -ballYdir;
                            }
                            break A ;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            
            if(ballposX <0)
            {
                ballXdir = - ballXdir;
            }
             if(ballposY <0)
            {
                ballYdir = - ballYdir;
            }
             if(ballposX >670)
            {
                ballXdir = - ballXdir;
            }
        }
        repaint(); //It is used to schedule a component to be redrawn by the system, and it ultimately triggers the paint method.
    }

    public void keyTyped(KeyEvent e) 
    {
        //just implementing the abstract method of keyListener interfaces 
    }

    public void keyPressed(KeyEvent e) 
    {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
        if(playerX >=600)
        {
            playerX =600;
        }
        else
        {
            moveRight();
        }
        } 

        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
        if(playerX < 10)
        {
            playerX = 10;
        }
        else
        {
            moveLeft();
        }
        } 
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!play)
            {
                play = true;
                ballposX =120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score =0;
                totalBricks = 21;
                map = new MapGenerator(3,7);
                repaint();
            }
        }
    }

    public void moveRight()
   {
       play = true;
       playerX +=20;
   }

   public void moveLeft()
   {
       play = true;
       playerX -=20;
       
   }
    public void keyReleased(KeyEvent e)
    {
        //implemented the abstract method of KeyListener class
    } 
}

class MapGenerator
{
    public int map[][];
    public int brickwidth;
    public  int brickheight;
    public MapGenerator(int row , int col)
    {
        map = new int[row][col];
        for(int i=0;i<map.length;i++)
        {
            for(int j=0;j<map[0].length;j++)
            {
                map[i][j] = 1;
            }
        }
        brickwidth = 540/col;
        brickheight = 150/row;
    }

    public void draw(Graphics2D g)
    {
        for(int i=0 ; i<map.length;i++)
        {
            for(int j=0 ;j<map[0].length;j++)
            {
               if(map[i][j] >0)
               {
                g.setColor(Color.white);
                g.fillRect(j*brickwidth + 80 , i*brickheight + 50 , brickwidth , brickheight );

                g.setStroke(new BasicStroke(3));
                g.setColor(Color.black);
                g.drawRect(j*brickwidth + 80 , i*brickheight + 50 , brickwidth , brickheight);
               }
            }
        }
    }

    public void setBrickValue(int value , int row , int col)
    {
        map[row][col] = value;
    }

}
public class MyGame
{
    public static void main(String args[])
    {
        JFrame obj = new JFrame();
        play game = new play();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Braker");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(game);
    }
}
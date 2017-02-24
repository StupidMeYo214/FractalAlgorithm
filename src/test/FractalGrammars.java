package test;
// FractalGrammars.java

// Copied from Section 8.3 of
//    Ammeraal, L. and K. Zhang (2007). Computer Graphics for Java Programmers, 2nd Edition,
//       Chichester: John Wiley.

import java.awt.*;
import java.awt.event.*;

public class FractalGrammars extends Frame
{  public static void main(String[] args)
   {  
         new FractalGrammars("Tree2.txt");
   }
   FractalGrammars(String fileName)
   {  super("Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      setSize(800, 600);
      add("Center", new CvFractalGrammars(fileName));
      setVisible(true);
   }
}

class CvFractalGrammars extends Canvas
{  String fileName, axiom, strF, strf, strX, strY;
   int maxX, maxY, level = 6; 
   double xLast, yLast, dir, rotation, dirStart, fxStart, fyStart,
      lengthFract, reductFact;
   double cuMark;

   void error(String str)
   {  System.out.println(str);
      System.exit(1);
   }

   CvFractalGrammars(String fileName)
   {  Input inp = new Input(fileName);
      if (inp.fails())
         error("Cannot open input file.");
      axiom = inp.readString(); inp.skipRest();
      strF = inp.readString(); inp.skipRest();
      strf = inp.readString(); inp.skipRest();
      strX = inp.readString(); inp.skipRest();
      strY = inp.readString(); inp.skipRest();
      rotation = inp.readFloat(); inp.skipRest();
      dirStart = inp.readFloat(); inp.skipRest();
      fxStart = inp.readFloat(); inp.skipRest();
      fyStart = inp.readFloat(); inp.skipRest();
      lengthFract = inp.readFloat(); inp.skipRest();
      reductFact = inp.readFloat();
      if (inp.fails())
         error("Input file incorrect.");
               
      addMouseListener(new MouseAdapter()
      {  public void mousePressed(MouseEvent evt)
         {  if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            {  level--;      // Right mouse button decreases level
               if (level < 5)
                  level = 5;
            }
            else            
               level++;      // Left mouse button increases level
            repaint();
         }
      });

   }

   Graphics g;
   int iX(double x){return (int)Math.round(x);}
   int iY(double y){return (int)Math.round(maxY-y);}

/*   void drawTo(Graphics g, double x, double y)
   {  
	   g.drawLine(iX(xLast), iY(yLast), iX(x) ,iY(y));
	   System.out.println("lastX: "+xLast);
	   System.out.println("LastY: "+yLast);
	   System.out.println("X: "+x);
	   System.out.println("Y: "+y);
	   System.out.println("cumark= "+cuMark);
	   System.out.println("===============================");
      xLast = x;
      yLast = y;
      cuMark -= 1; 
     
   }
*/
   void moveTo(Graphics g, double x, double y)
   {  xLast = x;
      yLast = y;
   }

   
   void drawTo(Graphics g, double x, double y, double l)
   {  
	   double xA = x, yA = y, xB = xLast, yB = yLast;
	   double xGap = (xA - xB)/10, yGap = (yA - yB)/10;
	   int [] xPoints = new int[4];
	   int [] yPoints = new int[4];
	   
		   for(int i = 0; i < 10; i++){
			   double sinTheta = Math.abs(yA - yB)/l;
			   double cosTheta = Math.abs(xA - xB)/l;
			   double xC = xB + xGap*i;
			   double yC = yB + yGap*i;
			   double xCu = xB + xGap*(i+1);
			   double yCu = yB + yGap*(i+1);
			   double dist = Math.sqrt(Math.pow(xCu-xLast, 2)+Math.pow(yCu - yLast, 2));
			   double lengthHalf = cuMark-dist*0.01;
			   double xC1 = xC - lengthHalf*sinTheta;
			   double yC1 = yC - lengthHalf*cosTheta;
			   double xC2 = xC + lengthHalf*sinTheta;
			   double yC2 = yC + lengthHalf*cosTheta;
			   double xC3 = xCu + lengthHalf*sinTheta;
			   double yC3 = yCu + lengthHalf*cosTheta;
			   double xC4 = xCu - lengthHalf*sinTheta;
			   double yC4 = yCu - lengthHalf*cosTheta;
			   xPoints[0] = iX(xC1);
			   xPoints[1] = iX(xC2);
			   xPoints[2] = iX(xC3);
			   xPoints[3] = iX(xC4);
			   yPoints[0] = iY(yC1);
			   yPoints[1] = iY(yC2);
			   yPoints[2] = iY(yC3);
			   yPoints[3] = iY(yC4);
			   g.fillPolygon(xPoints, yPoints, 4);
			   
		
	   }
		   cuMark /= 1.02;

	   
	   g.drawLine(iX(xLast), iY(yLast), iX(x) ,iY(y));
      xLast = x;
      yLast = y;
     
   }
   

   
   public void paint(Graphics g) 
   {  Dimension d = getSize();
      maxX = d.width - 1;
      maxY = d.height - 1; 
      xLast = fxStart * maxX;
      yLast = fyStart * maxY;
      cuMark = 30;
      dir = dirStart;   // Initial direction in degrees
      turtleGraphics(g, axiom, level, lengthFract * maxY);  
  
   }

   public void turtleGraphics(Graphics g, String instruction, 
      int depth, double len) 
   {  double xMark=0, yMark=0, dirMark=0;
   	  double cuMarkTemp = 0;
      for (int i=0;i<instruction.length();i++) 
      {  char ch = instruction.charAt(i);
         switch(ch)
         {
         case 'F': // Step forward and draw
            // Start: (xLast, yLast), direction: dir, steplength: len

            if (depth == 0)
            {  double rad = Math.PI/180 * dir, // Degrees -> radians
                  dx = len * Math.cos(rad), dy = len * Math.sin(rad);
              //drawTo(g, xLast + dx, yLast + dy);
            	drawTo(g, xLast + dx, yLast + dy, len);
            }
            else
            {cuMark /= 1.02;
               turtleGraphics(g, strF, depth - 1, reductFact * len); }
            break;
         case 'f': // Step forward without drawing
            // Start: (xLast, yLast), direction: dir, steplength: len
            if (depth == 0)
            {  double rad = Math.PI/180 * dir, // Degrees -> radians
                  dx = len * Math.cos(rad), dy = len * Math.sin(rad);
               moveTo(g, xLast + dx, yLast + dy);
            }
            else
               turtleGraphics(g, strf, depth - 1, reductFact * len); 
            break;
         case 'X':
            if (depth > 0)
            { cuMark /= 1.02;
               turtleGraphics(g, strX, depth - 1, reductFact * len);}
            break;
         case 'Y':
            if (depth > 0)
            {cuMark /= 1.02;
               turtleGraphics(g, strY, depth - 1, reductFact * len);}
            break;
         case '+': // Turn right
        	cuMark/=1.02;
            dir -= rotation; break;
         case '-': // Turn left
        	 cuMark/=1.02;
            dir += rotation; break;
         case '[': // Save position and direction
        	
            xMark = xLast; yMark = yLast; dirMark = dir; 
            cuMarkTemp = cuMark/1.02;
            break;
         case ']': // Back to saved position and direction
            xLast = xMark; yLast = yMark; dir = dirMark;
            cuMark = cuMarkTemp;
            break;
         }
      }
   }
}

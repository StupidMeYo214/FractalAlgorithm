package test;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;

public class Julia extends Frame{
	
	double cRe, cIm;

	public static void main(String[] args) {new Julia(-0.76,0.084);}
	
	Julia(double cRe, double cIm){
		this.cRe = cRe;
		this.cIm = cIm;
		setTitle("x= "+cRe+" , y= "+cIm);
		
		      addWindowListener(new WindowAdapter()
		         {public void windowClosing(WindowEvent e){e.getWindow().dispose();}});
		      setSize(380, 200);

		      add("Center", new JuliaCV(cRe, cIm));
		      
		      setVisible(true);
	}
	
	class JuliaCV extends Canvas {

		final double minRe0 = -2.0, maxRe0 = 1.0, 
                minIm0 = -1.0, maxIm0 = 1.0;
		double minRe = minRe0, maxRe =  maxRe0, 
                  minIm = minIm0, maxIm =  maxIm0, factor, r;
		int n, xs, ys, xe, ye, w, h;
		double cRe, cIm;
		
		public JuliaCV(double cRe, double cIm) {
			// TODO Auto-generated constructor stub
			this.cRe = cRe;
			this.cIm = cIm;
			repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			Dimension dimension = getSize();
			w = dimension.width;
			h = dimension.height;
			r = w/h;
			//double cRe = -0.76, cIm = 0.084;
			factor = Math.max((maxRe - minRe)/w, (maxIm - minIm)/h);
			for (int yPix = 0; yPix < h ; yPix++){
				for (int xPix = 0; xPix < w; xPix++){
					double x = minRe + xPix*factor,
							y = minIm +yPix*factor;
					int nMax = 100,n;
					for (n = 0; n < nMax; n++){
						double x2 = x*x, y2 = y*y;
						if (x2 + y2 > 4)
							break;
						y = 2*x*y + cIm;
						x = x2 - y2 +cRe;
					}
					g.setColor(n == nMax ? Color.BLACK : new Color(100 + 155 * n / nMax, 0, 0));
					g.drawLine(xPix, yPix, xPix, yPix);
				}
			}
		}
		
	}
	
	
	
}

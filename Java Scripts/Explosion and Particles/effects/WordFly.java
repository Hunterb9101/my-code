package effects;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Random;

import environment.SemFin2;
import particleCommons.ColorRange;

public class WordFly extends SemFin2{
	Thread t;
	public ColorRange colorer = ColorRange.pastel;
	public Random rand = new Random();
	int width;
	int height;

	double x;
	double y;
	double dX;
	double dY;

	public WordFly(double ix, double iy, double idX, double idY,int iWidth,int iHeight){
		x = ix;
		y = iy;
		dX = idX;
		dY = idY;
		width = iWidth;
		height = iHeight;
	}

	public void drawWord(Graphics g, String s){
		int txtWidth = g.getFontMetrics().stringWidth(s);
		g.setColor(Color.black);
		g.fillRect((int)(x)-1,(int)y-16,txtWidth,(int)22);

		g.setColor(colorer.generateColor());

		g.drawRect((int)(x)-1,(int)y-16,txtWidth,(int)22);
		g.drawString(s,(int)x,(int)y);

		x+=dX;
		y+=dY;

		if(x>width-txtWidth/2 || x<txtWidth/2){
			dX = -1*dX;
			dY = rand.nextDouble()*2 - 1;
		}

		if(y>height-txtWidth || y<15){
			dX = rand.nextDouble()*2 - 1;
			dY = -1*dY;
		}

		try{
			Thread.sleep(5);
		}
		catch(InterruptedException e){
		}
	}
}

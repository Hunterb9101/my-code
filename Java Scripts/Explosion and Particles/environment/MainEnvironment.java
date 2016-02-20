package environment;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;




import particleCommons.ColorRange;
import effects.Snowfall;
import explosion.*;
import explosion.Explosion.explosionShape;

public class MainEnvironment extends ConstructorClass {
	private static final long serialVersionUID = 1L;
	int explosionState = 0;
	Explosion e;
	Snowfall s = new Snowfall(180,new ColorRange(Color.white));
	
	Random rand = new Random();
	
	public void doInitialization(int width, int height){
		this.setSize(600,600);
		Snowfall.width = 600;
		Snowfall.height = 600;
		s.velocity = 2;
		s.initializeEffect();
		
	}
	
	synchronized public void drawFrame(Graphics g, int width, int height) {
		this.setSize(600,600);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		/*
		double time = (double)getElapsedTime()/1000;
		x = (120*Math.cos(Math.toRadians(45))*time);
		y =  -1*((120*Math.sin(Math.toRadians(45))*time)-(16*Math.pow(time, 2)))+600;
		System.out.println(x + "/" + y);
		g.setColor(Color.GREEN);
		g.fillOval((int)x, (int)y, 16, 16);
		*/
		
		s.drawSnowfall(g);
		Explosion.updateAllExplosions(g);
		g.setColor(Color.white);
		g.fillRect(0, 575, 600, 25);
		
		g.setColor(Color.red);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 42)); 
		int txtwidth =g.getFontMetrics().stringWidth("Merry Christmas!")/2;
		g.drawString("Merry Christmas!", 150, 300);
		
		g.setColor(Color.green);
		g.fillRect(150, 305, txtwidth*2, 3);
		g.setFont(new Font("TimesRoman", Font.ITALIC, 32)); 
		g.drawString("2015", 275, 335);
	}

	public void mousePressed(MouseEvent evt){
		super.mousePressed(evt);
		
		e = new Explosion(evt.getPoint(),80,35,ColorRange.christmas);
		e.blastDir = Explosion.dirs.ALL;
		e.changesColor = true;
		e.velocity = 4;
		e.particleSize = 8;
		e.particleShape = Explosion.shapes.FILLEDCIRCLE;
		
		switch(explosionState){
			case 0: 
				e.explosionType = explosionShape.RANDOM;
				break;
			case 1:
				e.explosionType = explosionShape.CIRCLE;
				break;
			case 2:
				e.explosionType = explosionShape.SQUARE;
				break;
			case 3:
				e.explosionType = explosionShape.STAR;
				break;
			default:
				e.explosionType = explosionShape.RANDOM;
				e.particleShape = Explosion.shapes.FILLEDCIRCLE;
				break;
		}
	e.initializeExplosion();
	}
	
	public void keyPressed(KeyEvent evt) {
		if(evt.getKeyChar() == 'a'){
			if(explosionState==0){
				explosionState=3;
			}
			else{
				explosionState--;
			}
		}
		
		if(evt.getKeyChar() == 'd'){
			if(explosionState==3){
				explosionState = 0;
			}
			else{
				explosionState++;
			}
		}
	}
}



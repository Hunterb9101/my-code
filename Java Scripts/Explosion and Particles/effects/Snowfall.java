package effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import particleCommons.ColorRange;
import particleCommons.Particle;

public class Snowfall {
	public static int width = 1;
	public static int height = 1;
	public Random rand = new Random(); //for random number generation

	public enum shapes{SQUARE,CIRCLE,FILLEDSQUARE,FILLEDCIRCLE,ASTERIK};

	public int velocity = 2;
	
	public shapes particleShape = shapes.ASTERIK;
	public int particleSize = 8;
	public int particleVariability = 4;
	
	public ColorRange particleColor;
	int particleNum; // Number of particles

	public boolean isLive = false; //Explosion is running

	int currLife = 0; //Current frame of Snowflakes

	ArrayList<Particle> allParticles;

	public Snowfall(int iParticles, ColorRange iColorRange){
		particleNum = iParticles;
		allParticles = new ArrayList<>();
		particleColor = iColorRange;
	}
	
	public Snowfall(int iParticles, Color iColor){
		particleNum = iParticles;
		allParticles = new ArrayList<>();
		particleColor = new ColorRange(iColor);
	}


	public void initializeEffect(){
		double dX;
		double dY;
		Particle newParticle;
		
		isLive = true;
		currLife = 0;
		
		for(int i = 0; i<particleNum; i++){
			dX = 0;
			dY = velocity + rand.nextDouble()*velocity;
			newParticle = new Particle(new Point(rand.nextInt(width),0),dX,dY,0,particleColor.generateColor());
			newParticle.size = rand.nextInt(particleVariability) + particleSize - particleVariability/2;
			allParticles.add(newParticle);	
		}
	}
	
	public void drawSnowfall(Graphics g){
		Particle newParticle;
		double dX;
		double dY;
		
		isLive = true;
		for(int i = 0; i<allParticles.toArray().length;i++){
			Particle p = allParticles.get(i);
			if(p.currY>height){
				allParticles.remove(i);

				dX = 0;
				dY = velocity + rand.nextDouble()*velocity;
				newParticle = new Particle(new Point(rand.nextInt(width),1),dX,dY,0,particleColor.generateColor());
				newParticle.size = rand.nextInt(particleVariability) + particleSize - particleVariability/2;
				
				allParticles.add(newParticle);
			}
			else{
				p.currX+=allParticles.get(i).deltX;
				p.currY+=allParticles.get(i).deltY;

				g.setColor(p.color);
				
				int drawX = (int) (p.currX -(p.size/2));
				int drawY = (int) (p.currY -(p.size/2));
				switch(particleShape){
				case SQUARE:
					g.drawRect(drawX,drawY, p.size, p.size);
					break;
				case CIRCLE:
					g.drawOval(drawX,drawY, p.size, p.size);
					break;
				case FILLEDSQUARE:
					g.fillRect(drawX,drawY, p.size, p.size);
					break;
				case FILLEDCIRCLE:
					g.fillOval(drawX,drawY, p.size, p.size);
					break;
				
				case ASTERIK:
					//drawX = g.getFontMetrics().stringWidth("*")/2;
					g.setFont(new Font("TimesRoman", Font.PLAIN, p.size*3)); 
					g.drawString("*", drawX, drawY);
					break;
				}
			}
		}
		currLife++;
	}
}

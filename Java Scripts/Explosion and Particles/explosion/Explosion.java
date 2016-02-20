package explosion;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import particleCommons.*;

public class Explosion {
	public static ArrayList<Explosion> allExplosions = new ArrayList<>();
	public Random rand = new Random(); //for random number generation

	public enum dirs{ALL,UP,DOWN,LEFT,RIGHT};
	public enum shapes{SQUARE,CIRCLE,FILLEDSQUARE,FILLEDCIRCLE};
	public enum explosionShape{RANDOM,SQUARE,CIRCLE,STAR};

	public dirs blastDir = dirs.ALL;
	public explosionShape explosionType= explosionShape.RANDOM;
	public int velocity = 2;
	public boolean gravity = true;
	
	public shapes particleShape = shapes.SQUARE;
	public int particleSize = 8;
	public ColorRange particleColor;
	public boolean changesColor = false;
	int particleNum; // Number of particles
	int particleMinLife;

	public boolean isLive = false; //Explosion is running

	int currLife = 0; //Current frame of Explosion
	int dur; //Maximum life of Explosion
	Point p; //Explosion Origin

	ArrayList<Particle> allParticles;

	public Explosion(Point iP, int iParticles, int iDur, ColorRange iColorRange){
		p = iP;
		dur = iDur;
		particleMinLife = 3/4*dur;
		particleNum = iParticles;
		allParticles = new ArrayList<>();
		particleColor = iColorRange;
		allExplosions.add(this);
	}

	public void initializeExplosion(){
		double dX = 0;
		double dY = 0;
		isLive = true;
		currLife = 0;
		
		for(int i = 0; i<particleNum; i++){
			switch(explosionType){
			case RANDOM:
				switch(blastDir){
					case ALL:
						dX = rand.nextDouble()*(2*velocity) - (velocity);
						dY = rand.nextDouble()*(2*velocity) - (velocity);
						break;
					case UP:
						dX = rand.nextDouble()*(2*velocity) - (velocity);
						dY = rand.nextDouble()*velocity*-1;
						break;
					case DOWN:
						dX = rand.nextDouble()*(2*velocity) - (velocity);
						dY = rand.nextDouble()*velocity;
						break;
					case LEFT:
						dX = rand.nextDouble()*velocity*-1;
						dY = rand.nextDouble()*(2*velocity) - (velocity);
						break;
					case RIGHT:
						dX = rand.nextDouble()*velocity;
						dY = rand.nextDouble()*(2*velocity) - (velocity);
						break;
					default:
						dX = 0;
						dY = 0;
				}
				break;
				
			case STAR:
				double speed = velocity*rand.nextDouble();
				dX = speed*((rand.nextInt(3))-1);
				dY = speed*((rand.nextInt(3))-1);
				break;
			case SQUARE:
				if(rand.nextInt(3) == 1){
					dX = velocity*((rand.nextInt(3))-1);
					dY = rand.nextDouble()*velocity*((rand.nextInt(3))-1);
				}
				else{
					dX = rand.nextDouble()*velocity*((rand.nextInt(3))-1);
					dY = velocity*((rand.nextInt(3))-1);
				}
				if(dX == 0){
					dX = velocity;
				}
				else if(dY == 0){
					dY = velocity;
				}
				break;
			case CIRCLE:				
				double angle = rand.nextInt(360);
				dX = velocity * Math.cos(Math.toRadians(angle)) ;
				dY = velocity * Math.sin(Math.toRadians(angle));
				break;
			}
			allParticles.add(new Particle(p,dX,dY,rand.nextInt(dur-particleMinLife) + particleMinLife,particleColor.generateColor()));
		}
	}
	
	public static void updateAllExplosions(Graphics g){
		for(int i = 0; i<allExplosions.toArray().length; i++){
			allExplosions.get(i).drawExplosion(g);
			if(!allExplosions.get(i).isLive){allExplosions.remove(i);}
		}
	}
	
	public void drawExplosion(Graphics g){
		isLive = true;
		for(int i = 0; i<allParticles.toArray().length;i++){
			Particle p = allParticles.get(i);
			if(p.lifespan < currLife){allParticles.remove(i);}
			else{
				p.currX+=allParticles.get(i).deltX;
				p.currY+=allParticles.get(i).deltY;

				if(changesColor){g.setColor(particleColor.generateColor());}
				else{g.setColor(p.color);}
				
				int drawX = (int) (p.currX -(particleSize/2));
				int drawY = (int) (p.currY -(particleSize/2));
				
				switch(particleShape){
				case SQUARE:
					g.drawRect(drawX,drawY, particleSize, particleSize);
					break;
				case CIRCLE:
					g.drawOval(drawX,drawY, particleSize, particleSize);
					break;
				case FILLEDSQUARE:
					g.fillRect(drawX,drawY, particleSize, particleSize);
					break;
				case FILLEDCIRCLE:
					g.fillOval(drawX,drawY, particleSize, particleSize);
					break;
				}
			}
		}
		if(allParticles.toArray().length == 0){
			isLive = false;
		}
		currLife++;
	}
}


////////////////////////////////////////
//     	Classes Used By Explosion     //
////////////////////////////////////////


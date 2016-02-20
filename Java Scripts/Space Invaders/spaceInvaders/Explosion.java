package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Explosion {
	public enum types{EXPLOSION};
	public enum color{RAINBOW,REDSCALE,SOLID};
	public Random rand = new Random(); //for random number generation

	boolean isLive = false; //Explosion is running

	int currLife = 0; //Current frame of Explosion
	int dur; //Maximum life of Explosion

	int x; // Explosion origins
	int y;

	int particles; // Number of particles
	ArrayList<Particle> allParticles;

	public Explosion(int iX, int iY, int iDur, int iParticles){
		x = iX;
		y = iY;
		dur = iDur;
		particles = iParticles;
		allParticles = new ArrayList<>();
	}

	public void initializeExplosion(){
		for(int i = 0; i<particles; i++){
			int r = rand.nextInt(59) + 196;
			int gr = rand.nextInt(255);
			if(gr>r){
				r = gr;
			}
			allParticles.add(new Particle(x,y,rand.nextDouble()*4-2,rand.nextDouble()*4-2,rand.nextInt(dur)+5,new Color(r,gr,0)));
		}
	}
	public void drawExplosion(Graphics g){
		isLive = true;
		for(int i = 0; i<allParticles.toArray().length;i++){
			if(allParticles.get(i).lifespan < currLife){
				allParticles.remove(i);
			}
			else{
				allParticles.get(i).currX+=allParticles.get(i).deltX;
				allParticles.get(i).currY+=allParticles.get(i).deltY;

				g.setColor(allParticles.get(i).color);
				g.drawRect((int)allParticles.get(i).currX -4,(int) allParticles.get(i).currY - 4, 8, 8);
			}
		}
		if(allParticles.toArray().length == 0){
			isLive = false;
		}
		currLife++;
	}
}
class Particle {
	double currX;
	double currY;
	double deltX;
	double deltY;
	int lifespan;

	Color color;
	public Particle(double x, double y, double dX, double dY, int life, Color iColor){
		currX = x;
		currY = y;
		deltX = dX;
		deltY = dY;
		lifespan = life;
		color = iColor;
	}
}
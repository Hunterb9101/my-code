import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Explosion {
	public static final ColorRange fire = new ColorRange(196,255,0,255,0,0,'r'); // Explosion-Like
	public static final ColorRange rainbow = new ColorRange(0,255,0,255,0,255); // Contains all Colors
	public static final ColorRange pastel = new ColorRange(196,255,196,255,196,255); // Has pastel-like colors
	public static final ColorRange blueScale = new ColorRange(0,0,0,0,0,255); //Blueish Scale
	public static final ColorRange dark = new ColorRange(0,60,0,60,0,60); //Dark Colors
	
	public static ArrayList<Explosion> allExplosions = new ArrayList<>();
	public Random rand = new Random(); //for random number generation

	public enum dirs{ALL,UP,DOWN,LEFT,RIGHT};
	public enum shapes{SQUARE,CIRCLE,FILLEDSQUARE,FILLEDCIRCLE};

	public dirs blastDir = dirs.ALL;
	public int velocity = 2;
	public boolean gravity = true;
	
	public shapes particleShape = shapes.SQUARE;
	public int particleSize = 8;
	public ColorRange particleColor;
	public boolean changesColor = false;
	int particleNum; // Number of particles
	int particleMinLife;

	boolean isLive = false; //Explosion is running

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
	
	public Explosion(Point iP, int iParticles, int iDur, Color iColor){
		p = iP;
		dur = iDur;
		particleMinLife = 3/4*dur;
		particleNum = iParticles;
		allParticles = new ArrayList<>();
		particleColor = new ColorRange(iColor.getRed(),iColor.getGreen(),iColor.getBlue());
		allExplosions.add(this);
	}


	public void initializeExplosion(){
		double dX;
		double dY;
		isLive = true;
		currLife = 0;
		for(int i = 0; i<particleNum; i++){
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
			allParticles.add(new Particle(p,dX,dY,rand.nextInt(dur-particleMinLife) + particleMinLife,particleColor.generateColor()));
		}
		}

		public void initializeExplosion(int newX, int newY){
			p.x = newX;
			p.y = newY;
			isLive = true;
			currLife = 0;
			double dX;
			double dY;
			for(int i = 0; i<particleNum; i++){
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
class ColorRange{
	Random rand = new Random();
	int r1;
	int r2;
	int g1;
	int g2;
	int b1;
	int b2;
	char dominantColor = 'n';

	public ColorRange(int ir1, int ir2, int ig1, int ig2, int ib1, int ib2){
		// Makes sure that the bottom range is in the first slot //
		if(r2>r1){r1 = ir2; r2 = ir1;}
		else{r1 = ir1; r2 = ir2;}

		if(g2>g1){g1 = ig2; g2 = ig1;}
		else{g1 = ig1; g2 = ig2;}

		if(b2>b1){b1 = ib2; b2 = ib1;}
		else{b1 = ib1; b2 = ib2;}
	}

	public ColorRange(int r, int g, int b){
		r1 = r; r2 = r;
		g1 = g; g2 = g;
		b1 = b; b2 = b;
	}

	public ColorRange(int ir1, int ir2, int ig1, int ig2, int ib1, int ib2, char dominant){
		// Makes sure that the bottom range is in the first slot //
		if(r2>r1){r1 = ir2; r2 = ir1;}
		else{r1 = ir1; r2 = ir2;}

		if(g2>g1){g1 = ig2; g2 = ig1;}
		else{g1 = ig1; g2 = ig2;}

		if(b2>b1){b1 = ib2; b2 = ib1;}
		else{b1 = ib1; b2 = ib2;}

		dominantColor = dominant;
	}

	public Color generateColor(){
		int r;
		int g;
		int b;
		try{r = rand.nextInt(r2-r1)+r1;}
		catch(IllegalArgumentException e){r = r1;}

		try{g = rand.nextInt(g2-g1)+g1;}
		catch(IllegalArgumentException e){g = g1;}

		try{b = rand.nextInt(b2-b1)+b1;}
		catch(IllegalArgumentException e){b = b1;}

		if(dominantColor == 'r'){
			if(r<g){g = r;}
			if(r<b){b = r;}
		}
		else if (dominantColor == 'g'){
			if(g<r){r = g;}
			if(g<b){b = g;}
		}
		else if (dominantColor == 'b'){
			if(b<r){r = b;}
			if(b<g){g = b;}
		}

		return new Color(r,g,b);
	}
}

class Particle {
	double currX;
	double currY;
	
	double deltX;
	double deltY; 
	double angle; 
	
	int lifespan;
	Color color;

	public Particle(Point p, double dX, double dY, int life, Color iColor){
		currX = p.getX();
		currY = p.getY();
		deltX = dX;
		deltY = dY;
		lifespan = life;
		color = iColor;
	}
}
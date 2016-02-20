package particleCommons;

import java.awt.Color;
import java.awt.Point;

public class Particle {
	public double currX;
	public double currY;
	
	public double deltX;
	public double deltY; 
	
	public int lifespan;
	
	public Color color;
	
	public int size = 8;

	public Particle(Point p, double dX, double dY, int life, Color iColor){
		currX = p.getX();
		currY = p.getY();
		deltX = dX;
		deltY = dY;
		lifespan = life;
		color = iColor;
	}
}


import java.awt.*;
import java.awt.event.*;

public class RocketShip extends ConstructorClass implements MouseMotionListener {
	Explosion e;
	int x = 300;
	int y = 451;
	int velocity = 2;

	public void doInitialization(int width, int height){
		this.setSize(600,600);
	}

	synchronized public void drawFrame(Graphics g, int width, int height) {
		this.setSize(600,600);

		Image rocketImg = getImage(getCodeBase(), "rocket-ship.png");
		Image bg = getImage(getCodeBase(), "Background.png");

		g.setColor(Color.lightGray);
		g.fillRect(0, 0, width, height);
		
		g.drawImage(bg,0,0,600,600,this);

		y-= velocity;
		velocity = velocity*4/3;

		e = new Explosion(new Point(x+40,y+149),80,20,Explosion.fire);
		e.blastDir = Explosion.dirs.DOWN;
		e.velocity = 5;
		e.changesColor = true;
		e.particleSize = 16;
		e.particleShape = Explosion.shapes.FILLEDCIRCLE;
		e.initializeExplosion(x+40,y+130);

		Explosion.updateAllExplosions(g);
		
		g.drawImage(rocketImg,x,y,81,149,this);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {}
}



package spaceInvaders;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Alien extends SpaceInvaders{
	public static ArrayList<Alien> allAliens = new ArrayList<Alien>();
	public Explosion e;
	public static int total = 0;
	public static int killed = 0;
	public static int alive = 0;

	public int baseScore = 250;
	public static int leftMostAlienX = 100000;
	public static int rightMostAlienX = 0;

	public static enum status{DEAD,ALIVE};

	public boolean exploding = false;
	public status alienStatus = status.ALIVE;
	public static int speed = 3;

	public int centerX;
	public int centerY;

	public static boolean isMovingLeft = false;
	public static boolean isMovingDown = false;

	public int explosionFrameNumber;

	public Alien(int x, int y, int width, int height){
		alive++;
		total++;
		centerX = x;
		centerY = y;

		if(centerX > rightMostAlienX){
			rightMostAlienX = centerX;
		}
		if(centerX < leftMostAlienX){
			leftMostAlienX = centerX;
		}

		allAliens.add(this);
	}

	public void makeAlienFrame(Graphics g, Image img, int width, int height){
	// If Alien is hit //
	if(explosionFrameNumber == 1){
		e = new Explosion(centerX,centerY,25,52);
		e.initializeExplosion();
	}
	if (alienStatus == status.DEAD && exploding) {
		e.drawExplosion(g);
		/*
			if (explosionFrameNumber == 14) {
			}
			else if (explosionFrameNumber > 10) { // For frames 11, 12, and
				explosionFrameNumber++;
			}
			else { // For frames 1 through 10, draw the sub under an expanding
				g.setColor(Color.white);
				g.fillOval(centerX - 30, centerY - 15, 60, 30);

				g.setColor(Color.yellow);
				g.fillOval(centerX - 4 * explosionFrameNumber, centerY- 2 * explosionFrameNumber, 8 * explosionFrameNumber, 4 * explosionFrameNumber);

				g.setColor(Color.red);
				g.fillOval(centerX - 2 * explosionFrameNumber, centerY- explosionFrameNumber / 2, 4 * explosionFrameNumber,explosionFrameNumber);

				explosionFrameNumber++;
			}
			*/
		explosionFrameNumber++;
	}
	// Normal Move Sequence
	else {
		if (isMovingLeft) { centerX -= speed;} // Move the alien left
		else {centerX += speed;} 	// Move the alien right
		
		if(isMovingDown){
			centerY += 20;
			speed = speed*7/6;
		}
		
		g.drawImage(img, centerX - 18, centerY -18,36,36,null);
		}

		leftMostAlienX  = findSmallestX();
		rightMostAlienX = findBiggestX();
	}

	public boolean isHit(int bulletX, int bulletY, boolean react, Player user){
		if(Math.abs(bulletX - centerX) <= 18 && Math.abs(bulletY - centerY) <= 18){
			if(react){
				alienStatus = status.DEAD;
				
				/* Score Formula 
				 * This Formula uses a percent error calculation between the
				 * shot and the alien's center point. The closer you get to the center 
				 * point, the more points you get!!!
				 */
				double normalizedPercent = Math.abs(
						(Math.sqrt(Math.pow(centerX - bulletX,2) + Math.pow(centerY - bulletY, 2)) 
						- Math.sqrt(Math.pow(centerX,2) + Math.pow(centerY,2)))
						/(Math.sqrt(Math.pow(centerX,2) + Math.pow(centerY,2))));
				
				double skewedPercent = Math.pow(normalizedPercent, 4);
				
				user.score+=baseScore*skewedPercent;
				
				if(!exploding){
					exploding = true;

					killed++;
					alive--;

					explosionFrameNumber = 1;
				}
			}
			return true;
		}
		else{
			return false;
		}
	}

	public int findSmallestX(){
		int num = 10000;
		for(int i = 0; i<allAliens.toArray().length;i++){
			if(allAliens.get(i).centerX < num){
				num = allAliens.get(i).centerX;
			}
		}
		return num;
	}
	
	public int findBiggestX(){
		int num = 0;
		for(int i = 0; i<allAliens.toArray().length;i++){
			if(allAliens.get(i).centerX > num){
				num = allAliens.get(i).centerX;
			}
		}
		return num;
	}
}
package spaceInvaders;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

import spaceInvaders.Player.status;

//need for music and sound

public class SpaceInvaders extends ConstructorClass {
	public Random rand = new Random();
	// ********Global Variables
	int defaultWidth = 600;
	int defaultHeight = 600;

	int GUIHeight = 50;
	int bulletCoordX;
	int bulletCoordY;
	boolean bulletIsShooting;

	Player p1;

	Explosion[] currExplosions = new Explosion[42];
	public void doInitialization(int width, int height) {
		for(int i = 0; i<currExplosions.length;i++){
			currExplosions[i] = new Explosion(rand.nextInt(width),rand.nextInt(height),rand.nextInt(120) + 5,52);
			currExplosions[i].initializeExplosion();
		}
		System.out.println("Initializing");
		this.setFont(new Font("Times New Roman", Font.BOLD, 22));
		for(int r = 0; r<2; r++){
			for(int i = 0; i<10; i++){
				new Alien(10+i*40,25*r + GUIHeight,width,height);
			}
		}
		this.setSize(600,600);
		p1 = new Player(defaultWidth/2,defaultHeight - 36);
	} // doInitialization

	// All drawing is done here //
	synchronized public void drawFrame(Graphics g, int width, int height) {
		Image tankImg = getImage(getCodeBase(), "Tank.png");
		Image alienImg = getImage(getCodeBase(), "UFO.png");
		
		this.setSize(600,600);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);


		g.setColor(Color.BLUE);
		g.drawString("Aliens Killed: " + Alien.killed, 5, GUIHeight - 20);

		int txtWidth = g.getFontMetrics().stringWidth("Score: ");
		g.drawString("Score: " + p1.score,width-txtWidth-100, GUIHeight - 20);
		
		g.setColor(Color.GRAY);
		g.drawLine(0,GUIHeight,width,GUIHeight);
		
		p1.makeTankFrame(g, tankImg, width, height);
		/*
		 * Deal with Alien movement...
		 */
		if(p1.playerStatus == status.PLAYING){
			if(Alien.isMovingLeft){
				Alien.leftMostAlienX-=Alien.speed;
				Alien.rightMostAlienX-=Alien.speed;
			}
			else{
				Alien.leftMostAlienX+=Alien.speed;
				Alien.rightMostAlienX+=Alien.speed;
			}

			if ((Alien.rightMostAlienX + Alien.speed > defaultWidth - 20) && !Alien.isMovingLeft) {
				Alien.isMovingLeft = true;
				Alien.isMovingDown = true;
			}
			else if ((Alien.leftMostAlienX - Alien.speed < 0)) {
				Alien.isMovingLeft = false;
				Alien.isMovingDown = true;
			}
			else{
				Alien.isMovingDown = false;
			}
			
			for(int i = 0; i<Alien.allAliens.toArray().length; i++){
				Alien currAlien = Alien.allAliens.get(i);
				currAlien.makeAlienFrame(g, alienImg, width, height);
				if(currAlien.centerY > height - 45){ p1.playerStatus = status.DEAD;}
				if(currAlien.total == currAlien.killed){p1.playerStatus = status.WON;}
			}
			
			doBulletFrame(g, width, height);
		}
		
		if(p1.playerStatus == status.DEAD){
			g.setColor(Color.red);
			txtWidth = g.getFontMetrics().stringWidth("You Died!");
			g.drawString("You Died!", width/2 - txtWidth/2, height/2);
		}
		else if(p1.playerStatus == status.WON){
			for(int i = 0; i<currExplosions.length; i++){
				currExplosions[i].drawExplosion(g);
				if(!currExplosions[i].isLive){
					if(rand.nextInt(13) == 1){
						AudioClip audioClip = getAudioClip(getCodeBase(), "AlienExplosion.wav");
						audioClip.play();
					}
					currExplosions[i] = new Explosion(rand.nextInt(width),rand.nextInt(height),rand.nextInt(120) + 5,52);
					currExplosions[i].initializeExplosion();
					currExplosions[i].drawExplosion(g);
				}
			}
			
			txtWidth = g.getFontMetrics().stringWidth("You Win!");
			
			g.setColor(Color.BLUE);
			g.drawString("You Win!", width/2 - txtWidth/2, height/2);
		}
	}

	void initBullet() {
		bulletIsShooting = false;
		bulletCoordX = p1.x + 18;
		bulletCoordY = p1.y;
	} // initBullet

	void clearBullet(){
		bulletCoordX = defaultWidth + 100;
		bulletCoordY = defaultHeight + 100;
	}

	void doBulletFrame(Graphics g, int width, int height) {
		boolean keepMoving = true;
		if (bulletIsShooting) {
			if (bulletCoordY <= 0) {
				clearBullet();
				bulletIsShooting = false;
				keepMoving = false;
			}
			Alien currAlien;
			for(int i = 0; i<Alien.allAliens.toArray().length; i++){
				currAlien = ((Alien)(Alien.allAliens.get(i)));

				if(currAlien.isHit(bulletCoordX,bulletCoordY,false,null) && currAlien.alienStatus == Alien.status.ALIVE){
					bulletIsShooting = false;

					AudioClip audioClip = getAudioClip(getCodeBase(), "AlienExplosion.wav");
					audioClip.play();

					currAlien.isHit(bulletCoordX,bulletCoordY,true,p1);

					keepMoving = false;
					clearBullet();
				}
			}

			if(keepMoving){
				g.setColor(Color.red);
				bulletCoordY -= 25;
				g.fillRect(bulletCoordX, bulletCoordY, 4, 8);
			}
		}
		else {
			g.setColor(Color.red);
			g.fillRect(bulletCoordX, bulletCoordY,4, 8);

		}
	}


	public void mousePressed(MouseEvent evt) {
		super.mousePressed(evt);
		if(!bulletIsShooting){
			initBullet();
			AudioClip audioClip = getAudioClip(getCodeBase(), "PlayerShot.wav");
			audioClip.play();
		}
		bulletIsShooting = true;
	}

	public void keyPressed(KeyEvent evt) {
		if(evt.getKeyChar() == 'a'){
			p1.currDir = Player.dir.LEFT;
		}
		else if(evt.getKeyChar() == 'd'){
			p1.currDir = Player.dir.RIGHT;
		}
		else{
			p1.currDir = Player.dir.STOPPED;
		}
	}

	public void keyReleased(KeyEvent evt){
		p1.currDir = Player.dir.STOPPED;
	}

}




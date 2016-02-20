package spaceInvaders;

import java.awt.Graphics;
import java.awt.Image;

public class Player extends SpaceInvaders{
	public int x;
	public int y;

	public int score = 0;
	public int speed = 5;
	public enum status{DEAD,PLAYING,WON};
	public enum dir{LEFT,STOPPED,RIGHT};
	public dir currDir = dir.STOPPED;
	public status playerStatus = status.PLAYING;

	public Player(int iX, int iY){
		x = iX;
		y = iY;
	}
	void makeTankFrame(Graphics g, Image img, int width, int height) {

		if(playerStatus == status.PLAYING){
			switch(currDir){
			case LEFT:
				x-=speed;
				break;
			case RIGHT:
				x+=speed;
				break;
			default:
				break;
			}
			g.drawImage(img, x, y ,36,36,null);
		}
	}
}


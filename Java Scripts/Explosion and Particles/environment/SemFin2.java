package environment;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.Random;

import effects.WordFly;
import particleCommons.ColorRange;

public class SemFin2 extends Applet implements ActionListener{
	Thread t = null;
	int width = 1400;
	int height = 800;

	public Random rand = new Random();
	public void init(){
		setBackground(Color.black);
		setFont(new Font("Times New Roman", Font.BOLD, 22));
		setLayout(null);
		setSize(width,height);
	}

	public void paint(Graphics g){
		WordFly w = new WordFly(300,300, rand.nextDouble()*2-1,rand.nextDouble()*2-1,width,height);
		w.colorer = ColorRange.fire;

		WordFly w2 = new WordFly(600,600, rand.nextDouble()*2-1,rand.nextDouble()*2-1,width,height);
		w2.colorer = ColorRange.rainbow;

		WordFly w3 = new WordFly(450,600, rand.nextDouble()*2-1,rand.nextDouble()*2-1,width,height);
		w3.colorer = ColorRange.pastel;

		WordFly w4 = new WordFly(100,100, rand.nextDouble()*2-1,rand.nextDouble()*2-1,width,height);
		w4.colorer = ColorRange.dark;

		// #8 drawCircles(g,300,300);
		// #9 randLines(g);
		// #10 randLinesWithRefresh(g);
		// #11 and #12
		while(true){
			w.drawWord(g,"Hunter");
			w2.drawWord(g,"RAINBOWS!");
			w3.drawWord(g,"FIRE!!!");
			w4.drawWord(g,"Word...");
		}
	}

	public void randLines(Graphics g){
		while(true){
			g.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
			g.drawLine(rand.nextInt(600),rand.nextInt(600),rand.nextInt(600),rand.nextInt(600));

			try{
				t.sleep(2);
			}
			catch(InterruptedException e){
			}
		}
	}

	public void randLinesWithRefresh(Graphics g){
		int i = 0;
		while(true){
			g.setColor(new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));
			g.drawLine(rand.nextInt(600),rand.nextInt(600),rand.nextInt(600),rand.nextInt(600));

			if(i==100){
				g.setColor(Color.white);
				g.fillRect(0,0,600,600);
				i=0;
			}
					try{
						t.sleep(2);
					}
					catch(InterruptedException e){
					}
				i++;
		}
	}

	public void drawCircles(Graphics g, int x, int y){
		g.drawOval(300,300,20,20);
		g.drawOval(290,290,40,40);
		g.drawOval(280,280,60,60);
		g.drawOval(270,270,80,80);
		g.drawOval(260,260,100,100);
		g.drawOval(250,250,120,120);
	}

	public void actionPerformed(ActionEvent e){
	}
}

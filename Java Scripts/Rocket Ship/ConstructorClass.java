
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ConstructorClass extends JApplet implements
		ActionListener, KeyListener, FocusListener, MouseListener {
	
	protected void doInitialization(int width, int height) {
	}

	protected void drawFrame(Graphics g, int width, int height) {
		this.setSize(600,600);
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		g.drawString("Elapsed Time:  " + (getElapsedTime() / 1000), 10, 20);
		g.drawString("Frame Number:  " + (getFrameNumber()), 10, 35);
	}

	public void keyTyped(KeyEvent evt) {}
	public void keyPressed(KeyEvent evt) {}
	public void keyReleased(KeyEvent evt) {}

	public int getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(int frameNumber) {
		if (frameNumber < 0)
			this.frameNumber = 0;
		else
			this.frameNumber = frameNumber;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setFrameCount(int max) {
		if (max <= 0)
			this.frameCount = -1;
		else
			frameCount = max;
	}

	public void setMillisecondsPerFrame(int time) {
		millisecondsPerFrame = time;
		if (timer != null) timer.setDelay(millisecondsPerFrame);
	}

	public void setFocusBorderColor(Color c) {
		focusBorderColor = c;
	}


	private int frameNumber = 0; // Current frame number.
	private int frameCount = -1; // If > 0, frame number loops from
									// frameNumber-1 back to 0.
	private int millisecondsPerFrame = 40; // Approximate time between frames.

	private long startTime; // Time since animation was started or restarted.
	private long oldElapsedTime; // Holds time the animation had been running
									// before
									// it was most recently restarted.
	private long elapsedTime; // Time animation has been running. This is set
								// just before each frame is drawn.

	private Timer timer; // The timer that drives the animation.

	private JPanel frame; 
	private boolean focussed = false; 
	Color focusBorderColor = Color.black; 

	public ConstructorClass() {
		frame = new JPanel() {
			public void paintComponent(Graphics g) {
				int width = 600;
				int height = 600;
				drawFrame(g, width, height);
				if(focussed)  g.setColor(focusBorderColor); 
				else g.setColor(ConstructorClass.this.getBackground());
				g.drawRect(0, 0, width - 1, height - 1);
				g.drawRect(1, 1, width - 3, height - 3);
				g.drawRect(2, 2, width - 5, height - 5);
				if (!focussed) { 
					g.setColor(ConstructorClass.this.getForeground());
					g.drawString("Development Environment Stopped", 10, height - 12);
				}
			}
		};
		setContentPane(frame);
		setBackground(Color.red); 
		setForeground(Color.red);
		frame.setFont(new Font("SanSerif", Font.BOLD, 30));
		frame.addFocusListener(this);
		frame.addKeyListener(this);
		addMouseListener(this);
	}

	public void init() {
		doInitialization(getSize().width, getSize().height);
	}

	public void actionPerformed(ActionEvent evt) {
		frameNumber++;
		if (frameCount > 0 && frameNumber >= frameCount)
			frameNumber = 0;
		elapsedTime = oldElapsedTime + (System.currentTimeMillis() - startTime);
		frame.repaint();
	}

	private void startAnimation() {
		if (focussed) {
			if (timer == null) {
				timer = new Timer(millisecondsPerFrame, this);
				timer.start();
			} else
				timer.restart();
			startTime = System.currentTimeMillis();
		}
	}

	private void stopAnimation() {
		if (focussed) {
			oldElapsedTime += (System.currentTimeMillis() - startTime);
			elapsedTime = oldElapsedTime;
			frame.repaint();
			timer.stop();
		}
	}

	public void start() {
		startAnimation();
	}

	public void stop() {
		stopAnimation();
	}

	public void focusGained(FocusEvent evt) {
		focussed = true;
		startAnimation();
	}

	public void focusLost(FocusEvent evt) {
		stopAnimation();
		focussed = false;
	}

	public void mousePressed(MouseEvent evt) {
		frame.requestFocus();
	}

	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	public void mouseReleased(MouseEvent evt) {}
	public void mouseClicked(MouseEvent evt) {}

} // end class KeyboardAnimationApplet2



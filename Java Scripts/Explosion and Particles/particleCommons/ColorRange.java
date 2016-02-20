package particleCommons;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ColorRange{
	public static final Color[]  christmasScheme = new Color[]{Color.green,Color.red,new Color(192,192,192)};
	
	public static final ColorRange fire = new ColorRange(new Color(196,0,0),new Color(255,255,0),'r'); // Explosion-Like
	public static final ColorRange christmas = new ColorRange(christmasScheme);
	public static final ColorRange rainbow = new ColorRange(Color.black,Color.white); // Contains all Colors
	public static final ColorRange pastel = new ColorRange(new Color(196,196,196),new Color(255,255,255)); // Has pastel-like colors
	public static final ColorRange blueScale = new ColorRange(Color.black,Color.BLUE); //Blueish Scale
	public static final ColorRange dark = new ColorRange(Color.black, new Color(60,60,60)); //Dark Colors
	
	Random rand = new Random();
	public ArrayList<Color> allowedColors = new ArrayList<Color>();
	int[][] colorRange = new int[2][3];
	char dominantColor = 'n';

	public ColorRange(Color fColor, Color lColor){
		int fHue = 0;
		int lHue = 0;
		for(int i = 0; i<3; i++){
			switch(i){
			case 0: 
				fHue = Integer.max(fColor.getRed(),lColor.getRed());
				lHue = Integer.min(fColor.getRed(),lColor.getRed());
				break;
			case 1:
				fHue = Integer.max(fColor.getGreen(),lColor.getGreen());
				lHue = Integer.min(fColor.getGreen(),lColor.getGreen());
				break;
			case 2: 
				fHue = Integer.max(fColor.getBlue(),lColor.getBlue());
				lHue = Integer.min(fColor.getBlue(),lColor.getBlue());
				break;
			}
			colorRange[0][i] = fHue;
			colorRange[1][i] = lHue;
		}
	}

	public ColorRange(Color onlyColor){
		int hue = 0;
		for(int i = 0; i<3; i++){
			switch(i){
			case 0: hue = onlyColor.getRed();
			case 1: hue = onlyColor.getGreen();
			case 2: hue = onlyColor.getBlue();
			}
			colorRange[0][i] = hue;
			colorRange[1][i] = hue;
		}
	}

	public ColorRange(Color fColor, Color lColor, char dominant){
		int fHue = 0;
		int lHue = 0;
		for(int i = 0; i<3; i++){
			switch(i){
			case 0: 
				fHue = Integer.max(fColor.getRed(),lColor.getRed());
				lHue = Integer.min(fColor.getRed(),lColor.getRed());
				break;
			case 1:
				fHue = Integer.max(fColor.getGreen(),lColor.getGreen());
				lHue = Integer.min(fColor.getGreen(),lColor.getGreen());
				break;
			case 2: 
				fHue = Integer.max(fColor.getBlue(),lColor.getBlue());
				lHue = Integer.min(fColor.getBlue(),lColor.getBlue());
				break;
			}
			colorRange[0][i] = fHue;
			colorRange[1][i] = lHue;
		}

		dominantColor = dominant;
	}
	
	public ColorRange(ArrayList<Color> colors){
		allowedColors = colors;
	}
	public ColorRange(Color[] colors){
		allowedColors = new ArrayList<Color>(Arrays.asList(colors));
	}
	
	public Color generateColor(){
		int[] hues = new int[3];
		char[] dominant = {'r','g','b'};
		if(allowedColors.toArray().length != 0){
			try{
				return allowedColors.get(rand.nextInt(allowedColors.toArray().length));
			}catch(IllegalArgumentException e){
				return allowedColors.get(0);
			}
		}
		else{
			for(int i = 0; i<hues.length; i++){
				try{
					hues[i] = rand.nextInt(colorRange[0][i] - colorRange[1][i]) + colorRange[1][i];
				}
				catch(IllegalArgumentException e){
					hues[i] = colorRange[0][i];
				}
			}
			
			for(int i = 0; i<hues.length; i++){
				if(dominantColor == dominant[i]){
					hues[i] = Integer.max(hues[0],Integer.max(hues[1],Integer.max(hues[2],hues[i])));
				}
			}
			return new Color(hues[0],hues[1],hues[2]);
		}
	}
}

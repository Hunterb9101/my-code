import java.util.Scanner;
import java.util.Random;

public class RockPaperScissorsLizardSpock{
	public static Scanner ui = new Scanner(System.in);
	public static Random rand = new Random();
	public static Boolean playing = true;

	public static void printTextSlowly(String text){
			try{
				for(int i = 0; i<text.length(); i++){
					System.out.print(text.substring(i,i+1));
					Thread.sleep(25);
				}
				System.out.println("");
			}catch(InterruptedException e){}
	}

	public static void main(String[] args){
		String option;
		String cOption;
		while(playing){
			printTextSlowly("What is your choice? Rock, Paper, Scissors, Lizard, or Spock???");
			option = ui.nextLine();
			cOption = ((RoShamBoObject)RoShamBoObject.allObjects.get(rand.nextInt(RoShamBoObject.allObjects.toArray().length))).name;
			switch(RoShamBoObject.compareObjects(option,cOption)){
				case 0: printTextSlowly("You tied!!! " + option + " vs. " + cOption); break;
				case 1: printTextSlowly("You won!!!  " + option + " vs. " + cOption); break;
				case 2: printTextSlowly("You lost!!! " + option + " vs. " + cOption); break;
			}

			printTextSlowly("Would you like to play again???");

			if(!ui.nextLine().equalsIgnoreCase("Yes")){
				playing = false;
			}
		}
	}
}


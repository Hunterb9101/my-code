import javax.swing.JOptionPane;
import java.util.*;
import java.io.*;

public class Pokemon{
	public static BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));

	public static List<String> pokemon = new ArrayList<String>();
	public static List<Integer> hPoints = new ArrayList<Integer>();
	public static List<Integer> aPoints = new ArrayList<Integer>();

	public static void addPokemon(String name, int hp, int attk){
		pokemon.add(name);
		hPoints.add(hp);
		aPoints.add(attk);
	}

	public static void main(String args[]) throws IOException{
		addPokemon("pikachu",35,55);
		addPokemon("jigglypuff",115,45);
		addPokemon("carizard",78,84);
		addPokemon("squirtle",44,48);
		addPokemon("abra",25,20);
		addPokemon("meowth",40,45);
		addPokemon("tangela",65,55);
		addPokemon("regigigas",110,160);
		addPokemon("ponyta",50,85);
		addPokemon("vulpix",38,41);
		addPokemon("deoxys",50,150);
		addPokemon("machoke",80,100);
		addPokemon("magnemite",25,35);
		addPokemon("ghastly",30,35);
		addPokemon("onix",35,45);
		addPokemon("ditto",48,48);
		addPokemon("porygon",65,60);
		addPokemon("gyrados",95,125);
		addPokemon("cody",1,0);
		addPokemon("hunter",9999,9999);

		int win=0;
		int lose=0;

		String myPokemon;
		int myPokeHP = 0;
		int myPokeAttk = 0;

		String enemyPokemon;
		int enemyPokeHP = 0;
		int enemyPokeAttk = 0;

		boolean playing = true;
		while(playing) {
			for(int i = 0; i<2; i++){
				String name;
				int idxNum = -1;

				for(int i1 = 0; i1 < 3; i1++) {
					int newPokemon = (int) (Math.random()*pokemon.size()) +0;
					System.out.println(pokemon.get(newPokemon));
				}
				
				boolean passed = false;
				while(!passed){
					name = JOptionPane.showInputDialog(null, "Player one Pick one of these three pokemon: ");
					try{
						idxNum = pokemon.indexOf(name);
						passed = true;
					}
					catch(IndexOutOfBoundsException e){}
				}
				JOptionPane.showMessageDialog(null,"You have chosen: " + pokemon.get(idxNum) + " HP: " + hPoints.get(idxNum) + " Attack: " + aPoints.get(idxNum));
				if(i == 0){
					myPokemon = pokemon.get(idxNum);
					myPokeHP = hPoints.get(idxNum);
					myPokeAttk = aPoints.get(idxNum);
				}
				else{
					enemyPokemon = pokemon.get(idxNum);
					enemyPokeHP = hPoints.get(idxNum);
					enemyPokeAttk = aPoints.get(idxNum);
				}
			}

			int turn = 0;

			do{
				int dice = (int) (Math.random() * 6) + 1;
				JOptionPane.showMessageDialog(null, "press ok to roll the dice" );
				double multiplier = 0;
				if(dice == 2 || dice == 3){
					multiplier = .5;
				}
				else if(dice == 4 || dice == 5){
					multiplier = 1;
				}
				else if(dice == 6){
					multiplier = 2;
				}

				if(turn % 2 == 0){ //Player 1
					enemyPokeHP = enemyPokeHP - (int)multiplier*myPokeAttk;
				}
				else{ //Player 2
					myPokeHP = myPokeHP - (int)multiplier*enemyPokeAttk;
				}
					System.out.println("Player " + (turn%2 + 1) + " did " + multiplier*((turn%2 == 0)? myPokeAttk:enemyPokeAttk) + " damage. " + "(x" + multiplier + ")");
				turn++;

			} while((myPokeHP >=0) && (enemyPokeHP>=0));

			if(myPokeHP > enemyPokeHP){
				System.out.println("Player 1 Wins!!!!");
				win++;
			}
			else{
				System.out.println("Player 2 Wins!!!");
				lose++;
			}

			System.out.println("Player 1's record is: " + win + "-" + lose);
			System.out.println("Do you want to play again?");

			String yON = dataIn.readLine();

			if(yON.equalsIgnoreCase("Yes")){
				System.out.println("Begin the next battle");
			}
			else{
				playing= false;
			}
		}
	}
}



import java.util.ArrayList;
import java.util.Collections;
public class Cards{
	public static ArrayList<Cards> deck = new ArrayList<Cards>();
	public static String[] suits = {"DIAMONDS","CLUBS","HEARTS","SPADES"};

	public String suit;
	public int num;

	public Cards(){}

	public Cards(String iSuit, int cardNum){
		suit = iSuit;
		num = cardNum;
		deck.add(this);
	}

	public static void shuffle(){
		Collections.shuffle(deck);
	}

	public static void createDeck(){
		deck.clear();
		for(int d = 0; d<6; d++){
			for(int s = 0; s<4; s++){
				for(int i = 1; i< 14; i++){
					new Cards(suits[s],i);
				}
			}
		}
	}
	public static boolean hasAce(ArrayList<Cards> deck){
		for(int i = 0; i<deck.toArray().length; i++){
			if(deck.get(i).num == 1){
				return true;
			}
		}
		return false;
	}
	
	public int bjCardVal(){
		if(num > 1 && num <= 10){
			return num;
		}
		else if(num > 10){
			return 10;
		}
		else{
			return 11; // This is not necessarily correct, Aces can be either a 1 or an 11;
		}
	}

	public String getNormalizedName(){
		String cardName = "";

		if(num > 1 && num <= 10){
			cardName = String.valueOf(num);
		}
		else if(num > 10 || num == 1){
			switch(num){
				case 1: cardName = "Ace"; break;
				case 11: cardName = "Jack"; break;
				case 12: cardName = "Queen"; break;
				case 13: cardName = "King"; break;
				default: cardName = "Null"; break;
			}
		}

		return (cardName + " of " + suit);
	}

	public static void dealCard(BjPlayer player){
		player.myCards.add(deck.get(0));
		deck.remove(0);
	}

	public static void dealCards(int num, BjPlayer player){
		for(int i = 0; i<num; i++){
			dealCard(player);
		}
	}
}

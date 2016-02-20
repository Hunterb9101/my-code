
import java.io.*;
import java.util.*;

public class BlackJack{
	public final static BjPlayer nullPlayer = new BjPlayer();
	public static Scanner scan = new Scanner(System.in);
	
	private static BjPlayer currPlayer;
	private static boolean isPlaying = true;
	private static int roundNum = 1;
	public static void main(String[] args) throws IOException{
		
		int playerNum = BjUtil.persistentLoop("How many players would you like?");
		for(int i = 0; i<playerNum; i++){
			System.out.println("What is player " + (i+1) + "'s name?");
			BjPlayer newPlayer = new BjPlayer(scan.nextLine());
		}
		BjPlayer dealer = new BjPlayer("Dealer",true);
		
		while(true){ // Safety net loop so game doesn't stop playing when a player gets a natural
			while(isPlaying){
				Cards.createDeck();
				for(int i = 0; i<10; i++){
					Cards.shuffle();
				}
				for(int i = 0; i<BjPlayer.playerQueue.toArray().length; i++){
					Cards.dealCards(2,BjPlayer.playerQueue.get(i));
				}
				
				// Print out player's cards //
				System.out.println("\n Starting Game... \n");
				
				System.out.println("-----------------------");
				System.out.println("        Round " + roundNum);
				System.out.println("-----------------------");
				BjPlayer.getAllCardStats();
				System.out.println();

				// Dealer Flip //
				if(dealer.myCards.get(0).bjCardVal() >= 10){
					System.out.println("Dealer has an ace or a card valued past 10, flipping other card");
					System.out.println("Dealer has a " + dealer.myCards.get(1).getNormalizedName());
				}

				// Check for Naturals //
				boolean aNatural = false;
				for(int i = 0; i<BjPlayer.playerQueue.toArray().length; i++){
					currPlayer = BjPlayer.playerQueue.get(i);
					if(currPlayer.getCardSum() == 21){
						System.out.println(currPlayer.name + " has a natural!");
						aNatural = true;
					}
				}
				if(aNatural){
					System.out.println("End of Round...");
					break;
				}
				
				
				for(int i = 0; i<BjPlayer.playerQueue.toArray().length; i++){
					currPlayer = BjPlayer.playerQueue.get(i);
					System.out.println(BjPlayer.playerQueue.get(i).name + "'s turn.");
					
					
					// Dealer Turn //
					if(BjPlayer.playerQueue.get(i).isDealer){
						while(currPlayer.getCardSum() < 17){
							Cards.dealCard(currPlayer);
							System.out.println("Dealer is hitting");
							System.out.println("    Got a " + currPlayer.myCards.get(currPlayer.myCards.toArray().length-1).getNormalizedName() + " for a total of: " + currPlayer.getCardSum());
						}
						System.out.println("Dealer stands at " + currPlayer.getCardSum());
					}
							
					// Player Turn //
					else{
						System.out.println("Press any key to see cards.");
						scan.nextLine();
						System.out.println();
						
						for(int c = 0; c<currPlayer.myCards.toArray().length; c++){
							System.out.println(currPlayer.myCards.get(c).getNormalizedName());
						}

						System.out.println("What would you like to do? Stand or Hit? Your total is: " + currPlayer.getCardSum());
						
						boolean firstTime = true;
						String userInput = "";
						
						while(!userInput.equalsIgnoreCase("stand") && currPlayer.getCardSum() < 21){
							if(!firstTime){
								System.out.println("What would you like to do? Stand or Hit? Your total is: " + currPlayer.getCardSum());
							}
							
							userInput = scan.nextLine();
							
							if(userInput.equalsIgnoreCase("hit")){
								System.out.println("    Cards: ");
								for(int c = 0; c<BjPlayer.playerQueue.get(i).myCards.toArray().length; c++){
									System.out.println("        " + BjPlayer.playerQueue.get(i).myCards.get(c).getNormalizedName());
								}
								
								Cards.dealCard(currPlayer);
								System.out.println("        Got a " + currPlayer.myCards.get(currPlayer.myCards.toArray().length-1).getNormalizedName());
							}
							firstTime = false;
						}
					}
					
					
					if(currPlayer.getCardSum() > 21){
						System.out.println(currPlayer.name + " went bust");
					}
					else if(currPlayer.getCardSum() == 21){
						System.out.println(currPlayer.name + " has hit the target of 21");
					}
					
					System.out.println();
				}
				
				BjPlayer closestPlayer = nullPlayer;
				ArrayList<BjPlayer> tiedPlayers = new ArrayList<>();
				
				for(int i = 0; i<BjPlayer.playerQueue.toArray().length; i++){
					if(BjPlayer.playerQueue.get(i).getCardSum() <= 21 && BjPlayer.playerQueue.get(i).getCardSum() > closestPlayer.getCardSum()){
						closestPlayer = BjPlayer.playerQueue.get(i);
						tiedPlayers.clear();
					}
					else if(BjPlayer.playerQueue.get(i).getCardSum() <= 21 && BjPlayer.playerQueue.get(i).getCardSum() == closestPlayer.getCardSum()){
						tiedPlayers.add(BjPlayer.playerQueue.get(i));
					}
				}
				
				if(tiedPlayers.toArray().length == 0){
					System.out.println(closestPlayer.name + " wins the bet with a total of " + closestPlayer.getCardSum());
				}
				else{
					System.out.println("The following players tied for highest standing of " + closestPlayer.getCardSum());
					System.out.println("    " + closestPlayer.name);
					
					for(int i = 0; i< tiedPlayers.toArray().length; i++){
						System.out.println("    " + tiedPlayers.get(i).name);
					}
				}
				System.out.println("Would you like to play another round? Say 'Yes' or 'No'");
				if(scan.nextLine().equals("No")){
					isPlaying = false;
					break;
				}
				else{
					
					// Reset Game to clear data
					for(int i = 0; i<BjPlayer.playerQueue.toArray().length;i++){
						BjPlayer.playerQueue.get(i).myCards.clear();
					}
					
					roundNum++;
				}
			}
			if(!isPlaying){
				break;
			}
		}
	}
}


/////////////////////////////////////
//				BlackJack Game     //

// Objective: Get a sum of 21 without going over
// (Aces can be either worth 1 or 11)
//
// Player on the left goes first and has 2 choices: Stand (No card) or Hit (Get another card)
// Round of play:
//   Place Bets($2 - $500)
//   Deal out 2 cards face up to each player, dealer gets 1 card face up and 1 card face down
//	 Player either stands or hits, until he stands on the total or goes "bust" (>21) if the player "busts", player loses the bet
//   Dealer goes to next player to the left, and serves him in the same manner
//
// Exceptions:
//   If a player gets a 10 card/Face Card and an ace, it is called a "Natural" or "BlackJack"
//     If any player has a natural and the dealer does not, the dealer immediately pays that player x1.5 the previous bet
//     If the dealer has a natural, the dealer collects all the bets of players who don't have naturals
//     If both a dealer and a player have naturals, the player takes back his chips
//
//   If the dealer doesn't have a 10-card or an ace, he doesn't look at the face-down card until dealer's turn.
//     However, if the dealer has a 10-card or an ace, he can look right away.
//
//    After all players deal, if the dealer has 17 or more cards, he must stand. If the total is 16 or under, he must take a card.
//
//
import java.util.ArrayList;
public class BjPlayer{
	public static ArrayList<BjPlayer> playerQueue = new ArrayList<BjPlayer>();
	public ArrayList<Cards> myCards = new ArrayList<Cards>();
	public String name;
	public boolean isDealer = false;

	public BjPlayer(){
	}
	
	public BjPlayer(String iName){
		name = iName;
		playerQueue.add(this);
	}
	public BjPlayer(String iName, boolean isDealer){
		this(iName);
		this.isDealer = true;
	}

	public static void getAllCardStats(){
		for(int i = 0; i<playerQueue.toArray().length; i++){
			System.out.println("    " + playerQueue.get(i).name + "'s hand:");
			System.out.println("        " + playerQueue.get(i).myCards.get(0).getNormalizedName());
			if(!playerQueue.get(i).isDealer){
				System.out.println("        " + playerQueue.get(i).myCards.get(1).getNormalizedName());
			}

			if(playerQueue.get(i).getCardSum() == 21){
				System.out.println("        natural!");
			}
		}
	}

	public int getCardSum(){
		int sum = 0;
		for(int i = 0; i<myCards.toArray().length; i++){
			sum+= myCards.get(i).bjCardVal();
		}
		return sum;
	}
}

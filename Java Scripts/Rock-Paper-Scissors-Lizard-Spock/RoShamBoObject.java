import java.util.ArrayList;

public class RoShamBoObject{
	public static ArrayList<RoShamBoObject> allObjects = new ArrayList<>();
	public String name = "";
	public String[] losesTo;

	public static final RoShamBoObject Rock = new RoShamBoObject("Rock",new String[]{"Paper","Spock"});
	public static final RoShamBoObject Paper = new RoShamBoObject("Paper",new String[]{"Scissors","Lizard"});
	public static final RoShamBoObject Scissors = new RoShamBoObject("Scissors",new String[]{"Spock","Rock"});
	public static final RoShamBoObject Lizard = new RoShamBoObject("Lizard",new String[]{"Scissors","Rock"});
	public static final RoShamBoObject Spock = new RoShamBoObject("Spock",new String[]{"Lizard","Paper"});

	public RoShamBoObject(String rName, String[] rLosesTo){
		name = rName;
		losesTo = rLosesTo;
		allObjects.add(this);
	}

	public static int compareObjects(String obj1, String obj2){
		if(obj1.equalsIgnoreCase(obj2)){
			return 0;
		}

		// Check only the first person, due to us already eliminating the option of a tie //
		for(int i = 0; i<allObjects.toArray().length; i++){
			if(obj1.equalsIgnoreCase(((RoShamBoObject)allObjects.get(i)).name)){
				for(int i1 = 0; i1<(((RoShamBoObject)allObjects.get(i))).losesTo.length; i1++){
					if(obj2.equalsIgnoreCase(((RoShamBoObject)allObjects.get(i)).losesTo[i1])){
						return 2;
					}
				}
			}
		}
		return 1;
	}
}

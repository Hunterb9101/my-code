import java.util.ArrayList;
import java.util.List;

import utils.Obj2Str;

public class FractionOperators {
		public String oName; //Name of the Operator
		public String[] oAcceptableInputs; // All acceptable types of the name
		public int oNumFrac; // Number of fractions allowed in the operation.
		public static List <String> oAllOperatorInputs = new ArrayList<String>();
		
	public FractionOperators(String iName, String[] iAcceptableInputs, int iNumFrac){
		oName = iName;
		oAcceptableInputs = iAcceptableInputs;
		oNumFrac = iNumFrac;
		for(String input : iAcceptableInputs){
			oAllOperatorInputs.add(input);
		}
	}
	
	public String inputExists(String iInput){ // Returns -1 if not found, 0 if in all, 1 if in class
		String inputExists = "None";
		String[] operatorInputs = new Obj2Str(oAllOperatorInputs.toArray()).ObjToString();
		
		for(String inputDat : operatorInputs){
			if(iInput.equalsIgnoreCase(inputDat)){
				inputExists = "All";
			}
		}
		for(String inputDat : oAcceptableInputs){
			if(iInput.equalsIgnoreCase(inputDat)){
				inputExists = "Class";
			}
		}
		return inputExists;
	}
	
	public void printAllOperators(){
		String[] operatorInputs = new Obj2Str(oAllOperatorInputs.toArray()).ObjToString();
		for(String operator : operatorInputs){
			System.out.println(operator);
		}
	}
}

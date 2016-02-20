import java.util.ArrayList;
import java.util.Random;

public class Int2Txt {
	public static Random rand = new Random();
	public static String printInt(long num){
		String strNum = String.valueOf(num);
		String totalString = "";
		ArrayList<String> bundleDigits = new ArrayList<>();
		String[] ones = {"","One","Two","Three","Four","Five","Six","Seven","Eight","Nine"};
		String[] unique = {"Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen","Nineteen"};
		String[] tens = {"","Ten","Twenty","Thirty","Forty","Fifty","Sixty","Seventy","Eighty","Ninety"};
		String[] bundleDigitNames = {"","Thousand","Million","Billion","Trillion","Quadrillion","Pentillion"};
		
		if(strNum.substring(0,1).equals("-")){
			totalString+= "Negative ";
			strNum = strNum.substring(1);
		}
		// Make sure that there is at least a multiple of 3 digits //
		while(strNum.length()%3 != 0){
			strNum = new StringBuilder(strNum).insert(0,"0").toString();
		}
		
		for(int i = 0; i<strNum.length()/3; i++){
			bundleDigits.add(strNum.substring(3*i,3*(i+1)));
		}
		int CurrInt = 0;
		boolean isUnique = false;
		for(int i = 0; i<bundleDigits.toArray().length; i++){
			CurrInt = Integer.parseInt(bundleDigits.get(i));
			if(CurrInt/100 > 0){
				totalString+= " " + ones[CurrInt/100] + " Hundred ";
				if(i+1 == bundleDigits.toArray().length && (CurrInt/100)*100!=0){
					totalString+= "and ";
				}
			}
			
			CurrInt = CurrInt - (CurrInt/100)*100;
			
			if(CurrInt/10 > 0){
				if(!(CurrInt > 10 && CurrInt < 20)){
					isUnique = false;
					totalString+= tens[CurrInt/10] + " ";
				}
				else{
					isUnique = true;
				}
			}
			
			CurrInt = CurrInt - (CurrInt/10)*10;
			if(CurrInt != 0){
				if(isUnique){
					totalString+= unique[CurrInt-1];
				}
				else{
					if(i == 0){
						totalString+= ones[CurrInt];
					}
					else{
						totalString+=ones[CurrInt];
					}
				}
			}
			
			totalString += " " + bundleDigitNames[bundleDigits.toArray().length - 1 - i];
			
			if(i != bundleDigits.toArray().length-1){
				totalString += ",";
			}
		}
		return totalString.trim();
	}

	public static void main(String[] args){
		System.out.println(printInt(42));
	}
}

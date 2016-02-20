import java.util.Scanner;

// Use double quotes for strings you doofus!!!
// Also, DON'T EVER COMPARE STRINGS USING "==" SIGNS!!!

public class FractionMutilator {
	
    FractionOperators Multiply = new FractionOperators("Multiply", new String[]{"Multiply","Multiplication","*","x"}, 2);
    FractionOperators Divide = new FractionOperators("Divide", new String[]{"Divide","Division","/"}, 2);
    FractionOperators Add = new FractionOperators("Add", new String[]{"Add","Addition","Sum","+"}, 2);
    FractionOperators Subtract = new FractionOperators("Subtract", new String[]{"Subtract","Subtraction","Minus","-"},2);
	//FractionOperators Exponential = new FractionOperators("Exponential", new String[]{"Exponential","Exponent"},1);
    
	public void FractionMutilatorGui(){
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		String frac1 = null;
		String frac2 = "/";
        String operator = null;
        
        System.out.println("Hello there mathematician! Give me an operator and some fractions, and I'll try my best to solve them!");
		
        System.out.println("What is the type of operation that is required from me?");	
	    operator= input.nextLine();
	    
		System.out.println("A fraction please:");
		frac1 = input.nextLine();
		
		System.out.println("What is the other fraction?");
		frac2 = input.nextLine();
		
		
		while(Multiply.inputExists(operator).equals("None")){
			System.out.println("I am sorry, but I didn't understand your operator. Could you retype it?");
			operator = input.nextLine();
    	}
		
        while(checkFraction(frac1) == false || checkFraction(frac2) == false){
        	System.out.println("Okay, I can't understand your fractions. Can you retype them for me?");
			
			System.out.println("Fraction 1:");
			frac1 = input.nextLine();
			
			System.out.println("Fraction 2:");
			frac2 = input.nextLine();
			}
        
        System.out.println("Everything looks good, now I am calculating!");
        
		String answer = new FractionMutilator().Compute(operator,frac1,frac2); //Does the actual computing
		System.out.println("Hey! I have found your answer: " + answer);
		
	}
    
	public Boolean checkFraction(String fraction){
		Boolean passed = true;
		
		if(fraction.split("/").length != 2){
			passed = false;
		}
		
		return passed;
	}
	
	public int CommonDenominator(int a, int b){
		int Denominator = 1;
		int c = 0;
		a = Math.abs(a);
		b = Math.abs(b);
		
		if(a>b){	// This block of statements helps determine the smallest number
			c = b;
		}
		else if (a<b){
			c = a;
		}
		else{
			return a; // If both numbers are equal, return the number!
		}
		
		int divisor = 1;
		
		while(divisor <= c){	// Finds the highest common denominator
			if (a%divisor == 0 && b%divisor == 0){
				Denominator = divisor;
			}
			divisor++;
		}	
		
		return Denominator;	
	}
	
	public int[] CommonMultiple(int a, int b){
		int[] Multiple = {1,1};
		
		Boolean foundMultiple = false;
		int overflow = 0;
		while(foundMultiple != true){
			if(a*Multiple[0]==b*Multiple[1]){
				break;
			}
			else if (a*Multiple[0]>b*Multiple[1]){
				Multiple[1]++;
			}
			else if (a*Multiple[0]<b*Multiple[1]){
				Multiple[0]++;
			}
			
			if(overflow > 1000){
				Multiple[0] = -1;
				Multiple[1] = -1;
				break;
			}
			overflow++;
		}
		return Multiple;
	}
	
	public String Compute(String operator, String rawFrac1, String rawFrac2){
		int[] frac1 = new int[]{Integer.parseInt(rawFrac1.split("/")[0]),Integer.parseInt(rawFrac1.split("/")[1])};
		int[] frac2 = new int[]{Integer.parseInt(rawFrac2.split("/")[0]),Integer.parseInt(rawFrac2.split("/")[1])};
		
		int Numer = 0;
		int Denom = 0;
		
		// Find an equation to use //
		if(Multiply.inputExists(operator).equals("Class")){
			Numer = frac1[0] * frac2[0];
			Denom = frac1[1] * frac2[1];
		}
	
		else if(Divide.inputExists(operator).equals("Class")){
			Numer = frac1[0] * frac2[1];
			Denom = frac1[1] * frac2[0];
		}
	
		else if(Add.inputExists(operator).equals("Class")){
			int[] Multiple = CommonMultiple(frac1[1],frac2[1]);
			
			Numer = frac1[0]*Multiple[0] + frac2[0]*Multiple[1];
			Denom = frac1[1]*Multiple[0];
		}
		
		else if(Subtract.inputExists(operator).equals("Class")){
			int[] Multiple = CommonMultiple(frac1[1],frac2[1]);
			
			Numer = frac1[0]*Multiple[0] - frac2[0]*Multiple[1];
			Denom = frac1[1]*Multiple[0];
		}
		
		else{
			System.out.println("FATAL: Couldn't find an operator");
		}
		
		System.out.println("Finished calculating, now simplifying");

		int AnswerNumer = Numer/CommonDenominator(Numer,Denom);
		int AnswerDenom = Denom/CommonDenominator(Numer,Denom);
		
		String answer = "";
		if(AnswerNumer%AnswerDenom == 0){
			answer = String.valueOf(AnswerNumer);
		}
		else{
			answer = String.valueOf(AnswerNumer) + "/" + String.valueOf(AnswerDenom);
		}
		
	return answer;
	}
}

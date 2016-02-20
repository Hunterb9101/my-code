

/*
 * This is a general math module that helps complete Project Euler problems
 * By: Hunter Boles
 */

public class MathModule {
	////////////////////////////////
	//		     IsType			  //
	////////////////////////////////
	public static Boolean Prime(int num){	
		for(int i = 2; i <= Math.sqrt(num); i++){
			if(num%i == 0){return false;}
		}
		return true;
	}
	
	public static Boolean isPerfectSquareInRange(int num, int range){
		int i = 0;
		while(i < range){
			if(i*i == num){
				return true;
			}
			i++;
		}
		return false;
	}
	
	public static Boolean isPerfectCubeInRange(int num, int range){
		int i = 0;
		while(i < range){
			if(i*i*i == num){
				return true;
			}
			i++;
		}
		return false;
	}
	
	public static Boolean Even(int num){
		if(num%2 == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static Boolean Odd(int num){
		if(num%2 == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static Boolean Multiple(int num, int multiple){
		if(multiple%num == 0){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static Boolean MultiplePlusConstant(int num, int multiple, int constant){
		if(multiple%(num) == 1 && multiple!=1){
			return true;
		}
		else{
			return false;	
		}
	}
	
	public static Boolean Palindrome(int num){		
		if(Integer.toString(num).equals(new StringBuilder(Integer.toString(num)).reverse().toString())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static int sumDivisors(int num){
		int currDivisor = 2;
		int currPower = 0;
		int currNum = num;
		int totalDivisor = 1;
		
		boolean passed = false;
		while(!passed){
			if(currNum%currDivisor == 0){
				currPower++;
				currNum = currNum/currDivisor;
			}
			else{
				// Get next prime factor
				do{
					currDivisor++;
				}while(!MathModule.Prime(currDivisor));
				
				totalDivisor = totalDivisor*(1+currPower);
				currPower = 0;
				
				if(currNum == 1){
					passed = true;
				}
			}
		}
		return totalDivisor;		
	}
///////////////////////////////////////
//			   Functions			 //
///////////////////////////////////////
	
	public static int sumProperDivisors(int a){
		int s = 0;
		for(int i = 1; i<a;i++){
			if(a%i == 0){
				s+=i;
			}
		}
		return s;
	}
}

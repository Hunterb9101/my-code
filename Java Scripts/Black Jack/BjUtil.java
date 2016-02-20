import java.util.Scanner;
public class BjUtil{
	public static Scanner scan = new Scanner(System.in);
	//Get user to type in an int
	public static int persistentLoop(String msg){
		int returnStuff = -1;
		boolean flag = false;

		System.out.println(msg);

		while(!flag){
			try{
				returnStuff = Integer.parseInt(scan.nextLine());
				flag = true;
			}catch(NumberFormatException e){
				System.out.println(msg);
			}
		}
		return returnStuff;
	}
}

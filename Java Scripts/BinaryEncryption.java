import java.io.UnsupportedEncodingException;

public class BinaryEncryption {
	public static void main(String[] args) throws UnsupportedEncodingException {
        String binStr = str2bin("Hunter is Awesome");
        String str = bin2str(binStr);
        
        System.out.println("Plain Encrypted Binary String: " + binStr);       
        System.out.println("Xor Encrypted Binary String: " + encryptBin(str,"1"));
        System.out.println("Xor Decrypted Binary String: " + decryptBin(encryptBin(str,"42"),"1"));
        System.out.println("Xor Decrypted Binary String: " + decryptBin(encryptBin(str,"1"),"1"));
    }
	
	public static String bin2str(String bin){
    	String str = "";
    	for (int i = 0; i < bin.length()/8; i++) {

            int a = Integer.parseInt(bin.substring(8*i,(i+1)*8),2);
            str += (char)(a);
        }
    	
    	return str;
    }
    
    public static String str2bin(String str) throws UnsupportedEncodingException{
    	String bin = "";
    	byte[] strChars = str.getBytes("UTF-8");
    	
    	for (byte b : strChars) {
    		for(int i = Integer.toBinaryString(b).length(); i<8; i++){
    			bin+="0";
    		}
           bin+=Integer.toBinaryString(b);
        }
    	return bin;
    }
    
    public static String encryptBin(String str, String key) throws UnsupportedEncodingException{
    	String encrypted_message = "";
    	int keyDigit = 0;
    	String currValue = "0";
    	for(int i=0; i<str2bin(str).length(); i++){
    		if(XOr(str2bin(key).substring(keyDigit,keyDigit+1),(str2bin(str).substring(i,i+1)))){
    			currValue = "1";
    		}
    		else{
    			currValue = "0";
    		}
    		encrypted_message += currValue;
    		
    		if(keyDigit == str2bin(key).length()-1){
    			keyDigit = 0;
    		}
    		else{
    			keyDigit++;
    		}
    	}
		return encrypted_message; 	
    }
    
    public static String decryptBin(String encrypted_message, String key) throws UnsupportedEncodingException{
    	String decrypted_message = "";
    	int keyDigit = 0;
    	String currValue = "0";
    	
    	for(int i=0; i<encrypted_message.length(); i++){
    		if(XOr(encrypted_message.substring(i,i+1),str2bin(key).substring(keyDigit,keyDigit+1))){
    			currValue = "1";
    		}
    		else{
    			currValue = "0";
    		}
    		
    		if(keyDigit == str2bin(key).length()-1){
    			keyDigit = 0;
    		}
    		else{
    			keyDigit++;
    		}
    		
    		decrypted_message += currValue;
    	}
    	return bin2str(decrypted_message);
    }
    
    public static Boolean XOr(String a, String b){
    	if(!a.equals(b)){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
}

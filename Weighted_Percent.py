import random

def Random(percentArray):
    Number = random.randint(0,100)
    prevsum = 0
    x = 0
    while (x < len(percentArray)):
        #print("prevsum: " + str(prevsum))

        if (x == 0):    
            #print(str(prevsum) + " <= " + str(Number) + " < " + str(percentArray[x] + prevsum))
            if(Number >= prevsum and Number < (int(percentArray[x]) + prevsum)): #Does First Comparison
                #print(str(Number) + " is between " + str(prevsum) + ' and ' + str(percentArray[x] + prevsum))
                return x
                break;

            if(Number >= int(percentArray[x]) and Number < int(percentArray[x + 1]) + int(percentArray[x])):
                #print(str(Number) + " is between " + str(percentArray[x]) + ' and ' + str(percentArray[x] + percentArray[x + 1]))
                return x + 1
                break;
            prevsum = int(percentArray[x]) + int(percentArray[x + 1]) + prevsum   
            
        else:
            #print(str(prevsum) + " <= " + str(Number) + " < " + str(percentArray[x + 1] + prevsum))
            if(Number >= prevsum and Number < (int(percentArray[x + 1]) + prevsum)):
                #print(str(Number) + " is between " + str(prevsum) + ' and ' + str(percentArray[x + 1] + prevsum))
                return x + 1
                break;
            
            if (x == len(percentArray) - 2):
                return x;
                #print(str(Number) + " is between " + str(prevsum) + ' and 100' + ", " + str(Number))
                
            prevsum = int(percentArray[x + 1]) + prevsum
        x = x + 1
        
print("    Weighted_Percent.py")

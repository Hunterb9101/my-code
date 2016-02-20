lastFibonacci = 1
thisFibonacci = 1
tempFibonacci = 0

termFibonacci = 2

passed = False
while(not passed):
    tempFibonacci = thisFibonacci
    thisFibonacci = lastFibonacci+thisFibonacci
    lastFibonacci = tempFibonacci
    termFibonacci+=1
    #print(termFibonacci,"--",thisFibonacci)
    if(len(str(thisFibonacci)) == 1000):
        passed = True;
        print(termFibonacci)
    

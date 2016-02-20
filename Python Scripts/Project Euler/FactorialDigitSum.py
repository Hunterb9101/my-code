factorial = 1;
for i in range (100):
    if not i==0:
        factorial = factorial*i
    print(factorial)
factorial = str(factorial)

bigSum = 0
for i in range(len(factorial)):
    bigSum+= int(factorial[i:i+1])
print(bigSum)

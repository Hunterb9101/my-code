def getBigNumber():
    bignum = str(2**1000)
    bigsum = 0;
    for i in range(len(bignum)):
        bigsum = bigsum + int(bignum[i:i+1])

    print(bigsum)
getBigNumber()

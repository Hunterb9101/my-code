import datetime

Added = []
    
def GetCurrTime(debug=False):
    currenttime = datetime.datetime.now()
    timeH   = currenttime.hour
    timeM   = currenttime.minute
    timeS   = currenttime.second
    
    timeDMY = datetime.date.today().strftime("%d/%m/%Y")
    timeArr = timeDMY.split('/')
    
    CurrTime = timeS + 60*timeM + 3600*timeH + 86400*int(timeArr[0])
    
    if debug: print(str(timeH) + ":" + str(timeM) + "." + str(timeS) + " " + str(timeDMY))
    return CurrTime

def FormatPredTime(predTime, allowNegative=False, returnType = 'mil'):
    maxTimes = [60,60,24,'x',12]
    maxTimeD = 0
    end = ""
    
    currenttime = datetime.datetime.now()
    predTime = TimeToStandard(predTime)

    timeS = currenttime.second + predTime[0]
    timeM = currenttime.minute + predTime[1]
    timeH = currenttime.hour + predTime[2]
    timeD = int(datetime.date.today().strftime("%d/%m/%Y").split('/')[0]) + predTime[3]
    timeMo = int(datetime.date.today().strftime("%d/%m/%Y").split('/')[1])
    timeY = int(datetime.date.today().strftime("%d/%m/%Y").split('/')[2])

    if timeS >= maxTimes[0]:
        timeS = timeS - 60*(int(timeS/60))
        timeM += 1   
    if timeM >= maxTimes[1]:
        timeM = timeM - 60*(int(timeM/60))
        timeH += 1      
    if timeH > maxTimes[2]:
        timeH = timeH - 24*(int(timeH/24))
        timeD += 1
    
    if timeMo == 2:
        if timeY%4 == 0: maxTimeD = 29
        else: maxTimeD = 28      
    elif timeMo <= 7:
        if timeMo%2 == 1: maxTimeD = 31
        else: maxTimeD = 30   
    elif timeMo > 7:
        if timeMo%2 == 0: maxTimeD = 31
        else: maxTimeD = 30
                
    if int(timeD) > maxTimeD:
        timeD = timeD - timeD*(int(timeD/maxTimeD))
        timeMo = timeMo + 1
        
    if timeMo > maxTimes[4]:
        timeMo= timeMo - timeMo*(int(timeMo/maxTimes[4]))
        timeY = timeY + 1
        
    if timeMo < 0:timeMo = timeMo * -1
    if timeD < 0: timeD = timeD* -1
    if timeH < 0: timeH = timeH* -1
    if timeM < 0: timeM = timeM* -1
    if timeS < 0: timeS = timeS* -1
        
    if returnType == '12hr':
        if timeH > 12:
            end = "PM"
            timeH = timeH - 12
        else: end = "AM"
    time = str(timeMo) + "/" + str(timeD) + "/" + str(timeY) + " " + str(timeH) + ":" + str(timeM) + "." + str(timeS) + " " + end
    return time
    
def CreateTime(s,m=0,h=0,d=0,addCurrTime=True):
    TimeS = s + 60*m + 3600*h + 86400*d
    if addCurrTime: TimeS = TimeS + GetCurrTime()
    return TimeS

def TimeToStandard(s):
    s = s - GetCurrTime()
    timeD = int(s/86400)
    timeH = int((s - timeD*86400)/3600)
    timeM = int((s - timeD*86400 - timeH*3600)/60)
    timeS = int((s - timeD*86400 - timeH*3600 - timeM*60))
    return [timeS,timeM,timeH,timeD]
    
def Add(obj,endtime):
    compiled = []
    compiled.append(obj)
    compiled.append(endtime)
    Added.append(compiled)
    print("Added: " + obj + " to the queue.")

def Update():
    Read = open("Queued.txt",'r')
    Queued = Read.read().split('\n')[:-1]
    Read.close()
    
    for x in range(len(Queued)): Queued[x] = Queued[x].split('=')

    CurrTime = GetCurrTime()
    for x in range(len(Queued)):
        if int(Queued[x][1]) <= CurrTime:
            print(Queued[x][0] + " has finished its production at " + str(FormatPredTime(int(Queued[x][1]),returnType="12hr")))
            Queued[x][0] = "Deleteme"
        else: print(Queued[x][0] + " will be done at: " + str(FormatPredTime(int(Queued[x][1]),returnType="12hr")))

    passed = False
    x = 0
    while not passed:
        if x == len(Queued)-1: passed = True
        
        try:    
            if "Deleteme" in Queued[x][0]: del Queued[x]
            else: x += 1
        except IndexError: passed = True

    Clear = open("Queued.txt",'w')
    Clear.write("")
    Clear.close()
    
    Append = open("Queued.txt",'a')
    for x in range(len(Queued)): Append.write(Queued[x][0] + '=' + Queued[x][1] + "\n")
    for x in range(len(Added)):  Append.write(str(Added[x][0]) + '=' + str(Added[x][1]) + "\n")
    del Added[0:len(Added)-1]
    Append.close()

#Add("The death star!",CreateTime(30))    
Update()

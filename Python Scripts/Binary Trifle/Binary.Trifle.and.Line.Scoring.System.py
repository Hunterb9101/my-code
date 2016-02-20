from tkinter import *
import random

rowArray = []
gPlayerPos = 3
gScore = 0

gWarningTimeout = 3
gbWarningEnabled = False
gWarningTurns = 0

FileContents = int(open("HiScoresLS.txt").read())

def LeaveMapWarning():
    global gbWarningEnabled
    global gWarningTurns
    WarningLabelTxt.set("Warning! No leaving map!")
    gbWarningEnabled = True
    gWarningTurns = 0
    
def Create_Row(row = 'rand'):
    numArray= ""
    
    if row != 'rand':
        try:
            numArray = row[0:7]
        except IndexError:
            print("Row given was too small at,",len(numArray),"characters.")
    else:
        x = 0
        while (x < 7):
            num = random.randint(0,9)
            if num > 5:
                num = 1
            else:
                num = 0
            numArray = numArray + str(num) 
            x = x + 1
    if numArray == '1111111':
        Create_Row
        print("Got rid of a row of 1s")
    else:
        rowArray.append(numArray)
        
    return numArray

def Move_Player(direction):
    global gPlayerPos
    global gbWarningEnabled
    global gScore
    
    gPlayerPos = rowArray[0].find('2')
    
    ## New Position using direction ##
    if direction == "Left":
        gPlayerPos = gPlayerPos - 1

        if gPlayerPos < 0:
            gPlayerPos = gPlayerPos + 1
            LeaveMapWarning()
        
    elif direction == "Right":
        gPlayerPos = rowArray[0].find('2') + 1 #New Position

        if gPlayerPos > 6:
            gPlayerPos = gPlayerPos - 1
            LeaveMapWarning()

    else:
        gPlayerPos = rowArray[0].find('2')
    
    ## Moves the Character down, and creates a new row ##
    if int(rowArray[1][gPlayerPos]) == 1:
        WarningLabelTxt.set("GAME OVER")
        ScoreLabelTxt.set("Score: " + str(gScore))
        LeftBtn['state'] = 'disabled'
        RightBtn['state'] = 'disabled'
        DownBtn['state'] = 'disabled'
        
        if FileContents  < gScore:
            FileWrite = open("HiScoresLS.txt",'w')
            HiScore = str(gScore)
            FileWrite.write(HiScore)
            FileWrite.close
            HighScoreLabel.configure(text="High Score: " + str(gScore))
    else:   
        del rowArray[0]
        rowArray[0] = gPlayerPos*'0' + '2' + (6 - gPlayerPos)*'0'
        Create_Row()
        row1txt.set(rowArray[0])
        row2txt.set(rowArray[1])
        row3txt.set(rowArray[2])
        row4txt.set(rowArray[3])
        row5txt.set(rowArray[4]) 
        gScore = gScore + 1
        ScoreLabelTxt.set("Score: " + str(gScore))

        if FileContents  < gScore:
            HighScoreLabel.configure(text="High Score: " + str(gScore))
            
    ## Warning Label Timeout##
    global gbWarningEnabled
    global gWarningTurns
    
    if gbWarningEnabled == True:
        gWarningTurns = gWarningTurns + 1

        if gWarningTimeout <= gWarningTurns:
            gbWarningEnabled = False
            gWarningTurns = 0
            WarningLabelTxt.set("")
            

def Create_Game():    
    Create_Row("00000000")
    Create_Row("00000000")
    Create_Row()
    Create_Row()
    Create_Row()
    rowArray[0] = rowArray[0][0:gPlayerPos] + "2" + rowArray[0][gPlayerPos + 1:8] #Place Player

def Restart_Game():
    global gScore
    global gPlayerPos
    
    del rowArray[0]
    del rowArray[0]
    del rowArray[0]
    del rowArray[0]
    del rowArray[0]
    gScore = 0
    gPlayerPos = 3
    Create_Game();

    row1txt.set(rowArray[0])
    row2txt.set(rowArray[1])
    row3txt.set(rowArray[2])
    row4txt.set(rowArray[3])
    row5txt.set(rowArray[4])
    WarningLabelTxt.set("")
    ScoreLabelTxt.set("Score:" + str(gScore))
    
    LeftBtn['state'] = 'active'
    RightBtn['state'] = 'active'
    DownBtn['state'] = 'active'
    
root = Tk()
Create_Game()

##### Menu #####

Restart = Button (root, text="Restart",font='timesnewroman 24',fg="#000088",command=(lambda:Restart_Game()))
Restart.place(x=0,y=0)

HighScoreLabel = Label(root, text=("High Score: " + str(FileContents)),font='timesnewroman 24',fg='green')
HighScoreLabel.place(x=200,y=0)

row1txt = StringVar()
row1 = Label(root, textvariable=row1txt, font='timesnewroman 36',fg="#000088").grid(row = 3,column = 0)
row1txt.set(rowArray[0])
            
row2txt = StringVar()
row2 = Label(root, textvariable=row2txt, font='timesnewroman 36',fg="#000044").grid(row = 4,column = 0)
row2txt.set(rowArray[1])

row3txt = StringVar()
row3 = Label(root, textvariable=row3txt, font='timesnewroman 36',fg="#000088").grid(row = 5,column = 0)
row3txt.set(rowArray[2])

row4txt = StringVar()
row4 = Label(root, textvariable=row4txt, font='timesnewroman 36',fg="#000044").grid(row = 6,column = 0)
row4txt.set(rowArray[3])

row5txt = StringVar()
row5 = Label(root, textvariable=row5txt, font='timesnewroman 36',fg="#000088").grid(row = 7,column = 0)
row5txt.set(rowArray[4])

Spacer = Label(root, text=" ",height = 4)
Spacer.grid(row = 1,column = 1)

Spacer2 = Label(root, text=" ")
Spacer2.grid(row = 2,column = 1)

LeftBtn = Button(root, text="Left", font='timesnewroman 36', command = (lambda: Move_Player('Left')))
LeftBtn.grid(row = 3,column = 2, rowspan = 2)

RightBtn = Button(root, text="Right", font='timesnewroman 36', command = (lambda: Move_Player('Right')))
RightBtn.grid(row = 3,column = 4, rowspan = 2)

DownBtn = Button(root, text="Down", font='timesnewroman 36', command = (lambda: Move_Player('Down')))
DownBtn.grid(row = 4,column = 3, rowspan = 2)

WarningLabelTxt = StringVar()
WarningLabel = Label(root, textvariable=WarningLabelTxt, font='timesnewroman 36',fg="Red").grid(row = 6, column = 2,columnspan=3)

ScoreLabelTxt = StringVar()
ScoreLabelTxt.set("Score:0")
ScoreLabel = Label(root, textvariable=ScoreLabelTxt, font='timesnewroman 36',fg="Green").grid(row = 7, column = 2,columnspan=3)
    
root.mainloop()
                        

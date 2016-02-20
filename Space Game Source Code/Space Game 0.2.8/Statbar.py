from tkinter import *
from Window import *

################################
#       Stat Bar Class!        #
################################

class Statbar:
    Info = []
    Active = []
    Attrs = ['Name','Align','Height','Width','Offset','Visibility','Background']
    
    def __init__(self,name,align="TL",height='100',width='100',offset=(0,0), visibility='True',bg="#000000"):
        Compile = []

        Compile.append(name)
        Compile.append(align)
        Compile.append(height)
        Compile.append(width)
        Compile.append(offset)
        Compile.append(visibility)
        Compile.append(bg)
        
        Statbar.Info.append(Compile)
        Statbar.Load(name)

    def GetSize(name = "", num = -1):
        if name != "": SbData=Statbar.Info[Statbar.getName(name)]
        elif num != -1: SbData=Statbar.Info[num]
        else: print("ERROR in Statbar.getSize(). Can't find Statbar size due to no identifying parameters"); return [0,0]

        Size = (SbData[Statbar.getAttrNum('Width')],SbData[Statbar.getAttrNum('Height')])
        GeneratedSize = []
        for coord in range(2):
            if Size[coord].find('winSize') != -1:
                Subtract = 0
                Equation = Size[coord].split('*')
                if '-' in Size[coord]:
                    Equation = Equation[1].split('-')[0]
                    SubtEquation = Size[coord].split('-')
                    Subtract = SubtEquation[1]
                try:
                    Equation = Equation[1]
                except IndexError:
                    Equation = Equation

                if coord == 0: GeneratedSize.append(int(Window.root.winfo_width() * float(Equation) - int(Subtract)))
                else:GeneratedSize.append(int(Window.root.winfo_height() * float(Equation) - int(Subtract)))
            else: GeneratedSize.append(Size[coord])
            
        return GeneratedSize
    
    def refresh_all(event = None):
        for cntr in range(len(Statbar.Active)):
            SbData = Statbar.Info
            Statbar.Active[cntr].config(width=Statbar.GetSize(num = cntr)[0],height=Statbar.GetSize(num = cntr)[1])
            print(SbData[cntr][Statbar.getAttrNum("Name")] + " Dimensions: " + str(Statbar.GetSize(num = cntr)[0]) + ", " + str(Statbar.GetSize(num = cntr)[1]))

    def Load(name = "", num = -1, insertAt = -1):
        if name != "":
            SbData=Statbar.Info[Statbar.getName(name)]
            sbSize = Statbar.GetSize(name=name)
            
        elif num != -1:
            SbData = Statbar.Info[num]
            sbSize = Statbar.GetSize(num=num)
            
        else: print("Error finding Statbar in Statbar.Load()")
        
        statbar = Frame(Window.root,bg=Statbar.Info[Statbar.getName(name)][Statbar.getAttrNum("Background")],width=Statbar.GetSize(name=name)[0],height=Statbar.GetSize(name=name)[1])
        if insertAt != -1: Statbar.Active.insert(insertAt, statbar)
        else: Statbar.Active.append(statbar)

        #Aligning the Statbar
        Align = SbData[Statbar.getAttrNum('Align')]
        Pos = (0,0)
        offset = SbData[Statbar.getAttrNum('Offset')]
                                    ##      Width                  Height               
        if Align   == "TL":  Pos = (offset[0], offset[1])
        elif Align == "TR":  Pos = (Window.Size[0] - sbWidth + offset[0], offset[1])
        elif Align == "TC":  Pos = (int(Window.Size[0]/2) + offset[0], offset[1])
                       
        elif Align == "CL":  Pos = (int(Window.Size[1]/2),0)
        elif Align == "C" :  Pos = (int(Window.Size[0]/2),int(Window.Size[1]/2))
        elif Align == "CR":  Pos = (int(Window.Size[0]/2),Window.Size[1])
                       
        elif Align == "BL":  Pos = (Window.Size[0]       ,0)
        elif Align == "BC":  Pos = (Window.Size[0],       int(Window.Size[1]/2))

        else: print("ERROR placing Statbar!")
        
        statbar.config(width=sbSize[0],height=sbSize[1])
        statbar.grid(row=0,column=0,sticky=NW)
        statbar.lift()
                
    def getName(name):
        for cntr in range(len(Statbar.Info)):
            if Statbar.Info[cntr][0] == name: return cntr      
            if cntr == (len(Statbar.Info)-1): return -1

    def getAttrNum(attr):
        for cntr in range(len(Statbar.Attrs)):
            if attr == Statbar.Attrs[cntr]: return cntr
 
            if cntr == (len(Statbar.Attrs) - 1): return -1
            

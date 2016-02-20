import Parser
import Utils
import random

from tkinter import *

XMLTileData  = Parser.XMLDataParser.TileData
XMLDataTags  = Parser.gDataTags

gThresholds = []
gBoardSize = Utils.GlobalByName('BoardSize')
gTileSize = Utils.GlobalByName('TileSize')
gPadding = Utils.GlobalByName('Padding')
gImagePath = Utils.GlobalByName('ImagePath')

################################
#           Tile Data          #
################################
            
class Tile:
    Images_Main = []
    Images_Overlay = []
    ObjCount = []
    Data=[]

    for cntr in range(len(XMLTileData)): ObjCount.append(0) #Sets up the object counter
                   
    def __init__(self,tileType=None,refCoords = None):
        Compile = []
            
        if tileType == None: Compile.append(0)
        else: Compile.append(tileType)

        if refCoords == None: Compile.append(-1)
        else: Compile.append(refCoords)

        Compile.append(XMLTileData[0][Tile.getTag('CreateName')])
        Tile.Data.append(Compile) #Grabs my reference coordinates, tiletype, and name
        
    def set(row,column,tileType, refCoords = None, changeNameTo = None):
        Tile.Data[Tile.ArrayNum(row,column)][0] = tileType
        
        if refCoords == None: Tile.Data[Tile.ArrayNum(row,column)][1] = -1
        else: Tile.Data[Tile.ArrayNum(row,column)][1] = refCoords

        if (changeNameTo != None): Tile.Data[Tile.ArrayNum(row,column)][2] = changeNameTo
        
    def setArea(TLRow,TLColumn,tileType, iSize):
        TileArrayNum = Tile.ArrayNum(TLRow,TLColumn)

        for vertSize in range(int(iSize)):
            for horizSize in range(int(iSize)):
                if vertSize == 0 and horizSize == 0: pass #I don't want to overwrite the top-left tile
                else: Tile.set(TLRow + horizSize, TLColumn + vertSize, tileType, TileArrayNum)
            
    def get(row,column,returnval = None):
        try:
            TileArrayNumRef = Tile.ArrayNum(row,column)
            TileArrayNum = TileArrayNumRef
            tileType = Tile.Data[TileArrayNum][0]
            
            if(Tile.Data[TileArrayNum][1] != -1):
                TileArrayNum = Tile.Data[TileArrayNum][1]

            if returnval == None:    
                print("Tile #: " + str(TileArrayNumRef) + ' (' + str(row) + ', ' + str(column) + ')')
                print("    Tile Type: " + str(tileType))
                print("    Regular Name: " + XMLTileData[tileType][Tile.getTag("Name")])
                print("    Special Name: " + str(Tile.Data[TileArrayNum][2]) + " (" + (XMLTileData[tileType][Tile.getTag('CreateName')] + " Name)"))
                print("    Reference Coords: " + str(Tile.Data[TileArrayNumRef][1]))

            if returnval == 'tileType': return tileType
            elif returnval != None and returnval != 'tileType': print("Error in Tile.get(). Invalid returnval: " + str(returnval))
            
        except IndexError:
            print("The data couldn't be retrieved because it was out of range.")
            
    def getTag(name):
        for cntr in range(len(XMLDataTags)):
            if name == XMLDataTags[cntr]: return cntr
            if cntr == len(XMLDataTags) - 1:
                return -1
                print("Can't find",name,"in the parsable data tags list")
                
    def getTagData(tileType,tag): return XMLTileData[tileType][Tile.getTag(tag)]
        
    def getByAttr(category,data,returntype = 'list'):
        filtered = []
        for cntr in range(len(XMLTileData)):
            if(XMLTileData[cntr][Tile.getTag(category)] == data):
                if returntype == 'list': filtered.append(cntr)
                elif returntype == 'integer': return cntr
                else: return -1
        return filtered
    
    def isContrasting(colorA,colorB):
        ContrastThresh = 165
        Passed = True
        
        try:
            Contrast = ((int(colorA[1:3]) - int(colorB[1:3]))**2 + (int(colorA[3:5]) - int(colorB[3:5]))**2 + (int(colorA[5:7]) - int(colorB[5:7]))**2)**.5
        
            if Contrast < ContrastThresh: Passed = False
            return Passed
        
        except ValueError: return Passed #Only one value found, so it passes
        
    def getGraphics(tileType):

        # Gets the Main Image #
        ImgDecider = random.randint(0,len(Tile.Images_Main[tileType]) - 1)
        image_main = Tile.Images_Main[tileType][ImgDecider] 
        if len(image_main) == 1: image_main = Tile.Images_Main[tileType] # If it finds only a character, it realizes that it is only one image
        
        # Gets the Overlay Image #
        ImgDecider = random.randint(0,len(Tile.Images_Overlay[tileType]) - 1)
        image_overlay = Tile.Images_Overlay[tileType][ImgDecider]
        if len(image_overlay) == 1: image_overlay = Tile.Images_Overlay[tileType] # Does the same thing with the overlay image    

        colorTypes = ['Bg','Fg']
        tileColors = []      

        passed = False
        
        # This for-loop creates the Background (Bg) and Foreground (Fg) colors, in that order and waits until both colors don't have similar colors#
        while not passed:
            tileColors = []
            for cntr in range(len(colorTypes)):
                Color = XMLTileData[tileType][Tile.getTag(colorTypes[cntr]+'Color')]
                
                if "[" in str(XMLTileData[tileType][Tile.getTag(colorTypes[cntr]+'Color')]) and "]" in str(XMLTileData[tileType][Tile.getTag(colorTypes[cntr]+'Color')]):
                    
                    Color = XMLTileData[tileType][Tile.getTag(colorTypes[cntr]+'Color')][1:-1].split(',') # Gets rid of brackets
                    for cntr in range(len(Color)): Color[cntr] = int(Color[cntr][2:],16) # Changes the gradient information from string into hex form (0xFF)
                    Color = Utils.new_gradient(Color)[random.randint(0,len(Utils.new_gradient(Color))-1)] #Pick a random color from the gradient defined in Tiles.xml

                tileColors.append(Color)

            if Tile.isContrasting(tileColors[0],tileColors[1]): passed = True
            
        return [tileColors[0], tileColors[1], image_main, image_overlay]
    
    def getGraphicsCoords(row,column,size):     
        Coord =(
               (row - 1)             * gTileSize + gPadding[3], (column - 1)             * gTileSize  + gPadding[0],
               (row - 1)             * gTileSize + gPadding[3], (column - 1 + int(size)) * gTileSize  + gPadding[0], 
               (row - 1 + int(size)) * gTileSize + gPadding[3], (column - 1 + int(size)) * gTileSize  + gPadding[0],
               (row - 1 + int(size)) * gTileSize + gPadding[3], (column - 1)             * gTileSize  + gPadding[0])
        
        return Coord
    
    def isBordering(row,column,iTargetType,iRange):
        Ranges = [(0,0),
                  (-1,1),(0,1),(1,1),(-1,0),(1,0),(-1,-1),(0,-1),(1,-1)]
        Borders = []
        if iRange == 1:
            for cntr in range(len(Ranges[1])):
                if row + 1 < gBoardSize[0] or column + 1 < gBoardSize[0]:
                    try: Borders.append(Tile.get(row + Ranges[cntr][0],column + Ranges[cntr][1],returnval = 'tileType'))
                    except IndexError: pass
        try:
            if(Borders.index(iTargetType) != -1): return True
        except ValueError: return False
        
    def ArrayNum(row,column): TileArrayNum = (column - 1) * gBoardSize[0] + row - 1; return TileArrayNum #Tile Array Number Reference function for formula

    def FinishParsing():
        for cntr in range(len(XMLTileData)): gThresholds.append(XMLTileData[cntr][2]) #Gets Weighted percentage rates for spawning

        for cntr in range(len(XMLTileData)):# Gets Images for tiles and puts it into Tile.Images
            Art = Tile.getTag('MainArt')
            if (XMLTileData[cntr][Tile.getTag('MainArt')].find(',') == -1): #If there is only one image
                Tile.Images_Main.append(gImagePath + XMLTileData[cntr][Art])
            else: #Multiple images
                XMLTileData[cntr][Tile.getTag('MainArt')] = XMLTileData[cntr][Art].split(' , ')
                Compile = [] 
                for cntr2 in range(len(XMLTileData[cntr][Art])):
                    Compile.append(gImagePath + XMLTileData[cntr][Art][cntr2])
                Tile.Images_Main.append(Compile)
        print("Parsed Main Images!")
        
        for cntr in range(len(XMLTileData)):# Gets Images for tiles and puts it into Tile.Images
            Art = Tile.getTag('OverlayArt')
            if (XMLTileData[cntr][Tile.getTag('OverlayArt')].find(',') == -1): #If there is only one image
                Tile.Images_Overlay.append(gImagePath + XMLTileData[cntr][Art])
            else: #Multiple images
                XMLTileData[cntr][Tile.getTag('OverlayArt')] = XMLTileData[cntr][Art].split(' , ')
                Compile = []
                for cntr2 in range(len(XMLTileData[cntr][Art])):
                    Compile.append(gImagePath + XMLTileData[cntr][Art][cntr2])
                Tile.Images_Overlay.append(Compile)

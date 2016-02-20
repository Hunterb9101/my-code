from tkinter import *
from Tile import *

import Name_Generator
import Parser
import Weighted_Percent
import Utils
import random

gGlobals = Parser.gGlobals
gVersion = Utils.GlobalByName("Version")
gIconPath = Utils.GlobalByName("IconPath")
gImagePath = Utils.GlobalByName("ImagePath")
gBoardSize = Utils.GlobalByName("BoardSize")
gTileSize = Utils.GlobalByName("TileSize")
gFullscreenState = False
gPadding = Utils.GlobalByName("Padding") #Top,Right,Down,Left
gSunThresholds = [12,50,38]

################################
#          Game Board          #
################################
class Window:
    root = Tk() # Main window
    root.title("Space Game " + gVersion)
    root.wm_iconbitmap(gIconPath)

    root.grid_rowconfigure(0, weight=1)   #Grid Sizing behaviors
    root.grid_columnconfigure(0, weight=1)#Grid Sizing behaviors 
    
    Size = [root.winfo_screenwidth(),root.winfo_screenheight()]

    main = Canvas(root, width=root.winfo_screenwidth() * .98, height= root.winfo_screenheight() * .98,bg="#000000") #-#Canvas
    main.grid(row=0, column=0, sticky='nswe')                                                                       #-#Canvas

    All_Image_Main = []
    All_Image_Overlay = []
    
    def Onclick(event,debug=True):
        formulax = event.x - gPadding[3] + Window.hScroll.get()[0] * (gBoardSize[0]*gTileSize + gPadding[3] + gPadding[1])# Gets EXACT pixel on X plane
        formulay = event.y - gPadding[0] + Window.vScroll.get()[0] * (gBoardSize[1]*gTileSize + gPadding[0] + gPadding[2])# Gets EXACT pixel on Y plane  
        coord = (int((formulax)/gTileSize) + 1, int((formulay)/gTileSize) + 1) #gets grid number
        Tile.get(coord[0],coord[1])

        if debug:
            print("")
            print("    X:" + str(formulax) + ", MAX: " + str(gBoardSize[0]*gTileSize))
            print("    Y:" + str(formulay) + ", MAX: " + str(gBoardSize[1]*gTileSize))
            print("")

    def toggle_fullscreen(event=None):
        global gFullscreenState
        gFullscreenState = not gFullscreenState
        Window.root.attributes("-fullscreen", gFullscreenState)
        if not gFullscreenState:
            l, h = Window.root.winfo_screenwidth(), Window.root.winfo_screenheight()
            Window.root.geometry("%dx%d+0+0" % (l, h))
            
    def end_fullscreen(event=None):
        global gFullscreenState
        gFullscreenState = False
        Window.root.attributes("-fullscreen", False)

    def on_mousewheel(event=None):
        Window.main.yview_scroll(int(-1*(event.delta/120)), "units")

    def on_arrowkey(event=None, x=0, y=0):
        Window.main.xview_scroll(int(x), "units")
        Window.main.yview_scroll(-1*int(y), "units")
        
    def refresh_winVals():
        Window.screenSize[0] = Window.root.winfo_screenwidth()
        Window.screenSize[1] = Window.root.winfo_screenheight()

    def Pre_Generate():
        for cntr in range(gBoardSize[0] * gBoardSize[1]):
            Tile(0) #Creates a "blank slate" for generation
            Window.All_Image_Main.append(Tile.getGraphics(0)[2])
            Window.All_Image_Overlay.append(Tile.getGraphics(0)[3])

    def Post_Generate():
        row = 1 
        column = 1
        while column <= gBoardSize[1]:
            if row > gBoardSize[0]:
                if(column == gBoardSize[1]): break
                row = 1
                column = column + 1

            TileType = Tile.Data[Tile.ArrayNum(row,column)][0]
            TileRef  = Tile.Data[Tile.ArrayNum(row,column)][1]
            
            if(TileRef == -1): # If this is the first tile of an object (has no reference)
                Graphics = Tile.getGraphics(TileType)
                CurrImage = PhotoImage(file=Graphics[2])
                OverlayImage = PhotoImage(file=Graphics[3])
                
                if Tile.getTagData(TileType,"MultiplyColor") == "True": Utils.colorMultiplier(CurrImage,Graphics[0],Graphics[1])
                
                Window.main.create_polygon(Tile.getGraphicsCoords(row, column, int(Tile.getTagData(TileType,'Size'))), fill = Graphics[0]) #This is for the primitive art types that don't have a full color
        
                Window.main.create_image(Tile.getGraphicsCoords(row, column, int(Tile.getTagData(TileType,'Size')))[0:2], image= CurrImage, anchor=NW)
                Window.main.create_image(Tile.getGraphicsCoords(row, column, int(Tile.getTagData(TileType,'Size')))[0:2], image= OverlayImage, anchor=NW)
                
                Window.All_Image_Main[Tile.ArrayNum(row,column)] = CurrImage #Getting a reference back to the image
                Window.All_Image_Overlay[Tile.ArrayNum(row,column)] = OverlayImage #Another reference
                
                Tile.setArea(row,column,TileType, Tile.getTagData(TileType,'Size'))


                BRPaddingCoords=[(gBoardSize[0]*gTileSize+gPadding[3]            ,gBoardSize[1]*gTileSize            +gPadding[0]), 
                             (gBoardSize[0]*gTileSize+gPadding[3]+gPadding[1],gBoardSize[1]*gTileSize            +gPadding[0]),
                             (gBoardSize[0]*gTileSize+gPadding[3]+gPadding[1],gBoardSize[1]*gTileSize+gPadding[2]+gPadding[0]),
                             (gBoardSize[0]*gTileSize+gPadding[3]            ,gBoardSize[1]*gTileSize+gPadding[2]+gPadding[0])]
            
            Window.main.create_polygon([(0,0),(1,0),(1,1),(0,1)]) #TL Padding is added in generation of images, so no numbers needed
            Window.main.create_polygon(BRPaddingCoords)
            
            Window.main["scrollregion"]=Window.main.bbox(ALL) # Makes scrollbar work
            row = row + 1
            
                
    def Generate(): # Creates Board for gameplay
        Window.Pre_Generate()
        
        # Generate Sun
        print("Generating Sun")
        CenterAppx = (int(gBoardSize[0]/2),int(gBoardSize[1]/2))
        Stars = Tile.getByAttr('Type','Star')
        TileType = Weighted_Percent.Random(gSunThresholds) + Stars[0] - 1 #Stars[0] is the first star found
        print("Tile Type of Sun: " + str(TileType))
        Tile.set(CenterAppx[0],CenterAppx[1],TileType, changeNameTo = Name_Generator.getName(iNameType=Tile.getTagData(TileType,'CreateName')))
        print("Generated Sun!")
        
        # Generate Planets
        print("Generating Planets")
        for cntr in range(int(Tile.getTagData(Tile.getByAttr('Type','Planet','integer'),'SpawnMax'))):
            Coords = (random.randint(0,gBoardSize[0]),random.randint(0,gBoardSize[1]))
            
            passed = False
            while not passed:
                Planet = Tile.getByAttr('Name','Planet','integer')
                if Tile.isBordering(Coords[0],Coords[1],Tile.getByAttr('Type','Planet','integer'),1):
                    Coords = (random.randint(0,gBoardSize[0]),random.randint(0,gBoardSize[1]))
                    passed = False
                if Tile.get(Coords[0],Coords[1],returnval='tileType') != 0:
                    Coords = (random.randint(0,gBoardSize[0]),random.randint(0,gBoardSize[1]))
                    passed = False
                passed = True
            Tile.set(Coords[0],Coords[1], Planet, changeNameTo = Name_Generator.getName(iNameType = Tile.getTagData(Planet,'CreateName')))
        print("Generated Planets!")
        
        # Generate Asteroids
        print("Generating Asteroid Fields")
        for cntr in range(int(Tile.getTagData(Tile.getByAttr('Name','Asteroid Field','integer'),'SpawnMax')) + random.randint(-2,2)):
            passed = False
            while not passed:
                Coords = (random.randint(0,gBoardSize[0]),random.randint(0,gBoardSize[1]))
                if Tile.get(Coords[0],Coords[1],returnval='tileType') != 0:
                    passed = False
                else:
                    passed = True
            Tile.set(Coords[0],Coords[1],Tile.getByAttr('Name','Asteroid Field','integer'))
        print("Generated Asteroid Fields!")

        Window.Post_Generate()
    
    hScroll = Scrollbar(root, orient=HORIZONTAL, command=main.xview); hScroll.grid(row=1, column=0, sticky='we') #Creates a horizontal Scrollbar                                     
    vScroll = Scrollbar(root, orient=VERTICAL  , command=main.yview); vScroll.grid(row=0, column=1, sticky='ns') #Creates a vertical Scrollbar as well
    
    main.configure(xscrollcommand=hScroll.set, yscrollcommand=vScroll.set) # Attaches Scrollbars to Canvas
    
    #################
    #     Keys      #
    #################
    
    main.bind("<Button-1>",Onclick) #Binds Left-Mouse to Screen, giving Event x and y coordinates and handler
    root.bind("<F11>", toggle_fullscreen)
    root.bind("<Escape>", end_fullscreen)
    root.bind_all("<MouseWheel>", on_mousewheel)
    root.bind_all("<Left>", lambda event: Window.on_arrowkey(x=-1))
    root.bind_all("<Right>",lambda event: Window.on_arrowkey(x=1))
    root.bind_all("<Down>", lambda event: Window.on_arrowkey(y=-1))
    root.bind_all("<Up>",   lambda event: Window.on_arrowkey(y=1))

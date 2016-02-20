import Utils

from Tile import *
from Window import *
from Statbar import *

from tkinter import *

gFullscreenStart = Utils.GlobalByName("FullscreenOnStart")

gSeed = random.randint(0,999999999)
random.seed(gSeed)

print("Map Seed: " + str(gSeed))
print("")
Utils.Stamp()
print("")
print("Initialization complete!")
print("")
    
##########################
#          Main          #
##########################
Window()
Tile.FinishParsing()
        
Statbar('Left',align="TL",width='winSize*.2',height="winSize*.65",offset=(0,Window.Size[1]*.1 - 1),bg='#008800')
Statbar('Top',align="TL",width='winSize*1-19',height='winSize*.1',bg='#880000')
Window.Generate()

if gFullscreenStart: Window.toggle_fullscreen()

Window.main.bind("<Configure>",Statbar.refresh_all)
Window.root.mainloop()

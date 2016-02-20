import tkinter
from tkinter import *
import random
import datetime

root = tkinter.Tk()
root.title('Merry Christmas!')
w = tkinter.Canvas(root, width=400, height=300, background="#000000",bd=0)
w.create_text(200,150,text="Merry Christmas",font="Arial 20",fill="#ff0000")
w.create_text(200,170,text="Ho Ho Ho",font="Arial 12",fill="#00ff00")
w.pack()


# Bare Ground #
groundcoords = [(0,200),(402,200),(402,302),(0,302)]
ground = w.create_polygon(groundcoords, fill="darkolivegreen",)
once = True

# House #
HouseImg = PhotoImage(file="HouseRed.gif")
House = w.create_image(400,175, image=HouseImg)

# Santa #
SantaImg = PhotoImage(file="Santa.gif")
Santa = w.create_image(-50,25, image=SantaImg)

# snow flakes #
flake = [];
moves = []
for i in range(100): # Amount of Snowflakes
 flake.append(w.create_text(random.randrange(400),random.randrange(300),text="*",fill="#ffffff",font="Times " + str(random.randint(8,30))))
 moves.append([0.04 + random.random()/10,0.7 + random.random()])

# Snow on ground #
gSnowTime = 3
polycoords= [(0,300),(402,300),(402,302),(0,302)]
snow = w.create_polygon(polycoords, fill="white",)
once = True

try:
 while 1:
  currenttime = datetime.datetime.now() # Checks Amount of Seconds

  # Moves Snowflakes #
  for i in range(len(flake)):
   p = w.coords(flake[i])
   p[0]+=moves[i][0]
   p[1]+=moves[i][1]
   w.coords(flake[i],p[0],p[1])
   if(p[1]>310):
    w.coords(flake[i],random.randrange(400),-10)

   # Moves Snowbank #
   if (((currenttime.microsecond % gSnowTime) == 0) and (once == True)):
    c = w.coords(snow)
    s = w.coords(Santa)
    # Moves Santa #
    if(c[1] < 200):
     if(s[0] <= 250):
      s[0] = s[0] + 3
     elif((s[0] > 130) and (s[1] < 160)):
      s[1] = s[1] + 3
      s[0] = s[0] + 3
     w.coords(Santa,s[0],s[1])
    elif (c[1] > 200):
     c[1] = c[1] - 3
     c[3] = c[3] - 3
     w.coords(snow,c[0],c[1],c[2],c[3],c[4],c[5],c[6],c[7])
    once = False
   elif ((currenttime.microsecond % gSnowTime) == 1):
    once = True
   root.update_idletasks() # redraw
   root.update() # process events
except:
 pass

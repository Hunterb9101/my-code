from tkinter import *
import math

water_colors = [0xff550055,0xffAA00FF,0xff0000FF,0xff000066] #Gradient Colors, proceeded by an 0xff
land_colors  = [0xff555555,0xff880000,0xffFF0000,0xffFF8800,0xFFFF00,0xff88FF00,0xff00FF00,0xff008800,0xff000000, 0xff000000] #Gradient Colors, proceeded by an 0xff
# Three-color gradient 0xff + Hex Color
#Water Colors: 0xff550055,0xffAA00FF,0xff0000FF,0xff000066
#Planet Colors: 0xff555555,0xff880000,0xffFF0000,0xffFF8800,0xFFFF00,0xff88FF00,0xff00FF00,0xff008800,0xff555555
#Gas Giant Color Scheme 1: 0xff0000FF, 0xff8888FF
#Gas Giant Color Scheme 2: 0xffDDDD00,0xffAA0000

root = Tk()
root.title("Visual Developer's Tools V0.1")
overlay = PhotoImage(file='Overlay.png')
badOverlay = PhotoImage(file='BadOverlay.png')
images = []


def ceil(a, b):
    if (b == 0):
        raise Exception("Division By Zero Error!!") # throw an division by zero error
    if int(a/b) != a/b:
        return int(a/b) + 1
    return int(a/b)

StartGridSize = [20,15]
gGridSize = [ceil(StartGridSize[0],len(water_colors)) * (len(water_colors)+1)-1,ceil(StartGridSize[1],len(land_colors)) * (len(land_colors)+1)-1]

c = Canvas(root, width=gGridSize[0]*70, height=gGridSize[1]*70,bg="blue")
c.grid(padx=0,pady=0, column=0, row=0)

def colorContrast(a,b): #Takes in Colors in form #rrggbb,
    Passed = True
    rawColors = [a,b]
    RGB = []
    
    for color in range(2):
        prevSum = 1
        Compile = []
        for hue in range(3):
            Compile.append(int(rawColors[color][prevSum:prevSum+2],16))
            prevSum+=2
        RGB.append(Compile)

    Contrast = ((RGB[0][0] - RGB[1][0])**2 + (RGB[0][1] - RGB[1][1])**2 + (RGB[0][2] - RGB[1][2])**2)**.5
    ContrastThresh = 185
    
    if Contrast < ContrastThresh: Passed = False
    
    return Passed

def generateColorGradient(colors,plotline):
    # color conversion utils
    f2c = lambda f: int(f * 255.0) & 0xff
    c2f = lambda c: float(c) / 255.0
    alpha = lambda c: (c >> 24) & 0xff
    red = lambda c: (c >> 16) & 0xff
    green = lambda c: (c >> 8) & 0xff
    blue = lambda c: c & 0xff
    pack = lambda a, r, g, b: (f2c(a) << 24) | (f2c(r) << 16) | (f2c(g) << 8) | f2c(b)

    gradient = []
    gradient_length = 0
    
    if plotline == "R": gradient_length = ceil(gGridSize[0],len(colors)) * (len(colors)+1) #Gets the lowest multiple that is higher than the row
    else: gradient_length = ceil(gGridSize[1],len(colors)) * (len(colors)+1)
    
    colors_per_step = gradient_length / len(colors)
    num_colors = int(colors_per_step) * len(colors) # get the 'corrected' length of the gradient...

    # color conversion utils
    f2c = lambda f: int(f * 255.0) & 0xff
    c2f = lambda c: float(c) / 255.0
    alpha = lambda c: (c >> 24) & 0xff
    red = lambda c: (c >> 16) & 0xff
    green = lambda c: (c >> 8) & 0xff
    blue = lambda c: c & 0xff
    pack = lambda a, r, g, b: (f2c(a) << 24) | (f2c(r) << 16) | (f2c(g) << 8) | f2c(b)
    
    for i, color in enumerate(colors):
        # start color...
        r1 = c2f(red(color))
        g1 = c2f(green(color))
        b1 = c2f(blue(color))

        # end color...
        color2 = colors[(i + 1) % len(colors)]
        r2 = c2f(red(color2))
        g2 = c2f(green(color2))
        b2 = c2f(blue(color2))

        # generate a gradient of one step from color to color:
        delta = 1.0 / colors_per_step
        for j in range(int(colors_per_step)):
            t = j * delta
            a = 1.0
            r = (1.0 - t) * r1 + t * r2
            g = (1.0 - t) * g1 + t * g2
            b = (1.0 - t) * b1 + t * b2
            gradient.append(pack(a, r, g, b))
    return gradient

def format_gradient(colors):
    colorfill = []
    for c in colors:
        colorfill.append('#' + '0x{0:x},'.format(c)[4:])
    return colorfill

def new_gradient(colors,plotline):
    return format_gradient(generateColorGradient(colors,plotline))

def colorMultiplier(image,multiplier,transparent="#000000"):
    #Statement Block allows for #rrggbb or (r,g,b) statements #
    try:
        multiplier = (int(multiplier[1:3],16),int(multiplier[3:5],16),int(multiplier[5:7],16))
    except TypeError:
        multiplier = multiplier
        
    wrange = range(0, image.width())
    hrange = range(0, image.height())
    get = image.get
    put = image.put

    def convert_pixel(raw,transparent="#000000"):
        colors = [raw[0],raw[1],raw[2]]
        compiled = ()
        for cntr in range(len(colors)):
            if colors[cntr]*multiplier[cntr] > 255: colors[cntr]=255
            elif colors[cntr]*multiplier[cntr] != 0: colors[cntr]=colors[cntr]*multiplier[cntr]
            compiled+=(colors[cntr],)
        if sum(colors) < 1: return(transparent)
        else: return ('#%02x%02x%02x' % tuple(int(component) for component in compiled))

    for y in hrange:
        put('{' + ' '.join(convert_pixel(get(x, y),transparent=transparent) for x in wrange) + '}', (0, y))


def colorify():
    row = 0
    column = 0
    planetcntr= 0
    
    water = new_gradient(water_colors,"R") #R stands for Row (Change along X axis)
    land  = new_gradient(land_colors,"C")  #C stands for Column (Change along Y axis)

    print("Compiling Data...")
    while column <= gGridSize[1]:
        if row > gGridSize[0]:
            if column == gGridSize[1]: break
            row = 0
            column = column + 1
            print('\r' + str(int((column/gGridSize[1])*100)) + "% Complete")
        newImage= PhotoImage(file="Planet_Main.png")    
        colorMultiplier(newImage,water[row],transparent=land[column])
        c.create_image(row*70,column*70, image = newImage, anchor = NW)

        if colorContrast(water[row],land[column]): c.create_image(row*70,column*70, image = overlay, anchor = NW)
        else: c.create_image(row*70,column*70, image = badOverlay, anchor = NW)
        
        images.append(newImage)
        row = row + 1
        planetcntr = planetcntr + 1
        c["scrollregion"]=c.bbox(ALL) # Makes scrollbar work

root.grid_rowconfigure(0, weight=1)   #Grid Sizing behaviors
root.grid_columnconfigure(0, weight=1)#Grid Sizing behaviors

hScroll = Scrollbar(root, orient=HORIZONTAL, command=c.xview)#--------#
hScroll.grid(row=1, column=0, sticky='we')                                    #----- Scrollbars
vScroll = Scrollbar(root, orient=VERTICAL, command=c.yview)           #----- Scrollbars
vScroll.grid(row=0, column=1, sticky='ns')                           #--------#
c.configure(xscrollcommand=hScroll.set, yscrollcommand=vScroll.set) # Attaches Scrollbars to Canvas
    
colorify()
root.mainloop()

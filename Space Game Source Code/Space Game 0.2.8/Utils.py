import datetime
import Parser

print("    Utils.py")

def Stamp():
    currenttime = datetime.datetime.now()
    timeH   = currenttime.hour
    timeM   = currenttime.minute
    timeS   = currenttime.second
    timeDMY = datetime.date.today().strftime("%d/%m/%Y")
    timeArr = timeDMY.split('/')
    timeEnc = timeS + 60*timeM + 3600*timeH + 3600*24*int(timeArr[1])
    print(str(timeH) + ":" + str(timeM) + "." + str(timeS) + " " + str(timeDMY))
    print("Time in seconds: " + str(timeEnc))

def GlobalByName(globalName):
    Globals = Parser.gGlobals
    for cntr in range(len(Globals)):
        if globalName in Globals[cntr][0]:
            return Globals[cntr][1]
    else:
        print("ERROR: Can't find global: " + globalName)

##############################
#       Color Utils          #
##############################

#0xff + Hex Color
base_water_colors = [0xff550055,0xffAA00FF,0xff0000FF,0xff000066]
base_planet_colors = [0xff555555,0xff880000,0xffFF0000,0xffFF8800,0xFFFF00,0xff88FF00,0xff00FF00,0xff008800,0xff555555]
gas_giant_colors_1 = [0xff0000FF, 0xff8888FF]
gas_giant_colors_2 = [0xffDDDD00,0xffAA0000]
# Rainbow [0xffFF0000,0xffFF8800,0xffFFFF00,0xff88FF00,0xff00FF00,0xff00FF88,0xff00FFFF,0xff0088FF,0xff0000FF,0xff8800FF,0xffFF00FF]

# generate the gradient and returns the list of colors as packed ints.
def generateColorGradient(colors):
    gradient = []

    gradient_length = 2000
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

def new_gradient(colors):
    return format_gradient(generateColorGradient(colors))

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

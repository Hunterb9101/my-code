# the Mote project (http://mote.sourceforge.net/)

import random
import string

gNames = []
gDigraphs = [
        "aa",'ab','ac','ad','ae','af','ag','ah','ai','aj','ak','al','am','an','ao','ap','aqu','ar','as','at','au','av','aw','ax','ay','az',
        'ba','be','bi','bl','bo','bu','ca','ce','ch','ck','cr','cy','f','vi','ion',
        'da','de','di','chr','bor', 'th','gr','ium','om', 'hu','kry','pt'
        "la",  "ve",  "ta",  "re",  "or",  "za",  "us",
        "te",  "ce",  "at",  "a",   "e",   "o",   "le",  "fa",
        "he",  "na",  "ar",  "to",  "oi",  "ne",  "no",  "ba",
        "bo",  "ha",  "ve",  "va",  "ax",  "is",  "or",  "in",
        "mo",  "on",  "cra", "ud",  "sa",  "tu",  "ju",  "pi",
        "mi",  "gu",  "it",  "ob",  "os",  "ut",  "ne",  "as",
        "en",  "ky",  "tha", "um",  "ka",  "qt",  "zi",  "ou",
        "ga",  "dro", "dre", "pha", "phi", "sha", "she", "fo",
        "cre", "tri", "ro",  "sta", "stu", "de",  "gi",  "pe",
        "the", "thi", "thy", "lo",  "ol",  "clu", "cla", "le",
        "di",  "so",  "ti",  "es",  "ed",  "po",  "ni",
        "ex",  "un",  "pho", "ci",  "ge",  "se",  "co"]

gGreekAlphabet = ['Alpha','Beta','Gamma','Delta','Epsilon','Zeta','Eta','Iota','Kappa','Lambda','Mu','Nu','Xi','Omicron','Pi','Rho','Sigma',
                 'Tau','Upsilon','Phi','Chi','Psi','Omega']

gGodNames = ['Andromeda','Antila','Apus','Aquarius','Aquila','Ara','Aries','Auriga','Bootes','Caelum','Camelopardalis','Cancer','Canes Venatici', 'Canis Major',
            'Canis Minor','Capricornus','Carina','Cassiopeia','Centaurus','Cepheus','Cetus','Chamaeleon','Circinus','Columbia','Coma Berenices', 'Corona Australis',
            'Corona Borealis','Corvus','Crater','Cygnus','Delphinus','Dorado','Draco','Equuleus','Eridanus','Fornax','Gemini','Grus','Hercules', 'Horlogium', 'Hydra',
            'Hydrus','Indus','Lacerta','Leo','Leo Minor','Lepus','Libra','Lupus','Lynx','Lyra','Mensa','Microscopium','Monoceros','Musca','Norma','Octans',
            'Ophiuchus','Orion','Pavo','Pegasus','Perseus','Phoenix','Pictor','Pisces','Piscis Austrinus','Puppis','Pyxis','Reticulum','Sagitta','Sagittarus',
            'Scorpius','Sculptor','Scutum','Serpens','Taurus','Telescopium','Triangulum','Tucana','Ursa Major','Ursa Minor','Vela','Virgo','Volans','Vulpecula',
            'Centurion','Hunter']

gGreekAlphabet = ['Alpha','Beta','Gamma','Delta','Epsilon','Zeta','Eta','Iota','Kappa','Lambda','Mu','Nu','Xi','Omicron','Pi','Rho','Sigma',
                 'Tau','Upsilon','Phi','Chi','Psi','Omega']
        
gNumDGS = len(gDigraphs) - 1

def checkName(iName):
    nVowels = 0
    nSamechar = 0
    sLastchar = "*"
    
    for i in range(len(iName)):
        char = iName[i]
        
        # Abort if more then two vowels are found together
        if char in ('a', 'e', 'i', 'o', 'u'):  nVowels = nVowels + 1
        else: nVowels = 0;
        
        if nVowels == 3: return 0
        
        # Abort if three same letters are found together    
        if char == sLastchar: nSamechar = nSamechar + 1
        else: nSamechar = 0
        
        if nSamechar == 2: return 0
            
        sLastchar = char   
    return 1
   
def isDuplicated(iName):
    # Check if iName was already used
    if iName in gNames: return 1
    else: return 0
    
def getName(iNameType = 'Planet'):
    iLength = 2 + int(random.random() * 3) # Create names with 2-3 syllables
    if int(random.random() * 15) == 1: iLength = iLength + 1 # But sometimes use 4 syllables
        
    bNameok = False
    while not bNameok: # Repeat until the generated name is ok
        iName = ""
        for i in range(iLength):
            bOk = False
            while not bOk:
                bOk = True
                
                # Get a random syllable and make sure that no
                # three-letter syllable is used as the last one
                nDigraph = gDigraphs[int(random.random() * gNumDGS)]
                if (i == iLength - 1) and (len(nDigraph) > 2): ok = 0   
            iName = iName + nDigraph # Add the syllable to the name
        
        bNameok = checkName(iName) and not isDuplicated(iName) # Check if iName is pronounceable and unique

    #Capitalize iName and find what type it is supposed to be             
    iName = iName.capitalize()
    
    if(iNameType == 'Star'):
        nNameFormat = random.randint(0,10)
        
        sSunAlph = gGreekAlphabet[random.randint(0,len(gGreekAlphabet) - 1)]
        nGreekAlphIdx = random.randint(0,len(gGreekAlphabet) - 1)
        nGodNameIdx = random.randint(0,len(gGodNames) - 1)
        Num = random.randint(42,999)
        
        if(nNameFormat == 0):   iName = gGodNames[nGodNameIdx] + '-' + gGreekAlphabet[nGreekAlphIdx]
        elif(nNameFormat == 1): iName = gGodNames[nGodNameIdx][0:3] + '-' + str(Num)
        elif(nNameFormat == 2): iName = gGreekAlphabet[nGreekAlphIdx] + '-' + str(Num)
        elif(nNameFormat == 3): iName = gGreekAlphabet[nGreekAlphIdx] + '-' + gGodNames[nGodNameIdx]
        elif nNameFormat > 3:   iName = iName + '-' + sSunAlph
            
    elif(iNameType != "Planet" and iNameType != "Star"): iName = ''

    gNames.append(iName) # Add the iName to the cache so names can be checked for uniqueness
    return iName

print("    NameGenerator.py")

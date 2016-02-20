import xml.sax
gDataTags = ['Name','Type','CreateName','SpawnPercent','SpawnMax','Size','MultiplyColor','FgColor','BgColor','MainArt','OverlayArt']

class XMLDataParser( xml.sax.ContentHandler ):
   TileData = []
   def __init__(self):
      for x in range(len(gDataTags)):
         exec('self.' + gDataTags[x] + "=''")
      self.ObjCount = -1
      self.Compile = []

   # Call when an element starts
   def startElement(self, tag, attributes):
      self.CurrentData = tag
      if tag == "Object":
         self.Compile = []
         self.ObjCount += 1       
         #title = attributes["title"]

   # Call when an elements ends
   def endElement(self, tag):
      for x in range(len(gDataTags)):
         if self.CurrentData == gDataTags[x]:
            exec('self.Compile.append(self.' + gDataTags[x] + ')')
            if x == len(gDataTags) - 1:
               XMLDataParser.TileData.append(self.Compile)
      self.CurrentData = ""

   # Call when a character is read
   def characters(self, content):
      for x in range(len(gDataTags)):
         if self.CurrentData == gDataTags[x]:
            exec('self.' + gDataTags[x] + '= content')

def ParseGlobals():
   Globals=open("Globals.txt")
   Text=Globals.read().split("\n")
   for cntr in range(len(Text)):
      Text[cntr]=Text[cntr].split("=")
      for cntr2 in range(len(Text[cntr])):
         if "*" in Text[cntr][cntr2]:
            Text[cntr][cntr2] = Text[cntr][cntr2].split('*') 
         
      if "i" in Text[cntr][0][0]:
         try:
            Text[cntr][1] = int(Text[cntr][1])
         except TypeError:
            for cntr3 in range(len(Text[cntr][1])):
               Text[cntr][1][cntr3] = int(Text[cntr][1][cntr3])
               
      elif "b" in Text[cntr][0][0]:
         if Text[cntr][1] == "False" or not Text[cntr][1]:
            Text[cntr][1] = False
         else:
            Text[cntr][1] = True
      Text[cntr][0] = Text[cntr][0][1:]
   return Text
   
parser = xml.sax.make_parser() # create an XMLReader
parser.setFeature(xml.sax.handler.feature_namespaces, 0) # turn off namepsaces
parser.setContentHandler(XMLDataParser()) # override the default ContextHandler

parser.parse("Tiles.xml")
gGlobals = ParseGlobals()
#print(XMLDataParser.TileData)

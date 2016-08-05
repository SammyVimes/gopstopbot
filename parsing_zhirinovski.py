# coding:utf-8
"""
    A parsing script is only a parsing script
"""
from os import listdir
from os.path import isfile, join
import BeautifulSoup as bs
import codecs
import re

path = "zhirik/"

for f in listdir(path):
    if isfile(join(path, f)):
        print f
        

        with codecs.open(path + f, encoding="utf-8") as html_file:
            soup = bs.BeautifulSoup(html_file.read())
            tag = soup.find('div', id='textmain')
            if tag is not None:
                cleanf = open(join("zhirik_clean/", f), "w+")
                text = tag.text.replace("\n", " ")
                text = re.sub("&[A-Za-z]+;", "",   re.sub("&nbsp;", " ",text))
                cleanf.write(text.encode("utf-8"))   
                cleanf.close()         
            print '------------------------------------------------------'
        

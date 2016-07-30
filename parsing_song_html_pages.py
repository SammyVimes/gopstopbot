# coding:utf-8
"""

    A parsing script is only a parsing script

    Prior actions

    curl 'http://***.ru/sitemap.xml' | grep -o 'http://***.ru/song.p[^<]*' > songs.url.txt

    wc -l songs.url.txt
    5207 songs.url.txt

    mkdir html
    cd html/

    wget -i ../songs.url.txt

    watch -n 1  'ls | wc -l'

"""
from os import listdir
from os.path import isfile, join
import BeautifulSoup as bs
import codecs
import re

path = "html/"

for f in listdir(path):
    if isfile(join(path, f)):
        print f
        cleanf = open(join("clean/", f), "w+")

        with codecs.open(path + f, encoding="cp1251") as html_file:
            soup = bs.BeautifulSoup(html_file.read())
            descs = soup.findAll(attrs={"name": "description"})[0]['content'].split("-")

            singer = descs[0]
            song = descs[1]
            cleanf.write(singer.encode("utf-8") + "\n")
            cleanf.write(song.encode("utf-8") + "\n")

            text = str(soup.find('div', id='song')).replace("\n", " ")

            text = re.sub("&amp;", "",
                          re.sub("&nbsp;", " ",
                                 re.sub(".*</h2>", "",
                                        re.sub("<!-- Put this.*", "", text))))
            text = text.replace("<br />", "\n").strip()

            for line in text.split("\n"):
                line = line.strip()

                if line and not ("рипев" in line.lower() and len(text) < 8):
                    print line
                    cleanf.write(line + "\n")
            print
            print '------------------------------------------------------'
        cleanf.close()

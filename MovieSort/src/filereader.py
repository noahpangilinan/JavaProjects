from movieclass import *
from ratingclass import *
from timeit import default_timer as timer
'''
Goes through the datasets and converts them into dictionaries
Movielist - converts media to a large dictionary
Ratinglist - converts the ratings to a large dictionary
'''

def movielist(filename: str) -> dict:
    '''
    Takes a dataset and turns it into a dictionary with the ID as the key
    Filters out Adult Movies and \\N
    :param filename: str
    :return: dict
    '''
    moviedict = {}
    print("reading " + filename + " into dict...")
    start = timer()
    with open(filename, encoding="utf-8") as f:
        lines = f.readlines()
        for i in range(1, len(lines)):
            lines[i] = lines[i].strip()
            lines[i] = lines[i].split("\t")
            if not lines[i][4] == "1":
                while "\\N" in lines[i]:
                    y = lines[i].index("\\N")
                    lines[i][y] = 0

                try:
                    lines[i][8] = lines[i][8].split(",")
                except:
                    lines[i][8] = ["None"]

                moviedict[lines[i][0]] = Movie(lines[i][0], lines[i][1], lines[i][2], lines[i][3], (lines[i][4]),
                                                   (lines[i][5]), (lines[i][6]), (lines[i][7]), (lines[i][8]))
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)

    return(moviedict)


def ratinglist(filename: str) -> dict:
    '''
    Takes a dataset and turns it into a dictionary with the ID as the key
    :param filename: str
    :return: dict
    '''
    ratingdict = {}
    print("reading " + filename + " into dict...")
    start = timer()
    with open(filename, encoding="utf-8") as f:
        lines = f.readlines()
        for i in range(1, len(lines)):
            lines[i] = lines[i].strip()
            lines[i] = lines[i].split("\t")
            ratingdict[lines[i][0]] = Rating(lines[i][0], lines[i][1], lines[i][2])
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)
    return ratingdict

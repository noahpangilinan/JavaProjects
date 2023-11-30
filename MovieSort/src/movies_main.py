import sys
from queries import *
from filereader import *
'''
Computer Science for AP Students
Author: Noah Pangilinan
nap2906@rit.edu

This program takes a very large dataset and uses dictionaries, sorting, and searching to find requested 
movies. 
You can sort by runtime, year, votes, ID, or by title using the different queries

First the program runs through the large dataset and implements it into a dictionary, then reads an
input file to decide which queries to implement 
'''


def main()-> None:
    '''
    The main program loop
    calls functions to implement a dictionary, and impose queries
    :return: None
    '''
    # how to process input queries.
    # this loop automatically terminates when EOF is reached from the file,
    # or the user enters ^D to terminate standard input
    x = sys.argsv
    if len(x) <= 1:
        x.append("title.basics.tsv")
        x.append("title.ratings.tsv")
    else:
        x.append("small.basics.tsv")
        x.append("small.ratings.tsv")
    movies = movielist(x[1])
    print("")
    ratings = ratinglist(x[2])

    print("\nTotal movies: " + str(len(movies)))
    print("Total ratings: " + str(len(ratings)))

    for line in sys.stdin:
        line = line.strip()  # remove trailing newline
        print("\n\nprocessing: " + line)
        line = line.split()
        if line[0] == "LOOKUP":
            lookup(line[1], movies, ratings)
        elif line[0] == "CONTAINS":
            contains(line[1], ' '.join(line[2:]), movies)
        elif line[0] == "YEAR_AND_GENRE":
            year_and_genre(line[1], line[2], line[3], movies)
        elif line[0] == "RUNTIME":
            runtime(line[1], int(line[2]), int(line[3]), movies)
        elif line[0] == "MOST_VOTES":
            most_votes(line[1], int(line[2]), movies, ratings)
        elif line[0] == "TOP":
            top(line[1], int(line[2]), int(line[3]), int(line[4]), movies, ratings)


if __name__ == '__main__':
    main()

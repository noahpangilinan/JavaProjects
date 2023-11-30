from dataclasses import dataclass
'''
A dataclass to store movies and other media based in the unique movie ID
Takes the ID, an type of title, primary and original title,
if it is an adult movie or not, start and end year, runtime, and genres
'''

@dataclass
class Movie:
    tconst: str
    titleType: str
    primaryTitle: str
    originalTitle: str
    isAdult: bool
    startYear: int
    endYear: int
    runtimeMinutes: int
    genres: list


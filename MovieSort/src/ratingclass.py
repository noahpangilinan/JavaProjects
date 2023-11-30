from dataclasses import dataclass
'''
A dataclass to store ratings based in the unique movie ID
Takes the ID, an average rating, and a number of votes

'''

@dataclass
class Rating:
    tconst: str
    averagerating: float
    votes: int




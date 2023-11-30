from timeit import default_timer as timer
'''
Where all the different queries are written

Lookup - finds movies based on ID
Contains - finds movies of a certain type and title
Year_and_Genre - finds movies within a range of years and genres
Runtime - finds movies within a type and genre
Most_Votes - finds the top movies of a type of media
Top - finds the top movies for a range of years
'''

def _split(data: list[int]) -> tuple[list[int], list[int]]:
    """
    Split the data into halves and return the two halves
    :param data: The list to split in half
    :return: Two lists, cut in half
    """
    return data[:len(data) // 2], data[len(data) // 2:]


def _merge(left: list[int], right: list[int]) -> list[int]:
    """
    Merges two sorted list, left and right, into a combined sorted result
    :param left: A sorted list
    :param right: A sorted list
    :return: One combined sorted list
    """
    # the sorted left + right will be stored in result
    result = []
    left_index = right_index = 0

    # loop through until either the left or right list is exhausted
    while left_index < len(left) and right_index < len(right):
        if int(left[left_index].votes) <= int(right[right_index].votes):
            result.append(left[left_index])
            left_index += 1
        else:
            result.append(right[right_index])
            right_index += 1

    # take the un-exhausted list and extend the remainder onto the result
    if left_index < len(left):
        result.extend(left[left_index:])
    elif right_index < len(right):
        result.extend(right[right_index:])

    return result


def merge_sort(data: list[int]) -> list[int]:
    """
    Performs a merge sort and returns a newly sorted list
    :param data: The data to be sorted (a list)
    :return: A sorted list
    """
    # an empty list, or list of 1 element is already sorted
    if len(data) < 2:
        return data
    else:
        # split the data into left and right halves
        left, right = _split(data)
        # return the merged recursive merge_sort of the halves
        return _merge(merge_sort(left), merge_sort(right))


def lookup(id: str, movies: dict, ratings: dict) -> None:
    """
    Uses a dictionary key to find a movie and rating based on its unique id
    :param id: str
    :param movies: dict
    :param ratings: dict
    :return: None
    """
    start = timer()
    if id in movies and id in ratings:
        print("\tMOVIE: Identifier: " + movies[id].tconst + ", Title: " + movies[id].primaryTitle + ", Type: " +
              movies[id].titleType + ", Year: " + str(movies[id].startYear)
              + ", Runtime: " + str(movies[id].runtimeMinutes) + ", Genres: " + (str(' '.join(movies[id].genres))))
        print(
            "\tRATING: Identifier: " + ratings[id].tconst + ", Rating: " + str(ratings[id].averagerating) + ", Votes: "
            + str(ratings[id].votes))
    else:
        print("\tMovie not found!\n\tRating not found!")
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)


def contains(type: str, words: str, movies: dict)-> None:
    '''
    Finds all movies of a certain type and have certain words in the title
    :param type: str
    :param words: str
    :param movies: dict
    :return: None
    '''
    containing = []
    start = timer()

    for key in movies:
        if movies[key].titleType == type and words in movies[key].primaryTitle:
            containing.append(key)
    if len(containing) > 0:
        for i in containing:
            print("\tIdentifier: " + movies[i].tconst + ", Title: " + movies[i].primaryTitle + ", Type: " +
                  movies[i].titleType + ", Year: " + str(movies[i].startYear)
                  + ", Runtime: " + str(movies[i].runtimeMinutes) + ", Genres: " + (str(', '.join(movies[i].genres))))

    else:
        print("\tNo match found!")
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)


def year_and_genre(type: str, year: int, genre: str, movies: dict)-> None:
    '''
    Finds all movies from a certain genre, type, and year
    :param type: str
    :param year: int
    :param genre: str
    :param movies: dict
    :return: None
    '''
    containing = []
    start = timer()

    for key in movies:
        if movies[key].startYear == year and movies[key].titleType == type and genre in movies[key].genres:
            containing.append(key)
    if len(containing) > 0:
        for i in containing:
            print("\tIdentifier: " + movies[i].tconst + ", Title: " + movies[i].primaryTitle + ", Type: " +
                  movies[i].titleType + ", Year: " + str(movies[i].startYear)
                  + ", Runtime: " + str(movies[i].runtimeMinutes) + ", Genres: " + (str(', '.join(movies[i].genres))))
    else:
        print("\tNo match found!")
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)


def runtime(type: str, min: int, max: int, movies: dict)-> None:
    '''
    Finds all movies of a certain type and runtime
    :param type: str
    :param min: int
    :param max: int
    :param movies: dict
    :return: None
    '''
    containing = []
    start = timer()

    for key in movies:
        if movies[key].titleType == type and max >= int(movies[key].runtimeMinutes) >= min:
            containing.append(key)
    for i in containing:
        print("\tIdentifier: " + movies[i].tconst + ", Title: " + movies[i].primaryTitle + ", Type: " +
              movies[i].titleType + ", Year: " + str(movies[i].startYear)
              + ", Runtime: " + str(movies[i].runtimeMinutes) + ", Genres: " + (str(', '.join(movies[i].genres))))
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)
    return containing


def most_votes(titletype: str, num: int, movies: dict, ratings: dict, toprint=True)-> list:
    '''
    finds the top x movies of a certain type
    :param titletype: str
    :param num: int
    :param movies: dict
    :param ratings: dict
    :param toprint: boolean
    :return: list(sometimes)
    '''
    containing = {}
    start = timer()

    for key in movies:
        if key in ratings and movies[key].titleType == titletype:
            containing[key] = ratings[key]
    y = merge_sort(list(containing.values()))

    y = list(reversed(y[(-num):]))
    if toprint:
        if len(y) > 0:
            for i in y:
                print(
                    "\t" + str(y.index(i)+1) +". VOTES: " + i.votes + ", MOVIE: Identifier: " + movies[i.tconst].tconst + ", Title: " + movies[i.tconst].primaryTitle + ", Type: " +
                    movies[i.tconst].titleType + ", Year: " + str(movies[i.tconst].startYear)
                    + ", Runtime: " + str(movies[i.tconst].runtimeMinutes) + ", Genres: " + (
                        str(', '.join(movies[i.tconst].genres))))
        else:
            print("\tNo match found!")
        elapsed = timer() - start
        print('elapsed time (s):', elapsed)
    else:
        return y


def top(type: str, num: int, startyear: int, endyear: int, movies: dict, ratings: dict)-> None:
    '''
    finds the top x movies per year in a range of years
    :param type: str
    :param num: int
    :param startyear: int
    :param endyear: int
    :param movies: dict
    :param ratings: dict
    :return: None
    '''

    start = timer()
    for g in range(endyear-startyear+1):
        containing = {}
        print("\tYEAR: " + str(startyear+g))
        for key in movies:
            if ((startyear+g) == int(movies[key].startYear) and movies[key].titleType == type and key in ratings and \
                    int(ratings[key].votes) >= 1000):
                containing[key] = movies[key]
                #print(containing)
        y = most_votes(type, num, containing, ratings, toprint=False)
        if len(y) > 0:
            for i in y:
                print(
                    "\t\t" + str(y.index(i) + 1) + ". RATING: "+ i.averagerating +", VOTES: " + i.votes + ", MOVIE: Identifier: " + movies[
                        i.tconst].tconst + ", Title: " + movies[i.tconst].primaryTitle + ", Type: " +
                    movies[i.tconst].titleType + ", Year: " + str(movies[i.tconst].startYear)
                    + ", Runtime: " + str(movies[i.tconst].runtimeMinutes) + ", Genres: " + (
                        str(', '.join(movies[i.tconst].genres))))
        else:
            print("\tNo match found!")
    elapsed = timer() - start
    print('elapsed time (s):', elapsed)

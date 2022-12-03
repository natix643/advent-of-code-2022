testInput = '''
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_03.txt/)
lines = realInput.readLines().findAll()

alphabet = ('a'..'z') + ('A'..'Z')

def priority(letter) {
    return alphabet.indexOf(letter) + 1
}

lines.collate(3).collect { triplet ->
    def item = triplet.collect { it.toSet() }
            .inject { accumulator, next -> accumulator.intersect(next) }
            .first()
    priority(item)
}.sum()

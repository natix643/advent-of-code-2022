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

lines.collect { line ->
    def middle = line.size() / 2
    def left = line[0..<middle]
    def right = line[middle..<line.size()]
    def item = left.toSet().intersect(right.toSet()).first()
    priority(item)
}.sum()

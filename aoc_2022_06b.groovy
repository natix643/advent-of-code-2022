testInput = 'mjqjpqmgbljsphdztnvjfqwrcgsmlb'
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_06.txt/).readLines()[0]
input = realInput.toList()

windowSize = 14

input.collate(windowSize, 1, false).findIndexOf {
    it.toSet().size() == windowSize
} + windowSize

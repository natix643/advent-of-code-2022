testInput = 'mjqjpqmgbljsphdztnvjfqwrcgsmlb'
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_06.txt/).readLines()[0]
input = realInput.toList()

def entry = input.collate(14, 1, false).indexed().find { index, quartet ->
    quartet.toSet().size() == 14
}
entry.key + 14

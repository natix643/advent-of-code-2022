testInput = """1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
"""
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_01.txt/).readLines().drop(1).join('\n')
input = realInput

blocks = input.split('\n\n')
subSums = blocks.collect { block ->
    def lines = block.readLines().findAll()
    def numbers = lines.collect { it.toInteger() }
    numbers.sum()
}

max = subSums.max()
println '# A #'
println max
println()

top3 = subSums.toSorted(Comparator.reverseOrder()).take(3).sum()
println '# B #'
println top3

testInput = '''
2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_04.txt/)
lines = realInput.readLines().findAll()

lines.collect { line ->
    def (left, right) = line.split(',').collect { assignment ->
        def (from, to) = assignment.split('-')*.toInteger()
        from..to
    }
    left.intersect(right)
}.count { it }

testInput = '''    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_05.txt/)
lines = realInput.readLines()

layoutLines = lines.takeWhile { !it.empty }.dropRight(1)
moveLines = lines.dropWhile { !it.empty }.drop(1)

List<Deque<String>> buildLayout(List<String> lines) {
    List<List<String>> parsedLayout = lines.collect { line ->
        List<String> crates = []
        for (def i = 1; i < line.length(); i += 4) {
            def crate = line[i]
            crates << (crate.isAllWhitespace() ? null : crate)
        }
        crates
    }

    parsedLayout.transpose().collect { column ->
        new ArrayDeque(column.findAll())
    }
}

def layout = buildLayout(layoutLines)

moveLines.each { line ->
    def parts = line.split()
    def count = parts[1] as int
    def from = (parts[3] as int) - 1
    def to = (parts[5] as int) - 1

    def crates = (1..count).collect {
        layout[from].pop()
    }
    crates.reverse().each {
        layout[to].push(it)
    }
}

layout*.peek().join()

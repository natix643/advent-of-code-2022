testInput = '''
30373
25512
65332
33549
35390
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_08.txt/)
lines = realInput.readLines().findAll()

class Color {
    static RED_BACKGROUND = "\u001b[38;5;231m\u001b[41;1m"
    static RESET = "\u001B[0m"

    static gray(int height) {
        "\u001b[38;5;${250 - 2 * height}m"
    }
}

class Tree {
    int height
    Integer score
    boolean winner
}

class Forest {
    List<List<Tree>> trees

    int getHeight() {
        trees.size()
    }

    int getWidth() {
        trees[0].size()
    }

    Tree getAt(int x, int y) {
        trees[y][x]
    }

    List<Tree> list() {
        trees.collectMany { it }
    }

    String toString() {
        trees.collect {
            it.collect {
                def color = it.winner ? Color.RED_BACKGROUND : Color.gray(it.height)
                "${color}${it.height}${Color.RESET}"
            }.join()
        }.join('\n')
    }
}

int countScore(Forest forest, int x, int y) {
    def tree = forest[x, y]

    def left = (0..<x).collect {
        forest[it, y]
    }.reverse()
    def right = (x + 1..<forest.width).collect {
        forest[it, y]
    }
    def up = (0..<y).collect {
        forest[x, it]
    }.reverse()
    def down = (y + 1..<forest.height).collect {
        forest[x, it]
    }

    def scores = [left, right, up, down].collect { trees ->
        def smallerCount = trees.takeWhile { it.height < tree.height }.size()
        return smallerCount == trees.size() ? smallerCount : smallerCount + 1
    }
    return scores.inject { a, b -> a * b }
}

forest = new Forest(
        trees: lines.collect {
            it.toList().collect {
                new Tree(height: it.toInteger())
            }
        }
)

for (x in 0..<forest.width) {
    for (y in 0..<forest.height) {
        forest[x, y].score = countScore(forest, x, y)
    }
}

best = forest.list().max { it.score }
best.winner = true

println forest
println ''
println best.score

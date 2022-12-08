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
    static GRAY = "\u001b[38;5;248m"
    static RESET = "\u001B[0m"
}

class Tree {
    int height
    boolean visible
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
                it.visible ? it.height : "${Color.GRAY}${it.height}${Color.RESET}"
            }.join()
        }.join('\n')
    }
}

boolean isVisible(Forest forest, int x, int y) {
    def tree = forest[x, y]

    def left = (0..<x).collect {
        forest[it, y]
    }
    def right = (x + 1..<forest.width).collect {
        forest[it, y]
    }
    def up = (0..<y).collect {
        forest[x, it]
    }
    def down = (y + 1..<forest.height).collect {
        forest[x, it]
    }

    return [left, right, up, down].any { trees ->
        trees.every { it.height < tree.height }
    }
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
        forest[x, y].visible = isVisible(forest, x, y)
    }
}

result = forest.list().count { it.visible }

println forest
println ''
println result

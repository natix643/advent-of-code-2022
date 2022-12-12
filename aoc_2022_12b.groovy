import groovy.transform.EqualsAndHashCode

testInput = '''
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_12.txt/)
lines = realInput.readLines().findAll()

@EqualsAndHashCode(includes = ['x', 'y'])
class Square {
    char letter
    Integer x
    Integer y
    Square previous

    int getHeight() {
        switch (letter) {
            case 'S':
                return 'a' as char
            case 'E':
                return 'z' as char
            default:
                return letter
        }
    }

    Square copy() {
        new Square(letter: letter, x: x, y: y)
    }

    String toString() {
        "($x,$y,$letter)"
    }
}

class Matrix {
    List<List<Square>> squares

    static Matrix parse(List<String> lines) {
        return new Matrix(
                squares: (0..<lines.size()).collect { y ->
                    def line = lines[y]
                    (0..<line.length()).collect { x ->
                        new Square(letter: line[x] as char, x: x, y: y)
                    }
                }
        )
    }

    Square getAt(int x, int y) {
        squares.getAt(y)?.getAt(x)
    }

    List<Square> getNeighbors(Square square) {
        def coordinates = square.with {
            [
                    [x - 1, y],
                    [x + 1, y],
                    [x, y - 1],
                    [x, y + 1]
            ]
        }
        return coordinates
                .findAll { x, y -> x >= 0 && y >= 0 }
                .collect { x, y -> this[x, y] }
                .findAll { it != null && it.height - square.height <= 1 }
    }

    void print() {
        squares.each { line ->
            line.each { square ->
                if (square.previous) {
                    print(square.letter)
                } else {
                    print('.')
                }
            }
            println()
        }
    }

    Matrix copy() {
        new Matrix(
                squares: squares.collect { line ->
                    line.collect { square ->
                        square.copy()
                    }
                }
        )
    }
}

Square findTarget(Matrix matrix, Square start) {
    def visited = [start].toSet()
    def queue = new ArrayDeque<Square>([start])

    while (!queue.empty) {
        def square = queue.remove()

        if (square.letter == ('E' as char)) {
            return square
        }

        matrix.getNeighbors(square).findAll {
            !(it in visited)
        }.each {
            it.previous = square
            queue << it
            visited << it
        }
    }
    return null
}

List<Square> buildPath(Square target) {
    def current = target
    def path = [] as List<Square>

    while (current != null) {
        path << current
        current = current.previous
    }
    return path.reverse()
}
matrix = Matrix.parse(lines)

starts = matrix.squares
        .collectMany { it }
        .findAll { it.height == ('a' as char) }
paths = starts.collect { start ->
    def target = findTarget(matrix.copy(), start)
    buildPath(target)
}.findAll()

shortest = paths.min { it.size() }
shortest.size() - 1

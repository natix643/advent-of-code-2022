import groovy.transform.Immutable
import groovy.transform.TupleConstructor

def testInput = '''
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
'''
def realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_14.txt/)
def lines = realInput.readLines().findAll()

class Config {
    static draw = false
    static drawPeriod = 10
}

@Immutable
class Point {
    final int x
    final int y

    Point down() {
        return new Point(x, y + 1)
    }

    Point downLeft() {
        return new Point(x - 1, y + 1)
    }

    Point downRight() {
        return new Point(x + 1, y + 1)
    }
}

@TupleConstructor
enum Tile {
    ROCK('#'),
    SAND('o'),
    SAND_SOURCE('+')

    final String symbol
}

class Cave {
    static final int VERTICAL_MARGIN = 2
    static final int HORIZONTAL_MARGIN = 10

    static final Point SAND_SOURCE_POSITION = new Point(500, 0)

    final int minX
    final int maxX
    final int minY
    final int maxY

    final Map<Point, Tile> tiles = [
            (SAND_SOURCE_POSITION): Tile.SAND_SOURCE
    ]

    Cave(List<String> lines) {
        def rocks = lines.collectMany { parseRocks(it) }

        this.minX = rocks.min { it.x }.x
        this.maxX = rocks.max { it.x }.x
        this.minY = rocks.min { it.y }.y
        this.maxY = rocks.max { it.y }.y

        rocks.each {
            this.tiles[it] = Tile.ROCK
        }
    }

    private Set<Point> parseRocks(String line) {
        def corners = line.split(' -> ').collect { coords ->
            def (x, y) = coords.split(',')*.toInteger()
            new Point(x, y)
        }

        def allRocks = [] as Set

        corners.collate(2, 1, false).each { Point from, Point to ->
            ((from.x)..(to.x)).each { x ->
                ((from.y)..(to.y)).each { y ->
                    allRocks << new Point(x, y)
                }
            }
        }
        return allRocks
    }

    void print() {
        print("\033[H\033[2J")

        for (y in (0..(maxY + VERTICAL_MARGIN))) {
            printf("%2d ", y)
            for (x in ((minX - HORIZONTAL_MARGIN)..(maxX + HORIZONTAL_MARGIN))) {
                if (y < maxY + VERTICAL_MARGIN) {
                    print(tiles[new Point(x, y)]?.symbol ?: '.')
                } else {
                    print('#')
                }
            }
            println()
        }
        println()
    }

    boolean rainSand() {
        def position = SAND_SOURCE_POSITION

        while (true) {
            def nextPosition = [position.down(), position.downLeft(), position.downRight()].find {
                !tiles.keySet().contains(it) && it.y < maxY + VERTICAL_MARGIN
            }
            if (nextPosition != null) {
                position = nextPosition
            } else {
                tiles[position] = Tile.SAND
                return position == SAND_SOURCE_POSITION
            }
        }
    }
}

def cave = new Cave(lines)
cave.print()

for (def i = 1; ; i++) {
    def done = cave.rainSand()

    if (Config.draw || done) {
        cave.print()
        println("Round #$i\n")
        Thread.sleep(Config.drawPeriod)
    }

    if (done) {
        break
    }
}

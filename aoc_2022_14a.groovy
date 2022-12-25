import groovy.transform.Immutable
import groovy.transform.TupleConstructor

def testInput = '''
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
'''
def realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_14.txt/)
def lines = testInput.readLines().findAll()

class Config {
    static draw = true
    static drawPeriod = 100
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
    static final int MARGIN = 1
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
        if (!Config.draw) {
            return
        }
        print("\033[H\033[2J")

        for (y in (0..(maxY + MARGIN))) {
            printf("%2d ", y)
            for (x in ((minX - MARGIN)..(maxX + MARGIN))) {
                print(tiles[new Point(x, y)]?.symbol ?: '.')
            }
            println()
        }
        println()
        Thread.sleep(Config.drawPeriod)
    }

    boolean rainSand() {
        def position = SAND_SOURCE_POSITION

        while (true) {
            def nextPosition = [position.down(), position.downLeft(), position.downRight()].find {
                !tiles.keySet().contains(it)
            }
            if (nextPosition != null) {
                position = nextPosition
                if (position.y >= maxY) {
                    return true
                }
            } else {
                tiles[position] = Tile.SAND
                print()
                return false
            }
        }
    }
}

def cave = new Cave(lines)
cave.print()

for (def i = 0; ; i++) {
    def done = cave.rainSand()
    if (done) {
        println("done in $i rounds")
        break
    }
}

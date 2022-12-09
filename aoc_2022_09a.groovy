import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

class Config {
    static radius = 5

    static draw = true
    static drawInPlace = true
    static drawPeriod = 100

    static testInput = false
    static maxSteps = 100

    static showVisited = true
}

testInput = '''
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_09.txt/)
lines = (Config.testInput ? testInput : realInput).readLines().findAll().take(Config.maxSteps)

@TupleConstructor
@EqualsAndHashCode(includes = ['x', 'y'])
class Point {
    int x
    int y

    boolean touches(Point that) {
        return this != that &&
                (this.x - that.x).abs() <= 1 &&
                (this.y - that.y).abs() <= 1
    }

    Point copy() {
        new Point(x, y)
    }
}

class Layout {
    int radius

    Point center = new Point(0, 0)
    Point start = new Point(0, 0)
    Point head = new Point(0, 0)
    Point tail = new Point(0, 0)

    Set<Point> visited = [tail.copy()]

    void up() {
        move {
            head.y--
        }
    }

    void down() {
        move {
            head.y++
        }
    }

    void left() {
        move {
            head.x--
        }
    }

    void right() {
        move {
            head.x++
        }
    }

    private void move(Closure moveHead) {
        moveHead()
        moveTail()
        recenter()
    }

    private void moveTail() {
        if (tail == head || tail.touches(head)) {
            return
        }
        tail.x += Math.signum(head.x - tail.x).toInteger()
        tail.y += Math.signum(head.y - tail.y).toInteger()

        visited << tail.copy()
    }

    private void recenter() {
        if (head.x > center.x + radius) {
            center.x += 1
        }
        if (head.x < center.x - radius) {
            center.x -= 1
        }
        if (head.y > center.y + radius) {
            center.y += 1
        }
        if (head.y < center.y - radius) {
            center.y -= 1
        }
    }

    void print() {
        if (!Config.draw) {
            return
        }
        if (Config.drawInPlace) {
            print("\033[H\033[2J")
        }

        for (y in (-radius + center.y)..(radius + center.y)) {
            for (x in (-radius + center.x)..(radius + center.x)) {
                def point = new Point(x, y)
                switch (point) {
                    case head:
                        print('H')
                        break
                    case tail:
                        print('T')
                        break
                    case start:
                        print('s')
                        break
                    default:
                        if (Config.showVisited && point in visited) {
                            print('#')
                        } else {
                            print('.')
                        }
                        break
                }
            }
            println()
        }
        println()

        if (Config.drawInPlace) {
            Thread.sleep(Config.drawPeriod)
        }
    }
}

layout = new Layout(radius: Config.radius)
layout.print()

lines.each { line ->
    if (Config.draw && !Config.drawInPlace) {
        println("== $line ==\n")
    }
    def (direction, steps) = line.split()

    steps.toInteger().times { i ->
        if (Config.draw && !Config.drawInPlace) {
            println("#${i + 1}\n")
        }

        switch (direction) {
            case 'U':
                layout.up()
                break
            case 'D':
                layout.down()
                break
            case 'L':
                layout.left()
                break
            case 'R':
                layout.right()
                break
        }
        layout.print()
    }
}

println layout.visited.size()

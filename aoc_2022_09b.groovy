import groovy.transform.EqualsAndHashCode

class Config {
    static draw = true
    static drawInPlace = true
    static drawPeriod = 100
    static radius = 10
    static showVisited = true

    static testInput = true
    static maxSteps = 100
}

testInput = '''
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_09.txt/)
lines = (Config.testInput ? testInput : realInput).readLines().findAll().take(Config.maxSteps)

@EqualsAndHashCode(includes = ['x', 'y'])
class Point {
    int x = 0
    int y = 0
    String name

    boolean touches(Point that) {
        return this != that &&
                (this.x - that.x).abs() <= 1 &&
                (this.y - that.y).abs() <= 1
    }

    Point copy() {
        new Point(x: x, y: y)
    }
}

class Layout {
    int radius

    Point center = new Point()
    Point start = new Point(name: 's')

    List<Point> snake
    Set<Point> visited = []

    Layout() {
        this.snake = (['H'] + (1..9)).collect {
            new Point(name: it)
        }
        this.visited << tail.copy()
    }

    Point getHead() {
        snake.first()
    }

    Point getTail() {
        snake.last()
    }

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
        moveRest(1)
        recenter()
    }

    private void moveRest(int index) {
        def previous = snake[index - 1]
        def current = snake[index]

        if (current == null || current == previous || current.touches(previous)) {
            return
        }
        current.x += Math.signum(previous.x - current.x).toInteger()
        current.y += Math.signum(previous.y - current.y).toInteger()

        if (current == tail) {
            visited << tail.copy()
        }

        moveRest(index + 1)
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
                def point = new Point(x: x, y: y)

                def segment = (snake + start).find { it == point }
                if (segment != null) {
                    print(segment.name)
                } else {
                    if (Config.showVisited && point in visited) {
                        print('#')
                    } else {
                        print('.')
                    }
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

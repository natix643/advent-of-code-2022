testInput = '''
A Y
B X
C Z
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_02.txt/)

lines = realInput.readLines().findAll()

def scoreSelection(shape) {
    switch (shape) {
        case Shape.ROCK: return 1
        case Shape.PAPER: return 2
        case Shape.SCISSORS: return 3
    }
}

def scoreOutcome(me, opponent) {
    def outcome = me.compareRps(opponent)
    if (outcome < 0) return 0
    else if (outcome == 0) return 3
    else if (outcome > 0) return 6
}

enum Shape {
    ROCK,
    PAPER,
    SCISSORS

    static Shape parse(code) {
        switch (code) {
            case ['A', 'X']: return ROCK
            case ['B', 'Y']: return PAPER
            case ['C', 'Z']: return SCISSORS
        }
    }

    int compareRps(Shape that) {
        def diff = this.ordinal() - that.ordinal()
        if (diff == 2) diff = -1
        if (diff == -2) diff = 1
        return diff
    }
}

scores = lines.collect { line ->
    def (opponent, me) = line.split().collect { Shape.parse(it) }
    scoreSelection(me) + scoreOutcome(me, opponent)
}
scores.sum()

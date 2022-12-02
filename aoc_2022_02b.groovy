testInput = '''
A Y
B X
C Z
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_02.txt/)
//lines = testInput.readLines().findAll()
lines = realInput.readLines().findAll()

enum Shape {
    ROCK,
    PAPER,
    SCISSORS

    static BY_CODE = [
            'A': ROCK,
            'B': PAPER,
            'C': SCISSORS
    ]

    def getScore() {
        switch (this) {
            case ROCK: return 1
            case PAPER: return 2
            case SCISSORS: return 3
        }
    }
}

enum Outcome {
    LOSE,
    DRAW,
    WIN

    static BY_CODE = [
            'X': LOSE,
            'Y': DRAW,
            'Z': WIN
    ]

    def getScore() {
        switch (this) {
            case LOSE: return 0
            case DRAW: return 3
            case WIN: return 6
        }
    }
}

Shape selectShape(Shape opponent, Outcome outcome) {
    def ordinal = opponent.ordinal()
    if (outcome == Outcome.LOSE) {
        ordinal--
    } else if (outcome == Outcome.WIN) {
        ordinal++
    }
    return Shape.values()[ordinal % Shape.values().size()]
}

lines.collect { line ->
    def (opponentCode, outcomeCode) = line.split()
    def opponentShape = Shape.BY_CODE[opponentCode]
    def outcome = Outcome.BY_CODE[outcomeCode]
    def myShape = selectShape(opponentShape, outcome)

    myShape.score + outcome.score
}.sum()

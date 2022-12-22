import groovy.transform.Memoized

testInput = '''
root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_21.txt/)
def lines = realInput.readLines().findAll()

class Name {
    static final ROOT = 'root'
    static final HUMAN = 'humn'
}

trait Node {
    String name

    abstract Double getValue()

    abstract boolean hasHuman()

    abstract void computeHuman(double value)

    abstract void printTree(int indent = 0)
}

class Leaf implements Node {
    Double value

    boolean hasHuman() {
        return name == Name.HUMAN
    }

    void computeHuman(double value) {
        if (name == Name.HUMAN) {
            this.value = value
        }
    }

    void printTree(int indent) {
        println((' ' * indent * 2) + this)
    }

    String toString() {
        "$name: $value"
    }
}

class Inner implements Node {
    String left
    String right
    String operator
    Map<String, Node> bindings

    Double getValue() {
        def left = leftNode.value
        def right = rightNode.value
        if ([left, right].contains(null)) {
            return null
        }
        switch (operator) {
            case '+':
                return left + right
            case '-':
                return left - right
            case '*':
                return left * right
            case '/':
                return left / right
        }
    }

    @Memoized
    boolean hasHuman() {
        return leftNode.hasHuman() || rightNode.hasHuman()
    }

    void computeHuman(double value) {
        def leftHasHuman = leftNode.hasHuman()
        def rightHasHuman = rightNode.hasHuman()

        if (leftHasHuman) {
            switch (operator) {
                case '+':
                    leftNode.computeHuman(value - rightNode.value)
                    break
                case '-':
                    leftNode.computeHuman(value + rightNode.value)
                    break
                case '*':
                    leftNode.computeHuman(value / rightNode.value)
                    break
                case '/':
                    leftNode.computeHuman(value * rightNode.value)
                    break
            }
        } else if (rightHasHuman) {
            switch (operator) {
                case '+':
                    rightNode.computeHuman(value - leftNode.value)
                    break
                case '-':
                    rightNode.computeHuman(leftNode.value - value)
                    break
                case '*':
                    rightNode.computeHuman(value / leftNode.value)
                    break
                case '/':
                    rightNode.computeHuman(leftNode.value / value)
                    break
            }
        }
    }

    void printTree(int indent) {
        println((' ' * indent * 2) + this)
        leftNode.printTree(indent + 1)
        rightNode.printTree(indent + 1)
    }

    Node getLeftNode() {
        return bindings[left]
    }

    Node getRightNode() {
        return bindings[right]
    }

    String toString() {
        "$name: $left $operator $right"
    }
}

Map<String, Node> parse(List<String> lines) {
    Map<String, Node> bindings = [:]
    lines.each { line ->
        def (name, value) = line.split(':').collect { it.trim() }
        if (value.isLong()) {
            bindings[name] = new Leaf(
                    name: name,
                    value: name == Name.HUMAN ? null : value.toDouble()
            )
        } else {
            def (left, operator, right) = value.split()
            bindings[name] = new Inner(
                    name: name,
                    left: left,
                    right: right,
                    operator: name == Name.ROOT ? '-' : operator,
                    bindings: bindings
            )
        }
    }
    return bindings
}

def nodes = parse(lines)
def root = nodes[Name.ROOT]
def human = nodes[Name.HUMAN]

root.computeHuman(0)
println human.value.toBigDecimal()

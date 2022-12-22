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

trait Node {
    String name
}

class Leaf implements Node {
    long value
}

class Inner implements Node {
    String left
    String right
    String operator
}

long eval(Map<String, Node> bindings, Node node) {
    switch (node) {
        case Leaf:
            return node.value
        case Inner:
            def left = eval(bindings, bindings[node.left])
            def right = eval(bindings, bindings[node.right])

            switch (node.operator) {
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
}

List<Node> nodes = lines.collect { line ->
    def (name, value) = line.split(':').collect { it.trim() }
    if (value.isLong()) {
        return new Leaf(name: name, value: value.toLong())
    } else {
        def (left, operator, right) = value.split()
        return new Inner(name: name, left: left, right: right, operator: operator)
    }
}

Map<String, Node> bindings = nodes.collectEntries {
    [(it.name): it]
}

eval(bindings, bindings['root'])

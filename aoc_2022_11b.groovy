import groovy.transform.ToString

import java.util.function.UnaryOperator

import static java.util.Comparator.reverseOrder

def testInput = '''
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
'''
def realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_11.txt/).getText()
def lineGroups = realInput.split('\n\n').collect { it.readLines().findAll() }

@ToString(includeNames = true)
class Monkey {
    int id
    Queue<Long> items
    UnaryOperator<Long> operation
    long divisor
    int ifTrueMonkey
    int ifFalseMonkey

    int inspectedCount = 0

    Monkey(List<String> lines) {
        this.id = parseId(lines[0])
        this.items = parseItems(lines[1])
        this.operation = parseOperation(lines[2])
        this.divisor = parseDivisor(lines[3])
        this.ifTrueMonkey = parseNextMonkey(lines[4])
        this.ifFalseMonkey = parseNextMonkey(lines[5])
    }

    private int parseId(String line) {
        def value = line.split()[1]
        return value[0..-2] as int
    }

    private Queue<Long> parseItems(String line) {
        def value = line.split(':')[1]
        def list = value.split(',')*.toLong()
        return new ArrayDeque(list)
    }

    private UnaryOperator<Long> parseOperation(String line) {
        def tokens = line.split()
        def operator = tokens[4].with {
            switch (it) {
                case '+':
                    return { a, b -> a + b }
                case '*':
                    return { a, b -> a * b }
            }
        }
        def right = tokens[5]
        return { left ->
            right == 'old' ? operator(left, left) : operator(left, right.toLong())
        }
    }

    private long parseDivisor(String line) {
        return line.split().last().toLong()
    }

    private int parseNextMonkey(String line) {
        return line.split().last() as int
    }
}

class Interpreter {
    final List<Monkey> monkeys
    final Map<Integer, Monkey> monkeysById
    final long commonDivisor

    Interpreter(List<Monkey> monkeys) {
        this.monkeys = monkeys
        this.monkeysById = monkeys.collectEntries { [(it.id): it] }
        this.commonDivisor = monkeys*.divisor.inject { a, b -> a * b }
    }

    void executeRound() {
        monkeys.each {
            executeTurn(it)
        }
    }

    void executeTurn(Monkey monkey) {
        //println("Monkey ${monkey.id}:")
        while (!monkey.items.empty) {
            def item = monkey.items.remove()
            monkey.inspectedCount++
            //println("  Monkey inspects an item with a worry level of $item.")

            def worryLevel = monkey.operation.apply(item) % commonDivisor
            //println("    Worry level is changed to $worryLevel.")

            def divisible = worryLevel % monkey.divisor == 0
            //println("    Current worry level is ${divisible ? ' ' : 'not '}divisible by ${monkey.divisor}.")

            def nextMonkey = divisible ? monkey.ifTrueMonkey : monkey.ifFalseMonkey
            monkeysById[nextMonkey].items.add(worryLevel)
            //println("    Item with worry level $worryLevel is thrown to monkey $nextMonkey.")
        }
    }
}

def interpreter = new Interpreter(lineGroups.collect { new Monkey(it) })

10000.times {
    def round = it + 1
    println("== After round $round ==")

    interpreter.executeRound()
    interpreter.monkeys.each {
        //println("Monkey ${it.id}: ${it.items.join(', ')}")
        println("Monkey ${it.id} inspected items ${it.inspectedCount} times.")
    }
    println()
}

def mostActive = interpreter.monkeys.collect { it.inspectedCount as long }.toSorted(reverseOrder()).take(2)
def result = mostActive[0] * mostActive[1]
println result

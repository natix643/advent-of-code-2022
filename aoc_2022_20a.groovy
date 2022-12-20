import groovy.transform.TupleConstructor

testInput = '''
1
2
-3
3
-2
0
4
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_20.txt/)
lines = realInput.readLines().findAll()

@TupleConstructor
class Reference {
    final int number

    String toString() {
        this.number
    }
}

void println(List<Reference> references) {
    println(references.join(', '))
}

void shiftRight(List<?> list, int index) {
    if (index == list.size() - 1) {
        Collections.rotate(list, 1)
        list.swap(0, 1)
    } else {
        def to = index + 1
        list.swap(index, to)
        if (to == list.size() - 1) {
            Collections.rotate(list, 1)
        }
    }
}

void shiftLeft(List<?> list, int index) {
    if (index == 0) {
        Collections.rotate(list, -1)
        list.swap(list.size() - 2, list.size() - 1)
    } else {
        def to = index - 1
        list.swap(index, to)
        if (to == 0) {
            Collections.rotate(list, -1)
        }
    }
}

def sourceNumbers = lines.collect {
    new Reference(it.toInteger())
}
def targetNumbers = sourceNumbers.toList()

println 'Initial arrangement:'
println targetNumbers

sourceNumbers.each { reference ->
    def number = reference.number
//    println "\n$number moves"

    if (number > 0) {
        number.times {
            def index = targetNumbers.indexOf(reference)
            shiftRight(targetNumbers, index)
//            println targetNumbers
        }
    } else {
        number.abs().times {
            def index = targetNumbers.indexOf(reference)
            shiftLeft(targetNumbers, index)
//            println targetNumbers
        }
    }
}

int findAtIndex(List<Reference> references, int index) {
    def zeroIndex = references.findIndexOf { it.number == 0 }
    def normalizedIndex = (zeroIndex + index) % references.size()
    return references[normalizedIndex].number
}

def result = findAtIndex(targetNumbers, 1000) + findAtIndex(targetNumbers, 2000) + findAtIndex(targetNumbers, 3000)
println "\n$result"

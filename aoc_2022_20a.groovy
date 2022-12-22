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

class Reference {
    int number
}

void println(List<Reference> references) {
    println(references*.number.join(', '))
}

int shiftRight(List<?> list, int index) {
    if (index == list.size() - 1) {
        Collections.rotate(list, 1)
        list.swap(0, 1)
        return 1
    } else {
        def to = index + 1
        list.swap(index, to)
        if (to == list.size() - 1) {
            Collections.rotate(list, 1)
            return 0
        } else {
            return to
        }
    }
}

int shiftLeft(List<?> list, int index) {
    if (index == 0) {
        Collections.rotate(list, -1)
        list.swap(list.size() - 2, list.size() - 1)
        return list.size() - 2
    } else {
        def to = index - 1
        list.swap(index, to)
        if (to == 0) {
            Collections.rotate(list, -1)
            return list.size() - 1
        } else {
            return to
        }
    }
}

int findAtIndex(List<Reference> references, int index) {
    def zeroIndex = references.findIndexOf { it.number == 0 }
    def normalizedIndex = (zeroIndex + index) % references.size()
    return references[normalizedIndex].number
}

def sourceNumbers = lines.collect {
    new Reference(number: it.toInteger())
}
def targetNumbers = sourceNumbers.toList()

println 'Initial arrangement:'
println targetNumbers

sourceNumbers.each { reference ->
    def shift = reference.number % (targetNumbers.size() - 1)
//    println "\n${reference.number} moves"

    if (shift > 0) {
        def index = targetNumbers.indexOf(reference)
        shift.times {
            index = shiftRight(targetNumbers, index)
//            println targetNumbers
        }
    } else {
        def index = targetNumbers.indexOf(reference)
        shift.abs().times {
            index = shiftLeft(targetNumbers, index)
//            println targetNumbers
        }
    }
}

def result = findAtIndex(targetNumbers, 1000) + findAtIndex(targetNumbers, 2000) + findAtIndex(targetNumbers, 3000)
println "\n$result"

import groovy.json.JsonSlurper
import groovy.transform.TupleConstructor

def testInput = '''
[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
'''
realInput = new File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_13.txt/)
lines = realInput.readLines().findAll()

def compare(List left, List right) {
    if (left == right) {
        return 0
    }
    if (left.empty) {
        return -1
    }
    if (right.empty) {
        return 1
    }

    def headCompare = compare(left.head(), right.head())
    if (headCompare != 0) {
        return headCompare
    } else {
        return compare(left.tail(), right.tail())
    }
}

def compare(int left, int right) {
    return left <=> right
}

def compare(List left, int right) {
    return compare(left, [right])
}

def compare(int left, List right) {
    return compare([left], right)
}

def divider1 = [[2]]
def divider2 = [[6]]

lists = lines.collect {
    new JsonSlurper().parse(new StringReader((it))) as List
} + [ divider1, divider2 ]
lists.sort { left, right -> compare(left, right) }

(index1, index2) = [divider1, divider2].collect { lists.indexOf(it) + 1 }
index1 * index2
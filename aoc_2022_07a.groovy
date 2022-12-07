testInput = '''
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
'''
realInput = new java.io.File(/C:\Users\jirka\IdeaProjects\advent-of-code-2022\aoc_2022_07.txt/)
input = realInput.readLines().findAll()

trait Path {
    String name
    int size
}

class File implements Path {

    String toString() {
        return "$name (file, size=$size)"
    }
}

class Dir implements Path {
    List<Path> children = []
    Dir parent

    int getSize() {
        return children.collect { it.size }.sum()
    }

    String toString() {
        return "$name (dir)"
    }
}

class FileSystem {
    Dir root = new Dir(name: '/')
    Dir current = root

    void cd(String name) {
        switch (name) {
            case '/':
                current = root
                break
            case '..':
                current = current.parent
                break
            default:
                current = current.children.find { it instanceof Dir && it.name == name } as Dir
        }
    }

    void print(Path path = root, int indent = 0) {
        println "${' ' * indent}- $path"
        if (path instanceof Dir) {
            path.children.each {
                print(it, indent + 2)
            }
        }
    }

    List<Path> list(Path path = root, List<Path> result = []) {
        result << path
        if (path instanceof Dir) {
            path.children.each {
                list(it, result)
            }
        }
        return result
    }
}

class Console {

    FileSystem fileSystem
    Queue<String> commands

    void run() {
        while (!commands.empty) {
            executeCommand()
        }
    }

    void executeCommand() {
        def command = commands.remove()
//        println "executing: $command"

        def parts = command.split()
        switch (parts[1]) {
            case 'cd':
                fileSystem.cd(parts[2])
                break
            case 'ls':
                ls()
                break
        }
    }

    void ls() {
        while (!commands.empty && !commands.peek().startsWith('$')) {
            def line = commands.remove()
            fileSystem.current.children << parsePath(line)
        }
    }

    Path parsePath(String line) {
        def parts = line.split()
        if (parts[0] == 'dir') {
            return new Dir(name: parts[1], parent: fileSystem.current)
        } else {
            return new File(name: parts[1], size: parts[0] as int)
        }
    }
}

fileSystem = new FileSystem()
console = new Console(
        fileSystem: fileSystem,
        commands: new ArrayDeque(input)
)
console.run()
//fileSystem.print()

fileSystem.list()
        .findAll { it instanceof Dir && it.size < 100_000 }
        .sum { it.size }

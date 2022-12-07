import kotlin.math.min

private fun root(): Node {
    val root = Node()
    var cur = root
    readInput("Day07").forEach { cmd ->
        if (cmd.startsWith("$")) {
            val cmdS = cmd.split(" ")
            if (cmdS[1] == "cd") {
                cur = when (cmdS[2]) {
                    "/" -> root
                    ".." -> cur.parent
                    else -> cur.children[cmdS[2]]!!
                }
            }
        } else {
            val (size, name) = cmd.split(" ")
            if (size == "dir") {
                cur.children.putIfAbsent(name, Node(name, cur))
            } else {
                cur.children[name] = Node(name, size.toInt(), cur)
            }
        }
    }
    return root
}

private fun traverseDirSizes(node: Node, acceptDirSize: (Long) -> Unit = {}): Long {
    return if (node.dir) {
        val dirSize = node.children.map { traverseDirSizes(it.value, acceptDirSize) }.sum()
        acceptDirSize(dirSize)
        dirSize
    } else {
        node.size.toLong()
    }
}

private fun part1(root: Node): Long {
    val threshold = 100000L
    var dirsSum = 0L

    traverseDirSizes(root) { dirSize ->
        if (dirSize <= threshold) {
            dirsSum += dirSize
        }
    }
    return dirsSum
}

private fun part2(root: Node): Long {
    val threshold = traverseDirSizes(root) - 40000000
    var minDirSize = Long.MAX_VALUE

    traverseDirSizes(root) { dirSize ->
        if (dirSize >= threshold) {
            minDirSize = min(minDirSize, dirSize)
        }
    }
    return minDirSize
}

fun main() {
    println(part1(root()))
    println(part2(root()))
}

data class Node(
    val dir: Boolean,
    val size: Int = 0,
    val name: String,
    val children: HashMap<String, Node> = HashMap()
) {
    var parent: Node = this

    // create dir
    constructor(dirName: String, parent: Node) : this(dir = true, name = dirName) {
        this.parent = parent
    }

    // create file
    constructor(filename: String, size: Int, parent: Node) : this(dir = false, name = filename, size = size) {
        this.parent = parent
    }

    // create root
    constructor() : this(dir = true, name = "/")
}

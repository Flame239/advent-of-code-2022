private fun input(): Stacks {
    val lines = readInput("Day05")
    val stacksInput = lines.takeWhile { it.isNotEmpty() }
    val stacksCount = (stacksInput.last().length + 2) / 4
    val stacks = List(stacksCount) { ArrayDeque<Char>() }
    stacksInput.forEach {
        for (i in 0 until stacksCount) {
            val charInd = 4 * i + 2
            if (charInd >= it.length) continue
            val curChar = it[4 * i + 1]
            if (curChar.isUpperCase()) {
                stacks[i].addFirst(curChar)
            }
        }
    }
    val ops = lines.subList(stacksInput.size + 1, lines.size).map {
        val split = it.split(" ")
        OP(split[1].toInt(), split[3].toInt() - 1, split[5].toInt() - 1)
    }
    return Stacks(stacks, ops)
}

private fun part1(input: Stacks): String {
    val stacks = input.stacks
    input.ops.forEach { op ->
        repeat(op.count) {
            stacks[op.to].add(stacks[op.from].removeLast())
        }
    }
    return stacks.map { it.last() }.joinToString(separator = "")
}

private fun part2(input: Stacks): String {
    val stacks = input.stacks
    input.ops.forEach { op ->
        val tmp = mutableListOf<Char>()
        repeat(op.count) {
            tmp.add(stacks[op.from].removeLast())
        }
        tmp.reversed().forEach { stacks[op.to].add(it) }
    }
    return stacks.map { it.last() }.joinToString(separator = "")
}

fun main() {
    println(part1(input()))
    println(part2(input()))
}

data class Stacks(val stacks: List<ArrayDeque<Char>>, val ops: List<OP>)

data class OP(val count: Int, val from: Int, val to: Int)

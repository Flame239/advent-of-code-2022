private fun ranges(): List<Pair<IntRange, IntRange>> {
    return readInput("Day04").map { it.split(",") }.map { it[0].split("-") + it[1].split("-") }
        .map { Pair(IntRange(it[0].toInt(), it[1].toInt()), IntRange(it[2].toInt(), it[3].toInt())) }
}

private fun part1(ranges: List<Pair<IntRange, IntRange>>): Int {
    return ranges.count { it.first.contains(it.second) || it.second.contains(it.first) }
}

private fun part2(ranges: List<Pair<IntRange, IntRange>>): Int {
    return ranges.count { it.first.intersect(it.second) }
}

fun main() {
    println(part1(ranges()))
    println(part2(ranges()))
}

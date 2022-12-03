private fun rucksacks(): List<Pair<String, String>> {
    return readInput("Day03").map { Pair(it.substring(0, it.length / 2), it.substring(it.length / 2)) }
}

private fun rucksackGroups(): List<List<String>> {
    return readInput("Day03").chunked(3)
}

private fun part1(rucksacks: List<Pair<String, String>>): Int {
    return rucksacks.sumOf {
        val common = it.first.find { c -> it.second.contains(c) }!!
        score(common)
    }
}

private fun part2(rucksacks: List<List<String>>): Int {
    return rucksacks.sumOf {
        val common = it[0].find { c -> it[1].contains(c) && it[2].contains(c) }!!
        score(common)
    }
}

private fun score(c: Char): Int = if (Character.isUpperCase(c)) (c - 'A' + 27) else (c - 'a' + 1)

fun main() {
    println(part1(rucksacks()))
    println(part2(rucksackGroups()))
}

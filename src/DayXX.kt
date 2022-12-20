private fun input(): List<String> {
    return readInput("DayXX")
}

private fun part1(input: List<String>): Int {
    return input.size
}

private fun part2(input: List<String>): Int {
    return input.size
}

fun main() {
    measure { part1(input()) }
    measure { part2(input()) }
}

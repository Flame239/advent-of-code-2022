private fun games(): List<RPS> = readInput("Day02").map { RPS(it[0] - 'A', it[2] - 'X') }
private fun games2(): List<RPS2> = readInput("Day02").map { RPS2(it[0] - 'A', it[2] - 'X') }

private fun part1(games: List<RPS>): Int {
    return games.sumOf { it.gamePts() + it.shapePts() }
}

private fun part2(games: List<RPS2>): Int {
    return games.sumOf { it.gamePts() + it.shapePts() }
}

fun main() {
    println(part1(games()))
    println(part2(games2()))
}

data class RPS(val opp: Int, val me: Int) {
    fun gamePts(): Int = (me - opp + 1).mod(3) * 3
    fun shapePts(): Int = me + 1
}

data class RPS2(val opp: Int, val res: Int) {
    fun gamePts(): Int = 3 * res
    fun shapePts(): Int = (opp + res - 1).mod(3) + 1
}



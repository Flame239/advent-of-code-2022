import kotlin.math.abs
import kotlin.math.sign

private fun moves(): List<Pair<String, Int>> {
    return readInput("Day09").map { it.split(" ") }.map { Pair(it[0], it[1].toInt()) }
}

private fun part1(moves: List<Pair<String, Int>>): Int = uniqueTailPositions(moves, 2)
private fun part2(moves: List<Pair<String, Int>>): Int = uniqueTailPositions(moves, 10)

private fun uniqueTailPositions(moves: List<Pair<String, Int>>, knotsCount: Int): Int {
    val knots = Array(knotsCount) { Coord(0, 0) }
    val uniqueTailPos = HashSet<Coord>()
    uniqueTailPos.add(knots.last())

    moves.forEach { m ->
        repeat(m.second) {
            when (m.first) {
                "R" -> knots[0].x += 1
                "L" -> knots[0].x -= 1
                "U" -> knots[0].y += 1
                "D" -> knots[0].y -= 1
            }
            for (i in 0 until knots.lastIndex) {
                knots[i + 1].moveTo(knots[i])
            }
            uniqueTailPos.add(knots.last().copy())
        }
    }

    return uniqueTailPos.size
}

fun main() {
    println(part1(moves()))
    println(part2(moves()))
}

data class Coord(var x: Int, var y: Int) {
    fun moveTo(other: Coord) {
        val diffX = (other.x - x)
        val diffY = (other.y - y)

        if (abs(diffX) <= 1 && abs(diffY) <= 1) return

        y += diffY.sign
        x += diffX.sign
    }
}
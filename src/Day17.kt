import kotlin.math.max

private fun pushes(): List<Int> {
    return readInput("Day17")[0].map { if (it == '>') 1 else -1 }
}

private val addHeightByTurn = mutableListOf<Long>()

private fun part1(pushes: List<Int>, rounds: Int): Int {
    val game = Array(7) { BooleanArray(100000) }
    for (i in 0 until 7) game[i][0] = true
    var height = 0
    var pushTurn = 0

    repeat(rounds) { round ->
        var curShape = Shape(round, height + 4)
        while (true) {
            val xDiff = pushes[pushTurn++ % pushes.size]
            val sideShape = curShape.shiftX(xDiff)
            if (!sideShape.collide(game)) curShape = sideShape
            val downShape = curShape.shiftY(-1)
            if (downShape.collide(game)) {
                curShape.includeInto(game)
                addHeightByTurn.add(max(curShape.top() - height, 0).toLong())
                height = max(curShape.top(), height)
                break
            } else {
                curShape = downShape
            }
        }
    }
    return height
}

private fun part2(pushes: List<Int>): Long {
    addHeightByTurn.clear()
    part1(pushes, 20000)

    val turns = 1000000000000L
    // found manually (V)-_-(V)
    val offset = 156 // 15
    val cycle = 1725 // 35

    val offsetSum = addHeightByTurn.take(offset).sum()
    val cycleSum = addHeightByTurn.drop(offset).take(cycle).sum()

    val cycleTotal = (turns - offset) / cycle * cycleSum
    val cycleRem = (turns - offset) % cycle
    val cycleRemTotal = addHeightByTurn.drop(offset).take(cycleRem.toInt()).sum()

    return offsetSum + cycleTotal + cycleRemTotal
}

data class Shape(var pts: List<C>) {
    constructor(turn: Int, height: Int) : this(createShape(turn, height))

    fun shiftX(diff: Int): Shape = Shape(pts.map { it.shiftX(diff) })

    fun shiftY(diff: Int) = Shape(pts.map { it.shiftY(diff) })

    fun top(): Int = pts.maxOf { it.y }

    fun collide(game: Array<BooleanArray>): Boolean {
        val collideWithSides = pts.any { it.x < 0 || it.x >= game.size }
        return collideWithSides || pts.any { game[it.x][it.y] }
    }

    fun includeInto(game: Array<BooleanArray>) {
        pts.forEach { game[it.x][it.y] = true }
    }
}

fun createShape(turn: Int, height: Int): List<C> = shapes[turn % shapes.size].map { it.shiftY(height) }

// already shifted right by 2
private val shapes = listOf(
    listOf(C(2, 0), C(3, 0), C(4, 0), C(5, 0)),
    listOf(C(2, 1), C(3, 0), C(3, 1), C(3, 2), C(4, 1)),
    listOf(C(2, 0), C(3, 0), C(4, 0), C(4, 1), C(4, 2)),
    listOf(C(2, 0), C(2, 1), C(2, 2), C(2, 3)),
    listOf(C(2, 0), C(2, 1), C(3, 0), C(3, 1))
)

fun main() {
    println(part1(pushes(), 2022))
    println(part2(pushes()))
}

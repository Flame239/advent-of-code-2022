import kotlin.math.max

private fun map(): Array<BooleanArray> {
    var maxY = 0
    val rocks: List<List<C>> = readInput("Day14").map {
        it.split(" -> ")
            .map { c -> c.split(",").map(String::toInt) }
            .map { c -> C(c[0], c[1].also { y -> maxY = max(maxY, y) }) }
    }
    val w = 1000
    val map = Array(w) { BooleanArray(maxY + 3) }
    rocks.forEach {
        it.windowed(2).forEach { (l, r) ->
            for (i in l.x toward r.x) map[i][l.y] = true
            for (j in l.y toward r.y) map[l.x][j] = true
        }
    }
    for (i in 0 until w) {
        map[i][map[0].lastIndex] = true
    }
    return map
}

private fun part1(map: Array<BooleanArray>): Int {
    var step = 0
    val maxY = map[0].lastIndex - 2
    while (true) {
        var x = 500
        var y = 0
        while (true) {
            if (y == maxY) return step
            if (!map[x][y + 1]) {
                y++
            } else if (!map[x - 1][y + 1]) {
                y++
                x--
            } else if (!map[x + 1][y + 1]) {
                y++
                x++
            } else {
                map[x][y] = true
                break
            }
        }
        step++
    }
}

private fun part2(map: Array<BooleanArray>): Int {
    var step = 0
    while (true) {
        var x = 500
        var y = 0
        if (map[x][y]) return step
        while (true) {
            if (!map[x][y + 1]) {
                y++
            } else if (!map[x - 1][y + 1]) {
                y++
                x--
            } else if (!map[x + 1][y + 1]) {
                y++
                x++
            } else {
                map[x][y] = true
                break
            }
        }
        step++
    }
}

fun main() {
    println(part1(map()))
    println(part2(map()))
}

import java.lang.Integer.max

private fun heights(): List<List<Int>> {
    return readInput("Day08").map { it.map { c -> c.digitToInt() } }
}

private fun part1(h: List<List<Int>>): Int {
    val size = h.size
    val visible = Array(size) { i ->
        BooleanArray(size) { j ->
            i == 0 || i == size - 1 || j == 0 || j == size - 1
        }
    }

    for (i in 1 until size - 1) {
        var curH = h[i][0]
        for (j in 1 until size - 1) {
            if (h[i][j] > curH) {
                visible[i][j] = true
                curH = h[i][j]
            }
        }

        curH = h[i][size - 1]
        for (j in size - 2 downTo 1) {
            if (h[i][j] > curH) {
                visible[i][j] = true
                curH = h[i][j]
            }
        }
    }

    for (j in 1 until size - 1) {
        var curH = h[0][j]
        for (i in 1 until size - 1) {
            if (h[i][j] > curH) {
                visible[i][j] = true
                curH = h[i][j]
            }
        }

        curH = h[size - 1][j]
        for (i in size - 2 downTo 1) {
            if (h[i][j] > curH) {
                visible[i][j] = true
                curH = h[i][j]
            }
        }
    }

    return visible.sumOf { arr -> arr.count { it } }
}

private fun part2(h: List<List<Int>>): Int {
    var max = 0
    val size = h.size
    for (i in 0 until size) {
        for (j in 0 until size) {
            var left = j
            for (k in j - 1 downTo 0) {
                if (h[i][k] >= h[i][j]) {
                    left = j - k
                    break
                }
            }
            var right = size - j - 1
            for (k in j + 1 until size) {
                if (h[i][k] >= h[i][j]) {
                    right = k - j
                    break
                }
            }
            var down = i
            for (k in i - 1 downTo 0) {
                if (h[k][j] >= h[i][j]) {
                    down = i - k
                    break
                }
            }
            var up = size - i - 1
            for (k in i + 1 until size) {
                if (h[k][j] >= h[i][j]) {
                    up = k - i
                    break
                }
            }
            val cur = left * right * down * up
            max = max(cur, max)
        }
    }
    return max
}

fun main() {
    println(part1(heights()))
    println(part2(heights()))
}

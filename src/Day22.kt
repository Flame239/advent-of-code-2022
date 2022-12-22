private fun mm(): MonkeyMap {
    val input = readInput("Day22")
    val moves = input.takeLastWhile { it.isNotBlank() }[0]
    val m = input.takeWhile { it.isNotBlank() }
    val w = m.maxOf { it.length }
    val map = Array(m.size) { IntArray(w) }
    m.forEachIndexed { i, s ->
        s.forEachIndexed { j, c ->
            val num = when (c) {
                ' ' -> -1
                '.' -> 0
                else -> 1
            }
            map[i][j] = num
        }
        for (j in s.length until w) {
            map[i][j] = -1
        }
    }
    return MonkeyMap(map, Regex("\\d+|\\D+").findAll(moves).map(MatchResult::value))
}

private fun part1(mm: MonkeyMap): Int {
    val map = mm.map
    val h = map.size
    val w = map[0].size
    var j = map[0].indexOfFirst { it >= 0 }
    var i = 0
    val d = D.RIGHT()
    mm.moves.forEach { move ->
        if (move == "L") {
            d.L()
        } else if (move == "R") {
            d.R()
        } else {
            for (s in 1..move.toInt()) {
                var nI = i + d.I
                var nJ = j + d.J
                if (d.I != 0 && (nI < 0 || nI >= h || map[nI][nJ] == -1)) {
                    while ((nI - d.I in 0 until h) && map[nI - d.I][nJ] >= 0) nI -= d.I
                }
                if (d.J != 0 && (nJ < 0 || nJ >= w || map[nI][nJ] == -1)) {
                    while ((nJ - d.J in 0 until w) && map[nI][nJ - d.J] >= 0) nJ -= d.J
                }
                if (map[nI][nJ] == 0) {
                    i = nI
                    j = nJ
                } else {
                    break
                }
            }
        }
    }
    return (1000 * (i + 1) + 4 * (j + 1) + d.score)
}

private fun part2(mm: MonkeyMap): Int {
    val map = mm.map
    val h = map.size
    val w = map[0].size
    var j = map[0].indexOfFirst { it >= 0 }
    var i = 0
    var d = D(0, 1)
    mm.moves.forEach { move ->
        if (move == "L") {
            d.L()
        } else if (move == "R") {
            d.R()
        } else {
            for (s in 1..move.toInt()) {
                var nI = i + d.I
                var nJ = j + d.J
                var nD = d

                val col = j / 50
                val colRem = j % 50
                val row = i / 50
                val rowRem = i % 50
                if (d.I != 0 && (nI < 0 || nI >= h || map[nI][nJ] == -1)) {
                    if (col == 0) {
                        if (d.I > 0) { // 7 -> 13
                            nD = D.DOWN()
                            nI = 0
                            nJ = 100 + colRem
                        } else { // 10 -> 11
                            nD = D.RIGHT()
                            nI = 50 + colRem
                            nJ = 50
                        }
                    }
                    if (col == 1) {
                        if (d.I > 0) { // 5 -> 6
                            nD = D.LEFT()
                            nI = 150 + colRem
                            nJ = 49
                        } else { // 2 -> 8
                            nD = D.RIGHT()
                            nI = 150 + colRem
                            nJ = 0
                        }
                    }
                    if (col == 2) {
                        if (d.I > 0) { // 14 -> 3
                            nD = D.LEFT()
                            nI = 50 + colRem
                            nJ = 99
                        } else { // 13 -> 7
                            nD = D.UP()
                            nI = 199
                            nJ = colRem
                        }
                    }
                }
                if (d.J != 0 && (nJ < 0 || nJ >= w || map[nI][nJ] == -1)) {
                    if (row == 0) {
                        if (d.J > 0) { // 1 -> 4 REV!
                            nD = D.LEFT()
                            nI = 149 - rowRem
                            nJ = 99
                        } else { // 12 -> 9 REV
                            nD = D.RIGHT()
                            nI = 149 - rowRem
                            nJ = 0
                        }
                    }

                    if (row == 1) {
                        if (d.J > 0) { // 3 -> 14
                            nD = D.UP()
                            nI = 49
                            nJ = 100 + rowRem
                        } else { // 11 -> 10
                            nD = D.DOWN()
                            nI = 100
                            nJ = rowRem
                        }
                    }

                    if (row == 2) {
                        if (d.J > 0) { //4 -> 1 rev
                            nD = D.LEFT()
                            nI = 49 - rowRem
                            nJ = 149
                        } else { // 9 -> 12  rev
                            nD = D.RIGHT()
                            nI = 49 - rowRem
                            nJ = 50
                        }
                    }

                    if (row == 3) {
                        if (d.J > 0) { // 6 -> 5
                            nD = D.UP()
                            nI = 149
                            nJ = 50 + rowRem
                        } else { // 8 -> 2
                            nD = D.DOWN()
                            nI = 0
                            nJ = 50 + rowRem
                        }
                    }
                }
                if (map[nI][nJ] == 0) {
                    i = nI
                    j = nJ
                    d = nD
                } else {
                    break
                }
            }
        }
    }

    return 1000 * (i + 1) + 4 * (j + 1) + d.score
}

fun main() {
    measure { part1(mm()) }
    measure { part2(mm()) }
}

private data class MonkeyMap(val map: Array<IntArray>, val moves: Sequence<String>)

private data class D(var I: Int, var J: Int) {
    val score: Int
        get() {
            if (I == 1) return 1
            if (I == -1) return 3
            if (J == 1) return 0
            if (J == -1) return 2
            return -1
        }

    fun L() {
        val newJ = if (J == 0) I else 0
        val newI = if (I == 0) -J else 0
        I = newI
        J = newJ
    }

    fun R() {
        val newJ = if (J == 0) -I else 0
        val newI = if (I == 0) J else 0
        I = newI
        J = newJ
    }

    companion object {
        fun UP() = D(-1, 0)
        fun DOWN() = D(1, 0)
        fun LEFT() = D(0, -1)
        fun RIGHT() = D(0, 1)
    }
}


private fun input(): Array<BooleanArray> {
    val gs = readInput("Day23")
    val padding = 100
    val h = gs.size + 2 * padding
    val w = gs[0].length + 2 * padding
    val arr = Array(h) { BooleanArray(w) }
    gs.forEachIndexed { rowInd, row ->
        row.forEachIndexed { i, c ->
            arr[rowInd + padding][i + padding] = (c == '#')
        }
    }
    return arr
}

fun moveElves(g: Array<BooleanArray>, part1: Boolean): Int {
    val elves = HashSet<CC>()
    for (i in g.indices) for (j in g[0].indices) if (g[i][j]) elves.add(CC(i, j))
    var d = 0
    repeat(if (part1) 10 else Int.MAX_VALUE) {
        val moves = HashMap<CC, MutableList<Move>>()
        elves.forEach { e ->
            val hasN = g[e.i - 1][e.j] || g[e.i - 1][e.j + 1] || g[e.i - 1][e.j - 1]
            val hasS = g[e.i + 1][e.j] || g[e.i + 1][e.j + 1] || g[e.i + 1][e.j - 1]
            val hasW = g[e.i][e.j - 1] || g[e.i + 1][e.j - 1] || g[e.i - 1][e.j - 1]
            val hasE = g[e.i][e.j + 1] || g[e.i + 1][e.j + 1] || g[e.i - 1][e.j + 1]
            if (!hasN && !hasW && !hasS && !hasE) return@forEach
            val has = arrayOf(hasN, hasS, hasW, hasE)
            for (di in 0..3) {
                val elfD = (d + di) % 4
                if (!has[elfD]) {
                    val from = CC(e.i, e.j)
                    val to = e.move(elfD)
                    moves.getOrPut(to) { mutableListOf() }.add(Move(from, to))
                    break
                }
            }
        }
        val possibleMoves = moves.filter { (_, moves) -> moves.size == 1 }
        if (possibleMoves.isEmpty() && !part1) return it + 1
        possibleMoves.forEach { (_, moves) ->
            val m = moves[0]
            g[m.from.i][m.from.j] = false
            g[m.to.i][m.to.j] = true
            elves.remove(m.from)
            elves.add(m.to)
        }
        d++
    }
    return if (part1) findArea(elves) else -1
}

private fun findArea(e: HashSet<CC>): Int {
    return (e.maxOf(CC::i) - e.minOf(CC::i) + 1) * (e.maxOf(CC::j) - e.minOf(CC::j) + 1) - e.size
}


fun main() {
    measure { moveElves(input(), true) }
    measure { moveElves(input(), false) }
}



private data class Move(val from: CC, val to: CC)

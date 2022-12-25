private fun map(): List<String> {
    val m = readInput("Day24")
    h = m.size
    w = m[0].length
    cycle = lcm(h - 2, w - 2)
    start = CC(0, m[0].indexOf("."))
    finish = CC(m.size - 1, m[m.size - 1].indexOf("."))
    return m
}

private var h = -1
private var w = -1
private var cycle = -1
private lateinit var start: CC
private lateinit var finish: CC

/*
 Just bfs, but 'visited' is tricky. Map repeats every 'cycle = lcm(h - 2, w - 2)' steps,
 so we should check if we visited cell on (step % cycle) before.
 e.g. if we visited a cell 600 steps before, we should skip it, as map looks exactly the same
 */
private fun shortestPathTo(map: List<String>, initStep: Int, start: CC, finish: CC): Int {
    val visited = HashMap<CC, BooleanArray>()
    var step = initStep
    val q = ArrayDeque(listOf(start))
    while (q.isNotEmpty()) {
        repeat(q.size) {
            val pos = q.removeFirst()
            if (pos == finish) return step - 1
            listOf(pos, pos.up(), pos.down(), pos.left(), pos.right())
                .filter { p -> shouldVisit(map, visited, p, step) }
                .forEach(q::add)
        }
        step++
    }
    return -1
}

private fun shouldVisit(map: List<String>, visited: HashMap<CC, BooleanArray>, p: CC, step: Int): Boolean {
    if (p.i < 0 || p.i >= h || map[p.i][p.j] == '#') return false
    val pVis = visited.getOrPut(p) { BooleanArray(cycle) }
    if (pVis[step % cycle]) return false
    pVis[step % cycle] = true

    // IMPROVEMENT: pre-calculate all possible maps (there will be `cycle` of them)
    if (map[p.i][(p.j - 1 - step).mod(w - 2) + 1] == '>') return false
    if (map[p.i][(p.j - 1 + step).mod(w - 2) + 1] == '<') return false
    if (map[(p.i - 1 - step).mod(h - 2) + 1][p.j] == 'v') return false
    if (map[(p.i - 1 + step).mod(h - 2) + 1][p.j] == '^') return false

    return true
}

private fun part1(map: List<String>): Int = shortestPathTo(map, 1, start, finish)

private fun part2(map: List<String>): Int {
    var step = shortestPathTo(map, 1, start, finish)
    step = shortestPathTo(map, step, finish, start)
    return shortestPathTo(map, step, start, finish)
}

fun main() {
    measure { part1(map()) }
    measure { part2(map()) }
}


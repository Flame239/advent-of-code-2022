private fun pts(): List<C3> {
    return readInput("Day18").map { it.split(",").map(String::toInt) }.map { C3(it[0], it[1], it[2]) }
        .also { ptsSet = HashSet(it) }
}

lateinit var ptsSet: HashSet<C3>
private fun part1(pts: List<C3>): Int {
    val total = pts.size * 6
    val covered = pts.sumOf { p -> getAdjacent(p).count { ptsSet.contains(it) } }
    return total - covered
}

var xBounds = 0..19
var yBounds = 1..19
var zBounds = 0..19
private fun part2(pts: List<C3>): Int {
    val trapped = HashMap<C3, Boolean>()
    for (x in xBounds) {
        for (y in yBounds) {
            for (z in zBounds) {
                val p = C3(x, y, z)
                if (ptsSet.contains(p) || trapped.contains(p)) continue
                val isTrapped = checkTrapped(p, trapped, mutableSetOf())
                markTrapped(p, isTrapped, trapped)
            }
        }
    }

    val total = pts.size * 6
    val coveredOrTrapped = pts.sumOf { p ->
        getAdjacent(p).filter { withinBounds(it) }.count { ptsSet.contains(it) || trapped[it]!! }
    }
    return total - coveredOrTrapped
}

private fun withinBounds(p: C3) = xBounds.contains(p.x) && yBounds.contains(p.y) && zBounds.contains(p.z)

fun getAdjacent(p: C3): List<C3> =
    listOf(p.shiftX(1), p.shiftX(-1), p.shiftY(1), p.shiftY(-1), p.shiftZ(1), p.shiftZ(-1))

fun checkTrapped(p: C3, trapped: HashMap<C3, Boolean>, current: MutableSet<C3>): Boolean {
    if (!withinBounds(p)) return false
    current.add(p)

    getAdjacent(p)
        .filter { !ptsSet.contains(it) }
        .forEach { if (!current.contains(it) && !checkTrapped(it, trapped, current)) return false }
    return true
}

fun markTrapped(p: C3, isTrapped: Boolean, trapped: HashMap<C3, Boolean>) {
    trapped[p] = isTrapped
    if (!(withinBounds(p))) return
    getAdjacent(p)
        .filter { !ptsSet.contains(it) }
        .forEach { if (!trapped.contains(it)) markTrapped(it, isTrapped, trapped) }
}

fun main() {
    println(part1(pts()))
    println(part2(pts()))
}

data class C3(val x: Int, val y: Int, val z: Int) {
    fun shiftX(diff: Int): C3 = C3(x + diff, y, z)
    fun shiftY(diff: Int): C3 = C3(x, y + diff, z)
    fun shiftZ(diff: Int): C3 = C3(x, y, z + diff)
}
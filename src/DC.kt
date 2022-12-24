import kotlin.math.abs

data class C(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x;$y)"
    }

    fun manhattan(other: C): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun shiftY(diff: Int): C = C(x, y + diff)
    fun shiftX(diff: Int): C = C(x + diff, y)
}

data class CC(val i: Int, val j: Int) {
    fun move(d: Int): CC {
        return when (d) {
            0 -> CC(i - 1, j)
            1 -> CC(i + 1, j)
            2 -> CC(i, j - 1)
            3 -> CC(i, j + 1)
            else -> error("D")
        }
    }

    fun up(): CC = CC(i - 1, j)
    fun down(): CC = CC(i + 1, j)
    fun left(): CC = CC(i, j - 1)
    fun right(): CC = CC(i, j + 1)
}
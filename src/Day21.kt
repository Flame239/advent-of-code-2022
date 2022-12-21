private fun input(): Map<String, M> {
    return readInput("Day21").associate { l ->
        val (id, exp) = l.split(": ")
        val expS = exp.split(" ")
        if (expS.size == 1) {
            id to M(id, const = exp.toLong())
        } else {
            parent[expS[2]] = id
            parent[expS[0]] = id
            id to M(id, l = expS[0], r = expS[2], op = expS[1])
        }
    }
}

private val parent = HashMap<String, String>()
private var monkeys = input()
private fun part1(): Long {
    return calc("root")
}

private fun calc(id: String): Long {
    val m = monkeys[id]!!
    return m.const ?: m.calc(calc(m.l!!), calc(m.r!!))
}

private fun part2(): Long {
    return calculateHumn("humn")
}

private fun calculateHumn(id: String): Long {
    val p = parent[id]!!
    val pM = monkeys[p]!!
    val left = pM.l!! == id
    val other = if (left) calc(pM.r!!) else calc(pM.l!!)
    return if (p == "root") other else pM.calcInv(calculateHumn(p), other, left)
}

fun main() {
    measure { part1() }
    measure { part2() }
}

private data class M(
    val id: String,
    val const: Long? = null,
    val l: String? = null,
    val r: String? = null,
    val op: String? = null
) {
    fun calc(a: Long, b: Long): Long {
        return when (op) {
            "+" -> a + b
            "-" -> a - b
            "*" -> a * b
            "/" -> a / b
            else -> -1
        }
    }

    fun calcInv(value: Long, other: Long, left: Boolean): Long {
        return when (op) {
            "+" -> value - other
            "-" -> if (left) value + other else other - value
            "*" -> value / other
            "/" -> if (left) value * other else other / value
            else -> -1
        }
    }
}
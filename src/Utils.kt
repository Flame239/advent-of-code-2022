import java.io.File
import kotlin.math.abs
import kotlin.math.min
import kotlin.system.measureNanoTime

fun readInput(name: String) = File("input", "$name.txt").readLines()

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

infix fun Int.towardExclusiveFrom(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this + step, to, step)
}

fun String.sortAlphabetically() = String(toCharArray().apply { sort() })

fun <T> List<T>.orderedPairs(): Sequence<Pair<T, T>> = sequence {
    for (i in 0 until size - 1) {
        for (j in 0 until size - 1) {
            if (i == j) continue
            yield(get(i) to get(j))
        }
    }
}

fun ClosedRange<Int>.intersect(other: ClosedRange<Int>) = !(start > other.endInclusive || endInclusive < other.start)

fun ClosedRange<Int>.contains(other: ClosedRange<Int>) = other.start >= start && other.endInclusive <= endInclusive

fun ClosedRange<Int>.count() = endInclusive - start + 1

fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
}

fun lcm(a: Int, b: Int): Int {
    return a / gcd(a, b) * b
}

fun lcmList(input: List<Int>): Int {
    return input.reduce { a, b -> lcm(a, b) }
}

fun List<Int>.lcm() = lcmList(this)

data class C(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x;$y)"
    }

    fun manhattan(other: C): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}

fun parseC(s: String): C = s.split(", ").map { it.substring(2) }.map { it.toInt() }.let { (x, y) -> C(x, y) }

fun measure(block: () -> Any) {
    var min: Long = Long.MAX_VALUE
    var result: Any = ""
    repeat(100) {
        measureNanoTime {
            result = block()
        }.also { min = min(min, it) }
    }
    println(result)
    println("Exec time: " + min / 1000 + " μs")
}
import java.io.File

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

//infix fun Int.mod(other: Int): Int = this.mod(other)
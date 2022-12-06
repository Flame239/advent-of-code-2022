private fun s(): String {
    return readInput("Day06")[0]
}

private fun slidingWindowNoDups(s: String, w: Int): Int {
    var dups = 0
    val count = IntArray(27)
    for (i in 0 until w) {
        if (++count[s[i] - 'a'] == 2) dups++
    }
    for (i in w until s.length) {
        if (dups == 0) {
            return i
        }
        if (++count[s[i] - 'a'] == 2) dups++
        if (--count[s[i - w] - 'a'] == 1) dups--

    }
    return if (dups == 0) s.length else -1
}

private fun part1(s: String): Int = slidingWindowNoDups(s, 4)
private fun part2(s: String): Int = slidingWindowNoDups(s, 14)

fun main() {
    println(part1(s()))
    println(part2(s()))
}


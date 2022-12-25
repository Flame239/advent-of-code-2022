private fun nums(): List<String> {
    return readInput("Day25")
}

val snafuDigit = mapOf(
    '2' to 2,
    '1' to 1,
    '0' to 0,
    '-' to -1,
    '=' to -2
)

private fun snafu2Dec(n: String): Long {
    var base = 1L
    var res = 0L
    n.reversed().forEach { c ->
        res += base * snafuDigit[c]!!
        base *= 5
    }
    return res
}

private fun dec2Snafu(num: Long): String {
    return buildString {
        var n = num
        while (n != 0L) {
            append("012=-"[n.mod(5)])
            // (n + 2) because if we got rem 3 or 4 (= and -), then we need next digit increased by 1
            n = (n + 2).floorDiv(5)
        }
        reverse()
    }
}

private fun part1(nums: List<String>): String = dec2Snafu(nums.sumOf(::snafu2Dec))

fun main() {
    measure { part1(nums()) }
}

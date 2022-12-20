import kotlin.math.abs
import kotlin.math.sign

private fun nums(): List<Long> = readInput("Day20").map { it.toLong() }

private fun decrypt(nums: List<Long>, mult: Long = 1, times: Int = 1): Long {
    val n = nums.size
    val order = nums.mapIndexed { i, v -> V(v * mult, i) }
    val a = order.toMutableList()
    repeat(times) {
        order.forEach { (v, i) ->
            var diff = (v % (n - 1)).toInt()
            if (i + diff < 0) diff = n + diff - 1
            if (i + diff >= n - 1) diff = diff - n + 1

            val ds = diff.sign
            repeat(abs(diff)) {
                val j = i + it * ds
                a.swap(j, j + ds)
                a[j].ind = j
                a[j + ds].ind = j + ds
            }
        }
    }
    val zeroInd = a.indexOfFirst { it.v == 0L }
    return a[(1000 + zeroInd) % n].v + a[(2000 + zeroInd) % n].v + a[(3000 + zeroInd) % n].v
}

private fun part1(nums: List<Long>): Long = decrypt(nums)
private fun part2(nums: List<Long>): Long = decrypt(nums, 811589153, 10)

fun main() {
    measure { part1(nums()) }
    measure { part2(nums()) }
}

private data class V(val v: Long, var ind: Int)
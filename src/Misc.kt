import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureNanoTime

fun readInput(name: String) = File("input", "$name.txt").readLines()


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

fun parseC(s: String): C = s.split(", ").map { it.substring(2) }.map { it.toInt() }.let { (x, y) -> C(x, y) }

private val red = "\u001b[0;31m"
private val green = "\u001b[32m"
private val reset = "\u001b[0m"
fun measure(block: () -> Any) {
    var min: Long = Long.MAX_VALUE
    var result: Any = ""
    repeat(1) {
        measureNanoTime {
            result = block()
        }.also { min = min(min, it) }
    }
    println(red + result + reset)
    println(green + "⏳ " + min / 1000000 + " ms" + reset)
}


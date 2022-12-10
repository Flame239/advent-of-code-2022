import kotlin.math.abs

private fun instructions(): List<Instruction> {
    return readInput("Day10").map { if (it == "noop") Instruction(0, 1) else Instruction(it.split(" ")[1].toInt(), 2) }
}

private fun part1(instructions: List<Instruction>): Int {
    val cyclesToCount = ArrayDeque(listOf(20, 60, 100, 140, 180, 220))
    var sum = 0
    var cycle = 0
    var x = 1
    instructions.forEach { i ->
        if (cyclesToCount.first() <= cycle + i.cycles) {
            sum += x * cyclesToCount.first();
            cyclesToCount.removeFirst();
            if (cyclesToCount.isEmpty()) return sum
        }
        if (i.cycles == 2) {
            x += i.add
        }
        cycle += i.cycles
    }
    return sum
}

private fun part2(instructions: List<Instruction>) {
    val screen = BooleanArray(240)
    var cycle = 0
    var x = 1
    instructions.forEach { i ->
        if (i.cycles == 1) {
            cycle += 1
            draw(cycle, x, screen)
        }
        if (i.cycles == 2) {
            cycle += 1
            draw(cycle, x, screen)
            cycle += 1
            draw(cycle, x, screen)
            x += i.add
        }
    }

    for (i in 0 until 6) {
        for (j in 0 until 40) {
            print(if (screen[40 * i + j]) "█" else " ")
        }
        println()
    }
}

private fun draw(cycle: Int, x: Int, screen: BooleanArray) {
    if (abs((cycle - 1) % 40 - x) <= 1) {
        screen[cycle - 1] = true
    }
}

fun main() {
    println(part1(instructions()))
    part2(instructions())
}

data class Instruction(val add: Int, val cycles: Int)
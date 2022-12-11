private fun monkeys(): List<Monkey> {
    return readInput("Day11").filter { it.isNotBlank() }.map(String::trim).chunked(6).mapIndexed { i, s ->
        val items = s[1].substring("Starting items: ".length).split(", ").map(String::toLong).toMutableList()
        val ops = s[2].substring("Operation: new = old ".length).split(" ")
        val op = when (ops[0]) {
            "+" -> { x: Long -> x + if (ops[1] == "old") x else ops[1].toLong() }
            "*" -> { x: Long -> x * if (ops[1] == "old") x else ops[1].toLong() }
            else -> error("unknown operator")
        }
        val div = s[3].substring("Test: divisible by ".length).toInt()
        val t = s[4].substring("If true: throw to monkey ".length).toInt()
        val f = s[5].substring("If false: throw to monkey ".length).toInt()
        Monkey(i, items, op, div, t, f)
    }
}

private fun part1(monkeys: List<Monkey>): Long {
    val totalInspections = LongArray(monkeys.size)
    repeat(20) {
        for (m in monkeys) {
            for (i in m.items) {
                val newWorry = m.operation(i) / 3
                monkeys[m.test(newWorry)].items.add(newWorry)
            }
            totalInspections[m.index] += m.items.size.toLong()
            m.items.clear()
        }
    }
    return totalInspections.sortedDescending().take(2).reduce(Long::times)
}

private fun part2(monkeys: List<Monkey>): Long {
    val divLCM = monkeys.map(Monkey::testDiv).lcm()
    val totalInspections = LongArray(monkeys.size)
    repeat(10000) {
        for (m in monkeys) {
            for (i in m.items) {
                val newWorry = m.operation(i) % divLCM
                monkeys[m.test(newWorry)].items.add(newWorry)
            }
            totalInspections[m.index] += m.items.size.toLong()
            m.items.clear()
        }
    }
    return totalInspections.sortedDescending().take(2).reduce(Long::times)
}

fun main() {
    println(part1(monkeys()))
    println(part2(monkeys()))
}

data class Monkey(
    val index: Int,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val testDiv: Int,
    val testTrue: Int,
    val testFalse: Int
) {
    fun test(x: Long) = if (x % testDiv == 0L) testTrue else testFalse
}
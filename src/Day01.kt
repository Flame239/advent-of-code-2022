fun elfsFood(): MutableList<MutableList<Int>> {
    val res = mutableListOf<MutableList<Int>>(mutableListOf())
    for (line in readInput("Day01")) {
        if (line.isEmpty()) {
            res.add(mutableListOf())
        } else {
            res.last().add(line.toInt())
        }
    }
    return res
}

fun part1(elfsFood: List<List<Int>>): Int = elfsFood.maxOf { list -> list.sum() }

fun part2(elfsFood: List<List<Int>>): Int = elfsFood.map { l -> l.sum() }.sortedDescending().take(3).sum()

fun main() {
    println(part1(elfsFood()))
    println(part2(elfsFood()))
}

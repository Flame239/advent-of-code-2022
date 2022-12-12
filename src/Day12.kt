private fun map(): List<CharArray> {
    return readInput("Day12").map { it.toCharArray() }
}

private fun part1(map: List<CharArray>): Int {
    val n = map.size
    val m = map[0].size
    val finish = find(map, 'E')
    map[finish.y][finish.x] = 'z'
    val start = find(map, 'S')
    map[start.y][start.x] = 'a'
    val visited = Array(n) { BooleanArray(m) }
    var q = HashSet<C>()
    q.add(start)
    var curStep = 0

    while (q.isNotEmpty()) {
        val newQ = HashSet<C>()
        q.forEach { cur ->
            if (cur == finish) return curStep
            visited[cur.y][cur.x] = true
            val curC = map[cur.y][cur.x]

            val left = C(cur.x - 1, cur.y)
            if (check(left, n, m, visited) && (curC + 1 >= map[left.y][left.x])) {
                newQ.add(left)
            }
            val right = C(cur.x + 1, cur.y)
            if (check(right, n, m, visited) && (curC + 1 >= map[right.y][right.x])) {
                newQ.add(right)
            }
            val up = C(cur.x, cur.y + 1)
            if (check(up, n, m, visited) && (curC + 1 >= map[up.y][up.x])) {
                newQ.add(up)
            }
            val down = C(cur.x, cur.y - 1)
            if (check(down, n, m, visited) && (curC + 1 >= map[down.y][down.x])) {
                newQ.add(down)
            }
        }
        q = newQ
        curStep++
    }

    return -1
}

private fun check(coord: C, n: Int, m: Int, visited: Array<BooleanArray>): Boolean {
    val withinBounds = (coord.x in 0 until m) && (coord.y in 0 until n)
    return withinBounds && !visited[coord.y][coord.x]
}

private fun find(map: List<CharArray>, c: Char): C {
    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == c) {
                return C(j, i)
            }
        }
    }
    return C(-1, -1)
}

private fun part2(map: List<CharArray>): Int {
    val n = map.size
    val m = map[0].size
    val start = find(map, 'E')
    map[start.y][start.x] = 'z'
    val visited = Array(n) { BooleanArray(m) }
    var q = HashSet<C>()
    q.add(start)
    var curStep = 0

    while (q.isNotEmpty()) {
        val newQ = HashSet<C>()
        q.forEach { cur ->
            val curC = map[cur.y][cur.x]
            if (curC == 'a') return curStep
            visited[cur.y][cur.x] = true

            val left = C(cur.x - 1, cur.y)
            if (check(left, n, m, visited) && (curC - 1 <= map[left.y][left.x])) {
                newQ.add(left)
            }
            val right = C(cur.x + 1, cur.y)
            if (check(right, n, m, visited) && (curC - 1 <= map[right.y][right.x])) {
                newQ.add(right)
            }
            val up = C(cur.x, cur.y + 1)
            if (check(up, n, m, visited) && (curC - 1 <= map[up.y][up.x])) {
                newQ.add(up)
            }
            val down = C(cur.x, cur.y - 1)
            if (check(down, n, m, visited) && (curC - 1 <= map[down.y][down.x])) {
                newQ.add(down)
            }
        }
        q = newQ
        curStep++
    }
    return -1
}

fun main() {
    println(part1(map()))
    println(part2(map()))
}

data class C(val x: Int, val y: Int)

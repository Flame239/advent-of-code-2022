import kotlin.math.abs

private fun pos(): List<Sensor> {
    return readInput("Day15").map { it.split(": ") }.map {
        val sensor = parseC(it[0].substring("Sensor at ".length))
        val beacon = parseC(it[1].substring("closest beacon is at ".length))
        Sensor(sensor, beacon, sensor.manhattan(beacon))
    }
}

// For each sensor we can calculate intersection segment of its area and given row.
// Then we take a length of union of those segments and subtract any beacons or sensors which happens to be in the union
private fun part1(sensors: List<Sensor>): Int {
    val row = 2000000
//    val row = 10
    val sSorted: List<IntRange> = sensors.filter { abs(it.sensor.y - row) <= it.dist }.map { s ->
        val diff = s.dist - abs(s.sensor.y - row)
        IntRange(s.sensor.x - diff, s.sensor.x + diff)
    }.sortedBy { it.first }

    val sUnion = mutableListOf<IntRange>()
    var start = sSorted[0].first
    var end = sSorted[0].last

    sSorted.forEach { s ->
        if (s.first > end) {
            sUnion.add(IntRange(start, end))
            start = s.first
            end = s.last
        }
        if (s.first <= end && s.last > end) {
            end = s.last
        }
    }
    sUnion.add(IntRange(start, end))

    var noBPos = sUnion.sumOf(IntRange::count)
    noBPos -= sensors.count { s -> s.sensor.y == row && sUnion.any { it.contains(s.sensor.x) } }
    noBPos -= sensors.map { s -> s.beacon }.toSet().count { b -> b.y == row && sUnion.any { it.contains(b.x) } }
    return noBPos
}

// Observation: point should be between 2 pairs of areas which are on a distance 1 of each other, like so
//     / * /
//    / * /
//   / * /
// and
//  \ * \
//   \ * \
//    \ * \
// their intersection gives exactly 1 possible point.
// We can calculate intersection point:
// knowing that one line has y = -x + b1 and another y = x + b2 => y = (b1 + b2) / 2, x = (b1 - b2) / 2
// b1 and b2 could be found simply from the points which we know are on the lines:
//    \
//     \  <- for example that point on the right angle with coords (sensor.x + 1 + dist; sensor.y)
//     / *<-/                                                   and then b2 = y - x
//    / *  /                                                 similar for b1 = x + y
//   / *  /

private fun part2(sensors: List<Sensor>): Long {
    val pairs: MutableList<Pair<Sensor, Sensor>> = mutableListOf()
    for (i in sensors.indices) {
        for (j in i + 1..sensors.lastIndex) {
            val centerDist = sensors[i].sensor.manhattan(sensors[j].sensor)
            val beaconDist = sensors[i].dist + sensors[j].dist
            if (centerDist - beaconDist == 2) pairs.add(Pair(sensors[i], sensors[j]))
        }
    }
    val xOrdPairs = pairs.map { (p1, p2) -> if (p1.sensor.x < p2.sensor.x) Pair(p1, p2) else Pair(p2, p1) }

    // y = -x + b1
    val leftDown = xOrdPairs.find { (p1, p2) -> (p1.sensor.y < p2.sensor.y) }!!.first
    val b1 = leftDown.sensor.x + leftDown.dist + 1 + leftDown.sensor.y

    // y = x + b2
    val leftUp = xOrdPairs.find { (p1, p2) -> (p1.sensor.y > p2.sensor.y) }!!.first
    val b2 = leftUp.sensor.y - (leftUp.sensor.x + leftUp.dist + 1)

    // intersection point
    val y = (b1 + b2) / 2
    val x = (b1 - b2) / 2

    return if (!withinRange(C(x, y), sensors)) x * 4000000L + y else -1
}

private fun withinRange(c: C, sensors: List<Sensor>): Boolean {
    sensors.forEach { s ->
        if (c.manhattan(s.sensor) <= s.dist) return true
    }
    return false
}

fun main() {
    measure { part1(pos()) }
    measure { part2(pos()) }
}

data class Sensor(val sensor: C, val beacon: C, val dist: Int)

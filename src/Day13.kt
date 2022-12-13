private fun packets(): List<Packet> {
    return readInput("Day13").filter { it.isNotBlank() }.map { parsePacket(it) }
}

private fun parsePacket(s: String): Packet = ListData().also { parseListData(it, s, 0) }

private fun parseListData(curList: ListData, s: String, start: Int): Int {
    var cur = start + 1
    while (s[cur] != ']') {
        if (s[cur].isDigit()) {
            var curInt = s[cur].digitToInt()
            cur++
            if (s[cur].isDigit()) {
                curInt = curInt * 10 + s[cur].digitToInt()
                cur++
            }
            curList.add(IntData(curInt))
        }
        if (s[cur] == '[') {
            val newList = ListData()
            curList.add(newList)
            cur = parseListData(newList, s, cur) + 1
        }
        if (s[cur] == ',') cur++
    }
    return cur
}

private fun part1(packets: List<Packet>): Int {
    return packets.chunked(2).withIndex().sumOf { (index, pair) -> if (pair[0] < pair[1]) index + 1 else 0 }
}

private fun part2(packets: List<Packet>): Int {
    val div1 = ListData(ListData(IntData(2)))
    val div2 = ListData(ListData(IntData(6)))
    val allPacketsSorted = (packets + div1 + div2).sorted()
    return (allPacketsSorted.indexOf(div1) + 1) * (allPacketsSorted.indexOf(div2) + 1)
}

fun main() {
    println(part1(packets()))
    println(part2(packets()))
}

typealias Packet = ListData

sealed interface PacketData : Comparable<PacketData> {
    override operator fun compareTo(other: PacketData): Int
}

data class IntData(val value: Int) : PacketData {
    override operator fun compareTo(other: PacketData): Int {
        return when (other) {
            is IntData -> value.compareTo(other.value)
            is ListData -> ListData(this).compareTo(other)
        }
    }
}

data class ListData(val list: MutableList<PacketData>) : PacketData {
    constructor() : this(mutableListOf())
    constructor(data: PacketData) : this(mutableListOf(data))

    fun add(data: PacketData) {
        list.add(data)
    }

    private fun compareToList(right: ListData): Int {
        var i = 0
        while (i < list.size && i < right.list.size) {
            val comp = list[i].compareTo(right.list[i])
            if (comp != 0) return comp
            i++
        }
        if (i == list.size && i < right.list.size) return -1
        if (i == right.list.size && i < list.size) return 1

        return 0
    }

    override operator fun compareTo(other: PacketData): Int {
        return when (other) {
            is IntData -> compareToList(ListData(other))
            is ListData -> compareToList(other)
        }
    }
}



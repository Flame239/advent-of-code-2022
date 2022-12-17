private fun input(): ValvesInput {
    val file = "Day16"
    val g: HashMap<Int, List<Edge>> = HashMap()
    val flows = IntArray(readInput(file).size)
    val idMap = readInput(file).mapIndexed { i, s ->
        (s.substring(
            "Valve ".length, "Valve ".length + 2
        ) to i).also { if (it.first == "AA") start = it.second }
    }.toMap()
    readInput(file).forEach { s ->
        val (valveS, tunnelS) = s.split(";")
        val id = idMap[valveS.substring("Valve ".length, "Valve ".length + 2)]!!
        val flow = valveS.substring("Valve AA has flow rate=".length).toInt()
        // input could contain `tunnel lead to valve` instead, but I just replaced it with plural
        val paths = tunnelS.substring(" tunnels lead to valves ".length).split(", ")
        g[id] = paths.map { Edge(idMap[it]!!, 1) }
        flows[id] = flow
    }
    // pre-calculate bitmask state with all open valves
    flows.withIndex().filter { (_, f) -> f > 0 }.forEach { allOpen = setOpened(allOpen, it.index) }

    // pre-calculate bitmask -> closed valves flow sum
    fun closedFlowSum(curFlowSum: Int, i: Int, curOpened: Long) {
        if (i >= flows.size) {
            closedFlowSum[curOpened] = curFlowSum
            return
        }
        if (flows[i] > 0) {
            closedFlowSum(curFlowSum - flows[i], i + 1, setOpened(curOpened, i))
        }
        closedFlowSum(curFlowSum, i + 1, curOpened)
    }
    closedFlowSum(flows.filter { f -> f > 0 }.sum(), 0, 0)

    return ValvesInput(g, flows)
}

private var start = -1
private var maxRelease = 0
private var allOpen = 0L
private var closedFlowSum = HashMap<Long, Int>()

private fun part1(input: ValvesInput): Int {
    dfs(input.g, input.flows, start, 0L, 30, 0)
    return maxRelease
}

private var cache = HashMap<CacheEntry, Int>()
private fun dfs(g: HashMap<Int, List<Edge>>, flows: IntArray, cur: Int, opened: Long, timeLeft: Int, releaseSum: Int) {
    // if run out of time or nothing left to open - stop
    if (timeLeft <= 0 || opened == allOpen) {
        maxRelease = maxRelease.getMax(releaseSum)
        return
    }

    // if we have already been in current state but didn't improve - stop
    val cacheE = CacheEntry(opened, cur, timeLeft)
    val cached = cache[cacheE]
    if (cached != null && cached >= releaseSum) return
    cache[cacheE] = releaseSum

    val curFlow = flows[cur]
    val shouldOpen = curFlow > 0 && !isOpened(opened, cur)
    val openedCur = setOpened(opened, cur)
    g[cur]!!.forEach {
        if (shouldOpen) {
            dfs(g, flows, it.v, openedCur, timeLeft - 1 - 1, releaseSum + (timeLeft - 1) * curFlow)
        }
        dfs(g, flows, it.v, opened, timeLeft - 1, releaseSum)
    }
}

// bitmask manipulation to keep track of opened valves
fun isOpened(n: Long, i: Int): Boolean = ((n shr i) and 1) == 1L
fun setOpened(n: Long, i: Int): Long = (n or (1L shl i))

private fun part2(input: ValvesInput): Int {
    maxRelease = 0
    bigDfs(input.g, input.flows, start, start, 0L, 26, 0)
    return maxRelease
}

private var bigCache = HashMap<BigCacheEntry, Int>()
private fun bigDfs(
    g: HashMap<Int, List<Edge>>, flows: IntArray, curMe: Int, curEl: Int, opened: Long, timeLeft: Int, releaseSum: Int
) {
    if (timeLeft <= 0 || opened == allOpen) {
        maxRelease = maxRelease.getMax(releaseSum)
        return
    }

    // if after opening all closed valves instantly we cant do better - stop
    if (closedFlowSum[opened]!! * (timeLeft - 1) + releaseSum <= maxRelease) {
        return
    }

    // since it doesn't matter who exactly in which position, cache both (me, el) and (el, me)
    val cacheE = BigCacheEntry(opened, curMe, curEl, timeLeft)
    val cached = bigCache[cacheE]
    if (cached != null && cached >= releaseSum) return
    bigCache[cacheE] = releaseSum
    bigCache[BigCacheEntry(opened, curEl, curMe, timeLeft)] = releaseSum

    val curFlowMe = flows[curMe]
    val makeSenseToOpenMe = curFlowMe > 0 && !isOpened(opened, curMe)
    val openedMe = setOpened(opened, curMe)
    val releaseMe = (timeLeft - 1) * curFlowMe

    val curFlowEl = flows[curEl]
    val makeSenseToOpenEl = curFlowEl > 0 && !isOpened(opened, curEl)
    val openedEl = setOpened(opened, curEl)
    val releaseEl = (timeLeft - 1) * curFlowEl

    if (makeSenseToOpenMe && makeSenseToOpenEl && curMe != curEl) {
        bigDfs(g, flows, curMe, curEl, openedMe or openedEl, timeLeft - 1, releaseSum + releaseMe + releaseEl)
    }
    if (makeSenseToOpenMe) {
        g[curEl]!!.forEach { el ->
            bigDfs(g, flows, curMe, el.v, openedMe, timeLeft - 1, releaseSum + releaseMe)
        }
    }
    if (makeSenseToOpenEl) {
        g[curMe]!!.forEach { me ->
            bigDfs(g, flows, me.v, curEl, openedEl, timeLeft - 1, releaseSum + releaseEl)
        }
    }
    g[curMe]!!.forEach { me ->
        g[curEl]!!.forEach { el ->
            bigDfs(g, flows, me.v, el.v, opened, timeLeft - 1, releaseSum)
        }
    }
}

fun main() {
    measure { part1(input()) }
    measure { part2(input()) }
}

data class BigCacheEntry(val opened: Long, val curMe: Int, val curEl: Int, val timeLeft: Int)
data class CacheEntry(val opened: Long, val cur: Int, val timeLeft: Int)
data class Edge(val v: Int, val w: Int)
data class ValvesInput(val g: HashMap<Int, List<Edge>>, val flows: IntArray)


// (V)-_(V) okay, dp would've been better after all
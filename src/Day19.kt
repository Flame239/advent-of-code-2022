import kotlin.math.max

private fun blueprints(): List<Blueprint> {
    return readInput("Day19").map {
        val bVals = Regex(
            "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. " +
                    "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
                    "Each geode robot costs (\\d+) ore and (\\d+) obsidian."
        ).matchEntire(it)!!.groupValues.drop(1).map { v -> v.toInt() }
        Blueprint(bVals[0], bVals[1], bVals[2], bVals[3], bVals[4], bVals[5], bVals[6])
    }
}

private var maxOreCost = 0
private var maxClayCost = 0
private var maxObsCost = 0
private var maxG = 0
private val tCache = HashMap<Timeless, Int>()
private val rCache = HashMap<ResourcelessState, Resources>()
private fun maxGeodes(b: Blueprint, time: Int): Int {
    maxG = 0
    rCache.clear()
    tCache.clear()
    maxOreCost = listOf(b.oreRCost, b.clayRCostOre, b.obsRCostOre, b.gRCostOre).max()
    maxClayCost = b.obsRCostClay
    maxObsCost = b.gRCostObs
    val initState = State(0, 0, 0, 0, 1, 0, 0, 0, time)
    rCache[initState.toRless()] = initState.toR()
    tCache[initState.toTimeless()] = initState.timeLeft
    val states = ArrayDeque<State>()
    states.add(initState)
    while (states.isNotEmpty()) {
        val s = states.removeFirst()
        processState(s, b, states)
    }
    return maxG
}

private fun processState(s: State, b: Blueprint, states: ArrayDeque<State>) {
    // potentially we will have at least s.geode + s.geodeRobots * s.timeLeft
    maxG = max(maxG, s.geode + s.geodeRobots * s.timeLeft)
    if (s.timeLeft == 0) {
        return
    }

    val newStates: List<State> = buildRobots(s, b).map { it.updateResAndTime(s) }
    newStates.forEach {
        // if we already visited state with same robots and at the same time, but with >= resources
        val rLess = it.toRless()
        val rc = rCache[rLess]
        val visMoreRes =
            rc != null && rc.ore >= it.ore && rc.clay >= it.clay && rc.obsidian >= it.obsidian && rc.geode >= it.geode

        // if we potentially can make more than maxG by building geode robot on every minute left
        val t = it.timeLeft
        val canImprove = (it.geode + it.geodeRobots * t + t * (t - 1) / 2) > maxG

        // if we already visited same state, but earlier
        val timeless = it.toTimeless()
        val tc = tCache[timeless]
        val visEarlier = (tc != null && tc >= it.timeLeft)

        if (!visMoreRes && !visEarlier && canImprove) {
            states.add(it)
            rCache[rLess] = it.toR()
            tCache[timeless] = it.timeLeft
        }
    }
}

private fun buildRobots(init: State, b: Blueprint): List<State> {
    val states = ArrayList<State>()
    val (ore, clay, obs, g, oreR, clayR, obsR, gR, timeLeft) = init
    // no need to wait if we can build any robot
    if (!(ore >= b.gRCostOre && obs >= b.gRCostObs && ore >= b.obsRCostOre && clay >= b.obsRCostClay && ore >= b.clayRCostOre && ore >= b.oreRCost)) {
        states.add(init)
    }

    // no need to build more robots than maxCost, because we will not be able to spend resources anyway
    // no need to build more robots if (timeLeft < (res / maxCost)), because enough resources already to build on each minute left
    if (ore >= b.gRCostOre && obs >= b.gRCostObs) {
        val newGR = State(ore - b.gRCostOre, clay, obs - b.gRCostObs, g, oreR, clayR, obsR, gR + 1, timeLeft)
        states.add(newGR)
    }
    if (ore >= b.obsRCostOre && clay >= b.obsRCostClay && (timeLeft > (init.obsidian / maxObsCost))) {
        val newObsR = State(ore - b.obsRCostOre, clay - b.obsRCostClay, obs, g, oreR, clayR, obsR + 1, gR, timeLeft)
        if (newObsR.obsidianRobots <= maxObsCost) states.add(newObsR)
    }
    if (ore >= b.clayRCostOre && (timeLeft > (init.clay / maxClayCost))) {
        val newClayR = State(ore - b.clayRCostOre, clay, obs, g, oreR, clayR + 1, obsR, gR, timeLeft)
        if (newClayR.clayRobots <= maxClayCost) states.add(newClayR)
    }
    if (ore >= b.oreRCost && (timeLeft > (init.ore / maxOreCost))) {
        val newOreR = State(ore - b.oreRCost, clay, obs, g, oreR + 1, clayR, obsR, gR, timeLeft)
        if (newOreR.oreRobots <= maxOreCost) states.add(newOreR)
    }
    return states
}

private fun part1(blueprints: List<Blueprint>): Int {
    return blueprints.sumOf { maxGeodes(it, 24) * it.id }
}

private fun part2(blueprints: List<Blueprint>): Int {
    return blueprints.take(3).map { maxGeodes(it, 32) }.reduce(Int::times)
}

fun main() {
    measure { part1(blueprints()) } // 1659
    measure { part2(blueprints()) } //6804
}

private data class Blueprint(
    val id: Int,
    val oreRCost: Int,
    val clayRCostOre: Int,
    val obsRCostOre: Int,
    val obsRCostClay: Int,
    val gRCostOre: Int,
    val gRCostObs: Int
)

private data class State(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int,
    val oreRobots: Int,
    val clayRobots: Int,
    val obsidianRobots: Int,
    val geodeRobots: Int,
    val timeLeft: Int
) {
    fun updateResAndTime(init: State): State = State(
        ore + init.oreRobots,
        clay + init.clayRobots,
        obsidian + init.obsidianRobots,
        geode + init.geodeRobots,
        oreRobots,
        clayRobots,
        obsidianRobots,
        geodeRobots,
        timeLeft - 1
    )

    fun toTimeless(): Timeless =
        Timeless(ore, clay, obsidian, geode, oreRobots, clayRobots, obsidianRobots, geodeRobots)

    fun toR(): Resources =
        Resources(ore, clay, obsidian, geode)

    fun toRless(): ResourcelessState =
        ResourcelessState(oreRobots, clayRobots, obsidianRobots, geodeRobots, timeLeft)
}

private data class ResourcelessState(
    val oreRobots: Int,
    val clayRobots: Int,
    val obsidianRobots: Int,
    val geodeRobots: Int,
    val timeLeft: Int
)

private data class Timeless(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int,
    val oreRobots: Int,
    val clayRobots: Int,
    val obsidianRobots: Int,
    val geodeRobots: Int,
)

private data class Resources(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int
)
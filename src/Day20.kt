
fun main() {

    val day = "20"

    abstract class Module(open val id: String, open val targets: List<String>)

    data class FlipFlop(
        override val id: String,
        override val targets: List<String>,
        var on: Boolean = false
    ) : Module(id, targets)

    data class Conjunction(
        override val id: String,
        override val targets: List<String>,
        var inputs: MutableMap<String, Boolean> = mutableMapOf()
    ) : Module(id, targets)

    data class Broadcaster(override val id: String, override val targets: List<String>): Module(id, targets)

    data class Transmission(val source: String, val target: String, val pulse: Boolean)

    fun getModule(id: String, targets: List<String>): Module {
        return when (id.first()) {
            '%' -> FlipFlop(id.drop(1), targets)
            '&' -> Conjunction(id.drop(1), targets)
            else -> Broadcaster(id, targets)
        }
    }

    fun parseInput(input: List<String>): Map<String, Module> {
        val modules = input
            .map { it.split(" -> ") }
            .map { (module, targets) -> getModule(module, targets.split(",").map { it.trim() }) }
            .associateBy { it.id }

        for ((id, m) in modules.filter { (_, v) -> v is Conjunction }) {
            for (otherModule in modules.values) {
                if (id in otherModule.targets) {
                    (m as Conjunction).inputs[otherModule.id] = false
                }
            }
        }

        return modules
    }

    fun transmit(
        source: String,
        currentModule: Module,
        pulse: Boolean,
        queue: ArrayDeque<Transmission>
    ) {
        when (currentModule) {
            is FlipFlop -> {
                if (!pulse) {
                    if (!currentModule.on) {
                        currentModule.on = true
                        for (target in currentModule.targets) {
                            queue.addLast(Transmission(currentModule.id, target, true))
                        }
                    } else {
                        currentModule.on = false
                        for (target in currentModule.targets) {
                            queue.addLast(Transmission(currentModule.id, target, false))
                        }
                    }
                }
            }

            is Conjunction -> {
                currentModule.inputs[source] = pulse
                val allHigh = currentModule.inputs.values.reduce { a, b -> a && b }
                for (target in currentModule.targets) {
                    queue.addLast(Transmission(currentModule.id, target, !allHigh))
                }
            }

            is Broadcaster -> for (target in currentModule.targets) {
                queue.addLast(Transmission(currentModule.id, target, pulse))
            }

            else -> error(currentModule)
        }
    }

    fun transmitSignal(signal: Boolean, modules: Map<String, Module>, startModule: String): Pair<Int, Int> {
        var highCount = 0
        var lowCount = 0

        val queue = ArrayDeque<Transmission>()
        queue.addLast(Transmission("button", startModule, signal))

        transmission@ while (queue.isNotEmpty()) {
            val (source, target, pulse) = queue.removeFirst()
            if (pulse) highCount++ else lowCount++
            val currentModule = modules[target] ?: continue@transmission
            transmit(source, currentModule, pulse, queue)
        }

        return highCount to lowCount
    }

    fun part1(input: List<String>): Int {
        val modules = parseInput(input)

        var highCount = 0
        var lowCount = 0

        repeat (1000) {
            val (hi, lo) = transmitSignal(false, modules, "broadcaster")
            highCount += hi
            lowCount += lo
        }

        return highCount * lowCount
    }

    fun findCycles(signal: Boolean, modules: Map<String, Module>, endModuleId: String): Map<String, Long> {

        val firstTimeHigh = mutableMapOf<String, Long>()
        val cycles = mutableMapOf<String, Long>()
        val endModule = modules[endModuleId]!! as Conjunction
        val queue = ArrayDeque<Transmission>()

        buttonPresses@ for (n in generateSequence(0L) { it + 1 }) {
            queue.addLast(Transmission("button", "broadcaster", signal))
            while (queue.isNotEmpty()) {
                val (source, target, pulse) = queue.removeFirst()
                val targetModule = modules[target]
                targetModule?.let {
                    transmit(source, it, pulse, queue)
                }
                // endModule is a Conjunction, so each of its inputs must be high in order for it to output low
                if ((target == endModuleId) && pulse) {
                    if (source !in cycles) {
                        if (source in firstTimeHigh) {
                            cycles[source] = n - firstTimeHigh[source]!!
                            if (endModule.inputs.size == cycles.size) break@buttonPresses
                        } else {
                            firstTimeHigh[source] = n
                        }
                    }
                }
            }
        }

        return cycles
    }

    fun part2(input: List<String>): Long {
        val modules = parseInput(input)
        val endModule = modules.filterValues { "rx" in it.targets }.keys.first()
        return findCycles(false, modules, endModule).map { it.value }.lcm()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    val testInput2 = readInput("Day${day}_test2")
    check(part1(testInput) == 32000000)
    check(part1(testInput2) == 11687500)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
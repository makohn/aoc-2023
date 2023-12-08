
fun main() {

    val day = "08"

    fun part1(input: List<String>): Int {
        val instructions = input.first()
        val paths = input
            .drop(2)
            .map { Regex("[A-Z][A-Z][A-Z]").findAll(it).map { it.value } }
            .associate { seq ->
                val (a, b, c) = seq.toList()
                a to (b to c)
            }

        var steps = 0
        var current = "AAA"

        while (true) {
            val instruction = instructions[steps%instructions.length]
            if (current == "ZZZ") break
            steps++
            val path = paths[current]!!
            current = if (instruction == 'L') path.first else path.second
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instructions = input.first()
        val paths = input
            .drop(2)
            .map { Regex("[A-Z0-9][A-Z0-9][A-Z0-9]").findAll(it).map { it.value } }
            .associate { seq ->
                val (a, b, c) = seq.toList()
                a to (b to c)
            }

        var steps = 0L
        val stepsPerPath = mutableMapOf<Int, Long>()
        var currentNodes = paths.keys.filter { it.endsWith('A') }

        while (true) {
            val nextNodes = mutableListOf<String>()
            for ((i, current) in currentNodes.withIndex()) {
                val pos = (steps.toInt())%instructions.length
                val instruction = instructions[pos]
                val path = paths[current]!!
                val nextNode = if (instruction == 'L') path.first else path.second
                if (nextNode.endsWith('Z')) {
                    stepsPerPath[i] = steps + 1
                    if (stepsPerPath.size == currentNodes.size) {
                        return stepsPerPath.values.lcm()
                    }
                }
                nextNodes.add(nextNode)
            }
            currentNodes = nextNodes
            steps++
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    val testInput2 = readInput("Day${day}_test2")
    val testInput3 = readInput("Day${day}_test3")
    check(part1(testInput) == 2)
    check(part1(testInput2) == 6)
    check(part2(testInput3) == 6L)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
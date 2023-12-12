
fun main() {

    val day = "12"

    fun checkPattern(str: String, distribution: List<Int>): Boolean {
        val actualDistribution = mutableListOf<Int>()
        var counter = 0
        for (c in str) {
            when (c) {
                '.' -> {
                    if (counter > 0) {
                        actualDistribution.add(counter)
                        counter = 0
                    }
                }
                '#' -> counter++
            }
        }
        if (counter > 0) {
            actualDistribution.add(counter)
        }
        return actualDistribution == distribution
    }

    fun countPossibilities(str: String, distribution: List<Int>, pos: Int): Int {
        if (pos == str.length) return if (checkPattern(str, distribution)) 1 else 0
        return when (str[pos]) {
            '?' -> "#.".sumOf { countPossibilities(str.substring(0..pos) + it + str.substring(pos+1), distribution, pos+1) }
            else -> countPossibilities(str, distribution, pos+1)
        }
    }

    fun part1(input: List<String>): Int {
        return input
            .map { it.split(" ") }
            .map { (a, b) -> a to b.split(",").map { it.toInt() } }
            .sumOf { (str, dist) -> countPossibilities(str, dist, 0) }
    }

    data class State(val strPos: Int, val distPos: Int, val blockLen: Int)
    val memoize = HashMap<State, Long>()

    fun countPossibilities(str: String, distribution: List<Int>, state: State): Long {
        if (state in memoize) return memoize[state]!!
        val (strPos, distPos, blockLen) = state
        if (strPos == str.length) return when {
            distPos == distribution.size && blockLen == 0 -> 1
            distPos == distribution.size-1 && blockLen == distribution[distPos] -> 1
            else -> 0
        }
        var count = 0L
        ".#".forEach { s ->
            val char = str[strPos]
            if (char == s || char == '?') count += when {
                (s == '.' && (blockLen == 0)) -> {
                    countPossibilities(str, distribution, State(strPos+1, distPos, 0))
                }
                (s == '.' && (blockLen > 0) && (distPos < distribution.size) && (blockLen == distribution[distPos])) -> {
                    countPossibilities(str, distribution, State(strPos+1, distPos+1, 0))
                }
                (s == '#') -> {
                    countPossibilities(str, distribution, State(strPos+1, distPos, blockLen+1))
                }
                else -> 0
            }
        }
        memoize[state] = count
        return count
    }

    fun part2(input: List<String>): Long {
        return input
            .asSequence()
            .map { it.split(" ") }
            .map { (a, b) -> a to b.split(",").map { it.toInt() } }
            .map { (a, b) -> listOf(a, a, a, a, a).joinToString("?") to listOf(b, b, b, b, b).flatten() }
            .onEach { memoize.clear() }
            .sumOf { (str, dist) -> countPossibilities(str, dist, State(0, 0, 0)) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152L)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
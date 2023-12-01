
fun main() {

    val day = "01"

    val digits = mapOf(
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9"
    )

    fun String.replace(mapping: Map<String, String>): String {
        var str = this
        mapping.forEach { str = str.replace(it.key, it.key.first() + it.value + it.key.last()) }
        return str
    }

    fun part1(input: List<String>) = input
        .map { Regex("\\d").findAll(it).map { it.value } }
        .sumOf { (it.first() + it.last()).toInt() }

    fun part2(input: List<String>) = input
        .map { it.replace(digits) }
        .map { Regex("\\d").findAll(it).map { it.value } }
        .sumOf { (it.first() + it.last()).toInt() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    val testInput2 = readInput("Day${day}_test2")
    check(part1(testInput) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
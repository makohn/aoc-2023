
fun main() {

    val day = "15"

    fun hash(str: String): Int {
        var curVal = 0
        for (c in str) {
            curVal += c.code
            curVal *= 17
            curVal %= 256
        }
        return curVal
    }

    fun part1(input: List<String>): Int {
        val sequence = input.first().split(",")
        val ans = sequence.sumOf { hash(it) }
        return ans
    }

    data class Lens(val label: String, val focalLength: Int) {
        override fun toString(): String {
            return "[$label $focalLength]"
        }
    }

    fun part2(input: List<String>): Int {
        val sequence = input.first().split(",")
        val boxes = Array(256) { mutableListOf<Lens>() }
        for (str in sequence) {
            val (label, opChar, focal) = Regex("([a-z]+)([-=])(\\d?)")
                .findAll(str)
                .flatMap { it.groupValues }
                .drop(1)
                .toList()
            val hash = hash(label)
            val focalLen = if (focal != "") focal.toInt() else 0
            when (opChar) {
                "-" -> boxes[hash].removeIf { it.label == label }
                "=" -> {
                    if (boxes[hash].any { it.label == label }) {
                        boxes[hash].replaceIf(Lens(label, focalLen)) { it.label == label }
                    } else {
                        boxes[hash].add(Lens(label, focalLen))
                    }
                }
            }
        }
        var ans = 0
        for ((i, box) in boxes.withIndex()) {
            for ((j, lens) in box.withIndex()) {
                ans += (i+1)*(j+1)*lens.focalLength
            }
        }
        return ans
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
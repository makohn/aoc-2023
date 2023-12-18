
fun main() {

    val day = "18"

    data class Instruction<T>(val direction: String, val amount: T)
    data class Pos<T>(val x: T, val y: T)

    fun part1(input: List<String>): Int {
        val instructions = input
            .map { it.split(" ") }
            .map { (d, a, _) -> Instruction(d, a.toInt()) }

        val vertices = mutableListOf<Pos<Int>>()
        var currentPos = Pos(0, 0)
        vertices.add(currentPos)
        for (i in instructions) {
            currentPos = when (i.direction) {
                "R" -> Pos(currentPos.x + i.amount, currentPos.y)
                "U" -> Pos(currentPos.x, currentPos.y - i.amount)
                "L" -> Pos(currentPos.x - i.amount, currentPos.y)
                "D" -> Pos(currentPos.x, currentPos.y + i.amount)
                else -> error("Undefined")
            }
            vertices.add(currentPos)
        }

        val a = vertices
            .zipWithNext()
            .sumOf { (a, b) -> a.x * b.y - a.y * b.x }

        val b = instructions.sumOf { it.amount }/2 + 1
        val c = a/2 + b

        return c
    }

    fun decode(str: String): Pair<String, Long> {
        val input = str.removeSurrounding("(", ")")
        val hex = input.removePrefix("#").dropLast(1)
        val amount = hex.toLong(radix = 16)
        val direction = when (input.last()) {
            '0' -> "R"
            '1' -> "D"
            '2' -> "L"
            '3' -> "U"
            else -> ""
        }
        return direction to amount
    }

    fun part2(input: List<String>): Long {
        val instructions = input
            .map { it.split(" ") }
            .map { (_, _, c) -> decode(c) }
            .map { (d, a) -> Instruction(d, a) }

        val vertices = mutableListOf<Pos<Long>>()
        var currentPos = Pos(0L, 0L)
        vertices.add(currentPos)
        for (i in instructions) {
            currentPos = when (i.direction) {
                "R" -> Pos(currentPos.x + i.amount, currentPos.y)
                "U" -> Pos(currentPos.x, currentPos.y - i.amount)
                "L" -> Pos(currentPos.x - i.amount, currentPos.y)
                "D" -> Pos(currentPos.x, currentPos.y + i.amount)
                else -> error("Undefined")
            }
            vertices.add(currentPos)
        }

        val a = vertices
            .zipWithNext()
            .sumOf { (a, b) -> a.x * b.y - a.y * b.x }

        val b = instructions.sumOf { it.amount }/2 + 1
        val c = a/2 + b

        return c
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 62)
    check(part2(testInput) == 952408144115)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
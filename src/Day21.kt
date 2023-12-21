
fun main() {

    val day = "21"

    val directions = arrayOf(Direction.North, Direction.East, Direction.South, Direction.West)

    fun countReachableCells(map: CharMatrix, vararg steps: Int, adjacentTo: (CharCell) -> List<CharCell>) = sequence {
        val charCells = map.flatMapIndexed { i, row -> row.mapIndexed{ j, char -> CharCell(i, j, char) }}
        val startCell = charCells.first { it.data == 'S' }

        val newCells = mutableSetOf<CharCell>()
        newCells.add(startCell)

        for (x in generateSequence(1) { it + 1 }) {
            val cells = newCells.flatMap {
                adjacentTo(it).filter { it.data != '#' }
            }
            newCells.clear()
            newCells.addAll(cells)
            if (x in steps) yield(newCells.size)
        }
    }

    fun part1(input: List<String>, steps: Int = 64): Int {
        val map = input.toCharMatrix()
        return countReachableCells(map, steps) {
            map.adjacentTo(it, *directions)
        }.take(1).first()
    }

    fun part2(input: List<String>, steps: Int): Long {
        val map = input.toCharMatrix()
        val (n, _) = map.dimension
        val x0 = steps % n
        val (a, b, c) = countReachableCells(map, x0, x0+1*n, x0+2*n) {
            map.adjacentToUnbound(it, *directions)
        }.take(3).map { it.toLong() }.toList()

        fun f(x: Long) = a + (b-a)*x + (x*(x-1)/2L) * ((c-b)-(b-a))

        val x = ((steps-x0)/n).toLong()
        return f(x)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput, steps = 6) == 16)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input, steps = 26501365))
}
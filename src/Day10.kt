
fun main() {

    val day = "10"

    fun getNeighbours(input: List<String>): Pair<CharCell, Map<CharCell, List<CharCell>>> {
        val map = input.toCharMatrix()
        val (n, m) = map.dimension

        lateinit var startCell: CharCell
        val neighbours = mutableMapOf<CharCell, List<CharCell>>()

        for (i in 0..<n) for (j in 0..<m) {
            val currentCell = CharCell(i, j, map[i][j])
            when (currentCell.data) {
                '|' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.North, Direction.South)
                '-' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.East, Direction.West)
                'L' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.North, Direction.East)
                'J' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.North, Direction.West)
                '7' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.South, Direction.West)
                'F' -> neighbours[currentCell] = map.adjacentTo(currentCell, Direction.South, Direction.East)
                'S' -> startCell = currentCell
            }
        }
        neighbours[startCell] = neighbours.filter { (_, v) -> v.any { it == startCell } }.keys.toList()
        return startCell to neighbours
    }

    fun getMainLoopWithDistance(startCell: CharCell, neighbours: Map<CharCell, List<CharCell>>): HashMap<CharCell, Int> {
        return bfs(startCell) { neighbours[it]!! }
    }

    fun part1(input: List<String>): Int {
        val (startCell, neighbours) = getNeighbours(input)
        return getMainLoopWithDistance(startCell, neighbours).maxBy { it.value }.value
    }

    fun part2(input: List<String>): Int {
        val (startCell, neighbours) = getNeighbours(input)
        val mainLoop = getMainLoopWithDistance(startCell, neighbours)
        val map = input.toCharMatrix()
        val cleanMap = map
            .mapIndexed { i, row ->
                row.mapIndexed { j, c -> CharCell(i, j, c) }.map {
                    if (it.data == 'S') {
                        val n = neighbours[it]!!.map { it.data }.toSet()
                        when {
                            n == setOf('-') || n == setOf('F', '7') || n == setOf('L', 'J') -> '-'
                            else -> '|'
                        }
                    }
                    else if (it in mainLoop) it.data else '.'
                }
            }

        var insideCount = 0

        for (row in cleanMap) {
            var parity = 0
            var previous = '.'
            for (char in row) {
                when (char) {
                    '.' -> if (parity%2 != 0) insideCount++
                    in "JLF7|" -> {
                        when(previous to char) {
                            'F' to 'J', 'L' to '7' -> Unit
                            else -> parity++
                        }
                        previous = char
                    }
                }
            }
        }
        return insideCount
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    val testInput2 = readInput("Day${day}_test2")
    val testInput3 = readInput("Day${day}_test3")
    val testInput4 = readInput("Day${day}_test4")
    val testInput5 = readInput("Day${day}_test5")
    check(part1(testInput) == 4)
    check(part1(testInput2) == 8)
    check(part2(testInput3) == 4)
    check(part2(testInput4) == 8)
    check(part2(testInput5) == 10)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
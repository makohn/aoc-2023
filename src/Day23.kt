import kotlin.math.max

fun main() {

    val day = "23"

    val directions = mapOf(
        '>' to arrayOf(Direction.East),
        '<' to arrayOf(Direction.West),
        'v' to arrayOf(Direction.South),
        '^' to arrayOf(Direction.North),
        '.' to arrayOf(Direction.North, Direction.East, Direction.South, Direction.West)
    )

    fun part1(input: List<String>): Int {
        val map = input.toCharMatrix()
        val (n, _) = map.dimension
        val startCell = map[0].mapIndexed { i, d -> CharCell(0, i, d) }.first { it.data != '#' }
        val endCell = map[n-1].mapIndexed { i, d -> CharCell(n-1, i, d) }.last { it.data != '#' }
        val visited = HashSet<CharCell>()

        fun dfs(node: CharCell): Int {
            if (node == endCell) return 0
            var ans = 0
            visited += node
            val neighbours = map
                .adjacentTo(node, *directions[node.data]!!)
                .filter { it.data != '#' }

            for (otherNode in neighbours) {
                if (otherNode !in visited) {
                    val pathLength = dfs(otherNode)
                    if (pathLength >= 0) ans = max(ans, pathLength + 1)
                }
            }
            visited -= node

            return ans
        }

        return dfs(startCell)
    }

    fun part2(input: List<String>): Int {
        val map = input.toCharMatrix()
        val (n, _) = map.dimension
        val charCells = map.toCharCells()
        val startCell = map[0].mapIndexed { i, d -> CharCell(0, i, d) }.first { it.data != '#' }
        val endCell = map[n-1].mapIndexed { i, d -> CharCell(n-1, i, d) }.last { it.data != '#' }

        val forks = charCells
            .filter { it.data != '#' }
            .map { it to map.adjacentTo(it, *directions['.']!!) }
            .filter { (_, neighbours) -> neighbours.count { it.data != '#' } >= 3 }
            .map { (fork, _) -> fork }
            .toTypedArray()

        val nodes = listOf(startCell, endCell, *forks)

        data class WeightedNode(val cell: CharCell, val weight: Int)

        val graph = HashMap<CharCell, HashSet<WeightedNode>>()
        for (node in nodes) {
            val stack = ArrayDeque<WeightedNode>()
            val visited = HashSet<CharCell>()
            val current = WeightedNode(node, 0)
            stack += current
            visited += current.cell

            while (stack.isNotEmpty()) {
                val otherNode = stack.removeLast()
                if (otherNode.weight != 0 && otherNode.cell in nodes) {
                    graph.computeIfAbsent(node) { HashSet() }
                    graph[node]!! += otherNode
                } else {
                    for (neighbour in map
                        .adjacentTo(otherNode.cell, *directions['.']!!)
                        .filter { it.data != '#' && it !in visited }) {
                        stack += WeightedNode(neighbour, otherNode.weight + 1)
                        visited += neighbour
                    }
                }
            }
        }

        val visited = HashSet<CharCell>()

        fun dfs(node: CharCell): Int {
            if (node == endCell) return 0
            var ans = -1
            visited += node
            val neighbours = graph[node]!!

            for (otherNode in neighbours) {
                if (otherNode.cell !in visited) {
                    val pathLength = dfs(otherNode.cell)
                    if (pathLength >= 0) ans = max(ans, pathLength + otherNode.weight)
                }
            }
            visited -= node

            return ans
        }

        return dfs(startCell)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
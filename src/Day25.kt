
fun main() {

    val day = "25"

    fun globalMinCut(mat: Array<IntArray>): Pair<Int, List<Int>> {
        var best = Int.MAX_VALUE to listOf<Int>()
        val n = mat.size
        val co = (0..<n).map { mutableListOf(it) }.toTypedArray()

        for (ph in 1..<n) {
            val w = mat[0].clone()
            var s = 0
            var t = 0
            for (it in 0..<(n - ph)) {
                w[t] = Int.MIN_VALUE
                s = t
                t = w.indexOf(w.max())
                for (i in 0..<n) w[i] += mat[t][i]
            }
            best = listOf(best, (w[t] - mat[t][t]) to co[t]).minBy { it.first }
            co[s].addAll(co[t])
            for (i in 0..<n) mat[s][i] += mat[t][i]
            for (i in 0..<n) mat[i][s] = mat[s][i]
            mat[0][t] = Int.MIN_VALUE
        }
        return best
    }

    fun part1(input: List<String>): Int {
        val edges = input
            .map { it.split(":").toList() }
            .flatMap { (k, v) -> v.trim().split(" ").map { setOf(k, it) } }
            .toSet()

        val nodes = edges.flatten().distinct().withIndex().associate { (i, v) -> v to i }
        val n = nodes.size
        val adj = Array(nodes.size) { IntArray(nodes.size) { 0 } }

        edges.map { it.first() to it.last() }.forEach { (s, t) ->
            adj[nodes[t]!!][nodes[s]!!]++
            adj[nodes[s]!!][nodes[t]!!]++
        }

        val (_, b) = globalMinCut(adj)
        return b.size * (n - b.size)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 54)

    val input = readInput("Day${day}")
    println(part1(input))
}
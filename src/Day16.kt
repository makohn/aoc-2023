
private data class Pos(val i: Int, val j: Int, val d: Dir)

private enum class Dir {
    Up,
    Right,
    Down,
    Left;

    fun apply(p: Pos) = when(this) {
        Up -> Pos(p.i-1, p.j, this)
        Right -> Pos(p.i, p.j+1, this)
        Left -> Pos(p.i, p.j-1, this)
        Down -> Pos(p.i+1, p.j, this)
    }

    // /
    // ^ -> >  U  R
    // > -> ^  R  U
    // v -> <  D  L
    // < -> v  L  D
    fun mirror90() = when(this) {
        Up -> Right
        Right -> Up
        Down -> Left
        Left -> Down
    }

    // \
    // ^ -> <  U  L
    // > -> v  R  D
    // v -> >  D  R
    // < -> ^  L  U
    fun mirrorMinus90() = when(this) {
        Up -> Left
        Right -> Down
        Down -> Right
        Left -> Up
    }
}

fun main() {

    val day = "16"

    fun findEnergizedTiles(map: CharMatrix, startPos: Pos): Int {
        val (n, m) = map.dimension
        val seen = HashSet<Pos>()
        val nextSteps = ArrayDeque<Pos>()
        nextSteps.addLast(startPos)
        while (nextSteps.isNotEmpty()) {
            val pos = nextSteps.removeFirst()
            val (i, j, d) = pos
            if (pos in seen || i !in 0..<n || j !in 0..<m) {
                continue
            }
            seen += pos
            val c = map[i][j]
            when (c) {
                '.' -> nextSteps.add(d.apply(pos))
                '|' -> {
                    if (d in listOf(Dir.Left, Dir.Right)) {
                        nextSteps += Dir.Up.apply(pos)
                        nextSteps += Dir.Down.apply(pos)
                    } else {
                        nextSteps.add(d.apply(pos))
                    }
                }
                '-' -> {
                    if (d in listOf(Dir.Up, Dir.Down)) {
                        nextSteps += Dir.Left.apply(pos)
                        nextSteps += Dir.Right.apply(pos)
                    } else {
                        nextSteps.add(d.apply(pos))
                    }
                }
                '\\' -> {
                    val newDir = d.mirrorMinus90()
                    nextSteps += newDir.apply(pos)
                }
                '/' -> {
                    val newDir = d.mirror90()
                    nextSteps += newDir.apply(pos)
                }
                else -> Unit
            }
        }

        return seen
            .map { it.i to it.j }
            .toSet()
            .count()
    }

    fun part1(input: List<String>): Int {
        val map = input.toCharMatrix()
        return findEnergizedTiles(map, Pos(0, 0, Dir.Right))
    }

    fun part2(input: List<String>): Int {
        val map = input.toCharMatrix()
        val (n, m) = map.dimension
        val startPositions = mutableListOf<Pos>()
        for (y in 0..<m) {
            startPositions.add(Pos(0, y, Dir.Down))
            startPositions.add(Pos(n-1, y, Dir.Up))
        }
        for (x in 0..<n) {
            startPositions.add(Pos(x, 0, Dir.Right))
            startPositions.add(Pos(x, m-1, Dir.Left))
        }

        return startPositions.maxOf { findEnergizedTiles(map, it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${day}_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)

    val input = readInput("Day${day}")
    println(part1(input))
    println(part2(input))
}
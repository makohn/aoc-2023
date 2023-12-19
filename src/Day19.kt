import kotlin.math.min
import kotlin.math.max

fun main() {

    val day = "19"

    data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

    data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
        fun countPossibilities() = 1L *
                (x.last - x.first + 1) *
                (m.last - m.first + 1) *
                (a.last - a.first + 1) *
                (s.last - s.first + 1)
    }

    abstract class Rule(val target: String) {
        abstract fun evaluate(part: Part): Boolean
        abstract fun narrow(range: PartRange, negate: Boolean = false): PartRange
    }
    class DirectRule(target: String): Rule(target) {
        override fun evaluate(part: Part) = true
        override fun narrow(range: PartRange, negate: Boolean) = range
    }
    class EvaluationRule(val category: String, val op: String, val value: Int, target: String): Rule(target) {
        override fun evaluate(part: Part): Boolean {
            val rating = when(category) {
                "x" -> part.x
                "m" -> part.m
                "a" -> part.a
                "s" -> part.s
                else -> error("Undefined")
            }
            return when (op) {
                "<" -> rating < value
                ">" -> rating > value
                else -> error("Undefined")
            }
        }

        override fun narrow(range: PartRange, negate: Boolean): PartRange {
            val constraint = when (op) {
                "<" ->
                    if (negate) { r: IntRange -> max(r.first, value)..r.last }
                    else { r: IntRange -> r.first..min(r.last, value - 1) }

                ">" ->
                    if (negate) { r: IntRange -> r.first..min(r.last, value) }
                    else { r: IntRange -> max(r.first, value + 1)..r.last }

                else -> error("Undefined")
            }
            return when (category) {
                "x" -> range.copy(x = constraint(range.x))
                "m" -> range.copy(m = constraint(range.m))
                "a" -> range.copy(a = constraint(range.a))
                "s" -> range.copy(s = constraint(range.s))
                else -> error("Undefined")
            }
        }
    }

    fun parseRule(rule: String): Rule {
        if (!rule.contains(":")) return DirectRule(rule)
        val (category, op, value, target) = Regex("([xmas])([<>])(\\d+):([A-Za-z]+)")
            .findAll(rule)
            .flatMap { it.groupValues }
            .drop(1)
            .toList()
        return EvaluationRule(category, op, value.toInt(), target)
    }

    fun parse(input: String): Pair<Map<String, List<Rule>>, List<Part>>  {
        val (workflowList, partList) = input.split("\n\n")

        val workflows = workflowList
            .split("\n")
            .map { it.split("{") }
            .associate { (a, b) -> a to b.removeSuffix("}").split(",").map { parseRule(it) } }

        val parts = partList
            .split("\n")
            .asSequence()
            .filter { it.isNotBlank() }
            .map { it.numbers() }
            .map { (x, m, a, s) -> Part(x, m, a, s) }
            .toList()

        return workflows to parts
    }

    fun part1(input: String): Int {
        val (workflows, parts) = parse(input)

        var ans = 0
        nextPart@ for (part in parts) {
            var workflow = workflows["in"]!!
            while (true) {
                currentWorkflow@ for (rule in workflow) {
                    if (rule.evaluate(part)) {
                        when (val target = rule.target) {
                            "A" -> {
                                ans += part.x + part.m + part.a + part.s
                                continue@nextPart
                            }

                            "R" -> continue@nextPart
                            else -> {
                                workflow = workflows[target]!!
                                break@currentWorkflow
                            }
                        }
                    }
                }
            }
        }

        return ans
    }

    fun findRange(workflows: Map<String, List<Rule>>, workflowId: String, range: PartRange): List<PartRange> {
        val acceptedRanges = mutableListOf<PartRange>()
        val workflow = workflows[workflowId]!!
        var currentRange = range
        for (rule in workflow) {
            when (val target = rule.target) {
                "A" -> acceptedRanges.add(rule.narrow(currentRange))
                "R" -> currentRange = rule.narrow(currentRange, negate = true)
                else -> acceptedRanges.addAll(findRange(workflows, target, rule.narrow(currentRange)))
            }
            currentRange = rule.narrow(currentRange, negate = true)
        }
        return acceptedRanges
    }

    fun part2(input: String): Long {
        val (workflows, _) = parse(input)

        val startRange = PartRange(1..4000, 1..4000, 1..4000, 1..4000)
        val ranges = findRange(workflows, "in", startRange)
        return ranges.sumOf { it.countPossibilities() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInputString("Day${day}_test")
    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)

    val input = readInputString("Day${day}")
    println(part1(input))
    println(part2(input))
}
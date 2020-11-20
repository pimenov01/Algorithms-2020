@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson8

import lesson6.Graph
import lesson6.Path
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import kotlin.math.pow

// Примечание: в этом уроке достаточно решить одну задачу

/**
 * Решить задачу о ранце (см. урок 6) любым эвристическим методом
 *
 * Очень сложная
 *
 * load - общая вместимость ранца, items - список предметов
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */
fun fillKnapsackHeuristics(load: Int, items: List<Item>, vararg parameters: Any): Fill {
    TODO()
}

/**
 * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
 * или любым другим эвристическим методом, кроме генетического и имитации отжига
 * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
 *
 * Очень сложная
 *
 * Граф передаётся через получатель метода
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */
class Ant(
    private val graph: Graph,
    private val pheromonesMap: MutableMap<Graph.Edge, Double>,
    private val alpha: Double = -1.0,
    private val beta: Double = 1.0,
) {
    private val visitedVertices: MutableList<Graph.Vertex> = mutableListOf()
    private val visitedEdges: MutableSet<Graph.Edge> = mutableSetOf()
    private var current: Graph.Vertex = graph.vertices.first()
    var path: Path? = null

    fun prepare() {
        val sumWeight = visitedEdges.sumBy { it.weight }

        visitedEdges.forEach { edge ->
            pheromonesMap[edge] = pheromonesMap
                .getOrDefault(edge, 0.0) + 1.0 / sumWeight
        }
        current = graph.vertices.first()
        visitedEdges.clear()
        visitedVertices.clear()
    }

    fun process() {
        while (visitedVertices.size != graph.vertices.size - 1) {
            val next = next()
            if (next == null) {
                prepare()
                return
            }
            visitedEdges.add(graph.getConnection(current, next)!!)
            visitedVertices.add(current)
            current = next
        }

        visitedVertices.add(current)

        val connection = graph.getConnection(graph.vertices.first(), current)

        if (connection == null) {
            prepare()
            return
        }

        visitedEdges.add(connection)

        var result = Path(visitedVertices.first())

        visitedVertices.drop(1).forEach { result = Path(result, graph, it) }
        path = Path(result, graph, visitedVertices.first())
    }


    fun next(): Graph.Vertex? {
        fun calculatePheromone(next: Graph.Vertex): Double {
            fun calculate(edge: Graph.Edge): Double {
                val pheromone = pheromonesMap.getOrDefault(edge, 0.0)
                return pheromone.pow(alpha) * (1.0 / edge.weight).pow(beta)
            }

            val numerator = calculate(graph.getConnection(current, next)!!)
            val denominator = graph.getNeighbors(current).map { graph.getConnection(current, it)!! }
                .sumByDouble { calculate(it) } + numerator

            return numerator / denominator
        }
        return graph.getNeighbors(current).filter { it !in visitedVertices }.maxBy { calculatePheromone(it) }
    }
}

//память О(кол-во муравьев * N)
//трудоемкость О(K*N*L)
fun Graph.findVoyagingPathHeuristics(vararg parameters: Array<Any>): Path {
    val size = this.vertices.size
    val pheromones = mutableMapOf<Graph.Edge, Double>()
    val antsList = Array(size * 10) { Ant(this, pheromonesMap = pheromones) }
    var path: Path? = null
    var iterationRemaining = size
    while (true) {
        antsList.forEach { ant -> ant.prepare() }
        antsList.forEach { ant -> ant.process() }
        val newPath = antsList.mapNotNull { it.path }.minBy { it.length }
        if (newPath != null && (path == null || newPath.length < path.length)) {
            iterationRemaining = size
            path = newPath
        }
        iterationRemaining--
        if (iterationRemaining == 0) return path!!
    }
}


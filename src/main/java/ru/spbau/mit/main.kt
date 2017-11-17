package ru.spbau.mit

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class Vertex {
    var hasUniversity: Boolean = false
    var universitiesInSubtree: Int = 0
}

class Graph {
    private val edges: MutableMap<Vertex, MutableList<Vertex>> = HashMap()

    fun addEdge(from: Vertex, to: Vertex) {
        edges.getOrPut(from, { mutableListOf() }).add(to)
        edges.getOrPut(to, { mutableListOf() }).add(from)
    }

    fun getNeighbours(from: Vertex): List<Vertex> = edges[from] ?: listOf()
}


fun solve(graph: Graph, root: Vertex, universitiesPairCount: Int): Long {
    calcUniversitiesInSubtrees(graph, root)
    val optimalRoot = findOptimalRoot(graph, root, universitiesPairCount)!!
    return calcSumOfDistancesToRoot(graph, optimalRoot)
}

private fun calcSumOfDistancesToRoot(graph: Graph, curVertex: Vertex,
                                     parent: Vertex? = null, distToRoot: Int = 0): Long {
    val sumInChildren = graph.getNeighbours(curVertex).filter { it != parent }.map {
        calcSumOfDistancesToRoot(graph, it, curVertex, distToRoot + 1)
    }.sum()

    return if (curVertex.hasUniversity) sumInChildren + distToRoot else sumInChildren
}

private fun calcUniversitiesInSubtrees(graph: Graph, curVertex: Vertex, parent: Vertex? = null): Int {
    val childCount = graph.getNeighbours(curVertex).filter { it != parent }.map {
        calcUniversitiesInSubtrees(graph, it, curVertex)
    }.sum()

    val childCountAndMe = if (curVertex.hasUniversity) childCount + 1 else childCount
    curVertex.universitiesInSubtree = childCountAndMe
    return childCountAndMe
}

private fun findOptimalRoot(graph: Graph, curVertex: Vertex, universitiesPairCount: Int,
                            parent: Vertex? = null, universitiesAbove: Int = 0): Vertex? {
    val children = graph.getNeighbours(curVertex).filter { it != parent }
    if (children.isEmpty()) {
        return if (universitiesAbove <= universitiesPairCount) curVertex else null
    }

    val vertexWithMaxUniversitiesInSubtree = children.maxBy { it.universitiesInSubtree }!!
    val maxUniversitiesCount = vertexWithMaxUniversitiesInSubtree.universitiesInSubtree

    return if (Math.max(maxUniversitiesCount, universitiesAbove) <= universitiesPairCount ) {
        curVertex
    } else {
        children.mapNotNull {
            findOptimalRoot(graph, it, universitiesPairCount, curVertex,
                    universitiesAbove + curVertex.universitiesInSubtree - it.universitiesInSubtree)
        }.firstOrNull()
    }
}

class DataReader(stream: InputStream) {
    private val reader = BufferedReader(InputStreamReader(stream))
    private var st : StringTokenizer? = null

    private fun next() : String? {
        while (st == null || !st!!.hasMoreTokens()) {
            val s = reader.readLine() ?: return null
            st = StringTokenizer(s)
        }

        return st?.nextToken()
    }

    private fun nextToken() = next()!!

    fun nextInt() = nextToken().toInt()
}


fun main(args: Array<String>) {
    val reader = DataReader(System.`in`)
    val vertexCount = reader.nextInt()
    val universityPairCount = reader.nextInt()

    val graph = Graph()
    val vertices = List(vertexCount, { Vertex() })

    for (i in 0 until 2 * universityPairCount) {
        val id = reader.nextInt()
        vertices[id - 1].hasUniversity = true
    }
    for (i in 0 until vertexCount - 1) {
        val from = reader.nextInt()
        val to = reader.nextInt()
        graph.addEdge(vertices[from - 1], vertices[to - 1])
    }

    println(solve(graph, vertices[0], universityPairCount))
}
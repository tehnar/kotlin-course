package ru.spbau.mit
import kotlin.test.assertEquals
import org.junit.Test

class TestSource {
    @Test
    fun testSample1() {
        val vertexCount = 7
        val universityPairCount = 2
        val vertices = List(vertexCount, { Vertex() })
        val graph = Graph()
        graph.addEdge(vertices[0], vertices[2])
        graph.addEdge(vertices[2], vertices[1])
        graph.addEdge(vertices[3], vertices[4])
        graph.addEdge(vertices[2], vertices[6])
        graph.addEdge(vertices[3], vertices[2])
        graph.addEdge(vertices[3], vertices[5])

        vertices[0].hasUniversity = true
        vertices[4].hasUniversity = true
        vertices[5].hasUniversity = true
        vertices[1].hasUniversity = true

        assertEquals(6, Solver().solve(graph, vertices[0], universityPairCount))
    }

    @Test
    fun testSample2() {
        val vertexCount = 9
        val universityPairCount = 3
        val vertices = List(vertexCount, { Vertex() })
        val graph = Graph()
        graph.addEdge(vertices[7], vertices[8])
        graph.addEdge(vertices[2], vertices[1])
        graph.addEdge(vertices[1], vertices[6])
        graph.addEdge(vertices[2], vertices[3])
        graph.addEdge(vertices[6], vertices[5])
        graph.addEdge(vertices[3], vertices[4])
        graph.addEdge(vertices[1], vertices[0])
        graph.addEdge(vertices[1], vertices[7])

        vertices[2].hasUniversity = true
        vertices[1].hasUniversity = true
        vertices[0].hasUniversity = true
        vertices[5].hasUniversity = true
        vertices[4].hasUniversity = true
        vertices[8].hasUniversity = true

        assertEquals(9, Solver().solve(graph, vertices[0], universityPairCount))
    }
}

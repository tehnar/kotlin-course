package ru.spbau.mit.model.map

import ru.spbau.mit.model.tile.FloorTile
import ru.spbau.mit.model.tile.Tile
import ru.spbau.mit.model.tile.WallTile
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Utility class that loads pregenerated map from disk
 */
class MapLoader {
    fun load(stream: InputStream): GameMap {
        val lines = BufferedReader(InputStreamReader(stream)).readLines()
        val (sizeX, sizeY) = lines[0].split(" ").map(String::toInt)
        val map = Array(sizeX, { Array<Tile>(sizeY, { FloorTile() }) })
        for (i in 0 until sizeY) {
            for (j in 0 until sizeX) {
                map[j][i] = when (lines[i + 1][j]) {
                    '#' -> WallTile()
                    '.' -> FloorTile()
                    else -> throw IllegalStateException("Unknown tile ${lines[i + 1][j]}")
                }
            }
        }
        return GameMapImpl(sizeX, sizeY, map)
    }
}
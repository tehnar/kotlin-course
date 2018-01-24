package ru.spbau.mit.model.map

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.tile.Tile
import java.io.Serializable

/**
 * Interface that provides access to game's map.
 * All tiles have coordinates in range [0, sizeX) x [0, sizeY)
 */
interface GameMap: Serializable {
    val sizeX: Int
    val sizeY: Int

    /**
     * Tile at position (x, y)
     */
    fun at(x: Int, y: Int): Tile

    /**
     * Tile at position pos
     */
    fun at(pos: Position): Tile = at(pos.x, pos.y)
}
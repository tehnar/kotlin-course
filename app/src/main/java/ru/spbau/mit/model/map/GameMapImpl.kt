package ru.spbau.mit.model.map

import ru.spbau.mit.model.tile.Tile

class GameMapImpl(
        override val sizeX: Int,
        override val sizeY: Int,
        private val tiles: Array<Array<Tile>>
) : GameMap {
    override fun at(x: Int, y: Int): Tile = tiles[x][y]
}
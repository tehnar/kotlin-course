package ru.spbau.mit.model.tile

import ru.spbau.mit.model.Position
import ru.spbau.mit.view.GameView

/**
 * Unpassable wall
 */
class WallTile: Tile {
    override val passable = false
    override val name = "wall"

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
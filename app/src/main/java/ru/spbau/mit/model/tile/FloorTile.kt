package ru.spbau.mit.model.tile

import ru.spbau.mit.model.Position
import ru.spbau.mit.view.GameView

/**
 * Passable floor
 */
class FloorTile: Tile {
    override val passable = true
    override val name = "floor"

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
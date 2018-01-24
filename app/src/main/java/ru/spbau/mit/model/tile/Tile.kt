package ru.spbau.mit.model.tile

import ru.spbau.mit.view.Drawable
import java.io.Serializable

/**
 * Interface that represents single cell of map
 */
interface Tile: Drawable, Serializable {
    /**
     * If creature can step on it
     */
    val passable: Boolean

    /**
     * Name of tile. Used for displaying tile's information
     */
    val name: String
}
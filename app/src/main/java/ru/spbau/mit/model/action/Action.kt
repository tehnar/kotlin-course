package ru.spbau.mit.model.action

import ru.spbau.mit.model.world.World
import java.io.Serializable

/**
 * Interface that represents creature's action
 */
interface Action: Serializable {
    /**
     * Applies action to world
     * @return description of action performed
     */
    fun applyAction(world: World): ActionResult?
}
package ru.spbau.mit.model.strategy

import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.world.World
import java.io.Serializable

/**
 * Strategy that defines mob's actions
 */
interface Strategy: Serializable {
    /**
     * Gets creature's action
     */
    fun getAction(world: World, me: Creature): Action
}
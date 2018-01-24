package ru.spbau.mit.model.world

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.map.GameMap
import java.io.Serializable

/**
 * Interface that represents all the world information
 */
interface World: Serializable {
    /**
     * World's map
     */
    val map: GameMap

    /**
     * Creatures on map
     */
    val creatures: MutableList<Creature>

    /**
     * @return creature on position pos if exists, null otherwise
     */
    fun creatureAt(pos: Position): Creature?

    /**
     * @return list of item at given position pos
     */
    fun itemsAt(pos: Position): List<Item>

    /**
     * Adds item to given position pos
     */
    fun addItemAt(pos: Position, item: Item)

    /**
     * Removes item from position pos
     */
    fun removeItemAt(pos: Position, item: Item)
}
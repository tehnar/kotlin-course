package ru.spbau.mit.model.item

import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.view.Drawable
import java.io.Serializable

/**
 * Interface that represents game item
 */
interface Item: Drawable, Serializable {
    /**
     * If item can be used
     */
    val usable: Boolean

    /**
     * If item can be worn
     */
    val wearable: Boolean

    /**
     * Name of item. Used for displaying item's information
     */
    val name: String

    /**
     * If item is used. Usable items can be used only once
     */
    fun isUsed(): Boolean

    /**
     * If item is worn on some creature
     */
    fun isWorn(): Boolean

    /**
     * Use item
     * @param creature: target of item usage
     */
    fun use(creature: Creature): ActionResult?

    /**
     * Put item on creature
     * @param creature: target
     */
    fun wear(creature: Creature): ActionResult?

    /**
     * Take off item from creature
     * @param creature: target
     */
    fun takeOff(creature: Creature): ActionResult?
}
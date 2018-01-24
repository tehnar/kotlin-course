package ru.spbau.mit.model.action

import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to take off item
 */
class TakeOffItemAction(val me: Creature, val item: Item): Action {
    override fun applyAction(world: World): ActionResult? {
        if (!me.inventory.contains(item)) {
            throw IllegalArgumentException("Attempt to take off item that is not in inventory")
        }
        if (!item.isWorn()) {
            return ActionResult("You cannot take off ${item.name} as it's not on you")
        }

        return item.takeOff(me)    }

}
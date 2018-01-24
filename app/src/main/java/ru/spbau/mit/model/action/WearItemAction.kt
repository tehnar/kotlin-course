package ru.spbau.mit.model.action

import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to wear item
 */
class WearItemAction(val me: Creature, val item: Item): Action {
    override fun applyAction(world: World): ActionResult? {
        if (!me.inventory.contains(item)) {
            throw IllegalArgumentException("Attempt to wear item that is not in inventory")
        }
        if (!item.wearable) {
            return ActionResult("${item.name} can not be worn")
        }
        if (item.isWorn()) {
            return ActionResult("${item.name} is already on you")
        }

        return item.wear(me)
    }

}
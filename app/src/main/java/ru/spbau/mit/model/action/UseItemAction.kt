package ru.spbau.mit.model.action

import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to use item
 */
class UseItemAction(val me: Creature, val item: Item): Action {
    override fun applyAction(world: World): ActionResult? {
        if (!me.inventory.contains(item)) {
            throw IllegalArgumentException("Attempt to use item that is not in inventory")
        }
        if (!item.usable) {
            return ActionResult("${item.name} can not be used")
        }
        val result = item.use(me)
        if (item.isUsed()) {
            me.inventory.remove(item)
        }
        return result
    }

}
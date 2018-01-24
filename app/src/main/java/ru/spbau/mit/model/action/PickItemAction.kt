package ru.spbau.mit.model.action

import ru.spbau.mit.model.GameConstants
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to pick up item from floor
 */
class PickItemAction(val me: Creature, val item: Item): Action {
    override fun applyAction(world: World): ActionResult? {
        if (!world.itemsAt(me.position).contains(item)) {
            throw IllegalArgumentException("Item you want to pick is not lying at ${me.position}")
        }

        if (me.inventory.size == GameConstants.MAX_ITEM_LIST_SIZE) {
            return ActionResult("Your inventory is already full")
        }

        me.inventory.add(item)
        world.removeItemAt(me.position, item)
        return ActionResult("You pick up ${item.name}")
    }

}
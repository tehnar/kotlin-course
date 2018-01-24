package ru.spbau.mit.model.action

import ru.spbau.mit.model.GameConstants
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to drop item
 */
class DropItemAction(val me: Creature, val item: Item): Action {
    override fun applyAction(world: World): ActionResult? {
        if (!me.inventory.contains(item)) {
            IllegalArgumentException("Cannot drop item that is not in inventory")
        }

        if (world.itemsAt(me.position).size + 1 > GameConstants.MAX_ITEM_LIST_SIZE) {
            return ActionResult("Cannot drop $item here: floor under you is too cluttered")
        }

        me.inventory.remove(item)
        world.addItemAt(me.position, item)
        return ActionResult("You drop ${item.name}")
    }

}
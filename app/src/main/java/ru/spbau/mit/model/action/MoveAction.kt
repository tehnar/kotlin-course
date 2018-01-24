package ru.spbau.mit.model.action

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.world.World

/**
 * Creature me wants to move to position to. If this position is occupied, then `me` attacks creature on it
 */
class MoveAction(private val me: Creature, val to: Position): Action {
    override fun applyAction(world: World): ActionResult? {
        if (me.position == to) {
            return null
        }

        if (Math.abs(me.position.x - to.x) + Math.abs(me.position.y - to.y) != 1) {
            throw IllegalArgumentException("Cannot move from ${me.position} to $to: too long move")
        }

        if (!world.map.at(to).passable) {
            return ActionResult("Movement is blocked by ${world.map.at(to).name}")
        }

        val creature = world.creatureAt(to)
        if (creature == null) {
            me.position = to
            return null
        } else {
            val damage = creature.getRealReceivingDamage(me.stats.damage)
            creature.stats.hp -= damage
            return if (damage == 0) {
                ActionResult("${me.name} hits ${creature.name}, but attack is reflected by ${creature.name}'s armor!")
            } else {
                ActionResult("${me.name} hits ${creature.name} for $damage damage!")
            }
        }
    }
}

package ru.spbau.mit.model.strategy

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.action.MoveAction
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.world.World
import java.util.*

/**
 * Makes random move if can not attack player, attacks him otherwise
 */
class RandomWalkStrategy: Strategy {
    private val random = Random()

    override fun getAction(world: World, me: Creature): Action {
        val directions = arrayOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        for (dir in directions) {
            val newPos = Position(me.position.x + dir.first, me.position.y + dir.second)
            if (world.creatureAt(newPos)?.isPlayer == true) {
                return MoveAction(me, newPos)
            }
        }
        val dir = directions[random.nextInt(4)]
        val newPos = Position(me.position.x + dir.first, me.position.y + dir.second)
        if (world.map.at(newPos).passable) {
            return MoveAction(me, newPos)
        } else {
            return MoveAction(me, me.position)
        }
    }
}
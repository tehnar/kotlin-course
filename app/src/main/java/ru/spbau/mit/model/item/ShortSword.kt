package ru.spbau.mit.model.item

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.view.GameView

/**
 * Modifies creature's damage stat by given value while put on
 */

class ShortSword(private val damage: Int) : Item {
    override val usable = false
    override val wearable = true
    override val name = if (damage >= 0)"Short sword of (+$damage)" else "Short sword of ($damage)"

    private var isWorn = false

    override fun isUsed(): Boolean = false

    override fun isWorn(): Boolean = isWorn

    override fun use(creature: Creature): ActionResult? {
        throw UnsupportedOperationException("You cannot use $name")
    }

    override fun wear(creature: Creature): ActionResult? {
        if (isWorn) {
            throw IllegalStateException("Item is already worn")
        }
        creature.stats.damage += damage
        isWorn = true
        return ActionResult("You put on your $name")
    }

    override fun takeOff(creature: Creature): ActionResult? {
        if (!isWorn) {
            throw IllegalArgumentException("Item is not worn")
        }
        creature.stats.damage -= damage
        isWorn = false
        return ActionResult("You take off your $name")
    }

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
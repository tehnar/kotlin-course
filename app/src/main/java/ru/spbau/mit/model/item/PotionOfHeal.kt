package ru.spbau.mit.model.item

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.view.GameView

/**
 * Usable item that heals hpToHeal hp when used
 */
class PotionOfHeal(private val hpToHeal: Int): Item {
    override val usable = true
    override val wearable = false
    override val name = "Potion of healing (+$hpToHeal)"

    private var isUsed = false

    override fun isUsed() = isUsed

    override fun isWorn(): Boolean = false

    override fun use(creature: Creature): ActionResult? {
        val healedHp = Math.min(creature.stats.maxHp - creature.stats.hp, hpToHeal.toDouble())
        creature.stats.hp += healedHp
        isUsed = true
        return ActionResult("You drink $name and recover $healedHp hp")
    }

    override fun wear(creature: Creature): ActionResult? {
        throw UnsupportedOperationException("You cannot wear $name")
    }

    override fun takeOff(creature: Creature): ActionResult? {
        throw UnsupportedOperationException("You cannot take off $name")
    }

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
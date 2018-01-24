package ru.spbau.mit.model.creature

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.strategy.Strategy
import ru.spbau.mit.model.world.World
import ru.spbau.mit.view.GameView

class Goblin(
        override var position: Position,
        override val inventory: MutableList<Item>,
        private val strategy: Strategy
) : Creature {

    override val name = "Goblin"

    override val stats = Creature.Stats(10.0, 10.0, 0.1, 3, 0, 0)

    override val isPlayer = false

    override fun getAction(world: World): Action = strategy.getAction(world, this)

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
package ru.spbau.mit.model.creature

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.action.SkipAction
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World
import ru.spbau.mit.view.GameView

class Player(
        override var position: Position,
        override val inventory: MutableList<Item>,
        override val name: String
): Creature {

    override val stats = Creature.Stats(100.0, 100.0, 0.1, 5, 0, 0)

    override val isPlayer = true

    var curAction: Action = SkipAction

    override fun getAction(world: World): Action = curAction

    override fun draw(gameView: GameView, pos: Position) {
        gameView.draw(this, pos)
    }
}
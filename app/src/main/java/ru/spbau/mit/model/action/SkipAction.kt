package ru.spbau.mit.model.action

import ru.spbau.mit.model.world.World

object SkipAction: Action {
    override fun applyAction(world: World): ActionResult? {
        return ActionResult("")
    }

}
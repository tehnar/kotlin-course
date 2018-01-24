package ru.spbau.mit.view

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.item.PotionOfHeal
import ru.spbau.mit.model.item.RingMail
import ru.spbau.mit.model.item.Shield
import ru.spbau.mit.model.item.ShortSword
import ru.spbau.mit.model.tile.FloorTile
import ru.spbau.mit.model.tile.WallTile
import ru.spbau.mit.model.world.World

interface GameView {

    fun draw(item: PotionOfHeal, pos: Position)
    fun draw(item: ShortSword, pos: Position)
    fun draw(item: Shield, pos: Position)
    fun draw(item: RingMail, pos: Position)

    fun draw(tile: FloorTile, pos: Position)
    fun draw(tile: WallTile, pos: Position)

    fun draw(creature: Player, pos: Position)
    fun draw(creature: Goblin, pos: Position)
    fun draw(creature: Troll, pos: Position)

    fun drawTick(world: World, messages: List<ActionResult>)

    fun close()
}
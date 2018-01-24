package ru.spbau.mit.model.creature

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.Action
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.world.World
import ru.spbau.mit.view.Drawable
import java.io.Serializable

interface Creature: Drawable, Serializable {
    val name: String
    val stats: Stats
    var position: Position
    val inventory: MutableList<Item>
    val isPlayer: Boolean

    fun getAction(world: World): Action
    fun getRealReceivingDamage(damage: Int): Int = Math.max(0, damage - stats.armor - stats.shield)

    data class Stats(var hp: Double,
                     val maxHp: Double,
                     var regenRate: Double,
                     var damage: Int,
                     var armor: Int,
                     var shield: Int): Serializable
}
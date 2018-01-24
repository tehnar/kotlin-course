package ru.spbau.mit.model.game

import ru.spbau.mit.model.GameConstants
import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.item.PotionOfHeal
import ru.spbau.mit.model.strategy.RandomWalkStrategy
import ru.spbau.mit.model.world.World
import java.util.*


class GameImpl(override val world: World, override val player: Player): Game {

    private val random = Random()
    private var curTick: Int = 0

    companion object {
        private const val ENEMY_SPAWN_THRESHOLD = 15
        private const val USABLE_SPAWN_THRESHOLD = 50
        private const val TROLL_CHANCE = 5
    }

    private var status = Game.Status.RUNNING

    override fun status() = status

    override fun score() = curTick

    override fun tick(): List<ActionResult> {
        curTick += 1

        val actions = world.creatures.mapNotNull { it.getAction(world).applyAction(world) }
        val dead = world.creatures.filter { it.stats.hp <= 0 }
        world.creatures.removeAll { it.stats.hp <= 0 }
        dead.forEach {
            val pos = it.position
            it.inventory.forEach {
                if (world.itemsAt(pos).size < GameConstants.MAX_ITEM_LIST_SIZE) {
                    world.addItemAt(pos, it)
                }
            }
            if (it.isPlayer) {
                status = Game.Status.GAME_OVER
            }
        }

        world.creatures.forEach {
            it.stats.hp = Math.min(it.stats.maxHp, it.stats.hp + it.stats.regenRate)
        }

        if (random.nextInt(ENEMY_SPAWN_THRESHOLD) == 0) {
            val pos = getEmptyCell(world)
            pos?.let {
                if (random.nextInt(TROLL_CHANCE) == 0) {
                    world.creatures.add(Troll(pos, mutableListOf(), RandomWalkStrategy()))
                } else {
                    world.creatures.add(Goblin(pos, mutableListOf(), RandomWalkStrategy()))
                }
            }
        }
        if (random.nextInt(USABLE_SPAWN_THRESHOLD) == 0) {
            val pos = getEmptyCell(world)
            pos?.let {
                world.addItemAt(pos, PotionOfHeal(random.nextInt(10) + 1))
            }
        }
        return actions
    }

    private fun getEmptyCell(world: World): Position? {
        val emptyCells = mutableListOf<Position>()
        for (i in 0 until world.map.sizeX) {
            for (j in 0 until world.map.sizeY) {
                Position(i, j).let {
                    if (world.itemsAt(it).isEmpty()
                            && world.creatureAt(it) == null
                            && world.map.at(it).passable) {
                        emptyCells.add(it)
                    }
                }
            }
        }

        if (emptyCells.isEmpty()) {
            return null
        }
        return emptyCells[random.nextInt(emptyCells.size)]
    }
}
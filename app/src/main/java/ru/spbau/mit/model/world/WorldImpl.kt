package ru.spbau.mit.model.world

import ru.spbau.mit.model.Position
import ru.spbau.mit.model.creature.Creature
import ru.spbau.mit.model.item.Item
import ru.spbau.mit.model.map.GameMap
import java.util.*

class WorldImpl(override val map: GameMap,
                override val creatures: MutableList<Creature>,
                items: List<Pair<Item, Position>>): World {
    private val items: Array<Array<MutableList<Item>>> =
            Array(map.sizeX, { Array(map.sizeY, { ArrayList<Item>() as MutableList<Item> }) })

    init {
        items.forEach { this.items[it.second.x][it.second.y].add(it.first) }
    }

    override fun creatureAt(pos: Position): Creature? = creatures.find { it.position == pos }

    override fun itemsAt(pos: Position): List<Item> {
        checkBounds(pos)
        return items[pos.x][pos.y]
    }

    override fun addItemAt(pos: Position, item: Item) {
        checkBounds(pos)
        this.items[pos.x][pos.y].add(item)
    }

    override fun removeItemAt(pos: Position, item: Item) {
        checkBounds(pos)
        this.items[pos.x][pos.y].remove(item)
    }

    private fun checkBounds(pos: Position) {
        if (pos.x !in 0 until map.sizeX || pos.y !in 0 until map.sizeY) {
            throw IllegalArgumentException("$pos is out of map bounds")
        }
    }
}
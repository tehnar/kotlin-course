package ru.spbau.mit.model.game

import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.world.World
import java.io.*

/**
 * Interface that represents current game
 */
interface Game: Serializable {
    /**
     * Game's world
     */
    val world: World

    val player: Player

    /**
     * Status of game
     */
    fun status(): Status

    /**
     * Asks every creature for action and applies them to world
     */
    fun tick(): List<ActionResult>

    fun score(): Int

    fun saveTo(out: OutputStream) {
        ObjectOutputStream(out).writeObject(this)
    }

    companion object {
        fun load(inp: InputStream): Game = ObjectInputStream(inp).readObject() as Game
    }
    enum class Status {
        RUNNING,
        GAME_OVER
    }
}
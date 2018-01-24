package ru.spbau.mit.roguelike

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.Button
import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.*
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.game.Game
import ru.spbau.mit.model.game.GameImpl
import ru.spbau.mit.model.item.*
import ru.spbau.mit.model.map.MapLoader
import ru.spbau.mit.model.strategy.RandomWalkStrategy
import ru.spbau.mit.model.world.WorldImpl
import ru.spbau.mit.view.GameViewImpl
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class GameActivity : AppCompatActivity() {
    companion object {
        const val START_TYPE = "START_TYPE"
        const val LOAD_GAME = "LOAD_GAME"
        const val NEW_GAME = "NEW_GAME"
        const val PLAYER_NAME = "PLAYER_NAME"
        const val SAVE_FILE_NANE = "save"
        const val SCOREBOARD_FILE_NAME = "scoreboard"
    }

    private lateinit var game: Game
    private lateinit var playerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        findViewById<Button>(R.id.button_take).let {
            it.setOnClickListener {
                drawItemDialog(game.world.itemsAt(game.player.position), "Take Item", {
                    if (it != null) {
                        game.player.curAction = PickItemAction(game.player, it)
                        doGameTick()
                    }
                })
            }
        }

        findViewById<Button>(R.id.button_drop).let {
            it.setOnClickListener {
                drawItemDialog(game.player.inventory, "Drop Item", {
                    if (it != null) {
                        game.player.curAction = DropItemAction(game.player, it)
                        doGameTick()
                    }
                })
            }
        }

        findViewById<Button>(R.id.button_inventory).let {
            it.setOnClickListener {
                drawItemDialog(game.player.inventory, "Inventory", {
                    if (it != null) {
                        if (it.usable) {
                            game.player.curAction = UseItemAction(game.player, it)
                        } else {
                            if (it.isWorn()) {
                                game.player.curAction = TakeOffItemAction(game.player, it)
                            } else {
                                game.player.curAction = WearItemAction(game.player, it)
                            }
                        }
                        doGameTick()
                    }
                })
            }
        }

        findViewById<Button>(R.id.button_skip).let {
            it.setOnClickListener {
                game.player.curAction = SkipAction
                doGameTick()
            }
        }

        playerName = intent.getStringExtra(PLAYER_NAME)
        val startType = intent.getStringExtra(START_TYPE)

        if (startType == LOAD_GAME) {
            loadGame()
        } else {
            newGame()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) {
            return false
        }
        val gameView = findViewById<GameViewImpl>(R.id.game_view_impl)
        val width = gameView.width
        val height = gameView.height

        val isLeftOrDown = event.x < width * event.y / height
        val isLeftOrUp = event.y < height * (width - event.x) / width

        game.player.let {
            val pos = it.position

            if (isLeftOrDown && isLeftOrUp) {
                it.curAction = MoveAction(it, Position(pos.x - 1, pos.y))
            } else if (isLeftOrDown && !isLeftOrUp) {
                it.curAction = MoveAction(it, Position(pos.x, pos.y + 1))
            } else if (!isLeftOrDown && isLeftOrUp) {
                it.curAction = MoveAction(it, Position(pos.x, pos.y - 1))
            } else {
                it.curAction = MoveAction(it, Position(pos.x + 1, pos.y))
            }
        }

        doGameTick()

        findViewById<Button>(R.id.button_take).isEnabled =
                game.world.itemsAt(game.player.position).isNotEmpty()
        findViewById<Button>(R.id.button_drop).isEnabled =
                game.player.inventory.isNotEmpty()

        return true
    }

    private fun newGame() {
        applicationContext.deleteFile(SAVE_FILE_NANE)

        val gameView = findViewById<GameViewImpl>(R.id.game_view_impl)
        val player = Player(Position(1, 1), mutableListOf(), playerName)

        val creatures = mutableListOf(
                player,
                Goblin(Position(4, 2), mutableListOf(), RandomWalkStrategy()),
                Goblin(Position(4, 6), mutableListOf(ShortSword(3)), RandomWalkStrategy()),
                Troll(Position(7, 8), mutableListOf(RingMail(3)), RandomWalkStrategy()),
                Goblin(Position(12, 6), mutableListOf(), RandomWalkStrategy())
        )
        val items = listOf(
                Pair(PotionOfHeal(10), Position(13, 2)),
                Pair(Shield(3), Position(5, 5))
        )
        val world = applicationContext.assets.open("map/map.txt").use {
            WorldImpl(MapLoader().load(it), creatures, items)
        }

        game = GameImpl(world, player)
        gameView.game = game
    }

    override fun onPause() {
        super.onPause()
        saveGame()
    }

    override fun onResume() {
        super.onResume()
        loadGame()
    }

    private fun saveGame() {
        if (game.status() != Game.Status.GAME_OVER) {
            applicationContext.openFileOutput(SAVE_FILE_NANE, Context.MODE_PRIVATE).use {
                game.saveTo(it)
            }
        } else {
            deleteFile(SAVE_FILE_NANE)
        }
    }

    private fun loadGame() {
        if (!fileExists(SAVE_FILE_NANE)) {
            newGame()
            return
        }

        try {
            applicationContext.openFileInput(SAVE_FILE_NANE).use {
                game = Game.load(it)
                findViewById<GameViewImpl>(R.id.game_view_impl).game = game
            }
        } catch (ignored: Exception) {
            newGame()
        }
    }

    private fun drawItemDialog(items: List<Item>, title: String, itemAction: (Item?) -> Unit) {
        AlertDialog.Builder(this).setTitle(title).setItems(
                (items.map { it.name + if (it.isWorn()) " (worn)" else ""} + "Do nothing").toTypedArray(),
                { _, i -> itemAction(if (i < items.size) items[i] else null) }
        ).create().show()
    }

    private fun fileExists(fileName: String): Boolean {
        val file = applicationContext.getFileStreamPath(fileName)
        return file != null && file.exists()
    }

    private fun doGameTick() {
        val gameView = findViewById<GameViewImpl>(R.id.game_view_impl)
        gameView.drawTick(game.world, game.tick())
        if (game.status() == Game.Status.GAME_OVER) {
            saveScore()
            val dialog = AlertDialog.Builder(this).setTitle("You are DEAD")
                    .setNegativeButton( "Go to menu", { _, _ ->
                        startActivity(Intent(this, MenuActivity::class.java))
                    })
                    .setNeutralButton("Start New Game", {_, _ -> newGame()})
                    .create()
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun saveScore() {
        val lines = if (!fileExists(SCOREBOARD_FILE_NAME)) {
            mutableListOf<String>()
        } else {
            applicationContext.openFileInput(SCOREBOARD_FILE_NAME).use {
                BufferedReader(InputStreamReader(it)).readLines().toMutableList()
            }
        }

        lines.add("${game.score()}: $playerName")
        applicationContext.openFileOutput(SCOREBOARD_FILE_NAME, Context.MODE_PRIVATE).use {
            BufferedWriter(OutputStreamWriter(it)).use { writer ->
                for (line in lines) {
                    writer.write(line)
                    writer.newLine()
                }
            }
        }

    }
}

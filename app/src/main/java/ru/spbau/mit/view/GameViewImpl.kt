package ru.spbau.mit.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.spbau.mit.model.GameConstants
import ru.spbau.mit.model.Position
import ru.spbau.mit.model.action.ActionResult
import ru.spbau.mit.model.creature.Goblin
import ru.spbau.mit.model.creature.Player
import ru.spbau.mit.model.creature.Troll
import ru.spbau.mit.model.game.Game
import ru.spbau.mit.model.item.PotionOfHeal
import ru.spbau.mit.model.item.RingMail
import ru.spbau.mit.model.item.Shield
import ru.spbau.mit.model.item.ShortSword
import ru.spbau.mit.model.tile.FloorTile
import ru.spbau.mit.model.tile.WallTile
import ru.spbau.mit.model.world.World
import java.util.*


class GameViewImpl(context: Context, attrs: AttributeSet?): GameView, View(context, attrs) {

    lateinit var game: Game

    private val messageHistory: Queue<ActionResult> = ArrayDeque()

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private val paint = Paint()

    private val charsInColumn = 22
    private val fontSize: Float
        get() = (height - 10) / charsInColumn.toFloat()

    init {
        paint.typeface = Typeface.MONOSPACE
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        drawTick(game.world, listOf())
    }

    override fun draw(item: PotionOfHeal, pos: Position) {
        putChar('!', pos, Color.rgb(200, 0, 0))
    }

    override fun draw(item: ShortSword, pos: Position) {
        putChar(')', pos, Color.LTGRAY)
    }

    override fun draw(item: Shield, pos: Position) {
        putChar('[', pos, Color.rgb(0, 206, 209))
    }

    override fun draw(item: RingMail, pos: Position) {
        putChar('[', pos, Color.rgb(0, 0, 205))
    }

    override fun draw(tile: FloorTile, pos: Position) {
        putChar('.', pos, Color.GRAY)
    }

    override fun draw(tile: WallTile, pos: Position) {
        putChar('#', pos, Color.GRAY)
    }

    override fun draw(creature: Player, pos: Position) {
        putChar('@', pos, Color.WHITE)
    }

    override fun draw(creature: Goblin, pos: Position) {
        putChar('g', pos, Color.rgb(139, 69, 19))
    }

    override fun draw(creature: Troll, pos: Position) {
        putChar('T', pos, Color.rgb(34, 139, 34))
    }

    override fun drawTick(world: World, messages: List<ActionResult>) {
        Paint().let {
            it.style = Paint.Style.FILL
            it.color = Color.BLACK
            canvas.drawPaint(it)
        }

        for (x in 0 until world.map.sizeX) {
            for (y in 0 until world.map.sizeY) {
                world.map.at(x, y).draw(this, Position(x, y))
                world.itemsAt(Position(x, y)).firstOrNull()?.draw(this, Position(x, y))
            }
        }
        world.creatures.forEach { it.draw(this, it.position) }


        messages.forEach {
            if (messageHistory.size == 5) {
                messageHistory.poll()
            }
            messageHistory.add(it)
        }
        messageHistory.forEachIndexed { i, actionResult ->
            putStr(0, charsInColumn - messageHistory.size + i, actionResult.text)
        }

        val statusLine = game.player.stats.let {
            String.format(
                    "HP: %.1f/%.1f AC: %d SH: %d DMG: %d",
                    it.hp, it.maxHp, it.armor, it.shield, it.damage
            )
        }

        putStr(world.map.sizeX + 3, 1, game.player.name)
        putStr(world.map.sizeX + 3, 2, statusLine)
        putStr(world.map.sizeX + 3, 3, "Your inventory:")
        game.player.inventory.forEachIndexed { i, item ->
            if (item.isWorn()) {
                putStr(world.map.sizeX + 3, 4 + i, "$i: ${item.name} (on you)")
            } else {
                putStr(world.map.sizeX + 3, 4 + i, "$i: ${item.name}")
            }
        }
        putStr(world.map.sizeX + 3, 4 + GameConstants.MAX_ITEM_LIST_SIZE, "Items on floor:")
        world.itemsAt(game.player.position).forEachIndexed { i, item ->
            putStr(world.map.sizeX + 3, 5 + i + GameConstants.MAX_ITEM_LIST_SIZE, "$i: ${item.name}")
        }

        invalidate()
    }

    private fun putChar(char: Char, pos: Position, color: Int) {
        paint.color = color
        paint.textSize = fontSize
        canvas.drawText(
                char.toString(),
                pos.x * fontSize,
                (pos.y + 1) * fontSize,
                paint
        )
    }

    private fun putStr(column: Int, row: Int, str: String) {
        paint.color = Color.rgb(255, 255, 255)
        paint.textSize = fontSize
        canvas.drawText(
                str,
                column * fontSize,
                (row  + 1)* fontSize,
                paint
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(bitmap, 0f, 0f, paint)

    }

    override fun close() {
    }
}
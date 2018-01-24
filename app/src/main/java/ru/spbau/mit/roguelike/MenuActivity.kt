package ru.spbau.mit.roguelike

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val nameEdit = findViewById<EditText>(R.id.edit_player_name)

        findViewById<Button>(R.id.button_new_game).setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.START_TYPE, GameActivity.NEW_GAME)
                putExtra(GameActivity.PLAYER_NAME, nameEdit.text.toString())
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_load_game).let {
            it.isEnabled = saveExists()
            it.setOnClickListener {
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra(GameActivity.START_TYPE, GameActivity.LOAD_GAME)
                    putExtra(GameActivity.PLAYER_NAME, nameEdit.text.toString())
                }
                startActivity(intent)
            }
        }

        findViewById<Button>(R.id.button_scoreboard).let {
            it.setOnClickListener {
                val intent = Intent(this, ScoreboardActivity::class.java)
                startActivity(intent)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        findViewById<Button>(R.id.button_load_game).isEnabled = saveExists()
    }

    private fun saveExists(): Boolean {
        val file = applicationContext.getFileStreamPath(GameActivity.SAVE_FILE_NANE)
        return file != null && file.exists()
    }

}

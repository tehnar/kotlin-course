package ru.spbau.mit.roguelike

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType.TYPE_NULL
import android.view.View.TEXT_ALIGNMENT_TEXT_END
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader

class ScoreboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
    }

    override fun onResume() {
        super.onResume()

        val scrollView = findViewById<ScrollView>(R.id.scoreboard_scroll)
        findViewById<LinearLayout>(R.id.scoreboard).let {
            it.removeAllViews()

            for (scoreEntry in readScoreboard()) {
                val entryLayout = LinearLayout(applicationContext)
                entryLayout.orientation = LinearLayout.HORIZONTAL

                val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.weight = 0.5f

                val nameView = TextView(applicationContext)
                nameView.text = scoreEntry.second
                nameView.textSize = 20f
                nameView.setTextColor(Color.WHITE)
                nameView.inputType = TYPE_NULL
                nameView.textAlignment = TEXT_ALIGNMENT_TEXT_START
                nameView.layoutParams = params
                entryLayout.addView(nameView)

                val scoreView = TextView(applicationContext)
                scoreView.text = scoreEntry.first.toString()
                scoreView.textSize = 20f
                scoreView.setTextColor(Color.WHITE)
                scoreView.inputType = TYPE_NULL
                scoreView.textAlignment = TEXT_ALIGNMENT_TEXT_END
                scoreView.layoutParams = params
                entryLayout.addView(scoreView)

                it.addView(entryLayout)
            }
        }
    }

    private fun readScoreboard(): List<Pair<Int, String> > {
        val scoreboardExists = applicationContext.getFileStreamPath(
                GameActivity.SCOREBOARD_FILE_NAME
        )?.exists() ?: false

        if (!scoreboardExists) {
            return listOf()
        }

        applicationContext.openFileInput(GameActivity.SCOREBOARD_FILE_NAME).use{ inp ->
            return BufferedReader(InputStreamReader(inp)).readLines().map {
                val scoreEndIdx = it.indexOf(":")
                val score = it.substring(0, scoreEndIdx).toInt()
                Pair(score, it.substring(scoreEndIdx + 2))
            }.sortedBy { it.first }.reversed()
        }
    }


}

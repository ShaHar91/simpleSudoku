package com.shahar91.sudoku.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.shahar91.sudoku.Music.play
import com.shahar91.sudoku.Music.stop
import com.shahar91.sudoku.R
import com.shahar91.sudoku.data.DatabaseConnector

class Game : Activity() {
    private var ROW_ID = 0
    var menu: Menu? = null
    var timer: MenuItem? = null
    private var puzzle = IntArray(9 * 9)
    private var puzzleView: PuzzleView? = null
    private lateinit var easy: Array<String>
    private lateinit var medium: Array<String>
    private lateinit var hard: Array<String>
    var diff = 0
    var score: RatingBar? = null
    var time: TextView? = null
    private var startTime = 0L
    private val myHandler: Handler = Handler()
    var timeInMillies = 0L
    var timeSwap = 0L
    var finalTime = 0L
    var scoring = 0
    var seconds = 0
    var minutes = 0

    // DatabaseConnector databaseConnector = new DatabaseConnector(this);
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        val extras = intent.extras
        ROW_ID = extras!!.getInt("row_id")
        diff = intent.getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY)
        puzzle = getPuzzle(diff)
        intent.putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE)
        calculateUsedTiles()
        puzzleView = PuzzleView(this)
        setContentView(puzzleView)
        puzzleView!!.requestFocus()
        startTime = SystemClock.uptimeMillis()
        myHandler.postDelayed(updateTimerMethod, 1000)
    }

    private val updateTimerMethod: Runnable = object : Runnable {
        override fun run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime
            finalTime = timeSwap + timeInMillies
            seconds = (finalTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            // int milliseconds = (int) (finalTime % 1000);
            timer?.title = minutes.toString() + ":" + String.format("%02d", seconds)
            myHandler.postDelayed(this, 0)
        }
    }

    private fun getPuzzle(diff: Int): IntArray {
        val puz: String?
        when (diff) {
            DIFFICULTY_CONTINUE -> {
                hard = resources.getStringArray(R.array.hard)
                val continuePosition = hard[ROW_ID]
                puz = getPreferences(MODE_PRIVATE).getString(
                    PREF_PUZZLE,
                    continuePosition
                )
            }
            DIFFICULTY_HARD -> {
                hard = resources.getStringArray(R.array.hard)
                val hardPosition = hard[ROW_ID]
                puz = hardPosition
            }
            DIFFICULTY_MEDIUM -> {
                medium = resources.getStringArray(R.array.medium)
                val mediumPosition = medium[ROW_ID]
                puz = mediumPosition
            }
            DIFFICULTY_EASY -> {
                easy = resources.getStringArray(R.array.easy)
                val easyPosition = easy[ROW_ID]
                puz = easyPosition
            }
            else -> {
                easy = resources.getStringArray(R.array.easy)
                val easyPosition = easy[ROW_ID]
                puz = easyPosition
            }
        }
        return fromPuzzleString(puz)
    }

    fun showKeypadOrError(x: Int, y: Int) {
        val tiles = getUsedTiles(x, y)
        if (tiles!!.size == 9) {
            val toast: Toast = Toast.makeText(
                this, R.string.no_moves_label,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles))
            val v: Dialog = Keypad(this, tiles, puzzleView!!)
            v.show()
        }
    }

    fun setTileIfValid(x: Int, y: Int, value: Int): Boolean {
        val tiles = getUsedTiles(x, y)
        if (value != 0) {
            for (tile in tiles!!) {
                if (tile == value) return false
            }
        }
        setTile(x, y, value)
        if (isSolved) {
            val nagDialog = Dialog(
                this@Game
            )
            nagDialog.setCancelable(false)
            nagDialog.setContentView(R.layout.score)
            val exit: Button = nagDialog.findViewById(R.id.exitGameBtn) as Button
            val newGame: Button = nagDialog.findViewById(R.id.newGameBtn) as Button
            myHandler.removeCallbacks(updateTimerMethod)
            score = nagDialog.findViewById(R.id.dialogRateBar)
            time = nagDialog.findViewById(R.id.dialogTime)
            time?.text = timer?.title

            // int diff = getIntent().getIntExtra(KEY_DIFFICULTY,
            // DIFFICULTY_EASY);
            saveScore(diff)
            exit.setOnClickListener {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                nagDialog.dismiss()
            }
            newGame.setOnClickListener {
                finish()
            }
            nagDialog.show()
        } else {
            calculateUsedTiles()
        }
        return true
    }

    private fun saveScore(diff: Int) {
        val databaseConnector = DatabaseConnector(this)
        when (diff) {
            DIFFICULTY_CONTINUE -> {
                // hard = getResources().getStringArray(R.array.hard);
                //
                // String continuePosition = hard[ROW_ID];
                // puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE,
                // continuePosition);
                val toast = Toast.makeText(
                    this, "difficulty_continue",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
            DIFFICULTY_HARD -> {
                // hard = getResources().getStringArray(R.array.hard);
                //
                // String hardPosition = hard[ROW_ID];
                // puz = hardPosition;
                val toast2 = Toast.makeText(
                    this, "difficulty_hard",
                    Toast.LENGTH_SHORT
                )
                toast2.show()
                scoring = if (minutes < 8 || minutes == 8 && seconds < 30) {
                    3
                } else if (minutes <= 9 && seconds < 59 || minutes == 10 || minutes == 10 && seconds < 30) {
                    2
                } else if (minutes <= 11 && seconds < 59 || minutes == 12) {
                    1
                } else {
                    0
                }
                score!!.rating = scoring.toFloat()
                databaseConnector.updateScoreHard(
                    ROW_ID.toLong(), score!!.rating, time
                        ?.text.toString()
                )
            }
            DIFFICULTY_MEDIUM -> {
                // medium = getResources().getStringArray(R.array.medium);
                //
                // String mediumPosition = medium[ROW_ID];
                // puz = mediumPosition;
                val toast3 = Toast.makeText(
                    this, "difficulty_medium",
                    Toast.LENGTH_SHORT
                )
                toast3.show()
                scoring = if (minutes < 5 || minutes == 5 && seconds < 30) {
                    3
                } else if (minutes == 5 && seconds < 59 || minutes == 6) {
                    2
                } else if (minutes <= 7 && seconds < 59 || minutes == 8 || minutes == 8 && seconds < 30) {
                    1
                } else {
                    0
                }
                score!!.rating = scoring.toFloat()
                databaseConnector.updateScoreMedium(
                    ROW_ID.toLong(), score!!.rating, time
                        ?.text.toString()
                )
            }
            DIFFICULTY_EASY -> {
                val toast4 = Toast.makeText(
                    this, "difficulty_easy",
                    Toast.LENGTH_SHORT
                )
                toast4.show()
                scoring = if (minutes < 3 || minutes == 3 && seconds < 30) {
                    3
                } else if (minutes == 3 && seconds < 59 || minutes == 4 || minutes == 4 && seconds < 30) {
                    2
                } else if (minutes == 4 && seconds < 59 || minutes == 5 || minutes == 5 && seconds < 59) {
                    1
                } else {
                    0
                }
                score!!.rating = scoring.toFloat()
                databaseConnector.updateScoreEasy(
                    ROW_ID.toLong(), score!!.rating, time
                        ?.text.toString()
                )
            }
            else -> {
                val toast4 = Toast.makeText(
                    this, "difficulty_easy",
                    Toast.LENGTH_SHORT
                )
                toast4.show()
                scoring = if (minutes < 3 || minutes == 3 && seconds < 30) {
                    3
                } else if (minutes == 3 && seconds < 59 || minutes == 4 || minutes == 4 && seconds < 30) {
                    2
                } else if (minutes == 4 && seconds < 59 || minutes == 5 || minutes == 5 && seconds < 59) {
                    1
                } else {
                    0
                }
                score!!.rating = scoring.toFloat()
                databaseConnector.updateScoreEasy(
                    ROW_ID.toLong(), score!!.rating, time
                        ?.text.toString()
                )
            }
        }
    }

    private val used = Array(9) { arrayOfNulls<IntArray>(9) }
    fun getUsedTiles(x: Int, y: Int): IntArray? {
        return used[x][y]
    }

    private fun calculateUsedTiles() {
        for (x in 0..8) {
            for (y in 0..8) {
                used[x][y] = calculateUsedTiles(x, y)
            }
        }
    }

    private fun calculateUsedTiles(x: Int, y: Int): IntArray {
        val c = IntArray(9)

        // horizontal
        for (i in 0..8) {
            if (i == y) continue
            val t = getTile(x, i)
            if (t != 0) c[t - 1] = t
        }
        // vertical
        for (i in 0..8) {
            if (i == x) continue
            val t = getTile(i, y)
            if (t != 0) c[t - 1] = t
        }
        // same cell block
        val startx = x / 3 * 3
        val starty = y / 3 * 3
        for (i in startx until startx + 3) {
            for (j in starty until starty + 3) {
                if (i == x && j == y) continue
                val t = getTile(i, j)
                if (t != 0) c[t - 1] = t
            }
        }
        // compress
        var nused = 0
        for (t in c) {
            if (t != 0) nused++
        }
        val c1 = IntArray(nused)
        nused = 0
        for (t in c) {
            if (t != 0) c1[nused++] = t
        }
        return c1
    }

    private fun getTile(x: Int, y: Int): Int {
        return puzzle[y * 9 + x]
    }

    private fun setTile(x: Int, y: Int, value: Int) {
        puzzle[y * 9 + x] = value
    }

    fun getTileString(x: Int, y: Int): String {
        val v = getTile(x, y)
        return if (v == 0) "" else v.toString()
    }

    override fun onPause() {
        super.onPause()
        stop()
        if (isSolved) {
            finish()
        } else {
            getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).apply()
        }
    }

    override fun onResume() {
        super.onResume()
        play(this, R.raw.game)
    }

    val isSolved: Boolean
        get() {
            for (element in puzzle) {
                if (element == 0) return false
            }
            return true
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.game, menu)
        timer = menu.findItem(R.id.timer)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private const val TAG = "Sudoku"
        const val KEY_DIFFICULTY = "com.ShaHar91.sudoku.difficulty"
        const val DIFFICULTY_EASY = 0
        const val DIFFICULTY_MEDIUM = 1
        const val DIFFICULTY_HARD = 2
        private const val PREF_PUZZLE = "puzzle"
        protected const val DIFFICULTY_CONTINUE = -1
        private fun toPuzzleString(puz: IntArray?): String {
            val buf = StringBuilder()
            for (element in puz!!) {
                buf.append(element)
            }
            return buf.toString()
        }

        protected fun fromPuzzleString(string: String?): IntArray {
            val puz = IntArray(string!!.length)
            for (i in puz.indices) {
                puz[i] = string[i] - '0'
            }
            return puz
        }
    }
}

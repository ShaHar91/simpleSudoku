package com.shahar91.sudoku.ui.old

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.shahar91.sudoku.R
import com.shahar91.sudoku.data.DatabaseConnector
import java.io.IOException
import java.sql.SQLException

class MainActivity : Activity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_old)
        val myDbHelper = DatabaseConnector(this@MainActivity)
        try {
            myDbHelper.createDataBase()
        } catch (ioe: IOException) {
            throw Error("Unable to create database")
        }
        try {
            DatabaseConnector.openDataBase()
        } catch (sqle: SQLException) {
            throw sqle
        }
        methodHolder()
    }

    private fun methodHolder() {
        val continueButton: View = findViewById(R.id.continueBtn)
        continueButton.setOnClickListener(this)
        val newButton: View = findViewById(R.id.newBtn)
        newButton.setOnClickListener(this)
        val aboutButton: View = findViewById(R.id.aboutBtn)
        aboutButton.setOnClickListener(this)
        val exitButton: View = findViewById(R.id.exitBtn)
        exitButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.newBtn -> openNewGameDialog()
//            R.id.aboutBtn -> {
//                val i = Intent(this, About::class.java)
//                startActivity(i)
//            }
            R.id.exitBtn -> {
                finish()
            }
            else -> {
            }
        }
    }

    private fun openNewGameDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.new_game_title)
            .setItems(R.array.difficulty
            ) { _, i -> startGame(i) }.show()
    }

    private fun startGame(i: Int) {
        Log.d(TAG, "clicked on $i")
        val intent = Intent(this@MainActivity, GameList::class.java)
        intent.putExtra(Game.KEY_DIFFICULTY, i)
        startActivity(intent)
    }

//    private fun continueGame(i: Int) {
//        Log.d(TAG, "clicked on $i")
//        val intent = Intent(this@MainActivity, Game::class.java)
//        intent.putExtra(Game.KEY_DIFFICULTY, i)
//        startActivity(intent)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this, Prefs::class.java))
                return true
            }
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        Music.play(this, R.raw.main)
    }

    override fun onPause() {
        super.onPause()
        Music.pause()
    }

    override fun onDestroy() {
        Music.stop()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "Sudoku"
    }
}
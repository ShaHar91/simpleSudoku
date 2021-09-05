package com.shahar91.sudoku.data

import android.content.ContentValues

import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteException

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException

import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Error

class DatabaseConnector(private val myContext: Context) : SQLiteOpenHelper(myContext, DB_NAME, null, 1) {

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    @Throws(IOException::class)
    fun createDataBase() {
        val dbExist = checkDataBase()
        if (!dbExist) {
            // By calling this method an empty database will be created into the
            // default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.writableDatabase
            try {
                copyDataBase()
            } catch (e: IOException) {
                throw Error("Error copying database")
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(
                myPath, null,
                SQLiteDatabase.OPEN_READWRITE
            )
        } catch (e: SQLiteException) {
            // database does't exist yet.
        }
        checkDB?.close()
        return checkDB != null
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    @Throws(IOException::class)
    private fun copyDataBase() {

        // Open your local db as the input stream
        val myInput: InputStream = myContext.assets.open(DB_NAME)

        // Path to the just created empty db
        val outFileName = DB_PATH + DB_NAME

        // Open the empty db as the output stream
        val myOutput: OutputStream = FileOutputStream(outFileName)

        // transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int
        while (myInput.read(buffer).also { length = it } > 0) {
            myOutput.write(buffer, 0, length)
        }

        // Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()
    }

    @Synchronized
    override fun close() {
        if (myDataBase != null) myDataBase!!.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {}
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    fun updateScoreEasy(id: Long, score: Float, time: String?) {
        val editScore = ContentValues()
        editScore.put("score", score)
        editScore.put("time", time)
        openDataBase() // open the database
        myDataBase!!.update("easy", editScore, "_id=$id", null)
    } // end method insertBook

    fun updateScoreMedium(id: Long, score: Float, time: String?) {
        val editScore = ContentValues()
        editScore.put("score", score)
        editScore.put("time", time)
        openDataBase() // open the database
        myDataBase!!.update("medium", editScore, "_id=$id", null)
    } // end method insertBook

    fun updateScoreHard(id: Long, score: Float, time: String?) {
        val editScore = ContentValues()
        editScore.put("score", score)
        editScore.put("time", time)
        openDataBase() // open the database
        myDataBase!!.update("hard", editScore, "_id=$id", null)
    } // end method insertBook

    fun getEasyGame(id: Long): Cursor {
        return myDataBase!!.query(
            "easy", null, "_id=$id", null, null, null,
            null
        )
    }

    val allEasy: Cursor
        get() = myDataBase!!.query("easy", null, null, null, null, null, "_id")

    fun getMediumGame(id: Long): Cursor {
        return myDataBase!!.query(
            "medium", null, "_id=$id", null, null, null,
            null
        )
    }

    val allMedium: Cursor
        get() = myDataBase!!.query("medium", null, null, null, null, null, "_id")

    fun getHardGame(id: Long): Cursor {
        return myDataBase!!.query(
            "hard", null, "_id=$id", null, null, null,
            null
        )
    }

    val allHard: Cursor
        get() = myDataBase!!.query("hard", null, null, null, null, null, "_id")

    companion object {
        @SuppressLint("SdCardPath")
        private val DB_PATH = "/data/data/com.shahar91.sudoku/databases/"
        private const val DB_NAME = "sudoku.sqlite"
        private var myDataBase: SQLiteDatabase? = null

        @Throws(SQLException::class)
        fun openDataBase() {
            // Open the database
            val myPath = DB_PATH + DB_NAME
            myDataBase = SQLiteDatabase.openDatabase(
                myPath, null,
                SQLiteDatabase.OPEN_READWRITE
            )
        }
    }

}
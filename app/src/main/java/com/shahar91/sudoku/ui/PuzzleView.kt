package com.shahar91.sudoku.ui

import android.os.Bundle

import android.os.Parcelable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

import android.view.MotionEvent

import com.shahar91.sudoku.Prefs

import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import com.shahar91.sudoku.R
import kotlin.math.max
import kotlin.math.min


class PuzzleView(context: Context) : View(context) {
    private val game: Game = context as Game

    private var width = 0f // width of one tile
    private var height = 0f // height of one tile
    private var selX = 0 // X index of selection
    private var selY = 0 // Y index of selection

    private val selRect: Rect = Rect()

    companion object {
        private const val TAG = "Sudoku"
        private const val ID = 42
        private const val SELX = "selX"
        private const val SELY = "selY"
        private const val VIEW_STATE = "viewState"
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        id = ID
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = w / 9f
        height = h / 9f
        getRect(selX, selY, selRect)
        Log.d(TAG, "onSizeChanged: width $width, height $height")
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun getRect(x: Int, y: Int, rect: Rect) {
        rect.set(
            (x * width).toInt(), (y * height).toInt(),
            (x * width + width).toInt(), (y * height + height).toInt()
        )
    }

    override fun onDraw(canvas: Canvas) {

        // drawing background
        val background = Paint()
        background.color = ContextCompat.getColor(context, R.color.puzzle_background)
        canvas.drawRect(0f, 0f, getWidth().toFloat(), getHeight().toFloat(), background)

        // draw the board, define colors for the grid lines
        val dark = Paint()
        dark.color = ContextCompat.getColor(context, R.color.puzzle_dark)
        val hilite = Paint()
        hilite.color = ContextCompat.getColor(context, R.color.puzzle_hilite)
        val light = Paint()
        light.color = ContextCompat.getColor(context, R.color.puzzle_light)

        // draw the minor grid lines
        for (i in 0..8) {
            canvas.drawLine(0f, i * height, getWidth().toFloat(), i * height, light)
            canvas.drawLine(
                0f, i * height + 1, getWidth().toFloat(), i * height + 1,
                hilite
            )
            canvas.drawLine(i * width, 0f, i * width, getHeight().toFloat(), light)
            canvas.drawLine(
                i * width + 1, 0f, i * width + 1, getHeight().toFloat(),
                hilite
            )
        }

        // draw the major grid lines
        for (i in 0..8) {
            if (i % 3 != 0) continue
            canvas.drawLine(0f, i * height, getWidth().toFloat(), i * height, dark)
            canvas.drawLine(
                0f, i * height + 1, getWidth().toFloat(), i * height + 1,
                hilite
            )
            canvas.drawLine(i * width, 0f, i * width, getHeight().toFloat(), dark)
            canvas.drawLine(
                i * width + 1, 0f, i * width + 1, getHeight().toFloat(),
                hilite
            )
        }

        // draw the numbers, define color and style for numbers
        val foreground = Paint(Paint.ANTI_ALIAS_FLAG)
        foreground.color = ContextCompat.getColor(context, R.color.puzzle_foreground)
        foreground.style = Paint.Style.FILL
        foreground.textSize = height * 0.75f
        foreground.textScaleX = width / height
        foreground.textAlign = Paint.Align.CENTER

        // Draw the number in the center of the tile
        val fm: Paint.FontMetrics = foreground.fontMetrics
        // Centering in X: use alignment (and X at midpoint)
        val x = width / 2
        // Centering in Y: measure ascent/descent first
        val y: Float = height / 2 - (fm.ascent + fm.descent) / 2
        for (i in 0..8) {
            for (j in 0..8) {
                canvas.drawText(
                    game.getTileString(i, j), i * width + x, j
                            * height + y, foreground
                )
            }
        }
        Log.d(TAG, "selRect=$selRect")
        val selected = Paint()
        selected.color = ContextCompat.getColor(context, R.color.puzzle_selected)
        canvas.drawRect(selRect, selected)
        if (Prefs.getHints(context)) {
            val hint = Paint()
            val c = intArrayOf(
                ContextCompat.getColor(context, R.color.puzzle_hint_0),
                ContextCompat.getColor(context, R.color.puzzle_hint_1),
                ContextCompat.getColor(context, R.color.puzzle_hint_2)
            )
            val r = Rect()
            for (i in 0..8) {
                for (j in 0..8) {
                    val movesleft: Int = 9 - (game.getUsedTiles(i, j)?.size ?: 0)
                    if (movesleft < c.size) {
                        getRect(i, j, r)
                        hint.color = c[movesleft]
                        canvas.drawRect(r, hint)
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.d(TAG, "onKeyDown: keycode=$keyCode, event=$event")
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> select(selX, selY - 1)
            KeyEvent.KEYCODE_DPAD_DOWN -> select(selX, selY + 1)
            KeyEvent.KEYCODE_DPAD_LEFT -> select(selX - 1, selY)
            KeyEvent.KEYCODE_DPAD_RIGHT -> select(selX + 1, selY)
            KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_SPACE -> setSelectedTile(0)
            KeyEvent.KEYCODE_1 -> setSelectedTile(1)
            KeyEvent.KEYCODE_2 -> setSelectedTile(2)
            KeyEvent.KEYCODE_3 -> setSelectedTile(3)
            KeyEvent.KEYCODE_4 -> setSelectedTile(4)
            KeyEvent.KEYCODE_5 -> setSelectedTile(5)
            KeyEvent.KEYCODE_6 -> setSelectedTile(6)
            KeyEvent.KEYCODE_7 -> setSelectedTile(7)
            KeyEvent.KEYCODE_8 -> setSelectedTile(8)
            KeyEvent.KEYCODE_9 -> setSelectedTile(9)
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                game.showKeypadOrError(selX, selY)
                if (game.isSolved) {
//                    val toast = Toast.makeText(
//                        game, "congrats",
//                        Toast.LENGTH_SHORT
//                    )
                }
            }
            else -> return super.onKeyDown(keyCode, event)
        }
        return true
    }

    private fun select(x: Int, y: Int) {
        invalidate(selRect)
        selX = min(max(x, 0), 8)
        selY = min(max(y, 0), 8)
        getRect(selX, selY, selRect)
        invalidate(selRect)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_DOWN) return super.onTouchEvent(event)
        select((event.x / width).toInt(), (event.y / height).toInt())
        game.showKeypadOrError(selX, selY)
        Log.d(TAG, "onTouchEvent: x $selX, y $selY")
//        for (i in 0..8) {
//            for (j in 0..8) {
//            }
//        }
        return true
    }

    fun setSelectedTile(tile: Int) {
        if (game.setTileIfValid(selX, selY, tile)) {
            invalidate() // may change hints
        } else {
            // number is not valid for this tile
            Log.d(TAG, "selSelectedTile: invalid: $tile")
            startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake))
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        Log.d(TAG, "onRestoreInstanceStat")
        val bundle = state as Bundle
        select(bundle.getInt(SELX), bundle.getInt(SELY))
        super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE))
    }

    override fun onSaveInstanceState(): Parcelable {
        val p: Parcelable? = super.onSaveInstanceState()
        Log.d(TAG, "onSaveInstanceState")
        val bundle = Bundle()
        bundle.putInt(SELX, selX)
        bundle.putInt(SELY, selY)
        bundle.putParcelable(VIEW_STATE, p)
        return bundle
    }
}
package com.shahar91.sudoku.ui.main.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import be.appwise.core.ui.base.list.BaseViewHolder
import com.shahar91.sudoku.data.database.entity.SudokuGame
import com.shahar91.sudoku.databinding.ListItemSudokuBinding

class SudokuAdapter(
    private val sudokuListener: SudokuListener
) : ListAdapter<SudokuGame, SudokuAdapter.SudokuViewHolder>(SUDOKU_DIFF) {

    companion object {
        private val SUDOKU_DIFF = object : DiffUtil.ItemCallback<SudokuGame>() {
            override fun areItemsTheSame(oldItem: SudokuGame, newItem: SudokuGame): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SudokuGame, newItem: SudokuGame): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun interface SudokuListener {
        fun onSudokuClicked(sudokuId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SudokuAdapter.SudokuViewHolder {
        return SudokuViewHolder(ListItemSudokuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SudokuAdapter.SudokuViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    inner class SudokuViewHolder(private val binding: ListItemSudokuBinding) : BaseViewHolder<SudokuGame>(binding.root) {
        override fun bind(item: SudokuGame) {
            binding.sudokuGame = item

            binding.root.setOnClickListener { sudokuListener.onSudokuClicked(item.id) }
        }
    }
}
package com.shahar91.sudoku.ui.main.list

import android.os.Bundle
import android.view.View
import be.appwise.core.extensions.view.setupRecyclerView
import com.shahar91.sudoku.R
import com.shahar91.sudoku.databinding.FragmentSudokuListBinding
import com.shahar91.sudoku.ui.base.AppBaseBindingVMFragment
import com.shahar91.sudoku.ui.main.list.adapter.SudokuAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SudokuListFragment : AppBaseBindingVMFragment<FragmentSudokuListBinding>() {

    private val sudokuAdapter = SudokuAdapter {

    }

    override val mViewModel: SudokuListViewModel by viewModel()
    override fun getLayout() = R.layout.fragment_sudoku_list
    override fun getToolbar() = mBinding.mtbToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            viewModel = mViewModel
        }

        initViews()
        initObservers()
    }

    private fun initViews() {
        mBinding.run {
            rvSudokuList.run {
                setupRecyclerView()
                adapter = sudokuAdapter
            }
        }
    }

    private fun initObservers() {
        mViewModel.sudokusLive.observe(viewLifecycleOwner) {
            sudokuAdapter.submitList(it)
        }
    }
}
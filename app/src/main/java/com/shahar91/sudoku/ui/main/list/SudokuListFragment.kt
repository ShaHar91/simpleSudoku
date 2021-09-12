package com.shahar91.sudoku.ui.main.list

import android.os.Bundle
import android.view.View
import com.shahar91.sudoku.R
import com.shahar91.sudoku.databinding.FragmentSudokuListBinding
import com.shahar91.sudoku.ui.base.AppBaseBindingVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SudokuListFragment : AppBaseBindingVMFragment<FragmentSudokuListBinding>() {

    override val mViewModel: SudokuListViewModel by viewModel()
    override fun getLayout() = R.layout.fragment_sudoku_list
    override fun getToolbar() = mBinding.mtbToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            viewModel = mViewModel
        }
    }
}
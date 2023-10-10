package com.shahar91.sudoku.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.shahar91.sudoku.R
import com.shahar91.sudoku.databinding.FragmentHomeBinding
import com.shahar91.sudoku.ui.base.AppBaseBindingVMFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : AppBaseBindingVMFragment<FragmentHomeBinding>() {

    override val mViewModel: HomeViewModel by viewModel()
    override fun getLayout() = R.layout.fragment_home
    override fun getToolbar() = mBinding.mtbToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.run {
            viewModel = mViewModel
        }

        initViews()
    }

    private fun initViews() {
        mBinding.run {
            mtbNewGame.setOnClickListener {
                HomeFragmentDirections.actionHomeFragmentToSudokuListFragment().run(findNavController()::navigate)
            }
        }
    }
}
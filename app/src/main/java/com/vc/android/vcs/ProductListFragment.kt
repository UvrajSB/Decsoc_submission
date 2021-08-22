package com.vc.android.vcs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ramotion.foldingcell.FoldingCell

class ProductListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val foldingCell1 = view.findViewById<FoldingCell>(R.id.folding_cell1)
        foldingCell1.initialize(50, 2000, Color.DKGRAY, 0)
        foldingCell1.setOnClickListener {
            foldingCell1.toggle(false)
        }
        val foldingCell2 = view.findViewById<FoldingCell>(R.id.folding_cell2)
        foldingCell2.initialize(30, 2000, Color.DKGRAY, 0)
        foldingCell2.setOnClickListener {
            foldingCell2.toggle(false)
        }
        val foldingCell = view.findViewById<FoldingCell>(R.id.folding_cell)
        foldingCell.initialize(50, 500, Color.DKGRAY, 0)
        foldingCell.setOnClickListener {
            foldingCell.toggle(false)
        }
    }
}
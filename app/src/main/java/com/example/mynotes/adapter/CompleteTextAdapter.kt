package com.example.mynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.mynotes.R
import com.example.mynotes.databinding.ItemAutoCompleteTextViewBinding
import com.example.mynotes.entities.Notes

class CompleteTextAdapter(
    context: Context,
    objects: List<Notes>,
    val click: ClickItemSearch
) : ArrayAdapter<Notes>(context, R.layout.item_auto_complete_text_view,objects){
    private val inflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemAutoCompleteTextViewBinding.inflate(inflater,parent,false)
        val item = getItem(position)
        binding.tvTitle.text = item?.title.toString()
        binding.tvSubTitle.text = item?.subTitle.toString()
        binding.itemCompleteText.setOnClickListener {
            item?.let { it1 -> click.clickSearch(it1) }
        }
        return binding.root
    }

    interface ClickItemSearch{
        fun clickSearch(notes: Notes)
    }
}
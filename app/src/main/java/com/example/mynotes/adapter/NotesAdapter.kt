package com.example.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.ItemRvNoteBinding
import com.example.mynotes.entities.Notes

class NotesAdapter(var listNotes: List<Notes>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRvNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun getNotes(note: Notes){
                binding.tvNoteTitle.text = note.title
                binding.tvSubNoteTitle.text = note.subTitle
                binding.tvDateTime.text = note.dateTime
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        listNotes?.let {
            return it.size
        }
        return 0;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listNotes[position]
        holder.getNotes(note)
    }
}
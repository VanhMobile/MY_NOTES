package com.example.mynotes.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.databinding.ItemRvNoteBinding
import com.example.mynotes.entities.Notes

class NotesAdapter(var listNotes: List<Notes>,val click: ClickItemNote) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemRvNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun getNotes(note: Notes){
                binding.tvNoteTitle.text = note.title
                binding.tvSubNoteTitle.text = note.subTitle
                binding.tvDateTime.text = note.dateTime
                when(note.noteColor){
                    "Magenta" -> {
                        binding.backgroundItem.setBackgroundResource(R.drawable.backroung_magenta_item_note)
                    }
                    "Orange" ->{
                        binding.backgroundItem.setBackgroundResource(R.drawable.backroung_orange_item_note)
                    }
                    "Tomato" -> {
                        binding.backgroundItem.setBackgroundResource(R.drawable.backroung_tomato_item_note)
                    }
                    "Salmon" -> {
                        binding.backgroundItem.setBackgroundResource(R.drawable.backroung_salmon_item_note)
                    }
                    "Yellow" -> {
                        binding.backgroundItem.setBackgroundResource(R.drawable.backroung_yellow_item_note)
                    }
                }

                if (note.imgPath.isNotEmpty()){
                    val bitmap = BitmapFactory.decodeFile(note.imgPath)
                    binding.imgNotes.setImageBitmap(bitmap)
                    binding.imgNotes.visibility = View.VISIBLE
                }else{
                    binding.imgNotes.visibility = View.GONE
                }

                binding.backgroundItem.setOnClickListener {
                    click.upDataNote(note)
                }

                binding.backgroundItem.setOnLongClickListener {
                    click.deleteNote(note)
                    true
                }
            }
    }

    interface ClickItemNote{
        fun deleteNote(note: Notes)
        fun upDataNote(note: Notes)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRvNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

     fun setData(list: List<Notes>){
        this.listNotes = list
        notifyDataSetChanged()
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




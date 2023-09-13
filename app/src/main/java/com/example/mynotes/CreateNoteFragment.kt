package com.example.mynotes

import android.nfc.cardemulation.HostNfcFService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mynotes.dataBase.NotesDataBase
import com.example.mynotes.databinding.FragmentCreateNoteBinding
import com.example.mynotes.entities.NotesBuilder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class CreateNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var currentDate: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNoteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding.tvDatTime.text = currentDate
        binding.btnTick.setOnClickListener {
            saveNote()
        }
        binding.btnBack.setOnClickListener {
            replaceFragment(HomeFragment(),false)
        }

    }

    private fun saveNote() {
        if(binding.edtNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Note title is required",Toast.LENGTH_SHORT).show()
        }

        if (binding.edtSubNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Note sub title is required",Toast.LENGTH_SHORT).show()
        }
        if (binding.edtDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"Note Description is required",Toast.LENGTH_SHORT).show()
        }

        launch {
            val note = NotesBuilder()
                .setTitle(binding.edtNoteTitle.text.toString())
                .setSubTitle(binding.edtSubNoteTitle.text.toString())
                .setContent(binding.edtDesc.text.toString())
                .setDateTime(currentDate!!)
                .build()
            context?.let { NotesDataBase.getDatabase(it).noteDao().insertNote(note) }
        }
        replaceFragment(HomeFragment(),false)
    }

    private fun replaceFragment(fragment: Fragment, isTransition: Boolean){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        if (isTransition){
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransaction.replace(R.id.fragmentLayout,fragment).commit()
    }

}
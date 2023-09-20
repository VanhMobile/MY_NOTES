package com.example.mynotes

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.dataBase.NotesDataBase
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.entities.Notes


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter
    private var listNotes: List<Notes> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context?.let {
            listNotes = NotesDataBase.getDatabase(it).noteDao().getAll()
        }
        notesAdapter = NotesAdapter(listNotes,requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = notesAdapter
        binding.btnAdd.setOnClickListener {
            replaceFragment(CreateNoteFragment(),true)
        }
        return binding.root
    }


    private fun replaceFragment(fragment: Fragment, isTransition: Boolean){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        if (isTransition){
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransaction.replace(R.id.fragmentLayout,fragment).commit()
    }

}
package com.example.mynotes

import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.adapter.CompleteTextAdapter
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.dataBase.NotesDataBase
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.entities.Notes


class HomeFragment : BaseFragment(), NotesAdapter.ClickItemNote,
    CompleteTextAdapter.ClickItemSearch {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter
    private var listNotes: List<Notes> = listOf()
    private lateinit var searchAdapter: CompleteTextAdapter


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
        searchAdapter = CompleteTextAdapter(requireContext(),listNotes,this)
        notesAdapter = NotesAdapter(listNotes,this)
        binding.autoCompleteTextView.setAdapter(searchAdapter)
        binding.autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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

    override fun deleteNote(note: Notes) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Thông báo")
        dialog.setMessage("bạn chắc chắn muốn xóa?")
        dialog.setPositiveButton("oke"){ dialog,which ->
            context?.let {
                NotesDataBase.getDatabase(it).noteDao().deleteNote(note)
                notesAdapter.setData(NotesDataBase.getDatabase(it).noteDao().getAll())
            }
            Toast.makeText(context,"Xóa thành công", Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton("cancel"){ dialog,which ->
            dialog.cancel()
        }
        dialog.show()
    }

    override fun upDataNote(note: Notes) {
        val bundle = Bundle()
        bundle.putSerializable("Note",note)
        val createNoteFragment = CreateNoteFragment()
        createNoteFragment.arguments = bundle
        replaceFragment(createNoteFragment,true)
    }

    override fun clickSearch(notes: Notes) {
        val bundle = Bundle()
        bundle.putSerializable("Note",notes)
        val createNoteFragment = CreateNoteFragment()
        createNoteFragment.arguments = bundle
        replaceFragment(createNoteFragment,true)
    }

}
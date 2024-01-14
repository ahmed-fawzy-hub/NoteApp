package com.example.noteapp

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.noteapp.databinding.FragmentAddNoteBinding


class AddNoteFragment : Fragment() {
    lateinit var binding:FragmentAddNoteBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAddNoteBinding.inflate(layoutInflater)
       return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.addnote_menu, menu)
        binding.buAddNotes.setOnClickListener {
            addNotesEvent()
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun addNotesEvent() {
        val title=binding.etTitle.text.toString()
        val description=binding.etDescription.text.toString()
        val values=ContentValues()
        values.put("Title",title)
        values.put("Description",description)
        val dbManager=DBManager(this!!.requireActivity())

        if (id!=0){
            val selectionArgs= arrayOf(id.toString())
            val id=dbManager.Update(values,"ID=?",selectionArgs)
            if (id > 0) {
                Toast.makeText(this!!.requireActivity(), "Record is update", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this!!.requireActivity(), "Fail to update record", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        else{
            val id=dbManager.Insert(values)
            if (id > 0) {
                Toast.makeText(this!!.requireActivity(), "Record is added", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this!!.requireActivity(), "Fail to add record", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId){
            R.id.backBu ->{
                requireView().findNavController().navigate(R.id.notesListFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
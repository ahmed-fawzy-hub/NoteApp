package com.example.noteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.noteapp.databinding.FragmentAddNoteBinding
import com.example.noteapp.databinding.FragmentNotesListBinding


/**
 * A simple [Fragment] subclass.
 * Use the [NotesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotesListFragment : Fragment() {
    lateinit var binding: FragmentNotesListBinding
    val listNotes = ArrayList<Note>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotesListBinding.inflate(layoutInflater)
        return binding.root
    }

    var id:Int?=0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        listNotes.add(Note(1," meet professor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//        listNotes.add(Note(2," meet doctor","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
//         listNotes.add(Note(3," meet friend","Create any pattern of your own - tiles, texture, skin, wallpaper, comic effect, website background and more.  Change any artwork of pattern you found into different flavors and call them your own."))
        querySearch("%")
         id=arguments?.getInt("ID")
        if (id!=0) {
            val title = arguments?.getString("name")
            binding.etTitle.setText(title)
            val description = arguments?.getString("des")
            binding.etDescription.setText(description)
        }
        setHasOptionsMenu(true)
    }

    fun querySearch(noteTitle: String) {
        var dbManager = DBManager(this!!.requireActivity())
        val projection = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(noteTitle)
        val cursor = dbManager.Query(projection, "Title like ?", selectionArgs, "Title")
        if (cursor.moveToNext()) {
            listNotes.clear()
            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(id, title, description))
            } while (cursor.moveToNext())
            var myAdapter = MyNotesAdapter(this!!.requireActivity(), listNotes)
            binding.lvNotes.adapter = myAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.noteslist_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.addNotesBu -> {
                requireView().findNavController()
                    .navigate(R.id.action_notesListFragment_to_addNoteFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter : BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context: Context? = null

        constructor(context: Context, listNotesAdapter: ArrayList<Note>) {
            this.context = context
            this.listNotesAdapter = listNotesAdapter
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {

            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticcket, null)
            val note = listNotesAdapter[p0]
            myView.findViewById<TextView>(R.id.tvTitle).text = note.nodeName
            myView.findViewById<TextView>(R.id.tvDes).text = note.nodeDes
            myView.findViewById<ImageView>(R.id.ivDelete).setOnClickListener {
                var dbManager = DBManager(this.context!!)
                val selectionArgs = arrayOf(note.nodeID.toString())
                dbManager.Delete("ID=?", selectionArgs)
                querySearch("%")
                myView.findViewById<TextView>(R.id.tvTitle).text = note.nodeName
                myView.findViewById<TextView>(R.id.tvDes).text = note.nodeDes


            }
            myView.findViewById<EditText>(R.id.ivEdit).setOnClickListener{

                GoToUpdate(note)

            }
            return myView
        }
    }

    private fun GoToUpdate(myNote: Note) {
       var bundle=Bundle()
        bundle.putInt("ID",myNote.nodeID!!)
        bundle.putString("name",myNote.nodeName!!)
        bundle.putString("des",myNote.nodeDes!!)
        requireView().findNavController().navigate(R.id.action_notesListFragment_to_addNoteFragment,bundle)

    }
}
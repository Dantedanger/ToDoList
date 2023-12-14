package com.sample.todolist

import com.google.android.material.chip.Chip

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.Date
import java.util.UUID

private const val TAG = "Fragment"
private const val ARG_LIST_ID = "id"

@Suppress("DEPRECATION")

class Fragment : Fragment() {
    private lateinit var list: ListItem
    private lateinit var titleField: EditText
    private lateinit var priorityChip: Chip
    private lateinit var addlistb: Button

    private val listDetailViewModel:
            ListDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ListDetailViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = ListItem()
        val listId: UUID = arguments?.getSerializable(ARG_LIST_ID) as UUID
        listDetailViewModel.loadList(listId)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment, container, false)
        titleField = view.findViewById(R.id.list_title) as EditText
        addlistb = view.findViewById(R.id.add) as Button
        //priorityChip = view.findViewById(R.id.add) as Button
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listDetailViewModel.listLiveData.observe(
            viewLifecycleOwner,
            Observer { list ->
                list?.let {
                    list.also { this.list = it }
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
// Это пространство оставлено пустым специально
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                list.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
// И это
            }
        }
        titleField.addTextChangedListener(titleWatcher)
        addlistb.setOnClickListener {
            listDetailViewModel.saveList(list)
            Toast.makeText(
                context,
                "Задача добавлена",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    private fun updateUI() {
        titleField.setText(list.title)
    }

    companion object {
        fun newInstance(listId: UUID): Fragment {
            val args = Bundle().apply {
                putSerializable(ARG_LIST_ID, listId)
            }
            return Fragment().apply {
                arguments = args
            }
        }
    }


}
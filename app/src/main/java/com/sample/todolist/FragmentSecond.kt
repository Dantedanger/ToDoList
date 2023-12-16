package com.sample.todolist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.UUID

private const val TAG = "FragmentSecond"
private const val ARG_LIST_ID = "list_id"

@Suppress("DEPRECATION")

class FragmentSecond : Fragment() {
    interface Callbacks {
        fun setToolbarTitle(title: String)
    }
    private var callbacks: Callbacks? = null
    private lateinit var list: ListItem
    private lateinit var titleField: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var radioButton2: RadioButton
    private lateinit var radioButton3: RadioButton
    private lateinit var addlistb: Button

    private val listDetailViewModel:
            ListDetailViewModel by lazy {
        ViewModelProviders.of(this).get(ListDetailViewModel::class.java)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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
        callbacks?.setToolbarTitle("Add a New Task")
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        titleField = view.findViewById(R.id.list_title) as EditText
        addlistb = view.findViewById(R.id.add) as Button
        radioGroup = view.findViewById(R.id.radio_group) as RadioGroup
        radioButton = view.findViewById(R.id.radioButton) as RadioButton
        radioButton2 = view.findViewById(R.id.radioButton2) as RadioButton
        radioButton3 = view.findViewById(R.id.radioButton3) as RadioButton
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
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton -> {
                    list.priority = 1
                }
                R.id.radioButton2 -> {
                    list.priority = 2
                }
                R.id.radioButton3 -> {
                    list.priority = 3
                }
            }
        }
        addlistb.setOnClickListener {
            if (!(radioButton.isChecked or radioButton2.isChecked or radioButton3.isChecked)){
                Toast.makeText(
                    context,
                    "Не выбран приоритет",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                listDetailViewModel.saveList(list)
                Toast.makeText(
                    context,
                    "Задача добавлена",
                    Toast.LENGTH_SHORT
                ).show()
                fragmentManager?.popBackStack()
            }
        }
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
    private fun updateUI() {
        titleField.setText(list.title)
        if (list.priority == 1)
            radioButton.isChecked = true
        if (list.priority == 2)
            radioButton2.isChecked = true
        if (list.priority == 3)
            radioButton3.isChecked = true
    }

    companion object {
        fun newInstance(listId: UUID): FragmentSecond {
            val args = Bundle().apply {
                putSerializable(ARG_LIST_ID, listId)
            }
            return FragmentSecond().apply {
                arguments = args
            }
        }
    }


}
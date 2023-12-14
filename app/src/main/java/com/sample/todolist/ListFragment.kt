@file:Suppress("DEPRECATION")

package com.sample.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.todolist.ListItem
import com.sample.todolist.ListViewModel
import com.sample.todolist.R
import java.util.UUID

private const val TAG = "ListFragment"

class ListFragment : Fragment() {
    /**
     * Требуемый интерфейс
     */
    interface Callbacks {
        fun onListSelected(crimeId: UUID)
    }
    private var callbacks: Callbacks? = null
    private lateinit var listRecyclerView: RecyclerView
    private var adapter: ListAdapter? = ListAdapter(emptyList())
    private val listViewModel:
            ListViewModel by lazy {
        ViewModelProviders.of(this).get(ListViewModel::class.java)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_list, container, false)
        listRecyclerView = view.findViewById(R.id.list_recycler_view) as RecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listViewModel.ListLiveData.observe(
            viewLifecycleOwner,
            Observer { list ->
                list?.let {
                    Log.i(TAG, "Got lists${list.size}")
                    updateUI(list)
                }
            })
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(crimes: List<ListItem>) {
        adapter = ListAdapter(crimes)
        listRecyclerView.adapter = adapter
    }

    private inner class ListHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var list: ListItem
        private val titleTextView: TextView = itemView.findViewById(R.id.title)
        private val priorityTextView: TextView = itemView.findViewById(R.id.priority)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(list: ListItem) {
            this.list = list
            titleTextView.text = this.list.title
            priorityTextView.text = this.list.priority
        }
        override fun onClick(v: View) {
            callbacks?.onListSelected(list.id)
        }

    }


    private inner class ListAdapter(var lists: List<ListItem>)
        : RecyclerView.Adapter<ListHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : ListHolder {
            val view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return ListHolder(view)
        }
        override fun getItemCount() = lists.size
        override fun onBindViewHolder(holder: ListHolder, position: Int) {
            val list = lists[position]
            holder.bind(list)
        }

    }
    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }
}

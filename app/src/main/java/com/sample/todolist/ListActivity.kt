package com.sample.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.UUID

class ListActivity : AppCompatActivity(), ListFragment.Callbacks, FragmentSecond.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = ListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }
    override fun onListSelected(listId: UUID)
    {
        val fragment = FragmentSecond.newInstance(listId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    override fun setToolbarTitle(title: String){
        supportActionBar?.title = title
    }

}
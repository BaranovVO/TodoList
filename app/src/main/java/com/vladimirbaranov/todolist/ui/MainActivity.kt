package com.vladimirbaranov.todolist.ui

import android.os.Bundle
import com.vladimirbaranov.todolist.R
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentContainer, TodoListFragment())
                .commit()
        }
    }
}
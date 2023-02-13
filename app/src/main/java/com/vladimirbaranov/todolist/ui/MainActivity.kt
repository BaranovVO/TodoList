package com.vladimirbaranov.todolist.ui

import android.os.Bundle
import androidx.activity.addCallback
import com.vladimirbaranov.todolist.R
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 1) {
                supportFragmentManager.popBackStack()
            } else {
                moveTaskToBack(true)
            }
        }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentContainer, TodoListFragment())
                .addToBackStack(null)
                .commit()
            supportFragmentManager.executePendingTransactions()
        }
    }
}
package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    private val taskList = ArrayList<Task>()
    private lateinit var adapter: TaskAdapter

    private fun saveTasks() {
        val sharedPref = getSharedPreferences("tasks", MODE_PRIVATE)
        val editor = sharedPref.edit()

        val gson = com.google.gson.Gson()
        val json = gson.toJson(taskList)

        editor.putString("taskList", json)
        editor.apply()
    }

    private fun loadTasks() {
        val sharedPref = getSharedPreferences("tasks", MODE_PRIVATE)
        val gson = com.google.gson.Gson()

        val json = sharedPref.getString("taskList", null)

        val type = object : com.google.gson.reflect.TypeToken<ArrayList<Task>>() {}.type

        if (json != null) {
            val savedList: ArrayList<Task> = gson.fromJson(json, type)
            taskList.clear()
            taskList.addAll(savedList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        fabAdd = findViewById(R.id.fabAdd)

        // Setup RecyclerView
        adapter = TaskAdapter(taskList) {
            saveTasks()   // ✅ pass function here
        }
        loadTasks()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add button click
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Get data from AddTaskActivity
        val sharedPref = getSharedPreferences("tasks", MODE_PRIVATE)

        val newTask = sharedPref.getString("newTask", null)
        val updatedTask = sharedPref.getString("updatedTask", null)
        val updatedPosition = sharedPref.getInt("updatedPosition", -1)

        if (updatedTask != null && updatedPosition != -1) {
            taskList[updatedPosition] = Task(updatedTask)
            saveTasks()
            sharedPref.edit().remove("updatedTask").remove("updatedPosition").apply()
        }
        else if (newTask != null) {
            taskList.add(Task(newTask))
            saveTasks()
            sharedPref.edit().remove("newTask").apply()
        }

            adapter.notifyDataSetChanged()

        // Clear after adding
        sharedPref.edit().remove("newTask").apply()

        }



    }

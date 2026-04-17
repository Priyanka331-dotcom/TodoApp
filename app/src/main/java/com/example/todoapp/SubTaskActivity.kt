package com.example.todoapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SubTaskActivity : AppCompatActivity() {

    private lateinit var etSubTask: EditText
    private lateinit var btnAddSubTask: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var taskList: ArrayList<Task>
    private lateinit var subTaskList: ArrayList<String>
    private lateinit var adapter: SubTaskAdapter    // ✅ FIXED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_task)

        etSubTask = findViewById(R.id.etSubTask)
        btnAddSubTask = findViewById(R.id.btnAddSubTask)
        recyclerView = findViewById(R.id.recyclerViewSubTasks)

        val position = intent.getIntExtra("position", -1)

        val sharedPref = getSharedPreferences("tasks", MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("taskList", null)
        val type = object : TypeToken<ArrayList<Task>>() {}.type

        taskList = if (json != null) {
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }

        val currentTask = taskList[position]
        subTaskList = currentTask.subTasks

        // ✅ Setup RecyclerView AFTER data is ready
        adapter = SubTaskAdapter(subTaskList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAddSubTask.setOnClickListener {
            val subTask = etSubTask.text.toString()

            if (subTask.isNotEmpty()) {
                subTaskList.add(subTask)
                adapter.notifyDataSetChanged()

                val updatedJson = gson.toJson(taskList)
                sharedPref.edit().putString("taskList", updatedJson).apply()

                etSubTask.text.clear()
            }
        }
    }
}
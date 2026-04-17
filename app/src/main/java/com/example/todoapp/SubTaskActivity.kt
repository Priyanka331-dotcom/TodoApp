package com.example.todoapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SubTaskActivity : AppCompatActivity() {

    private lateinit var etSubTask: EditText
    private lateinit var btnAddSubTask: Button
    private lateinit var listView: ListView

    private lateinit var taskList: ArrayList<Task>
    private lateinit var subTaskList: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_task)

        etSubTask = findViewById(R.id.etSubTask)
        btnAddSubTask = findViewById(R.id.btnAddSubTask)
        listView = findViewById(R.id.listSubTasks)

        val position = intent.getIntExtra("position", -1)

        val sharedPref = getSharedPreferences("tasks", MODE_PRIVATE)
        val gson = Gson()

        val json = sharedPref.getString("taskList", null)
        val type = object : TypeToken<ArrayList<Task>>() {}.type

        taskList = gson.fromJson(json, type)

        val currentTask = taskList[position]
        subTaskList = currentTask.subTasks

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subTaskList)
        listView.adapter = adapter

        btnAddSubTask.setOnClickListener {
            val subTask = etSubTask.text.toString()

            if (subTask.isNotEmpty()) {
                subTaskList.add(subTask)
                adapter.notifyDataSetChanged()

                // Save updated list
                val updatedJson = gson.toJson(taskList)
                sharedPref.edit().putString("taskList", updatedJson).apply()

                etSubTask.text.clear()
            }
        }
    }
}
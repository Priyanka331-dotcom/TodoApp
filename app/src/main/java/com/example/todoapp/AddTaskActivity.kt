package com.example.todoapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTask: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        etTask = findViewById(R.id.etTask)
        btnSave = findViewById(R.id.btnSave)

        val oldTask = intent.getStringExtra("task")
        val position = intent.getIntExtra("position", -1)

        if (oldTask != null) {
            etTask.setText(oldTask)
        }

        btnSave.setOnClickListener {
            val task = etTask.text.toString()

            if (task.isNotEmpty()) {

                // Save in SharedPreferences
                val sharedPref = getSharedPreferences("tasks", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("newTask", task)

                if (position != -1) {
                    // Editing existing task
                    editor.putString("updatedTask", task)
                    editor.putInt("updatedPosition", position)
                } else {
                    // New task
                    editor.putString("newTask", task)
                }

                editor.apply()
                finish() // go back to MainActivity
            }
        }
    }
}
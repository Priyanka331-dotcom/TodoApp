package com.example.todoapp

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: ArrayList<Task>,
                  private val onDataChanged: () -> Unit
) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTask: TextView = itemView.findViewById(R.id.tvTask)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox) // ✅ NEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val task = taskList[position]

        // ✅ Set text
        holder.tvTask.text = task.title

        // ✅ Set checkbox state
        holder.checkBox.isChecked = task.isCompleted

        // ✅ Checkbox click
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked

            // Optional UI effect
            if (isChecked) {
                holder.tvTask.alpha = 0.5f
            } else {
                holder.tvTask.alpha = 1.0f
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, SubTaskActivity::class.java)
            intent.putExtra("position", position)
            holder.itemView.context.startActivity(intent)
        }

        // ✅ Long press menu
        holder.itemView.setOnLongClickListener {

            val popup = PopupMenu(
                holder.itemView.context,
                holder.itemView,
                Gravity.END,
                0,
                R.style.CustomPopupMenu
            )

            popup.inflate(R.menu.menu_options)

            // Force text color
            for (i in 0 until popup.menu.size()) {
                val item = popup.menu.getItem(i)
                val spanString = SpannableString(item.title)
                spanString.setSpan(
                    ForegroundColorSpan(Color.BLACK),
                    0,
                    spanString.length,
                    0
                )
                item.title = spanString
            }

            popup.setOnMenuItemClickListener {

                when (it.title.toString()) {

                    "Edit" -> {
                        val intent = Intent(holder.itemView.context, AddTaskActivity::class.java)
                        intent.putExtra("task", task.title) // ✅ FIXED
                        intent.putExtra("position", position)
                        holder.itemView.context.startActivity(intent)
                    }

                    "Delete" -> {

                        val context = holder.itemView.context

                        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                        builder.setTitle("Delete Task")
                        builder.setMessage("Are you sure you want to delete this task?")

                        builder.setCancelable(false)

                        builder.setPositiveButton("Yes") { _, _ ->
                            taskList.removeAt(position)
                            notifyDataSetChanged()

                            onDataChanged()

                            Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                        }

                        builder.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }

                        builder.show()
                    }
                }
                true
            }

            popup.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}
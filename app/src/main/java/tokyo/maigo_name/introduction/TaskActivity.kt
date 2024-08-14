package tokyo.maigo_name.introduction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tokyo.maigo_name.introduction.databinding.ActivityTaskBinding
import tokyo.maigo_name.introduction.domain.Task
import tokyo.maigo_name.introduction.service.TaskAdapter

class TaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskAdapter = TaskAdapter(taskList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        binding.buttonAdd.setOnClickListener {
            val taskName = binding.editTextTask.text.toString()
            if (!TextUtils.isEmpty(taskName)) {
                val task = Task(taskName)
                taskAdapter.addTask(task)
                binding.editTextTask.text.clear()
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(activity: Activity) {
            val intent = createIntent(activity)
            activity.startActivity(intent)
        }

        fun createIntent(ctx: Context): Intent {
            return Intent(ctx, TaskActivity::class.java)
        }
    }
}
package tokyo.maigo_name.introduction

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tokyo.maigo_name.introduction.domain.Task
import tokyo.maigo_name.introduction.service.TaskAdapter
import tokyo.maigo_name.introduction.databinding.ActivityMainBinding


class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
}

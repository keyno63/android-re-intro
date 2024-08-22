package tokyo.maigo_name.introduction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import tokyo.maigo_name.introduction.databinding.ActivityTaskBinding
import tokyo.maigo_name.introduction.domain.Task
import tokyo.maigo_name.introduction.repository.TaskDatabase
import tokyo.maigo_name.introduction.service.TaskAdapter

class TaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskDatabase: TaskDatabase
    private val taskList = mutableListOf<Task>()

    // manage check status
    private var isAllChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init database
        taskDatabase = TaskDatabase.getDatabase(this)

        taskAdapter = TaskAdapter(taskList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        binding.buttonAdd.setOnClickListener {
            val taskName = binding.editTextTask.text.toString()
            if (!TextUtils.isEmpty(taskName)) {
                val task = Task(name = taskName)
                saveTask(task)
                binding.editTextTask.text.clear()
            }
        }

        // all checked ON/OFF
        binding.buttonToggleAll.setOnClickListener {
            isAllChecked = !isAllChecked // toggle to state
            taskAdapter.toggleAllTasks(isAllChecked) // all change check state
        }

        loadTasks()
    }

    // メニューの作成
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        return true
    }

    // メニューのアイテムが選択されたときの処理
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_title -> {
                // MainActivityを呼び出す
                val intent = Intent(this, MainActivity::class.java)
                // 起動済みのMainをもってくる場合
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveTask(task: Task) {
        lifecycleScope.launch {
            taskDatabase.taskDao().insertTask(task)
            taskList.add(task)
            taskAdapter.notifyItemInserted(taskList.size - 1)
        }
    }

    private fun loadTasks() {
        lifecycleScope.launch {
            val tasks = taskDatabase.taskDao().getAllTasks()
            taskList.clear()
            taskList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
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
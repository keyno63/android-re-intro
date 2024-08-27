package tokyo.maigo_name.introduction

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tokyo.maigo_name.introduction.databinding.ActivityTaskBinding
import tokyo.maigo_name.introduction.databinding.DialogAddTaskBinding
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

        // click FAB button
        binding.fab.setOnClickListener {
            showAddTaskDialog()
        }

        // ItemTouchHelperの設定
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition

                // タスクの位置を入れ替え
                taskAdapter.moveTask(fromPosition, toPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // スワイプされたときの処理
                val position = viewHolder.bindingAdapterPosition
                val task = taskList[position]
                task.isDeleted = true
                updateTask(task)
                taskAdapter.removeTask(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val background = ColorDrawable(Color.RED)
                background.setBounds(
                    viewHolder.itemView.left, viewHolder.itemView.top,
                    viewHolder.itemView.right, viewHolder.itemView.bottom
                )
                background.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // all checked ON/OFF
        binding.buttonToggleAll.setOnClickListener {
            isAllChecked = !isAllChecked // toggle to state
            taskAdapter.toggleAllTasks(isAllChecked) // all change check state
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

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

    private fun showAddTaskDialog() {
        // ダイアログのレイアウトをインフレート
        val dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this))

        // ダイアログを作成
        val dialog = AlertDialog.Builder(this)
            .setTitle("タスクを追加")
            .setView(dialogBinding.root)
            .setPositiveButton("追加") { _, _ ->
                val taskName = dialogBinding.editTextTaskName.text.toString()
                if (taskName.isNotEmpty()) {
                    val taskName = binding.editTextTask.text.toString()
                    if (!TextUtils.isEmpty(taskName)) {
                        val task = Task(name = taskName)
                        saveTask(task)
                        binding.editTextTask.text.clear()
                    }
                }
            }
            .setNegativeButton("キャンセル", null)
            .create()

        dialog.show()
    }

    private fun saveTask(task: Task) {
        lifecycleScope.launch {
            taskDatabase.taskDao().insertTask(task)
            taskList.add(task)
            taskAdapter.notifyItemInserted(taskList.size - 1)
        }
    }

    private fun updateTask(task: Task) {
        GlobalScope.launch {
            taskDatabase.taskDao().updateTask(task)
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
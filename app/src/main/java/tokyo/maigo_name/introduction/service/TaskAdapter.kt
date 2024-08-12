package tokyo.maigo_name.introduction.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tokyo.maigo_name.introduction.databinding.ItemTaskBinding
import tokyo.maigo_name.introduction.domain.Task


class TaskAdapter(private val taskList: MutableList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private lateinit var binding: ItemTaskBinding

    // ViewHolderクラスでView Bindingを使用
    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        // ItemTaskBindingをインフレート
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        // Bindingオブジェクトを通じてビューにアクセス
        holder.binding.textViewTask.text = task.name
        holder.binding.checkBoxTask.isChecked = task.isCompleted

        // チェックボックスの状態変更リスナー
        holder.binding.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
        }
    }

    override fun getItemCount() = taskList.size

    fun addTask(task: Task) {
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }

    fun removeTask(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }
}

package tokyo.maigo_name.introduction.repository

import androidx.room.*
import tokyo.maigo_name.introduction.domain.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM tasks WHERE isDeleted = false")
    suspend fun getAllTasks(): List<Task>
}

package com.example.aufgabe9.database.controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.util.Log
import com.example.aufgabe9.database.DbHelper
import com.example.aufgabe9.database.dataclass.TodoListDataClass
import com.example.aufgabe9.database.dataclass.Priority
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.net.ssl.SSLEngineResult.Status

/**
 * Controller class for handling CRUD operations on the ToDo database.
 *
 * This class provides methods to insert, read, update, and delete ToDo items from the database.
 * It communicates with the `DbHelper` to access the SQLite database, and it converts the data
 * into `TodoListDataClass` objects for use in the application.
 *
 * @param context The application context, used to initialize the `DbHelper` instance.
 */
class TodoListController(context: Context) {
    private val dbHelper = DbHelper(context)

    /**
     * Insert a new ToDo into the database.
     *
     * This method inserts a new `TodoListDataClass` instance into the `todos` table.
     * It returns `true` if the insert operation was successful, and `false` if it failed.
     *
     * @param todo The `TodoListDataClass` instance representing the ToDo to be inserted.
     * @return `true` if the insert was successful, `false` otherwise.
     */
    fun insertTodo(todo: TodoListDataClass): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("description", todo.description)
                put("priority", todo.priority.level)
                put("deadline", todo.deadline)
                put("status", if (todo.status == 1) 1 else 0)
            }
            Log.d("TodoListController", "Inserting values: $values")
            val rowId = db.insert("todos", null, values)
            Log.d("TodoListController", "Insert result rowId: $rowId")
            rowId != -1L
        } catch (e: Exception) {
            Log.e("TodoListController", "Insert failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Get all ToDos from the database.
     *
     * This method retrieves all ToDo items from the `todos` table and converts them into a
     * list of `TodoListDataClass` instances. It returns a list of all ToDos in the database.
     *
     * @return A list of `TodoListDataClass` objects representing all the ToDos in the database.
     */
    fun getAllTodos(): List<TodoListDataClass> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<TodoListDataClass>()
        val cursor = db.rawQuery("SELECT * FROM todos", null)
        Log.d("TodoListController", "Getting all todos, cursor count: ${cursor.count}")

        try {
            if (cursor.moveToFirst()) {
                do {
                    val todo = TodoListDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        priority = Priority.fromLevel(cursor.getInt(cursor.getColumnIndexOrThrow("priority"))),
                        deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        status = cursor.getInt(cursor.getColumnIndexOrThrow("status"))
                    )
                    todos.add(todo)
                    Log.d("TodoListController", "Loaded todo: $todo")
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("TodoListController", "Error fetching todos", e)
        } finally {
            cursor.close()
            db.close()
        }
        Log.d("TodoListController", "Returning ${todos.size} todos")
        return todos
    }

    /**
     * Update an existing ToDo in the database.
     *
     * This method updates an existing ToDo item with new data. It returns `true` if the
     * update was successful, and `false` if it failed.
     *
     * @param todo The `TodoListDataClass` instance representing the updated ToDo.
     * @return `true` if the update was successful, `false` otherwise.
     */
    fun updateTodo(todo: TodoListDataClass): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("description", todo.description)
                put("priority", todo.priority.level)
                put("deadline", todo.deadline)
                put("status", if (todo.status == 1) 1 else 0)
            }
            val rowsAffected = db.update("todos", values, "id = ?", arrayOf(todo.id.toString()))
            rowsAffected > 0
        } catch (e: Exception) {
            Log.e("TodoListController", "Update failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Delete a ToDo by ID.
     *
     * This method deletes a ToDo item from the database using its unique ID. It returns `true`
     * if the deletion was successful, and `false` if it failed.
     *
     * @param id The ID of the ToDo to delete.
     * @return `true` if the deletion was successful, `false` otherwise.
     */
    fun deleteTodoById(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val rowsDeleted = db.delete("todos", "id = ?", arrayOf(id.toString()))
            rowsDeleted > 0
        } catch (e: Exception) {
            Log.e("TodoListController", "Delete failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Delete all ToDos from the database.
     *
     * This method deletes all ToDo items from the `todos` table. It returns `true` if the
     * deletion was successful, and `false` if it failed.
     *
     * @return `true` if the deletion was successful, `false` otherwise.
     */
    fun deleteAllTodos(): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            db.delete("todos", null, null) > 0
        } catch (e: Exception) {
            Log.e("TodoListController", "Delete all failed", e)
            false
        } finally {
            db.close()
        }
    }
}

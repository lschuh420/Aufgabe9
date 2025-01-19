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

class TodoListController(context: Context) {
    private val dbHelper = DbHelper(context)

    // CREATE: Insert a new ToDo into the database
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

    // READ: Get all ToDos from the database
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

    // UPDATE: Update an existing ToDo
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

    // DELETE: Delete a ToDo by ID
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


    // DELETE: Delete all ToDos
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

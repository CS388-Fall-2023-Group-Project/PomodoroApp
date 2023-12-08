package com.example.pomodoro

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.Random

class MainDatabase (context: Context): SQLiteOpenHelper(context,
        MainDatabase.DATABASE_NAME, null,
        MainDatabase.DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"

        const val TABLE_TASK_DETAILS = "tbl_TaskDetails"
        const val TABLE_TASK_7DAYS = "tbl_Task7Days"
        const val COLUMN_ID = "Id"
        const val COLUMN_DATE = "Date"
        const val COLUMN_TASK_NAME = "TaskName"
        const val COLUMN_SUBJECT = "Subject"
        const val COLUMN_STUDY_ON = "StudyOn"
        const val COLUMN_STUDY_OFF = "StudyOff"
        const val COLUMN_CURRENT_TIME_START = "TimeStart"
        const val COLUMN_CURRENT_TIME_END = "TimeEnd"
        const val COLUMN_TIME_RANGE = "TimeRange"
        const val COLUMN_DURATION = "Duration"
        const val COLUMN_ROUNDS = "Rounds"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableTaskDetails = """
            CREATE TABLE $TABLE_TASK_DETAILS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_DATE STRING,
                $COLUMN_TASK_NAME STRING,
                $COLUMN_SUBJECT STRING,
                $COLUMN_STUDY_ON STRING,
                $COLUMN_STUDY_OFF STRING,
                $COLUMN_CURRENT_TIME_START STRING,
                $COLUMN_CURRENT_TIME_END STRING,
                $COLUMN_TIME_RANGE STRING,
                $COLUMN_DURATION INT,
                $COLUMN_ROUNDS INT
            )
        """.trimIndent()

        // Table to store tasks in the past 7 days
        val createTableTask7Days = """
            CREATE TABLE $TABLE_TASK_7DAYS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_DATE STRING,
                $COLUMN_TASK_NAME STRING,
                $COLUMN_SUBJECT STRING,
                $COLUMN_STUDY_ON STRING,
                $COLUMN_STUDY_OFF STRING,
                $COLUMN_CURRENT_TIME_START STRING,
                $COLUMN_CURRENT_TIME_END STRING,
                $COLUMN_TIME_RANGE STRING,
                $COLUMN_DURATION INT,                
                $COLUMN_ROUNDS STRING
            )
        """.trimIndent()

        db?.execSQL(createTableTaskDetails)
        db?.execSQL(createTableTask7Days)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASK_DETAILS")
        onCreate(db)
    }

    fun insertTableTaskDetails(
            weekNumber: Int,
            weekMonday: String,
            date: String,
            taskName: String,
            subject: String,
            studyOn: String,
            studyOff: String,
            currentTimeStart: String,
            currentTimeEnd: String,
            timeRange: String,
            duration: Int,
            rounds: Int,
    ) {
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_TASK_NAME, taskName)
            put(COLUMN_SUBJECT, subject)
            put(COLUMN_STUDY_ON, studyOn)
            put(COLUMN_STUDY_OFF, studyOff)
            put(COLUMN_CURRENT_TIME_START, currentTimeStart)
            put(COLUMN_CURRENT_TIME_END, currentTimeEnd)
            put(COLUMN_TIME_RANGE, timeRange)
            put(COLUMN_DURATION, duration)
            put(COLUMN_ROUNDS, rounds)
        }
        writableDatabase.insert(TABLE_TASK_DETAILS, null, values)
    }

    fun insertTableTask7Days(
            date: String,
            taskName: String,
            subject: String,
            studyOn: String,
            studyOff: String,
            currentTimeStart: String,
            currentTimeEnd: String,
            timeRange: String,
            duration: Int,
            rounds: Int,
    ) {
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_TASK_NAME, taskName)
            put(COLUMN_SUBJECT, subject)
            put(COLUMN_STUDY_ON, studyOn)
            put(COLUMN_STUDY_OFF, studyOff)
            put(COLUMN_CURRENT_TIME_START, currentTimeStart)
            put(COLUMN_CURRENT_TIME_END, currentTimeEnd)
            put(COLUMN_TIME_RANGE, timeRange)
            put(COLUMN_DURATION, duration)
            put(COLUMN_ROUNDS, rounds)
        }
        writableDatabase.insert(TABLE_TASK_DETAILS, null, values)
        Log.d("MainDatabase", "insertTableTask7Days works")
    }

    fun deleteOutdatedTasks(fromDate: String) {
        val db = writableDatabase
        try {
            db.delete(TABLE_TASK_7DAYS, "$COLUMN_DATE < ?", arrayOf(fromDate))
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error deleting outdated tasks", e)
        } finally {
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getTasksForDate(date: String): List<TaskInfo> {
        val tasks = mutableListOf<TaskInfo>()
        val db = readableDatabase

        val query =
                "SELECT * FROM ${MainDatabase.TABLE_TASK_DETAILS} WHERE ${MainDatabase.COLUMN_DATE} = ?"

        try {
            val cursor = db.rawQuery(query, arrayOf(date))

            cursor.use {
                while (it.moveToNext()) {
                    val taskID = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_ID))
                    val taskName = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TASK_NAME))
                    val taskCategory = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val timeRange = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TIME_RANGE))
                    val duration = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_DURATION))

                    val task = TaskInfo(taskID, taskName, taskCategory, timeRange, duration)
                    tasks.add(task)
                }
            }
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error fetching tasks for date: $date", e)
        } finally {
            db.close()
        }

        return tasks
    }

    @SuppressLint("Range")
    fun getTasksForLast7Days(fromDate: String): List<TaskInfo7Days> {
        val tasks = mutableListOf<TaskInfo7Days>()
        val db = readableDatabase

        val query =
                "SELECT * FROM ${MainDatabase.TABLE_TASK_7DAYS} WHERE ${MainDatabase.COLUMN_DATE} >= ?"

        try {
            val cursor = db.rawQuery(query, arrayOf(fromDate))

            cursor.use {
                while (it.moveToNext()) {
                    val taskID = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_ID))
                    val taskName = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TASK_NAME))
                    val subject = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val duration = it.getString(it.getColumnIndex(MainDatabase.COLUMN_DURATION))
                    val task = TaskInfo7Days(taskID, taskName, subject, duration)
                    tasks.add(task)
                }
            }
            Log.d("MainDatabase", "Task last 7 days $TaskInfo7Days")
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error fetching tasks for the last 7 days from $fromDate", e)
        } finally {
            db.close()
        }

        return tasks
    }

    @SuppressLint("Range")
    fun getTotalDurationByCategoryLast7Days(fromDate: String): Map<String, Int> {
        val totalDurations = mutableMapOf<String, Int>()
        val db = readableDatabase

        val query =
                """
        SELECT $COLUMN_SUBJECT, SUM($COLUMN_DURATION) as total_duration
        FROM $TABLE_TASK_DETAILS
        WHERE $COLUMN_DATE >= ?
        GROUP BY $COLUMN_SUBJECT
        """.trimIndent()

        try {
            val cursor = db.rawQuery(query, arrayOf(fromDate))

            cursor.use {
                while (it.moveToNext()) {
                    val category = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val totalDuration = it.getInt(it.getColumnIndex("total_duration"))
                    totalDurations[category] = totalDuration
                }
            }
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error calculating total duration by category", e)
        } finally {
            db.close()
        }

        return totalDurations
    }
}

data class TaskInfo7Days (
        var id: Int = 1,
        var task: String = "",
        var subject: String = "",
        var timerange: String = "",
        var duration: Int? = null
){
    companion object{
        fun getAutoId(): Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}

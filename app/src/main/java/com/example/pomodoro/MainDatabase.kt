package com.example.pomodoro

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MainDatabase (context: Context): SQLiteOpenHelper(context,
    MainDatabase.DATABASE_NAME, null,
    MainDatabase.DATABASE_VERSION
){
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"

        const val TABLE_TASKDETAILS = "tbl_TaskDetails"
        const val COLUMN_ID = "Id"
        const val COLUMN_DATE = "Date"
        const val COLUMN_TASK_NAME = "TaskName"
        const val COLUMN_SUBJECT = "Subject"
        const val COLUMN_STUDYON = "StudyOn"
        const val COLUMN_STUDYOFF = "StudyOff"
        const val COLUMN_CURRENT_TIME_START = "TimeStart"
        const val COLUMN_CURRENT_TIME_END = "TimeEnd"
        const val COLUMN_TIMERANGE = "TimeRange"
        const val COLUMN_DURATION = "Duration"
        const val COLUMN_ROUNDS = "Rounds"
        const val DEFAULT_ROW_ID = 1
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableTaskDetails = """
            CREATE TABLE $TABLE_TASKDETAILS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_DATE STRING,
                $COLUMN_TASK_NAME STRING,
                $COLUMN_SUBJECT STRING,
                $COLUMN_STUDYON STRING,
                $COLUMN_STUDYOFF STRING,
                $COLUMN_CURRENT_TIME_START STRING,
                $COLUMN_CURRENT_TIME_END STRING,
                $COLUMN_TIMERANGE STRING,
                $COLUMN_DURATION INT,
                $COLUMN_ROUNDS STRING
            )
        """.trimIndent()

        db?.execSQL(createTableTaskDetails)

        // Insert a default row with the specified ID
        if (db != null) {
            insertDefaultRow(db)
        }
    }

    private fun insertDefaultRow(db: SQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put(COLUMN_ID, DEFAULT_ROW_ID)
            put(COLUMN_TASK_NAME, "Revise for exam")
            put(COLUMN_SUBJECT, "Math")
            put(COLUMN_STUDYON, "1")
            put(COLUMN_STUDYOFF, "1")
            put(COLUMN_CURRENT_TIME_START,"9:00 AM")
            put(COLUMN_CURRENT_TIME_END,"11:00 AM")
            put(COLUMN_TIMERANGE, "10:00 AM - 11:00 AM")
            put(COLUMN_DATE, "2023-11-13")
            put(COLUMN_DURATION, 2)
            put(COLUMN_ROUNDS, "1")
        }

        db.insertWithOnConflict(
            TABLE_TASKDETAILS,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKDETAILS")
        onCreate(db)
    }
    @SuppressLint("Range")
    fun getTasksForDate(date: String): List<TaskInfo> {
        val tasks = mutableListOf<TaskInfo>()
        val db = readableDatabase

        val query = "SELECT * FROM ${MainDatabase.TABLE_TASKDETAILS} WHERE ${MainDatabase.COLUMN_DATE} = ?"

        try {
            val cursor = db.rawQuery(query, arrayOf(date))

            cursor.use {
                while (it.moveToNext()) {
                    val taskID = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_ID))
                    val taskName = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TASK_NAME))
                    val taskCategory = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val timeRange = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TIMERANGE))
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
    fun insertStudySession(
        date: String,
        taskName: String,
        subject: String,
        studyOn: String,
        studyOff: String,
        currentTimeStart: String,
        currentTimeEnd:String,
        time_range: String,
        duration: Int,
        rounds: String,
    ) {
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_TASK_NAME, taskName)
            put(COLUMN_SUBJECT, subject)
            put(COLUMN_STUDYON, studyOn)
            put(COLUMN_STUDYOFF, studyOff)
            put(COLUMN_CURRENT_TIME_START, currentTimeStart)
            put(COLUMN_CURRENT_TIME_END, currentTimeEnd)
            put(COLUMN_TIMERANGE, time_range)
            put(COLUMN_DURATION, duration)
            put(COLUMN_ROUNDS, rounds)
        }
        writableDatabase.insert(TABLE_TASKDETAILS, null, values)
    }
}
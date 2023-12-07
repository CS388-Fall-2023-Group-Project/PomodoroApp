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

        const val TABLE_TASK_DETAILS = "tbl_TaskDetails"
        const val COLUMN_ID = "Id"
        const val COLUMN_WEEK_NUMBER = "WeekNumber"
        const val COLUMN_WEEK_MONDAY = "WeekMonday"
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
        const val DEFAULT_ROW_ID = 1
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableTaskDetails = """
            CREATE TABLE $TABLE_TASK_DETAILS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_WEEK_NUMBER INT,
                $COLUMN_WEEK_MONDAY STRING,
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

        // Insert a default row with the specified ID
        if (db != null) {
            insertDefaultRow(db)
        }
    }

    private fun insertDefaultRow(db: SQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put(COLUMN_ID, DEFAULT_ROW_ID)
            put(COLUMN_WEEK_NUMBER, "46")
            put(COLUMN_WEEK_MONDAY, "2023-11-13")
            put(COLUMN_TASK_NAME, "Revise for exam")
            put(COLUMN_SUBJECT, "Math")
            put(COLUMN_STUDY_ON, "1")
            put(COLUMN_STUDY_OFF, "1")
            put(COLUMN_CURRENT_TIME_START,"9:00 AM")
            put(COLUMN_CURRENT_TIME_END,"11:00 AM")
            put(COLUMN_TIME_RANGE, "10:00 AM - 11:00 AM")
            put(COLUMN_DATE, "2023-11-13")
            put(COLUMN_DURATION, 2)
            put(COLUMN_ROUNDS, "1")
        }

        db.insertWithOnConflict(
            TABLE_TASK_DETAILS,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASK_DETAILS")
        onCreate(db)
    }
    @SuppressLint("Range")
    fun getTasksForDate(date: String): List<TaskInfo> {
        val tasks = mutableListOf<TaskInfo>()
        val db = readableDatabase

        val query = "SELECT * FROM ${MainDatabase.TABLE_TASK_DETAILS} WHERE ${MainDatabase.COLUMN_DATE} = ?"

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
    fun insertStudySession(
        weekNumber: Int,
        weekMonday: String,
        date: String,
        taskName: String,
        subject: String,
        studyOn: String,
        studyOff: String,
        currentTimeStart: String,
        currentTimeEnd:String,
        timeRange: String,
        duration: Int,
        rounds: String,
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
        Log.d("MainDatabase", "MainDatabase inserted weekNumber as: $weekNumber")
        Log.d("MainDatabase", "MainDatabase inserted weekMonday as: $weekMonday")
        writableDatabase.insert(TABLE_TASK_DETAILS, null, values)
    }
}
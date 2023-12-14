package com.example.pomodoro

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit
import kotlin.math.max

class MainDatabase (context: Context): SQLiteOpenHelper(context,
    MainDatabase.DATABASE_NAME, null,
    MainDatabase.DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"

        const val TABLE_TASK_DETAILS = "tbl_TaskDetails"
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
        db?.execSQL(createTableTaskDetails)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASK_DETAILS")
        onCreate(db)
    }

    fun insertTableTaskDetails(
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
                    val date = it.getString(it.getColumnIndex(MainDatabase.COLUMN_DATE))
                    val taskName = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TASK_NAME))
                    val taskCategory = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val timeRange = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TIME_RANGE))
                    val duration = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_DURATION))

                    val task = TaskInfo(taskID, date, taskName, taskCategory, timeRange, duration)
                    tasks.add(task)
                }
            }
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error fetching tasks for date getTasksForDate(): $date", e)
        } finally {
            db.close()
        }

        return tasks
    }

    @SuppressLint("Range")
    fun getTasksForLast7Days(): List<TaskInfo> {
        val tasks = mutableListOf<TaskInfo>()
        val db = readableDatabase

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        // Calculate the date 7 days ago
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(currentDate)!!
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgoDate = dateFormat.format(calendar.time)

        val query = """
        SELECT * FROM ${MainDatabase.TABLE_TASK_DETAILS}
        WHERE ${MainDatabase.COLUMN_DATE} BETWEEN date(?) AND date(?)
    """.trimIndent()

        try {
            val cursor = db.rawQuery(query, arrayOf(sevenDaysAgoDate, currentDate))

            cursor.use {
                while (it.moveToNext()) {
                    val taskID = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_ID))
                    val date = it.getString(it.getColumnIndex(MainDatabase.COLUMN_DATE))
                    val taskName = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TASK_NAME))
                    val taskCategory = it.getString(it.getColumnIndex(MainDatabase.COLUMN_SUBJECT))
                    val timeRange = it.getString(it.getColumnIndex(MainDatabase.COLUMN_TIME_RANGE))
                    val duration = it.getInt(it.getColumnIndex(MainDatabase.COLUMN_DURATION))

                    val task = TaskInfo(taskID, date, taskName, taskCategory, timeRange, duration)
                    tasks.add(task)
                }
            }
        } catch (e: Exception) {
            Log.e("MainDatabase", "Error fetching tasks for the last 7 days", e)
        } finally {
            db.close()
        }

        tasks.sortByDescending { it.date }

        return tasks
    }

    fun calculateStudyStreak(): Int {
        val tasksForLast7Days = getTasksForLast7Days()
        if (tasksForLast7Days.isEmpty()) {
            return 0 // No tasks in the last 7 days, streak is 0.
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Filter tasks with duration >= 1
        val validTasks = tasksForLast7Days.filter { it.duration!! >= 1 }

        // Extract unique dates from valid tasks
        val uniqueDates = validTasks.map { it.date }.toSet()

        // Sort unique dates in descending order
        val sortedDates = uniqueDates.sortedByDescending { dateFormat.parse(it) }

        Log.d("MainDatabase", "Soted Unique Dates: $sortedDates")

        val currentDate = dateFormat.parse(tasksForLast7Days[0].date)!!

        var streak = 1 // At least one task in the last 7 days, so streak starts at 1.
        for (i in 1 until sortedDates.size) {
            val currentDate = dateFormat.parse(sortedDates[i - 1])!!
            val currentDateTask = dateFormat.parse(sortedDates[i])!!

            val difference = currentDate.time - currentDateTask.time
            val daysDifference = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS)

            if (daysDifference == 1L && currentDate.after(currentDateTask)) {
                streak++
            } else {
                break // Break the loop if there is a gap in consecutive days.
            }
        }

        return streak
    }

    fun calculateTotalDurationBySubject(subject: String): Int {
        val tasksForLast7Days = getTasksForLast7Days()

        // Filter tasks for the specified subject
        val tasksForSubject = tasksForLast7Days.filter { it.subject == subject }

        // Calculate the total duration for the specified subject
        val totalDuration = tasksForSubject.sumBy { it.duration ?: 0 }

        return totalDuration
    }

    fun calculateTotalDurationForDate(date: String): Int {
        val tasksForDate = getTasksForDate(date)

        // Calculate the total duration for the specified date
        val totalDuration = tasksForDate.sumBy { it.duration ?: 0 }

        return totalDuration
    }

}

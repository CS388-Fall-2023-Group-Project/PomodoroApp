package com.example.pomodoro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database_Example (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "database.db"

        private const val TABLE_HISTORY = "tbl_history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TASK_NAME = "taskName"
        private const val COLUMN_TASK_CAT = "taskCategory"
        private const val COLUMN_TIME_RANGE = "timeRange"
        private const val COLUMN_DURATION = "duration"
        private const val DEFAULT_ROW_ID = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createHistoryTableQuery = """
            CREATE TABLE $TABLE_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TASK_NAME STRING,
                $COLUMN_TASK_CAT STRING,
                $COLUMN_TIME_RANGE STRING,
                $COLUMN_DURATION INT
            )
        """.trimIndent()

        db?.execSQL(createHistoryTableQuery)

        // Insert a default row with the specified ID
        if (db != null) {
            insertDefaultRow(db)
        }
        db?.execSQL(createHistoryTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    private fun insertDefaultRow(db: SQLiteDatabase) {
        val contentValues = ContentValues().apply {
            put(COLUMN_ID, DEFAULT_ROW_ID)
            put(COLUMN_TASK_NAME, "Study")
            put(COLUMN_TASK_CAT, "Math")
            put(COLUMN_TIME_RANGE, "9:00 - 11:00")
            put(COLUMN_DURATION, 2)
        }

        db.insertWithOnConflict(
            TABLE_HISTORY,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

}
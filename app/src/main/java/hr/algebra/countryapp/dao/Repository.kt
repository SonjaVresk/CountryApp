package hr.algebra.countryapp.dao

import android.content.ContentValues
import android.database.Cursor

interface Repository {

    fun delete(tableName: String, selection: String?, selectionArgs: Array<String>?): Int

    fun insert(tableName: String, values: ContentValues?): Long

    fun query(
        tableName: String,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor?

    fun update(tableName: String, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int
}
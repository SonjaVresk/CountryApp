package hr.algebra.countryapp.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.api.model.Monument
import hr.algebra.countryapp.api.model.Ruler

private const val DB_NAME = "countries.db"
private const val DB_VERSION = 1
private const val TABLE_NAME_COUNTRY = "country"
private const val TABLE_NAME_MONUMENT= "monument"
private const val TABLE_NAME_RULER= "ruler"


private val CREATE_TABLE_COUNTRY = "create table $TABLE_NAME_COUNTRY( " +
        "${Country::_id.name} integer primary key autoincrement, " +
        "${Country::name.name} text not null, " +
        "${Country::foundation.name} text not null, " +
        "${Country::fall.name} text not null, " +
        "${Country::capital.name} text not null, " +
        "${Country::location.name} text not null, " +
        "${Country::officialLanguage.name} text not null, " +
        "${Country::religion.name} text not null, " +
        "${Country::government.name} text not null, " +
        "${Country::map.name} text not null, " +
        "${Country::read.name} integer not null" +
        ")"
private const val DROP_TABLE_COUNTRY = "drop table $TABLE_NAME_COUNTRY"

private val CREATE_TABLE_MONUMENT = "create table $TABLE_NAME_MONUMENT( " +
        "${Monument::_id.name} integer primary key autoincrement, " +
        "${Monument::name.name} text not null, " +
        "${Monument::location.name} text not null, " +
        "${Monument::description.name} text not null, " +
        "CountryID integer not null" +
        ")"
private const val DROP_TABLE_MONUMENT = "drop table $TABLE_NAME_MONUMENT"

private val CREATE_TABLE_RULER = "create table $TABLE_NAME_RULER( " +
        "${Ruler::_id.name} integer primary key autoincrement, " +
        "${Ruler::name.name} text not null, " +
        "${Ruler::reign.name} text not null, " +
        "CountryID integer not null" +
        ")"
private const val DROP_TABLE_RULER = "drop table $TABLE_NAME_RULER"


class CountrySqlHelper(context: Context?) : SQLiteOpenHelper(
    context, DB_NAME, null, DB_VERSION
), Repository {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_COUNTRY)
        db.execSQL(CREATE_TABLE_MONUMENT)
        db.execSQL(CREATE_TABLE_RULER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE_RULER)
        db.execSQL(DROP_TABLE_MONUMENT)
        db.execSQL(DROP_TABLE_COUNTRY)
        onCreate(db)
    }

    override fun delete(tableName: String, selection: String?, selectionArgs: Array<String>?): Int {
        return writableDatabase.delete(tableName, selection, selectionArgs)
    }

    override fun insert(tableName: String, values: ContentValues?): Long {
        return writableDatabase.insert(tableName, null, values)
    }

    override fun query(
        tableName: String,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return readableDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun update(
        tableName: String,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return writableDatabase.update(tableName, values, selection, selectionArgs)
    }
}
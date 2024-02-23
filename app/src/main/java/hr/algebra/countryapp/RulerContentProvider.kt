package hr.algebra.countryapp

import hr.algebra.countryapp.api.model.Ruler
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.countryapp.api.model.Country
import hr.algebra.countryapp.dao.Repository
import hr.algebra.countryapp.factory.getRepository
import java.lang.IllegalArgumentException


private const val AUTHORITY = "hr.algebra.countryapp.api.provider.ruler"
private const val PATH = "countries"
val RULER_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}
private const val TABLE_NAME_RULER= "ruler"

//content:hr.algebra.countryapp.api.provider/countries

class RulerContentProvider : ContentProvider() {

    private lateinit var repository: Repository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val tableName = when (URI_MATCHER.match(uri)) {
            ITEMS -> TABLE_NAME_RULER
            ITEM_ID -> TABLE_NAME_RULER
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        return repository.delete(tableName, selection, selectionArgs)

        throw IllegalArgumentException("WRONG URI")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val tableName = when (URI_MATCHER.match(uri)) {
            ITEMS -> TABLE_NAME_RULER
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        val id = repository.insert(tableName, values)
        return ContentUris.withAppendedId(RULER_PROVIDER_CONTENT_URI, id)
    }

    override fun onCreate(): Boolean {
        repository = getRepository(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val tableName = when (URI_MATCHER.match(uri)) {
            ITEMS -> TABLE_NAME_RULER
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return repository.query(tableName, projection, selection, selectionArgs, sortOrder)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val tableName = when (URI_MATCHER.match(uri)) {
            ITEMS -> TABLE_NAME_RULER
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        return when (URI_MATCHER.match(uri)) {
            ITEMS -> repository.update(tableName, values, selection, selectionArgs)
            ITEM_ID -> {
                uri.lastPathSegment?.let { id ->
                    repository.update(tableName, values, "${Ruler::_id.name}=?", arrayOf(id))
                }
                    ?: 0
            }
            else -> throw IllegalArgumentException("WRONG URI")
        }
    }
}
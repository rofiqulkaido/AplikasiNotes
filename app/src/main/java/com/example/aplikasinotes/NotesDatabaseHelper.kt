package com.example.aplikasinotes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class NotesDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "notesapp.db"
        const val DATABASE_VERSION = 4

        // Table names
        const val TABLE_NOTES = "allnotes"
        const val TABLE_PROFILE = "useraccount"

        // Columns for notes
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"

        // Columns for account
        const val COLUMN_USERID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

    }

    // Create table account sql query
    private val CREATE_ACCOUNT_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_PROFILE + "("
            + COLUMN_USERID + " TEXT PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_EMAIL + " TEXT, "
            + COLUMN_PASSWORD + " TEXT)")

    // Drop table account sql query
    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_PROFILE"

    override fun onCreate(db: SQLiteDatabase?) {
        // Create notes table
        val createTableQuery = "CREATE TABLE $TABLE_NOTES ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)

        // Create account table
        db?.execSQL(CREATE_ACCOUNT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop notes table
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NOTES"
        db?.execSQL(dropTableQuery)

        // Drop account table
        db?.execSQL(DROP_ACCOUNT_TABLE)

        // Recreate tables
        onCreate(db)

    }

    private val context: Context = context.applicationContext

    fun insertNote(note:NotesModel){
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NOTES, null, values)
        db.close()
    }

    fun getAllNotes(): List<NotesModel> {
        val notesList = mutableListOf<NotesModel>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NOTES"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note = NotesModel(id, title, content)
            notesList.add(note)
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun updateNote(note: NotesModel){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NOTES, values, whereClause, whereArgs)
        db.close()
    }

    fun getNoteByID(noteId: Int): NotesModel{
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NOTES WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return NotesModel(id, title, content)
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NOTES, whereClause, whereArgs)
    }


    @SuppressLint("Range")
    fun checkDataUser(email:String):String{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name:String = ""

        val cursor = db.query(TABLE_PROFILE,
            colums, selection, selectionArgs,null,null,null)

        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }
        cursor.close()
        db.close()
        return name
    }

    fun addAccountUser(name: String, email: String, password: String) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_PROFILE, null, values)

        if (result == -1L) {
            Toast.makeText(context, "Register failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Register success, " +
                    "Please login using your new account", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    //CHECK LOGIN PASIEN
    @SuppressLint("Range")
    fun checkLoginUser(email: String, password: String): Boolean {
        println("Email: $email, Password: $password")
        try {
            val columns = arrayOf(COLUMN_NAME, COLUMN_EMAIL, COLUMN_PASSWORD)
            val db = this.readableDatabase
            val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
            val selectionArgs = arrayOf(email.trim(), password.trim())

            val cursor = db.query(
                TABLE_PROFILE,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            val cursorCount = cursor.count
            val result : Boolean
            //check data available or not
            if(cursorCount > 0){
                result = true
                //set data
                if(cursor.moveToFirst()){

                //retrieve data nama
                MainActivity.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))

                }
            }
            else {
                result = false
            }
            cursor.close()
            db.close()
            return result

            return cursorCount > 0
        } catch (e: Exception) {
            // Log the exception or handle it as needed
            e.printStackTrace()
            return false
        }
    }
}
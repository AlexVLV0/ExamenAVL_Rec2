package com.example.practicaexamen

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examenavl_rec2.Viaje

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "viajes_db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "viajes"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "fecha"
        const val COLUMN_TIME = "hora"
        const val COLUMN_ORIGIN = "origen"
        const val COLUMN_DESTINO = "destino"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_TIME TEXT, " +
                "$COLUMN_ORIGIN TEXT, " +
                "$COLUMN_DESTINO REAL)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertBar(viaje: Viaje) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, viaje.fecha)
        values.put(COLUMN_TIME, viaje.hora)
        values.put(COLUMN_ORIGIN, viaje.origen)
        values.put(COLUMN_DESTINO, viaje.destino)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllBars(): List<Viaje> {
        val viajes = mutableListOf<Viaje>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val fecha = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val hora = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
                val origen = cursor.getDouble(cursor.getColumnIndex(COLUMN_ORIGIN))
                val destino = cursor.getDouble(cursor.getColumnIndex(COLUMN_DESTINO))
                val viaje = Viaje(id, fecha, hora, origen.toString(), destino.toString())
                viajes.add(viaje)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return viajes
    }

    fun updateBar(viaje: Viaje) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, viaje.fecha)
        values.put(COLUMN_TIME, viaje.hora)
        values.put(COLUMN_ORIGIN, viaje.origen)
        values.put(COLUMN_DESTINO, viaje.destino)
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(viaje.id.toString()))
        db.close()
    }

    fun deleteViaje(viajeId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(viajeId.toString()))
        db.close()
    }
}
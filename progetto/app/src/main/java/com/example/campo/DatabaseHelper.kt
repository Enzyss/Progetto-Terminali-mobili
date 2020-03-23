package com.example.vicky.sqliteexample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.campo.Manutenzione
import com.example.campo.Prenotazioni
import com.example.campo.User
import kotlinx.android.synthetic.main.user_registration.*

/**
 * Created by VickY on 2017-11-28.
 */

val DATABASE_NAME ="MyDB13"
val TABLE_NAME="Users"
val COL_NAME = "name"
val COL_PASS = "password"
val COL_ID = "id"

/* TABELLA PER PRENOTAZIONI */
val TABLE_PRENOTAZIONI = "Prenotazioni"
val COL_ORA = "ora"
val COL_GIORNO = "giorno"
val COL_ID_PREN = "id"
val COL_NAME_FOR = "name_esterno"
val COL_TIPOLOGIA = "tipo"

/* TABLE PER MANUTENZIONE */
val TABLE_MANUTENZIONE = "Manutenzione"
val COL_RIFLETTORI = "riflettori"
val COL_RISCALDAMENTO = "riscaldamento"
val COL_IRRIGAZIONE = "irrigazione"
val COL_ID_MAN = "id"


class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
        //        COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256) PRIMARY KEY," +
                COL_PASS +" VARCHAR(30))"

        val tableNew = "CREATE TABLE " + TABLE_PRENOTAZIONI +" (" +
                COL_ID_PREN +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ORA + " VARCHAR(3)," +
                COL_GIORNO + " VARCHAR(3)," +
                COL_NAME_FOR + " VARCHAR(256)," +
                COL_TIPOLOGIA + " VARCHAR(50))"

        val tableManutenzione = "CREATE TABLE " + TABLE_MANUTENZIONE +" (" +
                COL_ID_PREN +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_RIFLETTORI + " INTEGER," +
                COL_RISCALDAMENTO + " VARCHAR(20)," +
                COL_IRRIGAZIONE + " VARCHAR(20))"

        db?.execSQL(createTable)
        db?.execSQL(tableNew)
        db?.execSQL(tableManutenzione)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertData(user : User){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,user.name)
        cv.put(COL_PASS,user.password)
        var result = db.insert(TABLE_NAME,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun insertManutenzione(manutenzione: Manutenzione) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_RIFLETTORI,manutenzione.riflettori)
        cv.put(COL_RISCALDAMENTO,manutenzione.riscaldamento)
        cv.put(COL_IRRIGAZIONE,manutenzione.irrigazione)
        var result = db.insert(TABLE_MANUTENZIONE,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun getOneManutenzione(): Manutenzione? {
        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_MANUTENZIONE "
        // "SELECT  * FROM $TABLE_PRENOTAZIONI WHERE $COL_GIORNO = \"$giorno\" AND $COL_ORA=\"$ora\""
        // val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COL_NAME = \"$name\" AND $COL_PASS=\"$password\""
        db.rawQuery(selectQuery, arrayOf()).use { // .use requires API 16
            if (it.moveToFirst()) {
                val result = Manutenzione()
                result.riflettori = it.getString(it.getColumnIndex(COL_RIFLETTORI)).toInt()
              //  result.password = it.getString(it.getColumnIndex(COL_PASS))
             //   println("##################### where is return"+result.name + result.password)
                return result
            }
        }
        println("##################### where is return " + "looool")
        return null
    }

    /*
    fun readData() : MutableList<User>{
        var list : MutableList<User> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT  * FROM "  + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.password = result.getString(result.getColumnIndex(COL_PASS))
                list.add(user)
                println("oooooooo")
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }
*/
    fun userPresent(name: String , password: String):Boolean {
        var user = User()
        val db = this.readableDatabase
        val query = "select * from " + TABLE_NAME + "where $COL_NAME=:$name"
        val cursor = db.rawQuery(query,null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true

    }

    fun getOneName(name: String , password: String): User? {
        val db = this.writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COL_NAME = ? "
       // "SELECT  * FROM $TABLE_PRENOTAZIONI WHERE $COL_GIORNO = \"$giorno\" AND $COL_ORA=\"$ora\""
       // val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COL_NAME = \"$name\" AND $COL_PASS=\"$password\""
        db.rawQuery(selectQuery, arrayOf(name)).use { // .use requires API 16
            if (it.moveToFirst()) {
                val result = User()
                result.name = it.getString(it.getColumnIndex(COL_NAME))
                result.password = it.getString(it.getColumnIndex(COL_PASS))
                println("##################### where is return"+result.name + result.password)
                return result
            }
        }
        println("##################### where is return " + "looool")
        return null
    }


    fun deleteData(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,null,null)
        db.close()
    }


    fun updateData(giorno:String,name: String) {
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(COL_GIORNO,giorno)
                db.update(TABLE_NAME,cv,COL_ID + "=? AND " + COL_NAME + "=name",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }

    fun insertPrenotazioni(prenotazioni: Prenotazioni, nome: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_GIORNO,prenotazioni.giorno)
        cv.put(COL_ORA,prenotazioni.ora)
        cv.put(COL_NAME_FOR,prenotazioni.name_esterno)
        cv.put(COL_TIPOLOGIA,prenotazioni.tipo)
        var result = db.insert(TABLE_PRENOTAZIONI,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun prelevaPrenotazioni(name: String): MutableList<Prenotazioni> {

        var list: MutableList<Prenotazioni> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT  * FROM $TABLE_PRENOTAZIONI WHERE $COL_NAME_FOR = \"$name\""

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var prenotazioni = Prenotazioni()
                prenotazioni.giorno = result.getString(result.getColumnIndex(COL_GIORNO))
                prenotazioni.ora = result.getString(result.getColumnIndex(COL_ORA))
                prenotazioni.tipo = result.getString(result.getColumnIndex(COL_TIPOLOGIA))
                prenotazioni.name_esterno = result.getString(result.getColumnIndex(COL_NAME_FOR))
                list.add(prenotazioni)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun prelevaPrenotazioniAdmin(): MutableList<Prenotazioni> {
        var list: MutableList<Prenotazioni> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT  * FROM $TABLE_PRENOTAZIONI"

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var prenotazioni = Prenotazioni()
                prenotazioni.giorno = result.getString(result.getColumnIndex(COL_GIORNO))
                prenotazioni.ora = result.getString(result.getColumnIndex(COL_ORA))
                prenotazioni.tipo = result.getString(result.getColumnIndex(COL_TIPOLOGIA))
                prenotazioni.name_esterno = result.getString((result.getColumnIndex(COL_NAME_FOR)))
                list.add(prenotazioni)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun cancellaPrenotazioni(giorno:String,ora:String): MutableList<Prenotazioni>{
        var list: MutableList<Prenotazioni> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT  * FROM $TABLE_PRENOTAZIONI WHERE $COL_GIORNO = \"$giorno\" AND $COL_ORA=\"$ora\""

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var prenotazioni = Prenotazioni()
                prenotazioni.giorno = result.getString(result.getColumnIndex(COL_GIORNO))
                prenotazioni.ora = result.getString(result.getColumnIndex(COL_ORA))
                prenotazioni.tipo = result.getString(result.getColumnIndex(COL_TIPOLOGIA))
                prenotazioni.name_esterno = result.getString(result.getColumnIndex(COL_NAME_FOR))
                list.add(prenotazioni)
            } while (result.moveToNext())
        }

        /* AGGIUNGERE RIMOZIONE DAL DATABASE */
        db.delete(TABLE_PRENOTAZIONI, "$COL_GIORNO = \"$giorno\" AND $COL_ORA=\"$ora\"",null)
        result.close()
        db.close()
        return list
    }
}
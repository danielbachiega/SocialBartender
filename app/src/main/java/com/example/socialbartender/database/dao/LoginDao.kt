package com.example.socialbartender.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.socialbartender.model.Login


@Dao
interface LoginDao {

    @Query("SELECT * FROM Login")
    fun buscaTodos(): List<Login>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg login: Login)

    @Delete
    fun remove(logion: Login)

    @Query("SELECT * FROM Login WHERE login= :login AND senha= :senha")
    fun buscaPorLoginSenha(login: String, senha: ByteArray): Login?

    @Query("SELECT * FROM Login WHERE login= :login")
    fun buscaPorLogin(login: String): Login?

    @Query("SELECT * FROM Login WHERE id= :id")
    fun buscaPorId(id:Long): Login?
}
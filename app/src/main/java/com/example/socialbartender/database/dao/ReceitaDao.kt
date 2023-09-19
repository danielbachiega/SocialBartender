package com.example.socialbartender.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.socialbartender.model.Receita

@Dao
interface ReceitaDao {

    @Query("SELECT * FROM Receita")
    fun buscaTodos(): List<Receita>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(vararg receita: Receita)

    @Delete
    fun remove(vararg receita: Receita)

    @Query("SELECT * FROM Receita WHERE id= :id")
    fun buscaPorId(id: Long): Receita?

    @Query("SELECT * FROM Receita WHERE loginId= :loginId")
    fun buscaPorLoginId(loginId: Long): List<Receita>

    @Query("SELECT * FROM Receita WHERE nome  LIKE '%' || :nome || '%' OR ingredientes LIKE '%' || :nome || '%'")
    fun buscaPorPesquisa(nome: String): List<Receita>
}
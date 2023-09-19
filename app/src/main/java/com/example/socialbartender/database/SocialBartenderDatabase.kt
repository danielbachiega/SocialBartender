package com.example.socialbartender.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.socialbartender.database.dao.LoginDao
import com.example.socialbartender.database.dao.ReceitaDao
import com.example.socialbartender.model.Login
import com.example.socialbartender.model.Receita

@Database(entities = [Receita::class,Login::class], version = 2, exportSchema = true)
abstract class SocialBartenderDatabase : RoomDatabase() {
    abstract fun receitaDao(): ReceitaDao
    abstract fun loginDao(): LoginDao


    companion object {
        fun instancia(context: Context):SocialBartenderDatabase{
            return Room.databaseBuilder(
                context,
                SocialBartenderDatabase::class.java,
                "social_bartender.db"
            ).allowMainThreadQueries()
                .build()
        }
    }
}
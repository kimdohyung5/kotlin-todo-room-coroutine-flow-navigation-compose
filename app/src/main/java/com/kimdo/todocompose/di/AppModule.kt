package com.kimdo.todocompose.di

import android.app.Application
import androidx.room.Room
import com.kimdo.todocompose.data.TodoDatabase
import com.kimdo.todocompose.data.TodoRepository
import com.kimdo.todocompose.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDatabase(app:Application): TodoDatabase {
        return Room.databaseBuilder(app, TodoDatabase::class.java, "todo.db").build()
    }

    @Singleton
    @Provides
    fun provideRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl( db.dao )
    }
}
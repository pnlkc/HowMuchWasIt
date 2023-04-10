package com.example.howmuchwasit.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHowMuchWasItDatabase(@ApplicationContext context: Context) : HowMuchWasItDatabase {
        return HowMuchWasItDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideItemDao(database: HowMuchWasItDatabase) : ItemDao {
        return database.itemDao()
    }

    @Singleton
    @Provides
    fun provideItemRepository(itemDao: ItemDao): ItemRepository {
        return DefaultItemRepository(itemDao)
    }
}
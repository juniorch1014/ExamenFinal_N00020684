package com.upn.chuquilin.guerra.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.Duelista;
import com.upn.chuquilin.guerra.repositories.CartaRepository;
import com.upn.chuquilin.guerra.repositories.DuelistaRepository;

@Database(entities = {Duelista.class, Carta.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

        public abstract DuelistaRepository duelistaRepository();
        public abstract CartaRepository cartaRepository();

        public static AppDatabase getInstance(Context context){
            return Room.databaseBuilder(context,AppDatabase.class, "VideojuegosFinal")
                    .allowMainThreadQueries()
                    .build();
        }
}

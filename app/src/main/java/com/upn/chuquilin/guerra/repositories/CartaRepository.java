package com.upn.chuquilin.guerra.repositories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.upn.chuquilin.guerra.entities.Carta;

import java.util.List;

@Dao
public interface CartaRepository {
    @Query("SELECT * FROM cartas")
    List<Carta> getAllCarta();

    @Query("SELECT * FROM cartas WHERE sincronizadoCartas LIKE :searchSincro")
    List<Carta> searchCarta(boolean searchSincro);

    @Query("SELECT * FROM cartas WHERE duelistaID LIKE :id")
    List<Carta> searchCartaID(int id);

    @Insert
    void createCarta(Carta carta);
    @Insert
    void AgregarList(List<Carta> cartas);
    @Update
    void  updateCartas(Carta carta);

    @Delete
    void delete(Carta carta);

    @Delete
    void deleteList(List<Carta> cartas);
}

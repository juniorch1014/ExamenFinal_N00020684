package com.upn.chuquilin.guerra.repositories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.Duelista;

import java.util.List;
@Dao
public interface DuelistaRepository {
    @Query("SELECT * FROM duelistas")
    List<Duelista> getAllDuelista();

    @Query("SELECT * FROM duelistas WHERE sincronizadoDuelista LIKE :searchSincro")
    List<Duelista> searchDuelista(boolean searchSincro);

    @Query("SELECT * FROM duelistas WHERE id LIKE :id")
    Duelista searchDuelistaID(int id);

    @Insert
    void createDuelista(Duelista duelista);
    @Insert
    void AgregarList(List<Duelista> duelistas);
    @Update
    void  updateDuelista(Duelista duelista);

    @Delete
    void delete(Duelista duelista);

    @Delete
    void deleteList(List<Duelista> duelistas);

}

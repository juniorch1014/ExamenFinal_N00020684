package com.upn.chuquilin.guerra.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "duelistas")
public class Duelista {

    public  int idD;
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "nameDuelista")
    public String nameDuelista;
    @ColumnInfo(name = "sincronizadoDuelista")
    public boolean sincronizadoDuelista;

}

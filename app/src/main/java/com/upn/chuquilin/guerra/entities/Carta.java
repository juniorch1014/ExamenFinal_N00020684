package com.upn.chuquilin.guerra.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cartas")
public class Carta {

    public int idC;
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "duelistaID")
    public int duelistaID;

    @ColumnInfo(name = "puntosAtaque")
    public int puntosAtaque;
    @ColumnInfo(name = "puntosDefenza")
    public int puntosDefenza;
    @ColumnInfo(name = "latitud")
    public String latitud;
    @ColumnInfo(name = "longitud")
    public String longitud;
    @ColumnInfo(name = "urlimagen")
    public String urlimagen;
    @ColumnInfo(name = "sincronizadoCartas")
    public boolean sincronizadoCartas;
    @ColumnInfo(name = "imagenBase64")
    public String imagenBase64;
    @ColumnInfo(name = "nameCarta")
    public String nameCarta;



}

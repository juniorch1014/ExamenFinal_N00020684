package com.upn.chuquilin.guerra.services;

import com.upn.chuquilin.guerra.entities.Carta;
import com.upn.chuquilin.guerra.entities.Duelista;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DuelistaService {

    @GET("duelistas")
    Call<List<Duelista>> getAllUser();

    @GET("duelistas")
    Call<List<Duelista>> getAllUser(@Query("limit") int limit, @Query("page") int page);

    @GET("duelistas")
    Call<List<Duelista>> getBuscar(@Query("search") String nombre);

    @GET("duelistas")
    Call<List<Duelista>> getCartasBySincro(@Query("sincronizadoDuelista") boolean sincronizar);

    @GET("duelistas/{id}")
    Call<Duelista> findUser(@Path("id") int id);

    // users
    @POST("duelistas")
    Call<Duelista> create(@Body Duelista user);

    @PUT("duelistas/{id}")
    Call<Duelista> update(@Path("id") int id, @Body Duelista user);

    @DELETE("duelistas/{id}")
    Call<Void> delete(@Path("id") int id);

}

package br.com.zup.omdbdesafio.controller.service;

import br.com.zup.omdbdesafio.model.domain.Filmes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbService {

    @GET("/")
    Call<Filmes> getOmdb(@Query("t") String q, @Query("plot") String sort, @Query("r") String type);

}

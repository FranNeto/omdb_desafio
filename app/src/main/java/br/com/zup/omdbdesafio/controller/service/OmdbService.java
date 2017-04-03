package br.com.zup.omdbdesafio.controller.service;

import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.model.domain.SearchFilms;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbService {

    @GET("/")
    Call<Filmes> getDetailOmdb(@Query("i") String q, @Query("r") String type);

    @GET("/")
    Call<SearchFilms> getSearchOmdb(@Query("s") String q, @Query("r") String type);


}

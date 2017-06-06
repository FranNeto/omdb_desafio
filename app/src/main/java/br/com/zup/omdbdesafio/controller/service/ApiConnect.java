package br.com.zup.omdbdesafio.controller.service;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zup on 05/06/17.
 */

public class ApiConnect {

    public static OmdbService restrofitConnect() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppApplication.getInstance().getContext().getResources().getString(R.string.url_search_film))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbService git = retrofit.create(OmdbService.class);
        return git;
    }



}

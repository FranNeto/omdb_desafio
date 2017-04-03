package br.com.zup.omdbdesafio.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.controller.service.OmdbService;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.SearchFilms;
import br.com.zup.omdbdesafio.view.interfaces.SearchViewFilmsImpl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by FranRose on 27/03/2017.
 */

public class SearchViewFilmsPresenter {
    private static final String TAG = SearchViewFilmsPresenter.class.getSimpleName();
    private SearchViewFilmsImpl listener;

    public void downloadRepositories(final String searchFilm, final SearchViewFilmsImpl listener) {
        this.listener = listener;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppApplication.getInstance().getContext().getResources().getString(R.string.url_search_film))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbService git = retrofit.create(OmdbService.class);

        Call<SearchFilms> call = git.getSearchOmdb(searchFilm, "json");
        call.enqueue(new Callback<SearchFilms>() {
            @Override
            public void onResponse(Call<SearchFilms> call, Response<SearchFilms> response) {
                SearchFilms sucessJSON = response.body();
                if (sucessJSON == null) {
                    listener.onError("Lista Vazia");
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
//                        Toast.makeText(getContext(), "responseBody:" + responseBody, Toast.LENGTH_SHORT).show();
                        Log.i(TAG," sucessJSON:responseBody "+responseBody);
                    } else {
//                        Toast.makeText(getContext(), "responseBody = " + responseBody, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (sucessJSON.getResponse() != null && sucessJSON.getResponse().equalsIgnoreCase("True")){

                        List<SearchFilms.Search> searchList = new ArrayList<SearchFilms.Search>();

                        for(SearchFilms.Search search: sucessJSON.getSearch() ){
                            SearchFilms.Search  searchFilms = new SearchFilms.Search();
                            search.setTitle(search.getTitle());
                            search.setYear(search.getYear());
                            search.setImdbID(search.getImdbID());
                            search.setPoster(search.getPoster());
                            search.setType(search.getType());

                            searchList.add(search);

                            ModelBO.getInstance().setSearchFilmsList(searchList);
                            Log.i(TAG," sucessJSON: Search"+search.getTitle());

                        }

                        listener.onSuccesso(ModelBO.getInstance().getSearchFilmsList());

                    }else{
                        listener.onError("Filme não encontrado");
                    }

                }
//                dismissProgress();
            }

            @Override
            public void onFailure(Call<SearchFilms> call, Throwable t) {
                listener.onError(t.getMessage());
            }

        });
    }


}

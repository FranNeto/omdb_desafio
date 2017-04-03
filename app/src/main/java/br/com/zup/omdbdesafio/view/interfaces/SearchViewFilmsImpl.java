package br.com.zup.omdbdesafio.view.interfaces;

import java.util.List;

import br.com.zup.omdbdesafio.model.domain.SearchFilms;

/**
 * Created by FranRose on 29/03/2017.
 */

public interface SearchViewFilmsImpl {
    void onSuccesso(List<SearchFilms.Search> searchList);
    void onError(String msg);
}

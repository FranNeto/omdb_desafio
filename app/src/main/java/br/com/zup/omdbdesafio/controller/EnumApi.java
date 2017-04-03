package br.com.zup.omdbdesafio.controller;

import br.com.zup.omdbdesafio.R;

/**
 * Created by FranRose on 16/03/2017.
 */

public enum EnumApi {

    API(String.valueOf(R.string.url_search_film));

    EnumApi(String action_settings) {
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

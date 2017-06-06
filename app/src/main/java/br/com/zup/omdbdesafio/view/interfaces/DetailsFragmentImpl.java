package br.com.zup.omdbdesafio.view.interfaces;

import br.com.zup.omdbdesafio.model.domain.Filmes;

/**
 * Created by FranRose on 29/03/2017.
 */

public interface DetailsFragmentImpl {
    void onSuccessoAdd(String msg);
    void onSuccessoDelete(String msg);
    void onError(String msg);
    void onSuccessDetails(Filmes filmes);
}

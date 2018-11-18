package br.com.zup.omdbdesafio.presenter;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.controller.service.ApiConnect;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.view.interfaces.DetailsFragmentImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by zup on 05/06/17.
 */

public class DetailPresenter {

    private DetailsFragmentImpl listener;

    public DetailPresenter(DetailsFragmentImpl listener) {
        this.listener = listener;
    }

    public void getDetailsFilm(final Filmes filmes) {

        Call<Filmes> call = ApiConnect.restrofitConnect().getDetailOmdb(filmes.getImdbID(), "json", AppApplication.getInstance().getContext().getResources().getString(R.string.apiKey));
        call.enqueue(new Callback<Filmes>() {
            @Override
            public void onResponse(Call<Filmes> call, Response<Filmes> response) {
                Filmes sucessJSON = response.body();
                if (sucessJSON == null) {
                    listener.onError("Detalhes do filme não encontrado");
                } else {
                    if (sucessJSON.getResponse() != null && sucessJSON.getResponse().equalsIgnoreCase("True")) {
                        Filmes item = new Filmes();
                        item.setId(filmes.getId());
                        item.setTitle(filmes.getTitle());
                        item.setActors(sucessJSON.getActors());
                        item.setDirector(sucessJSON.getDirector());
                        item.setGenre(sucessJSON.getGenre());
                        item.setPlot(sucessJSON.getPlot());
                        item.setAwards(sucessJSON.getAwards());
                        item.setImdbID(sucessJSON.getImdbID());
                        item.setRuntime(sucessJSON.getRuntime());
                        item.setRated(sucessJSON.getRated());
                        item.setReleased(sucessJSON.getReleased());
                        item.setLanguage(sucessJSON.getLanguage());
                        item.setCountry(sucessJSON.getCountry());
                        item.setMetascore(sucessJSON.getMetascore());
                        item.setWriter(sucessJSON.getWriter());
                        item.setYear(sucessJSON.getYear());
                        item.setPoster(sucessJSON.getPoster());
                        item.setType(sucessJSON.getType());
                        item.setTotalSeasons(sucessJSON.getTotalSeasons());

                        listener.onSuccessDetails(item);

                    } else {
//                        Toast.makeText(getContext(), "Filme " + filmes.getTitle() + " não encontrado.", Toast.LENGTH_SHORT).show();
                    }

                }
//                dismissProgress();
            }

            @Override
            public void onFailure(Call<Filmes> call, Throwable t) {
                //repositoryView.showProgressBar(View.GONE);
                listener.onError(t.getMessage());
            }

        });
    }
}

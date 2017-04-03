package br.com.zup.omdbdesafio.view.fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestHandler;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestThread;
import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import br.com.zup.omdbdesafio.controller.listener.IConnectionTestListener;
import br.com.zup.omdbdesafio.controller.service.OmdbService;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.view.interfaces.DetailsFragmentImpl;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends AbstractFragment implements IConnectionTestListener, DetailsFragmentImpl {

    private static final String TAG = DetailFragment.class.getSimpleName();
    @BindView(R.id.backdrop)
    ImageView poster;
    @BindView(R.id.txt_director)
    TextView txtDirector;
    @BindView(R.id.txt_actors)
    TextView txtactor;
    @BindView(R.id.txt_type)
    TextView txtType;
    @BindView(R.id.txt_plot)
    TextView txtPlot;
    @BindView(R.id.txt_writer)
    TextView txtWriter;
    @BindView(R.id.txt_genre)
    TextView txtGenre;
    @BindView(R.id.txt_year)
    TextView txtYear;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_country)
    TextView txtCountry;
    @BindView(R.id.txt_seasor)
    TextView txtTotalSeasons;
    @BindView(R.id.txt_released)
    TextView txtReleased;
    @BindView(R.id.txt_awards)
    TextView txtawards;
    @BindView(R.id.floating_add)
    FloatingActionButton floatingAdd;

    private BroadcastReceiver mDLCompleteReceiver;

    private Filmes filmes;
    private Filmes filmAdd;
    private String url;
    private String path;
    private boolean isFilmAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_detail, (ViewGroup) null);

        registerBackPress(null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        filmes = ModelBO.getInstance().getFilmeSelection();
        initActionBarScreen();
        initView();

        if (filmes != null) {
            inputData(filmes);
        }

    }

    private void initActionBarScreen() {

        final AppCompatActivity activity = initActionBar();
        if (toolbar != null) {
            toolbar.setTitle(filmes.getTitle());
        }

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(filmes.getTitle());

    }

    private void initView() {


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void downloadRepositories(final Filmes filmes) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppApplication.getInstance().getContext().getResources().getString(R.string.url_search_film))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbService git = retrofit.create(OmdbService.class);

        Call<Filmes> call = git.getDetailOmdb(filmes.getImdbID(), "json");
        call.enqueue(new Callback<Filmes>() {
            @Override
            public void onResponse(Call<Filmes> call, Response<Filmes> response) {
                Filmes sucessJSON = response.body();
                if (sucessJSON == null) {

                    ResponseBody responseBody = response.errorBody();
                    if (responseBody != null) {
                        Toast.makeText(getContext(), "responseBody:" + responseBody, Toast.LENGTH_SHORT).show();
                        try {
                            Toast.makeText(getContext(), "responseBody = " + responseBody.string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getContext(), "responseBody = " + responseBody, Toast.LENGTH_SHORT).show();
                    }
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

                        filmAdd = item;

                        txtDirector.setText(sucessJSON.getDirector());
                        txtactor.setText(sucessJSON.getActors());
                        txtPlot.setText(sucessJSON.getPlot());
                        txtWriter.setText(sucessJSON.getWriter());
                        txtGenre.setText(sucessJSON.getGenre());
                        txtYear.setText(sucessJSON.getYear());
                        txtType.setText(sucessJSON.getType());
                        txtTime.setText(sucessJSON.getRuntime());
                        txtReleased.setText(sucessJSON.getReleased());
                        txtTotalSeasons.setText(sucessJSON.getTotalSeasons());
                        txtCountry.setText(sucessJSON.getCountry());
                        txtawards.setText(sucessJSON.getAwards());

//                        downloadFile(item);

                        url = sucessJSON.getPoster();

                        Picasso.with(getContext())
                                .load(sucessJSON.getPoster())
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(poster);


                        Log.i("desafio", "desafio.onSuccess - " + String.valueOf(sucessJSON.getImdbID()));
                    } else {
                        Toast.makeText(getContext(), "Filme " + filmes.getTitle() + " não encontrado.", Toast.LENGTH_SHORT).show();
                    }

                }
                dismissProgress();
            }

            @Override
            public void onFailure(Call<Filmes> call, Throwable t) {
                //repositoryView.showProgressBar(View.GONE);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void inputData(Filmes filmes) {

        Filmes film = ModelBO.getInstance().getSearch(filmes);
        if (film != null && film.getImdbID() != null) {
            txtDirector.setText(film.getDirector());
            txtactor.setText(film.getActors());
            txtPlot.setText(film.getPlot());
            txtWriter.setText(film.getWriter());
            txtGenre.setText(film.getGenre());
            txtYear.setText(film.getYear());
            txtType.setText(film.getType());
            txtTime.setText(film.getRuntime());
            txtTime.setText(film.getRuntime());
            txtReleased.setText(film.getReleased());
            txtTotalSeasons.setText(film.getTotalSeasons());
            txtCountry.setText(film.getCountry());
            txtawards.setText(film.getAwards());

            url = film.getPoster();
            Log.i("path", "Url: " + url);
            Picasso.with(getContext())
                    .load(url)
                    .into(poster);

            floatingAdd.setImageResource(R.drawable.ic_delete);
            filmAdd = film;
            isFilmAdd = true;
        } else {
            floatingAdd.setImageResource(R.drawable.ic_done);
            connectionTest();
        }
    }

    private void showProgress() {
        fragmentListener.showLoading(true);

        registerBackPress(new IBackPressListener() {

            @Override
            public void onBackPress() {
                // Does nothing. Ignore it.
                //block the back button when the loading is showing
            }

            @Override
            public boolean isFirstBack() {
                // Does nothing. Ignore it.
                return false;
            }
        });
    }

    private void dismissProgress() {
        fragmentListener.showLoading(false);

    }

    private void connectionTest() {
        showProgress();
        new ConnectionTestThread(getContext(), new ConnectionTestHandler(this)).start();
    }

    @Override
    public void endConnectionTest(Boolean isSent) {
        if (isSent) {
            downloadRepositories(filmes);
        } else {
            dismissProgress();
            Toast.makeText(getContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
        }
    }

    public void downloadFile(Filmes filmes) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Omdb");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        final DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        if (filmes.getPoster() != null && !filmes.getPoster().toString().equalsIgnoreCase("N/A")) {

            String ur = filmes.getTitle().toString().trim().replace(" ", "_");

            Uri downloadUri = Uri.parse(filmes.getPoster());
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir("/Omdb", ur + ".jpg");

            mgr.enqueue(request);

            path = "file://" + direct + "/" + ur + ".jpg";
            File file = new File(path);

            Log.i("/", "path: " + file.getPath());

            Filmes update = new Filmes();
            update.setId(filmes.getId());
            update.setPoster(file.getPath());

            ModelBO.getInstance().updateFilmesUrl(update);

        } else {
            Toast.makeText(AppApplication.getInstance().getContext(), "Imagem não encontrada: " + filmes.getPoster(), Toast.LENGTH_SHORT).show();
        }

    }

    public void saveFilme() {
        ModelBO.getInstance().addFilmes(filmAdd, this);
        downloadFile(filmAdd);
    }

    public void deleteFilm(){
        ModelBO.getInstance().deleteFilmSelected(filmAdd, this);
    }

    @OnClick(R.id.floating_add)
    public void onClickSave() {
        if(!isFilmAdd) {
            saveFilme();
        }else{
            deleteFilm();
        }
    }

    @Override
    public void onSuccessoAdd(String msg) {
        isFilmAdd = true;
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        floatingAdd.setImageResource(R.drawable.ic_delete);
    }

    @Override
    public void onSuccessoDelete(String msg) {
        getActivity().onBackPressed();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

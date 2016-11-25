package br.com.zup.omdbdesafio.view.fragment;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
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
import br.com.zup.omdbdesafio.controller.activity.OmdbActivity;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestHandler;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestThread;

import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import br.com.zup.omdbdesafio.controller.listener.IConnectionTestListener;
import br.com.zup.omdbdesafio.controller.service.OmdbService;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailFragment extends AbstractFragment implements IConnectionTestListener {

    private String API = "http://www.omdbapi.com";
    private ImageView poster;
    private TextView txtDirector;
    private TextView txtactor;
    private TextView txtType;
    private TextView txtPlot;
    private TextView txtWriter;
    private TextView txtGenre;
    private TextView txtYear;
    private TextView txtTime;
    private TextView txtCountry;
    private TextView txtTotalSeasons;
    private TextView txtReleased;
    private TextView txtawards;

    private BroadcastReceiver mDLCompleteReceiver;


    private Filmes filmes;
    private String url;
    private String path;

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
        Log.i("Main Detail: ",getClass().getName()+" : "+filmes.getTitle());

        initActionBarScreen();
        initView();

        if(filmes != null){
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
        poster = (ImageView) findViewById(R.id.backdrop);
        txtDirector = (TextView) findViewById(R.id.txt_director);
        txtactor    = (TextView) findViewById(R.id.txt_actors);
        txtGenre    = (TextView) findViewById(R.id.txt_genre);
        txtPlot     = (TextView) findViewById(R.id.txt_plot);
        txtType     = (TextView) findViewById(R.id.txt_type);
        txtWriter   = (TextView) findViewById(R.id.txt_writer);
        txtYear     = (TextView) findViewById(R.id.txt_year);
        txtTime     = (TextView) findViewById(R.id.txt_time);
        txtCountry  = (TextView) findViewById(R.id.txt_country);
        txtTotalSeasons = (TextView) findViewById(R.id.txt_seasor);
        txtReleased = (TextView) findViewById(R.id.txt_released);
        txtawards   = (TextView) findViewById(R.id.txt_awards);

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
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbService git = retrofit.create(OmdbService.class);

        Call<Filmes> call = git.getOmdb(filmes.getTitle().toString().trim(), "sort", "json");
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
                    if (sucessJSON.getResponse() != null && sucessJSON.getResponse().equalsIgnoreCase("True")){
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


                        ModelBO.getInstance().addFilmes(item);

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

                        downloadFile(item);

                        url = sucessJSON.getPoster();

                        Picasso.with(getContext())
                                .load(sucessJSON.getPoster())
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(poster);


                        Log.i("desafio", "desafio.onSuccess - " + String.valueOf(sucessJSON.getImdbID()));
                    }else{
                        Toast.makeText(getContext(), "Filme "+filmes.getTitle()+" não encontrado.", Toast.LENGTH_SHORT).show();
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
                ((OmdbActivity) getActivity()).showMainScreen();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void inputData(Filmes filmes){

        Filmes f = ModelBO.getInstance().getSearch(filmes);
        if(f != null && f.getImdbID() != null) {
            txtDirector.setText(f.getDirector());
            txtactor.setText(f.getActors());
            txtPlot.setText(f.getPlot());
            txtWriter.setText(f.getWriter());
            txtGenre.setText(f.getGenre());
            txtYear.setText(f.getYear());
            txtType.setText(f.getType());
            txtTime.setText(f.getRuntime());
            txtTime.setText(f.getRuntime());
            txtReleased.setText(f.getReleased());
            txtTotalSeasons.setText(f.getTotalSeasons());
            txtCountry.setText(f.getCountry());
            txtawards.setText(f.getAwards());

            url = f.getPoster();
            Log.i("path","Url: "+url);
            Picasso.with(getContext())
                    .load(url)
                    .into(poster);

        }else {

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
            Toast.makeText(getContext(),R.string.error_connection,Toast.LENGTH_LONG).show();
        }
    }

    public void downloadFile(Filmes filmes) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Omdb");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        final DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        if(filmes.getPoster() != null && !filmes.getPoster().toString().equalsIgnoreCase("N/A")){

            String ur = filmes.getTitle().toString().trim().replace(" ","_");

            Uri downloadUri = Uri.parse(filmes.getPoster());
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setDestinationInExternalPublicDir("/Omdb", ur+".jpg");

            mgr.enqueue(request);

            path = "file://"+direct+"/"+ur+".jpg";
            File file = new File(path);

            Log.i("/","path: "+file.getPath());



            Filmes update = new Filmes();
            update.setId(filmes.getId());
            update.setPoster(file.getPath());

            ModelBO.getInstance().updateFilmesUrl(update);

        }else{
            Toast.makeText(AppApplication.getInstance().getContext(),"Imagem não encontrada: "+filmes.getPoster(), Toast.LENGTH_SHORT).show();
        }

    }
}

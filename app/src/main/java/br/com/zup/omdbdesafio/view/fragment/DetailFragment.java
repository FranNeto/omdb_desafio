package br.com.zup.omdbdesafio.view.fragment;


import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestHandler;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestThread;
import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import br.com.zup.omdbdesafio.controller.listener.IConnectionTestListener;
import br.com.zup.omdbdesafio.controller.permission.Permissions;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.presenter.DetailPresenter;
import br.com.zup.omdbdesafio.view.interfaces.DetailsFragmentImpl;
import br.com.zup.omdbdesafio.view.interfaces.ShowPermissions;
import butterknife.BindView;
import butterknife.OnClick;

public class DetailFragment extends AbstractFragment implements IConnectionTestListener, DetailsFragmentImpl, ShowPermissions {

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

    private Filmes filmes;
    private Filmes filmAdd;
    private String path;
    private boolean isFilmAdd = false;
    private DetailPresenter presenter;
    private final static String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

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

        presenter = new DetailPresenter(this);
        filmes = ModelBO.getInstance().getFilmeSelection();
        initActionBarScreen();

        inputData(filmes);

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

            Picasso.with(getContext())
                    .load(film.getPoster())
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
            presenter.getDetailsFilm(filmes);
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
        Permissions.permissionsStorage(this, PERMISSIONS);
    }

    @Override
    public void onSuccessoAdd(String msg) {
        isFilmAdd = true;
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        floatingAdd.setImageResource(R.drawable.ic_delete);
    }

    @Override
    public void onSuccessoDelete(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onError(String msg) {
        dismissProgress();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessDetails(Filmes filmes) {
        filmAdd = filmes;

        txtDirector.setText(filmes.getDirector());
        txtactor.setText(filmes.getActors());
        txtPlot.setText(filmes.getPlot());
        txtWriter.setText(filmes.getWriter());
        txtGenre.setText(filmes.getGenre());
        txtYear.setText(filmes.getYear());
        txtType.setText(filmes.getType());
        txtTime.setText(filmes.getRuntime());
        txtReleased.setText(filmes.getReleased());
        txtTotalSeasons.setText(filmes.getTotalSeasons());
        txtCountry.setText(filmes.getCountry());
        txtawards.setText(filmes.getAwards());

        Picasso.with(getContext())
                .load(filmes.getPoster())
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(poster);

        dismissProgress();

    }

    @Override
    public void showPermissionGranted(String permission) {
        if(!isFilmAdd) {
            saveFilme();
        }else{
            deleteFilm();
        }
    }

    @Override
    public void showPermissionDenied(String permission, boolean isPermanentlyDenied) {

    }
}

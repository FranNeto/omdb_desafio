package br.com.zup.omdbdesafio.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.activity.OmdbActivity;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.model.domain.SearchFilms;
import br.com.zup.omdbdesafio.view.adapter.MainAdapter;
import butterknife.BindView;

public class MainFragment extends AbstractFragment{

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private List<Filmes> filmesList;
    private List<SearchFilms> searchFilmsListList;
    private MainAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.content_main, (ViewGroup) null);

        registerBackPress(null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideKeyboard();
        initActionBarScreen();
        initView();
    }

    public void listFilmes(){
        filmesList = new ArrayList<>();
        filmesList = ModelBO.getInstance().getFilmes();
}

    private void initActionBarScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        toolbar.setTitle("Odmb");
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                ((OmdbActivity) getActivity()).showRegister();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void initView() {

        listFilmes();
        adapter = new MainAdapter(getContext(),filmesList,this);

        if(mRecyclerView != null){
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        }

        ModelBO.getInstance().setSearchFilmsList(null);

    }

    public void showDetailFilme(Filmes filmes){
         if(filmes != null) {
             ModelBO.getInstance().setFilmeSelection(filmes);
             ((OmdbActivity) getActivity()).showDetail();
         }
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

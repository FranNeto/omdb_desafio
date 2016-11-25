package br.com.zup.omdbdesafio.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.activity.OmdbActivity;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.view.adapter.MainAdapter;

public class MainFragment extends AbstractFragment implements View.OnClickListener {

    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private List<Filmes> filmesList;
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

    }

    private void initView() {
       fab = (FloatingActionButton) findViewById(R.id.float_add);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        listFilmes();

        adapter = new MainAdapter(getContext(),filmesList,this);

        if(mRecyclerView != null){
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        }


        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.float_add:
                ((OmdbActivity) getActivity()).showRegister();
                break;
        }
    }

    public void showDetailFilme(Filmes filmes){
        Log.i("Main ",getClass().getName()+" : "+filmes.getTitle());
         ModelBO.getInstance().setFilmeSelection(filmes);
        ((OmdbActivity)getActivity()).showDetail();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

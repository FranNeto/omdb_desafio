package br.com.zup.omdbdesafio.view.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.controller.activity.OmdbActivity;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestHandler;
import br.com.zup.omdbdesafio.controller.component.ConnectionTestThread;
import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import br.com.zup.omdbdesafio.controller.listener.IConnectionTestListener;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.model.domain.SearchFilms;
import br.com.zup.omdbdesafio.presenter.SearchViewFilmsPresenter;
import br.com.zup.omdbdesafio.view.adapter.SearchViewFilmsAdapter;
import br.com.zup.omdbdesafio.view.interfaces.SearchViewFilmsImpl;
import butterknife.BindView;

public class SearchViewFilmsFragment extends AbstractFragment implements IConnectionTestListener, SearchViewFilmsImpl {

    private static final String TAG = SearchViewFilmsFragment.class.getSimpleName();

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private SearchView searchView;
    private String mQuery;

    private SearchViewFilmsAdapter adapter;
    private List<SearchFilms.Search> filmsList;
    private SearchViewFilmsPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.activity_register, (ViewGroup) null);
        registerBackPress(null);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = new SearchViewFilmsPresenter(this);
        initActionBarScreen();
        initView();
    }

    private void initView() {

        filmsList = new ArrayList<>();
        adapter = new SearchViewFilmsAdapter(getActivity(), filmsList, this);

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }

    private void initActionBarScreen() {
        final AppCompatActivity activity = initActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.txt_title_search));
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.update(ModelBO.getInstance().getSearchFilmsList());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) AppApplication.getInstance().getContext().getSystemService(Context.SEARCH_SERVICE);

        MenuItem item = menu.findItem(R.id.action_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            searchView = (SearchView) item.getActionView();
        } else {
            searchView = (SearchView) MenuItemCompat.getActionView(item);
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getActivity().getComponentName()));
        searchView.setQueryHint(getResources().getString(R.string.action_search));
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query.toString().trim();
                connectionTest();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                    hideKeyboard();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDetailFilme(SearchFilms.Search searchFilms) {
        if (searchFilms != null) {
            Filmes filmes = new Filmes();
            filmes.setTitle(searchFilms.getTitle());
            filmes.setImdbID(searchFilms.getImdbID());

            ModelBO.getInstance().setFilmeSelection(filmes);
            ((OmdbActivity) getActivity()).showDetail();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }


    private void connectionTest() {
        showProgress();
        new ConnectionTestThread(getContext(), new ConnectionTestHandler(this)).start();
    }

    @Override
    public void endConnectionTest(Boolean isSent) {
        if (isSent) {
            mPresenter.downloadRepositories(mQuery);
        } else {
            dismissProgress();
            Toast.makeText(getContext(), R.string.error_connection, Toast.LENGTH_LONG).show();
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


    @Override
    public void onSuccesso(List<SearchFilms.Search> searchList) {
        dismissProgress();
        filmsList = searchList;
        adapter.update(filmsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        dismissProgress();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}

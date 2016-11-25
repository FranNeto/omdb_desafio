package br.com.zup.omdbdesafio.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.activity.OmdbActivity;
import br.com.zup.omdbdesafio.model.ModelBO;
import br.com.zup.omdbdesafio.model.domain.Filmes;

public class RegisterFragment extends AbstractFragment implements View.OnClickListener {

    private TextView btSave;
    private TextView txtName;
    private EditText editName;

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
        Log.i("onActivityCreated",getClass().getName());
        initActionBarScreen();
        initView();
    }

    private void initActionBarScreen() {
        final AppCompatActivity activity = initActionBar();
        if (toolbar != null) {
            toolbar.setTitle(getString(R.string.txt_title_regsiter));
        }

    }

    private void initView() {
        btSave   = (TextView) findViewById(R.id.bt_save);
        txtName  = (TextView) findViewById(R.id.txt_name);
        editName = (EditText) findViewById(R.id.edit_name);

        btSave.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.bt_save:
                hideKeyboard();
                btSave.setEnabled(false);

                if(editName.getText().toString() != null && !editName.getText().toString().isEmpty()){
                    Filmes item = new Filmes();
                    item.setTitle(editName.getText().toString());

                    List<Filmes> title =  ModelBO.getInstance().getSearchFilmes(item.getTitle());
                    if(title != null && title.size() > 0 && title.get(0).getTitle().equalsIgnoreCase(editName.getText().toString())){
                        Toast.makeText(getContext(),"Filme já está registrado",Toast.LENGTH_SHORT).show();
                        btSave.setEnabled(true);
                    }else{

                        ModelBO.getInstance().addFilmes(item);

                        Toast.makeText(getContext(),"Filme registrado com Sucesso",Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((OmdbActivity) getActivity()).showMainScreen();
                                btSave.setEnabled(true);
                            }
                        },1000);

                    }



                }else{
                    Toast.makeText(getContext(),"Digite o nome do Filme",Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btSave.setEnabled(true);
                    }
                },1000);
                break;
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

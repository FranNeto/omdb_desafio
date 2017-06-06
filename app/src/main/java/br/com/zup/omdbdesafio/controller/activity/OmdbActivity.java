package br.com.zup.omdbdesafio.controller.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.view.Frags;


public class OmdbActivity extends AbstractFragmentActivity {

    private transient View viewLoadingText;
    private transient View viewLoading;
    private transient TextView textLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       Log.e("OmdbActivity", getClass().getName());

        setContentView(R.layout.activity_main);

        this.viewLoading = findViewById(R.id.progress_init_app);
        this.viewLoadingText = findViewById(R.id.rl_progress_main_with_text);
        this.textLoading = (TextView) viewLoadingText.findViewById(R.id.txt_progress_main_with_text);

        viewLoading.setVisibility(View.GONE);

        showMainScreen();

    }

    @Override
    public void showDetail() {
        registerFragment(Frags.DETAIL);
        replaceFragment(Frags.DETAIL,true);
    }

    @Override
    public void showMainScreen() {
        if (fragMan.getBackStackEntryCount() > 0) {
            popBackStackStart();
        }
        registerFragment(Frags.MAIN);
        replaceFragment(Frags.MAIN, false);
    }

    @Override
    public void showRegister() {
        registerFragment(Frags.REGISTER);
        replaceFragment(Frags.REGISTER, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void showLoading(boolean visible) {
        if (!visible) {
            viewLoadingText.setVisibility(View.GONE);
            viewLoading.setVisibility(View.GONE);
        } else {
            viewLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

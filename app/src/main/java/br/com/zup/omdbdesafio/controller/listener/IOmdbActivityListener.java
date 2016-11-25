package br.com.zup.omdbdesafio.controller.listener;


import br.com.zup.omdbdesafio.view.Frags;
import br.com.zup.omdbdesafio.view.fragment.AbstractFragment;

public interface IOmdbActivityListener {

    String lastFragmentName();
    String recentFragmentName();
    void popBackStack(Frags frags);
    void popBackStack();
    void showDetail();
    void showMainScreen();
    void showRegister();

    AbstractFragment getAFragment(Frags frag);
    void showLoading(final boolean visible);


}

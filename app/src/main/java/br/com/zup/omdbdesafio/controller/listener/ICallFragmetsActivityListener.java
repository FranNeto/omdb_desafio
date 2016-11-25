package br.com.zup.omdbdesafio.controller.listener;


import br.com.zup.omdbdesafio.view.Frags;

public interface ICallFragmetsActivityListener {
	boolean replaceFragment(final Frags frag, final int resId);
	boolean replaceFragment(final Frags frag, final int resId, final boolean addBackStack);
	void replaceFragment(final Frags frag, final boolean addBackStack);
	boolean replaceFragment(final Frags frag, final int resId, final boolean addBackStack, final Integer animResIdEnter, final Integer animResIdExit);

}

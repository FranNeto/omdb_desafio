package br.com.zup.omdbdesafio.view;

import java.io.Serializable;

import br.com.zup.omdbdesafio.view.fragment.AbstractFragment;
import br.com.zup.omdbdesafio.view.fragment.DetailFragment;
import br.com.zup.omdbdesafio.view.fragment.MainFragment;
import br.com.zup.omdbdesafio.view.fragment.SearchViewFilmsFragment;


public enum Frags implements Serializable {


	MAIN  (MainFragment.class),
	REGISTER(SearchViewFilmsFragment.class),
	DETAIL(DetailFragment.class);

	private final Class<? extends AbstractFragment> classFrag;

	Frags(final Class<? extends AbstractFragment> classFrag) {
		this.classFrag = classFrag;
	}

	public String getName() {
		return classFrag.getSimpleName();
	}
	public Class<? extends AbstractFragment> getClassFrag() {
		return classFrag;
	}
}

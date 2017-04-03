
package br.com.zup.omdbdesafio.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.controller.activity.AbstractFragmentActivity;
import br.com.zup.omdbdesafio.controller.listener.IOmdbActivityListener;
import br.com.zup.omdbdesafio.controller.listener.IBackPressListener;
import butterknife.ButterKnife;

public abstract class AbstractFragment extends Fragment {

	private transient final String sClassName = getClass().getCanonicalName();
	protected transient View rootView;
	protected transient Toolbar toolbar;

	public IOmdbActivityListener fragmentListener;

	public AbstractFragment() {
		setRetainInstance(true);
	}

	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewCompat.requestApplyInsets(view);
		ButterKnife.bind(this,view);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if ( getActivity() != null && getActivity() instanceof IOmdbActivityListener) {
			fragmentListener = (IOmdbActivityListener) getActivity();
		}
	}


	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);

	}


	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);

	}


	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	/**
	 * Method to initialize base action bar
	 *
	 * @author
	 */
	public AppCompatActivity initActionBar() {
		if (rootView != null && getActivity() instanceof AppCompatActivity) {
			final AppCompatActivity activity = (AppCompatActivity) getActivity();
			toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
			final ActionBar ab = activity.getSupportActionBar();

			activity.setSupportActionBar(toolbar);
			activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			if (ab != null) {
				ab.setDisplayHomeAsUpEnabled(true);
			}
			setHasOptionsMenu(true);//create menu

			return activity;
		} else {
			return null;
		}
	}


	public void registerBackPress(final IBackPressListener backPressListener) {
		if (getActivity() instanceof AbstractFragmentActivity) {
			((AbstractFragmentActivity) getActivity()).registerBackPress(backPressListener);
		}
	}

	public IBackPressListener getBackPressListener() {
		if (getActivity() instanceof AbstractFragmentActivity) {
			return ((AbstractFragmentActivity) getActivity()).getBackPressListener();
		}

		return null;
	}
	public View findViewById(int resId) {

		View objRet = null;

		if(rootView != null) {
			objRet = rootView.findViewById(resId);
		}

		return objRet;
	}


}
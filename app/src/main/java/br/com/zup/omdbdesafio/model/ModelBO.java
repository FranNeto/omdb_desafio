package br.com.zup.omdbdesafio.model;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.zup.omdbdesafio.controller.AppApplication;
import br.com.zup.omdbdesafio.model.business.EnviromentManager;
import br.com.zup.omdbdesafio.model.business.ObjectAlreadyExistException;
import br.com.zup.omdbdesafio.model.domain.Filmes;

public class ModelBO {

	private static ModelBO instance = null;
	private static final Object SYNCOBJECT = new Object();

	private boolean isTablet;
	private boolean isClickable = true;
	private long clickLockKey = 0;
	private EnviromentManager am;
	private Filmes filmeSelection;


	public static ModelBO getInstance() {
		synchronized (SYNCOBJECT) {
			if (instance == null) {
				instance = new ModelBO();
			}
		}
		return instance;
	}

	public void hidKeyboad(final View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	synchronized public boolean isClickable(long time) {
		return (time == clickLockKey);
	}

	synchronized public long setClickable() {
		if ( isClickable ) {
			isClickable = false;
			clickLockKey = System.currentTimeMillis() + 500;
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					isClickable = true;
				}
			}, 500);

			return clickLockKey;
		}

		return 0;
	}


	public void vibrate(final Context context) {
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	}


	public void animateView(final ViewGroup view, final String property, final int duration) {
		final boolean isOpening = View.GONE == view.getVisibility();
		int size = getSize(view, property);
		final float start = isOpening ? size : 0;
		final float end = isOpening ? 0 : size;

		ObjectAnimator animator = ObjectAnimator.ofFloat(view, property, start, end);
		animator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				if ( isOpening ) {
					view.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				if ( !isOpening ) {
					view.setVisibility(View.GONE);
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {}
		});
		animator.setDuration(duration);
		animator.start();
	}

	public void animateView(final ViewGroup view, final String property, final int duration, final float startAxisPosition, final float finalAxisPosition) {
		final float start = startAxisPosition;
		final float end = finalAxisPosition;

		ObjectAnimator animator = ObjectAnimator.ofFloat(view, property, start, end);
		animator.setDuration(duration);
		animator.start();
	}

	private int getSize(ViewGroup view, String property) {
		int size = 0;
		if ( property.equals("translationX") ) {
			size = view.getWidth();
		} else if (property.equals("translationY")) {
			size = view.getHeight();
		}

		return size;
	}




	public boolean isTablet() {
		return isTablet;
	}

	public void setTablet(boolean isTablet) {
		this.isTablet = isTablet;
	}



	public void addFilmes(Filmes filmes) {
		am = (EnviromentManager) AppApplication.getInstance().get(EnviromentManager.KEY);
		try {
			am.insertFilmes(filmes);
		} catch (SQLException e) {
			Log.i("","Erro ao adicionar uma nova pesquisa " + e.getMessage());
		} catch (ObjectAlreadyExistException e) {
			Log.i("","Dados já foram inseridos" + e.getMessage());
		} catch (Exception e) {
			Log.i("","Erro:" + e.getMessage());
		}
	}

	public void updateFilmesUrl(Filmes filmes) {
		am = (EnviromentManager) AppApplication.getInstance().get(EnviromentManager.KEY);
		try {
			am.updateFilmes(filmes);
		} catch (SQLException e) {
			Log.i("","Erro ao atualizar Url " + e.getMessage());
		}
	}

	public Filmes getSearch(Filmes filmes) {

		am = (EnviromentManager) AppApplication.getInstance().get(EnviromentManager.KEY);
		 Filmes result = null;
		try {
			result = am.getSearch(filmes);
		} catch (SQLException e) {
			Log.i(""," Não foi possível consultar passos cadastrados. " + e.getMessage());
		}
		return result;
	}

	public List<Filmes> getFilmes() {
		am = (EnviromentManager) AppApplication.getInstance().get(EnviromentManager.KEY);
		List<Filmes> questions = new ArrayList<Filmes>();
		try {
			questions = am.getFilmesList();
		} catch (SQLException e) {
			Log.i(""," Não foi possível consultar passos cadastrados. " + e.getMessage());
		}
		return questions;
	}

	public List<Filmes> getSearchFilmes(String title) {
		am = (EnviromentManager) AppApplication.getInstance().get(EnviromentManager.KEY);
		List<Filmes> filme = new ArrayList<>();
		try {
			filme = am.getSearchList(title);
		} catch (SQLException e) {
			Log.i(""," Não foi possível consultar passos cadastrados. " + e.getMessage());
		}
		return filme;
	}

	public Filmes getFilmeSelection() {
		return filmeSelection;
	}

	public void setFilmeSelection(Filmes filmeSelection) {
		this.filmeSelection = filmeSelection;
	}
}

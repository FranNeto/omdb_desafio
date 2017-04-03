package br.com.zup.omdbdesafio.controller;


import android.app.Application;
import android.content.Context;

import java.util.HashMap;

import br.com.zup.omdbdesafio.R;
import br.com.zup.omdbdesafio.model.business.EnviromentManager;


public class AppApplication extends Application {
	private static AppApplication singleton = null;
	private HashMap<String, Object> attributes = new HashMap<String, Object>();
	private final String OMDB = "OMDB";

	public static AppApplication getInstance() {
		return singleton;
	}

	public AppApplication() {
		singleton = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		EnviromentManager am = EnviromentManager.getInstance();
		am.startSession( getApplicationContext() );
		put(EnviromentManager.KEY, am);
		singleton = this;

	}

	public Context getContext(){
		return getApplicationContext();
	}

	public void setAttributes(HashMap<String, Object> attributes) {
		this.attributes = attributes;
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public void put(final String key, final Object value) {
		this.attributes.put(key, value);
	}

	public Object get(final String key) {
		return this.attributes.get(key);
	}

}

package br.com.zup.omdbdesafio.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.zup.omdbdesafio.model.domain.Filmes;


public class AppSqliteOpenHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "bd_application.sqlite";
	
	private static final int DATABASE_VERSION = 1;
	private static String TAG = "AppSqliteOpenHelper";
	
	public AppSqliteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		Log.i(TAG,"Criando a Tabela do Banco de Dados");
		try{

			TableUtils.createTable(connectionSource, Filmes.class);

		}catch(SQLException e){
			Log.i(TAG,"Ocorreu um erro ao tentar criar a tabela do banco de dados."+ e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase dataBase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.i(TAG,"Atualizando Banco de dados. da vers?o[" + oldVersion + "] para a vers?o [" + newVersion + "]");
	}
}

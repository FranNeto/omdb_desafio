package br.com.zup.omdbdesafio.model.business;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.zup.omdbdesafio.model.domain.Filmes;
import br.com.zup.omdbdesafio.model.persistence.AppSqliteOpenHelper;


public class EnviromentManager implements IAppBusinessLogic {
    public static final String KEY = "ModelBO";
    private static EnviromentManager SINGLETON;

    private AppSqliteOpenHelper sqLite;

    public static EnviromentManager getInstance() {
        if (SINGLETON == null) {
            SINGLETON = new EnviromentManager();
        }
        return SINGLETON;
    }

    public void startSession(Context ctx) {
        sqLite = new AppSqliteOpenHelper(ctx);
        sqLite.getWritableDatabase();
    }



    @Override
    public Filmes getSearch(Filmes filmes) throws SQLException {
        QueryBuilder<Filmes, Integer> queryBuilder = (QueryBuilder<Filmes, Integer>) sqLite.getDao(Filmes.class).queryBuilder();
        queryBuilder.where().eq("id", filmes.getId());
        queryBuilder.limit(1);
        filmes = queryBuilder.queryForFirst();
        return filmes;
    }

    @Override
    public List<Filmes> getFilmesList() throws SQLException {
        List<Filmes> result = null;
        QueryBuilder queryBuilder = sqLite.getDao(Filmes.class).queryBuilder();
        result = queryBuilder.query();
        return result;
    }

    @Override
    public List<Filmes> getSearchList(String  item) throws SQLException {
        List<Filmes> result = null;
        QueryBuilder queryBuilder = sqLite.getDao(Filmes.class).queryBuilder();
		queryBuilder.where().eq("Title", item);
        queryBuilder.limit(1);
        result = queryBuilder.query();
        return result;
    }

    @Override
    public void insertFilmes(Filmes research) throws SQLException, ObjectAlreadyExistException {
        Dao<Filmes, Long> dao = sqLite.getDao(Filmes.class);
        sqLite.getDao(Filmes.class).createOrUpdate(research);
    }

    @Override
    public void updateFilmes(Filmes filmes) throws SQLException {
        UpdateBuilder updateBuilder = sqLite.getDao(Filmes.class).updateBuilder();
        updateBuilder.where().eq("id", filmes.getId());
        updateBuilder.updateColumnValue("Poster", filmes.getPoster());
        updateBuilder.update();
    }

}

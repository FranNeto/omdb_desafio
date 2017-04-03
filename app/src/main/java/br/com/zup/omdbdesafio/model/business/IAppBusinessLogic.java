package br.com.zup.omdbdesafio.model.business;

import java.sql.SQLException;
import java.util.List;

import br.com.zup.omdbdesafio.model.domain.Filmes;

public interface IAppBusinessLogic {
	Filmes getSearch(Filmes filmes) throws SQLException;

	List<Filmes> getFilmesList() throws SQLException;

	List<Filmes> getSearchList(String  item) throws SQLException;

	void insertFilmes(Filmes research) throws SQLException, ObjectAlreadyExistException;
	void deleteFilm(Filmes research) throws SQLException, ObjectAlreadyExistException;
	void updateFilmes(Filmes filmes) throws SQLException;


}

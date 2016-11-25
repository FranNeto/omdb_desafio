package br.com.zup.omdbdesafio.model.domain;



import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

public class Filmes implements Serializable {

    private static final long serialVersionUID = 1930564835504057515L;

    @DatabaseField(columnName="id",id = true)
    private Integer id;
    @DatabaseField(columnName = "Title")
    private String Title;
    @DatabaseField(columnName = "Year")
    private String Year;
    @DatabaseField(columnName = "Rated")
    private String Rated;
    @DatabaseField(columnName = "Released")
    private String Released;
    @DatabaseField(columnName = "Runtime")
    private String Runtime;
    @DatabaseField(columnName = "Genre")
    private String Genre;
    @DatabaseField(columnName = "Director")
    private String Director;
    @DatabaseField(columnName = "Writer")
    private String Writer;
    @DatabaseField(columnName = "Actors")
    private String Actors;
    @DatabaseField(columnName = "Plot")
    private String Plot;
    @DatabaseField(columnName = "Language")
    private String Language;
    @DatabaseField(columnName = "Country")
    private String Country;
    @DatabaseField(columnName = "Awards")
    private String Awards;
    @DatabaseField(columnName = "Poster")
    private String Poster;
    @DatabaseField(columnName = "Metascore")
    private String Metascore;
    @DatabaseField(columnName = "imdbRating")
    private String imdbRating;
    @DatabaseField(columnName = "imdbVotes")
    private String imdbVotes;
    @DatabaseField(columnName = "imdbID")
    private String imdbID;
    @DatabaseField(columnName = "Type")
    private String Type;
    @DatabaseField(columnName = "totalSeasons")
    private String totalSeasons;

    private String Response;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAwards() {
        return Awards;
    }

    public void setAwards(String awards) {
        Awards = awards;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getMetascore() {
        return Metascore;
    }

    public void setMetascore(String metascore) {
        Metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(String totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        Filmes other = (Filmes) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }

        if ( hashCode() != other.hashCode() ) {
            return false;
        }

        return true;
    }

}

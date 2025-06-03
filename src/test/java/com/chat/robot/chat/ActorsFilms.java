package com.chat.robot.chat;

import java.util.List;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ActorsFilms
 * @Description TODO
 * @date 2025/6/3 13:40
 **/
public class ActorsFilms {

    private String actor;

    private List<String> movies;

    public ActorsFilms() {
    }

    public String getActor() {
        return this.actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public List<String> getMovies() {
        return this.movies;
    }

    public void setMovies(List<String> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "ActorsFilms{" + "actor='" + this.actor + '\'' + ", movies=" + this.movies + '}';
    }
}
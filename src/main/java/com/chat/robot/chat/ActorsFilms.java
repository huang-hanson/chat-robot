package com.chat.robot.chat;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@JsonPropertyOrder({"actor", "movies"})
//@JsonPropertyOrder({"movies", "actor"})
public class ActorsFilms {

    private String actor;

    private List<String> movies;

    @Override
    public String toString() {
        return "ActorsFilms{" + "actor='" + this.actor + '\'' + ", movies=" + this.movies + '}';
    }
}
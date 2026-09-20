package org.java26.exceptions;

public class PokemonSaveException extends RuntimeException {
    public PokemonSaveException(String message) {
        super(message);
    }
}

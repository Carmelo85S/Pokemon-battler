package org.java26.exceptions;

public class InvalidPokemonNameException extends RuntimeException {
    public InvalidPokemonNameException(String message) {
        super(message);
    }
}

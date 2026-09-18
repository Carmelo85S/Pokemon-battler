package org.java26;

public class InvalidPokemonNameException extends RuntimeException {
    public InvalidPokemonNameException(String message) {
        super(message);
    }
}

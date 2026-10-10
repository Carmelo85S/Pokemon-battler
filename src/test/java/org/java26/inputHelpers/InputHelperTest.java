package org.java26.inputHelpers;

import org.java26.exceptions.InvalidPokemonException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputHelperTest {

    @Test
    void nullShouldThrowInvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString(null)
        );

        assertEquals("  Input cannot be empty.", exception.getMessage());
    }

    @Test
    void blankShouldThrowInvalidPokemonException() {
        assertThrows(
                InvalidPokemonException.class,
                () -> InputHelper.validateString(" ")
        );
    }

    @Test
    void inputShouldContainAtLeastOneLetter(){
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("123")
        );
        assertEquals("  Input must contain at least one letter.", exception.getMessage());

    }


    @Test
    void specialCharShouldThrowInvalidPokemonException(){
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString(" %&!Pizza")
        );

        assertEquals("  No special char allowed.", exception.getMessage());
    }


    @Test
    void inputToShortThrowInvalidPokemonException(){
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("A")
                );
        assertEquals("  Input must be between 2 and 11 characters.", exception.getMessage());
    }

    @Test
    void inputToLongThrowInvalidPokemonException(){
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("CharizardEvolve")
        );
        assertEquals("  Input must be between 2 and 11 characters.", exception.getMessage());
    }
}
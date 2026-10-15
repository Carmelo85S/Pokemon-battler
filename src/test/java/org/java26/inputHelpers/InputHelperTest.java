package org.java26.inputHelpers;

import org.java26.exceptions.InvalidPokemonException;
import org.java26.handlers.InputHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputHelperTest {

    @Test
    void null_ShouldThrow_InvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString(null)
        );

        assertEquals("  Input cannot be empty.", exception.getMessage());
    }

    @Test
    void blank_ShouldThrow_InvalidPokemonException() {
        assertThrows(
                InvalidPokemonException.class,
                () -> InputHelper.validateString(" ")
        );
    }

    @Test
    void input_ShouldContain_AtLeast_OneLetter() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("123")
        );
        assertEquals("  Input must contain at least one letter.", exception.getMessage());

    }

    @Test
    void specialChar_ShouldThrow_InvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString(" %&!Pizza")
        );

        assertEquals("  No special char allowed.", exception.getMessage());
    }

    @Test
    void inputToShort_Throw_InvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("A")
        );
        assertEquals("  Input must be between 2 and 11 characters.", exception.getMessage());
    }

    @Test
    void inputToLong_Throw_InvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateString("CharizardEvolve")
        );
        assertEquals("  Input must be between 2 and 11 characters.", exception.getMessage());
    }

    @Test
    void blankInput_ShouldThrow_InvalidPokemonException() {
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateInt(
                        " ", 1, 2
                )
        );
        assertEquals("  Your input is blank", exception.getMessage());
    }

    @Test
    void notNumericInput_ShouldThrow_InvalidPokemonException(){
        var exception = assertThrows(
                InvalidPokemonException.class, () -> InputHelper.validateInt(
                        "abc", 1, 3
                    )

        );
    }
}
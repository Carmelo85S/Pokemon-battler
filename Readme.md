# Pokédex (Work in progress)

A Java console application for managing a collection of Pokémon.

The application allows the user to view, create, edit and delete Pokémon, as well as save and load data from a file. The application also includes input validation to prevent crashes caused by invalid user input.

## How to Run

### IntelliJ IDEA

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Make sure Java 21 is configured.
4. Run `Main.java`.

### Maven

From the project root, run:

```bash
mvn clean compile
```

Then run the `Main` class.

## Example Usage

When the application starts, the user is shown the main menu:

```text
1 - Show all Pokemon.
2 - Insert a new Pokemon
3 - Customize your Pokemon
4 - Delete your Pokemon
5 - Save to file.
6 - Load from file.
7 - Reset to seed data
8 - Exit.

Your choice is: 1
```

The application then displays the Pokémon stored in the Pokédex:

```text
Pikachu
Bulbasaur
Charizard
```

Invalid input is handled without crashing the application:

```text
Your choice is: abc
Invalid input! Please enter only one number.

Your choice is: 99
Please insert a number between 1 and 8.

Your choice is: -4
Please insert a number between 1 and 8.
```

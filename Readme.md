# Pokémon Battler

A Java console application where the player can manage Pokémon and battle against computer-controlled opponents.

This project is an extension of my previous **Pokédex CRUD application**. 
The original functionality has been refactored and extended with a turn-based battle system.

## Features

### Pokédex

* Create Pokémon
* View Pokémon
* Update Pokémon
* Delete Pokémon
* Add and remove attacks
* Save and load Pokémon data
* Input validation and error handling

### Battle System

* Choose a Pokémon to battle with
* Fight against a random wild Pokémon
* Turn-based battles
* Choose attacks
* CPU chooses attacks randomly
* Accuracy affects whether attacks hit
* Damage includes a random factor
* Type effectiveness
* Critical hits
* Battle log
* Win/loss result

## Damage Formula

```text
Damage = Base Damage × Type Effectiveness × Random Factor
```

Critical hits can multiply the final damage.

## Type Effectiveness

| Attack   | Against | Effect |
| -------- | ------- | -----: |
| Fire     | Grass   |     2× |
| Water    | Fire    |     2× |
| Grass    | Water   |     2× |
| Electric | Water   |     2× |
| Fire     | Water   |   0.5× |
| Water    | Grass   |   0.5× |
| Grass    | Fire    |   0.5× |
| Normal   | Any     |     1× |

## Persistence

Pokémon and battle statistics are saved between sessions using **JSON and Jackson**.

## OOP

The project uses object-oriented programming with:

* Classes and objects
* Encapsulation
* Getters and setters
* Enums
* Collections
* Exceptions
* Inheritance / polymorphism where appropriate

The project has been refactored from the previous Pokédex to improve separation of responsibilities and encapsulation.

## Technologies

* Java 21
* Maven
* Jackson
* JUnit 5
* IntelliJ IDEA
* Git / GitHub

## How to Run

Clone the repository and open the project in IntelliJ IDEA.

Make sure Java 21 is installed, then run the `Main` class.

Alternatively:

```bash
mvn clean compile
```

## VG Requirements

* [x] Type effectiveness
* [x] JSON persistence
* [x] Critical hits
* [x] Persistent battle statistics
* [ ] Unit tests

## Course

**Programmering med Java, grund — Inlämning 2**

**Project:** Pokémon Battler
**Student:** Carmelo Salis

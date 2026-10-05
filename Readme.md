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
* Type effectiveness
* Random damage factor
* Battle log
* Win/loss result
* Persistent win/loss statistics

## Damage Formula

Damage is calculated using the following formula:

```text
Damage = Base Damage × Type Effectiveness × Random Factor
The random factor is a random value between 0.85 and 1.00.
The final damage is rounded down to the nearest integer.
```

## Example:

Base Damage = 50
Type Effectiveness = 2.0
Random Factor = 0.90

Damage = 50 × 2.0 × 0.90 = 90

## Type Effectiveness

| Attack | Against | Effect |
|---|---|---:|
| Fire | Grass | 2× |
| Water | Fire | 2× |
| Grass | Water | 2× |
| Electric | Water | 2× |
| Fire | Water | 0.5× |
| Water | Grass | 0.5× |
| Grass | Fire | 0.5× |
| Electric | Grass | 0.5× |
| Other combinations | Any | 1× |
## Battle Flow

The player selects a Pokémon from their Pokédex.
The CPU randomly selects a Pokémon from the wild Pokémon pool.
The first player is selected randomly.
The player selects an attack from a menu.
The CPU selects an attack randomly.
Accuracy determines whether the attack hits.
Damage is calculated using the damage formula.
HP is reduced by the calculated damage.
The battle continues until one Pokémon reaches 0 HP.
The result is displayed and the win/loss statistics are updated.

### Critical Hits

During battle, every attack has a **1 in 15 chance** of being a critical hit.

The critical hit is determined randomly:

```java
boolean criticalHit = random.nextInt(15) == 0;
```

`random.nextInt(15)` generates a value between `0` and `14`. If the result is `0`, the attack is critical.

* **1/15 chance** → Critical Hit (~6.67%)
* **14/15 chance** → Normal Hit
* A critical hit deals **double damage**

The critical-hit result is passed to the attack through `execute()`:

```java
attack.execute(attacker, defender, criticalHit);
```

`DamageAttack` then applies the critical-hit multiplier:

```java
@Override
public void execute(Pokemon attacker, Pokemon defender, boolean criticalHit) {
    int damage = power;

    if (criticalHit) {
        damage *= 2;
    }

    defender.takeDamage(damage);
}
```

This keeps the **random critical-hit calculation** separate from the **attack-specific damage logic**, using polymorphism.


## Persistence
Pokémon and battle statistics are saved between sessions using JSON and Jackson.
OOP
The project uses object-oriented programming with:
Classes and objects
Encapsulation
Getters and setters
Enums
Collections
Exceptions

## The project has been refactored from the previous Pokédex to improve separation of responsibilities and encapsulation.

## Technologies

Java 21
Maven
Jackson
JUnit 5
IntelliJ IDEA
Git / GitHub

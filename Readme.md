# Pokémon Battler

A Java console application where the player can manage Pokémon and battle against computer-controlled opponents.

This project is an extension of my previous **Pokédex CRUD application**. The original functionality has been refactored and extended with a turn-based battle system.

## Features

### Pokédex

* Create Pokémon
* View Pokémon
* Update Pokémon
* Delete Pokémon
* Add and remove attacks
* Save and load Pokémon data
* Input validation
* Error handling

### Battle System

* Choose a Pokémon to battle with
* Fight against a random wild Pokémon
* Turn-based battles
* Choose attacks
* CPU chooses attacks randomly
* Accuracy affects whether attacks hit
* Type effectiveness
* Random damage factor
* Critical hits
* Battle log
* Win/loss result
* Persistent win/loss statistics

## Damage Formula

Damage is calculated using three factors:

```text
Damage = Base Damage × Type Effectiveness × Random Factor
```

The random factor introduces a small amount of variation to each attack.

```java
double randomFactor = 0.85 + random.nextDouble() * 0.15;
```

`random.nextDouble()` generates a value between `0.0` and `1.0`, resulting in a random factor between **0.85 and 1.00**.

The final damage is rounded down to the nearest integer.

### Example

```text
Base Damage       = 50
Type Effectiveness = 2.0
Random Factor     = 0.90
```

```text
Damage = 50 × 2.0 × 0.90
       = 90
```

## Type Effectiveness

| Attack             | Against | Effect |
| ------------------ | ------- | -----: |
| Fire               | Grass   |     2× |
| Water              | Fire    |     2× |
| Grass              | Water   |     2× |
| Electric           | Water   |     2× |
| Fire               | Water   |   0.5× |
| Water              | Grass   |   0.5× |
| Grass              | Fire    |   0.5× |
| Electric           | Grass   |   0.5× |
| Other combinations | Any     |     1× |

## Critical Hits

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

The concrete attack implementation then applies the critical-hit multiplier together with the other damage modifiers:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
) {
    double effectiveness =
            BattleLogic.effectiveness(defender, this);

    double randomFactor =
            0.85 + random.nextDouble() * 0.15;

    double damage =
            getBaseDamage()
                    * effectiveness
                    * randomFactor;

    if (criticalHit) {
        damage *= 2;
    }

    defender.takeDamage((int) damage);
}
```

For example:

```text
Base Damage       = 100
Effectiveness     = 2.0
Random Factor     = 0.94
Critical Hit      = Yes
```

Normal damage:

```text
100 × 2.0 × 0.94 = 188
```

Critical damage:

```text
188 × 2 = 376
```

Therefore, the complete damage calculation is:

```text
Base Damage
    × Type Effectiveness
    × Random Factor
    × Critical Multiplier
    = Final Damage
```

Where:

```text
Critical Multiplier = 2.0 when critical
Critical Multiplier = 1.0 when normal
```

## Battle Flow

1. The player selects a Pokémon from their Pokédex.
2. The CPU randomly selects a Pokémon from the wild Pokémon pool.
3. The first player is selected randomly.
4. The player selects an attack from a menu.
5. The CPU selects an attack randomly.
6. Accuracy determines whether the attack hits.
7. A critical hit is randomly determined.
8. The attack is executed through the polymorphic `execute()` method.
9. Type effectiveness and random damage variation are applied.
10. Critical hits double the calculated damage.
11. HP is reduced by the calculated damage.
12. The battle continues until one Pokémon reaches 0 HP.
13. The result is displayed.
14. Win/loss statistics are updated.
15. Pokémon HP is restored after the battle.

## Object-Oriented Programming

The project uses several object-oriented programming principles.

### Encapsulation

Fields are kept private and accessed through methods such as getters and setters.

```java
private String name;
private int maxHp;
private int currentHp;
```

This prevents direct access to the internal state of objects and allows validation and controlled modification.

### Inheritance

Different attack types inherit from the abstract `Attack` class.

```text
Attack
  │
  ├── DamageAttack
  ├── HealingAttack
  └── Other attack types
```

The base `Attack` class contains the common properties shared by all attacks, while subclasses provide their specific behaviour.

### Abstraction

`Attack` is an abstract class.

It defines the common structure of an attack and requires concrete subclasses to implement `execute()`:

```java
public abstract class Attack {

    public abstract void execute(
            Pokemon attacker,
            Pokemon defender,
            boolean criticalHit
    );
}
```

The base class does not need to know exactly how every attack behaves.

### Polymorphism

The battle system works with the general `Attack` type instead of depending on a specific attack implementation.

```java
Attack attack = ...;

attack.execute(
        attacker,
        defender,
        criticalHit
);
```

The actual implementation of `execute()` is determined at runtime based on the concrete object.

For example, a `DamageAttack` can implement:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
) {
    // Damage calculation
}
```

Another attack type could implement the same method differently:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
) {
    // Different behaviour
}
```

The battle system can therefore call:

```java
attack.execute(...);
```

without needing to know which concrete subclass is being used.

This allows new attack types to be added without changing the overall battle flow.

### Polymorphic Attack Structure

```text
                    Attack
                 abstract class
                      │
          ┌───────────┼───────────┐
          │           │           │
    DamageAttack  HealingAttack  ...
          │           │
      execute()   execute()
          │           │
    Deal damage   Restore HP
```

This separates the **battle flow** from the **specific behaviour of each attack**.

## Persistence

Pokémon and battle statistics are saved between sessions using **JSON** and **Jackson**.

The application can:

* Save Pokémon data
* Load Pokémon data
* Save battle statistics
* Load battle statistics
* Maintain statistics between application sessions

## Validation and Error Handling

The application validates user input and handles invalid operations using validation logic and exceptions.

Examples include:

* Invalid Pokémon names
* Invalid HP values
* Invalid attack values
* Invalid menu selections
* Invalid attack selections
* Invalid Pokémon types
* Attempts to perform unsupported operations

Custom exceptions are used where appropriate to keep error handling separate from the main application logic.

## Project Structure

The application is organized into separate packages based on responsibility.

```text
src/
└── main/
    └── java/
        └── org/
            └── java26/
                ├── consoleLayout/
                ├── exceptions/
                ├── handlers/
                ├── inputHelpers/
                ├── models/
                ├── repository/
                └── service/
```

The separation of responsibilities makes the application easier to maintain and extend.

## Refactoring

The project has been refactored from the previous **Pokédex CRUD application**.

The original Pokémon management functionality was kept while the project was extended with:

* A complete battle system
* Wild Pokémon
* Attack selection
* CPU-controlled opponents
* Type effectiveness
* Accuracy
* Random damage
* Critical hits
* Battle statistics
* Persistent JSON storage
* Abstract attack classes
* Inheritance
* Polymorphism

The refactoring also improves separation of responsibilities and encapsulation.

## Technologies

* **Java 21**
* **Maven**
* **Jackson**
* **JUnit 5**
* **IntelliJ IDEA**
* **Git / GitHub**

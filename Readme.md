# Pokémon Battler

A Java console application where the player can manage Pokémon and battle against computer-controlled opponents.

This project is an extension of my previous **Pokédex CRUD application**. The original functionality has been refactored and extended with a turn-based battle system.

## Features

### Pokédex

- Create Pokémon
- View Pokémon
- Update Pokémon
- Delete Pokémon
- Add and remove attacks
- Save and load Pokémon data
- Pokémon levels
- Input validation
- Error handling

### Battle System

- Choose a Pokémon to battle with
- Fight against a random wild Pokémon
- Turn-based battles
- Choose attacks
- CPU chooses attacks randomly
- Accuracy affects whether attacks hit
- Type effectiveness
- Random damage factor
- Critical hits
- Battle log
- Win/loss result
- Persistent win/loss statistics
- Pokémon level progression and evolution
- Pokémon evolution through the `Evolveable` interface

## Damage Formula

Damage is calculated using three factors:

```text
Damage = Base Damage × Type Effectiveness × Random Factor
```

The random factor introduces a small amount of variation to each attack.

```java
double randomFactor = power + random.nextDouble() * 0.15;
```

`random.nextDouble()` generates a value between `0.0` and `1.0`, resulting in a random factor between **power and 1.00**.

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
## Pokémon Levels and Evolution (Interface)

Pokémon have a persistent level that increases through battle progression.

- Each Pokémon starts with a level.
- A Pokémon evolves after every 3 wins.
- Evolution increases the Pokémon's level by 1.
- The Pokémon keeps the same base name while the level is displayed separately.
- For example, `Charizard Lv5` becomes `Charizard Lv6`.
- The level is saved together with the Pokémon data in JSON.

The level is kept separate from the Pokémon name so the application's name validation remains intact and battle statistics continue to use a stable Pokémon name.

```text
3 wins  →  Lv1 → Lv2
6 wins  →  Lv2 → Lv3
9 wins  →  Lv3 → Lv4
```
  ...
## Battle Flow
1. The player selects a Pokémon from their Pokédex.
2. The CPU randomly selects a Pokémon from the wild Pokémon pool.
3. The first player is selected randomly.
4. The player selects an attack from a menu.
5. The CPU selects an attack randomly.
6. Accuracy determines whether the attack hits.
7. A critical hit is randomly determined.
8. The attack is executed through the polymorphic execute() method.
9. Type effectiveness and random damage variation are applied.
10. Critical hits double the calculated damage.
11. HP is reduced by the calculated damage.
12. The battle continues until one Pokémon reaches 0 HP.
13. The result is displayed.
14. Win/loss statistics are updated.
15. Pokémon HP is restored after the battle.

## Object-Oriented Programming

The project uses several object-oriented programming principles, including encapsulation, inheritance, abstraction and polymorphism.

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
  └── HealAttack
```

The `Attack` class contains properties shared by all attacks, such as:

```text
name
baseDamage
accuracy
type
```

The subclasses inherit these properties and provide their own implementation of the attack behaviour.

`DamageAttack` is responsible for dealing damage to the opponent, while `HealAttack` restores HP to the attacking Pokémon.

### Abstraction

`Attack` is an abstract class.

It defines the common structure of an attack and requires every concrete subclass to implement the `execute()` method:

```java
public abstract void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
);
```

The base class does not need to know how each individual attack behaves.

The specific behaviour is implemented by the subclasses.

For example, `DamageAttack` deals damage:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
) {
    // Calculate and apply damage
    defender.takeDamage(...);
}
```

While `HealAttack` restores HP:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit
) {
    attacker.heal(heal);
}
```

### Polymorphism

The battle system works with the general `Attack` type instead of depending on a specific attack implementation.

For example:

```java
Attack selectedPlayerAttack = chooseAttack(scanner, myPokemon);
```

The same approach is used for both `RegularBattle` and `HandicapBattle`.

The selected attack is then executed through the common method:

```java
attack.execute(
        attacker,
        defender,
        criticalHit
);
```

The actual implementation that is executed is determined at runtime.

If the object is a `DamageAttack`:

```text
DamageAttack.execute()
        ↓
Calculate damage
        ↓
Damage defender
```

If the object is a `HealAttack`:

```text
HealAttack.execute()
        ↓
Restore attacker HP
```

The battle system therefore does not need to check the concrete attack type using `instanceof` or casts.

This makes it possible to add new attack types without changing the main battle flow.

### Polymorphic Attack Structure

```text
                    Attack
                 abstract class
                      │
             ┌────────┴────────┐
             │                 │
       DamageAttack         HealAttack
             │                 │
         execute()          execute()
             │                 │
       Deal damage         Restore HP
             │                 │
          Defender           Attacker
           loses HP          gains HP
```

This separates the **battle flow** from the **specific behaviour of each attack**.

The battle classes only need to know that they have an `Attack` and can call:

```java
attack.execute(...);
```

The concrete subclass determines what actually happens.

### Polymorphism and JSON

Jackson is configured to preserve the concrete attack type when Pokémon are saved and loaded.

The abstract `Attack` class uses `@JsonTypeInfo` and `@JsonSubTypes`:

```java
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "attackClassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = DamageAttack.class,
                name = "damage"
        ),
        @JsonSubTypes.Type(
                value = HealAttack.class,
                name = "heal"
        )
})
```

The JSON therefore contains the concrete attack type:

```json
{
  "attackClassType": "damage",
  "name": "sparo",
  "baseDamage": 100,
  "accuracy": 90,
  "type": "FIRE"
}
```

or:

```json
{
  "attackClassType": "heal",
  "name": "heal",
  "baseDamage": 10,
  "accuracy": 100,
  "type": "FIRE",
  "heal": 50
}
```

When the JSON is loaded, Jackson uses `attackClassType` to create the correct subclass.

This allows the application to store different attack types inside the same:

```java
List<Attack>
```

while preserving their individual behaviour.

### OOP Design

The attack system therefore combines the main OOP concepts:

```text
                    Attack
                abstract class
                       │
                 inheritance
                       │
          ┌────────────┴────────────┐
          │                         │
    DamageAttack                HealAttack
          │                         │
     polymorphism              polymorphism
          │                         │
    execute()                  execute()
          │                         │
    Deal damage               Restore HP
```

The result is a more extensible battle system where new attack types can be introduced by creating a new subclass of `Attack` and implementing its `execute()` method.


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

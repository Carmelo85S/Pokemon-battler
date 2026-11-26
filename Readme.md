# Pokémon Battler

A Java console application where the player can manage Pokémon and battle against computer-controlled opponents.

This project is an extension of my previous **Pokédex CRUD application**. The original functionality has been refactored and extended with a turn-based battle system, database persistence, battle statistics, and additional gameplay mechanics.

The application uses **PostgreSQL** to persist Pokémon and their attacks, **MongoDB** to store battle statistics and battle logs, and **Jackson** to load the initial Pokémon data from a JSON seed file.

---

## How to Run

### Requirements

- Java 25
- Maven
- PostgreSQL
- MongoDB
- IntelliJ IDEA (recommended)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/Carmelo85S/Pokemon-battler.git
   ```

2. Open the project in IntelliJ IDEA.

3. Make sure the Project SDK is set to Java 25.

4. Configure the PostgreSQL database and MongoDB connection.

5. Reload the Maven project to download dependencies.

6. Make sure the required database services are running.

7. Locate the application's main class and run its `main()` method.

### Running Tests

Run JUnit 5 tests directly from IntelliJ IDEA, or execute the following command if Maven is installed:

```bash
mvn test
```

---

## Features

### POKÉARENA

- Create Pokémon
- View Pokémon
- Update Pokémon
- Delete Pokémon
- Add and remove attacks
- Persist Pokémon and attacks in PostgreSQL
- Load initial Pokémon data from a JSON seed file
- Pokémon levels
- Input validation
- Error handling
- Reset Pokémon data to the initial seed state

### Battle System

- Regular 1v1 battles
- Handicap 1v1 battles
- Random wild Pokémon selection
- Player attack selection
- CPU-controlled opponents
- Damage and healing attacks
- Accuracy calculation
- Type effectiveness
- Random damage variation
- Critical hits
- Pokémon evolution
- Battle logs
- Persistent battle statistics

## VG Requirements (Väl Godkänt)

This project implements the advanced requirements for the VG grade.

### 1. Type Effectiveness

- Implements a type effectiveness system with damage multipliers of `2.0×`, `0.5×`, and `1.0×`.
- Supports Fire, Water, Grass, Electric, and Normal Pokémon types.
- Type effectiveness is applied during damage calculation.
- The effectiveness calculation is separated into dedicated methods in the battle logic.

### 2. Database Persistence

The application uses different persistence technologies depending on the type of data being stored.

**PostgreSQL and JPA/Hibernate**

- Stores Pokémon and their attacks in a relational database.
- Uses JPA annotations to map Java entities to database tables.
- Uses Hibernate as the JPA implementation.
- Supports CRUD operations through the repository layer.
- Uses entity relationships to associate attacks with their Pokémon.
- Uses cascading operations and orphan removal to manage associated attacks.
- Loads Pokémon and their attacks from the database.

**MongoDB**

- Stores battle statistics.
- Stores battle logs.
- Maintains battle-related information between application sessions.

**Jackson**

- Loads the initial Pokémon collection from `seed-pokemons.json`.
- Supports polymorphic deserialization of `DamageAttack` and `HealAttack`.
- Provides the initial data used when the database needs to be populated or reset.

### 3. Critical Hits

- Each attack has a `1/15` probability of being critical.
- Critical hits multiply the calculated damage by `2`.
- Critical hits are displayed in the battle log.

### 4. Persistent Battle Statistics

The application tracks battle-related information and maintains it between sessions using MongoDB.

Statistics include:

- Total wins and losses.
- Individual Pokémon wins and losses.
- Pokémon usage.
- Attack usage.
- Captured Pokémon.
- Last Pokémon usage.
- Battle history and logs.

The application provides a menu option to display battle statistics.

### 5. Object-Oriented Programming and Polymorphism

- Uses an abstract `Attack` class with concrete `DamageAttack` and `HealAttack` subclasses.
- Implements polymorphism through the overridden `execute()` method.
- Uses the `Evolveable` interface for Pokémon progression.
- Separates models, battle logic, services, input handling, and persistence into dedicated classes and packages.
- Uses repositories to separate database operations from application logic.
- Extends and refactors the original Pokédex application while preserving its CRUD functionality.

### Known Limitations

- **Single Pokémon battles:** Each battle involves one player Pokémon and one opponent Pokémon. Team-based battles are not supported.
- **Handicap mode:** The battle system supports two types of handicaps: HP adjustments and accuracy penalties. These can be applied to either the player or the opponent, allowing for easier or more challenging battles.

---

## Battle System

- Choose a Pokémon to battle with.
- Fight against a random wild Pokémon.
- Turn-based combat with speed-based starting order and random variation.
- Choose attacks from a menu.
- CPU-controlled opponents select attacks randomly.
- Accuracy affects whether attacks hit.
- Type effectiveness modifies damage.
- Random damage variation.
- Critical hits with double damage.
- Damage and healing attacks.
- Battle logs and win/loss results.
- Persistent battle statistics.
- Pokémon level progression and evolution through the `Evolveable` interface.
- Catch the opponent's Pokémon after winning.
- Restore Pokémon HP after battle.

## Turn Order

The starting Pokémon is determined by combining each Pokémon's Speed stat with an independent random value.

```java
int playerStart = myPokemon.getSpeed() + random.nextInt(50);
int opponentStart = opponent.getSpeed() + random.nextInt(50);

boolean playerTurn = playerStart > opponentStart;
```

Each Pokémon receives a random value between 0 and 49, which is added to its Speed stat.

A higher Speed stat increases the probability of starting first, but does not guarantee it. If both scores are equal, the opponent starts first.

After the initial turn, the Pokémon alternate turns until one faints or the battle is cancelled.

## Battle Flow

- The player selects a Pokémon from the Pokédex.
- The CPU randomly selects an opponent.
- Speed and random variation determine who attacks first.
- The player selects an attack, or the CPU chooses one randomly.
- Attack accuracy determines whether the attack hits.
- Critical hits are determined randomly.
- The attack executes through the polymorphic `execute()` method.
- Type effectiveness and random damage variation are applied.
- Critical hits double the calculated damage.
- HP is updated.
- The battle continues until one Pokémon faints or the player cancels.
- The result is displayed and battle statistics are updated.
- The winning Pokémon may evolve according to its progression rules.
- The opponent may be caught after a victory.
- Pokémon HP is restored after battle.
- Battle logs and statistics are persisted.

## Pokémon Speed

- Each Pokémon has a Speed stat that influences the starting order of a battle.
- Speed is stored as part of the Pokémon data in PostgreSQL.
- The starting order combines Speed with random variation, allowing a slower Pokémon to start first occasionally.
- This introduces variability while still giving faster Pokémon an advantage.

## Damage Formula

Damage is calculated using four factors: base damage, type effectiveness, random variation, and a critical hit multiplier.

```text
Damage = BaseDamage × Type Effectiveness × Random Factor
```

The random factor introduces a small amount of variation to each attack.

```java
double randomFactor = 0.85 + random.nextDouble() * 0.15;
```

`random.nextDouble()` generates a value between `0.0` and `1.0`, resulting in a random factor between **0.85 and 1.00**.

The final damage is rounded down to the nearest integer.

### Example

Final Damage:

```text
(int) (Base Damage × Type Effectiveness × Random Factor × Critical Multiplier)
```

```text
Base Damage         = 50
Type Effectiveness  = 2.0
Random Factor       = 0.90
Critical Hit        = Yes
Critical Multiplier = 2
```

```text
Damage = 50 × 2.0 × 0.90 × 2 = 180
```

If the attack is not critical, the multiplier is 1:

```text
Damage = 50 × 2.0 × 0.90 × 1 = 90
```

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

## Critical Hits

During battle, every attack has a **1 in 15 chance** of being a critical hit.

The critical hit is determined randomly:

```java
boolean criticalHit = random.nextInt(15) == 0;
```

`random.nextInt(15)` generates a value between `0` and `14`. If the result is `0`, the attack is critical.

- **1/15 chance** → Critical Hit (~6.67%)
- **14/15 chance** → Normal Hit
- A critical hit deals **double damage**.

The critical-hit result is passed to the attack through `execute()`:

```java
attack.execute(attacker, defender, criticalHit, effectiveness, random);
```

The concrete attack implementation then applies the critical-hit multiplier together with the other damage modifiers:

```java
@Override
public void execute(
        Pokemon attacker,
        Pokemon defender,
        boolean criticalHit,
        double effectiveness,
        Random random
) {
    double randomFactor = 0.85 + random.nextDouble() * 0.15;
    double damage = getBaseDamage() * randomFactor * effectiveness;

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
- Pokémon levels are persisted together with the Pokémon data in PostgreSQL.

The level is kept separate from the Pokémon name so the application's name validation remains intact and battle statistics continue to use a stable Pokémon name.

```text
3 wins  →  Lv1 → Lv2
6 wins  →  Lv2 → Lv3
9 wins  →  Lv3 → Lv4
```

---

## Object-Oriented Programming

The project uses several object-oriented programming principles, including encapsulation, inheritance, abstraction, and polymorphism.

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
        boolean criticalHit,
        double effectiveness,
        Random random
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
        boolean criticalHit,
        double effectiveness,
        Random random
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
        boolean criticalHit,
        double effectiveness,
        Random random
) {
    attacker.heal(heal);
}
```

### Polymorphism

Both `DamageAttack` and `HealAttack` override the same `execute()` method.

`DamageAttack` calculates and applies damage using type effectiveness, random variation, and critical hits, while `HealAttack` restores the attacker's HP.

This demonstrates runtime polymorphism through a shared abstract `Attack` type.

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
        criticalHit,
        effectiveness,
        random
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

Jackson is configured to preserve the concrete attack type when Pokémon are loaded from the JSON seed file.

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

Or:

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

When the seed JSON is loaded, Jackson uses `attackClassType` to create the correct subclass.

This allows the application to deserialize different attack types into the same:

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

---

## Persistence

The application uses different persistence technologies for different types of data.

### PostgreSQL

PostgreSQL is the primary relational database for Pokémon and attacks.

- Stores Pokémon entities.
- Stores attack entities.
- Maintains the relationship between Pokémon and their attacks.
- Persists Pokémon attributes such as HP, Speed, level, and type.
- Supports creating, reading, updating, and deleting Pokémon.
- Uses JPA and Hibernate for object-relational mapping.

### JPA and Hibernate

The application uses JPA annotations to map Java classes to relational database tables.

Examples of the mapping include:

- `@Entity` to mark persistent classes.
- `@Id` to define entity identifiers.
- `@GeneratedValue` to generate primary keys.
- `@OneToMany` to define the relationship between Pokémon and attacks.
- `@ManyToOne` to associate an attack with its Pokémon.
- `@Enumerated(EnumType.STRING)` to persist Pokémon types as strings.

Hibernate handles the persistence operations through the JPA API.

The repository layer separates database access from the application's business logic.

### MongoDB

MongoDB is used for battle-related data.

- Stores battle statistics.
- Stores battle logs.
- Maintains information between application sessions.

Using a document database for battle records allows battle-related information to be stored independently from the relational Pokémon data.

### JSON and Jackson

Jackson is used to deserialize the initial Pokémon data from `seed-pokemons.json`.

The seed file provides the initial collection of Pokémon and their attacks.

JSON is not the primary persistence mechanism for Pokémon. Once loaded, Pokémon data is managed through PostgreSQL.

The reset functionality restores the Pokémon collection from the seed file and resets battle statistics.

### Persistence Responsibilities

| Technology | Responsibility |
|---|---|
| PostgreSQL | Pokémon and attacks |
| JPA | Object-relational mapping |
| Hibernate | JPA implementation |
| MongoDB | Battle statistics and battle logs |
| Jackson | Loading initial Pokémon data from JSON |

---

## Validation and Error Handling

The application validates user input and handles invalid operations using validation logic and exceptions.

Examples include:

- Invalid Pokémon names.
- Invalid HP values.
- Invalid attack values.
- Invalid menu selections.
- Invalid attack selections.
- Invalid Pokémon types.
- Attempts to perform unsupported operations.
- Errors while loading the initial Pokémon data.
- Database persistence errors.

Custom exceptions are used where appropriate to keep error handling separate from the main application logic.

Examples include `InvalidPokemonException` and `PokemonLoadException`.

---

## Project Structure

The application is organized into separate packages based on responsibility.

```text
battler/
   └──src/
      └── main/
          ├── java/
          │   └── org/
          │       └── java26/
          │           ├── UI/
          │           ├── exceptions/
          │           ├── handlers/
          │           ├── models/
          │           ├── repository/
          │           └── service/
          |           └──Main.class
          └── resources/
              ├── META-INF/
                       └── persistence.xml
└── seed-pokemons.json
└── wild-pokemons.json
└── .env
└── docker-compose.yaml
```

The main responsibilities are separated as follows:

- **UI:** Console menus and presentation.
- **exceptions:** Custom application exceptions.
- **handlers:** Input handling and validation.
- **models:** Pokémon, attacks, enums, and other domain objects.
- **repository:** Data access and persistence operations.
- **service:** Application and battle logic.
- **resources:** Configuration and initial seed data.

The separation of responsibilities makes the application easier to maintain and extend.

---

## Refactoring

The project has been refactored from the previous **Pokédex CRUD application**.

The original Pokémon management functionality was kept while the project was extended with:

- A complete battle system.
- Wild Pokémon.
- Attack selection.
- CPU-controlled opponents.
- Type effectiveness.
- Accuracy.
- Random damage.
- Critical hits.
- Healing attacks.
- Battle statistics.
- Battle logs.
- PostgreSQL persistence.
- JPA and Hibernate.
- MongoDB persistence.
- JSON seed loading.
- Abstract attack classes.
- Inheritance.
- Polymorphism.

The refactoring also improves separation of responsibilities, encapsulation, and persistence architecture.

The application now separates relational Pokémon data from document-based battle information instead of relying on JSON files as the main storage mechanism.

---

## Testing (JUnit 5)

The project uses **JUnit 5** to test battle rules, input validation, and JSON error handling.

Tests are organized by the component being tested and run independently of the interactive console application.

### BattleLogicTest

- **Type effectiveness:** Parameterized tests verify damage multipliers (`2.0`, `0.5`, and `1.0`) for different Pokémon type combinations.
- **Evolution:** Parameterized tests verify that evolution is triggered at every third win and not at other win counts.
- **Zero HP:** A battle must not start when either Pokémon has `0 HP`.
- **No attacks:** A battle must not start if a Pokémon has no available attacks.
- **Pokémon without playable Pokémon:** Verifies that a Pokédex containing no playable Pokémon cannot start a battle and that Pokémon selection returns `null`.
- **Cancel during battle:** Verifies that entering `B` during attack selection safely cancels the battle, returns `false`, and leaves both Pokémon's HP unchanged.

### InputHelperTest

Input-validation tests check that invalid values are rejected using `InvalidPokemonException`.

Examples include:

- Blank input.
- Nonnumeric input.
- Invalid Pokémon names.
- Values outside permitted ranges.

Invalid attack choices such as `-1`, `99`, and `abc` can be tested against the attack menu's allowed range.

### JsonHandlerTest

JSON-loading tests cover error cases and polymorphic deserialization:

- **Corrupted JSON:** A file containing malformed JSON causes `loadPokemon()` to throw `PokemonLoadException`.
- **Missing JSON:** Attempting to load a nonexistent file also causes `PokemonLoadException`.
- **Polymorphic deserialization:** Verifies that Jackson correctly identifies and creates the appropriate concrete attack subclass.

JUnit's `@TempDir` creates a temporary directory for each test, so test files do not overwrite real files such as `seed-pokemons.json`.

`Path.resolve()` builds a file path, while `Files.writeString()` creates a file containing the test data.

The `isValidJSON()` method can be tested directly with JSON strings without creating temporary files.

---

## Author

**Carmelo Salis**

Java Developer in Training

GitHub: [Carmelo85S](https://github.com/Carmelo85S)
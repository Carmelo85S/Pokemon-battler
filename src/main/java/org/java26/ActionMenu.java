package org.java26;

public class ActionMenu {
    public static void showAllPokemon(Pokedex pokedex) {
        for (Pokemon p : pokedex.getPokemons()) {
            System.out.println(
                    "Name: " + p.name + " type: " + p.type + " max Hp: " + p.maxHp + " current Hp: " + p.currentHp);
            for(Attack attack : p.attacks){
                System.out.println("Attacks: "+attack.name);
            }
        }
    }


    public static void insertNewPokemon() {
        System.out.println("Insert new Pokemon");
    }

    public static void customizePokemon() {
        System.out.println("Customize your pokemon");
    }

    public static void deletePokemon() {
        System.out.println("Are you sure you want to delete your pokemon?");
    }

    public static void saveToFile() {
        System.out.println("Saving...");
    }

    public static void loadFromFile() {
        System.out.println("Loading...");
    }

    static void resetToSeedData(Pokedex pokemon) {
        Attack thunderbolt = new Attack("Thunderbolt", 50, 80, PokemonType.ELECTRIC);
        Attack quickAttack = new Attack("Quick Attack", 40, 90, PokemonType.NORMAL);

        Pokemon pikachu = new Pokemon("Pikachu", PokemonType.ELECTRIC, 100);
        pikachu.addAttack(thunderbolt);
        pikachu.addAttack(quickAttack);


        Attack ember = new Attack("Ember", 40, 90, PokemonType.FIRE);
        Attack scratch = new Attack("Scratch", 35, 95, PokemonType.NORMAL);

        Pokemon charmander = new Pokemon("Charmander", PokemonType.FIRE, 100);
        charmander.addAttack(ember);
        charmander.addAttack(scratch);


        Attack waterGun = new Attack("Water Gun", 40, 95, PokemonType.WATER);
        Attack tackle = new Attack("Tackle", 35, 95, PokemonType.NORMAL);

        Pokemon squirtle = new Pokemon("Squirtle", PokemonType.WATER, 100);
        squirtle.addAttack(waterGun);
        squirtle.addAttack(tackle);


        Attack vineWhip = new Attack("Vine Whip", 45, 90, PokemonType.GRASS);
        Attack headbutt = new Attack("Headbutt", 50, 85, PokemonType.NORMAL);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", PokemonType.GRASS, 100);
        bulbasaur.addAttack(vineWhip);
        bulbasaur.addAttack(headbutt);


        Attack flamethrower = new Attack("Flamethrower", 70, 85, PokemonType.FIRE);
        Attack wingAttack = new Attack("Wing Attack", 60, 90, PokemonType.NORMAL);

        Pokemon charizard = new Pokemon("Charizard", PokemonType.FIRE, 100);
        charizard.addAttack(flamethrower);
        charizard.addAttack(wingAttack);


        Attack thunderShock = new Attack("Thunder Shock", 40, 95, PokemonType.ELECTRIC);
        Attack spark = new Attack("Spark", 50, 90, PokemonType.ELECTRIC);

        Pokemon raichu = new Pokemon("Raichu", PokemonType.ELECTRIC, 100);
        raichu.addAttack(thunderShock);
        raichu.addAttack(spark);

        Pokemon[] Pokemon = {
                pikachu,
                charmander,
                squirtle,
                bulbasaur,
                charizard,
                raichu
        };

        for (Pokemon p : Pokemon) {
            pokemon.addPokemon(p);
        }
    }
}


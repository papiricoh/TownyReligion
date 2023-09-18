package org.papiricoh.townyreligion.object;

import com.palmergames.bukkit.towny.object.Town;
import org.papiricoh.townyreligion.object.god.God;

import java.util.ArrayList;
import java.util.Date;

public class Religion {
    private String name;
    private Town founding_town;
    private Date time_established;
    private ArrayList<Town> towns;
    private ArrayList<Religion> allies;
    private ArrayList<Religion> enemies;
    private God main_god;


    public Religion(String name, Town foundingTown, Date timeEstablished, ArrayList<Town> towns, ArrayList<Religion> allies, ArrayList<Religion> enemies, God mainGod) {
        this.name = name;
        this.founding_town = foundingTown;
        this.time_established = timeEstablished;
        this.towns = towns;
        this.allies = allies;
        this.enemies = enemies;
        this.main_god = mainGod;
    }

    public Religion(String name, Town founding_town, God god) {
        this.name = name;
        this.founding_town = founding_town;
        this.time_established = new Date();
        this.towns = new ArrayList<>();
        this.allies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.main_god = god;
    }

    public boolean containsTown(Town town) {
        return this.towns.contains(town) || this.founding_town.equals(town);
    }

    public boolean addTown(Town town) {
        if(!containsTown(town)) {
            this.towns.add(town);
            return true;
        }
        return false;
    }

    public void removeTown(Town town) {
        this.towns.remove(town);
    }
}
package org.papiricoh.townyreligion.object.god;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

public class God {
    private String name;
    private String lore;
    private final PotionEffectType potion_effect_type;
    private Material material;

    public God(String name, PotionEffectType potion_effect_type) {
        this.name = name;
        this.potion_effect_type = potion_effect_type;
    }

    public God(String name, String lore, PotionEffectType potion_effect_type, Material material) {
        this.name = name;
        this.lore = lore;
        this.potion_effect_type = potion_effect_type;
        this.material = material;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public String getLore() {
        return lore;
    }

    public PotionEffectType getPotion_effect_type() {
        return potion_effect_type;
    }

    public Material getMaterial() {
        return this.material;
    }
}
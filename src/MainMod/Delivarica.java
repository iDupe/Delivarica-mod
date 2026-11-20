package MainMod;

import mindustry.mod.Mod;
import mindustry.content.Blocks;
import mindustry.type.Planet;
import mindustry.world.Block;
import MainMod.Planet.Delivia;
import MainMod.Planet.DeliviaTor;
import MainMod.Planet.Gertat;

public class Delivarica extends Mod {
    
    public static Delivia delivia;          
    public static Gertat gertat;            

    @Override
    public void init() {
    }

    @Override
    public void loadContent() {

        DelivaricaStatusEffects.load();
        DelivaricaItems.load();
        DelivaricaUnits.load();
        DelivaricaBlocks.load();
        
        gertat = new Gertat();
        delivia = new Delivia();

        Block[] tints = {
            DelivaricaBlocks.redSand,   
            Blocks.sand,                
            DelivaricaBlocks.sandrock   
        };

        for (int i = 0; i < 20; i++) {
            Block currentTint = tints[i % tints.length];

            Planet asteroid = DeliviaTor.createAsteroid(
                "delivia-asteroid-" + i, 
                delivia,                 
                DelivaricaBlocks.obsidian,  
                currentTint,                
                1000 + i,                
                0.4f,                    
                1 + (i % 3),             
                0.4f                     
            );

            asteroid.orbitRadius = 1.85f; 
            asteroid.orbitTime = 60f;       
            asteroid.orbitOffset = i * (360f / 20);    
        }
    }
}

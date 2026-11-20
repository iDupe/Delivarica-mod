package MainMod;

import MainMod.Planet.Delivia;
import MainMod.Planet.DeliviaTor;
import MainMod.Planet.Gertat;
import mindustry.content.Blocks;
import mindustry.mod.Mod;
import mindustry.type.Planet;
import mindustry.world.Block;

public class Delivarica extends Mod {

    // Основные планеты мода | Main mod planets
    public static Delivia delivia;
    public static Gertat gertat;

    @Override
    public void init() {
    }

    @Override
    public void loadContent() {
        // Загрузка контента (по порядку зависимостей) | Load content (ordered by dependencies)
        DelivaricaStatusEffects.load();
        DelivaricaItems.load();
        DelivaricaUnits.load();
        DelivaricaBlocks.load();

        // Инициализация планет | Initialize planets
        gertat = new Gertat();
        delivia = new Delivia();

        // Палитра поверхностных блоков для астероидов | Surface block palette for asteroids
        Block[] tints = {
            DelivaricaBlocks.redSand,
            Blocks.sand,
            DelivaricaBlocks.sandrock
        };

        // Генерация пояса из 20 астероидов вокруг Delivia | Generate a belt of 20 asteroids around Delivia
        int asteroidCount = 20;
        for (int i = 0; i < asteroidCount; i++) {
            Block currentTint = tints[i % tints.length];

            Planet asteroid = DeliviaTor.createAsteroid(
                "delivia-asteroid-" + i,      // Уникальный ID | Unique ID
                delivia,                      // Родительская планета | Parent planet
                DelivaricaBlocks.obsidian,    // Базовый блок | Base block
                currentTint,                  // Блок поверхности | Surface tint block
                1000 + i,                     // Сид генерации | Generation seed
                0.4f,                         // Порог шума | Noise threshold
                1 + (i % 3),                  // Октавы шума | Noise octaves
                0.4f                          // Затухание шума | Noise persistence
            );

            // Настройка параметров орбиты | Configure orbit parameters
            asteroid.orbitRadius = 1.85f;
            asteroid.orbitTime = 60f;
            asteroid.orbitOffset = i * (360f / asteroidCount);
        }
    }
}

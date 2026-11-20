package MainMod.Planet;

import MainMod.Delivarica;
import MainMod.DelivaricaBlocks;
import arc.graphics.Color;
import mindustry.graphics.g3d.HexMesh;
import mindustry.graphics.g3d.HexSkyMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.type.Planet;
import mindustry.type.SectorPreset;

public class Delivia extends Planet {

    // Стартовый сектор | Starting sector
    public static SectorPreset impact0039;

    public Delivia() {
        // Основные параметры планеты | Core planet parameters
        super("delivia", Delivarica.gertat, 1f, 2);
        this.localizedName = "Delivia";
        this.iconColor = DelivaricaBlocks.sandrock.mapColor;

        this.accessible = true;
        this.alwaysUnlocked = true;
        this.visible = true;
        this.orbitSpacing = 6f;
        this.generator = new DeliviaGenerator();
        this.meshLoader = () -> new HexMesh(this, 6);

        // Атмосфера | Atmosphere
        this.atmosphereColor = Color.valueOf("694035").a(0.25f);
        this.atmosphereRadIn = 0.00f;
        this.atmosphereRadOut = 0.15f;
        this.hasAtmosphere = true;
        this.bloom = false;

        // Слой облаков | Cloud layer
        this.cloudMeshLoader = () -> new MultiMesh(
            new HexSkyMesh(
                this,
                51,                                 // Сид | Seed
                0.26f,                              // Скорость вращения | Rotation speed
                0.17f,                              // Высота над поверхностью | Radius offset
                4,                                  // Детализация | Divisions
                Color.valueOf("8f8f8f").a(0.45f),   // Цвет облаков | Cloud color
                2,                                  // Октавы шума | Noise octaves
                0.45f,                              // Затухание шума | Noise persistence
                1f,                                 // Масштаб шума | Noise scale
                0.45f                               // Порог облачности | Cloud threshold
            )
        );

        // Орбитальные параметры | Orbital parameters
        this.rotateTime = 60f * 8f;
        this.orbitTime = 60f * 60f * 4f;

        // Правила высадки и секторов | Landing and sector rules
        this.defaultCore = DelivaricaBlocks.coreReset;
        this.startSector = 10;

        this.allowSectorInvasion = true;
        this.allowLaunchLoadout = false;
        this.clearSectorOnLose = true;
        this.allowLaunchToNumbered = true;

        // Пресет сектора высадки | Landing sector preset
        impact0039 = new SectorPreset("impact-0039", this, 10) {{
            this.localizedName = "[white]The [coral]Impact [accent]<[white]0039[accent]>";
            this.difficulty = 4;
            this.alwaysUnlocked = true;
        }};
    }
}
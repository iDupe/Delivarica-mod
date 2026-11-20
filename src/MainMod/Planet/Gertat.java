package MainMod.Planet;

import arc.graphics.Color;
import mindustry.graphics.g3d.SunMesh;
import mindustry.type.Planet;

public class Gertat extends Planet {

    public Gertat() {
        // Центральная звезда системы | Central system star
        super("gertat", null, 3.5f);

        this.accessible = false;
        this.visible = true;
        this.hasAtmosphere = false;
        this.bloom = true;
        this.drawOrbit = false;
        this.localizedName = "Gertat System";

        // Палитра свечения звезды | Star glow color palette
        this.meshLoader = () -> new SunMesh(
            this,
            4, 5, 0.3f, 1.7f, 1.2f, 1,
            1.1f,
            Color.valueOf("e0f0ff"),
            Color.valueOf("ffffff"),
            Color.valueOf("c0d8ff"),
            Color.valueOf("ffffff"),
            Color.valueOf("f0f8ff"),
            Color.valueOf("ffffff")
        );
    }
}
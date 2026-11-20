package MainMod.Planet;

import arc.graphics.Color;
import arc.math.Rand;
import arc.math.geom.Mat3D;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import mindustry.graphics.g3d.GenericMesh;
import mindustry.graphics.g3d.MatMesh;
import mindustry.graphics.g3d.MultiMesh;
import mindustry.graphics.g3d.NoiseMesh;
import mindustry.maps.planet.AsteroidGenerator;
import mindustry.type.Planet;
import mindustry.world.Block;
import mindustry.world.meta.Env;

public class DeliviaTor {

    // Фабрика процедурных астероидов | Procedural asteroid factory
    public static Planet createAsteroid(
        String name,
        Planet parent,
        Block base,
        Block tint,
        int seed,
        float tintThresh,
        int pieces,
        float scale
    ) {
        return new Planet(name, parent, 0.04f) {{
            updateLighting = false;
            drawOrbit = true;
            accessible = false;
            clipRadius = 2f;
            defaultEnv = Env.space;
            icon = "commandRally";
            hasAtmosphere = false;
            bloom = true;

            generator = new AsteroidGenerator();

            meshLoader = () -> {
                iconColor = tint.mapColor;
                Color tinted = tint.mapColor.cpy().a(1f - tint.mapColor.a);
                Seq<GenericMesh> meshes = new Seq<>();
                Color color = base.mapColor;
                Rand rand = new Rand(id + 2);

                // Основное тело астероида | Main asteroid body
                meshes.add(new NoiseMesh(
                    this, seed, 2, radius, 2, 0.55f, 0.45f, 14f,
                    color, tinted, 3, 0.6f, 0.38f, tintThresh
                ));

                // Окружающие осколки | Surrounding debris pieces
                for (int j = 0; j < pieces; j++) {
                    Vec3 offset = new Vec3()
                        .setToRandomDirection(rand)
                        .setLength(rand.random(0.44f, 1.4f) * scale);

                    meshes.add(new MatMesh(
                        new NoiseMesh(
                            this, seed + j + 1, 1, 0.022f + rand.random(0.039f) * scale,
                            2, 0.6f, 0.38f, 20f, color, tinted, 3, 0.6f, 0.38f, tintThresh
                        ),
                        new Mat3D().setToTranslation(offset)
                    ));
                }

                return new MultiMesh(meshes.toArray(GenericMesh.class));
            };
        }};
    }
}
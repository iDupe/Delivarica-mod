package MainMod.Planet;

import MainMod.DelivaricaBlocks;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.util.noise.Simplex;
import mindustry.content.Blocks;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.type.Sector;
import mindustry.world.Block;
import mindustry.world.Tiles;

public class DeliviaGenerator extends PlanetGenerator {

    // Параметры генерации высот | Height generation parameters
    float heightYOffset = 42.7f;
    float scl = 5f;
    float waterOffset = 0.04f;
    float heightScl = 1.01f;

    // Таблица биомов по широте и высоте | Biome lookup table by latitude and height
    Block[][] arr = {
        {DelivaricaBlocks.sandrock, Blocks.stone, Blocks.basalt, DelivaricaBlocks.obsidian},
        {DelivaricaBlocks.redSand, DelivaricaBlocks.redSand, DelivaricaBlocks.sandrock, DelivaricaBlocks.sandrock},
        {Blocks.ice, Blocks.snow, Blocks.iceSnow, Blocks.snow}
    };

    float rawHeight(Vec3 position) {
        return (Mathf.pow(Simplex.noise3d(
            seed, 7, 0.5f, 1f / 3f,
            position.x * scl, position.y * scl + heightYOffset, position.z * scl
        ) * heightScl, 2.3f) + waterOffset) / (1f + waterOffset);
    }

    @Override
    public float getHeight(Vec3 position) {
        float base = rawHeight(position);
        float latitude = Math.abs(position.y);

        // Усиление гор у экватора и полюсов | Mountain boost near equator and poles
        float eqFactor = Mathf.clamp(1f - (latitude * 2.25f));
        float poleFactor = Mathf.clamp((latitude - 0.85f) * 6.55f);

        float mountainMultiplier = Math.max(eqFactor * 0.85f, poleFactor * 0.7f);
        float height = base * (0.5f + mountainMultiplier);

        return Mathf.clamp(height, 0f, 0.9f);
    }

    Block getBlock(Vec3 position) {
        float height = getHeight(position);
        float latitude = Math.abs(position.y);

        // Температурный градиент с шумом | Temperature gradient with noise
        float tnoise = Simplex.noise3d(seed + 2, 4, 0.5f, 3f, position.x, position.y, position.z) * 0.32f;
        float temp = latitude + tnoise;

        int row;
        if (temp > 1.03f) {
            row = 2;
        } else if (temp < 0.32f) {
            row = 0;
        } else {
            // Генерация полос и пятен в умеренной зоне | Generate bands and patches in temperate zone
            float plainsNoise = Simplex.noise3d(seed + 10, 3, 0.5f, 1.5f, position.x, position.y, position.z);
            float tentacleNoise = Simplex.noise3d(seed + 20, 3, 0.5f, 4f, position.x, position.y * 0.15f, position.z);

            float eqTentacleFade = Mathf.clamp((0.45f - latitude) / 0.2f);
            float eqThreshold = 0.8f - (eqTentacleFade * 0.4f);

            if (tentacleNoise > eqThreshold) return Blocks.sand;

            float poleTentacleFade = Mathf.clamp((latitude - 0.55f) / 0.2f);
            float poleThreshold = 0.8f - (poleTentacleFade * 0.4f);

            if (latitude > 0.5f && tentacleNoise > poleThreshold) return DelivaricaBlocks.rindit;

            if (plainsNoise > 0.6f) return Blocks.slag;
            else if (plainsNoise < -0.45f) return Blocks.darksand;

            return DelivaricaBlocks.redSand;
        }

        int col = Mathf.clamp((int)(height * arr[0].length), 0, arr[0].length - 1);
        return arr[row][col];
    }

    @Override
    public void getColor(Vec3 position, Color out) {
        Block block = getBlock(position);
        if (block != null) {
            out.set(block.mapColor).a(1f - block.albedo);

            // Затемнение экваториального песчаника и вариативность песка | Darken equatorial sandrock and vary red sand
            if (block == DelivaricaBlocks.sandrock && Math.abs(position.y) < 0.32f) out.mul(0.85f);
            if (block == DelivaricaBlocks.redSand) {
                float patchNoise = Simplex.noise3d(seed + 7, 3, 0.5f, 2f, position.x, position.y, position.z);
                out.mul(0.8f + (patchNoise + 1f) * 0.2f);
            }

            // Мелкий шум текстуры | Fine texture noise
            float textureNoise = Simplex.noise3d(seed + 5, 5, 0.5f, 12f, position.x, position.y, position.z);
            out.mul(0.95f + textureNoise * 0.05f);
        } else {
            out.set(Color.gray);
        }
    }

    @Override
    protected void generate() {
        // Генерация тайлов карты сектора | Generate sector map tiles
        for (int x = 0; x < tiles.width; x++) {
            for (int y = 0; y < tiles.height; y++) {
                float distFromCenter = Mathf.dst(x, y, tiles.width / 2f, tiles.height / 2f);
                float latitude = distFromCenter / (tiles.width / 2f);
                float noise = Simplex.noise2d(seed, 5, 0.5f, 1.5f, x, y);

                if (latitude > 0.7f) {
                    tiles.getn(x, y).setFloor(Blocks.ice.asFloor());
                } else if (latitude < 0.3f) {
                    if (noise > 0.57f) tiles.getn(x, y).setFloor(DelivaricaBlocks.obsidian);
                    else if (noise > 0.45f) tiles.getn(x, y).setFloor(Blocks.stone.asFloor());
                    else if (noise > 0.3f) tiles.getn(x, y).setFloor(Blocks.basalt.asFloor());
                    else tiles.getn(x, y).setFloor(DelivaricaBlocks.sandrock);
                } else {
                    float tentacleNoise = Simplex.noise2d(seed + 20, 3, 0.5f, 5f, x, y * 0.15f);
                    float eqTentacleFade = Mathf.clamp((0.45f - latitude) / 0.2f);
                    float eqThreshold = 0.5f - (eqTentacleFade * 0.4f);
                    float poleTentacleFade = Mathf.clamp((latitude - 0.55f) / 0.2f);
                    float poleThreshold = 0.5f - (poleTentacleFade * 0.4f);

                    if (tentacleNoise > eqThreshold) tiles.getn(x, y).setFloor(Blocks.sand.asFloor());
                    else if (latitude > 0.5f && tentacleNoise > poleThreshold) tiles.getn(x, y).setFloor(DelivaricaBlocks.rindit);
                    else if (noise > 0.6f) tiles.getn(x, y).setFloor(Blocks.slag.asFloor());
                    else if (noise < -0.45f) tiles.getn(x, y).setFloor(Blocks.darksand.asFloor());
                    else tiles.getn(x, y).setFloor(DelivaricaBlocks.redSand);
                }
            }
        }
    }

    @Override
    public void postGenerate(Tiles tiles) {
    }

    @Override
    public boolean allowLanding(Sector sector) {
        return true;
    }
}
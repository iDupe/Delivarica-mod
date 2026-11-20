package MainMod.Blocks;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Point2;
import mindustry.core.Renderer;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.blocks.power.BeamNode;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;

import static mindustry.Vars.player;
import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class xNode extends BeamNode {

    // Диагональные направления соединений | Diagonal connection directions
    public static final Point2[] diagDirs = {
        new Point2(1, 1),   // Вправо-вверх | Top-right
        new Point2(-1, 1),  // Влево-вверх | Top-left
        new Point2(-1, -1), // Влево-вниз | Bottom-left
        new Point2(1, -1)   // Вправо-вниз | Bottom-right
    };

    public xNode(String name) {
        super(name);
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        // Отрисовка превью диагональных лучей при размещении | Draw diagonal beam preview on placement
        for (int i = 0; i < 4; i++) {
            int maxLen = range + size / 2;
            Building dest = null;
            var dir = diagDirs[i];
            int offset = size / 2;

            for (int j = 1 + offset; j <= range + offset; j++) {
                var other = world.build(x + j * dir.x, y + j * dir.y);
                if (other != null && other.isInsulated()) break;

                if (other != null
                    && other.block.hasPower
                    && other.team == player.team()
                    && !(other.block instanceof PowerNode)) {
                    maxLen = j;
                    dest = other;
                    break;
                }
            }

            Drawf.dashLine(
                Pal.placing,
                x * tilesize + dir.x * (tilesize * size / 2f + 2),
                y * tilesize + dir.y * (tilesize * size / 2f + 2),
                x * tilesize + dir.x * maxLen * tilesize,
                y * tilesize + dir.y * maxLen * tilesize
            );

            if (dest != null) {
                Drawf.square(dest.x, dest.y, dest.block.size * tilesize / 2f + 2.5f, 0f);
            }
        }
    }

    public class XNodeBuild extends BeamNodeBuild {

        @Override
        public void updateDirections() {
            // Поиск и обновление диагональных связей энергосети | Find and update diagonal power grid links
            for (int i = 0; i < 4; i++) {
                var prev = links[i];
                var dir = diagDirs[i];
                links[i] = null;
                dests[i] = null;
                int offset = size / 2;

                for (int j = 1 + offset; j <= range + offset; j++) {
                    var other = world.build(tile.x + j * dir.x, tile.y + j * dir.y);

                    if (other != null && other.isInsulated()) break;

                    if (other != null
                        && other.block.hasPower
                        && (other.block.connectedPower || other.block instanceof xNode)
                        && other.team == team
                        && !(other.block instanceof PowerNode)) {
                        links[i] = other;
                        dests[i] = world.tile(tile.x + j * dir.x, tile.y + j * dir.y);
                        break;
                    }
                }

                var next = links[i];

                // Пересчёт графа энергосети при изменении связи | Recalculate power graph on link change
                if (next != prev) {
                    if (prev != null && prev.isAdded()) {
                        prev.power.links.removeValue(pos());
                        power.links.removeValue(prev.pos());

                        PowerGraph newgraph = new PowerGraph();
                        newgraph.reflow(this);

                        if (prev.power.graph != newgraph) {
                            PowerGraph og = new PowerGraph();
                            og.reflow(prev);
                        }
                    }

                    if (next != null) {
                        power.links.addUnique(next.pos());
                        next.power.links.addUnique(pos());
                        power.graph.addGraph(next.power.graph);
                    }
                }
            }
        }

        @Override
        public void draw() {
            Draw.z(Layer.block);
            Draw.rect(region, x, y);

            if (Mathf.zero(Renderer.laserOpacity) || team == Team.derelict) return;

            // Отрисовка энергетических лучей | Draw power beams
            Draw.z(Layer.power);
            Draw.color(laserColor1, laserColor2, (1f - power.graph.getSatisfaction()) * 0.86f + Mathf.absin(3f, 0.1f));
            Draw.alpha(Renderer.laserOpacity);
            float w = laserWidth + Mathf.absin(pulseScl, pulseMag);

            for (int i = 0; i < 4; i++) {
                if (dests[i] != null && links[i].wasVisible && (!(links[i].block instanceof xNode node)
                    || (links[i].tileX() != tileX() && links[i].tileY() != tileY())
                    || (links[i].id > id && range >= node.range)
                    || range > node.range)) {

                    var dir = diagDirs[i];
                    int dst = Math.max(Math.abs(dests[i].x - tile.x), Math.abs(dests[i].y - tile.y));

                    if (dst > 1 + size / 2) {
                        float poff = tilesize / 2f;
                        // Координаты от углов блоков | Coordinates from block corners
                        float x1 = x + poff * size * dir.x;
                        float y1 = y + poff * size * dir.y;
                        float x2 = dests[i].worldx() - poff * dir.x;
                        float y2 = dests[i].worldy() - poff * dir.y;

                        Drawf.laser(laser, laserEnd, x1, y1, x2, y2, w);
                    }
                }
            }
            Draw.reset();
        }
    }
}
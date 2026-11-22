package MainMod.Units;

import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.EntityGroup;
import mindustry.entities.Units;
import mindustry.gen.Building; //Set
import mindustry.gen.LegsUnit;
import mindustry.gen.Payloadc;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;

public class Dent extends LegsUnit implements Payloadc {
    public static int classId = -1;
    public Seq<Payload> payloads = new Seq<>();
    private float payloadUsed;

    @Override
    public void setType(mindustry.type.UnitType type) {
        super.setType(type);
        resetLegs();
    }

    @Override
    public Seq<Payload> payloads() {
        return payloads;
    }

    @Override
    public void payloads(Seq<Payload> payloads) {
        this.payloads = payloads;
    }

    @Override
    public void update() {
        super.update();
        updatePayload();
    }

    @Override
    public void draw() {
        super.draw();
        drawPayload();
    }

    @Override
    public float payloadUsed() {
        return payloadUsed;
    }

    @Override
    public boolean canPickup(Unit unit) {
        return type.pickupUnits && payloadUsed + unit.hitSize * unit.hitSize <= type.payloadCapacity + 0.001f && unit.team == team && unit.isAI();
    }

    @Override
    public boolean canPickup(Building build) {
        return payloadUsed + build.block.size * build.block.size * Vars.tilesize * Vars.tilesize <= type.payloadCapacity + 0.001f && build.canPickup() && build.team == team;
    }

    @Override
    public boolean canPickupPayload(Payload pay) {
        return payloadUsed + pay.size() * pay.size() <= type.payloadCapacity + 0.001f && (type.pickupUnits || !(pay instanceof UnitPayload));
    }

    @Override
    public boolean hasPayload() {
        return payloads.size > 0;
    }

    @Override
    public boolean canDropPayload() {
        return hasPayload();
    }

    @Override
    public void playPayloadDropSound(Payload payload) {
        Sounds.mechStep.at(this);
    }

    @Override
    public void addPayload(Payload load) {
        payloads.add(load);
        payloadUsed += load.size() * load.size();
    }

    @Override
    public void pickup(Unit unit) {
        unit.remove();
        addPayload(new UnitPayload(unit));
        Fx.unitPickup.at(unit);
        if (Vars.net.client()) {
            Vars.netClient.clearRemovedEntity(unit.id);
        }
    }

    @Override
    public void pickup(Building tile) {
        tile.pickedUp();
        tile.tile.remove();
        tile.afterPickedUp();
        addPayload(new BuildPayload(tile));
        Fx.unitPickup.at(tile);
    }

    @Override
    public boolean dropLastPayload() {
        if (payloads.isEmpty()) return false;
        Payload load = payloads.peek();
        if (tryDropPayload(load)) {
            payloads.pop();
            payloadUsed -= load.size() * load.size();
            return true;
        }
        return false;
    }

    @Override
    public boolean tryDropPayload(Payload payload) {
        Tile on = tileOn();
        if (Vars.net.client() && payload instanceof UnitPayload u) {
            Vars.netClient.clearRemovedEntity(u.unit.id);
        }

        if (on != null && on.build != null && on.build.acceptPayload(on.build, payload)) {
            playPayloadDropSound(payload);
            Fx.unitDrop.at(on.build);
            on.build.handlePayload(on.build, payload);
            return true;
        }

        if (payload instanceof BuildPayload b) {
            return dropBlock(b);
        } else if (payload instanceof UnitPayload p) {
            return dropUnit(p);
        }
        return false;
    }

    @Override
    public boolean dropUnit(UnitPayload payload) {
        Unit u = payload.unit;
        if (!u.canPass(tileX(), tileY()) || Units.count(x, y, u.physicSize(), o -> o.isGrounded() && o != this) > 1) {
            return false;
        }
        playPayloadDropSound(payload);
        Fx.unitDrop.at(this);
        if (!Vars.net.client()) {
            u.set(x, y);
            u.trns(Vec2.ZERO);
            u.rotation(rotation);
            u.id = EntityGroup.nextId();
            if (!u.isAdded()) u.team.data().updateCount(u.type, -1);
            u.add();
            u.unloaded();
        }
        return true;
    }

    @Override
    public boolean dropBlock(BuildPayload payload) {
        Building tile = payload.build;
        int tx = World.toTile(x - tile.block.offset);
        int ty = World.toTile(y - tile.block.offset);
        Tile on = Vars.world.tile(tx, ty);
        if (on != null && Build.validPlace(tile.block, tile.team, tx, ty, tile.rotation, false)) {
            playPayloadDropSound(payload);
            payload.set(on.worldx() + tile.block.offset, on.worldy() + tile.block.offset, tile.rotation);
            on.setBlock(tile.block, tile.team, tile.rotation, () -> tile);
            tile.dropped();
            Fx.unitDrop.at(tile);
            return true;
        }
        return false;
    }

    @Override
    public void contentInfo(Table table, float itemSize, float width) {
        table.row();
        table.table(t -> {
            for (Payload p : payloads) {
                t.image(p.icon()).size(itemSize).padRight(4f);
            }
        }).left();
    }

        public void updatePayload() {
        for (Payload p : payloads) {
            p.set(x, y, rotation);
            p.update(this, null);
        }
    }

    public void drawPayload() {
        for (Payload p : payloads) {
            p.draw();
        }
    }

    @Override
    public int classId() {
        return classId;
    }
}
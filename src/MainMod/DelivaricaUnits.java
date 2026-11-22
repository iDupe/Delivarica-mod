package MainMod;

import MainMod.Units.Dent;
import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.BuilderAI;
import mindustry.ai.types.CargoAI;
import mindustry.ai.types.GroundAI;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BombBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.gen.LegsUnit;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.gen.EntityMapping;


public class DelivaricaUnits {

    // Юниты мода | Mod units
        // Юниты мода | Mod units
    public static UnitType side, photon, photonArmored, bunch, bunchArmored, scrape, dent;

    public static void load() {

        Dent.classId = EntityMapping.register("dent-legs-payload", Dent::new);

        // Юнит ядра | Core unit
        side = new UnitType("side") {{
            constructor = UnitEntity::create;
            aiController = BuilderAI::new;

            flying = true;
            isEnemy = false;
            speed = 2.6f;
            accel = 0.08f;
            drag = 0.04f;
            rotateSpeed = 5f;
            health = 240f;
            armor = 1f;
            hitSize = 11f;
            engineOffset = 6f;
            engineSize = 2.5f;
            outlineColor = Color.valueOf("36363c");

            // Строительство и добыча | Building and mining
            buildSpeed = 0.75f;
            mineSpeed = 7f;
            mineTier = 2;
            itemCapacity = 40;

            // Силовой щит: радиус 40, реген 1000/15с, прочность 1000, КД 15с | Force field: radius 40, regen 1000/15s, max 1000, cooldown 15s
            abilities.add(new ForceFieldAbility(40f, 1000f / 900f, 1000f, 900f) {
                float savedShield = 1f; // Старт с 1 HP против абуза респавна | Start with 1 HP to prevent respawn abuse
                float breakTimer = 0f;
                float drainTimer = 0f;
                boolean shieldDeployed = false;

                {
                    sides = 36;
                }

                @Override
                public void update(Unit unit) {
                    // Ожидание перезарядки после пробития (15 сек) | Cooldown wait after shield break (15s)
                    if (breakTimer > 0f) {
                        breakTimer -= Time.delta;
                        unit.shield = 0f;
                        shieldDeployed = false;
                        drainTimer = 0f;
                        radiusScale = Mathf.lerpDelta(radiusScale, 0f, 0.08f);
                        if (alpha > 0f) {
                            alpha = Math.max(0f, alpha - Time.delta / 10f);
                        }
                        if (breakTimer <= 0f) {
                            savedShield = max;
                        }
                        return;
                    }

                    boolean active = unit.isShooting && !unit.isBuilding() && !unit.mining();

                    if (active) {
                        if (!shieldDeployed) {
                            unit.shield = savedShield;
                            shieldDeployed = true;
                        }

                        super.update(unit);

                        if (unit.shield > 0f) {
                            // Компенсация встроенного регена из super.update() | Cancel built-in regen from super.update()
                            unit.shield = Math.max(0f, unit.shield - regen * Time.delta);

                            // Расход заряда раз в секунду | Drain shield charge once per second
                            drainTimer += Time.delta;
                            if (drainTimer >= 60f) {
                                drainTimer -= 60f;
                                unit.shield = Math.max(0f, unit.shield - (100f / 6f));
                            }
                        }

                        // Пробитие или полная разрядка щита | Shield break or full discharge
                        if (unit.shield <= 0f) {
                            unit.shield = 0f;
                            savedShield = 0f;
                            shieldDeployed = false;
                            drainTimer = 0f;
                            breakTimer = cooldown;
                            Fx.shieldBreak.at(unit.x, unit.y, radius, unit.team.color);
                        } else {
                            savedShield = unit.shield;
                        }
                    } else {
                        drainTimer = 0f;

                        // Плавное сворачивание купола | Smoothly collapse dome
                        if (radiusScale > 0.03f) {
                            radiusScale = Mathf.lerpDelta(radiusScale, 0f, 0.06f);
                            savedShield = unit.shield;
                        } else {
                            radiusScale = 0f;
                            if (shieldDeployed) {
                                savedShield = unit.shield;
                                shieldDeployed = false;
                            }
                            unit.shield = 0f;

                            // Пассивная регенерация в свёрнутом состоянии | Passive regeneration while collapsed
                            if (savedShield < max) {
                                savedShield = Math.min(max, savedShield + regen * Time.delta);
                            }
                        }

                        if (alpha > 0f) {
                            alpha = Math.max(0f, alpha - Time.delta / 10f);
                        }
                    }
                }
            });

            // Оружие-триггер без урона для активации щита | Zero-damage trigger weapon for shield activation
            weapons.add(new Weapon() {{
                x = 0f;
                y = 0f;
                mirror = false;
                reload = 1f;
                shootSound = Sounds.none;
                shootCone = 360f;

                bullet = new BulletType(0f, 0f) {{
                    lifetime = 1f;
                    instantDisappear = true;
                    collides = false;
                    hittable = false;
                    absorbable = false;
                    hitEffect = Fx.none;
                    despawnEffect = Fx.none;
                    shootEffect = Fx.none;
                    smokeEffect = Fx.none;
                }};
            }});
        }};

        // Фотон (базовый артиллерийский мех) | Photon (base artillery mech)
        photon = new UnitType("photon") {{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.60f;
            hitSize = 11f;
            health = 420f;
            armor = 2f;
            rotateSpeed = 4f;
            outlineColor = Color.valueOf("36363c");

            mechStride = 4.2f;
            mechFrontSway = 0.25f;
            mechSideSway = 0.35f;
            mechStepParticles = true;

            weapons.add(new Weapon(name + "-w") {{
                reload = 35f;
                x = 4.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing1;

                bullet = new ArtilleryBulletType(3f, 12, "shell") {{
                    hitEffect = Fx.flakExplosion;
                    knockback = 0.6f;
                    lifetime = 48f;
                    width = height = 6.5f;
                    collides = true;
                    collidesTiles = true;
                    splashDamageRadius = 14f;
                    splashDamage = 14f;
                    homingPower = 0.08f;
                    homingRange = 50f;
                    backColor = Pal.gray;
                    frontColor = Color.lightGray;
                    trailColor = Pal.gray;
                }};
            }});
        }};

        // Бронированный Фотон | Armored Photon
        photonArmored = new UnitType("photon-armored") {{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.48f;
            hitSize = 11f;
            health = 620f;
            armor = 5f;
            rotateSpeed = 4f;
            outlineColor = Color.valueOf("36363c");

            alwaysUnlocked = true;

            mechStride = 4.2f;
            mechFrontSway = 0.25f;
            mechSideSway = 0.35f;
            mechStepParticles = true;

            weapons.add(new Weapon(photon.name + "-w") {{
                reload = 35f;
                x = 4.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing1;

                bullet = new ArtilleryBulletType(3f, 12, "shell") {{
                    hitEffect = Fx.flakExplosion;
                    knockback = 0.6f;
                    lifetime = 48f;
                    width = height = 6.5f;
                    collides = true;
                    collidesTiles = true;
                    splashDamageRadius = 14f;
                    splashDamage = 14f;
                    homingPower = 0.08f;
                    homingRange = 50f;
                    backColor = Pal.gray;
                    frontColor = Color.lightGray;
                    trailColor = Pal.gray;
                }};
            }});
        }};

        // Банч (базовый штурмовой мех) | Bunch (base assault mech)
        bunch = new UnitType("bunch") {{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.55f;
            hitSize = 12f;
            health = 950f;
            armor = 6f;
            rotateSpeed = 3f;
            outlineColor = Color.valueOf("36363c");

            mechStride = 5.5f;
            mechFrontSway = 0.3f;
            mechSideSway = 0.4f;
            mechStepParticles = true;

            weapons.add(new Weapon(this.name + "-w") {{
                reload = 60f;
                x = 6.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing2;

                // Параметры дроби | Buckshot parameters
                inaccuracy = 12f;
                shoot.shots = 5;
                shoot.shotDelay = 0f;

                bullet = new BasicBulletType(5f, 25) {{
                    width = 5f;
                    height = 7f;
                    lifetime = 18f;

                    trailLength = 15;
                    trailWidth = 2f;
                    trailColor = Color.gray;

                    backColor = Color.darkGray;
                    frontColor = Color.lightGray;

                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.none;
                }};
            }});
        }};

        // Бронированный Банч | Armored Bunch
        bunchArmored = new UnitType("bunch-armored") {{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.45f;
            hitSize = 12f;
            health = 1350f;
            armor = 10f;
            rotateSpeed = 2.5f;
            outlineColor = Color.valueOf("36363c");

            alwaysUnlocked = true;

            mechStride = 5.5f;
            mechFrontSway = 0.3f;
            mechSideSway = 0.4f;
            mechStepParticles = true;

            weapons.add(new Weapon(bunch.name + "-w") {{
                reload = 60f;
                x = 6.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing2;

                // Параметры дроби | Buckshot parameters
                inaccuracy = 12f;
                shoot.shots = 5;
                shoot.shotDelay = 0f;

                bullet = new BasicBulletType(5f, 25) {{
                    width = 5f;
                    height = 7f;
                    lifetime = 18f;

                    trailLength = 15;
                    trailWidth = 2f;
                    trailColor = Color.gray;

                    backColor = Color.darkGray;
                    frontColor = Color.lightGray;

                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.none;
                }};
            }});
        }};

        // Скрейп (Т1 паук с круговой ударной волной) | Scrape (T1 spider with radial shockwave)
        scrape = new UnitType("scrape") {{
            constructor = LegsUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.72f;
            hitSize = 10f;
            health = 340f;
            armor = 2f;
            rotateSpeed = 4.5f;
            outlineColor = Color.valueOf("36363c");

            legCount = 4;
            legLength = 11f;
            legLengthScl = 0.75f;
            legBaseOffset = 2.2f;
            legExtension = -1.5f;
            legForwardScl = 0.65f;
            legMoveSpace = 1.1f;
            legSpeed = 0.25f;
            legGroupSize = 3;
            hovering = true;
            allowLegStep = true;
            groundLayer = Layer.legUnit;


            // Встроенное оружие со слабой круговой ударной волной вокруг себя | Radial shockwave weapon
            weapons.add(new Weapon(name + "-w") {{
                x = 0f;
                y = 0f;
                mirror = false;
                reload = 75f;
                shootCone = 360f;
                shootSound = Sounds.shootArtillerySmall;
                ejectEffect = Fx.none;

                bullet = new BasicBulletType(0f, 18f) {{
                    instantDisappear = true;
                    lifetime = 1f;
                    collides = false;
                    hittable = false;
                    absorbable = false;

                    // Увеличенный радиус ударной волны вокруг корпуса | Expanded radial shockwave around body
                    splashDamage = 18f;
                    splashDamageRadius = 56f;
                    knockback = 2.2f;
                    despawnEffect = Fx.none;

                    // Кастомный многослойный эффект волны под цвет ядра паука | Custom multi-layered shockwave effect
                    hitEffect = new Effect(28f, e -> {
                        // Внешнее фиолетовое кольцо | Outer violet ring
                        arc.graphics.g2d.Draw.color(Color.valueOf("a882ff"), Color.valueOf("5a4a9a"), e.fin());
                        arc.graphics.g2d.Lines.stroke(2.8f * e.fout());
                        arc.graphics.g2d.Lines.circle(e.x, e.y, 4f + 52f * e.finpow());

                        // Внутреннее оранжевое кольцо энергии | Inner orange energy ring
                        arc.graphics.g2d.Draw.color(Color.valueOf("ffb85c"), Color.valueOf("ff6e30"), e.fin());
                        arc.graphics.g2d.Lines.stroke(1.6f * e.fout());
                        arc.graphics.g2d.Lines.circle(e.x, e.y, 2f + 36f * e.fin());

                        // Разлетающиеся радиальные искры | Radial energy sparks
                        arc.graphics.g2d.Draw.color(Color.valueOf("e4d4ff"), Color.valueOf("ffb85c"), e.fin());
                        arc.math.Angles.randLenVectors(e.id, 14, 6f + 50f * e.finpow(), (x, y) -> {
                            arc.graphics.g2d.Lines.lineAngle(
                                e.x + x,
                                e.y + y,
                                arc.math.Mathf.angle(x, y),
                                4f * e.fout() + 1f
                            );
                        });
                    });
                }};
            }});
        }};

        // Дент (Т2 паук-бомбардировщик и перевозчик грузов) | Dent (T2 spider bomber and payload carrier)
        dent = new UnitType("dent") {{
            constructor = Dent::new;
            aiController = GroundAI::new;
            defaultCommand = UnitCommand.moveCommand;
            commands = Seq.with(
                UnitCommand.moveCommand,
                UnitCommand.enterPayloadCommand
            );

            flying = false;
            speed = 0.68f;
            hitSize = 14f;
            health = 780f;
            armor = 4f;
            rotateSpeed = 4f;
            outlineColor = Color.valueOf("36363c");

            // Грузоподъёмность: постройки и юниты до 2x2 | Payload capacity up to 2x2
            payloadCapacity = (2f * 8f) * (2f * 8f);
            pickupUnits = true;

            // Параметры паучьих лап (отображаются корректно) | Spider leg parameters (rendered properly)
            legCount = 6;
            legLength = 14f;
            legBaseOffset = 3f;
            legExtension = -1.5f;
            legForwardScl = 0.65f;
            legMoveSpace = 1.15f;
            legSpeed = 0.22f;
            legGroupSize = 3;
            hovering = true;
            allowLegStep = true;
            groundLayer = Layer.legUnit;

            // Бомбомёт: сбрасывает фиолетовую бомбу под себя раз в 2 секунды | Bomb dropper: drops purple bomb every 2s
            weapons.add(new Weapon() {{
                x = 0f;
                y = 0f;
                mirror = false;
                reload = 120f;
                shootCone = 360f;
                shootSound = Sounds.explosionArtilleryShock;
                ejectEffect = Fx.none;

                bullet = new BombBulletType(0f, 80f) {{
                    width = 11f;
                    height = 11f;
                    lifetime = 45f;
                    spin = 4f;

                    backColor = Color.valueOf("6e47c7");
                    frontColor = Color.valueOf("d6b8ff");

                    // Радиус 10 тайлов (80f) и урон: 140 в центре -> 14 на краю | 10-tile radius, 140 center -> 14 edge
                    splashDamage = 140f;
                    splashDamageRadius = 80f;
                    scaledSplashDamage = true;
                    splashDamagePierce = false;

                    hitEffect = new Effect(32f, e -> {
                        arc.graphics.g2d.Draw.color(Color.valueOf("b58aff"), Color.valueOf("5a36a8"), e.fin());
                        arc.graphics.g2d.Lines.stroke(3f * e.fout());
                        arc.graphics.g2d.Lines.circle(e.x, e.y, 6f + 74f * e.finpow());

                        arc.graphics.g2d.Draw.color(Color.valueOf("ebd9ff"), Color.valueOf("8b5cf6"), e.fin());
                        arc.math.Angles.randLenVectors(e.id, 18, 8f + 72f * e.finpow(), (x, y) -> {
                            arc.graphics.g2d.Fill.circle(e.x + x, e.y + y, 3.5f * e.fout());
                        });
                    });
                    despawnEffect = Fx.none;
                }};
            }});
        }};


        // Наследование спрайтов для бронированных версий | Inherit sprites for armored variants
        Events.on(ClientLoadEvent.class, e -> {
            photonArmored.legRegion = photon.legRegion;
            photonArmored.baseRegion = photon.baseRegion;
            photonArmored.cellRegion = photon.cellRegion;

            bunchArmored.legRegion = bunch.legRegion;
            bunchArmored.baseRegion = bunch.baseRegion;
            bunchArmored.cellRegion = bunch.cellRegion;
        });
    }
}

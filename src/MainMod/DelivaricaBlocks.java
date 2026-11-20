package MainMod;

import MainMod.Blocks.xNode;
import arc.graphics.Color;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.distribution.OverflowGate;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.Prop;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.SteamVent;
import mindustry.world.blocks.environment.TallBlock;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.payloads.Constructor;
import mindustry.world.blocks.payloads.PayloadConveyor;
import mindustry.world.blocks.payloads.PayloadRouter;
import mindustry.world.blocks.power.ThermalGenerator;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;
import mindustry.world.draw.DrawArcSmelt;
import mindustry.world.draw.DrawDefault;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawParticles;
import mindustry.world.draw.DrawRegion;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.Attribute;

public class DelivaricaBlocks {

    // Покрытия пола | Floor tiles
    public static Floor
        redSand, sandrock, obsidian, rindit, smoothRindit,
        drangite, whiteIce, letrite, letriteCrush, whiteSnow;

    // Природные стены | Natural walls
    public static StaticWall
        sandrockWall, sandrockWallLarge,
        obsidianWall, obsidianWallLarge,
        rinditWall, rinditWallLarge,
        drangiteWall, drangiteWallLarge,
        whiteIceWall, whiteIceWallLarge,
        letriteWall, letriteWallLarge,
        whiteSnowWall, whiteSnowWallLarge;

    // Высокие объекты и гейзеры | Tall blocks and vents
    public static TallBlock phosphoriteCubes, neotiteCluster, rinditColumn;
    public static SteamVent obsidianVent;

    // Декоративные валуны | Decorative boulders
    public static Prop obsidianBoulder, rinditBoulder, sandrockBoulder, drangiteBoulder, letriteBoulder;

    // Руды | Ores
    public static OreBlock oreChromium, oreNickel;

    // Ядра | Cores
    public static CoreBlock coreReset, coreOff;

    // Защитные стены | Defensive walls
    public static Wall chromiumWall, chromiumWallBig;

    // Буры | Drills
    public static Drill mechanicalDrill;

    // Грузовые конвейеры | Payload conveyors
    public static PayloadConveyor inertialFreightConveyor;
    public static PayloadRouter inertialFreightRouter;

    // Сборщики и фабрики | Constructors and factories
    public static Constructor basicComponentConstructor, basicCargoConstructor;
    public static Reconstructor basicUnitAmplifier, groundReconstructor, advancedReconstructor;
    public static UnitFactory groundConstructor;


    // Предметная логистика | Item distribution
    public static Duct inertialDuct;
    public static Junction inertialJunction;
    public static Router inertialRouter;
    public static BufferedItemBridge inertialBridge;
    public static OverflowGate inertialOverflowGate;
    public static OverflowGate inertialUnderflowGate;

    // Жидкостная логистика | Liquid distribution
    public static LiquidRouter liquiTankOne;

    // Энергетика | Power
    public static ThermalGenerator mawTurbine;
    public static xNode xNode;

    // Производство | Production
    public static GenericCrafter ramtaniumSmelter, lustriteMixer;

    // Турели | Turrets
    public static ItemTurret range, hawk, concussion;

    // Кастомный маршрутизатор каналов | Custom duct router
    public static class InertialDuctRouter extends DuctRouter {
        public InertialDuctRouter() {
            super("inertial-router");
        }

        @Override
        public void load() {
            super.load();
            topRegion = region;
        }
    }

    public static void load() {
        // Окружение: песок и песчаник | Environment: sand and sandrock
        redSand = new Floor("redsand-floor") {{
            variants = 3;
            mapColor = Color.valueOf("6e4535");
        }};

        sandrock = new Floor("sandrock-floor") {{
            variants = 3;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockWall = new StaticWall("sandrock-wall") {{
            variants = 2;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockWallLarge = new StaticWall("sandrock-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockBoulder = new Prop("sandrock-boulder") {{
            variants = 3;
        }};

        // Окружение: обсидиан | Environment: obsidian
        obsidian = new Floor("obsidian-floor") {{
            variants = 3;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianWall = new StaticWall("obsidian-wall") {{
            variants = 2;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianWallLarge = new StaticWall("obsidian-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianVent = new SteamVent("a-obsidian-vent") {{
            parent = blendGroup = obsidian;
            variants = 2;
            attributes.set(Attribute.steam, 1f);
            attributes.set(Attribute.heat, 1f);
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianBoulder = new Prop("obsidian-boulder") {{
            variants = 2;
        }};

        // Окружение: риндит и кристаллы | Environment: rindit and crystals
        rindit = new Floor("rindit-floor") {{
            variants = 3;
            mapColor = Color.valueOf("5a5d6b");
        }};
        smoothRindit = new Floor("smooth-rindit-floor") {{
            variants = 3;
            mapColor = Color.valueOf("4e505c");
        }};
        rinditWall = new StaticWall("rindit-wall") {{
            variants = 2;
            mapColor = Color.valueOf("5a5d6b");
        }};
        rinditWallLarge = new StaticWall("rindit-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("5a5d6b");
        }};
        rinditBoulder = new Prop("rindit-boulder") {{
            variants = 3;
        }};
        rinditColumn = new TallBlock("rindit-column") {{
            variants = 2;
            clipSize = 128f;
        }};

        phosphoriteCubes = new TallBlock("phosphorite-cubes") {{
            variants = 2;
            clipSize = 128f;
            solid = true;
            shadowOffset = -2.5f;
            mapColor = Color.valueOf("d4d4af");
        }};
        neotiteCluster = new TallBlock("neotite-cluster") {{
            variants = 2;
            clipSize = 128f;
            solid = true;
            shadowOffset = -2.5f;
            mapColor = Color.valueOf("404552");
        }};

        // Окружение: дрангит | Environment: drangite
        drangite = new Floor("drangite-floor") {{
            variants = 3;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteWall = new StaticWall("drangite-wall") {{
            variants = 2;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteWallLarge = new StaticWall("drangite-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteBoulder = new Prop("drangite-boulder") {{
            variants = 3;
        }};

        // Окружение: лёд и снег | Environment: ice and snow
        whiteIce = new Floor("white-ice-floor") {{
            variants = 4;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteIceWall = new StaticWall("white-ice-wall") {{
            variants = 2;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteIceWallLarge = new StaticWall("white-ice-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("e2eaf0");
        }};

        whiteSnow = new Floor("white-snow") {{
            variants = 3;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteSnowWall = new StaticWall("white-snow-wall") {{
            variants = 2;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteSnowWallLarge = new StaticWall("white-snow-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("e2eaf0");
        }};

        // Окружение: летрит | Environment: letrite
        letrite = new Floor("letrite-floor") {{
            variants = 4;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteCrush = new Floor("letrite-floor-crush") {{
            variants = 3;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteWall = new StaticWall("letrite-wall") {{
            variants = 2;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteWallLarge = new StaticWall("letrite-wall-large") {{
            variants = 1;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteBoulder = new Prop("letrite-boulder") {{
            variants = 2;
        }};

        // Руды | Ores
        oreChromium = new OreBlock("chromium-ore", DelivaricaItems.chromium) {{
            variants = 3;
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47f;
        }};

        oreNickel = new OreBlock("nickel-ore", DelivaricaItems.nickel) {{
            variants = 3;
            oreDefault = true;
            oreThreshold = 0.83f;
            oreScale = 24.5f;
        }};

        // Ядра | Cores
        coreReset = new CoreBlock("coreReset") {{
            requirements(Category.effect, ItemStack.with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = DelivaricaUnits.side;
            health = 4200;
            itemCapacity = 2000;
            size = 4;
            unitCapModifier = 8;
            drawTeamOverlay = true;
        }};

        coreOff = new CoreBlock("coreOff") {{
            requirements(Category.effect, ItemStack.with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = DelivaricaUnits.side;
            health = 2400;
            itemCapacity = 1000;
            size = 3;
            unitCapModifier = 4;
            drawTeamOverlay = true;
        }};

        // Стены | Walls
        chromiumWall = new Wall("chromium-wall") {{
            requirements(Category.defense, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 600;
            size = 1;
        }};

        chromiumWallBig = new Wall("chromium-wall-big") {{
            requirements(Category.defense, ItemStack.with(DelivaricaItems.chromium, 24));
            health = 2400;
            size = 2;
        }};

        // Буры | Drills
        mechanicalDrill = new Drill("mechanical-drill") {{
            requirements(Category.production, ItemStack.with(DelivaricaItems.chromium, 12));
            tier = 2;
            drillTime = 30;
            size = 2;
            drawMineItem = true;
            consumeLiquid(Liquids.water, 0.05f).boost();
        }};

        // Грузовые конвейеры | Payload conveyors
        inertialFreightConveyor = new PayloadConveyor("inertial-freight-conveyor") {{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 10));
            canOverdrive = false;
            size = 3;
            moveTime = 45f;
            payloadLimit = 3f;
        }};

        inertialFreightRouter = new PayloadRouter("inertial-freight-router") {{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 15));
            canOverdrive = false;
            size = 3;
            moveTime = 45f;
            payloadLimit = 3f;
        }};

        // Сборщики и фабрики | Constructors and factories
        basicComponentConstructor = new Constructor("basic-component-constructor") {{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 40));
            hasPower = true;
            consumePower(1.5f);
            size = 3;
            maxBlockSize = 2;
            buildSpeed = 0.6f;
            filter = Seq.with(chromiumWall, chromiumWallBig);
        }

        @Override
        public void load() {
            super.load();
            inRegion = arc.Core.atlas.find("delivarica-mod-unit-un");
            outRegion = arc.Core.atlas.find("delivarica-mod-unit-out");
        }};

        basicCargoConstructor = new Constructor("basic-cargo-constructor") {{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 80));
            hasPower = true;
            consumePower(3f);
            size = 3;
            maxBlockSize = 3;
            buildSpeed = 0.35f;
            filter = Seq.with(mechanicalDrill);
        }

        @Override
        public void load() {
            super.load();
            inRegion = arc.Core.atlas.find("delivarica-mod-unit-un");
            outRegion = arc.Core.atlas.find("delivarica-mod-unit-out");
        }};

        basicUnitAmplifier = new Reconstructor("basic-unit-amplifier") {{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 80));
            size = 3;
            consumePower(2f);
            consumeItems(ItemStack.with(DelivaricaItems.ramtanium, 20));
            constructTime = 60f * 10f;

            upgrades.addAll(
                new UnitType[]{DelivaricaUnits.photon, DelivaricaUnits.photonArmored},
                new UnitType[]{DelivaricaUnits.bunch, DelivaricaUnits.bunchArmored}
            );
        }

        @Override
        public void load() {
            super.load();
            inRegion = arc.Core.atlas.find("delivarica-mod-unit-un");
            outRegion = arc.Core.atlas.find("delivarica-mod-unit-out");
        }};

        groundConstructor = new UnitFactory("ground-constructor") {{
            requirements(Category.units, ItemStack.with(
                DelivaricaItems.chromium, 80,
                DelivaricaItems.nickel, 60
            ));
            size = 3;
            consumePower(1.5f);

            plans = Seq.with(
                new UnitPlan(
                    DelivaricaUnits.photon,
                    60f * 15f,
                    ItemStack.with(DelivaricaItems.chromium, 40, DelivaricaItems.nickel, 35)
                )
            );
        }

        @Override
        public void load() {
            super.load();
            outRegion = arc.Core.atlas.find("delivarica-mod-unit-out");
        }};

        groundReconstructor = new Reconstructor("ground-reconstructor") {{
            requirements(Category.units, ItemStack.with(
                DelivaricaItems.chromium, 120,
                DelivaricaItems.nickel, 80
            ));
            size = 3;
            consumePower(2.5f);
            consumeItems(ItemStack.with(
                DelivaricaItems.tantalum, 30,
                DelivaricaItems.nickel, 40
            ));
            constructTime = 60f * 15f;

            upgrades.addAll(
                new UnitType[]{DelivaricaUnits.photon, DelivaricaUnits.bunch}
            );
        }

        @Override
        public void load() {
            super.load();
            inRegion = arc.Core.atlas.find("delivarica-mod-unit-un");
            outRegion = arc.Core.atlas.find("delivarica-mod-unit-out");
        }};

        advancedReconstructor = new Reconstructor("advanced-reconstructor") {{
            requirements(Category.units, ItemStack.with(
                DelivaricaItems.chromium, 300,
                DelivaricaItems.ramtanium, 150,
                DelivaricaItems.lustrite, 80
            ));
            size = 5;
            consumePower(5f);
            consumeItems(ItemStack.with(
                DelivaricaItems.ramtanium, 60,
                DelivaricaItems.lustrite, 40
            ));
            constructTime = 60f * 25f;

            // Использует собственные спрайты advanced-reconstructor-in / advanced-reconstructor-out | Uses its own in/out sprites
        }};

        // Энергетика | Power
        mawTurbine = new ThermalGenerator("vent-turbine") {{
            requirements(Category.power, ItemStack.with(DelivaricaItems.chromium, 40, DelivaricaItems.tantalum, 20));

            attribute = Attribute.steam;
            displayEfficiency = false;
            ambientSound = Sounds.loopHum;
            ambientSoundVolume = 0.06f;
            minEfficiency = 9f - 0.0001f;
            displayEfficiencyScale = 1f / 9f;

            size = 3;
            health = 500;
            powerProduction = 4f / 9f;
            generateEffect = Fx.turbinegenerate;
            effectChance = 0.05f;

            drawer = new DrawMulti(
                new DrawDefault(),
                new DrawRegion("-rotator") {{
                    spinSprite = true;
                    rotateSpeed = 4f;
                }},
                new DrawRegion("-top")
            );
        }};

        xNode = new xNode("x-node") {{
            requirements(Category.power, ItemStack.with(DelivaricaItems.chromium, 12));
            consumesPower = outputsPower = true;
            health = 40;
            range = 6;
            fogRadius = 1;
            researchCost = ItemStack.with(DelivaricaItems.chromium, 12);
            crushFragile = true;
            consumePowerBuffered(500f);
        }};

        // Производство | Production
        ramtaniumSmelter = new GenericCrafter("ramtanium-smelter") {{
            requirements(Category.crafting, ItemStack.with(DelivaricaItems.chromium, 90, DelivaricaItems.nickel, 75));
            size = 3;
            health = 800;
            craftTime = 60f;
            hasPower = true;
            consumePower(1f);

            consumeItems(ItemStack.with(DelivaricaItems.nickel, 2, DelivaricaItems.chromium, 1));
            outputItem = new ItemStack(DelivaricaItems.ramtanium, 1);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawArcSmelt(),
                new DrawDefault()
            );
        }};

        lustriteMixer = new GenericCrafter("lustrite-mixer") {{
            requirements(Category.crafting, ItemStack.with(DelivaricaItems.chromium, 60, DelivaricaItems.nickel, 40));
            size = 3;
            health = 1000;
            craftTime = 120f;
            hasPower = true;
            consumePower(1.2f);

            drawer = new DrawMulti(
                new DrawRegion("-bottom"),
                new DrawParticles() {{
                    color = Color.valueOf("a0abe3");
                    alpha = 0.5f;
                    particleSize = 3f;
                    particles = 10;
                    particleRad = 9f;
                    particleLife = 200f;
                    reverse = true;
                    particleSizeInterp = Interp.one;
                }},
                new DrawRegion("-rotator") {{
                    spinSprite = true;
                    rotateSpeed = 4f;
                }},
                new DrawDefault()
            );

            consumeItems(ItemStack.with(DelivaricaItems.ramtanium, 1, DelivaricaItems.nickel, 2));
            outputItem = new ItemStack(DelivaricaItems.lustrite, 1);
        }};

        // Предметная логистика | Item distribution
        inertialJunction = new Junction("inertial-junction") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 4));
            health = 90;
            speed = 26f;
            capacity = 6;
        }};

        inertialBridge = new BufferedItemBridge("inertial-bridge") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 12));
            fadeIn = moveArrows = false;
            health = 90;
            range = 4;
            speed = 74f;
            arrowSpacing = 6f;
            bufferCapacity = 14;
            crushFragile = true;
        }};

        inertialDuct = new Duct("inertial-duct") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 1));
            health = 90;
            speed = 4f;
            junctionReplacement = inertialJunction;
            bridgeReplacement = inertialBridge;
        }};

        inertialRouter = new Router("inertial-router") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 4));
            health = 90;
            speed = 8f;
        }};

        inertialOverflowGate = new OverflowGate("inertial-in") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 90;
            speed = 1f;
        }};

        inertialUnderflowGate = new OverflowGate("inertial-out") {{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 90;
            speed = 1f;
            invert = true;
        }};

        // Турели | Turrets
        range = new ItemTurret("range") {{
            requirements(Category.turret, ItemStack.with(
                DelivaricaItems.chromium, 45
            ));
            size = 2;
            health = 420;
            range = 160f;
            reload = 30f;
            recoil = 1.5f;
            recoils = 2;
            rotateSpeed = 6f;
            shootSound = Sounds.shoot;

            shoot = new ShootAlternate() {{
                spread = 3.5f;
            }};

            drawer = new DrawTurret("delivarica-") {{
                parts.add(
                    new RegionPart("-barrel-l") {{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 0;
                    }},
                    new RegionPart("-barrel-r") {{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 1;
                    }}
                );
            }};

            ammo(
                DelivaricaItems.chromium, new BasicBulletType(5f, 22) {{
                    sprite = "shell";
                    width = 9f;
                    height = 12f;
                    lifetime = 32f;
                    ammoMultiplier = 3;
                    backColor = Color.valueOf("948f99");
                    frontColor = Color.white;
                }},
                DelivaricaItems.ramtanium, new BasicBulletType(5f, 45) {{
                    sprite = "shell";
                    width = 10f;
                    height = 14f;
                    lifetime = 32f;
                    ammoMultiplier = 2;
                    pierceCap = 2;
                    pierceArmor = true;
                    armorMultiplier = 1.5f;
                    backColor = Color.valueOf("b8c6ae");
                    frontColor = Color.white;
                }}
            );
        }};

        hawk = new ItemTurret("hawk") {{
            requirements(Category.turret, ItemStack.with(
                DelivaricaItems.chromium, 60
            ));
            size = 2;
            health = 720;
            range = 120f;
            reload = 60f;
            recoil = 1.5f;
            recoils = 2;
            rotateSpeed = 4f;
            shootSound = Sounds.shootSpectre;

            // Бьёт только по наземным целям | Targets ground units only
            targetAir = false;

            shoot = new ShootAlternate() {{
                spread = 3.5f;
            }};

            drawer = new DrawTurret("delivarica-") {{
                parts.add(
                    new RegionPart("-blade-l") {{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 0;
                    }},
                    new RegionPart("-blade-r") {{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 1;
                    }}
                );
            }};

            ammo(
                DelivaricaItems.nickel, new BasicBulletType(5f, 30) {{
                    sprite = "bullet";
                    width = 12f;
                    height = 16f;
                    lifetime = 24f;
                    ammoMultiplier = 2;
                    knockback = 1.2f;
                    collidesAir = false;
                    backColor = Color.valueOf("6d7885");
                    frontColor = Color.white;
                }}
            );
        }};

        concussion = new ItemTurret("concussion") {{
            requirements(Category.turret, ItemStack.with(
                DelivaricaItems.chromium, 70, DelivaricaItems.nickel, 40
            ));
            size = 2;
            health = 520;
            range = 80f;
            reload = 55f;
            recoil = 1.2f;
            rotateSpeed = 5f;
            shootSound = Sounds.shootArtillerySmall;
            ammoPerShot = 2;

            shoot.shots = 5;
            shoot.shotDelay = 0f;
            inaccuracy = 12f;

            ammo(
                DelivaricaItems.nickel, new BasicBulletType(5f, 10) {{
                    sprite = "bullet";
                    width = 8f;
                    height = 10f;
                    lifetime = 16f;
                    trailLength = 15;
                    trailWidth = 2f;
                    trailColor = Color.gray;
                    backColor = Color.valueOf("6d7885");
                    frontColor = Color.white;
                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.none;
                    ammoMultiplier = 4f;
                }},

                DelivaricaItems.sulfur, new BasicBulletType(5f, 15) {{
                    sprite = "bullet";
                    width = 8f;
                    height = 10f;
                    lifetime = 16f;
                    trailLength = 15;
                    trailWidth = 2f;
                    trailColor = Color.valueOf("dede6c");
                    backColor = Color.valueOf("8a7a20");
                    frontColor = Color.valueOf("e8d45a");
                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.none;
                    status = DelivaricaStatusEffects.inSulfur;
                    statusDuration = 60f * 8f;
                    ammoMultiplier = 3f;
                }},

                DelivaricaItems.lustrite, new BasicBulletType(5.5f, 25) {{
                    sprite = "bullet";
                    width = 8f;
                    height = 10f;
                    lifetime = 14.55f;
                    trailLength = 18;
                    trailWidth = 2.2f;
                    trailColor = Color.valueOf("8e94cc");
                    backColor = Color.valueOf("5a4a9a");
                    frontColor = Color.valueOf("c8cff2");
                    hitEffect = Fx.hitBulletColor;
                    despawnEffect = Fx.none;
                    reloadMultiplier = 0.7f;
                }}
            );
        }};
    }
}
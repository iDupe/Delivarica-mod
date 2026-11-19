package MainMod;

import arc.graphics.Color;
import arc.math.Interp;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.entities.bullet.BasicBulletType;

import mindustry.entities.part.RegionPart;
import mindustry.entities.pattern.ShootAlternate;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.power.ThermalGenerator;
import mindustry.world.draw.DrawMulti;
import mindustry.world.draw.DrawParticles;
import mindustry.world.draw.DrawRegion;
import mindustry.world.draw.DrawArcSmelt;
import mindustry.world.draw.DrawDefault;
import mindustry.world.meta.Attribute;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.draw.DrawTurret;
import mindustry.world.blocks.production.GenericCrafter; // ДОБАВЛЕНО

import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.distribution.BufferedItemBridge;
import mindustry.world.blocks.distribution.Duct;
import mindustry.world.blocks.distribution.Junction;
import mindustry.world.blocks.distribution.OverflowGate;
import mindustry.world.blocks.distribution.Router;
import mindustry.world.blocks.distribution.DuctRouter;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.Prop;
import mindustry.world.blocks.environment.StaticWall;
import mindustry.world.blocks.environment.SteamVent;
import mindustry.world.blocks.environment.TallBlock;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.payloads.Constructor;
import mindustry.world.blocks.payloads.PayloadConveyor;
import mindustry.world.blocks.payloads.PayloadRouter;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.units.Reconstructor;

    // РЕГИСТРАЦИЯ

public class DelivaricaBlocks {

    // 1. Рельеф и окружение
    public static Floor redSand, sandrock, obsidian, rindit, smoothRindit, drangite, whiteIce, letrite, letriteCrush, whiteSnow;
    public static StaticWall sandrockWall, obsidianWall, sandrockWallLarge, obsidianWallLarge, rinditWall, rinditWallLarge, drangiteWall, drangiteWallLarge, whiteIceWall, whiteIceWallLarge, letriteWall, letriteWallLarge,whiteSnowWall, whiteSnowWallLarge;
    public static TallBlock phosphoriteCubes, neotiteCluster, rinditColumn;
    public static SteamVent obsidianVent;
    public static Prop obsidianBoulder, rinditBoulder, sandrockBoulder, drangiteBoulder, letriteBoulder;

    // Руды
    public static OreBlock oreChromium, oreNickel;

    // Ядра
    public static CoreBlock coreReset, coreOff;

    // Стены
    public static Wall chromiumWall, chromiumWallBig;

    // Буры
    public static Drill mechanicalDrill;

    // Грузовые конвейеры
    public static PayloadConveyor inertialFreightConveyor;
    public static PayloadRouter inertialFreightRouter;

    // Сборщики
    public static Constructor basicComponentConstructor, basicCargoConstructor;
    public static Reconstructor basicUnitAmplifier;

    // Предментные
    public static Duct inertialDuct;
    public static Junction inertialJunction;
    public static Router inertialRouter;
    public static BufferedItemBridge inertialBridge;
    public static OverflowGate inertialOverflowGate;
    public static OverflowGate inertialUnderflowGate;

    // Жидкостные
    public static LiquidRouter liquiTankOne;

    // Энергетика
    public static ThermalGenerator mawTurbine;

    // Производство
    public static GenericCrafter ramtaniumSmelter, lustriteMixer; 

    // Турели
    public static ItemTurret range;

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

    // СВОЙСТВА

    public static void load() {
        // ОКРУЖЕНИЕ
        redSand = new Floor("redsand-floor"){{
            variants = 3;
            mapColor = Color.valueOf("6e4535");
        }};

        sandrock = new Floor("sandrock-floor"){{
            variants = 3;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockWall = new StaticWall("sandrock-wall"){{
            variants = 2;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockWallLarge = new StaticWall("sandrock-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("7d7065");
        }};
        sandrockBoulder = new Prop("sandrock-boulder"){{
            variants = 3;
        }};

        obsidian = new Floor("obsidian-floor"){{
            variants = 3;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianWall = new StaticWall("obsidian-wall"){{
            variants = 2;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianWallLarge = new StaticWall("obsidian-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianVent = new SteamVent("a-obsidian-vent"){{
            parent = blendGroup = obsidian;
            variants = 2;
            attributes.set(Attribute.steam, 1f);
            attributes.set(Attribute.heat, 1f);
            mapColor = Color.valueOf("2b2b33");
        }};
        obsidianBoulder = new Prop("obsidian-boulder"){{
            variants = 2;
        }};

        rindit = new Floor("rindit-floor"){{
            variants = 3;
            mapColor = Color.valueOf("5a5d6b");
        }};
        smoothRindit = new Floor("smooth-rindit-floor"){{
            variants = 3;
            mapColor = Color.valueOf("4e505c");
        }};
        rinditWall = new StaticWall("rindit-wall"){{
            variants = 2;
            mapColor = Color.valueOf("5a5d6b");
        }};
        rinditWallLarge = new StaticWall("rindit-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("5a5d6b");
        }};
        rinditBoulder = new Prop("rindit-boulder"){{
            variants = 3;
        }};

        phosphoriteCubes = new TallBlock("phosphorite-cubes"){{
            variants = 2;
            clipSize = 128f;
            solid = true;
            shadowOffset = -2.5f;
            mapColor = Color.valueOf("d4d4af");
        }};
        neotiteCluster = new TallBlock("neotite-cluster"){{
            variants = 2;
            clipSize = 128f;
            solid = true;
            shadowOffset = -2.5f;
            mapColor = Color.valueOf("404552");
        }};

        drangite = new Floor("drangite-floor"){{
            variants = 3;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteWall = new StaticWall("drangite-wall"){{
            variants = 2;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteWallLarge = new StaticWall("drangite-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("3c3845");
        }};
        drangiteBoulder = new Prop("drangite-boulder"){{
            variants = 3;
        }};

        whiteIce = new Floor("white-ice-floor"){{
            variants = 4;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteIceWall = new StaticWall("white-ice-wall"){{
            variants = 2;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteIceWallLarge = new StaticWall("white-ice-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("e2eaf0");
        }};

        letrite = new Floor("letrite-floor"){{
            variants = 4;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteCrush = new Floor("letrite-floor-crush"){{
            variants = 3;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteWall = new StaticWall("letrite-wall"){{
            variants = 2;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteWallLarge = new StaticWall("letrite-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("5c6963");
        }};
        letriteBoulder = new Prop("letrite-boulder"){{
            variants = 2;
        }};
        whiteSnow = new Floor("white-snow"){{
            variants = 3;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteSnowWall = new StaticWall("white-snow-wall"){{
            variants = 2;
            mapColor = Color.valueOf("e2eaf0");
        }};
        whiteSnowWallLarge = new StaticWall("white-snow-wall-large"){{
            variants = 1;
            mapColor = Color.valueOf("e2eaf0");
        }};

        rinditColumn = new TallBlock("rindit-column"){{
            variants = 2;
            clipSize = 128f;
        }};

        // РУДЫ
        oreChromium = new OreBlock("chromium-ore", DelivaricaItems.chromium){{
            variants = 3;
            oreDefault = true;
            oreThreshold = 0.81f;
            oreScale = 23.47f;
        }};

        oreNickel = new OreBlock("nickel-ore", DelivaricaItems.nickel){{
            variants = 3;
            oreDefault = true;
            oreThreshold = 0.83f;
            oreScale = 24.5f;
        }};

        // ЯДРА
        coreReset = new CoreBlock("coreReset"){{
            requirements(Category.effect, ItemStack.with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 4200;
            itemCapacity = 2000;
            size = 4;
            unitCapModifier = 8;
            drawTeamOverlay = true;
        }};

        coreOff = new CoreBlock("coreOff"){{
            requirements(Category.effect, ItemStack.with(Items.copper, 1000, Items.lead, 800));
            alwaysUnlocked = true;
            isFirstTier = true;
            unitType = UnitTypes.alpha;
            health = 2400;
            itemCapacity = 1000;
            size = 3;
            unitCapModifier = 4;
            drawTeamOverlay = true;
        }};

        // СТЕНЫ
        chromiumWall = new Wall("chromium-wall"){{
            requirements(Category.defense, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 600;
            size = 1;
        }};

        chromiumWallBig = new Wall("chromium-wall-big"){{
            requirements(Category.defense, ItemStack.with(DelivaricaItems.chromium, 24));
            health = 2400;
            size = 2;
        }};

        // БУРЫ
        mechanicalDrill = new Drill("mechanical-drill"){{
            requirements(Category.production, ItemStack.with(DelivaricaItems.chromium, 12));
            tier = 2;
            drillTime = 30;
            size = 2;
            drawMineItem = true;
            consumeLiquid(Liquids.water, 0.05f).boost();
        }};

        // ГРУЗОВЫЕ КОНВЕЙЕРЫ
        inertialFreightConveyor = new PayloadConveyor("inertial-freight-conveyor"){{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 10));
            canOverdrive = false;
            size = 3;
            moveTime = 45f;
            payloadLimit = 3f;
        }};

        inertialFreightRouter = new PayloadRouter("inertial-freight-router"){{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 15));
            canOverdrive = false;
            size = 3;
            moveTime = 45f;
            payloadLimit = 3f;
        }};

        // СБОРЩИКИ
        basicComponentConstructor = new Constructor("basic-component-constructor"){{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 40));
            hasPower = true;
            consumePower(1.5f);
            size = 3;
            maxBlockSize = 2;
            buildSpeed = 0.6f;
            filter = Seq.with(chromiumWall, chromiumWallBig);
        }};

        basicCargoConstructor = new Constructor("basic-cargo-constructor"){{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 80));
            hasPower = true;
            consumePower(3f);
            size = 3;
            maxBlockSize = 3;
            buildSpeed = 0.35f;
            filter = Seq.with(mechanicalDrill);
        }};

        basicUnitAmplifier = new Reconstructor("basic-unit-amplifier"){{
            requirements(Category.units, ItemStack.with(DelivaricaItems.chromium, 80));
            size = 3;
            consumePower(2f);
            consumeItems(ItemStack.with(DelivaricaItems.ramtanium, 20));
            constructTime = 60f * 10f;

            upgrades.addAll(
                new UnitType[]{DelivaricaUnits.photon, DelivaricaUnits.photonArmored},
                new UnitType[]{DelivaricaUnits.bunch, DelivaricaUnits.bunchArmored}
            );
        }};

        // ЭНЕРГЕТИКА
        mawTurbine = new ThermalGenerator("vent-turbine"){{
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

        // ПРОИЗВОДСТВО
        ramtaniumSmelter = new GenericCrafter("ramtanium-smelter"){{
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

        lustriteMixer = new GenericCrafter("lustrite-mixer"){{
            requirements(Category.crafting, ItemStack.with(DelivaricaItems.chromium, 60, DelivaricaItems.nickel, 40));
            size = 3;
            health = 1000;
            craftTime = 120f;
            hasPower = true;
            consumePower(1.2f);

            drawer = new DrawMulti(new DrawRegion("-bottom"),
            new DrawParticles(){{
                color = Color.valueOf("a0abe3");
                alpha = 0.5f;
                particleSize = 3f;
                particles = 10;
                particleRad = 9f;
                particleLife = 200f;
                reverse = true;
                particleSizeInterp = Interp.one;
            }},
            new DrawRegion("-rotator") {{spinSprite = true; rotateSpeed = 4f;}},
            new DrawDefault()
            );

            consumeItems(ItemStack.with(DelivaricaItems.ramtanium, 1, DelivaricaItems.nickel, 2));
            outputItem = new ItemStack(DelivaricaItems.lustrite, 1);
        }};

        // ПРЕДМЕТНЫЕ
        inertialJunction = new Junction("inertial-junction"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 4));
            health = 90;
            speed = 26f;
            capacity = 6;
        }};

        inertialBridge = new BufferedItemBridge("inertial-bridge"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 12));
            fadeIn = moveArrows = false;
            health = 90;
            range = 4;
            speed = 74f;
            arrowSpacing = 6f;
            bufferCapacity = 14;
            crushFragile = true;
        }};

        inertialDuct = new Duct("inertial-duct"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 1));
            health = 90;
            speed = 4f;
            junctionReplacement = inertialJunction;
            bridgeReplacement = inertialBridge;
        }};

        inertialRouter = new Router("inertial-router"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 4));
            health = 90;
            speed = 8f;
        }};

        inertialOverflowGate = new OverflowGate("inertial-in"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 90;
            speed = 1f;
        }};

        inertialUnderflowGate = new OverflowGate("inertial-out"){{
            requirements(Category.distribution, ItemStack.with(DelivaricaItems.chromium, 6));
            health = 90;
            speed = 1f;
            invert = true;
        }};

        // ЖИДКОСТНЫЕ

        // ТУРЕЛИ
        range = new ItemTurret("range"){{
            requirements(Category.turret, ItemStack.with(
                DelivaricaItems.chromium, 45
            ));
            size = 2;
            health = 720;
            range = 190f;
            reload = 35f;
            recoil = 1.5f;
            recoils = 2;
            rotateSpeed = 6f;
            shootSound = Sounds.shoot;

            shoot = new ShootAlternate(){{
                spread = 3.5f;
            }};

            drawer = new DrawTurret("delivarica-"){{
                parts.add(
                    new RegionPart("-barrel-l"){{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 0;
                    }},
                    new RegionPart("-barrel-r"){{
                        progress = PartProgress.recoil;
                        under = true;
                        moveY = -2.5f;
                        recoilIndex = 1;
                    }}
                );
            }};

            ammo(
                DelivaricaItems.chromium, new BasicBulletType(5f, 28){{
                    width = 9f;
                    height = 12f;
                    lifetime = 40f;
                    ammoMultiplier = 3;
                }}
            );
        }};
    };
}
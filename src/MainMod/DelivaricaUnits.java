package MainMod;


import arc.math.Mathf;
import arc.util.Time;
import mindustry.ai.types.BuilderAI;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Sounds;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import arc.Events;
import arc.graphics.Color;
import mindustry.ai.types.BuilderAI;
import mindustry.ai.types.GroundAI;
import mindustry.content.Fx;
import mindustry.entities.bullet.ArtilleryBulletType;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.entities.bullet.BulletType;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.gen.MechUnit;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

public class DelivaricaUnits {
    // Добавили bunchArmored в список
    public static UnitType side, photon, photonArmored, bunch, bunchArmored;

    public static void load() {

        // Side
        // Side
        side = new UnitType("side"){{
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

            // Характеристики как у Beta
            buildSpeed = 0.75f;
            mineSpeed = 7f;
            mineTier = 2;
            itemCapacity = 40;

            // Встроенный щит как у Quasar, но управляемый через ЛКМ
            // Параметры: радиус 24f (6x6 клеток), реген 0.5f/тик, прочность 1000f, перезарядка при поломке 3 сек (180f)
            abilities.add(new ForceFieldAbility(24f, 0.5f, 1000f, 180f){{
                sides = 36; // Делаем щит круглым (36 граней вместо 6-угольника; если нужен 6-угольник — уберите эту строку)
            }
                @Override
                public void update(Unit unit) {
                    boolean active = unit.isShooting && !unit.isBuilding() && !unit.mining();

                    if (active) {
                        // Запускаем стандартную логику (резкое раскрытие и поглощение пуль)
                        super.update(unit);
                    } else {
                        // Если ЛКМ отпущена — медленно сворачиваем щит в 0
                        radiusScale = Mathf.lerpDelta(radiusScale, 0f, 0.05f);

                        // Восстанавливаем щит и тикаем кулдаун поломки в фоне
                        if (unit.shield < max) {
                            unit.shield = Math.min(max, unit.shield + regen * Time.delta);
                        }
                        if (alpha > 0f) {
                            alpha = Math.max(0f, alpha - Time.delta / 10f);
                        }
                    }
                }
            });

            // Невидимое оружие-триггер, чтобы при зажатии ЛКМ включался флаг unit.isShooting
            weapons.add(new Weapon(){{
                x = 0f;
                y = 0f;
                mirror = false;
                reload = 1f;
                shootSound = Sounds.none;
                shootCone = 360f;

                bullet = new BulletType(0f, 0f){{
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


        // Photon
        photon = new UnitType("photon"){{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.60f; // Скорость понижена (было 0.75f)
            hitSize = 11f;
            health = 420f;
            armor = 2f;
            rotateSpeed = 4f;
            outlineColor = Color.valueOf("36363c");

            mechStride = 4.2f;
            mechFrontSway = 0.25f;
            mechSideSway = 0.35f;
            mechStepParticles = true;

            weapons.add(new Weapon(name + "-w"){{
                reload = 35f;
                x = 4.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing1;

                bullet = new ArtilleryBulletType(3f, 12, "shell"){{ // Урон понижен до 12
                    hitEffect = Fx.flakExplosion;
                    knockback = 0.6f;
                    lifetime = 48f; // 3 * 48 = 144f (ровно 18 блоков)
                    width = height = 6.5f;
                    collides = true;
                    collidesTiles = true;
                    splashDamageRadius = 14f;
                    splashDamage = 14f; // Урон по площади понижен до 14
                    homingPower = 0.08f;
                    homingRange = 50f;
                    backColor = Pal.gray;
                    frontColor = Color.lightGray;
                    trailColor = Pal.gray;
                }};
            }});
        }};

        // Armored photon
        photonArmored = new UnitType("photon-armored"){{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.48f; // Скорость понижена (было 0.60f)
            hitSize = 11f;
            health = 620f; // 420 + 200 HP
            armor = 5f;
            rotateSpeed = 4f;
            outlineColor = Color.valueOf("36363c");

            mechStride = 4.2f;
            mechFrontSway = 0.25f;
            mechSideSway = 0.35f;
            mechStepParticles = true;

            weapons.add(new Weapon(photon.name + "-w"){{
                reload = 35f;
                x = 4.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing1;

                bullet = new ArtilleryBulletType(3f, 12, "shell"){{ // Урон понижен до 12
                    hitEffect = Fx.flakExplosion;
                    knockback = 0.6f;
                    lifetime = 48f; // 3 * 48 = 144f (ровно 18 блоков)
                    width = height = 6.5f;
                    collides = true;
                    collidesTiles = true;
                    splashDamageRadius = 14f;
                    splashDamage = 14f; // Урон по площади понижен до 14
                    homingPower = 0.08f;
                    homingRange = 50f;
                    backColor = Pal.gray;
                    frontColor = Color.lightGray;
                    trailColor = Pal.gray;
                }};
            }});
        }};

        // Bunch
        bunch = new UnitType("bunch"){{
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

            // Пушка от обычного банча
            weapons.add(new Weapon(this.name + "-w"){{
                reload = 60f; // 1 выстрел в секунду 
                
                x = 6.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing2;

                // НАСТРОЙКИ ДРОБИ
                inaccuracy = 12f;       // Разброс
                shoot.shots = 5;        // Количество дробинок
                shoot.shotDelay = 0f;   

                bullet = new BasicBulletType(5f, 25){{ 
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

        // Armored Bunch (Твой новый юнит)
        bunchArmored = new UnitType("bunch-armored"){{
            constructor = MechUnit::create;
            aiController = GroundAI::new;

            flying = false;
            speed = 0.45f;       // Чуть медленнее обычного (было 0.55f)
            hitSize = 12f;       // Такой же размер
            health = 1350f;      // Значительно больше ХП (было 950f)
            armor = 10f;         // Толще броня (было 6f)
            rotateSpeed = 2.5f;  // Медленнее поворачивается (было 3f)
            outlineColor = Color.valueOf("36363c");

            mechStride = 5.5f;
            mechFrontSway = 0.3f;
            mechSideSway = 0.4f;
            mechStepParticles = true;

            // Используем пушку от обычного банча
            weapons.add(new Weapon(bunch.name + "-w"){{
                reload = 60f; // 1 выстрел в секунду (60 тиков)
                
                // Твои родные координаты и слои (НЕ ТРОГАЕМ)
                x = 6.5f;
                y = 0f;
                top = false;
                layerOffset = -0.01f;
                mirror = true;
                alternate = true;
                rotate = false;
                ejectEffect = Fx.casing2;

                // --- НАСТРОЙКИ ДРОБИ ---
                inaccuracy = 12f;       // Разброс (пишется напрямую)
                shoot.shots = 5;        // Количество дробинок (пишется через shoot)
                shoot.shotDelay = 0f;   // Стрелять всеми разом

                bullet = new BasicBulletType(5f, 25){{ 
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

        // Подтягиваем ноги/базу/ячейки для армированных версий
        Events.on(ClientLoadEvent.class, e -> {
            // Для фотона
            photonArmored.legRegion = photon.legRegion;
            photonArmored.baseRegion = photon.baseRegion;
            photonArmored.cellRegion = photon.cellRegion;
            
            // Для банча
            bunchArmored.legRegion = bunch.legRegion;
            bunchArmored.baseRegion = bunch.baseRegion;
            bunchArmored.cellRegion = bunch.cellRegion;
        });
    }
}
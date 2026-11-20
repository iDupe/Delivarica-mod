package MainMod;

import arc.struct.Seq;
import mindustry.game.Objectives.Produce;
import mindustry.game.Objectives.Research;
import mindustry.game.Objectives.SectorComplete;

import static MainMod.DelivaricaBlocks.*;
import static MainMod.DelivaricaItems.*;
import static MainMod.DelivaricaUnits.*;
import static MainMod.Planet.Delivia.*;
import static mindustry.content.TechTree.*;

public class DelivaricaTechTree {

    public static void load() {
        Delivarica.delivia.techTree = nodeRoot("delivia", coreReset, () -> {

            // 1. Логистика (сначала перекрёсток, затем роутер) | Distribution (junction first, then router)
            node(inertialDuct, () -> {
                // Ветка 1: перекрёсток -> одновременно шлюз и затвор | Branch 1
                node(inertialJunction, () -> {
                    node(inertialOverflowGate);
                    node(inertialUnderflowGate);
                });

                // Ветка 2: маршрутизатор -> мост и грузовые конвейеры | Branch 2
                node(inertialRouter, () -> {
                    node(inertialBridge);

                    node(inertialFreightConveyor, Seq.with(new Research(groundConstructor)), () -> {
                        node(inertialFreightRouter);
                    });
                });
            });

            // 2. Добыча | Drills
            node(mechanicalDrill);

            // 3. Производство | Crafting
            node(ramtaniumSmelter, () -> {
                node(lustriteMixer);
            });

            // 4. Энергетика | Power
            node(mawTurbine, () -> {
                node(xNode);
            });

            // 5. Оборона: отдельно стены, отдельно турели | Defense: walls and turrets separated
            node(chromiumWall, () -> {
                node(chromiumWallBig);
            });

            node(range, () -> {
                node(hawk);
                node(concussion, Seq.with(new Research(sulfur)), () -> {});
            });

            // 6. Второе ядро | Secondary core
            node(coreOff);

            // 7. Юниты и конструкторы | Units and constructors
            node(basicCargoConstructor, () -> {
                node(basicComponentConstructor, () -> {
                    // Ветка апгрейдеров брони | Armor upgraders branch
                    node(basicUnitAmplifier, Seq.with(new Produce(ramtanium)), () -> {});

                    // Ветка реконструкторов тира юнитов (заглушка под будущий блок) | Tier reconstructors branch (placeholder)
                    // node(tier1Reconstructor, () -> { ... });

                    // Ветви конструкторов юнитов (наземный; позже — пауковый и летучий) | Unit factories (ground; spider & air later)
                    node(groundConstructor, () -> {
                        node(photon, () -> {
                            // Требуется первый реконструктор тира (добавить Research(tier1Reconstructor) после создания) | Requires tier-1 reconstructor
                            node(bunch, Seq.with(new SectorComplete(impact0039)), () -> {});
                        });
                    });
                });
            });

            // 8. Сектора | Sectors
            node(impact0039);

            // 9. Ресурсы и сплавы | Resources and alloys
            nodeProduce(chromium, () -> {
                nodeProduce(nickel, () -> {
                    nodeProduce(ramtanium, () -> {
                        nodeProduce(lustrite, () -> {});
                    });
                });

                nodeProduce(sulfur, () -> {});
                nodeProduce(tantalum, () -> {});
                nodeProduce(phosphore, () -> {});
            });
        });
    }
}
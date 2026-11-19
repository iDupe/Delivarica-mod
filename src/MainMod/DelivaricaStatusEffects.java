package MainMod;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import mindustry.content.Fx;
import mindustry.content.StatusEffects;
import mindustry.entities.Effect;
import mindustry.type.StatusEffect;

public class DelivaricaStatusEffects {
    public static StatusEffect inSulfur;

    public static void load() {
        inSulfur = new StatusEffect("in-sulfur"){{
            color = Color.valueOf("dede6c");
            speedMultiplier = 0.9f;

            effect = new Effect(42f, e -> {
                Draw.color(Color.valueOf("dede6c"));
                e.scaled(20f, s -> {
                    Fill.circle(e.x, e.y, 1.5f * s.fout());
                });
                Fill.circle(e.x + Mathf.sin(e.id * 10f) * 2f * e.fin(), e.y - 3f * e.fin(), 1.2f * e.fout());
            });
            effectChance = 0.15f;

            applyEffect = Fx.hitFlameSmall;
            applyColor = Color.valueOf("dede6c");

            affinity(StatusEffects.burning, (unit, result, time) -> {
                unit.damagePierce(transitionDamage);
                Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                result.set(StatusEffects.burning, Math.min(time + result.time, 300f));
            });

            affinity(StatusEffects.melting, (unit, result, time) -> {
                unit.damagePierce(transitionDamage);
                Fx.burning.at(unit.x + Mathf.range(unit.bounds() / 2f), unit.y + Mathf.range(unit.bounds() / 2f));
                result.set(StatusEffects.melting, Math.min(time + result.time, 300f));
            });
        }};
    }
}

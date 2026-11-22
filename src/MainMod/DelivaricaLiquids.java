package MainMod;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class DelivaricaLiquids {
    // Жидкости мода | Mod liquids
    public static Liquid bromiumAcid;

    public static void load() {
        bromiumAcid = new Liquid("bromium", Color.valueOf("c47856")) {{
            viscosity = 0.65f;
            flammability = 0f;
            explosiveness = 0f;
            heatCapacity = 0.4f;
            temperature = 0.55f;
            boilPoint = 0.6f;
            effect = DelivaricaStatusEffects.inSulfur; // Можешь заменить на свой эффект коррозии/кислоты | Replace with acid status if needed
            gasColor = Color.valueOf("d99b7e");
        }};
    }
}

package MainMod;

import arc.graphics.Color;
import mindustry.type.Item;

public class DelivaricaItems {

    // Сырьё и минералы | Raw resources and minerals
    public static Item chromium, sulfur, nickel, tantalum, phosphore;

    // Сплавы и композиты | Alloys and composites
    public static Item ramtanium, lustrite;

    public static void load() {
        // Сырьё и минералы | Raw resources and minerals
        chromium = new Item("chromium", Color.valueOf("948f99")) {{
            localizedName = "Хром";
            hardness = 2;
            cost = 1.5f;
        }};

        sulfur = new Item("sulfur", Color.valueOf("dede6c")) {{
            localizedName = "Сера";
            flammability = 0.8f;
            explosiveness = 0.4f;
            hardness = 1;
            cost = 0.5f;
        }};

        nickel = new Item("nickel", Color.valueOf("6d7885")) {{
            localizedName = "Никель";
            charge = 0.1f;
            hardness = 2;
            cost = 1.0f;
        }};

        tantalum = new Item("tantalum", Color.valueOf("594f5e")) {{
            localizedName = "Тантал";
            hardness = 3;
            cost = 2.5f;
        }};

        phosphore = new Item("phosphore", Color.valueOf("d1ca8a")) {{
            localizedName = "Фосфор";
            hardness = 2;
            cost = 1.5f;
        }};

        // Сплавы и композиты | Alloys and composites
        ramtanium = new Item("ramtanium", Color.valueOf("b8c6ae")) {{
            localizedName = "Рамтан";
            hardness = 2;
            cost = 2f;
        }};

        lustrite = new Item("lustrite", Color.valueOf("8e94cc")) {{
            localizedName = "Люстрит";
            charge = 0.25f;
            hardness = 3;
            cost = 2.5f;
        }};
    }
}
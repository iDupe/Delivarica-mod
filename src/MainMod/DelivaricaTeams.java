package MainMod;

import arc.graphics.Color;
import mindustry.game.Team;
import mindustry.type.TeamEntry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelivaricaTeams {

    // Новая фракция: Порочные (ID 78) | New faction: Vicious (ID 78)
    public static Team vicious;
    public static TeamEntry viciousEntry;

    public static void load() {
        vicious = Team.get(78);
        vicious.name = "vicious";

        vicious.color.set(Color.valueOf("38d9c8"));

        Color[] colors = {
            Color.valueOf("82fff2"),
            Color.valueOf("38d9c8"),
            Color.valueOf("1d8f83")
        };
        System.arraycopy(colors, 0, vicious.palette, 0, 3);


        for (int i = 0; i < 3; i++) {
            vicious.palettei[i] = vicious.palette[i].rgba();
        }

        viciousEntry = new TeamEntry(vicious);

        addTeamToEditor();
    }

    public static void addTeamToEditor() {
        try {
            Team[] team7 = new Team[Team.baseTeams.length + 1];
            System.arraycopy(Team.baseTeams, 0, team7, 0, Team.baseTeams.length);
            team7[Team.baseTeams.length] = vicious;

            Field field = Team.class.getDeclaredField("baseTeams");
            field.setAccessible(true);

            try {
                // Путь для Android (ART): обход Hidden API через двойную рефлексию | Android (ART) hidden API bypass
                Method getDeclaredField = Class.class.getDeclaredMethod("getDeclaredField", String.class);
                Field accessFlags = (Field) getDeclaredField.invoke(Field.class, "accessFlags");
                accessFlags.setAccessible(true);
                accessFlags.setInt(field, field.getInt(field) & ~Modifier.FINAL);
                field.set(null, team7);
            } catch (Exception desktopFallback) {
                // Путь для ПК (HotSpot JVM) через Unsafe | Desktop (HotSpot JVM) Unsafe path
                Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
                Field theUnsafe = unsafeClass.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Object unsafe = theUnsafe.get(null);

                Object base = unsafeClass.getMethod("staticFieldBase", Field.class).invoke(unsafe, field);
                long offset = (long) unsafeClass.getMethod("staticFieldOffset", Field.class).invoke(unsafe, field);
                unsafeClass.getMethod("putObject", Object.class, long.class, Object.class).invoke(unsafe, base, offset, team7);
            }
        } catch (Throwable ignored) {
        }
    }
}

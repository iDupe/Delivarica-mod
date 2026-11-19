package MainMod;

import arc.graphics.Color;
import mindustry.game.Team;
import mindustry.type.TeamEntry;

public class DelivaricaTeams {
    public static Team vicious;
    public static TeamEntry viciousEntry;

    public static void load() {
        vicious = Team.get(78);
        vicious.name = "vicious";

        vicious.color.set(Color.valueOf("38d9c8"));

        vicious.palette[0] = Color.valueOf("82fff2");
        vicious.palette[1] = Color.valueOf("38d9c8");
        vicious.palette[2] = Color.valueOf("1d8f83");

        for (int i = 0; i < 3; i++) {
            vicious.palettei[i] = vicious.palette[i].rgba();
        }

        viciousEntry = new TeamEntry(vicious);

        addTeamToEditor();
    }


    public static void addTeamToEditor() {
        try {
            Team[] TEAM7 = new Team[Team.baseTeams.length + 1];
            System.arraycopy(Team.baseTeams, 0, TEAM7, 0, Team.baseTeams.length);
            TEAM7[6] = vicious;

            java.lang.reflect.Field field = Team.class.getDeclaredField("baseTeams");
            sun.misc.Unsafe unsafe = getUnsafe();
            unsafe.putObject(
                unsafe.staticFieldBase(field),
                unsafe.staticFieldOffset(field),
                TEAM7
            );
        } catch (Exception e) {/*...*/}
    }

    private static sun.misc.Unsafe getUnsafe() throws Exception {
        java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (sun.misc.Unsafe) f.get(null);
    }
}

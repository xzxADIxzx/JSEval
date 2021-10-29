package jseval;

import arc.util.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.mod.*;

import static mindustry.Vars.*;

@SuppressWarnings("unused")
public class JSEval extends Plugin {

    private Seq<Player> excep = new Seq<>();

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("js", "<code...>", "Execute JavaScript code.", (args, player) -> {
            if (player.admin) {
                String output = mods.getScripts().runConsole(args[0]);
                if (!isExcep(player)) player.sendMessage("> " + (isError(output) ? "[#ff341c]" + output : output));
            } else {
                player.sendMessage("[scarlet]You must be admin to use this command.");
            }
        });

        handler.<Player>register("return", "Toggle return from /js command", (args, player) -> {
            if (player.admin) {
                if (isExcep(player)){
                    player.sendMessage("[green]Return enabled");
                    excep.remove(player);
                } else {
                    player.sendMessage("[scarlet]Return disabled");
                    excep.add(player);
                }
            } else {
                player.sendMessage("[scarlet]You must be admin to use this command.");
            }
        });
    }

    private boolean isError(String output) {
        try {
            String errorName = output.substring(0, output.indexOf(' ') - 1);
            Class.forName("org.mozilla.javascript." + errorName);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private boolean isExcep(Player player){
        return excep.contains(player);
    }
}

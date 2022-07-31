package jseval;

import arc.struct.Seq;
import arc.util.CommandHandler;
import mindustry.gen.Player;
import mindustry.mod.Plugin;

import static mindustry.Vars.*;

public class JSEval extends Plugin {

    public Seq<String> except = new Seq<>();

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("js", "<code...>", "Execute JavaScript code.", (args, player) -> {
            if (player.admin) {
                String output = mods.getScripts().runConsole(args[0]);
                if (!isExcepted(player)) player.sendMessage("> " + (isError(output) ? "[#ff341c]" + output : output));
            } else player.sendMessage("[scarlet]You must be admin to use this command.");
        });

        handler.<Player>register("return", "Toggle return from /js command.", (args, player) -> {
            if (player.admin) {
                if (isExcepted(player)) {
                    player.sendMessage("[green]Return enabled.");
                    except.remove(player.uuid());
                } else {
                    player.sendMessage("[scarlet]Return disabled.");
                    except.add(player.uuid());
                }
            } else player.sendMessage("[scarlet]You must be admin to use this command.");
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

    private boolean isExcepted(Player player) {
        return except.contains(player.uuid());
    }
}

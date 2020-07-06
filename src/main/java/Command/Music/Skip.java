package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;

import java.util.Arrays;
import java.util.List;

public class Skip implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager manager = PlayerManager.getPlayerManager();
        manager.skipSong(context);

    }

    @Override
    public List<String> getCalls() {
        return Arrays.asList("skip", "s");
    }
}

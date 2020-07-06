package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;

import java.util.Collections;
import java.util.List;

public class Pause implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager manager = PlayerManager.getPlayerManager();
        manager.pausePlayer(context.getGuild());

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("pause");
    }
}

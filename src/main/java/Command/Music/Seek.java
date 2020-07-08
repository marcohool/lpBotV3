package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;

import java.util.Collections;
import java.util.List;

public class Seek implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager manager = PlayerManager.getPlayerManager();
        manager.seekTrack(context);

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("seek");
    }
}

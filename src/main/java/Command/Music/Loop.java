package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;

import java.util.Collections;
import java.util.List;

public class Loop implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager.getPlayerManager().loopTrack(context.getChannel());

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("loop");
    }
}

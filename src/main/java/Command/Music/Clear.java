package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;

import java.util.Collections;
import java.util.List;

public class Clear implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager manager = PlayerManager.getPlayerManager();
        manager.clearQueue(context);
        context.getChannel().sendMessage("CLEARED !").queue();

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("clear");
    }
}

package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import Music.PlayerManager;
import java.util.Arrays;
import java.util.List;

public class Play implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        PlayerManager manager = PlayerManager.getPlayerManager();

        try {

            String requestedSong = context.getMessage().getContentRaw().substring(8);

            boolean joined = Join.joinChannel(context.getMember(), context, context.getChannel());

            if (!joined){
                return;
            }

            manager.playTrack(context, requestedSong);

        } catch (StringIndexOutOfBoundsException e){
            context.getChannel().sendMessage("play what kid").queue();
        }

    }


    @Override
    public List<String> getCalls() {
        return Arrays.asList("play", "p");
    }
}

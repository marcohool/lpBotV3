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

            boolean joined = Join.joinChannel(context.getMember(), context, context.getChannel());

            if (!joined){
                return;
            }

            String raw = context.getMessage().getContentRaw();

            String[] rawSplit = raw.split(" ", 3);
            String requestedSong = rawSplit[2];
            System.out.println(requestedSong);

            manager.playTrack(context, requestedSong);

        } catch (ArrayIndexOutOfBoundsException e){
            context.getChannel().sendMessage("play what kid").queue();
        }

    }

    @Override
    public List<String> getCalls() {
        return Arrays.asList("play", "p");
    }
}

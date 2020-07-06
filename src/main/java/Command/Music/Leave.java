package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collections;
import java.util.List;

public class Leave implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        AudioManager audioManager = context.getGuild().getAudioManager();

        if (!audioManager.isConnected()) {
            context.getChannel().sendMessage("the fuck you want me to leave dumbass ?").queue();
            return;
        }

        if (!audioManager.getConnectedChannel().getMembers().contains(context.getMember())){
            context.getChannel().sendMessage("dont tell me what to do if ur not even in the channel :rofl:").queue();
            return;
        }

        context.getChannel().sendMessage("cya kid").queue();
        audioManager.closeAudioConnection();

    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("leave");
    }
}

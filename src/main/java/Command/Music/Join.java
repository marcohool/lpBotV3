package Command.Music;

import Command.CommandContext;
import Command.CommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.nio.channels.Channel;
import java.util.Collections;
import java.util.List;

public class Join implements CommandInterface {

    @Override
    public void handle(CommandContext context) {

        Member member = context.getGuild().getMember(context.getAuthor());

        joinChannel(member, context, context.getChannel());

    }

    public static boolean joinChannel(Member member, CommandContext context, TextChannel textChannel){

        AudioManager audioManager = context.getGuild().getAudioManager();

        if (!member.getVoiceState().inVoiceChannel()){
            textChannel.sendMessage("are u gonna join a channel or what").queue();
            return false;
        }

        if (member.getVoiceState().getChannel().equals(audioManager.getConnectedChannel())) {
            if (context.getMessage().getContentRaw().endsWith("play")){
                textChannel.sendMessage("im already in the channel cat").queue();
                return true;
            }
            return true;
        }

        audioManager.openAudioConnection(member.getVoiceState().getChannel());

        return true;
    }

    @Override
    public List<String> getCalls() {
        return Collections.singletonList("join");
    }
}

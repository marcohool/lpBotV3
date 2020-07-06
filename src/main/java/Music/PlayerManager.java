package Music;

import Command.CommandContext;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager playerManager;
    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;


    private PlayerManager() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        GuildMusicManager guildMusicManager = musicManagers.get(guild.getIdLong());

        if (guildMusicManager == null) {
            guildMusicManager = new GuildMusicManager(audioPlayerManager);
            musicManagers.put(guild.getIdLong(), guildMusicManager);
        }

        guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

        return guildMusicManager;
    }

    public void playTrack(CommandContext context, String requestedSong) {

        TextChannel textChannel = context.getChannel();
        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());

        try {
            new URL(requestedSong);
        } catch (MalformedURLException e) {
            requestedSong = "ytsearch:"+requestedSong;
        }

        audioPlayerManager.loadItemOrdered(musicManager, requestedSong, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                textChannel.sendMessage("Adding to queue: " + track.getInfo().title).queue();
                musicManager.scheduler.queue(track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
            }

            @Override
            public void noMatches() {
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            }
        });
    }

    public static synchronized PlayerManager getPlayerManager() {

        if (playerManager == null) {
            playerManager = new PlayerManager();
        }

        return playerManager;

    }
}

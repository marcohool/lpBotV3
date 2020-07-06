package Music;

import Command.CommandContext;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
            requestedSong = "ytsearch: "+requestedSong;
        }

        audioPlayerManager.loadItemOrdered(musicManager, requestedSong, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                textChannel.sendMessage("Adding to queue: " + track.getInfo().title).queue();
                musicManager.scheduler.queue(track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                textChannel.sendMessage("Adding to queue: " + firstTrack.getInfo().title ).queue();

                musicManager.scheduler.queue(firstTrack);
            }

            @Override
            public void noMatches() {
                textChannel.sendMessage("Song doesnt exist :rofl:").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("Load failed");
                exception.printStackTrace();
            }
        });
    }

    public void pausePlayer(CommandContext context) {

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());
        AudioPlayer audioPlayer = musicManager.getPlayer();

        if (audioPlayer.isPaused()){
            context.getChannel().sendMessage("The player is already paused !").queue();
            return;
        }

        context.getChannel().sendMessage("PAUSED !").queue();

        musicManager.getPlayer().setPaused(true);

    }

    public void unpausePlayer(CommandContext context) {

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());
        AudioPlayer audioPlayer = musicManager.getPlayer();

        if (!audioPlayer.isPaused()){
            context.getChannel().sendMessage("The player isnt paused !").queue();
            return;
        }

        context.getChannel().sendMessage("UNPAUSED !").queue();

        musicManager.getPlayer().setPaused(false);

    }

    public static synchronized PlayerManager getPlayerManager() {

        if (playerManager == null) {
            playerManager = new PlayerManager();
        }

        return playerManager;

    }
}

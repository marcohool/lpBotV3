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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

                displaySongAsEmbed(track.getInfo().title, textChannel);
                musicManager.scheduler.queue(track);

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                displaySongAsEmbed(firstTrack.getInfo().title,textChannel);

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

    public void nowPlaying(CommandContext context) {

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());

        if (musicManager.getPlayer().getPlayingTrack() == null){
            context.getChannel().sendMessage("Nothing is playing").queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Now Playing")
                .setDescription(musicManager.getPlayer().getPlayingTrack().getInfo().title)
                .setColor(new Color(54, 57, 63));
        context.getChannel().sendMessage(builder.build()).queue();

    }

    private void displaySongAsEmbed(String message, TextChannel channel){

        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());
        String author = "Track Queued";

        if (musicManager.getPlayer().getPlayingTrack() == null){
            author = "Currently Playing";
        }

        EmbedBuilder builder = new EmbedBuilder().setAuthor(author)
                .setDescription(message)
                .setColor(new Color(54, 57, 63));
        channel.sendMessage(builder.build()).queue();

    }

    public void clearQueue(CommandContext context) {

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());
        LinkedList<AudioTrack> queue = musicManager.scheduler.getQueue();

        queue.clear();
        musicManager.scheduler.nextTrack();

    }

    public void displayQueue(CommandContext context){

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());
        LinkedList<AudioTrack> queue = musicManager.scheduler.getQueue();

        String currentTrack = "";

        try {
            currentTrack = "1. " + musicManager.getPlayer().getPlayingTrack().getInfo().title;
        } catch (NullPointerException e){
            context.getChannel().sendMessage("Queue is empty dumbass").queue();
            return;
        }

        EmbedBuilder builder = new EmbedBuilder().setAuthor("Queue")
                .addField("", currentTrack, false)
                .setColor(new Color(54, 57, 63));


        for (int i = 0; i < queue.size(); i++){
            builder.addField("", (i+2)+". "+queue.get(i).getInfo().title, false);
        }

        context.getChannel().sendMessage(builder.build()).queue();

    }

    public void skipSong(CommandContext context){

        GuildMusicManager musicManager = getGuildMusicManager(context.getGuild());
        musicManager.scheduler.nextTrack();

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

package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.LinkedList;


public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;
    private boolean loop;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList();
        this.loop = false;
    }

    public void loop(TextChannel channel) {
        if (this.loop == true){
            this.loop = false;
            channel.sendMessage("loop is now DISABLED ! :flushed:").queue();
            return;
        }
        this.loop = true;
        channel.sendMessage("loop is now ENABLED ! :flushed:").queue();


    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)){
            queue.offer(track);
        }
    }

    public void nextTrack() {
        if (this.loop == false) {
            player.startTrack(queue.poll(), false);
            return;
        }
        AudioTrack track = player.getPlayingTrack();
        queue.add(track);
        player.startTrack(queue.poll(), false);
    }

    public LinkedList<AudioTrack> getQueue() {
        return queue;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

}

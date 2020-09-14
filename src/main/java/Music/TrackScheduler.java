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
    private AudioTrack lastTrack;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList();
        this.loop = false;
        this.lastTrack = null;
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
        System.out.println("test2");
        if (!this.loop) {
            player.startTrack(queue.poll(), false);
            return;
        }
        AudioTrack playingTrack = player.getPlayingTrack();
        if (playingTrack != null){
            this.lastTrack = playingTrack;
        }
        queue.add(this.lastTrack);
        player.startTrack(queue.poll().makeClone(), false);
    }

    public LinkedList<AudioTrack> getQueue() {
        return queue;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("test");
        if (endReason.mayStartNext) {
            System.out.println("test3");
            this.lastTrack = track;
            nextTrack();
        }
    }

}

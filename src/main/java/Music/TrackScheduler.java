package Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import java.util.LinkedList;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final LinkedList<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)){
            queue.offer(track);
        }
    }

    public AudioTrack getNextSong() {
        for (AudioTrack audioTrack : queue) {
            return audioTrack;
        }
        return null;
    }

}

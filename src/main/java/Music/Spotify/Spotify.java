package Music.Spotify;

import Config.Config;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class Spotify {

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("33ec57952491483ab46702a08b2f2e68")
            .setClientSecret(Config.getClientSecret("CLIENTSECRET"))
            .build();

    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static String getTrackName(String id){

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());

        }

        GetTrackRequest getTrackRequest = spotifyApi.getTrack(id).build();

        try {
            final Track track = getTrackRequest.execute();
            String artists = "";
            for (ArtistSimplified artist : track.getArtists()){
                artists = artists.concat(" "+artist.getName());
            }
            System.out.println(track.getName() + artists);
            return track.getName() + artists;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

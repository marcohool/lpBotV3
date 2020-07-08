package Command.Commands;

import Command.CommandContext;
import Command.CommandInterface;
import Config.Constants;

import javax.activation.MimeType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Photos implements CommandInterface {

    private String mimeType;

    @Override
    public void handle(CommandContext context) {

        context.getChannel().sendTyping().queue();
        InputStream image = getRandomImage();
        context.getChannel().sendTyping().queue();

        context.getChannel().sendFile(image, "SAMO LOKO"+mimeType).queue();

    }

    private InputStream getRandomImage() {

        Random rand = new Random();
        int randNumb = rand.nextInt(Constants.lokoPhotoFiles);
        for (int gif : Constants.gifs){
            if (gif == randNumb){
                mimeType = ".gif";
                break;
            } else {
                mimeType = ".png";
            }
        }
        String fileName = "lokoPhoto"+randNumb+mimeType;
        return getClass().getClassLoader().getResourceAsStream("assets/photoFiles/"+fileName);

    }

    @Override
    public List<String> getCalls() {
        return Arrays.asList("photos", "photo");
    }
}

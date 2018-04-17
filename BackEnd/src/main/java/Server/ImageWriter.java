package Server;



import Config.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageWriter {

    private static Config conf = Config.getInstance();

    public static String write(String s){
        String[] base64 = s.split(",",2);
        String metaData = base64[0];
        String imageData = base64[1];

        Pattern pattern = Pattern.compile("/(.*?);");
        Matcher matcher = pattern.matcher(metaData);

        String mimeType;
        if (matcher.find())
        {
            mimeType = matcher.group(1);
        }else{
            return null;
        }

        if(!(mimeType.equals("jpeg") || mimeType.equals("png"))){
            return null;
        }

       //BASE64Decoder decoder = new BASE64Decoder();
        String name;

        try{
            byte[] imageByte = null ;
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage image = ImageIO.read(bis);
            UUID uuid = UUID.randomUUID();
            name = uuid.toString()+"."+mimeType;
            File outputFile = new File(conf.getMediaServerPath()+name);
            ImageIO.write(image, mimeType, outputFile);
            bis.close();
        }catch(IOException e){
            return null;
        }

        return name;
    }

    public static void main(String args[]){
        String test = "data:image/jpeg;base64,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";
        String n =ImageWriter.write(test);
        System.out.println(n);
    }
}

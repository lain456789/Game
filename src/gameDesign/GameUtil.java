package gameDesign;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GameUtil {
    public static Image getImage(String path) {
        BufferedImage img= null;
        try {
            // 尝试使用相对路径加载图像文件
            java.io.File file = new java.io.File(path);
            if (!file.exists()) {
                // 如果文件不存在，尝试从src目录加载
                file = new java.io.File("src/" + path);
            }
            if (file.exists()) {
                img = ImageIO.read(file);
            } else {
                System.out.println("Image file not found: " + path);
                // 尝试使用ClassLoader加载作为备选
                URL u = GameUtil.class.getClassLoader().getResource(path);
                if (u != null) {
                    img = ImageIO.read(u);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + path);
            e.printStackTrace();
        }
        return img;
    }
}
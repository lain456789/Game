package gameDesign;

import java.awt.*;

/**
 * 爆炸效果类
 */
public class Explode {
    double x, y; // 爆炸位置
    static Image[] imgs = new Image[16]; // 爆炸图片数组
    int count = 0; // 爆炸帧计数器
    boolean live = true; // 爆炸是否存活
    int width, height; // 爆炸效果的宽度和高度

    /**
     * 静态初始化块，加载爆炸图片
     */
    static {
        // 注意：这里没有实际的爆炸图片文件，所以只初始化数组
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = null; // 实际项目中应该加载真实的爆炸图片
        }
    }

    /**
     * 构造函数
     * @param x x坐标
     * @param y y坐标
     * @param width 宽度
     * @param height 高度
     */
    public Explode(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * 绘制爆炸效果
     * @param g 图形上下文
     */
    public void draw(Graphics g) {
        if (!live) {
            return;
        }

        if (imgs[count] == null) {
            // 如果没有爆炸图片，绘制一个简单的圆形爆炸效果
            Color c = g.getColor();
            g.setColor(Color.ORANGE);
            int radius = (int) (width * (count + 1) / 16.0);
            g.fillOval((int) x - radius / 2, (int) y - radius / 2, radius, radius);
            g.setColor(c);
        } else {
            // 使用预加载的爆炸图片
            g.drawImage(imgs[count], (int) x, (int) y, width, height, null);
        }

        count++;

        // 如果爆炸动画播放完毕，将live设为false
        if (count >= imgs.length) {
            live = false;
            count = 0;
        }
    }
}
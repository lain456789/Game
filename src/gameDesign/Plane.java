package gameDesign;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 玩家飞机类
 */
public class Plane extends GameObject {
    boolean left, up, right, down; // 控制方向
    boolean live = true; // 飞机是否存活
    
    // 窗口尺寸（用于限制飞机移动范围）
    private int windowWidth = GameConfig.INITIAL_WINDOW_WIDTH;
    private int windowHeight = GameConfig.INITIAL_WINDOW_HEIGHT;

    /**
     * 构造函数
     * @param img 飞机图像
     * @param x x坐标
     * @param y y坐标
     * @param speed 移动速度
     * @param width 宽度
     * @param height 高度
     */
    public Plane(Image img, int x, int y, int speed, int width, int height) {
        super(img, x, y, speed, width > 0 ? width : 1, height > 0 ? height : 1);
        
        // 初始化缓冲图像用于像素级碰撞检测
        if (img != null && this.width > 0 && this.height > 0) {
            bufferedImg = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImg.createGraphics();
            g2d.drawImage(img, 0, 0, this.width, this.height, null);
            g2d.dispose();
        }
    }

    /**
     * 绘制飞机
     * @param g 图形上下文
     */
    public void draw(Graphics g) {
        if (live) {
            g.drawImage(img, (int) x, (int) y, width, height, null);
            Movement();
        }
    }

    /**
     * 根据方向键移动飞机
     */
    public void Movement() {
        if (left) {
            x -= speed;
        }
        if (right) {
            x += speed;
        }
        if (up) {
            y -= speed;
        }
        if (down) {
            y += speed;
        }

        // 限制飞机移动范围
        if (x < 0) {
            x = 0;
        }
        if (x > windowWidth - width) {
            x = windowWidth - width;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > windowHeight - height) {
            y = windowHeight - height;
        }
    }

    /**
     * 停止飞机移动
     */
    public void Stop() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    /**
     * 设置窗口尺寸，用于动态调整飞机移动范围
     * @param width 窗口宽度
     * @param height 窗口高度
     */
    public void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
    }
}
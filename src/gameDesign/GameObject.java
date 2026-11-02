package gameDesign;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 游戏对象基类，所有游戏对象的父类
 */
public class GameObject {
    double x, y; // 坐标
    Image img; // 图像
    double speed; // 速度
    int width, height; // 宽高
    BufferedImage bufferedImg; // 用于碰撞检测的缓冲图像
    boolean live = true; // 对象是否存活

    /**
     * 默认构造函数
     */
    public GameObject() {
    }

    /**
     * 构造函数
     * @param img 图像
     * @param x x坐标
     * @param y y坐标
     * @param speed 速度
     * @param width 宽度
     * @param height 高度
     */
    public GameObject(Image img, int x, int y, int speed, int width, int height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
    }

    /**
     * 绘制对象
     * @param g 图形上下文
     */
    public void drawMyself(Graphics g) {
        if (live) {
            g.drawImage(img, (int) x, (int) y, width, height, null);
        }
    }

    /**
     * 获取对象的矩形区域
     * @return 矩形区域
     */
    public Rectangle getRect() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * 检查对象指定位置的像素是否不透明
     * @param x x坐标
     * @param y y坐标
     * @return 是否不透明
     */
    public boolean isPixelOpaque(int x, int y) {
        if (bufferedImg == null) {
            return false;
        }
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        // 额外检查，确保坐标在缓冲图像范围内
        if (x < 0 || x >= bufferedImg.getWidth() || y < 0 || y >= bufferedImg.getHeight()) {
            return false;
        }
        try {
            return (bufferedImg.getRGB(x, y) >> 24) != 0;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * 像素级碰撞检测
     * @param other 另一个游戏对象
     * @return 是否碰撞
     */
    public boolean isCollidedWith(GameObject other) {
        Rectangle r1 = this.getRect();
        Rectangle r2 = other.getRect();
        
        // 先进行矩形碰撞检测
        if (!r1.intersects(r2)) {
            return false;
        }
        
        // 矩形碰撞后再进行像素级碰撞检测
        int x1 = Math.max((int) this.x, (int) other.x);
        int y1 = Math.max((int) this.y, (int) other.y);
        int x2 = Math.min((int) this.x + this.width, (int) other.x + other.width);
        int y2 = Math.min((int) this.y + this.height, (int) other.y + other.height);
        
        // 遍历重叠区域的像素
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                // 检查两个对象在该位置的像素是否都不透明
                if (this.isPixelOpaque(x - (int) this.x, y - (int) this.y) && 
                    other.isPixelOpaque(x - (int) other.x, y - (int) other.y)) {
                    return true; // 像素级碰撞
                }
            }
        }
        
        return false; // 没有像素级碰撞
    }
}
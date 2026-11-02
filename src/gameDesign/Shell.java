package gameDesign;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 小球/炮弹类
 */
public class Shell extends GameObject {

    /**
     * 构造函数
     * @param x x坐标
     * @param y y坐标
     * @param speed 移动速度
     * @param width 宽度
     * @param height 高度
     */
    public Shell(int x, int y, double speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width > 0 ? width : 1;
        this.height = height > 0 ? height : 1;
        
        // 初始化缓冲图像用于像素级碰撞检测
        if (this.width > 0 && this.height > 0) {
            bufferedImg = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImg.createGraphics();
            g2d.setColor(Color.CYAN);
            g2d.fillOval(0, 0, this.width, this.height);
            g2d.dispose();
        }
    }

    /**
     * 绘制小球并更新位置
     * @param g 图形上下文
     * @param canMove 是否允许移动
     */
    public void draw(Graphics g, boolean canMove) {
        if (live) {
            Color c = g.getColor();
            g.setColor(Color.CYAN);
            g.fillOval((int) x, (int) y, width, height);
            g.setColor(c);

            // 向下移动，暂停时不移动
            if (canMove) {
                y += speed;
            }
        }
    }
    
    /**
     * 重载draw方法，默认允许移动
     */
    public void draw(Graphics g) {
        draw(g, true);
    }

    /**
     * 检查小球是否超出边界
     * @param windowHeight 当前窗口高度
     * @return 是否超出边界
     */
    public boolean isOutOfBounds(int windowHeight) {
        return y > windowHeight;
    }
}
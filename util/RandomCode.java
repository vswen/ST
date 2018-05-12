package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import org.apache.log4j.Logger;

/**
 * 随机验证码的接口类，所有的生成规则需继承本接口
 * @author wilson
 *
 */
public abstract class RandomCode
{

    private Logger log = Logger.getLogger(RandomCode.class);

    /**
     * 图片
     */
    protected byte[] image;

    protected OutputStream os = null;

    /**
     * 值
     */
    protected String code;

    public RandomCode()
    {
        this.init(this.getWidth(), this.getHeight());
    }

    public RandomCode(int width, int height)
    {
        init(width, height);
    }

    /**
     * 获取图片
     * 
     * @return
     */
    public byte[] getImage()
    {
        return this.image;
    }

    /**
     * 获取验证码
     * 
     * @return
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     * 生成图片的方法
     * 
     */
    private void init(int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            return;
        }

        this.code = this.createValue();
        if (this.code == null)
        {
            return;
        }

        Random rand = new Random(System.currentTimeMillis());
        Graphics2D g = null;

        String[] fontTypes = this.getFontTypes();
        int fontTypesLen = fontTypes.length;

        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        g = bimage.createGraphics();

        // 设置随机背景色
        // Color color = new Color(rand.nextInt(255), rand.nextInt(255),
        // rand.nextInt(255));
        Color color = new Color(245, 255, 250);

        // 填充背景
        g.setColor(color);
        g.fillRect(0, 0, width, height);

        // 设置字体
        g.setFont(new Font(fontTypes[rand.nextInt(fontTypesLen)], Font.BOLD,
                getFontSize()));

        // 随机生成字符,根据截取的位数决定产生的数字
        int w = (g.getFontMetrics()).stringWidth(this.code);
        //int d = (g.getFontMetrics()).getDescent();
        int a = (g.getFontMetrics()).getMaxAscent();
        int x = 0, y = 0;

        // 画边框
        g.setColor(new Color(198, 198, 198));
        g.drawRect(0, 0, width - 1, height - 1);

        // 设置随机线条
        for (int i = 0; i < 7; i++)
        {
            g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand
                    .nextInt(255)).brighter());
            x = rand.nextInt(width);
            y = rand.nextInt(height);
            int xl = rand.nextInt(30);
            int yl = rand.nextInt(30);
            g.drawLine(x, y, x + xl, y + yl);
        }

        // reset x and y
        x = 0;
        y = 0;

        // 展示验证码中颜色,随机
        g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand
                .nextInt(255)).brighter());
        // 设置文字出现位置为中央
        x = width / 2 - w / 2 - 8;
        y = height / 2 + a / 2 - 4;

        // 文字变形设置
        AffineTransform fontAT = new AffineTransform();
        int xp = x - 2;

        // 每个文字都变形
        for (int c = 0, length = this.code.length(); c < length; c++)
        {
            // 产生弧度
            int rotate = rand.nextInt(20);
            fontAT.rotate(rand.nextBoolean() ? Math.toRadians(rotate) : -Math
                    .toRadians(rotate / 2));
            Font fx = new Font(fontTypes[rand.nextInt(fontTypesLen)],
                    Font.BOLD, getFontSize()).deriveFont(fontAT);
            g.setFont(fx);

            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            Random random = new Random();
            int red = random.nextInt(200);
            int green = random.nextInt(200);
            int blue = random.nextInt(200);

            // 用随机产生的颜色将验证码绘制到图像中。
            g.setColor(new Color(red, green, blue));
            String ch = String.valueOf(this.code.charAt(c));
            int ht = rand.nextInt(3);

            // 打印字并移动位置
            g.drawString(ch, xp, y + (rand.nextBoolean() ? -ht : ht));

            // 移动指针.
            xp += g.getFontMetrics().stringWidth(ch) + 2;
        }

        // 图象生效
        g.dispose();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try
        {
            ImageOutputStream imgeOut = ImageIO.createImageOutputStream(output);
            ImageIO.write(bimage, "JPEG", imgeOut);
            imgeOut.close();
            this.image = output.toByteArray();
        }
        catch (Exception ex)
        {
            log.info("生成验证码图像错误", ex);
        }
        finally
        {
            if (output != null)
            {
                try
                {
                    output.close();
                }
                catch (IOException ex)
                {
                    log.info("关闭输出流错误", ex);
                }
            }
        }
    }

    /**
     * 获取文字类型高度
     * 
     * @return
     */
    protected abstract String[] getFontTypes();

    /**
     * 获取文字的大小
     * 
     * @return
     */
    protected abstract int getFontSize();

    /**
     * 获取图片宽度
     * 
     * @return
     */
    protected abstract int getWidth();

    /**
     * 获取图片高度
     * 
     * @return
     */
    protected abstract int getHeight();

    /**
     * 生成验证码的值
     * 
     * @return
     */
    protected abstract String createValue();

}

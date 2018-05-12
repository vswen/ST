package util;

import java.util.UUID;

/**
 * 数字和字母组成的验证码
 * @author wilson
 *
 */
public class NumericLetterCode extends RandomCode
{

    // 字体大小
    private int fontSize;

    // 图片宽度
    private int width;

    // 图片高度
    private int height;

    // 需要生成的字符串长度
    private int length;

    // 生成图片的默认字符长度
    private static int DEFAULT_LENGTH = 4;

    // 图片的默认宽度
    private static int DEFAULT_WIDTH = 120;

    // 图片的默认高度
    private static int DEFAULT_HEIGHT = 26;

    // 字体默认大小
    private static int DEFAULT_FONT_SIZE = 20;

    // 英文字体类型
    public static final String[] FONT_TYPES_EN = { "Times New Roman",
            "Verdana", "arial" };

    public NumericLetterCode(int length)
    {
        super();
    }

    public NumericLetterCode(int width, int height)
    {
        super(width, height);
        this.width = width;
        this.height = height;
    }

    public NumericLetterCode()
    {
    }

    @Override
    protected String createValue()
    {
        return UUID.randomUUID().toString().replace("-", "")
                .substring(0, 0 != length ? length : DEFAULT_LENGTH);
    }

    @Override
    public int getFontSize()
    {
        return 0 != fontSize ? fontSize : DEFAULT_FONT_SIZE;
    }

    @Override
    protected String[] getFontTypes()
    {
        return FONT_TYPES_EN;
    }

    @Override
    public int getHeight()
    {
        return 0 != height ? height : DEFAULT_HEIGHT;
    }

    @Override
    public int getWidth()
    {
        return 0 != width ? width : DEFAULT_WIDTH;
    }

}

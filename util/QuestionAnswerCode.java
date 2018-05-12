package util;

import java.util.HashMap;


import java.util.Map;
import java.util.Random;

import util.Tools;

/**
 * 计算式验证码
 * @author wilson
 *
 */
public class QuestionAnswerCode extends RandomCode
{

	// 字体大小
	private int fontSize;

	// 图片宽度
	private int width;

	// 图片高度
	private int height;

	// 图片的默认宽度
	private static int DEFAULT_WIDTH = 120;

	// 图片的默认高度
	private static int DEFAULT_HEIGHT = 26;

	// 字体默认大小
	private static int DEFAULT_FONT_SIZE = 19;

	// 中文数字到英文数字的转换
	private static Map<String, Integer> NUMERIC_MAP = new HashMap<String, Integer>();

	// 加法表达式
	private static final String ADD_EXP = "a + b = c";

	// 加法表达式中问号可以放RAMDOM个位置
	private static final int RAMDOM = 3;

	// 标记问号在哪个位置，该值没有用
	private static final int QUESTION_MARK = -1;

	// 1 - 10 壹、贰、叁、肆、伍、陆、柒、捌、玖、拾
	/*
	 * private static final String[] NUMERIC = { "1", "2", "3", "4", "5", "6",
	 * "7", "8", "9", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	 */

	private static final String[] NUMERIC = { "1", "2", "3", "4", "5", "6",
			"7", "8", "9" };

	private String value;

	// 中文字体类型
	private static final String[] FONT_TYPES_ZH = { "楷体", "Arial" };

	// 初始化Map内容
	static
	{
		NUMERIC_MAP.put("壹", 1);
		NUMERIC_MAP.put("贰", 2);
		NUMERIC_MAP.put("叁", 3);
		NUMERIC_MAP.put("肆", 4);
		NUMERIC_MAP.put("伍", 5);
		NUMERIC_MAP.put("陆", 6);
		NUMERIC_MAP.put("柒", 7);
		NUMERIC_MAP.put("捌", 8);
		NUMERIC_MAP.put("玖", 9);
	}

	public QuestionAnswerCode()
	{
		super();
	}

	public QuestionAnswerCode(int width, int height)
	{
		super(width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	protected String createValue()
	{
		String exp;
		int result = -1;
		do
		{
			exp = generateQAString();
		}
		while ((result = calcExpValue(exp)) < 0);
		value = String.valueOf(result);
		return exp;
	}

	@Override
	public String getCode()
	{
		return value;
	}

	@Override
	public int getFontSize()
	{
		return 0 != fontSize ? fontSize : DEFAULT_FONT_SIZE;
	}

	@Override
	protected String[] getFontTypes()
	{
		return FONT_TYPES_ZH;
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

	/**
	 * @Description 随机产生算术表达式内容
	 * @return 返回算术表达式内容
	 */
	private String generateQAString()
	{
		String addExp = ADD_EXP;
		Random rand = new Random(System.currentTimeMillis());

		switch (rand.nextInt(RAMDOM))
		{
			case 0:
				addExp = addExp.replace("a", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("b", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("c", "?");
				break;
			case 1:
				addExp = addExp.replace("b", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("c", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("a", "?");
				break;
			case 2:
				addExp = addExp.replace("a", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("c", NUMERIC[rand
						.nextInt(NUMERIC.length)]);
				addExp = addExp.replace("b", "?");
				break;
		}

		return addExp;
	}

	/**
	 * @Description 计算表达式
	 * @param a
	 *            被加数
	 * @param b
	 *            加数
	 * @param c
	 *            值
	 * @return 返回表达式值
	 */
	private Integer calcValue(int a, int b, int c)
	{
		if (QUESTION_MARK == a)
		{
			return c - b;
		}
		else if (QUESTION_MARK == b)
		{
			return c - a;
		}
		else if (QUESTION_MARK == c)
		{
			return a + b;
		}
		else
		{
			return null;
		}
	}

	/**
	 * @Description 计算表达式的值
	 * @param exp
	 *            表达式
	 * @return 返回表达式结果
	 */
	private Integer calcExpValue(String exp)
	{
		int pos = exp.indexOf("?");
		int calcResult = 0;
		int na = 0, nb = 0, nc = 0;
		String a = "", b = "", c = "";

		a = exp.substring(0, 1);
		b = exp.substring(4, 5);
		c = exp.substring(exp.length() - 1);

		if (pos == 0)
		{
			// 问号为被加数
			if (null != NUMERIC_MAP.get(b))
			{
				nb = NUMERIC_MAP.get(b);
			}
			else
			{
				nb = Tools.str2Int(b);
			}

			if (null != NUMERIC_MAP.get(c))
			{
				nc = NUMERIC_MAP.get(c);
			}
			else
			{
				nc = Tools.str2Int(c);
			}

			// 计算加法表达式结果
			calcResult = calcValue(QUESTION_MARK, nb, nc);

		}
		else if (pos == exp.length() - 1)
		{
			// 问号为结果
			if (null != NUMERIC_MAP.get(a))
			{
				na = NUMERIC_MAP.get(a);
			}
			else
			{
				na = Tools.str2Int(a);
			}

			if (null != NUMERIC_MAP.get(b))
			{
				nb = NUMERIC_MAP.get(b);
			}
			else
			{
				nb = Tools.str2Int(b);
			}

			// 计算加法表达式结果
			calcResult = calcValue(na, nb, QUESTION_MARK);

		}
		else
		{
			// 问号为加数
			if (null != NUMERIC_MAP.get(a))
			{
				na = NUMERIC_MAP.get(a);
			}
			else
			{
				na = Tools.str2Int(a);
			}

			if (null != NUMERIC_MAP.get(c))
			{
				nc = NUMERIC_MAP.get(c);
			}
			else
			{
				nc = Tools.str2Int(c);
			}

			// 计算加法表达式结果
			calcResult = calcValue(na, QUESTION_MARK, nc);
		}

		return calcResult;
	}
}

package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;

public class Tools
{
    private static final char[] LT_ENCODE = "&lt;".toCharArray();

    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    public static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.CHINA);
    
    private static final Logger log = Logger.getLogger(Tools.class);
    
    /**
     * 将带HTML标记的字符串中的“<”“>”标记替换成“&lt;”，“&gt;”
     * 
     * @param in 要替换的字符串
     * @return String 替换后的字符串
     */
    public static final String escapeHTMLTags(String in)
    {
        if (in == null)
        {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++)
        {
            ch = input[i];
            if (ch > '>')
            {
                continue;
            }
            else if (ch == '<')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '>')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            }
        }
        if (last == 0)
        {
            return in;
        }
        if (i > last)
        {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * 获得当前年
     * 
     * 
     * @return String 当前年的字符串，格式：yyyy
     */
    public static String getThisYear()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(new Date());
    }

    /**
     * 获得当前年月
     * 
     * 
     * @return String 当前年的字符串，格式：yyyy-MM
     */
    public static String getThisMonth()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(new Date());
    }
    
    /**
     * 获得当前日期和时间
     * 
     * 
     * @return String 当前日期和时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurDateTime()
    {
        SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return nowDate.format(new Date());
    }

    // /**
    // * 获得当前日期和时间
    // *
    // * @return String 当前日期和时间，格式：yyyyMMddHHmmssSSS
    // */
    // public static String getCurTimePoint()
    // {
    // SimpleDateFormat nowDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    // return nowDate.format(new Date());
    // }
    //

    /**
     * 得到今天的星期
     * 
     * 
     * @return
     */
    public static int getCurDay()
    {
        // 获得当天是星期几和一个月中的某天
        int weekDay = 0;
        // int monthDay = 0;
        /** 定制为北京时区，中文地区的日历空件 */
        synchronized (calendar)
        {
            calendar.clear();
            calendar.setTime(new Date());
            weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if (weekDay == 0)
        {
            weekDay = 7;
        }
        return weekDay;
    }
    
    /**
     * 时间比较 
     * @param d
     * @return true表示传入的时间大于当前时间
     */
    public static boolean compToCurDateTime(Date d){
    	boolean b = false;
        try{
            int retValue = d.compareTo(new Date());
            if(retValue>=0){
            	b = true;
            }
        }catch (Exception e){
            
        }
        return b;
    }
    
    /**
     * 获得当前日期
     * 
     * @return String 当前日期，格式：yyyy-MM-dd
     */
    public static String getCurDate()
    {
        SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd");
        return nowDate.format(new Date());
    }

    /**
     * 将日期时间转换成yyyy-MM-dd HH:mm:ss的格式
     * 
     * 
     * @param date Date 要转换的日期时间
     * @return String 转换后的日期时间
     */
    public static String getDateTime(Date date)
    {
        if (date == null)
        {
            return "";
        }
        SimpleDateFormat nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return nowDate.format(date);
    }

    /**
     * 将日期转换成yyy-MM-dd的格式
     * 
     * 
     * @param date Date 要转换的日期
     * @return String 转换后的日期
     */
    public static String getDate(Date date)
    {
        if (date == null)
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将日期形的字符串转换成yyy-MM-dd的格式
     * 
     * 
     * @param date String 要转换的日期
     * @return String 转换后的日期
     */
    public static String getDate(String dateString)
    {
        Date date = str2Date(dateString);
        if (date == null)
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将日期转换成yyy-MM-dd的格式
     * 
     * 
     * @param date Date 要转换的日期
     * @return String 转换后的日期
     */
    public static String getDateTime(Date date, String pattern)
    {
    	if(date == null){
    		return null;
    	}
        if (pattern == null)
            pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 获取当前时间 N 天以前的日期
     * 
     * @param days
     * @return
     * 
     */
    public static String getCurrDateBefore(int days)
    {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -days);
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return bartDateFormat.format(ca.getTime());
    }

    /**
     * 获取当前时间 N 天以前的日期
     * 
     * @param days
     * @return
     * 
     */
    public static String getCurrDateBefore2(Date date,int days)
    {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, -days);
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return bartDateFormat.format(ca.getTime());
    }
    
    /**
     * 获取当前时间 N 天以后的日期
     * 
     * @param days
     * @return
     * 
     */
    public static String getCurrDateAfter(int days)
    {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, +days);
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return bartDateFormat.format(ca.getTime());
    }

    /**
     * 获取当前时间 N 天以后的日期
     * 
     * @param days
     * @return
     * 
     */
    public static Date getCurrDateAfter2(Date date,int days)
    {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DATE, +days);
        return ca.getTime();
    }
    
    /**
     * 字符串转化为日期
     * 
     * @param str
     * @return
     */
    public static Date str2Date(String str)
    {
        Date returnDate = null;
        if (str != null)
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                returnDate = df.parse(str);
            }
            catch (Exception e)
            {
                return returnDate;
            }
        }
        return returnDate;
    }

    /**
     * 将对象类型转换成字符串类型
     * 
     * @param obj
     * @return
     */
    public static String obj2Str(Object obj)
    {
        if (obj == null)
        {
            return "";
        }

        return String.valueOf(obj);
    }

    /**
     * 将对象类型转换成boolean类型
     * 
     * @param obj
     * @return
     */
    public static boolean obj2Bool(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        return (Boolean) obj;
    }

    /**
     * 将对象类型转换成整数类型
     * 
     * @param obj
     * @return
     */
    public static int obj2Int(Object obj)
    {
        if (obj == null)
        {
            return 0;
        }
        if (obj instanceof Number)
        {
            return ((Number) obj).intValue();
        }
        try
        {
            return Integer.parseInt(String.valueOf(obj));
        }
        catch (Exception e)
        {
            return 0;// 转换错误时，返回0
        }
    }

    /**
     * 将对象类型转换成double类型
     * 
     * @param obj
     * @return
     */
    public static double obj2Double(Object obj)
    {
        if (obj == null)
        {
            return 0d;
        }
        if (obj instanceof Number)
        {
            return ((Number) obj).doubleValue();
        }
        try
        {
            return Double.parseDouble(String.valueOf(obj));
        }
        catch (Exception e)
        {
            return 0d;// 转换错误时，返回0
        }
    }

    /**
     * 将字符串类型转换成整数类型
     * 
     * @param orig
     * @return
     */
    public static int str2Int(String orig)
    {
        if (orig == null)
        {
            return 0;
        }

        try
        {
            return Integer.parseInt(orig);
        }
        catch (Exception e)
        {
            return 0;// 转换错误时，返回0
        }
    }

    /**
     * 字符串转换为日期（包含小时分）
     * 
     * 
     * @param str
     * @return Date
     */
    public static Date str2DateTime(String str)
    {
        Date returnDate = null;
        if (str != null)
        {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try
            {
                int strLength = str.length();
                if (strLength < 11)
                {
                    str += " 00:00:00";
                }
                else if (strLength > 11 && strLength < 14)
                {
                    str += ":00:00";
                }
                else if (strLength > 14 && strLength < 17)
                {
                    str += ":00";
                }

                returnDate = df.parse(str);
            }
            catch (Exception e)
            {
                return returnDate;
            }
        }
        return returnDate;
    }

    /**
     * 字符串转换为日期（包含小时分）yymmddhhmm
     * 
     * @param str
     * @return Date
     */
    public static Date strToDateTime(String str)
    {
        StringBuffer tmpStr = new StringBuffer();
        tmpStr.append(Tools.getThisYear().substring(0, 2)).append(str.substring(0, 2)).append("-")
                .append(str.substring(2, 4)).append("-").append(str.substring(4, 6)).append(" ")
                .append(str.substring(6, 8)).append(":").append(str.substring(8, 10));
        return str2DateTime(tmpStr.toString());
    }

    /**
     * 字符串转换为整数
     * 
     * @param str String 要转换的字符串
     * 
     * @return int 转换后的整数
     */
    public static int getInt(String str)
    {
        if ((str != null) && (!"".equals(str)))
        {
            if (str.charAt(0) == '+')// 带正负号的数也有效

                str = str.substring(1);
            try
            {
                return Integer.parseInt(str);
            }
            catch (Exception e)
            {
                return 0;// 转换错误时，返回0
            }
        }
        else
        {
            // 字符串为空时返回0
            return 0;
        }
    }

    /**
     * 字符串转换为double
     * 
     * @param str String 要转换的字符串
     * 
     * @return double 转换后的double
     */
    public static double getDouble(String str)
    {
        if (str != null && !"".equals(str))
        {
            try
            {
                return Double.parseDouble(str);
            }
            catch (Exception e)
            {
                return 0;// 转换错误时返回0
            }
        }
        else
        {
            // 字符串为空时，返回0
            return 0;
        }
    }

    /**
     * 字符串转换为GB2312编码的字符串
     * 
     * @param str String 要转换的字符串
     * 
     * @return String GB2312的字符串
     */
    public static String getGBK(String str)
    {

        if (str == null)
        {
            return "";
        }

        String temp;
        try
        {
            byte[] buf = str.trim().getBytes("iso8859-1");
            temp = new String(buf, "GBK");
        }
        catch (Exception e)
        {
            // 转换错误时，返回原字符串
            temp = str;
        }
        return temp;
    }

    /**
     * 字符串转换为iso8859-1编码的字符串
     * 
     * @param str String 要转换的字符串
     * 
     * @return String iso8859-1的字符串
     */
    public static String getISO(String str)
    {

        if (str == null)
        {
            return "";
        }
        String temp;
        try
        {
            byte[] buf = str.trim().getBytes("GBK");
            temp = new String(buf, "iso8859-1");
        }
        catch (Exception e)
        {
            // 转换错误时，返回原字符串
            temp = str;
        }
        return temp;
    }

    /**
     * 字符串转换为utf-8编码的字符串
     * 
     * @param str String 要转换的字符串
     * 
     * @return String utf-8的字符串
     */
    public static String getUTF8(String str)
    {

        if (str == null)
        {
            return "";
        }
        String temp;
        try
        {
            byte[] buf = str.trim().getBytes("iso8859-1");
            temp = new String(buf, "utf-8");
        }
        catch (Exception e)
        {
            // 转换错误时，返回原字符串
            temp = str;
        }
        return temp;
    }

    /**
     * 将字符串按分隔符转换为字符串List
     * 
     * @param str String 要转换的字符串
     * 
     * @param delim String 分隔符
     * 
     * @return ArrayList
     */
    public static List<String> getStringTokenizer(String str, String delim)
    {
        StringTokenizer st = new StringTokenizer(str, delim);
        List<String> lists = new ArrayList<String>();
        String tmp;
        while (st.hasMoreTokens())
        {
            tmp = st.nextToken().trim();
            if (!"".equals(tmp))
            {
                lists.add(tmp);
            }
        }
        return lists;
    }

    /**
     * 将字符串按分隔符转换为整型数字List
     * 
     * @param str String 要转换的字符串
     * 
     * @param delim String 分隔符
     * 
     * @return ArrayList
     */
    public static List<Integer> getIntegerTokenizer(String str, String delim)
    {
        StringTokenizer st = new StringTokenizer(str, delim);
        List<Integer> lists = new ArrayList<Integer>();
        String tmp;
        while (st.hasMoreTokens())
        {
            tmp = st.nextToken().trim();
            if (tmp.length() == 0)
                continue;

            if ("0".equals(tmp))
                lists.add(0);
            else
            {
                int t = Tools.getInt(tmp);
                if (t != 0)
                    lists.add(t);
            }
        }
        return lists;
    }

    /**
     * 去掉字符串两边的空格，为空时返回“”
     * 
     * 
     * @return String
     */
    public static String trim(String args)
    {
        if (args == null)
        {
            return "";
        }
        else
        {
            return args.trim();
        }
    }

    /**
     * 根据最大长度maxLen截断字符串srcString，中英文都算一个单位长度， 同时支持Iso8859-1,GB2312
     * 
     * @param srcString 原字符串
     * @param maxLen 截取长度，中英文都算一个单位长度。
     * 
     * @return 截取后的字符串
     */
    public static String trimByEncode(String srcString, int maxLen)
    {
        if (srcString == null)
            return "";

        int srcLength = srcString.length();
        if (srcLength <= maxLen || maxLen <= 0)
            return srcString.trim();

        int num = 0, i = 0, c;
        for (; i < srcString.length();)
        {
            c = srcString.charAt(i);
            if (c > 256) // 为中文且编码格式为GB2312时c>256
            {
                i++;
                num++;
            }
            else if (c > 'z' && c < 256) // 为中文且编码格式为ISO8859-1时c>'z'&&c<256
            {
                i = i + 2;
                num++;
            }
            else
            // 为英文字母

            {
                i++;
                num++;
            }
            if (num >= maxLen)
            {
                break;
            }
        }
        if (i > srcLength)
        {
            i = srcLength;
        }
        return srcString.substring(0, i);
    }

    /**
     * 判断字符串为中文还是英文， 同时支持Iso8859-1,GB2312
     * 
     * @param srcString 原字符串
     * @return true:中文；false:英文
     */
    public static boolean isEncode(String srcString)
    {
        boolean ret = false;
        if (srcString == null)
            return ret;

        int i = 0, c;
        for (; i < srcString.length();)
        {
            c = srcString.charAt(i);
            if (c > 256) // 为中文且编码格式为GB2312时c>256
            {
                i++;
                ret = true;
                break;
            }
            else if (c > 'z' && c < 256) // 为中文且编码格式为ISO8859-1时c>'z'&&c<256
            {
                i = i + 2;
                ret = true;
                break;
            }
            else
            // 为英文字母

            {
                i++;
            }
        }

        return ret;
    }

    /**
     * 字符串转换为链接的编码字符串
     * @param str
     * @return
     */
    public static String getURLEncoder(String str)
    {

        if (str == null)
        {
            return "";
        }
        String temp;
        try
        {
            temp = URLEncoder.encode(str,"utf-8");
        }
        catch (Exception e)
        {
            // 转换错误时，返回原字符串
            temp = str;
        }
        return temp;
    }
    
    /**
     * Description: 取字符串的固定长度部分,用"..."结尾
     * 
     * @param src 源字符串
     * @param len 截取长度
     * @return 截取后的字符串
     */
    public static String getSizedString(String src, int len)
    {
        if (src == null)
            return "";
        String str = trimByEncode(src, len);
        if (src.length() > str.length())// 源串长大于新串长度

            str += "...";
        return str;
    }

    // 字符串替空,不需要去掉空格

    public static String ConvertNull(String string)
    {
        if (string == null)
        {
            return "";
        }
        else
        {
            return string;
        }

    }

    /** 转换成HTML空格符 */
    public static String ConvertBlank(String str)
    {

        if (str == null)
        {
            return "&nbsp;";
        }
        else
        {
            return str;
        }
    }

    // 检测中国移动手机号码

    public static boolean isLegalMobile(String MobileSn)
    {
        // num：手机号码，11位，前面不可加0，86，086
        return isMobileFactory(MobileSn, "mu");
    }

    /**
     * 检测手机号码集中是否有错误的手机号码
     * 
     * 
     * @param mobiles 手机号码集
     * 
     * @return String null=没有错误，否则为错误描述信息
     */
    public static String checkIllegalMobile(String[] mobiles)
    {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0, len = mobiles.length; i < len; i++)
        {
            if (!Tools.isLegalMobile(mobiles[i]))
            {
                strBuf.append(mobiles[i]).append(", ");
            }
        }

        String tmp = strBuf.toString().trim();
        if (tmp.length() > 0)
        {
            return "如下号码错误：" + tmp;
        }
        else
        {
            return null;
        }

    }

    // 检测手机号码

    // num：手机号码，11位，前面可加0，86，086
    // factory: m：移动，u：联通

    public static boolean isMobileFactory(String num, String factory)
    {
        // 联通号段

        int[] u = { 130, 131, 132, 133, 189 };
        // 移动号段
        int[] m = { 134, 135, 136, 137, 138, 139, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 188 };

        // 空处理

        if (num == null || num.equals("")) // 手机号码不能为空
        {
            return false;
        }
        num = num.trim();
        // 长度，是否为数字处理
        boolean isDigital = true;
        String scope = "0123456789";
        // 小灵通号码

        String firstNum = num.substring(0, 1);
        if ("0".equals(firstNum))
        {
            if (num.length() > 12)
            {
                return false;
            }
            for (int i = 0; i < num.length(); i++)
            {
                if (scope.indexOf(num.charAt(i)) < 0) // 没找到

                {
                    isDigital = false;
                    break;
                }
            }
            return isDigital;
        }

        // 手机号码
        int len = num.length();
        if (len != 11)
        {
            return false;
        }

        if (num.length() == 11)
        {
            for (int i = 0; i < 11; i++)
            {
                if (scope.indexOf(num.charAt(i)) < 0) // 没找到

                {
                    isDigital = false;
                    break;
                }
            }

            if (!isDigital) // 手机号码只能为数字

            {
                return false;
            }
        }
        else
        {
            // 手机号码只能为11位

            return false;
        }

        String first3Num = num.substring(0, 3);

        boolean haveIn;
        if ("u".equalsIgnoreCase(factory)) // 联通

        {
            haveIn = isMobileExist(u, first3Num);

            if (!haveIn)
            {
                // 不是中国联通的号码
                return false;
            }
        }
        else if ("m".equalsIgnoreCase(factory)) // 移动
        {
            haveIn = isMobileExist(m, first3Num);

            if (!haveIn) // 不是中国移动的号码

            {
                return false;
            }
        }
        else if ("mu".equalsIgnoreCase(factory)) // 移动和联通

        {
            haveIn = isMobileExist(m, first3Num);
            if (!haveIn)
                haveIn = isMobileExist(u, first3Num);

            if (!haveIn) // 不是中国移动和联通的号码
            {
                return false;
            }
        }
        else
        // 非法的厂商参数

        {
            return false;
        }

        return true;
    }

    /**
     * @param arr
     * @param first3Num
     * @return boolean
     */
    private static boolean isMobileExist(int[] arr, String first3Num)
    {
        boolean haveIn = false;
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] == Tools.getInt(first3Num))
            {
                haveIn = true;
                break;
            }
        }
        return haveIn;
    }

    /**
     * 将null值或""值转为字符串"NULL"，其它的转为带两个单引号的字符串
     * 
     * @param str String
     * @return String
     */
    public static String toSQLNull(String str)
    {
        if (str == null || str.length() == 0)
        {
            return "NULL";
        }
        else
        {
            return "'" + str + "'";
        }
    }

    /**
     * 替换字符串中的特殊字符（' 和 \）
     * 
     * 
     * @param str
     * @return String
     */
    public static String replace(String str)
    {
        if (str == null)
        {
            return "";
        }

        String tmp = str;
        if (tmp.indexOf("'") > -1)
            tmp = StringUtils.replace(tmp, "'", "''");
        if (tmp.indexOf("\\") > -1)
            tmp = StringUtils.replace(tmp, "\\", "\\\\");
        return tmp;
    }

    /**
     * 得到系统的换行符
     * 
     * @return String
     */
    public static String getLineSeparator()
    {
        return System.getProperty("line.separator");
    }

    /**
     * 返回整点值，如：0-10之间的数，返回10，11-20之间的数，返回20
     * 
     * @param num
     * @return int
     */
    public static int getWholeNum(int num)
    {
        if (num <= 10)
        {
            return 10;
        }
        else if (num > 10 && num <= 20)
        {
            return 20;
        }
        else if (num > 20 && num <= 30)
        {
            return 30;
        }
        else if (num > 30 && num <= 40)
        {
            return 40;
        }
        else if (num > 40 && num <= 50)
        {
            return 50;
        }

        return 0;
    }

    /**
     * 计算短信分割后的条数
     * 
     * @param smsLength 短信长度
     * @return 分割后的短信条数
     */
    public static int countSmsNum(int smsLength)
    {
        int smsNum;
        if (smsLength == 0)
        {
            smsNum = 0;
        }
        else if (smsLength <= 70)
        {
            smsNum = 1;
        }
        else if ((smsLength % 65) == 0)
        {
            smsNum = smsLength / 65;
        }
        else
        {
            smsNum = smsLength / 65 + 1;
        }
        return smsNum;
    }

    /**
     * 判断一个元素是否存在数组中
     * 
     * @param arr 数组
     * @param str 元素
     * @return 存在返回元素在数组中的位置，不存在返回-1
     */
    public static int getArrayIndex(String[] arr, String str)
    {
        if (arr == null || str == null)
            return -1;

        for (int i = 0; i < arr.length; i++)
        {
            String tmpID = arr[i];
            if (str.equalsIgnoreCase(tmpID))
                return i;
        }
        return -1;
    }

    /**
     * 判断一个对象是否存在List中
     * 
     * clazz为null或methodName为null 表示集合内为简单的值
     * 
     * 
     * @param list 对象集合
     * @param value 与元素值作比较的值
     * 
     * @param clazz 对象的类
     * @param methodName 对象的方法名
     * @return 存在返回对象在List中的位置，不存在返回-1,方法名不存在，返回-2
     */
    @SuppressWarnings("unchecked")
	public static int getListIndexByValue(List<Object> list, Object value, Class clazz, String methodName)
    {
        if (list == null || value == null)
            return -1;

        if (clazz == null || methodName == null) // 集合内为简单的值ֵ

        {
            for (int i = 0; i < list.size(); i++)
            {
                Object objTmp = list.get(i);
                if (objTmp == null)
                    continue;

                if (objTmp.equals(value))
                    return i;
            }
            return -1;
        }

        for (int i = 0; i < list.size(); i++)
        {
            Object objTmp = list.get(i);
            if (objTmp == null)
                continue;

            try
            {
                java.lang.reflect.Method method = clazz.getMethod(methodName, clazz);
                if (method == null)
                    return -2;

                Object obj = method.invoke(objTmp, clazz);
                if (obj != null && obj.equals(value))
                    return i;
            }
            catch (Exception e)
            {
                return -1;
            }
        }
        return -1;
    }

    // 数组中是否有重复值

    public static boolean isRepeat(Object obj[])
    {
        boolean ret = false;

        for (int i = 0; i < obj.length - 1; i++)
        {
            Object tmp = obj[i];
            for (int j = i + 1; j < obj.length; j++)
            {
                if (tmp.equals(obj[j])) // 有重复

                {
                    return true;
                }
            }
        }

        return ret;
    }

    /**
     * 截取字符
     * str 原始数据
     * len 希望显示的长度
     * 
     * des 截断后加的显示数据 :如"..." 等
     */
    public static String getSubString(String str, int len, String des)
    {
        int counterOfDoubleByte;
        byte b[];
        counterOfDoubleByte = 0;
        try
        {
            b = str.getBytes("iso8859-1");
            if (b.length <= len)
                return str;
            for (int i = 0; i < len; i++)
            {
                if (b[i] < 0)
                    counterOfDoubleByte++;
            }

            if (counterOfDoubleByte % 2 == 0)
                return new String(b, 0, len, "iso8859-1") + des;
            else
                return new String(b, 0, len - 1, "iso8859-1") + des;
        }
        catch (UnsupportedEncodingException e)
        {
            return str;
        }
    }

    /**
     * 随机产生字符串
     * 
     * @param length 字符串长度
     * 
     * @param type 类型 (0: 仅数字; 2:仅字符; 别的数字:数字和字符)
     * @return
     */
    public static String getRandomStr(int length, int type)
    {
        String str = "";
        int beginChar = 'a';
        int endChar = 'z';
        // 只有数字
        if (type == 0)
        {
            beginChar = 'z' + 1;
            endChar = 'z' + 10;
        }
        // 只有小写字母
        else if (type == 2)
        {
            beginChar = 'a';
            endChar = 'z';
        }
        // 有数字和字母
        else
        {
            beginChar = 'a';
            endChar = 'z' + 10;
        }

        // 生成随机类

        Random random = new Random();
        for (int i = 0; i < length; i++)
        {
            int tmp = (beginChar + random.nextInt(endChar - beginChar));
            // 大于'z'的是数字
            if (tmp > 'z')
            {
                tmp = '0' + (tmp - 'z');
            }
            str += (char) tmp;
        }

        return str;
    }

    /**
     * 对模糊查询的特殊字符进行转换
     * 
     * @param str 需要转换的字符串
     * @return 转换后的字符串
     */
    public static String convertForMySql(String str)
    {
        if (str == null)
        {
            return "";
        }
        String tmp = trim(str);
        tmp = tmp.replaceAll("[%]", "\\\\%");
        tmp = tmp.replaceAll("[_]", "\\\\_");
        tmp = tmp.replaceAll("'", "''");
        return tmp;
    }

    /**
     * 判断给定的数组中，是否存在指定的值
     * 
     * @param array
     * @param v
     * @return
     * 
     */
    public static <T> boolean contains(final T[] array, final T v)
    {
        for (final T e : array)
        {
            if (e == v || v != null && v.equals(e))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算百分比
     * 
     * @param digits 保留的小数位
     * @param number 需要计算的数字
     * @param total 总数
     * @return
     * 
     */
    public static String getPercent(int digits, int number, int total)
    {
        if (total == 0)
        {
            return "0%";
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(0); // 设置两位小数位
        double result = (double) number / total;
        return nf.format(result);
    }


    /**
     * 将后缀为.0的数据去掉.0
     * 
     * @param d
     * @return
     */
    public static BigDecimal stripTrailingZeros(double d)
    {
        return new BigDecimal(Double.toString(d)).stripTrailingZeros();
    }

 

    /**
     * 从个位开始每隔三位添加一个逗号
     * 
     * @param num
     * @return
     * 
     */
    public static String displayWithComma(String num)
    {
        String str1 = num;
        str1 = new StringBuilder(str1).reverse().toString(); // 先将字符串颠倒顺序
        String str2 = "";
        for (int i = 0; i < str1.length(); i++)
        {
            if (i * 3 + 3 > str1.length())
            {
                str2 += str1.substring(i * 3, str1.length());
                break;
            }
            str2 += str1.substring(i * 3, i * 3 + 3) + ",";
        }
        if (str2.endsWith(","))
        {
            str2 = str2.substring(0, str2.length() - 1);
        }
        // 最后再将顺序反转过来
        return new StringBuilder(str2).reverse().toString();
    }

    /**
     * 获取UUID
     * 
     * @return
     */
    public static String getUUID()
    {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 转换html中textarea的的回车和空格，以显示在页面上
     * 
     * @param str
     * @return
     * 
     */
    public static String parseTextArea(String str)
    {
        if (str == null)
        {
            return null;
        }
        // str = str.replace("\n\n", "<br/>");
        str = str.replace("\r\n", "<br/>");
        str = str.replace("\n", "<br/>");
        str = str.replace("\r", "<br/>");
        str = str.replace(" ", "&nbsp;");
        return str;
    }
 
    /**
     * 读取文件内容
     * @param file
     * @return
     */
	public static String txt2String(File file) 
	{
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file))); 
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				sb.append(s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 返回客户端IP地址
	 * @param request HttpServletRequest
	 * @return 客户端IP地址，若等于""，则表示取IP失败
	 */
	public static String getRealRemoteIP(HttpServletRequest request)
	{
		String ret = "";
		
		//获取Aliyun WAF屏蔽的IP地址
		String wafSrcIp = request.getHeader("x-forwarded-for");
		if (wafSrcIp == null)
		{
			String ipNginx = request.getHeader("X_REAL_IP");
			String ip = null;
			if (ipNginx != null)
			{
				ip = ipNginx;
			}
			else
			{
				ip = request.getRemoteAddr();
			}

			if (ip != null)
			{
				ret = ip;
			}
		}
		else
		{
			ret = wafSrcIp.trim();
		}

		if (ret.startsWith("0:0:0:0:"))
		{
			ret = "127.0.0.1";
		}
		return ret;
	}
	
	/**
     * 计算两个日期间的天数
     * 
     * @param fromDate 起始日期
     * @param toDate 结束日期
     * @return
     * @throws ParseException
     */
    public static int dateDiff(Date first, Date last)
    {
        try
        {
            Calendar cal = Calendar.getInstance(Locale.CHINA);
            cal.setTime(first);
            long time1 = cal.getTimeInMillis();
            cal.setTime(last);
            long time2 = cal.getTimeInMillis();
            double between_days = (time2 - time1) / (1000f * 3600f * 24f);
            Double d =Math.ceil(between_days);
            return d.intValue();
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * 计算两个日期间的天数
     * 
     * @param fromDate 起始日期
     * @param toDate 结束日期
     * @return
     * @throws ParseException
     */
    public static long dateDiff2(Date first, Date last)
    {
        try
        {
            Calendar cal = Calendar.getInstance(Locale.CHINA);
            cal.setTime(first);
            long time1 = cal.getTimeInMillis();
            cal.setTime(last);
            long time2 = cal.getTimeInMillis();
            
            return time2 - time1;
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /** 
     * 取得当月天数 
     * */  
    public static int getCurrentMonthLastDay()  
    {  
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    } 
    
    
    /**
     * 取得某月份天数 
     * @param time 格式 2016-01
     * @return
     */
    public static int getMonthLastDay(String time){
    	int year = obj2Int(time.substring(0,4));
    	int month = obj2Int(time.substring(5));
    	Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month - 1);  
        a.set(Calendar.DATE, 1);  
        a.roll(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate; 
    } 
    
    public static String getqhTime(String time,int d){
    	Calendar ca = Calendar.getInstance();
        ca.setTime(str2DateTime(time));
        ca.add(Calendar.DATE, d);
        return getDate(ca.getTime());
    }
    
    /**
	 * 解析url内容
	 * @param targetURL
	 * @return
	 * @throws Exception
	 */
	public static String resolve(String targetURL){
		String output = "";
		HttpURLConnection httpConnection = null;
		try {
			URL targetUrl = new URL(targetURL);
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Content-Type",
					"application/json");
			InputStreamReader isr = new InputStreamReader(httpConnection
					.getInputStream(),"utf-8");
			BufferedReader responseBuffer = new BufferedReader(isr);
			output = responseBuffer.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}
		return output;
	}
	
	
	/**
	 * 解析url内容
	 * @param targetURL
	 * @return
	 * @throws Exception
	 */
	public static String resolve2(String targetURL,String params){
		String output = "";
		HttpURLConnection httpConnection = null;
		try {
			URL targetUrl = new URL(targetURL);
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpConnection.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");
			byte[] bypes = params.getBytes("utf-8");
			httpConnection.getOutputStream().write(bypes);// 输入参数
			InputStreamReader isr = new InputStreamReader(httpConnection
					.getInputStream(),"utf-8");
			BufferedReader responseBuffer = new BufferedReader(isr);
			output = responseBuffer.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}
		return output;
	}
	
	public static String resolve3(String targetURL,String params){
		String output = "";
		HttpURLConnection httpConnection = null;
		try {
			URL targetUrl = new URL(targetURL);
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
			byte[] bypes = params.getBytes("utf-8");
			httpConnection.getOutputStream().write(bypes);// 输入参数
			InputStreamReader isr = new InputStreamReader(httpConnection
					.getInputStream(),"utf-8");
			BufferedReader responseBuffer = new BufferedReader(isr);
			output = responseBuffer.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpConnection.disconnect();
		}
		return output;
	}
	
	
	/**
	 * 写文件
	 * @param fileName
	 * @param content
	 * @return
	 */
	public static boolean writeFile(String fileName,String content){
		FileWriter output = null;
        BufferedWriter writer = null;
        try{
            output = new FileWriter(fileName);
            writer = new BufferedWriter(output);
            writer.write(content);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            if(null != writer){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != output){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
	}
	
	/**
	 * 实现网络访问文件，将获取到的数据保存在指定目录中
	 * 
	 * @param url
	 * @param destFile
	 * @return
	 */
	public static JSONObject saveFileFromURL(String url,String path) {
		JSONObject jsonObject = new JSONObject();
		HttpURLConnection httpConn = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileName = null;
		String fileSize = null;
		try {

			URL urlObj = new URL(url);
			httpConn = (HttpURLConnection) urlObj.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(5000);
			httpConn.connect();

			if (httpConn.getResponseCode() == 200) {
				Map<String, List<String>> map = httpConn.getHeaderFields();
				fileName = map.get("Content-disposition").get(0);
				fileName = fileName.substring(fileName.indexOf("\"")+1, fileName
						.length() - 1);
				fileSize = map.get("Content-Length").get(0);
				bos = new BufferedOutputStream(new FileOutputStream(path + fileName));
				bis = new BufferedInputStream(httpConn.getInputStream());
				int c = 0;
				byte[] buffer = new byte[8 * 1024];
				while ((c = bis.read(buffer)) != -1) {
					bos.write(buffer, 0, c);
					bos.flush();
				}
			}
			jsonObject.put("path", path + fileName);
			jsonObject.put("fileSize",fileSize);
			return jsonObject;
		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				httpConn.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	/**
	 * 图片压缩
	 * @param fileName 源文件地址
	 * @param w
	 * @param h
	 * @param smallIcon 给源文件名添加 小名
	 * @return
	 */
	public static JSONObject ImgCompress(String fileName, int w, int h, String smallIcon){
		JSONObject json = new JSONObject();
		try {
			File file = new File(fileName);
			
			String filePrex = fileName.substring(0, fileName.lastIndexOf('.'));
			String imageFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
			String newImage = filePrex + smallIcon + "." + imageFormat;
			File file2 = new File(newImage);
			FileUtils.copyFile(file, file2);
			
			FileInputStream in = new FileInputStream(file2);
			Image img = ImageIO.read(in);
			int height = img.getHeight(null);
		    int width = img.getWidth(null);
		    if(height < h && width < w){
		    	json.put("success", true);
		    	return json;
		    }
			
			// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
			BufferedImage image = new BufferedImage(w, h,
					BufferedImage.TYPE_INT_RGB);
			image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, imageFormat, baos);
			byte[] b = baos.toByteArray();
	
			FileOutputStream fos = new FileOutputStream(newImage);
			fos.write(b, 0, b.length);
			fos.flush();
			fos.close();
			json.put("success", true);
		} catch (Exception e) {
			json.put("success", false);
		}
		return json;
	}
	
	/**
	 * 等比例图片压缩
	 * @param fileName 源文件地址
	 * @param height 压缩高度
	 * width按height压缩尺寸比例进行压缩
	 * @param smallIcon 给源文件名添加 小名
	 * @param isRun ture为同步
	 * @return
	 */
	public static JSONObject ImgCompress2(String fileName, int height, String smallIcon, boolean isRun) {
		class MutliThread extends Thread{
			private String fileName;
			private int height;
			private String smallIcon;
			private JSONObject json;
			public MutliThread(String fileName, int height, String smallIcon) {
				this.fileName = fileName;
				this.height = height;
				this.smallIcon = smallIcon;
			}
			public void run(){
				json = new JSONObject();
				try {
					File file = new File(fileName);

					String filePrex = fileName.substring(0, fileName.lastIndexOf('.'));
					String imageFormat = fileName
							.substring(fileName.lastIndexOf(".") + 1);
					String newImage = filePrex + smallIcon + "." + imageFormat;
					File file2 = new File(newImage);
					FileUtils.copyFile(file, file2);

					FileInputStream in = new FileInputStream(file2);
					Image img = ImageIO.read(in);
					int srcWidth = img.getWidth(null);
					int srcHeight = img.getHeight(null);
					
					if(srcHeight <= height || srcWidth <= height){
						json.put("success", true);
					}else{
						int deskWidth = 0;// 缩略图宽
						double hb = Tools.obj2Double(height)/srcHeight;
						deskWidth = Tools.obj2Int(srcWidth * hb);
						
						// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
						BufferedImage image = new BufferedImage(deskWidth, height,
								BufferedImage.TYPE_INT_RGB);
						image.getGraphics().drawImage(img, 0, 0, deskWidth, height,
								null); // 绘制缩小后的图
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(image, imageFormat, baos);
						byte[] b = baos.toByteArray();
	
						FileOutputStream fos = new FileOutputStream(newImage);
						fos.write(b, 0, b.length);
						fos.flush();
						fos.close();
						json.put("success", true);
					}
				} catch (Exception e) {
					json.put("success", false);
				}
			}
		}
		MutliThread mt = new MutliThread(fileName, height, smallIcon);
		mt.start();
		if(isRun){
			try {
				mt.join();
			} catch (InterruptedException e) {
				log.error(e);
			}
		}
		return mt.json;
	}
	
	/**
     * 将汉字转换成拼音
     * @param hanzi 汉字
     * @param separator 分割符
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String hanziToPinyin(String hanzi, String separator)
    {
        // 创建汉语拼音处理类
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出设置，大小写，音标方式
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        String pinyingStr = "";
        try
        {
            pinyingStr = PinyinHelper.toHanyuPinyinString(hanzi, defaultFormat, separator);
        }
        catch (BadHanyuPinyinOutputFormatCombination e)
        {
            e.printStackTrace();
        }
        return pinyingStr;
    }
    
    /**
     * BASE64解码
     * @param str
     * @return
     */
    public static String decode(String str) {
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			byte[] bt = decoder.decodeBuffer(str);
			str = new String(bt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
    
    /**
     * 下载
     * @param response response
     * @param fileName 文件名
     * @param filePath 文件路径
     * @throws Exception
     */
    public static void down(HttpServletResponse response,HttpServletRequest request,String fileName,String filePath,String fileSize) throws Exception{
    	response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		String encodeFileName = new String(fileName.getBytes(), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;fileName="+encodeFileName);
		response.setHeader("Content-length", fileSize);
		byte[] b = new byte[2048];
		// 用于记录以完成的下载的数据量，单位是byte
		long downloadedLength = 0l;

		InputStream inputStream = new FileInputStream(filePath+fileName);
		// 激活下载操作
		OutputStream os = response.getOutputStream();
		// 循环写入输出流
		int length;
		try {
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
				downloadedLength += b.length;
			}
		} catch (Exception e) {
			throw new Exception();
		}finally{
			os.close();
			inputStream.close();
		}
		
	}
    
    /**  
     * bytes转换成十六进制字符串  
     * @param byte[] b byte数组  
     * @return String 每个Byte值之间空格分隔  
     */    
    public static String byte2HexStr(byte[] b)  
    {    
        String stmp="";    
        StringBuilder sb = new StringBuilder("");    
        for (int n=0;n<b.length;n++)    
        {    
            stmp = Integer.toHexString(b[n] & 0xFF);    
            sb.append((stmp.length()==1)? "0"+stmp : stmp);    
            sb.append(" ");    
        }    
        return sb.toString().toUpperCase().trim();    
    }
}

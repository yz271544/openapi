package com.teradata.openapi.framework.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

/**
 * 字符串工具类
 * <p>
 * 该类封装了字符串类型数据的常用方法，该类中的方法均为静态方法。
 * </p>
 * 
 * @author admin
 * 
 */
public class StringUtil {

	/**
	 * 将下划线分割风格的字符串转为驼峰风格的字符串（首字母小写）
	 * 
	 * <pre>
	 * StringUtil.toCamelCase(null)  = null
	 * StringUtil.capitalize("")    = ""
	 * StringUtil.toCamelCase("cat_dog") = "catDog"
	 * StringUtil.toCamelCase("cAt_DoG") = "catDog"
	 * </pre>
	 * 
	 * @param str
	 * @return
	 * @author houbl
	 */
	public static String toCamelCase(String str) {
		if (str == null) {
			return null;
		}
		return StringUtils.uncapitalize(WordUtils.capitalizeFully(str, new char[] { '_' }).replaceAll("_", ""));
	}

	/**
	 * 用指定的字符串split分割驼峰风格的字符串str
	 * 
	 * <pre>
	 * StringUtil.splitCamelCase(null, "_")  = null
	 * StringUtil.splitCamelCase("", "_")    = ""
	 * StringUtil.splitCamelCase("StringUtilTest", "_") = "String_Util_Test"
	 * StringUtil.splitCamelCase("TheCRMTest", "_") = "The_CRM_Test"
	 * </pre>
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	public static String splitCamelCase(String str, String split) {
		if (str == null) {
			return null;
		}
		return str
		        .replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), split);
	}

	/**
	 * 将驼峰风格的字符串转为下划线分割风格的字符串（全部大写）
	 * 
	 * @param str
	 * @return
	 * @author houbl
	 */
	public static String toUnderScoreCase(String str) {
		if (str == null) {
			return null;
		}
		return splitCamelCase(str, "_").toUpperCase();
	}

	/**
	 * 中文数字小写
	 */
	public static final String[] CHINESE_DIGITAL = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七",
	        "十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六", "二十七", "二十八", "二十九", "三十", "三十一", "三十二", "三十三", "三十四", "三十五", "三十六",
	        "三十七", "三十八", "三十九", "四十", "四十一", "四十二", "四十三", "四十四", "四十五", "四十六", "四十七", "四十八", "四十九", "五十" };

	/**
	 * 字符串连接时的分隔符
	 * <p>
	 * 该分隔符用于{@link #toString(Collection)} 和 {@link #toString(Collection, String)}方法。
	 * </p>
	 */
	public static final String DEFAULT_SEPARATOR = ",";

	/**
	 * 检查当前字符串是否为空
	 * <p>
	 * 如果字符串为null，或者长度为0，都被归为空。
	 * </p>
	 * 
	 * @param id 要检查的字符串
	 * @return 返回结果，true表示不为空，false表示为空
	 */
	public static boolean isEmpty(Object id) {
		if (id == null || id.toString().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 检查当前字符串是否为空
	 * <p>
	 * 如果字符串为null，或者调用{@link #java.lang.String.trim()}后长度为0，都被归为空。
	 * </p>
	 * 
	 * @param str 要检查的字符串
	 * @return 检查结果，true 为空，false不为空
	 */
	public static boolean isTrimEmpty(String str) {
		if (str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串数组转化为 字符串
	 * 
	 * @param array
	 * @return
	 */
	public static String arrayToString(Object[] array) {
		if (array == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		for (Object item : array) {
			result.append(item).append(",");
		}
		if (result.length() > 0) {
			return result.substring(0, result.length() - 1);
		}
		return "";
	}

	/**
	 * 字符串数组转化为 字符串 答案截断
	 * 
	 * @param array
	 * @return
	 */
	public static String arrayToStringAnswer(String[] array) {
		if (array == null) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		for (Object item : array) {
			result.append(item);
		}
		return result.toString();
	}

	/**
	 * 将对象解析成字符串
	 * 
	 * @param value 要解析的对象
	 * @return 解析的字符串
	 */
	public static String toJSON(Object value) {
		return null;
	}

	/**
	 * 判断当前字符串是否是由数字组成
	 * 
	 * @param str 要检查的字符串
	 * @return 结果
	 */
	public static boolean isDigit(String str) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return false;
		}
		return Pattern.matches("^[0-9]+(.[0-9]{1,2})?$", str);
	}

	/**
	 * 是否数字或字符组成
	 * 
	 * @param str
	 * @return
	 * 
	 * @author admin
	 */
	public static boolean isDigitOrLetter(String str) {
		if (StringUtil.isTrimEmpty(str)) {
			return false;
		}
		return Pattern.matches("^[0-9a-zA-Z]+$", str);
	}

	/**
	 * 判断当前字符串是否表示数字区间
	 * 
	 * @param str 要检查的字符串
	 * @return 结果
	 */
	public static boolean isDigitRange(String str) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return false;
		}
		return Pattern.matches("^\\d+-\\d+$", str);
	}

	/**
	 * 提换html的部分特殊字符
	 * <p>
	 * 只替换了&、<和>符号
	 * </p>
	 * 
	 * @param str 要替换的字符串
	 * @return 替换结果
	 */
	public static String formatHtml(String str) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return str;
		}
		// 替换特殊字符串
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		return str;
	}

	/**
	 * 替换HTML的全部特殊字符
	 * <p>
	 * 替换了&、<、>、"和空格
	 * </p>
	 * 
	 * @param str 要替换的字符串
	 * @return 替换的结果
	 */
	public static String formatAllHtml(String str) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return str;
		}
		// 替换特殊字符串
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll(" ", "&nbsp;");
		return str;
	}

	/**
	 * 将过长字符串进行截取，并在末尾追加描述符，如...
	 * 
	 * @param str 要截取的字符串
	 * @param maxLength 最大长度
	 * @param replace 追加的字符串，如果是null，则默认为...
	 * @return 截取结果
	 */
	public static String cut(String str, int maxLength, String replace) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return str;
		}
		// 检查replace是否存在
		if (replace == null) {
			replace = "...";
		}
		// 检查长度
		if (str.length() + replace.length() <= maxLength || maxLength < 1 || replace.length() > maxLength) {
			return str;
		}
		// 开始截取
		return str.substring(0, maxLength - replace.length()) + replace;
	}

	/**
	 * 将string 集合拼接成字符串，使用{@value #DEFAULT_SEPARATOR}分隔
	 * 
	 * @param list 要处理的集合
	 * @return 处理结果
	 */
	public static String toString(Collection<?> list) {
		return toString(list, null);
	}

	/**
	 * 将string 集合拼接成字符串，使用特定字符分隔
	 * 
	 * @param list 要处理的集合
	 * @param separator 分隔符，如果为null，则默认使用{@value #DEFAULT_SEPARATOR}
	 * @return 处理结果
	 */
	public static String toString(Collection<?> list, String separator) {
		if (separator == null) {
			separator = DEFAULT_SEPARATOR;
		}
		// 检查list是否存在
		if (list == null) {
			return null;
		}
		StringBuffer rs = new StringBuffer();
		Iterator<?> it = list.iterator();
		Object next = null;
		while (it.hasNext()) {
			next = it.next();
			if (next == null) {
				continue;
			}
			rs.append(next.toString());
			// 如果有下一个值，则添加分隔符
			if (it.hasNext()) {
				rs.append(separator);
			}
		}
		return rs.toString();
	}

	/**
	 * 检查输入的字符串是否为查询条件 有[ 标识
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isQueryCondition(String str) {
		// 检查是否为空
		if (StringUtil.isTrimEmpty(str)) {
			return false;
		}
		// 检查是否为查询条件
		if (str.indexOf("[") != -1) {
			return true;
		}

		return false;
	}

	/**
	 * @Title strToInt
	 * @Description 将字符串数字转换成数字
	 * @param ojb
	 * @return Integer
	 * @throws
	 */
	public static Integer strToInt(Object ojb) {
		if (isEmpty(ojb)) {
			return 0;
		}
		try {
			return Integer.valueOf(ojb.toString());
		}
		catch (Exception e) {
			return 0;
		}
	}

	public static Long strToLong(Object obj) {
		if (isEmpty(obj)) {
			return 0L;
		}
		try {
			return Long.valueOf(obj.toString());
		}
		catch (Exception e) {
			return 0L;
		}
	}

	public static Short strToShort(Object ojb) {
		if (isEmpty(ojb)) {
			return 0;
		}
		try {
			return Short.valueOf(ojb.toString());
		}
		catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @Title filterImgTag
	 * @Description 过滤字符串中的图片标签
	 * @param content
	 * @return String
	 */
	public static String filterImgTag(String content) {
		return content.replaceAll("<img.*?>", "");
	}

	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式

	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式

	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

	private static final String regEx_ = "&[a-zA-Z0-9]+;"; // 定义&nbsp;类似标签的正则表达式

	/**
	 * 删除html 标签
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTag(String htmlStr) {
		if (StringUtil.isTrimEmpty(htmlStr)) {
			return htmlStr;
		}
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_ = Pattern.compile(regEx_, Pattern.CASE_INSENSITIVE);
		Matcher m_ = p_.matcher(htmlStr);
		htmlStr = m_.replaceAll(""); // &nbsp;类似标签

		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
	 * 
	 * @param c 需要判断的字符
	 * @return 返回true,Ascill字符
	 */
	public static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param s 需要得到长度的字符串
	 * @return i得到的字符串长度
	 */
	public static int length(String s) {
		if (s == null) {
			return 0;
		}
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	/**
	 * 截取一段字符的长度,支持中文(中文占2个字符),如果数字不正好，则少取一个字符位
	 * 
	 * @param origin 原始字符串
	 * @param len 截取长度(一个汉字长度按2算的)
	 * @param c 后缀
	 * @return 返回的字符串
	 */
	public static String substring(String origin, int len, String c) {
		if (origin == null || origin.equals("") || len < 1) {
			return "";
		}
		byte[] strByte = new byte[len];
		if (len > length(origin)) {
			return origin;
		}
		try {
			System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if (count % 2 != 0) {
				len = (len == 1) ? ++len : --len;
			}
			return new String(strByte, 0, len, "GBK") + c;
		}
		catch (Exception e) {
			// LOGGER.error("origin=" + origin, e);
			return origin;
		}
	}

	/**
	 * 将数组已,号连接成字符串
	 * 
	 * @param obj 要连接的数组，如果是string则直接返回
	 * @return
	 */
	public static String join(Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof String) {
			return obj.toString();
		} else if (obj.getClass().isArray()) {
			StringBuffer s = new StringBuffer();
			Object[] list = (Object[]) obj;
			for (Object o : list) {
				s.append(o.toString() + ",");
			}
			if (s.length() > 0) {
				return s.deleteCharAt(s.length() - 1).toString();
			}
		}
		return null;
	}

	/**
	 * 功能:替换字符串中的\t\n以及前后空格
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceTN(String str) {
		if (str == null) {
			return str;
		}
		return str.replaceAll("\t|\n", "").trim();
	}

	/**
	 * 处理富文本编辑器产生的文本<br/>
	 * 将数据源中的特殊字符（以<开头 >结尾，或空格）全部截除
	 * 
	 * @param source 数据源
	 * @return 结果
	 */
	public static String trimHtml(String source) {
		if (StringUtil.isTrimEmpty(source)) {
			return null;
		}
		return source.replaceAll("[\\s]+", "").replaceAll("<[^>]*>", "").trim();
	}

	public static String formatDigital(Integer digital) {
		return CHINESE_DIGITAL[digital - 1];
	}

	/**
	 * 功能:数据精度格式化
	 * 
	 * @param o 只能为数字
	 * @param precision格式精度
	 * @return
	 */
	public static String formatNumber(Object o, Integer precision) {
		if (o == null) {
			return "";
		}
		DecimalFormat df = null;
		try {
			switch (precision) {
				case 0:
					df = new DecimalFormat("#######");
					break;
				case 1:
					df = new DecimalFormat("#######.#");
					break;
				case 2:
					df = new DecimalFormat("#######.##");
					break;
				case 3:
					df = new DecimalFormat("#######.###");
					break;
				default:
					df = new DecimalFormat("#######.##");
					break;
			}
			return df.format(Double.parseDouble(o.toString()) + 0.00000001d);
		}
		catch (Exception e) {
			return "0";
		}
	}

	public static Float formatNumber(Float number) {
		if (number == null) {
			return 0f;
		}
		return number;
	}

	/**
	 * 将String类原生的IndexOf提供给前台使用
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public static int indexOf(String parent, String child) {
		return parent.indexOf(child);
	}

	/**
	 * 将String类原生的IndexOf提供给前台使用
	 * 
	 * @param parent
	 * @param child
	 * @return
	 */
	public static String substring(String str, int beginIndex, int endIndex) {
		if (endIndex != 0) {
			return str.substring(beginIndex, endIndex);
		} else {
			return str.substring(beginIndex);
		}
	}

	/**
	 * @Title compare
	 * @Description 字符串比较
	 */
	public static int compare(String s1, String s2) {
		return s1.compareTo(s2);
	}

	public static int calc(String week, String num) {
		return Integer.parseInt(week) + Integer.parseInt(num);
	}

	/**
	 * @Title specialSub
	 * @Description 人力资源管理师,心理咨询师,会计从业资格,司法考试,注册会计师， 类似这种类别处理，从第二个逗号截取，余下的加...
	 */
	public static String specialSub(String str) {
		if (StringUtil.isEmpty(str)) {
			return "";
		}
		if (str.indexOf(",") == -1) {
			return str;
		}
		String[] arr = str.split(",");
		if (arr.length == 2) {
			return str;
		}
		return arr[0].concat(",").concat(arr[1]).concat("...");
	}

	/**
	 * @Title isBlank
	 * @Description 字符串是否为空白
	 */
	public static boolean isBlank(String str) {
		if (null == str) {
			return true;
		}
		if ("".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * @Title toString
	 * @Description 对象toString
	 */
	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString();
	}

	/**
	 * @Title restrictLength
	 * @Description 字符串截取最大长度
	 */
	public static String restrictLength(String strSrc, int iMaxLength) {
		if (strSrc == null) {
			return null;
		}
		if (iMaxLength <= 0) {
			return strSrc;
		}
		String strResult = strSrc;
		byte[] b = null;
		int iLength = strSrc.length();
		if (iLength > iMaxLength) {
			strResult = strResult.substring(0, iMaxLength);
			iLength = iMaxLength;
		}
		while (true) {
			b = strResult.getBytes();
			if (b.length <= iMaxLength) {
				break;
			}
			iLength--;
			strResult = strResult.substring(0, iLength);
		}
		return strResult;
	}

	/**
	 * 获取指定长度(按字节长度获取)的字符串，中文算2个字节长度，兼容oracle的 varchar2长度计算方式
	 * 
	 * @param src 源字符串
	 * @param length 长度
	 * @return
	 */
	public static String getSubStr(String src, int length) {
		if (StringUtil.isEmpty(src)) {
			return null;
		}
		byte[] b = src.getBytes();
		if (b.length > length) {
			byte[] s = new byte[length];
			for (int i = 0; i < length; i++) {
				s[i] = b[i];
			}
			return new String(s);
		} else {
			return src;
		}
	}

	/**
	 * 将String型的List以分隔符拼接为字符串
	 * 
	 * @param list
	 * @param separator
	 * @return 当传入空数组时返回空串""
	 * @author 龚长江
	 * @date 2014-6-11 下午5:02:01
	 */
	public static String getJoinStrfromList(List<String> list, String separator) {
		return (list != null && list.size() > 0) == true ? StringUtils.join(list, separator) : "";
	}

	/**
	 * 将带有分隔符的字符串还原回String型List
	 * 
	 * @param str
	 * @param separator
	 * @return 当传入空串时返回长度为0的数组
	 * @author 龚长江
	 * @date 2014-6-11 下午5:01:06
	 */
	public static List<String> getListFromJoinStr(String str, String separator) {
		return (StringUtils.isNotBlank(str)) == true ? Arrays.asList(StringUtils.split(str, separator)) : new ArrayList<String>();
	}

	/**
	 * 将str第i位(从1开始)改为ch
	 * 
	 * <pre>
	 * StringUtil.replace(null, 0)  = null
	 * StringUtil.replace("", "_")    = ""
	 * StringUtil.replace("StringUtilTest", "_") = "String_Util_Test"
	 * StringUtil.replace("TheCRMTest", "_") = "The_CRM_Test"
	 * </pre>
	 * 
	 * @param str
	 * @param index
	 * @return
	 * @author 龚长江
	 * @date 2014-7-5 下午3:31:33
	 */
	public static String replace(String str, int index, String ch) {
		return str == null ? null : (str.length() < index || index < 1 ? str : (str.substring(1, index) + (ch == null ? "" : ch) + str
		        .substring(index, str.length())));
	}

	/**
	 * 将类似"101111000010"的两个String做与(type=2)、或(type=1)操作
	 * 
	 * @param sourceStr
	 * @param targetStr
	 * @param type
	 * @return 返回入参长度，补足补0
	 * @author quhl
	 * @date 2014年6月12日 下午5:04:02
	 */
	public static String getBinaryStr(String sourceStr, String targetStr, int type) {
		long source = Long.parseLong(sourceStr, 2);
		long target = Long.parseLong(targetStr, 2);
		int sourceLength = sourceStr.length();
		int targetLength = targetStr.length();

		if (sourceLength != targetLength) {
			return null;
		}

		long grp;
		if (type == 1) {
			// 或操作
			grp = source | target;
		} else if (type == 2) {
			// 与操作
			grp = source & target;
		} else {
			return null;
		}

		String src = Long.toBinaryString(grp);
		int srcLength = src.length();

		if (sourceLength > srcLength) {
			for (int i = 0; i < sourceLength - srcLength; i++) {
				src = "0" + src; // 在不足的位数前都加“0”
			}
		}
		return src;
	}

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 * @author 龚长江
	 * @date 2014-8-1 下午3:33:27
	 */
	public static String initialUpper(String str) {
		if (str == null || str.length() < 1 || !isLetter(str.charAt(0))) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

	/**
	 * 判断字符串是否全是数字
	 * 
	 * @param str
	 * @return
	 * @author lihuij
	 * @date 2014-10-14 上午9:33:27
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static int stringNumbers(String str, String reg) {
		int num = 0;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		while (m.find()) {
			num++;
		}
		return num;
	}
}

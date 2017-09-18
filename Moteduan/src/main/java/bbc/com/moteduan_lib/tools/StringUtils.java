package bbc.com.moteduan_lib.tools;


public class StringUtils {
	public static final String EMPTY_STRING = "";

	/**
	 * 判断字符串是否是手机号码,以联通或移动的开头
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isTel(String phone) {
		if (phone == null || phone.length() != 11) {
			return false;
		}
		return true;
	}

	/**
	 * 截取手机号码
	 * 
	 * @param number
	 * @return
	 */
	public static String splitePhoneNumber(String number) {
		String mobile = "";
		if (number != null && number.length() >= 11) {
			mobile = number.substring(number.length() - 11);
			// Logger.logD("StringUtils", "number" + number + "mobile:" +
			// mobile);
		}
		return mobile;
	}

	/**
	 * 字符串工具类 功能： 1、判断字符串是否为空 2、判断是否是有效的电话号码 3、替换字符串 4、转换url 5、字符串分割 6、字符串分行
	 * 7、判断字符串是否是数字 8、转换密码
	 */
	private StringUtils() {

	}

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.equals("null") || str.equals("")
				|| str.equals("null null") || str.equals("{}"));//
	}

	public static boolean isNullOrEmpty(Object value) {
		return (value == null) || ("".equals(value)) || ("null".equals(value));
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String encodeURl(String url) {
		try {
			// url = new String(url.getBytes("iso-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		/***************** ******************/
		String protocol = url.substring(0, url.indexOf("//") + 2);
		String paramString = url.substring(url.indexOf("?"), url.length());
		url = url.substring(url.indexOf(protocol) + protocol.length(),
				url.length() - paramString.length());

		String[] tempUrlArray = url.split("/");
		url = protocol;
		for (int i = 0; i < tempUrlArray.length; i++) {
			if (tempUrlArray[i].indexOf("?") == -1) {
				url += java.net.URLEncoder.encode(tempUrlArray[i]);
				if (i < tempUrlArray.length - 1)
					url += "/";

			} else {
				if (!url.endsWith("/"))
					url += "/" + tempUrlArray[i];
				else
					url += tempUrlArray[i];
			}
		}

		url += paramString;

		/***************** ******************/
		int paramIndex = url.indexOf("?");
		if (paramIndex != -1) {
			StringBuffer urlLink = new StringBuffer(
					url.substring(0, paramIndex));
			String[] paramters = url.substring(paramIndex + 1, url.length())
					.split("&");

			if (paramters.length > 0) {
				urlLink.append("?");
			}

			for (int i = 0; i < paramters.length; i++) {
				String[] valueName = paramters[i].split("=");
				urlLink.append(java.net.URLEncoder.encode(valueName[0]));

				if (paramters[i].indexOf("=") != -1)
					urlLink.append("=");

				if (valueName.length > 1) {
					urlLink.append(java.net.URLEncoder.encode(valueName[1]));
				}

				if (i < paramters.length - 1) {
					urlLink.append("&");
				}
			}

			url = urlLink.toString();
		} else {
			String paramStr = url.substring(url.lastIndexOf("/") + 1,
					url.length());
			StringBuffer urlLink = new StringBuffer(url.substring(0,
					url.lastIndexOf("/")));
			if (paramStr.indexOf("&") == -1) {
				return urlLink + "/" + java.net.URLEncoder.encode(paramStr);
			}

		}

		return url;
	}

	public static String getHttp(String str) {
		System.out.println("htptp" + str);

		try {
			str = new String(str.getBytes("iso-8859-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		java.util.StringTokenizer t = new java.util.StringTokenizer(str, "/");
		String str1 = "";
		int p = t.countTokens();
		while (t.hasMoreElements()) {
			String pt = t.nextToken();
			int flage = 0;
			p--;
			if (pt.indexOf("&") >= 0 || pt.indexOf("=") >= 0) {
				flage = 1;
				if (pt.indexOf("&") >= 0) {
					java.util.StringTokenizer t2 = new java.util.StringTokenizer(str, "&");
					int p2 = t2.countTokens();
					while (t2.hasMoreTokens()) {
						p2--;
						String tr = t2.nextToken();
						if (tr.indexOf("=") >= 0) {
							java.util.StringTokenizer t3 = new java.util.StringTokenizer(str, "=");
							int p3 = t3.countTokens();
							while (t3.hasMoreElements()) {
								p3--;
								str1 += urlEncode(t3.nextToken());
								if (p3 > 0) {
									str1 += "=";
								}
							}
						} else {
							str1 += urlEncode(tr);

						}
						if (p2 > 0) {
							str1 += "&";
						}

					}
				} else if (pt.indexOf("=") >= 0) {
					java.util.StringTokenizer t2 = new java.util.StringTokenizer(str, "=");
					int p4 = t2.countTokens();
					while (t2.hasMoreTokens()) {
						p4--;
						String tu = t2.nextToken();
						System.out.println("tu" + tu);
						str1 += urlEncode(tu);
						if (p4 > 0) {
							str1 += "=";
						}

					}

				}

			}
			if (flage == 0) {
				str1 += urlEncode(pt) + "/";

			}
			if (p > 0) {
				str1 += "/";
			}
			// System.out.println("WEWEYW"+pt);
		}
		System.out.println("str1" + str);
		return str1;
	}

	/**
	 * 替换str中的str1，替换为str2
	 */
	public static String replaceString(String str, String str1, String str2) {
		if (isEmpty(str) || isEmpty(str1)) {
			return str;
		}

		if (str1.equals(str2)) {
			return str;
		}

		StringBuffer temp = new StringBuffer(str);

		int post = temp.toString().indexOf(str1);

		while (post > -1) {
			temp.delete(post, post + str1.length());
			temp.insert(post, str2);
			post = temp.toString().indexOf(str1);
		}
		return temp.toString();
	}

	/**
	 *
	 */
	public static String formatString(String str, String[] temp) {
		StringBuffer sb = new StringBuffer(str);
		int post = -1;
		for (int i = 0; i < temp.length; i++) {
			post = sb.toString().indexOf("%S");
			if (post > -1) {
				sb.delete(post, post + 2);
				sb.insert(post, temp[i]);
			}
		}
		return sb.toString();
	}

	// //////////////////////////////////////////////////////////////////////////
	// ///
	public static String[] parserUrl(String url) {
		String[] str = new String[3];
		int poste = url.indexOf("?");
		int post = url.indexOf("http://");
		int postl = 0;
		if (post > -1) {
			postl = url.substring(post + 7).indexOf("/");
			if (postl > -1) {
				str[0] = "http://"
						+ url.substring(post + 7, post + 8 + postl - 1);
				if (poste < 0) {
					str[1] = url.substring(post + 8 + postl - 1);
				} else {
					str[1] = url.substring(post + 8 + postl - 1, poste);
					str[2] = url.substring(poste);
				}
				return str;
			}
		}
		return null;
	}

	public static String checkPushPage(String data, String url) {
		String[] str = StringUtils.parserUrl(url);
		if (str != null) {
			int post = data.indexOf(str[1]);
			int post1 = 0;
			if (post > -1) {
				post1 = data.substring(post + 1).indexOf("\"");
				if (post1 > -1) {
					return str[0] + data.substring(post, post + post1 + 1);
				}
			}
		}
		return url;
	}

	/**
	 * ת��UTF-8
	 *
	 * @param arg0
	 * @return
	 */
	public static String urlEncode(String arg0) {
		if (StringUtils.isEmpty(arg0)) {
			return "";
		}
		StringBuffer out = new StringBuffer();
		java.io.ByteArrayOutputStream bOut = new java.io.ByteArrayOutputStream();
		java.io.DataOutputStream dOut = new java.io.DataOutputStream(bOut);
		try {
			dOut.writeUTF(arg0);
			java.io.ByteArrayInputStream bIn = new java.io.ByteArrayInputStream(
					bOut.toByteArray());
			bIn.read();
			bIn.read();
			int c = bIn.read();
			while (c >= 0) {
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
						|| (c >= '0' && c <= '9') || c == '.' || c == '-'
						|| c == '*' || c == '_')
					out.append((char) c);
				else if (c == ' ')
					out.append('+');
				else {
					if (c < 128) {
						appendHex(c, out);
					} else if (c < 224) {
						appendHex(c, out);
						appendHex(bIn.read(), out);
					} else if (c < 240) {
						appendHex(c, out);
						appendHex(bIn.read(), out);
						appendHex(bIn.read(), out);
					}
				}
				c = bIn.read();
			}
		} catch (Exception e) {
		}
		return out.toString();
	}

	private static void appendHex(int arg0, StringBuffer buff) {
		buff.append('%');
		if (arg0 < 16)
			buff.append('0');
		buff.append(Integer.toHexString(arg0));
	}


	public static String[] split(String original, String regex) {
		int startIndex = 0;
		java.util.Vector<String> v = new java.util.Vector<String>();
		String[] str = null;
		int index = 0;

		startIndex = original.indexOf(regex);
		while (startIndex < original.length() && startIndex != -1) {
			String temp = original.substring(index, startIndex);
			v.addElement(temp);
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
		v.addElement(original.substring(index + 1 - regex.length()));
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		return str;
	}

	/**
	 *
	 * @param text
	 *            String
	 * @return boolean
	 */
	public static boolean isNumeric(String text) {
		// Logger.systemOut(tab);
		char[] chars = text.toCharArray();
		if (chars.length == 0) {
			return false;
		}
		int start = 0;
		if (chars[0] == '-') {
			start = 1;
		}
		boolean dotEncountered = false;
		for (int i = start; i < chars.length; i++) {
			char c = chars[i];
			if (Character.isDigit(c)) {
				// that's okay
			} else if (!dotEncountered && c == '.') {
				dotEncountered = true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static String parsePassword(String pw) {
		if (pw == null)
			return null;

		int size = pw.length();
		if (size == 0)
			return null;

		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < size; i++) {
			buff.append("*");
		}
		return buff.toString();
	}

	private static android.graphics.Paint paint;
	private static android.graphics.Rect rect;

	public static int getTextWidth(char text, float size) {
		if (paint == null) {
			paint = new android.graphics.Paint();
		}
		paint.setTextSize(size);
		if (rect == null) {
			rect = new android.graphics.Rect();
		}
		paint.getTextBounds(new char[] { text }, 0, 1, rect);
		int w = rect.width();

		return w;
	}

	public static java.util.Vector<String> parseString(String s, float textsize, float w) {
		if (s == null || s.equals("")) {
			return null;
		}

		java.util.Vector<String> messages = new java.util.Vector<String>();
		int totalLine = 1;
		java.util.Vector<Integer> vLine = new java.util.Vector<Integer>();
		vLine.addElement(new Integer(0));
		int currentLen = 0;
		int singleWordLen = 0;
		char[] charAry = s.toCharArray();
		int lastSpaceIndex = 0;
		int lineLen = 0;
		for (int i = 0; i < charAry.length; ++i) {

			singleWordLen = getTextWidth(charAry[i], textsize);
			if (singleWordLen == 0) {
				singleWordLen = (int) ((textsize) / 2 + 1);
			}
			if (charAry[i] == '\n' || charAry[i] == '\r' || charAry[i] == '\t') {
				vLine.addElement(new Integer((i - 1)));
				currentLen = 0;
				++totalLine;
				lastSpaceIndex = i + 1;
			} else if (charAry[i] == '\\'
					&& ((i + 1) < charAry.length)
					&& (charAry[i + 1] == 'n' || charAry[i + 1] == 'r' || charAry[i + 1] == 't')) {
				vLine.addElement(new Integer((i - 1)));
				currentLen = 0;
				++totalLine;
				++i;
				lastSpaceIndex = i + 1;
			} else if (currentLen + singleWordLen > w - textsize * 2) {
				if (lineLen > 0) {
					vLine.addElement(new Integer(lastSpaceIndex));
					currentLen = currentLen + singleWordLen - lineLen;
					lineLen = 0;
				} else {
					vLine.addElement(new Integer(i));
					currentLen = 0;
					lineLen = 0;
					lastSpaceIndex = i + 1;
				}
				++totalLine;
			} else {
				if ((((charAry[i] & 0xFF00) > 0) || charAry[i] == ' '
						|| charAry[i] == '-' || charAry[i] == '\t' || charAry[i] == '\r')
						&& i != 0) {
					lastSpaceIndex = i;
					lineLen = currentLen + singleWordLen;
				}
				if (charAry[i] != ' ' && charAry[i] != '-'
						&& charAry[i] != '\r') {
					currentLen += singleWordLen;
				} else {
					currentLen += singleWordLen / 2;
				}
			}
		}
		vLine.addElement(new Integer((s.length() - 1)));

		int startIndex;
		int lastIndex;

		for (int i = 0; i < totalLine; ++i) {
			if (i < totalLine - 1) {
				lastIndex = ((Integer) (vLine.elementAt((i + 1)))).intValue();
			} else {

				lastIndex = s.length() - 1;
			}

			if (i > 0) {
				startIndex = ((Integer) (vLine.elementAt((i)))).intValue() + 1;
			} else {
				startIndex = 0;
			}
			if (startIndex <= charAry.length - 1) {
				if (charAry[startIndex] == '\n' || charAry[startIndex] == '\t'
						|| charAry[startIndex] == '\r') {
					++startIndex;
				} else if (charAry[startIndex] == '\\'
						&& ((startIndex + 1) < charAry.length)
						&& (charAry[startIndex + 1] == 'n'
								|| charAry[startIndex + 1] == 't' || charAry[startIndex + 1] == 'r')) {
					startIndex += 2;
				}
			}

			if (startIndex > lastIndex) {
				messages.addElement("");
			} else {
				messages.addElement(s.substring(startIndex, lastIndex + 1)
						.trim());
			}
		}
		System.gc();
		return messages;
	}

	/**
	 * 截取字符串
	 *
	 * @param arrStr
	 * @param mSplit
	 *            截取表示 ，注意 转义 \\
	 * @param i
	 * @return
	 */
	public static String getSplitStr(String arrStr, String mSplit, int i) {

		String result = "";
		if (arrStr != null && !"".equals(arrStr)) {
			String[] tempStr = arrStr.split(mSplit);
			if (tempStr != null && tempStr.length > i) {
				result = tempStr[i];
			}
		}
		return result;
	}

	/**
	 * 检查是否合法的电话号码
	 *
	 * @param phoneNumber
	 * @return
	 *//*
	public static boolean isPhoneNumberValid(String phoneNumber) {

		boolean isValid = false;
		String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";
		String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";
		CharSequence inputStr = phoneNumber;

		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(expression);

		java.util.regex.Matcher matcher = pattern.matcher(inputStr);

		java.util.regex.Pattern pattern2 = java.util.regex.Pattern.compile(expression2);

		java.util.regex.Matcher matcher2 = pattern2.matcher(inputStr);

		if (matcher.matches() || matcher2.matches()) {
			isValid = true;
		}

		return isValid;

	}
*/
	/**
	 * 得到文件名
	 *
	 * @param url
	 * @return
	 */
	public static String getFileNa(String url) {
		return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
	}

	/**
	 * 得到文件後綴名
	 *
	 * @param url
	 * @return
	 */
	public static String getFileEx(String url) {
		return url.substring(url.lastIndexOf("."));
	}

	/**
	 * 把字串(通常用于中文)转成16进制字符串
	 *
	 * @return null if given str is null 如: 37efd
	 * */
	public static String getHex(String str) {
		if (str == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0, j = str.length(); i < j; ++i) {
			sb.append(Integer.toHexString(str.charAt(i)));
		}
		return sb.toString();
	}

	public static void printLog(int logLevel, String tag, String log,
								Exception e) {
		StringBuffer strB = new StringBuffer();
		switch (logLevel) {
		case android.util.Log.VERBOSE:
			if (e == null) {
				android.util.Log.v(tag, log);
			} else {
				android.util.Log.v(tag, log, e);
			}
			break;

		case android.util.Log.DEBUG:

			if (e == null) {
				android.util.Log.d(tag, log);
			} else {
				android.util.Log.d(tag, log, e);
			}

			break;

		case android.util.Log.INFO:

			if (e == null) {
				android.util.Log.i(tag, log);
			} else {
				android.util.Log.i(tag, log, e);
			}

			break;

		case android.util.Log.WARN:

			if (e == null) {
				android.util.Log.w(tag, log);
			} else {
				android.util.Log.w(tag, log, e);
			}

			break;

		case android.util.Log.ERROR:

			if (e == null) {
				android.util.Log.e(tag, log);
			} else {
				android.util.Log.e(tag, log, e);
			}

			break;

		default:

			if (e == null) {
				android.util.Log.d(tag, log);
			} else {
				android.util.Log.d(tag, log, e);
			}
		}
	}

	public static String urlEncoder(String str) {
		String ret = "";
		try {
			ret = java.net.URLEncoder.encode(str, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public static void showShortToast(android.content.Context ctx, String message) {
		android.widget.Toast.makeText(ctx, message, android.widget.Toast.LENGTH_LONG).show();
	}

	/**
	 * 在val不为空的情况下，是否是有效的数字
	 *
	 * @param val
	 * @return 有效的數字 返回 true 否則為false
	 */

	public static boolean isValNumeric(String val) {
		if (!isEmpty(val)) {
			if (!isNumeric(val)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符是否中文
	 *
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		String chinese = "[\u0391-\uFFE5]";
		if (str.matches(chinese)) {
			return true;
		}
		return false;
	}

	public static boolean isChineseForLastChar(String lastStr) {
		if (!isNullOrEmpty(lastStr)) {
			String temp = lastStr.substring(lastStr.length() - 1);
			if (isChinese(temp)) {
				return true;
			}
		} else {// 为空的时候不加后缀字
			return true;
		}
		return false;
	}

	/**
	 * 如果是0 则返回 空 integer 转化为 字符串
	 *
	 * @param val
	 * @return
	 */
	public static String convertInteger2String(int val) {
		if (val == 0) {
			return "";
		}
		return String.valueOf(val);
	}

	public static String convertDouble2String(double val) {
		if (val == 0) {
			return "";
		}
		return String.valueOf(val);
	}

	public static int convertString2Integer(String val) {
		int result = 0;
		if (isNullOrEmpty(val))
			return result;
		try {
			result = Integer.parseInt(val);
		} catch (NumberFormatException e) {
		}

		return result;
	}

	/**
	 * 转换成 Double
	 *
	 * @param val
	 * @return
	 */
	public static double convertString2Double(String val) {
		double result = 0;
		if (isNullOrEmpty(val))
			return result;
		try {
			result = Double.parseDouble(val);
		} catch (NumberFormatException e) {
		}

		return result;
	}

	/**
	 * 格式化手机号
	 */
	public static String getFormatPhoneNum(String phoneStr) {
		String result = "";
		if (!StringUtils.isNullOrEmpty(phoneStr) && phoneStr.length() > 8) {
			String strStart = phoneStr.substring(0, 4);
			String strEnd = phoneStr.substring(phoneStr.length() - 4,
					phoneStr.length());
			return strStart + add0(phoneStr.length() - 8) + strEnd;
		}
		return result;

	}

	/**
	 * 格式化银行卡号
	 */
	public static String getFormatCardNumber(String phoneStr) {
		String result = "";
		if (!StringUtils.isNullOrEmpty(phoneStr) && phoneStr.length() > 8) {
			String strStart = phoneStr.substring(0, 6);
			String strEnd = phoneStr.substring(phoneStr.length() - 4,
					phoneStr.length());
			return strStart + add0(phoneStr.length() - 10) + strEnd;
		}
		return result;

	}

	private static String add0(int length) {
		StringBuilder mBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			mBuilder.append("*");
		}
		return mBuilder.toString();
	}

	public static boolean isUpdate(String oldVersion, String newVersion) {
		oldVersion = oldVersion.replace(".", "");
		newVersion = newVersion.replace(".", "");
		try {
			if (Integer.valueOf(newVersion) > Integer.valueOf(oldVersion)) {
				return true;
			}
		} catch (Exception e) {
			// Logger.exceptionOut(e);
		}
		return false;
	}

	/**
	 * 格式化版本号(200->2.0.0)
	 *
	 * @param versionCode
	 * @return
	 */
	public static String formatVersionCode(String versionCode) {
		char[] c = versionCode.toCharArray();
		StringBuilder s = new StringBuilder();
		s.append(c[0]);
		for (int i = 1; i < c.length; i++) {
			s.append(".");
			s.append(c[i]);
		}
		return s.toString();
	}

	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(str);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	// public static boolean isEmail(String email) {
	// String str =
	// "^[a-zA-Z][a-zA-Z0-9._-]*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
	// Pattern p = Pattern.compile(str);
	// Matcher m = p.matcher(email);
	// return m.matches();
	// }

	public static String getAssestFile2Str(android.content.Context mContext, String filePath) {
		try {
			java.io.InputStream mInputStream = mContext.getAssets().open(filePath);
			java.io.BufferedReader mBufferedReader = new java.io.BufferedReader(
					new java.io.InputStreamReader(mInputStream, "UTF-8"));
			StringBuffer mStringBuffer = new StringBuffer();
			String line = "";
			String nl = System.getProperty("line.separator");
			while ((line = mBufferedReader.readLine()) != null) {
				// Logger.systemOut("line->"+line);
				mStringBuffer.append(line + nl);
			}
			mBufferedReader.close();
			return mStringBuffer.toString();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 字符串去空格、换行符等
	 *
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\s*|\t|\r|\n");
			java.util.regex.Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/*
	 * public static String stringToAscii(String value) { StringBuffer sbu = new
	 * StringBuffer(); char[] chars = value.toCharArray(); for (int i = 0; i <
	 * chars.length; i++) { if (i != chars.length - 1) { sbu.append((int)
	 * chars[i]).append(","); } else { sbu.append((int) chars[i]); } } return
	 * sbu.toString(); }
	 */

	public static String getAsciiFromStr(String s) {
		String[] temp = new String[s.length()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = s.substring(i, i + 1);
		}
		char[] chars = s.toCharArray();
		String t = "";
		for (int i = 0; i < chars.length; i++) {
			if (char2ASCII(chars[i]) >= 19968 && char2ASCII(chars[i]) <= 40869) {// 判断是否为汉字
				try {
					t += java.net.URLEncoder.encode(temp[i], "GB2312").replaceAll("%",
							"");
				} catch (java.io.UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				String tem = Integer.toHexString(char2ASCII(chars[i]));
				t += tem;
			}
		}
		return t;
	}

	public static int char2ASCII(char c) {
		return (int) c;
	}
}

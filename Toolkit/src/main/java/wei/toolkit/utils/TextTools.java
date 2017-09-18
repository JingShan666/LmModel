package wei.toolkit.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.URLSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class TextTools {
    private static final String TAG = "TextTools";

    /**
     * @param input
     * @return 半角转全角
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    public static class Url {
        private static final String[] urlRegexResourceArray = {
                "top", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc", "tv", "info",
                "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "coop", "aero", "ad", "ae", "af",
                "ag", "ai", "al", "am", "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd",
                "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br", "bs", "bt", "bv", "bw", "by", "bz",
                "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
                "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi",
                "fj", "fk", "fm", "fo", "fr", "ga", "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp",
                "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id", "ie", "il", "in", "io",
                "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw",
                "ky", "kz", "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md",
                "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz",
                "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om", "qa", "pa",
                "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw",
                "sa", "sb", "sc", "sd", "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st",
                "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk", "tm", "tn", "to", "tp", "tr", "tt",
                "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
                "ye", "yu", "za", "zm", "zr", "zw"
        };
        private static StringBuilder urlRegexResourceStringBuilder = new StringBuilder();
        private static String urlRegexResourcePattern = "";

        /**
         * 如果文本中有链接,把链接显示的文本替换成自定义的文本
         *
         * @param content
         * @param replaceText
         */
        public static SpannableStringBuilder replaceUrlToText(String content, String replaceText){
            if (TextUtils.isEmpty(content)) return null;
            if (TextUtils.isEmpty(replaceText)) return null;
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
            if (urlRegexResourceStringBuilder.length() < 1) {
                urlRegexResourceStringBuilder.append("(");
                for (int i = 0; i < urlRegexResourceArray.length; i++) {
                    urlRegexResourceStringBuilder.append(urlRegexResourceArray[i]);
                    urlRegexResourceStringBuilder.append("|");
                }
                urlRegexResourceStringBuilder.deleteCharAt(urlRegexResourceStringBuilder.length() - 1);
                urlRegexResourceStringBuilder.append(")");
                urlRegexResourcePattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.]+\\.)?[a-zA-Z0-9\\-]+\\."
                        + urlRegexResourceStringBuilder.toString()
                        + "(:[0-9]{1,5})?))((/[a-zA-Z0-9\\./,;\\?'\\+&%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&%\\$#=~_\\-]*))";
            }

            Pattern pattern = Pattern.compile(urlRegexResourcePattern);
            Matcher matcher = pattern.matcher(spannableStringBuilder);
            int sizeOffset = 0;
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String url = matcher.group();
                int urlLength = url.length();
                int replaceTextLength = replaceText.length();
                Loger.log(TAG, "matcher.group : " + url + " matcher.start() : " + start + " matcher.end() : " + end);
                spannableStringBuilder.replace(start - sizeOffset, end - sizeOffset, replaceText);
                spannableStringBuilder.setSpan(new URLSpan(url), start - sizeOffset, start - sizeOffset + replaceTextLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sizeOffset = urlLength - replaceTextLength;
            }

            return spannableStringBuilder;

        }

        public static SpannableStringBuilder replaceUrlToText(String content){
            return replaceUrlToText(content, "网页链接");
        }
    }

}

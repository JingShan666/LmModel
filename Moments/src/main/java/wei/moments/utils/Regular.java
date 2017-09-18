package wei.moments.utils;

public class Regular {

    private static final String reg = "[\u4e00-\u9fa5]";//汉字正则表达式

    /**
     * 过滤汉字： 123好和456 --> 123_456
     * @param chineseCharacters
     */
    public static String FilteringChineseCharacters(String chineseCharacters) {

        java.util.regex.Pattern pat = java.util.regex.Pattern.compile(reg);
        java.util.regex.Matcher mat = pat.matcher(chineseCharacters);

        String repickStr = mat.replaceAll("_");

        return repickStr;
    }


//    public static void main(String[] args) {
//
//        String str = "123收到过包efc";
//        String reg = "[\u4e00-\u9fa5]";
//
//        java.util.regex.Pattern pat = java.util.regex.Pattern.compile(reg);
//        java.util.regex.Matcher mat = pat.matcher(str);
//
//        String repickStr = mat.replaceAll("");
//
//        System.out.println(repickStr);
//    }




}

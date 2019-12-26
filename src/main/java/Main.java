import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        new WebSiteUrl("http://192.168.31.168:4000/");
//        if (!getHost("https://hexo.io/").equals(getHost("https://lab508.gitee.io"))){
//            System.out.println("a");
//        }
//        if ("./page/add.html".substring(0, 1).equals("#")) {
//            System.out.println("a");
//        }
//        System.out.println(isHttpUrl("https://github.com/508lab/ForeignApi/issues"));


    }


    public static String getHost(String link){
        String host = "";
        URL url;
        try {
            url = new URL(link);
            host = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }

    public static boolean isHttpUrl(String url){
        String regex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,5})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(url.trim());
        return mat.matches();
    }

}

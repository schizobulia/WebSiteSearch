import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSiteUrl {
    private String url;
    private ArrayList<String> listUrl = new ArrayList<String>();
    public WebSiteUrl(String url){
        super();
        this.url = url;
        this.getPageUrl(this.url);
        for (int i = 0; i < listUrl.size(); i++) {
            System.out.println(listUrl.get(i));
        }
    }

    public void getPageUrl(String u){
        try {
            Document doc = Jsoup.connect(u).get();
            Elements aList = doc.select("a");
            for (Element ele: aList) {
                String href = ele.attr("href");
                if (this.isGetUrl(href)){
                    if (!listUrl.contains(this.url + href)){
                        listUrl.add(this.url + href);
                        this.getPageUrl(this.url + href);
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
        }
    }



    /**
     * 去除不合格的Url地址
     * @param href
     * @return
     */
    public boolean isGetUrl(String href){
        if (href.equals("") || href.equals("/") || href.equals("#")
                || href.equals("javascript:;") || href.substring(0, 1).equals("#")){
            return false;
        }
        if (this.isHttpUrl(href) ){
            if (href.equals(this.url)){
                return false;
            }
            if (!this.getHost(href).equals(this.getHost(this.url)) ){
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否是Url
     * @param url
     * @return
     */
    public boolean isHttpUrl(String url){
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

    public String getHost(String link){
        String host = "";
        URL url;
        try {
            url = new URL(link);
            host = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return host;
    }
}

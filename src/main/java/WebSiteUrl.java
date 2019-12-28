import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSiteUrl {
    private String url;
    private ArrayList<String> listUrl = new ArrayList<String>();
    public String path = "";  //数据存储路径

    public int count = 0;
    private String str = "";
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     *
     * @param url  需要获取的Url地址
     * @param path 数据存储地址
     */
    public WebSiteUrl(String url, String path){
        super();
        this.url = url;
        this.path = path;
        init(url);
        fixedThreadPool.execute(new TaskThread(this.path, str));
        fixedThreadPool.shutdown();
        System.out.println("=========================end=======================");
        count = 0;
        str = "";
    }

    /**
     * 执行初始化
     * @param url
     */
    private void init(String url) {
        try {
            /**
             * 对首页单独处理
             */
            Document doc = Jsoup.connect(url).get();
            this.refresh(url, doc.title(), doc.body().text().trim().replaceAll("\n", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            this.getPageUrl(url);
        }
    }

    public void getPageUrl(String u){
        try {
            Document doc = Jsoup.connect(u).get();
            Elements aList = doc.select("a");
            String title = doc.title();
            for (Element ele: aList) {
                String href = ele.attr("href");
                if (this.isGetUrl(href) && !title.equals("")){
                    String endUrl = this.url + href;
                    if (!listUrl.contains(endUrl) && this.getResCode(endUrl) == 200){
                        listUrl.add(endUrl);
                        String html = doc.body().text().trim().replaceAll("\n", "");
                        this.refresh(endUrl, title, html);
                        this.getPageUrl(endUrl);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新数据到文件中
     * @param endUrl 来源地址
     * @param title  网页标题
     * @param html   网页body部分的html文本
     */
    public void refresh(String endUrl, String title, String html){
        if (count == 30){
            fixedThreadPool.execute(new TaskThread(this.path, str));
            count = 0;
            str = "";
        }
        System.out.println(endUrl);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", endUrl);
        jsonObject.put("title", title);
        jsonObject.put("html", html);
        str += jsonObject.toString() + "\r\n";
        count++;
    }

    /**
     * 去除不合格的Url地址
     * @param href
     * @return
     */
    public boolean isGetUrl(String href){
        if (href.equals("") || href.equals("/") || href.equals("#")
                || href.equals("javascript:;") || href.substring(0, 1).equals("#")
                || href.equals("javascript:void(0);")){
            return false;
        }
        if (this.isHttpUrl(href)){
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
        url = url.toLowerCase();
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

    /**
     * 获取Url的Host
     * @param link
     * @return
     */
    public String getHost(String link){
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

    /**
     * 获取Url返回的HttpCode
     * @param url
     * @return
     */
    public int getResCode(String url){
        int code = 0;
        URL u = null;
        try {
            u = new URL(url);
            HttpURLConnection uConnection = (HttpURLConnection) u.openConnection();
            code = uConnection.getResponseCode();
            uConnection.disconnect();
        } catch (MalformedURLException e) {
            code = 0;
            e.printStackTrace();
        } catch (IOException e) {
            code = 0;
            e.printStackTrace();
        }
        return code;
    }
}

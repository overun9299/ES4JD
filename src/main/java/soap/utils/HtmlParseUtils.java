package soap.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import soap.pojo.JDGoodsInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangPY on 2020/12/16
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 爬取网页信息工具
 */
@Component
public class HtmlParseUtils {

//    public static void main(String[] args) throws Exception {
//        getJDHtmlInfo("心理学").forEach(System.out::println);
//    }


    public List<JDGoodsInfo> getJDHtmlInfo(String keyWord) throws Exception {
        /** 1.获取请求  https://search.jd.com/Search?keyword=java **/
        String url = "https://search.jd.com/Search?keyword="+ keyWord +"&enc=utf-8";
        /** 2.解析网页 返回的Document就是一个js网页**/
        Document parse = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 5.1; zh-CN) AppleWebKit/535.12 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/535.12").timeout(30000).get();
        /** 获取商品区域 **/
        Element j_goodsList = parse.getElementById("J_goodsList");

        /** 获取商品li **/
        Elements li = j_goodsList.getElementsByTag("li");

        /** 构建数据集合 **/
        List<JDGoodsInfo> jdGoodsInfoList = new ArrayList<>();

        /** 便利每个li **/
        for (Element element : li) {

            /** 获取商品图片地址 **/
            String imgUrl = element.getElementsByTag("img").eq(0).attr("data-lazy-img");
            /** 获取商品价格 **/
            String price = element.getElementsByClass("p-price").eq(0).text();
            /** 获取商品标题 **/
            String title = element.getElementsByClass("p-name").eq(0).text();

            jdGoodsInfoList.add(new JDGoodsInfo().setImgUrl(imgUrl).setPrice(price).setTitle(title));
        }

        return jdGoodsInfoList;
    }
}

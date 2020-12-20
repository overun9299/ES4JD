package soap.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import soap.pojo.JDGoodsInfo;
import soap.service.JDInfoEsService;
import soap.utils.HtmlParseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangPY on 2020/12/20
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: JDInfoEsService实现类
 */
@Service
public class JDInfoEsServiceImpl implements JDInfoEsService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private HtmlParseUtils htmlParseUtils;

    @Override
    public Boolean getJDInfoToES(String keyWord) {


        try {
            /** 判断是否存在索引 **/
            GetIndexRequest java_test = new GetIndexRequest("jd_goods");
            boolean exists = restHighLevelClient.indices().exists(java_test, RequestOptions.DEFAULT);
            if (!exists) {
                /** 创建索引请求 **/
                CreateIndexRequest jd_goods = new CreateIndexRequest("jd_goods");
                /** 客户端执行请求 indicesClient，请求后获得响应 **/
                CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(jd_goods, RequestOptions.DEFAULT);
                /** 得到响应 */
                boolean acknowledged = createIndexResponse.isAcknowledged();

                if (!acknowledged) {
                    return false;
                } else {
                    return false;
                }
            }

            /** 爬取京东数据 **/
            List<JDGoodsInfo> jdHtmlInfo = htmlParseUtils.getJDHtmlInfo(keyWord);
            /** 批量插入数据 **/

            /** 批量请求对象 **/
            BulkRequest request = new BulkRequest();
            /** 批量请求 **/
            for (JDGoodsInfo jdGoodsInfo : jdHtmlInfo) {
                request.add(new IndexRequest("jd_goods").source(JSONObject.toJSONString(jdGoodsInfo), XContentType.JSON));
            }
            /** 发送请求 **/
            BulkResponse bulk = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);



            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public String searchJDGoods(String keyWord, Integer pageNo, Integer pageSize) {

        List<Map> searchList = new ArrayList<>();

        try {
            SearchRequest java_test = new SearchRequest("jd_goods");
            /** 构建搜索条件 **/
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            if (pageNo == null) {
                pageNo = 0;
            }
            if (pageSize == null) {
                pageSize = 20;
            }
            /** 设置分页 **/
            searchSourceBuilder.from(pageNo);
            searchSourceBuilder.size(pageSize);
            /** 设置高亮 **/
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);
            /** 查询条件，我么可以使用QueryBuilders工具来实现 **/
            MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", keyWord);
            searchSourceBuilder.query(matchQueryBuilder);

            /** 放入查询体 **/
            java_test.source(searchSourceBuilder);

            SearchResponse search = restHighLevelClient.search(java_test, RequestOptions.DEFAULT);

            for (SearchHit hit : search.getHits().getHits()) {
                /** 取出高亮字段 */
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                /** 获取结果map **/
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                if(highlightFields!=null){
                    /** 取出name高亮字段 */
                    HighlightField nameHighlightField = highlightFields.get("title");
                    if(nameHighlightField!=null){
                        /** 去除内容，此处为一段一段的，所以后面要拼接 */
                        Text[] fragments = nameHighlightField.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for(Text text:fragments){
                            stringBuffer.append(text);
                        }
                        sourceAsMap.put("title" , stringBuffer.toString());
                    }
                }
                searchList.add(sourceAsMap);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONObject.toJSONString(searchList);
    }
}

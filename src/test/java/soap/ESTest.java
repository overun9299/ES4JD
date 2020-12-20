package soap;



import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import soap.pojo.MyUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangPY on 2020/12/12
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 * Explain: 测试类
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ESTest {

    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;

    /**
     * 测试创建索引
     */
    @Test
    public void testCreateIndex() throws IOException {

        /** 创建索引请求 **/
        CreateIndexRequest java_test = new CreateIndexRequest("java_test");
        /** 设置参数 number_of_replicas 是数据备份数，如果只有一台机器，设置为0 、number_of_shards  是数据分片数，默认为5，有时候设置为3 */
        java_test.settings(Settings.builder().put("number_of_shards","1").put("number_of_replicas","0"));
        /** 操作索引的客户端 */
        IndicesClient indices = client.indices();
        /** 客户端执行请求 indicesClient，请求后获得响应 **/
        CreateIndexResponse createIndexResponse = indices.create(java_test, RequestOptions.DEFAULT);
        /** 得到响应 */
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 测试获取索引
     * @throws IOException
     */
    @Test
    public void testGetIndex() throws IOException {
        GetIndexRequest java_test = new GetIndexRequest("java_test");
        boolean exists = client.indices().exists(java_test, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 测试删除索引
     * @throws IOException
     */
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest java_test = new DeleteIndexRequest("java_test");
        AcknowledgedResponse delete = client.indices().delete(java_test, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    /**
     * 测试添加文档，索引必须存在
     */
    @Test
    public void testAddDocument() throws IOException {
        /** 创建对象 **/
        MyUser myUser = new MyUser();
        myUser.setName("张三plus").setAge(18).setAddress("陕西省西安市周至县");

        /** 创建请求 **/
        IndexRequest request = new IndexRequest("java_test");

        /** 规则 put/java_test/_doc/1 **/

        /** id可设置可不设置，不设置es会自动生成 **/
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));

        /** 将我们的数据放入请求 json **/
        request.source(JSONObject.toJSONString(myUser), XContentType.JSON);

        /** 客户端发送请求，获取响应结果 **/
        IndexResponse index = client.index(request, RequestOptions.DEFAULT);

        System.out.println(index.toString());
        System.out.println(index.status());

    }

    /**
     * 批量添加文档
     */
    @Test
    public void testAddBatchDocument() throws IOException {

        /** 构建list **/
        List<MyUser> myUserList = new ArrayList<>();
        myUserList.add(new MyUser().setName("张三丰").setAge(21).setAddress("河北省保定").setId("7"));
        myUserList.add(new MyUser().setName("王实甫").setAge(28).setAddress("山西省史蒂夫").setId("8"));

        /** 批量请求对象 **/
        BulkRequest request = new BulkRequest();

        /** 批量请求 **/
        for (MyUser myUser : myUserList) {
            request.add(new IndexRequest("java_test").source(JSONObject.toJSONString(myUser),XContentType.JSON));
        }
        /** 发送请求 **/
        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);

        System.out.println(bulk.toString());
        System.out.println(bulk.status());
    }

    /**
     * 判断文档是否存在
     */
    @Test
    public void testDocumentIsExists() throws IOException {
        GetRequest java_test = new GetRequest("java_test", "2");

        boolean exists = client.exists(java_test, RequestOptions.DEFAULT);

        System.out.println(exists);
    }

    /**
     * 获取文档
     */
    @Test
    public void testGetDocument() throws IOException {
        GetRequest java_test = new GetRequest("java_test", "4");

        GetResponse documentFields = client.get(java_test, RequestOptions.DEFAULT);

        System.out.println(documentFields.getSourceAsString());
        System.out.println(documentFields.getSourceAsMap());
        System.out.println(documentFields);
    }

    /**
     * 更新文档
     * @throws IOException
     */
    @Test
    public void testUpdateDocument() throws IOException {
        UpdateRequest java_test = new UpdateRequest("java_test", "4");
        /** 更新内容 **/
        MyUser myUser = new MyUser().setName("王实甫").setAge(98).setAddress("山西省史蒂夫").setId("4");
        /** 更新请求 **/
        java_test.doc(JSONObject.toJSONString(myUser), XContentType.JSON);

        UpdateResponse update = client.update(java_test, RequestOptions.DEFAULT);

        System.out.println(update.status());
    }


    /**
     * 测试删除文档
     */
    @Test
    public void testDeleteDocument() throws IOException {
        DeleteRequest java_test = new DeleteRequest("java_test", "2");

        DeleteResponse delete = client.delete(java_test, RequestOptions.DEFAULT);

        System.out.println(delete.status());
    }


    /**
     * 测试查询文档
     * SearchRequest 搜索请求
     * SearchSourceBuilder 条件构造
     * HighlightBuilder 构建高亮
     * TermQueryBuilder 精确查询
     * MatchAllQueryBuilder 模糊查询
     * 详细搜索请参考 ES6项目 https://github.com/overun9299/ElasticSearchHighLevel-6.git
     */
    @Test
    public void testSearchDocument() throws IOException {
        SearchRequest java_test = new SearchRequest("jd_goods");
        /** 构建搜索条件 **/
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /** 构建查询体 **/
        /** 查询条件，我么可以使用QueryBuilders工具来实现 **/
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "核心技术");
        searchSourceBuilder.query(matchQueryBuilder);
        /** 构建高亮字段 **/
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        /** 对name加高亮 */
        highlightBuilder.fields().add(new HighlightBuilder.Field("address"));
        searchSourceBuilder.highlighter(highlightBuilder);
        java_test.source(searchSourceBuilder);

        SearchResponse search = client.search(java_test, RequestOptions.DEFAULT);

        for (SearchHit hit : search.getHits().getHits()) {
            System.out.println(hit.getSourceAsString());
        }

    }
}

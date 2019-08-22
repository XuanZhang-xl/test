package solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by MSI-PC on 2017/5/24.
 */
public class SolrJ {
    @Test
    public void testWriteDocument() throws Exception {
        //连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        //创建Solr的输入Document
        SolrInputDocument doc = new SolrInputDocument();
        //添加字段
        doc.addField("id",15L);
        doc.addField("title","霸王手机,手机中的战斗机");
        doc.addField("price",99800);

        //添加Document到server
        server.add(doc);
        //提交请求,如果Id不存在,添加新数据,如果已经存在,则修改数据
        server.commit();
    }


    @Test
    public void testWriteBean() throws Exception {
        //连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");

        Item item = new Item(16L, "Duang手机,加了特效的手机", 100000000L);

        //添加Document到server
        server.addBean(item);
        //提交请求,如果Id不存在,添加新数据,如果已经存在,则修改数据
        server.commit();
    }

    @Test
    public void testDelete() throws Exception {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 根据ID删除数据，注意这里需要传字符串
        //server.deleteById("16");

        // 根据查询条件删除，参数是字符串格式，写查询条件
        server.deleteByQuery("title:霸王");
        // 提交
        server.commit();
    }

    @Test
    public void testDeleteAll() throws Exception {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");

        // 删除所有,所有字段的所有内容
        server.deleteByQuery("*:*");
        // 提交
        server.commit();
    }

    @Test
    public void testQueryByDocument() throws Exception {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 创建查询对象：SolrQuery
        SolrQuery query = new SolrQuery("title:Apple");
        // 执行查询,获取响应
        QueryResponse response = server.query(query);

        // 获取查询结果,本质是一个Document的集合
        SolrDocumentList results = response.getResults();
        // 获取总条数
        System.out.println("本次共搜索到" + results.size() + "条数据。");
        // 遍历集合
        for (SolrDocument document : results) {
            System.out.println("id: " + document.getFieldValue("id"));
            System.out.println("titile: " + document.getFieldValue("title"));
            System.out.println("price: " + document.getFieldValue("price"));
        }
    }

    @Test
    public void testQueryByBean() throws Exception {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 创建查询对象：SolrQuery
        SolrQuery query = new SolrQuery("title:Apple");
        // 执行查询,获取响应
        QueryResponse response = server.query(query);

        // 获取查询结果,指定实体类的类型，返回实体类的集合
        List<Item> list = response.getBeans(Item.class);
        // 打印总条数：
        System.out.println("本次共搜索到" + list.size() + "条数据。");
        // 遍历集合
        for (Item item : list) {
            System.out.println(item.getId());
            System.out.println(item.getTitle());
            System.out.println(item.getPrice());
        }
    }
    @Test
    public void testByBoolean() throws SolrServerException {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        //OR,AND,NOT布尔操作,用大写
        SolrQuery query = new SolrQuery("title:Apple OR title:小米");

        QueryResponse response = server.query(query);
        List<Item> items = response.getBeans(Item.class);
        for (Item item : items) {
            System.out.println(item.getId());
            System.out.println(item.getTitle());
            System.out.println(item.getPrice());
        }
    }

    //相似度查询,与luence一样
    @Test
    public void testFuzzyQuery() throws SolrServerException{
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        //在后面加~就表示相似度查询,2表示可以错2个地方
        SolrQuery query = new SolrQuery("title:adplr~2");

        QueryResponse response = server.query(query);
        List<Item> items = response.getBeans(Item.class);
        for (Item item : items) {
            System.out.println(item.getId());
            System.out.println(item.getTitle());
            System.out.println(item.getPrice());
        }
    }
    @Test
    public void testSortedQuery() throws Exception {
        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 创建查询对象：SolrQuery
        SolrQuery query = new SolrQuery("title:华为");
        // 设置查询的排序参数,参数： 排序的字段名、排序方式
        query.setSort("price", SolrQuery.ORDER.desc);// 这里是按价格降序

        // 执行查询,获取响应
        QueryResponse response = server.query(query);
        // 获取查询结果,指定实体类的类型，返回实体类的集合
        List<Item> list = response.getBeans(Item.class);
        // 打印总条数：
        System.out.println("本次共搜索到" + list.size() + "条数据。");
        // 遍历集合
        for (Item item : list) {
            System.out.println(item.getId());
            System.out.println(item.getTitle());
            System.out.println(item.getPrice());
        }
    }


    //分页查询
    @Test
    public void testPageQuery() throws SolrServerException {
        //准备分页
        int pageNum = 3;//要查询的页数
        int pageSize = 5;//煤业显示条数
        int start = (pageNum - 1) * pageSize;// 当前页的起始条数

        // 连接Solr服务器
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 创建查询对象：SolrQuery
        SolrQuery query = new SolrQuery("title:手机");
        // 设置按ID排序，方便查看分页信息
        query.setSort("id", SolrQuery.ORDER.asc);
        // 设置分页信息到查询对象中
        query.setStart(start);// 设置起始条数
        query.setRows(pageSize);// 设置每页条数

        // 执行查询,获取响应
        QueryResponse response = server.query(query);
        // 获取查询结果,指定实体类的类型，返回实体类的集合
        List<Item> list = response.getBeans(Item.class);
        // 打印搜索结果信息
        System.out.println("当前第" + pageNum + "页，本页共" + list.size() + "条数据。");
        // 遍历集合
        for (Item item : list) {
            System.out.println(item.getId());
            System.out.println(item.getTitle());
            System.out.println(item.getPrice());
        }
    }


    // 查询索引并且高亮：
    @Test
    public void testHighlightingQuery() throws Exception {
        // 连接Solr服务器,需要指定地址：我们可以直接从浏览器复制地址。要删除#
        HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr/core1");
        // 创建查询对象
        SolrQuery query = new SolrQuery("title:华为");
        // 设置高亮的标签
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        // 高亮字段
        query.addHighlightField("title");

        // 查询
        QueryResponse response = server.query(query);
        // 解析高亮响应结果,是一个Map

        // 外层的Map：它的键是文档的id,值是这个文档的其它高亮字段，又是一个Map
        // 内存的Map：是其它高亮字段，键是其它字段的名称，值是这个字段的值，这个值是一个List
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();

        // 首先我们获取所有的文档ID
		Set<String> ids = highlighting.keySet();
		for (String id : ids) {
			System.out.println("id: " + id);
			// 根据ID来获取文档的其它字段的集合
			Map<String, List<String>> fields = highlighting.get(id);
			System.out.println("title: " + fields.get("title").get(0));
			// highlighting只有高亮字段，price不是高亮字段，所以没有！也就是这儿内层的Map中只有title字段
			// System.out.println("price: " + fields.get("price"));
		}

		System.out.println("以下是非高亮字段");
		System.out.println("以下是非高亮字段");
		System.out.println("以下是非高亮字段");
		System.out.println("以下是非高亮字段");
		System.out.println("以下是非高亮字段");
        // 获取非高亮结果
        List<Item> list = response.getBeans(Item.class);
        for (Item item : list) {
            long id = item.getId();
            System.out.println("id: " + id);
            // 这里ID是long类型，与集合的键不匹配，所以我们需要把id转为String类型，再get
            //System.out.println("title: " + highlighting.get(id+"").get("title").get(0));
            System.out.println("price: " + item.getPrice());
        }
    }


}

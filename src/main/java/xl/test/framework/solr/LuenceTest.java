package xl.test.framework.solr;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * Created by MSI-PC on 2017/5/22.
 */
public class LuenceTest {
    @Test
    public void testCreate() throws Exception {
        //创建文档对象
        Document doc = new Document();
        //创建并添加字段信息,参数:字段的名称,字段的值,是否存储到文档列表.会创建索引,但不会分词
        //如果不分词,会造成整个字段作为一个词条,除非完全匹配,否则搜索不到
        doc.add(new StringField("id", "8", Store.YES));
        //title字段需要用TestField,创建索引又分词,
        doc.add(new TextField("title", "谷歌地图之父跳槽Facebook", Store.YES));
        //StroeField一定会被存储,但不一定创建索引
        doc.add(new StoredField("url", "www.sina.com"));

        //创建文件目录
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        //创建分词器
        //Analyzer analyzer = new StandardAnalyzer();
        //使用IK分词器
        Analyzer analyzer = new IKAnalyzer();

        //索引写出工具的配置对象
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //设置打开方式:
        /**CREATE_OR_APPEND:
         * Creates a new index if one does not exist,
         * otherwise it opens the index and documents will be appended.
         *
         * OpenMode.CREATE:不但会清空所有索引,也会清空所有文档
         */
        //config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //创建索引的写出工具类,参数:索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, config);


        //把文档交给IndexWriter
        indexWriter.addDocument(doc);
        //提交
        indexWriter.commit();
        //关流
        indexWriter.close();
        System.out.println("写出成功");
    }


    /*批量创建索引*/
    @Test
    public void testCreate2() throws Exception {
        // 创建文档的集合
        Collection<Document> docs = new ArrayList<>();


        String[] strArr = {"谷歌地图之父跳槽facebook", "谷歌地图之父加盟FaceBook", "谷歌地图创始人拉斯离开谷歌加盟Facebook", "谷歌地图之父跳槽Facebook与Wave项目取消有关", "谷歌地图之父拉斯加盟社交网站Facebook"};
        for (int i = 1; i <= strArr.length; i++) {
            Document doc = new Document();
            doc.add(new StringField("id", i + "", Store.YES));
            //doc.add(new LongField("id", (long) i, Store.YES));
            doc.add(new TextField("title", strArr[i - 1], Store.YES));
            docs.add(doc);
        }

        // 索引目录类,指定索引在硬盘中的位置
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        // 引入IK分词器
        Analyzer analyzer = new IKAnalyzer();
        // 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(analyzer);
        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。OpenMode.CREATE会先清空原来数据，再提交新的索引
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 创建索引的写出工具类。参数：索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        // 把文档集合交给IndexWriter
        indexWriter.addDocuments(docs);
        // 提交
        indexWriter.commit();
        // 关闭
        indexWriter.close();

    }

    /*查询索引数据*/
    @Test
    public void testSearch() throws Exception {
        // 索引目录类,指定索引在硬盘中的位置
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());

        // 索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        // 创建查询解析器.参数:默认要查询的字段的名称,分词器
        QueryParser parse = new QueryParser("title", new IKAnalyzer());

        // 创建查询对象
        Query query = parse.parse("谷歌地图之父拉斯");

        // 搜索数据,参数:查询对象,查询的最大结果数
        // 返回的结果是按照匹配度排名得分前N名的文档信息,里面包含了文档及一共有几条文档的信息
        TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
        System.out.println("本次总共搜索到" + topDocs.totalHits + "条.");

        //获取分数文档对象,包含文档,文档编号,得分
        ScoreDoc[] docs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : docs) {
            //取出文档编号
            int docID = scoreDoc.doc;
            System.out.println("文档编号为: " + docID);
            //根据编号去找文档
            Document doc = reader.document(docID);
            System.out.println("id: " + doc.get("id"));
            System.out.println("title: " + doc.get("title"));
            System.out.println("得分: " + scoreDoc.score);
            System.out.println();
        }
    }

    /*抽取查询数据的通用方法*/
    public void search(Query query) throws Exception {
        // 索引目录对象
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        // 索引读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        // 搜索数据,两个参数：查询条件对象要查询的最大结果条数
        // 返回的结果是 按照匹配度排名得分前N名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
        TopDocs topDocs = searcher.search(query, 10);
        // 获取总条数
        System.out.println("本次搜索共找到" + topDocs.totalHits + "条数据");
        // 获取得分文档对象（ScoreDoc）数组.SocreDoc中包含：文档的编号、文档的得分
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取出文档编号
            int docID = scoreDoc.doc;
            // 根据编号去找文档
            Document doc = reader.document(docID);
            System.out.println("id: " + doc.get("id"));
            System.out.println("title: " + doc.get("title"));
            // 取出文档得分
            System.out.println("得分： " + scoreDoc.score);
        }
    }

    /*
    * TermQuery,词条查询
    * Term(词条)是搜索的最小单位,不可在分词,值必须是字符串
    * */
    @Test
    public void testTermQuery() throws Exception {
        TermQuery query = new TermQuery(new Term("title", "谷歌地图"));//并没有谷歌地图这个索引,所以找不到数据
        search(query);
    }

    /*
    * WildcardQuery,通配符查询
    * Wildcard  通配符
    * ? 代表任意一个字符
    * * 代表任意个或0 个字符
    * */
    @Test
    public void testWildCardQuery() throws Exception {
        // 创建查询对象
        Query query = new WildcardQuery(new Term("title", "*谷歌*"));//还是一样,具体的字必须在索引里面有才行
        search(query);
    }


    /*
    * FuzzyQuery,模糊查询
    * Fuzzy     模糊
    * */
    @Test
    public void testFuzzyQuery() throws Exception {
        // 创建模糊查询对象:允许用户输错。但是要求错误的最大编辑距离不能超过2
        // 编辑距离：一个单词到另一个单词最少要修改的次数 facebool --> facebook 需要编辑1次，编辑距离就是1
        //Query query = new FuzzyQuery(new Term("title","fscevool"));
        // 可以手动指定编辑距离，但是参数必须在0~2之间
        Query query = new FuzzyQuery(new Term("title", "facevool"), 2);
        search(query);
    }

    /*
    *   NumericRangeQuery,数值范围查询
    *   可以用来对非String类型的ID进行精确查找
    * */
    @Test
    public void testNumericRangeQuery() throws Exception {
        // 数值范围查询对象，参数：字段名称，最小值、最大值、是否包含最小值、是否包含最大值
        Query query = LongPoint.newRangeQuery("id", 2L, 2L);//找不到,因为数据库中的id是String.
        search(query);
    }

    /*
    * BooleanQuery,组合查询
    * 交集：Occur.MUST + Occur.MUST
    * 并集：Occur.SHOULD + Occur.SHOULD
    * 非：Occur.MUST_NOT
    * */
    @Test
    public void testBooleanQuery() throws Exception {

        Query query1 = LongPoint.newRangeQuery("id", 1L, 3L);
        Query query2 = LongPoint.newRangeQuery("id", 2L, 4L);
        BooleanClause booleanClause1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
        BooleanClause booleanClause2 = new BooleanClause(query2, BooleanClause.Occur.MUST_NOT);

        // 创建布尔查询的对象
        BooleanQuery query = new BooleanQuery.Builder().add(booleanClause1).add(booleanClause2).build();
        search(query);
    }

    /*
    * 修改索引
    * 注意:
    * 1.luence修改功能底层会先删除,再把新的文档添加
    * 2.修改功能会根据Term进行匹配,所有匹配到的都会被删除,
    * 3.因此,一般我们修改时,都会根据一个唯一不重复的字段进行匹配修改,如ID
    * 4.但是词条搜索,要求ID必须是字符串,如果不是,这个方法就不能用
    * 如果ID是数值类型,我们不能直接去修改,可以先手动删除deleteDocuments(数值范围查询锁定ID),再添加
    *
    * */
    @Test
    public void testUpdate() throws Exception {
        // 创建目录对象
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(new IKAnalyzer());
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 创建新的文档数据
        Document doc = new Document();
        doc.add(new StringField("id","1",Store.YES));
        doc.add(new TextField("title","谷歌地图之父跳槽facebook 为了加入传智播客 屌爆了啊",Store.YES));
		/* 修改索引。参数：
		 * 	词条：根据这个词条匹配到的所有文档都会被修改
		 * 	文档信息：要修改的新的文档数据
		 */
        writer.updateDocument(new Term("id","1"), doc);
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }

    /*
    * 删除索引
    * 注意:
    * 一般威客进行精确删除,我们会根据唯一字段来删除,比如ID
    * 如果使用Term删除,要求ID也必须是字符串类型
    * */
    @Test
    public void testDelete() throws Exception {
        // 创建目录对象
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        // 创建配置对象
        IndexWriterConfig conf = new IndexWriterConfig(new IKAnalyzer());
        // 创建索引写出工具
        IndexWriter writer = new IndexWriter(directory, conf);

        // 根据词条进行删除
		writer.deleteDocuments(new Term("id", "1"));

        // 根据query对象删除,如果ID是数值类型，那么我们可以用数值范围查询锁定一个具体的ID
		//Query query = NumericRangeQuery.newLongRange("id", 2L, 2L, true, true);
		//writer.deleteDocuments(query);

        // 删除所有
        //writer.deleteAll();
        // 提交
        writer.commit();
        // 关闭
        writer.close();
    }

    //高亮显示
    @Test
    public void testHighlighter() throws Exception {
        // 目录对象
        Directory directory = FSDirectory.open(new File("D:\\07_temp\\lucenelukeall").toPath());
        // 创建读取工具
        IndexReader reader = DirectoryReader.open(directory);
        // 创建搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);

        //解析用户输入的内容,创建查询对象
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        Query query = parser.parse("Wave");

        // 格式化器
        Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
        QueryScorer scorer = new QueryScorer(query);



        // 准备高亮工具,这个高亮工具没有用到啊?      TODO
        Highlighter highlighter = new Highlighter(formatter, scorer);



        // 搜索
        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("本次搜索共" + topDocs.totalHits + "条数据");

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档编号
            int docID = scoreDoc.doc;
            Document doc = reader.document(docID);
            System.out.println("id: " + doc.get("id"));

            String title = doc.get("title");
            // 用高亮工具处理普通的查询结果,参数：分词器，要高亮的字段的名称，高亮字段的原始值
            String hTitle = highlighter.getBestFragment(new IKAnalyzer(), "title", title);

            System.out.println("title: " + hTitle);
            // 获取文档的得分
            System.out.println("得分：" + scoreDoc.score);
        }
    }
}
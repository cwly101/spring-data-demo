package com.cw;

import com.cw.entify.Article;
import com.cw.entify.ArticleType;
import com.cw.respository.ArticleDao;
import com.cw.respository.TypeDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ManyToManyTest {

    @Autowired
    TypeDao typeDao;

    @Autowired
    ArticleDao articleDao;

    @Test
    void manyToManyTest(){
        Article a2 = articleDao.findById(2).get();
        Article a3 = articleDao.findById(3).get();

        ArticleType type1 = new ArticleType("技术");
        ArticleType type2 = new ArticleType("mongodb");
        ArticleType type3 = new ArticleType("java");



        // 创建两张表间的关系

        // 文章建立关键
        // a2文章 与 文章类型建立关系
        List<ArticleType> a2_types = new ArrayList<>();
        a2_types.add(type1);
        a2_types.add(type2);
        a2.setArticleTypes(a2_types);

        // a3文章 与 文章类型建立关系
        List<ArticleType> a3_types = new ArrayList<>();
        a3_types.add(type1);
        a3_types.add(type3);
        a3.setArticleTypes(a2_types);


        // 文章类型建立关系
        // type1文章类型 与 相关文章建立关系
        List<Article> type1_articles = new ArrayList<>();
        type1_articles.add(a2);
        type1_articles.add(a3);
        type1.setArticles(type1_articles);

        // type2文章类型 与 相关文章建立关系
        List<Article> type2_articles = new ArrayList<>();
        type2_articles.add(a2);
        type2.setArticles(type2_articles);

        // type3文章类型 与 相关文章建立关系
        List<Article> type3_articles = new ArrayList<>();
        type3_articles.add(a3);
        type3.setArticles(type3_articles);

        typeDao.save(type1);
        typeDao.save(type2);
        typeDao.save(type3);

        // 注：这种表之间紧耦合的依赖关系我不太推荐。我还是推荐一种松散的关系。
        // 如果在文章添加时指定所属技术类别，这没问题。
        // 可如果后期要调整重新编辑文章所属技术类别，就是仅修改article_type中间表，这可以满足么？会不会太过复杂了。还是我不太理解。
    }

    @Test
    @Transactional
    void manyToManyQuery(){
        Article article = articleDao.getOne(2);
        List<ArticleType> articleTypes = article.getArticleTypes();
        System.out.println(article.getTitle());
        System.out.println(articleTypes.size());
        articleTypes.forEach(item -> {
            System.out.println(item.getName());
        });

        /**
         * （延迟）查询结果：
         * 2019-11-10 08:37:38.248  INFO 6304 --- [           main] o.s.t.c.transaction.TransactionContext   : Began transaction (1) for test context [DefaultTestContext@4bf48f6 testClass = ManyToManyTest, testInstance = com.cw.ManyToManyTest@729c8def, testMethod = manyToManyQuery@ManyToManyTest, testException = [null], mergedContextConfiguration = [WebMergedContextConfiguration@420a85c4 testClass = ManyToManyTest, locations = '{}', classes = '{class com.cw.SpringDataJpaDemoApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@6913c1fb, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@609cd4d8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7ee8290b, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@15ca7889], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true]]; transaction manager [org.springframework.orm.jpa.JpaTransactionManager@c85486]; rollback [true]
         * Hibernate: select article0_.aid as aid1_0_0_, article0_.author as author2_0_0_, article0_.create_time as create_t3_0_0_, article0_.title as title4_0_0_ from article article0_ where article0_.aid=?
         * 一篇Mongodb文章
         * Hibernate: select articletyp0_.aid as aid2_2_0_, articletyp0_.tid as tid1_2_0_, articletyp1_.tid as tid1_4_1_, articletyp1_.name as name2_4_1_ from article_type articletyp0_ inner join type articletyp1_ on articletyp0_.tid=articletyp1_.tid where articletyp0_.aid=?
         * 2
         * 技术
         * mongodb
         * 2019-11-10 08:37:38.387  INFO 6304 --- [           main] o.s.t.c.transaction.TransactionContext   : Rolled back transaction for test: [DefaultTestContext@4bf48f6 testClass = ManyToManyTest, testInstance = com.cw.ManyToManyTest@729c8def, testMethod = manyToManyQuery@ManyToManyTest, testException = [null], mergedContextConfiguration = [WebMergedContextConfiguration@420a85c4 testClass = ManyToManyTest, locations = '{}', classes = '{class com.cw.SpringDataJpaDemoApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@6913c1fb, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@609cd4d8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@7ee8290b, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@15ca7889], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true]]
         */
    }

}

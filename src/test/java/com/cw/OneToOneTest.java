package com.cw;

import com.cw.entify.Article;
import com.cw.entify.ArticleContent;
import com.cw.entify.Comment;
import com.cw.respository.ArticleContentDao;
import com.cw.respository.ArticleDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
public class OneToOneTest {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleContentDao contentDao;

    /**
     * Article 和 ArticleContent 一对一关系的 添加文章测试
     */
    @Test
    @Transactional
    @Rollback(false)
    void ArticleAndContentSave(){

        Article article = new Article("cao wei", LocalDateTime.now(),"每个人都好");
        ArticleContent content = new ArticleContent("世界是才是真的好，所有人都好！");
        // 建立对象的关联关系。
        content.setArticle(article);  // 多的一方到一的一方的关联关系。（保存时自动对外键赋值）

        articleDao.save(article);  // 保存时一定要确保两个对象的关系已经建立
        contentDao.save(content);

        /**
         * 单元测试的中的事务，请使用 @Rollback(false)，这样才能提交成功。
         * 2019-11-09 15:57:38.969  INFO 11380 --- [           main] o.s.t.c.transaction.TransactionContext   : Began transaction (1) for test context [DefaultTestContext@420a85c4 testClass = OneToOneTest, testInstance = com.cw.OneToOneTest@44e3f3e5, testMethod = ArticleAndContentSave@OneToOneTest, testException = [null], mergedContextConfiguration = [WebMergedContextConfiguration@1c39680d testClass = OneToOneTest, locations = '{}', classes = '{class com.cw.SpringDataJpaDemoApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@17f7cd29, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@158a8276, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@26b3fd41, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@2898ac89], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true]]; transaction manager [org.springframework.orm.jpa.JpaTransactionManager@53dbe7b2]; rollback [false]
         * Hibernate: insert into article (author, create_time, title) values (?, ?, ?)
         * Hibernate: insert into article_data (aid, content) values (?, ?)
         * 2019-11-09 15:57:39.114  INFO 11380 --- [           main] o.s.t.c.transaction.TransactionContext   : Committed transaction for test: [DefaultTestContext@420a85c4 testClass = OneToOneTest, testInstance = com.cw.OneToOneTest@44e3f3e5, testMethod = ArticleAndContentSave@OneToOneTest, testException = [null], mergedContextConfiguration = [WebMergedContextConfiguration@1c39680d testClass = OneToOneTest, locations = '{}', classes = '{class com.cw.SpringDataJpaDemoApplication}', contextInitializerClasses = '[]', activeProfiles = '{}', propertySourceLocations = '{}', propertySourceProperties = '{org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true}', contextCustomizers = set[org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@17f7cd29, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@158a8276, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@26b3fd41, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizerFactory$Customizer@2898ac89], resourceBasePath = 'src/main/webapp', contextLoader = 'org.springframework.boot.test.context.SpringBootContextLoader', parent = [null]], attributes = map['org.springframework.test.context.web.ServletTestExecutionListener.activateListener' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.populatedRequestContextHolder' -> true, 'org.springframework.test.context.web.ServletTestExecutionListener.resetRequestContextHolder' -> true]]
         */
    }

    @Test
    // 解决在单元测试中 no session 的错误.
    // 所有操作需要在一个事务中进行，如果不在一个事务中，一个操作过后，session已经关闭了，再进行查询，会no session
    @Transactional
    void oneToOneQuery(){
        // getOne() 默认使用延迟（懒惰）加载的方式来执行。
        ArticleContent content = contentDao.getOne(3);
        System.out.println(content.getContent());
        System.out.println(content.getId());
        System.out.println(content.getArticle());
    }


    /**
     * 对象导航查询：
     *   默认使用延迟加载的方式。即调用get()方法并不会执行查询，而是在使用关联对象时才会执行查询
     */
    @Test
    @Transactional
    void articleAndCommentQuery(){
        Article article = articleDao.getOne(1);
        System.out.println(article.getAuthor() + "," + article.getTitle());
        System.out.println(article.getAid());
        // 延迟加载
        Set<Comment> comments = article.getComment();
        comments.forEach(comment -> System.out.println(comment.getComment()));
    }

}

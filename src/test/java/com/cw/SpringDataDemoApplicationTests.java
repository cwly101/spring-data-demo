package com.cw;

import com.cw.entify.Article;
import com.cw.respository.ArticleDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SpringDataDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    ArticleDao dao;

    /**
     * 保存一条数据
     */
    @Test
    void articleSave(){
        System.out.println("=== article save ===");
        Article article = new Article("唐朝诗人们", LocalDateTime.now(),"唐诗300");
        dao.save(article);
    }

    /**
     * 保存多条数据
     */
//    @Test
    void article_list_save(){
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("cao wei",LocalDateTime.now(),"一篇Mongodb文章"));
        articles.add(new Article("jack",LocalDateTime.now(),"Java8新特性"));
        articles.add(new Article("spring",LocalDateTime.now(),"Spring Data JPA"));
        articles.add(new Article("spring",LocalDateTime.now(),"Spring Data Redis"));
        dao.saveAll(articles);
    }

    /**
     * 查询全部
     */
    @Test
    void article_findAll(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        dao.findAll().forEach(item -> {
                    System.out.println(String.format("%s,%s,%s,%s",
                            item.getAid(), item.getAuthor(), item.getCreateTime().format(dtf), item.getTitle()));
                }
        );

        /**
         * java8 Lambda
         * (parameters) -> expression
         * 或
         * (parameters) ->{ statements; }
         */
    }

    /**
     * 通过ID查询指定数据
     */
//    @Test
    void article_findById(){
        // idea 给一个对象创建变量名快捷键： 对象.var
        Optional<Article> article = dao.findById(1);
        if(article.isPresent()){
            System.out.println("Article对象不为空");
            System.out.println(article.get().toString());
        }
    }

    /**
     * 更新指定数据
     */
//    @Test
    void article_update(){
        Article article = dao.findById(1).get();
        article.setAuthor("cao wei");
        dao.save(article);  // 有主键即是修改，无主键即是添加
    }


    /**
     * 删除
     */
//    @Test
    void article_delete(){
        dao.deleteById(1);
    }

    void article_delete_all() {
        // 删除功能对比。
        dao.deleteAll();  // 先查询出所有，而后一条一条删除。观看Hibernate 打印的sql语句。
        dao.deleteAllInBatch(); // 直接删除表所有数据，即批量删除
    }
}

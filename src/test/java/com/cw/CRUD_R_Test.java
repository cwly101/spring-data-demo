package com.cw;

import com.cw.entify.Article;
import com.cw.respository.ArticleDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CRUD_R_Test {

    @Autowired
    ArticleDao dao;

    // ============= 父接口方法查询方式 ============

    /**
     * 多个主键查询
     */
    @Test
    void findAllByIds(){
        Integer[] ids={1,3,5};
        dao.findAllById(Arrays.asList(ids)).forEach(item -> {
//            System.out.println(String.format("%s,%s,%s,%s",
//                    item.getAid(),item.getAuthor(),item.getCreateTime(),item.getTitle()));
            System.out.println(item.toString());
        });

    }

    /**
     * 查询全部，并按指定条件排序
     */
    @Test
    void findAllWithSort() {
        Sort sort = Sort.by(Sort.Order.desc("aid"));  // 倒序
        dao.findAll(sort).forEach(item -> {
            System.out.println(item.toString());
        });
    }

    /**
     * 查询全部---分页 + 排序
     */
    @Test
    void findAllWithPaging() {
        Sort sort = Sort.by(Sort.Order.desc("aid"));  // 倒序
        // 分页使用的是索引，0表示第1页，1表示第2页，以此类推
        // 仅分页
//        Pageable pageable = PageRequest.of(2,2);
        // 分页 + 排序
        Pageable pageable = PageRequest.of(2,2,sort);
        Page<Article> all = dao.findAll(pageable);

        System.out.println("总记录数："+all.getTotalElements());
        System.out.println("总页数："+all.getTotalPages());
        System.out.println("当前页:"+all.getNumber());  // 当前页+1,是真正的当前页。
        System.out.println("每页多少条："+all.getSize());
        // 当前页的元素
        all.getContent().forEach(item -> {
            System.out.println(item.toString());
        });
    }

    // ============= 方法命名规则查询 ============

    /**
     * 根据标题模糊查询
     * 分页（内部可传入Sort排序）
     */
    @Test
    void queryLike(){
        Sort sort = Sort.by(Sort.Order.desc("aid"));  // 倒序
        Pageable pageable = PageRequest.of(0,1,sort);  // 分页
        Page<Article> byTitleLike = dao.findByTitleLike("%Spring%", pageable);
        System.out.println("总记录数："+byTitleLike.getTotalElements());
        System.out.println("总页数："+byTitleLike.getTotalPages());
        System.out.println("当前页:"+byTitleLike.getNumber());  // 当前页+1,是真正的当前页。
        System.out.println("每页多少条："+byTitleLike.getSize());
        byTitleLike.getContent().forEach(item -> {
            System.out.println(item.toString());
        });
        /**
         * hibernate 生成的SQL语句：
         * Hibernate: select article0_.aid as aid1_0_, article0_.author as author2_0_,
         * article0_.create_time as create_t3_0_, article0_.title as title4_0_
         * from article article0_
         * where article0_.title like ? escape ? order by article0_.aid desc limit ?
         */
    }

    /**
     * 多条件组合查询
     */
    @Test
    void multiConditionsQuery(){
        Sort sort = Sort.by(Sort.Order.desc("aid"));
        dao.findByAuthorAndTitle("cao wei","一篇Mysql文章",sort)
                .forEach(item -> {
                    System.out.println(item.toString());
                });
        /**
         * Hibernate: select article0_.aid as aid1_0_, article0_.author as author2_0_, article0_.create_time as create_t3_0_,
         * article0_.title as title4_0_
         * from article
         * article0_ where article0_.author=? and article0_.title=? limit ?
         */
    }


    /**
     * 查询指定时间之后的数据
     */
    @Test
    void CreateTimeAfter(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String str1="2019-11-07 10:59:58";
        // jdk8后推荐使用LocalDateTime替代之前的Date
        LocalDateTime parse = LocalDateTime.parse(str1, dtf);
        dao.findByCreateTimeAfter(parse).forEach(item -> {
            System.out.println(item.toString());
        });
    }


    // ============= Specifications 动态查询（用于查询条件不固定的场景） ============


    @Test
    void specificationQuery(){
        // 查询条件不固定（目前假设为2个，author、title，后期可扩展，只要是实体中的属性都可作为查询条件）
        // 查询条件可选，某个条件为空表示忽略该条件
        Article article = new Article();
//        article.setAuthor("Spring");
//        article.setTitle(null);
        Pageable pageable = PageRequest.of(0,2);  // 分页

        Page<Article> all = dao.findAll(new Specification<Article>() {
            /**
             *
             * @param root 实体对象。这里指Article
             * @param cq 用于生成SQL语句
             * @param cb 用于拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (!StringUtils.isEmpty(article.getAuthor())) {  // 拼接查询条件
                    System.out.println("author:" + article.getAuthor());
                    Predicate author = cb.equal(root.get("author").as(String.class), article.getAuthor());
                    list.add(author);
                    // cb.like()
                    // cb.between()
                    // cb.in()
                    // cb.greaterThan()
                    // cb.lessThan()
                    // cb.lessThanOrEqualTo()
                }
                if (!StringUtils.isEmpty(article.getTitle())) {
                    System.out.println("title:" + article.getTitle());
                    Predicate title = cb.equal(root.get("title").as(String.class), article.getTitle());
                    list.add(title);
                }
                Predicate predicate = cb.and(list.toArray(Predicate[]::new));
                // 构建组合的Predicate示例：Predicate p = cb.and(p3,cb.or(p1,p2));
                return predicate;
            }
        }, pageable);

        System.out.println("总记录数："+all.getTotalElements());
        System.out.println("总页数："+all.getTotalPages());
        System.out.println("当前页:"+all.getNumber());  // 当前页+1,是真正的当前页。
        System.out.println("每页多少条："+all.getSize());
        all.getContent().forEach(item -> System.out.println(item.toString()));

    }
}

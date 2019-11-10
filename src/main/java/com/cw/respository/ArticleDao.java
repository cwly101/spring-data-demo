package com.cw.respository;

import com.cw.entify.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JpaRepository<实体类类型,主键类型>：完成基本CRUD操作：(Create\Read\Update\Delete)
 * JpaSpecificationExecutor<实体类类型>: 用于复杂查询（分页等查询操作）
 */
public interface ArticleDao
        extends JpaRepository<Article,Integer>,JpaSpecificationExecutor<Article> {

    // Ctrl + F12 查看类中定义的所有方法
    /**
     * interface Repository<T, ID> 标记接口。
     * 作用：
     *  1. 被Repository接口标记过的子接口类实现对象，可自动被SpringIOC容器识别
     *  2. 子接口可以定义一些指定规范的方式
     *
     *  运行时动态代理会生成ArticleDao接口的实现类：
     *   h = {JdkDynamicAopProxy@9346}
     *   dao = {$Proxy94@9274} "org.springframework.data.jpa.repository.support.SimpleJpaRepository@64cec4d0"
     *  可查看SimpleJpaRepository类的具体实现
     */
    // Ctrl + N 全局搜索类快捷键

    // ============= 方法命名规则查询 ============

    /**
     * 根据标题模糊查询
     * @param title 标题
     * @param pageable 分页（内部可传入Sort排序）
     * @return Page<Article>
     */
    Page<Article> findByTitleLike(String title, Pageable pageable);  // 多条件组合查询也与之类似。

    /**
     * 根据作者和标题查询
     * @param author
     * @param title
     * @param sort
     * @return
     */
    List<Article> findByAuthorAndTitle(String author, String title, Sort sort);  // 多条件组合查询
    // 这只是一个示例，真实情况下，作者+标题筛选，匹配的结果应该很少。分页完全没有必要，排序一下即可。

    // 关于between 、IsLessThan(小于) 等... idea均有智能提示。

    /**
     * 查询指定时间之后的数据
     * @param createTime
     * @return
     */
    List<Article> findByCreateTimeAfter(LocalDateTime createTime);

    // ============= JPQL（Java Persistence Query Language）查询语句 ============
    // 类似于SQL语法，只是要把查询的表名换成实体类的名称，把表中字段换成实体类的属性名
    /**
     * 方法命名规则查询有其局限性：
     * 1. 条件越多函数名称越长，过长的方法名不易读
     * 2. 方法参数个数必须方法名包含的查询条件一致，可能导致参数过多。
     */
    // 注：我个人更推荐使用【方法命名规则查询】方式。

//    @Query("from Article as a where a.author = ?1 and a.title = ?2")  // JPQL语句。占位符是从1开始的。
//    List<Article> findByConditions1(String author,String title);
//    // 方法名的命名这样更难懂了，会导致调用者的困惑。参数必须一一对应，顺序必须与JPQL语句的参数占位符一致。
//
//    // 方式2，方式1的变体。这样不要参数必须一一对应了。
//    @Query("from Article as a where a.author = :author and a.title = :title")
//    List<Article> findByConditions2(
//            @Param("author") String author,
//            @Param("title") String title);
//
//    @Query("from Article as a where a.title like %:title%")  // 模糊查询方式
//    List<Article> findByConditions3(@Param("title") String title);
//
//    @Query("from Article as a where a.title like %:title% order by a.aid desc ")  // 排序方式
//    List<Article> findByConditions4(@Param("title") String title);
//
//    @Query("from Article as a where a.title like %:title%")
//    Page<Article> findByConditions5(@Param("title")String title, Pageable pageable);  // 分页的方式
//
//    @Query("from Article as a where a.aid in :aids ")  // in的方式。
//    List<Article> findByConditions6(@Param("aids") List<Integer> aids);
//
//    // 实体bean参数，搭配SpEL表达式的查询方式
//    @Query("from Article as a where a.author = #{#article.author} and #{#article.title}")
//    List<Article> findByConditions7(@Param("article") Article article);


    // ============= Specifications 动态查询（用于查询条件不固定的场景） ============
    /**
     * JpaSpecificationExecutor<T>
     * 相比JPQL查询，优势是类型安全，更加面向对象，缺点是书写较麻烦。
     */
}

package com.cw.entify;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

//@Data  注：lombok和jpa的@OneToMany等注解在运行时有点冲突，报空指定异常，我怀疑和加载顺序有关。
@Entity  // 告诉jpa这是一个实体类，与数据库表做映射。任何Hibernate映射对象都要有这个注释，否则不做映射。
// 声明此对象映射到数据库的数据表，通过它可以为实体指定表(table),目录(Catalog)和schema的名字。该注释不是必须的，如果没有则系统使用默认值(实体的短类名)。
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略，自增
    private int aid;

    @Column  // length 默认为255。 如属性名与表字段名一致，此注解可省略。
    private String author;

    @Column
    private LocalDateTime createTime;  // jdk8后推荐使用LocalDateTime替代之前的Date

    private String title;

    /**
     * 在类中使用注解声明表间关系的步骤：
     * 1. 书写注解
     * 2. 明确谁来维护关系。维护原则：关系少的一方负责维护。这里明显是文章内容(ArticleContent)引用关系少于文章(Article)
     *   在负责维护关系的一方使用 @JoinColumn 来维护
     *   在放弃维护的一方使用 mappedBy属性声明
     */

    // Article实体对象声明放弃维护主外键关系，交由一对一中关系中的另一方维护。
    // mappedBy 就是负责维护的一方中的对应该实体的名称，即：private Article article; (见ArticleContent实体对象）
    // optional = true 表示左外链接，false 表示内链接
//    @OneToOne(mappedBy = "article",cascade = CascadeType.PERSIST,optional = true)  // 级联保存。如果是All，表示所有操作都是级联的。
//    private ArticleContent articleContent;

    /**
     * 注：这只是一个样例。在实际中文章列表不可能和文章评论一起加载，没有这么设计的。
     * FetchType.LAZY 延迟加载
     * FetchType.EAGER 即时加载
     */
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY)
    private Set<Comment> comment = new HashSet<>(0);  // 集合属性建议直接初始化，以防空指针异常。
//
//    // 数据库中表示多对多关系的表，在JPA中不需要创建实体
    @ManyToMany(mappedBy = "articles",fetch = FetchType.LAZY)
    private List<ArticleType> articleTypes = new ArrayList<>(0);


    // ************* 实战经验 **************
    // 以上只是用JPA创建数据库表结构时，主外键引用关系的的步骤。主要目的是讲述用JPA如何建表结构。
    // 但是实际应用中，尤其结合查询，以及考虑的查询效率性能优化时，是不推荐这么设计的。
    // 文章列表通常只是显示一些列表中必要的信息，如作者、标题、发布时间等。谁会级联查询出其它没必要信息？
    // 当点击具体文章时，才会加载文章内容。然后用异步加载的方式加载评论信息。
    // 没必要把级联操作的所有事件都交给JPA完成，业务逻辑的Service层，应该负担这部分操作。当然，是建立在没有缓存层的情况下。
    // -------- 个人总结  ---------
    // 根据一对多维护原则，不负责维护关系的一方，完全可以不写不声明。写上唯一用途的是级联操作，但付出的代价得不偿失，增加
    // 了复杂度，这完全该交由业务逻辑Service层处理。而负责维护的一方（即多的一方），它单独存在本身没有意义，需要同时（饥饿式）查询出的主表信息。这样，大部分需求情况
    // 都满足了。
    // ***************************

    // idea的构造快捷键：Alt+Insert(Ins), 多选中要参与构造属性。
    public Article(String author, LocalDateTime createTime, String title) {
        this.author = author;
        this.createTime = createTime;
        this.title = title;
    }

    public Article() {
    }

    // 当POJO有属性不需要映射的时候一定要用@Transitent修饰，该注释表示此属性与表没有映射关系，只是一个暂时的属性。


    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Comment> getComment() {
        return comment;
    }

    public void setComment(Set<Comment> comment) {
        this.comment = comment;
    }

    public List<ArticleType> getArticleTypes() {
        return articleTypes;
    }

    public void setArticleTypes(List<ArticleType> articleTypes) {
        this.articleTypes = articleTypes;
    }
}

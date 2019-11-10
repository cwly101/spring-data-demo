package com.cw.entify;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "article_data")
// @Table(name = "article_data", uniqueConstraints = {@UniqueConstraint(columnNames = {"aid"})}) 在类顶层给指定属性设置唯一索引
public class ArticleContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    // 声明类之间映射关系。
    // 默认就是饥饿式查询方式。
    @OneToOne(fetch = FetchType.LAZY)   // 一对一关系。 维护原则：关系少的一方负责维护。这里明显是文章内容(ArticleContent)引用关系少于文章(Article)
    // name: 当前表中的外键名。
    // referencedColumnName：引用表中的主键名，即Article实体中的主键名。
    // 设置unique属性后实际上就成了一对一关系。不过上面已经标注@OneToOne了
    @JoinColumn(name = "aid",referencedColumnName = "aid",unique = true)
    private Article article;

    public ArticleContent(String content) {
        this.content = content;
    }

    public ArticleContent() {
    }
}

package com.cw.entify;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Data
@Entity
@Table(name = "type")
public class ArticleType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tid;

    @Column(unique = true) // 在表结构已经创建情况，这里设置unique无作用了，允许重复。
    private String name;

    @ManyToMany
    // 注：@JoinColumn表示外键（一对多，多对一），@JoinTable表示中间表（多对多）。
    @JoinTable(
            name = "article_type",  // 中间表名称
            // 中间表（article_type表）的外键对应的当前表(type表)的主键名称
            joinColumns = {@JoinColumn(name = "tid",referencedColumnName = "tid")},
            // 中间表（article_type表）的外键对应的对方表(article表)的主键名称
            inverseJoinColumns = {@JoinColumn(name = "aid",referencedColumnName = "aid")}
    )
    private List<Article> articles = new ArrayList<>(0);

    public ArticleType(String name) {
        this.name = name;
    }

    public ArticleType() {
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}

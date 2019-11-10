package com.cw.entify;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comment;

    @ManyToOne(targetEntity = Article.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "aid",referencedColumnName = "aid")
    private Article article;

}

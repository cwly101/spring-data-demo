package com.cw;

import com.cw.entify.Article;
import com.cw.entify.Comment;
import com.cw.respository.ArticleDao;
import com.cw.respository.CommentDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class OneToManyTest {

    @Autowired
    ArticleDao articleDao;

    @Autowired
    CommentDao commentDao;

//    @Test
//    void oneToManyTest(){
//        Article article = articleDao.findById(1).get();
//        Comment comment = new Comment("好文章...",article);   // 评论，构造同时设置关系
//        Comment comment2 = new Comment("111,灌水",article);
//
//        List<Comment> comments = new ArrayList<>();
//        comments.add(comment);
//        comments.add(comment2);
//
//        // 建立两个对象的关系。
//        article.setComment(comments);  // 文章设置关系。
//
////        commentDao.save(comment);  // 上面仅仅是设置了关系。save一次还是保存一条。
////        commentDao.save(comment2);
//
//        commentDao.saveAll(comments);  // 批量保存
//    }
}

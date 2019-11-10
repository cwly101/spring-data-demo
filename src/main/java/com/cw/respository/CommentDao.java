package com.cw.respository;

import com.cw.entify.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentDao
        extends JpaRepository<Comment,Integer>,JpaSpecificationExecutor<Comment> {
}

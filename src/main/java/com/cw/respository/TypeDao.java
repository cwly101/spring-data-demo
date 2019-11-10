package com.cw.respository;

import com.cw.entify.ArticleType;
import com.cw.entify.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeDao
        extends JpaRepository<ArticleType,Integer>,JpaSpecificationExecutor<ArticleType> {
}

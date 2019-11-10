package com.cw.respository;

import com.cw.entify.Article;
import com.cw.entify.ArticleContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JpaRepository<实体类类型,主键类型>：完成基本CRUD操作：(Create\Read\Update\Delete)
 * JpaSpecificationExecutor<实体类类型>: 用于复杂查询（分页等查询操作）
 */
public interface ArticleContentDao
        extends JpaRepository<ArticleContent,Integer>,JpaSpecificationExecutor<ArticleContent> {


}

package com.example.hellpyending.article.repository;

import com.example.hellpyending.DeleteType;
import com.example.hellpyending.article.domain.Article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByDeleteYn(DeleteType deleteYn, Pageable pageable);

    Page<Article> findByTitleContainsAndDeleteYn(String kw, DeleteType deleteYn, Pageable pageable);

    Optional<Article> findByIdAndDeleteYn(Long id, DeleteType deleteYn);

    @Modifying
    @Query("update Article a set a.hitCount = a.hitCount + 1 where a.id = :id")
    int updateView(@Param("id") Long id);

}

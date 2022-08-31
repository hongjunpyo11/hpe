package com.example.hellpyending.article.service;

import com.example.hellpyending.DeleteType;
import com.example.hellpyending.article.domain.Article;
import com.example.hellpyending.article.exception.DataNotFoundException;
import com.example.hellpyending.article.form.ArticleForm;
import com.example.hellpyending.article.repository.ArticleRepository;
import com.example.hellpyending.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<Article> getList(String kw, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("create"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 한 페이지에 10까지 가능

        if (kw == null || kw.trim().length() == 0) {
            return articleRepository.findByDeleteYn(DeleteType.NORMAL, pageable);
        }
        return articleRepository.findByTitleContainsAndDeleteYn(kw, DeleteType.NORMAL, pageable);

    }

    public Article getArticle(long id) {
//        return articleRepository.findById(id)
//                .orElseThrow(() -> new DataNotFoundException("no %d question not found,".formatted(id)));

        return articleRepository.findByIdAndDeleteYn(id, DeleteType.NORMAL)
                .orElseThrow(() -> new DataNotFoundException("no %d question not found,".formatted(id)));
        // jpa 내장 메서드는 대댓글 수 만큼 댓글이 복사되는 오류가 생기고 커스텀한 메서드만 오류가 안생김
    }

    @Transactional
    public void create(String title, String content, String areaName, Users users) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setDeleteYn(DeleteType.NORMAL);
        article.setCreate(LocalDateTime.now());
        article.setUpdate(LocalDateTime.now());
        article.setUsers(users);
        article.setAreaName(areaName); // 지역명은 회원가입 할 때 가져오는 것, 조회수는 생각 해보자
        articleRepository.save(article);
    }

    @Transactional
    public boolean modify(Long articleId, String title, String content, String areaName) {

        Optional<Article> articleOptional = articleRepository.findByIdAndDeleteYn(articleId, DeleteType.NORMAL);

        if (articleOptional.isEmpty()) { //조회가 안되면 잘못 요청한 것임
            return false;
        } else {
            Article article = articleOptional.get();
            article.setTitle(title);
            article.setContent(content);
            article.setUpdate(LocalDateTime.now());
            article.setAreaName(areaName);

            return true;
        }
    }

    @Transactional
    public boolean delete(Long id) {

        Optional<Article> articleOptional = articleRepository.findByIdAndDeleteYn(id, DeleteType.NORMAL);

        if (articleOptional.isEmpty()) { //조회가 안되면 잘못 요청한 것임
            return false;
        } else {
            Article article = articleOptional.get();
            article.setDeleteYn(DeleteType.DELETE);
            return true;
        }
    }



    @Transactional
    public void setHitCount(Article article) {
        Integer PrevHitCount = article.getHitCount();
        PrevHitCount++;
        article.setHitCount(PrevHitCount);
        articleRepository.save(article);
    }

}

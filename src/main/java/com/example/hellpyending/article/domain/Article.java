package com.example.hellpyending.article.domain;

import com.example.hellpyending.DeleteType;
import com.example.hellpyending.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime create;

    @Column(name = "update_at", nullable = false) //, nullable = false
    private LocalDateTime update;

    @Column(name = "title")
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "hit_count", columnDefinition = "Integer default 0", nullable = false)
    private Integer hitCount;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn")
    @JsonIgnore
    private DeleteType deleteYn;

    @Column(name = "area_name")
    private String areaName;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users users;

    //    @Transient
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"article", "commentBundle"})
    private List<ArticleComment> articleCommentList;

    public void addArticleComment(ArticleComment articleComment) {
        articleComment.setArticle(this);
        getArticleCommentList().add(articleComment);
    }





//    @OneToMany(mappedBy = "article")
//    @JsonIgnoreProperties({"article"})
//    private List<ArticleImg> articleImgList;
//
//    @OneToMany(mappedBy = "article")
//    @JsonIgnoreProperties({"article"})
//    private List<ArticleHashtag> articleHashtagList;
}
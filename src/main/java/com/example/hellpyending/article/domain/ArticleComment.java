package com.example.hellpyending.article.domain;

import com.example.hellpyending.DeleteType;
import com.example.hellpyending.user.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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
public class ArticleComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long id;

    @Column(name = "create_at")
    private LocalDateTime create;

    @Column(name = "update_at")
    private LocalDateTime update;

    @Enumerated(EnumType.STRING)
    @Column(name = "delete_yn")
    @JsonIgnore
    private DeleteType deleteYn;

    // 새로 추가
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "comment_depth") // 0이면 댓글 1이면 대댓글
    private int commentDepth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_bundle", referencedColumnName ="board_comment_id")
//    @JsonIgnoreProperties({"commentBundle", "article", "child"})
    @JsonIgnore
    private ArticleComment commentBundle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Article article;

    @OneToMany(mappedBy = "commentBundle", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"commentBundle", "article", "child"})
    private List<ArticleComment> child = new ArrayList<>();

    public void addChild(ArticleComment articleCommentReply) {
        articleCommentReply.setCommentBundle(this);
        this.getChild().add(articleCommentReply);
    }


}
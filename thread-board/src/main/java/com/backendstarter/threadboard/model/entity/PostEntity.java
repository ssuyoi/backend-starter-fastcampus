package com.backendstarter.threadboard.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "post",
    indexes = {@Index(name = "post_userid_idx", columnList = "userid")}) // 쿼리 성능 개선
@SQLDelete(sql = "UPDATE post SET deleteddatetime = CURRENT_TIMESTAMP WHERE postid = ?")
@SQLRestriction("deleteddatetime IS NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column
    private ZonedDateTime createdDateTime;

    @Column
    private ZonedDateTime updatedDateTime;

    @Column
    private ZonedDateTime deletedDateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity user;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(ZonedDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public ZonedDateTime getDeletedDateTime() {
        return deletedDateTime;
    }

    public void setDeletedDateTime(ZonedDateTime deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostEntity that = (PostEntity) o;
        return Objects.equals(postId, that.postId) && Objects.equals(body,
            that.body) && Objects.equals(createdDateTime, that.createdDateTime)
            && Objects.equals(updatedDateTime, that.updatedDateTime)
            && Objects.equals(deletedDateTime, that.deletedDateTime)
            && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, body, createdDateTime, updatedDateTime, deletedDateTime, user);
    }

    // PostService에서 직접 처리하던 로직 이동 => 엔티티 생성 책임을 서비스 레이어 대신 엔티티에서 갖도록 관심사 분리
    public static PostEntity of(String body, UserEntity user) {
        var post = new PostEntity();
        post.setBody(body);
        post.setUser(user);
        return post;
    }

    @PrePersist
    private void prePersist() {
        this.createdDateTime = ZonedDateTime.now();
        this.updatedDateTime = ZonedDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedDateTime = ZonedDateTime.now();
    }
}

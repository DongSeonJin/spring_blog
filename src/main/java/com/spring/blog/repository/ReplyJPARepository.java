package com.spring.blog.repository;

import com.spring.blog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyJPARepository extends JpaRepository<Reply, Long> {

    // blogId를 기준으로 전체 댓글을 얻어오는 메서드를 쿼리메서드 방식으로 생성
    List<Reply> findAllByBlogId(Long blogId);

}

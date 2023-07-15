package com.spring.blog.repository;

import com.spring.blog.dto.ReplyFindByBlogIdDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyRepository {

    List<ReplyFindByBlogIdDTO> findAllByBlogId(long blogId);

    ReplyResponseDTO findByReplyId(long replyId);

    void deleteByReplyId(long replyId);

    void save(ReplyCreateRequestDTO replyCreateRequestDTO);

    void update(ReplyUpdateRequestDTO replyUpdateRequestDTO);

    void deleteByBlogId(long blogId);


}

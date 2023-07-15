package com.spring.blog.service;

import com.spring.blog.dto.ReplyFindByBlogIdDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReplyServiceTest {

    // 서비스 객체 세팅해주세요
    @Autowired
    ReplyService replyService;

    // findAllByBlogIdTest()는 ReplyRepositoryTest 코드를 참조해서 작성해주세요.

    @Test
    @Transactional
    public void FindAllByBlogIdTest(){
        long id = 2;

        List<ReplyFindByBlogIdDTO> replyList = replyService.findAllByBlogId(id);

        assertEquals(4, replyList.size());
    }

    @Test
    @Transactional
    public void FindByReplyIdTest(){
        long id = 5;
        String writer = "개발고수";

        ReplyResponseDTO reply = replyService.findByReplyId(id);

        assertEquals(5, reply.getReplyId());
        assertEquals("개발고수", reply.getReplyWriter());
    }

    @Test
    @Transactional
    public void deleteByReplyIdTest(){
        long replyId = 2;
        long blogId = 2;

        replyService.deleteByReplyId(replyId);
        List<ReplyFindByBlogIdDTO> replyList = replyService.findAllByBlogId(blogId);

        assertEquals(3, replyList.size());

    }

    @Test
    @Transactional
    public void insertTest(){
        long blogId = 3;
        String replyWriter = "토토비";
        String replyContent = "토비보자";

        ReplyCreateRequestDTO replyCreateRequestDTO = ReplyCreateRequestDTO.builder()
                                                        .blogId(blogId)
                                                        .replyWriter(replyWriter)
                                                        .replyContent(replyContent)
                                                        .build();
        replyService.save(replyCreateRequestDTO);
        List<ReplyFindByBlogIdDTO> result = replyService.findAllByBlogId(blogId);
        ReplyFindByBlogIdDTO reply = result.get(result.size() - 1);


        assertEquals(2, result.size());
        assertEquals(replyWriter, reply.getReplyWriter());
        assertEquals(replyContent, reply.getReplyContent());
    }

    @Test
    @Transactional
    public void UpdateTest(){
        long replyId = 2;
        String replyWriter = "업데이트 제목";
        String replyContent = "업데이트 내용";

        ReplyUpdateRequestDTO replyUpdateRequestDTO = ReplyUpdateRequestDTO.builder()
                                                        .replyId(replyId)
                                                        .replyWriter(replyWriter)
                                                        .replyContent(replyContent)
                                                        .build();


        replyService.update(replyUpdateRequestDTO);

        assertEquals(replyWriter, replyService.findByReplyId(replyId).getReplyWriter());
        assertEquals(replyContent, replyService.findByReplyId(replyId).getReplyContent());
    }


}
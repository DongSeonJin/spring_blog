package com.spring.blog.repository;

import com.spring.blog.dto.ReplyFindByBlogIdDTO;
import com.spring.blog.dto.ReplyResponseDTO;
import com.spring.blog.dto.ReplyCreateRequestDTO;
import com.spring.blog.dto.ReplyUpdateRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class ReplyRepositoryTest {

    @Autowired// 테스트 코드에서는 필드 주입을 써도 무방합니다.
    ReplyRepository replyRepository;

    @Test
    @Transactional
    @DisplayName("2번 글에 연동된 댓글 개수가 4개인지 확인")
    public void findAllByBlogIdTest(){
        long id = 2;

        List<ReplyFindByBlogIdDTO> result = replyRepository.findAllByBlogId(id);

        assertEquals(4, result.size());
    }

    @Test
    @Transactional
    @DisplayName("댓글번호 3번 자료의 댓글은 3번이고, 글쓴이는 '바둑이'")
    public void findByReplyIdTest(){
        long id = 3;

        ReplyResponseDTO result = replyRepository.findByReplyId(id);

        assertEquals(3, result.getReplyId());
        assertEquals("야옹이", result.getReplyWriter());

    }

    @Test
    @Transactional
    @DisplayName("댓글번호 2번을 삭제한 다음, 전체 데이터 개수가 4개이고, 그리고 2번으로 재조회시 null인것이다.")
    public void deleteByReplyIdTest(){
        long id = 2;

        replyRepository.deleteByReplyId(id);
        List<ReplyFindByBlogIdDTO> result = replyRepository.findAllByBlogId(id);



        assertEquals(3, result.size());
        assertNull(replyRepository.findByReplyId(id));
    }

    @Test
    @Transactional
    @DisplayName("픽스처를 이용해 INSERT후, 전체 데이터를 가져와서 마지막인덱스 번호 요소를 얻어와서 입력했던 finxture와 비교하면 같다")
    public void insertReplyTest(){
        long id = 1;
        String writer = "홍길동";
        String content = "가나다";

        ReplyCreateRequestDTO replyCreateRequestDTO = ReplyCreateRequestDTO.builder()
                                                    .blogId(id)
                                                    .replyWriter(writer)
                                                    .replyContent(content)
                                                    .build();


        replyRepository.save(replyCreateRequestDTO);


        List<ReplyFindByBlogIdDTO> result = replyRepository.findAllByBlogId(id);
        ReplyFindByBlogIdDTO reply = result.get(result.size() - 1);

        assertEquals(writer, reply.getReplyWriter());
        assertEquals(content, reply.getReplyContent());

    }

    @Test
    @Transactional
    @DisplayName("fixture로 수정할 댓글쓴이, 댓글내용, 3번 replyId를 지정합니다. 수정 후 3번자료를 DB에서 꺼내 fixture비교 및 published_at과 updated_at이 다른지 확인")
    public  void updateTest(){
        long replyId = 3;
        String replyWriter = "아무개";
        String replyContent = "아몰랑";

        ReplyUpdateRequestDTO replyUpdateRequestDTO = ReplyUpdateRequestDTO.builder()
                                                    .replyWriter(replyWriter)
                                                    .replyContent(replyContent)
                                                    .replyId(replyId)
                                                    .build();
        replyRepository.update(replyUpdateRequestDTO);

        ReplyResponseDTO result = replyRepository.findByReplyId(replyId);

        assertEquals(replyWriter, result.getReplyWriter());
        assertEquals(replyContent, result.getReplyContent());
        assertTrue(result.getUpdatedAt().isAfter(result.getPublishedAt()));

    }
    @Test
    @Transactional
    @DisplayName("blogId가 2인 글을 삭제하면, 삭제한 글의 전체 댓글 조회시 null일것이다.")
    public void deleteByBlogIdTest(){
        long blogId = 2;

        replyRepository.deleteByBlogId(blogId);

        List<ReplyFindByBlogIdDTO> resultList = replyRepository.findAllByBlogId(blogId);
        assertEquals(0, resultList.size());
    }
}

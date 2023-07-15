package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BlogServiceTest {

    @Autowired
    BlogService blogService;

    @Test
    @Transactional // 이 테스트의 결과가 디비 커밋을 하지 않음
    public void findAllTest(){
        // given : 없음

        // when : 전체 데이터 가져오기
        List<Blog> blogList = blogService.findAll();
        // then : 길이가 3일것이다.
        assertThat(blogList.size(), is(3));
    }

    @Test
    @Transactional
    public void findByIdTest(){
        long blogId = 2;
        String writer = "2번유저";
        String blogTitle = "2번제목";

        Blog blog = blogService.findById(blogId);

        assertThat(blog.getBlogId(), is(blogId));
        assertThat(blog.getWriter(), is(writer));
        assertThat(blog.getBlogTitle(), is(blogTitle));
    }

    @Test
    @Transactional
    //@Commit // 트랜젝션 적용된 테스트의 결과를 커밋해서 디비에 반영하도록 만듬
    public void deleteByIdTest(){
        long blogId = 2;

        blogService.deleteById(blogId);

        assertThat(blogService.findAll().size(), is(2));
        assertNull(blogService.findById(blogId));
    }

    @Test
    @Transactional
    public void saveTest(){
        String writer = "서비스추가글쓴이";
        String title = "서비스추가제목";
        String content = "서비스추가본문";

        Blog blog = Blog.builder()
                .writer(writer)
                .blogTitle(title)
                .blogContent(content)
                .build();

        blogService.save(blog);

        assertThat(blogService.findAll().size(), is(4));
        assertThat(blog.getWriter(), is(writer));
        assertThat(blog.getBlogTitle(), is(title));
        assertThat(blog.getBlogContent(), is(content));
    }

    @Test
    @Transactional
    public void updateTest(){
        String title = "업데이트 제목";
        String content = "업데이트 본문";
        long blogId = 2;

        blogService.update(Blog.builder()
                        .blogTitle(title)
                        .blogContent(content)
                        .blogId(blogId)
                            .build());

        assertThat(blogService.findById(2).getBlogId(), is(blogId));
        assertThat(blogService.findById(2).getBlogTitle(), is(title));
        assertThat(blogService.findById(2).getBlogContent(), is(content));

    }

    // blog와 함께 reply가 삭제되는 케이스는 따로 다시 테스트코드를 하나 더 작성해주는게 좋습니다.


}

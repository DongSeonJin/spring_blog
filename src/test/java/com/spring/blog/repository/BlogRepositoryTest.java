package com.spring.blog.repository;

import com.spring.blog.entity.Blog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)// 테스트 코드 추기가 메서드 단위
public class BlogRepositoryTest {
    @Autowired
    BlogRepository blogRepository;

    @BeforeEach // 각 테스트 전에 공통적으로 실행한 코드를 저장해두는곳
    public void setBlogTable(){
        blogRepository.createBlogTable();// blog 테이블 생성
        blogRepository.insertTestData();// 생성된 blog 테이블에 더미데이터 3개 입력

    }

    @Test
    @DisplayName("전체 행을 얻어오고, 그 중 자바 1번 인덱스 행만 추출해 번호 확인")
    public void findAllTest(){
        //given (사람기준) 2번째요소 조회를 위한 fixture 선언
        int blogId = 1; // 자바 자료구조 인덱스는 0번부터
        //given 없음
        // when DB에 있는 모든 데이터를 자바로 역직렬함
        List<Blog> blogList = blogRepository.findAll();
        System.out.println(blogList);

        // then 더미데이터가 3개이므로 3개일것이라 단언
        assertEquals(3, blogList.size());
        // 2번째 객체의 ID번호는 2번일것이다.
        assertEquals(2, blogList.get(blogId).getBlogId());
    }

    @Test
    @DisplayName("2번 글을 조회했을때, 제목과 글쓴이와 번호가 단언대로 받아와지는지 확인")
    public void findByIdTest(){
        // given : 조회할 2번 id를 변수로 저장합니다.
        long blogId = 2;
        // when : 레포지토리에서 단일행 Blog를 얻어와 저장합니다.
        Blog blog = blogRepository.findById(blogId);

        // then : 해당 객체의 writer 멤버변수는 "2번유저" 이고 blogTitle은 "2번제목"이고
        // blogId 는 2이다.
        assertEquals("2번유저", blog.getWriter());
        assertEquals("2번제목", blog.getBlogTitle());
        assertEquals(2, blog.getBlogId());
    }

    @Test
    @DisplayName("4번째 행 데이터 저장 후, 행 저장여부 및 던달데이터 저장 여부 확인")
    public void saveTest(){
        // given : 저장을 위한 Blog entity 생성 및 writer, blogTitle, blogContent
        // 에 해당하는 fixture setter로 저장하기

        //Blog blog = new Blog();
        //blog.setWriter("4번유저");
        //blog.setBlogTitle("4번제목");
        //blog.setBlogContent("4번본문");
        // blog 객체 생성 코드를 빌더패턴으로 리팩토링
        // 빌더 패턴 쓰는법
        // 장점 : 파라미터 순서를 뒤바꿔서 집어넣어도 상관없음
        Blog blog = Blog.builder()// 빌더패턴 시작
                .writer("4번유저")
                .blogTitle("4번제목")
                .blogContent("4번본문")
                .build();// 빌더패턴 끝

        int blogId = 3;
        // when : save() 메서드 호출 하고, findAll()로 전체 데이터 가져오기
        blogRepository.save(blog);
        List<Blog> blogList = blogRepository.findAll();

        // then : 전체 데이터 개수가 4개인지,
        // 그리고 방금 INSERT한 데이터의 writer, blogtitle, blogContent가
        // 입력한대로 들어갔는지 단언문으로 확인
        assertEquals(4, blogList.size());
        assertEquals("4번유저", blogList.get(blogId).getWriter());
        assertEquals("4번제목", blogList.get(blogId).getBlogTitle());
        assertEquals("4번본문", blogList.get(blogId).getBlogContent());

    }

    @Test
    @DisplayName("2번 삭제 후 전체 목록을 가져왔을때 남는행수는 2개, 삭제한번호 재조회시 null")
    public void deleteByIdTest(){
        // given
        int id = 2;
        // when
        blogRepository.deleteById(id);
        // then
        assertEquals(2, blogRepository.findAll().size());
        assertNull(blogRepository.findById(id));
    }

    @Test
    @DisplayName("2번글의 제목을 '수정한제목' 으로, 본문도 '수정한 본문' 으로 수정 후 확인")
    public void updateByIdTest(){
        long id = 2;
        String title = "수정한 제목";
        String content = "수정한 본문";

        blogRepository.update(Blog.builder()
                .blogId(id)
                .blogTitle(title)
                .blogContent(content)
                .build());

        String updatedTitle = blogRepository.findById(2).getBlogTitle();
        String updatedContent = blogRepository.findById(2).getBlogContent();

        assertThat(updatedTitle, is(title));
        assertThat(updatedContent, is(content));

    }

    @AfterEach // 각 단위테스트 끝난 후에 실행할 구문을 작성
    public void dropBlogTable(){
        blogRepository.dropBlogTable();// blog 테이블 지우기
    }
}

package com.spring.blog.service;

import com.spring.blog.entity.Blog;
import com.spring.blog.repository.BlogJPARepository;
import com.spring.blog.repository.BlogRepository;
import com.spring.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {

    ReplyRepository replyRepository;
    BlogRepository blogRepository;

    BlogJPARepository blogJPARepository;

    @Autowired// 생성자 주입이 속도가 더 빠름
    public BlogServiceImpl(BlogRepository blogRepository,
                           ReplyRepository replyRepository,
                           BlogJPARepository blogJPARepository){
        this.blogRepository = blogRepository;
        this.replyRepository = replyRepository;
        this.blogJPARepository = blogJPARepository;
    }

    @Override
    public Page<Blog> findAll(Integer pageNumber) { // 페이지 정보를 함꼐 포함하고 있는 리스트인 Page를 리턴해야함
        // return  blogJPARepository.findAll(); // <- JPA를 활용한 전체 글 가져오기
//        return blogRepository.findAll(); // -< <- Mybatis를 활용한 전체 글 가져오기

        // 페이징 처리에 관련된 정보를 먼저 객체로 생성합니다.
        Pageable pageable = PageRequest.of(getCalibratedPageNum(pageNumber) - 1, 10);

        return blogJPARepository.findAll(pageable);
    }

    @Override
    public Blog findById(long blogId){
        //return  blogRepository.findById(blogId);
        // JPA의 findById는 Optional을 리턴하므로, 일반 객체로 만들기 위해 뒤에 .get()을 사용합니다.
        // Optional은 참조형 변수에 대해서 null검사 및 처리를 쉽게 할 수 있도록 제공하는 제네릭입니다.
        // JPA는 Optional을 쓰는것을 권장하기 위해 리턴 자료형으로 강제해뒀습니다.
        return  blogJPARepository.findById(blogId).get();
    }

    @Transactional // 둘다 실행되던지 둘 다 실행 안되던지
    @Override
    public void deleteById(long blogId) {
        //replyRepository.deleteByBlogId(blogId);
        //blogRepository.deleteById(blogId);
        blogJPARepository.deleteById(blogId);
    }

    @Override
    public void save(Blog blog){
         //blogRepository.save(blog);
         blogJPARepository.save(blog);
    }

    @Override
    public void update(Blog blog) {
        // JPA 수정은, findById()를 이용해 얻어온 엔터티 클래스의 객체 내부 내용물을 수정한 다음
        // 해당 요소를 save() 해서 이뤄집니다.
        //blogRepository.update(blog);

        Blog updateBlog = blogJPARepository.findById(blog.getBlogId()).get();

        updateBlog.setBlogTitle(blog.getBlogTitle());
        updateBlog.setBlogContent(blog.getBlogContent());

        blogJPARepository.save(updateBlog);
    }

    public int getCalibratedPageNum(Integer pageNumber){
        if(pageNumber == null || pageNumber <= 0){
            pageNumber = 1;
            return pageNumber;
        } else {
            int totalPagesCount = (int) Math.ceil(blogJPARepository.count() / 10.0);

            pageNumber = pageNumber > totalPagesCount ? totalPagesCount : pageNumber;
        }

        return pageNumber;
    }
}

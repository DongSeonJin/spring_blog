package com.spring.blog.controller;

import com.spring.blog.entity.Blog;
import com.spring.blog.exception.NotFoundBlogIdException;
import com.spring.blog.service.BlogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;


@Controller // 컨트롤러 어노테이션은 1. 빈 등록 + 2. url 매핑 처리 기능을 함께 가지고 있으므로 다른 어노테이션과
            // 교환해서 쓸 수 없다
@RequestMapping("/blog")
@Log4j2 // sout이 아닌 로깅을 통한 디버깅을 위해 선언
public class BlogController {

    // 컨트롤러 레이어는 서비스 레이어를 직접 호출합니다.
    @Autowired
    private BlogService blogService;
    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    // /blog/list 주소로 get방식 접속했을때
    // 1. 서비스객체를 이용해 게시글 전체를 얻어오세요.
    // 2. 얻어온 게시글을 .jsp로 보낼 수 있도록 적재해주세요.
    // 3. .jsp 에서 볼 수 있도록 출력해주세요.
    // 해당 파일의 이름은 blog/list.jsp 입니다

    @RequestMapping({"/list/{pageNumber}", "/list"})
    public String List(Model model, @PathVariable(required = false) Integer pageNumber){
        Page<Blog> pageInfo = blogService.findAll(pageNumber);

        // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        final int PAGE_BTN_NUM = 10;

        // 현재 조회중인 페이지 번호(0부터 세므로 주의)
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지에 강조하기 위해서 필요

        // 현재 조회중인 페이지 그룹의 끝번호
        int endPageNum = (int)Math.ceil(currentPageNum / (double)PAGE_BTN_NUM) * PAGE_BTN_NUM;

        // 현재 조회중인 페이지 그룹의 시작번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1;

        // 마지막 그룹 번호 보정
        endPageNum = endPageNum > pageInfo.getTotalPages() ? pageInfo.getTotalPages() : endPageNum;

        // prev(이전 페이지) 버튼
        boolean prevBtn = startPageNum != 1; // startPageNum이 1이 아님


        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("endPageNum", endPageNum);
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("pageInfo", pageInfo);

        return "blog/list";
    }

    // 디테일 페이지의 주소 패턴
    // /blog/detail/글번호
    // 위 방식으로 글 번호를 입력받아, service를 이용해 해당 글 번호 요소만 얻어서
    // 뷰에 적재하는 코드를 아래쪽에 작성해주세요
    @RequestMapping("/detail/{blogId}")
    public String detail(@PathVariable int blogId, Model model, Principal principal){

        model.addAttribute("username", principal.getName());
        Blog blog = blogService.findById(blogId);
        model.addAttribute("blog", blog);

        if(blog == null){
            try {
                throw new NotFoundBlogIdException("없는 blogId로 조회했습니다. 조회번호 : " + blogId);
            } catch (NotFoundBlogIdException e){
                e.printStackTrace();// 예외 메세지 체크
                return "blog/NotFoundBlogIdExceptionResultPage";
            }
        }

        return "blog/detail";
    }

    // 폼 페이지와 실제 등록 url은 같은 url을 쓰도록 합니다.
    // 대신 폼 페이지는 GET방식으로 접속했을때 연결해주고
    // 폼에서 작성완료한 내용을 POST방식으로 제출해 저장하도록 만들어줍니다.


    @RequestMapping(value = "/insert", method= RequestMethod.GET)
    public String insert(Model model, Principal principal){
        // SecurityContext, Principal은 둘 다 인증정보를 가지고 있는 객체입니다.
        // 둘 중 편한걸 사용해주시면 됩니다.
        model.addAttribute("username", principal.getName());
        //WEB-INF/views/blog/blog-form.jsp
        return "blog/blog-form";
    }

    @RequestMapping(value = "/insert", method= RequestMethod.POST)
    public String insert(Blog blog){
        blogService.save(blog);
        return "redirect:/blog/list";
    }


    // DELETE로직은 삭제 한 /blog/list로 리다이렉트 되어서 자료가 삭제된 것을 확인해야 합니다.
    // 글 번호만으로 삭제를 진행해야 합니다.
    // 따라서, 디테일 페이지에 삭제버튼을 추가하고, 해당 버튼을 클릭했을때, 삭제 번호가 전달되어서
    // 전달받은 번호를 토대로 삭제하도록 로직을 구성해주시면 됩니다.
    // 폼에 추가한 삭제버튼 코드와 컨트롤러에 작성한 delete 메서드
    // 이 두 가지를 보내주세요.

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(long blogId){
        log.info(blogId);
        blogService.deleteById(blogId);
        return "redirect:/blog/list";
    }

    // UPDATE구문은 다른 내역은 다 INSERT와 비슷하지만
    // 한 가지 차이점은, 폼이 이미 기존에 작성된 정보로 채워져 있다는 점입니다.
    // 이를 구현하기 위해 수정 버튼이 눌렸을때, 제일 먼저 해당 글 정보를 회득한다음
    // 홈 페이지에 model.addAttribute()로 보내줘야 합니다.
    // 그 다음 수정용 폼 페이지에 해당 자료를 채운 채 연결해주면 됩니다.
    // 이를 위해 value 를 이용하면 미리 원하는 내용으로 폼을 채워둘 수 있습니다.
    @RequestMapping(value = "/updateform", method = RequestMethod.POST)
    public String update(long blogId, Model model){
        // blogId를 이용해서 blog 객체를 받아옵니다.
        Blog blog = blogService.findById(blogId);
        // .jsp로 보내기 위해 적제합니다.
        model.addAttribute("blog", blog);
        // /WEB-INF/views/blog/blog-update-form.jsp
        return "blog/blog-update-form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Blog blog){
        // 받아온 blog엔터티로 글 수정
        blogService.update(blog);

        // 리다이렉트는 가능하면 해당 글번호의 디테일페이지로 넘가가게 해 주고
        // 그게 어렵다면 list로 넘어가게 해주기.
        return "redirect:/blog/detail/" + blog.getBlogId();
    }
}

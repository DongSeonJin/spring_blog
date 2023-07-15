package com.spring.blog.dto;


import com.spring.blog.entity.Reply;
import lombok.*;

@AllArgsConstructor @Getter @Setter
@Builder @ToString @NoArgsConstructor
public class ReplyCreateRequestDTO {
    private long blogId;
    private String replyWriter;
    private String replyContent;

    public ReplyCreateRequestDTO(Reply reply){
        this.blogId = reply.getBlogId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    }
}

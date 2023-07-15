##
CREATE TABLE reply(
	reply_id int primary key auto_increment,
    blog_id int not null,
    reply_writer varchar(40) not null,
    reply_content varchar(200) not null,
    published_at datetime default now(),
    updated_at datetime default now()
);

# 외래키 설정
# blog_id에는 기존에 존재하는 글의 blog_id만 들어가야 한다.
alter table reply add constraint fk_reply foreign key (blog_id) references blog(blog_id);

# 더미 데이터 입력(테스트 DB에서만 사용합니다.)
INSERT INTO reply VALUES(null, 2, "댓글쓴사람", "1빠댓~", now(), now()),
(null, 2, "짹짹이", "꽥꽥쨱짹", now(), now()),
(null, 2, "바둑이", "멍멍멍멍머머머멍", now(), now()),
(null, 2, "야옹이", "야아아아아아옹", now(), now()),
(null, 2, "개발고수", "REST 개어렵노", now(), now());

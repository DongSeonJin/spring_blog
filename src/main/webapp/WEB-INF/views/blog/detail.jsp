<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-9ndCyUaIbzAi2FUVXJi0CjmCapSmO7SnpJef0486qhLnuZ2cdeRhO02iuK6FUUVM" crossorigin="anonymous">
<style>
    div {
        border: 1px solid black;
    }
</style>
</head>
<body>
    <div class="container">
        <!-- 모달 자리 -->
        <div class="modal fade" id="replyUpdateModal" tabindex="-1">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">댓글 수정하기</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                  작성자: <input type="text" class="form-control" id="modalReplyWriter"><br>
                  댓글내용: <input type="text" class="form-control" id="modalReplyContent">
                  <input type="hidden" id="modalReplyId" value="">
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                  <button type="button" class="btn btn-primary" data-bs-dismiss="modal" id="replyUpdateBtn">수정하기</button>
                </div>
              </div>
            </div>
          </div>

        ${blog}
        <div class="row">
            <div class="row first-row">
                <div class="col-2">
                    글번호
                </div>
                <div class="col-1">
                    ${blog.blogId}
                </div>
                <div class="col-2">
                    글제목
                </div>
                <div class="col-1">
                    ${blog.blogTitle}
                </div>
                <div class="col-2">
                    수정일
                </div>
                <div class="col-1">
                    ${blog.updatedAt}
                </div>
            </div>
            <div class="row second-row">
                <div class="col-1">
                    작성일
                </div>
                <div class="col-2">
                    ${blog.publishedAt}
                </div>
            </div>
            <div class="row fourth-row">
                <div class="col-1">
                    <a href="/blog/list"><button class="btn btn-secondary">목록으로</button> </a>
                </div>
                <div class="col-1">
                    <form action="/blog/delete" method="POST">
                        <input type="hidden" name="blogId" value="${blog.blogId}">
                        <input type="submit" value="삭제하기" class="btn btn-warning">
                    </form>
                </div>
                <div class="col-1">
                    <form action="/blog/updateform" method="POST">
                        <input type="hidden" name="blogId" value="${blog.blogId}">
                        <input type="submit" value="수정하기" class="btn btn-info">
                    </form>
                </div>
                <div class="row">
                    <div id="replies">

                    </div>
                </div>
                <div class="row">
                    <!-- 비동기 form의 경우는 목적지로 이동하지 않고 페이지 내에서 처리가 되므로
                    action을 가지지 않습니다. 그리고 제출버튼도 제출기능을 막고 fetch 요청만 넣습니다-->
                    
                    <div class="col-1">
                        <input type="text" class="form-control" name="replyWriter" id="replyWriter">
                    </div>
                    <div class="col-6">
                         <input type="text" class="form-control" name="replyContent" id="replyContent">
                    </div>
                    <div class="col-1">
                         <button class="btn btn-primary" id="replySubmit">댓글쓰기</button>
                    </div>
                    
                </div>
            </div>
        </div>
    </div>
    
        <script>
            // 글 구성에 필요한 글번호를 자바스크립트 변수에 저장
            let blogId = "${blog.blogId}";

            // blogId를 받아 전체 데이터를 JS내부로 가져오는 함수 선언
            function getAllReplies(Id){
                //<%-- jsp와 js가 모두 \${변수명} 문법을 공유하고, 이 중 .jsp파일에서는
                // \${}의 해석을 jsp식으로 먼저 하기 때문에, 해당 \${}가 백틱 내부에서 쓰이는 경우
                // \${} 형식으로 \를 추가로 왼쪽에 붙여서 jsp용으로 작성한 것이 아님을 명시해야함. --%>
                let url = `http://localhost:8080/reply/\${Id}/all`;
                
                let str = ""; // 받아온 json을 표현할 html 코드를 저장할 문자열 str 선언
                fetch(url, {method:'get'}) // get방식으로 위 주소에 요청넣기
                    .then((res) => res.json())// 응답받은 요소중 json만 뽑기
                    .then(replies => { // 뽑아온 json으로 처리작업하기
                        console.log(replies);
                        
                        //for(reply of replies){
                        //    console.log(reply);
                        //    console.log("------------");
                        //    str += `<h3>글쓴이: \${reply.replyWriter},
                        //        댓글내용: \${reply.replyContent}</h3>`
                       // }

                       // .map()을 이용한 간결한 반복문 처리
                       replies.map((reply, i) => { // 첫 파라미터 : 반복대상자료, 두번째 파라미터 : 순번
                            str += `<h3>\${i+1}번째 댓글 || 글쓴이: 
                                         <span id="replyWriter\${reply.replyId}">\${reply.replyWriter}</span>, 
                                    댓글내용: 
                                       <span id="replyContent\${reply.replyId}">\${reply.replyContent}</span>
                                        <span class="deleteReplyBtn" data-replyId="\${reply.replyId}">
                                                [삭제]
                                        </span>
                                        <span class="updateReplyBtn" data-replyId=\${reply.replyId} 
                                            data-bs-toggle="modal" data-bs-target="#replyUpdateModal">
                                                [수정]
                                        </span>
                                     </h3>`;
                                    
                       })
                        console.log(str);// 저장된 태그 확인

                        const $replies = document.getElementById('replies');

                        $replies.innerHTML = str;
                    });
            }
            // 함수 호출
            getAllReplies(blogId);

            // 해당 함수 실행시 비동기 폼에 작성된 글쓴이, 내용으로 댓글 입력
            function insertReply(){
                let url = `http://localhost:8080/reply`;
                
                //요소가 다 채워졌는지 확인
                if(document.getElementById("replyWriter").value.trim() === ""){
                alert("글쓴이를 채워주셔야 합니다.");
                return;
                }
                if(document.getElementById("replyContent").value.trim() === ""){
                alert("본문을 채워주셔야 합니다.");
                return;
                }

            
                fetch(url, {
                    method: 'post',
                    headers: {// header에는 보내는 데이터의 자료형에 대해서 기술
                        //json 데이터를 요청과 함께 전달, @RequestBody를 입력받는 로직에 추가
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ // 여기에 실질적으로 요청과 보낼 json정보를 기술함
                        replyWriter: document.getElementById("replyWriter").value,
                        replyContent: document.getElementById("replyContent").value,
                        blogId: "${blog.blogId}"
                    }), // insert 로직이기 때문에 response에 실제 화면에서 사용할 데이터가 전송 x
                }).then(()=>{
                    alert("댓글 작성이 완료되었습니다!");
                    // 댓글 작성 후 폼에 작성되어있던 내용 소거
                    document.getElementById("replyWriter").value = "";
                    document.getElementById("replyContent").value = "";
                    getAllReplies(blogId);
                });
            }

            // 제출버튼에 이벤트 연결하기
            $replySubmit = document.getElementById("replySubmit");
            // 버튼 클릭시 insertReply 내부 로직 실행
            $replySubmit.addEventListener("click", insertReply);

            const $replies = document.querySelector("#replies");

            // 이벤트 객체를 활용해야 이벤트 위임을 구현하기 수월하므로 먼저 html객체부터 자져옵니다.
            $replies.onclick = (e) => {
                // 클릭한 요소가 #replies의 자식태그인 .deleteReplyBtn 인지 검사하기
                // 이벤트객체.target.matches는 클릭한 요소가 어떤 태그인지 검사해줍니다.
                if(!e.target.matches('#replies .deleteReplyBtn') // 삭제버튼도 아니고
                    && !e.target.matches('#replies .updateReplyBtn')){ // 수정버튼도 아니고
                    return; // 위 두 조건이 모두 충족되면 기능 실행 x
                }else if(e.target.matches('#replies .deleteReplyBtn')){
                    // 클릭된 요소가 삭제버튼이라면
                    deleteReply();// 아래에 정의해둔 deleteeply() 호출해서 클릭된 요소 삭제
                } else if(e.target.matches('#replies .updateReplyBtn')){
                    // 클릭된 요소가 수정버튼이면
                    openUpdateReplyModal(); // 아래에 정의해둔 함수를 
                }

                // 수정버튼을 누르면 실행될 함수
                function openUpdateReplyModal(){
                    // 클릭이벤트 객체 e의 target 속성의 dataset 속성 내부에 댓글번호가 있으므로 확인
                    console.log(e.target.dataset['replyid']);

                    const replyId = e.target.dataset['replyid'];
                    //const replyId = e.target.dataset.replyid; 위와 동일

                    // hidden태그에 현재 내가 클릭한 replyId값을 value 프로퍼티에 저장해주기
                    const $modalReplyId = document.querySelector("#modalReplyId");
                    $modalReplyId.value = replyId;

                    // 가져올 id요소를 문자로 먼저 저장합니다.
                    let replyWriterId = `#replyWriter\${replyId}`;
                    let replyContentId = `#replyContent\${replyId}`;
                    
                    // 위에서 추출한 id번호를 이용해 document.querySelector 를 통해 
                    //  요소를 가져온 다음
                    // 해당 요소의 text값을 얻어서 모달창의 폼 양식 내부에 넣어줍니다.
                    
                    let $replyWriter = document.querySelector(replyWriterId);
                    let $replyContent = document.querySelector(replyContentId);

                    let replyWriterOriginalValue = $replyWriter.innerText;
                    let replyContentOriginalValue = $replyContent.innerText;

                    console.log(replyWriterOriginalValue);
                    console.log(replyContentOriginalValue);

                    const $modalReplyWriter = document.getElementById("modalReplyWriter");
                    const $modalReplyContent = document.getElementById("modalReplyContent");

                    $modalReplyWriter.value = replyWriterOriginalValue;
                    $modalReplyContent.value = replyContentOriginalValue;

                    // modal창 내부의 ReplyWriter, Replycontent를 적을 수 있는 폼을 가져옵니다.
                    
                    // 폼.value = InnerText 형식으로 추출한 값을 대입해줍니다.
                }
                // 삭제버튼을 누르면 실행될 함수.
                function deleteReply(){

                    // 클릭이벤트 객체 e의 target 속성의 dataset 속성 내부에 댓글번호가 있으므로 확인
                    console.log(e.target.dataset['replyid']);

                    const replyId = e.target.dataset['replyid'];

                    if(confirm("정말로 삭제하시겠어요?")){ // 에, 아니오로 답할수있는 경고창을 띄웁니다.
                        // 예를 선택하면 true, 아니오를 선택하면 false입니다.
                        let url = `/reply/\${replyId}`;

                        // 위 정보를 토대로 url 세팅 후 비동기 요청으로 삭제를 처리하고 댓글을 갱신해주세요.
                        fetch(url,{ method: 'delete'})
                            .then(()=>{
                                alert("댓글을 삭제했습니다.")
                                getAllReplies(blogId);
                        })
                    }else {
                        // 삭제 취소
                        return;
                    }
                }
             }

             // 수정창이 열렸고, 댓글 수정 내역을 모두 폼에 입력한 뒤 수정하기 버튼을 누를경우
             // 비동기 요청으로 수정 요청이 들어가도록 처리
             $replyUpdateBtn = document.querySelector('#replyUpdateBtn');

             $replyUpdateBtn.onclick = (e) => {
                // 히든으로 숨겨놓은 태그를 가져온 다음
                const $modalReplyId = document.querySelector("#modalReplyId");
                // 변수에 해당 글 번호를 저장한다음
                const replyId = $modalReplyId.value;
                // url에 포함시킴
                const url = `http://localhost:8000/reply/\${replyId}`;


                // 그 후 비동기 요청 넣기
                fetch(url, {
                    method: 'PATCH',
                    headers: {
                        "Content-Type" : "application/json",
                    },
                    body: JSON.stringify({
                        replyWriter : document.querySelector("#modalReplyWriter").value,
                        replyContent : document.querySelector("#modalReplyContent").value,
                        replyId : replyId, // 위에 선언한 replyId 변수에 들어있는 값
                    }),
                }).then(() => {
                    // 폼 소거
                    document.getElementById("replyWriter").value = "";
                    document.getElementById("replyContent").value = "";
                    getAllReplies(blogId); // 목록 갱신
                })
             }

            
        </script>
        <!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
        
</body>
</html>
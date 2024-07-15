# Java WAS

2024 우아한 테크캠프 프로젝트 WAS

# 사이트 접속
- 메인 페이지  
  - http://16.171.140.177:8080/index
  - http://16.171.140.177:8080/index.html


# 제약사항
- 로깅, assertj 외 라이브러리 금지
- java 17
- mvc naming 금지
- nio 금지
- System.out.println 금지

# 요구 사항
## 1-1
- http://localhost:8080/index.html 접속시 src/main/resources/static 디렉토리의 index.html 반환
- http request 내용 로깅
- 멀티 스레드 서버

## 1-2
- src/main/resources/static 폴더의 svg, css, js, ico, png, jpg 응답 지원
- GET localhost:8080/index 요청시
  - 브라우저에서 GET localhost:8080/xx.img, GET localhost:8080/xx.css, 등의 요청을 보낸다

## 1-3
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/registration 로 이동(href)
  - stiac/registration/index.html 응답
- 회원가입 폼에서 사용자 입력 후 가입 버튼 클릭시 다음과 같이 전송 
  - /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
    - query string
      - userId=javajigi
      - password=password
      - name=%EB%B0%95%EC%9E%AC%EC%84%B1
      - email=javajigi%40slipp.net
- url 파싱해 User 클래스 저장

# 2-1
- 회원 가입 POST 요청
  - query string 이 아니라 request body 지원
  - 성공시 redirect
  - CREATE 요청시 실패 처리

# 2-2
- User Data Base 
  - User 를 관리한다
- 로그인 버튼 클리식
  - /user/loging.html 이동
- 로그인 api
  - 아이디와 비밀번호가 같은 User 확인
    - 성공시 
      - index.html 리다이렉트
      - 응답 헤더에 쿠키 값 sid=세션ID; Path=/
      - 세션 아이디는 무작위 숫자 또는 문자열
      - 서버는 세션 아이드를 활용해 User 자원 요청 가능해야 한다
    - 실패시 /user/login_failed.html 이동
- /index 접속시 사용자가 로그인 상태인지 쿠키로 확인
  - 로그인 상태일 경우 로그인 버튼 대신 사용자 nickname 표시 와 로그아웃 버튼
  - 아닐 경우 로그인 버튼 사용 가능

# 2-3
- /user/list 
  - 로그인 상태일 경우 생성된 모든 user 출력
  - 로그인 하지 않고 접근시 로그인 페이지로 리다이렉트


# 3-1
- index.html 하단에 글쓰기 버튼 추가
  - 글쓰기 버튼 클릭시 write.html로 이동
  - 로그인 하지 않은 사용자는 로그인 페이지로 이동
- write.html
  - 폼으로 글의 본문 입력
- 작성 글 제목들을 index.html 에 보여준다
- 메인 게시글 아래에 댓글을 달 수 있게 구현
- 존재하지 않는 url 요청은 404
- 처리할 수 없는 메서드 요청은 405
- h2와 jdbc를 사용해 회원 정보 저장

# 3-2
- 이미 업로드
  - 게시글 작성 페이지에 이미지 첨부 기능 추가
  - post multipart form data 서버 전송
  - 서버에서 로컬 파일로 저장
- 게시글 조회 페이지에서 업로드한 이미지 보이게 수정

# 3-3
- csv 파일을 데이터베이스로 사용하는 기능 추가
  - csv 파일 읽고/쓰기 기능
  - 사용자 정보를 user.csv 관리
  - 게시글 정보를 board.csv 관리
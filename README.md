# Segment
협동분산시스템 팀프로젝트

# 목차

[1. 프로젝트 설명](#프로젝트-설명)

[2. Database 구조](#database-구조)

[3. Sequence Diagram](#sequence-diagram)

[4. 시연 설명](#시연-설명)

[5. 개선할 부분](#개선할-부분)

---

# 프로젝트 설명

- 2021년 1학기 협동분산시스템 강의에서 진행한 **팀 프로젝트**
- 기간 : 2021.05.09 ~ 2021.06.06
<h3>프로젝트 컨셉</h3>

- 특정 주제를 가지고 대화를 나눌 수 있는 메인 채팅방과 해당 주제와 관련된 소주제에 대해 대화를 나눌 수 있는 소 채팅방
- 메인 채팅방은 5개로 정해져있으며, 각 메인 채팅방에는 최대 3개의 소 채팅방이 개설될 수 있다.
- 일반 사용자는 메인 채팅방을 개설할 수 없다. 관리자가 개설
- 일반 사용자는 메인 채팅방에 속한 소 채팅방이 3개 미만이라면 새로운 소 채팅방을 개설할 수 있다.
- 메인 채팅방은 일반 사용자에 의해 삭제될 수 없다.
- 소 채팅방은 해당 주제에 대한 토론이 끝나면, 일반 사용자에 의해 삭제될 수 있다.

<h3>팀원</h3>

- 주윤지 : 팀장, 메인 채팅방 신청 입장 퇴장 기능, 유저 페이지 구현
- 류다현 : 팀원, 전체적인 View 구성 & 회원가입, 로그인, 로그아웃 구현
- 마지영 : 팀원, 작은 채팅방 생성, 입장, 퇴장, 삭제 기능 구현 & 메인 채팅방 기능 구현 보조 & 전체 코드 수정 및 뷰 연결
- 오예림 : 팀원, 예전 메세지 불러오기, 채팅 메시지 보내기, 서버에서 채팅 뿌리기 구현
<br>

---

# Database 구조

![ERD](https://user-images.githubusercontent.com/50768959/159203561-64edfe4e-62dd-4e65-a862-728326c8359c.jpg)

# Sequence Diagram

<details>
    <summary>회원 가입</summary>

  <h3>회원 가입</h3>
  
  ![Sequence Diagram 1 회원가입](https://user-images.githubusercontent.com/50768959/159204605-b4388f9c-0750-4320-b9ef-ccd28384443b.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|SignUp<br>(클라이언트)|회원가입 요청<br>(유저 추가 요청)|Client(View)|Server|String nickname<br>String password|
|Signup<br>(서버)|User DB에 유저 추가하기|Server|User DB|String nickname<br>String password|
  
</details>

<details>
    <summary>로그인</summary>

  <h3>로그인</h3>
  
![Sequence Diagram 2 로그인](https://user-images.githubusercontent.com/50768959/159205264-99d053ba-ebbd-4d8c-bb0e-467e88bdde35.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|Login<br>(클라이언트)|유저 로그인 요청|Client(View)|Server|String nickname<br>String password|
|Login<br>(서버)|유저 확인 및 status 변경|Server|User DB|Int clientNumber|
  
</details>

<details>
    <summary>메인 채팅방 목록 열기</summary>

  <h3>메인 채팅방 목록 열기</h3>
  
![Sequence Diagram 3 메인 채팅방 목록 열기](https://user-images.githubusercontent.com/50768959/159205376-c30a1870-da9a-4442-8802-36c02e27974a.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|requestList<br>(클라이언트)|메인 채팅방 목록 요청|Client(View)|Server| |
|showList<br>(서버)|메인 채팅방 목록 Room DB에서 받아서 보내주기|Server|Room DB| |
  
</details>

<details>
    <summary>메인 채팅방 입장</summary>

  <h3>메인 채팅방 입장</h3>
  
![Sequence Diagram 4 메인 채팅방 입장](https://user-images.githubusercontent.com/50768959/159205475-a4bdaba0-441c-4299-b024-8c32d3c1d273.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|enterBigRoom<br>(클라이언트)|메인 채팅방 입장 요청|Client(View)|Server|Int bigRoomNumber|
|enterBigRoom<br>(서버)|요청 받은 Room DB에 해당 유저 추가|Server|Room DB|Int bigRoomNumber<br>Int chattingNumber|
|requestOldMessage<br>(서버)|Room DB에 해당 채팅방 역대 채팅 요청|Server|Room DB|Int clientNumber<br>Int bigRoomNumber|
</details>

<details>
    <summary>메인/소규모 채팅방에 채팅 입력</summary>

  <h3>메인/소규모 채팅방에 채팅 입력</h3>
  
![Sequence Diagram 5 메인 소규모 채팅방에 채팅 입력](https://user-images.githubusercontent.com/50768959/159205646-41068e82-b3f9-4f7f-abff-26607c1ddada.jpg)


|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|sendMessage<br>(클라이언트)|메인 채팅방에 채팅 입력|Client(View)|Server|Int bigRoomNumber<br>String Message|
|sendMessage<br>(서버)|해당 Room DB에 채팅 추가|Server|Room DB|Int clientNumber<br>Int bigRoomNumber<br>String Message|
|scatterMessage<br>(서버)|해당 메인 채팅방에 들어와 있는 사용자들한테 채팅 뿌리기|Server|(Other) Client|Int chattingNumber|
</details>

<details>
    <summary>소규모 채팅방 개설</summary>

  <h3>소규모 채팅방 개설</h3>
  
![Sequence Diagram 6 소규모 채팅방 개설](https://user-images.githubusercontent.com/50768959/159205772-6f92e7ed-c91a-48a2-b093-ea9f39bbe423.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|createSmallRoom<br>(클라이언트)|소규모 채팅방 개설 요청|Client(View)|Server|Int bigRoomNumber|
|createSmallRoom<br>(서버)|소규모 채팅방의 DB 만들기|Server|Room DB|Int clientNumber<br>Int bigRoomNumber<br>String topic|
</details>

<details>
    <summary>소규모 채팅방 종료하기</summary>

  <h3>소규모 채팅방 종료하기</h3>

  ![Sequence Diagram 7 소규모 채팅방 종료하기](https://user-images.githubusercontent.com/50768959/159205896-e5ce47c2-e6b6-46a5-9657-9ddac66f52c7.jpg)


|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|destroySmallRoom<br>(클라이언트)|소규모 채팅방 종료 요청|Client(View)|Server|Int bigRoomNumber<br>Int smallRoomNumber|
|destroySmallRoom<br>(서버)|소규모 채팅방의 DB 삭제|Server|Room DB|Int bigRoomNumber<br>Int smallRoomNumber|
</details>

<details>
    <summary>메인 채팅방 나가기</summary>

  <h3>메인 채팅방 나가기</h3>

![Sequence Diagram 8 메인 채팅방 나가기](https://user-images.githubusercontent.com/50768959/159206057-b60d6821-5ee7-453d-8749-8279ce883648.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|exitBigRoom<br>(클라이언트)|메인 채팅방 퇴장 요청|Client(View)|Server|Int bigRoomNumber|
|exitBigRoom<br>(서버)|메인 채팅방 Room DB 중 해당 user 제거|Server|Room DB|Int bigRoomNumber<br>Int clientNumber|
</details>


<details>
    <summary>유저 페이지 조회</summary>

  <h3>유저 페이지 조회</h3>

  ![Sequence Diagram 9 유저 페이지 조회](https://user-images.githubusercontent.com/50768959/159206108-9dbfd3c7-e31a-4660-bd99-7fa5773a9f40.jpg)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|enterUserPage<br>(클라이언트)|유저 리스트 및 상태 요청|Client(View)|Server| |
|checkAllStatus<br>(서버)|모든 유저의 리스트와 status 요청|Server|User DB| |
</details>

<details>
    <summary>로그아웃</summary>

  <h3>로그아웃</h3>
  
![Sequence Diagram 10 로그아웃](https://user-images.githubusercontent.com/50768959/159206186-ce4731b1-b941-4eb6-a9e5-48a66410f838.png)

|Function|Role|Sender|Receiver|Message fields|
|------|---|---|---|---|
|Logout<br>(클라이언트)|로그아웃 요청|Client(View)|Server| |
|Logout<br>(서버)|User DB에서 user status 변경|Server|User DB|Int clientNumber|
</details>

# 시연 설명

<details>
    <summary>시연 설명 (사진)</summary>

- 회원가입

<img src="https://user-images.githubusercontent.com/50768959/159206432-96e6dd95-04ac-49e9-a076-62bb713ebdeb.jpg" width="300" />

회원가입이 정상적으로 되는 모습

---

- 로그인

<img src="https://user-images.githubusercontent.com/50768959/159206447-58a2123e-be57-43e6-b388-8974afabfb52.jpg" width="300" />

로그인 성공 시, 로그인 성공 메세지를 확인하며 로그인을 완료한다.

---

- 유저 목록 확인
                                                                                                                           
<img src="https://user-images.githubusercontent.com/50768959/159207302-ad11e90e-2d53-4507-bf10-0c7784dd8579.jpg" width="300" />

User Status을 누르면, 사용자의 현재 접속 여부를 확인할 수 있다.<br>
아래쪽 부분에 보이는 초록색은 현재 online, 위쪽 부분에 보이는 회색은 offline을 의미한다.

---

- 큰 채팅방 목록 확인

<img src="https://user-images.githubusercontent.com/50768959/159207770-2d4bbc10-4b83-4e06-a223-ec7f6a811140.jpg" width="300" />

Go Chatroom을 누르면, 다음과 같이 큰 채팅방 목록이 나오고, 이 중 하나를 선택하면 입장할 수 있다.

---

- 채팅방 내부 및 채팅 보내기

<img src="https://user-images.githubusercontent.com/50768959/159207985-4e43bb5e-ae73-4c98-a216-c421fa8aebf9.jpg" width="300" />

채팅방을 선택하면, DB에 저장되어 있던 이전 메세지들이 보이고 그 후에 채팅을 보냈을 때도 잘 보내지는 것을 확인할 수 있다.

---

- 큰 채팅방 내, 소 채팅방 목록 확인

<img src="https://user-images.githubusercontent.com/50768959/159208047-00467059-06fe-401f-9f60-4e0f76caa070.jpg" width="300" />
<img src="https://user-images.githubusercontent.com/50768959/159208088-c1d2ca7e-3834-423d-99e4-0f3a6fd011cb.jpg" width="300" />

큰 채팅방 내에서 소채팅방 개설을 누를 경우, 소채팅방이 없으면 만들기 버튼이 있고 생성되어있을 경우 다음 사진과 같이 들어가기(이동) 버튼이 있는 것을 확인할 수 있다.

---

- 소 채팅방 삭제

<img src="https://user-images.githubusercontent.com/50768959/159208164-78a4597f-23e0-4cc8-92a4-00bb64f5b49e.jpg" width="300" />

작은 채팅방도 큰 채팅방과 마찬가지로 채팅을 작성할 수 있고, 큰 채팅방과 다른 점은 다음과 같이 방을 삭제하는 기능이 있다는 것이다.
  
</details>


# 개선할 부분

<h3>1. 새로운 채팅을 더 효과적으로 전달할 방법 : Polling</h3>

클라이언트는 주기적으로 본인이 속한 채팅방과, 해당 채팅방에서 마지막으로 읽은 채팅 번호(이하 last chat id)를 서버에게 보냅니다. 서버는 이를 보고 해당 채팅방에서의 가장 최근 채팅번호가 last chat id보다 크다면 last chat id 이후의 채팅을 클라이언트에게 전달합니다. 이때, 전달받은 채팅 번호들을 통해 중간에 빈 번호가 있는지 체크를 함으로써 오류를 체크할 수 있습니다.

<h3>2. 전달 받은 채팅 데이터를 어떻게 처리할지</h3>

클라이언트는 전달받은 채팅을 미리보기와 알람, 그리고 채팅방에 띄울 때 사용하게 됩니다.
따라서, 특정 채팅방에 특정 개수가 넘게 채팅이 쌓일 경우, 저장한 채팅을 버리고, 채팅방 입장 시 요청하는 방식을 사용하면 효율적일 것이라고 생각했습니다. 채팅이 적게 온 채팅방의 경우, 채팅방 입장 시에도 서버에 요청할 필요 없이 채팅을 띄울 수 있고, 채팅이 너무 많이 온 채팅방의 경우 너무 많은 채팅을 다 클라이언트가 들고 있지 않아도 되기 때문입니다.

<h3>3. 서버는 어떻게 구성할 것인지</h3>
<h4>* Distribution? Replication?</h4>


채팅은 하나의 데이터에 많은 사용자들이 집중적으로 모이는 경우가 아님<br>

=> __replication이 적절하지 않다고 생각__ <br>

=> **distribution 방식으로 각 서버가 채팅방들을 나누어서 관리** <br>


<h4>* Chatroom별 서버 & Server_Chatroom 테이블을 관리하는 관리 서버</h4>

- 클라이언트는 관리 서버에게 채팅방 번호를 제시하여 채팅을 요청할 서버를 안내받습니다. 
- 클라이언트는 안내받은 Chatroom 서버에 채팅을 요청합니다. 
- 클라이언트는 안내받은 Chatroom  서버를 캐싱할 수 있고, 만약 해당 채팅방이 없어졌다면, 관리 서버는 해당 Chatroom 서버가 없음을 클라이언트에게 알려줄 수 있습니다.

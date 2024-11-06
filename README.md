# DemoChat
채팅프로그램 데모버전

작동원리
1. Admin이 chatRoom을 생성을 함. -> 채팅방 생성됨.
2. client가 chatRoom에 접속을 함.
3. 채팅을 입력함.

(상세)

chatRoom인스턴스는 DB에 저장되는 것이 아니라, JVM 메모리 힙 영역에 반드시 상주를 해야 함. ( Session 관리를 위해서 메모리에 항상 존재해야함.)
각각의 chatRoom인스턴스는 개별적으로 Unique한 Set<WebSocketSession>을 가지고 있음.

예시)
사용자가 1이라는 chatRoom에 접속을 한 경우 -> 해당 사용자의 Session은 chatRoom의 id가 1인 Session집합에 저장이 됨. ( 이는 다른 chatRoom에게도 동일하게 적용이 됨.)
채팅을 입력할 경우 -> id가 1인 chatRoom을 find한 후 -> 해당 chatRoom의 Set<WebSocketSession>의 Session들에게 Message를 브로드캐스팅을 함.

(추가)
1. 프론트엔드에서는 useEffect을 통해서, 백엔드 서버와 Socket연결을 함. 단, Socket은 useRef를 사용했고, 채팅이 연결된 상태에서 Socket에 대한 변경감지가 필요없을 것이라 판단이 됨.
-> 따라서, 오로지 마운트시에만 useEffect가 작동이 되도록 설정했음.

2. 나가기를 누름 -> Socket연결 끊어짐 + 뒤로가기
3. 전송을 누름 -> Chat메시지를 전송.

==========

[개선부분]
1. 예기치 못한 문제에 의해서, 채팅서버가 꺼질 수 있음 -> 해당 채팅의 모든 Session 삭제 -> 모든 사용자 강제 종료.
따라서, 외부 Session Storage를 생성하든 or 로드밸런싱을 쓸 경우 모든 Session을 복사해서 다른 동일한 서버에 복사해서 뿌리든 조치를 취해야 할 것으로 보임.

2. Socket연결이 된 상태에서 client가 새로고침을 할 경우, Session이 중복으로 등록이 됨 -> 예기치 못한 오류 발생.
따라서, 사용자가 새로고침을 못하도록 해야함.


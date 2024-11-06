import { useEffect, useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';


const ChatRoom = ()=>{
    const webSocket = useRef(null);
    const roomId = useParams();
    const navigate = useNavigate();


    const [message,setMessage] = useState('');
    const [contents,setContents] = useState([]);

    //console.log(contents);

    useEffect(()=>{
        webSocket.current = new WebSocket('ws://localhost:7000/ws/chat');

        webSocket.current.onopen = () => {
            //console.log('WebSocket 연결!'); 
            let sayHello = new Object();
            sayHello.id = roomId.id;
            sayHello.content = "Greeting~";
            sayHello.state="Enter";

            const json = JSON.stringify(sayHello);

            webSocket.current.send(json);// 서버로 메시지를 전송을 한다. 
            console.log("WebSocket 연결!");
        };
        webSocket.current.onclose = (error) => { // webSocket.current?.close()이 활성화될 경우 작동 -> Exit()를 할 경우 활성화
            console.log("ONCLOSE");
            console.log(error);
            webSocket.current?.close(); // 서버와의 연결을 끊는다.
            navigate('/chat/list');
        }
        webSocket.current.onerror = (error) => {
            console.log("ERROR");
            console.log(error);
            webSocket.current?.close(); // 서버와의 연결을 끊는다.
            navigate('/chat/list');
        }
        webSocket.current.onmessage = (event) => { // 서버로부터 받은 모든 Message를 onmessage 이벤트가 처리.  
            //console.log(event.data);
            const receive = event.data;

            setContents(prevList => [...prevList, receive]);

            console.log(contents);
        };
    },[])

    const Send = ()=>{
        
        let chat = new Object();
        chat.id = roomId.id;
        chat.content = message;
        chat.state="message";

        const json = JSON.stringify(chat);

        webSocket.current.send(json);

    }

    const Exit = ()=>{
        //console.log("Bye~");
        let sayGoodBye = new Object();
            sayGoodBye.id = roomId.id;
            sayGoodBye.content = "Bye";
            sayGoodBye.state="Exit";

            const json = JSON.stringify(sayGoodBye);

            webSocket.current.send(json);

        webSocket.current?.close(); // 서버와의 연결을 끊는다.
        navigate('/chat/list');
    }

    const ChatMessages = ()=>{
        const chatContents = [];
        for(let i=0; i<contents.length; i++){
            chatContents.push(
                <div key={i}>{contents[i]}</div>
            )
        }
        return chatContents;
    }

    return(
        <div>
            <h1>Hello Chat!</h1>
            <input type='text' onChange={(e)=>{setMessage(e.target.value)}} placeholder='채팅할 내용을 입력하세요'/>
            <button onClick={()=>{Send()}}>전송하기</button>
            <button onClick={()=>{Exit()}}>나가기</button>
            <br></br>
            {ChatMessages()}
        </div>
        
        
    )
    ;
}

export default ChatRoom;
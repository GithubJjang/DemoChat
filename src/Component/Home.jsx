import axios from "axios"
import { useEffect, useState } from "react"
import { Link } from 'react-router-dom';

const ChatList = ()=>{

    const [list,setList] = useState('');

    useEffect(()=>{
        console.log("Get ChatRoom");
        axios.get('http://localhost:7000/chat/room/list',{})
        .then(
            
            // 성공했을떄, 반환이 되는 값
            (Response) => {

            console.log(Response.data)
            const fetchChats = Response.data
            setList(fetchChats);

        }                
        ).
        catch(
            // 실패, 즉 에러가 발생했을떄 발생.
            (error) => {
                console.log(error)
            }
        )
        
            }
        ,[])
    
    ;

    const chatList = () =>{

        const result = [];

        for (let i = 0; i < list.length; i++) {
          result.push(
                      <div key={i}>
                        <Link to={`/chat/room/${list[i].id}`}><div>{list[i].name}</div></Link>
                      </div>);
        }
        return result;
         
    }
    ;

    const page =(
        <div>
            <h2>My First ChatRoom</h2>
            {chatList()}
        </div>

    )
    ;

    return page;

}
;

export default ChatList;
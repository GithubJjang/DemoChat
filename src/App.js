import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './Component/Home';
import ChatRoom from './Component/ChatRoom';


function App() {

  return(
    <BrowserRouter>
      <Routes>
        <Route path='/chat/list' element={<Home></Home>}></Route>
        <Route path='/chat/room/:id' element={<ChatRoom></ChatRoom>}></Route>
      </Routes>
    </BrowserRouter>

  )
}

export default App;

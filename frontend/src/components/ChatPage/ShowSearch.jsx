import React, { useContext, useEffect, useState } from 'react'
import Search from './Search'
import Chats from './Chats'
import { api } from '../../api/api';
import ChatRoomProvider from '../../context/ChatRoomProvider';
import { ChatRoomContext } from '../../context/ChatRoomContext';

const ShowSearch = ({setSelectedPerson,setShowChat,setStartConversation,setShowStart,chatRoomRef}) => {
    const [searchValue, setSearchValue] = useState('');

    const {resUsers,searchUsers} = useContext(ChatRoomContext);

    useEffect(()=>{
      if(searchValue.length>2){
        chatRoomRef.current.classList.add("hidden");
        searchUsers(searchValue);
        console.log(resUsers+"this are users!!");
      }else{
        chatRoomRef.current.classList.remove("hidden");
      }
    },[searchValue])
    
  return (
    <>
        <Search searchValue={searchValue} setSearchValue={setSearchValue}></Search>
        {
          <Chats userInfo={resUsers} setSelectedPerson={setSelectedPerson} setShowChat={setShowChat} setStartConversation={setStartConversation} setShowStart={setShowStart}></Chats>
        } 
    </>
  )
}

export default ShowSearch
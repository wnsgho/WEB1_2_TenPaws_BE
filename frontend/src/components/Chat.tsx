import { useEffect, useRef, useState } from "react";
import logo from "../assets/logo.png";
import axios from "axios";
import useStore from "../store/store";

interface userChatRoom {
  chatRoomId: number;
  unReadCount: number;
  oppositeEmail: string;
  oppositeName: string;
  userEmail: string;
}

interface chatUser {
  username: string;
  email: string;
}

interface chatMessage {
  message: string;
  chatDate: string;
  senderEmail: string;
  senderName: string;
  unRead: number;
}

const Chat = () => {
  const isLoggedIn = localStorage.getItem("accessToken");
  if (!isLoggedIn) return null;
  
  const [makeChatRoom, setMakeChatRoom] = useState(false);
  const [chatRoomOpen, setChatRoomOpen] = useState(false);
  const [oppositeEmail, setOppositeEmail] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [userChatRoom, setUserChatRoom] = useState<userChatRoom[]>([]);
  const [chatUser, setChatUser] = useState<chatUser[]>([]);
  const [chatRoomId, setChatRoomId] = useState<number | null>(null);
  const [chatMessage, setChatMessage] = useState<chatMessage[]>([]);
  const [message, setMessage] = useState("");
  const [currentSubscription, setCurrentSubscription] = useState<any>(null);
  const [subscribed, setSubscribed] = useState(false);
  const { connectWebSocket, stompClient, isConnected, fetchChatroom, setFetchChatroom } = useStore();
  const [searchTerm, setSearchTerm] = useState('');
const filteredUsers = chatUser.filter(user => 
  user.email.toLowerCase().includes(searchTerm.toLowerCase()) || 
  (user.username && user.username.toLowerCase().includes(searchTerm.toLowerCase()))
);
const location = window.location.pathname;

  const handlemakechatroom = () => {
    setMakeChatRoom(!makeChatRoom);
    setChatRoomOpen(false);
  };

  const handlechatroomopen = async (RoomId: number) => {
    if (chatRoomOpen && chatRoomId === RoomId) {
      handleCloseChatRoom();
      return;
    }

    try {
      console.log("채팅방 열기 - 룸ID:", RoomId);
      setChatRoomOpen(true);
      setMakeChatRoom(false);
      setChatRoomId(RoomId);

      // 이전 메시지 조회
      const messageResponse = await axios.get(`http://15.164.103.160:8080/api/v1/chatmessages/${RoomId}`, {
        headers: {
          Authorization: localStorage.getItem("accessToken")
        }
      });
      setChatMessage(messageResponse.data);

      // 채팅방을 실제로 열었을 때만 읽음 처리
      await initializeUnRead(RoomId);
      await fetchChatroom();
    } catch (error) {
      console.error("채팅방 열기 실패", error);
    }
  };

  // 채팅방 닫을 때 구독 해제
  const handleCloseChatRoom = () => {
    if (currentSubscription) {
      currentSubscription.unsubscribe();
      setCurrentSubscription(null);
      setSubscribed(false);
    }
    setChatRoomOpen(false);
    setChatRoomId(null);
    setChatMessage([]);
    
    // 채팅방 닫을 때 목록 갱신
    fetchChatroom();
  };

  // 현재 사용자의 이메일 추출
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const email = payload.email;
      setUserEmail(email);
    }
  }, [userEmail]);

  // fetchChatroom 함수 전역
  useEffect(() => {
    setFetchChatroom(async () => {
      try {
        const response = await axios.get("http://15.164.103.160:8080/api/v1/chatrooms/user", {
          headers: {
            Authorization: localStorage.getItem("accessToken")
          }
        });
        setUserChatRoom(response.data);
      } catch (error) {
        console.error("목록 조회 실패", error);
        throw error;
      }
    });
  }, [setFetchChatroom]);

  //WebSocket 연결 및 채팅방 목록 로드
  useEffect(() => {
    if (!isConnected) {
      connectWebSocket();
    }
    fetchChatroom();
  }, [isConnected, connectWebSocket, fetchChatroom]);

  //채팅방 생성
  const handleCreateChat = async () => {
    try {
      const response = await axios.post(
        "http://15.164.103.160:8080/api/v1/chatrooms",
        {
          userEmail,
          oppositeEmail
        },
        {
          headers: {
            Authorization: localStorage.getItem("accessToken"),
            "Content-Type": "application/json"
          }
        }
      );
      
      alert("채팅방이 생성 되었습니다.");
      await fetchChatroom();
      
      if (response.data && response.data.chatRoomId) {
        // 채팅방을 생성하고 바로 구독
        if (stompClient?.connected) {
          const subscription = stompClient.subscribe(`/topic/chatroom/${response.data.chatRoomId}`, async (message) => {
            console.log('채팅방 메시지 수신:', message.body);
            const receivedMessage = JSON.parse(message.body);
            setChatMessage(prev => [...prev, receivedMessage]);
            
            await initializeUnRead(response.data.chatRoomId);
            await fetchChatroom();
          });
          setCurrentSubscription(subscription);
          setSubscribed(true);
        }
        
        await handlechatroomopen(response.data.chatRoomId);
      }
      
      setMakeChatRoom(false);
    } catch (error) {
      console.error("채팅방 생성 실패", error);
      alert("채팅방 생성에 실패하였습니다.");
    }
  };

  //참여중인 채팅방 목록 조회
  useEffect(() => {
    const fetchChatroom = async () => {
      try {
        const response = await axios.get("http://15.164.103.160:8080/api/v1/chatrooms/user", {
          headers: {
            Authorization: localStorage.getItem("accessToken")
          }
        });
        setUserChatRoom(response.data);
      } catch (error) {
        console.error("목록 조회 실패", error);
      }
    };

    if (userEmail) {
      fetchChatroom();
    }
  }, []);

  //채팅방 삭제
  const handleChatDelete = async () => {
    console.log(chatRoomId);
    try {
      await axios.delete(`http://15.164.103.160:8080/api/v1/chatrooms/${chatRoomId}`, {
        headers: {
          Authorization: localStorage.getItem("accessToken")
        }
      });
      alert("채팅방을 삭제 하였습니다.");
      await fetchChatroom();
      setChatRoomOpen(false);
    } catch (error) {
      console.error("채팅방 삭제 실패", error);
    }
  };

  // 스크롤 관리를 위한 ref 추가
  const messageEndRef = useRef<HTMLDivElement>(null);

  // 스크롤 이동 함수
  const scrollToBottom = () => {
    messageEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  // 새 메시지가 추가될 때마다 스크롤 이동
  useEffect(() => {
    scrollToBottom();
  }, [chatMessage]); 

  const sendMessage = async () => {
    if (!message.trim() || !stompClient?.connected || !chatRoomId) return;

    try {
      stompClient.publish({
        destination: `/app/chat/send/${chatRoomId}`,
        body: JSON.stringify({
          message: message,
          senderEmail: userEmail,
          receiverEmail: oppositeEmail
        })
      });

      
      await fetchChatroom();
      setMessage("");
    } catch (error) {
      console.error("메시지 전송 실패:", error);
    }
  };

  //회원 목록 조회
  useEffect(() => {
    const fetchChatUser = async () => {
      try {
        const response = await axios.get("http://15.164.103.160:8080/api/v1/users/chat-users", {
          headers: {
            Authorization: localStorage.getItem("accessToken")
          }
        });
        setChatUser(response.data);
      } catch (error) {
        console.error("회원 목록 조회에 실패하였습니다.", error);
      }
    };
    fetchChatUser();
  }, []);

 
  useEffect(() => {
    if (chatRoomId && stompClient?.connected && !subscribed) {
      console.log("채팅방 구독 시작:", chatRoomId);
      
      if (currentSubscription) {
        currentSubscription.unsubscribe();
        setSubscribed(false);
      }

      const subscription = stompClient.subscribe(`/topic/chatroom/${chatRoomId}`, async (message) => {
        console.log('채팅방 메시지 수신:', message.body);
        const receivedMessage = JSON.parse(message.body);
        setChatMessage(prev => [...prev, receivedMessage]);
        
        
        await initializeUnRead(chatRoomId);
        await fetchChatroom();
      });
      
      setCurrentSubscription(subscription);
      setSubscribed(true);
    }

    return () => {
      if (currentSubscription) {
        currentSubscription.unsubscribe();
        setSubscribed(false);
      }
    };
  }, [chatRoomId, stompClient, userEmail]);

  
  const initializeUnRead = async (roomId: number) => {
    try {
      await axios.put(
        `http://15.164.103.160:8080/api/v1/unread/init`,
        {
          userEmail: userEmail,
          chatRoomId: roomId
        },
        {
          headers: {
            Authorization: localStorage.getItem("accessToken")
          }
        }
      );
      await fetchChatroom();  
    } catch (error) {
      console.error("읽음 처리 실패:", error);
    }
  };

  
  useEffect(() => {
    if (isConnected) {
      const updateChatList = async () => {
        try {
          console.log("경로 변경으로 인한 채팅 목록 갱신:", location);
          await fetchChatroom();
        } catch (error) {
          console.error("채팅 목록 갱신 실패:", error);
        }
      };

      updateChatList();
    }
  }, [location, isConnected, fetchChatroom]); 

  
  useEffect(() => {
    if (isConnected) {
      const updateChatList = async () => {
        try {
          await fetchChatroom();
        } catch (error) {
          console.error("채팅 목록 갱신 실패:", error);
        }
      };

      const handleFocus = () => {
        updateChatList();
      };

      window.addEventListener('focus', handleFocus);
      window.addEventListener('visibilitychange', () => {
        if (document.visibilityState === 'visible') {
          updateChatList();
        }
      });

      return () => {
        window.removeEventListener('focus', handleFocus);
        window.removeEventListener('visibilitychange', handleFocus);
      };
    }
  }, [isConnected, fetchChatroom]);

  return (
    <div className="fixed bottom-[89px] right-2 z-50">
      <div>
        {/* 채팅방 생성 UI*/}

        {makeChatRoom && (
        <div className="bg-[#f1a34a] absolute -left-96 -top-80 w-96 rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)] h-[500px]">
        <div className="p-4 border-b border-[#3c2a13]">
          <input 
            type="text" 
            placeholder="사용자 검색..."
            className="w-full p-2 rounded-md shadow-sm"
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        
        <div className="max-h-[350px] overflow-y-auto">
          {filteredUsers.map((user) => (
            <div 
            key={user.email}
            className={`p-4 border-b border-gray-300 cursor-pointer hover:bg-[#f8b968] transition-colors
              ${oppositeEmail === user.email ? 'bg-[#f8b968]' : ''}`}
            onClick={() => setOppositeEmail(user.email)}>
            <div className="font-semibold">
              {user.username?.toLowerCase().includes('naver') 
                ? '네이버로그인 사용자'
                : user.username?.toLowerCase().includes('kakao')
                  ? '카카오로그인 사용자'
                  : user.username || '사용자'}
            </div>
            <div className="text-sm text-gray-600">{user.email}</div>
          </div>
        ))}
      </div>
      
        <div className="p-4 border-t border-[#3c2a13]">
          <button
            className="w-full bg-[#3c2a13] text-white font-bold py-2 px-4 rounded-md hover:scale-105 hover:transition-transform"
            onClick={handleCreateChat}>
            채팅방 생성
          </button>
        </div>
      </div>
        )}

        {/* 채팅방 생성 버튼 */}
        <div
          className="bg-[#f1a34a] m-6 p-6 rounded-full font-bold text-[40px] w-16 h-16 flex justify-center items-center pl-[24.6px] pb-[35px] cursor-pointer  hover:scale-105 transition-transform shadow-[0_0_15px_rgba(0,0,0,0.5)]"
          onClick={handlemakechatroom}>
          +
        </div>
      </div>

      {/* 채팅방 내부  */}
      {chatRoomOpen && (
        <div className="fixed bottom-[30px] right-[114px] z-50">
          <div className="bg-[#f1a34a] w-[384px] h-[590px] rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)]">
            {/* 헤더 */}
            {userChatRoom
              .filter((item) => item.chatRoomId === chatRoomId)
              .map((item) => (
                <div className="bg-white p-3 rounded-t-lg flex justify-between" key={item.chatRoomId}>
                  <div className="font-bold">{item.oppositeName}</div>
                  <div className="cursor-pointer flex gap-3">
                    <div onClick={handleChatDelete}>🗑️</div>
                    <div onClick={handleCloseChatRoom}>✖️</div>
                  </div>
                </div>
              ))}

            <div className="bg-white mx-3 mt-3 w-76 h-[477px] rounded-t-lg overflow-y-auto max-h-[500px] scrollbar-hide">
              {chatMessage.map((message, index) =>
                message.senderEmail === userEmail ? (
                  // 자신의 메시지
                  <div className="flex p-4 justify-end" key={message.chatDate + index}>
                    <div className="flex flex-col items-end">
                      <div className="text-sm pb-1.5 pr-1">{message.senderName}</div>
                      <div className="flex items-end gap-1">
                        <div className="p-2 rounded-xl bg-[#f1a34a] break-words">{message.message}</div>
                      </div>
                    </div>
                    <div className="rounded-full w-10 h-10 min-w-10 min-h-10 ml-2">
                      <img src={logo} alt="logo" className="w-full h-full object-cover" />
                    </div>
                  </div>
                ) : (
                  // 상대방의 메시지
                  <div className="flex p-4" key={message.chatDate + index}>
                    <div className="rounded-full w-10 h-10 min-w-10 min-h-10">
                      <img src={logo} alt="logo" className="w-full h-full object-cover" />
                    </div>
                    <div>
                      <div className="ml-2 pb-1.5 text-sm">{message.senderName}</div>
                      <div className="flex items-end gap-1">
                        <div className="ml-2 p-2 rounded-xl bg-[#f1a34a] break-words">{message.message}</div>
                      </div>
                    </div>
                  </div>
                )
              )}
              <div ref={messageEndRef} />
            </div>

            <div className="bg-white mx-3 w-76 h-10 rounded-b-lg border-t-2 border-black flex justify-between">
              <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                onKeyPress={(e) => e.key === "Enter" && sendMessage()}
                className="w-80 focus:outline-none p-2 text-sm"
                placeholder="메시지를 입력하세요"
              />
              <div
                onClick={sendMessage}
                className="text-3xl px-1 cursor-pointer hover:scale-105 transition-transform duration-300">
                ➤
              </div>
            </div>
          </div>
        </div>
      )}

      {/* 채팅방1 아이콘 (목록 조회)*/}
      {userChatRoom.map((item) => (
        <div className="transition-transform hover:scale-105" key={item.chatRoomId}>
          <div
            className="bg-[#f1a34a] rounded-full w-16 h-16 m-6 items-center justify-center cursor-pointer"
            onClick={() => handlechatroomopen(item.chatRoomId)}>
            <div className="absolute top-13 right-5">
              <div className=" bg-red-600 text-white rounded-full px-1.5 min-w-[20px] h-[20px] flex items-center justify-center text-sm font-bold ">
                {item.unReadCount > 99 ? (
                  <>
                    {item.unReadCount}
                    <span className="pb-[4px] pl-[1px]">+</span>
                  </>
                ) : (
                  item.unReadCount
                )}
              </div>
            </div>
            <div className="p-3 pl-3.5 pt-3.5 font-bold text-3xl text-center ">{item.oppositeName.charAt(0)}</div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default Chat;

import { create } from "zustand";
import { Client } from "@stomp/stompjs";

interface StoreState {
  role: string | null;
  setRole: (role: string | null) => void;
  
  // WebSocket 관련 상태
  stompClient: Client | null;
  setStompClient: (client: Client | null) => void;
  
  // 채팅방 관련 상태
  chatRooms: any[];
  setChatRooms: (rooms: any[]) => void;
  
  // WebSocket 연결 상태
  isConnected: boolean;
  setIsConnected: (connected: boolean) => void;
  
  // 채팅방 목록 갱신 함수
  fetchChatroom: () => Promise<void>;
  setFetchChatroom: (fetch: () => Promise<void>) => void;

  // WebSocket 연결 함수
  connectWebSocket: () => void;
  disconnectWebSocket: () => void;
}

const useStore = create<StoreState>((set, get) => ({
  role: null,
  setRole: (role) => set({ role }),
  
  stompClient: null,
  setStompClient: (client) => set({ stompClient: client }),
  
  chatRooms: [],
  setChatRooms: (rooms) => set({ chatRooms: rooms }),
  
  isConnected: false,
  setIsConnected: (connected) => set({ isConnected: connected }),
  
  fetchChatroom: async () => {},
  setFetchChatroom: (fetch) => set({ fetchChatroom: fetch }),

  connectWebSocket: () => {
    const token = localStorage.getItem("accessToken")?.replace("Bearer ", "");
    if (!token || get().isConnected) return;

    const client = new Client({
      brokerURL: `ws://15.164.103.160:8080/ws?authorization=${token}`,
      connectHeaders: {
        Authorization: token
      },
      onConnect: () => {
        console.log("WebSocket 전역 연결 성공");
        set({ isConnected: true });
        
        // 전역 알림 구독
        client.subscribe("/user/queue/notifications", async (message) => {
          const notification = JSON.parse(message.body);
          console.log("새 메시지 알림:", notification);
          
          // 알림 표시 및 채팅방 목록 갱신
          alert(notification.content);
          try {
            await get().fetchChatroom();
          } catch (error) {
            console.error("채팅방 목록 갱신 실패:", error);
          }
        });

        // 모든 채팅방 메시지 구독 (전역)
        client.subscribe("/user/queue/messages", async (message) => {
          console.log("새 메시지 수신:", message.body);
          try {
            await get().fetchChatroom();
          } catch (error) {
            console.error("채팅방 목록 갱신 실패:", error);
          }
        });

        // 채팅방 삭제 알림 구독
        client.subscribe("/user/queue/chatroom-close", async (message) => {
          const notice = JSON.parse(message.body);
          alert(notice.message);
          await get().fetchChatroom();
        });
      }
    });

    try {
      client.activate();
      set({ stompClient: client });
    } catch (error) {
      console.error("WebSocket 전역 연결 실패:", error);
    }
  },

  disconnectWebSocket: () => {
    const { stompClient } = get();
    if (stompClient?.connected) {
      localStorage.removeItem('wasConnected');
      stompClient.deactivate();
      set({ stompClient: null, isConnected: false });
    }
  }
}));

export default useStore;
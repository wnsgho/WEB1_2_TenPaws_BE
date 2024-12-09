import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance";

const Alarm = () => {
  const navigate = useNavigate();

  const [notifications, setNotifications] = useState<
    { id: number; content: string; createdAt: string; read: boolean }[]
  >([]);
  const [unreadCount, setUnreadCount] = useState<number | string>("정보 없음");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  // 날짜와 시간 형식 변환
  const formatDateTime = (dateString: string) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hours = String(date.getHours()).padStart(2, "0");
    const minutes = String(date.getMinutes()).padStart(2, "0");
    return `${year}.${month}.${day} ${hours}:${minutes}`;
  };

  // 알림 목록 및 읽지 않은 알림 수 불러오기
  const fetchNotifications = async (currentPage: number = page) => {
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
      alert("로그인이 되어 있지 않아 로그인 페이지로 이동합니다.");
      navigate("/login");
      return;
    }

    const authHeader = accessToken.startsWith("Bearer ")
      ? accessToken
      : `Bearer ${accessToken}`;

    try {
      const [notificationsResponse, unreadCountResponse] = await Promise.all([
        axiosInstance.get(`/api/v1/notifications?page=${currentPage}`, {
          headers: { Authorization: authHeader },
        }),
        axiosInstance.get("/api/v1/notifications/unread-count", {
          headers: { Authorization: authHeader },
        }),
      ]);

      const newNotifications = notificationsResponse.data.content.map((item: any) => ({
        id: item.id,
        content: item.content,
        createdAt: item.createdAt,
        read: item.read,
      }));

      if (currentPage === 0) {
        setNotifications(newNotifications);
      } else {
        setNotifications((prev) => [...prev, ...newNotifications]);
      }

      setUnreadCount(unreadCountResponse.data ?? "정보 없음");
      setTotalPages(notificationsResponse.data.page.totalPages);
    } catch (err) {
      setError("알림 목록을 가져올 수 없습니다.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotifications();
  }, [page]);

  const fetchNextPage = async () => {
    if (page < totalPages - 1) {
      await fetchNotifications(page + 1);
      setPage((prev) => prev + 1);
    }
  };

  // 알림 삭제
  const deleteNotification = async (id: number) => {
    const accessToken = localStorage.getItem("accessToken");
  
    if (!accessToken) {
      alert("로그인이 필요합니다.");
      return;
    }
  
    const authHeader = accessToken.startsWith("Bearer ")
      ? accessToken
      : `Bearer ${accessToken}`;
  
    try {
      await axiosInstance.delete(`/api/v1/notifications/${id}`, {
        headers: { Authorization: authHeader },
      });
  
      setNotifications((prev) => prev.filter((notification) => notification.id !== id));
  
      const unreadCountResponse = await axiosInstance.get("/api/v1/notifications/unread-count", {
        headers: { Authorization: authHeader },
      });
      setUnreadCount(unreadCountResponse.data ?? "정보 없음");
  
      const remainingNotifications = notifications.filter((notification) => notification.id !== id);
      const remainingCount = remainingNotifications.length;
      const neededCount = 10 - remainingCount;
  
      if (neededCount > 0 && page < totalPages - 1) {
        const nextPage = page + 1;
        const nextNotificationsResponse = await axiosInstance.get(
          `/api/v1/notifications?page=${nextPage}`,
          { headers: { Authorization: authHeader } }
        );
  
        const nextNotifications = nextNotificationsResponse.data.content.map((item: any) => ({
          id: item.id,
          content: item.content,
          createdAt: item.createdAt,
          read: item.read,
        }));
  
        const newNotifications = [...remainingNotifications, ...nextNotifications.slice(0, neededCount)];
        setNotifications(newNotifications);
  
        if (nextNotifications.length <= neededCount) {
          setPage(nextPage);
        }
      }
    } catch {
      console.error("알림 삭제 중 오류 발생");
    }
  };

  // 알림 읽음 처리
  const markAsRead = async (id: number) => {
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
      alert("로그인이 필요합니다.");
      return;
    }

    const authHeader = accessToken.startsWith("Bearer ")
      ? accessToken
      : `Bearer ${accessToken}`;

    try {
      await axiosInstance.patch(`/api/v1/notifications/${id}/read`, null, {
        headers: { Authorization: authHeader },
      });

      setNotifications((prev) =>
        prev.map((notification) =>
          notification.id === id ? { ...notification, read: true } : notification
        )
      );
      setUnreadCount((prev) =>
        typeof prev === "number" ? Math.max(prev - 1, 0) : prev
      );
    } catch (err) {
      console.error("알림 읽음 처리 중 오류 발생:", err);
    }
  };

  return (
    <div className="min-h-screen flex flex-col">
      <header className="relative flex items-center px-10 py-6 bg-white shadow-md z-50">
        <img
          src="/src/assets/before.svg"
          alt="Back"
          className="w-8 h-8 cursor-pointer"
          onClick={() => navigate(-1)}
        />
        <div className="flex-grow text-center">
          <span className="text-4xl font-bold" style={{ color: "#7F5546" }}>
            알림
          </span>
        </div>
      </header>

      <main className="flex-grow flex flex-col items-center bg-gray-100 py-6 px-4">
        {/* 읽지 않은 알림 */}
        <div className="w-[92%] sm:w-[90%] md:w-[87%] lg:w-[85%] xl:w-[85%] 2xl:w-[85%] flex justify-between items-center mb-4">
          <span className="text-2xl font-bold pl-5">
            읽지 않은 알림: {error ? "정보 없음" : unreadCount}
          </span>
        </div>

        {loading && page === 0 ? (
          <div className="flex-grow flex items-center justify-center">
            <h1 className="text-xl text-gray-500">알림을 불러오는 중 입니다.</h1>
          </div>
        ) : error ? (
          <div className="flex-grow flex items-center justify-center">
            <h1 className="text-xl text-gray-500">{error}</h1>
          </div>
        ) : notifications.length > 0 ? (
          <div className="w-[92%] sm:w-[90%] md:w-[87%] lg:w-[85%] xl:w-[85%] 2xl:w-[85%] space-y-4">
            {notifications.map((notification) => (
              <div
                key={notification.id}
                className={`shadow-md rounded-xl p-6 relative ${
                  notification.read ? "bg-gray-200" : "bg-white hover:bg-[#f0efef]"
                }`}
                onClick={() => markAsRead(notification.id)}
              >
                <div className="flex justify-between items-center mb-2">
                  <span className="text-lg text-gray-500">{formatDateTime(notification.createdAt)}</span>
                  <img
                    src="/src/assets/x2.svg"
                    alt="Delete"
                    className="w-7 h-7 cursor-pointer"
                    onClick={(e) => {
                      e.stopPropagation();
                      deleteNotification(notification.id);
                    }}
                  />
                </div>
                <p className="text-xl">{notification.content}</p>
              </div>
            ))}
            {page < totalPages - 1 && (
              <div className="w-full flex justify-end">
                <span
                  className="text-gray-500 text-xl cursor-pointer hover:underline"
                  onClick={fetchNextPage} 
                >
                  더보기
                </span>
              </div>
            )}
          </div>
        ) : (
          <div className="flex-grow flex items-center justify-center">
            <h1 className="text-2xl text-gray-500">현재 알림이 없습니다.</h1>
          </div>
        )}
      </main>
    </div>
  );
};

export default Alarm;
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance"; 
import notificationIcon from "../assets/notification.svg";

const Footer: React.FC = () => {
  const [announcements, setAnnouncements] = useState<
    { id: number; title: string }[]
  >([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate();

  // 공지사항 데이터 가져오기
  useEffect(() => {
    const fetchAnnouncements = async () => {
      try {
        const response = await axiosInstance.get("/api/v1/announcements", {
          headers: {
            "Content-Type": "application/json",
          },
        });
        setAnnouncements(response.data.content || []); 
      } catch (error) {
        console.error("공지사항 데이터를 가져오는데 실패했습니다.", error);
      }
    };
    fetchAnnouncements();
  }, []);

  // 10초마다 공지사항 목록을 변경하도록 설정
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) =>
        announcements.length > 0 ? (prevIndex + 1) % announcements.length : 0
      );
    }, 10000); 

    return () => clearInterval(interval);
  }, [announcements]);

  const currentAnnouncement =
    announcements.length > 0 ? announcements[currentIndex] : null;

  // 목록 클릭 시 공지사항 상세 페이지로 이동하기
  const handleAnnouncementClick = async () => {
    if (currentAnnouncement) {
      try {
        await axiosInstance.get(`/api/v1/announcements/${currentAnnouncement.id}`, {
          headers: {
            "Content-Type": "application/json",
          },
        });
        navigate(`/guide/announcement/${currentAnnouncement.id}`);
      } catch (error) {
        console.error("공지사항 목록을 조회하는데 실패했습니다.", error);
      }
    }
  };

  return (
    <footer className="bg-gray-300 text-black py-2 px-4 sm:px-6 md:px-7 lg:px-9 flex items-center w-full">
      {/* 공지사항 아이콘 */}
      <img
        src={notificationIcon}
        alt="Notification Icon"
        className="w-8 h-8 sm:w-9 sm:h-9 md:w-10 md:h-10 lg:w-11 lg:h-11 mr-2 sm:mr-3 md:mr-4 lg:mr-5"
      />

      {/* 공지사항 텍스트 및 목록 */}
      <div className="flex items-center">
        <span className="text-lg sm:text-xl md:text-xl lg:text-2xl font-bold mr-2 sm:mr-4 md:mr-6">
          공지사항
        </span>
        <span
          className="text-base sm:text-lg md:text-xl lg:text-2xl font-medium hover:underline cursor-pointer"
          onClick={handleAnnouncementClick} 
        >
          {currentAnnouncement?.title || "공지사항이 없습니다."}
        </span>
      </div>
    </footer>
  );
};

export default Footer;
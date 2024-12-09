import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import axiosInstance from "../utils/axiosInstance";
import useUserStore from "../store/store";

const Header = () => {
  const [isDropdownVisible, setIsDropdownVisible] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [expandedSection, setExpandedSection] = useState<string | null>(null);
  const [userRole, setUserRole] = useState<string | null>(null);

  // Zustand 사용자 역할 가져오기
  const setRoleSave = useUserStore((state) => state.setRole); 
  
  const navigate = useNavigate();

  const showDropdown = () => setIsDropdownVisible(true);
  const hideDropdown = () => setIsDropdownVisible(false);
  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const handleLogout = async () => {
    const accessToken = localStorage.getItem("accessToken");

    if (!accessToken) {
      alert("이미 로그아웃 되어 있는 상태입니다.");
      setIsLoggedIn(false);
      localStorage.removeItem("isSocialLogin");
      navigate("/");
      return;
    }

    try {
      const authHeader = accessToken.startsWith("Bearer ")
        ? accessToken
        : `Bearer ${accessToken}`;

      const response = await axiosInstance.post(
        "/logout",
        {},
        { headers: { Authorization: authHeader } }
      );

      if (response.status === 200) {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("isSocialLogin");
        setIsLoggedIn(false);
        alert("로그아웃 되었습니다.");
        window.dispatchEvent(new Event("storage"));
        navigate("/");
      }
    } catch (error) {
      alert("로그아웃을 할 수 없습니다.");
      console.error("로그아웃 실패:", error);
    }
  };

  useEffect(() => {
    const updateLoginState = () => {
      const accessToken = localStorage.getItem("accessToken");
  
      setIsLoggedIn(!!accessToken);
  
      if (accessToken) {
        const authHeader = accessToken.startsWith("Bearer ")
          ? accessToken
          : `Bearer ${accessToken}`;
  
        axiosInstance
          .get("/api/v1/features/role", {
            headers: { Authorization: authHeader },
          })
          .then((response) => {
            setUserRole(response.data.role || null);
            setRoleSave(response.data.role || null); // zustand 상태 저장
          })
          .catch((error) => {
            setIsLoggedIn(false);
            setUserRole(null);
            setRoleSave(null) // zustand 상태 저장
            console.error("사용자 역할 가져오기 실패:", error);
          });
      } else {
        setUserRole(null);
      }
    };

  
    updateLoginState();
  
    const handleStorageChange = () => {
      updateLoginState();
    };
  
    window.addEventListener("storage", handleStorageChange);
  
    return () => {
      window.removeEventListener("storage", handleStorageChange);
    };
  }, []);
  

  const getUserInfoLink = () => {
    return userRole === "ROLE_SHELTER" ? "/mypage-shelter" : "/mypage-user";
  };

  return (
    <>
      <header className="relative flex items-center justify-between px-10 py-3 sm:py-3 md:py-3 lg:py-3 xl:py-3 bg-white shadow-md z-50">
        {/* TENPAWS - 프로젝트명 */}
        <div
          className="text-4xl md:text-4xl lg:text-4xl xl:text-4xl 2xl:text-4xl font-bold"
          style={{ color: "#7F5546" }}
        >
          <Link to="/">TenPaws</Link>
        </div>

        {/* 매칭, 안내, 내정보와 드롭다운 바 */}
        <div className="hidden md:flex flex-1 justify-center relative">
          <div className="flex justify-between items-center w-full max-w-3xl text-3xl md:text-3xl lg:text-3xl xl:text-3xl 2xl:text-3xl font-medium rounded-lg px-0 py-3">
            {/* 매칭 */}
            <div
              className="relative flex-1 text-center hover:scale-105 transition-transform duration-200"
              onMouseEnter={showDropdown}
              onMouseLeave={hideDropdown}
            >
              <span>매칭</span>
              <div className="absolute top-full left-0 w-full h-4"></div>
            </div>
            {/* 안내 */}
            <div
              className="relative flex-1 text-center hover:scale-105 transition-transform duration-200"
              onMouseEnter={showDropdown}
              onMouseLeave={hideDropdown}
            >
              <span>안내</span>
              <div className="absolute top-full left-0 w-full h-4"></div>
            </div>
            {/* 내정보 */}
            <div
              className="relative flex-1 text-center hover:scale-105 transition-transform duration-200"
              onMouseEnter={showDropdown}
              onMouseLeave={hideDropdown}
            >
              <span>내정보</span>
              <div className="absolute top-full left-0 w-full h-4"></div>
            </div>
          </div>

          {/* 드롭다운 바 */}
          {isDropdownVisible && (
            <div
              className="absolute left-1/2 transform -translate-x-1/2 bg-[#ffffff]/80 shadow-none z-[1000] w-[100%] lg:w-[95%] xl:w-[90%]"
              style={{
                top: "calc(100%)",
                backdropFilter: "blur(4px)",
                borderBottomLeftRadius: "8px",
                borderBottomRightRadius: "8px",
              }}
              onMouseEnter={showDropdown}
              onMouseLeave={hideDropdown}
            >
              <div className="flex divide-x divide-gray-300 py-7">
                {/* 매칭 세부사항 */}
                <div className="flex-1 flex flex-col items-center px-4">
                  <Link
                    to="/matching"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    반려동물 조회
                  </Link>
                  {userRole === "ROLE_SHELTER" && (
                    <Link
                      to="/detailadd"
                      className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                    >
                      반려동물 등록
                    </Link>
                  )}
                  <Link
                    to="/ai-matching"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    AI 매칭 시스템
                  </Link>
                </div>
                {/* 안내 세부사항 */}
                <div className="flex-1 flex flex-col items-center px-4">
                  <Link
                    to="/guide/announcement"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    공지사항
                  </Link>
                  <Link
                    to="/guide/facilities"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    관련 시설
                  </Link>
                  <Link
                    to="/guide/walking-course"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    산책 코스
                  </Link>
                </div>
                {/* 내정보 세부사항 */}
                <div className="flex-1 flex flex-col items-center px-4">
                  <Link
                    to={getUserInfoLink()}
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    나의 정보
                  </Link>
                  <Link
                    to="/prefer"
                    className="font-normal text-black mb-2 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    선호 동물 입력 및 수정
                  </Link>
                  {/* <Link
                    to="/my-walking-course"
                    className="font-normal text-black mb-3 text-sm sm:text-lg md:text-xl lg:text-2xl xl:text-2xl hover:scale-105 transition-transform duration-200"
                  >
                    나의 산책 코스
                  </Link> */}
                  {isLoggedIn && (
                    <div
                      className="flex items-center justify-end w-full hover:scale-105 cursor-pointer mt-8"
                      onClick={handleLogout}
                    >
                      <span className="text-black text-sm sm:text-base md:text-base lg:text-lg xl:text-xl font-medium">
                        로그아웃
                      </span>
                      <img
                        src="/src/assets/logout.svg"
                        alt="Logout Icon"
                        className="w-6 h-6 ml-2"
                      />
                    </div>
                  )}
                </div>
              </div>
            </div>
          )}
        </div>

        {/* 일반 헤더: 회원가입/알림 아이콘 */}
        {/* 로그인이 되어 있지 않은 경우 회원가입 버튼 / 로그인이 되어 있는 경우 알림 버튼으로 구현 */}
        <div className="hidden md:block text-2xl font-medium">
          {!isLoggedIn ? (
            <Link to="/signup" className="hover:text-gray-700">
              회원가입
            </Link>
          ) : (
            <div className="flex items-center">
              <Link to="/alarm">
                <img
                  src="/src/assets/alarm.svg"
                  alt="Alarm Icon"
                  className="w-9 h-9 cursor-pointer"
                />
              </Link>
            </div>
          )}
        </div>

        {/* 모바일 헤더 */}
        <button
          onClick={toggleSidebar}
          className="md:hidden flex flex-1 justify-end"
        >
          <img src="/side.svg" alt="Side menu" className="w-12 h-16 cursor-pointer" />
        </button>

        {/* 사이드바 */}
        {isSidebarOpen && (
          <div className="fixed inset-0 z-50">
            <div
              className="fixed inset-0 bg-gray-800 bg-opacity-50"
              onClick={toggleSidebar}
            ></div>
            <div className="fixed top-0 right-0 w-3/4 max-w-sm h-full bg-white shadow-lg z-50 flex flex-col">
              <div className="flex items-center justify-between px-6 py-5 bg-[#D7B8A3]">
                <Link
                  to="/"
                  className="text-3xl font-bold text-white transition-transform hover:scale-105"
                >
                  HOME
                </Link>
                <div className="flex items-center">
                  {!isLoggedIn ? (
                    <Link
                      to="/signup"
                      className="text-3xl font-semibold text-white pr-5 transition-transform hover:scale-105"
                    >
                      회원가입
                    </Link>
                  ) : (
                    <button
                      onClick={handleLogout}
                      className="text-3xl font-semibold text-white pr-5 transition-colors hover:scale-105"
                    >
                      로그아웃
                    </button>
                  )}
                  <button
                    onClick={toggleSidebar}
                    className="flex items-center justify-center ml-5 transition-transform hover:scale-105"
                  >
                    <img
                      src="/src/assets/x.svg"
                      alt="Close sidebar"
                      className="w-10 h-10"
                    />
                  </button>
                </div>
              </div>
              <nav className="flex-grow flex flex-col">
                {[
                  {
                    name: "매칭",
                    link: "#",
                    items: [
                      { name: "반려동물 조회", link: "/matching" },
                      { name: "반려동물 등록", link: "/detailadd", role: "ROLE_SHELTER" }, // ROLE_SHELTER 조건 추가
                      { name: "AI 매칭 시스템", link: "/ai-matching" },
                    ],
                  },
                  {
                    name: "안내",
                    link: "#",
                    items: [
                      { name: "공지사항", link: "/guide/announcement" },
                      { name: "반려동물 관련 시설", link: "/guide/facilities" },
                      { name: "산책 코스", link: "/guide/walking-course" },
                    ],
                  },
                  {
                    name: "내정보",
                    link: "#",
                    items: [
                      { name: "나의 정보", link: userRole === "ROLE_SHELTER" ? "/mypage-shelter" : "/mypage-user" },
                      { name: "선호동물 입력 및 수정", link: "/prefer" },
                    ],
                  },
                  ...(isLoggedIn
                    ? [{ name: "알림", link: "/alarm", items: [] }]
                    : []),
                ]
                  .map((section) => ({
                    ...section,
                    items: section.items.filter((item) => !item.role || item.role === userRole), // role 기반 필터링
                  }))
                  .map((section) => (
                    <div
                      key={section.name}
                      onMouseEnter={() => setExpandedSection(section.name)}
                      onMouseLeave={() => setExpandedSection(null)}
                      className={`relative group ${
                        ["매칭", "안내", "내정보", "알림"].includes(section.name)
                          ? "bg-[#FCF7F7]"
                          : ""
                      }`}
                    >
                      {section.link === "#" ? (
                        <span
                          className="p-5 block text-3xl transition-transform hover:scale-105 font-medium"
                        >
                          {section.name}
                        </span>
                      ) : (
                        <Link
                          to={section.link}
                          className="p-5 block text-3xl transition-transform hover:scale-105 font-medium"
                        >
                          {section.name}
                        </Link>
                      )}
                      <div className="h-px bg-[#E3E2E2]"></div>
                      {expandedSection === section.name && section.items.length > 0 && (
                        <div className="bg-[#ffffff]">
                          {section.items.map((item, index) => (
                            <div key={index}>
                              <Link
                                to={item.link}
                                className="block pl-7 p-5 text-2xl transition-transform hover:scale-105"
                              >
                                {item.name}
                              </Link>
                              <div className="h-px bg-[#E3E2E2]"></div>
                            </div>
                          ))}
                        </div>
                      )}
                    </div>
                  ))}
              </nav>
            </div>
          </div>
        )}
      </header>
    </>
  );
};

export default Header;

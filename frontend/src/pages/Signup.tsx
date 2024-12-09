import React from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";

const Signup = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      {/* 헤더 */}
      <Header />

      {/* 회원가입 페이지 */}
      <main className="flex-grow bg-[#CDC3BF] flex items-center justify-center relative">
        {/* 네모 박스 */}
        <div className="absolute w-[90%] h-[85%] bg-white shadow-lg rounded-lg flex flex-col md:flex-row overflow-hidden">
          {/* 네모 박스 왼쪽(로고) */}
          <div className="hidden md:flex flex-1 bg-[#EDEDED] px-8 py-14 md:px-8 md:py-14 lg:px-8 lg:py-14 flex-col justify-between">
            <div>
              <h1 className="text-3xl md:text-5xl lg:text-6xl xl:text-7xl text-gray-800 mb-2 md:mb-3 lg:mb-4">
                환영합니다!
              </h1>
            </div>

            {/* Ten.이랑 개발바닥 로고 */}
            <div className="flex items-end justify-center gap-2 md:gap-3 lg:gap-6 xl:gap-8">
              <span
                className="text-[3rem] md:text-[5rem] lg:text-[8rem] xl:text-[10rem] 2xl:text-[12rem] font-semibold text-gray-800"
              >
                Ten.
              </span>
              <img
                src="/src/assets/logo.png"
                alt="로고"
                className="w-[120px] h-[120px] md:w-[180px] md:h-[180px] lg:w-[220px] lg:h-[220px] xl:w-[250px] xl:h-[250px] 2xl:w-[300px] 2xl:h-[300px] rounded-full bg-[#7F5546]"
              />
            </div>

            {/* 로그인하기 */}
            <div className="text-sm md:text-2xl lg:text-2xl xl:text-3xl text-gray-600 flex items-center gap-1 md:gap-2 lg:gap-3">
              <p>이미 계정이 있으신가요?</p>
              <button
                className="font-bold transition-transform transform hover:scale-105"
                onClick={() => navigate("/login")}
              >
                로그인하기
              </button>
            </div>
          </div>

          {/* 네모 박스 오른쪽(회원가입 폼) */}
          <div className="flex-1 bg-[#FFFFFF] px-8 py-14 md:px-8 md:py-14 lg:px-8 lg:py-14 flex flex-col justify-between w-full md:w-auto">
            {/* 제목 */}
            <h1 className="text-5xl md:text-4xl lg:text-5xl xl:text-6xl font-bold text-gray-800 mb-3 md:mb-4 lg:mb-6">
              회원가입하기
            </h1>

            {/* 회원가입 버튼(개인 회원 및 보호소) */}
            <div className="flex flex-col items-center justify-center flex-grow gap-8">
              <button
                className="w-[480px] sm:w-[480px] md:w-[330px] lg:w-[450px] xl:w-[500px] h-[120px] sm:h-[120px] md:h-[100px] lg:h-[110px] xl:h-[130px] bg-[#B8A399] rounded-2xl text-6xl sm:text-6xl md:text-5xl lg:text-6xl xl:text-7xl font-semibold text-black shadow-md transition-transform transform hover:scale-105"
                onClick={() => navigate("/create-user1")}
              >
                개인 회원
              </button>
              <button
                className="w-[480px] sm:w-[480px] md:w-[330px] lg:w-[450px] xl:w-[500px] h-[120px] sm:h-[120px] md:h-[100px] lg:h-[110px] xl:h-[130px] bg-[#D8D8D8] rounded-2xl text-6xl sm:text-6xl md:text-5xl lg:text-6xl xl:text-7xl font-semibold text-black shadow-md transition-transform transform hover:scale-105"
                onClick={() => navigate("/create-user2")}
              >
                보호소
              </button>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Signup;

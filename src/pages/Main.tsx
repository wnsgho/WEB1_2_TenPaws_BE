import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import Chat from "../components/Chat";
import FAQ from "../components/FAQ";
import Main2 from "../assets/main3.png";
import AImatching from "../assets/pet-house (2) 1.svg";
import met from "../assets/pet-food 1.png";
import care from "../assets/animal (1) 1.png";

// 상세 설명 데이터 타입 정의
type IconType = "dog" | "approve" | "board" | "check";
type Description = {
  title: string;
  content: string[];
};

const descriptions: Record<IconType, Description> = {
  dog: {
    title: "매칭 동물 결정",
    content: [
      "필요한 조건을 골라 원하는 반려 동물들을 결정해 보세요."
      // "고르는데 어려움이 있으시면 AI 기능을 활용해 보세요.",
    ]
  },
  approve: {
    title: "매칭 신청",
    content: ["매칭 신청 시 보호소 사이트로 이동하게 되며 보호소 양식에 따라 입양 신청서 작성이 필요합니다."]
  },
  board: {
    title: "보호소 확인 및 승인",
    content: ["보호소에서 입양 신청서를 확인하고 승인 및 거부 결정을 합니다."]
  },
  check: {
    title: "승인 완료",
    content: ["보호소에서 승인 완료 및 거절 시 알림과 내정보에서 확인이 가능합니다."]
  }
};

const Main: React.FC = () => {
  const [activeIcon, setActiveIcon] = useState<IconType>("dog");
  const navigation = useNavigate();

  return (
    <div className="flex flex-col min-h-screen">
      {/* 헤더 */}
      <Header />
      <Chat />
      <FAQ />
      {/* About TenPaws */}
      <section className="relative ">
        <div>
          <div className="absolute pt-12 sm:pt-16 md:pt-20 lg:pt-24 pl-8 sm:pl-16 md:pl-24 lg:pl-32">
            <div className="font-semibold text-2xl sm:text-5xl md:text-6xl lg:text-7xl xl:text-8xl ">
              <div className="sm:pb-5 md:pb-8 lg:pb-8 xl:pb-15">기다림의 끝에서</div>
              <div className="pb-1 sm:pb-7 md:pb-9 lg:pb-12 xl:pb-15">서로를 만나는 순간</div>
            </div>
            <div className="text-sm sm:text-3xl md:text-4xl lg:text-5xl pb-5 sm:pb-7 md:pb-9 lg:pb-12 xl:pb-15">
              AI가 맺어주는 하나뿐인 인연
            </div>
            <div
              className="font-bold text-white bg-[#f1a34a] inline py-2 sm:py-3 px-8 sm:px-16 text-xl sm:text-2xl md:text-3xl rounded-full shadow-[0_0_15px_rgba(0,0,0,0.5)] cursor-pointer hover:bg-[#3c2a13] hover:duration-300"
              onClick={() => navigation("/matching")}>
              시작하기
            </div>
          </div>
          <img src={Main2} alt="main2" />
        </div>
      </section>
      {/* <section className="flex flex-col items-center justify-center bg-[#f1a34a] py-5 px-4">
        <h2 className="text-3xl sm:text-3xl md:text-4xl font-bold mb-3">About TenPaws</h2>
        <p className="text-xl sm:text-xl md:text-2xl lg:text-2xl max-w-full text-center leading-relaxed mb-1">
        TenPaws는 나이 드신 분들이 반려동물을 통해 새로운 인연을 맺을 수 있도록 돕는 서비스입니다. <br />
        </p>
        <p className="text-xl sm:text-xl md:text-2xl lg:text-2xl max-w-full text-center leading-relaxed mb-1">
        나의 정보에 선호하시는 동물의 종, 크기, 운동량 등을  입력하시면, AI가 당신에게 꼭 맞는 반려동물을 추천해드립니다.  
        </p>
        <p className="text-xl sm:text-xl md:text-2xl lg:text-2xl max-w-full text-center leading-relaxed mb-1">
        AI 매칭을 통해 생활 방식에 어울리는 반려동물을 쉽게 찾을 수 있습니다.
        </p>
        <p className="text-xl sm:text-xl md:text-2xl lg:text-2xl max-w-full text-center leading-relaxed">
        이외에도 TenPaws가 제공하는 산책 코스 추천, 시설 검색 등 다양한 서비스를 확인해보세요.
        </p>
      </section> */}

      {/* 설명 섹션 */}
      <section className="flex flex-col items-center justify-center bg-[#f1a34a] py-10 px-4">
        <h2 className="text-3xl sm:text-3xl md:text-4xl font-bold mb-10">About TenPaws</h2>

        <div className="flex flex-col md:flex-row justify-center items-center gap-8 max-w-6xl mx-auto">
          {/* AI 매칭 */}
          <div
            className="flex flex-col items-center p-6 rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)] bg-white w-full md:w-1/3 cursor-pointer
          hover:scale-105 transition-transform duration-300">
            <div className="bg-[#f1a34a] mb-4 p-2 pb-3 rounded-full">
              <img src={AImatching} alt="AI 매칭" className="w-10 h-10 " />
            </div>
            <h3 className="text-xl font-bold mb-2">AI 매칭</h3>
            <p className="text-center">
              AI가 당신의 생활패턴과 선호도를 분석하여 가장 잘 맞는 반려동물을 추천해드립니다
            </p>
          </div>

          {/* 신중한 입양 */}
          <div
            className="flex flex-col items-center p-6 rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)] bg-white w-full md:w-1/3 cursor-pointer
          hover:scale-105 transition-transform duration-300">
            <div className="bg-[#f1a34a] mb-4 p-2 pb-3 rounded-full">
              <img src={met} alt="신중한 입양" className="w-10 h-10 " />
            </div>
            <h3 className="text-xl font-bold mb-2">신중한 입양</h3>
            <p className="text-center ">
              보호소와 협력하여
              <br /> 책임감 있는 입양 절차를 도와드립니다
            </p>
          </div>

          {/* 편의 케어 */}
          <div
            className="flex flex-col items-center p-6 rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)] bg-white w-full md:w-1/3 cursor-pointer
          hover:scale-105 transition-transform duration-300">
            <div className="bg-[#f1a34a] mb-[21px] p-2 rounded-full">
              <img src={care} alt="편의 케어" className="w-10 h-10 " />
            </div>
            <h3 className="text-xl font-bold mb-2">편의 케어</h3>
            <p className="text-center ">
              주변 시설 정보 부터 산책로 까지
              <br /> 반려동물 케어에 필요한정보를 제공합니다
            </p>
          </div>
        </div>
      </section>

      {/* 설명 섹션 */}
      <section className="relative flex flex-col items-center justify-center h-[15vh] sm:h-[20vh] md:h-[20vh] lg:h-[20vh] xl:h-[25vh] 2xl:h-[25vh] bg-[#3c2a13]">
        {/* 내용 */}
        <div className="relative flex flex-col items-center justify-center w-[95] sm:w-[85%] md:w-[80%] lg:w-[85%] xl:w-[80%] 2xl:w-[90%] max-w-7xl mt-7">
          {/* 아이콘 및 화살표 */}
          <div className="w-full flex items-center justify-around mb-6 md:mb-10">
            {(["dog", "approve", "board", "check"] as IconType[]).map((icon, idx, arr) => (
              <React.Fragment key={icon}>
                <div
                  className={`flex flex-col items-center cursor-pointer hover:scale-110 transition-transform duration-300 ${
                    activeIcon === icon ? "opacity-100 scale-110" : "opacity-50"
                  }`}
                  onClick={() => setActiveIcon(icon)}>
                  <img
                    src={`/src/assets/${icon}.png`}
                    alt={icon}
                    className="w-10 h-10 sm:w-12 sm:h-12 md:w-14 md:h-14 lg:w-16 lg:h-16 xl:w-20 xl:h-20 object-contain mb-3"
                  />
                  <span className="text-lg sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl font-bold text-white">
                    {descriptions[icon].title}
                  </span>
                </div>
                {idx < arr.length - 1 && (
                  <img
                    src="/src/assets/arrow.png"
                    alt="Arrow"
                    className="w-7 h-7 sm:w-9 sm:h-9 md:w-10 md:h-10 lg:w-10 lg:h-10 xl:w-11 xl:h-11 2xl:w-12 2xl:h-12 object-contain"
                  />
                )}
              </React.Fragment>
            ))}
          </div>
        </div>
      </section>

      {/* 상세 설명 */}
      <section className="relative flex flex-col items-center justify-start h-[53vh] sm:h-[54vh] md:h-[60vh] lg:h-[70vh] xl:h-[75vh] 2xl:h-[75vh]">
        {/* 상단 배경 */}
        <div className="absolute top-0 left-0 w-full h-1/3 bg-[#3c2a13]"></div>
        {/* 하단 배경 */}
        <div className="absolute bottom-0 left-0 w-full h-2/3 bg-[#eeeceb] shadow-[0_0_15px_rgba(0,0,0,0.5)]"></div>
        {/* 내용 */}
        <div className="relative flex flex-col items-center justify-start w-[85%] sm:w-[85%] md:w-[80%] lg:w-[85%] xl:w-[80%] 2xl:w-[90%] max-w-7xl mt-0">
          <div className="w-[460px] sm:w-[630px] md:w-[730px] lg:w-[980px] xl:w-[1200px] 2xl:w-[1400px] bg-white rounded-md shadow-md p-6 sm:p-6 md:p-8">
            <div className="text-left mb-6">
              <h2 className="text-3xl sm:text-3xl md:text-3xl lg:text-4xl xl:text-4xl 2xl:text-5xl font-semibold text-gray-800 mb-4">
                {descriptions[activeIcon].title}
              </h2>
              {descriptions[activeIcon].content.map((line: string, index: number) => (
                <p
                  key={index}
                  className="text-xl sm:text-xl md:text-2xl lg:text-2xl xl:text-3xl 2xl:text-3xl leading-relaxed mb-1 pl-4">
                  {line}
                </p>
              ))}
            </div>
            <img
              src={`/src/assets/page${activeIcon === "dog" ? "" : activeIcon === "approve" ? "2" : activeIcon === "board" ? "3" : "4"}.png`}
              alt="Matching Animal Example"
              className="w-full sm:w-[87%] md:w-[85%] lg:w-[75%] xl:w-[70%] 2xl:w-[60%] h-[290px] sm:h-[330px] md:h-[350px] lg:h-[450px] xl:h-[500px] 2xl:h-[520px] mx-auto rounded-md object-contain"
            />
          </div>
        </div>
      </section>

      {/* 푸터 */}
      <Footer />
    </div>
  );
};

export default Main;

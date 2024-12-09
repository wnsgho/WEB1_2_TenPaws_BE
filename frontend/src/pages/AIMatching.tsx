import React, { useState, useEffect } from "react";
import Header from "../components/Header";
import Footer from "../components/Footer";
import axiosInstance from "../utils/axiosInstance";

const AIMatching = () => {
  const [userId, setUserId] = useState<number | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [petInfo, setPetInfo] = useState<{
    petId: number | null;
    recommendation: string | null;
    petName: string | null;
    petImage: string | null;
    details: { label: string; value: string }[];
  }>({
    petId: null,
    recommendation: "",
    petName: null,
    petImage: null,
    details: [
      { label: "종", value: "매칭 정보를 불러오는 중..." },
      { label: "크기", value: "매칭 정보를 불러오는 중..." },
      { label: "나이", value: "매칭 정보를 불러오는 중..." },
      { label: "성격", value: "매칭 정보를 불러오는 중..." },
      { label: "운동량", value: "매칭 정보를 불러오는 중..." },
    ],
  });

  // 유저 ID 가져오기
  useEffect(() => {
    const fetchUserId = async () => {
      const accessToken = localStorage.getItem("accessToken");

      if (!accessToken) {
        alert("로그인이 되어 있지 않아 매칭 정보를 불러올 수 없습니다.");
        setPetInfo({
          petId: null,
          recommendation: "정보 없음",
          petName: "정보 없음",
          petImage: null,
          details: [
            { label: "종", value: "정보 없음" },
            { label: "크기", value: "정보 없음" },
            { label: "나이", value: "정보 없음" },
            { label: "성격", value: "정보 없음" },
            { label: "운동량", value: "정보 없음" },
          ],
        });
        setIsLoading(false);
        return;
      }

      const authHeader = accessToken.startsWith("Bearer ")
        ? accessToken
        : `Bearer ${accessToken}`;

      try {
        const response = await axiosInstance.get("/api/v1/features/user-id", {
          headers: { Authorization: authHeader },
        });
        setUserId(response.data.Id || null);
      } catch {
        alert("로그인이 되어 있지 않아 매칭 정보를 불러올 수 없습니다.");
        setPetInfo({
          petId: null,
          recommendation: "정보 없음",
          petName: "정보 없음",
          petImage: null,
          details: [
            { label: "종", value: "정보 없음" },
            { label: "크기", value: "정보 없음" },
            { label: "나이", value: "정보 없음" },
            { label: "성격", value: "정보 없음" },
            { label: "운동량", value: "정보 없음" },
          ],
        });
        setIsLoading(false);
      }
    };

    fetchUserId();
  }, []);

  // 매칭 정보 가져오기
  useEffect(() => {
    if (!userId) {
      setIsLoading(false);
      return;
    }

    const fetchPetInfo = async () => {
      const accessToken = localStorage.getItem("accessToken");

      if (!accessToken) {
        alert("로그인이 되어 있지 않습니다.");
        setIsLoading(false);
        return;
      }

      const authHeader = accessToken.startsWith("Bearer ")
        ? accessToken
        : `Bearer ${accessToken}`;

      try {
        const response = await axiosInstance.post(
          `/api/v1/users/${userId}/recommend-pet`,
          {},
          { headers: { "Content-Type": "application/json", Authorization: authHeader } }
        );

        const data = response.data;
        console.log(data)

        if (!data.pet) {
          alert("추천할 반려동물이 존재하지 않습니다.");
          setPetInfo({
            petId: null,
            recommendation: "정보 없음",
            petName: "정보 없음",
            petImage: null,
            details: [
              { label: "종", value: "정보 없음" },
              { label: "크기", value: "정보 없음" },
              { label: "나이", value: "정보 없음" },
              { label: "성격", value: "정보 없음" },
              { label: "운동량", value: "정보 없음" },
            ],
          });
          setIsLoading(false);
          return;
        }

        const absoluteImageUrl = data.pet.imageUrls?.[0]
        ? `${axiosInstance.defaults.baseURL}${data.pet.imageUrls[0]}`
        : null;

        console.log("Fetched pet data:", data);
    console.log("Image URL:", absoluteImageUrl);

        setPetInfo({
          petId: data.pet.petId || null,
          recommendation: data.recommendation || "정보 없음",
          petName: data.pet.petName || "정보 없음",
          petImage: absoluteImageUrl,
          details: [
            { label: "종", value: data.pet.species || "정보 없음" },
            { label: "크기", value: data.pet.size || "정보 없음" },
            { label: "나이", value: data.pet.age ? `${data.pet.age}` : "정보 없음" },
            { label: "성격", value: data.pet.personality || "정보 없음" },
            { label: "운동량", value: data.pet.exerciseLevel || "정보 없음" },
          ],
        });
      } catch {
        alert("매칭 정보를 가져오는 데 실패했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchPetInfo();
  }, [userId]);


  return (
    <div className="min-h-screen flex flex-col bg-[#FDF8F5]">
      <Header />
      <main className="flex items-center justify-center flex-grow px-8 overflow-y-auto py-7">
        <div className="w-[90%] sm:w-[100%] md:w-[100%] lg:w-[100%] xl:w-[90%] 2xl:w-[70%] min-h-[80vh] bg-white rounded-md shadow-md p-8 space-y-8 flex flex-col items-center">
        <div className="relative flex flex-col w-full mt-4 sm:flex-row sm:items-center sm:justify-center">
            {/* 제목 */}
            <h1 className="text-4xl font-bold text-[#7F5546] text-center sm:text-left">
              반려동물 매칭 정보
            </h1>
            {/* 상세보기 버튼 */}
            {petInfo.petId && !isLoading && (
              <button
                className="mt-2 sm:mt-0 sm:absolute sm:right-4 sm:top-1/2 sm:-translate-y-1/2 text-[#7F5546] font-medium text-lg sm:text-xl lg:text-2xl hover:underline"
                style={{ marginBottom: "-20px" }} 
                onClick={() => (window.location.href = `/detail/${petInfo.petId}`)}
              >
                상세보기
              </button>
            )}
          </div>
          <div className="h-[30vh] sm:h-[35vh] md:h-[40vh] lg:h-[40vh] xl:h-[40vh] 2xl:h-[40vh] w-[30vh] sm:w-[45vh] md:w-[55vh] lg:w-[60vh] xl:w-[65vh] 2xl:w-[65vh] bg-gray-200 flex items-center justify-center rounded-md overflow-hidden min-h-[280px] min-w-[300px]">
            {petInfo.petImage ? (
              <img
                src={petInfo.petImage}
                alt="추천 반려동물"
                className="object-cover w-full h-full"
              />
            ) : (
              <span className="text-xl text-gray-500">이미지가 없습니다.</span>
            )}
          </div>
          <div className="relative flex items-center justify-center w-full">
            <span
              className={`block text-2xl sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-3xl font-bold ${
                petInfo.petName === "정보 없음" || petInfo.petName === "매칭 정보를 불러오는 중..."
                  ? "text-gray-500"
                  : "text-[#7F5546]"
              }`}
            >
              {petInfo.petName}
            </span>
            <button
              className="absolute right-0 text-[#7F5546] font-medium text-lg sm:text-xl lg:text-2xl hover:underline"
              onClick={() => window.location.reload()}
            >
              재시도
            </button>
          </div>
          <div className="w-full space-y-3">
            {petInfo.details.map((item, index) => (
              <div
                key={index}
                className="flex justify-between pt-1 pb-3 text-2xl border-b sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-3xl"
              >
                <span className="font-bold text-[#7F5546]">{item.label}</span>
                <span
                  className={`font-semibold ${
                    item.value === "정보 없음" || item.value === "매칭 정보를 불러오는 중..."
                      ? "text-gray-500"
                      : "text-black"
                  }`}
                >
                  {item.value}
                </span>
              </div>
            ))}
            <div
                className="flex flex-col justify-between pt-1 pb-3 text-2xl border-b sm:text-2xl md:text-2xl lg:text-3xl xl:text-3xl 2xl:text-3xl"
            >
              <span className="font-bold text-[#7F5546]">추천 이유</span> 
              <span
                  className={`font-semibold ${
                    petInfo.recommendation === "정보 없음" || petInfo.recommendation === "매칭 정보를 불러오는 중..."
                      ? "text-gray-500"
                      : "text-black"
                  }`}
                >
                  {petInfo.recommendation}
                </span>   
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default AIMatching;
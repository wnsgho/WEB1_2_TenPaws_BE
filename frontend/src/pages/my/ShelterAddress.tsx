import axiosInstance from "../../utils/axiosInstance"; 
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from 'react-router-dom';


declare global {
  interface Window {
    kakao: any;
  }
}

interface ShelterInfo {
  shelterId: number;
  shelterName: string;
  shelterAddress: string;
}

interface UseId {
  Id: number;
}

interface UseRole {
  role: string;
}

const ShelterAddress: React.FC = () => {
  const { petId } = useParams();
  const mapRef = useRef<HTMLDivElement>(null);
  const [token, setToken] = useState<string | null>(null);
  const [error, setError] = useState<{ status: number; message: string } | null>(null);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [useId, setUseId] = useState<UseId>({
    Id: 0
  });
  const [useRole, setUseRole] = useState<UseRole>({
    role: ""
  })
  const [shelterInfo, setShelterInfo] = useState<ShelterInfo>({
    shelterId: 0,
    shelterName: "",
    shelterAddress: ""
  });
  const [tempShelterInfo, setTempShelterInfo] = useState<ShelterInfo>(shelterInfo);

  useEffect(() => {
    const storedToken = localStorage.getItem("accessToken");
    if (storedToken) {
      setToken(storedToken);
    } else {
      console.error("로컬 스토리지에 토큰이 없습니다.");
    }
  }, []);


  const headers = {
    'Authorization': `${token}`,
  };


  // ID, ROLE 불러오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const userIdResponse = await axiosInstance.get(`/api/v1/features/user-id`, { headers });
        setUseId(userIdResponse.data);

        const roleResponse = await axiosInstance.get(`/api/v1/features/role`, { headers });
        setUseRole(roleResponse.data);
      } catch (error) {
        console.error("유저 데이터를 불러오는 중 오류 발생:", error);
        handleError(error);
      } finally {
        setIsLoading(false); // 로딩 상태 종료
      }
    };

    fetchUserData();
  }, [token]);

  // 보호소 정보 가져오기 (동물 상세 정보 이용)
  useEffect(() => {
    if(petId) {
      const shelterInfo = async () => {
        try {
          const response = await axiosInstance.get<ShelterInfo>(`/api/v1/pets/${petId}`);
          setShelterInfo(response.data);
        } catch (error) {
          console.error("보호소 정보를 불러오는 중 오류 발생:", error);
          // handleError(error);
        }
      };
      shelterInfo();
    }
  }, [petId]);

  // 카카오 지도 API 연동
  useEffect(() => {
    window.kakao.maps.load(() => {
      if (!mapRef.current) return;

      const mapInstance = new window.kakao.maps.Map(mapRef.current, {
        center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 기본 위치
        level: 5,
      });

      const geocoder = new window.kakao.maps.services.Geocoder();

      geocoder.addressSearch(shelterInfo.shelterAddress, (result:any, status:any) => {
        if (status === window.kakao.maps.services.Status.OK) {
          const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x);

          // 지도 중심 이동
          mapInstance.setCenter(coords);

          // 마커 생성
          const marker = new window.kakao.maps.Marker({
            position: coords,
            map: mapInstance,
          });

          // 정보 오버레이 생성
          const overlayContent = `
            <div style="
              padding: 10px;
              text-align: center;
              background: white;
              border-radius: 4px;
              font-weight: bold;
              min-width: 120px;
              box-shadow: 0 2px 6px rgba(0,0,0,0.2);
              font-size: 18px;
            ">
              ${shelterInfo.shelterName}
              <div style="font-size: 12px; color: #888;">${shelterInfo.shelterAddress}</div>
            </div>
          `;
          const overlay = new window.kakao.maps.CustomOverlay({
            position: coords,
            content: overlayContent,
            xAnchor: 0.5,
            yAnchor: 1.5,
          });

          // 마커 클릭 시 오버레이 표시
          window.kakao.maps.event.addListener(marker, "click", () => {
            overlay.setMap(mapInstance);
          });
        } else {
          console.warn("주소 검색 결과가 없습니다.");
        }
      });
    });
  }, [shelterInfo.shelterAddress]);


  // 모달 열기/닫기
  const openModal = () => {
    setTempShelterInfo(shelterInfo);
    setIsModalOpen(true);
  };
  const closeModal = () => setIsModalOpen(false);
  const saveChanges = () => {
    setShelterInfo(tempShelterInfo);
    setIsModalOpen(false);
    editSubmit();
  };

  // 정보 수정 제출
  const editSubmit = async (): Promise<void> => {
    if (!shelterInfo) return;

    try {
      await axiosInstance.put(`/api/v1/shelters/${useId.Id}`, shelterInfo, {headers});
      alert('정보가 수정되었습니다.');
      setIsModalOpen(false)
    } catch (error) {
      console.error('정보 수정 중 오류 발생:', error);
      alert('정보 수정에 실패했습니다.');
    }
  };

  // 확인 버튼 클릭시 뒤로가기
  const Cancel = () => {
    navigate(-1); // 이전 페이지로 이동
  };

  // 에러 핸들링 함수
  const handleError = (error: any) => {
    const status = error.response?.status || 500;
    const message = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
    navigate("/errorpage", { state: { status, message } }); // state로 에러 정보 전달
  };
  
  if (error) return null; // 이미 에러 페이지로 이동한 경우 렌더링 방지
  


  const shelter = useRole.role == "ROLE_SHELTER" && useId.Id == shelterInfo.shelterId


  return (
    <div>
      <div className="flex flex-col items-center mt-20">
        <section>
          <h2 className="text-2xl font-bold">보호소 주소</h2>
        </section>
        <section className="flex flex-col items-center w-full max-w-lg m-8">
          <div className="flex flex-wrap justify-center gap-8 p-5 bg-bgColor rounded-2xl">
            <div className="flex justify-between w-full p-3 bg-mainColor rounded-xl">
              <p className="text-xl font-bold">보호기관 이름</p>
              <p className="text-lg">{shelterInfo.shelterName}</p>
            </div>
            <div className="flex justify-between w-full p-3 bg-mainColor rounded-xl">
              <p className="text-xl font-bold">주소</p>
              <p className="text-lg">{shelterInfo.shelterAddress}</p>
            </div>
          </div>
        </section>
        {shelter ? <section className="flex gap-24 my-8">
          <button className="px-4 py-2 text-lg font-bold text-mainColor hover:text-orange-600" onClick={Cancel}>확인</button>
          <button
            className="px-4 py-2 text-lg text-cancelColor"
            onClick={openModal}
          >
            수정
          </button>
        </section> 
        :
        <section className="flex gap-24 my-8">
          <button className="px-4 py-2 text-lg font-bold text-mainColor hover:text-orange-600" onClick={Cancel}>확인</button>
        </section> 
        }

      </div>
      <div className="flex justify-center mb-20">
        <div ref={mapRef} className="w-[600px] h-[300px] rounded-lg border border-black"></div>  
      </div>


      {/* 모달 */}
      {isModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
          <div className="p-6 bg-white rounded-lg w-96">
            <h3 className="mb-4 text-xl font-bold">정보 수정</h3>
            <div className="mb-4">
              <label className="block mb-2 font-bold">보호기관 이름</label>
              <input
                type="text"
                value={tempShelterInfo.shelterName}
                onChange={(e) =>
                  setTempShelterInfo((prev) => ({ ...prev, shelterName: e.target.value }))
                }
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div className="mb-4">
              <label className="block mb-2 font-bold">주소</label>
              <input
                type="text"
                value={tempShelterInfo.shelterAddress}
                onChange={(e) =>
                  setTempShelterInfo((prev) => ({ ...prev, shelterAddress: e.target.value }))
                }
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div className="flex justify-end gap-4">
              <button
                onClick={closeModal}
                className="px-4 py-2 text-white bg-gray-500 rounded"
              >
                취소
              </button>
              <button
                onClick={saveChanges}
                className="px-4 py-2 text-white bg-blue-500 rounded"
              >
                저장
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ShelterAddress;



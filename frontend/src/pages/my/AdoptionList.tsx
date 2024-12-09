import axiosInstance from "../../utils/axiosInstance"; 
import React, { useEffect, useState } from 'react';
import { GoChevronUp, GoChevronDown } from 'react-icons/go';
import Header from "../../components/Header";
import MyPageModal from '../../components/MyPageModal';
import { useParams, useNavigate } from 'react-router-dom';
import axios from "axios";

// 데이터 타입 정의
interface Pet {
  petId: number;
  species: string;
  size: string;
  age: string;
  personality: string;
  exerciseLevel: number;
}

interface ApplyPet {
  id: number;
  pet: Pet;
  userId: number;
  applyDate: string;
  applyStatus: string;
}

interface ProcessedPet {
  id: number;
  petId: number;
  species: string;
  age: string;
  size: string;
  personality: string;
  exerciseLevel: number;
  userId: number;
  applyDate: string;
  applyStatus: string;
}

interface UseId {
  Id: number;
}

const AdoptionList: React.FC = () => {
  const { shelterId } = useParams();
  // 여러 펫의 정보를 관리하는 상태
  const [pets, setPets] = useState<ProcessedPet[]>([]);
  // 상태 정의: 각 펫의 상세 정보 표시 여부를 관리
  const [visibleDetails, setVisibleDetails] = useState<Record<number, boolean>>({});
  const [token, setToken] = useState<string | null>(null);
  const [error, setError] = useState<{ status: number; message: string } | null>(null);
  const navigate = useNavigate();
  const [useId, setUseId] = useState<UseId>({
    Id: 0
  })
  // 입양 승인 모달
  const [isApprovalModalOpen, setApprovalModalOpen] = useState<boolean>(false);
  
  // 입양 거절 모달
  const [isRefusalModalOpen, setRefusalModalOpen] = useState<boolean>(false);

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

  // ID 불러오기
  useEffect(() => {
    const shelterId = async () => {
      try {
        const response = await axiosInstance.get(`/api/v1/features/user-id`, {headers});
        setUseId(response.data);
      } catch(error) {
        console.error("유저 ID를 불러오는 중 오류 발생:", error);
        // handleError(error);
      }
    };
    shelterId();
  }, [token])


  // 입양 신청 정보 가져오기
  useEffect(() => {
    if(useId.Id !== 0) {
      const fetchApplyPets = async () => {
        try {
          const response = await axiosInstance.get<ApplyPet[]>(`/api/v1/applypet/shelter/${useId.Id}`, {headers});
          const fetchedPets: ProcessedPet[] = response.data.map((applyPet) => ({
            id: applyPet.id,
            petId: applyPet.pet.petId,
            species: applyPet.pet.species,
            size: applyPet.pet.size,
            age: applyPet.pet.age,
            personality: applyPet.pet.personality,
            exerciseLevel: applyPet.pet.exerciseLevel,
            userId: applyPet.userId,
            applyDate: applyPet.applyDate,
            applyStatus: applyPet.applyStatus
          }));
          setPets(fetchedPets);
        } catch (error) {
          console.error('데이터를 가져오는 중 오류 발생:', error);
          // handleError(error);
        }
      };
      fetchApplyPets();
    }
  }, [useId.Id]);

  // 토글 함수: 특정 펫 ID의 상태를 토글
  const toggleDetails = (id: number): void => {
    setVisibleDetails((prev) => ({
      ...prev,
      [id]: !prev[id], // 해당 ID의 상태를 반전
    }));
  };

  // 입양 거부
  const rejected = async (applyId: number) => {
    try {
      await axiosInstance.put(`/api/v1/applypet/${useId.Id}/status`, null, {
        headers: {
          'Authorization': token,
          'Content-Type': 'application/json'
        },
        params: {
          applyId: applyId,
          status: "REJECTED"
        }
      });
      alert('입양 신청이 거절되었습니다.');
      setPets(prevPets => prevPets.filter(pet => pet.id !== applyId));
    } catch (error) {
      console.error('입양 거절 처리 중 오류 발생:', error);
      alert('입양 거절 처리에 실패했습니다.');
    }
  };

  // 입양 승인
  const completed = async (applyId: number) => {
    try {
      await axiosInstance.put(`/api/v1/applypet/${useId.Id}/status`, null, {
        headers: {
          'Authorization': token,
          'Content-Type': 'application/json'
        },
        params: {
          applyId: applyId,
          status: "COMPLETED"
        }
      });
      alert('입양 신청이 승인되었습니다.');
      setPets(prevPets => prevPets.filter(pet => pet.id !== applyId));
    } catch (error) {
      console.error('입양 승인 처리 중 오류 발생:', error);
      alert('입양 승인 처리에 실패했습니다.');
    }
  };


  // 에러 핸들링 함수
  const handleError = (error: any) => {
    const status = error.response?.status || 500;
    const message = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
    navigate("/errorpage", { state: { status, message } }); // state로 에러 정보 전달
  };
      
  if (error) return null; // 이미 에러 페이지로 이동한 경우 렌더링 방지

  return (
    <div>
      {/* 헤더 */}
      <Header />
      <div className="flex flex-col items-center">
        <section className="flex flex-col items-center w-full max-w-lg gap-4 mt-8">
          <div className="flex justify-center">
            <h2 className="text-3xl font-bold">입양신청 리스트</h2>
          </div>
        </section>

        {/* 리스트 섹션 */}
        <section className="mt-20">
          <div>
            <ul className="flex flex-col gap-10">
              {/* "PENDING" 상태의 펫만 렌더링 */}
              {pets
                .filter((pet) => pet.applyStatus === "PENDING")
                .map((pet) => (
                  <li key={pet.id} className="flex flex-col justify-between pb-4 mb-4 border-b">
                    <div className="flex items-center justify-between">
                      <span>신청 ID: {pet.petId}</span>
                      <button
                        onClick={() => toggleDetails(pet.id)}
                        className="ml-32 text-blue-500 underline"
                      >
                        {visibleDetails[pet.id] ? <GoChevronUp /> : <GoChevronDown />}
                      </button>
                    </div>

                    {visibleDetails[pet.id] && (
                      <div className="p-2 mt-2 bg-gray-100 rounded">
                        <h3 className="text-lg font-semibold">펫 정보</h3>
                        <p>펫 ID: {pet.petId}</p>
                        <p>종류: {pet.species}</p>
                        <p>크기: {pet.size}</p>
                        <p>나이: {pet.age}</p>
                        <p>성격: {pet.personality}</p>
                        <p>활동량: {pet.exerciseLevel}</p>
                        <p>신청자 ID: {pet.userId}</p>
                        {/* 승인/거절 버튼 */}
                        <div className="flex gap-2 mt-2">
                          <button
                            className="px-4 py-2 text-white bg-green-500 rounded"
                            onClick={() => setApprovalModalOpen(true)}
                          >
                            입양 승인
                          </button>
                          <button
                            className="px-4 py-2 text-white bg-red-500 rounded"
                            onClick={() => setRefusalModalOpen(true)}
                          >
                            입양 거절
                          </button>
                        {/* 입양 승인 모달 (승인 및 거절 시 알람 기능 미구현)*/}
                        <MyPageModal isOpen={isApprovalModalOpen} onClose={() => setApprovalModalOpen(false)}>
                          <h3 className="mb-4 text-lg font-bold">정말로 승인하시겠습니까?</h3>
                          <div className="flex justify-end gap-4 mt-6">
                            <button 
                              className="text-mainColor" 
                              onClick={() => {
                              completed(pet.id);
                              setApprovalModalOpen(false);
                              }} 
                            > 
                              네
                            </button>
                            <button className="text-cancelColor" onClick={() => setApprovalModalOpen(false)}>
                              아니오
                            </button>
                          </div>
                        </MyPageModal>
                        {/* 입양 거절 모달 */}
                        <MyPageModal isOpen={isRefusalModalOpen} onClose={() => setRefusalModalOpen(false)}>
                          <h3 className="mb-4 text-lg font-bold">정말로 거절하시겠습니까?</h3>
                          <div className="flex justify-end gap-4 mt-6">
                          <button 
                            className="text-mainColor" 
                            onClick={() => {
                              rejected(pet.id);
                              setRefusalModalOpen(false);
                            }}
                          >
                              네
                            </button>
                            <button className="text-cancelColor" onClick={() => setRefusalModalOpen(false)}>
                              아니오
                            </button>
                          </div>
                        </MyPageModal>
                      </div>
                    </div>
                  )}
                </li>
              ))}
            </ul>
          </div>
        </section>
      </div>
    </div>

  );
};

export default AdoptionList;

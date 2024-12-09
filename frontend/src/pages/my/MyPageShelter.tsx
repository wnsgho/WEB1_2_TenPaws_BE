import React, { useEffect, useState } from 'react';
import { GoChevronRight } from "react-icons/go";
import { Link, useNavigate } from 'react-router-dom';
import MyPageModal from '../../components/MyPageModal';
import Header from '../../components/Header';
import axiosInstance from "../../utils/axiosInstance"; 

import mainImage from '../../assets/image/mainimage.webp';


interface ShelterInfo {
  email: string;
  shelterName: string;
  phoneNumber: string;
  address: string;
  password: string;
}

interface Pet {
  petId: string;
  species: string;
  size: string;
  age: string;
  personality: string;
  exerciseLevel: number;
  imageUrls: string[];
}

interface UseId {
  Id: number;
}

const MyPageShelter: React.FC = () => {
  const [isEditModalOpen, setEditModalOpen] = useState(false);
  const [isDeleteModalOpen, setDeleteModalOpen] = useState(false);
  const [token, setToken] = useState<string | null>(null);
  const [error, setError] = useState<{ status: number; message: string } | null>(null);
  const navigate = useNavigate();
  const [passwordError, setPasswordError] = useState<string | null>(null); // 비밀번호 오류 메시지 상태
  const [useId, setUseId] = useState<UseId>({
    Id: 0
  })
  const [shelterInfo, setShelterInfo] = useState<ShelterInfo>({
    shelterName: '',
    address: '',
    email: '',
    phoneNumber: '',
    password: ""
  });
  const [petLists, setPetLists] = useState<Pet[]>([]);


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
        console.error("보호소 ID를 불러오는 중 오류 발생:", error);
        // handleError(error);
      }
    };
    shelterId();
  }, [token])

  // 보호소 정보 가져오기 / 보호소 등록 동물 리스트
  useEffect(() => {
    if(useId.Id !== 0) {
      const shelterInfo = async () => {
        try {
          const response = await axiosInstance.get<ShelterInfo>(`/api/v1/shelters/${useId.Id}`, {headers});
          setShelterInfo(response.data);
        } catch(error) {
          console.error('보호소 정보를 불러오는 중 오류 발생:', error);
          // handleError(error);
        }
      };     
      shelterInfo();

    }
  }, [useId.Id]);

  useEffect(() => {
    if(useId.Id !== 0) {
      const petList = async () => {
        try {
          const response = await axiosInstance.get(`/api/v1/pets/${useId.Id}/list`, {headers});
          console.log('서버 응답:', response.data); // 응답 데이터 로깅
          setPetLists(response.data);
        } catch(error) {
          console.error('동물리스트 정보를 불러오는 중 오류 발생:', error);
          // handleError(error);
        }
      };      
      petList();
    }
  }, [useId.Id]);



  // 비밀번호 유효성 검증 함수
  const validatePassword = (password: string): string | null => {
    if (password.length < 8 || password.length > 12) {
      return '비밀번호는 8자 이상 12자 이하로 설정해야 합니다.';
    }
    if (!/[A-Z]/.test(password)) {
      return '비밀번호에 최소 1개의 대문자가 포함되어야 합니다.';
    }
    if (!/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
      return '비밀번호에 최소 1개의 특수문자가 포함되어야 합니다.';
    }
    return null;
  };



  // 입력값 변경 처리
  const editChange = (e: React.ChangeEvent<HTMLInputElement>): void => {
    const { name, value } = e.target;
  
    if (shelterInfo) { // 먼저 userInfo가 null이 아닌지 확인
      setShelterInfo((prev) =>
        prev ? { ...prev, [name]: value } : prev
      );
  
      if (name === 'password') {
        const error = validatePassword(value);
        setPasswordError(error);
      }
    }
  };


  // 정보 수정 제출

  const editSubmit = async (): Promise<void> => {
    if (!shelterInfo) return;

    // 비밀번호 검증
    if (passwordError) { // passwordError 상태로 검증
      alert(passwordError);
      return;
    }

    try {
      await axiosInstance.put(`/api/v1/shelters/${useId.Id}`, shelterInfo , {headers});
      alert('정보가 수정되었습니다.');
      setEditModalOpen(false);
    } catch (error) {
      console.error('정보 수정 중 오류 발생:', error);
      alert('정보 수정에 실패했습니다.');
    }
  };

  // 회원 탈퇴 처리
  const deleteAccount = async (): Promise<void> => {
    try {
      await axiosInstance.delete(`/api/v1/shelters/${useId.Id}`, {headers});
      alert('회원탈퇴가 완료되었습니다.');
      setDeleteModalOpen(false);
      // 필요시 리다이렉트 로직 추가
    } catch (error) {
      console.error('회원탈퇴 중 오류 발생:', error);
      alert('회원탈퇴에 실패했습니다.');
    }
  };


  // 입양신청 리스트 페이지로 이동하는 링크 생성 함수
  const applyListLink = (shelterId:number) => {
    return `/adoption-list/${shelterId}`; // 입양신청 리스트 페이지 URL 생성
  };
      
  if (!shelterInfo) {
    return <div>로딩 중...</div>;
  }

  const detailLink = (petId:number) => {
    return `/detail/${petId}`; // 상세 페이지 URL 생성
  };

  // 에러 핸들링 함수
  const handleError = (error: any) => {
    const status = error.response?.status || 500;
    const message = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
    navigate("/errorpage", { state: { status, message } }); // state로 에러 정보 전달
  };
    
  if (error) return null; // 이미 에러 페이지로 이동한 경우 렌더링 방지
  

  return (
    <>
      <div>
        {/* 헤더 */}
        <Header />
        <div className="flex flex-col items-center">
          <section className="flex flex-col items-center w-full max-w-lg gap-4 m-8">
            <div className="flex justify-center">
              <h3 className='text-2xl font-bold'>마이페이지</h3>
            </div>
            <div className="flex flex-wrap justify-center gap-10 p-5 bg-bgColor rounded-2xl">
              <div className="flex justify-between w-full p-3 rounded-lg bg-mainColor">
                <p className="text-xl font-bold">단체이름</p>
                <p className='text-lg'>{shelterInfo.shelterName}</p>
              </div>
              <div className="flex justify-between w-full p-3 rounded-lg bg-mainColor">
                <p className="text-xl font-bold">주소</p>
                <button className='flex items-center justify-center text-lg'>{shelterInfo.address}</button>
              </div>
              <div className="flex justify-between w-full p-3 rounded-lg bg-mainColor">
                <p className="text-xl font-bold">단체 메일</p>
                <p className='text-lg'>{shelterInfo.email}</p>
              </div>
              <div className="flex justify-between w-full p-3 rounded-lg bg-mainColor">
                <p className="text-xl font-bold">전화번호</p>
                <p className='text-lg'>{shelterInfo.phoneNumber}</p>
              </div>
            </div>
            <div className="flex gap-32 mt-6">
              <button
                className="text-lg text-mainColor hover:text-orange-500"
                onClick={() => setEditModalOpen(true)}
              >
                정보수정
              </button>
              <button
                className="text-lg text-cancelColor hover:text-red-700"
                onClick={() => setDeleteModalOpen(true)}
              >
                회원탈퇴
              </button>
            </div>
            <div className='mt-5'>
              <Link to={applyListLink(useId.Id)}>
                <button className="flex items-center justify-center text-lg font-bold hover:text-mainColor">입양 신청 리스트 <GoChevronRight />
                </button>
              </Link>
            </div>
          </section>
          <section className="flex flex-col items-center justify-center w-full max-w-lg gap-4 mt-8">
            <div>
              <h3 className="mb-10 text-xl font-bold">등록 입양 정보</h3>
            </div>
          </section>
          <section className='flex items-center justify-center m-6'>
            <div className='flex flex-wrap justify-center gap-10'>
              {Array.isArray(petLists) && petLists.length > 0 ? (
                petLists.map((pet) => (
                  <Link to={detailLink(pet.petId)}>
                    <div key={pet.petId} className='overflow-hidden border border-solid rounded-lg min-w-40 max-w-48 min-h-72 max-h-72'>
                      <img
                        src={pet.imageUrls && pet.imageUrls.length > 0 ? `http://15.164.103.160:8080${pet.imageUrls[0]}` : mainImage} 
                        alt="동물 사진"
                        onError={(e) => {
                          e.currentTarget.src = mainImage;
                          e.currentTarget.onerror = null;
                        }}
                      />
                      <div className='m-3'>
                        <p>{pet.species} / {pet.size} / {pet.age} / <br />
                          {pet.personality} / 활동량({pet.exerciseLevel})
                        </p>
                      </div>
                    </div>
                  </Link>
                ))
              ) : (
                <p>등록된 동물이 없습니다.</p>
              )}
            </div>
          </section>
        </div>

        {/* 정보수정 모달 */}
        <MyPageModal isOpen={isEditModalOpen} onClose={() => setEditModalOpen(false)}>
          <h3 className="mb-4 text-lg font-bold">정보 수정</h3>
          <div className="flex flex-col gap-4">
            <label>
              단체이름:
              <input
                type="text"
                name="shelterName"
                value={shelterInfo.shelterName}
                onChange={editChange}
                className="block w-full p-2 border rounded"
              />
            </label>
            <label>
              주소:
              <input
                type="text"
                name="address"
                value={shelterInfo.address}
                onChange={editChange}
                className="block w-full p-2 border rounded"
              />
            </label>
            <label>
              전화번호:
              <input
                type="text"
                name="phoneNumber"
                value={shelterInfo.phoneNumber}
                onChange={editChange}
                className="block w-full p-2 border rounded"
              />
            </label>
            <label>
              비밀번호:
              <input
                type="password"
                name="password"
                value={shelterInfo.password}
                onChange={editChange}
                className="block w-full p-2 border rounded"
              />
              {passwordError && (
                <p className="text-sm text-red-500">{passwordError}</p>
              )}
            </label>
          </div>
          <div className="flex justify-end gap-4 mt-6">
            <button className="text-mainColor" onClick={editSubmit}>
              수정완료
            </button>
            <button className="text-cancelColor" onClick={() => setEditModalOpen(false)}>
              취소
            </button>
          </div>
        </MyPageModal>
        {/* 회원탈퇴 모달 */}
        <MyPageModal isOpen={isDeleteModalOpen} onClose={() => setDeleteModalOpen(false)}>
          <h3 className="mb-4 text-lg font-bold">정말로 탈퇴하시겠습니까?</h3>
          <div className="flex justify-end gap-4 mt-6">
            <button className="text-mainColor" onClick={deleteAccount}>
              네
            </button>
            <button className="text-cancelColor" onClick={() => setDeleteModalOpen(false)}>
              아니오
            </button>
          </div>
        </MyPageModal>
      </div>
    </>
  );
};

export default MyPageShelter;
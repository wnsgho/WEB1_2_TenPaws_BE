import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from 'react-router-dom';
import { GoChevronLeft, GoChevronRight } from "react-icons/go";
import Header from "../../components/Header";
import axiosInstance from "../../utils/axiosInstance"; 
import MyPageModal from "../../components/MyPageModal";
import axios from "axios";

interface PetInfo {
  petId: number;
  petName: string;
  species: string;
  size: string;
  age: number;
  gender: string;
  neutering: string;
  reason: string;
  preAdoption: string;
  vaccinated: string;
  extra: string;
  personality: string;
  exerciseLevel: number;
  imageUrls: string[];
  shelterId: number;
  shelterName: string;
  address: string;
}

interface PetApplyInfo {
  id: number;
  pet: {
    petId: number;
    species: string;
    size: string;
    age: string;
    personality: string;
    exerciseLevel: number;
    imageUrls: string[];
  };
  userId: number;
  applyDate: string;
  applyStatus: string;
}

interface UseId {
  Id: number;
}


const DetailReadPage = () => {
  const { petId } = useParams();
  const [currentIndex, setCurrentIndex] = useState(0);
  const navigate = useNavigate(); 
  const [roles, setRoles] = useState({role:""});
  const [isLoading, setIsLoading] = useState(true); 
  const [isDeleteModalOpen, setDeleteModalOpen] = useState<boolean>(false);
  const [token, setToken] = useState<string | null>(null);
  const [isApplyModalOpen, setApplyModalOpen] = useState<boolean>(false);

  const [petApplyInfo, setPetApplyInfo] = useState<PetApplyInfo>({
    id: 0,
    pet: {
      petId: 0,
      species: "",
      size: "",
      age: "",
      personality: "",
      exerciseLevel: 0,
      imageUrls: [],
    },
    userId: 0,
    applyDate: "",
    applyStatus: ""
  });

  const [petInfo, setPetInfo] = useState({
    petId: 0,
    petName: "",
    species: "",
    size: "",
    age: 0,
    gender: "",
    neutering: "",
    reason: "",
    preAdoption: "",
    vaccinated: "",
    extra: "",
    personality: "",
    exerciseLevel: 0,
    imageUrls: [""],
    shelterId: 0,
    shelterName: "",
    address: "",
  })

  const [applyInfo, setApplyInfo] = useState({
    petId: "",
  })

  const [useId, setUseId] = useState<UseId>({
    Id: 0
  })

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



  // 동물 상세정보 불러오기
  useEffect(() => {
    const pets = async () => {
      try{
        const response = await axiosInstance.get<PetInfo>(`/api/v1/pets/${petId}`);
        setPetInfo(response.data);
      }catch(error) {
        console.error("동물 상세정보 불러오는 중 오류 발생", error)
      }
    }
    pets();
  }, [token])

  // ID, ROLE 불러오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const userIdResponse = await axiosInstance.get(`/api/v1/features/user-id`, { headers });
        setUseId(userIdResponse.data);

        const roleResponse = await axiosInstance.get(`/api/v1/features/role`, { headers });
        setRoles(roleResponse.data);
      } catch (error) {
        console.error("유저 데이터를 불러오는 중 오류 발생:", error);
      } finally {
        setIsLoading(false); // 로딩 상태 종료
      }
    };
  
    fetchUserData();
  }, [token]);
  

  // petId 추가하기
  useEffect(() => {
    if (petId) {
      setApplyInfo(prevState => ({
        ...prevState,
        petId: petId
      }));
    }
  }, [petId]);


  // 회원 입양 신청 조회
  useEffect(() => {
    if(useId.Id !== 0 && roles.role == "ROLE_USER"){
      const petApplyInfo = async () => {
        try {
          const response = await axiosInstance.get(`/api/v1/applypet/${useId.Id}/list`, {headers});
          setPetApplyInfo(response.data[0]);
          console.log(response.data[0])
        }catch(error: any) {
          console.error('동물 입양 정보를 불러오는 중 오류 발생:', error);
        }
      };
      petApplyInfo();
    }
  }, [roles]);


  // 입양 신청 
  const applypet = async () => {
    try {
      await axios.post(`http://15.164.103.160:8080/api/v1/applypet`, null, {
        headers: {
          'Authorization': token,
          'Content-Type': 'application/json'
        },
        params: {
          petId: petId,
          userId: useId.Id
        }
      });
      alert('입양 신청이 완료되었습니다.');
      setApplyModalOpen(false);
      window.location.reload(); // 페이지 새로 고침 추가
    } catch (error) {
      console.error("입양 신청 보내는 중 오류 발생", error);
      alert('오류가 발생했습니다 현재 입양신청 중인지 확인해주세요');
      setApplyModalOpen(false);
    }
  };
  

  // 보호소 동물 삭제
  const deletePet = async () => {
    try {
      await axiosInstance.delete(`/api/v1/pets/${useId.Id}/${petId}`, {headers});
      alert('삭제가 완료되었습니다.');
      setDeleteModalOpen(false);
      navigate("/matching");
    } catch (error) {
      console.error("동물 삭제 중 오류 발생", error);
      alert('삭제가 실패되었습니다 다시 시도해주세요.');
    }
  };


  const handlePrev = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? petInfo.imageUrls.length - 1 : prevIndex - 1
    );
  };

  const handleNext = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === petInfo.imageUrls.length - 1 ? 0 : prevIndex + 1
    );
  };

  // 취소 버튼 클릭시 뒤로가기
  const Cancel = () => {
    navigate(-1); // 이전 페이지로 이동
  };

  // 상세정보 수정 페이지로 이동하는 링크 생성 함수
  const petLink = (petId:any) => {
    return `/detail-correct/${petId}`; // 입양신청 리스트 페이지 URL 생성
  };
  
  const mapLink = (petId:any) => {
    return `/shelter-address/${petId}`; // 지도 페이지 URL 생성
  };

  const shelter = roles.role == "ROLE_SHELTER" && useId.Id == petInfo.shelterId


  return (
    <>
      <Header />
      <div className="flex flex-col items-center mt-10">
        <section className="relative w-full max-w-lg overflow-hidden">
          <div className="flex items-center">
            <button
              className="absolute left-0 z-10 p-2 text-white bg-gray-800 rounded-full hover:bg-gray-600"
              onClick={handlePrev}
            >
              <GoChevronLeft size={24} />
            </button>
            <div className="flex items-center justify-center w-full h-64">
              <img
                src={`http://15.164.103.160:8080${petInfo.imageUrls[currentIndex]}`}
                alt={`Slide ${currentIndex + 1}`}
                className="object-contain w-full h-full"
              />
            </div>
            <button
              className="absolute right-0 z-10 p-2 text-white bg-gray-800 rounded-full hover:bg-gray-600"
              onClick={handleNext}
            >
              <GoChevronRight size={24} />
            </button>
          </div>
          <div className="flex justify-center gap-2 mt-4">
            {petInfo.imageUrls.map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentIndex(index)}
                className={`w-3 h-3 rounded-full ${
                  currentIndex === index ? "bg-blue-500" : "bg-gray-400"
                }`}
              ></button>
            ))}
          </div>
        </section>

        <section className="flex flex-col w-full max-w-lg gap-3 mt-8">
          <div className="flex justify-center">
            <h3 className="text-2xl font-bold text-mainColor">{petInfo.petName}</h3>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">종류</p>
              <p className="text-lg text-white">{petInfo.species}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">나이</p>
              <p className="text-lg text-white">{petInfo.age}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">성별</p>
              <p className="text-lg text-white">{petInfo.gender}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">접종 유무</p>
              <p className="text-lg text-white">{petInfo.vaccinated}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">중성화 유무</p>
              <p className="text-lg text-white">{petInfo.neutering}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
                <p className="text-xl font-bold text-mainColor">성격</p>
                <p className="text-lg text-white">{petInfo.personality}</p>
              </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">활동량</p>
              <p className="text-lg text-white">{petInfo.exerciseLevel}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">보호소로 오게 된 이유</p>
              <p className="text-lg text-white">{petInfo.reason}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">맡겨지기 전 가정환경</p>
              <p className="text-lg text-white">{petInfo.preAdoption}</p>
            </div>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-3 bg-bgColor rounded-xl">
            <div className="flex justify-between w-full">
              <p className="text-xl font-bold text-mainColor">보호 기관</p>
                <Link to={mapLink(petId)}>
                  <p className="flex items-center text-lg text-white">{petInfo.shelterName}<GoChevronRight /></p>
                </Link>
            </div>
          </div>
          <div className="flex items-center justify-between p-3 bg-bgColor rounded-xl">
            <p className="text-xl font-bold text-mainColor">추가정보</p>
            <p className="text-lg text-white">{petInfo.extra}</p>
          </div>
        </section>
        {shelter ?  <section className="flex gap-24 my-8">
            <Link to={petLink(petId)}>
              <button className="px-4 py-2 text-lg text-cancelColor">수정</button>
            </Link>
            <button className="px-4 py-2 text-lg font-bold text-mainColor" onClick={Cancel}>완료</button>
            <button className="px-4 py-2 text-lg text-cancelColor"  onClick={() => setDeleteModalOpen(true)}>삭제</button>
          </section>
        : 
          <section className="flex gap-32 my-8">
            {petApplyInfo.applyStatus === "PENDING" ? (
              <button
                className="px-4 py-2 text-lg font-bold text-gray-500 cursor-not-allowed"
                disabled
              >
                입양 신청 완료
              </button>
            ) : (
              <button
                className="px-4 py-2 text-lg font-bold text-mainColor hover:text-bgColor"
                onClick={() => setApplyModalOpen(true)}
              >
                입양 신청
              </button>
            )}
            <button className="px-4 py-2 text-lg font-bold text-cancelColor hover:text-blue-700" onClick={Cancel}>
              취소
            </button>
          </section>
        }
        {/* 입양 신청 모달 */}
        <MyPageModal isOpen={isApplyModalOpen} onClose={() => setApplyModalOpen(false)}>
          <h3 className="mb-4 text-lg font-bold">입양 신청 하시겠습니까?</h3>
          <div className="flex justify-end gap-4 mt-6">
            <button className="text-mainColor" onClick={applypet}>
              네
            </button>
            <button className="text-cancelColor" onClick={() => setApplyModalOpen(false)}>
              아니오
            </button>
          </div>
        </MyPageModal>
        {/* 동물 삭제 모달 */}
        <MyPageModal isOpen={isDeleteModalOpen} onClose={() => setDeleteModalOpen(false)}>
          <h3 className="mb-4 text-lg font-bold">정말로 삭제하시겠습니까?</h3>
          <div className="flex justify-end gap-4 mt-6">
            <button className="text-mainColor" onClick={deletePet}>
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

export default DetailReadPage;
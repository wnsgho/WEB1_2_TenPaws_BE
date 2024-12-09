import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Header from '../../components/Header';
import { GoChevronRight } from "react-icons/go";
import { RxDividerVertical } from "react-icons/rx";
import axiosInstance from "../../utils/axiosInstance";

interface ProcessedPet {
  petId: number;
  species: string;
  age: string;
  personality: string;
  exerciseLevel: number;
  size: string;
  status: string;
  imageUrls: string[];
}

interface UseRole {
  role: string;
}

const MatchingPage = () => {
  const [pets, setPets] = useState<ProcessedPet[]>([]); // 동물 데이터 저장 상태
  const [error, setError] = useState<{ status: number; message: string } | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [token, setToken] = useState<string | null>(null);
  const navigate = useNavigate();
  const [useRole, setUseRole] = useState({role: ""});
  const [filters, setFilters] = useState({
    species: "",
    age: "",
    size: ""
  });

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

  useEffect(() => {
    const fetchPetList = async () => {
      setLoading(true); // 로딩 상태 시작
      try {
        const response = await axiosInstance.get('/api/v1/pets'); // API 호출
        setPets(response.data);
      } catch (error) {
        console.error("동물 리스트를 불러오는 중 오류 발생:", error);
        // handleError(error);
      } finally {
        setLoading(false); // 로딩 상태 종료
      }
    };
    fetchPetList();
  }, [])

  useEffect(() => {
    const userRole = async () => {
      try {
        const response = await axiosInstance.get(`/api/v1/features/role`, {headers}); // 현재 로그인 유저 role 확인 API 호출
        setUseRole(response.data)
      } catch (error) {
        console.error("유저 role 불러오는 중 오류 발생:", error);
        // handleError(error);
      }
    };
    userRole(); // 데이터 가져오기 함수 실행
  }, [token]); 

  const shelter = useRole.role == "ROLE_SHELTER"
  // const shelter = true // 임시 테스트용

  // 필터 변경 시 호출되는 핸들러
  const filterChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { id, value } = e.target;
    setFilters((prevFilters) => ({
      ...prevFilters,
      [id]: value
    }));
  };

  // 필터링된 동물 리스트 반환
  const filteredPets = Array.isArray(pets) ? pets.filter((pet) => {
    return (
      pet.status === "AVAILABLE" &&
      (!filters.species || pet.species === filters.species) &&
      (!filters.age || pet.age === filters.age) &&
      (!filters.size || pet.size === filters.size)
    );
  }) : [];
  
  // 상세 페이지로 이동하는 링크 생성 함수
  const detailLink = (petId:number) => {
    return `/detail/${petId}`; // 상세 페이지 URL 생성
  };

  // 에러 핸들링 함수
  const handleError = (error: any) => {
    const status = error.response?.status || 500;
    const message = error.response?.data?.message || "알 수 없는 오류가 발생했습니다.";
    navigate("/errorpage", { state: { status, message } }); // state로 에러 정보 전달
  };

  if (loading) {
    return <div>로딩 중...</div>; // 로딩 상태 표시
  }
      
  if (error) return null; // 이미 에러 페이지로 이동한 경우 렌더링 방지

  
  return (
    <>
      <Header />
      <div className='flex flex-col items-center max-w-screen'>
        <section className='flex flex-wrap items-center justify-center w-8/12 gap-10 p-10 mt-10 border bg-mainColor rounded-2xl'>
          <p className='p-3 text-3xl font-bold'>선택 옵션</p>
          <form className="flex flex-wrap max-[1041px]:justify-center max-[1041px]:gap-5 max-[790px]:gap-3 max-[726px]:justify-center mx-10 ">
            <select id="species" className="text-3xl px-7 rounded-xl" onChange={filterChange}>
              <option value="">종류</option>
              <option value="강아지">강아지</option>
              <option value="고양이">고양이</option>
            </select>
            <div>
              <RxDividerVertical className='w-10 h-10 max-[850px]:hidden' />
            </div>
            <select id="age" className="text-3xl px-7 rounded-xl" onChange={filterChange}>
              <option value="">연령</option>
              <option value="0~3살">0~3살</option>
              <option value="4~6살">4~6살</option>
              <option value="7~8살">7~10살</option>
            </select>
            <div>
              <RxDividerVertical className='w-10 h-10 max-[1041px]:hidden' />
            </div>
            <select id="size" className="text-3xl px-7 rounded-xl" onChange={filterChange}>
              <option value="">크기</option>
              <option value="소형">소형</option>
              <option value="중형">중형</option>
              <option value="대형">대형</option>
            </select>
          </form>     
        </section>
        <section className='mt-6'>
          <div>
            <Link to="/detailadd">
              {shelter ? <button className='flex items-center justify-center text-2xl text-mainColor hover:text-orange-600'>등록 <GoChevronRight /></button> : null}
            </Link>
          </div>
        </section>
        <section className='mt-16'>
          <div className='flex flex-col items-center justify-center'>
            <h3 className='mb-5 text-4xl font-bold'>매칭이 어려우신가요?</h3>
            <Link to="/ai-matching">
              <button className='flex items-center justify-center text-lg text-mainColor hover:text-orange-600'>AI매칭 바로가기<GoChevronRight /></button>
            </Link>
          </div>
        </section>
        <section className='flex items-center justify-center m-20'>
          <div className='flex flex-wrap justify-center gap-10'>
            {filteredPets.map((pet) => (
              <Link to={detailLink(pet.petId)}>
                <div key={pet.petId} className='border border-solid rounded-lg min-w-48 max-w-48 min-h-80 max-h-80'>
                  <img src={`http://15.164.103.160:8080${pet.imageUrls[0]}`} alt="동물 사진" className='w-full h-40 rounded-t-md'/>
                  <div className='m-3'>
                    <div className='flex justify-center'>
                      <p className='mt-2 text-xl font-bold'>{pet.species}</p>
                    </div>
                    <div className='flex justify-between px-5'>
                      <p className='text-neutral-500'>연령</p><p className='text-black'>{pet.age}</p>
                    </div>
                    <div className='flex justify-between px-5'>
                      <p className='text-neutral-500'>크기</p><p className='text-black'>{pet.size}</p>
                    </div>
                    <div className='flex justify-between px-5'>
                      <p className='text-neutral-500'>성격</p><p className='text-black'>{pet.personality}</p>
                    </div>
                    <div className='flex justify-between px-5'>
                      <p className='text-neutral-500'>활동량</p><p className='text-black'>{pet.exerciseLevel}</p>
                    </div>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        </section>
      </div>
    </>
  );
};

export default MatchingPage;
import React, { useEffect, useState } from 'react';
import Header from '../../components/Header';
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from 'react-router-dom';

interface UserInfo {
  species: string;
  preferredSize: string;
  preferredPersonality: string;
  preferredExerciseLevel: number;
}

interface UseId {
  Id: number;
}

const PreferPage: React.FC = () => {
  const navigate = useNavigate();
  const [error, setError] = useState<{ status: number; message: string } | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [useId, setUseId] = useState<UseId>({
    Id: 0
  })

  const [userInfo, setUserInfo] = useState<UserInfo>({
    species: "",
    preferredSize: "",
    preferredPersonality: "",
    preferredExerciseLevel: 0
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

  // ID 불러오기
  useEffect(() => {
    const userId = async () => {
      try {
        const response = await axiosInstance.get(`/api/v1/features/user-id`, {headers});
        setUseId(response.data);
      } catch(error) {
        console.error("유저 ID를 불러오는 중 오류 발생:", error);
        // handleError(error);
      }
    };
    userId();
  }, [])

  // select 값 변경 핸들러
  const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { id, value } = e.target;
    setUserInfo(prevState => ({
      ...prevState,
      [id]: id === 'preferredExerciseLevel' ? Number(value) : value
    }));
  };
  
  // 폼 제출 핸들러
  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault(); // 페이지 새로고침 방지
    await editSubmit();
  };
  

  // 정보 수정 제출

  const editSubmit = async (): Promise<void> => {
    if (!userInfo) return;
  
  
    try {
      await axiosInstance.put(`/api/v1/users/${useId.Id}`, userInfo, {headers});
      alert('정보가 수정되었습니다.');
      cancel()
    } catch (error) {
      console.error('정보 수정 중 오류 발생:', error);
      alert('정보 수정에 실패했습니다.');
    }
  };
  
  // 취소 버튼 핸들러
  const cancel = () => {
    navigate(-1); // 이전 페이지로 이동
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
      <Header />
      <form className="flex flex-col items-center" onSubmit={handleSubmit}>
        <section className="flex flex-col w-full max-w-lg gap-20 mt-8">
          <div className="flex justify-center">
            <h3 className="text-2xl font-bold text-mainColor">선호동물</h3>
          </div>
          <div className="flex flex-wrap justify-center gap-8 p-5 bg-bgColor rounded-2xl">
            <div className="flex justify-between w-3/4 p-3 bg-mainColor rounded-xl">
                <p className="text-xl font-bold">종류</p>
                <select id="species" className="text-lg border rounded-lg bg-gray-50 border-mainColor" value={userInfo.species}
                onChange={handleSelectChange}>
                  <option value="">종류</option>
                  <option value="강아지">강아지</option>
                  <option value="고양이">고양이</option>
                </select>
            </div>
            <div className="flex justify-between w-3/4 p-3 bg-mainColor rounded-xl">
              <p className="text-xl font-bold">나이</p>
              <select id="preferredSize" className="text-lg border rounded-lg bg-gray-50 border-mainColor" value={userInfo.preferredSize}
                onChange={handleSelectChange}>
                <option value="">크기</option>
                <option value="소형">소형</option>
                <option value="중형">중형</option>
                <option value="대형">대형</option>
              </select>
            </div>
            <div className="flex justify-between w-3/4 p-3 bg-mainColor rounded-xl">
              <p className="text-xl font-bold">성격</p>
              <select id="preferredPersonality" className="text-lg border rounded-lg bg-gray-50 border-mainColor" value={userInfo.preferredPersonality}
                onChange={handleSelectChange}>
                <option value="">성격</option>
                <option value="얌전함">얌전함</option>
                <option value="활발함">활발함</option>
                <option value="사나움">사나움</option>
              </select>
            </div>
            <div className="flex justify-between w-3/4 p-3 bg-mainColor rounded-xl">
              <p className="text-xl font-bold">활동량</p>
              <select id="preferredExerciseLevel" className="text-lg border rounded-lg bg-gray-50 border-mainColor" value={userInfo.preferredExerciseLevel}
                onChange={handleSelectChange}>
                <option value="">적음 1 ~ 많음 5</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
          </div>
        </section>
        <section className="flex gap-24 mt-8">
          <button type="submit" className="px-4 py-2 text-lg text-mainColor" onClick={editSubmit}>등록</button>
          <button type="button" className="px-4 py-2 text-lg text-cancelColor" onClick={cancel}>취소</button>
        </section>
      </form>
    </>
  );
};

export default PreferPage;
import React, { useEffect, useState } from 'react';
import { GoX, GoChevronRight } from "react-icons/go";
import Header from '../../components/Header';
import MyPageModal from "../../components/MyPageModal";
import axios from 'axios';
import axiosInstance from "../../utils/axiosInstance"; 
import { useParams, useNavigate } from 'react-router-dom';

interface PetAdd {
  petName: string;
  species: string;
  size: string;
  age: string;
  gender: string;
  neutering: string;
  reason: string;
  preAdoption: string;
  vaccinated: string;
  extra?: string;
  personality: string;
  exerciseLevel: number;
  shelterId: number;
  shelterName: string;
  address: string;
  imageUrls: File[];
}

interface Shelters {
  shelterName: string;
  address: string;
}

interface UseId {
  Id: number;
}


const DetailCorrect = () => {
  const { petId } = useParams();
  const [useId, setUseId] = useState<UseId>({Id:0});
  const navigate = useNavigate(); // useNavigate 훅 사용
  const [postImg, setPostImg] = useState<File[]>([]); // 업로드된 파일 리스트
  const [previewImg, setPreviewImg] = useState<string[]>([]); // 미리보기 이미지 URL 리스트
  const [token, setToken] = useState<string | null>(null);
  const [isChangeModalOpen, setChangeModalOpen] = useState<boolean>(false);
  const [shelterInfo, setShelterInfo] = useState<Shelters>({
    shelterName: "",
    address: ""
  });

  const [addPet, setAddPet] = useState<PetAdd>({
    petName: "",
    species: "",
    size: "",
    age: "",
    gender: "",
    neutering: "",
    reason: "",
    preAdoption: "", 
    vaccinated: "",
    extra: "",
    personality: "",
    exerciseLevel: 0,
    shelterId: useId.Id,
    shelterName: shelterInfo.shelterName,
    address: shelterInfo.address,
    imageUrls: postImg
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



  const saveImgFile = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileArr = e.target.files;

    if (fileArr) {
      const fileList = Array.from(fileArr); // File 객체 배열로 변환

      // 기존 이미지와 새로운 이미지를 합쳐 저장
      setPostImg((prev) => [...prev, ...fileList]);

      // 새로운 이미지를 읽어와 미리보기 URL 추가
      const fileUrlPromises = fileList.map(
        (file) =>
          new Promise<string>((resolve) => {
            const fileReader = new FileReader();
            fileReader.onload = () => {
              if (fileReader.result) {
                resolve(fileReader.result as string);
              }
            };
            fileReader.readAsDataURL(file);
          })
      );

      // 모든 파일 URL 생성 후 상태 업데이트
      Promise.all(fileUrlPromises).then((urls) => {
        setPreviewImg((prev) => [...prev, ...urls]);
      });
    }
  };

  useEffect(() => {
    const userId = async () => {
      try{
        const response = await axiosInstance.get(`/api/v1/features/user-id`, {headers});
        setUseId(response.data);
      }catch(error) {
        console.error("유저 아이디를 불러오는 중 에러 발생", error);
      }
    }
    userId();
  }, [token])

  useEffect(() => {
    if(useId.Id !== 0) {
      const petInfo = async() => {
        try {
          const response = await axiosInstance.get(`/api/v1/pets/${petId}/${useId.Id}`, {headers});
          setAddPet(response.data)
          console.log(addPet)
        } catch(error) {
          console.error('동물 정보 불러오는 중 오류 발생:', error);
        }
      }
      petInfo();
    }
  }, [useId.Id])

  // select 값 변경 핸들러
  const InputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { id, value } = e.target;
    setAddPet(prevState => ({
      ...prevState,
      [id]: id === 'exerciseLevel' ? Number(value) : value
    }));
  };

  const Cancel = () => {
    navigate(-1); // 이전 페이지로 이동
  };


      // 폼 제출 처리
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const petData = new FormData();

    // JSON 데이터를 Blob으로 변환하여 추가
    const jsonBlob = new Blob([JSON.stringify({ ...addPet,
      shelterId: useId.Id, // ensure shelterId is up-to-date
      imageUrls: undefined,
    })], {
      type: "application/json",
    });
    petData.append("petData", jsonBlob);

    // 파일 추가
    postImg.forEach((file) => petData.append("images", file));

    if (!token) {
      alert("인증 정보가 없습니다. 다시 로그인해주세요.");
      return;
    }

    try {
      const response = await axios.put(
        `http://15.164.103.160:8080/api/v1/pets/${useId.Id}/${petId}`,
        petData,
        {
          headers: {
            Authorization: token,
            "Content-Type": "multipart/form-data",
          },
        }
      );
      alert("동물 수정이 완료되었습니다.");
      setChangeModalOpen(false);
      navigate(-1);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        console.error("Error registering pet:", error.response?.data);
        alert(`동물 수정에 실패했습니다. 다시 시도해주세요 오류: ${error.response?.data?.message || error.message}`);
      } else {
        console.error("Unknown error:", error);
        alert("동물 수정에 실패했습니다. 알 수 없는 오류가 발생했습니다.");
      }
    }


    // FormData 내용 디버깅 출력
    console.log("FormData Debugging:");
    petData.forEach((value, key) => {
      console.log(`${key}:`, value);
    });
  };


  


  return (
    <>
      <Header />
      <div className="flex flex-col items-center mt-10">
        <section className="flex flex-wrap gap-8">
          {/* 미리보기 이미지 렌더링 */}
          {previewImg.map((imgSrc, i) => (
            <div
              key={i}
              className="relative w-24 h-24 overflow-hidden border rounded"
            >
              <button
                type="button"
                onClick={() => {
                  // 이미지 제거 로직
                  setPreviewImg((prev) => prev.filter((_, index) => index !== i));
                  setPostImg((prev) => prev.filter((_, index) => index !== i));
                }}
                className="absolute p-1 bg-white rounded-full top-1 right-1"
              >
                <GoX />
              </button>
              <img
                src={imgSrc}
                alt={`업로드된 이미지 ${i + 1}`}
                className="object-cover w-full h-full"
              />
            </div>
          ))}

          {/* 업로드 가능한 경우 업로드 버튼 표시 */}
          {postImg.length < 5 && (
            <label
              htmlFor="fileUpload"
              className="flex items-center justify-center w-24 h-24 border border-dashed rounded cursor-pointer"
            >
              <span>+</span>
            </label>
          )}

          {/* 파일 업로드 입력 */}
          <input
            id="fileUpload"
            type="file"
            onChange={saveImgFile}
            accept="image/*"
            multiple
            className="hidden"
          />
        </section>
        <section className="mt-10">
          <div className="flex flex-col flex-wrap gap-5">
            <div className="flex items-center justify-between p-2 px-10 border gap-52">
              <label htmlFor="species" className="text-xl">종류</label>
              <select id="species" className="pl-2 text-xs font-bold"  value={addPet.species} onChange={InputChange}>
                <option value="">종류</option>
                <option value="강아지">강아지</option>
                <option value="고양이">고양이</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="petName" className="text-xl">이름</label>
              <input type="text" id="petName" placeholder="예) 코코, 흰둥이" className="w-32 pl-3" value={addPet.petName} onChange={InputChange}/>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="age" className="text-xl">연령</label>
              <select id="age" className="pl-2 text-xs font-bold" value={addPet.age} onChange={InputChange}>
                <option value="">연령</option>
                <option value="0~3살">0~3살</option>
                <option value="4~6살">4~6살</option>
                <option value="7~10살">7~10살</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="gender" className="text-xl">성별</label>
              <select id="gender" className="pl-2 text-xs font-bold" value={addPet.gender} onChange={InputChange}>
                <option value="">성별</option>
                <option value="수컷">수컷</option>
                <option value="암컷">암컷</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="reason" className="text-xl">보호소로 오게 된 이유</label>
              <input type="text" id="reason" placeholder="예) 유기, 보호자 병환" className="pl-2 w-36" value={addPet.reason} onChange={InputChange}/>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="vaccinated" className="text-xl">접종 유무</label>
              <select id="vaccinated" className="pl-2 text-xs font-bold" value={addPet.vaccinated} onChange={InputChange}>
                <option value="">접종유무</option>
                <option value="1">1차</option>
                <option value="2">2차</option>
                <option value="3">3차</option>
                <option value="4">4차</option>
                <option value="5">5차</option>
                <option value="6">6차</option>
                <option value="no">미접종</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="neutering" className="text-xl">중성화 유무</label>
              <select id="neutering" className="pl-2 text-xs font-bold" value={addPet.neutering} onChange={InputChange}>
                <option value="">중성화유무</option>
                <option value="완료">완료</option>
                <option value="미완료">미완료</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="personality" className="text-xl">성격</label>
              <select id="personality" className="pl-2 text-xs font-bold" value={addPet.personality} onChange={InputChange}>
                <option value="">성격</option>
                <option value="얌전함">얌전함</option>
                <option value="활발함">활발함</option>
                <option value="사나움">사나움</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="exerciseLevel" className="text-xl">활동량</label>
              <select id="exerciseLevel" className="pl-2 text-xs font-bold" value={addPet.exerciseLevel} onChange={InputChange}>
                <option value="">적음 1 ~ 많음 5</option>
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="size" className="text-xl">크기</label>
              <select id="size" className="pl-2 text-xs font-bold" value={addPet.size} onChange={InputChange}>
                <option value="">크기</option>
                <option value="소형">소형</option>
                <option value="중형">중형</option>
                <option value="대형">대형</option>
              </select>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="preAdoption" className="text-xl">맡겨지기 전 가정환경</label>
              <input type="text" id="preAdoption" placeholder="예) 임시보호, 사육장" className="pl-2 w-36" value={addPet.preAdoption} onChange={InputChange}/>
            </div>
            <div className="flex items-center justify-between p-2 px-10 border">
              <label htmlFor="extra" className="text-xl">추가 정보(선택사항)</label>
              <input type="text" id="extra" placeholder="동물 추가정보 작성" className="pl-2 w-36" value={addPet.extra} onChange={InputChange}/>
            </div>
          </div>
        </section>
        <section className="flex gap-24 my-8">
          <button className="px-4 py-2 text-lg text-cancelColor" onClick={Cancel}>취소</button>
          <button className="px-4 py-2 text-lg text-mainColor" onClick={() => setChangeModalOpen(true)}>수정</button>
        </section>
        {/* 동물 정보 수정 모달 */}
        <MyPageModal isOpen={isChangeModalOpen} onClose={() => setChangeModalOpen(false)}>
          <h3 className="mb-4 text-lg font-bold">수정 하시겠습니까?</h3>
          <div className="flex justify-end gap-4 mt-6">
            <button className="text-mainColor" onClick={handleSubmit}>
              네
            </button>
            <button className="text-cancelColor" onClick={() => setChangeModalOpen(false)}>
              아니오
            </button>
          </div>
        </MyPageModal>
      </div>
    </>
  );
};

export default DetailCorrect;
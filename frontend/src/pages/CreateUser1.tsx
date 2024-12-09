import React, { useState, ChangeEvent } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header";
import axiosInstance from "../utils/axiosInstance";

const CreateUser1 = () => {
  const [inputValues, setInputValues] = useState({
    email: "",
    password: "",
    passwordConfirm: "",
    username: "",
    birthDate: "",
    phoneNumber: "",
    address: "",
    species: "",
    preferredSize: "",
    preferredPersonality: "",
    preferredExerciseLevel: null as number | null,
  });

  const [emailCheckStatus, setEmailCheckStatus] = useState<string | null>(null);
  const [emailStatus, setEmailStatus] = useState("default"); 
  const [isEmailChecking, setIsEmailChecking] = useState(false);
  const [passwordError, setPasswordError] = useState<string | null>(null); 
  const [passwordConfirmError, setPasswordConfirmError] = useState<string | null>(null);  
  const [userError, setUserError] = useState<string | null>(null);
  const [birthError, setBirthError] = useState<string | null>(null);
  const [phoneNumberError, setPhoneNumberError] = useState<string | null>(null);
  const [addressError, setAddressError] = useState<string | null>(null);
  const [exerciseLevelError, setExerciseLevelError] = useState<string | null>(null);
  const navigate = useNavigate();

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { id, value } = e.target;

    if (id === "email") {
      setEmailCheckStatus(null);
      setEmailStatus("default");
    }

    if (id === "password") {
      setPasswordError(null);
      setPasswordConfirmError(null); 
    }
    if (id === "passwordConfirm") {
      setPasswordConfirmError(null); 
    }
    
    if (id === "username") {
      setUserError(null);
    }

    if (id === "birthDate") {
      setBirthError(null);
    }

    if (id === "phoneNumber") {
      setPhoneNumberError(null);
    }

    if (id === "address") {
      setAddressError(null);
    }
    
    if (id === "preferredExerciseLevel") {
      setExerciseLevelError(null);
    }

    setInputValues((prev) => ({
      ...prev,
      [id]: value,
    }));
  };

  const handleEmailCheck = async () => {
    const email = inputValues.email.trim();
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  
    if (!email) {
      setEmailStatus("error");
      setEmailCheckStatus("이메일을 입력해주세요.");
      alert("이메일을 입력해주세요.");
      return;
    } else if (!emailRegex.test(email)) {
      setEmailStatus("error");
      setEmailCheckStatus("유효한 이메일 형식이 아닙니다.");
      alert("유효한 이메일 형식이 아닙니다.");
      return;
    }
  
    // 백엔드 API로 이메일 중복 검사(GET 요청)
    setIsEmailChecking(true);
    try {
      const response = await axiosInstance.get("/api/v1/features/check-email", {
        params: { email },
      });
  
      if (response.data.isAvailable) {
        setEmailStatus("success");
        setEmailCheckStatus("사용 가능한 이메일입니다.");
        alert("사용 가능한 이메일입니다.");
      } else {
        setEmailStatus("error");
        setEmailCheckStatus("사용할 수 없는 이메일입니다.");
        alert("사용할 수 없는 이메일입니다.");
      }
    } catch (error) {
      setEmailStatus("error");
      setEmailCheckStatus("이메일 중복을 확인할 수 없습니다.");
      alert("이메일 중복을 확인할 수 없습니다.");
    } finally {
      setIsEmailChecking(false);
    }
  };

  const validateInputs = () => {
    const { email, password, passwordConfirm, username, birthDate, phoneNumber, address, preferredExerciseLevel } = inputValues;
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const phoneNumberRegex = /^\d{3}-\d{4}-\d{4}$/;

    if (!email) {
      setEmailCheckStatus("이메일을 입력해주세요.");
      setEmailStatus("error");
      alert("이메일을 입력해주세요.");
      return false;
    } else if (!emailRegex.test(email)) {
      setEmailCheckStatus("유효한 이메일 형식이 아닙니다.");
      setEmailStatus("error");
      alert("유효한 이메일 형식이 아닙니다.");
      return false;
    }
    if (emailStatus !== "success") {
      setEmailCheckStatus("이메일 중복을 검사해주세요.");
      setEmailStatus("error");
      alert("이메일 중복을 검사해주세요.");
      return false;
    }

    if (!password) {
      setPasswordError("비밀번호를 입력해주세요.");
      alert("비밀번호를 입력해주세요.");
      return false;
    } else if (password.length < 7 || password.length > 20) {
      setPasswordError("비밀번호는 7자 이상, 20자 이하로 입력해야 합니다.");
      alert("비밀번호는 7자 이상, 20자 이하로 입력해야 합니다.");
      return false;
    }
    
    if (!passwordConfirm) {
      setPasswordConfirmError("비밀번호 확인을 입력해주세요.");
      alert("비밀번호 확인을 입력해주세요.");
      return false;
    } else if (password !== passwordConfirm) {
      setPasswordConfirmError("비밀번호가 일치하지 않습니다.");
      alert("비밀번호가 일치하지 않습니다.");
      return false;
    }

    if (!username) {
      setUserError("사용자 이름을 입력해주세요.");
      alert("사용자 이름을 입력해주세요.");
      return false;
    }

    const birthDateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!birthDate) {
      setBirthError("생년월일을 입력해주세요.");
      alert("생년월일을 입력해주세요.");
      return false;
    } else if (!birthDateRegex.test(birthDate)) {
      setBirthError("유효한 생년월일 형식이 아닙니다.");
      alert("유효한 생년월일 형식이 아닙니다. (예: 2000-01-01)");
      return false;
    }

    if (!phoneNumber) {
      setPhoneNumberError("전화번호를 입력해주세요.");
      alert("전화번호를 입력해주세요.");
      return false;
    } else if (!phoneNumberRegex.test(phoneNumber)) {
      setPhoneNumberError("유효한 전화번호 형식이 아닙니다.(예: 010-0000-0000)");
      alert("유효한 전화번호 형식이 아닙니다.(예: 010-0000-0000)");
      return false;
    }

    if (!address) {
      setAddressError("주소를 입력해주세요.");
      alert("주소를 입력해주세요.");
      return false;
    }

    // const exerciseLevel = Number(preferredExerciseLevel);
    // if (exerciseLevel < 1 || exerciseLevel > 5 || isNaN(exerciseLevel)) {
    //   setExerciseLevelError("운동량은 1에서 5 사이의 숫자를 입력해주세요.");
    //   alert("운동량은 1에서 5 사이의 숫자를 입력해주세요.");
    //   return false;
    // }

    return true;
  };

  // 백엔드 API로 POST 요청 보내기
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (validateInputs()) {
      try {
        const response = await axiosInstance.post("/api/v1/users/regular/join", {
          email: inputValues.email,
          username: inputValues.username,
          password: inputValues.password,
          birthDate: inputValues.birthDate,
          phoneNumber: inputValues.phoneNumber,
          address: inputValues.address,
          species: inputValues.species || null,
          preferredSize: inputValues.preferredSize || null,
          preferredPersonality: inputValues.preferredPersonality || null,
          preferredExerciseLevel: inputValues.preferredExerciseLevel
            ? Number(inputValues.preferredExerciseLevel)
            : null,
          userRole: "ROLE_USER",
        });

        if (response.status === 201) {
          alert("회원가입이 완료되었습니다!");
          navigate("/login");
        }
      } catch (error) {
        console.error(error); 
        alert("회원가입을 할 수 없습니다.");
      }
    }
  };

 

  return (
    <div className="flex flex-col min-h-screen bg-[#CDC3BF]">
      <Header />
      <main className="flex-grow flex items-center justify-center px-4 sm:px-8 md:px-16 lg:px-24 py-8">
        <div className="w-full max-w-7xl bg-white shadow-lg rounded-lg p-10 sm:p-12 md:p-16 lg:p-20">
          <h1 className="text-4xl sm:text-5xl md:text-5xl font-bold text-gray-800 mb-10 text-center">
            개인 회원가입
          </h1>
          <form className="space-y-10" onSubmit={handleSubmit}>
            {/* 이메일 */}
            <div>
              <label
                htmlFor="email"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                이메일
              </label>
              <div className="flex items-center space-x-4">
                <input
                  id="email"
                  type="email"
                  placeholder="이메일을 입력해주세요."
                  value={inputValues.email}
                  onChange={handleInputChange}
                  className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                    emailStatus === "error"
                      ? "bg-red-100 border-red-500"
                      : emailStatus === "success"
                      ? "bg-gray-100 border-gray-500"
                      : inputValues.email
                      ? "bg-gray-100 border-gray-300"
                      : "bg-white border-gray-300"
                  } overflow-hidden truncate`}
                />
                <button
                  type="button"
                  onClick={handleEmailCheck}
                  disabled={isEmailChecking}
                  className="px-3 py-5 sm:px-4 sm:py-6 bg-gray-500 text-white rounded-lg sm:text-lg hover:bg-gray-600 transition text-center"
                  style={{ whiteSpace: "pre" }}
                >
                  중복 확인
                </button>
              </div>

              {/* 상태 메시지 표시 */}
              {emailCheckStatus && (
                <p
                  className={`mt-2 text-lg ${
                    emailStatus === "success" ? "text-blue-600" : "text-red-600"
                  }`}
                >
                  {emailCheckStatus}
                </p>
              )}
            </div>

            {/* 비밀번호 */}
            <div>
              <label
                htmlFor="password"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                비밀번호
              </label>
              <input
                id="password"
                type="text"
                placeholder="비밀번호를 입력해주세요."
                value={inputValues.password}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  passwordError
                    ? "border-red-500 bg-red-100"
                    : inputValues.password
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {passwordError && <p className="text-red-600 mt-2">{passwordError}</p>}
            </div>
            
            {/* 비밀번호 확인 */}
            <div>
              <label
                htmlFor="passwordConfirm"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                비밀번호 확인
              </label>
              <input
                id="passwordConfirm"
                type="password"
                placeholder="비밀번호를 다시 입력해주세요."
                value={inputValues.passwordConfirm}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl 
                ${
                  passwordConfirmError
                    ? "border-red-500 bg-red-100"
                    : inputValues.passwordConfirm
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {passwordConfirmError && (
                <p className="text-red-600 mt-2">{passwordConfirmError}</p>
              )}
            </div>
            
            {/* 사용자 이름 */}
            <div>
              <label
                htmlFor="username"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                사용자 이름
              </label>
              <input
                id="username"
                type="text"
                placeholder="사용자 이름을 입력해주세요."
                value={inputValues.username}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  userError
                    ? "border-red-500 bg-red-100"
                    : inputValues.username
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {userError && <p className="text-red-600 mt-2">{userError}</p>}
            </div>

            {/* 생년월일 */}
            <div>
              <label
                htmlFor="birthDate"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                생년월일
              </label>
              <input
                id="birthDate"
                type="text"
                placeholder="생년월일을 입력해주세요. (예: 1990-01-01)"
                value={inputValues.birthDate}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  birthError
                    ? "border-red-500 bg-red-100"
                    : inputValues.birthDate
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {birthError && <p className="text-red-600 mt-2">{birthError}</p>}
            </div>

            {/* 전화번호 */}
            <div>
              <label
                htmlFor="phoneNumber"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                전화번호
              </label>
              <input
                id="phoneNumber"
                type="tel"
                placeholder="전화번호를 입력해주세요. (예: 010-0000-0000)"
                value={inputValues.phoneNumber}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  phoneNumberError
                    ? "border-red-500 bg-red-100"
                    : inputValues.phoneNumber
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {phoneNumberError && <p className="text-red-600 mt-2">{phoneNumberError}</p>}
            </div>

            {/* 주소 */}
            <div>
              <label
                htmlFor="address"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                주소
              </label>
              <input
                id="address"
                type="text"
                placeholder="주소를 입력해주세요."
                value={inputValues.address}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  addressError
                    ? "border-red-500 bg-red-100"
                    : inputValues.address
                    ? "border-gray-300 bg-gray-100"
                    : "border-gray-300 bg-white"
                } overflow-hidden truncate`}
              />
              {addressError && <p className="text-red-600 mt-2">{addressError}</p>}
            </div>

            <div>
              <label
                htmlFor="species"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                <span className="text-black">선호 동물 종</span>
                <span className="text-gray-400 text-xl sm:text-xl md:text-2xl">(선택사항)</span>
              </label>
              <input
                id="species"
                type="text"
                placeholder="선호하는 동물을 종을 입력해주세요. (예: 강아지)"
                value={inputValues.species}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  inputValues.species
                    ? "border-gray-500 bg-gray-100"
                    : "border-gray-300 focus:ring-2 focus:ring-gray-400"
                } overflow-hidden truncate`}
              />
            </div>

            {/* 선호 동물 크기 */}
            <div>
              <label
                htmlFor="preferredSize"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                <span className="text-black">선호 동물 크기</span><span className="text-gray-400 text-xl sm:text-xl md:text-2xl" >(선택사항)</span>
              </label>
              <input
                id="preferredSize"
                type="text"
                placeholder="선호하는 동물 크기를 입력해주세요. (예: 중간)"
                value={inputValues.preferredSize}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  inputValues.preferredSize
                    ? "border-gray-500 bg-gray-100"
                    : "border-gray-300 focus:ring-2 focus:ring-gray-400"
                } overflow-hidden truncate`}
              />
            </div>

            {/* 선호 동물 성격 */}
            <div>
              <label
                htmlFor="preferredPersonality"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                <span className="text-black">선호 동물 성격</span><span className="text-gray-400 text-xl sm:text-xl md:text-2xl">(선택사항)</span>
              </label>
              <input
                id="preferredPersonality"
                type="text"
                placeholder="선호하는 동물 성격을 입력해주세요. (예: 온순함)"
                value={inputValues.preferredPersonality}
                onChange={handleInputChange}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  inputValues.preferredPersonality
                    ? "border-gray-500 bg-gray-100"
                    : "border-gray-300 focus:ring-2 focus:ring-gray-400"
                } overflow-hidden truncate`}
              />
            </div>

            {/* 선호 동물 운동량 */}
            <div>
              <label
                htmlFor="preferredExerciseLevel"
                className="block text-2xl sm:text-2xl md:text-3xl font-medium text-gray-700 mb-4"
              >
                <span className="text-black">선호 동물 운동량</span><span className="text-gray-400 text-xl sm:text-xl md:text-2xl">(선택사항)</span>
              </label>
              <input
                id="preferredExerciseLevel"
                type="number"
                placeholder="1부터 5까지 숫자를 입력해주세요."
                value={inputValues.preferredExerciseLevel || ""}
                onChange={handleInputChange}
                min={1}
                max={5}
                className={`w-full px-5 py-4 sm:py-5 md:py-6 border rounded-lg text-2xl sm:text-2xl md:text-2xl ${
                  exerciseLevelError
                  ? "border-red-500 bg-red-100"
                  : inputValues.preferredExerciseLevel
                  ? "border-gray-300 bg-gray-100"
                  : "border-gray-300 bg-white"
              } overflow-hidden truncate`}
              />
            {exerciseLevelError && <p className="text-red-600 mt-2">{exerciseLevelError}</p>}
            </div>

            {/* 회원가입 버튼 */}
            <div className="flex justify-center mt-10">
              <button
                type="submit"
                className="w-full max-w-md px-8 py-5 bg-[#3D3D3D] text-white text-3xl font-semibold rounded-lg hover:bg-black transition"
              >
                회원가입 하기
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
};

export default CreateUser1;
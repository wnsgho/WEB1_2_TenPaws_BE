import React, { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Alarm from "./pages/Alarm";
import AiMatcing from "./pages/AIMatching";
import Main from "./pages/Main";
import Facilities from "./pages/guide/Facilities";
import WalkingCourse from "./pages/guide/WalkingCourse";
import MyWalkingCourse from "./pages/MyWalkingCourse";
import MatchingPage from "./pages/matching/MatchingPage";
import DetailPage from "./pages/matching/DetailPage";
import DetailReadPage from "./pages/matching/DetailReadPage";
import PreferPage from "./pages/matching/PreferPage";
import DetailCorrect from "./pages/matching/DetailCorrect";
import MyPageUser from './pages/my/MyPageUser';
import MyPageShelter from "./pages/my/MyPageShelter";
import Announcement from "./pages/guide/Announcement";
import Announcementpost from "./pages/guide/Announcementpost";
import Announcementpostcreate from "./pages/guide/Announcementpostcreate";
import QAndA from "./pages/guide/QAndA";
import QAandApost from "./pages/guide/QAandApost";
import QAandApostcreate from "./pages/guide/QAandApostcreate";
import CreateUser1 from "./pages/CreateUser1";
import CreateUser2 from "./pages/CreateUser2";
import ShelterAddress from "./pages/my/ShelterAddress";
import AdoptionList from "./pages/my/AdoptionList";
import Error from "./pages/Error";
import Worldcup from "./pages/worldcup/Worldcup";
import AnnouncementEdit from "./pages/guide/AnnouncementEdit";
import QAandAEdit from "./pages/guide/QAndAEdit";
import AuthResponse from "./pages/AuthResponse";

function App() {

  
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} /> {/* 메인 페이지 */}
        <Route path="/login" element={<Login />} /> {/* 로그인 페이지 */}
        <Route path="/signup" element={<Signup />} /> {/* 회원가입 페이지 */}
        <Route path="/create-user1" element={<CreateUser1 />} /> {/* 유저정보생성(개인) 페이지 1 */}
        <Route path="/create-user2" element={<CreateUser2 />} /> {/* 유저정보생성(보호소) 페이지 2 */}
        <Route path="/ai-matching" element={<AiMatcing />} /> {/* Ai 매칭 페이지 */}
        <Route path="/alarm" element={<Alarm />} /> {/* 알람 페이지 */}
        <Route path="/guide" element={<Announcement />} /> {/* 안내 페이지*/}
        <Route path="/guide/announcement" element={<Announcement />} /> {/* 공지사항 페이지*/}
        <Route path="/guide/announcement/:id" element={<Announcementpost />} /> {/* 공지사항 포스트 페이지*/}
        <Route path="/guide/announcement/create" element={<Announcementpostcreate />} /> {/* 공지사항 작성 페이지*/}
        <Route path="/guide/announcement/Edit/:id" element={<AnnouncementEdit />} /> {/* 공지사항 수정 페이지*/}
        {/* 이상형 월드컵 페이지 */}
        <Route path="/worldcup" element={<Worldcup />} />
        <Route path="/guide/qna" element={<QAndA />} /> {/* 질의응답 페이지 */}
        <Route path="/guide/qna/:id" element={<QAandApost />} /> {/* 질의응답 포스트 페이지 */}
        <Route path="/guide/qna/create" element={<QAandApostcreate />} /> {/* 질의응답 작성 페이지 */}
        <Route path="/guide/qna/Edit/:id" element={<QAandAEdit />} /> {/* 질의응답 수정 페이지 */}
        <Route path="/guide/facilities" element={<Facilities />} /> {/* 반려동물 관련 시설 페이지 */}
        <Route path="/guide/walking-course" element={<WalkingCourse />} /> {/* 산책 코스 페이지 */}
        <Route path="/my-walking-course" element={<MyWalkingCourse />} /> {/* 나의 산책 코스 페이지 */}
        <Route path="/prefer" element={<PreferPage />} /> {/* 선호동물 입력 및 수정 페이지 */}
        <Route path="/matching" element={<MatchingPage />} /> {/* 반려동물 매칭 페이지 */}
        <Route path="/detailadd" element={<DetailPage />} /> {/* 반려동물 상세정보 작성 페이지 (보호소) */}
        <Route path="/detail/:petId" element={<DetailReadPage />} /> {/* 반려동물 상세정보 페이지 */}
        <Route path="/detail-correct/:petId" element={<DetailCorrect />} /> {/* 반려동물 상세정보 수정 페이지 (보호소) */}
        <Route path="/mypage-user" element={<MyPageUser />} /> {/* 마이페이지 (유저) */}
        <Route path="/mypage-shelter" element={<MyPageShelter />} /> {/* 마이페이지 (보호소) */}
        <Route path="/shelter-address/:petId" element={<ShelterAddress />} /> {/* 보호소 주소 등록 페이지 */}
        <Route path="/adoption-list/:shelterId" element={<AdoptionList />} /> {/* 입양리스트 페이지 */}
        <Route path="/errorpage" element={<Error/>} /> {/* 에러페이지 (임시로 이곳에 위치) */}
        <Route path="/auth/oauth-response/:token/:expiresIn" element={<AuthResponse />} /> {/* 소셜 로그인 */}
      </Routes>
    </Router>

  );
}

export default App;
import GuideNavigation from "../../components/GuideNavigation";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../../components/Header";
import useUserStore from "../../store/store";
import Chat from "../../components/Chat";
import FAQ from "../../components/FAQ";

interface AnnouncementPost {
  id: number;
  title: string;
  content: string;
  category: string;
  viewCount : number;
  created_at: string;
}

const Announcementpost = () => {
  const [announcementPost, setAnnouncementPost] = useState<AnnouncementPost | null>(null);
  const navigate = useNavigate();
  const { id } = useParams()
  const role = useUserStore((state)=> state.role)

  //조회
  useEffect(() => {
    let isMounted = true; 

    const fetchAnnouncementPost = async () => {
      try {
        const response = await axios.get(`http://15.164.103.160:8080/api/v1/announcements/${id}`);
        if (isMounted) {
          setAnnouncementPost(response.data);
        }
      } catch (error) {
        console.error("불러오기 실패", error);
        if (isMounted) {
          navigate("/guide/announcement");
        }
      }
    };

    if (id) {
      fetchAnnouncementPost();
    }

    return () => {
      isMounted = false;
    };
  }, [id, navigate]); 

  //삭제
  const handleDelete = async () => {
    if(!window.confirm("정말로 게시글을 삭제하시겠습니까?")) return;
    
    try{
      const token = localStorage.getItem("accessToken")
      await axios.delete(`http://15.164.103.160:8080/api/v1/announcements/${id}?userId=${userId}`,
        {
        headers: {
          Authorization : token,
          'Content-Type': 'application/json',
        }
      }

      )
      alert("삭제되었습니다.")
      navigate("/guide/announcement")
    }catch(error){
      console.error("삭제 실패", error)
      alert("삭제에 실패하였습니다.")
    }
  }

  return (
    <div>
    <Header/>
    <Chat/>
    <FAQ/>
    <div className="flex flex-col justify-center items-center">
        <div className="w-full">
          <div className="relative">
            <div className="bg-slate-400"></div>
            <div className="bg-[#3c2a13]/90 h-[300px]"></div>
            <div className="absolute inset-0 flex flex-col justify-center text-center font-bold">
              <div className="text-[50px] pb-2 text-white">공지사항</div>
              <div className="text-[25px] text-white">다양한 정보를 제공하고 있습니다.</div>
            </div>
          </div>
          <GuideNavigation/>
        {announcementPost && ( 
            <div className="max-w-[1000px] mx-auto" key={announcementPost.id}>
              <div className="text-[35px] text-center border-t-[1px] border-black pt-5 font-bold mx-3">{announcementPost.title}</div>
              <div className="text-center pt-4 pb-5 border-b-[1px] border-gray-300 mx-3">
              <span className={announcementPost.category === "NOTICE" ? ("text-red-500") : ("text-blue-500")}>{announcementPost.category === "NOTICE" ? ("공지") : ("지원")}</span> <span className="opacity-70 text-gray-400">|</span> 작성일 {announcementPost.created_at} <span className="opacity-70 text-gray-400">|</span> 조회수 {announcementPost.viewCount}
              </div>
              <div className="text-[20px] py-10 px-5 border-b-[1px] border-black mb-10 mx-3" dangerouslySetInnerHTML={{ __html: announcementPost.content }} />
              <div
                className="float-right mr-3 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                onClick={() => navigate("/guide/announcement")}>
                목록으로
              </div>

              {role === "ROLE_ADMIN" && localStorage.getItem("accessToken") &&(
                <>
                <div
                className="float-right mr-8 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                onClick={handleDelete}>
                삭제하기
              </div>

              <div
                className="float-right mr-8 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                onClick={() => navigate(`/guide/announcement/edit/${announcementPost.id}`)}>
                수정하기
              </div>
              </>
              )}
            </div>
          )}
      </div>
    </div>
    </div>
  );
};

export default Announcementpost;

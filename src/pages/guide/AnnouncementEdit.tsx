import GuideNavigation from "../../components/GuideNavigation";
import Walk from "../../../public/walk.png";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axios from "axios";
import Header from "../../components/Header";
import FAQ from "../../components/FAQ";
import Chat from "../../components/Chat";

const AnnouncementEdit = () => {
  const [content, setContent] = useState("");
  const [title, setTitle] = useState("");
  const [category, setCategory] = useState<string>("NOTICE");
  const navigate = useNavigate();
  const { id } = useParams();

  //기존 내용 불러오기
  useEffect(() => {
    const fetchPost = async () => {
      try {
        const response = await axios.get(`http://15.164.103.160:8080/api/v1/announcements/${id}`);
        setTitle(response.data.title);
        setContent(response.data.content);
        setCategory(response.data.category);
        console.log(response.data);
      } catch (error) {
        console.error("게시글을 불러올 수 없습니다.", error);
        navigate("/guide/announcement");
      }
    };
    if (id) {
      fetchPost();
    }
  }, [id]);

  //수정 요청
  const handleSubmit = async () => {
    const token = localStorage.getItem("accessToken")
    try {
      await axios.put(`http://15.164.103.160:8080/api/v1/announcements/${id}`, {
        category,
        title,
        content
      },
      {
        headers: {
          Authorization : token,
          'Content-Type': 'application/json',
        }
      });
      alert("수정 되었습니다.");
      navigate(`/guide/announcement/${id}`);
    } catch (error) {
        console.error("수정 실패:", error);
        alert("수정에 실패했습니다.");
    }
  };

  const modules = {
    toolbar: [
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ font: [] }],
      [{ size: ["small", false, "large", "huge"] }],
      ["bold", "italic", "underline", "strike"],
      [{ color: [] }, { background: [] }],
      [{ list: "ordered" }, { list: "bullet" }],
      [{ align: [] }],
      ["link", "image"],
      ["clean"]
    ]
  };

  const formats = [
    "header",
    "font",
    "size",
    "bold",
    "italic",
    "underline",
    "strike",
    "color",
    "background",
    "list",
    "bullet",
    "align",
    "link",
    "image"
  ];

  return (
    <div>
      <Header />
      <FAQ/>
      <Chat/>
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
          <div className="max-w-[1000px] mx-auto px-4">
            <div className="bg-[#3c2a13]/90 p-4 md:p-8 rounded-xl">
              <select className="mb-6 w-auto p-2 font-bold rounded-xl" onChange={(e) => setCategory(e.target.value)} value={category}>
                <option value="NOTICE">공지</option>
                <option value="SUPPORT">지원</option>
              </select>
              <div className="mb-6">
                <input
                  type="text"
                  className="w-full p-3 rounded-xl"
                  placeholder="제목을 입력하세요"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </div>
              <div className="h-[500px] md:h-[1000px] bg-white overflow-hidden">
                <ReactQuill
                  theme="snow"
                  value={content}
                  onChange={setContent}
                  modules={modules}
                  formats={formats}
                  className="h-[458px] md:h-[958px]"
                  placeholder="내용을 입력하세요"
                />
              </div>
            </div>
            <div className="mt-7 flex justify-end space-x-4 mb-20">
              <button
                className="bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] rounded-xl hover:scale-105 transition-transform"
                onClick={() => {
                  if (window.confirm("수정을 취소하시겠습니까?")) {
                    navigate("/guide/announcement");
                  }
                }}>
                취소
              </button>
              <button
                className="bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] rounded-xl hover:scale-105 transition-transform"
                onClick={handleSubmit}>
                수정하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AnnouncementEdit;

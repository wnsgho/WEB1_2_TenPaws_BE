import GuideNavigation from "../../components/GuideNavigation";
import Walk from "../../../public/walk.png";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import axios from "axios";
import Header from "../../components/Header";
import Chat from "../../components/Chat";
import FAQ from "../../components/FAQ";

const QAandApostcreate = () => {
  const [content, setContent] = useState("");
  const [title, setTitle] = useState("")
  const navigate = useNavigate()

    
    const handleSubmit = async() => {
      if (!title.trim()) {
        alert('제목을 입력해주세요.');
        return;
      }
  
      if (!content.trim()) {
        alert('내용을 입력해주세요.');
        return;
      }

      try{
        const token = localStorage.getItem("accessToken")
        const response = await axios.post("http://15.164.103.160:8080/api/v1/inquiries",{
          title,
          content,
        },
        {
          headers: {
            Authorization : token,
            'Content-Type': 'application/json',
          }
        }
      )
        if(response.status === 200){
          alert("작성이 완료되었습니다!")
          navigate("/guide/qna")
        }
      }catch(error){
        console.error("작성 실패", error)
        alert('포스트 작성에 실패했습니다.');
      }
    }

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
      <Header/>
      <Chat/>
      <FAQ/>
    <div className="flex flex-col justify-center items-center">
        <div className="w-full">
          <div className="relative">
            <div className="bg-slate-400"></div>
            <div className="bg-[#3c2a13]/90 h-[300px]"></div>
            <div className="absolute inset-0 flex flex-col justify-center text-center font-bold">
              <div className="text-[50px] pb-2 text-white">문의 게시판</div>
              <div className="text-[25px] text-white">무엇이든 물어보세요.</div>
            </div>
          </div>
          <GuideNavigation/>

        <div className="max-w-[1100px] mx-auto px-4">
          <div className="bg-[#3c2a13]/90 p-4 md:p-8 rounded-xl">
            <div className="mb-6">
              <input 
                type="text" 
                className="w-full p-3 rounded-xl" 
                value={title} 
                onChange={(e)=> setTitle(e.target.value)}
                placeholder="제목을 입력하세요"
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
              onClick={() => navigate("/guide/qna")}>
              취소
            </button>
            <button
              className="bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] rounded-xl hover:scale-105 transition-transform"
              onClick={handleSubmit}>
              작성하기
            </button>
          </div>
        </div>
      </div>
    </div>
    </div>
  );
};

export default QAandApostcreate;

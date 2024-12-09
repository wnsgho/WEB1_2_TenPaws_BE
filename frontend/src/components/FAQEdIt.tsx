import axios from "axios";
import { useEffect, useState } from "react";

interface FAQEdit {
  Close: () => void;
}

interface TopFAQ {
  faqId: number;
  content: string | number;
  refFaqId: null | number;
}

const FAQEdIt = ({ Close }: FAQEdit) => {
  const [content, setContent] = useState("");
  const [selectedTopId, setSelectedTopId] = useState<number | null>(null);
  const [selectedSubId, setSelectedSubId] = useState<number | null>(null);
  const [topContent, setTopContent] = useState<TopFAQ[]>([]);
  const [subContent, setSubContent] = useState<TopFAQ[]>([]);
  const [edit, setEdit] = useState<boolean>(false);


  //상위 질문 작성
  const TopHandleSubmit = async () => {
    try {
      await axios.post("http://15.164.103.160:8080/api/v1/faqs", {
        content
      },
    {
      headers:{
        Authorization : localStorage.getItem("accessToken")
      }
    });
      setContent("");
      alert("작성이 완료 되었습니다.");
    } catch (error) {
      console.error("질문 작성 실패", error);
      alert("질문 작성에 실패하였습니다.");
    }
  };

  //모든 질문 불러오기
  useEffect(() => {
    const fetchTopFAQ = async () => {
      try {
        const response = await axios.get("http://15.164.103.160:8080/api/v1/faqs");
        setTopContent(response.data);
      } catch (error) {
        console.error("최상위 질문 불러오기 실패", error);
      }
    };
    fetchTopFAQ();
  }, []);

  //하위 질문 작성
  const RefHandleSubmit = async () => {
    try {
      await axios.post("http://15.164.103.160:8080/api/v1/faqs", {
        content,
        parentId: selectedTopId
      },
      {
        headers:{
          Authorization : localStorage.getItem("accessToken")
        }
      });
      setContent("");
      alert("작성이 완료 되었습니다.");
    } catch (error) {
      console.error("질문 작성 실패", error);
      alert("질문 작성에 실패하였습니다.");
    }
  };

  //수정
  const handleTopEdit = async () => {
    try {
      await axios.put(`http://15.164.103.160:8080/api/v1/faqs/${selectedTopId}`, {
        content
      },
      {
        headers:{
          Authorization : localStorage.getItem("accessToken")
        }
      });
      alert("수정되었습니다.");
      const response = await axios.get("http://15.164.103.160:8080/api/v1/faqs");
      setTopContent(response.data);
    } catch (error) {
      console.error("수정 실패:", error);
      alert("수정에 실패했습니다.");
    }
  };

  //삭제
  const handleTopDelete = async () => {
    if (!window.confirm("해당 질문을 삭제하시겠습니까?")) return;

    try {
      await axios.delete(`http://15.164.103.160:8080/api/v1/faqs/${selectedTopId}`,
        {
          headers:{
            Authorization : localStorage.getItem("accessToken")
          }
        });
      alert("삭제되었습니다.");
      const response = await axios.get("http://15.164.103.160:8080/api/v1/faqs");
      setTopContent(response.data);
      setSelectedTopId(null);
      setContent("");
    } catch (error) {
      console.error("삭제 실패", error);
      alert("삭제에 실패하였습니다.");
    }
  };


  return (
    <div className="bg-white shadow-[0_0_15px_rgba(0,0,0,0.5)] w-96 h-[556px] fixed right-[490px] bottom-2 m-6 z-50 rounded-md">
      {/* 헤더 */}
      <div className=" bg-[#3c2a13] text-white font-bold text-xl p-3 flex  rounded-t-md content-center justify-between">
        <div>FAQ</div>
        <div className="flex gap-2">
          <div className="cursor-pointer float-end" onClick={() => setEdit(false)}>
            작성
          </div>
          <div className="cursor-pointer float-end" onClick={() => setEdit(true)}>
            수정
          </div>
          <div className="cursor-pointer" onClick={Close}>
          ✖️
          </div>
        </div>
      </div>
      {/* 수정버튼을 누를시 수정 & 삭제 창 나타남 (edit)*/}
      {edit ? (
        <div className="overflow-y-auto max-h-[500px] scrollbar-hide">
            <div className="font-bold px-4 my-4 w-full text-3xl">질문 수정 & 삭제</div>
          <div className="font-bold px-4 mt-3 w-full">질문을 선택하세요</div>
            
            {/* 최상위 질문 가져오기 */}
          <select
            className="m-4 p-2 border-2 border-black w-[350px]"
            onChange={(e) => {
              const value = Number(e.target.value);
              console.log(value)
              setSelectedTopId(value);
            }}
            value={selectedTopId || ""}>
              <option value="">선택하세요</option>
            {topContent.map((faq) => (
              <option value={faq.faqId} key={faq.faqId}>
                {faq.content}
              </option>
            ))}
          </select>

          <div className="">
            <div className="font-bold px-4 mt-1">질문을 수정하세요</div>
            <div className="flex justify-center">
              <input
                type="text"
                className="border-2 border-black w-[350px] h-[100px] m-3 p-2"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className="flex justify-end px-3">
              <div
                className="bg-[#3c2a13] text-white font-semibold w-12 text-center cursor-pointer px-2 py-1 rounded-md mr-3"
                onClick={handleTopEdit}>
                수정
              </div>
              <div
                className="bg-red-400 font-semibold w-12 text-center cursor-pointer px-2 py-1 rounded-md mr-1"
                onClick={handleTopDelete}>
                삭제
              </div>
            </div>
          </div>
          </div>
      ) : 
      // edit이 아니라면 작성 하는 창 나타남
      (
        <div className="overflow-y-auto max-h-[500px] scrollbar-hide">
          <div>
            <div className="font-bold px-4 mt-3 ">최상위 질문을 작성하세요</div>
            <div className="flex justify-center">
              <input
                type="text"
                className="border-2 border-black w-[350px] m-3 p-2"
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className="flex justify-end px-3">
              <div
                className="bg-[#3c2a13] text-white font-semibold w-12 text-center cursor-pointer px-2 py-1 rounded-md mr-1"
                onClick={TopHandleSubmit}>
                작성
              </div>
            </div>
          </div>

          <hr className="border-black mt-3 mx-3" />

          <div className="font-bold px-4 mt-3 w-full">참조할 상위 질문을 선택하세요</div>

          {/* 질문 가져오기 */}
          <select
            className="m-4 p-2 border-2 border-black w-[350px]"
             onChange={(e) => {
              const value = Number(e.target.value);
              console.log(value)
              setSelectedTopId(value);
            }}
            value={selectedTopId || ""}>
              <option value="">선택하세요</option>
            {topContent.map((faq) => ( 
              <option value={faq.faqId} key={faq.faqId}>
                {faq.content}
              </option>
            ))}
          </select>

          <div className="">
            <div className="font-bold px-4 mt-1">질문을 작성하세요</div>
            <div className="flex justify-center">
              <input
                type="text"
                className="border-2 border-black w-[350px] h-[100px] m-3 p-2"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className="flex justify-end px-3">
              <div
                className="bg-[#3c2a13] text-white font-semibold w-12 text-center cursor-pointer px-2 py-1 rounded-md mr-1"
                onClick={RefHandleSubmit}>
                작성
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default FAQEdIt;

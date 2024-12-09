import { useEffect, useState } from "react";
import logo from "../assets/logo.png";
import FAQEdIt from "./FAQEdIt";
import axios from "axios";
import useUserStore from "../store/store";

interface TopFAQ {
  faqId: number;
  content: string | number;
  refFaqId: null | number;
  parentId: null | number;
}

const FAQ = () => {
  const [open, setOpen] = useState<boolean>(false);
  const [selectFAQ, setSelectFAQ] = useState<string | null>(null);
  const [edit, setEdit] = useState<boolean>(false);
  const [topContent, setTopContent] = useState<TopFAQ[]>([]);
  const [allContent, setAllContent] = useState<TopFAQ[]>([]);
  const [parentId, setParentId] = useState<number | null>(null);
  const role = useUserStore((state) => state.role);
  //작성창 열기
  const handleEditClick = () => {
    setEdit(!edit);
  };

  //FAQ채팅 열기
  const handleClick = () => {
    setOpen(!open);
    setSelectFAQ(null);
  };

  //선택한 FAQ만 남기기 & 다시 누르면 전체목록 보이기
  const handleSelectFAQ = (FAQ: string, faqId: number) => {
    if (selectFAQ === FAQ) {
      setSelectFAQ(null);
      setParentId(null);
    } else {
      setSelectFAQ(FAQ);
      setParentId(faqId);
    }
  };

  //처음으로 돌아가기
  const handleBack = () => {
    setSelectFAQ(null);
    setParentId(null);
  };

  //최상위 질문 불러오기
  useEffect(() => {
    const fetchTopFAQ = async () => {
      try {
        const response = await axios.get("http://15.164.103.160:8080/api/v1/faqs/top-level");
        setTopContent(response.data);
      } catch (error) {
        console.error("최상위 질문 불러오기 실패", error);
      }
    };
    fetchTopFAQ();
  }, []);

  //모든 질문 불러오기
  useEffect(() => {
    const fetchAllFAQ = async () => {
      try {
        const response = await axios.get(`http://15.164.103.160:8080/api/v1/faqs`);
        setAllContent(response.data);
      } catch (error) {
        console.error("하위 질문 불러오기 실패", error);
      }
    };
    fetchAllFAQ();
  }, []);

  return (
    <div>
      <div
        className="bg-[#3c2a13] text-white m-6 p-6 rounded-full font-bold text-[40px] w-16 h-16 flex justify-center items-center pl-[24.6px] pb-[29px] cursor-pointer fixed bottom-0 right-2 hover:scale-105 transition-transform z-50 shadow-[0_0_15px_rgba(0,0,0,0.5)]"
        onClick={handleClick}>
        {open ? "x" : "?"}
      </div>
      {open && (
        <>
          <div className="bg-white shadow-[0_0_15px_rgba(0,0,0,0.5)] w-96 h-[556px] fixed right-[90px] bottom-2 m-6 z-50 rounded-md">
            <div className=" bg-[#3c2a13] text-white font-bold text-xl p-3 flex  rounded-t-md content-center justify-between">
              <div>FAQ</div>
              <div className="flex gap-2">
                {role === "ROLE_ADMIN" && (
                  <div className="cursor-pointer float-end" onClick={handleEditClick}>
                    +
                  </div>
                )}
                <div className="cursor-pointer" onClick={() => setOpen(false)}>
                  ✖️
                </div>
              </div>
            </div>
            <div className="overflow-y-auto max-h-[500px] scrollbar-hide">
              <div className="flex px-5 py-5">
                <div className="rounded-full w-12 h-12">
                  <img src={logo} alt="logo" />
                </div>
                <div className="ml-5 p-2 rounded-xl content-center bg-gray-200">어떤 도움이 필요하신가요?</div>
              </div>
              {!selectFAQ ? (
                // 최상위 질문들 표시
                <>
                  {topContent.map((faq) => (
                    <div className="flex px-5 py-1 justify-end" key={faq.faqId}>
                      <div
                        className="mr-2 p-2 rounded-xl content-center border-2  border-[#3c2a13] text-[#3c2a13] cursor-pointer hover:bg-[#3c2a13] hover:text-white"
                        onClick={() => handleSelectFAQ(faq.content.toString(), faq.faqId)}>
                        {faq.content}
                      </div>
                    </div>
                  ))}
                </>
              ) : (
                <>
                  {/* 하위 질문들 */}
                  {(() => {
                    const filteredContent = allContent.filter((item) => item.parentId === parentId);

                    if (filteredContent.length === 1) {
                      return (
                        <>
                          {/* 선택된 질문 표시 */}
                          <div className="flex px-5 py-1 justify-end">
                            <div className="mr-2 p-2 rounded-xl content-center border-2 border-[#3c2a13] text-[#3c2a13]">
                              {selectFAQ}
                            </div>
                          </div>

                          <div className="flex px-5 py-3">
                            <div className="rounded-full w-12 h-12 min-w-12">
                              <img src={logo} alt="logo" />
                            </div>
                            <div className="ml-5 p-2 rounded-xl content-center bg-gray-200">
                              {filteredContent[0].content}
                            </div>
                          </div>

                          <div
                            onClick={handleBack}
                            className="text-white bg-[#3c2a13] mx-20 mt-5 p-2 text-center rounded-lg font-bold text-xl cursor-pointer absolute bottom-4 right-[60px]
                          hover:scale-105 hover:transition-transform z-50">
                            처음으로
                          </div>
                        </>
                      );
                    } else {
                      // 하위 질문이 여러 개 일때
                      return [
                        ...filteredContent.map((item) => (
                          <div className="flex px-5 py-1 justify-end" key={item.faqId}>
                            <div
                              className="mr-2 p-2 rounded-xl content-center border-2 border-[#3c2a13] text-[#3c2a13] cursor-pointer hover:bg-[#3c2a13] hover:text-white"
                              onClick={() => handleSelectFAQ(item.content.toString(), item.faqId)}>
                              {item.content}
                            </div>
                          </div>
                        )),
                        <div
                          onClick={handleBack}
                          className="text-white bg-[#3c2a13] mx-20 mt-5 p-2 text-center rounded-lg font-bold text-xl cursor-pointer absolute bottom-4 right-[60px]
                          hover:scale-105 hover:transition-transform z-50">
                          처음으로
                        </div>
                      ];
                    }
                  })()}
                </>
              )}
            </div>
          </div>
          {edit && <FAQEdIt Close={() => setEdit(false)} />}
        </>
      )}
    </div>
  );
};

export default FAQ;

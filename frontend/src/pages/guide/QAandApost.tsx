import GuideNavigation from "../../components/GuideNavigation";
import Walk from "../../../public/walk.png";
import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import axios from "axios";
import Header from "../../components/Header";
import useUserStore from "../../store/store";
import Chat from "../../components/Chat";
import FAQ from "../../components/FAQ";

interface Comment {
  id: number;
  content: string;
  adminName: string;
  created_at: string;
}

interface QnApost {
  id: number;
  title: string;
  content: string;
  viewCount: number;
  writerName: string;
  comments: Comment[];
  writerEmail: string;
  created_at: string;
}

const QAandApost = () => {
  const [qnapost, setQnapost] = useState<QnApost | null>(null);
  const navigate = useNavigate();
  const { id } = useParams();
  const [commentId, setCommentId] = useState<number | null>(null);
  const [reply, setReply] = useState(false);
  const [content, setContent] = useState("");
  const [edit, setEdit] = useState(false);
  const [editContent, setEditContent] = useState("");
  const [currentUserEmail, setCurrentUserEmail] = useState("");
  const role = useUserStore((state) => state.role);

  // 현재 사용자의 이메일 추출
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      const payload = JSON.parse(atob(token.split(".")[1]));
      const email = payload.email;
      setCurrentUserEmail(email);
    }
  }, [currentUserEmail]);

  //조회
  useEffect(() => {
    const fetchQnapost = async () => {
      try {
        const response = await axios.get(`http://15.164.103.160:8080/api/v1/inquiries/${id}`);
        setQnapost(response.data);
        if (response.data.comments.length > 0) {
          setCommentId(response.data.comments[0].id);
        }
        console.log(commentId);
      } catch (error) {
        console.error("불러오기 실패", error);
        navigate("/guide/qna");
      }
    };
    if (id) {
      fetchQnapost();
    }
  }, [id]);

  //삭제
  const handleDelete = async () => {
    if (!window.confirm("정말로 게시글을 삭제하시겠습니까?")) return;

    try {
      await axios.delete(`http://15.164.103.160:8080/api/v1/inquiries/${id}`, {
        headers: {
          Authorization: localStorage.getItem("accessToken"),
          "Content-Type": "application/json"
        }
      });
      alert("삭제되었습니다.");
      navigate("/guide/qna");
    } catch (error) {
      console.error("삭제 실패", error);
      alert("삭제에 실패하였습니다.");
    }
  };

  //답변 작성
  const handleReply = async () => {
    try {
      await axios.post(
        `http://15.164.103.160:8080/api/v1/inquiries/${id}/comments`,
        {
          content
        },
        {
          headers: {
            Authorization: localStorage.getItem("accessToken"),
            "Content-Type": "application/json"
          }
        }
      );
      alert("작성 되었습니다.");
      setContent("");
      setReply(false);

      const response = await axios.get(`http://15.164.103.160:8080/api/v1/inquiries/${id}`);
      setQnapost(response.data);
    } catch (error) {
      console.error("답변 작성이 취소되었습니다.", error);
      setContent("");
      setReply(false);
    }
  };

  //답변 삭제
  const handleReplyDelete = async () => {
    if (!window.confirm("정말로 답변을 삭제하시겠습니까?")) return;

    try {
      await axios.delete(`http://15.164.103.160:8080/api/v1/inquiries/${id}/comments/${commentId}`, {
        headers: {
          Authorization: localStorage.getItem("accessToken"),
          "Content-Type": "application/json"
        }
      });
      alert("답변이 삭제되었습니다.");
      setReply(false);
      const response = await axios.get(`http://15.164.103.160:8080/api/v1/inquiries/${id}`);
      setQnapost(response.data);
    } catch (error) {
      console.error("삭제 실패", error);
      alert("삭제에 실패하였습니다.");
    }
  };

  // 답변 수정
  const handleReplyEdit = async () => {
    try {
      await axios.put(
        `http://15.164.103.160:8080/api/v1/inquiries/${id}/comments/${commentId}`,
        {
          content: editContent
        },
        {
          headers: {
            Authorization: localStorage.getItem("accessToken"),
            "Content-Type": "application/json"
          }
        }
      );
      alert("수정 되었습니다.");
      setEdit(false);
      setContent("");
      setEditContent("");
      const response = await axios.get(`http://15.164.103.160:8080/api/v1/inquiries/${id}`);
      setQnapost(response.data);
    } catch (error) {
      console.error("수정 실패:", error);
      alert("수정을 실패하였습니다.");
    }
  };

  return (
    <div>
      <Header />
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
          <GuideNavigation />
          {qnapost && (
            <div className="max-w-[1000px] mx-auto" key={qnapost.id}>
              <div className="text-[35px] text-center border-t-[1px] border-black pt-4 font-bold mx-3">
                {qnapost.title}
              </div>
              <div className="text-center pt-4 pb-5 border-b-[1px] border-gray-300 mx-3">
                {qnapost.writerName} <span className="opacity-70 text-gray-400">|</span> {qnapost.created_at}{" "}
                <span className="opacity-70 text-gray-400">|</span> {qnapost.viewCount}
              </div>
              <div className="text-[20px] py-10 px-5 border-b-[1px] border-black mb-10 mx-3">
                <div dangerouslySetInnerHTML={{ __html: qnapost.content }} />
              </div>

              {/* 문의 답변 */}

              {qnapost.comments.length > 0 && (
                <>
                  <div className="bg-[#f1a34a]/90 w-auto h-auto p-5 rounded-lg shadow-[0_0_15px_rgba(0,0,0,0.5)] my-10 mx-3">
                    <div className="pb-6 pl-2 font-bold text-xl">문의 답변</div>
                    {edit ? (
                      <>
                        <textarea
                          className="w-full h-80 p-3 rounded-xl"
                          value={editContent}
                          onChange={(e) => setEditContent(e.target.value)}
                        />
                        <div className="flex gap-5 justify-end mt-5">
                          <div
                            className="bg-red-400 px-5 py-3 rounded-md font-bold cursor-pointer"
                            onClick={() => setEdit(false)}>
                            취소
                          </div>
                          <div
                            className="bg-blue-400 px-5 py-3 rounded-md font-bold cursor-pointer"
                            onClick={handleReplyEdit}>
                            수정
                          </div>
                        </div>
                      </>
                    ) : (
                      <>
                        {qnapost.comments.map((comment) => (
                          <>
                            <div className="bg-[#ffc37f]/90 w-full h-auto p-5 rounded-xl mb-10" key={comment.id}>
                              {comment.content}
                            </div>

                            <div className="flex gap-5 justify-end">
                              {role === "ROLE_ADMIN" && (
                                <>
                                  <div
                                    className="bg-red-400 px-5 py-3 rounded-md font-bold cursor-pointer"
                                    onClick={handleReplyDelete}>
                                    삭제
                                  </div>
                                  <div
                                    className="bg-blue-400 px-5 py-3 rounded-md font-bold cursor-pointer"
                                    onClick={() => {
                                      setEdit(true);
                                      setContent(comment.content);
                                      setEditContent(comment.content);
                                    }}>
                                    수정
                                  </div>
                                </>
                              )}
                            </div>
                          </>
                        ))}
                      </>
                    )}
                  </div>
                </>
              )}

              {reply && (
                <div className=" bg-[#f1a34a]/90 w-full h-auto p-5 rounded-lg my-10">
                  <div className="pb-5 font-bold text-center text-xl">답변 작성</div>
                  <div>
                    <textarea
                      className="w-full h-96 p-3 rounded-xl"
                      value={content}
                      onChange={(e) => {
                        setContent(e.target.value);
                      }}
                    />
                  </div>
                  <div className="flex gap-5 justify-end pt-5">
                    <div
                      className="bg-red-400 px-5 py-3 rounded-xl font-bold cursor-pointer"
                      onClick={() => setReply(false)}>
                      취소
                    </div>
                    <div className="bg-blue-400 px-5 py-3 rounded-xl font-bold cursor-pointer" onClick={handleReply}>
                      작성
                    </div>
                  </div>
                </div>
              )}

              <div
                className="float-right mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                onClick={() => navigate("/guide/qna")}>
                목록으로
              </div>
              {localStorage.getItem("accessToken") && (qnapost.writerEmail === currentUserEmail || role === "ROLE_ADMIN") && (
                  <>
                    <div
                      className="float-right mr-8 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                      onClick={handleDelete}>
                      삭제하기
                    </div>
                    <div
                      className="float-right mr-8 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                      onClick={() => navigate(`/guide/qna/Edit/${id}`)}>
                      수정하기
                    </div>
                  </>
               )}

              {role === "ROLE_ADMIN" && localStorage.getItem("accessToken") && !reply && (
                <div
                  className="float-right mr-8 mb-20 bg-[#3c2a13]/90 p-4 text-white font-bold text-[20px] cursor-pointer rounded-xl hover:scale-105 transition-transform"
                  onClick={() => setReply(!reply)}>
                  답변하기
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default QAandApost;

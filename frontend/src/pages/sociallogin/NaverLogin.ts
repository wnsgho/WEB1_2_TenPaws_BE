import axiosInstance from "../../utils/axiosInstance";

export const naverLogin = async (
  onSuccess: (token: string) => void,
  onError: (message: string) => void
) => {
  const clientId = import.meta.env.VITE_NAVER_CLIENT_ID;
  const callbackUrl = import.meta.env.VITE_NAVER_CALLBACK_URL;

  if (!clientId || !callbackUrl) {
    onError("네이버 로그인 설정이 올바르지 않습니다.");
    return;
  }

  const state = Math.random().toString(36).substr(2);
  const popupUrl = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${clientId}&redirect_uri=${encodeURIComponent(
    callbackUrl
  )}&state=${state}`;

  try {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("isSocialLogin");
    console.log("네이버 세션이 초기화 되었습니다.");
  } catch (error) {
    console.error("네이버 세션 초기화 실패:", error);
  }

  const popup = window.open(popupUrl, "NaverLogin", "width=500,height=700");

  if (!popup) {
    onError("팝업이 차단되었습니다.");
    return;
  }

  const popupInterval = setInterval(async () => {
    try {
      if (popup.location.href.includes("code=")) {
        const query = new URLSearchParams(popup.location.search);
        const authCode = query.get("code");
        const returnedState = query.get("state");
        popup.close();
        clearInterval(popupInterval);

        if (!authCode || state !== returnedState) {
          onError("인증 코드 또는 상태가 올바르지 않습니다.");
          return;
        }

        try {
          const response = await axiosInstance.post("/api/v1/auth/oauth2/naver", {
            code: authCode,
            state,
          });

          const { accessToken } = response.data;
          const formattedToken = accessToken.startsWith("Bearer ")
            ? accessToken
            : `Bearer ${accessToken}`;

          localStorage.setItem("accessToken", formattedToken);
          localStorage.setItem("isSocialLogin", "true");

          window.dispatchEvent(new Event("storage"));

          onSuccess(formattedToken);
        } catch (error) {
          console.error("네이버 로그인 서버 오류:", error);
          onError("로그인 중 오류가 발생했습니다.");
        }
      }
    } catch (error) {
      console.error("팝업 상태 확인 중 오류:", error);
    }

    if (popup.closed) {
      clearInterval(popupInterval);
    }
  }, 500);
};
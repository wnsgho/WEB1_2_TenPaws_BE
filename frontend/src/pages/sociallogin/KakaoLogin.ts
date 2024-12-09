import axiosInstance from "../../utils/axiosInstance";

export const kakaoLogin = async (
  onSuccess: (token: string) => void,
  onError: (message: string) => void
) => {
  console.log("카카오 JavaScript 키:", import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY);

  if (typeof window.Kakao !== "undefined") {
    
    if (!window.Kakao.isInitialized()) {
      const kakaoKey = import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY;

      if (kakaoKey) {
        window.Kakao.init(kakaoKey);
        console.log("Kakao SDK 초기화 완료");
      } else {
        onError("카카오 JavaScript 키가 설정되지 않았습니다.");
        return;
      }
    }

    try {
      if (window.Kakao.Auth.logout) {
        await new Promise((resolve) => {
          window.Kakao.Auth.logout(() => {
            console.log("기존 카카오 세션 초기화 완료");
            resolve(true);
          });
        });
      }
    } catch (error) {
      console.error("기존 세션 초기화 중 오류 발생:", error);
    }

    try {
      window.Kakao.Auth.login({
        throughTalk: false, 
        success: async (authObj: { access_token: string }) => {
          console.log("카카오 액세스 토큰:", authObj.access_token);

          try {
            const response = await axiosInstance.post("/api/v1/auth/oauth2/kakao", {
              token: authObj.access_token,
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
            console.error("카카오 로그인 백엔드 오류:", error);
            onError("카카오 로그인을 할 수 없습니다.");
          }
        },
        fail: (err: Error) => {
          console.error("카카오 로그인 실패:", err);
          onError("카카오 로그인 실패");
        },
      });
    } catch (error) {
      console.error("카카오 로그인 중 오류:", error);
      onError("카카오 로그인 중 오류가 발생했습니다.");
    }
  } else {
    onError("Kakao SDK가 로드되지 않았습니다.");
  }
};
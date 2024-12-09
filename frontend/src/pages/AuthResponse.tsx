import React, { useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";

const AuthResponse = () => {
  const { token } = useParams<{ token: string }>();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      alert("로그인을 할 수 없습니다. 다시 로그인해주세요.");
      navigate("/login");
      return;
    }

    try {
      const formattedToken = token.startsWith("Bearer ") ? token : `Bearer ${token}`;
      localStorage.setItem("accessToken", formattedToken);
      localStorage.setItem("isSocialLogin", "true");

      navigate("/");
    } catch (error) {
      console.error("토큰 처리 중 오류 발생:", error);
      alert("로그인을 할 수 없습니다. 다시 시도해주세요.");
    }
  }, [token, navigate]);

  return null;
};

export default AuthResponse;
import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://15.164.103.160:8080", // URL(변경된 경우 변경된 URL로 바꾸기)
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosInstance;

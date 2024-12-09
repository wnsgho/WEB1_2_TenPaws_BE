import React from 'react';
import { GoChevronRight } from "react-icons/go";
import { Link, useLocation } from 'react-router-dom';
import mainimage from '../assets/image/mainimage.webp';

// Props의 타입 정의
interface ErrorState {
  status: number;
}

const Error: React.FC = () => {

  const location = useLocation();

    // state에서 에러 정보를 받아오기
    const error = location.state as ErrorState | undefined;

    // 에러 정보가 없을 경우 기본값 설정
    const status = error?.status || 500;

  return (
    <div className="flex flex-col items-center justify-center">
      <div className="mt-32 w-60">
        <img src={mainimage} alt="mainImage" className="rounded-full" />
      </div>
      <div className="mt-4 text-center">
        <p className="text-xl font-semibold text-red-500">Error Code:{status}</p>
        <p className="text-gray-500">문제가 발생했습니다. 나중에 다시 시도해주세요.</p>
      </div>
      <div className='mt-10'>
        <Link to="/">
          <button className='flex items-center font-bold'>메인으로 이동 <GoChevronRight /></button>
        </Link>
      </div>
    </div>
  );
};

export default Error;

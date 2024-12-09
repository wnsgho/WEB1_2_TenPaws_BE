import React from 'react';

interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
}

const MyPageModal: React.FC<ModalProps> = ({ isOpen, onClose, children }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center">
      {/* 배경 흐림 처리 */}
      <div
        className="fixed inset-0 z-40 bg-black bg-opacity-30 backdrop-blur-sm"
        onClick={onClose} // 모달 밖 클릭 시 닫기
      ></div>
      
      {/* 모달 콘텐츠 */}
      <div className="relative z-50 w-full max-w-md p-6 bg-white !bg-white rounded-lg shadow-lg">
        <button
          onClick={onClose}
          className="absolute text-gray-500 top-3 right-3 hover:text-black"
        >
          ✖
        </button>
        {children}
      </div>
    </div>
  );
};

export default MyPageModal;

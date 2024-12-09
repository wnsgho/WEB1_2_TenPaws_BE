import { NavLink, useLocation } from "react-router-dom";
import { useState } from "react";

const GuideNavigation = () => {
    const [isOpen, setIsOpen] = useState(false);
    const location = useLocation();

    const menuItems = [
        { path: "/guide/announcement", label: "공지사항" },
        { path: "/guide/qna", label: "문의" },
        { path: "/guide/facilities", label: "반려동물 관련 시설" },
        { path: "/guide/walking-course", label: "산책 코스" },
    ];

    
    const getCurrentTitle = () => {
        const currentItem = menuItems.find(item => location.pathname.includes(item.path));
        return currentItem ? currentItem.label : "공지사항";
    };

    return (
        <>
            {/* 데스크탑 */}
            <div className="hidden min-[950px]:flex text-[30px] space-x-28 font-bold mb-20 bg-[#f1a34a] w-full justify-center text-black p-3">
                {menuItems.map((item) => (
                    <NavLink
                        key={item.path}
                        to={item.path}
                        className={({ isActive }) =>
                            isActive
                                ? "opacity-100"
                                : "opacity-30 hover:scale-105 hover:transition-transform hover:opacity-100"
                        }
                    >
                        {item.label}
                    </NavLink>
                ))}
            </div>

            {/* 반응형 950 이하 */}
            <div className="min-[950px]:hidden w-full mb-20">
                <div 
                    className="bg-[#f1a34a] p-4 cursor-pointer"
                    onClick={() => setIsOpen(!isOpen)}
                >
                    <div className="flex justify-between items-center text-[25px] font-bold">
                        <span>{getCurrentTitle()}</span>
                        <svg 
                            className={`w-6 h-6 transition-transform ${isOpen ? 'rotate-180' : ''}`}
                            fill="none" 
                            stroke="currentColor" 
                            viewBox="0 0 24 24"
                        >
                            <path 
                                strokeLinecap="round" 
                                strokeLinejoin="round" 
                                strokeWidth={2} 
                                d="M19 9l-7 7-7-7"
                            />
                        </svg>
                    </div>
                </div>
                
                {isOpen && (
                    <div className="border border-[#f1a34a]">
                        {menuItems.map((item) => (
                            <NavLink
                                key={item.path}
                                to={item.path}
                                className={({ isActive }) =>
                                    `block p-4 text-[18px] ${
                                        isActive 
                                            ? "bg-[#f1a34a]/20 font-bold" 
                                            : "hover:bg-[#f1a34a]/10"
                                    }`
                                }
                                onClick={() => setIsOpen(false)}
                            >
                                {item.label}
                            </NavLink>
                        ))}
                    </div>
                )}
            </div>
        </>
    );
};

export default GuideNavigation;

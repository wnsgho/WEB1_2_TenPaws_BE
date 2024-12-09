import { NavLink } from "react-router-dom";

const GuideNavigationMap = () => {
    return (
        <div className="flex text-[30px] space-x-28 font-bold bg-[#f1a34a]/90 w-full justify-center p-2">
          <NavLink 
            to="/guide/announcement"
            className={({ isActive }) => 
              isActive ? "opacity-100" : "opacity-30"
            }
          >
            공지사항
          </NavLink>
          <NavLink 
            to="/guide/qna"
            className={({ isActive }) => 
              isActive ? "opacity-100" : "opacity-30 "
            }
          >
            문의
          </NavLink>
          <NavLink 
            to="/guide/facilities"
            className={({ isActive }) => 
              isActive ? "opacity-100" : "opacity-30"
            }
          >
            반려동물 관련 시설
          </NavLink>
          <NavLink 
            to="/guide/walking-course"
            className={({ isActive }) => 
              isActive ? "opacity-100" : "opacity-30"
            }
          >
            산책 코스
          </NavLink>
        </div>
      );
    };
export default GuideNavigationMap;

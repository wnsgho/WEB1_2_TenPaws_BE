import { useEffect, useRef, useState } from "react";
import Header from "../../components/Header";
import FAQ from "../../components/FAQ";
import Chat from "../../components/Chat";

declare global {
  interface Window {
    kakao: any;
  }
}

const WalkingCourse = () => {
  const mapRef = useRef<HTMLDivElement>(null);
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [places, setPlaces] = useState<any[]>([]);
  const [markers, setMarkers] = useState<{ [key: string]: any }>({});
  const [overlays, setOverlays] = useState<{ [key: string]: any }>({});
  const [map, setMap] = useState<any>(null);
  const [isPanelOpen, setIsPanelOpen] = useState<boolean>(true);

  //사용자 위치 geolocation api
  useEffect(() => {
    if ("geolocation" in navigator) {
      const options = {
        enableHighAccuracy: true,
        maximumAge: 0,
        timeout: 5000
      };

      navigator.geolocation.getCurrentPosition(
        (position) => {
          setUserLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude
          });
        },
        (error) => {
          console.error("Error getting location:", error);
        }
      ),
        options;
    }
  }, []);

  //카카오 api 연동
  useEffect(() => {
    if (!userLocation) return;
    if (places.length > 0) return;

    window.kakao.maps.load(() => {
      if (!mapRef.current) return;

      const options = {
        center: new window.kakao.maps.LatLng(userLocation.lat, userLocation.lng),
        level: 5 // 지도 반경
      };

      const mapInstance = new window.kakao.maps.Map(mapRef.current, options);
      setMap(mapInstance); // map 객체 저장

      // 현재 위치 마커
      const currentMarker = new window.kakao.maps.CustomOverlay({
        position: new window.kakao.maps.LatLng(userLocation.lat, userLocation.lng),
        map: mapInstance,
        content: `
          <div style="
            width: 30px;
            height: 30px;
            background: red;
            border: 3px solid white;
            border-radius: 50%;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
          "></div>
        `
      });

      // 현재 위치 텍스트 조절
      const currentOverlay = new window.kakao.maps.CustomOverlay({
        position: currentMarker.getPosition(),
        content: `
          <div style="
            padding: 10px;
            text-align: center;
            background: white;
            border-radius: 4px;
            font-weight: bold;
            min-width: 120px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.2);
            font-size: 18px;
          ">
            현재 위치
          </div>
        `,
        xAnchor: 0.5,
        yAnchor: 1.5
      });
      currentOverlay.setMap(mapInstance);

      // 장소 검색
      const ps = new window.kakao.maps.services.Places(mapInstance);

      // 검색 결과
      const placesSearchCB = (data: any, status: any) => {
        if (status === window.kakao.maps.services.Status.OK) {
          setPlaces((prev) => [...prev, ...data]);
          data.forEach((place: any) => {
            const marker = new window.kakao.maps.Marker({
              position: new window.kakao.maps.LatLng(place.y, place.x),
              map: mapInstance
            });

            const customOverlay = new window.kakao.maps.CustomOverlay({
              position: marker.getPosition(),
              content: `
                <div style="
                  padding: 10px;
                  text-align: center;
                  background: white;
                  border-radius: 4px;
                  font-weight: bold;
                  min-width: 120px;
                  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
                  font-size: 18px;
                ">
                  ${place.place_name}
                  <div style="font-size: 12px; color: #888;">${place.category_group_name}</div>
                </div>
              `,
              xAnchor: 0.5,
              yAnchor: 1.7
            });

            setMarkers((prev) => ({ ...prev, [place.id]: marker }));
            setOverlays((prev) => ({ ...prev, [place.id]: customOverlay }));

            //마커호버
            window.kakao.maps.event.addListener(marker, "mouseover", () => {
              customOverlay.setMap(mapInstance);
            });
            window.kakao.maps.event.addListener(marker, "mouseout", () => {
              customOverlay.setMap(null);
            });
          });
        }
      };

      const searchOption = {
        location: new window.kakao.maps.LatLng(userLocation.lat, userLocation.lng),
        radius: 3000, //반경 km
        sort: window.kakao.maps.services.SortBy.DISTANCE
      };

      setPlaces([]);
      ps.keywordSearch("산책로", placesSearchCB, searchOption);
      ps.keywordSearch("반려동물산책", placesSearchCB, searchOption);
      ps.keywordSearch("공원", placesSearchCB, searchOption);
    });
  }, [userLocation]);

  const getLastCategory = (categoryName: string) => {
    return categoryName.split(">").pop()?.trim() || categoryName;
  };

  return (
    <div>
      <Header />
      <FAQ />
      <Chat />
      <div className="relative w-full h-[calc(100vh-64px)]">
        {/* 지도 컨테이너 */}
        <div className="absolute inset-0">
          <div ref={mapRef} className="w-full h-full"></div>
        </div>

        {/* 왼쪽 패널 */}
        <div className={`${isPanelOpen ? "w-[400px]" : "w-0"} absolute left-0 top-0 h-[calc(100vh-64px)] bg-white border-r transition-all duration-300 overflow-hidden z-10`}>
          <div className="w-[400px] h-full flex flex-col">
            <div className="text-center pt-6 text-3xl font-bold">산책로</div>
            <div className="bg-white p-3 text-md text-center font-bold text-red-500">
              현재 위치를 기준으로 반경 3km 검색 결과입니다
            </div>
            
            {/* 장소 목록 */}
            <div 
              className="flex-1 overflow-y-auto overflow-x-hidden"
              style={{
                scrollbarWidth: 'thin',
                scrollbarColor: '#888 #f1f1f1'
              }}>
              {places.map((place) => (
                <div
                  key={place.id}
                  className="p-4 border-b hover:bg-gray-50 cursor-pointer"
                  onClick={() => window.open(place.place_url, "_blank")}
                  onMouseEnter={() => overlays[place.id]?.setMap(map)}
                  onMouseLeave={() => overlays[place.id]?.setMap(null)}>
                  <div className="text-sm text-blue-600">{getLastCategory(place.category_name)}</div>
                  <div className="font-bold text-lg mb-1">{place.place_name}</div>
                  <div className="text-sm text-gray-600">{place.road_address_name}</div>
                  {place.phone && <div className="text-sm text-gray-600">{place.phone}</div>}
                  <div className="text-sm text-gray-500 mt-1">현재 위치로부터 {place.distance}m</div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* 토글 버튼 */}
        <div
          className={`bg-white z-50 absolute cursor-pointer hover:bg-gray-100 transition-all duration-300 rounded-r-xl
            ${isPanelOpen ? "left-[399px]" : "left-0"} 
            top-1/2 px-2 py-6 border-y border-r border-gray-300`}
          onClick={() => setIsPanelOpen(!isPanelOpen)}>
          {isPanelOpen ? "◀︎" : "▶︎"}
        </div>
      </div>
    </div>
  );
};

export default WalkingCourse;

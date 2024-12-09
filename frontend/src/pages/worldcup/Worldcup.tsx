import { useEffect, useState } from "react";

interface Pets {
  id: number;
  name: string;
  image: string;
  breed: string;
}

const Worldcup = () => {
  const [pets, setPets] = useState<Pets[]>([]);
  const [display, setDisplay] = useState<Pets[]>([]);
  const [winners, setWinners] = useState<Pets[]>([]);
  const [currentRound, setCurrentRound] = useState<number>(0);
  const [round, setRound] = useState<number>(16);
  const [choosenum, setChoosenum] = useState<number>(0);
  const [selectedId, setSelectedId] = useState<number | null>(null);

  useEffect(() => {
    const fetchPets = async () => {
      const petsdata: Pets[] = [
        {
          id: 1,
          name: "골든 리트리버",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 2,
          name: "골든 리트리버2",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 3,
          name: "골든 리트리버3",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 4,
          name: "골든 리트리버4",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 5,
          name: "골든 리트리버5",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 6,
          name: "골든 리트리버6",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 7,
          name: "골든 리트리버7",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 8,
          name: "골든 리트리버8",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 9,
          name: "골든 리트리버9",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 10,
          name: "골든 리트리버10",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 11,
          name: "골든 리트리버11",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 12,
          name: "골든 리트리버12",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 13,
          name: "골든 리트리버13",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 14,
          name: "골든 리트리버14",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 15,
          name: "골든 리트리버15",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        },
        {
          id: 16,
          name: "골든 리트리버16",
          image:
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0gcUu-D8ElLcFw0TZSvCdBasqs4rG_YesHQm9Rw269lUZeRMN-dPzVZ9VH82U9Ji8OC3XHBke75d21Coy7Cho4Q",
          breed: "리트리버"
        }
      ];
      //데이터 저장
      setPets(petsdata);

      //랜덤으로 값 지정후 오름차순 정렬(섞기)
      const suffled = [...petsdata].sort(() => Math.random() - 0.5);
      setDisplay(suffled);
    };
    fetchPets();
  }, []);

  //동물을 골랐을때의 함수 동작
  const selectPets = async (selected: Pets) => {
    //고르면 승자에 추가
    setWinners((prev) => [...prev, selected]);

    setSelectedId(selected.id);

    await new Promise(resolve => setTimeout(resolve, 500))

    //마지막 라운드인지 확인
    //승자가 라운드의 절반에 딱 맞는다면? >> 마지막 라운드
    if (winners.length + 1 === round / 2) {
      const nextroundsuffle = [...winners, selected].sort(() => Math.random() - 0.5);
      setDisplay(nextroundsuffle);
      setRound(round / 2);
      setWinners([]);
      setCurrentRound(0);
      setChoosenum(0);
    } else {
      //마지막 라운드가 아니라면 다음 대결 진행
      setCurrentRound((prev) => prev + 2);
      setChoosenum((prev) => prev + 1);
    }
    setSelectedId(null);
  };

return (
  <div>
    {round === 1 ? (
      <div className="text-center">
        <div className="text-4xl font-bold my-8"> 우승 </div>
        <div className="relative inline-block">
          <img 
            src={display[0]?.image} 
            alt={display[0]?.name} 
            className="w-[400px] h-[500px] object-cover rounded-lg"
          />
          <div className="absolute bottom-0 left-0 w-full bg-black/50 text-white p-4 rounded-t-lg">
            <div className="font-bold text-3xl">{display[0]?.name}</div>
          </div>
        </div>
        <div>
        <button className="mt-8 px-6 py-2 bg-black/50 text-white rounded-lg">
          다시하기
        </button>
        </div>
      </div>
    ) : (
      <div>
        <div className="font-bold text-center text-[50px] mb-12 bg-black/50 text-white">
          강아지 이상형 월드컵 {round === 2 ? "결승" : `${round}강 ${choosenum}/${round / 2}`} 
        </div>
        <div className="flex justify-center items-center gap-20">
        {display.slice(currentRound, currentRound + 2).map((pet) => (
  <div 
    key={pet.id} 
    onClick={() => selectPets(pet)} 
    className="relative cursor-pointer hover:transform hover:scale-[1.03] transition-all"
  >
    <img 
      src={pet.image} 
      alt={pet.name} 
      className="w-[800px] h-[800px] object-cover rounded-lg"
    />
    <div className="absolute bottom-0 w-full bg-black/50 text-white p-4 rounded-t-lg text-center">
      <div className="font-bold text-5xl">{pet.name}</div>
    </div>
    
    {selectedId === pet.id && (
      <div className="absolute inset-0 bg-black/50 flex items-center justify-center rounded-lg transition-all duration-300">
        <svg 
          className="w-32 h-32 text-green-500 animate-scale-up" 
          fill="none" 
          stroke="currentColor" 
          viewBox="0 0 24 24"
        >
          <path 
            strokeLinecap="round" 
            strokeLinejoin="round" 
            strokeWidth={2} 
            d="M5 13l4 4L19 7" 
          />
        </svg>
      </div>
    )}
  </div>
))}
        </div>
      </div>
    )}
  </div>
);
};

export default Worldcup;

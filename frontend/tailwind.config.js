export default {
  darkMode: false, 
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        mainColor: '#f1a34a',
        cancelColor: '#FF1212',
        bgColor: '#3c2a13',
      },
    },
  },
  plugins: [require("tailwind-scrollbar-hide")],
};

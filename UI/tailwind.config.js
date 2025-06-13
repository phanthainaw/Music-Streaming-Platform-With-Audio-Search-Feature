// tailwind.config.js
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        DarkestBlue: '#27374D',
        DarkerBlue: '#526D82',
        LighterBlue: '#9DB2BF',
        LightestBlue: '#DDE6ED',
        CrimsonRed: '#B04A4A'
      }
    }
  },
  plugins: [],
}

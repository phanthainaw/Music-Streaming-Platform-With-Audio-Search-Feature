import { COLOR } from "../constants/color";
import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faQuoteRight,
  faQuoteLeft,
  faQuoteLeftAlt,
} from "@fortawesome/free-solid-svg-icons";
import { MainPaneProvider, useMainPane } from "../contexts/MainPaneContext";

function Home() {
  return (
    <MainPaneProvider>
      <div className="h-screen grid grid-cols-[1fr_4fr]">
        <div className="p-4">
          <Sidebar />
        </div>
        <div className="p-4 grid grid-rows-[calc(100vh-100px-48px)_100px] gap-4">
          <div className="grid grid-cols-[3fr_360px] grid-rows-[50px_1fr] gap-4">
            <div className="flex">
              <Searchbar />
              <button
                className={`aspect-square ml-2 rounded-full shadow-lg bg-[${COLOR.LighterBlue}] hover:border-2 hover:cursor-pointer active:scale-105 flex items-center justify-center`}
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-ear-icon lucide-ear"
                >
                  <path d="M6 8.5a6.5 6.5 0 1 1 13 0c0 6-6 6-6 10a3.5 3.5 0 1 1-7 0" />
                  <path d="M15 8.5a2.5 2.5 0 0 0-5 0v1a2 2 0 1 1 0 4" />
                </svg>
              </button>
            </div>
            <div className="grid grid-cols-[1fr_50px] items-center gap-4">
              <p className="text-xl justify-self-end">dudestin'</p>
              <img
                className="aspect-square object-cover rounded-full border-lighterBlue border-2 shadow-lightestBlue-500"
                src="https://yt3.googleusercontent.com/INj9Uxeh21bASJSi1Z4P679ISTSmOG8v2QWbxCkc5AiKtCtTSg_aaLXdjkFVtdWWb4K7I_tzydc=s160-c-k-c0x00ffffff-no-rj"
                alt=""
              />
            </div>
            <MainPane />
            <Infobar />
          </div>
          <Playbar />
        </div>
      </div>
    </MainPaneProvider>
  );
}

export function MainPane() {
  const { paneMode, setPaneMode, nowPlaying, setNowPlaying } = useMainPane();

  return (
    <>
      {paneMode === "Tracks" && (
        <TrackList
          nowPlaying={nowPlaying}
          setNowPlaying={setNowPlaying}
        ></TrackList>
      )}
      {paneMode === "Artists" && <ArtistList />}
      {paneMode === "Albums" && <AlbumList />}
      {paneMode === "ArtistDetails" && <ArtistDetails />}
      {!["Tracks", "Artists", "Albums", "ArtistDetails"].includes(paneMode) && (
        <div></div>
      )}
    </>
  );
}

export function Sidebar() {
  const { paneMode, setPaneMode } = useMainPane();

  return (
    <div
      className={`bg-[${COLOR.LighterBlue}] w-full h-[calc(100%-20px)] rounded-md p-4 shadow-lg flex flex-col`}
    >
      <h1
        className={`text-[${COLOR.LightestBlue}] hover:[text:${COLOR.CrimsonRed}] text-8xl`}
      >
        HEAR
      </h1>
      <div className="grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 h-16 hover:h-20 transition-all duration-100">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="size-12"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M12 21a9.004 9.004 0 0 0 8.716-6.747M12 21a9.004 9.004 0 0 1-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 0 1 7.843 4.582M12 3a8.997 8.997 0 0 0-7.843 4.582m15.686 0A11.953 11.953 0 0 1 12 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0 1 21 12c0 .778-.099 1.533-.284 2.253m0 0A17.919 17.919 0 0 1 12 16.5c-3.162 0-6.133-.815-8.716-2.247m0 0A9.015 9.015 0 0 1 3 12c0-1.605.42-3.113 1.157-4.418"
          />
        </svg>
        <p className="text-2xl">Browse</p>
      </div>

      <div
        className={`grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 h-14 hover:h-18 transition-all duration-100 hover:cursor-pointer active:scale-105 transition-all duration-100 ease-in-out ${
          paneMode === "Tracks" ? "bg-lightestBlue text-darkerBlue" : ""
        }`}
        onClick={() => setPaneMode("Tracks")}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="size-8"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="m9 9 10.5-3m0 6.553v3.75a2.25 2.25 0 0 1-1.632 2.163l-1.32.377a1.803 1.803 0 1 1-.99-3.467l2.31-.66a2.25 2.25 0 0 0 1.632-2.163Zm0 0V2.25L9 5.25v10.303m0 0v3.75a2.25 2.25 0 0 1-1.632 2.163l-1.32.377a1.803 1.803 0 0 1-.99-3.467l2.31-.66A2.25 2.25 0 0 0 9 15.553Z"
          />
        </svg>

        <p>Tracks</p>
      </div>
      <div
        className={`grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 h-14 hover:h-18 transition-all duration-100 hover:cursor-pointer active:scale-105 transition-all duration-100 ease-in-out ${
          paneMode === "Artists" ? "bg-lightestBlue text-darkerBlue" : ""
        }`}
        onClick={() => setPaneMode("Artists")}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="size-8"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z"
          />
        </svg>
        <p>Artists</p>
      </div>
      <div
        className={`grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 h-14 hover:h-18 transition-all duration-100 hover:cursor-pointer active:scale-105 transition-all duration-100 ease-in-out ${
          paneMode === "Albums" ? "bg-lightestBlue  text-darkerBlue" : ""
        }`}
        onClick={() => setPaneMode("Albums")}
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          fill="none"
          viewBox="0 0 24 24"
          strokeWidth={1.5}
          stroke="currentColor"
          className="size-8"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M3.75 12h16.5m-16.5 3.75h16.5M3.75 19.5h16.5M5.625 4.5h12.75a1.875 1.875 0 0 1 0 3.75H5.625a1.875 1.875 0 0 1 0-3.75Z"
          />
        </svg>
        <p>Albums</p>
      </div>
      <hr
        className={`border-1 w-8/10 rounded-full  border-[${COLOR.LightestBlue}] my-4 `}
      />
      <div>
        <div className="grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 w-9/10 h-14 hover:bg-darkerBlue">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={1.5}
            stroke="currentColor"
            className="size-8"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12Z"
            />
          </svg>

          <p>Liked Tracks</p>
        </div>
        <div className="grid grid-cols-[4em_1fr] items-center border-2 p-2 mb-2 w-9/10 h-14 hover:bg-darkerBlue">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            strokeWidth={1.5}
            stroke="currentColor"
            className="size-6"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25H12"
            />
          </svg>
          <p>Playlists</p>
        </div>
      </div>
    </div>
  );
}

export function Searchbar() {
  return (
    <input
      type="text"
      className={`bg-[${COLOR.LighterBlue}] rounded-full w-128 p-4`}
      placeholder="Search..."
    />
  );
}

export function Playbar() {
  const { nowPlaying } = useMainPane();
  const [paused, setPaused] = useState(true);
  const [muted, setMuted] = useState(false);

  return (
    <div
      className={`bg-[${COLOR.LighterBlue}] w-[calc(100%-20px)] h-full rounded-md shadow-lg grid grid-cols-[1fr_2fr_1fr] gap-2`}
    >
      <div className="grid grid-cols-[68px_204px] gap-4 h-full p-4">
        <img
          className="aspect-square object-cover mr-2 shadow-lg rounded-lg"
          src={nowPlaying.coverUrl}
          alt=""
        />
        <div className="flex items-center">
          <div className="grid grid-rows-[1fr_1fr] items-center overflow-hidden">
            <div className="text-xl hover:cursor-pointer'">
              {nowPlaying.title}
            </div>
            <div className="text-darkestBlue/75 hover:cursor-pointer">
              {nowPlaying.artist}
            </div>
          </div>
        </div>
      </div>
      <div className="flex items-center justify-center">
        <div className="grid grid-rows-[3fr 1fr] w-full justify-items-center items-center">
          <div className="flex flex-row items-center justify-center">
            <div className="grid grid-cols-[1fr_1fr_2fr_1fr_1fr] items-center justify-items-center gap-4">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-repeat-icon lucide-repeat"
              >
                <path d="m17 2 4 4-4 4" />
                <path d="M3 11v-1a4 4 0 0 1 4-4h14" />
                <path d="m7 22-4-4 4-4" />
                <path d="M21 13v1a4 4 0 0 1-4 4H3" />
              </svg>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="size-6"
              >
                <path d="M9.195 18.44c1.25.714 2.805-.189 2.805-1.629v-2.34l6.945 3.968c1.25.715 2.805-.188 2.805-1.628V8.69c0-1.44-1.555-2.343-2.805-1.628L12 11.029v-2.34c0-1.44-1.555-2.343-2.805-1.628l-7.108 4.061c-1.26.72-1.26 2.536 0 3.256l7.108 4.061Z" />
              </svg>
              {paused ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="40"
                  height="40"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-pause-icon lucide-pause"
                  onClick={() => {
                    setPaused((prev) => !prev);
                  }}
                >
                  <rect x="14" y="4" width="4" height="16" rx="1" />
                  <rect x="6" y="4" width="4" height="16" rx="1" />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="40"
                  height="40"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-play-icon lucide-play"
                  onClick={() => {
                    setPaused((prev) => !prev);
                  }}
                >
                  <polygon points="6 3 20 12 6 21 6 3" />
                </svg>
              )}
              <svg
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
                className="size-6"
              >
                <path d="M5.055 7.06C3.805 6.347 2.25 7.25 2.25 8.69v8.122c0 1.44 1.555 2.343 2.805 1.628L12 14.471v2.34c0 1.44 1.555 2.343 2.805 1.628l7.108-4.061c1.26-.72 1.26-2.536 0-3.256l-7.108-4.061C13.555 6.346 12 7.249 12 8.689v2.34L5.055 7.061Z" />
              </svg>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-shuffle-icon lucide-shuffle"
              >
                <path d="m18 14 4 4-4 4" />
                <path d="m18 2 4 4-4 4" />
                <path d="M2 18h1.973a4 4 0 0 0 3.3-1.7l5.454-8.6a4 4 0 0 1 3.3-1.7H22" />
                <path d="M2 6h1.972a4 4 0 0 1 3.6 2.2" />
                <path d="M22 18h-6.041a4 4 0 0 1-3.3-1.8l-.359-.45" />
              </svg>
            </div>
          </div>
          <div className="grid grid-cols-[50px_1fr] w-full items-center ">
            <p>{nowPlaying.duration}</p>
            <hr
              className={`border-0.5 w-full rounded-full  border-[${COLOR.LightestBlue}]`}
            />
          </div>
        </div>
      </div>

      <div className="flex flex-row items-center justify-center p-4">
        <div className="grid grid-cols-[1fr_6fr] gap-4">
          {muted ? (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="size-8"
              onClick={() => {
                setMuted((prev) => !prev);
              }}
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M17.25 9.75 19.5 12m0 0 2.25 2.25M19.5 12l2.25-2.25M19.5 12l-2.25 2.25m-10.5-6 4.72-4.72a.75.75 0 0 1 1.28.53v15.88a.75.75 0 0 1-1.28.53l-4.72-4.72H4.51c-.88 0-1.704-.507-1.938-1.354A9.009 9.009 0 0 1 2.25 12c0-.83.112-1.633.322-2.396C2.806 8.756 3.63 8.25 4.51 8.25H6.75Z"
              />
            </svg>
          ) : (
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth={1.5}
              stroke="currentColor"
              className="size-8"
              onClick={() => {
                setMuted((prev) => !prev);
              }}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M19.114 5.636a9 9 0 0 1 0 12.728M16.463 8.288a5.25 5.25 0 0 1 0 7.424M6.75 8.25l4.72-4.72a.75.75 0 0 1 1.28.53v15.88a.75.75 0 0 1-1.28.53l-4.72-4.72H4.51c-.88 0-1.704-.507-1.938-1.354A9.009 9.009 0 0 1 2.25 12c0-.83.112-1.633.322-2.396C2.806 8.756 3.63 8.25 4.51 8.25H6.75Z"
              />
            </svg>
          )}
          <hr
            className={`border-0.5 w-full rounded-full  border-[${COLOR.LightestBlue}] my-4 `}
          />
        </div>
      </div>
    </div>
  );
}

export function Infobar() {
  const { nowPlaying } = useMainPane();

  return (
    <div
      className={`bg-[${COLOR.LighterBlue}] relative w-full h-full rounded-md shadow-lg grid grid-rows-[360px_1fr] justify-items-center overflow-hidden`}
    >
      <div className="p-4">
        <img
          className=" aspect-square object-cover w-full rounded-full border-lightestBlue border-4 shadow-lg"
          style={{
            "animation-name": "spin",
            "animation-duration": "15000ms",
            "animation-iteration-count": "infinite",
            "animation-timing-function": "linear",
          }}
          src={nowPlaying.coverUrl}
          alt=""
        />
        <svg
          className="w-8 h-8 text-lighterBlue absolute top-[180px] left-[180px] -translate-x-1/2 -translate-y-1/2 scale-120"
          viewBox="0 0 24 24"
          fill="rgb(39, 55, 77)"
          xmlns="http://www.w3.org/2000/svg"
        >
          <circle
            className="opacity-100"
            cx="12"
            cy="12"
            r="10"
            stroke="currentColor"
            strokeWidth="4"
          />
        </svg>
      </div>
      <p className="text-2xl">{nowPlaying.title}</p>
    </div>
  );
}

export function TrackList() {
  const { nowPlaying, setNowPlaying } = useMainPane();
  const [trackSearchResult, setTrackSearchResult] = useState([
    {
      id: 1,
      coverUrl: "https://i.ytimg.com/vi/pb7Gv-FcY8E/hqdefault.jpg",
      title: "Dáng Em",
      duration: 90,
      genre: "Indie",
      artist: "dudestin'",
      album: "Lonely Vibes",
      liked: true,
    },
    {
      id: 2,
      coverUrl: "https://i.ytimg.com/vi/XHOmBV4js_E/hqdefault.jpg",
      title: "Lost Sky",
      duration: 120,
      genre: "EDM",
      artist: "Lost Sky",
      album: "Dreamscape",
      liked: true,
    },
    {
      id: 3,
      coverUrl: "https://i.ytimg.com/vi/fLexgOxsZu0/hqdefault.jpg",
      title: "Happy",
      duration: 115,
      genre: "Pop",
      artist: "Pharrell Williams",
      album: "G I R L",
      liked: false,
    },
    {
      id: 4,
      coverUrl: "https://i.ytimg.com/vi/OPf0YbXqDm0/hqdefault.jpg",
      title: "Uptown Funk",
      duration: 130,
      genre: "Funk",
      artist: "Mark Ronson ft. Bruno Mars",
      album: "Uptown Special",
      liked: false,
    },
    {
      id: 5,
      coverUrl: "https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg",
      title: "Shape of You",
      duration: 125,
      genre: "Pop",
      artist: "Ed Sheeran",
      album: "÷ (Divide)",
      liked: false,
    },
    {
      id: 6,
      coverUrl: "https://i.ytimg.com/vi/2Vv-BfVoq4g/hqdefault.jpg",
      title: "Perfect",
      duration: 145,
      genre: "Ballad",
      artist: "Ed Sheeran",
      album: "÷ (Divide)",
      liked: false,
    },
    {
      id: 7,
      coverUrl: "https://i.ytimg.com/vi/Kx1R8hkE82w/hqdefault.jpg",
      title: "No Time To Die",
      duration: 135,
      genre: "Soundtrack",
      artist: "Billie Eilish",
      album: "No Time To Die OST",
      liked: false,
    },
    {
      id: 8,
      coverUrl: "https://i.ytimg.com/vi/09R8_2nJtjg/hqdefault.jpg",
      title: "Sugar",
      duration: 110,
      genre: "Pop",
      artist: "Maroon 5",
      album: "V",
      liked: false,
    },
    {
      id: 9,
      coverUrl: "https://i.ytimg.com/vi/60ItHLz5WEA/hqdefault.jpg",
      title: "Faded",
      duration: 140,
      genre: "EDM",
      artist: "Alan Walker",
      album: "Different World",
      liked: false,
    },
    {
      id: 10,
      coverUrl: "https://i.ytimg.com/vi/kXYiU_JCYtU/hqdefault.jpg",
      title: "Numb",
      duration: 100,
      genre: "Rock",
      artist: "Linkin Park",
      album: "Meteora",
      liked: false,
    },
  ]);

  const likeTrack = (id) => {
    setTrackSearchResult(
      trackSearchResult.map((track) =>
        track.id == id ? { ...track, liked: !track.liked } : track
      )
    );
  };
  return (
    <div className="relative overflow-y-scroll no-scrollbar overscroll-contain h-full flex flex-col items-center ">
      {trackSearchResult.map((track) => (
        <>
          <div
            className="grid grid-cols-[100px_3fr_1fr_200px] h-[100px] gap-4 items-center w-full"
            onClick={() =>
              setNowPlaying(trackSearchResult.find((tr) => tr.id == track.id))
            }
          >
            <img
              className="aspect-square object-cover h-full rounded"
              src={track.coverUrl}
              alt=""
            />
            <div>
              <p className="text-xl">{track.title}</p>
              <p>{track.album}</p>
            </div>
            <p>{track.artist}</p>
            <div className="flex justify-evenly">
              {track.id == nowPlaying.id ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  className="size-6 text-lighterBlue"
                >
                  <path
                    fillRule="evenodd"
                    d="M4.5 5.653c0-1.427 1.529-2.33 2.779-1.643l11.54 6.347c1.295.712 1.295 2.573 0 3.286L7.28 19.99c-1.25.687-2.779-.217-2.779-1.643V5.653Z"
                    clipRule="evenodd"
                  />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-play-icon lucide-play"
                  onClick={(e) => {
                    e.stopPropagation();
                    setNowPlaying(
                      trackSearchResult.find((tr) => tr.id == track.id)
                    );
                  }}
                >
                  <polygon points="6 3 20 12 6 21 6 3" />
                </svg>
              )}

              {track.liked ? (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                  className="size-6 text-lighterBlue shadow-lg"
                  onClick={(e) => {
                    e.stopPropagation();
                    likeTrack(track.id);
                  }}
                >
                  <path d="m11.645 20.91-.007-.003-.022-.012a15.247 15.247 0 0 1-.383-.218 25.18 25.18 0 0 1-4.244-3.17C4.688 15.36 2.25 12.174 2.25 8.25 2.25 5.322 4.714 3 7.688 3A5.5 5.5 0 0 1 12 5.052 5.5 5.5 0 0 1 16.313 3c2.973 0 5.437 2.322 5.437 5.25 0 3.925-2.438 7.111-4.739 9.256a25.175 25.175 0 0 1-4.244 3.17 15.247 15.247 0 0 1-.383.219l-.022.012-.007.004-.003.001a.752.752 0 0 1-.704 0l-.003-.001Z" />
                </svg>
              ) : (
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1.5"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="lucide lucide-heart-icon lucide-heart"
                  onClick={(e) => {
                    e.stopPropagation();
                    likeTrack(track.id);
                  }}
                >
                  <path d="M19 14c1.49-1.46 3-3.21 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.76 0-3 .5-4.5 2-1.5-1.5-2.74-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4.05 3 5.5l7 7Z" />
                </svg>
              )}

              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-list-plus-icon lucide-list-plus"
              >
                <path d="M11 12H3" />
                <path d="M16 6H3" />
                <path d="M16 18H3" />
                <path d="M18 9v6" />
                <path d="M21 12h-6" />
              </svg>
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
                class="lucide lucide-share-icon lucide-share"
              >
                <path d="M12 2v13" />
                <path d="m16 6-4-4-4 4" />
                <path d="M4 12v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8" />
              </svg>
            </div>
          </div>
          <hr
            className={`border-0.5 border-lighterBlue/75 w-[calc(100%-16px)] rounded-full mt-2 mb-2`}
          />
        </>
      ))}

      {/* <div className="pointer-events-none absolute bottom-0 w-full h-10 bg-gradient-to-t from-darkerBlue to-transparent z-20"></div> */}
    </div>
  );
}

export function ArtistList() {
  const { setPaneMode, setArtistDetails } = useMainPane();
  const [artistSearchResult, setArtistSearchResult] = useState([
    {
      id: 1,
      avatarUrl:
        "https://yt3.googleusercontent.com/INj9Uxeh21bASJSi1Z4P679ISTSmOG8v2QWbxCkc5AiKtCtTSg_aaLXdjkFVtdWWb4K7I_tzydc=s160-c-k-c0x00ffffff-no-rj",
      name: "dudestin'",
      bio: "We follow passion fruit",
      country: "VN",
    },
    {
      id: 2,
      avatarUrl:
        "https://yt3.googleusercontent.com/pZQ5JMD4EOI8TcNYAPTzMexe_fC0CKnb_hYlV4rPfIzmDidF239fH1XKmzkeT30XSg7fxNwc_w=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Ed Sheeran",
      bio: "Ed Sheeran is an English singer-songwriter known for his hit singles and acoustic guitar performances.",
      country: "UK",
    },
    {
      id: 3,
      avatarUrl:
        "https://yt3.googleusercontent.com/pbESKAjfNT6bL7WjlWxbyiJ2h8ceCOKYLJwpo2ZM8gCesyGCWZbVtJ8VVuIxhRk-QOwU64ou=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "BTS",
      bio: "BTS, also known as the Bangtan Boys, is a South Korean boy band known for their global influence and record-breaking music.",
      country: "South Korea",
    },
    {
      id: 4,
      avatarUrl:
        "https://yt3.googleusercontent.com/dirvtoDAmx-u0UR76-pxfhYL6Wxj2vfL2geUcxDwk62tTWWhGG6QDGc63RG3NdOz38-yBwRHDQ=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Billie Eilish",
      bio: "Billie Eilish is an American singer known for her whispery vocals, unique style, and chart-topping hits.",
      country: "USA",
    },
    {
      id: 5,
      avatarUrl:
        "https://yt3.googleusercontent.com/BkJSG0gIJTKyUziqxJOdtJSjXbPNtOelrBBFjRoOaa_xcFgaptqm8hvHxbh46P4uZc4lV3f4=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Adele",
      bio: "Adele is a British singer-songwriter known for her powerful voice and emotional ballads.",
      country: "UK",
    },
    {
      id: 3,
      avatarUrl:
        "https://yt3.googleusercontent.com/pbESKAjfNT6bL7WjlWxbyiJ2h8ceCOKYLJwpo2ZM8gCesyGCWZbVtJ8VVuIxhRk-QOwU64ou=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "BTS",
      bio: "BTS, also known as the Bangtan Boys, is a South Korean boy band known for their global influence and record-breaking music.",
      country: "South Korea",
    },
    {
      id: 4,
      avatarUrl:
        "https://yt3.googleusercontent.com/dirvtoDAmx-u0UR76-pxfhYL6Wxj2vfL2geUcxDwk62tTWWhGG6QDGc63RG3NdOz38-yBwRHDQ=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Billie Eilish",
      bio: "Billie Eilish is an American singer known for her whispery vocals, unique style, and chart-topping hits.",
      country: "USA",
    },
    {
      id: 5,
      avatarUrl:
        "https://yt3.googleusercontent.com/BkJSG0gIJTKyUziqxJOdtJSjXbPNtOelrBBFjRoOaa_xcFgaptqm8hvHxbh46P4uZc4lV3f4=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Adele",
      bio: "Adele is a British singer-songwriter known for her powerful voice and emotional ballads.",
      country: "UK",
    },
    {
      id: 3,
      avatarUrl:
        "https://yt3.googleusercontent.com/pbESKAjfNT6bL7WjlWxbyiJ2h8ceCOKYLJwpo2ZM8gCesyGCWZbVtJ8VVuIxhRk-QOwU64ou=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "BTS",
      bio: "BTS, also known as the Bangtan Boys, is a South Korean boy band known for their global influence and record-breaking music.",
      country: "South Korea",
    },
    {
      id: 4,
      avatarUrl:
        "https://yt3.googleusercontent.com/dirvtoDAmx-u0UR76-pxfhYL6Wxj2vfL2geUcxDwk62tTWWhGG6QDGc63RG3NdOz38-yBwRHDQ=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Billie Eilish",
      bio: "Billie Eilish is an American singer known for her whispery vocals, unique style, and chart-topping hits.",
      country: "USA",
    },
    {
      id: 5,
      avatarUrl:
        "https://yt3.googleusercontent.com/BkJSG0gIJTKyUziqxJOdtJSjXbPNtOelrBBFjRoOaa_xcFgaptqm8hvHxbh46P4uZc4lV3f4=s176-c-k-c0x00ffffff-no-rj-mo",
      name: "Adele",
      bio: "Adele is a British singer-songwriter known for her powerful voice and emotional ballads.",
      country: "UK",
    },
  ]);
  return (
    <div className="grid sm:grid-cols-2 3xl:grid-cols-3 4xl:grid-cols-4 overflow-y-scroll gap-4 no-scrollbar ">
      {artistSearchResult.map((artist) => (
        <div className="p-2 rounded-lg grid grid-rows-[1fr_4rem] items-center justify-items-center">
          <img
            className="aspect-square object-center w-4/5 rounded-full hover:border-lighterBlue hover:border-2 hover:cursor-pointer active:scale-101"
            onClick={() => {
              setPaneMode("ArtistDetails");
              setArtistDetails(artist);
            }}
            src={artist.avatarUrl}
            alt=""
          />
          <p>
            {artist.name} &middot; {artist.country}
          </p>
        </div>
      ))}
    </div>
  );
}

export function AlbumList() {
  return <div></div>;
}

export function ArtistDetails() {
  const { artistDetails, setPaneMode } = useMainPane();

  return (
    <div className="grid grid-rows-[12rem_calc(6rem)_1fr] relative">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="1.5"
        stroke-linecap="round"
        stroke-linejoin="round"
        class="lucide lucide-move-left-icon lucide-move-left absolute top-2 left-4 text-lightestBlue hover:-translate-x-1 transition-all duration-100 "
        onClick={()=>{setPaneMode('Artists')}}
      >
        <path d="M6 8L2 12L6 16" />
        <path d="M2 12H22" />
      </svg>
      <div className="bg-gradient-to-r bg-lighterBlue from-darkerBlue to-lighterBlue rounded-t-lg">
        <img
          className="aspect-square object-centerl rounded-full w-[12rem] top-[12rem] left-[8rem] -translate-x-1/2 -translate-y-1/2 border-darkestBlue border-4 absolute"
          src={artistDetails.avatarUrl}
          alt=""
        />
        <div className="absolute top-[9rem] left-[16rem] flex items-baseline">
          <div className="text-4xl mr-2">{artistDetails.name}</div>
          <div className="opacity-90">{arguments.country}</div>
        </div>
      </div>
      <div className="grid grid-cols-[16rem_1fr]">
        <div></div>
        <div className="pt-2 grid grid-cols-[24px_1fr] items-start">
          <FontAwesomeIcon
            className="text-lighterBlue inline"
            icon={faQuoteLeft}
          />
          <div className="grid grid-cols-[1fr_24px] ">
            <div>{artistDetails.bio}</div>
            <FontAwesomeIcon
              className="text-lighterBlue self-end"
              icon={faQuoteRight}
            />
          </div>
        </div>
      </div>
      <div className="p-4">
        <div className="text-4xl">Tracks</div>
      </div>
    </div>
  );
}

export default Home;

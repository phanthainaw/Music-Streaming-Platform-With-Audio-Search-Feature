// ArtistContext.jsx
import React, { createContext, useContext, useState } from "react";

// Create context
const MainPaneContext = createContext();

// Create provider
export const MainPaneProvider = ({ children }) => {
  const [paneMode, setPaneMode] = useState("");
  const [artistDetails, setArtistDetails] = useState({
    id: 0,
    avatarUrl: "",
    name: "",
    bio: "",
    country: "",
  });
  const [albumDetails, setAlbumDetails] = useState({});
  const [nowPlaying, setNowPlaying] = useState({
    id: 0,
    coverUrl: "",
    title: "",
    duration: 0,
    genre: "",
    artist: "",
    album: "",
    liked: false,
  });

  return (
    <MainPaneContext.Provider
      value={{
        paneMode,
        setPaneMode,
        artistDetails,
        setArtistDetails,
        albumDetails,
        setAlbumDetails,
        nowPlaying,
        setNowPlaying
      }}
    >
      {children}
    </MainPaneContext.Provider>
  );
};

// Custom hook for consuming
export const useMainPane = () => useContext(MainPaneContext);

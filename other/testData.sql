-- Test Data for Music Streaming Database
-- Generated for PostgreSQL

-- Users
INSERT INTO public.user_ (email, name, password_hash, role, username)
VALUES ('admin@example.com', 'Admin User', 'hashed_pw1', 2, 'admin'),
       ('manager1@example.com', 'Manager One', 'hashed_pw2', 1, 'manager1'),
       ('manager2@example.com', 'Manager Two', 'hashed_pw3', 1, 'manager2'),
       ('listener1@example.com', 'Listener One', 'hashed_pw4', 0, 'listener1'),
       ('listener2@example.com', 'Listener Two', 'hashed_pw5', 0, 'listener2'),
       ('listener3@example.com', 'Listener Three', 'hashed_pw6', 0, 'listener3'),
       ('nam.pt207622@sis.hust.edu.vn', 'Phan Thái Nam', '$2a$10$mvedy.vSezeRRz9j8Zjad..myGVf1qCAMfK2hdkUGTiFXZiajwd9G', 2, 'phanthainam2002');

-- Genres
INSERT INTO public.genre (title)
VALUES ('Pop'),
       ('Rock'),
       ('Jazz'),
       ('Hip Hop'),
       ('Classical');

-- Artists
INSERT INTO public.artist (avatar_url, bio, country, name, manager_id)
VALUES ('http://example.com/avatar1.jpg', 'Bio 1', 'US', 'Artist One', 2),
       ('http://example.com/avatar2.jpg', 'Bio 2', 'UK', 'Artist Two', 2),
       ('http://example.com/avatar3.jpg', 'Bio 3', 'CA', 'Artist Three', 3),
       ('http://example.com/avatar4.jpg', 'Bio 4', 'AU', 'Artist Four', 3);

-- Albums
INSERT INTO public.album (cover_url, release_date, title, genre_id, owner_id)
VALUES ('http://example.com/cover1.jpg', '2023-01-01', 'Pop Album', 1, 1),
       ('http://example.com/cover2.jpg', '2023-02-01', 'Rock Album', 2, 1),
       ('http://example.com/cover3.jpg', '2023-03-01', 'Jazz Album', 3, 1),
       ('http://example.com/cover4.jpg', '2023-04-01', 'Hip Hop Album', 4, 1);

-- Album-Artist Mapping
INSERT INTO public.album_artist (album_id, artist_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 3),
       (3, 4),
       (4, 4);

-- Tracks
INSERT INTO public.track (cover_url, duration, release_date, title, album_id, genre_id, publisher_id)
VALUES ('http://example.com/t1.jpg', 180, '2023-01-10', 'Track A', 1, 1, 1),
       ('http://example.com/t2.jpg', 200, '2023-01-11', 'Track B', 1, 1, 1),
       ('http://example.com/t3.jpg', 240, '2023-02-12', 'Track C', 2, 2, 1),
       ('http://example.com/t4.jpg', 210, '2023-03-13', 'Track D', 3, 3, 1),
       ('http://example.com/t5.jpg', 195, '2023-04-14', 'Track E', 4, 4, 1),
       ('http://example.com/t6.jpg', 220, '2023-04-15', 'Track F', 4, 4, 1);

-- Track-Artist Mapping
INSERT INTO public.track_artist (track_id, artist_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 1),
       (5, 4),
       (6, 3);

-- Playlists
INSERT INTO public.playlist (description, name, owner_id)
VALUES ('Best of Pop', 'Pop Hits', 4),
       ('Rock Forever', 'Rock Classics', 5),
       ('Smooth Jazz', 'Jazz Vibes', 6),
       ('Hip Hop Essentials', 'Hip Hop Beats', 4),
       ('Mix 2023', 'Top Mix', 5);

-- Playlist-Track Mapping
INSERT INTO public.playlist_track (playlist_id, track_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (3, 4),
       (4, 5),
       (4, 6),
       (5, 1),
       (5, 3),
       (5, 5);

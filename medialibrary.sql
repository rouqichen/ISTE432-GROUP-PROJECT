--
-- PostgreSQL database dump
--

-- Dumped from database version 12.6 (Ubuntu 12.6-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.6 (Ubuntu 12.6-0ubuntu0.20.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genre (
    genreid integer NOT NULL,
    name character varying(50)
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- Name: genre_genreid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genre_genreid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genre_genreid_seq OWNER TO postgres;

--
-- Name: genre_genreid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genre_genreid_seq OWNED BY public.genre.genreid;


--
-- Name: lib_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lib_user (
    username character varying(50) NOT NULL,
    password character varying(255) NOT NULL,
    uid integer NOT NULL
);


ALTER TABLE public.lib_user OWNER TO postgres;

--
-- Name: lib_user_uid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.lib_user_uid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lib_user_uid_seq OWNER TO postgres;

--
-- Name: lib_user_uid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.lib_user_uid_seq OWNED BY public.lib_user.uid;


--
-- Name: media; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.media (
    mediaid integer NOT NULL,
    title character varying(50),
    release_date date,
    image_url character varying(255),
    description text
);


ALTER TABLE public.media OWNER TO root;

--
-- Name: media_genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.media_genre (
    mediaid integer NOT NULL,
    genreid integer NOT NULL
);


ALTER TABLE public.media_genre OWNER TO postgres;

--
-- Name: media_mediaid_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.media_mediaid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.media_mediaid_seq OWNER TO root;

--
-- Name: media_mediaid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.media_mediaid_seq OWNED BY public.media.mediaid;


--
-- Name: movie; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movie (
    movieid integer NOT NULL,
    mediaid integer,
    runtime integer,
    mpaa_rating character varying(5)
);


ALTER TABLE public.movie OWNER TO postgres;

--
-- Name: movie_movieid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.movie_movieid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.movie_movieid_seq OWNER TO postgres;

--
-- Name: movie_movieid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.movie_movieid_seq OWNED BY public.movie.movieid;


--
-- Name: tv_show; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tv_show (
    showid integer NOT NULL,
    mediaid integer,
    episode_length integer,
    tv_rating character varying(5)
);


ALTER TABLE public.tv_show OWNER TO postgres;

--
-- Name: tv_show_showid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tv_show_showid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tv_show_showid_seq OWNER TO postgres;

--
-- Name: tv_show_showid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tv_show_showid_seq OWNED BY public.tv_show.showid;


--
-- Name: user_media; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_media (
    uid integer NOT NULL,
    mediaid integer NOT NULL,
    location character varying(50)
);


ALTER TABLE public.user_media OWNER TO postgres;

--
-- Name: genre genreid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre ALTER COLUMN genreid SET DEFAULT nextval('public.genre_genreid_seq'::regclass);


--
-- Name: lib_user uid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lib_user ALTER COLUMN uid SET DEFAULT nextval('public.lib_user_uid_seq'::regclass);


--
-- Name: media mediaid; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.media ALTER COLUMN mediaid SET DEFAULT nextval('public.media_mediaid_seq'::regclass);


--
-- Name: movie movieid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movie ALTER COLUMN movieid SET DEFAULT nextval('public.movie_movieid_seq'::regclass);


--
-- Name: tv_show showid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tv_show ALTER COLUMN showid SET DEFAULT nextval('public.tv_show_showid_seq'::regclass);


--
-- Data for Name: genre; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.genre (genreid, name) FROM stdin;
0	Comedy
1	Drama
2	Action
3	Sci-Fi
4	Thriller
\.


--
-- Data for Name: lib_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lib_user (username, password, uid) FROM stdin;
testuser	password	1
\.


--
-- Data for Name: media; Type: TABLE DATA; Schema: public; Owner: root
--

COPY public.media (mediaid, title, release_date, image_url, description) FROM stdin;
0	Inception	2010-07-14	https://imdb-api.com/images/original/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6791_AL_.jpg	Dom Cobb is a skilled thief, the absolute best in the dangerous art of extraction, stealing valuable secrets from deep within the subconscious during the dream state, when the mind is at its most vulnerable. Cobbs rare ability has made him a coveted player in this treacherous new world of corporate espionage, but it has also made him an international fugitive and cost him everything he has ever loved. Now Cobb is being offered a chance at redemption. One last job could give him his life back but only if he can accomplish the impossible, inception. Instead of the perfect heist, Cobb and his team of specialists have to pull off the reverse: their task is not to steal an idea, but to plant one. If they succeed, it could be the perfect crime. But no amount of careful planning or expertise can prepare the team for the dangerous enemy that seems to predict their every move. An enemy that only Cobb could have seen coming.
1	Scrubs	2001-10-02	https://imdb-api.com/images/original/MV5BODE1MGVjZjMtODc5My00ODBjLTg0NWItMDllNTNlM2Y3ZGYyXkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_Ratio0.6791_AL_.jpg	Set in the fictional Sacred Heart hospital in California, John J.D Dorian makes his way through the overwhelming world of medicine, with the help of his best friend, his fellow rookie doctors, and the arrogant, but brilliant attending physician he views as his mentor.
\.


--
-- Data for Name: media_genre; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.media_genre (mediaid, genreid) FROM stdin;
0	2
0	3
0	4
1	0
1	1
\.


--
-- Data for Name: movie; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movie (movieid, mediaid, runtime, mpaa_rating) FROM stdin;
0	0	148	PG-13
\.


--
-- Data for Name: tv_show; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tv_show (showid, mediaid, episode_length, tv_rating) FROM stdin;
0	1	22	TV-14
\.


--
-- Data for Name: user_media; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_media (uid, mediaid, location) FROM stdin;
1	0	\N
1	1	\N
\.


--
-- Name: genre_genreid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.genre_genreid_seq', 2, true);


--
-- Name: lib_user_uid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.lib_user_uid_seq', 1, true);


--
-- Name: media_mediaid_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.media_mediaid_seq', 1, false);


--
-- Name: movie_movieid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.movie_movieid_seq', 1, false);


--
-- Name: tv_show_showid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tv_show_showid_seq', 1, false);


--
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (genreid);


--
-- Name: lib_user lib_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lib_user
    ADD CONSTRAINT lib_user_pkey PRIMARY KEY (uid);


--
-- Name: media_genre media_genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_genre
    ADD CONSTRAINT media_genre_pkey PRIMARY KEY (mediaid, genreid);


--
-- Name: media media_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.media
    ADD CONSTRAINT media_pkey PRIMARY KEY (mediaid);


--
-- Name: movie movie_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movie
    ADD CONSTRAINT movie_pkey PRIMARY KEY (movieid);


--
-- Name: tv_show tv_show_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tv_show
    ADD CONSTRAINT tv_show_pkey PRIMARY KEY (showid);


--
-- Name: user_media user_media_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_media
    ADD CONSTRAINT user_media_pkey PRIMARY KEY (uid, mediaid);


--
-- Name: media_genre media_genre_genreid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_genre
    ADD CONSTRAINT media_genre_genreid_fkey FOREIGN KEY (genreid) REFERENCES public.genre(genreid);


--
-- Name: media_genre media_genre_mediaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.media_genre
    ADD CONSTRAINT media_genre_mediaid_fkey FOREIGN KEY (mediaid) REFERENCES public.media(mediaid);


--
-- Name: movie movie_mediaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movie
    ADD CONSTRAINT movie_mediaid_fkey FOREIGN KEY (mediaid) REFERENCES public.media(mediaid);


--
-- Name: tv_show tv_show_mediaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tv_show
    ADD CONSTRAINT tv_show_mediaid_fkey FOREIGN KEY (mediaid) REFERENCES public.media(mediaid);


--
-- Name: user_media user_media_mediaid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_media
    ADD CONSTRAINT user_media_mediaid_fkey FOREIGN KEY (mediaid) REFERENCES public.media(mediaid);


--
-- Name: user_media user_media_uid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_media
    ADD CONSTRAINT user_media_uid_fkey FOREIGN KEY (uid) REFERENCES public.lib_user(uid);


--
-- PostgreSQL database dump complete
--


--
-- PostgreSQL database dump
--

-- Dumped from database version 10.7
-- Dumped by pg_dump version 10.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: rssfeed; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rssfeed (
    "Title" character varying(255),
    "Link" character varying(512),
    "Description" text,
    "Language" character varying(255),
    "Copyright" character varying(255),
    "Pubdate" timestamp without time zone,
    "RssID" integer NOT NULL
);


ALTER TABLE public.rssfeed OWNER TO postgres;

--
-- Name: rssfeeditem; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rssfeeditem (
    "Guid" character varying(255) NOT NULL,
    "Description" text,
    "Link" character varying(255) NOT NULL,
    "Title" character varying(255),
    "Author" character varying(255),
    "RssItemID" bigint
);


ALTER TABLE public.rssfeeditem OWNER TO postgres;

--
-- Name: suscriberrss; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suscriberrss (
    "userID" bigint NOT NULL,
    "rssID" bigint NOT NULL,
    "suscriberID" integer NOT NULL
);


ALTER TABLE public.suscriberrss OWNER TO postgres;

--
-- Name: suscriberrss_suscriberID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."suscriberrss_suscriberID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."suscriberrss_suscriberID_seq" OWNER TO postgres;

--
-- Name: suscriberrss_suscriberID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."suscriberrss_suscriberID_seq" OWNED BY public.suscriberrss."suscriberID";


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    "Login" character varying(255) NOT NULL,
    "Password" character varying(255) NOT NULL,
    "LoginID" integer NOT NULL,
    "Email" character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_loginID_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."users_loginID_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public."users_loginID_seq" OWNER TO postgres;

--
-- Name: users_loginID_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."users_loginID_seq" OWNED BY public.users."LoginID";


--
-- Name: suscriberrss suscriberID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suscriberrss ALTER COLUMN "suscriberID" SET DEFAULT nextval('public."suscriberrss_suscriberID_seq"'::regclass);


--
-- Name: users LoginID; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN "LoginID" SET DEFAULT nextval('public."users_loginID_seq"'::regclass);


--
-- Data for Name: rssfeed; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rssfeed ("Title", "Link", "Description", "Language", "Copyright", "Pubdate", "RssID") FROM stdin;
\.


--
-- Data for Name: rssfeeditem; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rssfeeditem ("Guid", "Description", "Link", "Title", "Author", "RssItemID") FROM stdin;
\.


--
-- Data for Name: suscriberrss; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.suscriberrss ("userID", "rssID", "suscriberID") FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users ("Login", "Password", "LoginID", "Email") FROM stdin;
jnelson	password1234	1	jnelson.epi@epitech.eu
Jnelson3	md5testtt	3	megatest@live.fr
Jnelson5	md5testtt	5	megatest@live.fr
\.


--
-- Name: suscriberrss_suscriberID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."suscriberrss_suscriberID_seq"', 1, false);


--
-- Name: users_loginID_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."users_loginID_seq"', 5, true);


--
-- Name: suscriberrss PK_COUPLE; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suscriberrss
    ADD CONSTRAINT "PK_COUPLE" PRIMARY KEY ("userID", "rssID");


--
-- Name: rssfeeditem RSSFeedItem_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rssfeeditem
    ADD CONSTRAINT "RSSFeedItem_pkey" PRIMARY KEY ("Guid");


--
-- Name: users login_unique_constraint; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT login_unique_constraint UNIQUE ("Login");


--
-- Name: rssfeed rssfeed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rssfeed
    ADD CONSTRAINT rssfeed_pkey PRIMARY KEY ("RssID");


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY ("LoginID");


--
-- Name: rssfeeditem FK_Constraint_RSSIDITEM_RSSID; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rssfeeditem
    ADD CONSTRAINT "FK_Constraint_RSSIDITEM_RSSID" FOREIGN KEY ("RssItemID") REFERENCES public.rssfeed("RssID");


--
-- Name: suscriberrss FK_SUSCRIBER_RSS; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suscriberrss
    ADD CONSTRAINT "FK_SUSCRIBER_RSS" FOREIGN KEY ("rssID") REFERENCES public.rssfeed("RssID");


--
-- Name: suscriberrss FK_SUSCRIBER_USER; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suscriberrss
    ADD CONSTRAINT "FK_SUSCRIBER_USER" FOREIGN KEY ("userID") REFERENCES public.users("LoginID");


--
-- PostgreSQL database dump complete
--
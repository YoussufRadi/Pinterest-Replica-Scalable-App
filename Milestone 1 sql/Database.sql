--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

-- Started on 2018-02-12 18:44:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 32769)
-- Name: user; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "user";


ALTER SCHEMA "user" OWNER TO postgres;

SET search_path = "user", pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 189 (class 1259 OID 32785)
-- Name: Blocks; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "Blocks" (
    user_blocking_id bigint,
    user_blocked_id bigint,
    block_id bigint NOT NULL
);


ALTER TABLE "Blocks" OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 32783)
-- Name: Blocks_block_id_seq; Type: SEQUENCE; Schema: user; Owner: postgres
--

CREATE SEQUENCE "Blocks_block_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "Blocks_block_id_seq" OWNER TO postgres;

--
-- TOC entry 2155 (class 0 OID 0)
-- Dependencies: 188
-- Name: Blocks_block_id_seq; Type: SEQUENCE OWNED BY; Schema: user; Owner: postgres
--

ALTER SEQUENCE "Blocks_block_id_seq" OWNED BY "Blocks".block_id;


--
-- TOC entry 187 (class 1259 OID 32772)
-- Name: Users; Type: TABLE; Schema: user; Owner: postgres
--

CREATE TABLE "Users" (
    user_id bigint NOT NULL,
    gender boolean NOT NULL,
    email character varying(100) NOT NULL,
    user_name character varying(50) NOT NULL,
    pass character varying(128) NOT NULL,
    country character varying(50) NOT NULL,
    first_name character varying(100),
    last_name character varying(100),
    categories_id bigint[],
    users_followers_id bigint[],
    users_following_id bigint[],
    picture_id bigint,
    "Notifications" boolean,
    about_you character varying(300),
    location character varying(100),
    boards bigint[],
    pins bigint[],
    liked_images_id bigint[],
    disliked_images_id bigint[],
    hashtags_id bigint[]
);


ALTER TABLE "Users" OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 32770)
-- Name: Users_user_id_seq; Type: SEQUENCE; Schema: user; Owner: postgres
--

CREATE SEQUENCE "Users_user_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "Users_user_id_seq" OWNER TO postgres;

--
-- TOC entry 2156 (class 0 OID 0)
-- Dependencies: 186
-- Name: Users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: user; Owner: postgres
--

ALTER SEQUENCE "Users_user_id_seq" OWNED BY "Users".user_id;


--
-- TOC entry 2021 (class 2604 OID 32788)
-- Name: Blocks block_id; Type: DEFAULT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Blocks" ALTER COLUMN block_id SET DEFAULT nextval('"Blocks_block_id_seq"'::regclass);


--
-- TOC entry 2020 (class 2604 OID 32775)
-- Name: Users user_id; Type: DEFAULT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Users" ALTER COLUMN user_id SET DEFAULT nextval('"Users_user_id_seq"'::regclass);


--
-- TOC entry 2150 (class 0 OID 32785)
-- Dependencies: 189
-- Data for Name: Blocks; Type: TABLE DATA; Schema: user; Owner: postgres
--

COPY "Blocks" (user_blocking_id, user_blocked_id, block_id) FROM stdin;
1	2	1
\.


--
-- TOC entry 2157 (class 0 OID 0)
-- Dependencies: 188
-- Name: Blocks_block_id_seq; Type: SEQUENCE SET; Schema: user; Owner: postgres
--

SELECT pg_catalog.setval('"Blocks_block_id_seq"', 2, true);


--
-- TOC entry 2148 (class 0 OID 32772)
-- Dependencies: 187
-- Data for Name: Users; Type: TABLE DATA; Schema: user; Owner: postgres
--

COPY "Users" (user_id, gender, email, user_name, pass, country, first_name, last_name, categories_id, users_followers_id, users_following_id, picture_id, "Notifications", about_you, location, boards, pins, liked_images_id, disliked_images_id, hashtags_id) FROM stdin;
21	f	mhana@hotmail	hana	hana	Egypt	hana	mohamed	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
22	f	dina@hotmail	dina	1211	usa	dina	diaa	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
23	t	hazem@hotmail	hazemm	1231	Egypt	haze,	asy	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
24	f	sara@hotmail	saraa	1231	Egypt	sara	asy	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
25	t	karim@hotmail	karimm	1231	Egypt	karim	asy	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
26	t	kareem@hotmail	kareem	1231	Egypt	kareem	hazem	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
27	t	lala@gmail.com	ahmed	1231	Egypt	ahmed	mohamed	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
28	f	farah@hotmail	farah	1231	Egypt	mfarah	asy	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
29	t	john@hotmail	john	1231	Egypt	john	youssed	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
30	t	youssef@hotmail	youssed	1231	Egypt	yousses	ahmed	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
31	f	salma@hotmail	salma	1231	Egypt	salma	ahmed	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
20	t	mohamed1@hotmail	mohamed1	1231	Egypt	mohamed1	asy	\N	{1}	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
1	t	ahmed@hotmail	m	123	England	mohamed	ahmed	\N	\N	{20}	\N	\N	\N	\N	\N	\N	{}	\N	\N
3	f	wafa@gmail.com	wafaa_shanta	98765	Egypt	wafaa	shanta	\N	{}	\N	\N	\N	\N	\N	\N	\N	\N	\N	\N
2	t	mohamed@hotmail	mohamed	123	egypt	mohamed	asy	{1,2}	\N	{}	\N	\N	\N	\N	\N	\N	{1}	{}	\N
\.


--
-- TOC entry 2158 (class 0 OID 0)
-- Dependencies: 186
-- Name: Users_user_id_seq; Type: SEQUENCE SET; Schema: user; Owner: postgres
--

SELECT pg_catalog.setval('"Users_user_id_seq"', 31, true);


--
-- TOC entry 2027 (class 2606 OID 32790)
-- Name: Blocks Blocks_pkey; Type: CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Blocks"
    ADD CONSTRAINT "Blocks_pkey" PRIMARY KEY (block_id);


--
-- TOC entry 2023 (class 2606 OID 32782)
-- Name: Users email_unique; Type: CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Users"
    ADD CONSTRAINT email_unique UNIQUE (email);


--
-- TOC entry 2025 (class 2606 OID 32780)
-- Name: Users user_id; Type: CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Users"
    ADD CONSTRAINT user_id PRIMARY KEY (user_id);


--
-- TOC entry 2029 (class 2606 OID 32796)
-- Name: Blocks blocked; Type: FK CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Blocks"
    ADD CONSTRAINT blocked FOREIGN KEY (user_blocking_id) REFERENCES "Users"(user_id) ON DELETE CASCADE;


--
-- TOC entry 2028 (class 2606 OID 32791)
-- Name: Blocks blocking; Type: FK CONSTRAINT; Schema: user; Owner: postgres
--

ALTER TABLE ONLY "Blocks"
    ADD CONSTRAINT blocking FOREIGN KEY (user_blocking_id) REFERENCES "Users"(user_id) ON DELETE CASCADE;


-- Completed on 2018-02-12 18:44:15

--
-- PostgreSQL database dump complete
--


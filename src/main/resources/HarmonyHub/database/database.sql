--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

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
-- Name: chat; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat (
    id_chat uuid DEFAULT gen_random_uuid() NOT NULL,
    chat_name character varying(256) NOT NULL,
    id_theme uuid NOT NULL
);


ALTER TABLE public.chat OWNER TO postgres;

--
-- Name: chattheme; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chattheme (
    id_theme uuid DEFAULT gen_random_uuid() NOT NULL,
    theme_name character varying(256) NOT NULL,
    background_model character varying(256) NOT NULL,
    user_color character varying(256) NOT NULL,
    other_color character varying(256) NOT NULL
);


ALTER TABLE public.chattheme OWNER TO postgres;

--
-- Name: friendship; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.friendship (
    date_friendship date NOT NULL,
    friend_1 uuid NOT NULL,
    friend_2 uuid NOT NULL
);


ALTER TABLE public.friendship OWNER TO postgres;

--
-- Name: friendshiprequest; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.friendshiprequest (
    id_request uuid DEFAULT gen_random_uuid() NOT NULL,
    user_sender uuid NOT NULL,
    user_receiver uuid NOT NULL,
    date_request date NOT NULL,
    status character varying(256) NOT NULL
);


ALTER TABLE public.friendshiprequest OWNER TO postgres;

--
-- Name: message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message (
    id_message uuid DEFAULT gen_random_uuid() NOT NULL,
    id_chat uuid NOT NULL,
    id_from uuid NOT NULL,
    message character varying(256) NOT NULL,
    messagedate date NOT NULL,
    id_reply uuid DEFAULT uuid_in('1'::cstring)
);


ALTER TABLE public.message OWNER TO postgres;

--
-- Name: messagereceiver; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messagereceiver (
    id_message uuid NOT NULL,
    id_receiver uuid NOT NULL
);


ALTER TABLE public.messagereceiver OWNER TO postgres;

--
-- Name: userchats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.userchats (
    id_user uuid NOT NULL,
    id_chat uuid NOT NULL
);


ALTER TABLE public.userchats OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id_user uuid DEFAULT gen_random_uuid() NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(512) NOT NULL,
    salt character varying(512) NOT NULL,
    app_theme character varying(100) DEFAULT 'light'::character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: chat; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat (id_chat, chat_name, id_theme) FROM stdin;
\.


--
-- Data for Name: chattheme; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chattheme (id_theme, theme_name, background_model, user_color, other_color) FROM stdin;
\.


--
-- Data for Name: friendship; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friendship (date_friendship, friend_1, friend_2) FROM stdin;
\.


--
-- Data for Name: friendshiprequest; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.friendshiprequest (id_request, user_sender, user_receiver, date_request, status) FROM stdin;
\.


--
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (id_message, id_chat, id_from, message, messagedate, id_reply) FROM stdin;
\.


--
-- Data for Name: messagereceiver; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.messagereceiver (id_message, id_receiver) FROM stdin;
\.


--
-- Data for Name: userchats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.userchats (id_user, id_chat) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id_user, first_name, last_name, username, email, password_hash, salt, app_theme) FROM stdin;
\.


--
-- Name: chat chat_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_pk PRIMARY KEY (id_chat);


--
-- Name: chattheme chattheme_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chattheme
    ADD CONSTRAINT chattheme_pk UNIQUE (theme_name);


--
-- Name: chattheme chattheme_pk_2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chattheme
    ADD CONSTRAINT chattheme_pk_2 PRIMARY KEY (id_theme);


--
-- Name: friendship friendship_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_pk PRIMARY KEY (friend_2, friend_1);


--
-- Name: friendshiprequest friendshiprequest_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendshiprequest
    ADD CONSTRAINT friendshiprequest_pk PRIMARY KEY (id_request);


--
-- Name: message message_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pk PRIMARY KEY (id_message);


--
-- Name: messagereceiver messagereceiver_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messagereceiver
    ADD CONSTRAINT messagereceiver_pk PRIMARY KEY (id_receiver, id_message);


--
-- Name: userchats userchats_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.userchats
    ADD CONSTRAINT userchats_pk PRIMARY KEY (id_chat, id_user);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id_user);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: chat chat_chattheme_id_theme_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat
    ADD CONSTRAINT chat_chattheme_id_theme_fk FOREIGN KEY (id_theme) REFERENCES public.chattheme(id_theme) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: friendship friendship_f1__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_f1__fk FOREIGN KEY (friend_1) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: friendship friendship_f2__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendship
    ADD CONSTRAINT friendship_f2__fk FOREIGN KEY (friend_2) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: friendshiprequest friendshiprequest_ur__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendshiprequest
    ADD CONSTRAINT friendshiprequest_ur__fk FOREIGN KEY (user_receiver) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: friendshiprequest friendshiprequest_us__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.friendshiprequest
    ADD CONSTRAINT friendshiprequest_us__fk FOREIGN KEY (user_sender) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: message message_chat__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_chat__fk FOREIGN KEY (id_chat) REFERENCES public.chat(id_chat) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: message message_from__fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_from__fk FOREIGN KEY (id_from) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: message message_message_id_message_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_message_id_message_fk FOREIGN KEY (id_reply) REFERENCES public.message(id_message) ON UPDATE CASCADE ON DELETE SET DEFAULT;


--
-- Name: messagereceiver messagereceiver_message_id_message_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messagereceiver
    ADD CONSTRAINT messagereceiver_message_id_message_fk FOREIGN KEY (id_message) REFERENCES public.message(id_message) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: messagereceiver messagereceiver_users_id_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messagereceiver
    ADD CONSTRAINT messagereceiver_users_id_user_fk FOREIGN KEY (id_receiver) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: userchats userchats_chat_id_chat_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.userchats
    ADD CONSTRAINT userchats_chat_id_chat_fk FOREIGN KEY (id_chat) REFERENCES public.chat(id_chat) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: userchats userchats_users_id_user_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.userchats
    ADD CONSTRAINT userchats_users_id_user_fk FOREIGN KEY (id_user) REFERENCES public.users(id_user) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--


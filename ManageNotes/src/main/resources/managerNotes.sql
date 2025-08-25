--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Ubuntu 17.5-1.pgdg24.04+1)
-- Dumped by pg_dump version 17.5 (Ubuntu 17.5-1.pgdg24.04+1)

-- Started on 2025-08-25 14:52:49 WAT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
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
-- TOC entry 217 (class 1259 OID 17226)
-- Name: departments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.departments (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    name character varying(100) NOT NULL
);


ALTER TABLE public.departments OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17326)
-- Name: departments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.departments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.departments_seq OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17231)
-- Name: grade_claims; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grade_claims (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    rejection_reason character varying(255),
    resolved_at timestamp(6) without time zone,
    status character varying(255),
    cause text,
    description text,
    period_label character varying(255),
    requested_score double precision NOT NULL,
    teacher_comment text,
    created_by_id bigint,
    grade_id bigint NOT NULL,
    semester_id bigint,
    student_id bigint NOT NULL,
    CONSTRAINT grade_claims_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.grade_claims OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17327)
-- Name: grade_claims_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grade_claims_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grade_claims_seq OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 17239)
-- Name: grade_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grade_report (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    gpa double precision,
    id_semesters bigint,
    id_students bigint,
    pdf_path character varying(255),
    status character varying(255)
);


ALTER TABLE public.grade_report OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17328)
-- Name: grade_report_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grade_report_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grade_report_seq OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17246)
-- Name: grades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grades (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    comments character varying(255),
    max_value double precision,
    period_label character varying(255),
    grade_type character varying(255),
    value double precision,
    id_users bigint,
    id_semester bigint,
    id_students bigint,
    id_subject bigint,
    CONSTRAINT grades_grade_type_check CHECK (((grade_type)::text = ANY ((ARRAY['ASSIGNMENT'::character varying, 'EXAM'::character varying, 'QUIZ'::character varying, 'PROJECT'::character varying, 'CC'::character varying, 'SN'::character varying, 'PRACTICAL'::character varying])::text[])))
);


ALTER TABLE public.grades OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17329)
-- Name: grades_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grades_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grades_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17254)
-- Name: grading_window; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grading_window (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    color character varying(255),
    end_date date,
    is_active boolean,
    name character varying(255),
    order_index integer,
    short_name character varying(255),
    start_date date,
    type character varying(255),
    id_semester bigint,
    period_type smallint,
    CONSTRAINT grading_window_period_type_check CHECK (((period_type >= 0) AND (period_type <= 3))),
    CONSTRAINT grading_window_type_check CHECK (((type)::text = ANY ((ARRAY['CC'::character varying, 'SN'::character varying])::text[])))
);


ALTER TABLE public.grading_window OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17330)
-- Name: grading_window_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grading_window_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grading_window_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17263)
-- Name: invite_token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.invite_token (
    id bigint NOT NULL,
    claimed boolean NOT NULL,
    expires_at timestamp(6) with time zone NOT NULL,
    role character varying(255) NOT NULL,
    token character varying(255) NOT NULL,
    CONSTRAINT invite_token_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'STUDENT'::character varying, 'TEACHER'::character varying])::text[])))
);


ALTER TABLE public.invite_token OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17262)
-- Name: invite_token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.invite_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.invite_token_id_seq OWNER TO postgres;

--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 222
-- Name: invite_token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.invite_token_id_seq OWNED BY public.invite_token.id;


--
-- TOC entry 224 (class 1259 OID 17272)
-- Name: report_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_record (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    academic_year character varying(255),
    class_id bigint,
    download_url character varying(255),
    faculty character varying(255),
    generated_by bigint,
    gpa double precision,
    pdf_path character varying(255),
    report_type character varying(50),
    semester_id bigint,
    status character varying(255),
    student_id bigint,
    subject_id bigint,
    university_name character varying(255)
);


ALTER TABLE public.report_record OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17331)
-- Name: report_record_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.report_record_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.report_record_seq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17279)
-- Name: semesters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.semesters (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    end_date date,
    name character varying(255),
    order_index integer,
    start_date date
);


ALTER TABLE public.semesters OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17332)
-- Name: semesters_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.semesters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.semesters_seq OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17284)
-- Name: student_info_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.student_info_requests (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    created_at timestamp(6) without time zone,
    rejection_reason character varying(255),
    requested_changes_json text NOT NULL,
    status character varying(255),
    student_id bigint NOT NULL,
    CONSTRAINT student_info_requests_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.student_info_requests OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17333)
-- Name: student_info_requests_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_info_requests_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_info_requests_seq OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17292)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    cycle character varying(255),
    date_of_birth date,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    level character varying(255),
    matricule character varying(255),
    place_of_birth character varying(255),
    speciality character varying(255),
    CONSTRAINT students_cycle_check CHECK (((cycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[]))),
    CONSTRAINT students_level_check CHECK (((level)::text = ANY ((ARRAY['LEVEL1'::character varying, 'LEVEL2'::character varying, 'LEVEL3'::character varying, 'LEVEL4'::character varying, 'LEVEL5'::character varying])::text[])))
);


ALTER TABLE public.students OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17334)
-- Name: students_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.students_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.students_seq OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17301)
-- Name: subject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subject (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    code character varying(255),
    credits numeric(38,2),
    cycle character varying(255),
    description character varying(500),
    id_teacher bigint,
    level character varying(255),
    name character varying(255),
    department_id bigint,
    id_semester bigint,
    CONSTRAINT subject_cycle_check CHECK (((cycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[]))),
    CONSTRAINT subject_level_check CHECK (((level)::text = ANY ((ARRAY['LEVEL1'::character varying, 'LEVEL2'::character varying, 'LEVEL3'::character varying, 'LEVEL4'::character varying, 'LEVEL5'::character varying])::text[])))
);


ALTER TABLE public.subject OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 17335)
-- Name: subject_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.subject_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.subject_seq OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 17398)
-- Name: user_levels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_levels (
    user_id bigint NOT NULL,
    level character varying(255)
);


ALTER TABLE public.user_levels OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17310)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    department character varying(100),
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    must_change_password boolean,
    password character varying(255),
    phone character varying(30),
    role character varying(255),
    username character varying(255),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'STUDENT'::character varying, 'TEACHER'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 17336)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO postgres;

--
-- TOC entry 3349 (class 2604 OID 17266)
-- Name: invite_token id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invite_token ALTER COLUMN id SET DEFAULT nextval('public.invite_token_id_seq'::regclass);


--
-- TOC entry 3551 (class 0 OID 17226)
-- Dependencies: 217
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.departments (id, creation_date, last_modified_date, name) FROM stdin;
1	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Computer Science
2	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Mathematics
3	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Physics
4	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Engineering
5	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Business Administration
\.


--
-- TOC entry 3552 (class 0 OID 17231)
-- Dependencies: 218
-- Data for Name: grade_claims; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grade_claims (id, creation_date, last_modified_date, rejection_reason, resolved_at, status, cause, description, period_label, requested_score, teacher_comment, created_by_id, grade_id, semester_id, student_id) FROM stdin;
1	2025-08-25 14:15:05.505976+01	2025-08-25 14:15:05.505976+01	\N	\N	REJECTED	CALCULATION_ERROR	I believe there was an error in my calculus grade calculation.	CC_1	16	After review, the grade is correct.	\N	2	1	1
2	2025-08-25 14:15:05.505976+01	2025-08-25 14:15:05.505976+01	\N	\N	PENDING	MISSING_WORK	I submitted additional programming exercises that were not graded.	CC_1	15	\N	\N	7	1	3
\.


--
-- TOC entry 3553 (class 0 OID 17239)
-- Dependencies: 219
-- Data for Name: grade_report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grade_report (id, creation_date, last_modified_date, gpa, id_semesters, id_students, pdf_path, status) FROM stdin;
\.


--
-- TOC entry 3554 (class 0 OID 17246)
-- Dependencies: 220
-- Data for Name: grades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grades (id, creation_date, last_modified_date, comments, max_value, period_label, grade_type, value, id_users, id_semester, id_students, id_subject) FROM stdin;
1	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good understanding of programming basics	20	CC_1	CC	15.5	2	1	1	1
2	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Needs improvement in calculus	20	CC_1	CC	14	3	1	1	2
3	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Excellent physics understanding	20	CC_1	CC	16	4	1	1	3
4	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Excellent programming skills	20	CC_1	CC	17	2	1	2	1
5	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Outstanding mathematical ability	20	CC_1	CC	18	3	1	2	2
6	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good physics performance	20	CC_1	CC	15.5	4	1	2	3
7	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Basic programming understanding	20	CC_1	CC	13.5	2	1	3	1
8	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good calculus progress	20	CC_1	CC	15	3	1	3	2
9	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Excellent physics aptitude	20	CC_1	CC	17.5	4	1	3	3
10	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good data structures knowledge	20	CC_1	CC	16.5	2	1	4	4
11	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Linear algebra progress	20	CC_1	CC	15	3	1	4	5
12	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Physics concepts understood	20	CC_1	CC	14.5	4	1	4	6
13	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Excellent data structures work	20	CC_1	CC	17.5	2	1	5	4
14	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Outstanding linear algebra	20	CC_1	CC	18	3	1	5	5
15	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good physics understanding	20	CC_1	CC	16	4	1	5	6
16	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Exceptional algorithm skills	20	CC_1	CC	18.5	2	1	7	7
17	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good thermodynamics grasp	20	CC_1	CC	16.5	5	1	7	8
18	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Algorithm concepts developing	20	CC_1	CC	15	2	1	8	7
19	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Strong thermodynamics work	20	CC_1	CC	17	5	1	8	8
20	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Excellent ML understanding	20	CC_1	CC	17	2	1	9	9
21	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Good advanced mechanics	20	CC_1	CC	16	5	1	9	10
22	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	ML concepts progressing	20	CC_1	CC	15.5	2	1	10	9
23	2025-08-25 14:15:05.496981+01	2025-08-25 14:15:05.496981+01	Outstanding mechanics work	20	CC_1	CC	18	5	1	10	10
24	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Final exam performance	20	SN_1	SN	14	2	1	1	1
25	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Calculus final exam	20	SN_1	SN	13.5	3	1	1	2
26	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Physics final exam	20	SN_1	SN	15.5	4	1	1	3
27	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Excellent final exam	20	SN_1	SN	18.5	2	1	2	1
28	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Outstanding math final	20	SN_1	SN	19	3	1	2	2
29	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Good physics final	20	SN_1	SN	16	4	1	2	3
30	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Needs improvement	20	SN_1	SN	12	2	1	3	1
31	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Better calculus final	20	SN_1	SN	14.5	3	1	3	2
32	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Excellent physics final	20	SN_1	SN	18	4	1	3	3
33	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Good data structures final	20	SN_1	SN	15.5	2	1	4	4
34	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Linear algebra final	20	SN_1	SN	14	3	1	4	5
35	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Physics final exam	20	SN_1	SN	13.5	4	1	4	6
36	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Excellent final performance	20	SN_1	SN	18	2	1	5	4
37	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Outstanding math final	20	SN_1	SN	19.5	3	1	5	5
38	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Very good physics final	20	SN_1	SN	17	4	1	5	6
39	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Exceptional algorithms final	20	SN_1	SN	19	2	1	7	7
40	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Good thermodynamics final	20	SN_1	SN	17.5	5	1	7	8
41	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Algorithm final improving	20	SN_1	SN	14.5	2	1	8	7
42	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Excellent thermodynamics final	20	SN_1	SN	18.5	5	1	8	8
43	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Good ML final exam	20	SN_1	SN	17.5	2	1	9	9
44	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Advanced mechanics final	20	SN_1	SN	16.5	5	1	9	10
45	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	ML final exam progress	20	SN_1	SN	16	2	1	10	9
46	2025-08-25 14:32:02.750508+01	2025-08-25 14:32:02.750508+01	Outstanding mechanics final	20	SN_1	SN	19	5	1	10	10
47	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Good OOP understanding	20	CC_2	CC	16	2	2	1	11
48	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Calculus II progress	20	CC_2	CC	15	3	2	1	12
49	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Physics II improvement	20	CC_2	CC	17	4	2	1	13
50	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Excellent OOP skills	20	CC_2	CC	18	2	2	2	11
51	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Outstanding calculus	20	CC_2	CC	19	3	2	2	12
52	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Good physics work	20	CC_2	CC	16.5	4	2	2	13
53	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	OOP concepts developing	20	CC_2	CC	14	2	2	3	11
54	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Better calculus performance	20	CC_2	CC	16	3	2	3	12
55	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Excellent physics aptitude	20	CC_2	CC	18.5	4	2	3	13
56	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Good database knowledge	20	CC_2	CC	17	2	2	4	14
57	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Statistics understanding	20	CC_2	CC	15.5	3	2	4	15
58	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Modern physics concepts	20	CC_2	CC	14	4	2	4	16
59	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Excellent database work	20	CC_2	CC	18.5	2	2	5	14
60	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Outstanding statistics	20	CC_2	CC	19	3	2	5	15
61	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Very good modern physics	20	CC_2	CC	17.5	4	2	5	16
62	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Exceptional software engineering	20	CC_2	CC	19	2	2	7	17
63	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Good fluid mechanics	20	CC_2	CC	17	5	2	7	18
64	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Software engineering progress	20	CC_2	CC	15.5	2	2	8	17
65	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Strong fluid mechanics	20	CC_2	CC	18	5	2	8	18
66	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Excellent deep learning	20	CC_2	CC	17.5	2	2	9	19
67	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Good control systems	20	CC_2	CC	16	5	2	9	20
68	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Deep learning improving	20	CC_2	CC	16.5	2	2	10	19
69	2025-08-25 14:32:02.762729+01	2025-08-25 14:32:02.762729+01	Outstanding control systems	20	CC_2	CC	18.5	5	2	10	20
70	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	OOP final exam	20	SN_2	SN	15	2	2	1	11
71	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Calculus II final	20	SN_2	SN	14	3	2	1	12
72	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Physics II final	20	SN_2	SN	16.5	4	2	1	13
73	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Excellent OOP final	20	SN_2	SN	19	2	2	2	11
74	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Outstanding calculus final	20	SN_2	SN	18.5	3	2	2	12
75	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Good physics final	20	SN_2	SN	17	4	2	2	13
76	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	OOP final needs work	20	SN_2	SN	13	2	2	3	11
77	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Better calculus final	20	SN_2	SN	15.5	3	2	3	12
78	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Excellent physics final	20	SN_2	SN	19	4	2	3	13
79	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Good database final	20	SN_2	SN	16	2	2	4	14
80	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Statistics final exam	20	SN_2	SN	14.5	3	2	4	15
81	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Modern physics final	20	SN_2	SN	13	4	2	4	16
82	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Excellent database final	20	SN_2	SN	19	2	2	5	14
83	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Outstanding statistics final	20	SN_2	SN	18.5	3	2	5	15
84	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Very good physics final	20	SN_2	SN	18	4	2	5	16
85	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Exceptional software final	20	SN_2	SN	18.5	2	2	7	17
86	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Good fluid mechanics final	20	SN_2	SN	17.5	5	2	7	18
87	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Software engineering final	20	SN_2	SN	15	2	2	8	17
88	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Outstanding fluid final	20	SN_2	SN	19	5	2	8	18
89	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Excellent deep learning final	20	SN_2	SN	18	2	2	9	19
90	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Good control systems final	20	SN_2	SN	16.5	5	2	9	20
91	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Deep learning final progress	20	SN_2	SN	17	2	2	10	19
92	2025-08-25 14:32:02.768061+01	2025-08-25 14:32:02.768061+01	Outstanding control final	20	SN_2	SN	19.5	5	2	10	20
\.


--
-- TOC entry 3555 (class 0 OID 17254)
-- Dependencies: 221
-- Data for Name: grading_window; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grading_window (id, creation_date, last_modified_date, color, end_date, is_active, name, order_index, short_name, start_date, type, id_semester, period_type) FROM stdin;
1	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#FF6B6B	2024-02-15	t	Continuous Assessment 1	1	CC_1	2024-02-01	CC	1	0
2	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#45B7D1	2024-06-15	f	Session Normale 1	2	SN_1	2024-05-15	SN	1	2
3	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#4ECDC4	2024-08-15	f	Continuous Assessment 2	3	CC_2	2024-08-01	CC	2	1
4	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#96CEB4	2024-12-15	f	Session Normale 2	4	SN_2	2024-11-15	SN	2	3
\.


--
-- TOC entry 3557 (class 0 OID 17263)
-- Dependencies: 223
-- Data for Name: invite_token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.invite_token (id, claimed, expires_at, role, token) FROM stdin;
\.


--
-- TOC entry 3558 (class 0 OID 17272)
-- Dependencies: 224
-- Data for Name: report_record; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_record (id, creation_date, last_modified_date, academic_year, class_id, download_url, faculty, generated_by, gpa, pdf_path, report_type, semester_id, status, student_id, subject_id, university_name) FROM stdin;
\.


--
-- TOC entry 3559 (class 0 OID 17279)
-- Dependencies: 225
-- Data for Name: semesters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.semesters (id, creation_date, last_modified_date, active, end_date, name, order_index, start_date) FROM stdin;
1	2025-08-25 14:15:05.490253+01	2025-08-25 14:15:05.490253+01	t	2024-06-30	S1 2024	1	2024-01-01
2	2025-08-25 14:15:05.490253+01	2025-08-25 14:15:05.490253+01	f	2024-12-31	S2 2024	2	2024-07-01
\.


--
-- TOC entry 3560 (class 0 OID 17284)
-- Dependencies: 226
-- Data for Name: student_info_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.student_info_requests (id, creation_date, last_modified_date, created_at, rejection_reason, requested_changes_json, status, student_id) FROM stdin;
\.


--
-- TOC entry 3561 (class 0 OID 17292)
-- Dependencies: 227
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (id, creation_date, last_modified_date, cycle, date_of_birth, email, first_name, last_name, level, matricule, place_of_birth, speciality) FROM stdin;
1	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2005-03-15	alice.cooper@student.university.edu	Alice	Cooper	LEVEL1	STU2024001	Yaoundé	Computer Science
2	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2005-07-22	bob.martin@student.university.edu	Bob	Martin	LEVEL1	STU2024002	Douala	Mathematics
3	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2005-01-10	carol.white@student.university.edu	Carol	White	LEVEL1	STU2024003	Bamenda	Physics
4	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2004-04-12	daniel.green@student.university.edu	Daniel	Green	LEVEL2	STU2023001	Yaoundé	Computer Science
5	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2004-08-30	eva.black@student.university.edu	Eva	Black	LEVEL2	STU2023002	Douala	Mathematics
6	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2004-02-14	frank.blue@student.university.edu	Frank	Blue	LEVEL2	STU2023003	Bamenda	Physics
7	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2003-05-20	grace.red@student.university.edu	Grace	Red	LEVEL3	STU2022001	Yaoundé	Computer Science
8	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	BACHELOR	2003-09-15	henry.yellow@student.university.edu	Henry	Yellow	LEVEL3	STU2022002	Douala	Engineering
9	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	MASTER	2002-01-15	iris.purple@student.university.edu	Iris	Purple	LEVEL4	STU2021001	Yaoundé	Computer Science
10	2025-08-25 14:15:05.487861+01	2025-08-25 14:15:05.487861+01	MASTER	2002-05-22	jack.orange@student.university.edu	Jack	Orange	LEVEL4	STU2021002	Douala	Engineering
\.


--
-- TOC entry 3562 (class 0 OID 17301)
-- Dependencies: 228
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subject (id, creation_date, last_modified_date, active, code, credits, cycle, description, id_teacher, level, name, department_id, id_semester) FROM stdin;
1	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS101	4.00	BACHELOR	Basic programming concepts using Python	2	LEVEL1	Introduction to Programming	1	1
2	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	MATH101	3.00	BACHELOR	Differential and integral calculus	3	LEVEL1	Calculus I	2	1
3	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	PHYS101	3.00	BACHELOR	Mechanics and thermodynamics	4	LEVEL1	Physics I	3	1
4	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS201	4.00	BACHELOR	Arrays, linked lists, trees, and graphs	2	LEVEL2	Data Structures	1	1
5	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	MATH201	3.00	BACHELOR	Vectors, matrices, and linear transformations	3	LEVEL2	Linear Algebra	2	1
6	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	PHYS201	3.00	BACHELOR	Electric fields, magnetic fields, and circuits	4	LEVEL2	Electricity and Magnetism	3	1
7	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS301	4.00	BACHELOR	Algorithm design and analysis	2	LEVEL3	Algorithms	1	1
8	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	ENG301	3.00	BACHELOR	Heat transfer and energy systems	5	LEVEL3	Thermodynamics	4	1
9	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS401	4.00	MASTER	Supervised and unsupervised learning algorithms	2	LEVEL4	Machine Learning	1	1
10	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	ENG401	3.00	MASTER	Advanced topics in mechanical engineering	5	LEVEL4	Advanced Mechanics	4	1
11	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS102	4.00	BACHELOR	OOP concepts using Java	2	LEVEL1	Object-Oriented Programming	1	2
12	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	MATH102	3.00	BACHELOR	Advanced calculus and series	3	LEVEL1	Calculus II	2	2
13	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	PHYS102	3.00	BACHELOR	Waves and optics	4	LEVEL1	Physics II	3	2
14	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS202	4.00	BACHELOR	Database design and SQL	2	LEVEL2	Database Systems	1	2
15	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	MATH202	3.00	BACHELOR	Probability and statistics	3	LEVEL2	Statistics	2	2
16	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	PHYS202	3.00	BACHELOR	Quantum and relativity basics	4	LEVEL2	Modern Physics	3	2
17	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS302	4.00	BACHELOR	Software development lifecycle	2	LEVEL3	Software Engineering	1	2
18	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	ENG302	3.00	BACHELOR	Fluid flow and dynamics	5	LEVEL3	Fluid Mechanics	4	2
19	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS402	4.00	MASTER	Neural networks and AI	2	LEVEL4	Deep Learning	1	2
20	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	ENG402	3.00	MASTER	Automatic control theory	5	LEVEL4	Control Systems	4	2
\.


--
-- TOC entry 3575 (class 0 OID 17398)
-- Dependencies: 241
-- Data for Name: user_levels; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_levels (user_id, level) FROM stdin;
2	LEVEL1
2	LEVEL2
2	LEVEL3
2	LEVEL4
3	LEVEL1
3	LEVEL2
4	LEVEL1
4	LEVEL2
5	LEVEL3
5	LEVEL4
\.


--
-- TOC entry 3563 (class 0 OID 17310)
-- Dependencies: 229
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, creation_date, last_modified_date, active, department, email, first_name, last_name, must_change_password, password, phone, role, username) FROM stdin;
1	2025-08-25 14:15:05.478869+01	2025-08-25 14:15:05.478869+01	t	Administration	admin@university.edu	System	Administrator	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	+237123456789	ADMIN	admin
2	2025-08-25 14:15:05.481011+01	2025-08-25 14:15:05.481011+01	t	Computer Science	mjohnson@university.edu	Michael	Johnson	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	+237123456791	TEACHER	prof.johnson
3	2025-08-25 14:15:05.481011+01	2025-08-25 14:15:05.481011+01	t	Mathematics	swilliams@university.edu	Sarah	Williams	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	+237123456792	TEACHER	prof.williams
4	2025-08-25 14:15:05.481011+01	2025-08-25 14:15:05.481011+01	t	Physics	dbrown@university.edu	David	Brown	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	+237123456793	TEACHER	prof.brown
5	2025-08-25 14:15:05.481011+01	2025-08-25 14:15:05.481011+01	t	Engineering	edavis@university.edu	Emily	Davis	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	+237123456794	TEACHER	prof.davis
6	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	alice.cooper@student.university.edu	Alice	Cooper	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	alice.cooper
7	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	bob.martin@student.university.edu	Bob	Martin	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	bob.martin
8	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	carol.white@student.university.edu	Carol	White	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	carol.white
9	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	daniel.green@student.university.edu	Daniel	Green	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	daniel.green
10	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	eva.black@student.university.edu	Eva	Black	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	eva.black
11	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	frank.blue@student.university.edu	Frank	Blue	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	frank.blue
12	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	grace.red@student.university.edu	Grace	Red	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	grace.red
13	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	henry.yellow@student.university.edu	Henry	Yellow	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	henry.yellow
14	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	iris.purple@student.university.edu	Iris	Purple	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	iris.purple
15	2025-08-25 14:33:45.933515+01	2025-08-25 14:33:45.933515+01	t	\N	jack.orange@student.university.edu	Jack	Orange	f	$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.	\N	STUDENT	jack.orange
\.


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 230
-- Name: departments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.departments_seq', 1, false);


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 231
-- Name: grade_claims_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grade_claims_seq', 1, false);


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 232
-- Name: grade_report_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grade_report_seq', 1, false);


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 233
-- Name: grades_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grades_seq', 1, false);


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 234
-- Name: grading_window_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grading_window_seq', 1, false);


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 222
-- Name: invite_token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.invite_token_id_seq', 1, false);


--
-- TOC entry 3588 (class 0 OID 0)
-- Dependencies: 235
-- Name: report_record_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.report_record_seq', 1, false);


--
-- TOC entry 3589 (class 0 OID 0)
-- Dependencies: 236
-- Name: semesters_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.semesters_seq', 1, false);


--
-- TOC entry 3590 (class 0 OID 0)
-- Dependencies: 237
-- Name: student_info_requests_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.student_info_requests_seq', 1, false);


--
-- TOC entry 3591 (class 0 OID 0)
-- Dependencies: 238
-- Name: students_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.students_seq', 651, true);


--
-- TOC entry 3592 (class 0 OID 0)
-- Dependencies: 239
-- Name: subject_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.subject_seq', 1, false);


--
-- TOC entry 3593 (class 0 OID 0)
-- Dependencies: 240
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_seq', 151, true);


--
-- TOC entry 3362 (class 2606 OID 17230)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (id);


--
-- TOC entry 3366 (class 2606 OID 17238)
-- Name: grade_claims grade_claims_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT grade_claims_pkey PRIMARY KEY (id);


--
-- TOC entry 3368 (class 2606 OID 17245)
-- Name: grade_report grade_report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_report
    ADD CONSTRAINT grade_report_pkey PRIMARY KEY (id);


--
-- TOC entry 3370 (class 2606 OID 17253)
-- Name: grades grades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT grades_pkey PRIMARY KEY (id);


--
-- TOC entry 3372 (class 2606 OID 17261)
-- Name: grading_window grading_window_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_window
    ADD CONSTRAINT grading_window_pkey PRIMARY KEY (id);


--
-- TOC entry 3374 (class 2606 OID 17271)
-- Name: invite_token invite_token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invite_token
    ADD CONSTRAINT invite_token_pkey PRIMARY KEY (id);


--
-- TOC entry 3378 (class 2606 OID 17278)
-- Name: report_record report_record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_record
    ADD CONSTRAINT report_record_pkey PRIMARY KEY (id);


--
-- TOC entry 3380 (class 2606 OID 17283)
-- Name: semesters semesters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semesters_pkey PRIMARY KEY (id);


--
-- TOC entry 3382 (class 2606 OID 17291)
-- Name: student_info_requests student_info_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_info_requests
    ADD CONSTRAINT student_info_requests_pkey PRIMARY KEY (id);


--
-- TOC entry 3384 (class 2606 OID 17300)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- TOC entry 3388 (class 2606 OID 17309)
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- TOC entry 3386 (class 2606 OID 17323)
-- Name: students uk_1psraxut9bwn2why6ex4xf1en; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT uk_1psraxut9bwn2why6ex4xf1en UNIQUE (matricule);


--
-- TOC entry 3376 (class 2606 OID 17321)
-- Name: invite_token uk_60k2cybcnd18p0cnf0e5sssb6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.invite_token
    ADD CONSTRAINT uk_60k2cybcnd18p0cnf0e5sssb6 UNIQUE (token);


--
-- TOC entry 3364 (class 2606 OID 17319)
-- Name: departments uk_j6cwks7xecs5jov19ro8ge3qk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT uk_j6cwks7xecs5jov19ro8ge3qk UNIQUE (name);


--
-- TOC entry 3390 (class 2606 OID 17325)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 3392 (class 2606 OID 17317)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3393 (class 2606 OID 17342)
-- Name: grade_claims fk2kbul9dstw4uo15gc5tomfkni; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fk2kbul9dstw4uo15gc5tomfkni FOREIGN KEY (grade_id) REFERENCES public.grades(id);


--
-- TOC entry 3397 (class 2606 OID 17367)
-- Name: grades fk384e8h2qimc9qlnh970ytgdr5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fk384e8h2qimc9qlnh970ytgdr5 FOREIGN KEY (id_students) REFERENCES public.students(id);


--
-- TOC entry 3403 (class 2606 OID 17392)
-- Name: subject fk4b66tj7yip7jmo922vvy6bw4y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT fk4b66tj7yip7jmo922vvy6bw4y FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3394 (class 2606 OID 17337)
-- Name: grade_claims fk8b4k9pw7j018vpks0ns5wmsp0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fk8b4k9pw7j018vpks0ns5wmsp0 FOREIGN KEY (created_by_id) REFERENCES public.users(id);


--
-- TOC entry 3395 (class 2606 OID 17352)
-- Name: grade_claims fkc1rbjpi3wpf5ihn0h81pygtnc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fkc1rbjpi3wpf5ihn0h81pygtnc FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3402 (class 2606 OID 17382)
-- Name: student_info_requests fkfawh6psw2f3yivjbpciph35nh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.student_info_requests
    ADD CONSTRAINT fkfawh6psw2f3yivjbpciph35nh FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3398 (class 2606 OID 17357)
-- Name: grades fkfrqv6jj26ycmq0uqihsmofd9w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkfrqv6jj26ycmq0uqihsmofd9w FOREIGN KEY (id_users) REFERENCES public.users(id);


--
-- TOC entry 3399 (class 2606 OID 17372)
-- Name: grades fkippbjcacm6nkn7hon47r53l99; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkippbjcacm6nkn7hon47r53l99 FOREIGN KEY (id_subject) REFERENCES public.subject(id);


--
-- TOC entry 3401 (class 2606 OID 17377)
-- Name: grading_window fkmkan866y61akk4qim8e74d3dj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_window
    ADD CONSTRAINT fkmkan866y61akk4qim8e74d3dj FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3404 (class 2606 OID 17387)
-- Name: subject fkqym877gemkcwuhmjmbuokvf6f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT fkqym877gemkcwuhmjmbuokvf6f FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- TOC entry 3405 (class 2606 OID 17401)
-- Name: user_levels fkr5aqf5bnqm78uhohv89f203v2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_levels
    ADD CONSTRAINT fkr5aqf5bnqm78uhohv89f203v2 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3400 (class 2606 OID 17362)
-- Name: grades fks0yeww9160sohy3wpgmt7dve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fks0yeww9160sohy3wpgmt7dve FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3396 (class 2606 OID 17347)
-- Name: grade_claims fkteaqiirgxcnndhnx4ljcoc60h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fkteaqiirgxcnndhnx4ljcoc60h FOREIGN KEY (semester_id) REFERENCES public.semesters(id);


-- Completed on 2025-08-25 14:52:49 WAT

--
-- PostgreSQL database dump complete
--


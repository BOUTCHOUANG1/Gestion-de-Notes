--
-- PostgreSQL database dump
--

\restrict iTntDtaZsMxGXSk999iBUjZwJVfwRIjaLwdFqqvT5nLPnKqfGYqhj64ELciBakq

-- Dumped from database version 17.6 (Ubuntu 17.6-1.pgdg24.04+1)
-- Dumped by pg_dump version 17.6 (Ubuntu 17.6-1.pgdg24.04+1)

-- Started on 2025-10-06 11:08:49 WAT

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
-- TOC entry 218 (class 1259 OID 60246)
-- Name: departments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.departments (
    department_id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    name character varying(255),
    last_modified_date timestamp(6) with time zone
);


ALTER TABLE public.departments OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 60245)
-- Name: departments_department_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.departments_department_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.departments_department_id_seq OWNER TO postgres;

--
-- TOC entry 3611 (class 0 OID 0)
-- Dependencies: 217
-- Name: departments_department_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.departments_department_id_seq OWNED BY public.departments.department_id;


--
-- TOC entry 220 (class 1259 OID 60253)
-- Name: exam_periods; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exam_periods (
    exam_period_id bigint NOT NULL,
    assessment_type character varying(20),
    CONSTRAINT exam_periods_assessment_type_check CHECK (((assessment_type)::text = ANY ((ARRAY['CC_1'::character varying, 'CC_2'::character varying, 'SN_1'::character varying, 'SN_2'::character varying])::text[])))
);


ALTER TABLE public.exam_periods OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 60252)
-- Name: exam_periods_exam_period_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.exam_periods_exam_period_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.exam_periods_exam_period_id_seq OWNER TO postgres;

--
-- TOC entry 3612 (class 0 OID 0)
-- Dependencies: 219
-- Name: exam_periods_exam_period_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.exam_periods_exam_period_id_seq OWNED BY public.exam_periods.exam_period_id;


--
-- TOC entry 222 (class 1259 OID 60261)
-- Name: grades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grades (
    grade_id bigint NOT NULL,
    cc_score double precision,
    comments character varying(255),
    creation_date timestamp(6) with time zone NOT NULL,
    gpa double precision,
    has_passed boolean,
    last_modified_date timestamp(6) with time zone,
    sn_score double precision,
    total_score double precision,
    exam_id bigint,
    teacher_id bigint,
    semester_id bigint,
    student_id bigint,
    subject_id bigint
);


ALTER TABLE public.grades OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 60260)
-- Name: grades_grade_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grades_grade_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grades_grade_id_seq OWNER TO postgres;

--
-- TOC entry 3613 (class 0 OID 0)
-- Dependencies: 221
-- Name: grades_grade_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.grades_grade_id_seq OWNED BY public.grades.grade_id;


--
-- TOC entry 224 (class 1259 OID 60268)
-- Name: revendication; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.revendication (
    revendication_id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    description text,
    last_modified_date timestamp(6) with time zone,
    requested_score double precision NOT NULL,
    status character varying(255),
    teacher_comment text,
    grade_id bigint,
    period_id bigint,
    semester_id bigint,
    student_id bigint,
    CONSTRAINT revendication_requested_score_check CHECK ((requested_score >= (0)::double precision)),
    CONSTRAINT revendication_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.revendication OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 60279)
-- Name: revendication_period; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.revendication_period (
    revendication_period_id bigint NOT NULL,
    color character varying(255),
    creation_date timestamp(6) with time zone NOT NULL,
    end_date date NOT NULL,
    is_active boolean,
    last_modified_date timestamp(6) with time zone,
    start_date date NOT NULL,
    exam_period_id bigint,
    semester_id bigint
);


ALTER TABLE public.revendication_period OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 60278)
-- Name: revendication_period_revendication_period_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.revendication_period_revendication_period_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.revendication_period_revendication_period_id_seq OWNER TO postgres;

--
-- TOC entry 3614 (class 0 OID 0)
-- Dependencies: 225
-- Name: revendication_period_revendication_period_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.revendication_period_revendication_period_id_seq OWNED BY public.revendication_period.revendication_period_id;


--
-- TOC entry 223 (class 1259 OID 60267)
-- Name: revendication_revendication_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.revendication_revendication_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.revendication_revendication_id_seq OWNER TO postgres;

--
-- TOC entry 3615 (class 0 OID 0)
-- Dependencies: 223
-- Name: revendication_revendication_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.revendication_revendication_id_seq OWNED BY public.revendication.revendication_id;


--
-- TOC entry 228 (class 1259 OID 60286)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    role_id bigint NOT NULL,
    role_name character varying(20),
    CONSTRAINT roles_role_name_check CHECK (((role_name)::text = ANY ((ARRAY['ADMIN'::character varying, 'STUDENT'::character varying, 'TEACHER'::character varying])::text[])))
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 60285)
-- Name: roles_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_role_id_seq OWNER TO postgres;

--
-- TOC entry 3616 (class 0 OID 0)
-- Dependencies: 227
-- Name: roles_role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_role_id_seq OWNED BY public.roles.role_id;


--
-- TOC entry 230 (class 1259 OID 60294)
-- Name: semester; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.semester (
    semester_id bigint NOT NULL,
    is_active boolean,
    creation_date timestamp(6) with time zone NOT NULL,
    end_date date,
    last_modified_date timestamp(6) with time zone,
    name character varying(255),
    start_date date
);


ALTER TABLE public.semester OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 60293)
-- Name: semester_semester_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.semester_semester_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.semester_semester_id_seq OWNER TO postgres;

--
-- TOC entry 3617 (class 0 OID 0)
-- Dependencies: 229
-- Name: semester_semester_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.semester_semester_id_seq OWNED BY public.semester.semester_id;


--
-- TOC entry 231 (class 1259 OID 60300)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    cycle character varying(255),
    date_of_birth date,
    matricule character varying(255),
    place_of_birth character varying(255),
    speciality character varying(255),
    id bigint NOT NULL,
    level_id bigint,
    CONSTRAINT students_cycle_check CHECK (((cycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[])))
);


ALTER TABLE public.students OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 60309)
-- Name: subjects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subjects (
    subject_id bigint NOT NULL,
    studentcycle character varying(255),
    credits numeric(38,2),
    description character varying(500),
    subject_code character varying(255),
    subject_name character varying(255),
    department_id bigint,
    semester_id bigint,
    teaching_level_id bigint,
    teacher_id bigint,
    transcript_id bigint,
    CONSTRAINT subjects_studentcycle_check CHECK (((studentcycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[])))
);


ALTER TABLE public.subjects OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 60308)
-- Name: subjects_subject_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.subjects_subject_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.subjects_subject_id_seq OWNER TO postgres;

--
-- TOC entry 3618 (class 0 OID 0)
-- Dependencies: 232
-- Name: subjects_subject_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.subjects_subject_id_seq OWNED BY public.subjects.subject_id;


--
-- TOC entry 234 (class 1259 OID 60318)
-- Name: teachers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teachers (
    phone_number character varying(9),
    id bigint NOT NULL,
    department_id bigint
);


ALTER TABLE public.teachers OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 60324)
-- Name: teaching_levels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teaching_levels (
    teaching_level_id bigint NOT NULL,
    student_level character varying(20),
    teacher_id bigint,
    CONSTRAINT teaching_levels_student_level_check CHECK (((student_level)::text = ANY ((ARRAY['LEVEL1'::character varying, 'LEVEL2'::character varying, 'LEVEL3'::character varying, 'LEVEL4'::character varying, 'LEVEL5'::character varying])::text[])))
);


ALTER TABLE public.teaching_levels OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 60323)
-- Name: teaching_levels_teaching_level_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.teaching_levels_teaching_level_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.teaching_levels_teaching_level_id_seq OWNER TO postgres;

--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 235
-- Name: teaching_levels_teaching_level_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.teaching_levels_teaching_level_id_seq OWNED BY public.teaching_levels.teaching_level_id;


--
-- TOC entry 238 (class 1259 OID 60332)
-- Name: transcript; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transcript (
    transcript_id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    gpa double precision,
    last_modified_date timestamp(6) with time zone,
    status character varying(255),
    semester_id bigint,
    student_id bigint,
    CONSTRAINT transcript_status_check CHECK (((status)::text = ANY ((ARRAY['PASSED'::character varying, 'FAILED'::character varying])::text[])))
);


ALTER TABLE public.transcript OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 60331)
-- Name: transcript_transcript_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transcript_transcript_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.transcript_transcript_id_seq OWNER TO postgres;

--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 237
-- Name: transcript_transcript_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transcript_transcript_id_seq OWNED BY public.transcript.transcript_id;


--
-- TOC entry 240 (class 1259 OID 60340)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    email character varying(255),
    first_name character varying(255),
    is_active boolean,
    last_modified_date timestamp(6) with time zone,
    last_name character varying(255),
    must_change_password boolean,
    password character varying(255),
    username character varying(255),
    role_id bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 60339)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 239
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 3348 (class 2604 OID 60249)
-- Name: departments department_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments ALTER COLUMN department_id SET DEFAULT nextval('public.departments_department_id_seq'::regclass);


--
-- TOC entry 3349 (class 2604 OID 60256)
-- Name: exam_periods exam_period_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam_periods ALTER COLUMN exam_period_id SET DEFAULT nextval('public.exam_periods_exam_period_id_seq'::regclass);


--
-- TOC entry 3350 (class 2604 OID 60264)
-- Name: grades grade_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades ALTER COLUMN grade_id SET DEFAULT nextval('public.grades_grade_id_seq'::regclass);


--
-- TOC entry 3351 (class 2604 OID 60271)
-- Name: revendication revendication_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication ALTER COLUMN revendication_id SET DEFAULT nextval('public.revendication_revendication_id_seq'::regclass);


--
-- TOC entry 3352 (class 2604 OID 60282)
-- Name: revendication_period revendication_period_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication_period ALTER COLUMN revendication_period_id SET DEFAULT nextval('public.revendication_period_revendication_period_id_seq'::regclass);


--
-- TOC entry 3353 (class 2604 OID 60289)
-- Name: roles role_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN role_id SET DEFAULT nextval('public.roles_role_id_seq'::regclass);


--
-- TOC entry 3354 (class 2604 OID 60297)
-- Name: semester semester_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semester ALTER COLUMN semester_id SET DEFAULT nextval('public.semester_semester_id_seq'::regclass);


--
-- TOC entry 3355 (class 2604 OID 60312)
-- Name: subjects subject_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects ALTER COLUMN subject_id SET DEFAULT nextval('public.subjects_subject_id_seq'::regclass);


--
-- TOC entry 3356 (class 2604 OID 60327)
-- Name: teaching_levels teaching_level_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching_levels ALTER COLUMN teaching_level_id SET DEFAULT nextval('public.teaching_levels_teaching_level_id_seq'::regclass);


--
-- TOC entry 3357 (class 2604 OID 60335)
-- Name: transcript transcript_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transcript ALTER COLUMN transcript_id SET DEFAULT nextval('public.transcript_transcript_id_seq'::regclass);


--
-- TOC entry 3358 (class 2604 OID 60343)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 3583 (class 0 OID 60246)
-- Dependencies: 218
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.departments (department_id, creation_date, name, last_modified_date) FROM stdin;
1	2025-10-01 10:34:53.972874+01	Computer Science	2025-10-01 10:34:53.972877+01
2	2025-10-01 10:34:53.979918+01	Mathematics	2025-10-01 10:34:53.979919+01
3	2025-10-01 10:34:53.985708+01	Physics	2025-10-01 10:34:53.98571+01
4	2025-10-01 10:34:53.991112+01	Business Administration	2025-10-01 10:34:53.991113+01
5	2025-10-01 10:34:53.997224+01	Engineering	2025-10-01 10:34:53.997225+01
\.


--
-- TOC entry 3585 (class 0 OID 60253)
-- Dependencies: 220
-- Data for Name: exam_periods; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exam_periods (exam_period_id, assessment_type) FROM stdin;
1	CC_1
2	CC_2
3	SN_1
4	SN_2
\.


--
-- TOC entry 3587 (class 0 OID 60261)
-- Dependencies: 222
-- Data for Name: grades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grades (grade_id, cc_score, comments, creation_date, gpa, has_passed, last_modified_date, sn_score, total_score, exam_id, teacher_id, semester_id, student_id, subject_id) FROM stdin;
1	2.574836626922463	Requires significant improvement	2025-10-01 10:35:02.820855+01	0	f	2025-10-01 10:35:02.820857+01	0	2.574836626922463	1	2	1	18	1
2	16.492092534357855	Needs improvement	2025-10-01 10:35:02.828516+01	0	f	2025-10-01 10:35:02.828527+01	0	16.492092534357855	2	2	1	18	1
3	0	Requires significant improvement	2025-10-01 10:35:02.834526+01	0	f	2025-10-01 10:35:02.834527+01	14.863082010761222	14.863082010761222	3	2	1	18	1
4	0	Excellent work!	2025-10-01 10:35:02.840224+01	2.3	t	2025-10-01 10:35:02.840224+01	68.57303859124568	68.57303859124568	4	2	1	18	1
5	17.90903950481876	Needs improvement	2025-10-01 10:35:02.845014+01	0	f	2025-10-01 10:35:02.845015+01	0	17.90903950481876	1	2	2	18	1
6	28.92530323413853	Excellent work!	2025-10-01 10:35:02.850687+01	0	f	2025-10-01 10:35:02.850687+01	0	28.92530323413853	2	2	2	18	1
7	0	Needs improvement	2025-10-01 10:35:02.864219+01	0	f	2025-10-01 10:35:02.864219+01	37.316688628859346	37.316688628859346	3	2	2	18	1
8	0	Requires significant improvement	2025-10-01 10:35:02.870494+01	0	f	2025-10-01 10:35:02.870495+01	26.8074645087863	26.8074645087863	4	2	2	18	1
9	21.601330804048917	Good performance	2025-10-01 10:35:02.875789+01	0	f	2025-10-01 10:35:02.875789+01	0	21.601330804048917	1	6	1	18	6
10	17.79522827082888	Needs improvement	2025-10-01 10:35:02.880654+01	0	f	2025-10-01 10:35:02.880654+01	0	17.79522827082888	2	6	1	18	6
11	0	Satisfactory	2025-10-01 10:35:02.885608+01	1	f	2025-10-01 10:35:02.885609+01	47.41340137232599	47.41340137232599	3	6	1	18	6
12	0	Excellent work!	2025-10-01 10:35:02.892782+01	2	t	2025-10-01 10:35:02.892783+01	63.546441147703405	63.546441147703405	4	6	1	18	6
13	20.73410453668382	Satisfactory	2025-10-01 10:35:02.900304+01	0	f	2025-10-01 10:35:02.900304+01	0	20.73410453668382	1	6	2	18	6
14	25.00494654842246	Good performance	2025-10-01 10:35:02.91325+01	0	f	2025-10-01 10:35:02.913251+01	0	25.00494654842246	2	6	2	18	6
15	0	Good performance	2025-10-01 10:35:02.921594+01	1.7	t	2025-10-01 10:35:02.921595+01	57.54261421513771	57.54261421513771	3	6	2	18	6
16	0	Satisfactory	2025-10-01 10:35:02.934806+01	1	f	2025-10-01 10:35:02.934807+01	47.743544839061606	47.743544839061606	4	6	2	18	6
17	18.418074308453225	Satisfactory	2025-10-01 10:35:02.948099+01	0	f	2025-10-01 10:35:02.948099+01	0	18.418074308453225	1	9	1	18	11
18	15.177436472202388	Needs improvement	2025-10-01 10:35:02.955509+01	0	f	2025-10-01 10:35:02.95551+01	0	15.177436472202388	2	9	1	18	11
19	0	Needs improvement	2025-10-01 10:35:02.964611+01	0	f	2025-10-01 10:35:02.964612+01	37.7882984401019	37.7882984401019	3	9	1	18	11
20	0	Requires significant improvement	2025-10-01 10:35:02.974556+01	0	f	2025-10-01 10:35:02.974558+01	24.324859777420272	24.324859777420272	4	9	1	18	11
21	18.08080784280438	Satisfactory	2025-10-01 10:35:02.983348+01	0	f	2025-10-01 10:35:02.983349+01	0	18.08080784280438	1	9	2	18	11
22	24.30311963245847	Good performance	2025-10-01 10:35:02.990241+01	0	f	2025-10-01 10:35:02.990241+01	0	24.30311963245847	2	9	2	18	11
23	0	Good performance	2025-10-01 10:35:02.995626+01	1.3	t	2025-10-01 10:35:02.995627+01	53.599070116480746	53.599070116480746	3	9	2	18	11
24	0	Excellent work!	2025-10-01 10:35:03.005472+01	2.3	t	2025-10-01 10:35:03.005474+01	65.87502882025224	65.87502882025224	4	9	2	18	11
25	21.233773619473432	Good performance	2025-10-01 10:35:03.012107+01	0	f	2025-10-01 10:35:03.012108+01	0	21.233773619473432	1	12	1	18	16
26	24.93664652409086	Good performance	2025-10-01 10:35:03.018378+01	0	f	2025-10-01 10:35:03.018378+01	0	24.93664652409086	2	12	1	18	16
27	0	Excellent work!	2025-10-01 10:35:03.023089+01	2.3	t	2025-10-01 10:35:03.02309+01	65.55050453496746	65.55050453496746	3	12	1	18	16
28	0	Good performance	2025-10-01 10:35:03.027729+01	1	f	2025-10-01 10:35:03.027729+01	49.45327380526098	49.45327380526098	4	12	1	18	16
29	4.5059947755894045	Requires significant improvement	2025-10-01 10:35:03.032209+01	0	f	2025-10-01 10:35:03.032209+01	0	4.5059947755894045	1	12	2	18	16
30	25.44111656156317	Good performance	2025-10-01 10:35:03.037245+01	0	f	2025-10-01 10:35:03.037245+01	0	25.44111656156317	2	12	2	18	16
31	0	Needs improvement	2025-10-01 10:35:03.041701+01	0	f	2025-10-01 10:35:03.041701+01	39.16171717145949	39.16171717145949	3	12	2	18	16
32	0	Excellent work!	2025-10-01 10:35:03.046464+01	2	t	2025-10-01 10:35:03.046464+01	63.7241845166802	63.7241845166802	4	12	2	18	16
33	10.024865134163582	Requires significant improvement	2025-10-01 10:35:03.051557+01	0	f	2025-10-01 10:35:03.051558+01	0	10.024865134163582	1	15	1	18	21
34	27.444531587674806	Excellent work!	2025-10-01 10:35:03.056129+01	0	f	2025-10-01 10:35:03.056129+01	0	27.444531587674806	2	15	1	18	21
35	0	Excellent work!	2025-10-01 10:35:03.06119+01	2	t	2025-10-01 10:35:03.061191+01	61.34747027576073	61.34747027576073	3	15	1	18	21
36	0	Requires significant improvement	2025-10-01 10:35:03.065403+01	0	f	2025-10-01 10:35:03.065403+01	6.275214330565647	6.275214330565647	4	15	1	18	21
37	27.59636045496288	Excellent work!	2025-10-01 10:35:03.070874+01	0	f	2025-10-01 10:35:03.070875+01	0	27.59636045496288	1	15	2	18	21
38	6.125734025911141	Requires significant improvement	2025-10-01 10:35:03.075509+01	0	f	2025-10-01 10:35:03.075509+01	0	6.125734025911141	2	15	2	18	21
39	0	Good performance	2025-10-01 10:35:03.079784+01	1.7	t	2025-10-01 10:35:03.079785+01	57.478299529622745	57.478299529622745	3	15	2	18	21
40	0	Excellent work!	2025-10-01 10:35:03.084463+01	2	t	2025-10-01 10:35:03.084464+01	63.31036143902202	63.31036143902202	4	15	2	18	21
41	26.77129899885106	Excellent work!	2025-10-01 10:35:03.089017+01	0	f	2025-10-01 10:35:03.089018+01	0	26.77129899885106	1	2	1	19	1
42	29.132791135890095	Excellent work!	2025-10-01 10:35:03.093429+01	0	f	2025-10-01 10:35:03.093429+01	0	29.132791135890095	2	2	1	19	1
43	0	Excellent work!	2025-10-01 10:35:03.09819+01	2	t	2025-10-01 10:35:03.098191+01	62.452954091730746	62.452954091730746	3	2	1	19	1
44	0	Needs improvement	2025-10-01 10:35:03.104387+01	0	f	2025-10-01 10:35:03.104388+01	38.027812609288155	38.027812609288155	4	2	1	19	1
45	16.50025926713972	Needs improvement	2025-10-01 10:35:03.109304+01	0	f	2025-10-01 10:35:03.109305+01	0	16.50025926713972	1	2	2	19	1
46	6.624763699274695	Requires significant improvement	2025-10-01 10:35:03.114547+01	0	f	2025-10-01 10:35:03.114548+01	0	6.624763699274695	2	2	2	19	1
47	0	Satisfactory	2025-10-01 10:35:03.1192+01	1	f	2025-10-01 10:35:03.119201+01	45.51272695333	45.51272695333	3	2	2	19	1
48	0	Satisfactory	2025-10-01 10:35:03.124127+01	1	f	2025-10-01 10:35:03.124127+01	48.2915295450644	48.2915295450644	4	2	2	19	1
49	15.184366704717634	Needs improvement	2025-10-01 10:35:03.129183+01	0	f	2025-10-01 10:35:03.129184+01	0	15.184366704717634	1	6	1	19	6
50	29.2613391390989	Excellent work!	2025-10-01 10:35:03.135275+01	0	f	2025-10-01 10:35:03.135276+01	0	29.2613391390989	2	6	1	19	6
51	0	Requires significant improvement	2025-10-01 10:35:03.142971+01	0	f	2025-10-01 10:35:03.142973+01	19.76347950659191	19.76347950659191	3	6	1	19	6
52	0	Requires significant improvement	2025-10-01 10:35:03.147436+01	0	f	2025-10-01 10:35:03.147436+01	33.7722884910078	33.7722884910078	4	6	1	19	6
53	7.193727517532679	Requires significant improvement	2025-10-01 10:35:03.152008+01	0	f	2025-10-01 10:35:03.152009+01	0	7.193727517532679	1	6	2	19	6
54	2.2125451300974355	Requires significant improvement	2025-10-01 10:35:03.156991+01	0	f	2025-10-01 10:35:03.156992+01	0	2.2125451300974355	2	6	2	19	6
55	0	Requires significant improvement	2025-10-01 10:35:03.161477+01	0	f	2025-10-01 10:35:03.161477+01	6.36538898182449	6.36538898182449	3	6	2	19	6
56	0	Excellent work!	2025-10-01 10:35:03.166929+01	2	t	2025-10-01 10:35:03.16693+01	64.95450115959457	64.95450115959457	4	6	2	19	6
57	25.426570666166196	Good performance	2025-10-01 10:35:03.17282+01	0	f	2025-10-01 10:35:03.172821+01	0	25.426570666166196	1	9	1	19	11
58	26.77911059628633	Excellent work!	2025-10-01 10:35:03.177005+01	0	f	2025-10-01 10:35:03.177005+01	0	26.77911059628633	2	9	1	19	11
59	0	Excellent work!	2025-10-01 10:35:03.181536+01	2	t	2025-10-01 10:35:03.181536+01	62.27633560598191	62.27633560598191	3	9	1	19	11
60	0	Satisfactory	2025-10-01 10:35:03.186556+01	1	f	2025-10-01 10:35:03.186556+01	45.34197918411745	45.34197918411745	4	9	1	19	11
61	29.187509074475102	Excellent work!	2025-10-01 10:35:03.191193+01	0	f	2025-10-01 10:35:03.191193+01	0	29.187509074475102	1	9	2	19	11
62	13.841219358298257	Requires significant improvement	2025-10-01 10:35:03.195495+01	0	f	2025-10-01 10:35:03.195496+01	0	13.841219358298257	2	9	2	19	11
63	0	Good performance	2025-10-01 10:35:03.200029+01	1	f	2025-10-01 10:35:03.20003+01	49.52044589311243	49.52044589311243	3	9	2	19	11
64	0	Excellent work!	2025-10-01 10:35:03.205762+01	2.3	t	2025-10-01 10:35:03.205762+01	68.09049335830923	68.09049335830923	4	9	2	19	11
65	27.25522283134972	Excellent work!	2025-10-01 10:35:03.210698+01	0	f	2025-10-01 10:35:03.210699+01	0	27.25522283134972	1	12	1	19	16
66	20.03118147255489	Satisfactory	2025-10-01 10:35:03.216836+01	0	f	2025-10-01 10:35:03.216837+01	0	20.03118147255489	2	12	1	19	16
67	0	Requires significant improvement	2025-10-01 10:35:03.221814+01	0	f	2025-10-01 10:35:03.221815+01	9.85661310555021	9.85661310555021	3	12	1	19	16
68	0	Excellent work!	2025-10-01 10:35:03.226577+01	2	t	2025-10-01 10:35:03.226578+01	60.110798966317745	60.110798966317745	4	12	1	19	16
69	6.5371038894203695	Requires significant improvement	2025-10-01 10:35:03.231275+01	0	f	2025-10-01 10:35:03.231276+01	0	6.5371038894203695	1	12	2	19	16
70	17.473813997097515	Needs improvement	2025-10-01 10:35:03.237973+01	0	f	2025-10-01 10:35:03.237974+01	0	17.473813997097515	2	12	2	19	16
71	0	Satisfactory	2025-10-01 10:35:03.243697+01	1	f	2025-10-01 10:35:03.243697+01	47.316289377899764	47.316289377899764	3	12	2	19	16
72	0	Excellent work!	2025-10-01 10:35:03.249798+01	2	t	2025-10-01 10:35:03.249798+01	64.38234672078663	64.38234672078663	4	12	2	19	16
73	23.840123723616014	Good performance	2025-10-01 10:35:03.255403+01	0	f	2025-10-01 10:35:03.255403+01	0	23.840123723616014	1	15	1	19	21
74	28.701651601611438	Excellent work!	2025-10-01 10:35:03.260167+01	0	f	2025-10-01 10:35:03.260168+01	0	28.701651601611438	2	15	1	19	21
75	0	Satisfactory	2025-10-01 10:35:03.265275+01	1	f	2025-10-01 10:35:03.265276+01	47.14938170563106	47.14938170563106	3	15	1	19	21
76	0	Satisfactory	2025-10-01 10:35:03.270706+01	0	f	2025-10-01 10:35:03.270707+01	44.551527763427046	44.551527763427046	4	15	1	19	21
77	25.13773523490228	Good performance	2025-10-01 10:35:03.275809+01	0	f	2025-10-01 10:35:03.275809+01	0	25.13773523490228	1	15	2	19	21
78	29.080886203493552	Excellent work!	2025-10-01 10:35:03.280504+01	0	f	2025-10-01 10:35:03.280505+01	0	29.080886203493552	2	15	2	19	21
79	0	Excellent work!	2025-10-01 10:35:03.285253+01	2	t	2025-10-01 10:35:03.285253+01	61.89872944792055	61.89872944792055	3	15	2	19	21
80	0	Good performance	2025-10-01 10:35:03.289894+01	1.7	t	2025-10-01 10:35:03.289894+01	58.957252498368106	58.957252498368106	4	15	2	19	21
81	15.546253688921833	Needs improvement	2025-10-01 10:35:03.294248+01	0	f	2025-10-01 10:35:03.294248+01	0	15.546253688921833	1	2	1	20	1
82	29.11830569267453	Excellent work!	2025-10-01 10:35:03.298492+01	0	f	2025-10-01 10:35:03.298492+01	0	29.11830569267453	2	2	1	20	1
83	0	Excellent work!	2025-10-01 10:35:03.303358+01	2.3	t	2025-10-01 10:35:03.303359+01	67.60595095289396	67.60595095289396	3	2	1	20	1
84	0	Needs improvement	2025-10-01 10:35:03.308273+01	0	f	2025-10-01 10:35:03.308275+01	36.022654190797645	36.022654190797645	4	2	1	20	1
85	25.74078683955451	Excellent work!	2025-10-01 10:35:03.313038+01	0	f	2025-10-01 10:35:03.313038+01	0	25.74078683955451	1	2	2	20	1
86	21.578106599505034	Good performance	2025-10-01 10:35:03.318471+01	0	f	2025-10-01 10:35:03.318472+01	0	21.578106599505034	2	2	2	20	1
87	0	Excellent work!	2025-10-01 10:35:03.323734+01	2.3	t	2025-10-01 10:35:03.323735+01	68.01504768782343	68.01504768782343	3	2	2	20	1
88	0	Excellent work!	2025-10-01 10:35:03.330592+01	2	t	2025-10-01 10:35:03.330593+01	64.03132907752055	64.03132907752055	4	2	2	20	1
89	26.550281409182677	Excellent work!	2025-10-01 10:35:03.337018+01	0	f	2025-10-01 10:35:03.337019+01	0	26.550281409182677	1	6	1	20	6
90	17.968407252147763	Needs improvement	2025-10-01 10:35:03.341802+01	0	f	2025-10-01 10:35:03.341803+01	0	17.968407252147763	2	6	1	20	6
91	0	Requires significant improvement	2025-10-01 10:35:03.34606+01	0	f	2025-10-01 10:35:03.34606+01	9.382085076847973	9.382085076847973	3	6	1	20	6
92	0	Excellent work!	2025-10-01 10:35:03.350435+01	2.3	t	2025-10-01 10:35:03.350436+01	65.55176317939048	65.55176317939048	4	6	1	20	6
93	28.523803177393436	Excellent work!	2025-10-01 10:35:03.355419+01	0	f	2025-10-01 10:35:03.355419+01	0	28.523803177393436	1	6	2	20	6
94	24.197772761345117	Good performance	2025-10-01 10:35:03.360656+01	0	f	2025-10-01 10:35:03.360657+01	0	24.197772761345117	2	6	2	20	6
95	0	Requires significant improvement	2025-10-01 10:35:03.367662+01	0	f	2025-10-01 10:35:03.367663+01	30.796261093551475	30.796261093551475	3	6	2	20	6
96	0	Requires significant improvement	2025-10-01 10:35:03.375295+01	0	f	2025-10-01 10:35:03.375296+01	20.59227013733018	20.59227013733018	4	6	2	20	6
97	16.877959677260026	Needs improvement	2025-10-01 10:35:03.379819+01	0	f	2025-10-01 10:35:03.37982+01	0	16.877959677260026	1	9	1	20	11
98	10.61128840286847	Requires significant improvement	2025-10-01 10:35:03.384118+01	0	f	2025-10-01 10:35:03.384119+01	0	10.61128840286847	2	9	1	20	11
99	0	Excellent work!	2025-10-01 10:35:03.388884+01	2	t	2025-10-01 10:35:03.388885+01	60.70885606882048	60.70885606882048	3	9	1	20	11
100	0	Good performance	2025-10-01 10:35:03.394792+01	1.3	t	2025-10-01 10:35:03.394792+01	54.6205383323544	54.6205383323544	4	9	1	20	11
101	26.753134481776044	Excellent work!	2025-10-01 10:35:03.399202+01	0	f	2025-10-01 10:35:03.399202+01	0	26.753134481776044	1	9	2	20	11
102	17.676214716486005	Needs improvement	2025-10-01 10:35:03.404566+01	0	f	2025-10-01 10:35:03.404566+01	0	17.676214716486005	2	9	2	20	11
103	0	Requires significant improvement	2025-10-01 10:35:03.409148+01	0	f	2025-10-01 10:35:03.409149+01	31.06996169452672	31.06996169452672	3	9	2	20	11
104	0	Satisfactory	2025-10-01 10:35:03.413356+01	1	f	2025-10-01 10:35:03.413356+01	48.83220326252536	48.83220326252536	4	9	2	20	11
105	29.238379013037253	Excellent work!	2025-10-01 10:35:03.417642+01	0	f	2025-10-01 10:35:03.417642+01	0	29.238379013037253	1	12	1	20	16
106	20.220229013096997	Satisfactory	2025-10-01 10:35:03.422038+01	0	f	2025-10-01 10:35:03.422038+01	0	20.220229013096997	2	12	1	20	16
107	0	Good performance	2025-10-01 10:35:03.426233+01	1.7	t	2025-10-01 10:35:03.426234+01	59.003706884512695	59.003706884512695	3	12	1	20	16
108	0	Satisfactory	2025-10-01 10:35:03.430391+01	0	f	2025-10-01 10:35:03.430391+01	43.91692295195368	43.91692295195368	4	12	1	20	16
109	23.15894517926911	Good performance	2025-10-01 10:35:03.434635+01	0	f	2025-10-01 10:35:03.434636+01	0	23.15894517926911	1	12	2	20	16
110	1.3242930409051656	Requires significant improvement	2025-10-01 10:35:03.439367+01	0	f	2025-10-01 10:35:03.439367+01	0	1.3242930409051656	2	12	2	20	16
111	0	Excellent work!	2025-10-01 10:35:03.443618+01	2.3	t	2025-10-01 10:35:03.443618+01	68.3771749712245	68.3771749712245	3	12	2	20	16
112	0	Requires significant improvement	2025-10-01 10:35:03.447849+01	0	f	2025-10-01 10:35:03.44785+01	15.395228541259385	15.395228541259385	4	12	2	20	16
113	29.619140809291288	Excellent work!	2025-10-01 10:35:03.45242+01	0	f	2025-10-01 10:35:03.45242+01	0	29.619140809291288	1	15	1	20	21
114	8.131285437371078	Requires significant improvement	2025-10-01 10:35:03.457298+01	0	f	2025-10-01 10:35:03.457298+01	0	8.131285437371078	2	15	1	20	21
115	0	Requires significant improvement	2025-10-01 10:35:03.461932+01	0	f	2025-10-01 10:35:03.461933+01	8.143014128980118	8.143014128980118	3	15	1	20	21
116	0	Requires significant improvement	2025-10-01 10:35:03.466464+01	0	f	2025-10-01 10:35:03.466464+01	24.291640435642385	24.291640435642385	4	15	1	20	21
117	20.136157168724615	Satisfactory	2025-10-01 10:35:03.471729+01	0	f	2025-10-01 10:35:03.47173+01	0	20.136157168724615	1	15	2	20	21
118	27.21130991385619	Excellent work!	2025-10-01 10:35:03.476159+01	0	f	2025-10-01 10:35:03.476159+01	0	27.21130991385619	2	15	2	20	21
119	0	Needs improvement	2025-10-01 10:35:03.480647+01	0	f	2025-10-01 10:35:03.480647+01	39.40438747034078	39.40438747034078	3	15	2	20	21
120	0	Good performance	2025-10-01 10:35:03.486304+01	1.7	t	2025-10-01 10:35:03.486304+01	57.717325424044915	57.717325424044915	4	15	2	20	21
121	19.11493830840052	Satisfactory	2025-10-01 10:35:03.490667+01	0	f	2025-10-01 10:35:03.490667+01	0	19.11493830840052	1	2	1	21	1
122	23.895629976501375	Good performance	2025-10-01 10:35:03.494847+01	0	f	2025-10-01 10:35:03.494848+01	0	23.895629976501375	2	2	1	21	1
123	0	Excellent work!	2025-10-01 10:35:03.499485+01	2.3	t	2025-10-01 10:35:03.499485+01	69.91323624486964	69.91323624486964	3	2	1	21	1
124	0	Good performance	2025-10-01 10:35:03.504409+01	1.3	t	2025-10-01 10:35:03.504409+01	51.61810610808022	51.61810610808022	4	2	1	21	1
125	28.553749668319146	Excellent work!	2025-10-01 10:35:03.508837+01	0	f	2025-10-01 10:35:03.508837+01	0	28.553749668319146	1	2	2	21	1
126	24.41645564456005	Good performance	2025-10-01 10:35:03.51309+01	0	f	2025-10-01 10:35:03.513091+01	0	24.41645564456005	2	2	2	21	1
127	0	Requires significant improvement	2025-10-01 10:35:03.517539+01	0	f	2025-10-01 10:35:03.517539+01	24.233123041234624	24.233123041234624	3	2	2	21	1
128	0	Good performance	2025-10-01 10:35:03.522161+01	1.3	t	2025-10-01 10:35:03.522163+01	51.30484832355382	51.30484832355382	4	2	2	21	1
129	26.886518859565555	Excellent work!	2025-10-01 10:35:03.530691+01	0	f	2025-10-01 10:35:03.530692+01	0	26.886518859565555	1	6	1	21	6
130	21.36591800918672	Good performance	2025-10-01 10:35:03.535479+01	0	f	2025-10-01 10:35:03.53548+01	0	21.36591800918672	2	6	1	21	6
131	0	Needs improvement	2025-10-01 10:35:03.540651+01	0	f	2025-10-01 10:35:03.540651+01	41.13629497717365	41.13629497717365	3	6	1	21	6
132	0	Excellent work!	2025-10-01 10:35:03.545489+01	1.7	t	2025-10-01 10:35:03.54549+01	59.90395027621979	59.90395027621979	4	6	1	21	6
133	24.414360677978166	Good performance	2025-10-01 10:35:03.55017+01	0	f	2025-10-01 10:35:03.55017+01	0	24.414360677978166	1	6	2	21	6
134	8.92084590237712	Requires significant improvement	2025-10-01 10:35:03.555216+01	0	f	2025-10-01 10:35:03.555216+01	0	8.92084590237712	2	6	2	21	6
135	0	Excellent work!	2025-10-01 10:35:03.560106+01	2	t	2025-10-01 10:35:03.560107+01	60.84044949617032	60.84044949617032	3	6	2	21	6
136	0	Excellent work!	2025-10-01 10:35:03.56503+01	2	t	2025-10-01 10:35:03.56503+01	60.76156661696952	60.76156661696952	4	6	2	21	6
137	16.078782873633987	Needs improvement	2025-10-01 10:35:03.570218+01	0	f	2025-10-01 10:35:03.570219+01	0	16.078782873633987	1	9	1	21	11
138	27.006357368855564	Excellent work!	2025-10-01 10:35:03.575454+01	0	f	2025-10-01 10:35:03.575454+01	0	27.006357368855564	2	9	1	21	11
139	0	Satisfactory	2025-10-01 10:35:03.580045+01	1	f	2025-10-01 10:35:03.580045+01	45.00447386688944	45.00447386688944	3	9	1	21	11
140	0	Requires significant improvement	2025-10-01 10:35:03.584494+01	0	f	2025-10-01 10:35:03.584495+01	5.738102620205376	5.738102620205376	4	9	1	21	11
141	18.865803288261905	Satisfactory	2025-10-01 10:35:03.590464+01	0	f	2025-10-01 10:35:03.590464+01	0	18.865803288261905	1	9	2	21	11
142	1.578188369654852	Requires significant improvement	2025-10-01 10:35:03.595166+01	0	f	2025-10-01 10:35:03.595166+01	0	1.578188369654852	2	9	2	21	11
143	0	Excellent work!	2025-10-01 10:35:03.599858+01	2.3	t	2025-10-01 10:35:03.599859+01	68.74320476139269	68.74320476139269	3	9	2	21	11
144	0	Requires significant improvement	2025-10-01 10:35:03.605532+01	0	f	2025-10-01 10:35:03.605533+01	34.052430538607496	34.052430538607496	4	9	2	21	11
145	1.5011085686784735	Requires significant improvement	2025-10-01 10:35:03.61154+01	0	f	2025-10-01 10:35:03.611541+01	0	1.5011085686784735	1	12	1	21	16
146	7.793562881897155	Requires significant improvement	2025-10-01 10:35:03.616369+01	0	f	2025-10-01 10:35:03.616369+01	0	7.793562881897155	2	12	1	21	16
147	0	Satisfactory	2025-10-01 10:35:03.621076+01	1	f	2025-10-01 10:35:03.621077+01	48.24013164461935	48.24013164461935	3	12	1	21	16
148	0	Good performance	2025-10-01 10:35:03.625898+01	1.7	t	2025-10-01 10:35:03.625899+01	59.00269251710415	59.00269251710415	4	12	1	21	16
149	5.220999952514224	Requires significant improvement	2025-10-01 10:35:03.632439+01	0	f	2025-10-01 10:35:03.632439+01	0	5.220999952514224	1	12	2	21	16
150	15.117647420041646	Needs improvement	2025-10-01 10:35:03.639465+01	0	f	2025-10-01 10:35:03.639465+01	0	15.117647420041646	2	12	2	21	16
151	0	Excellent work!	2025-10-01 10:35:03.646077+01	2.3	t	2025-10-01 10:35:03.646078+01	68.67284413200278	68.67284413200278	3	12	2	21	16
152	0	Excellent work!	2025-10-01 10:35:03.651403+01	2	t	2025-10-01 10:35:03.651404+01	60.50250196616311	60.50250196616311	4	12	2	21	16
153	23.786961048099815	Good performance	2025-10-01 10:35:03.655992+01	0	f	2025-10-01 10:35:03.655993+01	0	23.786961048099815	1	15	1	21	21
154	27.30244869173236	Excellent work!	2025-10-01 10:35:03.660611+01	0	f	2025-10-01 10:35:03.660611+01	0	27.30244869173236	2	15	1	21	21
155	0	Requires significant improvement	2025-10-01 10:35:03.664934+01	0	f	2025-10-01 10:35:03.664934+01	9.84360901701433	9.84360901701433	3	15	1	21	21
156	0	Needs improvement	2025-10-01 10:35:03.669843+01	0	f	2025-10-01 10:35:03.669844+01	40.75200996529825	40.75200996529825	4	15	1	21	21
157	23.40275512253418	Good performance	2025-10-01 10:35:03.6749+01	0	f	2025-10-01 10:35:03.6749+01	0	23.40275512253418	1	15	2	21	21
158	20.14258287352771	Satisfactory	2025-10-01 10:35:03.679814+01	0	f	2025-10-01 10:35:03.679814+01	0	20.14258287352771	2	15	2	21	21
159	0	Good performance	2025-10-01 10:35:03.684681+01	1.7	t	2025-10-01 10:35:03.684682+01	58.0981404972577	58.0981404972577	3	15	2	21	21
160	0	Good performance	2025-10-01 10:35:03.689565+01	1.3	t	2025-10-01 10:35:03.689565+01	50.48339597087816	50.48339597087816	4	15	2	21	21
161	29.450816338072336	Excellent work!	2025-10-01 10:35:03.694595+01	0	f	2025-10-01 10:35:03.694595+01	0	29.450816338072336	1	2	1	22	1
162	26.78357882160138	Excellent work!	2025-10-01 10:35:03.699189+01	0	f	2025-10-01 10:35:03.699189+01	0	26.78357882160138	2	2	1	22	1
163	0	Good performance	2025-10-01 10:35:03.704779+01	1.3	t	2025-10-01 10:35:03.704779+01	50.856203234461745	50.856203234461745	3	2	1	22	1
164	0	Excellent work!	2025-10-01 10:35:03.709653+01	1.7	t	2025-10-01 10:35:03.709653+01	59.5254301718916	59.5254301718916	4	2	1	22	1
165	21.757701484168905	Good performance	2025-10-01 10:35:03.714787+01	0	f	2025-10-01 10:35:03.714788+01	0	21.757701484168905	1	2	2	22	1
166	29.047006210853024	Excellent work!	2025-10-01 10:35:03.719677+01	0	f	2025-10-01 10:35:03.719677+01	0	29.047006210853024	2	2	2	22	1
167	0	Excellent work!	2025-10-01 10:35:03.724592+01	2.3	t	2025-10-01 10:35:03.724592+01	66.91110155321965	66.91110155321965	3	2	2	22	1
168	0	Requires significant improvement	2025-10-01 10:35:03.729566+01	0	f	2025-10-01 10:35:03.729567+01	31.400904639819288	31.400904639819288	4	2	2	22	1
169	20.973314578782468	Satisfactory	2025-10-01 10:35:03.734383+01	0	f	2025-10-01 10:35:03.734383+01	0	20.973314578782468	1	6	1	22	6
170	18.694714591444054	Satisfactory	2025-10-01 10:35:03.739979+01	0	f	2025-10-01 10:35:03.739979+01	0	18.694714591444054	2	6	1	22	6
171	0	Satisfactory	2025-10-01 10:35:03.746963+01	0	f	2025-10-01 10:35:03.746964+01	42.62055479325961	42.62055479325961	3	6	1	22	6
172	0	Requires significant improvement	2025-10-01 10:35:03.754817+01	0	f	2025-10-01 10:35:03.754818+01	30.740237041868703	30.740237041868703	4	6	1	22	6
173	29.93016157733382	Excellent work!	2025-10-01 10:35:03.760045+01	0	f	2025-10-01 10:35:03.760045+01	0	29.93016157733382	1	6	2	22	6
174	27.611626896128932	Excellent work!	2025-10-01 10:35:03.76474+01	0	f	2025-10-01 10:35:03.764741+01	0	27.611626896128932	2	6	2	22	6
175	0	Excellent work!	2025-10-01 10:35:03.769772+01	2.3	t	2025-10-01 10:35:03.769772+01	65.14377091667228	65.14377091667228	3	6	2	22	6
176	0	Excellent work!	2025-10-01 10:35:03.774555+01	2	t	2025-10-01 10:35:03.774555+01	60.20181900960424	60.20181900960424	4	6	2	22	6
177	3.64119949132022	Requires significant improvement	2025-10-01 10:35:03.780062+01	0	f	2025-10-01 10:35:03.780063+01	0	3.64119949132022	1	9	1	22	11
178	23.034520934389754	Good performance	2025-10-01 10:35:03.785676+01	0	f	2025-10-01 10:35:03.785676+01	0	23.034520934389754	2	9	1	22	11
179	0	Excellent work!	2025-10-01 10:35:03.791053+01	2	t	2025-10-01 10:35:03.791053+01	61.967564216281495	61.967564216281495	3	9	1	22	11
180	0	Needs improvement	2025-10-01 10:35:03.797022+01	0	f	2025-10-01 10:35:03.797023+01	35.288920974652484	35.288920974652484	4	9	1	22	11
181	12.698320188945873	Requires significant improvement	2025-10-01 10:35:03.804706+01	0	f	2025-10-01 10:35:03.804706+01	0	12.698320188945873	1	9	2	22	11
182	27.17423892612819	Excellent work!	2025-10-01 10:35:03.809891+01	0	f	2025-10-01 10:35:03.809892+01	0	27.17423892612819	2	9	2	22	11
183	0	Good performance	2025-10-01 10:35:03.814896+01	1.7	t	2025-10-01 10:35:03.814897+01	57.06604510160729	57.06604510160729	3	9	2	22	11
184	0	Excellent work!	2025-10-01 10:35:03.819965+01	2	t	2025-10-01 10:35:03.819965+01	64.38000713481847	64.38000713481847	4	9	2	22	11
185	29.977032563472328	Excellent work!	2025-10-01 10:35:03.824647+01	0	f	2025-10-01 10:35:03.824647+01	0	29.977032563472328	1	12	1	22	16
186	9.96630065104246	Requires significant improvement	2025-10-01 10:35:03.829369+01	0	f	2025-10-01 10:35:03.82937+01	0	9.96630065104246	2	12	1	22	16
187	0	Excellent work!	2025-10-01 10:35:03.834122+01	2.3	t	2025-10-01 10:35:03.834122+01	66.7469773185737	66.7469773185737	3	12	1	22	16
188	0	Requires significant improvement	2025-10-01 10:35:03.839754+01	0	f	2025-10-01 10:35:03.839754+01	26.315240042389508	26.315240042389508	4	12	1	22	16
189	22.4348523423147	Good performance	2025-10-01 10:35:03.844375+01	0	f	2025-10-01 10:35:03.844376+01	0	22.4348523423147	1	12	2	22	16
190	21.64946584405518	Good performance	2025-10-01 10:35:03.849069+01	0	f	2025-10-01 10:35:03.849069+01	0	21.64946584405518	2	12	2	22	16
191	0	Satisfactory	2025-10-01 10:35:03.853856+01	1	f	2025-10-01 10:35:03.853857+01	46.12342968600838	46.12342968600838	3	12	2	22	16
192	0	Excellent work!	2025-10-01 10:35:03.859003+01	2.3	t	2025-10-01 10:35:03.859004+01	65.43515076560914	65.43515076560914	4	12	2	22	16
193	11.509890135467055	Requires significant improvement	2025-10-01 10:35:03.864063+01	0	f	2025-10-01 10:35:03.864063+01	0	11.509890135467055	1	15	1	22	21
194	19.04516230497226	Satisfactory	2025-10-01 10:35:03.871197+01	0	f	2025-10-01 10:35:03.871198+01	0	19.04516230497226	2	15	1	22	21
195	0	Requires significant improvement	2025-10-01 10:35:03.876781+01	0	f	2025-10-01 10:35:03.876781+01	33.68194756624901	33.68194756624901	3	15	1	22	21
196	0	Requires significant improvement	2025-10-01 10:35:03.882053+01	0	f	2025-10-01 10:35:03.882054+01	32.153217930908276	32.153217930908276	4	15	1	22	21
197	16.347184881459796	Needs improvement	2025-10-01 10:35:03.88705+01	0	f	2025-10-01 10:35:03.887051+01	0	16.347184881459796	1	15	2	22	21
198	18.69241072038978	Satisfactory	2025-10-01 10:35:03.893459+01	0	f	2025-10-01 10:35:03.89346+01	0	18.69241072038978	2	15	2	22	21
199	0	Requires significant improvement	2025-10-01 10:35:03.90035+01	0	f	2025-10-01 10:35:03.900351+01	32.10038000754857	32.10038000754857	3	15	2	22	21
200	0	Excellent work!	2025-10-01 10:35:03.906642+01	2	t	2025-10-01 10:35:03.906642+01	61.87789649421859	61.87789649421859	4	15	2	22	21
201	27.595766692098476	Excellent work!	2025-10-01 10:35:03.911619+01	0	f	2025-10-01 10:35:03.91162+01	0	27.595766692098476	1	2	1	23	1
202	28.662110750065285	Excellent work!	2025-10-01 10:35:03.916865+01	0	f	2025-10-01 10:35:03.916865+01	0	28.662110750065285	2	2	1	23	1
203	0	Needs improvement	2025-10-01 10:35:03.922715+01	0	f	2025-10-01 10:35:03.922716+01	36.46457382983934	36.46457382983934	3	2	1	23	1
204	0	Good performance	2025-10-01 10:35:03.928351+01	1.3	t	2025-10-01 10:35:03.928352+01	50.84931108177642	50.84931108177642	4	2	1	23	1
205	23.123038781309127	Good performance	2025-10-01 10:35:03.935539+01	0	f	2025-10-01 10:35:03.93554+01	0	23.123038781309127	1	2	2	23	1
206	17.76977966405725	Needs improvement	2025-10-01 10:35:03.941514+01	0	f	2025-10-01 10:35:03.941515+01	0	17.76977966405725	2	2	2	23	1
207	0	Good performance	2025-10-01 10:35:03.947011+01	1.7	t	2025-10-01 10:35:03.947012+01	56.204566275365195	56.204566275365195	3	2	2	23	1
208	0	Needs improvement	2025-10-01 10:35:03.953048+01	0	f	2025-10-01 10:35:03.953049+01	39.3419648257816	39.3419648257816	4	2	2	23	1
209	20.073591122328494	Satisfactory	2025-10-01 10:35:03.958309+01	0	f	2025-10-01 10:35:03.95831+01	0	20.073591122328494	1	6	1	23	6
210	27.957214599115524	Excellent work!	2025-10-01 10:35:03.963674+01	0	f	2025-10-01 10:35:03.963675+01	0	27.957214599115524	2	6	1	23	6
211	0	Excellent work!	2025-10-01 10:35:03.969149+01	2	t	2025-10-01 10:35:03.96915+01	62.40418909975125	62.40418909975125	3	6	1	23	6
212	0	Excellent work!	2025-10-01 10:35:03.974839+01	2.3	t	2025-10-01 10:35:03.974839+01	65.52568973145962	65.52568973145962	4	6	1	23	6
213	25.237691526517175	Good performance	2025-10-01 10:35:03.97988+01	0	f	2025-10-01 10:35:03.979881+01	0	25.237691526517175	1	6	2	23	6
214	27.303566994761017	Excellent work!	2025-10-01 10:35:03.984968+01	0	f	2025-10-01 10:35:03.984968+01	0	27.303566994761017	2	6	2	23	6
215	0	Needs improvement	2025-10-01 10:35:03.989665+01	0	f	2025-10-01 10:35:03.989666+01	41.64274799652138	41.64274799652138	3	6	2	23	6
216	0	Excellent work!	2025-10-01 10:35:03.99568+01	2.3	t	2025-10-01 10:35:03.995681+01	67.20912222615773	67.20912222615773	4	6	2	23	6
217	25.726966430624884	Excellent work!	2025-10-01 10:35:04.001466+01	0	f	2025-10-01 10:35:04.001466+01	0	25.726966430624884	1	9	1	23	11
218	18.877133148113053	Satisfactory	2025-10-01 10:35:04.00682+01	0	f	2025-10-01 10:35:04.006821+01	0	18.877133148113053	2	9	1	23	11
219	0	Excellent work!	2025-10-01 10:35:04.011789+01	2	t	2025-10-01 10:35:04.01179+01	60.356124617570075	60.356124617570075	3	9	1	23	11
220	0	Satisfactory	2025-10-01 10:35:04.016666+01	1	f	2025-10-01 10:35:04.016667+01	48.7571774224711	48.7571774224711	4	9	1	23	11
221	23.925388011467327	Good performance	2025-10-01 10:35:04.021659+01	0	f	2025-10-01 10:35:04.02166+01	0	23.925388011467327	1	9	2	23	11
222	20.168272843950906	Satisfactory	2025-10-01 10:35:04.027854+01	0	f	2025-10-01 10:35:04.027855+01	0	20.168272843950906	2	9	2	23	11
223	0	Excellent work!	2025-10-01 10:35:04.032766+01	1.7	t	2025-10-01 10:35:04.032766+01	59.63597179308641	59.63597179308641	3	9	2	23	11
224	0	Excellent work!	2025-10-01 10:35:04.038417+01	2	t	2025-10-01 10:35:04.038418+01	61.27255591270422	61.27255591270422	4	9	2	23	11
225	26.700845022198017	Excellent work!	2025-10-01 10:35:04.043448+01	0	f	2025-10-01 10:35:04.043448+01	0	26.700845022198017	1	12	1	23	16
226	19.895294343960188	Satisfactory	2025-10-01 10:35:04.048394+01	0	f	2025-10-01 10:35:04.048395+01	0	19.895294343960188	2	12	1	23	16
227	0	Excellent work!	2025-10-01 10:35:04.053651+01	2	t	2025-10-01 10:35:04.053651+01	64.2175204813685	64.2175204813685	3	12	1	23	16
228	0	Excellent work!	2025-10-01 10:35:04.059764+01	2.3	t	2025-10-01 10:35:04.059765+01	69.92541509080006	69.92541509080006	4	12	1	23	16
229	25.736100993880935	Excellent work!	2025-10-01 10:35:04.065022+01	0	f	2025-10-01 10:35:04.065022+01	0	25.736100993880935	1	12	2	23	16
230	25.615655876577236	Excellent work!	2025-10-01 10:35:04.0712+01	0	f	2025-10-01 10:35:04.071201+01	0	25.615655876577236	2	12	2	23	16
231	0	Requires significant improvement	2025-10-01 10:35:04.076471+01	0	f	2025-10-01 10:35:04.076471+01	14.759152859304923	14.759152859304923	3	12	2	23	16
232	0	Satisfactory	2025-10-01 10:35:04.081843+01	0	f	2025-10-01 10:35:04.081844+01	43.26158353325357	43.26158353325357	4	12	2	23	16
233	13.952486840969586	Requires significant improvement	2025-10-01 10:35:04.087214+01	0	f	2025-10-01 10:35:04.087215+01	0	13.952486840969586	1	15	1	23	21
234	25.648011289384506	Excellent work!	2025-10-01 10:35:04.092307+01	0	f	2025-10-01 10:35:04.092308+01	0	25.648011289384506	2	15	1	23	21
235	0	Excellent work!	2025-10-01 10:35:04.097239+01	2	t	2025-10-01 10:35:04.09724+01	62.37523003067231	62.37523003067231	3	15	1	23	21
236	0	Excellent work!	2025-10-01 10:35:04.102895+01	2.3	t	2025-10-01 10:35:04.102896+01	69.82205780552518	69.82205780552518	4	15	1	23	21
237	26.930723826737754	Excellent work!	2025-10-01 10:35:04.108253+01	0	f	2025-10-01 10:35:04.108253+01	0	26.930723826737754	1	15	2	23	21
238	15.365328461915857	Needs improvement	2025-10-01 10:35:04.113176+01	0	f	2025-10-01 10:35:04.113177+01	0	15.365328461915857	2	15	2	23	21
239	0	Satisfactory	2025-10-01 10:35:04.119085+01	1	f	2025-10-01 10:35:04.119086+01	46.939910603626124	46.939910603626124	3	15	2	23	21
240	0	Good performance	2025-10-01 10:35:04.124444+01	1.3	t	2025-10-01 10:35:04.124444+01	54.8749209870649	54.8749209870649	4	15	2	23	21
241	19.555717684534514	Satisfactory	2025-10-01 10:35:04.129729+01	0	f	2025-10-01 10:35:04.129729+01	0	19.555717684534514	1	2	1	24	1
242	7.885445531986504	Requires significant improvement	2025-10-01 10:35:04.136322+01	0	f	2025-10-01 10:35:04.136323+01	0	7.885445531986504	2	2	1	24	1
243	0	Excellent work!	2025-10-01 10:35:04.142545+01	2.3	t	2025-10-01 10:35:04.142545+01	69.953122902511	69.953122902511	3	2	1	24	1
244	0	Satisfactory	2025-10-01 10:35:04.14843+01	0	f	2025-10-01 10:35:04.14843+01	44.99903119374665	44.99903119374665	4	2	1	24	1
245	26.446556275015514	Excellent work!	2025-10-01 10:35:04.154029+01	0	f	2025-10-01 10:35:04.154029+01	0	26.446556275015514	1	2	2	24	1
246	27.478090753165823	Excellent work!	2025-10-01 10:35:04.159324+01	0	f	2025-10-01 10:35:04.159324+01	0	27.478090753165823	2	2	2	24	1
247	0	Good performance	2025-10-01 10:35:04.164987+01	1	f	2025-10-01 10:35:04.164987+01	49.83047540960494	49.83047540960494	3	2	2	24	1
248	0	Needs improvement	2025-10-01 10:35:04.170753+01	0	f	2025-10-01 10:35:04.170754+01	35.53546683687941	35.53546683687941	4	2	2	24	1
249	6.2915071622002925	Requires significant improvement	2025-10-01 10:35:04.175938+01	0	f	2025-10-01 10:35:04.175939+01	0	6.2915071622002925	1	6	1	24	6
250	16.44306215789381	Needs improvement	2025-10-01 10:35:04.181422+01	0	f	2025-10-01 10:35:04.181422+01	0	16.44306215789381	2	6	1	24	6
251	0	Satisfactory	2025-10-01 10:35:04.187956+01	1	f	2025-10-01 10:35:04.187957+01	48.285303663522924	48.285303663522924	3	6	1	24	6
252	0	Excellent work!	2025-10-01 10:35:04.193547+01	2.3	t	2025-10-01 10:35:04.193548+01	69.08108267604028	69.08108267604028	4	6	1	24	6
253	28.88793732129038	Excellent work!	2025-10-01 10:35:04.19922+01	0	f	2025-10-01 10:35:04.19922+01	0	28.88793732129038	1	6	2	24	6
254	28.120742263315005	Excellent work!	2025-10-01 10:35:04.205677+01	0	f	2025-10-01 10:35:04.205678+01	0	28.120742263315005	2	6	2	24	6
255	0	Excellent work!	2025-10-01 10:35:04.211075+01	2	t	2025-10-01 10:35:04.211076+01	62.978739960496554	62.978739960496554	3	6	2	24	6
256	0	Requires significant improvement	2025-10-01 10:35:04.216752+01	0	f	2025-10-01 10:35:04.216753+01	33.12837138450002	33.12837138450002	4	6	2	24	6
257	8.574110127737516	Requires significant improvement	2025-10-01 10:35:04.223033+01	0	f	2025-10-01 10:35:04.223035+01	0	8.574110127737516	1	9	1	24	11
258	28.78150130010742	Excellent work!	2025-10-01 10:35:04.228156+01	0	f	2025-10-01 10:35:04.228156+01	0	28.78150130010742	2	9	1	24	11
259	0	Needs improvement	2025-10-01 10:35:04.233913+01	0	f	2025-10-01 10:35:04.233913+01	40.220915693847374	40.220915693847374	3	9	1	24	11
260	0	Excellent work!	2025-10-01 10:35:04.239844+01	2.3	t	2025-10-01 10:35:04.239844+01	69.61133271958158	69.61133271958158	4	9	1	24	11
261	15.646205396569922	Needs improvement	2025-10-01 10:35:04.244984+01	0	f	2025-10-01 10:35:04.244985+01	0	15.646205396569922	1	9	2	24	11
262	27.191197465563377	Excellent work!	2025-10-01 10:35:04.250899+01	0	f	2025-10-01 10:35:04.250899+01	0	27.191197465563377	2	9	2	24	11
263	0	Good performance	2025-10-01 10:35:04.25634+01	1.3	t	2025-10-01 10:35:04.256341+01	50.483869536258425	50.483869536258425	3	9	2	24	11
264	0	Excellent work!	2025-10-01 10:35:04.261768+01	2	t	2025-10-01 10:35:04.261768+01	61.61856383430485	61.61856383430485	4	9	2	24	11
265	26.902091092428503	Excellent work!	2025-10-01 10:35:04.267196+01	0	f	2025-10-01 10:35:04.267196+01	0	26.902091092428503	1	12	1	24	16
266	15.81994222159493	Needs improvement	2025-10-01 10:35:04.27303+01	0	f	2025-10-01 10:35:04.273031+01	0	15.81994222159493	2	12	1	24	16
267	0	Satisfactory	2025-10-01 10:35:04.278458+01	1	f	2025-10-01 10:35:04.278458+01	46.83475164053748	46.83475164053748	3	12	1	24	16
268	0	Satisfactory	2025-10-01 10:35:04.284094+01	1	f	2025-10-01 10:35:04.284095+01	47.70475700109943	47.70475700109943	4	12	1	24	16
269	15.738831784880432	Needs improvement	2025-10-01 10:35:04.292016+01	0	f	2025-10-01 10:35:04.292016+01	0	15.738831784880432	1	12	2	24	16
270	20.379674651218945	Satisfactory	2025-10-01 10:35:04.297481+01	0	f	2025-10-01 10:35:04.297482+01	0	20.379674651218945	2	12	2	24	16
271	0	Needs improvement	2025-10-01 10:35:04.303717+01	0	f	2025-10-01 10:35:04.303718+01	38.03996230016633	38.03996230016633	3	12	2	24	16
272	0	Excellent work!	2025-10-01 10:35:04.309159+01	2	t	2025-10-01 10:35:04.309159+01	62.556698669451606	62.556698669451606	4	12	2	24	16
273	6.252480823722308	Requires significant improvement	2025-10-01 10:35:04.314644+01	0	f	2025-10-01 10:35:04.314644+01	0	6.252480823722308	1	15	1	24	21
274	24.508693064598855	Good performance	2025-10-01 10:35:04.320365+01	0	f	2025-10-01 10:35:04.320366+01	0	24.508693064598855	2	15	1	24	21
275	0	Needs improvement	2025-10-01 10:35:04.325878+01	0	f	2025-10-01 10:35:04.325878+01	35.6383123055536	35.6383123055536	3	15	1	24	21
276	0	Good performance	2025-10-01 10:35:04.3316+01	1.3	t	2025-10-01 10:35:04.331601+01	52.758270256308826	52.758270256308826	4	15	1	24	21
277	19.15351293241117	Satisfactory	2025-10-01 10:35:04.338058+01	0	f	2025-10-01 10:35:04.338059+01	0	19.15351293241117	1	15	2	24	21
278	24.841094114937633	Good performance	2025-10-01 10:35:04.343739+01	0	f	2025-10-01 10:35:04.343739+01	0	24.841094114937633	2	15	2	24	21
279	0	Excellent work!	2025-10-01 10:35:04.349009+01	2	t	2025-10-01 10:35:04.349009+01	63.318749822820905	63.318749822820905	3	15	2	24	21
280	0	Excellent work!	2025-10-01 10:35:04.35468+01	1.7	t	2025-10-01 10:35:04.354681+01	59.83838555920824	59.83838555920824	4	15	2	24	21
281	22.967658499828442	Good performance	2025-10-01 10:35:04.360019+01	0	f	2025-10-01 10:35:04.36002+01	0	22.967658499828442	1	2	1	25	1
282	19.258722781921254	Satisfactory	2025-10-01 10:35:04.365698+01	0	f	2025-10-01 10:35:04.365699+01	0	19.258722781921254	2	2	1	25	1
283	0	Requires significant improvement	2025-10-01 10:35:04.37247+01	0	f	2025-10-01 10:35:04.372471+01	19.38263916751673	19.38263916751673	3	2	1	25	1
284	0	Good performance	2025-10-01 10:35:04.379765+01	1.3	t	2025-10-01 10:35:04.379766+01	50.547509065667825	50.547509065667825	4	2	1	25	1
285	28.515066161987072	Excellent work!	2025-10-01 10:35:04.385632+01	0	f	2025-10-01 10:35:04.385633+01	0	28.515066161987072	1	2	2	25	1
286	25.105012144595037	Good performance	2025-10-01 10:35:04.390835+01	0	f	2025-10-01 10:35:04.390835+01	0	25.105012144595037	2	2	2	25	1
287	0	Requires significant improvement	2025-10-01 10:35:04.395999+01	0	f	2025-10-01 10:35:04.396+01	4.497959417071526	4.497959417071526	3	2	2	25	1
288	0	Needs improvement	2025-10-01 10:35:04.401502+01	0	f	2025-10-01 10:35:04.401503+01	37.89452335576166	37.89452335576166	4	2	2	25	1
289	21.960639961481974	Good performance	2025-10-01 10:35:04.407363+01	0	f	2025-10-01 10:35:04.407363+01	0	21.960639961481974	1	6	1	25	6
290	18.13370288214592	Satisfactory	2025-10-01 10:35:04.412512+01	0	f	2025-10-01 10:35:04.412513+01	0	18.13370288214592	2	6	1	25	6
291	0	Excellent work!	2025-10-01 10:35:04.418312+01	2.3	t	2025-10-01 10:35:04.418312+01	66.94161375728461	66.94161375728461	3	6	1	25	6
292	0	Needs improvement	2025-10-01 10:35:04.423971+01	0	f	2025-10-01 10:35:04.423971+01	36.60033304161257	36.60033304161257	4	6	1	25	6
293	26.097017653798577	Excellent work!	2025-10-01 10:35:04.429542+01	0	f	2025-10-01 10:35:04.429542+01	0	26.097017653798577	1	6	2	25	6
294	16.958731882008742	Needs improvement	2025-10-01 10:35:04.435194+01	0	f	2025-10-01 10:35:04.435195+01	0	16.958731882008742	2	6	2	25	6
295	0	Excellent work!	2025-10-01 10:35:04.441588+01	2	t	2025-10-01 10:35:04.441588+01	62.64423133597197	62.64423133597197	3	6	2	25	6
296	0	Good performance	2025-10-01 10:35:04.446784+01	1.3	t	2025-10-01 10:35:04.446785+01	50.88463228009415	50.88463228009415	4	6	2	25	6
297	4.273063379830459	Requires significant improvement	2025-10-01 10:35:04.452348+01	0	f	2025-10-01 10:35:04.452349+01	0	4.273063379830459	1	9	1	25	11
298	6.16623495235863	Requires significant improvement	2025-10-01 10:35:04.457958+01	0	f	2025-10-01 10:35:04.457958+01	0	6.16623495235863	2	9	1	25	11
299	0	Requires significant improvement	2025-10-01 10:35:04.463789+01	0	f	2025-10-01 10:35:04.463789+01	2.6398028484287455	2.6398028484287455	3	9	1	25	11
300	0	Needs improvement	2025-10-01 10:35:04.470479+01	0	f	2025-10-01 10:35:04.470479+01	35.94859833238754	35.94859833238754	4	9	1	25	11
301	29.344174630516477	Excellent work!	2025-10-01 10:35:04.476201+01	0	f	2025-10-01 10:35:04.476201+01	0	29.344174630516477	1	9	2	25	11
302	25.631322819955532	Excellent work!	2025-10-01 10:35:04.48187+01	0	f	2025-10-01 10:35:04.48187+01	0	25.631322819955532	2	9	2	25	11
303	0	Excellent work!	2025-10-01 10:35:04.487509+01	2	t	2025-10-01 10:35:04.487509+01	64.94516331256926	64.94516331256926	3	9	2	25	11
304	0	Needs improvement	2025-10-01 10:35:04.493048+01	0	f	2025-10-01 10:35:04.493049+01	36.31749430411822	36.31749430411822	4	9	2	25	11
305	27.35227907271297	Excellent work!	2025-10-01 10:35:04.498518+01	0	f	2025-10-01 10:35:04.498518+01	0	27.35227907271297	1	12	1	25	16
306	29.129146729147415	Excellent work!	2025-10-01 10:35:04.504899+01	0	f	2025-10-01 10:35:04.504899+01	0	29.129146729147415	2	12	1	25	16
307	0	Needs improvement	2025-10-01 10:35:04.510555+01	0	f	2025-10-01 10:35:04.510556+01	41.78304491293446	41.78304491293446	3	12	1	25	16
308	0	Excellent work!	2025-10-01 10:35:04.516124+01	2.3	t	2025-10-01 10:35:04.516124+01	68.4819893830234	68.4819893830234	4	12	1	25	16
309	25.326345131698144	Good performance	2025-10-01 10:35:04.52176+01	0	f	2025-10-01 10:35:04.521761+01	0	25.326345131698144	1	12	2	25	16
310	25.71476754804844	Excellent work!	2025-10-01 10:35:04.527479+01	0	f	2025-10-01 10:35:04.527479+01	0	25.71476754804844	2	12	2	25	16
311	0	Excellent work!	2025-10-01 10:35:04.532781+01	2	t	2025-10-01 10:35:04.532781+01	60.332470749888415	60.332470749888415	3	12	2	25	16
312	0	Excellent work!	2025-10-01 10:35:04.538835+01	2.3	t	2025-10-01 10:35:04.538836+01	65.91358558609251	65.91358558609251	4	12	2	25	16
313	6.6310815423841625	Requires significant improvement	2025-10-01 10:35:04.544653+01	0	f	2025-10-01 10:35:04.544653+01	0	6.6310815423841625	1	15	1	25	21
314	6.652446379191817	Requires significant improvement	2025-10-01 10:35:04.550251+01	0	f	2025-10-01 10:35:04.550252+01	0	6.652446379191817	2	15	1	25	21
315	0	Needs improvement	2025-10-01 10:35:04.556209+01	0	f	2025-10-01 10:35:04.556209+01	37.3908564651233	37.3908564651233	3	15	1	25	21
316	0	Excellent work!	2025-10-01 10:35:04.562075+01	2.3	t	2025-10-01 10:35:04.562075+01	68.33322715622143	68.33322715622143	4	15	1	25	21
317	29.290843358061046	Excellent work!	2025-10-01 10:35:04.567793+01	0	f	2025-10-01 10:35:04.567793+01	0	29.290843358061046	1	15	2	25	21
318	29.717057069373134	Excellent work!	2025-10-01 10:35:04.573894+01	0	f	2025-10-01 10:35:04.573895+01	0	29.717057069373134	2	15	2	25	21
319	0	Requires significant improvement	2025-10-01 10:35:04.579486+01	0	f	2025-10-01 10:35:04.579486+01	24.004348238832993	24.004348238832993	3	15	2	25	21
320	0	Good performance	2025-10-01 10:35:04.585293+01	1.7	t	2025-10-01 10:35:04.585294+01	58.75305052077364	58.75305052077364	4	15	2	25	21
321	26.857407807960353	Excellent work!	2025-10-01 10:35:04.591245+01	0	f	2025-10-01 10:35:04.591246+01	0	26.857407807960353	1	2	1	26	1
322	18.777108880411106	Satisfactory	2025-10-01 10:35:04.598305+01	0	f	2025-10-01 10:35:04.598305+01	0	18.777108880411106	2	2	1	26	1
323	0	Satisfactory	2025-10-01 10:35:04.604899+01	0	f	2025-10-01 10:35:04.604899+01	42.09358259675087	42.09358259675087	3	2	1	26	1
324	0	Requires significant improvement	2025-10-01 10:35:04.610684+01	0	f	2025-10-01 10:35:04.610684+01	31.584683488192063	31.584683488192063	4	2	1	26	1
325	28.882444644762415	Excellent work!	2025-10-01 10:35:04.616384+01	0	f	2025-10-01 10:35:04.616385+01	0	28.882444644762415	1	2	2	26	1
326	15.760501755242014	Needs improvement	2025-10-01 10:35:04.625003+01	0	f	2025-10-01 10:35:04.625004+01	0	15.760501755242014	2	2	2	26	1
327	0	Excellent work!	2025-10-01 10:35:04.63316+01	2	t	2025-10-01 10:35:04.63316+01	64.97608776631496	64.97608776631496	3	2	2	26	1
328	0	Excellent work!	2025-10-01 10:35:04.639806+01	2.3	t	2025-10-01 10:35:04.639806+01	68.56539171591564	68.56539171591564	4	2	2	26	1
329	29.408507769300996	Excellent work!	2025-10-01 10:35:04.645871+01	0	f	2025-10-01 10:35:04.645871+01	0	29.408507769300996	1	6	1	26	6
330	26.236881676825586	Excellent work!	2025-10-01 10:35:04.660571+01	0	f	2025-10-01 10:35:04.660574+01	0	26.236881676825586	2	6	1	26	6
331	0	Excellent work!	2025-10-01 10:35:04.666813+01	2	t	2025-10-01 10:35:04.666813+01	63.61221731250542	63.61221731250542	3	6	1	26	6
332	0	Satisfactory	2025-10-01 10:35:04.673702+01	1	f	2025-10-01 10:35:04.673703+01	47.0026472150259	47.0026472150259	4	6	1	26	6
333	22.789887731718085	Good performance	2025-10-01 10:35:04.681662+01	0	f	2025-10-01 10:35:04.681663+01	0	22.789887731718085	1	6	2	26	6
334	0.19429624615972363	Requires significant improvement	2025-10-01 10:35:04.687985+01	0	f	2025-10-01 10:35:04.687986+01	0	0.19429624615972363	2	6	2	26	6
335	0	Excellent work!	2025-10-01 10:35:04.693888+01	2	t	2025-10-01 10:35:04.693888+01	60.88745272500587	60.88745272500587	3	6	2	26	6
336	0	Satisfactory	2025-10-01 10:35:04.699712+01	1	f	2025-10-01 10:35:04.699712+01	47.49537254435519	47.49537254435519	4	6	2	26	6
337	19.33538872563663	Satisfactory	2025-10-01 10:35:04.706635+01	0	f	2025-10-01 10:35:04.706635+01	0	19.33538872563663	1	9	1	26	11
338	26.324079500716316	Excellent work!	2025-10-01 10:35:04.712273+01	0	f	2025-10-01 10:35:04.712274+01	0	26.324079500716316	2	9	1	26	11
339	0	Needs improvement	2025-10-01 10:35:04.718306+01	0	f	2025-10-01 10:35:04.718307+01	40.03982182024186	40.03982182024186	3	9	1	26	11
340	0	Good performance	2025-10-01 10:35:04.724373+01	1.7	t	2025-10-01 10:35:04.724373+01	55.28791685841357	55.28791685841357	4	9	1	26	11
341	21.426494184808053	Good performance	2025-10-01 10:35:04.730226+01	0	f	2025-10-01 10:35:04.730227+01	0	21.426494184808053	1	9	2	26	11
342	28.04160425806923	Excellent work!	2025-10-01 10:35:04.745828+01	0	f	2025-10-01 10:35:04.74583+01	0	28.04160425806923	2	9	2	26	11
343	0	Excellent work!	2025-10-01 10:35:04.753133+01	2	t	2025-10-01 10:35:04.753133+01	63.13008942020882	63.13008942020882	3	9	2	26	11
344	0	Requires significant improvement	2025-10-01 10:35:04.75959+01	0	f	2025-10-01 10:35:04.75959+01	11.193797534441044	11.193797534441044	4	9	2	26	11
345	27.66920780240963	Excellent work!	2025-10-01 10:35:04.765778+01	0	f	2025-10-01 10:35:04.765779+01	0	27.66920780240963	1	12	1	26	16
346	0.46787226696989037	Requires significant improvement	2025-10-01 10:35:04.772858+01	0	f	2025-10-01 10:35:04.772858+01	0	0.46787226696989037	2	12	1	26	16
347	0	Excellent work!	2025-10-01 10:35:04.778719+01	2	t	2025-10-01 10:35:04.778719+01	61.83695457483257	61.83695457483257	3	12	1	26	16
348	0	Excellent work!	2025-10-01 10:35:04.784886+01	2.3	t	2025-10-01 10:35:04.784886+01	66.8809848951483	66.8809848951483	4	12	1	26	16
349	15.294230637196675	Needs improvement	2025-10-01 10:35:04.790528+01	0	f	2025-10-01 10:35:04.790529+01	0	15.294230637196675	1	12	2	26	16
350	20.452703476255436	Satisfactory	2025-10-01 10:35:04.798399+01	0	f	2025-10-01 10:35:04.798399+01	0	20.452703476255436	2	12	2	26	16
351	0	Needs improvement	2025-10-01 10:35:04.80656+01	0	f	2025-10-01 10:35:04.806561+01	39.66633169542521	39.66633169542521	3	12	2	26	16
352	0	Excellent work!	2025-10-01 10:35:04.812701+01	2	t	2025-10-01 10:35:04.812701+01	64.43153261633734	64.43153261633734	4	12	2	26	16
353	26.83298808760827	Excellent work!	2025-10-01 10:35:04.818864+01	0	f	2025-10-01 10:35:04.818865+01	0	26.83298808760827	1	15	1	26	21
354	25.627149059752465	Excellent work!	2025-10-01 10:35:04.825013+01	0	f	2025-10-01 10:35:04.825013+01	0	25.627149059752465	2	15	1	26	21
355	0	Requires significant improvement	2025-10-01 10:35:04.830807+01	0	f	2025-10-01 10:35:04.830808+01	14.470846383021971	14.470846383021971	3	15	1	26	21
356	0	Requires significant improvement	2025-10-01 10:35:04.837236+01	0	f	2025-10-01 10:35:04.837236+01	8.86392522537191	8.86392522537191	4	15	1	26	21
357	21.638996690378676	Good performance	2025-10-01 10:35:04.843173+01	0	f	2025-10-01 10:35:04.843173+01	0	21.638996690378676	1	15	2	26	21
358	23.092183062966654	Good performance	2025-10-01 10:35:04.849128+01	0	f	2025-10-01 10:35:04.849129+01	0	23.092183062966654	2	15	2	26	21
359	0	Needs improvement	2025-10-01 10:35:04.854797+01	0	f	2025-10-01 10:35:04.854798+01	41.0696709960856	41.0696709960856	3	15	2	26	21
360	0	Requires significant improvement	2025-10-01 10:35:04.860297+01	0	f	2025-10-01 10:35:04.860297+01	28.061694323570876	28.061694323570876	4	15	2	26	21
361	3.0589785153573414	Requires significant improvement	2025-10-01 10:35:04.866149+01	0	f	2025-10-01 10:35:04.866149+01	0	3.0589785153573414	1	2	1	27	1
362	27.260024059305163	Excellent work!	2025-10-01 10:35:04.872264+01	0	f	2025-10-01 10:35:04.872265+01	0	27.260024059305163	2	2	1	27	1
363	0	Good performance	2025-10-01 10:35:04.878579+01	1.3	t	2025-10-01 10:35:04.878579+01	54.56237432822003	54.56237432822003	3	2	1	27	1
364	0	Excellent work!	2025-10-01 10:35:04.885472+01	2	t	2025-10-01 10:35:04.885473+01	63.30025588012487	63.30025588012487	4	2	1	27	1
365	16.233189500356573	Needs improvement	2025-10-01 10:35:04.891398+01	0	f	2025-10-01 10:35:04.891398+01	0	16.233189500356573	1	2	2	27	1
366	25.651140087527942	Excellent work!	2025-10-01 10:35:04.897157+01	0	f	2025-10-01 10:35:04.897157+01	0	25.651140087527942	2	2	2	27	1
367	0	Good performance	2025-10-01 10:35:04.903643+01	1.3	t	2025-10-01 10:35:04.903643+01	51.58672016296966	51.58672016296966	3	2	2	27	1
368	0	Good performance	2025-10-01 10:35:04.909657+01	1.3	t	2025-10-01 10:35:04.909658+01	53.39075309397391	53.39075309397391	4	2	2	27	1
369	29.51361443981103	Excellent work!	2025-10-01 10:35:04.915488+01	0	f	2025-10-01 10:35:04.915489+01	0	29.51361443981103	1	6	1	27	6
370	12.511543465957544	Requires significant improvement	2025-10-01 10:35:04.921266+01	0	f	2025-10-01 10:35:04.921266+01	0	12.511543465957544	2	6	1	27	6
371	0	Satisfactory	2025-10-01 10:35:04.927424+01	1	f	2025-10-01 10:35:04.927425+01	45.81989686025145	45.81989686025145	3	6	1	27	6
372	0	Excellent work!	2025-10-01 10:35:04.933158+01	2	t	2025-10-01 10:35:04.933159+01	61.699742267729036	61.699742267729036	4	6	1	27	6
373	23.256490393875943	Good performance	2025-10-01 10:35:04.939602+01	0	f	2025-10-01 10:35:04.939602+01	0	23.256490393875943	1	6	2	27	6
374	21.393223548867738	Good performance	2025-10-01 10:35:04.945745+01	0	f	2025-10-01 10:35:04.945745+01	0	21.393223548867738	2	6	2	27	6
375	0	Good performance	2025-10-01 10:35:04.951662+01	1.7	t	2025-10-01 10:35:04.951662+01	56.50258823730319	56.50258823730319	3	6	2	27	6
376	0	Excellent work!	2025-10-01 10:35:04.95759+01	2.3	t	2025-10-01 10:35:04.95759+01	67.7974587804168	67.7974587804168	4	6	2	27	6
377	27.874883042889252	Excellent work!	2025-10-01 10:35:04.963743+01	0	f	2025-10-01 10:35:04.963744+01	0	27.874883042889252	1	9	1	27	11
378	4.017468591272878	Requires significant improvement	2025-10-01 10:35:04.969804+01	0	f	2025-10-01 10:35:04.969805+01	0	4.017468591272878	2	9	1	27	11
379	0	Excellent work!	2025-10-01 10:35:04.97674+01	2	t	2025-10-01 10:35:04.976741+01	60.910069585414846	60.910069585414846	3	9	1	27	11
380	0	Requires significant improvement	2025-10-01 10:35:04.983157+01	0	f	2025-10-01 10:35:04.983158+01	11.663158996527486	11.663158996527486	4	9	1	27	11
381	14.771417199966931	Requires significant improvement	2025-10-01 10:35:04.989459+01	0	f	2025-10-01 10:35:04.989459+01	0	14.771417199966931	1	9	2	27	11
382	23.972641958779906	Good performance	2025-10-01 10:35:04.995237+01	0	f	2025-10-01 10:35:04.995237+01	0	23.972641958779906	2	9	2	27	11
383	0	Excellent work!	2025-10-01 10:35:05.001307+01	2	t	2025-10-01 10:35:05.001307+01	64.15994154244717	64.15994154244717	3	9	2	27	11
384	0	Requires significant improvement	2025-10-01 10:35:05.008684+01	0	f	2025-10-01 10:35:05.008685+01	27.898089958718877	27.898089958718877	4	9	2	27	11
385	28.400591466948583	Excellent work!	2025-10-01 10:35:05.015156+01	0	f	2025-10-01 10:35:05.015157+01	0	28.400591466948583	1	12	1	27	16
386	3.354732920609948	Requires significant improvement	2025-10-01 10:35:05.0212+01	0	f	2025-10-01 10:35:05.0212+01	0	3.354732920609948	2	12	1	27	16
387	0	Good performance	2025-10-01 10:35:05.027542+01	1	f	2025-10-01 10:35:05.027542+01	49.23144249859005	49.23144249859005	3	12	1	27	16
388	0	Satisfactory	2025-10-01 10:35:05.033501+01	0	f	2025-10-01 10:35:05.033501+01	42.69726148768444	42.69726148768444	4	12	1	27	16
389	27.995981343891668	Excellent work!	2025-10-01 10:35:05.040805+01	0	f	2025-10-01 10:35:05.040805+01	0	27.995981343891668	1	12	2	27	16
390	20.12035511470539	Satisfactory	2025-10-01 10:35:05.047659+01	0	f	2025-10-01 10:35:05.047659+01	0	20.12035511470539	2	12	2	27	16
391	0	Needs improvement	2025-10-01 10:35:05.054188+01	0	f	2025-10-01 10:35:05.054188+01	38.030213334136995	38.030213334136995	3	12	2	27	16
392	0	Excellent work!	2025-10-01 10:35:05.060808+01	2	t	2025-10-01 10:35:05.060809+01	60.76112602661742	60.76112602661742	4	12	2	27	16
393	22.46221855986555	Good performance	2025-10-01 10:35:05.067132+01	0	f	2025-10-01 10:35:05.067132+01	0	22.46221855986555	1	15	1	27	21
394	19.424002094193174	Satisfactory	2025-10-01 10:35:05.074156+01	0	f	2025-10-01 10:35:05.074156+01	0	19.424002094193174	2	15	1	27	21
395	0	Good performance	2025-10-01 10:35:05.080343+01	1.7	t	2025-10-01 10:35:05.080344+01	57.71675301482945	57.71675301482945	3	15	1	27	21
396	0	Needs improvement	2025-10-01 10:35:05.086444+01	0	f	2025-10-01 10:35:05.086445+01	38.81861463315276	38.81861463315276	4	15	1	27	21
397	24.729714855386046	Good performance	2025-10-01 10:35:05.095109+01	0	f	2025-10-01 10:35:05.09511+01	0	24.729714855386046	1	15	2	27	21
398	28.416510754727707	Excellent work!	2025-10-01 10:35:05.102072+01	0	f	2025-10-01 10:35:05.102072+01	0	28.416510754727707	2	15	2	27	21
399	0	Requires significant improvement	2025-10-01 10:35:05.108346+01	0	f	2025-10-01 10:35:05.108346+01	14.575721340736452	14.575721340736452	3	15	2	27	21
400	0	Excellent work!	2025-10-01 10:35:05.114083+01	2	t	2025-10-01 10:35:05.114083+01	64.06041314986581	64.06041314986581	4	15	2	27	21
401	29.375383627273386	Excellent work!	2025-10-01 10:35:05.120261+01	0	f	2025-10-01 10:35:05.120262+01	0	29.375383627273386	1	2	1	28	1
402	26.107405989522604	Excellent work!	2025-10-01 10:35:05.126084+01	0	f	2025-10-01 10:35:05.126085+01	0	26.107405989522604	2	2	1	28	1
403	0	Excellent work!	2025-10-01 10:35:05.131899+01	2	t	2025-10-01 10:35:05.131899+01	63.627834614590924	63.627834614590924	3	2	1	28	1
404	0	Needs improvement	2025-10-01 10:35:05.138114+01	0	f	2025-10-01 10:35:05.138114+01	40.65103668512902	40.65103668512902	4	2	1	28	1
405	23.481928310693846	Good performance	2025-10-01 10:35:05.144166+01	0	f	2025-10-01 10:35:05.144166+01	0	23.481928310693846	1	2	2	28	1
406	25.30796759868065	Good performance	2025-10-01 10:35:05.150766+01	0	f	2025-10-01 10:35:05.150766+01	0	25.30796759868065	2	2	2	28	1
407	0	Needs improvement	2025-10-01 10:35:05.156515+01	0	f	2025-10-01 10:35:05.156515+01	39.305330926106144	39.305330926106144	3	2	2	28	1
408	0	Excellent work!	2025-10-01 10:35:05.162968+01	2.3	t	2025-10-01 10:35:05.162969+01	67.20618511827139	67.20618511827139	4	2	2	28	1
409	19.669175026961206	Satisfactory	2025-10-01 10:35:05.169709+01	0	f	2025-10-01 10:35:05.169709+01	0	19.669175026961206	1	6	1	28	6
410	24.700372999188954	Good performance	2025-10-01 10:35:05.175838+01	0	f	2025-10-01 10:35:05.175838+01	0	24.700372999188954	2	6	1	28	6
411	0	Needs improvement	2025-10-01 10:35:05.1825+01	0	f	2025-10-01 10:35:05.1825+01	36.71832788510952	36.71832788510952	3	6	1	28	6
412	0	Good performance	2025-10-01 10:35:05.189006+01	1.3	t	2025-10-01 10:35:05.189007+01	50.93439717975981	50.93439717975981	4	6	1	28	6
413	21.433302189006596	Good performance	2025-10-01 10:35:05.195163+01	0	f	2025-10-01 10:35:05.195164+01	0	21.433302189006596	1	6	2	28	6
414	18.042548286364703	Satisfactory	2025-10-01 10:35:05.201291+01	0	f	2025-10-01 10:35:05.201291+01	0	18.042548286364703	2	6	2	28	6
415	0	Excellent work!	2025-10-01 10:35:05.207474+01	2	t	2025-10-01 10:35:05.207474+01	60.29157827284867	60.29157827284867	3	6	2	28	6
416	0	Good performance	2025-10-01 10:35:05.213716+01	1.3	t	2025-10-01 10:35:05.213717+01	51.65524076360124	51.65524076360124	4	6	2	28	6
417	28.92014759292511	Excellent work!	2025-10-01 10:35:05.220087+01	0	f	2025-10-01 10:35:05.220087+01	0	28.92014759292511	1	9	1	28	11
418	19.45061014792001	Satisfactory	2025-10-01 10:35:05.225924+01	0	f	2025-10-01 10:35:05.225924+01	0	19.45061014792001	2	9	1	28	11
419	0	Good performance	2025-10-01 10:35:05.232381+01	1.3	t	2025-10-01 10:35:05.232381+01	51.45110981254269	51.45110981254269	3	9	1	28	11
420	0	Satisfactory	2025-10-01 10:35:05.23861+01	0	f	2025-10-01 10:35:05.238611+01	42.1248685147583	42.1248685147583	4	9	1	28	11
421	1.9065629068021424	Requires significant improvement	2025-10-01 10:35:05.244381+01	0	f	2025-10-01 10:35:05.244381+01	0	1.9065629068021424	1	9	2	28	11
422	2.860944269216708	Requires significant improvement	2025-10-01 10:35:05.250397+01	0	f	2025-10-01 10:35:05.250397+01	0	2.860944269216708	2	9	2	28	11
423	0	Excellent work!	2025-10-01 10:35:05.256236+01	2.3	t	2025-10-01 10:35:05.256237+01	69.4091251186521	69.4091251186521	3	9	2	28	11
424	0	Excellent work!	2025-10-01 10:35:05.262222+01	2.3	t	2025-10-01 10:35:05.262222+01	67.4404243438592	67.4404243438592	4	9	2	28	11
425	21.78498972748975	Good performance	2025-10-01 10:35:05.268471+01	0	f	2025-10-01 10:35:05.268472+01	0	21.78498972748975	1	12	1	28	16
426	12.851702855357857	Requires significant improvement	2025-10-01 10:35:05.275171+01	0	f	2025-10-01 10:35:05.275171+01	0	12.851702855357857	2	12	1	28	16
427	0	Excellent work!	2025-10-01 10:35:05.281063+01	2.3	t	2025-10-01 10:35:05.281063+01	68.68866862350107	68.68866862350107	3	12	1	28	16
428	0	Requires significant improvement	2025-10-01 10:35:05.287527+01	0	f	2025-10-01 10:35:05.287527+01	6.3496252628572005	6.3496252628572005	4	12	1	28	16
429	22.054054273850802	Good performance	2025-10-01 10:35:05.293395+01	0	f	2025-10-01 10:35:05.293396+01	0	22.054054273850802	1	12	2	28	16
430	27.629135250549837	Excellent work!	2025-10-01 10:35:05.29922+01	0	f	2025-10-01 10:35:05.299221+01	0	27.629135250549837	2	12	2	28	16
431	0	Excellent work!	2025-10-01 10:35:05.30615+01	2	t	2025-10-01 10:35:05.30615+01	62.78209629610089	62.78209629610089	3	12	2	28	16
432	0	Excellent work!	2025-10-01 10:35:05.312217+01	2	t	2025-10-01 10:35:05.312217+01	62.59289669143752	62.59289669143752	4	12	2	28	16
433	29.90764951023085	Excellent work!	2025-10-01 10:35:05.318665+01	0	f	2025-10-01 10:35:05.318665+01	0	29.90764951023085	1	15	1	28	21
434	27.75502856723071	Excellent work!	2025-10-01 10:35:05.324728+01	0	f	2025-10-01 10:35:05.324728+01	0	27.75502856723071	2	15	1	28	21
435	0	Requires significant improvement	2025-10-01 10:35:05.331208+01	0	f	2025-10-01 10:35:05.331209+01	3.9547897895885575	3.9547897895885575	3	15	1	28	21
436	0	Good performance	2025-10-01 10:35:05.338343+01	1.7	t	2025-10-01 10:35:05.338343+01	56.82167299900377	56.82167299900377	4	15	1	28	21
437	8.790262543370138	Requires significant improvement	2025-10-01 10:35:05.344776+01	0	f	2025-10-01 10:35:05.344776+01	0	8.790262543370138	1	15	2	28	21
438	28.80344302415826	Excellent work!	2025-10-01 10:35:05.351646+01	0	f	2025-10-01 10:35:05.351646+01	0	28.80344302415826	2	15	2	28	21
439	0	Excellent work!	2025-10-01 10:35:05.35801+01	2.3	t	2025-10-01 10:35:05.35801+01	65.2746225190718	65.2746225190718	3	15	2	28	21
440	0	Requires significant improvement	2025-10-01 10:35:05.364122+01	0	f	2025-10-01 10:35:05.364123+01	8.580107347613298	8.580107347613298	4	15	2	28	21
441	5.6780786494328455	Requires significant improvement	2025-10-01 10:35:05.371202+01	0	f	2025-10-01 10:35:05.371203+01	0	5.6780786494328455	1	2	1	29	1
442	2.7303102621471997	Requires significant improvement	2025-10-01 10:35:05.377558+01	0	f	2025-10-01 10:35:05.377558+01	0	2.7303102621471997	2	2	1	29	1
443	0	Excellent work!	2025-10-01 10:35:05.385658+01	2.3	t	2025-10-01 10:35:05.385658+01	66.49774720112893	66.49774720112893	3	2	1	29	1
444	0	Good performance	2025-10-01 10:35:05.391776+01	1.3	t	2025-10-01 10:35:05.391776+01	54.74978773741415	54.74978773741415	4	2	1	29	1
445	15.007303428481912	Needs improvement	2025-10-01 10:35:05.398095+01	0	f	2025-10-01 10:35:05.398096+01	0	15.007303428481912	1	2	2	29	1
446	28.75766085490591	Excellent work!	2025-10-01 10:35:05.405376+01	0	f	2025-10-01 10:35:05.405376+01	0	28.75766085490591	2	2	2	29	1
447	0	Needs improvement	2025-10-01 10:35:05.411703+01	0	f	2025-10-01 10:35:05.411703+01	37.17842583448099	37.17842583448099	3	2	2	29	1
448	0	Excellent work!	2025-10-01 10:35:05.41832+01	2.3	t	2025-10-01 10:35:05.41832+01	68.85744607444565	68.85744607444565	4	2	2	29	1
449	22.414507064889435	Good performance	2025-10-01 10:35:05.42437+01	0	f	2025-10-01 10:35:05.424371+01	0	22.414507064889435	1	6	1	29	6
450	27.34664692400109	Excellent work!	2025-10-01 10:35:05.430468+01	0	f	2025-10-01 10:35:05.430469+01	0	27.34664692400109	2	6	1	29	6
451	0	Excellent work!	2025-10-01 10:35:05.437186+01	2.3	t	2025-10-01 10:35:05.437186+01	65.79423541585284	65.79423541585284	3	6	1	29	6
452	0	Satisfactory	2025-10-01 10:35:05.443504+01	0	f	2025-10-01 10:35:05.443505+01	42.61438514869473	42.61438514869473	4	6	1	29	6
453	7.6569398943018125	Requires significant improvement	2025-10-01 10:35:05.44976+01	0	f	2025-10-01 10:35:05.44976+01	0	7.6569398943018125	1	6	2	29	6
454	15.790069827897181	Needs improvement	2025-10-01 10:35:05.460684+01	0	f	2025-10-01 10:35:05.460685+01	0	15.790069827897181	2	6	2	29	6
455	0	Satisfactory	2025-10-01 10:35:05.470476+01	0	f	2025-10-01 10:35:05.470477+01	43.0417973258619	43.0417973258619	3	6	2	29	6
456	0	Good performance	2025-10-01 10:35:05.47741+01	1.7	t	2025-10-01 10:35:05.477411+01	59.25927410504339	59.25927410504339	4	6	2	29	6
457	25.419026475497184	Good performance	2025-10-01 10:35:05.484106+01	0	f	2025-10-01 10:35:05.484106+01	0	25.419026475497184	1	9	1	29	11
458	23.79240217866901	Good performance	2025-10-01 10:35:05.490609+01	0	f	2025-10-01 10:35:05.49061+01	0	23.79240217866901	2	9	1	29	11
459	0	Good performance	2025-10-01 10:35:05.496692+01	1.3	t	2025-10-01 10:35:05.496692+01	50.36096284473898	50.36096284473898	3	9	1	29	11
460	0	Needs improvement	2025-10-01 10:35:05.503194+01	0	f	2025-10-01 10:35:05.503195+01	36.08964692144189	36.08964692144189	4	9	1	29	11
461	22.087769296734095	Good performance	2025-10-01 10:35:05.509858+01	0	f	2025-10-01 10:35:05.509858+01	0	22.087769296734095	1	9	2	29	11
462	25.824357077751444	Excellent work!	2025-10-01 10:35:05.51606+01	0	f	2025-10-01 10:35:05.516061+01	0	25.824357077751444	2	9	2	29	11
463	0	Requires significant improvement	2025-10-01 10:35:05.522531+01	0	f	2025-10-01 10:35:05.522531+01	31.44658113409141	31.44658113409141	3	9	2	29	11
464	0	Excellent work!	2025-10-01 10:35:05.529087+01	2	t	2025-10-01 10:35:05.529087+01	63.35120916366059	63.35120916366059	4	9	2	29	11
465	15.871001922942199	Needs improvement	2025-10-01 10:35:05.535513+01	0	f	2025-10-01 10:35:05.535513+01	0	15.871001922942199	1	12	1	29	16
466	20.55159173222051	Satisfactory	2025-10-01 10:35:05.542502+01	0	f	2025-10-01 10:35:05.542503+01	0	20.55159173222051	2	12	1	29	16
467	0	Excellent work!	2025-10-01 10:35:05.548722+01	2.3	t	2025-10-01 10:35:05.548723+01	66.79080430819772	66.79080430819772	3	12	1	29	16
468	0	Excellent work!	2025-10-01 10:35:05.555165+01	2	t	2025-10-01 10:35:05.555166+01	60.387423398784975	60.387423398784975	4	12	1	29	16
469	17.90676124915395	Needs improvement	2025-10-01 10:35:05.561479+01	0	f	2025-10-01 10:35:05.56148+01	0	17.90676124915395	1	12	2	29	16
470	1.81223860584802	Requires significant improvement	2025-10-01 10:35:05.568129+01	0	f	2025-10-01 10:35:05.56813+01	0	1.81223860584802	2	12	2	29	16
471	0	Requires significant improvement	2025-10-01 10:35:05.575628+01	0	f	2025-10-01 10:35:05.575628+01	29.808660834744643	29.808660834744643	3	12	2	29	16
472	0	Satisfactory	2025-10-01 10:35:05.581927+01	1	f	2025-10-01 10:35:05.581927+01	46.787728513482726	46.787728513482726	4	12	2	29	16
473	17.430370744929277	Needs improvement	2025-10-01 10:35:05.588814+01	0	f	2025-10-01 10:35:05.588815+01	0	17.430370744929277	1	15	1	29	21
474	25.642056045978784	Excellent work!	2025-10-01 10:35:05.595125+01	0	f	2025-10-01 10:35:05.595125+01	0	25.642056045978784	2	15	1	29	21
475	0	Excellent work!	2025-10-01 10:35:05.601824+01	2	t	2025-10-01 10:35:05.601824+01	64.34668617585828	64.34668617585828	3	15	1	29	21
476	0	Good performance	2025-10-01 10:35:05.609326+01	1.7	t	2025-10-01 10:35:05.609326+01	56.78108098593952	56.78108098593952	4	15	1	29	21
477	27.640858769917998	Excellent work!	2025-10-01 10:35:05.615791+01	0	f	2025-10-01 10:35:05.615792+01	0	27.640858769917998	1	15	2	29	21
478	27.7332944478337	Excellent work!	2025-10-01 10:35:05.622678+01	0	f	2025-10-01 10:35:05.622679+01	0	27.7332944478337	2	15	2	29	21
479	0	Excellent work!	2025-10-01 10:35:05.629223+01	2	t	2025-10-01 10:35:05.629224+01	62.17753941406875	62.17753941406875	3	15	2	29	21
480	0	Requires significant improvement	2025-10-01 10:35:05.635984+01	0	f	2025-10-01 10:35:05.635985+01	4.548220076592661	4.548220076592661	4	15	2	29	21
481	14.957557564636026	Requires significant improvement	2025-10-01 10:35:05.643134+01	0	f	2025-10-01 10:35:05.643135+01	0	14.957557564636026	1	2	1	30	1
482	19.9298895862305	Satisfactory	2025-10-01 10:35:05.649555+01	0	f	2025-10-01 10:35:05.649556+01	0	19.9298895862305	2	2	1	30	1
483	0	Satisfactory	2025-10-01 10:35:05.656391+01	1	f	2025-10-01 10:35:05.656392+01	46.64862181018748	46.64862181018748	3	2	1	30	1
484	0	Excellent work!	2025-10-01 10:35:05.662966+01	1.7	t	2025-10-01 10:35:05.662967+01	59.763409560868034	59.763409560868034	4	2	1	30	1
485	17.947378086019075	Needs improvement	2025-10-01 10:35:05.670129+01	0	f	2025-10-01 10:35:05.67013+01	0	17.947378086019075	1	2	2	30	1
486	26.23548606066366	Excellent work!	2025-10-01 10:35:05.677106+01	0	f	2025-10-01 10:35:05.677106+01	0	26.23548606066366	2	2	2	30	1
487	0	Satisfactory	2025-10-01 10:35:05.683511+01	1	f	2025-10-01 10:35:05.683511+01	45.94744883125468	45.94744883125468	3	2	2	30	1
488	0	Excellent work!	2025-10-01 10:35:05.69032+01	2.3	t	2025-10-01 10:35:05.690321+01	67.78627023086268	67.78627023086268	4	2	2	30	1
489	20.168263247814505	Satisfactory	2025-10-01 10:35:05.696669+01	0	f	2025-10-01 10:35:05.69667+01	0	20.168263247814505	1	6	1	30	6
490	22.972515105868933	Good performance	2025-10-01 10:35:05.703649+01	0	f	2025-10-01 10:35:05.70365+01	0	22.972515105868933	2	6	1	30	6
491	0	Good performance	2025-10-01 10:35:05.710549+01	1.7	t	2025-10-01 10:35:05.71055+01	56.93737486810654	56.93737486810654	3	6	1	30	6
492	0	Satisfactory	2025-10-01 10:35:05.71691+01	1	f	2025-10-01 10:35:05.71691+01	47.52273082522021	47.52273082522021	4	6	1	30	6
493	29.583721492154535	Excellent work!	2025-10-01 10:35:05.72355+01	0	f	2025-10-01 10:35:05.72355+01	0	29.583721492154535	1	6	2	30	6
494	16.988209532744985	Needs improvement	2025-10-01 10:35:05.730425+01	0	f	2025-10-01 10:35:05.730426+01	0	16.988209532744985	2	6	2	30	6
495	0	Requires significant improvement	2025-10-01 10:35:05.737559+01	0	f	2025-10-01 10:35:05.73756+01	5.362050502467316	5.362050502467316	3	6	2	30	6
496	0	Excellent work!	2025-10-01 10:35:05.744647+01	2.3	t	2025-10-01 10:35:05.744647+01	67.63716678657482	67.63716678657482	4	6	2	30	6
497	18.869289266206277	Satisfactory	2025-10-01 10:35:05.751588+01	0	f	2025-10-01 10:35:05.751589+01	0	18.869289266206277	1	9	1	30	11
498	19.699586614008346	Satisfactory	2025-10-01 10:35:05.758433+01	0	f	2025-10-01 10:35:05.758434+01	0	19.699586614008346	2	9	1	30	11
499	0	Excellent work!	2025-10-01 10:35:05.764886+01	2	t	2025-10-01 10:35:05.764886+01	64.6938560092797	64.6938560092797	3	9	1	30	11
500	0	Requires significant improvement	2025-10-01 10:35:05.772732+01	0	f	2025-10-01 10:35:05.772732+01	12.744585465581483	12.744585465581483	4	9	1	30	11
501	28.889394461617456	Excellent work!	2025-10-01 10:35:05.779626+01	0	f	2025-10-01 10:35:05.779627+01	0	28.889394461617456	1	9	2	30	11
502	16.262625820309975	Needs improvement	2025-10-01 10:35:05.786878+01	0	f	2025-10-01 10:35:05.786878+01	0	16.262625820309975	2	9	2	30	11
503	0	Good performance	2025-10-01 10:35:05.793772+01	1.3	t	2025-10-01 10:35:05.793773+01	53.52123102540326	53.52123102540326	3	9	2	30	11
504	0	Excellent work!	2025-10-01 10:35:05.800923+01	2.3	t	2025-10-01 10:35:05.800924+01	68.24799639495335	68.24799639495335	4	9	2	30	11
505	19.489747095274662	Satisfactory	2025-10-01 10:35:05.808615+01	0	f	2025-10-01 10:35:05.808616+01	0	19.489747095274662	1	12	1	30	16
506	5.281822162807617	Requires significant improvement	2025-10-01 10:35:05.815378+01	0	f	2025-10-01 10:35:05.815379+01	0	5.281822162807617	2	12	1	30	16
507	0	Needs improvement	2025-10-01 10:35:05.822164+01	0	f	2025-10-01 10:35:05.822164+01	37.94828749396351	37.94828749396351	3	12	1	30	16
508	0	Excellent work!	2025-10-01 10:35:05.829314+01	2	t	2025-10-01 10:35:05.829314+01	60.141398829992994	60.141398829992994	4	12	1	30	16
509	29.52512920962168	Excellent work!	2025-10-01 10:35:05.836492+01	0	f	2025-10-01 10:35:05.836492+01	0	29.52512920962168	1	12	2	30	16
510	24.01743496157354	Good performance	2025-10-01 10:35:05.843494+01	0	f	2025-10-01 10:35:05.843495+01	0	24.01743496157354	2	12	2	30	16
511	0	Good performance	2025-10-01 10:35:05.849704+01	1.3	t	2025-10-01 10:35:05.849704+01	54.42575173067113	54.42575173067113	3	12	2	30	16
512	0	Satisfactory	2025-10-01 10:35:05.857415+01	0	f	2025-10-01 10:35:05.857415+01	42.10389521013684	42.10389521013684	4	12	2	30	16
513	29.73337001975207	Excellent work!	2025-10-01 10:35:05.864056+01	0	f	2025-10-01 10:35:05.864057+01	0	29.73337001975207	1	15	1	30	21
514	21.67745994180309	Good performance	2025-10-01 10:35:05.871701+01	0	f	2025-10-01 10:35:05.871702+01	0	21.67745994180309	2	15	1	30	21
515	0	Needs improvement	2025-10-01 10:35:05.878225+01	0	f	2025-10-01 10:35:05.878225+01	37.013135952530185	37.013135952530185	3	15	1	30	21
516	0	Excellent work!	2025-10-01 10:35:05.884735+01	2	t	2025-10-01 10:35:05.884736+01	63.77809494741712	63.77809494741712	4	15	1	30	21
517	26.38769833906918	Excellent work!	2025-10-01 10:35:05.893908+01	0	f	2025-10-01 10:35:05.893908+01	0	26.38769833906918	1	15	2	30	21
518	15.385015733646107	Needs improvement	2025-10-01 10:35:05.901283+01	0	f	2025-10-01 10:35:05.901284+01	0	15.385015733646107	2	15	2	30	21
519	0	Excellent work!	2025-10-01 10:35:05.908804+01	2	t	2025-10-01 10:35:05.908804+01	64.68797753370919	64.68797753370919	3	15	2	30	21
520	0	Needs improvement	2025-10-01 10:35:05.916223+01	0	f	2025-10-01 10:35:05.916224+01	40.589417980203066	40.589417980203066	4	15	2	30	21
521	26.113826919249657	Excellent work!	2025-10-01 10:35:05.923909+01	0	f	2025-10-01 10:35:05.923909+01	0	26.113826919249657	1	2	1	31	1
522	15.474096972218783	Needs improvement	2025-10-01 10:35:05.932913+01	0	f	2025-10-01 10:35:05.932914+01	0	15.474096972218783	2	2	1	31	1
523	0	Good performance	2025-10-01 10:35:05.940816+01	1.7	t	2025-10-01 10:35:05.940817+01	55.4403268177897	55.4403268177897	3	2	1	31	1
524	0	Satisfactory	2025-10-01 10:35:05.947854+01	0	f	2025-10-01 10:35:05.947855+01	43.823454680753045	43.823454680753045	4	2	1	31	1
525	25.13499485622438	Good performance	2025-10-01 10:35:05.957249+01	0	f	2025-10-01 10:35:05.957249+01	0	25.13499485622438	1	2	2	31	1
526	21.627573761437567	Good performance	2025-10-01 10:35:05.964538+01	0	f	2025-10-01 10:35:05.964539+01	0	21.627573761437567	2	2	2	31	1
527	0	Requires significant improvement	2025-10-01 10:35:05.972792+01	0	f	2025-10-01 10:35:05.972793+01	13.901567176315494	13.901567176315494	3	2	2	31	1
528	0	Excellent work!	2025-10-01 10:35:05.980055+01	2.3	t	2025-10-01 10:35:05.980055+01	67.43249318539068	67.43249318539068	4	2	2	31	1
529	23.781433375221862	Good performance	2025-10-01 10:35:05.987436+01	0	f	2025-10-01 10:35:05.987437+01	0	23.781433375221862	1	6	1	31	6
530	16.22759717055156	Needs improvement	2025-10-01 10:35:05.99458+01	0	f	2025-10-01 10:35:05.994581+01	0	16.22759717055156	2	6	1	31	6
531	0	Excellent work!	2025-10-01 10:35:06.001609+01	2.3	t	2025-10-01 10:35:06.00161+01	66.86449851520103	66.86449851520103	3	6	1	31	6
532	0	Satisfactory	2025-10-01 10:35:06.009109+01	0	f	2025-10-01 10:35:06.00911+01	42.223768138111154	42.223768138111154	4	6	1	31	6
533	25.211682122341774	Good performance	2025-10-01 10:35:06.01615+01	0	f	2025-10-01 10:35:06.01615+01	0	25.211682122341774	1	6	2	31	6
534	13.600766877508207	Requires significant improvement	2025-10-01 10:35:06.023047+01	0	f	2025-10-01 10:35:06.023048+01	0	13.600766877508207	2	6	2	31	6
535	0	Good performance	2025-10-01 10:35:06.030241+01	1.3	t	2025-10-01 10:35:06.030242+01	54.828265523896874	54.828265523896874	3	6	2	31	6
536	0	Good performance	2025-10-01 10:35:06.038247+01	1.3	t	2025-10-01 10:35:06.038247+01	54.123428448097776	54.123428448097776	4	6	2	31	6
537	22.75687319532591	Good performance	2025-10-01 10:35:06.045328+01	0	f	2025-10-01 10:35:06.045329+01	0	22.75687319532591	1	9	1	31	11
538	20.413504773814243	Satisfactory	2025-10-01 10:35:06.05262+01	0	f	2025-10-01 10:35:06.052621+01	0	20.413504773814243	2	9	1	31	11
539	0	Good performance	2025-10-01 10:35:06.05952+01	1.7	t	2025-10-01 10:35:06.059521+01	56.9827893689743	56.9827893689743	3	9	1	31	11
540	0	Good performance	2025-10-01 10:35:06.066592+01	1.3	t	2025-10-01 10:35:06.066592+01	50.64436281257484	50.64436281257484	4	9	1	31	11
541	6.486541593433673	Requires significant improvement	2025-10-01 10:35:06.073755+01	0	f	2025-10-01 10:35:06.073756+01	0	6.486541593433673	1	9	2	31	11
542	25.867244177625075	Excellent work!	2025-10-01 10:35:06.080549+01	0	f	2025-10-01 10:35:06.080549+01	0	25.867244177625075	2	9	2	31	11
543	0	Excellent work!	2025-10-01 10:35:06.087724+01	2	t	2025-10-01 10:35:06.087725+01	64.37702198181898	64.37702198181898	3	9	2	31	11
544	0	Needs improvement	2025-10-01 10:35:06.094622+01	0	f	2025-10-01 10:35:06.094622+01	36.77211704177539	36.77211704177539	4	9	2	31	11
545	24.380715079917415	Good performance	2025-10-01 10:35:06.10147+01	0	f	2025-10-01 10:35:06.101471+01	0	24.380715079917415	1	12	1	31	16
546	26.998113708519917	Excellent work!	2025-10-01 10:35:06.108639+01	0	f	2025-10-01 10:35:06.10864+01	0	26.998113708519917	2	12	1	31	16
547	0	Good performance	2025-10-01 10:35:06.115484+01	1.3	t	2025-10-01 10:35:06.115484+01	51.15105579473679	51.15105579473679	3	12	1	31	16
548	0	Excellent work!	2025-10-01 10:35:06.128752+01	2	t	2025-10-01 10:35:06.128753+01	63.29080361974127	63.29080361974127	4	12	1	31	16
549	26.269822782184427	Excellent work!	2025-10-01 10:35:06.136249+01	0	f	2025-10-01 10:35:06.13625+01	0	26.269822782184427	1	12	2	31	16
550	24.94358358314782	Good performance	2025-10-01 10:35:06.143694+01	0	f	2025-10-01 10:35:06.143694+01	0	24.94358358314782	2	12	2	31	16
551	0	Excellent work!	2025-10-01 10:35:06.151479+01	2.3	t	2025-10-01 10:35:06.151479+01	69.95712215165062	69.95712215165062	3	12	2	31	16
552	0	Excellent work!	2025-10-01 10:35:06.158447+01	2.3	t	2025-10-01 10:35:06.158448+01	65.9174061582098	65.9174061582098	4	12	2	31	16
553	29.34618373482776	Excellent work!	2025-10-01 10:35:06.165346+01	0	f	2025-10-01 10:35:06.165347+01	0	29.34618373482776	1	15	1	31	21
554	17.99124512365966	Needs improvement	2025-10-01 10:35:06.173192+01	0	f	2025-10-01 10:35:06.173193+01	0	17.99124512365966	2	15	1	31	21
555	0	Excellent work!	2025-10-01 10:35:06.180333+01	2	t	2025-10-01 10:35:06.180334+01	63.50832359067097	63.50832359067097	3	15	1	31	21
556	0	Excellent work!	2025-10-01 10:35:06.187748+01	2	t	2025-10-01 10:35:06.187749+01	62.793012082971345	62.793012082971345	4	15	1	31	21
557	16.34616051331281	Needs improvement	2025-10-01 10:35:06.194789+01	0	f	2025-10-01 10:35:06.194789+01	0	16.34616051331281	1	15	2	31	21
558	29.100394589089404	Excellent work!	2025-10-01 10:35:06.20183+01	0	f	2025-10-01 10:35:06.20183+01	0	29.100394589089404	2	15	2	31	21
559	0	Needs improvement	2025-10-01 10:35:06.209286+01	0	f	2025-10-01 10:35:06.209286+01	41.446858704096265	41.446858704096265	3	15	2	31	21
560	0	Requires significant improvement	2025-10-01 10:35:06.216626+01	0	f	2025-10-01 10:35:06.216627+01	11.2912420456297	11.2912420456297	4	15	2	31	21
561	20.770887562451914	Satisfactory	2025-10-01 10:35:06.22373+01	0	f	2025-10-01 10:35:06.22373+01	0	20.770887562451914	1	2	1	32	1
562	28.022233385580105	Excellent work!	2025-10-01 10:35:06.230735+01	0	f	2025-10-01 10:35:06.230736+01	0	28.022233385580105	2	2	1	32	1
563	0	Needs improvement	2025-10-01 10:35:06.238322+01	0	f	2025-10-01 10:35:06.238322+01	37.674210798071336	37.674210798071336	3	2	1	32	1
564	0	Good performance	2025-10-01 10:35:06.245253+01	1.7	t	2025-10-01 10:35:06.245254+01	59.27729248938995	59.27729248938995	4	2	1	32	1
565	4.3792658488352165	Requires significant improvement	2025-10-01 10:35:06.252539+01	0	f	2025-10-01 10:35:06.25254+01	0	4.3792658488352165	1	2	2	32	1
566	8.049818885161487	Requires significant improvement	2025-10-01 10:35:06.259558+01	0	f	2025-10-01 10:35:06.259559+01	0	8.049818885161487	2	2	2	32	1
567	0	Good performance	2025-10-01 10:35:06.26661+01	1.3	t	2025-10-01 10:35:06.266611+01	52.92669101488959	52.92669101488959	3	2	2	32	1
568	0	Excellent work!	2025-10-01 10:35:06.274051+01	2	t	2025-10-01 10:35:06.274051+01	60.83978658489873	60.83978658489873	4	2	2	32	1
569	25.961820947285368	Excellent work!	2025-10-01 10:35:06.281042+01	0	f	2025-10-01 10:35:06.281043+01	0	25.961820947285368	1	6	1	32	6
570	27.4014509456786	Excellent work!	2025-10-01 10:35:06.288283+01	0	f	2025-10-01 10:35:06.288284+01	0	27.4014509456786	2	6	1	32	6
571	0	Excellent work!	2025-10-01 10:35:06.295594+01	2	t	2025-10-01 10:35:06.295594+01	63.305214758745166	63.305214758745166	3	6	1	32	6
572	0	Requires significant improvement	2025-10-01 10:35:06.302752+01	0	f	2025-10-01 10:35:06.302752+01	14.427731798700638	14.427731798700638	4	6	1	32	6
573	28.640630367106123	Excellent work!	2025-10-01 10:35:06.309856+01	0	f	2025-10-01 10:35:06.309856+01	0	28.640630367106123	1	6	2	32	6
574	18.41104795588879	Satisfactory	2025-10-01 10:35:06.31715+01	0	f	2025-10-01 10:35:06.317151+01	0	18.41104795588879	2	6	2	32	6
575	0	Requires significant improvement	2025-10-01 10:35:06.324425+01	0	f	2025-10-01 10:35:06.324426+01	16.457101085113525	16.457101085113525	3	6	2	32	6
576	0	Requires significant improvement	2025-10-01 10:35:06.331868+01	0	f	2025-10-01 10:35:06.331868+01	27.46259790065653	27.46259790065653	4	6	2	32	6
577	28.55474533014833	Excellent work!	2025-10-01 10:35:06.339849+01	0	f	2025-10-01 10:35:06.33985+01	0	28.55474533014833	1	9	1	32	11
578	27.153084246042273	Excellent work!	2025-10-01 10:35:06.346927+01	0	f	2025-10-01 10:35:06.346927+01	0	27.153084246042273	2	9	1	32	11
579	0	Good performance	2025-10-01 10:35:06.354071+01	1.7	t	2025-10-01 10:35:06.354072+01	57.88767961206193	57.88767961206193	3	9	1	32	11
580	0	Excellent work!	2025-10-01 10:35:06.361117+01	2	t	2025-10-01 10:35:06.361117+01	60.00331026852417	60.00331026852417	4	9	1	32	11
581	16.885892051900566	Needs improvement	2025-10-01 10:35:06.368225+01	0	f	2025-10-01 10:35:06.368226+01	0	16.885892051900566	1	9	2	32	11
582	21.91487918665301	Good performance	2025-10-01 10:35:06.377773+01	0	f	2025-10-01 10:35:06.377774+01	0	21.91487918665301	2	9	2	32	11
583	0	Excellent work!	2025-10-01 10:35:06.385395+01	2	t	2025-10-01 10:35:06.385396+01	61.430199537443976	61.430199537443976	3	9	2	32	11
584	0	Satisfactory	2025-10-01 10:35:06.39453+01	0	f	2025-10-01 10:35:06.394531+01	44.417704094535964	44.417704094535964	4	9	2	32	11
585	11.679290636334287	Requires significant improvement	2025-10-01 10:35:06.401761+01	0	f	2025-10-01 10:35:06.401761+01	0	11.679290636334287	1	12	1	32	16
586	29.251890546765534	Excellent work!	2025-10-01 10:35:06.409411+01	0	f	2025-10-01 10:35:06.409412+01	0	29.251890546765534	2	12	1	32	16
587	0	Good performance	2025-10-01 10:35:06.416659+01	1.3	t	2025-10-01 10:35:06.41666+01	50.2962936241985	50.2962936241985	3	12	1	32	16
588	0	Excellent work!	2025-10-01 10:35:06.424383+01	2	t	2025-10-01 10:35:06.424383+01	61.29168081845665	61.29168081845665	4	12	1	32	16
589	18.802972774225633	Satisfactory	2025-10-01 10:35:06.431432+01	0	f	2025-10-01 10:35:06.431432+01	0	18.802972774225633	1	12	2	32	16
590	16.4549469793431	Needs improvement	2025-10-01 10:35:06.439343+01	0	f	2025-10-01 10:35:06.439344+01	0	16.4549469793431	2	12	2	32	16
591	0	Needs improvement	2025-10-01 10:35:06.446336+01	0	f	2025-10-01 10:35:06.446336+01	35.79400981923223	35.79400981923223	3	12	2	32	16
592	0	Needs improvement	2025-10-01 10:35:06.453431+01	0	f	2025-10-01 10:35:06.453432+01	39.71253960169704	39.71253960169704	4	12	2	32	16
593	22.535816700224924	Good performance	2025-10-01 10:35:06.46035+01	0	f	2025-10-01 10:35:06.460351+01	0	22.535816700224924	1	15	1	32	21
594	29.39307430667793	Excellent work!	2025-10-01 10:35:06.468048+01	0	f	2025-10-01 10:35:06.468048+01	0	29.39307430667793	2	15	1	32	21
595	0	Excellent work!	2025-10-01 10:35:06.475987+01	2	t	2025-10-01 10:35:06.475988+01	60.57403251728158	60.57403251728158	3	15	1	32	21
596	0	Satisfactory	2025-10-01 10:35:06.48321+01	0	f	2025-10-01 10:35:06.48321+01	43.94822666898678	43.94822666898678	4	15	1	32	21
597	8.255526870013073	Requires significant improvement	2025-10-01 10:35:06.490543+01	0	f	2025-10-01 10:35:06.490543+01	0	8.255526870013073	1	15	2	32	21
598	6.980442791653172	Requires significant improvement	2025-10-01 10:35:06.498109+01	0	f	2025-10-01 10:35:06.498109+01	0	6.980442791653172	2	15	2	32	21
599	0	Excellent work!	2025-10-01 10:35:06.506229+01	2.3	t	2025-10-01 10:35:06.506229+01	65.40880061310764	65.40880061310764	3	15	2	32	21
600	0	Excellent work!	2025-10-01 10:35:06.513745+01	2.3	t	2025-10-01 10:35:06.513745+01	66.81161034696379	66.81161034696379	4	15	2	32	21
601	23.42736254564335	Good performance	2025-10-01 10:35:06.521635+01	0	f	2025-10-01 10:35:06.521635+01	0	23.42736254564335	1	2	1	33	2
602	24.28921330655562	Good performance	2025-10-01 10:35:06.528926+01	0	f	2025-10-01 10:35:06.528926+01	0	24.28921330655562	2	2	1	33	2
603	0	Requires significant improvement	2025-10-01 10:35:06.537159+01	0	f	2025-10-01 10:35:06.53716+01	31.00161426058746	31.00161426058746	3	2	1	33	2
604	0	Requires significant improvement	2025-10-01 10:35:06.544192+01	0	f	2025-10-01 10:35:06.544192+01	13.816508502825734	13.816508502825734	4	2	1	33	2
605	21.690516123298337	Good performance	2025-10-01 10:35:06.552005+01	0	f	2025-10-01 10:35:06.552006+01	0	21.690516123298337	1	2	2	33	2
606	12.841352385305843	Requires significant improvement	2025-10-01 10:35:06.559451+01	0	f	2025-10-01 10:35:06.559452+01	0	12.841352385305843	2	2	2	33	2
607	0	Requires significant improvement	2025-10-01 10:35:06.566988+01	0	f	2025-10-01 10:35:06.566988+01	25.391962153394008	25.391962153394008	3	2	2	33	2
608	0	Needs improvement	2025-10-01 10:35:06.57539+01	0	f	2025-10-01 10:35:06.57539+01	38.92697896447246	38.92697896447246	4	2	2	33	2
609	4.919188798679668	Requires significant improvement	2025-10-01 10:35:06.582974+01	0	f	2025-10-01 10:35:06.582974+01	0	4.919188798679668	1	6	1	33	7
610	24.738772239083854	Good performance	2025-10-01 10:35:06.590515+01	0	f	2025-10-01 10:35:06.590515+01	0	24.738772239083854	2	6	1	33	7
611	0	Needs improvement	2025-10-01 10:35:06.598687+01	0	f	2025-10-01 10:35:06.598688+01	41.23499862859179	41.23499862859179	3	6	1	33	7
612	0	Needs improvement	2025-10-01 10:35:06.60677+01	0	f	2025-10-01 10:35:06.606771+01	36.75701280108729	36.75701280108729	4	6	1	33	7
613	25.458857243067907	Good performance	2025-10-01 10:35:06.613995+01	0	f	2025-10-01 10:35:06.613995+01	0	25.458857243067907	1	6	2	33	7
614	24.436549297341955	Good performance	2025-10-01 10:35:06.621663+01	0	f	2025-10-01 10:35:06.621663+01	0	24.436549297341955	2	6	2	33	7
615	0	Good performance	2025-10-01 10:35:06.628974+01	1.3	t	2025-10-01 10:35:06.628974+01	51.125857883267415	51.125857883267415	3	6	2	33	7
616	0	Good performance	2025-10-01 10:35:06.637453+01	1.7	t	2025-10-01 10:35:06.637453+01	56.118216245665046	56.118216245665046	4	6	2	33	7
617	25.163275274973397	Good performance	2025-10-01 10:35:06.64482+01	0	f	2025-10-01 10:35:06.64482+01	0	25.163275274973397	1	9	1	33	12
618	22.475368689152603	Good performance	2025-10-01 10:35:06.652338+01	0	f	2025-10-01 10:35:06.652339+01	0	22.475368689152603	2	9	1	33	12
619	0	Requires significant improvement	2025-10-01 10:35:06.660009+01	0	f	2025-10-01 10:35:06.66001+01	19.02159843660894	19.02159843660894	3	9	1	33	12
620	0	Good performance	2025-10-01 10:35:06.667265+01	1.7	t	2025-10-01 10:35:06.667266+01	55.17614166881844	55.17614166881844	4	9	1	33	12
621	29.119942060193992	Excellent work!	2025-10-01 10:35:06.675304+01	0	f	2025-10-01 10:35:06.675304+01	0	29.119942060193992	1	9	2	33	12
622	15.396835029151632	Needs improvement	2025-10-01 10:35:06.682926+01	0	f	2025-10-01 10:35:06.682927+01	0	15.396835029151632	2	9	2	33	12
623	0	Good performance	2025-10-01 10:35:06.690533+01	1.3	t	2025-10-01 10:35:06.690533+01	51.70977290210921	51.70977290210921	3	9	2	33	12
624	0	Good performance	2025-10-01 10:35:06.697975+01	1.7	t	2025-10-01 10:35:06.697976+01	57.47979652700488	57.47979652700488	4	9	2	33	12
625	22.12885982784661	Good performance	2025-10-01 10:35:06.706794+01	0	f	2025-10-01 10:35:06.706795+01	0	22.12885982784661	1	12	1	33	17
626	25.479197836909933	Good performance	2025-10-01 10:35:06.713922+01	0	f	2025-10-01 10:35:06.713922+01	0	25.479197836909933	2	12	1	33	17
627	0	Good performance	2025-10-01 10:35:06.72239+01	1.3	t	2025-10-01 10:35:06.72239+01	51.14730742500471	51.14730742500471	3	12	1	33	17
628	0	Requires significant improvement	2025-10-01 10:35:06.729878+01	0	f	2025-10-01 10:35:06.729879+01	10.428953236536193	10.428953236536193	4	12	1	33	17
629	15.927397944672613	Needs improvement	2025-10-01 10:35:06.737865+01	0	f	2025-10-01 10:35:06.737865+01	0	15.927397944672613	1	12	2	33	17
630	21.536716726007533	Good performance	2025-10-01 10:35:06.7452+01	0	f	2025-10-01 10:35:06.7452+01	0	21.536716726007533	2	12	2	33	17
631	0	Needs improvement	2025-10-01 10:35:06.7528+01	0	f	2025-10-01 10:35:06.752801+01	40.52323655506525	40.52323655506525	3	12	2	33	17
632	0	Excellent work!	2025-10-01 10:35:06.761194+01	2	t	2025-10-01 10:35:06.761195+01	62.01254545723266	62.01254545723266	4	12	2	33	17
633	18.496752739207547	Satisfactory	2025-10-01 10:35:06.768912+01	0	f	2025-10-01 10:35:06.768912+01	0	18.496752739207547	1	15	1	33	22
634	15.564712585232778	Needs improvement	2025-10-01 10:35:06.776436+01	0	f	2025-10-01 10:35:06.776437+01	0	15.564712585232778	2	15	1	33	22
635	0	Excellent work!	2025-10-01 10:35:06.783795+01	1.7	t	2025-10-01 10:35:06.783795+01	59.57221173300648	59.57221173300648	3	15	1	33	22
636	0	Good performance	2025-10-01 10:35:06.791037+01	1.7	t	2025-10-01 10:35:06.791037+01	56.60419144366591	56.60419144366591	4	15	1	33	22
637	27.76924481667945	Excellent work!	2025-10-01 10:35:06.798854+01	0	f	2025-10-01 10:35:06.798855+01	0	27.76924481667945	1	15	2	33	22
638	22.940161434068933	Good performance	2025-10-01 10:35:06.807302+01	0	f	2025-10-01 10:35:06.807303+01	0	22.940161434068933	2	15	2	33	22
639	0	Requires significant improvement	2025-10-01 10:35:06.814655+01	0	f	2025-10-01 10:35:06.814656+01	13.188184762830353	13.188184762830353	3	15	2	33	22
640	0	Needs improvement	2025-10-01 10:35:06.822267+01	0	f	2025-10-01 10:35:06.822267+01	40.63777905816611	40.63777905816611	4	15	2	33	22
641	21.279432367136458	Good performance	2025-10-01 10:35:06.829817+01	0	f	2025-10-01 10:35:06.829818+01	0	21.279432367136458	1	2	1	34	2
642	29.258150061404624	Excellent work!	2025-10-01 10:35:06.838215+01	0	f	2025-10-01 10:35:06.838215+01	0	29.258150061404624	2	2	1	34	2
643	0	Excellent work!	2025-10-01 10:35:06.845791+01	2.3	t	2025-10-01 10:35:06.845791+01	68.44727261593178	68.44727261593178	3	2	1	34	2
644	0	Needs improvement	2025-10-01 10:35:06.853895+01	0	f	2025-10-01 10:35:06.853895+01	41.03662230440532	41.03662230440532	4	2	1	34	2
645	19.736779781670133	Satisfactory	2025-10-01 10:35:06.862731+01	0	f	2025-10-01 10:35:06.862731+01	0	19.736779781670133	1	2	2	34	2
646	19.697685958429933	Satisfactory	2025-10-01 10:35:06.871811+01	0	f	2025-10-01 10:35:06.871812+01	0	19.697685958429933	2	2	2	34	2
647	0	Requires significant improvement	2025-10-01 10:35:06.879561+01	0	f	2025-10-01 10:35:06.879562+01	13.896013199371136	13.896013199371136	3	2	2	34	2
648	0	Excellent work!	2025-10-01 10:35:06.88723+01	2	t	2025-10-01 10:35:06.887231+01	62.41628861496655	62.41628861496655	4	2	2	34	2
649	18.036111826934583	Satisfactory	2025-10-01 10:35:06.894995+01	0	f	2025-10-01 10:35:06.894995+01	0	18.036111826934583	1	6	1	34	7
650	27.88217050542454	Excellent work!	2025-10-01 10:35:06.904641+01	0	f	2025-10-01 10:35:06.904641+01	0	27.88217050542454	2	6	1	34	7
651	0	Requires significant improvement	2025-10-01 10:35:06.91264+01	0	f	2025-10-01 10:35:06.912641+01	27.148411771589245	27.148411771589245	3	6	1	34	7
652	0	Satisfactory	2025-10-01 10:35:06.920843+01	1	f	2025-10-01 10:35:06.920843+01	47.59496630889623	47.59496630889623	4	6	1	34	7
653	17.351241804647643	Needs improvement	2025-10-01 10:35:06.928886+01	0	f	2025-10-01 10:35:06.928887+01	0	17.351241804647643	1	6	2	34	7
654	25.09269396823827	Good performance	2025-10-01 10:35:06.937974+01	0	f	2025-10-01 10:35:06.937975+01	0	25.09269396823827	2	6	2	34	7
655	0	Excellent work!	2025-10-01 10:35:06.945914+01	2.3	t	2025-10-01 10:35:06.945915+01	67.7962397514439	67.7962397514439	3	6	2	34	7
656	0	Requires significant improvement	2025-10-01 10:35:06.953888+01	0	f	2025-10-01 10:35:06.953889+01	9.42237499510874	9.42237499510874	4	6	2	34	7
657	15.302354921612498	Needs improvement	2025-10-01 10:35:06.961968+01	0	f	2025-10-01 10:35:06.961969+01	0	15.302354921612498	1	9	1	34	12
658	18.507834737809663	Satisfactory	2025-10-01 10:35:06.969791+01	0	f	2025-10-01 10:35:06.969791+01	0	18.507834737809663	2	9	1	34	12
659	0	Good performance	2025-10-01 10:35:06.978175+01	1.3	t	2025-10-01 10:35:06.978176+01	54.42407767538573	54.42407767538573	3	9	1	34	12
660	0	Satisfactory	2025-10-01 10:35:06.986412+01	0	f	2025-10-01 10:35:06.986413+01	42.981780490031404	42.981780490031404	4	9	1	34	12
661	26.670718296588845	Excellent work!	2025-10-01 10:35:06.99383+01	0	f	2025-10-01 10:35:06.993831+01	0	26.670718296588845	1	9	2	34	12
662	7.599868952511485	Requires significant improvement	2025-10-01 10:35:07.001906+01	0	f	2025-10-01 10:35:07.001906+01	0	7.599868952511485	2	9	2	34	12
663	0	Excellent work!	2025-10-01 10:35:07.010554+01	2	t	2025-10-01 10:35:07.010554+01	63.48825622361403	63.48825622361403	3	9	2	34	12
664	0	Needs improvement	2025-10-01 10:35:07.018284+01	0	f	2025-10-01 10:35:07.018284+01	38.43101265895214	38.43101265895214	4	9	2	34	12
665	18.24831859447553	Satisfactory	2025-10-01 10:35:07.026321+01	0	f	2025-10-01 10:35:07.026322+01	0	18.24831859447553	1	12	1	34	17
666	19.887779971407436	Satisfactory	2025-10-01 10:35:07.033703+01	0	f	2025-10-01 10:35:07.033703+01	0	19.887779971407436	2	12	1	34	17
667	0	Requires significant improvement	2025-10-01 10:35:07.042181+01	0	f	2025-10-01 10:35:07.042181+01	14.681800612571816	14.681800612571816	3	12	1	34	17
668	0	Excellent work!	2025-10-01 10:35:07.04975+01	2.3	t	2025-10-01 10:35:07.049751+01	69.54641579583196	69.54641579583196	4	12	1	34	17
669	22.75136308385021	Good performance	2025-10-01 10:35:07.057406+01	0	f	2025-10-01 10:35:07.057407+01	0	22.75136308385021	1	12	2	34	17
670	23.805582082293782	Good performance	2025-10-01 10:35:07.065509+01	0	f	2025-10-01 10:35:07.065509+01	0	23.805582082293782	2	12	2	34	17
671	0	Excellent work!	2025-10-01 10:35:07.074182+01	2	t	2025-10-01 10:35:07.074182+01	64.82992027886158	64.82992027886158	3	12	2	34	17
672	0	Excellent work!	2025-10-01 10:35:07.082106+01	2	t	2025-10-01 10:35:07.082106+01	64.06854321818447	64.06854321818447	4	12	2	34	17
673	29.781887241010946	Excellent work!	2025-10-01 10:35:07.090117+01	0	f	2025-10-01 10:35:07.090117+01	0	29.781887241010946	1	15	1	34	22
674	20.234033697876274	Satisfactory	2025-10-01 10:35:07.098183+01	0	f	2025-10-01 10:35:07.098183+01	0	20.234033697876274	2	15	1	34	22
675	0	Excellent work!	2025-10-01 10:35:07.107355+01	2	t	2025-10-01 10:35:07.107355+01	60.181965889395926	60.181965889395926	3	15	1	34	22
676	0	Excellent work!	2025-10-01 10:35:07.11536+01	2	t	2025-10-01 10:35:07.115361+01	61.78570347972439	61.78570347972439	4	15	1	34	22
677	22.152801129086406	Good performance	2025-10-01 10:35:07.123587+01	0	f	2025-10-01 10:35:07.123587+01	0	22.152801129086406	1	15	2	34	22
678	29.45537552721064	Excellent work!	2025-10-01 10:35:07.131755+01	0	f	2025-10-01 10:35:07.131755+01	0	29.45537552721064	2	15	2	34	22
679	0	Excellent work!	2025-10-01 10:35:07.140993+01	2.3	t	2025-10-01 10:35:07.140993+01	68.00160409876702	68.00160409876702	3	15	2	34	22
680	0	Excellent work!	2025-10-01 10:35:07.149404+01	2	t	2025-10-01 10:35:07.149405+01	60.59831433892339	60.59831433892339	4	15	2	34	22
681	20.7397447502635	Satisfactory	2025-10-01 10:35:07.157533+01	0	f	2025-10-01 10:35:07.157533+01	0	20.7397447502635	1	2	1	35	2
682	29.081436072130664	Excellent work!	2025-10-01 10:35:07.165791+01	0	f	2025-10-01 10:35:07.165791+01	0	29.081436072130664	2	2	1	35	2
683	0	Excellent work!	2025-10-01 10:35:07.174492+01	2.3	t	2025-10-01 10:35:07.174493+01	65.09535808480766	65.09535808480766	3	2	1	35	2
684	0	Satisfactory	2025-10-01 10:35:07.182329+01	0	f	2025-10-01 10:35:07.18233+01	44.21534673448036	44.21534673448036	4	2	1	35	2
685	20.299791403243045	Satisfactory	2025-10-01 10:35:07.190583+01	0	f	2025-10-01 10:35:07.190584+01	0	20.299791403243045	1	2	2	35	2
686	16.75526520022714	Needs improvement	2025-10-01 10:35:07.19862+01	0	f	2025-10-01 10:35:07.19862+01	0	16.75526520022714	2	2	2	35	2
687	0	Needs improvement	2025-10-01 10:35:07.207389+01	0	f	2025-10-01 10:35:07.20739+01	37.270510631053995	37.270510631053995	3	2	2	35	2
688	0	Excellent work!	2025-10-01 10:35:07.215496+01	2	t	2025-10-01 10:35:07.215496+01	60.506651262811744	60.506651262811744	4	2	2	35	2
689	6.069111031489409	Requires significant improvement	2025-10-01 10:35:07.224182+01	0	f	2025-10-01 10:35:07.224183+01	0	6.069111031489409	1	6	1	35	7
690	25.066080110792694	Good performance	2025-10-01 10:35:07.232488+01	0	f	2025-10-01 10:35:07.232489+01	0	25.066080110792694	2	6	1	35	7
691	0	Excellent work!	2025-10-01 10:35:07.242264+01	2	t	2025-10-01 10:35:07.242264+01	63.33762566389605	63.33762566389605	3	6	1	35	7
692	0	Excellent work!	2025-10-01 10:35:07.250471+01	2	t	2025-10-01 10:35:07.250472+01	64.31543197593484	64.31543197593484	4	6	1	35	7
693	29.983953052109065	Excellent work!	2025-10-01 10:35:07.258542+01	0	f	2025-10-01 10:35:07.258543+01	0	29.983953052109065	1	6	2	35	7
694	15.285223789742714	Needs improvement	2025-10-01 10:35:07.266561+01	0	f	2025-10-01 10:35:07.266561+01	0	15.285223789742714	2	6	2	35	7
695	0	Needs improvement	2025-10-01 10:35:07.274787+01	0	f	2025-10-01 10:35:07.274787+01	39.462860089970874	39.462860089970874	3	6	2	35	7
696	0	Requires significant improvement	2025-10-01 10:35:07.283277+01	0	f	2025-10-01 10:35:07.283278+01	26.910788136655277	26.910788136655277	4	6	2	35	7
697	16.628216204441497	Needs improvement	2025-10-01 10:35:07.29153+01	0	f	2025-10-01 10:35:07.29153+01	0	16.628216204441497	1	9	1	35	12
698	27.542171628399046	Excellent work!	2025-10-01 10:35:07.299368+01	0	f	2025-10-01 10:35:07.299369+01	0	27.542171628399046	2	9	1	35	12
699	0	Satisfactory	2025-10-01 10:35:07.308583+01	1	f	2025-10-01 10:35:07.308583+01	45.64426250730271	45.64426250730271	3	9	1	35	12
700	0	Excellent work!	2025-10-01 10:35:07.316573+01	2.3	t	2025-10-01 10:35:07.316573+01	68.83571225542062	68.83571225542062	4	9	1	35	12
701	21.143805864280527	Good performance	2025-10-01 10:35:07.32504+01	0	f	2025-10-01 10:35:07.325041+01	0	21.143805864280527	1	9	2	35	12
702	1.0747699231485675	Requires significant improvement	2025-10-01 10:35:07.333119+01	0	f	2025-10-01 10:35:07.33312+01	0	1.0747699231485675	2	9	2	35	12
703	0	Needs improvement	2025-10-01 10:35:07.342255+01	0	f	2025-10-01 10:35:07.342256+01	36.58651285379556	36.58651285379556	3	9	2	35	12
704	0	Requires significant improvement	2025-10-01 10:35:07.350194+01	0	f	2025-10-01 10:35:07.350195+01	32.396167601200254	32.396167601200254	4	9	2	35	12
705	20.99715440433204	Satisfactory	2025-10-01 10:35:07.358383+01	0	f	2025-10-01 10:35:07.358384+01	0	20.99715440433204	1	12	1	35	17
706	27.914191637959572	Excellent work!	2025-10-01 10:35:07.366684+01	0	f	2025-10-01 10:35:07.366684+01	0	27.914191637959572	2	12	1	35	17
707	0	Requires significant improvement	2025-10-01 10:35:07.375498+01	0	f	2025-10-01 10:35:07.375498+01	24.05565810856664	24.05565810856664	3	12	1	35	17
708	0	Excellent work!	2025-10-01 10:35:07.384528+01	2.3	t	2025-10-01 10:35:07.384528+01	67.9705311464902	67.9705311464902	4	12	1	35	17
709	27.77424274211224	Excellent work!	2025-10-01 10:35:07.392573+01	0	f	2025-10-01 10:35:07.392574+01	0	27.77424274211224	1	12	2	35	17
710	28.29440990261766	Excellent work!	2025-10-01 10:35:07.402607+01	0	f	2025-10-01 10:35:07.402607+01	0	28.29440990261766	2	12	2	35	17
711	0	Good performance	2025-10-01 10:35:07.411416+01	1.3	t	2025-10-01 10:35:07.411416+01	51.15416830314486	51.15416830314486	3	12	2	35	17
712	0	Needs improvement	2025-10-01 10:35:07.419877+01	0	f	2025-10-01 10:35:07.419877+01	36.93618200584729	36.93618200584729	4	12	2	35	17
713	9.921568072568585	Requires significant improvement	2025-10-01 10:35:07.427867+01	0	f	2025-10-01 10:35:07.427867+01	0	9.921568072568585	1	15	1	35	22
714	16.063249844334553	Needs improvement	2025-10-01 10:35:07.435909+01	0	f	2025-10-01 10:35:07.435909+01	0	16.063249844334553	2	15	1	35	22
715	0	Requires significant improvement	2025-10-01 10:35:07.444308+01	0	f	2025-10-01 10:35:07.444308+01	23.255498953710315	23.255498953710315	3	15	1	35	22
716	0	Satisfactory	2025-10-01 10:35:07.452596+01	0	f	2025-10-01 10:35:07.452597+01	42.29477758879736	42.29477758879736	4	15	1	35	22
717	5.167162138923679	Requires significant improvement	2025-10-01 10:35:07.46058+01	0	f	2025-10-01 10:35:07.460581+01	0	5.167162138923679	1	15	2	35	22
718	26.953781294092874	Excellent work!	2025-10-01 10:35:07.468741+01	0	f	2025-10-01 10:35:07.468741+01	0	26.953781294092874	2	15	2	35	22
719	0	Good performance	2025-10-01 10:35:07.477125+01	1.7	t	2025-10-01 10:35:07.477125+01	57.26985157920585	57.26985157920585	3	15	2	35	22
720	0	Satisfactory	2025-10-01 10:35:07.485355+01	1	f	2025-10-01 10:35:07.485356+01	47.109268670188776	47.109268670188776	4	15	2	35	22
721	21.776600613587753	Good performance	2025-10-01 10:35:07.493761+01	0	f	2025-10-01 10:35:07.493761+01	0	21.776600613587753	1	2	1	36	2
722	28.43330101272762	Excellent work!	2025-10-01 10:35:07.502772+01	0	f	2025-10-01 10:35:07.502772+01	0	28.43330101272762	2	2	1	36	2
723	0	Satisfactory	2025-10-01 10:35:07.511257+01	1	f	2025-10-01 10:35:07.511257+01	46.41614192910513	46.41614192910513	3	2	1	36	2
724	0	Good performance	2025-10-01 10:35:07.519431+01	1.7	t	2025-10-01 10:35:07.519431+01	57.6862478563757	57.6862478563757	4	2	1	36	2
725	28.57448261776323	Excellent work!	2025-10-01 10:35:07.536383+01	0	f	2025-10-01 10:35:07.536384+01	0	28.57448261776323	1	2	2	36	2
726	9.51132997924073	Requires significant improvement	2025-10-01 10:35:07.545181+01	0	f	2025-10-01 10:35:07.545182+01	0	9.51132997924073	2	2	2	36	2
727	0	Good performance	2025-10-01 10:35:07.553478+01	1	f	2025-10-01 10:35:07.553479+01	49.79432602966399	49.79432602966399	3	2	2	36	2
728	0	Excellent work!	2025-10-01 10:35:07.561717+01	2.3	t	2025-10-01 10:35:07.561718+01	65.17448422737478	65.17448422737478	4	2	2	36	2
729	1.093498140698087	Requires significant improvement	2025-10-01 10:35:07.570627+01	0	f	2025-10-01 10:35:07.570628+01	0	1.093498140698087	1	6	1	36	7
730	25.07184992499045	Good performance	2025-10-01 10:35:07.579617+01	0	f	2025-10-01 10:35:07.579618+01	0	25.07184992499045	2	6	1	36	7
731	0	Satisfactory	2025-10-01 10:35:07.588145+01	1	f	2025-10-01 10:35:07.588146+01	48.054522374093786	48.054522374093786	3	6	1	36	7
732	0	Good performance	2025-10-01 10:35:07.596509+01	1.3	t	2025-10-01 10:35:07.596509+01	53.44037094634647	53.44037094634647	4	6	1	36	7
733	26.052545551492884	Excellent work!	2025-10-01 10:35:07.60542+01	0	f	2025-10-01 10:35:07.605421+01	0	26.052545551492884	1	6	2	36	7
734	0.2263034896534799	Requires significant improvement	2025-10-01 10:35:07.613411+01	0	f	2025-10-01 10:35:07.613412+01	0	0.2263034896534799	2	6	2	36	7
735	0	Excellent work!	2025-10-01 10:35:07.621594+01	2	t	2025-10-01 10:35:07.621594+01	64.51347480582812	64.51347480582812	3	6	2	36	7
736	0	Satisfactory	2025-10-01 10:35:07.629699+01	1	f	2025-10-01 10:35:07.6297+01	47.14009449577134	47.14009449577134	4	6	2	36	7
737	26.170964564582682	Excellent work!	2025-10-01 10:35:07.638324+01	0	f	2025-10-01 10:35:07.638324+01	0	26.170964564582682	1	9	1	36	12
738	7.69207219254754	Requires significant improvement	2025-10-01 10:35:07.646459+01	0	f	2025-10-01 10:35:07.64646+01	0	7.69207219254754	2	9	1	36	12
739	0	Good performance	2025-10-01 10:35:07.654568+01	1.7	t	2025-10-01 10:35:07.654569+01	57.82253822253672	57.82253822253672	3	9	1	36	12
740	0	Satisfactory	2025-10-01 10:35:07.662803+01	1	f	2025-10-01 10:35:07.662803+01	45.625640040680466	45.625640040680466	4	9	1	36	12
741	29.28775919035186	Excellent work!	2025-10-01 10:35:07.671632+01	0	f	2025-10-01 10:35:07.671633+01	0	29.28775919035186	1	9	2	36	12
742	27.75491310641438	Excellent work!	2025-10-01 10:35:07.679829+01	0	f	2025-10-01 10:35:07.679829+01	0	27.75491310641438	2	9	2	36	12
743	0	Good performance	2025-10-01 10:35:07.688592+01	1.3	t	2025-10-01 10:35:07.688592+01	54.12413333415744	54.12413333415744	3	9	2	36	12
744	0	Needs improvement	2025-10-01 10:35:07.696726+01	0	f	2025-10-01 10:35:07.696726+01	38.92115151081235	38.92115151081235	4	9	2	36	12
745	19.77973110747465	Satisfactory	2025-10-01 10:35:07.706141+01	0	f	2025-10-01 10:35:07.706142+01	0	19.77973110747465	1	12	1	36	17
746	29.83845892226759	Excellent work!	2025-10-01 10:35:07.714389+01	0	f	2025-10-01 10:35:07.714389+01	0	29.83845892226759	2	12	1	36	17
747	0	Excellent work!	2025-10-01 10:35:07.72372+01	2	t	2025-10-01 10:35:07.723721+01	61.060575650830614	61.060575650830614	3	12	1	36	17
748	0	Excellent work!	2025-10-01 10:35:07.732392+01	2	t	2025-10-01 10:35:07.732393+01	61.81408454910893	61.81408454910893	4	12	1	36	17
749	26.003790146490854	Excellent work!	2025-10-01 10:35:07.741476+01	0	f	2025-10-01 10:35:07.741477+01	0	26.003790146490854	1	12	2	36	17
750	2.192337672274705	Requires significant improvement	2025-10-01 10:35:07.750617+01	0	f	2025-10-01 10:35:07.750618+01	0	2.192337672274705	2	12	2	36	17
751	0	Satisfactory	2025-10-01 10:35:07.759199+01	1	f	2025-10-01 10:35:07.759199+01	48.69333692108061	48.69333692108061	3	12	2	36	17
752	0	Excellent work!	2025-10-01 10:35:07.771085+01	2.3	t	2025-10-01 10:35:07.771086+01	69.71511341048951	69.71511341048951	4	12	2	36	17
753	12.048338360588268	Requires significant improvement	2025-10-01 10:35:07.779291+01	0	f	2025-10-01 10:35:07.779292+01	0	12.048338360588268	1	15	1	36	22
754	1.3686976283411878	Requires significant improvement	2025-10-01 10:35:07.787625+01	0	f	2025-10-01 10:35:07.787625+01	0	1.3686976283411878	2	15	1	36	22
755	0	Good performance	2025-10-01 10:35:07.79566+01	1.3	t	2025-10-01 10:35:07.79566+01	54.055948483061776	54.055948483061776	3	15	1	36	22
756	0	Excellent work!	2025-10-01 10:35:07.805046+01	2	t	2025-10-01 10:35:07.805047+01	63.22119422502567	63.22119422502567	4	15	1	36	22
757	20.26632017920366	Satisfactory	2025-10-01 10:35:07.813677+01	0	f	2025-10-01 10:35:07.813678+01	0	20.26632017920366	1	15	2	36	22
758	29.80568713455638	Excellent work!	2025-10-01 10:35:07.822878+01	0	f	2025-10-01 10:35:07.822878+01	0	29.80568713455638	2	15	2	36	22
759	0	Excellent work!	2025-10-01 10:35:07.831726+01	2	t	2025-10-01 10:35:07.831726+01	64.0736114477202	64.0736114477202	3	15	2	36	22
760	0	Excellent work!	2025-10-01 10:35:07.840555+01	2	t	2025-10-01 10:35:07.840555+01	60.92480996221897	60.92480996221897	4	15	2	36	22
761	24.243218242491416	Good performance	2025-10-01 10:35:07.848777+01	0	f	2025-10-01 10:35:07.848777+01	0	24.243218242491416	1	2	1	37	2
762	3.6149155675977918	Requires significant improvement	2025-10-01 10:35:07.857111+01	0	f	2025-10-01 10:35:07.857112+01	0	3.6149155675977918	2	2	1	37	2
763	0	Excellent work!	2025-10-01 10:35:07.865386+01	2.3	t	2025-10-01 10:35:07.865387+01	69.99365289903712	69.99365289903712	3	2	1	37	2
764	0	Needs improvement	2025-10-01 10:35:07.874249+01	0	f	2025-10-01 10:35:07.87425+01	40.39647465252325	40.39647465252325	4	2	1	37	2
765	27.547967902103885	Excellent work!	2025-10-01 10:35:07.883054+01	0	f	2025-10-01 10:35:07.883055+01	0	27.547967902103885	1	2	2	37	2
766	17.92910069328522	Needs improvement	2025-10-01 10:35:07.891775+01	0	f	2025-10-01 10:35:07.891775+01	0	17.92910069328522	2	2	2	37	2
767	0	Requires significant improvement	2025-10-01 10:35:07.900382+01	0	f	2025-10-01 10:35:07.900382+01	14.064622302693975	14.064622302693975	3	2	2	37	2
768	0	Good performance	2025-10-01 10:35:07.913152+01	1.7	t	2025-10-01 10:35:07.913153+01	57.232558105989824	57.232558105989824	4	2	2	37	2
769	26.986945982431102	Excellent work!	2025-10-01 10:35:07.922411+01	0	f	2025-10-01 10:35:07.922411+01	0	26.986945982431102	1	6	1	37	7
770	20.628975813297668	Satisfactory	2025-10-01 10:35:07.931253+01	0	f	2025-10-01 10:35:07.931254+01	0	20.628975813297668	2	6	1	37	7
771	0	Satisfactory	2025-10-01 10:35:07.940859+01	0	f	2025-10-01 10:35:07.940859+01	43.43206913241002	43.43206913241002	3	6	1	37	7
772	0	Good performance	2025-10-01 10:35:07.949448+01	1.7	t	2025-10-01 10:35:07.949448+01	56.037289527643495	56.037289527643495	4	6	1	37	7
773	26.151805949202533	Excellent work!	2025-10-01 10:35:07.9582+01	0	f	2025-10-01 10:35:07.9582+01	0	26.151805949202533	1	6	2	37	7
774	19.45501797089924	Satisfactory	2025-10-01 10:35:07.967598+01	0	f	2025-10-01 10:35:07.967599+01	0	19.45501797089924	2	6	2	37	7
775	0	Satisfactory	2025-10-01 10:35:07.977328+01	0	f	2025-10-01 10:35:07.977329+01	44.00611653691989	44.00611653691989	3	6	2	37	7
776	0	Good performance	2025-10-01 10:35:07.986509+01	1	f	2025-10-01 10:35:07.986509+01	49.89578707778384	49.89578707778384	4	6	2	37	7
777	26.706950008433214	Excellent work!	2025-10-01 10:35:07.995218+01	0	f	2025-10-01 10:35:07.995218+01	0	26.706950008433214	1	9	1	37	12
778	29.668076707410187	Excellent work!	2025-10-01 10:35:08.004508+01	0	f	2025-10-01 10:35:08.004509+01	0	29.668076707410187	2	9	1	37	12
779	0	Needs improvement	2025-10-01 10:35:08.0133+01	0	f	2025-10-01 10:35:08.013301+01	36.807287977906974	36.807287977906974	3	9	1	37	12
780	0	Good performance	2025-10-01 10:35:08.021917+01	1.3	t	2025-10-01 10:35:08.021918+01	53.031878833454584	53.031878833454584	4	9	1	37	12
781	1.3969746323930077	Requires significant improvement	2025-10-01 10:35:08.030738+01	0	f	2025-10-01 10:35:08.030739+01	0	1.3969746323930077	1	9	2	37	12
782	25.181766919538436	Good performance	2025-10-01 10:35:08.039808+01	0	f	2025-10-01 10:35:08.039809+01	0	25.181766919538436	2	9	2	37	12
835	0	Excellent work!	2025-10-01 10:35:08.530136+01	2	t	2025-10-01 10:35:08.530136+01	64.07381808207182	64.07381808207182	3	15	1	38	22
783	0	Requires significant improvement	2025-10-01 10:35:08.048519+01	0	f	2025-10-01 10:35:08.048519+01	22.411172421363467	22.411172421363467	3	9	2	37	12
784	0	Requires significant improvement	2025-10-01 10:35:08.057416+01	0	f	2025-10-01 10:35:08.057416+01	25.341787673646795	25.341787673646795	4	9	2	37	12
785	16.32241257926924	Needs improvement	2025-10-01 10:35:08.066339+01	0	f	2025-10-01 10:35:08.06634+01	0	16.32241257926924	1	12	1	37	17
786	19.08159115439863	Satisfactory	2025-10-01 10:35:08.075752+01	0	f	2025-10-01 10:35:08.075753+01	0	19.08159115439863	2	12	1	37	17
787	0	Needs improvement	2025-10-01 10:35:08.084523+01	0	f	2025-10-01 10:35:08.084523+01	38.98853872378957	38.98853872378957	3	12	1	37	17
788	0	Excellent work!	2025-10-01 10:35:08.093331+01	2.3	t	2025-10-01 10:35:08.093331+01	67.9850642548348	67.9850642548348	4	12	1	37	17
789	29.699592245095758	Excellent work!	2025-10-01 10:35:08.102211+01	0	f	2025-10-01 10:35:08.102212+01	0	29.699592245095758	1	12	2	37	17
790	23.38896933344204	Good performance	2025-10-01 10:35:08.111347+01	0	f	2025-10-01 10:35:08.111347+01	0	23.38896933344204	2	12	2	37	17
791	0	Requires significant improvement	2025-10-01 10:35:08.120322+01	0	f	2025-10-01 10:35:08.120323+01	7.901110164729162	7.901110164729162	3	12	2	37	17
792	0	Requires significant improvement	2025-10-01 10:35:08.129232+01	0	f	2025-10-01 10:35:08.129232+01	20.922136545805753	20.922136545805753	4	12	2	37	17
793	15.202209619267899	Needs improvement	2025-10-01 10:35:08.13875+01	0	f	2025-10-01 10:35:08.138751+01	0	15.202209619267899	1	15	1	37	22
794	4.503592272644427	Requires significant improvement	2025-10-01 10:35:08.147546+01	0	f	2025-10-01 10:35:08.147546+01	0	4.503592272644427	2	15	1	37	22
795	0	Excellent work!	2025-10-01 10:35:08.156888+01	2	t	2025-10-01 10:35:08.156888+01	63.75568177018492	63.75568177018492	3	15	1	37	22
796	0	Excellent work!	2025-10-01 10:35:08.165468+01	2.3	t	2025-10-01 10:35:08.165469+01	65.44741582208201	65.44741582208201	4	15	1	37	22
797	18.791790119358474	Satisfactory	2025-10-01 10:35:08.175035+01	0	f	2025-10-01 10:35:08.175036+01	0	18.791790119358474	1	15	2	37	22
798	25.052135845675977	Good performance	2025-10-01 10:35:08.183888+01	0	f	2025-10-01 10:35:08.183888+01	0	25.052135845675977	2	15	2	37	22
799	0	Excellent work!	2025-10-01 10:35:08.193108+01	2.3	t	2025-10-01 10:35:08.193108+01	69.33657918063692	69.33657918063692	3	15	2	37	22
800	0	Excellent work!	2025-10-01 10:35:08.202267+01	2.3	t	2025-10-01 10:35:08.202268+01	69.37527824670184	69.37527824670184	4	15	2	37	22
801	20.0055091111182	Satisfactory	2025-10-01 10:35:08.211467+01	0	f	2025-10-01 10:35:08.211467+01	0	20.0055091111182	1	2	1	38	2
802	18.56639563520355	Satisfactory	2025-10-01 10:35:08.220391+01	0	f	2025-10-01 10:35:08.220392+01	0	18.56639563520355	2	2	1	38	2
803	0	Excellent work!	2025-10-01 10:35:08.229237+01	2	t	2025-10-01 10:35:08.229238+01	62.728792897637085	62.728792897637085	3	2	1	38	2
804	0	Excellent work!	2025-10-01 10:35:08.23836+01	2.3	t	2025-10-01 10:35:08.23836+01	68.21147905812327	68.21147905812327	4	2	1	38	2
805	19.44262226458467	Satisfactory	2025-10-01 10:35:08.246874+01	0	f	2025-10-01 10:35:08.246875+01	0	19.44262226458467	1	2	2	38	2
806	28.667054505702048	Excellent work!	2025-10-01 10:35:08.256114+01	0	f	2025-10-01 10:35:08.256115+01	0	28.667054505702048	2	2	2	38	2
807	0	Excellent work!	2025-10-01 10:35:08.265839+01	2.3	t	2025-10-01 10:35:08.26584+01	69.35100223859757	69.35100223859757	3	2	2	38	2
808	0	Requires significant improvement	2025-10-01 10:35:08.275455+01	0	f	2025-10-01 10:35:08.275455+01	17.95513987832396	17.95513987832396	4	2	2	38	2
809	9.641426655508479	Requires significant improvement	2025-10-01 10:35:08.284824+01	0	f	2025-10-01 10:35:08.284825+01	0	9.641426655508479	1	6	1	38	7
810	18.049086823560472	Satisfactory	2025-10-01 10:35:08.293633+01	0	f	2025-10-01 10:35:08.293634+01	0	18.049086823560472	2	6	1	38	7
811	0	Excellent work!	2025-10-01 10:35:08.302879+01	2	t	2025-10-01 10:35:08.30288+01	62.60063270717383	62.60063270717383	3	6	1	38	7
812	0	Satisfactory	2025-10-01 10:35:08.312546+01	0	f	2025-10-01 10:35:08.312547+01	44.74134329574095	44.74134329574095	4	6	1	38	7
813	25.99306990620298	Excellent work!	2025-10-01 10:35:08.321888+01	0	f	2025-10-01 10:35:08.321888+01	0	25.99306990620298	1	6	2	38	7
814	19.32480939454568	Satisfactory	2025-10-01 10:35:08.331319+01	0	f	2025-10-01 10:35:08.331319+01	0	19.32480939454568	2	6	2	38	7
815	0	Needs improvement	2025-10-01 10:35:08.341188+01	0	f	2025-10-01 10:35:08.341189+01	36.793738554543495	36.793738554543495	3	6	2	38	7
816	0	Needs improvement	2025-10-01 10:35:08.35048+01	0	f	2025-10-01 10:35:08.350481+01	40.883506692075485	40.883506692075485	4	6	2	38	7
817	18.243937968919727	Satisfactory	2025-10-01 10:35:08.359266+01	0	f	2025-10-01 10:35:08.359266+01	0	18.243937968919727	1	9	1	38	12
818	16.1437996594598	Needs improvement	2025-10-01 10:35:08.368448+01	0	f	2025-10-01 10:35:08.368448+01	0	16.1437996594598	2	9	1	38	12
819	0	Requires significant improvement	2025-10-01 10:35:08.377832+01	0	f	2025-10-01 10:35:08.377832+01	21.007200121415867	21.007200121415867	3	9	1	38	12
820	0	Satisfactory	2025-10-01 10:35:08.387124+01	1	f	2025-10-01 10:35:08.387125+01	46.32334247207905	46.32334247207905	4	9	1	38	12
821	12.330810909243029	Requires significant improvement	2025-10-01 10:35:08.396166+01	0	f	2025-10-01 10:35:08.396167+01	0	12.330810909243029	1	9	2	38	12
822	15.68101778656256	Needs improvement	2025-10-01 10:35:08.405618+01	0	f	2025-10-01 10:35:08.405618+01	0	15.68101778656256	2	9	2	38	12
823	0	Excellent work!	2025-10-01 10:35:08.416418+01	2.3	t	2025-10-01 10:35:08.416419+01	66.79522178612659	66.79522178612659	3	9	2	38	12
824	0	Excellent work!	2025-10-01 10:35:08.425329+01	2.3	t	2025-10-01 10:35:08.42533+01	67.31152402975876	67.31152402975876	4	9	2	38	12
825	22.834444182608767	Good performance	2025-10-01 10:35:08.43494+01	0	f	2025-10-01 10:35:08.43494+01	0	22.834444182608767	1	12	1	38	17
826	15.675146322573472	Needs improvement	2025-10-01 10:35:08.444502+01	0	f	2025-10-01 10:35:08.444503+01	0	15.675146322573472	2	12	1	38	17
827	0	Good performance	2025-10-01 10:35:08.453783+01	1.3	t	2025-10-01 10:35:08.453784+01	50.699559456970576	50.699559456970576	3	12	1	38	17
828	0	Requires significant improvement	2025-10-01 10:35:08.462487+01	0	f	2025-10-01 10:35:08.462488+01	10.391243718957073	10.391243718957073	4	12	1	38	17
829	29.308602084719347	Excellent work!	2025-10-01 10:35:08.472602+01	0	f	2025-10-01 10:35:08.472603+01	0	29.308602084719347	1	12	2	38	17
830	25.300719583178008	Good performance	2025-10-01 10:35:08.482155+01	0	f	2025-10-01 10:35:08.482156+01	0	25.300719583178008	2	12	2	38	17
831	0	Needs improvement	2025-10-01 10:35:08.491749+01	0	f	2025-10-01 10:35:08.49175+01	40.63647431268057	40.63647431268057	3	12	2	38	17
832	0	Excellent work!	2025-10-01 10:35:08.501046+01	2	t	2025-10-01 10:35:08.501046+01	63.77923074386949	63.77923074386949	4	12	2	38	17
833	23.16579829816795	Good performance	2025-10-01 10:35:08.511024+01	0	f	2025-10-01 10:35:08.511025+01	0	23.16579829816795	1	15	1	38	22
834	23.73864378843873	Good performance	2025-10-01 10:35:08.520655+01	0	f	2025-10-01 10:35:08.520656+01	0	23.73864378843873	2	15	1	38	22
836	0	Needs improvement	2025-10-01 10:35:08.540041+01	0	f	2025-10-01 10:35:08.540042+01	41.48764881257658	41.48764881257658	4	15	1	38	22
837	29.602394137244293	Excellent work!	2025-10-01 10:35:08.549292+01	0	f	2025-10-01 10:35:08.549292+01	0	29.602394137244293	1	15	2	38	22
838	21.181739458040525	Good performance	2025-10-01 10:35:08.558928+01	0	f	2025-10-01 10:35:08.558929+01	0	21.181739458040525	2	15	2	38	22
839	0	Excellent work!	2025-10-01 10:35:08.568245+01	2.3	t	2025-10-01 10:35:08.568246+01	69.10805877772464	69.10805877772464	3	15	2	38	22
840	0	Requires significant improvement	2025-10-01 10:35:08.578223+01	0	f	2025-10-01 10:35:08.578223+01	8.465527784001043	8.465527784001043	4	15	2	38	22
841	7.703816099741	Requires significant improvement	2025-10-01 10:35:08.587823+01	0	f	2025-10-01 10:35:08.587824+01	0	7.703816099741	1	2	1	39	2
842	16.517878484407483	Needs improvement	2025-10-01 10:35:08.59683+01	0	f	2025-10-01 10:35:08.596831+01	0	16.517878484407483	2	2	1	39	2
843	0	Excellent work!	2025-10-01 10:35:08.607143+01	2.3	t	2025-10-01 10:35:08.607143+01	66.49343034493232	66.49343034493232	3	2	1	39	2
844	0	Excellent work!	2025-10-01 10:35:08.616514+01	2	t	2025-10-01 10:35:08.616514+01	62.58618732792403	62.58618732792403	4	2	1	39	2
845	19.835609550790675	Satisfactory	2025-10-01 10:35:08.62558+01	0	f	2025-10-01 10:35:08.625581+01	0	19.835609550790675	1	2	2	39	2
846	16.81135875215229	Needs improvement	2025-10-01 10:35:08.635183+01	0	f	2025-10-01 10:35:08.635184+01	0	16.81135875215229	2	2	2	39	2
847	0	Satisfactory	2025-10-01 10:35:08.645217+01	0	f	2025-10-01 10:35:08.645218+01	44.21962060289123	44.21962060289123	3	2	2	39	2
848	0	Good performance	2025-10-01 10:35:08.654839+01	1.3	t	2025-10-01 10:35:08.65484+01	53.814991992886334	53.814991992886334	4	2	2	39	2
849	27.07195982629684	Excellent work!	2025-10-01 10:35:08.664011+01	0	f	2025-10-01 10:35:08.664012+01	0	27.07195982629684	1	6	1	39	7
850	27.80387457000635	Excellent work!	2025-10-01 10:35:08.673658+01	0	f	2025-10-01 10:35:08.673658+01	0	27.80387457000635	2	6	1	39	7
851	0	Satisfactory	2025-10-01 10:35:08.684327+01	1	f	2025-10-01 10:35:08.684327+01	45.60462594299398	45.60462594299398	3	6	1	39	7
852	0	Satisfactory	2025-10-01 10:35:08.693927+01	0	f	2025-10-01 10:35:08.693928+01	44.41191129401718	44.41191129401718	4	6	1	39	7
853	8.333574337591703	Requires significant improvement	2025-10-01 10:35:08.703392+01	0	f	2025-10-01 10:35:08.703393+01	0	8.333574337591703	1	6	2	39	7
854	26.13173116333399	Excellent work!	2025-10-01 10:35:08.712975+01	0	f	2025-10-01 10:35:08.712975+01	0	26.13173116333399	2	6	2	39	7
855	0	Excellent work!	2025-10-01 10:35:08.722596+01	2	t	2025-10-01 10:35:08.722597+01	62.55572322082718	62.55572322082718	3	6	2	39	7
856	0	Excellent work!	2025-10-01 10:35:08.732433+01	2	t	2025-10-01 10:35:08.732434+01	62.97751671287998	62.97751671287998	4	6	2	39	7
857	10.554112712722596	Requires significant improvement	2025-10-01 10:35:08.742291+01	0	f	2025-10-01 10:35:08.742291+01	0	10.554112712722596	1	9	1	39	12
858	23.12046830408703	Good performance	2025-10-01 10:35:08.751968+01	0	f	2025-10-01 10:35:08.751978+01	0	23.12046830408703	2	9	1	39	12
859	0	Requires significant improvement	2025-10-01 10:35:08.76163+01	0	f	2025-10-01 10:35:08.76163+01	20.38464972245801	20.38464972245801	3	9	1	39	12
860	0	Excellent work!	2025-10-01 10:35:08.77339+01	2.3	t	2025-10-01 10:35:08.773391+01	67.88324111823684	67.88324111823684	4	9	1	39	12
861	0.45066771179271214	Requires significant improvement	2025-10-01 10:35:08.783751+01	0	f	2025-10-01 10:35:08.783751+01	0	0.45066771179271214	1	9	2	39	12
862	25.067623528947195	Good performance	2025-10-01 10:35:08.793668+01	0	f	2025-10-01 10:35:08.793668+01	0	25.067623528947195	2	9	2	39	12
863	0	Excellent work!	2025-10-01 10:35:08.804454+01	2	t	2025-10-01 10:35:08.804455+01	63.9857954217825	63.9857954217825	3	9	2	39	12
864	0	Good performance	2025-10-01 10:35:08.814848+01	1.7	t	2025-10-01 10:35:08.814848+01	56.68518991147684	56.68518991147684	4	9	2	39	12
865	28.409727989849905	Excellent work!	2025-10-01 10:35:08.824975+01	0	f	2025-10-01 10:35:08.824975+01	0	28.409727989849905	1	12	1	39	17
866	19.938204580776485	Satisfactory	2025-10-01 10:35:08.834894+01	0	f	2025-10-01 10:35:08.834894+01	0	19.938204580776485	2	12	1	39	17
867	0	Good performance	2025-10-01 10:35:08.845613+01	1.7	t	2025-10-01 10:35:08.845614+01	55.71212617219959	55.71212617219959	3	12	1	39	17
868	0	Requires significant improvement	2025-10-01 10:35:08.856055+01	0	f	2025-10-01 10:35:08.856056+01	17.31722918532575	17.31722918532575	4	12	1	39	17
869	20.633761863717194	Satisfactory	2025-10-01 10:35:08.866357+01	0	f	2025-10-01 10:35:08.866357+01	0	20.633761863717194	1	12	2	39	17
870	25.755554134542614	Excellent work!	2025-10-01 10:35:08.876842+01	0	f	2025-10-01 10:35:08.876843+01	0	25.755554134542614	2	12	2	39	17
871	0	Satisfactory	2025-10-01 10:35:08.886832+01	0	f	2025-10-01 10:35:08.886832+01	44.71392639842945	44.71392639842945	3	12	2	39	17
872	0	Needs improvement	2025-10-01 10:35:08.898436+01	0	f	2025-10-01 10:35:08.898436+01	37.11052543164939	37.11052543164939	4	12	2	39	17
873	28.69319502426504	Excellent work!	2025-10-01 10:35:08.910888+01	0	f	2025-10-01 10:35:08.910888+01	0	28.69319502426504	1	15	1	39	22
874	27.503107555859422	Excellent work!	2025-10-01 10:35:08.921653+01	0	f	2025-10-01 10:35:08.921653+01	0	27.503107555859422	2	15	1	39	22
875	0	Needs improvement	2025-10-01 10:35:08.931413+01	0	f	2025-10-01 10:35:08.931414+01	36.49386297090021	36.49386297090021	3	15	1	39	22
876	0	Requires significant improvement	2025-10-01 10:35:08.941855+01	0	f	2025-10-01 10:35:08.941856+01	19.920927589644702	19.920927589644702	4	15	1	39	22
877	27.19257170213679	Excellent work!	2025-10-01 10:35:08.9522+01	0	f	2025-10-01 10:35:08.952201+01	0	27.19257170213679	1	15	2	39	22
878	28.713911584820096	Excellent work!	2025-10-01 10:35:08.980631+01	0	f	2025-10-01 10:35:08.980634+01	0	28.713911584820096	2	15	2	39	22
879	0	Requires significant improvement	2025-10-01 10:35:08.99634+01	0	f	2025-10-01 10:35:08.996341+01	32.02505433490476	32.02505433490476	3	15	2	39	22
880	0	Excellent work!	2025-10-01 10:35:09.012628+01	2	t	2025-10-01 10:35:09.012628+01	64.78255355859949	64.78255355859949	4	15	2	39	22
881	24.692271705351597	Good performance	2025-10-01 10:35:09.028264+01	0	f	2025-10-01 10:35:09.028265+01	0	24.692271705351597	1	2	1	40	2
882	28.30436567524673	Excellent work!	2025-10-01 10:35:09.043644+01	0	f	2025-10-01 10:35:09.043645+01	0	28.30436567524673	2	2	1	40	2
883	0	Good performance	2025-10-01 10:35:09.059231+01	1.3	t	2025-10-01 10:35:09.059231+01	54.741564318578924	54.741564318578924	3	2	1	40	2
884	0	Requires significant improvement	2025-10-01 10:35:09.074822+01	0	f	2025-10-01 10:35:09.074823+01	16.949912541876827	16.949912541876827	4	2	1	40	2
885	15.091010540128206	Needs improvement	2025-10-01 10:35:09.094638+01	0	f	2025-10-01 10:35:09.09464+01	0	15.091010540128206	1	2	2	40	2
886	22.654827548213767	Good performance	2025-10-01 10:35:09.106076+01	0	f	2025-10-01 10:35:09.106077+01	0	22.654827548213767	2	2	2	40	2
887	0	Requires significant improvement	2025-10-01 10:35:09.116394+01	0	f	2025-10-01 10:35:09.116395+01	6.909233736043702	6.909233736043702	3	2	2	40	2
888	0	Excellent work!	2025-10-01 10:35:09.126709+01	2.3	t	2025-10-01 10:35:09.126709+01	67.54382298684592	67.54382298684592	4	2	2	40	2
889	26.35274094962577	Excellent work!	2025-10-01 10:35:09.137154+01	0	f	2025-10-01 10:35:09.137154+01	0	26.35274094962577	1	6	1	40	7
890	28.21387337070845	Excellent work!	2025-10-01 10:35:09.146621+01	0	f	2025-10-01 10:35:09.146621+01	0	28.21387337070845	2	6	1	40	7
891	0	Excellent work!	2025-10-01 10:35:09.157109+01	2	t	2025-10-01 10:35:09.15711+01	64.0244409653069	64.0244409653069	3	6	1	40	7
892	0	Satisfactory	2025-10-01 10:35:09.166491+01	0	f	2025-10-01 10:35:09.166492+01	43.49051995231136	43.49051995231136	4	6	1	40	7
893	0.5879307467518358	Requires significant improvement	2025-10-01 10:35:09.177126+01	0	f	2025-10-01 10:35:09.177126+01	0	0.5879307467518358	1	6	2	40	7
894	25.9825422531176	Excellent work!	2025-10-01 10:35:09.187244+01	0	f	2025-10-01 10:35:09.187245+01	0	25.9825422531176	2	6	2	40	7
895	0	Needs improvement	2025-10-01 10:35:09.196999+01	0	f	2025-10-01 10:35:09.196999+01	35.278035201474694	35.278035201474694	3	6	2	40	7
896	0	Good performance	2025-10-01 10:35:09.207493+01	1.3	t	2025-10-01 10:35:09.207494+01	52.92978074561205	52.92978074561205	4	6	2	40	7
897	9.679450622811288	Requires significant improvement	2025-10-01 10:35:09.217125+01	0	f	2025-10-01 10:35:09.217125+01	0	9.679450622811288	1	9	1	40	12
898	25.85728402494454	Excellent work!	2025-10-01 10:35:09.226761+01	0	f	2025-10-01 10:35:09.226762+01	0	25.85728402494454	2	9	1	40	12
899	0	Excellent work!	2025-10-01 10:35:09.236534+01	2	t	2025-10-01 10:35:09.236535+01	63.32926139992761	63.32926139992761	3	9	1	40	12
900	0	Satisfactory	2025-10-01 10:35:09.246264+01	1	f	2025-10-01 10:35:09.246264+01	46.3199958139635	46.3199958139635	4	9	1	40	12
901	18.968554993617587	Satisfactory	2025-10-01 10:35:09.255842+01	0	f	2025-10-01 10:35:09.255843+01	0	18.968554993617587	1	9	2	40	12
902	4.661816612868744	Requires significant improvement	2025-10-01 10:35:09.264905+01	0	f	2025-10-01 10:35:09.264906+01	0	4.661816612868744	2	9	2	40	12
903	0	Excellent work!	2025-10-01 10:35:09.275326+01	2.3	t	2025-10-01 10:35:09.275326+01	65.15501234500523	65.15501234500523	3	9	2	40	12
904	0	Requires significant improvement	2025-10-01 10:35:09.285389+01	0	f	2025-10-01 10:35:09.28539+01	10.271002795235251	10.271002795235251	4	9	2	40	12
905	20.869973493507782	Satisfactory	2025-10-01 10:35:09.294869+01	0	f	2025-10-01 10:35:09.29487+01	0	20.869973493507782	1	12	1	40	17
906	27.915817440345	Excellent work!	2025-10-01 10:35:09.30537+01	0	f	2025-10-01 10:35:09.30537+01	0	27.915817440345	2	12	1	40	17
907	0	Excellent work!	2025-10-01 10:35:09.315656+01	2.3	t	2025-10-01 10:35:09.315656+01	69.88854153525348	69.88854153525348	3	12	1	40	17
908	0	Excellent work!	2025-10-01 10:35:09.325578+01	2.3	t	2025-10-01 10:35:09.325578+01	65.77157674157911	65.77157674157911	4	12	1	40	17
909	17.56703861310172	Needs improvement	2025-10-01 10:35:09.335302+01	0	f	2025-10-01 10:35:09.335302+01	0	17.56703861310172	1	12	2	40	17
910	24.575799332872442	Good performance	2025-10-01 10:35:09.344773+01	0	f	2025-10-01 10:35:09.344774+01	0	24.575799332872442	2	12	2	40	17
911	0	Excellent work!	2025-10-01 10:35:09.35434+01	2.3	t	2025-10-01 10:35:09.354341+01	69.38432174134385	69.38432174134385	3	12	2	40	17
912	0	Requires significant improvement	2025-10-01 10:35:09.36371+01	0	f	2025-10-01 10:35:09.363711+01	3.873826004547461	3.873826004547461	4	12	2	40	17
913	27.853428823191926	Excellent work!	2025-10-01 10:35:09.374271+01	0	f	2025-10-01 10:35:09.374271+01	0	27.853428823191926	1	15	1	40	22
914	16.737399303446942	Needs improvement	2025-10-01 10:35:09.384481+01	0	f	2025-10-01 10:35:09.384481+01	0	16.737399303446942	2	15	1	40	22
915	0	Good performance	2025-10-01 10:35:09.394214+01	1.7	t	2025-10-01 10:35:09.394214+01	59.3346116976852	59.3346116976852	3	15	1	40	22
916	0	Requires significant improvement	2025-10-01 10:35:09.404654+01	0	f	2025-10-01 10:35:09.404654+01	29.944861097717947	29.944861097717947	4	15	1	40	22
917	24.41480597065942	Good performance	2025-10-01 10:35:09.415146+01	0	f	2025-10-01 10:35:09.415146+01	0	24.41480597065942	1	15	2	40	22
918	15.380586401510588	Needs improvement	2025-10-01 10:35:09.428255+01	0	f	2025-10-01 10:35:09.428255+01	0	15.380586401510588	2	15	2	40	22
919	0	Excellent work!	2025-10-01 10:35:09.4396+01	2	t	2025-10-01 10:35:09.439601+01	63.34213618830209	63.34213618830209	3	15	2	40	22
920	0	Excellent work!	2025-10-01 10:35:09.449776+01	2	t	2025-10-01 10:35:09.449776+01	61.315440389100644	61.315440389100644	4	15	2	40	22
921	8.498597048591426	Requires significant improvement	2025-10-01 10:35:09.461004+01	0	f	2025-10-01 10:35:09.461004+01	0	8.498597048591426	1	2	1	41	2
922	22.374960425755457	Good performance	2025-10-01 10:35:09.471415+01	0	f	2025-10-01 10:35:09.471415+01	0	22.374960425755457	2	2	1	41	2
923	0	Needs improvement	2025-10-01 10:35:09.481211+01	0	f	2025-10-01 10:35:09.481211+01	39.924428394327734	39.924428394327734	3	2	1	41	2
924	0	Excellent work!	2025-10-01 10:35:09.490929+01	2	t	2025-10-01 10:35:09.49093+01	61.80211978435932	61.80211978435932	4	2	1	41	2
925	29.627042539462444	Excellent work!	2025-10-01 10:35:09.501437+01	0	f	2025-10-01 10:35:09.501438+01	0	29.627042539462444	1	2	2	41	2
926	29.37478024887931	Excellent work!	2025-10-01 10:35:09.516283+01	0	f	2025-10-01 10:35:09.516283+01	0	29.37478024887931	2	2	2	41	2
927	0	Excellent work!	2025-10-01 10:35:09.526727+01	2	t	2025-10-01 10:35:09.526728+01	60.79000265858109	60.79000265858109	3	2	2	41	2
928	0	Needs improvement	2025-10-01 10:35:09.542382+01	0	f	2025-10-01 10:35:09.542382+01	41.5609615002429	41.5609615002429	4	2	2	41	2
929	22.506702353857328	Good performance	2025-10-01 10:35:09.558464+01	0	f	2025-10-01 10:35:09.558464+01	0	22.506702353857328	1	6	1	41	7
930	20.393630945485512	Satisfactory	2025-10-01 10:35:09.569915+01	0	f	2025-10-01 10:35:09.569915+01	0	20.393630945485512	2	6	1	41	7
931	0	Good performance	2025-10-01 10:35:09.583386+01	1.7	t	2025-10-01 10:35:09.583386+01	58.34780980737757	58.34780980737757	3	6	1	41	7
932	0	Needs improvement	2025-10-01 10:35:09.593766+01	0	f	2025-10-01 10:35:09.593766+01	36.29573259278614	36.29573259278614	4	6	1	41	7
933	28.97971422408209	Excellent work!	2025-10-01 10:35:09.605495+01	0	f	2025-10-01 10:35:09.605495+01	0	28.97971422408209	1	6	2	41	7
934	19.816939884826414	Satisfactory	2025-10-01 10:35:09.61593+01	0	f	2025-10-01 10:35:09.615931+01	0	19.816939884826414	2	6	2	41	7
935	0	Excellent work!	2025-10-01 10:35:09.62642+01	2.3	t	2025-10-01 10:35:09.626421+01	69.94856143574742	69.94856143574742	3	6	2	41	7
936	0	Needs improvement	2025-10-01 10:35:09.637104+01	0	f	2025-10-01 10:35:09.637104+01	37.59357028625106	37.59357028625106	4	6	2	41	7
937	20.082828949588112	Satisfactory	2025-10-01 10:35:09.647817+01	0	f	2025-10-01 10:35:09.647818+01	0	20.082828949588112	1	9	1	41	12
938	21.571974669809507	Good performance	2025-10-01 10:35:09.658245+01	0	f	2025-10-01 10:35:09.658246+01	0	21.571974669809507	2	9	1	41	12
939	0	Good performance	2025-10-01 10:35:09.669158+01	1.3	t	2025-10-01 10:35:09.669158+01	50.31239333872047	50.31239333872047	3	9	1	41	12
940	0	Excellent work!	2025-10-01 10:35:09.68006+01	2.3	t	2025-10-01 10:35:09.68006+01	68.16822614714233	68.16822614714233	4	9	1	41	12
941	25.885422357119513	Excellent work!	2025-10-01 10:35:09.690349+01	0	f	2025-10-01 10:35:09.69035+01	0	25.885422357119513	1	9	2	41	12
942	21.76847558757227	Good performance	2025-10-01 10:35:09.700957+01	0	f	2025-10-01 10:35:09.700958+01	0	21.76847558757227	2	9	2	41	12
943	0	Good performance	2025-10-01 10:35:09.712066+01	1.7	t	2025-10-01 10:35:09.712066+01	56.77166367086783	56.77166367086783	3	9	2	41	12
944	0	Requires significant improvement	2025-10-01 10:35:09.722603+01	0	f	2025-10-01 10:35:09.722604+01	31.787680231811574	31.787680231811574	4	9	2	41	12
945	23.231596955661317	Good performance	2025-10-01 10:35:09.733027+01	0	f	2025-10-01 10:35:09.733027+01	0	23.231596955661317	1	12	1	41	17
946	27.37234143024683	Excellent work!	2025-10-01 10:35:09.744207+01	0	f	2025-10-01 10:35:09.744207+01	0	27.37234143024683	2	12	1	41	17
947	0	Requires significant improvement	2025-10-01 10:35:09.755587+01	0	f	2025-10-01 10:35:09.755587+01	28.831817880612217	28.831817880612217	3	12	1	41	17
948	0	Satisfactory	2025-10-01 10:35:09.766437+01	1	f	2025-10-01 10:35:09.766437+01	45.937308840074536	45.937308840074536	4	12	1	41	17
949	17.44776427176834	Needs improvement	2025-10-01 10:35:09.782827+01	0	f	2025-10-01 10:35:09.782827+01	0	17.44776427176834	1	12	2	41	17
950	23.077115015646623	Good performance	2025-10-01 10:35:09.798731+01	0	f	2025-10-01 10:35:09.798731+01	0	23.077115015646623	2	12	2	41	17
951	0	Good performance	2025-10-01 10:35:09.814908+01	1.3	t	2025-10-01 10:35:09.814909+01	50.92074613297502	50.92074613297502	3	12	2	41	17
952	0	Requires significant improvement	2025-10-01 10:35:09.831235+01	0	f	2025-10-01 10:35:09.831235+01	0.4959170302772353	0.4959170302772353	4	12	2	41	17
953	13.858276337032931	Requires significant improvement	2025-10-01 10:35:09.84851+01	0	f	2025-10-01 10:35:09.848511+01	0	13.858276337032931	1	15	1	41	22
954	18.328535125808802	Satisfactory	2025-10-01 10:35:09.865523+01	0	f	2025-10-01 10:35:09.865524+01	0	18.328535125808802	2	15	1	41	22
955	0	Excellent work!	2025-10-01 10:35:09.883078+01	2	t	2025-10-01 10:35:09.883078+01	61.42039624033212	61.42039624033212	3	15	1	41	22
956	0	Requires significant improvement	2025-10-01 10:35:09.898047+01	0	f	2025-10-01 10:35:09.898048+01	3.824059779569979	3.824059779569979	4	15	1	41	22
957	29.20599504992436	Excellent work!	2025-10-01 10:35:09.910275+01	0	f	2025-10-01 10:35:09.910276+01	0	29.20599504992436	1	15	2	41	22
958	24.310175039926982	Good performance	2025-10-01 10:35:09.921523+01	0	f	2025-10-01 10:35:09.921524+01	0	24.310175039926982	2	15	2	41	22
959	0	Excellent work!	2025-10-01 10:35:09.934419+01	2	t	2025-10-01 10:35:09.934419+01	60.71750456483849	60.71750456483849	3	15	2	41	22
960	0	Needs improvement	2025-10-01 10:35:09.95146+01	0	f	2025-10-01 10:35:09.951461+01	39.9251980166896	39.9251980166896	4	15	2	41	22
961	25.666955118219143	Excellent work!	2025-10-01 10:35:09.964732+01	0	f	2025-10-01 10:35:09.964732+01	0	25.666955118219143	1	2	1	42	2
962	17.00083891571223	Needs improvement	2025-10-01 10:35:09.975335+01	0	f	2025-10-01 10:35:09.975336+01	0	17.00083891571223	2	2	1	42	2
963	0	Excellent work!	2025-10-01 10:35:09.986105+01	2.3	t	2025-10-01 10:35:09.986105+01	68.18469455520366	68.18469455520366	3	2	1	42	2
964	0	Satisfactory	2025-10-01 10:35:09.995905+01	0	f	2025-10-01 10:35:09.995906+01	42.642089932846474	42.642089932846474	4	2	1	42	2
965	27.870313474101668	Excellent work!	2025-10-01 10:35:10.006098+01	0	f	2025-10-01 10:35:10.006099+01	0	27.870313474101668	1	2	2	42	2
966	18.724664213562907	Satisfactory	2025-10-01 10:35:10.015826+01	0	f	2025-10-01 10:35:10.015827+01	0	18.724664213562907	2	2	2	42	2
967	0	Excellent work!	2025-10-01 10:35:10.025753+01	2.3	t	2025-10-01 10:35:10.025753+01	65.76265638320055	65.76265638320055	3	2	2	42	2
968	0	Excellent work!	2025-10-01 10:35:10.035849+01	2.3	t	2025-10-01 10:35:10.03585+01	67.88794001506173	67.88794001506173	4	2	2	42	2
969	16.490142972941822	Needs improvement	2025-10-01 10:35:10.045917+01	0	f	2025-10-01 10:35:10.045918+01	0	16.490142972941822	1	6	1	42	7
970	21.004475188423605	Good performance	2025-10-01 10:35:10.055894+01	0	f	2025-10-01 10:35:10.055895+01	0	21.004475188423605	2	6	1	42	7
971	0	Requires significant improvement	2025-10-01 10:35:10.071369+01	0	f	2025-10-01 10:35:10.07137+01	17.444860419861087	17.444860419861087	3	6	1	42	7
972	0	Good performance	2025-10-01 10:35:10.086982+01	1.3	t	2025-10-01 10:35:10.086982+01	51.753196967784675	51.753196967784675	4	6	1	42	7
973	27.422788782948405	Excellent work!	2025-10-01 10:35:10.097357+01	0	f	2025-10-01 10:35:10.097358+01	0	27.422788782948405	1	6	2	42	7
974	25.56089004120592	Excellent work!	2025-10-01 10:35:10.108596+01	0	f	2025-10-01 10:35:10.108597+01	0	25.56089004120592	2	6	2	42	7
975	0	Excellent work!	2025-10-01 10:35:10.118793+01	2.3	t	2025-10-01 10:35:10.118793+01	66.50694591165869	66.50694591165869	3	6	2	42	7
976	0	Satisfactory	2025-10-01 10:35:10.129381+01	0	f	2025-10-01 10:35:10.129381+01	43.22264256097006	43.22264256097006	4	6	2	42	7
977	25.38526964591957	Good performance	2025-10-01 10:35:10.140591+01	0	f	2025-10-01 10:35:10.140592+01	0	25.38526964591957	1	9	1	42	12
978	21.46659020784661	Good performance	2025-10-01 10:35:10.150796+01	0	f	2025-10-01 10:35:10.150796+01	0	21.46659020784661	2	9	1	42	12
979	0	Requires significant improvement	2025-10-01 10:35:10.161071+01	0	f	2025-10-01 10:35:10.161071+01	27.968748794536168	27.968748794536168	3	9	1	42	12
980	0	Excellent work!	2025-10-01 10:35:10.171259+01	2.3	t	2025-10-01 10:35:10.171259+01	68.68899419533489	68.68899419533489	4	9	1	42	12
981	28.11390282025368	Excellent work!	2025-10-01 10:35:10.18138+01	0	f	2025-10-01 10:35:10.181381+01	0	28.11390282025368	1	9	2	42	12
982	29.949436038206514	Excellent work!	2025-10-01 10:35:10.192065+01	0	f	2025-10-01 10:35:10.192066+01	0	29.949436038206514	2	9	2	42	12
983	0	Excellent work!	2025-10-01 10:35:10.203017+01	2.3	t	2025-10-01 10:35:10.203017+01	67.46582174652713	67.46582174652713	3	9	2	42	12
984	0	Excellent work!	2025-10-01 10:35:10.213842+01	2.3	t	2025-10-01 10:35:10.213843+01	69.64130886992066	69.64130886992066	4	9	2	42	12
985	3.2862622562181	Requires significant improvement	2025-10-01 10:35:10.224936+01	0	f	2025-10-01 10:35:10.224937+01	0	3.2862622562181	1	12	1	42	17
986	27.292595076228988	Excellent work!	2025-10-01 10:35:10.235475+01	0	f	2025-10-01 10:35:10.235475+01	0	27.292595076228988	2	12	1	42	17
987	0	Satisfactory	2025-10-01 10:35:10.246286+01	1	f	2025-10-01 10:35:10.246286+01	45.84275764063306	45.84275764063306	3	12	1	42	17
988	0	Needs improvement	2025-10-01 10:35:10.256965+01	0	f	2025-10-01 10:35:10.256965+01	38.450454757906606	38.450454757906606	4	12	1	42	17
989	25.440370279370114	Good performance	2025-10-01 10:35:10.267337+01	0	f	2025-10-01 10:35:10.267338+01	0	25.440370279370114	1	12	2	42	17
990	29.996368491209278	Excellent work!	2025-10-01 10:35:10.278404+01	0	f	2025-10-01 10:35:10.278404+01	0	29.996368491209278	2	12	2	42	17
991	0	Good performance	2025-10-01 10:35:10.289061+01	1.3	t	2025-10-01 10:35:10.289062+01	52.00612654906148	52.00612654906148	3	12	2	42	17
992	0	Excellent work!	2025-10-01 10:35:10.299187+01	2.3	t	2025-10-01 10:35:10.299187+01	66.51073123769476	66.51073123769476	4	12	2	42	17
993	28.018978908228856	Excellent work!	2025-10-01 10:35:10.311009+01	0	f	2025-10-01 10:35:10.311009+01	0	28.018978908228856	1	15	1	42	22
994	26.239874458875875	Excellent work!	2025-10-01 10:35:10.321911+01	0	f	2025-10-01 10:35:10.321912+01	0	26.239874458875875	2	15	1	42	22
995	0	Requires significant improvement	2025-10-01 10:35:10.332489+01	0	f	2025-10-01 10:35:10.33249+01	25.82598155651419	25.82598155651419	3	15	1	42	22
996	0	Excellent work!	2025-10-01 10:35:10.344386+01	2	t	2025-10-01 10:35:10.344386+01	64.18679105323938	64.18679105323938	4	15	1	42	22
997	11.568717494263728	Requires significant improvement	2025-10-01 10:35:10.355215+01	0	f	2025-10-01 10:35:10.355215+01	0	11.568717494263728	1	15	2	42	22
998	0.9636447698655959	Requires significant improvement	2025-10-01 10:35:10.365456+01	0	f	2025-10-01 10:35:10.365456+01	0	0.9636447698655959	2	15	2	42	22
999	0	Good performance	2025-10-01 10:35:10.376764+01	1.7	t	2025-10-01 10:35:10.376764+01	57.386385955352814	57.386385955352814	3	15	2	42	22
1000	0	Requires significant improvement	2025-10-01 10:35:10.387464+01	0	f	2025-10-01 10:35:10.387464+01	26.438563825904023	26.438563825904023	4	15	2	42	22
1001	28.65183571295929	Excellent work!	2025-10-01 10:35:10.398025+01	0	f	2025-10-01 10:35:10.398025+01	0	28.65183571295929	1	2	1	43	2
1002	3.070465602277438	Requires significant improvement	2025-10-01 10:35:10.409148+01	0	f	2025-10-01 10:35:10.409148+01	0	3.070465602277438	2	2	1	43	2
1003	0	Excellent work!	2025-10-01 10:35:10.419359+01	2	t	2025-10-01 10:35:10.419359+01	61.01531298883647	61.01531298883647	3	2	1	43	2
1004	0	Excellent work!	2025-10-01 10:35:10.430704+01	2.3	t	2025-10-01 10:35:10.430705+01	65.39051195095281	65.39051195095281	4	2	1	43	2
1005	6.0993565045185205	Requires significant improvement	2025-10-01 10:35:10.441852+01	0	f	2025-10-01 10:35:10.441852+01	0	6.0993565045185205	1	2	2	43	2
1006	12.901810083425142	Requires significant improvement	2025-10-01 10:35:10.452445+01	0	f	2025-10-01 10:35:10.452446+01	0	12.901810083425142	2	2	2	43	2
1007	0	Excellent work!	2025-10-01 10:35:10.463012+01	2.3	t	2025-10-01 10:35:10.463012+01	68.29605628180045	68.29605628180045	3	2	2	43	2
1008	0	Excellent work!	2025-10-01 10:35:10.474416+01	2.3	t	2025-10-01 10:35:10.474417+01	67.54227503790126	67.54227503790126	4	2	2	43	2
1009	10.107150654746123	Requires significant improvement	2025-10-01 10:35:10.485241+01	0	f	2025-10-01 10:35:10.485241+01	0	10.107150654746123	1	6	1	43	7
1010	27.73083857282027	Excellent work!	2025-10-01 10:35:10.49622+01	0	f	2025-10-01 10:35:10.496221+01	0	27.73083857282027	2	6	1	43	7
1011	0	Excellent work!	2025-10-01 10:35:10.508391+01	2.3	t	2025-10-01 10:35:10.508391+01	66.79087285029775	66.79087285029775	3	6	1	43	7
1012	0	Satisfactory	2025-10-01 10:35:10.519812+01	1	f	2025-10-01 10:35:10.519812+01	47.33031182461836	47.33031182461836	4	6	1	43	7
1013	16.97957511971539	Needs improvement	2025-10-01 10:35:10.540902+01	0	f	2025-10-01 10:35:10.540903+01	0	16.97957511971539	1	6	2	43	7
1014	28.222985960266897	Excellent work!	2025-10-01 10:35:10.551738+01	0	f	2025-10-01 10:35:10.551738+01	0	28.222985960266897	2	6	2	43	7
1015	0	Good performance	2025-10-01 10:35:10.562266+01	1.3	t	2025-10-01 10:35:10.562266+01	52.98289797516721	52.98289797516721	3	6	2	43	7
1016	0	Good performance	2025-10-01 10:35:10.573014+01	1.7	t	2025-10-01 10:35:10.573014+01	58.366469178209606	58.366469178209606	4	6	2	43	7
1017	15.962682106682802	Needs improvement	2025-10-01 10:35:10.583831+01	0	f	2025-10-01 10:35:10.583832+01	0	15.962682106682802	1	9	1	43	12
1018	28.014854602547366	Excellent work!	2025-10-01 10:35:10.594493+01	0	f	2025-10-01 10:35:10.594493+01	0	28.014854602547366	2	9	1	43	12
1019	0	Needs improvement	2025-10-01 10:35:10.605826+01	0	f	2025-10-01 10:35:10.605827+01	35.44437128888774	35.44437128888774	3	9	1	43	12
1020	0	Excellent work!	2025-10-01 10:35:10.616538+01	2.3	t	2025-10-01 10:35:10.616538+01	69.73243595375985	69.73243595375985	4	9	1	43	12
1021	29.787447549331468	Excellent work!	2025-10-01 10:35:10.627109+01	0	f	2025-10-01 10:35:10.62711+01	0	29.787447549331468	1	9	2	43	12
1022	29.49026007442413	Excellent work!	2025-10-01 10:35:10.638054+01	0	f	2025-10-01 10:35:10.638054+01	0	29.49026007442413	2	9	2	43	12
1023	0	Excellent work!	2025-10-01 10:35:10.64878+01	2	t	2025-10-01 10:35:10.648781+01	62.39370632790866	62.39370632790866	3	9	2	43	12
1024	0	Excellent work!	2025-10-01 10:35:10.659334+01	2	t	2025-10-01 10:35:10.659335+01	62.50790359989648	62.50790359989648	4	9	2	43	12
1025	23.011318147642022	Good performance	2025-10-01 10:35:10.670284+01	0	f	2025-10-01 10:35:10.670284+01	0	23.011318147642022	1	12	1	43	17
1026	1.935487741783863	Requires significant improvement	2025-10-01 10:35:10.680727+01	0	f	2025-10-01 10:35:10.680728+01	0	1.935487741783863	2	12	1	43	17
1027	0	Satisfactory	2025-10-01 10:35:10.691502+01	1	f	2025-10-01 10:35:10.691503+01	48.99352714666115	48.99352714666115	3	12	1	43	17
1028	0	Requires significant improvement	2025-10-01 10:35:10.701807+01	0	f	2025-10-01 10:35:10.701808+01	17.99871335221764	17.99871335221764	4	12	1	43	17
1029	27.29580742669014	Excellent work!	2025-10-01 10:35:10.713537+01	0	f	2025-10-01 10:35:10.713537+01	0	27.29580742669014	1	12	2	43	17
1030	22.71626078030861	Good performance	2025-10-01 10:35:10.724279+01	0	f	2025-10-01 10:35:10.72428+01	0	22.71626078030861	2	12	2	43	17
1031	0	Satisfactory	2025-10-01 10:35:10.736299+01	1	f	2025-10-01 10:35:10.7363+01	46.03408847262353	46.03408847262353	3	12	2	43	17
1032	0	Good performance	2025-10-01 10:35:10.747753+01	1.7	t	2025-10-01 10:35:10.747754+01	57.51098860361607	57.51098860361607	4	12	2	43	17
1033	27.813150981079055	Excellent work!	2025-10-01 10:35:10.758659+01	0	f	2025-10-01 10:35:10.75866+01	0	27.813150981079055	1	15	1	43	22
1034	21.942307584884105	Good performance	2025-10-01 10:35:10.770701+01	0	f	2025-10-01 10:35:10.770702+01	0	21.942307584884105	2	15	1	43	22
1035	0	Excellent work!	2025-10-01 10:35:10.782185+01	2	t	2025-10-01 10:35:10.782186+01	63.89094596966267	63.89094596966267	3	15	1	43	22
1036	0	Good performance	2025-10-01 10:35:10.793215+01	1.7	t	2025-10-01 10:35:10.793216+01	55.16020018496337	55.16020018496337	4	15	1	43	22
1037	29.04590707701218	Excellent work!	2025-10-01 10:35:10.803807+01	0	f	2025-10-01 10:35:10.803807+01	0	29.04590707701218	1	15	2	43	22
1038	26.83760663121293	Excellent work!	2025-10-01 10:35:10.815616+01	0	f	2025-10-01 10:35:10.815616+01	0	26.83760663121293	2	15	2	43	22
1039	0	Excellent work!	2025-10-01 10:35:10.82646+01	2.3	t	2025-10-01 10:35:10.826461+01	69.90234902441843	69.90234902441843	3	15	2	43	22
1040	0	Excellent work!	2025-10-01 10:35:10.837747+01	2.3	t	2025-10-01 10:35:10.837747+01	66.17103083597698	66.17103083597698	4	15	2	43	22
1041	29.441796825001536	Excellent work!	2025-10-01 10:35:10.849339+01	0	f	2025-10-01 10:35:10.84934+01	0	29.441796825001536	1	2	1	44	2
1042	3.279920420100653	Requires significant improvement	2025-10-01 10:35:10.860424+01	0	f	2025-10-01 10:35:10.860425+01	0	3.279920420100653	2	2	1	44	2
1043	0	Excellent work!	2025-10-01 10:35:10.872076+01	2	t	2025-10-01 10:35:10.872077+01	61.401514471329776	61.401514471329776	3	2	1	44	2
1044	0	Requires significant improvement	2025-10-01 10:35:10.883138+01	0	f	2025-10-01 10:35:10.883138+01	5.328492605028681	5.328492605028681	4	2	1	44	2
1045	10.273113892060028	Requires significant improvement	2025-10-01 10:35:10.894732+01	0	f	2025-10-01 10:35:10.894732+01	0	10.273113892060028	1	2	2	44	2
1046	25.063811682396068	Good performance	2025-10-01 10:35:10.906477+01	0	f	2025-10-01 10:35:10.906478+01	0	25.063811682396068	2	2	2	44	2
1047	0	Requires significant improvement	2025-10-01 10:35:10.917766+01	0	f	2025-10-01 10:35:10.917767+01	32.33552032494684	32.33552032494684	3	2	2	44	2
1048	0	Needs improvement	2025-10-01 10:35:10.928803+01	0	f	2025-10-01 10:35:10.928803+01	41.48921923949472	41.48921923949472	4	2	2	44	2
1049	15.394522695249726	Needs improvement	2025-10-01 10:35:10.941621+01	0	f	2025-10-01 10:35:10.941622+01	0	15.394522695249726	1	6	1	44	7
1050	19.228871581675197	Satisfactory	2025-10-01 10:35:10.952976+01	0	f	2025-10-01 10:35:10.952977+01	0	19.228871581675197	2	6	1	44	7
1051	0	Needs improvement	2025-10-01 10:35:10.964051+01	0	f	2025-10-01 10:35:10.964052+01	36.36666751591315	36.36666751591315	3	6	1	44	7
1052	0	Excellent work!	2025-10-01 10:35:10.97537+01	2.3	t	2025-10-01 10:35:10.97537+01	66.5042140884455	66.5042140884455	4	6	1	44	7
1053	5.495204117053422	Requires significant improvement	2025-10-01 10:35:10.986329+01	0	f	2025-10-01 10:35:10.98633+01	0	5.495204117053422	1	6	2	44	7
1054	17.1589960825496	Needs improvement	2025-10-01 10:35:10.997381+01	0	f	2025-10-01 10:35:10.997382+01	0	17.1589960825496	2	6	2	44	7
1055	0	Excellent work!	2025-10-01 10:35:11.009126+01	2.3	t	2025-10-01 10:35:11.009126+01	68.25764581666587	68.25764581666587	3	6	2	44	7
1056	0	Satisfactory	2025-10-01 10:35:11.020485+01	1	f	2025-10-01 10:35:11.020485+01	48.800279611474004	48.800279611474004	4	6	2	44	7
1057	29.979752256716196	Excellent work!	2025-10-01 10:35:11.031692+01	0	f	2025-10-01 10:35:11.031693+01	0	29.979752256716196	1	9	1	44	12
1058	23.953319885333364	Good performance	2025-10-01 10:35:11.043456+01	0	f	2025-10-01 10:35:11.043456+01	0	23.953319885333364	2	9	1	44	12
1059	0	Good performance	2025-10-01 10:35:11.054909+01	1.7	t	2025-10-01 10:35:11.05491+01	55.53736482548271	55.53736482548271	3	9	1	44	12
1060	0	Requires significant improvement	2025-10-01 10:35:11.065881+01	0	f	2025-10-01 10:35:11.065882+01	12.714877964272244	12.714877964272244	4	9	1	44	12
1061	20.21489264989716	Satisfactory	2025-10-01 10:35:11.077738+01	0	f	2025-10-01 10:35:11.077739+01	0	20.21489264989716	1	9	2	44	12
1062	23.77915654948053	Good performance	2025-10-01 10:35:11.08899+01	0	f	2025-10-01 10:35:11.088991+01	0	23.77915654948053	2	9	2	44	12
1063	0	Excellent work!	2025-10-01 10:35:11.099706+01	2.3	t	2025-10-01 10:35:11.099707+01	66.6261351987339	66.6261351987339	3	9	2	44	12
1064	0	Good performance	2025-10-01 10:35:11.111505+01	1.3	t	2025-10-01 10:35:11.111506+01	51.65453054784727	51.65453054784727	4	9	2	44	12
1065	17.42448646680495	Needs improvement	2025-10-01 10:35:11.122731+01	0	f	2025-10-01 10:35:11.122732+01	0	17.42448646680495	1	12	1	44	17
1066	4.977995908661861	Requires significant improvement	2025-10-01 10:35:11.133688+01	0	f	2025-10-01 10:35:11.133689+01	0	4.977995908661861	2	12	1	44	17
1067	0	Needs improvement	2025-10-01 10:35:11.145+01	0	f	2025-10-01 10:35:11.145+01	41.58816862619081	41.58816862619081	3	12	1	44	17
1068	0	Excellent work!	2025-10-01 10:35:11.156261+01	2	t	2025-10-01 10:35:11.156261+01	64.19338578208988	64.19338578208988	4	12	1	44	17
1069	17.378373584160403	Needs improvement	2025-10-01 10:35:11.167761+01	0	f	2025-10-01 10:35:11.167761+01	0	17.378373584160403	1	12	2	44	17
1070	6.430959293813903	Requires significant improvement	2025-10-01 10:35:11.179269+01	0	f	2025-10-01 10:35:11.179269+01	0	6.430959293813903	2	12	2	44	17
1071	0	Requires significant improvement	2025-10-01 10:35:11.191182+01	0	f	2025-10-01 10:35:11.191183+01	25.32918156751335	25.32918156751335	3	12	2	44	17
1072	0	Needs improvement	2025-10-01 10:35:11.202435+01	0	f	2025-10-01 10:35:11.202435+01	38.66877754515691	38.66877754515691	4	12	2	44	17
1073	18.16277278326458	Satisfactory	2025-10-01 10:35:11.21393+01	0	f	2025-10-01 10:35:11.213931+01	0	18.16277278326458	1	15	1	44	22
1074	3.392789781535039	Requires significant improvement	2025-10-01 10:35:11.225312+01	0	f	2025-10-01 10:35:11.225313+01	0	3.392789781535039	2	15	1	44	22
1075	0	Requires significant improvement	2025-10-01 10:35:11.23739+01	0	f	2025-10-01 10:35:11.237391+01	2.263352341857132	2.263352341857132	3	15	1	44	22
1076	0	Requires significant improvement	2025-10-01 10:35:11.249024+01	0	f	2025-10-01 10:35:11.249024+01	14.689675177915706	14.689675177915706	4	15	1	44	22
1077	23.31361838608982	Good performance	2025-10-01 10:35:11.260696+01	0	f	2025-10-01 10:35:11.260696+01	0	23.31361838608982	1	15	2	44	22
1078	23.03799098525574	Good performance	2025-10-01 10:35:11.272748+01	0	f	2025-10-01 10:35:11.272749+01	0	23.03799098525574	2	15	2	44	22
1079	0	Good performance	2025-10-01 10:35:11.284075+01	1.7	t	2025-10-01 10:35:11.284076+01	57.82755246190887	57.82755246190887	3	15	2	44	22
1080	0	Excellent work!	2025-10-01 10:35:11.295678+01	2	t	2025-10-01 10:35:11.295678+01	63.02273807767931	63.02273807767931	4	15	2	44	22
1082	11.299519698827233	Requires significant improvement	2025-10-01 10:35:11.319351+01	0	f	2025-10-01 10:35:11.319352+01	0	11.299519698827233	2	3	1	45	3
1083	0	Excellent work!	2025-10-01 10:35:11.330124+01	2	t	2025-10-01 10:35:11.330125+01	64.51294367211227	64.51294367211227	3	3	1	45	3
1084	0	Satisfactory	2025-10-01 10:35:11.34201+01	1	f	2025-10-01 10:35:11.34201+01	46.20355418819207	46.20355418819207	4	3	1	45	3
1085	15.595009991469068	Needs improvement	2025-10-01 10:35:11.353908+01	0	f	2025-10-01 10:35:11.353908+01	0	15.595009991469068	1	3	2	45	3
1086	29.315934480293194	Excellent work!	2025-10-01 10:35:11.365758+01	0	f	2025-10-01 10:35:11.365759+01	0	29.315934480293194	2	3	2	45	3
1087	0	Good performance	2025-10-01 10:35:11.378461+01	1.3	t	2025-10-01 10:35:11.378461+01	52.852515339663725	52.852515339663725	3	3	2	45	3
1088	0	Needs improvement	2025-10-01 10:35:11.389919+01	0	f	2025-10-01 10:35:11.38992+01	36.74047712834027	36.74047712834027	4	3	2	45	3
1089	29.224882470134506	Excellent work!	2025-10-01 10:35:11.400783+01	0	f	2025-10-01 10:35:11.400784+01	0	29.224882470134506	1	7	1	45	8
1090	22.078646638812646	Good performance	2025-10-01 10:35:11.412397+01	0	f	2025-10-01 10:35:11.412397+01	0	22.078646638812646	2	7	1	45	8
1091	0	Good performance	2025-10-01 10:35:11.423612+01	1.7	t	2025-10-01 10:35:11.423613+01	55.56694494134098	55.56694494134098	3	7	1	45	8
1092	0	Excellent work!	2025-10-01 10:35:11.434674+01	2	t	2025-10-01 10:35:11.434675+01	61.473627487812315	61.473627487812315	4	7	1	45	8
1093	26.68003434683588	Excellent work!	2025-10-01 10:35:11.447256+01	0	f	2025-10-01 10:35:11.447257+01	0	26.68003434683588	1	7	2	45	8
1094	29.067945836443037	Excellent work!	2025-10-01 10:35:11.458751+01	0	f	2025-10-01 10:35:11.458751+01	0	29.067945836443037	2	7	2	45	8
1095	0	Good performance	2025-10-01 10:35:11.470093+01	1.3	t	2025-10-01 10:35:11.470094+01	51.5822377509339	51.5822377509339	3	7	2	45	8
1096	0	Requires significant improvement	2025-10-01 10:35:11.481338+01	0	f	2025-10-01 10:35:11.481339+01	29.38336820995365	29.38336820995365	4	7	2	45	8
1097	21.57148849125918	Good performance	2025-10-01 10:35:11.49278+01	0	f	2025-10-01 10:35:11.49278+01	0	21.57148849125918	1	10	1	45	13
1098	19.102750837602706	Satisfactory	2025-10-01 10:35:11.50444+01	0	f	2025-10-01 10:35:11.504441+01	0	19.102750837602706	2	10	1	45	13
1099	0	Requires significant improvement	2025-10-01 10:35:11.516054+01	0	f	2025-10-01 10:35:11.516054+01	12.316422268121578	12.316422268121578	3	10	1	45	13
1100	0	Excellent work!	2025-10-01 10:35:11.527142+01	2	t	2025-10-01 10:35:11.527143+01	61.762559939594865	61.762559939594865	4	10	1	45	13
1101	16.371937830965546	Needs improvement	2025-10-01 10:35:11.539046+01	0	f	2025-10-01 10:35:11.539047+01	0	16.371937830965546	1	10	2	45	13
1102	26.361800803934084	Excellent work!	2025-10-01 10:35:11.550773+01	0	f	2025-10-01 10:35:11.550774+01	0	26.361800803934084	2	10	2	45	13
1103	0	Excellent work!	2025-10-01 10:35:11.561783+01	2.3	t	2025-10-01 10:35:11.561784+01	66.92800349146033	66.92800349146033	3	10	2	45	13
1104	0	Good performance	2025-10-01 10:35:11.573453+01	1.7	t	2025-10-01 10:35:11.573453+01	58.043190144856496	58.043190144856496	4	10	2	45	13
1105	27.950719947055113	Excellent work!	2025-10-01 10:35:11.584768+01	0	f	2025-10-01 10:35:11.584768+01	0	27.950719947055113	1	13	1	45	18
1106	27.21975009220298	Excellent work!	2025-10-01 10:35:11.596386+01	0	f	2025-10-01 10:35:11.596387+01	0	27.21975009220298	2	13	1	45	18
1107	0	Good performance	2025-10-01 10:35:11.609641+01	1.3	t	2025-10-01 10:35:11.609642+01	51.810736531619	51.810736531619	3	13	1	45	18
1108	0	Satisfactory	2025-10-01 10:35:11.62151+01	0	f	2025-10-01 10:35:11.62151+01	42.794278181309814	42.794278181309814	4	13	1	45	18
1109	18.77137057935891	Satisfactory	2025-10-01 10:35:11.633613+01	0	f	2025-10-01 10:35:11.633613+01	0	18.77137057935891	1	13	2	45	18
1110	26.74581184544151	Excellent work!	2025-10-01 10:35:11.649734+01	0	f	2025-10-01 10:35:11.649735+01	0	26.74581184544151	2	13	2	45	18
1111	0	Good performance	2025-10-01 10:35:11.661475+01	1.3	t	2025-10-01 10:35:11.661475+01	53.50869421654215	53.50869421654215	3	13	2	45	18
1112	0	Satisfactory	2025-10-01 10:35:11.673616+01	1	f	2025-10-01 10:35:11.673616+01	46.66822894255536	46.66822894255536	4	13	2	45	18
1113	0.7025802532405523	Requires significant improvement	2025-10-01 10:35:11.685121+01	0	f	2025-10-01 10:35:11.685121+01	0	0.7025802532405523	1	16	1	45	23
1114	21.141216222824703	Good performance	2025-10-01 10:35:11.696723+01	0	f	2025-10-01 10:35:11.696723+01	0	21.141216222824703	2	16	1	45	23
1115	0	Requires significant improvement	2025-10-01 10:35:11.708882+01	0	f	2025-10-01 10:35:11.708882+01	21.074437940956024	21.074437940956024	3	16	1	45	23
1116	0	Good performance	2025-10-01 10:35:11.720493+01	1.7	t	2025-10-01 10:35:11.720494+01	56.46289306464567	56.46289306464567	4	16	1	45	23
1117	17.039771159337334	Needs improvement	2025-10-01 10:35:11.731567+01	0	f	2025-10-01 10:35:11.731567+01	0	17.039771159337334	1	16	2	45	23
1118	22.963273494237743	Good performance	2025-10-01 10:35:11.743764+01	0	f	2025-10-01 10:35:11.743764+01	0	22.963273494237743	2	16	2	45	23
1119	0	Excellent work!	2025-10-01 10:35:11.755059+01	2.3	t	2025-10-01 10:35:11.75506+01	65.4779565122736	65.4779565122736	3	16	2	45	23
1120	0	Excellent work!	2025-10-01 10:35:11.766421+01	2.3	t	2025-10-01 10:35:11.766421+01	68.55656762709212	68.55656762709212	4	16	2	45	23
1121	27.582368621942496	Excellent work!	2025-10-01 10:35:11.778679+01	0	f	2025-10-01 10:35:11.77868+01	0	27.582368621942496	1	3	1	46	3
1122	25.53210815927189	Excellent work!	2025-10-01 10:35:11.790739+01	0	f	2025-10-01 10:35:11.79074+01	0	25.53210815927189	2	3	1	46	3
1123	0	Satisfactory	2025-10-01 10:35:11.802837+01	0	f	2025-10-01 10:35:11.802837+01	42.05342527806331	42.05342527806331	3	3	1	46	3
1124	0	Excellent work!	2025-10-01 10:35:11.815303+01	2	t	2025-10-01 10:35:11.815303+01	62.320199955025274	62.320199955025274	4	3	1	46	3
1125	19.362830130759065	Satisfactory	2025-10-01 10:35:11.82683+01	0	f	2025-10-01 10:35:11.826831+01	0	19.362830130759065	1	3	2	46	3
1126	24.870854196865253	Good performance	2025-10-01 10:35:11.839078+01	0	f	2025-10-01 10:35:11.839078+01	0	24.870854196865253	2	3	2	46	3
1127	0	Excellent work!	2025-10-01 10:35:11.850812+01	2	t	2025-10-01 10:35:11.850812+01	61.128363632134054	61.128363632134054	3	3	2	46	3
1128	0	Excellent work!	2025-10-01 10:35:11.862286+01	2	t	2025-10-01 10:35:11.862287+01	62.46551195061207	62.46551195061207	4	3	2	46	3
1129	24.029039797828585	Good performance	2025-10-01 10:35:11.883371+01	0	f	2025-10-01 10:35:11.883372+01	0	24.029039797828585	1	7	1	46	8
1130	20.014914177939538	Satisfactory	2025-10-01 10:35:11.894864+01	0	f	2025-10-01 10:35:11.894864+01	0	20.014914177939538	2	7	1	46	8
1131	0	Excellent work!	2025-10-01 10:35:11.907016+01	2.3	t	2025-10-01 10:35:11.907017+01	65.55824831786215	65.55824831786215	3	7	1	46	8
1132	0	Needs improvement	2025-10-01 10:35:11.918733+01	0	f	2025-10-01 10:35:11.918733+01	37.97241837312757	37.97241837312757	4	7	1	46	8
1133	27.509630482566024	Excellent work!	2025-10-01 10:35:11.930661+01	0	f	2025-10-01 10:35:11.930661+01	0	27.509630482566024	1	7	2	46	8
1134	20.470252106502738	Satisfactory	2025-10-01 10:35:11.944808+01	0	f	2025-10-01 10:35:11.944808+01	0	20.470252106502738	2	7	2	46	8
1135	0	Needs improvement	2025-10-01 10:35:11.956224+01	0	f	2025-10-01 10:35:11.956224+01	41.384684409067006	41.384684409067006	3	7	2	46	8
1136	0	Requires significant improvement	2025-10-01 10:35:11.967982+01	0	f	2025-10-01 10:35:11.967983+01	19.377687901418774	19.377687901418774	4	7	2	46	8
1137	28.994688899796703	Excellent work!	2025-10-01 10:35:11.98047+01	0	f	2025-10-01 10:35:11.980471+01	0	28.994688899796703	1	10	1	46	13
1138	22.427078708634085	Good performance	2025-10-01 10:35:11.991793+01	0	f	2025-10-01 10:35:11.991793+01	0	22.427078708634085	2	10	1	46	13
1139	0	Requires significant improvement	2025-10-01 10:35:12.004137+01	0	f	2025-10-01 10:35:12.004138+01	31.05626501769029	31.05626501769029	3	10	1	46	13
1140	0	Needs improvement	2025-10-01 10:35:12.01599+01	0	f	2025-10-01 10:35:12.01599+01	40.17036319673598	40.17036319673598	4	10	1	46	13
1141	14.292689539845158	Requires significant improvement	2025-10-01 10:35:12.027538+01	0	f	2025-10-01 10:35:12.027538+01	0	14.292689539845158	1	10	2	46	13
1142	23.801902126865876	Good performance	2025-10-01 10:35:12.039753+01	0	f	2025-10-01 10:35:12.039753+01	0	23.801902126865876	2	10	2	46	13
1143	0	Requires significant improvement	2025-10-01 10:35:12.051414+01	0	f	2025-10-01 10:35:12.051414+01	25.700999089009393	25.700999089009393	3	10	2	46	13
1144	0	Requires significant improvement	2025-10-01 10:35:12.062917+01	0	f	2025-10-01 10:35:12.062918+01	24.094422123188853	24.094422123188853	4	10	2	46	13
1145	27.430905247424832	Excellent work!	2025-10-01 10:35:12.074824+01	0	f	2025-10-01 10:35:12.074825+01	0	27.430905247424832	1	13	1	46	18
1146	20.32808727449057	Satisfactory	2025-10-01 10:35:12.086633+01	0	f	2025-10-01 10:35:12.086633+01	0	20.32808727449057	2	13	1	46	18
1147	0	Needs improvement	2025-10-01 10:35:12.098909+01	0	f	2025-10-01 10:35:12.098909+01	40.31995716573905	40.31995716573905	3	13	1	46	18
1148	0	Needs improvement	2025-10-01 10:35:12.11144+01	0	f	2025-10-01 10:35:12.11144+01	37.045787232206024	37.045787232206024	4	13	1	46	18
1149	8.674685141222776	Requires significant improvement	2025-10-01 10:35:12.123356+01	0	f	2025-10-01 10:35:12.123356+01	0	8.674685141222776	1	13	2	46	18
1150	17.344065355401433	Needs improvement	2025-10-01 10:35:12.135824+01	0	f	2025-10-01 10:35:12.135824+01	0	17.344065355401433	2	13	2	46	18
1151	0	Excellent work!	2025-10-01 10:35:12.153014+01	2.3	t	2025-10-01 10:35:12.153014+01	69.1536168465227	69.1536168465227	3	13	2	46	18
1152	0	Excellent work!	2025-10-01 10:35:12.16508+01	2	t	2025-10-01 10:35:12.16508+01	60.57500855401729	60.57500855401729	4	13	2	46	18
1153	22.825808186881318	Good performance	2025-10-01 10:35:12.177496+01	0	f	2025-10-01 10:35:12.177496+01	0	22.825808186881318	1	16	1	46	23
1154	19.72425174731156	Satisfactory	2025-10-01 10:35:12.190171+01	0	f	2025-10-01 10:35:12.190172+01	0	19.72425174731156	2	16	1	46	23
1155	0	Satisfactory	2025-10-01 10:35:12.202083+01	1	f	2025-10-01 10:35:12.202084+01	47.48999429511704	47.48999429511704	3	16	1	46	23
1156	0	Needs improvement	2025-10-01 10:35:12.213994+01	0	f	2025-10-01 10:35:12.213995+01	37.05433827187093	37.05433827187093	4	16	1	46	23
1157	27.051219441552433	Excellent work!	2025-10-01 10:35:12.225978+01	0	f	2025-10-01 10:35:12.225978+01	0	27.051219441552433	1	16	2	46	23
1158	6.68086030315923	Requires significant improvement	2025-10-01 10:35:12.239347+01	0	f	2025-10-01 10:35:12.239347+01	0	6.68086030315923	2	16	2	46	23
1159	0	Needs improvement	2025-10-01 10:35:12.25127+01	0	f	2025-10-01 10:35:12.25127+01	39.04346527505606	39.04346527505606	3	16	2	46	23
1160	0	Requires significant improvement	2025-10-01 10:35:12.262574+01	0	f	2025-10-01 10:35:12.262574+01	23.654658905696007	23.654658905696007	4	16	2	46	23
1161	20.931245399477564	Satisfactory	2025-10-01 10:35:12.274988+01	0	f	2025-10-01 10:35:12.274988+01	0	20.931245399477564	1	3	1	47	3
1162	12.793180839923906	Requires significant improvement	2025-10-01 10:35:12.286966+01	0	f	2025-10-01 10:35:12.286967+01	0	12.793180839923906	2	3	1	47	3
1163	0	Excellent work!	2025-10-01 10:35:12.298753+01	2	t	2025-10-01 10:35:12.298753+01	64.99444978403785	64.99444978403785	3	3	1	47	3
1164	0	Satisfactory	2025-10-01 10:35:12.311413+01	1	f	2025-10-01 10:35:12.311414+01	46.076521224079315	46.076521224079315	4	3	1	47	3
1165	26.07562760183278	Excellent work!	2025-10-01 10:35:12.322968+01	0	f	2025-10-01 10:35:12.322969+01	0	26.07562760183278	1	3	2	47	3
1166	20.541611652030216	Satisfactory	2025-10-01 10:35:12.335612+01	0	f	2025-10-01 10:35:12.335613+01	0	20.541611652030216	2	3	2	47	3
1167	0	Satisfactory	2025-10-01 10:35:12.347304+01	1	f	2025-10-01 10:35:12.347304+01	47.67567572581912	47.67567572581912	3	3	2	47	3
1168	0	Good performance	2025-10-01 10:35:12.359245+01	1	f	2025-10-01 10:35:12.359245+01	49.760428583608345	49.760428583608345	4	3	2	47	3
1169	29.030585813784406	Excellent work!	2025-10-01 10:35:12.371523+01	0	f	2025-10-01 10:35:12.371524+01	0	29.030585813784406	1	7	1	47	8
1170	26.754487093246127	Excellent work!	2025-10-01 10:35:12.383804+01	0	f	2025-10-01 10:35:12.383805+01	0	26.754487093246127	2	7	1	47	8
1171	0	Excellent work!	2025-10-01 10:35:12.396504+01	2.3	t	2025-10-01 10:35:12.396504+01	66.44937117765899	66.44937117765899	3	7	1	47	8
1172	0	Excellent work!	2025-10-01 10:35:12.409557+01	2	t	2025-10-01 10:35:12.409558+01	60.192649496549464	60.192649496549464	4	7	1	47	8
1173	10.239199940653656	Requires significant improvement	2025-10-01 10:35:12.421571+01	0	f	2025-10-01 10:35:12.421571+01	0	10.239199940653656	1	7	2	47	8
1174	17.66254718684086	Needs improvement	2025-10-01 10:35:12.4328+01	0	f	2025-10-01 10:35:12.4328+01	0	17.66254718684086	2	7	2	47	8
1175	0	Excellent work!	2025-10-01 10:35:12.445769+01	2.3	t	2025-10-01 10:35:12.445769+01	68.33703967276858	68.33703967276858	3	7	2	47	8
1176	0	Satisfactory	2025-10-01 10:35:12.457894+01	1	f	2025-10-01 10:35:12.457895+01	47.188315899963854	47.188315899963854	4	7	2	47	8
1177	20.741144879966264	Satisfactory	2025-10-01 10:35:12.469665+01	0	f	2025-10-01 10:35:12.469666+01	0	20.741144879966264	1	10	1	47	13
1178	29.753736978041715	Excellent work!	2025-10-01 10:35:12.481824+01	0	f	2025-10-01 10:35:12.481824+01	0	29.753736978041715	2	10	1	47	13
1179	0	Requires significant improvement	2025-10-01 10:35:12.49387+01	0	f	2025-10-01 10:35:12.49387+01	15.027650098652053	15.027650098652053	3	10	1	47	13
1180	0	Satisfactory	2025-10-01 10:35:12.5066+01	0	f	2025-10-01 10:35:12.5066+01	43.664154313128094	43.664154313128094	4	10	1	47	13
1181	29.210770610498365	Excellent work!	2025-10-01 10:35:12.51861+01	0	f	2025-10-01 10:35:12.51861+01	0	29.210770610498365	1	10	2	47	13
1182	19.52265694847951	Satisfactory	2025-10-01 10:35:12.5314+01	0	f	2025-10-01 10:35:12.5314+01	0	19.52265694847951	2	10	2	47	13
1183	0	Requires significant improvement	2025-10-01 10:35:12.546479+01	0	f	2025-10-01 10:35:12.546479+01	34.287992441016385	34.287992441016385	3	10	2	47	13
1184	0	Satisfactory	2025-10-01 10:35:12.558974+01	0	f	2025-10-01 10:35:12.558974+01	44.90269086515321	44.90269086515321	4	10	2	47	13
1185	9.569023187673098	Requires significant improvement	2025-10-01 10:35:12.571478+01	0	f	2025-10-01 10:35:12.571479+01	0	9.569023187673098	1	13	1	47	18
1186	26.50597152123401	Excellent work!	2025-10-01 10:35:12.583233+01	0	f	2025-10-01 10:35:12.583233+01	0	26.50597152123401	2	13	1	47	18
1187	0	Good performance	2025-10-01 10:35:12.595071+01	1	f	2025-10-01 10:35:12.595072+01	49.42757286121849	49.42757286121849	3	13	1	47	18
1188	0	Good performance	2025-10-01 10:35:12.607472+01	1.7	t	2025-10-01 10:35:12.607472+01	56.60630305152093	56.60630305152093	4	13	1	47	18
1189	23.12132744993844	Good performance	2025-10-01 10:35:12.619377+01	0	f	2025-10-01 10:35:12.619378+01	0	23.12132744993844	1	13	2	47	18
1190	23.128323472507255	Good performance	2025-10-01 10:35:12.63108+01	0	f	2025-10-01 10:35:12.631081+01	0	23.128323472507255	2	13	2	47	18
1191	0	Excellent work!	2025-10-01 10:35:12.643879+01	2	t	2025-10-01 10:35:12.643879+01	63.909274791311915	63.909274791311915	3	13	2	47	18
1192	0	Requires significant improvement	2025-10-01 10:35:12.655781+01	0	f	2025-10-01 10:35:12.655782+01	9.966434936680837	9.966434936680837	4	13	2	47	18
1193	9.110164166181942	Requires significant improvement	2025-10-01 10:35:12.667585+01	0	f	2025-10-01 10:35:12.667586+01	0	9.110164166181942	1	16	1	47	23
1194	21.5277538647539	Good performance	2025-10-01 10:35:12.680062+01	0	f	2025-10-01 10:35:12.680063+01	0	21.5277538647539	2	16	1	47	23
1195	0	Good performance	2025-10-01 10:35:12.691741+01	1.3	t	2025-10-01 10:35:12.691741+01	51.234338446446515	51.234338446446515	3	16	1	47	23
1196	0	Good performance	2025-10-01 10:35:12.703669+01	1.7	t	2025-10-01 10:35:12.703669+01	56.63760532203093	56.63760532203093	4	16	1	47	23
1197	24.56675895154539	Good performance	2025-10-01 10:35:12.715731+01	0	f	2025-10-01 10:35:12.715731+01	0	24.56675895154539	1	16	2	47	23
1198	28.120860279114435	Excellent work!	2025-10-01 10:35:12.727817+01	0	f	2025-10-01 10:35:12.727818+01	0	28.120860279114435	2	16	2	47	23
1199	0	Excellent work!	2025-10-01 10:35:12.740808+01	2.3	t	2025-10-01 10:35:12.740808+01	65.83999152857209	65.83999152857209	3	16	2	47	23
1200	0	Requires significant improvement	2025-10-01 10:35:12.752712+01	0	f	2025-10-01 10:35:12.752713+01	2.4851218820332814	2.4851218820332814	4	16	2	47	23
1201	17.830646296635912	Needs improvement	2025-10-01 10:35:12.764572+01	0	f	2025-10-01 10:35:12.764572+01	0	17.830646296635912	1	3	1	48	3
1202	24.456435307850583	Good performance	2025-10-01 10:35:12.777141+01	0	f	2025-10-01 10:35:12.777141+01	0	24.456435307850583	2	3	1	48	3
1203	0	Good performance	2025-10-01 10:35:12.78989+01	1	f	2025-10-01 10:35:12.789891+01	49.22697861034019	49.22697861034019	3	3	1	48	3
1204	0	Good performance	2025-10-01 10:35:12.803708+01	1.7	t	2025-10-01 10:35:12.803709+01	56.97453506479977	56.97453506479977	4	3	1	48	3
1205	26.226394317230685	Excellent work!	2025-10-01 10:35:12.816558+01	0	f	2025-10-01 10:35:12.816558+01	0	26.226394317230685	1	3	2	48	3
1206	12.761019671819543	Requires significant improvement	2025-10-01 10:35:12.828688+01	0	f	2025-10-01 10:35:12.828688+01	0	12.761019671819543	2	3	2	48	3
1207	0	Satisfactory	2025-10-01 10:35:12.840767+01	0	f	2025-10-01 10:35:12.840767+01	43.36432101105265	43.36432101105265	3	3	2	48	3
1208	0	Excellent work!	2025-10-01 10:35:12.852493+01	2	t	2025-10-01 10:35:12.852494+01	63.508467400050876	63.508467400050876	4	3	2	48	3
1209	18.283250941298885	Satisfactory	2025-10-01 10:35:12.864141+01	0	f	2025-10-01 10:35:12.864142+01	0	18.283250941298885	1	7	1	48	8
1210	10.43295425520844	Requires significant improvement	2025-10-01 10:35:12.876597+01	0	f	2025-10-01 10:35:12.876597+01	0	10.43295425520844	2	7	1	48	8
1211	0	Satisfactory	2025-10-01 10:35:12.888507+01	1	f	2025-10-01 10:35:12.888508+01	47.68410069608602	47.68410069608602	3	7	1	48	8
1212	0	Satisfactory	2025-10-01 10:35:12.900089+01	1	f	2025-10-01 10:35:12.90009+01	45.03904485194434	45.03904485194434	4	7	1	48	8
1213	15.235589772984888	Needs improvement	2025-10-01 10:35:12.91333+01	0	f	2025-10-01 10:35:12.91333+01	0	15.235589772984888	1	7	2	48	8
1214	28.82379149194703	Excellent work!	2025-10-01 10:35:12.925725+01	0	f	2025-10-01 10:35:12.925725+01	0	28.82379149194703	2	7	2	48	8
1215	0	Good performance	2025-10-01 10:35:12.938867+01	1.3	t	2025-10-01 10:35:12.938867+01	50.98467105582627	50.98467105582627	3	7	2	48	8
1216	0	Excellent work!	2025-10-01 10:35:12.953171+01	2	t	2025-10-01 10:35:12.953171+01	61.57966096234375	61.57966096234375	4	7	2	48	8
1217	26.58174882736955	Excellent work!	2025-10-01 10:35:12.965619+01	0	f	2025-10-01 10:35:12.96562+01	0	26.58174882736955	1	10	1	48	13
1218	25.379987675023255	Good performance	2025-10-01 10:35:12.97914+01	0	f	2025-10-01 10:35:12.97914+01	0	25.379987675023255	2	10	1	48	13
1219	0	Good performance	2025-10-01 10:35:12.991976+01	1	f	2025-10-01 10:35:12.991977+01	49.51400787615759	49.51400787615759	3	10	1	48	13
1220	0	Excellent work!	2025-10-01 10:35:13.005403+01	2.3	t	2025-10-01 10:35:13.005403+01	67.73142294534095	67.73142294534095	4	10	1	48	13
1221	17.136033130614823	Needs improvement	2025-10-01 10:35:13.018215+01	0	f	2025-10-01 10:35:13.018215+01	0	17.136033130614823	1	10	2	48	13
1222	23.24849818492731	Good performance	2025-10-01 10:35:13.030763+01	0	f	2025-10-01 10:35:13.030764+01	0	23.24849818492731	2	10	2	48	13
1223	0	Excellent work!	2025-10-01 10:35:13.044253+01	2.3	t	2025-10-01 10:35:13.044253+01	65.83895899091624	65.83895899091624	3	10	2	48	13
1224	0	Good performance	2025-10-01 10:35:13.056734+01	1	f	2025-10-01 10:35:13.056734+01	49.65703811564335	49.65703811564335	4	10	2	48	13
1225	7.344290718239391	Requires significant improvement	2025-10-01 10:35:13.069775+01	0	f	2025-10-01 10:35:13.069775+01	0	7.344290718239391	1	13	1	48	18
1226	21.093173480282736	Good performance	2025-10-01 10:35:13.082754+01	0	f	2025-10-01 10:35:13.082755+01	0	21.093173480282736	2	13	1	48	18
1227	0	Excellent work!	2025-10-01 10:35:13.094788+01	2	t	2025-10-01 10:35:13.094788+01	62.10484539184034	62.10484539184034	3	13	1	48	18
1228	0	Excellent work!	2025-10-01 10:35:13.108074+01	2	t	2025-10-01 10:35:13.108075+01	61.26394590016794	61.26394590016794	4	13	1	48	18
1229	27.25476471147941	Excellent work!	2025-10-01 10:35:13.120453+01	0	f	2025-10-01 10:35:13.120454+01	0	27.25476471147941	1	13	2	48	18
1230	26.215942907249687	Excellent work!	2025-10-01 10:35:13.133174+01	0	f	2025-10-01 10:35:13.133174+01	0	26.215942907249687	2	13	2	48	18
1231	0	Good performance	2025-10-01 10:35:13.14666+01	1.3	t	2025-10-01 10:35:13.14666+01	53.82721093264233	53.82721093264233	3	13	2	48	18
1232	0	Good performance	2025-10-01 10:35:13.159269+01	1.3	t	2025-10-01 10:35:13.159269+01	52.750473969402094	52.750473969402094	4	13	2	48	18
1233	21.558403193693906	Good performance	2025-10-01 10:35:13.173139+01	0	f	2025-10-01 10:35:13.17314+01	0	21.558403193693906	1	16	1	48	23
1234	21.807313029862076	Good performance	2025-10-01 10:35:13.186409+01	0	f	2025-10-01 10:35:13.186409+01	0	21.807313029862076	2	16	1	48	23
1235	0	Requires significant improvement	2025-10-01 10:35:13.199386+01	0	f	2025-10-01 10:35:13.199387+01	13.421874026259527	13.421874026259527	3	16	1	48	23
1236	0	Requires significant improvement	2025-10-01 10:35:13.221397+01	0	f	2025-10-01 10:35:13.221399+01	3.322619430189695	3.322619430189695	4	16	1	48	23
1237	28.34129529929326	Excellent work!	2025-10-01 10:35:13.236405+01	0	f	2025-10-01 10:35:13.236406+01	0	28.34129529929326	1	16	2	48	23
1238	26.894389481848417	Excellent work!	2025-10-01 10:35:13.257228+01	0	f	2025-10-01 10:35:13.257228+01	0	26.894389481848417	2	16	2	48	23
1239	0	Requires significant improvement	2025-10-01 10:35:13.27741+01	0	f	2025-10-01 10:35:13.277411+01	2.401976766680918	2.401976766680918	3	16	2	48	23
1240	0	Good performance	2025-10-01 10:35:13.291461+01	1.3	t	2025-10-01 10:35:13.291461+01	52.3189816232764	52.3189816232764	4	16	2	48	23
1241	16.565485420921462	Needs improvement	2025-10-01 10:35:13.311417+01	0	f	2025-10-01 10:35:13.31142+01	0	16.565485420921462	1	3	1	49	3
1242	24.97756102977275	Good performance	2025-10-01 10:35:13.325604+01	0	f	2025-10-01 10:35:13.325604+01	0	24.97756102977275	2	3	1	49	3
1243	0	Requires significant improvement	2025-10-01 10:35:13.338772+01	0	f	2025-10-01 10:35:13.338773+01	1.426380885399684	1.426380885399684	3	3	1	49	3
1244	0	Needs improvement	2025-10-01 10:35:13.351477+01	0	f	2025-10-01 10:35:13.351477+01	38.574037408335705	38.574037408335705	4	3	1	49	3
1245	10.79903885318049	Requires significant improvement	2025-10-01 10:35:13.362754+01	0	f	2025-10-01 10:35:13.362754+01	0	10.79903885318049	1	3	2	49	3
1246	16.35250350020123	Needs improvement	2025-10-01 10:35:13.37577+01	0	f	2025-10-01 10:35:13.37577+01	0	16.35250350020123	2	3	2	49	3
1247	0	Requires significant improvement	2025-10-01 10:35:13.388279+01	0	f	2025-10-01 10:35:13.38828+01	18.658833567093925	18.658833567093925	3	3	2	49	3
1248	0	Requires significant improvement	2025-10-01 10:35:13.400542+01	0	f	2025-10-01 10:35:13.400543+01	8.856644900658054	8.856644900658054	4	3	2	49	3
1249	23.33741623606972	Good performance	2025-10-01 10:35:13.413268+01	0	f	2025-10-01 10:35:13.413269+01	0	23.33741623606972	1	7	1	49	8
1250	24.690652414980853	Good performance	2025-10-01 10:35:13.426315+01	0	f	2025-10-01 10:35:13.426315+01	0	24.690652414980853	2	7	1	49	8
1251	0	Excellent work!	2025-10-01 10:35:13.43934+01	2.3	t	2025-10-01 10:35:13.43934+01	65.78803432401658	65.78803432401658	3	7	1	49	8
1252	0	Needs improvement	2025-10-01 10:35:13.453115+01	0	f	2025-10-01 10:35:13.453115+01	37.516171076079914	37.516171076079914	4	7	1	49	8
1253	21.809128834770398	Good performance	2025-10-01 10:35:13.465827+01	0	f	2025-10-01 10:35:13.465827+01	0	21.809128834770398	1	7	2	49	8
1254	29.818139003390186	Excellent work!	2025-10-01 10:35:13.478614+01	0	f	2025-10-01 10:35:13.478614+01	0	29.818139003390186	2	7	2	49	8
1255	0	Excellent work!	2025-10-01 10:35:13.49113+01	2.3	t	2025-10-01 10:35:13.49113+01	67.82747378664232	67.82747378664232	3	7	2	49	8
1256	0	Excellent work!	2025-10-01 10:35:13.503748+01	2.3	t	2025-10-01 10:35:13.503749+01	67.4582823316003	67.4582823316003	4	7	2	49	8
1257	28.741576088802848	Excellent work!	2025-10-01 10:35:13.516449+01	0	f	2025-10-01 10:35:13.51645+01	0	28.741576088802848	1	10	1	49	13
1258	19.931357350482045	Satisfactory	2025-10-01 10:35:13.528828+01	0	f	2025-10-01 10:35:13.528828+01	0	19.931357350482045	2	10	1	49	13
1259	0	Needs improvement	2025-10-01 10:35:13.541768+01	0	f	2025-10-01 10:35:13.541768+01	40.15616818901355	40.15616818901355	3	10	1	49	13
1260	0	Excellent work!	2025-10-01 10:35:13.554485+01	2.3	t	2025-10-01 10:35:13.554486+01	65.12762399797523	65.12762399797523	4	10	1	49	13
1261	23.826630066922213	Good performance	2025-10-01 10:35:13.566458+01	0	f	2025-10-01 10:35:13.566459+01	0	23.826630066922213	1	10	2	49	13
1262	22.264534452059973	Good performance	2025-10-01 10:35:13.579028+01	0	f	2025-10-01 10:35:13.579028+01	0	22.264534452059973	2	10	2	49	13
1263	0	Needs improvement	2025-10-01 10:35:13.591873+01	0	f	2025-10-01 10:35:13.591873+01	38.396116298448696	38.396116298448696	3	10	2	49	13
1264	0	Needs improvement	2025-10-01 10:35:13.604467+01	0	f	2025-10-01 10:35:13.604467+01	36.79190780714485	36.79190780714485	4	10	2	49	13
1265	26.78197180351593	Excellent work!	2025-10-01 10:35:13.617288+01	0	f	2025-10-01 10:35:13.617288+01	0	26.78197180351593	1	13	1	49	18
1266	25.445673724522887	Good performance	2025-10-01 10:35:13.629554+01	0	f	2025-10-01 10:35:13.629555+01	0	25.445673724522887	2	13	1	49	18
1267	0	Good performance	2025-10-01 10:35:13.643082+01	1.3	t	2025-10-01 10:35:13.643083+01	50.431511116983664	50.431511116983664	3	13	1	49	18
1268	0	Needs improvement	2025-10-01 10:35:13.655833+01	0	f	2025-10-01 10:35:13.655833+01	41.36289840982085	41.36289840982085	4	13	1	49	18
1269	27.25469587728727	Excellent work!	2025-10-01 10:35:13.668384+01	0	f	2025-10-01 10:35:13.668384+01	0	27.25469587728727	1	13	2	49	18
1270	17.10616213883303	Needs improvement	2025-10-01 10:35:13.681145+01	0	f	2025-10-01 10:35:13.681146+01	0	17.10616213883303	2	13	2	49	18
1271	0	Good performance	2025-10-01 10:35:13.6939+01	1.3	t	2025-10-01 10:35:13.6939+01	51.61061521033879	51.61061521033879	3	13	2	49	18
1272	0	Good performance	2025-10-01 10:35:13.70659+01	1.3	t	2025-10-01 10:35:13.706591+01	53.904738799002814	53.904738799002814	4	13	2	49	18
1273	22.657608160168557	Good performance	2025-10-01 10:35:13.718875+01	0	f	2025-10-01 10:35:13.718876+01	0	22.657608160168557	1	16	1	49	23
1274	28.409558496476343	Excellent work!	2025-10-01 10:35:13.731508+01	0	f	2025-10-01 10:35:13.731508+01	0	28.409558496476343	2	16	1	49	23
1275	0	Good performance	2025-10-01 10:35:13.744753+01	1.3	t	2025-10-01 10:35:13.744753+01	50.79100656922712	50.79100656922712	3	16	1	49	23
1276	0	Satisfactory	2025-10-01 10:35:13.75759+01	0	f	2025-10-01 10:35:13.75759+01	44.07578697637572	44.07578697637572	4	16	1	49	23
1277	25.723903677433356	Excellent work!	2025-10-01 10:35:13.771177+01	0	f	2025-10-01 10:35:13.771177+01	0	25.723903677433356	1	16	2	49	23
1278	0.9760153648135356	Requires significant improvement	2025-10-01 10:35:13.783719+01	0	f	2025-10-01 10:35:13.783719+01	0	0.9760153648135356	2	16	2	49	23
1279	0	Excellent work!	2025-10-01 10:35:13.79688+01	2.3	t	2025-10-01 10:35:13.79688+01	68.20784825727029	68.20784825727029	3	16	2	49	23
1280	0	Requires significant improvement	2025-10-01 10:35:13.810964+01	0	f	2025-10-01 10:35:13.810964+01	15.739026089923724	15.739026089923724	4	16	2	49	23
1281	21.67384985473452	Good performance	2025-10-01 10:35:13.824643+01	0	f	2025-10-01 10:35:13.824643+01	0	21.67384985473452	1	3	1	50	3
1282	27.931943482293278	Excellent work!	2025-10-01 10:35:13.838489+01	0	f	2025-10-01 10:35:13.838489+01	0	27.931943482293278	2	3	1	50	3
1283	0	Good performance	2025-10-01 10:35:13.852087+01	1.7	t	2025-10-01 10:35:13.852088+01	56.50791314746063	56.50791314746063	3	3	1	50	3
1284	0	Excellent work!	2025-10-01 10:35:13.864886+01	2.3	t	2025-10-01 10:35:13.864887+01	68.22479541329714	68.22479541329714	4	3	1	50	3
1285	19.147213189961576	Satisfactory	2025-10-01 10:35:13.878566+01	0	f	2025-10-01 10:35:13.878566+01	0	19.147213189961576	1	3	2	50	3
1286	23.810810184855647	Good performance	2025-10-01 10:35:13.891724+01	0	f	2025-10-01 10:35:13.891724+01	0	23.810810184855647	2	3	2	50	3
1287	0	Excellent work!	2025-10-01 10:35:13.905072+01	2	t	2025-10-01 10:35:13.905072+01	62.59232983000602	62.59232983000602	3	3	2	50	3
1288	0	Requires significant improvement	2025-10-01 10:35:13.917815+01	0	f	2025-10-01 10:35:13.917815+01	32.61828901182764	32.61828901182764	4	3	2	50	3
1289	2.4866659779369904	Requires significant improvement	2025-10-01 10:35:13.930764+01	0	f	2025-10-01 10:35:13.930764+01	0	2.4866659779369904	1	7	1	50	8
1290	23.69172056492929	Good performance	2025-10-01 10:35:13.944254+01	0	f	2025-10-01 10:35:13.944255+01	0	23.69172056492929	2	7	1	50	8
1291	0	Excellent work!	2025-10-01 10:35:13.959466+01	2.3	t	2025-10-01 10:35:13.959467+01	67.41129168136331	67.41129168136331	3	7	1	50	8
1292	0	Excellent work!	2025-10-01 10:35:13.974242+01	2.3	t	2025-10-01 10:35:13.974242+01	68.93822203288511	68.93822203288511	4	7	1	50	8
1293	29.206174387864078	Excellent work!	2025-10-01 10:35:13.98836+01	0	f	2025-10-01 10:35:13.988361+01	0	29.206174387864078	1	7	2	50	8
1294	21.689571423557165	Good performance	2025-10-01 10:35:14.002343+01	0	f	2025-10-01 10:35:14.002344+01	0	21.689571423557165	2	7	2	50	8
1295	0	Excellent work!	2025-10-01 10:35:14.016782+01	2.3	t	2025-10-01 10:35:14.016783+01	66.42029797619956	66.42029797619956	3	7	2	50	8
1296	0	Excellent work!	2025-10-01 10:35:14.0377+01	2	t	2025-10-01 10:35:14.0377+01	62.828500275161524	62.828500275161524	4	7	2	50	8
1297	29.711369225746026	Excellent work!	2025-10-01 10:35:14.051896+01	0	f	2025-10-01 10:35:14.051896+01	0	29.711369225746026	1	10	1	50	13
1298	19.22666770947637	Satisfactory	2025-10-01 10:35:14.066387+01	0	f	2025-10-01 10:35:14.066387+01	0	19.22666770947637	2	10	1	50	13
1299	0	Excellent work!	2025-10-01 10:35:14.086283+01	2	t	2025-10-01 10:35:14.086283+01	61.45047574236195	61.45047574236195	3	10	1	50	13
1300	0	Excellent work!	2025-10-01 10:35:14.099527+01	2	t	2025-10-01 10:35:14.099527+01	64.39772998669471	64.39772998669471	4	10	1	50	13
1301	22.568803547997973	Good performance	2025-10-01 10:35:14.114043+01	0	f	2025-10-01 10:35:14.114044+01	0	22.568803547997973	1	10	2	50	13
1302	22.03769631317768	Good performance	2025-10-01 10:35:14.127486+01	0	f	2025-10-01 10:35:14.127486+01	0	22.03769631317768	2	10	2	50	13
1303	0	Good performance	2025-10-01 10:35:14.141582+01	1.3	t	2025-10-01 10:35:14.141583+01	54.144154858936986	54.144154858936986	3	10	2	50	13
1304	0	Excellent work!	2025-10-01 10:35:14.155352+01	2.3	t	2025-10-01 10:35:14.155352+01	65.26973329801466	65.26973329801466	4	10	2	50	13
1305	28.382909332135316	Excellent work!	2025-10-01 10:35:14.169117+01	0	f	2025-10-01 10:35:14.169118+01	0	28.382909332135316	1	13	1	50	18
1306	20.413579390404664	Satisfactory	2025-10-01 10:35:14.18262+01	0	f	2025-10-01 10:35:14.182621+01	0	20.413579390404664	2	13	1	50	18
1307	0	Needs improvement	2025-10-01 10:35:14.196735+01	0	f	2025-10-01 10:35:14.196735+01	37.90650691959158	37.90650691959158	3	13	1	50	18
1308	0	Excellent work!	2025-10-01 10:35:14.210849+01	2	t	2025-10-01 10:35:14.210849+01	63.656489999444844	63.656489999444844	4	13	1	50	18
1309	27.3709522290538	Excellent work!	2025-10-01 10:35:14.224085+01	0	f	2025-10-01 10:35:14.224085+01	0	27.3709522290538	1	13	2	50	18
1310	23.24711506109206	Good performance	2025-10-01 10:35:14.237777+01	0	f	2025-10-01 10:35:14.237778+01	0	23.24711506109206	2	13	2	50	18
1311	0	Needs improvement	2025-10-01 10:35:14.251626+01	0	f	2025-10-01 10:35:14.251626+01	35.321081616513865	35.321081616513865	3	13	2	50	18
1312	0	Satisfactory	2025-10-01 10:35:14.263826+01	0	f	2025-10-01 10:35:14.263826+01	43.436758867621165	43.436758867621165	4	13	2	50	18
1313	29.967486004469187	Excellent work!	2025-10-01 10:35:14.277529+01	0	f	2025-10-01 10:35:14.277529+01	0	29.967486004469187	1	16	1	50	23
1314	29.771473928929165	Excellent work!	2025-10-01 10:35:14.290301+01	0	f	2025-10-01 10:35:14.290302+01	0	29.771473928929165	2	16	1	50	23
1315	0	Excellent work!	2025-10-01 10:35:14.303584+01	2	t	2025-10-01 10:35:14.303584+01	62.404066121157264	62.404066121157264	3	16	1	50	23
1316	0	Excellent work!	2025-10-01 10:35:14.316937+01	2.3	t	2025-10-01 10:35:14.316938+01	66.38504275302196	66.38504275302196	4	16	1	50	23
1317	22.303043680775833	Good performance	2025-10-01 10:35:14.330121+01	0	f	2025-10-01 10:35:14.330122+01	0	22.303043680775833	1	16	2	50	23
1318	4.534632369199965	Requires significant improvement	2025-10-01 10:35:14.343779+01	0	f	2025-10-01 10:35:14.34378+01	0	4.534632369199965	2	16	2	50	23
1319	0	Needs improvement	2025-10-01 10:35:14.357047+01	0	f	2025-10-01 10:35:14.357047+01	39.03818468038058	39.03818468038058	3	16	2	50	23
1320	0	Requires significant improvement	2025-10-01 10:35:14.370817+01	0	f	2025-10-01 10:35:14.370818+01	30.159111552882635	30.159111552882635	4	16	2	50	23
1321	25.2927331541253	Good performance	2025-10-01 10:35:14.384568+01	0	f	2025-10-01 10:35:14.384569+01	0	25.2927331541253	1	3	1	51	3
1322	17.89419612023321	Needs improvement	2025-10-01 10:35:14.397905+01	0	f	2025-10-01 10:35:14.397905+01	0	17.89419612023321	2	3	1	51	3
1323	0	Good performance	2025-10-01 10:35:14.412069+01	1.7	t	2025-10-01 10:35:14.41207+01	56.76485182102012	56.76485182102012	3	3	1	51	3
1324	0	Good performance	2025-10-01 10:35:14.425221+01	1.7	t	2025-10-01 10:35:14.425222+01	58.00186990723123	58.00186990723123	4	3	1	51	3
1325	4.17316566777761	Requires significant improvement	2025-10-01 10:35:14.439206+01	0	f	2025-10-01 10:35:14.439207+01	0	4.17316566777761	1	3	2	51	3
1326	27.731811665067823	Excellent work!	2025-10-01 10:35:14.452794+01	0	f	2025-10-01 10:35:14.452794+01	0	27.731811665067823	2	3	2	51	3
1327	0	Requires significant improvement	2025-10-01 10:35:14.469085+01	0	f	2025-10-01 10:35:14.469086+01	3.9506592564772247	3.9506592564772247	3	3	2	51	3
1328	0	Good performance	2025-10-01 10:35:14.482571+01	1.3	t	2025-10-01 10:35:14.482572+01	53.372505593399715	53.372505593399715	4	3	2	51	3
1329	10.834670415287302	Requires significant improvement	2025-10-01 10:35:14.495891+01	0	f	2025-10-01 10:35:14.495891+01	0	10.834670415287302	1	7	1	51	8
1330	21.19800936521707	Good performance	2025-10-01 10:35:14.509543+01	0	f	2025-10-01 10:35:14.509544+01	0	21.19800936521707	2	7	1	51	8
1331	0	Excellent work!	2025-10-01 10:35:14.522661+01	2	t	2025-10-01 10:35:14.522661+01	63.798298810300004	63.798298810300004	3	7	1	51	8
1332	0	Needs improvement	2025-10-01 10:35:14.536711+01	0	f	2025-10-01 10:35:14.536711+01	36.88086182531451	36.88086182531451	4	7	1	51	8
1333	29.83504363569945	Excellent work!	2025-10-01 10:35:14.549848+01	0	f	2025-10-01 10:35:14.549848+01	0	29.83504363569945	1	7	2	51	8
1334	13.441625709179652	Requires significant improvement	2025-10-01 10:35:14.563182+01	0	f	2025-10-01 10:35:14.563182+01	0	13.441625709179652	2	7	2	51	8
1335	0	Excellent work!	2025-10-01 10:35:14.57774+01	2.3	t	2025-10-01 10:35:14.577741+01	69.75660106075634	69.75660106075634	3	7	2	51	8
1336	0	Excellent work!	2025-10-01 10:35:14.599499+01	2.3	t	2025-10-01 10:35:14.5995+01	68.09205044519291	68.09205044519291	4	7	2	51	8
1337	18.418901472430452	Satisfactory	2025-10-01 10:35:14.613871+01	0	f	2025-10-01 10:35:14.613872+01	0	18.418901472430452	1	10	1	51	13
1338	22.506993882335237	Good performance	2025-10-01 10:35:14.627166+01	0	f	2025-10-01 10:35:14.627167+01	0	22.506993882335237	2	10	1	51	13
1339	0	Excellent work!	2025-10-01 10:35:14.64194+01	2	t	2025-10-01 10:35:14.64194+01	60.25784082139376	60.25784082139376	3	10	1	51	13
1340	0	Requires significant improvement	2025-10-01 10:35:14.655527+01	0	f	2025-10-01 10:35:14.655527+01	12.15974218411152	12.15974218411152	4	10	1	51	13
1341	26.082991888810767	Excellent work!	2025-10-01 10:35:14.669168+01	0	f	2025-10-01 10:35:14.669168+01	0	26.082991888810767	1	10	2	51	13
1342	20.87730159202975	Satisfactory	2025-10-01 10:35:14.682897+01	0	f	2025-10-01 10:35:14.682898+01	0	20.87730159202975	2	10	2	51	13
1343	0	Needs improvement	2025-10-01 10:35:14.696058+01	0	f	2025-10-01 10:35:14.696059+01	39.500333887728296	39.500333887728296	3	10	2	51	13
1344	0	Satisfactory	2025-10-01 10:35:14.710374+01	0	f	2025-10-01 10:35:14.710374+01	43.034761374597295	43.034761374597295	4	10	2	51	13
1345	2.625584519210056	Requires significant improvement	2025-10-01 10:35:14.723756+01	0	f	2025-10-01 10:35:14.723757+01	0	2.625584519210056	1	13	1	51	18
1346	12.922668357171625	Requires significant improvement	2025-10-01 10:35:14.737358+01	0	f	2025-10-01 10:35:14.737359+01	0	12.922668357171625	2	13	1	51	18
1347	0	Excellent work!	2025-10-01 10:35:14.750861+01	2.3	t	2025-10-01 10:35:14.750862+01	69.36512012855792	69.36512012855792	3	13	1	51	18
1348	0	Requires significant improvement	2025-10-01 10:35:14.76382+01	0	f	2025-10-01 10:35:14.763821+01	11.420204793477652	11.420204793477652	4	13	1	51	18
1349	27.306408725136965	Excellent work!	2025-10-01 10:35:14.778064+01	0	f	2025-10-01 10:35:14.778065+01	0	27.306408725136965	1	13	2	51	18
1350	3.722020151189562	Requires significant improvement	2025-10-01 10:35:14.791574+01	0	f	2025-10-01 10:35:14.791574+01	0	3.722020151189562	2	13	2	51	18
1351	0	Requires significant improvement	2025-10-01 10:35:14.805722+01	0	f	2025-10-01 10:35:14.805723+01	17.655252839697017	17.655252839697017	3	13	2	51	18
1352	0	Requires significant improvement	2025-10-01 10:35:14.820241+01	0	f	2025-10-01 10:35:14.820242+01	33.15612995884481	33.15612995884481	4	13	2	51	18
1353	16.000282179789263	Needs improvement	2025-10-01 10:35:14.833624+01	0	f	2025-10-01 10:35:14.833625+01	0	16.000282179789263	1	16	1	51	23
1354	13.47310460329461	Requires significant improvement	2025-10-01 10:35:14.84725+01	0	f	2025-10-01 10:35:14.847251+01	0	13.47310460329461	2	16	1	51	23
1355	0	Satisfactory	2025-10-01 10:35:14.860874+01	1	f	2025-10-01 10:35:14.860874+01	45.02879537131423	45.02879537131423	3	16	1	51	23
1356	0	Good performance	2025-10-01 10:35:14.875481+01	1.7	t	2025-10-01 10:35:14.875482+01	58.154935640774816	58.154935640774816	4	16	1	51	23
1357	25.591821336939674	Excellent work!	2025-10-01 10:35:14.889064+01	0	f	2025-10-01 10:35:14.889065+01	0	25.591821336939674	1	16	2	51	23
1358	27.634378000144117	Excellent work!	2025-10-01 10:35:14.902804+01	0	f	2025-10-01 10:35:14.902804+01	0	27.634378000144117	2	16	2	51	23
1359	0	Requires significant improvement	2025-10-01 10:35:14.920122+01	0	f	2025-10-01 10:35:14.920123+01	23.370270538577632	23.370270538577632	3	16	2	51	23
1360	0	Requires significant improvement	2025-10-01 10:35:14.960367+01	0	f	2025-10-01 10:35:14.960368+01	2.9283404832246465	2.9283404832246465	4	16	2	51	23
1361	1.8781475143759374	Requires significant improvement	2025-10-01 10:35:14.979627+01	0	f	2025-10-01 10:35:14.979627+01	0	1.8781475143759374	1	3	1	52	3
1362	15.107353259797435	Needs improvement	2025-10-01 10:35:14.992768+01	0	f	2025-10-01 10:35:14.992768+01	0	15.107353259797435	2	3	1	52	3
1363	0	Requires significant improvement	2025-10-01 10:35:15.006752+01	0	f	2025-10-01 10:35:15.006752+01	31.56283343189863	31.56283343189863	3	3	1	52	3
1364	0	Requires significant improvement	2025-10-01 10:35:15.020196+01	0	f	2025-10-01 10:35:15.020197+01	7.632893492306356	7.632893492306356	4	3	1	52	3
1365	3.2441076065971735	Requires significant improvement	2025-10-01 10:35:15.033708+01	0	f	2025-10-01 10:35:15.033709+01	0	3.2441076065971735	1	3	2	52	3
1366	26.621079848323635	Excellent work!	2025-10-01 10:35:15.047757+01	0	f	2025-10-01 10:35:15.047757+01	0	26.621079848323635	2	3	2	52	3
1367	0	Needs improvement	2025-10-01 10:35:15.061187+01	0	f	2025-10-01 10:35:15.061188+01	35.77388362338229	35.77388362338229	3	3	2	52	3
1368	0	Excellent work!	2025-10-01 10:35:15.075193+01	2.3	t	2025-10-01 10:35:15.075194+01	67.94904055338125	67.94904055338125	4	3	2	52	3
1369	27.65774626379321	Excellent work!	2025-10-01 10:35:15.090332+01	0	f	2025-10-01 10:35:15.090332+01	0	27.65774626379321	1	7	1	52	8
1370	25.10814955131404	Good performance	2025-10-01 10:35:15.104852+01	0	f	2025-10-01 10:35:15.104853+01	0	25.10814955131404	2	7	1	52	8
1371	0	Excellent work!	2025-10-01 10:35:15.118158+01	2.3	t	2025-10-01 10:35:15.118158+01	67.320564465061	67.320564465061	3	7	1	52	8
1372	0	Requires significant improvement	2025-10-01 10:35:15.131327+01	0	f	2025-10-01 10:35:15.131328+01	7.443315976839704	7.443315976839704	4	7	1	52	8
1373	15.496097408550082	Needs improvement	2025-10-01 10:35:15.145532+01	0	f	2025-10-01 10:35:15.145533+01	0	15.496097408550082	1	7	2	52	8
1374	5.186976230589845	Requires significant improvement	2025-10-01 10:35:15.158611+01	0	f	2025-10-01 10:35:15.158611+01	0	5.186976230589845	2	7	2	52	8
1375	0	Good performance	2025-10-01 10:35:15.173619+01	1.7	t	2025-10-01 10:35:15.173619+01	58.00091833932227	58.00091833932227	3	7	2	52	8
1376	0	Requires significant improvement	2025-10-01 10:35:15.187268+01	0	f	2025-10-01 10:35:15.187269+01	27.794486923632583	27.794486923632583	4	7	2	52	8
1377	23.520076433374065	Good performance	2025-10-01 10:35:15.200931+01	0	f	2025-10-01 10:35:15.200932+01	0	23.520076433374065	1	10	1	52	13
1378	25.91769753331151	Excellent work!	2025-10-01 10:35:15.214793+01	0	f	2025-10-01 10:35:15.214794+01	0	25.91769753331151	2	10	1	52	13
1379	0	Satisfactory	2025-10-01 10:35:15.228597+01	0	f	2025-10-01 10:35:15.228597+01	43.535560784372635	43.535560784372635	3	10	1	52	13
1380	0	Excellent work!	2025-10-01 10:35:15.243304+01	2.3	t	2025-10-01 10:35:15.243304+01	66.657231198148	66.657231198148	4	10	1	52	13
1381	23.962727981023683	Good performance	2025-10-01 10:35:15.257176+01	0	f	2025-10-01 10:35:15.257177+01	0	23.962727981023683	1	10	2	52	13
1382	24.205653627837783	Good performance	2025-10-01 10:35:15.27207+01	0	f	2025-10-01 10:35:15.272071+01	0	24.205653627837783	2	10	2	52	13
1383	0	Excellent work!	2025-10-01 10:35:15.285846+01	2.3	t	2025-10-01 10:35:15.285846+01	68.8082738876581	68.8082738876581	3	10	2	52	13
1384	0	Excellent work!	2025-10-01 10:35:15.299424+01	2.3	t	2025-10-01 10:35:15.299424+01	69.46352032811261	69.46352032811261	4	10	2	52	13
1385	7.003614385740708	Requires significant improvement	2025-10-01 10:35:15.31314+01	0	f	2025-10-01 10:35:15.313141+01	0	7.003614385740708	1	13	1	52	18
1386	3.086529528253632	Requires significant improvement	2025-10-01 10:35:15.326221+01	0	f	2025-10-01 10:35:15.326222+01	0	3.086529528253632	2	13	1	52	18
1387	0	Excellent work!	2025-10-01 10:35:15.339768+01	2.3	t	2025-10-01 10:35:15.339768+01	66.00188202029655	66.00188202029655	3	13	1	52	18
1388	0	Requires significant improvement	2025-10-01 10:35:15.354369+01	0	f	2025-10-01 10:35:15.35437+01	17.236916474785936	17.236916474785936	4	13	1	52	18
1389	24.23528658078326	Good performance	2025-10-01 10:35:15.36891+01	0	f	2025-10-01 10:35:15.36891+01	0	24.23528658078326	1	13	2	52	18
1390	20.987807000277687	Satisfactory	2025-10-01 10:35:15.383995+01	0	f	2025-10-01 10:35:15.383995+01	0	20.987807000277687	2	13	2	52	18
1391	0	Needs improvement	2025-10-01 10:35:15.397296+01	0	f	2025-10-01 10:35:15.397297+01	40.1568431804549	40.1568431804549	3	13	2	52	18
1392	0	Requires significant improvement	2025-10-01 10:35:15.411551+01	0	f	2025-10-01 10:35:15.411551+01	21.769311974213988	21.769311974213988	4	13	2	52	18
1393	13.579929930021684	Requires significant improvement	2025-10-01 10:35:15.425196+01	0	f	2025-10-01 10:35:15.425197+01	0	13.579929930021684	1	16	1	52	23
1394	0.40193598288871035	Requires significant improvement	2025-10-01 10:35:15.439967+01	0	f	2025-10-01 10:35:15.439967+01	0	0.40193598288871035	2	16	1	52	23
1395	0	Requires significant improvement	2025-10-01 10:35:15.45379+01	0	f	2025-10-01 10:35:15.453791+01	32.28512115088993	32.28512115088993	3	16	1	52	23
1396	0	Good performance	2025-10-01 10:35:15.46767+01	1	f	2025-10-01 10:35:15.467671+01	49.421175181802894	49.421175181802894	4	16	1	52	23
1397	19.21766931186165	Satisfactory	2025-10-01 10:35:15.483321+01	0	f	2025-10-01 10:35:15.483322+01	0	19.21766931186165	1	16	2	52	23
1398	11.510906985048358	Requires significant improvement	2025-10-01 10:35:15.497208+01	0	f	2025-10-01 10:35:15.497209+01	0	11.510906985048358	2	16	2	52	23
1399	0	Requires significant improvement	2025-10-01 10:35:15.512053+01	0	f	2025-10-01 10:35:15.512054+01	27.080627517721137	27.080627517721137	3	16	2	52	23
1400	0	Satisfactory	2025-10-01 10:35:15.525645+01	0	f	2025-10-01 10:35:15.525645+01	43.082078565848924	43.082078565848924	4	16	2	52	23
1401	15.751160247229192	Needs improvement	2025-10-01 10:35:15.54021+01	0	f	2025-10-01 10:35:15.54021+01	0	15.751160247229192	1	3	1	53	3
1402	15.21731674202938	Needs improvement	2025-10-01 10:35:15.554251+01	0	f	2025-10-01 10:35:15.554251+01	0	15.21731674202938	2	3	1	53	3
1403	0	Good performance	2025-10-01 10:35:15.568324+01	1.3	t	2025-10-01 10:35:15.568325+01	50.94405035545604	50.94405035545604	3	3	1	53	3
1404	0	Satisfactory	2025-10-01 10:35:15.582299+01	0	f	2025-10-01 10:35:15.582299+01	42.841497184119575	42.841497184119575	4	3	1	53	3
1405	28.808712242900278	Excellent work!	2025-10-01 10:35:15.595649+01	0	f	2025-10-01 10:35:15.59565+01	0	28.808712242900278	1	3	2	53	3
1406	24.146714302119765	Good performance	2025-10-01 10:35:15.609534+01	0	f	2025-10-01 10:35:15.609534+01	0	24.146714302119765	2	3	2	53	3
1407	0	Excellent work!	2025-10-01 10:35:15.622891+01	2.3	t	2025-10-01 10:35:15.622891+01	69.99044872631774	69.99044872631774	3	3	2	53	3
1408	0	Good performance	2025-10-01 10:35:15.636796+01	1.7	t	2025-10-01 10:35:15.636796+01	58.72392799677399	58.72392799677399	4	3	2	53	3
1409	11.28725434147646	Requires significant improvement	2025-10-01 10:35:15.650653+01	0	f	2025-10-01 10:35:15.650654+01	0	11.28725434147646	1	7	1	53	8
1410	27.68157937326298	Excellent work!	2025-10-01 10:35:15.663722+01	0	f	2025-10-01 10:35:15.663722+01	0	27.68157937326298	2	7	1	53	8
1411	0	Satisfactory	2025-10-01 10:35:15.677696+01	0	f	2025-10-01 10:35:15.677696+01	43.615698402849446	43.615698402849446	3	7	1	53	8
1412	0	Satisfactory	2025-10-01 10:35:15.691648+01	1	f	2025-10-01 10:35:15.691648+01	45.93620694094402	45.93620694094402	4	7	1	53	8
1413	0.7731514911259885	Requires significant improvement	2025-10-01 10:35:15.706342+01	0	f	2025-10-01 10:35:15.706342+01	0	0.7731514911259885	1	7	2	53	8
1414	23.118582107268317	Good performance	2025-10-01 10:35:15.720184+01	0	f	2025-10-01 10:35:15.720185+01	0	23.118582107268317	2	7	2	53	8
1415	0	Excellent work!	2025-10-01 10:35:15.734636+01	2	t	2025-10-01 10:35:15.734636+01	63.81959151010645	63.81959151010645	3	7	2	53	8
1416	0	Good performance	2025-10-01 10:35:15.74937+01	1.7	t	2025-10-01 10:35:15.749371+01	57.12278455459389	57.12278455459389	4	7	2	53	8
1417	24.968923928087676	Good performance	2025-10-01 10:35:15.763039+01	0	f	2025-10-01 10:35:15.763039+01	0	24.968923928087676	1	10	1	53	13
1418	29.145610117539825	Excellent work!	2025-10-01 10:35:15.778163+01	0	f	2025-10-01 10:35:15.778164+01	0	29.145610117539825	2	10	1	53	13
1419	0	Requires significant improvement	2025-10-01 10:35:15.792544+01	0	f	2025-10-01 10:35:15.792544+01	16.35444297464823	16.35444297464823	3	10	1	53	13
1420	0	Needs improvement	2025-10-01 10:35:15.807416+01	0	f	2025-10-01 10:35:15.807417+01	37.38698528759518	37.38698528759518	4	10	1	53	13
1421	23.777544085121363	Good performance	2025-10-01 10:35:15.821842+01	0	f	2025-10-01 10:35:15.821843+01	0	23.777544085121363	1	10	2	53	13
1422	10.926311751931612	Requires significant improvement	2025-10-01 10:35:15.835514+01	0	f	2025-10-01 10:35:15.835515+01	0	10.926311751931612	2	10	2	53	13
1423	0	Good performance	2025-10-01 10:35:15.849397+01	1.3	t	2025-10-01 10:35:15.849398+01	51.92331155136305	51.92331155136305	3	10	2	53	13
1424	0	Excellent work!	2025-10-01 10:35:15.863403+01	2	t	2025-10-01 10:35:15.863403+01	63.13924622457956	63.13924622457956	4	10	2	53	13
1425	10.796805583054482	Requires significant improvement	2025-10-01 10:35:15.877923+01	0	f	2025-10-01 10:35:15.877923+01	0	10.796805583054482	1	13	1	53	18
1426	11.738422319495424	Requires significant improvement	2025-10-01 10:35:15.891515+01	0	f	2025-10-01 10:35:15.891515+01	0	11.738422319495424	2	13	1	53	18
1427	0	Satisfactory	2025-10-01 10:35:15.905563+01	0	f	2025-10-01 10:35:15.905564+01	43.01195449836415	43.01195449836415	3	13	1	53	18
1428	0	Requires significant improvement	2025-10-01 10:35:15.919258+01	0	f	2025-10-01 10:35:15.919259+01	21.364088182656534	21.364088182656534	4	13	1	53	18
1429	7.337679425158777	Requires significant improvement	2025-10-01 10:35:15.932664+01	0	f	2025-10-01 10:35:15.932665+01	0	7.337679425158777	1	13	2	53	18
1430	23.04354868083306	Good performance	2025-10-01 10:35:15.946863+01	0	f	2025-10-01 10:35:15.946864+01	0	23.04354868083306	2	13	2	53	18
1431	0	Requires significant improvement	2025-10-01 10:35:15.971404+01	0	f	2025-10-01 10:35:15.971404+01	31.543332761830992	31.543332761830992	3	13	2	53	18
1432	0	Excellent work!	2025-10-01 10:35:15.986451+01	2	t	2025-10-01 10:35:15.986451+01	60.54823025951804	60.54823025951804	4	13	2	53	18
1433	20.336647257716493	Satisfactory	2025-10-01 10:35:15.99986+01	0	f	2025-10-01 10:35:15.99986+01	0	20.336647257716493	1	16	1	53	23
1434	4.691126875442358	Requires significant improvement	2025-10-01 10:35:16.014494+01	0	f	2025-10-01 10:35:16.014494+01	0	4.691126875442358	2	16	1	53	23
1435	0	Excellent work!	2025-10-01 10:35:16.028506+01	2.3	t	2025-10-01 10:35:16.028506+01	66.9656361444616	66.9656361444616	3	16	1	53	23
1436	0	Satisfactory	2025-10-01 10:35:16.042929+01	1	f	2025-10-01 10:35:16.042929+01	48.84596277760533	48.84596277760533	4	16	1	53	23
1437	25.91864242635069	Excellent work!	2025-10-01 10:35:16.056491+01	0	f	2025-10-01 10:35:16.056491+01	0	25.91864242635069	1	16	2	53	23
1438	22.821218505761756	Good performance	2025-10-01 10:35:16.070737+01	0	f	2025-10-01 10:35:16.070737+01	0	22.821218505761756	2	16	2	53	23
1439	0	Satisfactory	2025-10-01 10:35:16.084519+01	1	f	2025-10-01 10:35:16.084519+01	47.275290687752246	47.275290687752246	3	16	2	53	23
1440	0	Excellent work!	2025-10-01 10:35:16.09851+01	2	t	2025-10-01 10:35:16.09851+01	61.98905721083456	61.98905721083456	4	16	2	53	23
1441	24.85542214466613	Good performance	2025-10-01 10:35:16.11323+01	0	f	2025-10-01 10:35:16.11323+01	0	24.85542214466613	1	3	1	54	3
1442	7.7168133140422155	Requires significant improvement	2025-10-01 10:35:16.126917+01	0	f	2025-10-01 10:35:16.126917+01	0	7.7168133140422155	2	3	1	54	3
1443	0	Requires significant improvement	2025-10-01 10:35:16.142209+01	0	f	2025-10-01 10:35:16.14221+01	24.71297796344988	24.71297796344988	3	3	1	54	3
1444	0	Excellent work!	2025-10-01 10:35:16.156962+01	2.3	t	2025-10-01 10:35:16.156963+01	66.65456185579227	66.65456185579227	4	3	1	54	3
1445	15.905788973472584	Needs improvement	2025-10-01 10:35:16.172604+01	0	f	2025-10-01 10:35:16.172604+01	0	15.905788973472584	1	3	2	54	3
1446	7.755687681938987	Requires significant improvement	2025-10-01 10:35:16.186596+01	0	f	2025-10-01 10:35:16.186597+01	0	7.755687681938987	2	3	2	54	3
1447	0	Excellent work!	2025-10-01 10:35:16.201629+01	2	t	2025-10-01 10:35:16.201629+01	62.96545693217564	62.96545693217564	3	3	2	54	3
1448	0	Good performance	2025-10-01 10:35:16.216905+01	1.3	t	2025-10-01 10:35:16.216906+01	50.63405449056276	50.63405449056276	4	3	2	54	3
1449	6.41728007130311	Requires significant improvement	2025-10-01 10:35:16.231302+01	0	f	2025-10-01 10:35:16.231303+01	0	6.41728007130311	1	7	1	54	8
1450	25.734266557458483	Excellent work!	2025-10-01 10:35:16.24697+01	0	f	2025-10-01 10:35:16.246971+01	0	25.734266557458483	2	7	1	54	8
1451	0	Satisfactory	2025-10-01 10:35:16.261521+01	1	f	2025-10-01 10:35:16.261522+01	48.77893361267911	48.77893361267911	3	7	1	54	8
1452	0	Good performance	2025-10-01 10:35:16.277056+01	1.7	t	2025-10-01 10:35:16.277057+01	56.95762453614625	56.95762453614625	4	7	1	54	8
1453	29.414450100281016	Excellent work!	2025-10-01 10:35:16.291512+01	0	f	2025-10-01 10:35:16.291513+01	0	29.414450100281016	1	7	2	54	8
1454	27.64247403696357	Excellent work!	2025-10-01 10:35:16.306635+01	0	f	2025-10-01 10:35:16.306635+01	0	27.64247403696357	2	7	2	54	8
1455	0	Needs improvement	2025-10-01 10:35:16.321164+01	0	f	2025-10-01 10:35:16.321165+01	35.09915030551366	35.09915030551366	3	7	2	54	8
1456	0	Good performance	2025-10-01 10:35:16.335592+01	1.7	t	2025-10-01 10:35:16.335592+01	55.469838079977364	55.469838079977364	4	7	2	54	8
1457	17.9032343806418	Needs improvement	2025-10-01 10:35:16.35008+01	0	f	2025-10-01 10:35:16.350081+01	0	17.9032343806418	1	10	1	54	13
1458	18.510589182090413	Satisfactory	2025-10-01 10:35:16.364203+01	0	f	2025-10-01 10:35:16.364203+01	0	18.510589182090413	2	10	1	54	13
1459	0	Requires significant improvement	2025-10-01 10:35:16.379577+01	0	f	2025-10-01 10:35:16.379578+01	11.359962000848894	11.359962000848894	3	10	1	54	13
1460	0	Excellent work!	2025-10-01 10:35:16.394278+01	2	t	2025-10-01 10:35:16.394278+01	61.203614259253406	61.203614259253406	4	10	1	54	13
1461	28.76157494065368	Excellent work!	2025-10-01 10:35:16.409614+01	0	f	2025-10-01 10:35:16.409615+01	0	28.76157494065368	1	10	2	54	13
1462	29.048745390490318	Excellent work!	2025-10-01 10:35:16.423269+01	0	f	2025-10-01 10:35:16.42327+01	0	29.048745390490318	2	10	2	54	13
1463	0	Requires significant improvement	2025-10-01 10:35:16.437122+01	0	f	2025-10-01 10:35:16.437123+01	1.203365058381432	1.203365058381432	3	10	2	54	13
1464	0	Good performance	2025-10-01 10:35:16.450995+01	1.3	t	2025-10-01 10:35:16.450996+01	50.716340732280294	50.716340732280294	4	10	2	54	13
1465	24.411742102615342	Good performance	2025-10-01 10:35:16.464166+01	0	f	2025-10-01 10:35:16.464166+01	0	24.411742102615342	1	13	1	54	18
1466	2.6349913830462626	Requires significant improvement	2025-10-01 10:35:16.479059+01	0	f	2025-10-01 10:35:16.47906+01	0	2.6349913830462626	2	13	1	54	18
1467	0	Needs improvement	2025-10-01 10:35:16.49401+01	0	f	2025-10-01 10:35:16.494011+01	39.608394827471336	39.608394827471336	3	13	1	54	18
1468	0	Excellent work!	2025-10-01 10:35:16.509+01	2	t	2025-10-01 10:35:16.509001+01	63.97368192176013	63.97368192176013	4	13	1	54	18
1469	15.001689426301665	Needs improvement	2025-10-01 10:35:16.523998+01	0	f	2025-10-01 10:35:16.523999+01	0	15.001689426301665	1	13	2	54	18
1470	23.268076621376018	Good performance	2025-10-01 10:35:16.539002+01	0	f	2025-10-01 10:35:16.539003+01	0	23.268076621376018	2	13	2	54	18
1471	0	Excellent work!	2025-10-01 10:35:16.553413+01	2.3	t	2025-10-01 10:35:16.553414+01	65.77723447523081	65.77723447523081	3	13	2	54	18
1472	0	Excellent work!	2025-10-01 10:35:16.567468+01	1.7	t	2025-10-01 10:35:16.567468+01	59.507278775074326	59.507278775074326	4	13	2	54	18
1473	5.130151206849887	Requires significant improvement	2025-10-01 10:35:16.58243+01	0	f	2025-10-01 10:35:16.58243+01	0	5.130151206849887	1	16	1	54	23
1474	26.58571616198121	Excellent work!	2025-10-01 10:35:16.597034+01	0	f	2025-10-01 10:35:16.597034+01	0	26.58571616198121	2	16	1	54	23
1475	0	Excellent work!	2025-10-01 10:35:16.612215+01	2	t	2025-10-01 10:35:16.612216+01	64.0972422535557	64.0972422535557	3	16	1	54	23
1476	0	Needs improvement	2025-10-01 10:35:16.626974+01	0	f	2025-10-01 10:35:16.626974+01	35.031040119242505	35.031040119242505	4	16	1	54	23
1477	15.173427385698515	Needs improvement	2025-10-01 10:35:16.642447+01	0	f	2025-10-01 10:35:16.642448+01	0	15.173427385698515	1	16	2	54	23
1478	16.006347103535752	Needs improvement	2025-10-01 10:35:16.65705+01	0	f	2025-10-01 10:35:16.657051+01	0	16.006347103535752	2	16	2	54	23
1479	0	Needs improvement	2025-10-01 10:35:16.672813+01	0	f	2025-10-01 10:35:16.672813+01	37.104904790501465	37.104904790501465	3	16	2	54	23
1480	0	Needs improvement	2025-10-01 10:35:16.687207+01	0	f	2025-10-01 10:35:16.687207+01	38.49718015887015	38.49718015887015	4	16	2	54	23
1481	16.340389683861694	Needs improvement	2025-10-01 10:35:16.702259+01	0	f	2025-10-01 10:35:16.702259+01	0	16.340389683861694	1	4	1	55	4
1482	11.657515865250948	Requires significant improvement	2025-10-01 10:35:16.716979+01	0	f	2025-10-01 10:35:16.71698+01	0	11.657515865250948	2	4	1	55	4
1483	0	Good performance	2025-10-01 10:35:16.730493+01	1.7	t	2025-10-01 10:35:16.730494+01	58.958301659532204	58.958301659532204	3	4	1	55	4
1484	0	Excellent work!	2025-10-01 10:35:16.745329+01	2.3	t	2025-10-01 10:35:16.74533+01	66.2841994041375	66.2841994041375	4	4	1	55	4
1485	20.869324162984483	Satisfactory	2025-10-01 10:35:16.760253+01	0	f	2025-10-01 10:35:16.760254+01	0	20.869324162984483	1	4	2	55	4
1486	17.167752196883352	Needs improvement	2025-10-01 10:35:16.775248+01	0	f	2025-10-01 10:35:16.775248+01	0	17.167752196883352	2	4	2	55	4
1487	0	Excellent work!	2025-10-01 10:35:16.789426+01	2.3	t	2025-10-01 10:35:16.789426+01	69.22536826240662	69.22536826240662	3	4	2	55	4
1488	0	Requires significant improvement	2025-10-01 10:35:16.804873+01	0	f	2025-10-01 10:35:16.804873+01	3.280892001898874	3.280892001898874	4	4	2	55	4
1489	16.854447171972975	Needs improvement	2025-10-01 10:35:16.822405+01	0	f	2025-10-01 10:35:16.822406+01	0	16.854447171972975	1	8	1	55	9
1490	6.241884617197158	Requires significant improvement	2025-10-01 10:35:16.837407+01	0	f	2025-10-01 10:35:16.837407+01	0	6.241884617197158	2	8	1	55	9
1491	0	Excellent work!	2025-10-01 10:35:16.851645+01	2	t	2025-10-01 10:35:16.851645+01	62.30673695823968	62.30673695823968	3	8	1	55	9
1492	0	Satisfactory	2025-10-01 10:35:16.865575+01	1	f	2025-10-01 10:35:16.865576+01	48.37846813272867	48.37846813272867	4	8	1	55	9
1493	24.033156981881962	Good performance	2025-10-01 10:35:16.88057+01	0	f	2025-10-01 10:35:16.880571+01	0	24.033156981881962	1	8	2	55	9
1494	17.801226001062705	Needs improvement	2025-10-01 10:35:16.894838+01	0	f	2025-10-01 10:35:16.894838+01	0	17.801226001062705	2	8	2	55	9
1495	0	Good performance	2025-10-01 10:35:16.909333+01	1	f	2025-10-01 10:35:16.909333+01	49.647920080232964	49.647920080232964	3	8	2	55	9
1496	0	Excellent work!	2025-10-01 10:35:16.923676+01	2.3	t	2025-10-01 10:35:16.923677+01	66.34901924488827	66.34901924488827	4	8	2	55	9
1497	18.60465113079389	Satisfactory	2025-10-01 10:35:16.942838+01	0	f	2025-10-01 10:35:16.942838+01	0	18.60465113079389	1	11	1	55	14
1498	29.410146915066864	Excellent work!	2025-10-01 10:35:16.96424+01	0	f	2025-10-01 10:35:16.964241+01	0	29.410146915066864	2	11	1	55	14
1499	0	Excellent work!	2025-10-01 10:35:16.980323+01	2	t	2025-10-01 10:35:16.980324+01	63.824135234132285	63.824135234132285	3	11	1	55	14
1500	0	Excellent work!	2025-10-01 10:35:16.996293+01	2	t	2025-10-01 10:35:16.996294+01	64.7940098280568	64.7940098280568	4	11	1	55	14
1501	18.898378858499985	Satisfactory	2025-10-01 10:35:17.012052+01	0	f	2025-10-01 10:35:17.012052+01	0	18.898378858499985	1	11	2	55	14
1502	1.3279279386712646	Requires significant improvement	2025-10-01 10:35:17.026673+01	0	f	2025-10-01 10:35:17.026673+01	0	1.3279279386712646	2	11	2	55	14
1503	0	Satisfactory	2025-10-01 10:35:17.041885+01	0	f	2025-10-01 10:35:17.041885+01	43.21389169862885	43.21389169862885	3	11	2	55	14
1504	0	Excellent work!	2025-10-01 10:35:17.056929+01	2.3	t	2025-10-01 10:35:17.05693+01	65.33713753253951	65.33713753253951	4	11	2	55	14
1505	26.405103971515175	Excellent work!	2025-10-01 10:35:17.072123+01	0	f	2025-10-01 10:35:17.072124+01	0	26.405103971515175	1	14	1	55	19
1506	27.31536184067347	Excellent work!	2025-10-01 10:35:17.086929+01	0	f	2025-10-01 10:35:17.08693+01	0	27.31536184067347	2	14	1	55	19
1507	0	Good performance	2025-10-01 10:35:17.102029+01	1.7	t	2025-10-01 10:35:17.10203+01	58.028220526055804	58.028220526055804	3	14	1	55	19
1508	0	Good performance	2025-10-01 10:35:17.116741+01	1.7	t	2025-10-01 10:35:17.116741+01	57.13067153465264	57.13067153465264	4	14	1	55	19
1509	21.926561169885616	Good performance	2025-10-01 10:35:17.130784+01	0	f	2025-10-01 10:35:17.130785+01	0	21.926561169885616	1	14	2	55	19
1510	25.32031889652197	Good performance	2025-10-01 10:35:17.14714+01	0	f	2025-10-01 10:35:17.147141+01	0	25.32031889652197	2	14	2	55	19
1511	0	Needs improvement	2025-10-01 10:35:17.162157+01	0	f	2025-10-01 10:35:17.162158+01	38.219089323545376	38.219089323545376	3	14	2	55	19
1512	0	Requires significant improvement	2025-10-01 10:35:17.178116+01	0	f	2025-10-01 10:35:17.178117+01	23.918264941319446	23.918264941319446	4	14	2	55	19
1513	16.202054448366056	Needs improvement	2025-10-01 10:35:17.193072+01	0	f	2025-10-01 10:35:17.193072+01	0	16.202054448366056	1	17	1	55	24
1514	26.540995489745438	Excellent work!	2025-10-01 10:35:17.208492+01	0	f	2025-10-01 10:35:17.208492+01	0	26.540995489745438	2	17	1	55	24
1515	0	Satisfactory	2025-10-01 10:35:17.223746+01	0	f	2025-10-01 10:35:17.223747+01	42.779948798592685	42.779948798592685	3	17	1	55	24
1516	0	Satisfactory	2025-10-01 10:35:17.239132+01	1	f	2025-10-01 10:35:17.239132+01	48.63913768819455	48.63913768819455	4	17	1	55	24
1517	10.684709986476925	Requires significant improvement	2025-10-01 10:35:17.253802+01	0	f	2025-10-01 10:35:17.253802+01	0	10.684709986476925	1	17	2	55	24
1518	25.7248644695012	Excellent work!	2025-10-01 10:35:17.268679+01	0	f	2025-10-01 10:35:17.268679+01	0	25.7248644695012	2	17	2	55	24
1519	0	Excellent work!	2025-10-01 10:35:17.283516+01	2	t	2025-10-01 10:35:17.283516+01	60.295814156417364	60.295814156417364	3	17	2	55	24
1520	0	Needs improvement	2025-10-01 10:35:17.298174+01	0	f	2025-10-01 10:35:17.298174+01	38.24621024584922	38.24621024584922	4	17	2	55	24
1521	6.4221487448592205	Requires significant improvement	2025-10-01 10:35:17.313513+01	0	f	2025-10-01 10:35:17.313513+01	0	6.4221487448592205	1	4	1	56	4
1522	15.547295126152035	Needs improvement	2025-10-01 10:35:17.32829+01	0	f	2025-10-01 10:35:17.328291+01	0	15.547295126152035	2	4	1	56	4
1523	0	Requires significant improvement	2025-10-01 10:35:17.343828+01	0	f	2025-10-01 10:35:17.343828+01	33.72937990794631	33.72937990794631	3	4	1	56	4
1524	0	Excellent work!	2025-10-01 10:35:17.365366+01	2	t	2025-10-01 10:35:17.365369+01	60.78055314090798	60.78055314090798	4	4	1	56	4
1525	5.073328824641032	Requires significant improvement	2025-10-01 10:35:17.383465+01	0	f	2025-10-01 10:35:17.383466+01	0	5.073328824641032	1	4	2	56	4
1526	20.03573055164218	Satisfactory	2025-10-01 10:35:17.40783+01	0	f	2025-10-01 10:35:17.407831+01	0	20.03573055164218	2	4	2	56	4
1527	0	Satisfactory	2025-10-01 10:35:17.432536+01	0	f	2025-10-01 10:35:17.432537+01	43.75905664078367	43.75905664078367	3	4	2	56	4
1528	0	Excellent work!	2025-10-01 10:35:17.456687+01	2	t	2025-10-01 10:35:17.456687+01	64.79206474127203	64.79206474127203	4	4	2	56	4
1529	24.93950292332132	Good performance	2025-10-01 10:35:17.482205+01	0	f	2025-10-01 10:35:17.482208+01	0	24.93950292332132	1	8	1	56	9
1530	28.66014974042761	Excellent work!	2025-10-01 10:35:17.502296+01	0	f	2025-10-01 10:35:17.502296+01	0	28.66014974042761	2	8	1	56	9
1531	0	Requires significant improvement	2025-10-01 10:35:17.52321+01	0	f	2025-10-01 10:35:17.523211+01	33.843796419427626	33.843796419427626	3	8	1	56	9
1532	0	Excellent work!	2025-10-01 10:35:17.538992+01	2.3	t	2025-10-01 10:35:17.538992+01	69.4175943099276	69.4175943099276	4	8	1	56	9
1533	9.576114837161775	Requires significant improvement	2025-10-01 10:35:17.553735+01	0	f	2025-10-01 10:35:17.553736+01	0	9.576114837161775	1	8	2	56	9
1534	26.47294262308214	Excellent work!	2025-10-01 10:35:17.569749+01	0	f	2025-10-01 10:35:17.56975+01	0	26.47294262308214	2	8	2	56	9
1535	0	Excellent work!	2025-10-01 10:35:17.584994+01	2.3	t	2025-10-01 10:35:17.584995+01	67.25524893873484	67.25524893873484	3	8	2	56	9
1536	0	Needs improvement	2025-10-01 10:35:17.599682+01	0	f	2025-10-01 10:35:17.599682+01	38.302416651270164	38.302416651270164	4	8	2	56	9
1537	15.87854497763193	Needs improvement	2025-10-01 10:35:17.615246+01	0	f	2025-10-01 10:35:17.615247+01	0	15.87854497763193	1	11	1	56	14
1538	25.409366454410247	Good performance	2025-10-01 10:35:17.629634+01	0	f	2025-10-01 10:35:17.629634+01	0	25.409366454410247	2	11	1	56	14
1539	0	Excellent work!	2025-10-01 10:35:17.645562+01	2	t	2025-10-01 10:35:17.645563+01	64.36427377803929	64.36427377803929	3	11	1	56	14
1540	0	Excellent work!	2025-10-01 10:35:17.660066+01	2.3	t	2025-10-01 10:35:17.660066+01	67.61097059277682	67.61097059277682	4	11	1	56	14
1541	29.67008530748855	Excellent work!	2025-10-01 10:35:17.674742+01	0	f	2025-10-01 10:35:17.674743+01	0	29.67008530748855	1	11	2	56	14
1542	27.773442764520233	Excellent work!	2025-10-01 10:35:17.689562+01	0	f	2025-10-01 10:35:17.689563+01	0	27.773442764520233	2	11	2	56	14
1543	0	Good performance	2025-10-01 10:35:17.704716+01	1.7	t	2025-10-01 10:35:17.704716+01	57.50702111890149	57.50702111890149	3	11	2	56	14
1544	0	Needs improvement	2025-10-01 10:35:17.71931+01	0	f	2025-10-01 10:35:17.719311+01	37.269694256288325	37.269694256288325	4	11	2	56	14
1545	29.047331381377084	Excellent work!	2025-10-01 10:35:17.733837+01	0	f	2025-10-01 10:35:17.733837+01	0	29.047331381377084	1	14	1	56	19
1546	25.088007023570054	Good performance	2025-10-01 10:35:17.749365+01	0	f	2025-10-01 10:35:17.749366+01	0	25.088007023570054	2	14	1	56	19
1547	0	Requires significant improvement	2025-10-01 10:35:17.76393+01	0	f	2025-10-01 10:35:17.763931+01	24.585525118678586	24.585525118678586	3	14	1	56	19
1548	0	Requires significant improvement	2025-10-01 10:35:17.77904+01	0	f	2025-10-01 10:35:17.77904+01	10.377021195959548	10.377021195959548	4	14	1	56	19
1549	20.612498295575186	Satisfactory	2025-10-01 10:35:17.793379+01	0	f	2025-10-01 10:35:17.79338+01	0	20.612498295575186	1	14	2	56	19
1550	25.052173926284112	Good performance	2025-10-01 10:35:17.808376+01	0	f	2025-10-01 10:35:17.808376+01	0	25.052173926284112	2	14	2	56	19
1551	0	Satisfactory	2025-10-01 10:35:17.823723+01	1	f	2025-10-01 10:35:17.823723+01	47.266560172037686	47.266560172037686	3	14	2	56	19
1552	0	Needs improvement	2025-10-01 10:35:17.83857+01	0	f	2025-10-01 10:35:17.838571+01	38.865945405096305	38.865945405096305	4	14	2	56	19
1553	21.469060112428213	Good performance	2025-10-01 10:35:17.852876+01	0	f	2025-10-01 10:35:17.852877+01	0	21.469060112428213	1	17	1	56	24
1554	6.319112623153886	Requires significant improvement	2025-10-01 10:35:17.867402+01	0	f	2025-10-01 10:35:17.867402+01	0	6.319112623153886	2	17	1	56	24
1555	0	Requires significant improvement	2025-10-01 10:35:17.882177+01	0	f	2025-10-01 10:35:17.882177+01	22.80557916977485	22.80557916977485	3	17	1	56	24
1556	0	Excellent work!	2025-10-01 10:35:17.896375+01	1.7	t	2025-10-01 10:35:17.896376+01	59.682758464558646	59.682758464558646	4	17	1	56	24
1557	24.753010686733795	Good performance	2025-10-01 10:35:17.911927+01	0	f	2025-10-01 10:35:17.911927+01	0	24.753010686733795	1	17	2	56	24
1558	20.712127988579745	Satisfactory	2025-10-01 10:35:17.926747+01	0	f	2025-10-01 10:35:17.926747+01	0	20.712127988579745	2	17	2	56	24
1559	0	Excellent work!	2025-10-01 10:35:17.941846+01	2.3	t	2025-10-01 10:35:17.941846+01	65.26757307626467	65.26757307626467	3	17	2	56	24
1560	0	Satisfactory	2025-10-01 10:35:17.956223+01	0	f	2025-10-01 10:35:17.956223+01	44.31141188565508	44.31141188565508	4	17	2	56	24
1561	19.91162841773837	Satisfactory	2025-10-01 10:35:17.971812+01	0	f	2025-10-01 10:35:17.971812+01	0	19.91162841773837	1	4	1	57	4
1562	19.59026581747219	Satisfactory	2025-10-01 10:35:17.986115+01	0	f	2025-10-01 10:35:17.986116+01	0	19.59026581747219	2	4	1	57	4
1563	0	Needs improvement	2025-10-01 10:35:18.002746+01	0	f	2025-10-01 10:35:18.002747+01	41.165165988541766	41.165165988541766	3	4	1	57	4
1564	0	Satisfactory	2025-10-01 10:35:18.017886+01	1	f	2025-10-01 10:35:18.017887+01	46.75904878513494	46.75904878513494	4	4	1	57	4
1565	13.297602644935175	Requires significant improvement	2025-10-01 10:35:18.032634+01	0	f	2025-10-01 10:35:18.032635+01	0	13.297602644935175	1	4	2	57	4
1566	29.718445754263243	Excellent work!	2025-10-01 10:35:18.047994+01	0	f	2025-10-01 10:35:18.047994+01	0	29.718445754263243	2	4	2	57	4
1567	0	Needs improvement	2025-10-01 10:35:18.062766+01	0	f	2025-10-01 10:35:18.062766+01	40.64978506503103	40.64978506503103	3	4	2	57	4
1568	0	Excellent work!	2025-10-01 10:35:18.078053+01	2.3	t	2025-10-01 10:35:18.078053+01	65.69868387787781	65.69868387787781	4	4	2	57	4
1569	23.839610604355492	Good performance	2025-10-01 10:35:18.09278+01	0	f	2025-10-01 10:35:18.09278+01	0	23.839610604355492	1	8	1	57	9
1570	16.526272522891706	Needs improvement	2025-10-01 10:35:18.108133+01	0	f	2025-10-01 10:35:18.108134+01	0	16.526272522891706	2	8	1	57	9
1571	0	Excellent work!	2025-10-01 10:35:18.122974+01	2.3	t	2025-10-01 10:35:18.122975+01	67.29993346294917	67.29993346294917	3	8	1	57	9
1572	0	Excellent work!	2025-10-01 10:35:18.138057+01	2	t	2025-10-01 10:35:18.138057+01	62.76897292977513	62.76897292977513	4	8	1	57	9
1573	1.7173296403544618	Requires significant improvement	2025-10-01 10:35:18.153456+01	0	f	2025-10-01 10:35:18.153457+01	0	1.7173296403544618	1	8	2	57	9
1574	12.069136008754684	Requires significant improvement	2025-10-01 10:35:18.168603+01	0	f	2025-10-01 10:35:18.168603+01	0	12.069136008754684	2	8	2	57	9
1575	0	Satisfactory	2025-10-01 10:35:18.184197+01	0	f	2025-10-01 10:35:18.184197+01	42.13115807040582	42.13115807040582	3	8	2	57	9
1576	0	Requires significant improvement	2025-10-01 10:35:18.19945+01	0	f	2025-10-01 10:35:18.199451+01	21.79187285589687	21.79187285589687	4	8	2	57	9
1577	26.85819239991386	Excellent work!	2025-10-01 10:35:18.214768+01	0	f	2025-10-01 10:35:18.214768+01	0	26.85819239991386	1	11	1	57	14
1578	21.81607847791299	Good performance	2025-10-01 10:35:18.229877+01	0	f	2025-10-01 10:35:18.229878+01	0	21.81607847791299	2	11	1	57	14
1579	0	Requires significant improvement	2025-10-01 10:35:18.246981+01	0	f	2025-10-01 10:35:18.246981+01	29.59326013476056	29.59326013476056	3	11	1	57	14
1580	0	Good performance	2025-10-01 10:35:18.262678+01	1.3	t	2025-10-01 10:35:18.262678+01	53.999594273240305	53.999594273240305	4	11	1	57	14
1581	25.351410442732284	Good performance	2025-10-01 10:35:18.278842+01	0	f	2025-10-01 10:35:18.278842+01	0	25.351410442732284	1	11	2	57	14
1582	25.903504836825476	Excellent work!	2025-10-01 10:35:18.294153+01	0	f	2025-10-01 10:35:18.294153+01	0	25.903504836825476	2	11	2	57	14
1583	0	Excellent work!	2025-10-01 10:35:18.309877+01	2.3	t	2025-10-01 10:35:18.309877+01	65.7931312101954	65.7931312101954	3	11	2	57	14
1584	0	Requires significant improvement	2025-10-01 10:35:18.325376+01	0	f	2025-10-01 10:35:18.325376+01	33.77640224377133	33.77640224377133	4	11	2	57	14
1585	28.457682297508008	Excellent work!	2025-10-01 10:35:18.341281+01	0	f	2025-10-01 10:35:18.341282+01	0	28.457682297508008	1	14	1	57	19
1586	26.617661132507408	Excellent work!	2025-10-01 10:35:18.356487+01	0	f	2025-10-01 10:35:18.356488+01	0	26.617661132507408	2	14	1	57	19
1587	0	Excellent work!	2025-10-01 10:35:18.372088+01	2	t	2025-10-01 10:35:18.372089+01	60.5021174111583	60.5021174111583	3	14	1	57	19
1588	0	Good performance	2025-10-01 10:35:18.38785+01	1.3	t	2025-10-01 10:35:18.38785+01	53.95971055782968	53.95971055782968	4	14	1	57	19
1589	22.308910605521348	Good performance	2025-10-01 10:35:18.402811+01	0	f	2025-10-01 10:35:18.402812+01	0	22.308910605521348	1	14	2	57	19
1590	21.525774903295797	Good performance	2025-10-01 10:35:18.417817+01	0	f	2025-10-01 10:35:18.417818+01	0	21.525774903295797	2	14	2	57	19
1591	0	Good performance	2025-10-01 10:35:18.432022+01	1.3	t	2025-10-01 10:35:18.432022+01	51.37201692707184	51.37201692707184	3	14	2	57	19
1592	0	Excellent work!	2025-10-01 10:35:18.447144+01	2.3	t	2025-10-01 10:35:18.447144+01	68.76714946797404	68.76714946797404	4	14	2	57	19
1593	9.868971356404167	Requires significant improvement	2025-10-01 10:35:18.462027+01	0	f	2025-10-01 10:35:18.462027+01	0	9.868971356404167	1	17	1	57	24
1594	10.895656091078129	Requires significant improvement	2025-10-01 10:35:18.477353+01	0	f	2025-10-01 10:35:18.477354+01	0	10.895656091078129	2	17	1	57	24
1595	0	Excellent work!	2025-10-01 10:35:18.492227+01	2	t	2025-10-01 10:35:18.492228+01	64.44608236259664	64.44608236259664	3	17	1	57	24
1596	0	Requires significant improvement	2025-10-01 10:35:18.509468+01	0	f	2025-10-01 10:35:18.509469+01	24.493025853413556	24.493025853413556	4	17	1	57	24
1597	19.93719284139878	Satisfactory	2025-10-01 10:35:18.524208+01	0	f	2025-10-01 10:35:18.524209+01	0	19.93719284139878	1	17	2	57	24
1598	23.315717235719838	Good performance	2025-10-01 10:35:18.54079+01	0	f	2025-10-01 10:35:18.540791+01	0	23.315717235719838	2	17	2	57	24
1599	0	Good performance	2025-10-01 10:35:18.556428+01	1.7	t	2025-10-01 10:35:18.556429+01	58.63748997878003	58.63748997878003	3	17	2	57	24
1600	0	Excellent work!	2025-10-01 10:35:18.572469+01	2.3	t	2025-10-01 10:35:18.572469+01	68.30304454658535	68.30304454658535	4	17	2	57	24
1601	24.93255495461907	Good performance	2025-10-01 10:35:18.587858+01	0	f	2025-10-01 10:35:18.587858+01	0	24.93255495461907	1	4	1	58	4
1602	25.205159100723716	Good performance	2025-10-01 10:35:18.60597+01	0	f	2025-10-01 10:35:18.605971+01	0	25.205159100723716	2	4	1	58	4
1603	0	Excellent work!	2025-10-01 10:35:18.631087+01	2	t	2025-10-01 10:35:18.631087+01	61.65460898770514	61.65460898770514	3	4	1	58	4
1604	0	Excellent work!	2025-10-01 10:35:18.65639+01	2.3	t	2025-10-01 10:35:18.656391+01	68.04207985255132	68.04207985255132	4	4	1	58	4
1605	24.227957599536644	Good performance	2025-10-01 10:35:18.675009+01	0	f	2025-10-01 10:35:18.675009+01	0	24.227957599536644	1	4	2	58	4
1606	29.69968989637182	Excellent work!	2025-10-01 10:35:18.690473+01	0	f	2025-10-01 10:35:18.690473+01	0	29.69968989637182	2	4	2	58	4
1607	0	Excellent work!	2025-10-01 10:35:18.706881+01	2.3	t	2025-10-01 10:35:18.706882+01	67.80699597037673	67.80699597037673	3	4	2	58	4
1608	0	Excellent work!	2025-10-01 10:35:18.722322+01	2	t	2025-10-01 10:35:18.722322+01	64.81561904595083	64.81561904595083	4	4	2	58	4
1609	21.287222574886552	Good performance	2025-10-01 10:35:18.739363+01	0	f	2025-10-01 10:35:18.739363+01	0	21.287222574886552	1	8	1	58	9
1610	28.796731351170326	Excellent work!	2025-10-01 10:35:18.755324+01	0	f	2025-10-01 10:35:18.755324+01	0	28.796731351170326	2	8	1	58	9
1611	0	Requires significant improvement	2025-10-01 10:35:18.771671+01	0	f	2025-10-01 10:35:18.771672+01	32.25704782865811	32.25704782865811	3	8	1	58	9
1612	0	Requires significant improvement	2025-10-01 10:35:18.787521+01	0	f	2025-10-01 10:35:18.787522+01	9.118795102854502	9.118795102854502	4	8	1	58	9
1613	16.760829342006943	Needs improvement	2025-10-01 10:35:18.809272+01	0	f	2025-10-01 10:35:18.809273+01	0	16.760829342006943	1	8	2	58	9
1614	26.06226504047244	Excellent work!	2025-10-01 10:35:18.825228+01	0	f	2025-10-01 10:35:18.825229+01	0	26.06226504047244	2	8	2	58	9
1615	0	Excellent work!	2025-10-01 10:35:18.84135+01	2	t	2025-10-01 10:35:18.84135+01	60.61247410387453	60.61247410387453	3	8	2	58	9
1616	0	Requires significant improvement	2025-10-01 10:35:18.856852+01	0	f	2025-10-01 10:35:18.856852+01	5.393256756511603	5.393256756511603	4	8	2	58	9
1617	18.132991966551074	Satisfactory	2025-10-01 10:35:18.872776+01	0	f	2025-10-01 10:35:18.872776+01	0	18.132991966551074	1	11	1	58	14
1618	25.149901615597784	Good performance	2025-10-01 10:35:18.888232+01	0	f	2025-10-01 10:35:18.888232+01	0	25.149901615597784	2	11	1	58	14
1619	0	Good performance	2025-10-01 10:35:18.903534+01	1.7	t	2025-10-01 10:35:18.903534+01	58.99079257623549	58.99079257623549	3	11	1	58	14
1620	0	Good performance	2025-10-01 10:35:18.919331+01	1.7	t	2025-10-01 10:35:18.919332+01	57.31137251294888	57.31137251294888	4	11	1	58	14
1621	20.826601526687025	Satisfactory	2025-10-01 10:35:18.9349+01	0	f	2025-10-01 10:35:18.934901+01	0	20.826601526687025	1	11	2	58	14
1622	28.234976419506108	Excellent work!	2025-10-01 10:35:18.950982+01	0	f	2025-10-01 10:35:18.950983+01	0	28.234976419506108	2	11	2	58	14
1623	0	Good performance	2025-10-01 10:35:18.96687+01	1.7	t	2025-10-01 10:35:18.966871+01	57.882268302305285	57.882268302305285	3	11	2	58	14
1624	0	Satisfactory	2025-10-01 10:35:18.982778+01	0	f	2025-10-01 10:35:18.982779+01	44.726258800025434	44.726258800025434	4	11	2	58	14
1625	29.46627484649377	Excellent work!	2025-10-01 10:35:18.999108+01	0	f	2025-10-01 10:35:18.999109+01	0	29.46627484649377	1	14	1	58	19
1626	28.482916252765442	Excellent work!	2025-10-01 10:35:19.016198+01	0	f	2025-10-01 10:35:19.016199+01	0	28.482916252765442	2	14	1	58	19
1627	0	Satisfactory	2025-10-01 10:35:19.031412+01	1	f	2025-10-01 10:35:19.031413+01	45.49184078604473	45.49184078604473	3	14	1	58	19
1628	0	Needs improvement	2025-10-01 10:35:19.047313+01	0	f	2025-10-01 10:35:19.047313+01	37.946780148795554	37.946780148795554	4	14	1	58	19
1629	19.258176274178716	Satisfactory	2025-10-01 10:35:19.062537+01	0	f	2025-10-01 10:35:19.062537+01	0	19.258176274178716	1	14	2	58	19
1630	24.04857714265398	Good performance	2025-10-01 10:35:19.078597+01	0	f	2025-10-01 10:35:19.078598+01	0	24.04857714265398	2	14	2	58	19
1631	0	Needs improvement	2025-10-01 10:35:19.094266+01	0	f	2025-10-01 10:35:19.094267+01	35.715726798425386	35.715726798425386	3	14	2	58	19
1632	0	Excellent work!	2025-10-01 10:35:19.110813+01	2.3	t	2025-10-01 10:35:19.110814+01	68.09061188060252	68.09061188060252	4	14	2	58	19
1633	24.833621707101152	Good performance	2025-10-01 10:35:19.128012+01	0	f	2025-10-01 10:35:19.128013+01	0	24.833621707101152	1	17	1	58	24
1634	22.517217644898906	Good performance	2025-10-01 10:35:19.143821+01	0	f	2025-10-01 10:35:19.143822+01	0	22.517217644898906	2	17	1	58	24
1635	0	Needs improvement	2025-10-01 10:35:19.159137+01	0	f	2025-10-01 10:35:19.159138+01	38.62161957737864	38.62161957737864	3	17	1	58	24
1636	0	Satisfactory	2025-10-01 10:35:19.175421+01	1	f	2025-10-01 10:35:19.175421+01	45.539353660528036	45.539353660528036	4	17	1	58	24
1637	26.272191412793127	Excellent work!	2025-10-01 10:35:19.19105+01	0	f	2025-10-01 10:35:19.19105+01	0	26.272191412793127	1	17	2	58	24
1638	16.238414837672465	Needs improvement	2025-10-01 10:35:19.20735+01	0	f	2025-10-01 10:35:19.20735+01	0	16.238414837672465	2	17	2	58	24
1639	0	Satisfactory	2025-10-01 10:35:19.222709+01	1	f	2025-10-01 10:35:19.222709+01	48.746415070148366	48.746415070148366	3	17	2	58	24
1640	0	Requires significant improvement	2025-10-01 10:35:19.23889+01	0	f	2025-10-01 10:35:19.238891+01	25.340380834608716	25.340380834608716	4	17	2	58	24
1641	21.660341109111314	Good performance	2025-10-01 10:35:19.254354+01	0	f	2025-10-01 10:35:19.254354+01	0	21.660341109111314	1	4	1	59	4
1642	20.421120908049605	Satisfactory	2025-10-01 10:35:19.270236+01	0	f	2025-10-01 10:35:19.270236+01	0	20.421120908049605	2	4	1	59	4
1643	0	Needs improvement	2025-10-01 10:35:19.285905+01	0	f	2025-10-01 10:35:19.285905+01	38.497395426521614	38.497395426521614	3	4	1	59	4
1644	0	Satisfactory	2025-10-01 10:35:19.303032+01	1	f	2025-10-01 10:35:19.303033+01	46.442224816192635	46.442224816192635	4	4	1	59	4
1645	20.896059145849772	Satisfactory	2025-10-01 10:35:19.319279+01	0	f	2025-10-01 10:35:19.31928+01	0	20.896059145849772	1	4	2	59	4
1646	29.845043766278515	Excellent work!	2025-10-01 10:35:19.334511+01	0	f	2025-10-01 10:35:19.334511+01	0	29.845043766278515	2	4	2	59	4
1647	0	Good performance	2025-10-01 10:35:19.349479+01	1.7	t	2025-10-01 10:35:19.349479+01	56.371142028223815	56.371142028223815	3	4	2	59	4
1648	0	Requires significant improvement	2025-10-01 10:35:19.364731+01	0	f	2025-10-01 10:35:19.364731+01	5.856295958979012	5.856295958979012	4	4	2	59	4
1649	15.294946098700837	Needs improvement	2025-10-01 10:35:19.380453+01	0	f	2025-10-01 10:35:19.380453+01	0	15.294946098700837	1	8	1	59	9
1650	29.441124356742492	Excellent work!	2025-10-01 10:35:19.396524+01	0	f	2025-10-01 10:35:19.396524+01	0	29.441124356742492	2	8	1	59	9
1651	0	Needs improvement	2025-10-01 10:35:19.412402+01	0	f	2025-10-01 10:35:19.412402+01	35.70559431681914	35.70559431681914	3	8	1	59	9
1652	0	Excellent work!	2025-10-01 10:35:19.427517+01	2.3	t	2025-10-01 10:35:19.427518+01	69.88014663045564	69.88014663045564	4	8	1	59	9
1653	29.085964572669965	Excellent work!	2025-10-01 10:35:19.443818+01	0	f	2025-10-01 10:35:19.443819+01	0	29.085964572669965	1	8	2	59	9
1654	15.967928037267141	Needs improvement	2025-10-01 10:35:19.459089+01	0	f	2025-10-01 10:35:19.459089+01	0	15.967928037267141	2	8	2	59	9
1655	0	Good performance	2025-10-01 10:35:19.475054+01	1.7	t	2025-10-01 10:35:19.475055+01	57.861992545413	57.861992545413	3	8	2	59	9
1656	0	Good performance	2025-10-01 10:35:19.490846+01	1.7	t	2025-10-01 10:35:19.490846+01	59.15527102909164	59.15527102909164	4	8	2	59	9
1657	27.395052513965634	Excellent work!	2025-10-01 10:35:19.508062+01	0	f	2025-10-01 10:35:19.508063+01	0	27.395052513965634	1	11	1	59	14
1658	25.831891015261405	Excellent work!	2025-10-01 10:35:19.524026+01	0	f	2025-10-01 10:35:19.524026+01	0	25.831891015261405	2	11	1	59	14
1659	0	Needs improvement	2025-10-01 10:35:19.539787+01	0	f	2025-10-01 10:35:19.539788+01	37.37432687393628	37.37432687393628	3	11	1	59	14
1660	0	Needs improvement	2025-10-01 10:35:19.555379+01	0	f	2025-10-01 10:35:19.555379+01	37.54097972858731	37.54097972858731	4	11	1	59	14
1661	29.09182797386891	Excellent work!	2025-10-01 10:35:19.570928+01	0	f	2025-10-01 10:35:19.570928+01	0	29.09182797386891	1	11	2	59	14
1662	24.38995155895108	Good performance	2025-10-01 10:35:19.586319+01	0	f	2025-10-01 10:35:19.586319+01	0	24.38995155895108	2	11	2	59	14
1663	0	Satisfactory	2025-10-01 10:35:19.601587+01	1	f	2025-10-01 10:35:19.601588+01	45.061131112734	45.061131112734	3	11	2	59	14
1664	0	Requires significant improvement	2025-10-01 10:35:19.618702+01	0	f	2025-10-01 10:35:19.618702+01	1.3911002483135841	1.3911002483135841	4	11	2	59	14
1665	29.29683282097304	Excellent work!	2025-10-01 10:35:19.635057+01	0	f	2025-10-01 10:35:19.635058+01	0	29.29683282097304	1	14	1	59	19
1666	28.985346530579793	Excellent work!	2025-10-01 10:35:19.65128+01	0	f	2025-10-01 10:35:19.65128+01	0	28.985346530579793	2	14	1	59	19
1667	0	Satisfactory	2025-10-01 10:35:19.666829+01	1	f	2025-10-01 10:35:19.666829+01	45.29292339675665	45.29292339675665	3	14	1	59	19
1668	0	Good performance	2025-10-01 10:35:19.682666+01	1.7	t	2025-10-01 10:35:19.682666+01	57.95279005622214	57.95279005622214	4	14	1	59	19
1669	25.612563965088672	Excellent work!	2025-10-01 10:35:19.698411+01	0	f	2025-10-01 10:35:19.698411+01	0	25.612563965088672	1	14	2	59	19
1670	26.530407353422525	Excellent work!	2025-10-01 10:35:19.714927+01	0	f	2025-10-01 10:35:19.714928+01	0	26.530407353422525	2	14	2	59	19
1671	0	Requires significant improvement	2025-10-01 10:35:19.729849+01	0	f	2025-10-01 10:35:19.729849+01	26.314070416566203	26.314070416566203	3	14	2	59	19
1672	0	Satisfactory	2025-10-01 10:35:19.746151+01	0	f	2025-10-01 10:35:19.746152+01	44.599341478284096	44.599341478284096	4	14	2	59	19
1673	25.36437322600566	Good performance	2025-10-01 10:35:19.761748+01	0	f	2025-10-01 10:35:19.761748+01	0	25.36437322600566	1	17	1	59	24
1674	21.95586500611152	Good performance	2025-10-01 10:35:19.778042+01	0	f	2025-10-01 10:35:19.778043+01	0	21.95586500611152	2	17	1	59	24
1675	0	Excellent work!	2025-10-01 10:35:19.793333+01	2.3	t	2025-10-01 10:35:19.793333+01	67.22446981917066	67.22446981917066	3	17	1	59	24
1676	0	Satisfactory	2025-10-01 10:35:19.809016+01	0	f	2025-10-01 10:35:19.809017+01	43.79914289772244	43.79914289772244	4	17	1	59	24
1677	12.076870161635219	Requires significant improvement	2025-10-01 10:35:19.824936+01	0	f	2025-10-01 10:35:19.824936+01	0	12.076870161635219	1	17	2	59	24
1678	16.093416229750414	Needs improvement	2025-10-01 10:35:19.841671+01	0	f	2025-10-01 10:35:19.841671+01	0	16.093416229750414	2	17	2	59	24
1679	0	Needs improvement	2025-10-01 10:35:19.857415+01	0	f	2025-10-01 10:35:19.857415+01	37.361960218264905	37.361960218264905	3	17	2	59	24
1680	0	Excellent work!	2025-10-01 10:35:19.873925+01	2.3	t	2025-10-01 10:35:19.873926+01	69.93343700867413	69.93343700867413	4	17	2	59	24
1681	27.647195017135545	Excellent work!	2025-10-01 10:35:19.88961+01	0	f	2025-10-01 10:35:19.88961+01	0	27.647195017135545	1	4	1	60	4
1682	27.646790502419254	Excellent work!	2025-10-01 10:35:19.905778+01	0	f	2025-10-01 10:35:19.905779+01	0	27.646790502419254	2	4	1	60	4
1683	0	Good performance	2025-10-01 10:35:19.921474+01	1.3	t	2025-10-01 10:35:19.921475+01	51.164771277890445	51.164771277890445	3	4	1	60	4
1684	0	Satisfactory	2025-10-01 10:35:19.9368+01	0	f	2025-10-01 10:35:19.936801+01	42.38426197537948	42.38426197537948	4	4	1	60	4
1685	19.19776763704458	Satisfactory	2025-10-01 10:35:19.952436+01	0	f	2025-10-01 10:35:19.952437+01	0	19.19776763704458	1	4	2	60	4
1686	17.22257475713555	Needs improvement	2025-10-01 10:35:19.967652+01	0	f	2025-10-01 10:35:19.967652+01	0	17.22257475713555	2	4	2	60	4
1687	0	Needs improvement	2025-10-01 10:35:19.983553+01	0	f	2025-10-01 10:35:19.983553+01	37.939931838138904	37.939931838138904	3	4	2	60	4
1688	0	Satisfactory	2025-10-01 10:35:19.999082+01	1	f	2025-10-01 10:35:19.999082+01	46.19394349015904	46.19394349015904	4	4	2	60	4
1689	26.40989850483446	Excellent work!	2025-10-01 10:35:20.016756+01	0	f	2025-10-01 10:35:20.016756+01	0	26.40989850483446	1	8	1	60	9
1690	17.510751384728163	Needs improvement	2025-10-01 10:35:20.031818+01	0	f	2025-10-01 10:35:20.031819+01	0	17.510751384728163	2	8	1	60	9
1691	0	Excellent work!	2025-10-01 10:35:20.048194+01	2	t	2025-10-01 10:35:20.048195+01	63.859610526485234	63.859610526485234	3	8	1	60	9
1692	0	Excellent work!	2025-10-01 10:35:20.064226+01	2.3	t	2025-10-01 10:35:20.064227+01	68.25219720977333	68.25219720977333	4	8	1	60	9
1693	22.06174578024543	Good performance	2025-10-01 10:35:20.081505+01	0	f	2025-10-01 10:35:20.081505+01	0	22.06174578024543	1	8	2	60	9
1694	3.9477761448624813	Requires significant improvement	2025-10-01 10:35:20.097307+01	0	f	2025-10-01 10:35:20.097308+01	0	3.9477761448624813	2	8	2	60	9
1695	0	Good performance	2025-10-01 10:35:20.124523+01	1.7	t	2025-10-01 10:35:20.124524+01	58.315039106881855	58.315039106881855	3	8	2	60	9
1696	0	Needs improvement	2025-10-01 10:35:20.141204+01	0	f	2025-10-01 10:35:20.141205+01	41.410362685797864	41.410362685797864	4	8	2	60	9
1697	6.182721622697349	Requires significant improvement	2025-10-01 10:35:20.157043+01	0	f	2025-10-01 10:35:20.157044+01	0	6.182721622697349	1	11	1	60	14
1698	18.007879498540554	Satisfactory	2025-10-01 10:35:20.182239+01	0	f	2025-10-01 10:35:20.18224+01	0	18.007879498540554	2	11	1	60	14
1699	0	Satisfactory	2025-10-01 10:35:20.197985+01	1	f	2025-10-01 10:35:20.197985+01	48.38640163034054	48.38640163034054	3	11	1	60	14
1700	0	Excellent work!	2025-10-01 10:35:20.215463+01	2	t	2025-10-01 10:35:20.215464+01	62.12995164197791	62.12995164197791	4	11	1	60	14
1701	18.603296418499617	Satisfactory	2025-10-01 10:35:20.232451+01	0	f	2025-10-01 10:35:20.232452+01	0	18.603296418499617	1	11	2	60	14
1702	4.909540549518935	Requires significant improvement	2025-10-01 10:35:20.249724+01	0	f	2025-10-01 10:35:20.249724+01	0	4.909540549518935	2	11	2	60	14
1703	0	Good performance	2025-10-01 10:35:20.26566+01	1.7	t	2025-10-01 10:35:20.265661+01	55.50969964663569	55.50969964663569	3	11	2	60	14
1704	0	Good performance	2025-10-01 10:35:20.281649+01	1.7	t	2025-10-01 10:35:20.28165+01	57.24192690331324	57.24192690331324	4	11	2	60	14
1705	0.7088621178015131	Requires significant improvement	2025-10-01 10:35:20.297518+01	0	f	2025-10-01 10:35:20.297518+01	0	0.7088621178015131	1	14	1	60	19
1706	12.148667236468697	Requires significant improvement	2025-10-01 10:35:20.313509+01	0	f	2025-10-01 10:35:20.313509+01	0	12.148667236468697	2	14	1	60	19
1707	0	Requires significant improvement	2025-10-01 10:35:20.32922+01	0	f	2025-10-01 10:35:20.329221+01	4.216405701412395	4.216405701412395	3	14	1	60	19
1708	0	Satisfactory	2025-10-01 10:35:20.347631+01	1	f	2025-10-01 10:35:20.347631+01	47.88368351170196	47.88368351170196	4	14	1	60	19
1709	22.027049111715364	Good performance	2025-10-01 10:35:20.363301+01	0	f	2025-10-01 10:35:20.363301+01	0	22.027049111715364	1	14	2	60	19
1710	20.268891510854104	Satisfactory	2025-10-01 10:35:20.380834+01	0	f	2025-10-01 10:35:20.380835+01	0	20.268891510854104	2	14	2	60	19
1711	0	Good performance	2025-10-01 10:35:20.39684+01	1.7	t	2025-10-01 10:35:20.396841+01	58.37435189043239	58.37435189043239	3	14	2	60	19
1712	0	Excellent work!	2025-10-01 10:35:20.413446+01	2	t	2025-10-01 10:35:20.413447+01	60.173411220925736	60.173411220925736	4	14	2	60	19
1713	26.03870325626848	Excellent work!	2025-10-01 10:35:20.429534+01	0	f	2025-10-01 10:35:20.429535+01	0	26.03870325626848	1	17	1	60	24
1714	22.575333147327537	Good performance	2025-10-01 10:35:20.446001+01	0	f	2025-10-01 10:35:20.446001+01	0	22.575333147327537	2	17	1	60	24
1715	0	Good performance	2025-10-01 10:35:20.462174+01	1.3	t	2025-10-01 10:35:20.462175+01	54.95876320524896	54.95876320524896	3	17	1	60	24
1716	0	Excellent work!	2025-10-01 10:35:20.478391+01	2	t	2025-10-01 10:35:20.478391+01	61.23383463411873	61.23383463411873	4	17	1	60	24
1717	23.291651173384924	Good performance	2025-10-01 10:35:20.494222+01	0	f	2025-10-01 10:35:20.494222+01	0	23.291651173384924	1	17	2	60	24
1718	5.124563318107874	Requires significant improvement	2025-10-01 10:35:20.511566+01	0	f	2025-10-01 10:35:20.511567+01	0	5.124563318107874	2	17	2	60	24
1719	0	Requires significant improvement	2025-10-01 10:35:20.530732+01	0	f	2025-10-01 10:35:20.530733+01	3.6435993564537403	3.6435993564537403	3	17	2	60	24
1720	0	Needs improvement	2025-10-01 10:35:20.547806+01	0	f	2025-10-01 10:35:20.547807+01	39.98961497924337	39.98961497924337	4	17	2	60	24
1721	19.76388550946517	Satisfactory	2025-10-01 10:35:20.564151+01	0	f	2025-10-01 10:35:20.564152+01	0	19.76388550946517	1	4	1	61	4
1722	23.858465365659974	Good performance	2025-10-01 10:35:20.580718+01	0	f	2025-10-01 10:35:20.580719+01	0	23.858465365659974	2	4	1	61	4
1723	0	Satisfactory	2025-10-01 10:35:20.597572+01	0	f	2025-10-01 10:35:20.597573+01	44.08832704087462	44.08832704087462	3	4	1	61	4
1724	0	Satisfactory	2025-10-01 10:35:20.614388+01	0	f	2025-10-01 10:35:20.614389+01	42.15952321565116	42.15952321565116	4	4	1	61	4
1725	29.3686777089704	Excellent work!	2025-10-01 10:35:20.630095+01	0	f	2025-10-01 10:35:20.630095+01	0	29.3686777089704	1	4	2	61	4
1726	29.75441605509735	Excellent work!	2025-10-01 10:35:20.647039+01	0	f	2025-10-01 10:35:20.64704+01	0	29.75441605509735	2	4	2	61	4
1727	0	Satisfactory	2025-10-01 10:35:20.66389+01	0	f	2025-10-01 10:35:20.66389+01	44.97878733021375	44.97878733021375	3	4	2	61	4
1728	0	Good performance	2025-10-01 10:35:20.680962+01	1.3	t	2025-10-01 10:35:20.680962+01	53.454527368450165	53.454527368450165	4	4	2	61	4
1729	20.077673597154583	Satisfactory	2025-10-01 10:35:20.696456+01	0	f	2025-10-01 10:35:20.696456+01	0	20.077673597154583	1	8	1	61	9
1730	24.68086865177365	Good performance	2025-10-01 10:35:20.712875+01	0	f	2025-10-01 10:35:20.712875+01	0	24.68086865177365	2	8	1	61	9
1731	0	Satisfactory	2025-10-01 10:35:20.728792+01	1	f	2025-10-01 10:35:20.728793+01	46.1917993756715	46.1917993756715	3	8	1	61	9
1732	0	Excellent work!	2025-10-01 10:35:20.745055+01	2	t	2025-10-01 10:35:20.745056+01	64.69606086680355	64.69606086680355	4	8	1	61	9
1733	26.023223081294685	Excellent work!	2025-10-01 10:35:20.761269+01	0	f	2025-10-01 10:35:20.76127+01	0	26.023223081294685	1	8	2	61	9
1734	28.52203570039821	Excellent work!	2025-10-01 10:35:20.777541+01	0	f	2025-10-01 10:35:20.777541+01	0	28.52203570039821	2	8	2	61	9
1735	0	Satisfactory	2025-10-01 10:35:20.793352+01	0	f	2025-10-01 10:35:20.793353+01	44.003249078038344	44.003249078038344	3	8	2	61	9
1736	0	Requires significant improvement	2025-10-01 10:35:20.810425+01	0	f	2025-10-01 10:35:20.810425+01	29.30539183957253	29.30539183957253	4	8	2	61	9
1737	9.357875929587731	Requires significant improvement	2025-10-01 10:35:20.827262+01	0	f	2025-10-01 10:35:20.827262+01	0	9.357875929587731	1	11	1	61	14
1738	8.487160399383729	Requires significant improvement	2025-10-01 10:35:20.844248+01	0	f	2025-10-01 10:35:20.844248+01	0	8.487160399383729	2	11	1	61	14
1739	0	Excellent work!	2025-10-01 10:35:20.860558+01	2.3	t	2025-10-01 10:35:20.860558+01	67.51568595783338	67.51568595783338	3	11	1	61	14
1740	0	Needs improvement	2025-10-01 10:35:20.877463+01	0	f	2025-10-01 10:35:20.877464+01	40.60492108526825	40.60492108526825	4	11	1	61	14
1741	27.299254276868748	Excellent work!	2025-10-01 10:35:20.893679+01	0	f	2025-10-01 10:35:20.893679+01	0	27.299254276868748	1	11	2	61	14
1742	17.995074447707346	Needs improvement	2025-10-01 10:35:20.910624+01	0	f	2025-10-01 10:35:20.910624+01	0	17.995074447707346	2	11	2	61	14
1743	0	Needs improvement	2025-10-01 10:35:20.926699+01	0	f	2025-10-01 10:35:20.9267+01	38.657564404707315	38.657564404707315	3	11	2	61	14
1744	0	Good performance	2025-10-01 10:35:20.943416+01	1.3	t	2025-10-01 10:35:20.943417+01	50.801040487717174	50.801040487717174	4	11	2	61	14
1745	29.667329113427673	Excellent work!	2025-10-01 10:35:20.959354+01	0	f	2025-10-01 10:35:20.959354+01	0	29.667329113427673	1	14	1	61	19
1746	10.943385489180505	Requires significant improvement	2025-10-01 10:35:20.976266+01	0	f	2025-10-01 10:35:20.976267+01	0	10.943385489180505	2	14	1	61	19
1747	0	Needs improvement	2025-10-01 10:35:20.992435+01	0	f	2025-10-01 10:35:20.992435+01	38.42327272696864	38.42327272696864	3	14	1	61	19
1748	0	Satisfactory	2025-10-01 10:35:21.009129+01	0	f	2025-10-01 10:35:21.009129+01	42.817928534154994	42.817928534154994	4	14	1	61	19
1749	23.234870613534248	Good performance	2025-10-01 10:35:21.027565+01	0	f	2025-10-01 10:35:21.027566+01	0	23.234870613534248	1	14	2	61	19
1750	18.387656886315643	Satisfactory	2025-10-01 10:35:21.044574+01	0	f	2025-10-01 10:35:21.044574+01	0	18.387656886315643	2	14	2	61	19
1751	0	Satisfactory	2025-10-01 10:35:21.06118+01	1	f	2025-10-01 10:35:21.061181+01	47.217015327659766	47.217015327659766	3	14	2	61	19
1752	0	Excellent work!	2025-10-01 10:35:21.078259+01	2	t	2025-10-01 10:35:21.07826+01	64.67601935654858	64.67601935654858	4	14	2	61	19
1753	25.930961985398856	Excellent work!	2025-10-01 10:35:21.094531+01	0	f	2025-10-01 10:35:21.094531+01	0	25.930961985398856	1	17	1	61	24
1754	10.598888462381494	Requires significant improvement	2025-10-01 10:35:21.11214+01	0	f	2025-10-01 10:35:21.112141+01	0	10.598888462381494	2	17	1	61	24
1755	0	Good performance	2025-10-01 10:35:21.12869+01	1.7	t	2025-10-01 10:35:21.12869+01	59.481951276497725	59.481951276497725	3	17	1	61	24
1756	0	Needs improvement	2025-10-01 10:35:21.145565+01	0	f	2025-10-01 10:35:21.145565+01	35.16728402506059	35.16728402506059	4	17	1	61	24
1757	20.554776858749975	Satisfactory	2025-10-01 10:35:21.16165+01	0	f	2025-10-01 10:35:21.161651+01	0	20.554776858749975	1	17	2	61	24
1758	18.90885907220886	Satisfactory	2025-10-01 10:35:21.178868+01	0	f	2025-10-01 10:35:21.178869+01	0	18.90885907220886	2	17	2	61	24
1759	0	Requires significant improvement	2025-10-01 10:35:21.195351+01	0	f	2025-10-01 10:35:21.195352+01	28.38039808179481	28.38039808179481	3	17	2	61	24
1760	0	Requires significant improvement	2025-10-01 10:35:21.21239+01	0	f	2025-10-01 10:35:21.21239+01	34.12492603506733	34.12492603506733	4	17	2	61	24
1761	15.234066187473248	Needs improvement	2025-10-01 10:35:21.228896+01	0	f	2025-10-01 10:35:21.228896+01	0	15.234066187473248	1	4	1	62	4
1762	27.49761302824693	Excellent work!	2025-10-01 10:35:21.245887+01	0	f	2025-10-01 10:35:21.245887+01	0	27.49761302824693	2	4	1	62	4
1763	0	Excellent work!	2025-10-01 10:35:21.262113+01	2	t	2025-10-01 10:35:21.262113+01	61.46222250296573	61.46222250296573	3	4	1	62	4
1764	0	Requires significant improvement	2025-10-01 10:35:21.279287+01	0	f	2025-10-01 10:35:21.279288+01	1.019990877436388	1.019990877436388	4	4	1	62	4
1765	26.388967940365752	Excellent work!	2025-10-01 10:35:21.295574+01	0	f	2025-10-01 10:35:21.295575+01	0	26.388967940365752	1	4	2	62	4
1766	18.49330516047777	Satisfactory	2025-10-01 10:35:21.313148+01	0	f	2025-10-01 10:35:21.313149+01	0	18.49330516047777	2	4	2	62	4
1767	0	Needs improvement	2025-10-01 10:35:21.32958+01	0	f	2025-10-01 10:35:21.329581+01	36.7901038032816	36.7901038032816	3	4	2	62	4
1768	0	Good performance	2025-10-01 10:35:21.346313+01	1	f	2025-10-01 10:35:21.346313+01	49.77799941277519	49.77799941277519	4	4	2	62	4
1769	25.864530010207172	Excellent work!	2025-10-01 10:35:21.362551+01	0	f	2025-10-01 10:35:21.362551+01	0	25.864530010207172	1	8	1	62	9
1770	15.337761459827536	Needs improvement	2025-10-01 10:35:21.379458+01	0	f	2025-10-01 10:35:21.379459+01	0	15.337761459827536	2	8	1	62	9
1771	0	Requires significant improvement	2025-10-01 10:35:21.395454+01	0	f	2025-10-01 10:35:21.395454+01	30.895642041066413	30.895642041066413	3	8	1	62	9
1772	0	Good performance	2025-10-01 10:35:21.412925+01	1.7	t	2025-10-01 10:35:21.412925+01	55.154937312896706	55.154937312896706	4	8	1	62	9
1773	27.72920451064751	Excellent work!	2025-10-01 10:35:21.429675+01	0	f	2025-10-01 10:35:21.429676+01	0	27.72920451064751	1	8	2	62	9
1774	29.87014450703653	Excellent work!	2025-10-01 10:35:21.447601+01	0	f	2025-10-01 10:35:21.447601+01	0	29.87014450703653	2	8	2	62	9
1775	0	Good performance	2025-10-01 10:35:21.463817+01	1.3	t	2025-10-01 10:35:21.463817+01	53.058390377320634	53.058390377320634	3	8	2	62	9
1776	0	Requires significant improvement	2025-10-01 10:35:21.48106+01	0	f	2025-10-01 10:35:21.481061+01	21.911358561319506	21.911358561319506	4	8	2	62	9
1777	19.06777431083098	Satisfactory	2025-10-01 10:35:21.497958+01	0	f	2025-10-01 10:35:21.497958+01	0	19.06777431083098	1	11	1	62	14
1778	25.544933707398517	Excellent work!	2025-10-01 10:35:21.515049+01	0	f	2025-10-01 10:35:21.515049+01	0	25.544933707398517	2	11	1	62	14
1779	0	Excellent work!	2025-10-01 10:35:21.533073+01	2	t	2025-10-01 10:35:21.533074+01	63.56889794387689	63.56889794387689	3	11	1	62	14
1780	0	Requires significant improvement	2025-10-01 10:35:21.550011+01	0	f	2025-10-01 10:35:21.550012+01	32.66266978058502	32.66266978058502	4	11	1	62	14
1781	23.66480659344433	Good performance	2025-10-01 10:35:21.566774+01	0	f	2025-10-01 10:35:21.566775+01	0	23.66480659344433	1	11	2	62	14
1782	25.257615161059846	Good performance	2025-10-01 10:35:21.58443+01	0	f	2025-10-01 10:35:21.584431+01	0	25.257615161059846	2	11	2	62	14
1783	0	Requires significant improvement	2025-10-01 10:35:21.628112+01	0	f	2025-10-01 10:35:21.628115+01	0.2097565704778792	0.2097565704778792	3	11	2	62	14
1784	0	Requires significant improvement	2025-10-01 10:35:21.678319+01	0	f	2025-10-01 10:35:21.67832+01	34.899055532884354	34.899055532884354	4	11	2	62	14
1785	21.25818376264737	Good performance	2025-10-01 10:35:21.713456+01	0	f	2025-10-01 10:35:21.713457+01	0	21.25818376264737	1	14	1	62	19
1786	26.369298875289335	Excellent work!	2025-10-01 10:35:21.742785+01	0	f	2025-10-01 10:35:21.742786+01	0	26.369298875289335	2	14	1	62	19
1787	0	Good performance	2025-10-01 10:35:21.760209+01	1	f	2025-10-01 10:35:21.760209+01	49.03248903046003	49.03248903046003	3	14	1	62	19
1788	0	Excellent work!	2025-10-01 10:35:21.78861+01	2.3	t	2025-10-01 10:35:21.788612+01	68.49769619186996	68.49769619186996	4	14	1	62	19
1789	28.899786881272775	Excellent work!	2025-10-01 10:35:21.807999+01	0	f	2025-10-01 10:35:21.808+01	0	28.899786881272775	1	14	2	62	19
1790	4.828483762918837	Requires significant improvement	2025-10-01 10:35:21.826145+01	0	f	2025-10-01 10:35:21.826146+01	0	4.828483762918837	2	14	2	62	19
1791	0	Excellent work!	2025-10-01 10:35:21.843196+01	2	t	2025-10-01 10:35:21.843197+01	61.018264747137415	61.018264747137415	3	14	2	62	19
1792	0	Excellent work!	2025-10-01 10:35:21.859909+01	2	t	2025-10-01 10:35:21.85991+01	64.42370095864138	64.42370095864138	4	14	2	62	19
1793	29.730984845313564	Excellent work!	2025-10-01 10:35:21.878253+01	0	f	2025-10-01 10:35:21.878253+01	0	29.730984845313564	1	17	1	62	24
1794	11.802090426891999	Requires significant improvement	2025-10-01 10:35:21.895144+01	0	f	2025-10-01 10:35:21.895145+01	0	11.802090426891999	2	17	1	62	24
1795	0	Excellent work!	2025-10-01 10:35:21.913368+01	2	t	2025-10-01 10:35:21.913369+01	63.89471214973116	63.89471214973116	3	17	1	62	24
1796	0	Good performance	2025-10-01 10:35:21.929793+01	1.3	t	2025-10-01 10:35:21.929793+01	52.64351946905789	52.64351946905789	4	17	1	62	24
1797	17.925106004786976	Needs improvement	2025-10-01 10:35:21.94785+01	0	f	2025-10-01 10:35:21.947851+01	0	17.925106004786976	1	17	2	62	24
1798	29.124971999550283	Excellent work!	2025-10-01 10:35:21.964667+01	0	f	2025-10-01 10:35:21.964667+01	0	29.124971999550283	2	17	2	62	24
1799	0	Requires significant improvement	2025-10-01 10:35:21.981896+01	0	f	2025-10-01 10:35:21.981897+01	34.949367288413505	34.949367288413505	3	17	2	62	24
1800	0	Excellent work!	2025-10-01 10:35:21.998667+01	2	t	2025-10-01 10:35:21.998667+01	61.087182827435484	61.087182827435484	4	17	2	62	24
1801	5.045835816613063	Requires significant improvement	2025-10-01 10:35:22.015675+01	0	f	2025-10-01 10:35:22.015675+01	0	5.045835816613063	1	5	1	63	5
1802	23.077737210433853	Good performance	2025-10-01 10:35:22.034251+01	0	f	2025-10-01 10:35:22.034251+01	0	23.077737210433853	2	5	1	63	5
1803	0	Satisfactory	2025-10-01 10:35:22.051865+01	1	f	2025-10-01 10:35:22.051865+01	48.31300109243102	48.31300109243102	3	5	1	63	5
1804	0	Needs improvement	2025-10-01 10:35:22.069433+01	0	f	2025-10-01 10:35:22.069434+01	41.414364904329354	41.414364904329354	4	5	1	63	5
1805	20.300023670722176	Satisfactory	2025-10-01 10:35:22.087168+01	0	f	2025-10-01 10:35:22.087169+01	0	20.300023670722176	1	5	2	63	5
1806	26.328906017564375	Excellent work!	2025-10-01 10:35:22.103542+01	0	f	2025-10-01 10:35:22.103543+01	0	26.328906017564375	2	5	2	63	5
1807	0	Good performance	2025-10-01 10:35:22.120061+01	1.3	t	2025-10-01 10:35:22.120061+01	52.76887253422544	52.76887253422544	3	5	2	63	5
1808	0	Needs improvement	2025-10-01 10:35:22.137332+01	0	f	2025-10-01 10:35:22.137332+01	37.199377384712996	37.199377384712996	4	5	2	63	5
1809	26.335150040736135	Excellent work!	2025-10-01 10:35:22.153938+01	0	f	2025-10-01 10:35:22.153939+01	0	26.335150040736135	1	8	1	63	10
1810	23.064203721025976	Good performance	2025-10-01 10:35:22.171243+01	0	f	2025-10-01 10:35:22.171244+01	0	23.064203721025976	2	8	1	63	10
1811	0	Satisfactory	2025-10-01 10:35:22.188445+01	0	f	2025-10-01 10:35:22.188446+01	42.6875930443767	42.6875930443767	3	8	1	63	10
1812	0	Satisfactory	2025-10-01 10:35:22.206225+01	1	f	2025-10-01 10:35:22.206225+01	45.55447142243999	45.55447142243999	4	8	1	63	10
1813	29.075092409994248	Excellent work!	2025-10-01 10:35:22.223382+01	0	f	2025-10-01 10:35:22.223383+01	0	29.075092409994248	1	8	2	63	10
1814	21.555039565709592	Good performance	2025-10-01 10:35:22.241188+01	0	f	2025-10-01 10:35:22.241189+01	0	21.555039565709592	2	8	2	63	10
1815	0	Requires significant improvement	2025-10-01 10:35:22.258605+01	0	f	2025-10-01 10:35:22.258605+01	11.60071510033136	11.60071510033136	3	8	2	63	10
1816	0	Satisfactory	2025-10-01 10:35:22.276315+01	0	f	2025-10-01 10:35:22.276316+01	43.74604544431886	43.74604544431886	4	8	2	63	10
1817	18.064171797345185	Satisfactory	2025-10-01 10:35:22.293359+01	0	f	2025-10-01 10:35:22.29336+01	0	18.064171797345185	1	11	1	63	15
1818	27.062902729209803	Excellent work!	2025-10-01 10:35:22.311469+01	0	f	2025-10-01 10:35:22.311469+01	0	27.062902729209803	2	11	1	63	15
1819	0	Good performance	2025-10-01 10:35:22.328624+01	1.3	t	2025-10-01 10:35:22.328624+01	50.41985768671897	50.41985768671897	3	11	1	63	15
1820	0	Excellent work!	2025-10-01 10:35:22.346631+01	2.3	t	2025-10-01 10:35:22.346632+01	67.9582707798418	67.9582707798418	4	11	1	63	15
1821	27.87740899129102	Excellent work!	2025-10-01 10:35:22.363997+01	0	f	2025-10-01 10:35:22.363997+01	0	27.87740899129102	1	11	2	63	15
1822	25.6917347716412	Excellent work!	2025-10-01 10:35:22.382389+01	0	f	2025-10-01 10:35:22.382389+01	0	25.6917347716412	2	11	2	63	15
1823	0	Excellent work!	2025-10-01 10:35:22.3994+01	2.3	t	2025-10-01 10:35:22.3994+01	68.39423274128836	68.39423274128836	3	11	2	63	15
1824	0	Satisfactory	2025-10-01 10:35:22.417554+01	0	f	2025-10-01 10:35:22.417555+01	43.51808125507023	43.51808125507023	4	11	2	63	15
1825	16.131143490220158	Needs improvement	2025-10-01 10:35:22.434721+01	0	f	2025-10-01 10:35:22.434721+01	0	16.131143490220158	1	14	1	63	20
1826	25.154582315928057	Good performance	2025-10-01 10:35:22.452631+01	0	f	2025-10-01 10:35:22.452632+01	0	25.154582315928057	2	14	1	63	20
1827	0	Requires significant improvement	2025-10-01 10:35:22.47035+01	0	f	2025-10-01 10:35:22.470351+01	25.729613225603924	25.729613225603924	3	14	1	63	20
1828	0	Good performance	2025-10-01 10:35:22.487544+01	1	f	2025-10-01 10:35:22.487545+01	49.93840940051268	49.93840940051268	4	14	1	63	20
1829	20.913911720079746	Satisfactory	2025-10-01 10:35:22.50544+01	0	f	2025-10-01 10:35:22.505441+01	0	20.913911720079746	1	14	2	63	20
1830	19.330800159763474	Satisfactory	2025-10-01 10:35:22.523318+01	0	f	2025-10-01 10:35:22.523318+01	0	19.330800159763474	2	14	2	63	20
1831	0	Excellent work!	2025-10-01 10:35:22.542765+01	2	t	2025-10-01 10:35:22.542766+01	64.56948718089538	64.56948718089538	3	14	2	63	20
1832	0	Satisfactory	2025-10-01 10:35:22.561068+01	1	f	2025-10-01 10:35:22.561069+01	48.49121479019382	48.49121479019382	4	14	2	63	20
1833	23.696631607754604	Good performance	2025-10-01 10:35:22.579433+01	0	f	2025-10-01 10:35:22.579433+01	0	23.696631607754604	1	17	1	63	25
1834	22.123698463913733	Good performance	2025-10-01 10:35:22.597639+01	0	f	2025-10-01 10:35:22.597639+01	0	22.123698463913733	2	17	1	63	25
1835	0	Needs improvement	2025-10-01 10:35:22.615661+01	0	f	2025-10-01 10:35:22.615661+01	41.1765973358045	41.1765973358045	3	17	1	63	25
1836	0	Excellent work!	2025-10-01 10:35:22.633088+01	2.3	t	2025-10-01 10:35:22.633089+01	65.24621781081568	65.24621781081568	4	17	1	63	25
1837	19.334973038904835	Satisfactory	2025-10-01 10:35:22.650888+01	0	f	2025-10-01 10:35:22.650889+01	0	19.334973038904835	1	17	2	63	25
1838	17.21677752189114	Needs improvement	2025-10-01 10:35:22.667673+01	0	f	2025-10-01 10:35:22.667673+01	0	17.21677752189114	2	17	2	63	25
1839	0	Excellent work!	2025-10-01 10:35:22.685449+01	2.3	t	2025-10-01 10:35:22.68545+01	68.4456230407598	68.4456230407598	3	17	2	63	25
1840	0	Good performance	2025-10-01 10:35:22.703026+01	1.7	t	2025-10-01 10:35:22.703027+01	57.81673609591935	57.81673609591935	4	17	2	63	25
1841	25.127113960351977	Good performance	2025-10-01 10:35:22.721229+01	0	f	2025-10-01 10:35:22.72123+01	0	25.127113960351977	1	5	1	64	5
1842	20.01539777963615	Satisfactory	2025-10-01 10:35:22.739697+01	0	f	2025-10-01 10:35:22.739698+01	0	20.01539777963615	2	5	1	64	5
1843	0	Excellent work!	2025-10-01 10:35:22.75664+01	2.3	t	2025-10-01 10:35:22.75664+01	68.68254658737939	68.68254658737939	3	5	1	64	5
1844	0	Requires significant improvement	2025-10-01 10:35:22.774732+01	0	f	2025-10-01 10:35:22.774733+01	12.719860707353405	12.719860707353405	4	5	1	64	5
1845	16.515361716930013	Needs improvement	2025-10-01 10:35:22.791809+01	0	f	2025-10-01 10:35:22.79181+01	0	16.515361716930013	1	5	2	64	5
1846	16.38048532868377	Needs improvement	2025-10-01 10:35:22.809938+01	0	f	2025-10-01 10:35:22.809939+01	0	16.38048532868377	2	5	2	64	5
1847	0	Needs improvement	2025-10-01 10:35:22.828566+01	0	f	2025-10-01 10:35:22.828567+01	36.28757663476162	36.28757663476162	3	5	2	64	5
1848	0	Excellent work!	2025-10-01 10:35:22.848573+01	2.3	t	2025-10-01 10:35:22.848573+01	68.45669171529306	68.45669171529306	4	5	2	64	5
1849	20.132252032150618	Satisfactory	2025-10-01 10:35:22.865711+01	0	f	2025-10-01 10:35:22.865711+01	0	20.132252032150618	1	8	1	64	10
1850	8.495068307602116	Requires significant improvement	2025-10-01 10:35:22.883296+01	0	f	2025-10-01 10:35:22.883296+01	0	8.495068307602116	2	8	1	64	10
1851	0	Good performance	2025-10-01 10:35:22.9005+01	1.3	t	2025-10-01 10:35:22.900501+01	52.504902370993904	52.504902370993904	3	8	1	64	10
1852	0	Excellent work!	2025-10-01 10:35:22.918256+01	2.3	t	2025-10-01 10:35:22.918257+01	67.50270471198172	67.50270471198172	4	8	1	64	10
1853	17.01612191062135	Needs improvement	2025-10-01 10:35:22.935546+01	0	f	2025-10-01 10:35:22.935547+01	0	17.01612191062135	1	8	2	64	10
1854	26.65942129670639	Excellent work!	2025-10-01 10:35:22.953433+01	0	f	2025-10-01 10:35:22.953434+01	0	26.65942129670639	2	8	2	64	10
1855	0	Good performance	2025-10-01 10:35:22.971167+01	1.3	t	2025-10-01 10:35:22.971168+01	52.63729894788104	52.63729894788104	3	8	2	64	10
1856	0	Good performance	2025-10-01 10:35:22.989224+01	1.7	t	2025-10-01 10:35:22.989224+01	58.27246073469567	58.27246073469567	4	8	2	64	10
1857	17.58624515168337	Needs improvement	2025-10-01 10:35:23.007448+01	0	f	2025-10-01 10:35:23.007448+01	0	17.58624515168337	1	11	1	64	15
1858	18.62631463483171	Satisfactory	2025-10-01 10:35:23.025464+01	0	f	2025-10-01 10:35:23.025465+01	0	18.62631463483171	2	11	1	64	15
1859	0	Good performance	2025-10-01 10:35:23.04479+01	1.3	t	2025-10-01 10:35:23.044791+01	51.33577135140628	51.33577135140628	3	11	1	64	15
1860	0	Excellent work!	2025-10-01 10:35:23.062327+01	2	t	2025-10-01 10:35:23.062328+01	63.362455366198816	63.362455366198816	4	11	1	64	15
1861	25.728766905576574	Excellent work!	2025-10-01 10:35:23.08134+01	0	f	2025-10-01 10:35:23.08134+01	0	25.728766905576574	1	11	2	64	15
1862	2.7799459497880843	Requires significant improvement	2025-10-01 10:35:23.098545+01	0	f	2025-10-01 10:35:23.098546+01	0	2.7799459497880843	2	11	2	64	15
1863	0	Requires significant improvement	2025-10-01 10:35:23.117242+01	0	f	2025-10-01 10:35:23.117243+01	7.986677954411335	7.986677954411335	3	11	2	64	15
1864	0	Excellent work!	2025-10-01 10:35:23.135041+01	2	t	2025-10-01 10:35:23.135042+01	60.24244168406555	60.24244168406555	4	11	2	64	15
1865	27.78947271146926	Excellent work!	2025-10-01 10:35:23.153264+01	0	f	2025-10-01 10:35:23.153265+01	0	27.78947271146926	1	14	1	64	20
1866	17.614716473433198	Needs improvement	2025-10-01 10:35:23.17067+01	0	f	2025-10-01 10:35:23.17067+01	0	17.614716473433198	2	14	1	64	20
1867	0	Excellent work!	2025-10-01 10:35:23.193329+01	2.3	t	2025-10-01 10:35:23.19333+01	69.35130939139097	69.35130939139097	3	14	1	64	20
1868	0	Satisfactory	2025-10-01 10:35:23.21222+01	0	f	2025-10-01 10:35:23.212221+01	42.140481129787645	42.140481129787645	4	14	1	64	20
1869	24.890469968689075	Good performance	2025-10-01 10:35:23.233032+01	0	f	2025-10-01 10:35:23.233033+01	0	24.890469968689075	1	14	2	64	20
1870	8.743473444451073	Requires significant improvement	2025-10-01 10:35:23.262348+01	0	f	2025-10-01 10:35:23.262348+01	0	8.743473444451073	2	14	2	64	20
1871	0	Excellent work!	2025-10-01 10:35:23.281597+01	2	t	2025-10-01 10:35:23.281597+01	62.62754912001164	62.62754912001164	3	14	2	64	20
1872	0	Needs improvement	2025-10-01 10:35:23.299141+01	0	f	2025-10-01 10:35:23.299141+01	38.97062850472928	38.97062850472928	4	14	2	64	20
1873	9.177375595492736	Requires significant improvement	2025-10-01 10:35:23.318182+01	0	f	2025-10-01 10:35:23.318183+01	0	9.177375595492736	1	17	1	64	25
1874	13.412713143061731	Requires significant improvement	2025-10-01 10:35:23.33612+01	0	f	2025-10-01 10:35:23.33612+01	0	13.412713143061731	2	17	1	64	25
1875	0	Requires significant improvement	2025-10-01 10:35:23.35369+01	0	f	2025-10-01 10:35:23.35369+01	10.587423392770072	10.587423392770072	3	17	1	64	25
1876	0	Needs improvement	2025-10-01 10:35:23.37149+01	0	f	2025-10-01 10:35:23.37149+01	40.380827842224754	40.380827842224754	4	17	1	64	25
1877	17.95814509051935	Needs improvement	2025-10-01 10:35:23.389607+01	0	f	2025-10-01 10:35:23.389608+01	0	17.95814509051935	1	17	2	64	25
1878	7.926765050304059	Requires significant improvement	2025-10-01 10:35:23.407486+01	0	f	2025-10-01 10:35:23.407487+01	0	7.926765050304059	2	17	2	64	25
1879	0	Requires significant improvement	2025-10-01 10:35:23.425872+01	0	f	2025-10-01 10:35:23.425873+01	27.31059963482694	27.31059963482694	3	17	2	64	25
1880	0	Excellent work!	2025-10-01 10:35:23.443929+01	2.3	t	2025-10-01 10:35:23.44393+01	67.98200630231597	67.98200630231597	4	17	2	64	25
1881	12.81051906877121	Requires significant improvement	2025-10-01 10:35:23.461744+01	0	f	2025-10-01 10:35:23.461745+01	0	12.81051906877121	1	5	1	65	5
1882	27.53035879367183	Excellent work!	2025-10-01 10:35:23.479709+01	0	f	2025-10-01 10:35:23.479709+01	0	27.53035879367183	2	5	1	65	5
1883	0	Good performance	2025-10-01 10:35:23.497961+01	1	f	2025-10-01 10:35:23.497962+01	49.45688528472111	49.45688528472111	3	5	1	65	5
1884	0	Satisfactory	2025-10-01 10:35:23.515767+01	1	f	2025-10-01 10:35:23.515768+01	46.04979449728377	46.04979449728377	4	5	1	65	5
1885	24.39572677178335	Good performance	2025-10-01 10:35:23.533551+01	0	f	2025-10-01 10:35:23.533551+01	0	24.39572677178335	1	5	2	65	5
1886	29.861818341424176	Excellent work!	2025-10-01 10:35:23.552917+01	0	f	2025-10-01 10:35:23.552918+01	0	29.861818341424176	2	5	2	65	5
1887	0	Excellent work!	2025-10-01 10:35:23.571211+01	2	t	2025-10-01 10:35:23.571212+01	64.15041046711036	64.15041046711036	3	5	2	65	5
1888	0	Good performance	2025-10-01 10:35:23.589723+01	1.3	t	2025-10-01 10:35:23.589724+01	51.68599310747524	51.68599310747524	4	5	2	65	5
1889	27.89297218888059	Excellent work!	2025-10-01 10:35:23.607673+01	0	f	2025-10-01 10:35:23.607673+01	0	27.89297218888059	1	8	1	65	10
1890	21.65094255182921	Good performance	2025-10-01 10:35:23.625358+01	0	f	2025-10-01 10:35:23.625358+01	0	21.65094255182921	2	8	1	65	10
1891	0	Needs improvement	2025-10-01 10:35:23.642764+01	0	f	2025-10-01 10:35:23.642765+01	41.965162672635245	41.965162672635245	3	8	1	65	10
1892	0	Requires significant improvement	2025-10-01 10:35:23.659639+01	0	f	2025-10-01 10:35:23.659639+01	4.672271878126539	4.672271878126539	4	8	1	65	10
1893	27.14099051265844	Excellent work!	2025-10-01 10:35:23.677116+01	0	f	2025-10-01 10:35:23.677117+01	0	27.14099051265844	1	8	2	65	10
1894	25.58711939960173	Excellent work!	2025-10-01 10:35:23.694466+01	0	f	2025-10-01 10:35:23.694467+01	0	25.58711939960173	2	8	2	65	10
1895	0	Needs improvement	2025-10-01 10:35:23.712476+01	0	f	2025-10-01 10:35:23.712477+01	39.8064656192572	39.8064656192572	3	8	2	65	10
1896	0	Satisfactory	2025-10-01 10:35:23.729802+01	1	f	2025-10-01 10:35:23.729802+01	45.96715335747164	45.96715335747164	4	8	2	65	10
1897	12.989666631401025	Requires significant improvement	2025-10-01 10:35:23.748629+01	0	f	2025-10-01 10:35:23.748629+01	0	12.989666631401025	1	11	1	65	15
1898	29.831623773878412	Excellent work!	2025-10-01 10:35:23.767168+01	0	f	2025-10-01 10:35:23.767169+01	0	29.831623773878412	2	11	1	65	15
1899	0	Excellent work!	2025-10-01 10:35:23.785481+01	2	t	2025-10-01 10:35:23.785482+01	62.65791070409932	62.65791070409932	3	11	1	65	15
1900	0	Satisfactory	2025-10-01 10:35:23.80311+01	0	f	2025-10-01 10:35:23.80311+01	44.61120785412511	44.61120785412511	4	11	1	65	15
1901	24.896893421266817	Good performance	2025-10-01 10:35:23.82069+01	0	f	2025-10-01 10:35:23.820691+01	0	24.896893421266817	1	11	2	65	15
1902	16.386805571663814	Needs improvement	2025-10-01 10:35:23.839176+01	0	f	2025-10-01 10:35:23.839177+01	0	16.386805571663814	2	11	2	65	15
1903	0	Satisfactory	2025-10-01 10:35:23.856604+01	1	f	2025-10-01 10:35:23.856605+01	46.921005158764814	46.921005158764814	3	11	2	65	15
1904	0	Good performance	2025-10-01 10:35:23.874332+01	1.7	t	2025-10-01 10:35:23.874333+01	55.73904827756142	55.73904827756142	4	11	2	65	15
1905	15.202694771210886	Needs improvement	2025-10-01 10:35:23.891482+01	0	f	2025-10-01 10:35:23.891483+01	0	15.202694771210886	1	14	1	65	20
1906	26.31456102861364	Excellent work!	2025-10-01 10:35:23.909065+01	0	f	2025-10-01 10:35:23.909066+01	0	26.31456102861364	2	14	1	65	20
1907	0	Requires significant improvement	2025-10-01 10:35:23.926453+01	0	f	2025-10-01 10:35:23.926453+01	22.941706251813027	22.941706251813027	3	14	1	65	20
1908	0	Excellent work!	2025-10-01 10:35:23.943715+01	2.3	t	2025-10-01 10:35:23.943716+01	66.69912269374781	66.69912269374781	4	14	1	65	20
1909	10.237873581573064	Requires significant improvement	2025-10-01 10:35:23.961183+01	0	f	2025-10-01 10:35:23.961184+01	0	10.237873581573064	1	14	2	65	20
1910	19.051220344358704	Satisfactory	2025-10-01 10:35:23.978871+01	0	f	2025-10-01 10:35:23.978872+01	0	19.051220344358704	2	14	2	65	20
1911	0	Good performance	2025-10-01 10:35:23.995893+01	1.3	t	2025-10-01 10:35:23.995893+01	51.24106760335988	51.24106760335988	3	14	2	65	20
1912	0	Excellent work!	2025-10-01 10:35:24.013841+01	2	t	2025-10-01 10:35:24.013842+01	61.521640901244766	61.521640901244766	4	14	2	65	20
1913	28.0239438090353	Excellent work!	2025-10-01 10:35:24.032014+01	0	f	2025-10-01 10:35:24.032015+01	0	28.0239438090353	1	17	1	65	25
1914	26.381049560945634	Excellent work!	2025-10-01 10:35:24.052245+01	0	f	2025-10-01 10:35:24.052245+01	0	26.381049560945634	2	17	1	65	25
1915	0	Good performance	2025-10-01 10:35:24.070782+01	1.3	t	2025-10-01 10:35:24.070782+01	52.84453217066118	52.84453217066118	3	17	1	65	25
1916	0	Good performance	2025-10-01 10:35:24.089365+01	1.3	t	2025-10-01 10:35:24.089366+01	54.243931372088994	54.243931372088994	4	17	1	65	25
1917	19.129397793540413	Satisfactory	2025-10-01 10:35:24.107484+01	0	f	2025-10-01 10:35:24.107485+01	0	19.129397793540413	1	17	2	65	25
1918	23.0383604254162	Good performance	2025-10-01 10:35:24.124986+01	0	f	2025-10-01 10:35:24.124986+01	0	23.0383604254162	2	17	2	65	25
1919	0	Good performance	2025-10-01 10:35:24.143417+01	1.3	t	2025-10-01 10:35:24.143418+01	54.67951772295266	54.67951772295266	3	17	2	65	25
1920	0	Requires significant improvement	2025-10-01 10:35:24.160887+01	0	f	2025-10-01 10:35:24.160887+01	26.040040963011084	26.040040963011084	4	17	2	65	25
1921	17.623521435660685	Needs improvement	2025-10-01 10:35:24.179231+01	0	f	2025-10-01 10:35:24.179231+01	0	17.623521435660685	1	5	1	66	5
1922	15.403097689614462	Needs improvement	2025-10-01 10:35:24.197279+01	0	f	2025-10-01 10:35:24.19728+01	0	15.403097689614462	2	5	1	66	5
1923	0	Requires significant improvement	2025-10-01 10:35:24.21662+01	0	f	2025-10-01 10:35:24.216621+01	5.3402173009558345	5.3402173009558345	3	5	1	66	5
1924	0	Excellent work!	2025-10-01 10:35:24.234659+01	2.3	t	2025-10-01 10:35:24.23466+01	67.08135864889033	67.08135864889033	4	5	1	66	5
1925	25.894009228633337	Excellent work!	2025-10-01 10:35:24.25319+01	0	f	2025-10-01 10:35:24.25319+01	0	25.894009228633337	1	5	2	66	5
1926	28.16774784868418	Excellent work!	2025-10-01 10:35:24.271878+01	0	f	2025-10-01 10:35:24.271878+01	0	28.16774784868418	2	5	2	66	5
1927	0	Excellent work!	2025-10-01 10:35:24.289724+01	2.3	t	2025-10-01 10:35:24.289725+01	65.85659325304249	65.85659325304249	3	5	2	66	5
1928	0	Good performance	2025-10-01 10:35:24.308178+01	1.7	t	2025-10-01 10:35:24.308179+01	57.1338517386708	57.1338517386708	4	5	2	66	5
1929	21.303377823064288	Good performance	2025-10-01 10:35:24.325991+01	0	f	2025-10-01 10:35:24.325992+01	0	21.303377823064288	1	8	1	66	10
1930	26.823312044624153	Excellent work!	2025-10-01 10:35:24.345018+01	0	f	2025-10-01 10:35:24.345019+01	0	26.823312044624153	2	8	1	66	10
1931	0	Excellent work!	2025-10-01 10:35:24.362762+01	2	t	2025-10-01 10:35:24.362762+01	61.9898648868743	61.9898648868743	3	8	1	66	10
1932	0	Excellent work!	2025-10-01 10:35:24.381096+01	2.3	t	2025-10-01 10:35:24.381096+01	65.72269881002128	65.72269881002128	4	8	1	66	10
1933	16.90203629205816	Needs improvement	2025-10-01 10:35:24.398889+01	0	f	2025-10-01 10:35:24.398889+01	0	16.90203629205816	1	8	2	66	10
1934	25.249596145949372	Good performance	2025-10-01 10:35:24.417627+01	0	f	2025-10-01 10:35:24.417627+01	0	25.249596145949372	2	8	2	66	10
1935	0	Needs improvement	2025-10-01 10:35:24.436089+01	0	f	2025-10-01 10:35:24.43609+01	39.49347703359962	39.49347703359962	3	8	2	66	10
1936	0	Satisfactory	2025-10-01 10:35:24.454054+01	0	f	2025-10-01 10:35:24.454054+01	42.47213173560277	42.47213173560277	4	8	2	66	10
1937	6.802855287146991	Requires significant improvement	2025-10-01 10:35:24.472866+01	0	f	2025-10-01 10:35:24.472867+01	0	6.802855287146991	1	11	1	66	15
1938	29.346661609059925	Excellent work!	2025-10-01 10:35:24.490829+01	0	f	2025-10-01 10:35:24.49083+01	0	29.346661609059925	2	11	1	66	15
1939	0	Needs improvement	2025-10-01 10:35:24.509321+01	0	f	2025-10-01 10:35:24.509322+01	41.26844782082628	41.26844782082628	3	11	1	66	15
1940	0	Excellent work!	2025-10-01 10:35:24.527178+01	2.3	t	2025-10-01 10:35:24.527179+01	65.36211555835513	65.36211555835513	4	11	1	66	15
1941	9.421369213741833	Requires significant improvement	2025-10-01 10:35:24.547363+01	0	f	2025-10-01 10:35:24.547364+01	0	9.421369213741833	1	11	2	66	15
1942	29.229985452187464	Excellent work!	2025-10-01 10:35:24.565554+01	0	f	2025-10-01 10:35:24.565554+01	0	29.229985452187464	2	11	2	66	15
1943	0	Good performance	2025-10-01 10:35:24.583478+01	1.7	t	2025-10-01 10:35:24.583478+01	56.87325782452258	56.87325782452258	3	11	2	66	15
1944	0	Good performance	2025-10-01 10:35:24.601478+01	1.7	t	2025-10-01 10:35:24.601479+01	55.35658813810968	55.35658813810968	4	11	2	66	15
1945	18.61454032466046	Satisfactory	2025-10-01 10:35:24.620513+01	0	f	2025-10-01 10:35:24.620513+01	0	18.61454032466046	1	14	1	66	20
1946	26.743331204654666	Excellent work!	2025-10-01 10:35:24.638988+01	0	f	2025-10-01 10:35:24.638988+01	0	26.743331204654666	2	14	1	66	20
1947	0	Needs improvement	2025-10-01 10:35:24.65755+01	0	f	2025-10-01 10:35:24.657551+01	38.872213130883	38.872213130883	3	14	1	66	20
1948	0	Good performance	2025-10-01 10:35:24.675626+01	1.3	t	2025-10-01 10:35:24.675626+01	51.38249161278662	51.38249161278662	4	14	1	66	20
1949	19.81352448556872	Satisfactory	2025-10-01 10:35:24.700582+01	0	f	2025-10-01 10:35:24.700582+01	0	19.81352448556872	1	14	2	66	20
1950	10.656547208121061	Requires significant improvement	2025-10-01 10:35:24.719614+01	0	f	2025-10-01 10:35:24.719615+01	0	10.656547208121061	2	14	2	66	20
1951	0	Excellent work!	2025-10-01 10:35:24.738603+01	2	t	2025-10-01 10:35:24.738603+01	60.138495002565804	60.138495002565804	3	14	2	66	20
1952	0	Requires significant improvement	2025-10-01 10:35:24.756976+01	0	f	2025-10-01 10:35:24.756977+01	22.97963060335276	22.97963060335276	4	14	2	66	20
1953	15.887038514376945	Needs improvement	2025-10-01 10:35:24.777048+01	0	f	2025-10-01 10:35:24.777049+01	0	15.887038514376945	1	17	1	66	25
1954	20.10547156352015	Satisfactory	2025-10-01 10:35:24.797082+01	0	f	2025-10-01 10:35:24.797082+01	0	20.10547156352015	2	17	1	66	25
1955	0	Satisfactory	2025-10-01 10:35:24.816804+01	0	f	2025-10-01 10:35:24.816804+01	44.742209556932984	44.742209556932984	3	17	1	66	25
1956	0	Good performance	2025-10-01 10:35:24.835327+01	1.7	t	2025-10-01 10:35:24.835327+01	55.7450836849234	55.7450836849234	4	17	1	66	25
1957	25.036099272684762	Good performance	2025-10-01 10:35:24.853606+01	0	f	2025-10-01 10:35:24.853606+01	0	25.036099272684762	1	17	2	66	25
1958	28.19650378193451	Excellent work!	2025-10-01 10:35:24.872825+01	0	f	2025-10-01 10:35:24.872825+01	0	28.19650378193451	2	17	2	66	25
1959	0	Excellent work!	2025-10-01 10:35:24.891036+01	2.3	t	2025-10-01 10:35:24.891037+01	69.27679318094255	69.27679318094255	3	17	2	66	25
1960	0	Needs improvement	2025-10-01 10:35:24.909939+01	0	f	2025-10-01 10:35:24.909939+01	35.168518103195005	35.168518103195005	4	17	2	66	25
1961	16.62512941054635	Needs improvement	2025-10-01 10:35:24.928455+01	0	f	2025-10-01 10:35:24.928455+01	0	16.62512941054635	1	5	1	67	5
1962	23.709522352446204	Good performance	2025-10-01 10:35:24.947509+01	0	f	2025-10-01 10:35:24.947509+01	0	23.709522352446204	2	5	1	67	5
1963	0	Excellent work!	2025-10-01 10:35:24.965669+01	2	t	2025-10-01 10:35:24.965669+01	61.991785958192246	61.991785958192246	3	5	1	67	5
1964	0	Satisfactory	2025-10-01 10:35:24.985224+01	0	f	2025-10-01 10:35:24.985224+01	43.57078452053912	43.57078452053912	4	5	1	67	5
1965	19.166963160664167	Satisfactory	2025-10-01 10:35:25.004332+01	0	f	2025-10-01 10:35:25.004333+01	0	19.166963160664167	1	5	2	67	5
1966	8.17949671246892	Requires significant improvement	2025-10-01 10:35:25.022649+01	0	f	2025-10-01 10:35:25.022649+01	0	8.17949671246892	2	5	2	67	5
1967	0	Needs improvement	2025-10-01 10:35:25.041306+01	0	f	2025-10-01 10:35:25.041306+01	40.655674133761224	40.655674133761224	3	5	2	67	5
1968	0	Good performance	2025-10-01 10:35:25.060584+01	1.7	t	2025-10-01 10:35:25.060585+01	59.2705499052902	59.2705499052902	4	5	2	67	5
1969	20.067335221687788	Satisfactory	2025-10-01 10:35:25.07982+01	0	f	2025-10-01 10:35:25.079821+01	0	20.067335221687788	1	8	1	67	10
1970	27.190855064473933	Excellent work!	2025-10-01 10:35:25.098354+01	0	f	2025-10-01 10:35:25.098354+01	0	27.190855064473933	2	8	1	67	10
1971	0	Excellent work!	2025-10-01 10:35:25.117857+01	2.3	t	2025-10-01 10:35:25.117858+01	67.40332180857727	67.40332180857727	3	8	1	67	10
1972	0	Good performance	2025-10-01 10:35:25.139439+01	1.7	t	2025-10-01 10:35:25.13944+01	56.86477402346674	56.86477402346674	4	8	1	67	10
1973	21.679353564400213	Good performance	2025-10-01 10:35:25.158789+01	0	f	2025-10-01 10:35:25.15879+01	0	21.679353564400213	1	8	2	67	10
1974	16.28014608456828	Needs improvement	2025-10-01 10:35:25.177637+01	0	f	2025-10-01 10:35:25.177637+01	0	16.28014608456828	2	8	2	67	10
1975	0	Satisfactory	2025-10-01 10:35:25.196463+01	1	f	2025-10-01 10:35:25.196464+01	46.99198550243067	46.99198550243067	3	8	2	67	10
1976	0	Satisfactory	2025-10-01 10:35:25.216295+01	1	f	2025-10-01 10:35:25.216296+01	46.5256090104144	46.5256090104144	4	8	2	67	10
1977	23.470984819630665	Good performance	2025-10-01 10:35:25.235328+01	0	f	2025-10-01 10:35:25.235329+01	0	23.470984819630665	1	11	1	67	15
1978	0.04896175238520428	Requires significant improvement	2025-10-01 10:35:25.25473+01	0	f	2025-10-01 10:35:25.25473+01	0	0.04896175238520428	2	11	1	67	15
1979	0	Satisfactory	2025-10-01 10:35:25.275011+01	0	f	2025-10-01 10:35:25.275012+01	42.62183202401001	42.62183202401001	3	11	1	67	15
1980	0	Excellent work!	2025-10-01 10:35:25.293675+01	2	t	2025-10-01 10:35:25.293676+01	61.18582949204359	61.18582949204359	4	11	1	67	15
1981	29.26817607882879	Excellent work!	2025-10-01 10:35:25.313121+01	0	f	2025-10-01 10:35:25.313121+01	0	29.26817607882879	1	11	2	67	15
1982	27.127218410050354	Excellent work!	2025-10-01 10:35:25.331741+01	0	f	2025-10-01 10:35:25.331742+01	0	27.127218410050354	2	11	2	67	15
1983	0	Satisfactory	2025-10-01 10:35:25.351253+01	1	f	2025-10-01 10:35:25.351253+01	45.507903330075834	45.507903330075834	3	11	2	67	15
1984	0	Requires significant improvement	2025-10-01 10:35:25.36992+01	0	f	2025-10-01 10:35:25.369921+01	0.6891738066164765	0.6891738066164765	4	11	2	67	15
1985	17.34659808133565	Needs improvement	2025-10-01 10:35:25.389039+01	0	f	2025-10-01 10:35:25.389039+01	0	17.34659808133565	1	14	1	67	20
1986	28.127192423303224	Excellent work!	2025-10-01 10:35:25.408698+01	0	f	2025-10-01 10:35:25.408698+01	0	28.127192423303224	2	14	1	67	20
1987	0	Excellent work!	2025-10-01 10:35:25.42805+01	2.3	t	2025-10-01 10:35:25.42805+01	66.41847505784877	66.41847505784877	3	14	1	67	20
1988	0	Excellent work!	2025-10-01 10:35:25.448306+01	1.7	t	2025-10-01 10:35:25.448307+01	59.73804030299177	59.73804030299177	4	14	1	67	20
1989	9.10683005515171	Requires significant improvement	2025-10-01 10:35:25.46752+01	0	f	2025-10-01 10:35:25.46752+01	0	9.10683005515171	1	14	2	67	20
1990	22.90898997378422	Good performance	2025-10-01 10:35:25.486479+01	0	f	2025-10-01 10:35:25.48648+01	0	22.90898997378422	2	14	2	67	20
1991	0	Good performance	2025-10-01 10:35:25.505874+01	1.7	t	2025-10-01 10:35:25.505874+01	55.54406175499456	55.54406175499456	3	14	2	67	20
1992	0	Requires significant improvement	2025-10-01 10:35:25.524693+01	0	f	2025-10-01 10:35:25.524693+01	12.300501040817483	12.300501040817483	4	14	2	67	20
1993	24.04279511328747	Good performance	2025-10-01 10:35:25.544012+01	0	f	2025-10-01 10:35:25.544012+01	0	24.04279511328747	1	17	1	67	25
1994	19.40953131250473	Satisfactory	2025-10-01 10:35:25.563758+01	0	f	2025-10-01 10:35:25.563759+01	0	19.40953131250473	2	17	1	67	25
1995	0	Needs improvement	2025-10-01 10:35:25.58362+01	0	f	2025-10-01 10:35:25.583621+01	38.953076002470816	38.953076002470816	3	17	1	67	25
1996	0	Excellent work!	2025-10-01 10:35:25.603071+01	2.3	t	2025-10-01 10:35:25.603072+01	68.73143110037665	68.73143110037665	4	17	1	67	25
1997	17.281962205092295	Needs improvement	2025-10-01 10:35:25.622497+01	0	f	2025-10-01 10:35:25.622498+01	0	17.281962205092295	1	17	2	67	25
1998	29.68937554706482	Excellent work!	2025-10-01 10:35:25.641488+01	0	f	2025-10-01 10:35:25.641488+01	0	29.68937554706482	2	17	2	67	25
1999	0	Excellent work!	2025-10-01 10:35:25.660017+01	2	t	2025-10-01 10:35:25.660018+01	63.844472356996086	63.844472356996086	3	17	2	67	25
2000	0	Good performance	2025-10-01 10:35:25.679277+01	1.3	t	2025-10-01 10:35:25.679278+01	51.356555309060916	51.356555309060916	4	17	2	67	25
2001	29.657205764854794	Excellent work!	2025-10-01 10:35:25.698397+01	0	f	2025-10-01 10:35:25.698397+01	0	29.657205764854794	1	5	1	68	5
2002	29.728432079514327	Excellent work!	2025-10-01 10:35:25.71772+01	0	f	2025-10-01 10:35:25.71772+01	0	29.728432079514327	2	5	1	68	5
2003	0	Requires significant improvement	2025-10-01 10:35:25.736249+01	0	f	2025-10-01 10:35:25.73625+01	16.145388862827556	16.145388862827556	3	5	1	68	5
2004	0	Excellent work!	2025-10-01 10:35:25.755639+01	2	t	2025-10-01 10:35:25.755639+01	64.51124690207497	64.51124690207497	4	5	1	68	5
2005	2.9914492693553187	Requires significant improvement	2025-10-01 10:35:25.775095+01	0	f	2025-10-01 10:35:25.775095+01	0	2.9914492693553187	1	5	2	68	5
2006	24.490434288054548	Good performance	2025-10-01 10:35:25.794346+01	0	f	2025-10-01 10:35:25.794347+01	0	24.490434288054548	2	5	2	68	5
2007	0	Needs improvement	2025-10-01 10:35:25.814532+01	0	f	2025-10-01 10:35:25.814532+01	39.41410574644079	39.41410574644079	3	5	2	68	5
2008	0	Good performance	2025-10-01 10:35:25.834784+01	1.7	t	2025-10-01 10:35:25.834785+01	55.191343513431235	55.191343513431235	4	5	2	68	5
2009	29.688149179987175	Excellent work!	2025-10-01 10:35:25.854435+01	0	f	2025-10-01 10:35:25.854435+01	0	29.688149179987175	1	8	1	68	10
2010	27.60203318764792	Excellent work!	2025-10-01 10:35:25.874243+01	0	f	2025-10-01 10:35:25.874244+01	0	27.60203318764792	2	8	1	68	10
2011	0	Excellent work!	2025-10-01 10:35:25.893592+01	2	t	2025-10-01 10:35:25.893593+01	64.49995442551999	64.49995442551999	3	8	1	68	10
2012	0	Excellent work!	2025-10-01 10:35:25.913433+01	2.3	t	2025-10-01 10:35:25.913433+01	69.59559907310934	69.59559907310934	4	8	1	68	10
2013	28.046334194702215	Excellent work!	2025-10-01 10:35:25.932786+01	0	f	2025-10-01 10:35:25.932787+01	0	28.046334194702215	1	8	2	68	10
2014	5.497145827107179	Requires significant improvement	2025-10-01 10:35:25.95302+01	0	f	2025-10-01 10:35:25.95302+01	0	5.497145827107179	2	8	2	68	10
2015	0	Good performance	2025-10-01 10:35:25.972875+01	1.3	t	2025-10-01 10:35:25.972875+01	50.85617962081528	50.85617962081528	3	8	2	68	10
2016	0	Satisfactory	2025-10-01 10:35:25.99205+01	1	f	2025-10-01 10:35:25.992051+01	47.25510006752794	47.25510006752794	4	8	2	68	10
2017	28.99762054110023	Excellent work!	2025-10-01 10:35:26.011715+01	0	f	2025-10-01 10:35:26.011716+01	0	28.99762054110023	1	11	1	68	15
2018	6.83724413957834	Requires significant improvement	2025-10-01 10:35:26.031352+01	0	f	2025-10-01 10:35:26.031353+01	0	6.83724413957834	2	11	1	68	15
2019	0	Excellent work!	2025-10-01 10:35:26.050939+01	2	t	2025-10-01 10:35:26.050939+01	64.52089013230669	64.52089013230669	3	11	1	68	15
2020	0	Excellent work!	2025-10-01 10:35:26.070851+01	2	t	2025-10-01 10:35:26.070851+01	60.50576131822531	60.50576131822531	4	11	1	68	15
2021	10.764321932618564	Requires significant improvement	2025-10-01 10:35:26.089512+01	0	f	2025-10-01 10:35:26.089513+01	0	10.764321932618564	1	11	2	68	15
2022	29.545452473695974	Excellent work!	2025-10-01 10:35:26.108352+01	0	f	2025-10-01 10:35:26.108352+01	0	29.545452473695974	2	11	2	68	15
2023	0	Good performance	2025-10-01 10:35:26.12667+01	1.7	t	2025-10-01 10:35:26.12667+01	55.30000254617147	55.30000254617147	3	11	2	68	15
2024	0	Excellent work!	2025-10-01 10:35:26.145842+01	2	t	2025-10-01 10:35:26.145843+01	62.319985656684665	62.319985656684665	4	11	2	68	15
2025	16.53303926671163	Needs improvement	2025-10-01 10:35:26.164764+01	0	f	2025-10-01 10:35:26.164765+01	0	16.53303926671163	1	14	1	68	20
2026	20.986794923723934	Satisfactory	2025-10-01 10:35:26.1839+01	0	f	2025-10-01 10:35:26.183901+01	0	20.986794923723934	2	14	1	68	20
2027	0	Good performance	2025-10-01 10:35:26.203244+01	1.7	t	2025-10-01 10:35:26.203245+01	57.01128140661828	57.01128140661828	3	14	1	68	20
2028	0	Excellent work!	2025-10-01 10:35:26.229586+01	2.3	t	2025-10-01 10:35:26.229589+01	67.45265118741958	67.45265118741958	4	14	1	68	20
2029	19.31805893980708	Satisfactory	2025-10-01 10:35:26.252578+01	0	f	2025-10-01 10:35:26.252578+01	0	19.31805893980708	1	14	2	68	20
2030	18.138428797318515	Satisfactory	2025-10-01 10:35:26.274064+01	0	f	2025-10-01 10:35:26.274064+01	0	18.138428797318515	2	14	2	68	20
2031	0	Excellent work!	2025-10-01 10:35:26.297441+01	2	t	2025-10-01 10:35:26.297443+01	62.48140083742757	62.48140083742757	3	14	2	68	20
2032	0	Excellent work!	2025-10-01 10:35:26.320416+01	2	t	2025-10-01 10:35:26.320416+01	63.10274251876424	63.10274251876424	4	14	2	68	20
2033	22.806542951414265	Good performance	2025-10-01 10:35:26.341143+01	0	f	2025-10-01 10:35:26.341143+01	0	22.806542951414265	1	17	1	68	25
2034	5.540814203540896	Requires significant improvement	2025-10-01 10:35:26.360358+01	0	f	2025-10-01 10:35:26.360358+01	0	5.540814203540896	2	17	1	68	25
2035	0	Excellent work!	2025-10-01 10:35:26.37961+01	2.3	t	2025-10-01 10:35:26.37961+01	69.99205771956467	69.99205771956467	3	17	1	68	25
2036	0	Excellent work!	2025-10-01 10:35:26.398855+01	2	t	2025-10-01 10:35:26.398856+01	60.731561512596265	60.731561512596265	4	17	1	68	25
2037	25.188204875777103	Good performance	2025-10-01 10:35:26.418252+01	0	f	2025-10-01 10:35:26.418253+01	0	25.188204875777103	1	17	2	68	25
2038	23.19684111923478	Good performance	2025-10-01 10:35:26.437861+01	0	f	2025-10-01 10:35:26.437861+01	0	23.19684111923478	2	17	2	68	25
2039	0	Needs improvement	2025-10-01 10:35:26.456843+01	0	f	2025-10-01 10:35:26.456843+01	35.127161370575095	35.127161370575095	3	17	2	68	25
2040	0	Good performance	2025-10-01 10:35:26.476575+01	1.3	t	2025-10-01 10:35:26.476575+01	53.933972422704365	53.933972422704365	4	17	2	68	25
\.


--
-- TOC entry 3589 (class 0 OID 60268)
-- Dependencies: 224
-- Data for Name: revendication; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.revendication (revendication_id, creation_date, description, last_modified_date, requested_score, status, teacher_comment, grade_id, period_id, semester_id, student_id) FROM stdin;
\.


--
-- TOC entry 3591 (class 0 OID 60279)
-- Dependencies: 226
-- Data for Name: revendication_period; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.revendication_period (revendication_period_id, color, creation_date, end_date, is_active, last_modified_date, start_date, exam_period_id, semester_id) FROM stdin;
7	#3357FF	2025-10-05 19:45:15.440476+01	2026-04-27	f	2025-10-05 19:45:15.440476+01	2026-04-20	2	2
8	#FF33F5	2025-10-05 19:45:15.441748+01	2026-06-27	f	2025-10-05 19:45:15.441748+01	2026-06-20	4	2
5	#FF5733	2025-10-05 19:45:15.433795+01	2025-10-15	t	2025-10-05 19:45:15.433795+01	2025-10-01	1	1
6	#33FF57	2025-10-05 19:45:15.438259+01	2025-12-27	f	2025-10-05 19:45:15.438259+01	2025-12-20	3	1
\.


--
-- TOC entry 3593 (class 0 OID 60286)
-- Dependencies: 228
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (role_id, role_name) FROM stdin;
1	ADMIN
2	STUDENT
3	TEACHER
\.


--
-- TOC entry 3595 (class 0 OID 60294)
-- Dependencies: 230
-- Data for Name: semester; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.semester (semester_id, is_active, creation_date, end_date, last_modified_date, name, start_date) FROM stdin;
1	t	2025-10-01 10:34:53.883739+01	2026-02-20	2025-10-01 10:34:53.883743+01	Semester 1 - 2025/2026	2025-09-08
2	f	2025-10-01 10:34:53.896134+01	2026-07-31	2025-10-01 10:34:53.896135+01	Semester 2 - 2025/2026	2026-03-02
\.


--
-- TOC entry 3596 (class 0 OID 60300)
-- Dependencies: 231
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (cycle, date_of_birth, matricule, place_of_birth, speciality, id, level_id) FROM stdin;
BACHELOR	2007-10-01	24A0001	Douala	Business Administration	18	1
BACHELOR	2002-10-01	24A0002	Ngaoundéré	Software Engineering	19	1
BACHELOR	2002-10-01	24A0003	Douala	Engineering	20	1
BACHELOR	2001-10-01	24A0004	Ebolowa	Engineering	21	1
BACHELOR	2003-10-01	24A0005	Yaoundé	Mathematics	22	1
BACHELOR	2001-10-01	24A0006	Yaoundé	Software Engineering	23	1
BACHELOR	2001-10-01	24A0007	Bertoua	Business Administration	24	1
BACHELOR	2004-10-01	24A0008	Bafoussam	Applied Mathematics	25	1
BACHELOR	2006-10-01	24A0009	Garoua	Software Engineering	26	1
BACHELOR	2006-10-01	24A0010	Buea	Engineering	27	1
BACHELOR	2000-10-01	24A0011	Kumba	Software Engineering	28	1
BACHELOR	2006-10-01	24A0012	Kribi	Software Engineering	29	1
BACHELOR	2000-10-01	24A0013	Ebolowa	Software Engineering	30	1
BACHELOR	2000-10-01	24A0014	Ebolowa	Business Administration	31	1
BACHELOR	2001-10-01	24A0015	Kribi	Physics	32	1
BACHELOR	2002-10-01	24B0001	Dschang	Physics	33	2
BACHELOR	2003-10-01	24B0002	Buea	Computer Science	34	2
BACHELOR	2005-10-01	24B0003	Dschang	Engineering	35	2
BACHELOR	2003-10-01	24B0004	Kumba	Mathematics	36	2
BACHELOR	2003-10-01	24B0005	Foumban	Mathematics	37	2
BACHELOR	2001-10-01	24B0006	Maroua	Physics	38	2
BACHELOR	2006-10-01	24B0007	Ebolowa	Mathematics	39	2
BACHELOR	2005-10-01	24B0008	Kribi	Applied Mathematics	40	2
BACHELOR	2001-10-01	24B0009	Limbe	Applied Mathematics	41	2
BACHELOR	2006-10-01	24B0010	Dschang	Applied Mathematics	42	2
BACHELOR	2006-10-01	24B0011	Buea	Business Administration	43	2
BACHELOR	2000-10-01	24B0012	Dschang	Software Engineering	44	2
BACHELOR	2007-10-01	24C0001	Kribi	Physics	45	3
BACHELOR	2006-10-01	24C0002	Limbe	Computer Science	46	3
BACHELOR	2000-10-01	24C0003	Garoua	Computer Science	47	3
BACHELOR	2001-10-01	24C0004	Maroua	Data Science	48	3
BACHELOR	2003-10-01	24C0005	Bamenda	Data Science	49	3
BACHELOR	2006-10-01	24C0006	Garoua	Business Administration	50	3
BACHELOR	2002-10-01	24C0007	Yaoundé	Business Administration	51	3
BACHELOR	2006-10-01	24C0008	Bamenda	Business Administration	52	3
BACHELOR	2000-10-01	24C0009	Kribi	Business Administration	53	3
BACHELOR	2000-10-01	24C0010	Ngaoundéré	Computer Science	54	3
MASTER	2001-10-01	24D0001	Dschang	Engineering	55	4
MASTER	2006-10-01	24D0002	Limbe	Physics	56	4
MASTER	2000-10-01	24D0003	Foumban	Mathematics	57	4
MASTER	2001-10-01	24D0004	Limbe	Applied Mathematics	58	4
MASTER	2003-10-01	24D0005	Yaoundé	Mathematics	59	4
MASTER	2000-10-01	24D0006	Kribi	Mathematics	60	4
MASTER	2001-10-01	24D0007	Kumba	Computer Science	61	4
MASTER	2007-10-01	24D0008	Foumban	Mathematics	62	4
MASTER	2005-10-01	24E0001	Dschang	Applied Mathematics	63	5
MASTER	2005-10-01	24E0002	Bertoua	Physics	64	5
MASTER	2000-10-01	24E0003	Bafoussam	Business Administration	65	5
MASTER	2000-10-01	24E0004	Douala	Software Engineering	66	5
MASTER	2004-10-01	24E0005	Buea	Software Engineering	67	5
MASTER	2007-10-01	24E0006	Ngaoundéré	Engineering	68	5
\.


--
-- TOC entry 3598 (class 0 OID 60309)
-- Dependencies: 233
-- Data for Name: subjects; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subjects (subject_id, studentcycle, credits, description, subject_code, subject_name, department_id, semester_id, teaching_level_id, teacher_id, transcript_id) FROM stdin;
1	BACHELOR	6.00	Description for Programming Fundamentals	CS101	Programming Fundamentals	1	1	1	2	\N
2	BACHELOR	6.00	Description for Data Structures	CS201	Data Structures	1	2	2	2	\N
3	BACHELOR	6.00	Description for Algorithms	CS301	Algorithms	1	1	3	3	\N
4	MASTER	6.00	Description for Machine Learning	CS401	Machine Learning	1	1	4	4	\N
5	MASTER	6.00	Description for AI Research	CS501	AI Research	1	1	5	5	\N
6	BACHELOR	8.00	Description for Calculus I	MATH111	Calculus I	2	1	1	6	\N
7	BACHELOR	8.00	Description for Calculus II	MATH211	Calculus II	2	2	2	6	\N
8	BACHELOR	6.00	Description for Real Analysis	MATH311	Real Analysis	2	1	3	7	\N
9	MASTER	6.00	Description for Advanced Analysis	MATH411	Advanced Analysis	2	1	4	8	\N
10	MASTER	6.00	Description for Mathematical Research Methods	MATH511	Mathematical Research Methods	2	1	5	8	\N
11	BACHELOR	8.00	Description for Classical Mechanics	PHY111	Classical Mechanics	3	1	1	9	\N
12	BACHELOR	8.00	Description for Electromagnetism	PHY211	Electromagnetism	3	2	2	9	\N
13	BACHELOR	6.00	Description for Quantum Mechanics	PHY311	Quantum Mechanics	3	1	3	10	\N
15	MASTER	6.00	Description for Physics Research Project	PHY511	Physics Research Project	3	1	5	11	\N
16	BACHELOR	6.00	Description for Principles of Management	BUS111	Principles of Management	4	1	1	12	\N
17	BACHELOR	6.00	Description for Marketing Principles	MKT211	Marketing Principles	4	2	2	12	\N
18	BACHELOR	6.00	Description for Strategic Management	BUS311	Strategic Management	4	1	3	13	\N
19	MASTER	6.00	Description for Advanced Finance	FIN411	Advanced Finance	4	1	4	14	\N
20	MASTER	6.00	Description for Business Research Methods	BUS511	Business Research Methods	4	1	5	14	\N
21	BACHELOR	7.00	Description for Engineering Mathematics I	MATH141	Engineering Mathematics I	5	1	1	15	\N
22	BACHELOR	7.00	Description for Engineering Mathematics II	MATH241	Engineering Mathematics II	5	2	2	15	\N
23	BACHELOR	6.00	Description for Control Systems	ENG341	Control Systems	5	1	3	16	\N
24	MASTER	6.00	Description for Advanced Engineering	ENG441	Advanced Engineering	5	1	4	17	\N
25	MASTER	6.00	Description for Engineering Research Methods	ENG541	Engineering Research Methods	5	1	5	17	\N
14	MASTER	6.00	Description for Advanced Quantum	PHY411	Advanced Quantum	3	1	4	11	\N
26	BACHELOR	8.00	Updated description	TEST101	Updated Test Subject	1	1	\N	2	\N
\.


--
-- TOC entry 3599 (class 0 OID 60318)
-- Dependencies: 234
-- Data for Name: teachers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.teachers (phone_number, id, department_id) FROM stdin;
123456789	2	1
123456790	3	1
123456791	4	1
123456792	5	1
123456793	6	2
123456794	7	2
123456795	8	2
123456796	9	3
123456797	10	3
123456798	11	3
123456799	12	4
123456800	13	4
123456801	14	4
123456802	15	5
123456803	16	5
123456804	17	5
\.


--
-- TOC entry 3601 (class 0 OID 60324)
-- Dependencies: 236
-- Data for Name: teaching_levels; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.teaching_levels (teaching_level_id, student_level, teacher_id) FROM stdin;
1	LEVEL1	15
2	LEVEL2	16
3	LEVEL3	16
4	LEVEL4	17
5	LEVEL5	17
6	LEVEL4	11
7	LEVEL5	11
8	LEVEL1	12
9	LEVEL2	10
10	LEVEL1	2
11	LEVEL2	13
12	LEVEL4	5
13	LEVEL4	8
14	LEVEL1	6
15	LEVEL3	4
16	LEVEL2	3
17	LEVEL4	14
18	LEVEL1	9
19	LEVEL2	7
20	LEVEL2	2
21	LEVEL3	3
22	LEVEL4	4
23	LEVEL5	5
24	LEVEL2	6
25	LEVEL3	7
26	LEVEL5	8
27	LEVEL2	9
28	LEVEL3	10
29	LEVEL1	11
30	LEVEL2	12
31	LEVEL3	13
32	LEVEL5	14
33	LEVEL1	15
34	LEVEL1	16
35	LEVEL1	17
\.


--
-- TOC entry 3603 (class 0 OID 60332)
-- Dependencies: 238
-- Data for Name: transcript; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transcript (transcript_id, creation_date, gpa, last_modified_date, status, semester_id, student_id) FROM stdin;
1	2025-10-01 10:35:26.593042+01	4	2025-10-01 10:35:26.593046+01	PASSED	1	18
2	2025-10-01 10:35:26.664912+01	4	2025-10-01 10:35:26.664913+01	PASSED	1	19
3	2025-10-01 10:35:26.718776+01	4	2025-10-01 10:35:26.718777+01	PASSED	1	20
4	2025-10-01 10:35:26.760903+01	4	2025-10-01 10:35:26.760904+01	PASSED	1	21
5	2025-10-01 10:35:26.801096+01	0.41218731751760074	2025-10-01 10:35:26.801097+01	FAILED	1	22
6	2025-10-01 10:35:26.844699+01	4	2025-10-01 10:35:26.8447+01	PASSED	1	23
7	2025-10-01 10:35:26.887341+01	4	2025-10-01 10:35:26.887342+01	PASSED	1	24
8	2025-10-01 10:35:26.932735+01	1.7566630409057635	2025-10-01 10:35:26.932736+01	FAILED	1	25
9	2025-10-01 10:35:26.984827+01	4	2025-10-01 10:35:26.984828+01	PASSED	1	26
10	2025-10-01 10:35:27.024719+01	4	2025-10-01 10:35:27.02472+01	PASSED	1	27
11	2025-10-01 10:35:27.067967+01	4	2025-10-01 10:35:27.067968+01	PASSED	1	28
12	2025-10-01 10:35:27.110778+01	4	2025-10-01 10:35:27.110779+01	PASSED	1	29
13	2025-10-01 10:35:27.156357+01	4	2025-10-01 10:35:27.156358+01	PASSED	1	30
14	2025-10-01 10:35:27.202315+01	0.7777347215516914	2025-10-01 10:35:27.202316+01	FAILED	1	31
15	2025-10-01 10:35:27.24315+01	4	2025-10-01 10:35:27.243151+01	PASSED	1	32
16	2025-10-01 10:35:27.282342+01	4	2025-10-01 10:35:27.282342+01	PASSED	1	33
17	2025-10-01 10:35:27.322156+01	4	2025-10-01 10:35:27.322157+01	PASSED	1	34
18	2025-10-01 10:35:27.362826+01	0.8973827089226896	2025-10-01 10:35:27.362828+01	FAILED	1	35
19	2025-10-01 10:35:27.412683+01	4	2025-10-01 10:35:27.412684+01	PASSED	1	36
20	2025-10-01 10:35:27.454224+01	4	2025-10-01 10:35:27.454225+01	PASSED	1	37
21	2025-10-01 10:35:27.493733+01	4	2025-10-01 10:35:27.493734+01	PASSED	1	38
22	2025-10-01 10:35:27.533086+01	4	2025-10-01 10:35:27.533087+01	PASSED	1	39
23	2025-10-01 10:35:27.579224+01	4	2025-10-01 10:35:27.579225+01	PASSED	1	40
24	2025-10-01 10:35:27.629372+01	1.3526020435224293	2025-10-01 10:35:27.629373+01	FAILED	1	41
25	2025-10-01 10:35:27.672562+01	4	2025-10-01 10:35:27.672563+01	PASSED	1	42
26	2025-10-01 10:35:27.71434+01	0.19807399302894324	2025-10-01 10:35:27.714341+01	FAILED	1	43
27	2025-10-01 10:35:27.752359+01	4	2025-10-01 10:35:27.75236+01	PASSED	1	44
28	2025-10-01 10:35:27.797771+01	4	2025-10-01 10:35:27.797772+01	PASSED	1	45
29	2025-10-01 10:35:27.850671+01	4	2025-10-01 10:35:27.850672+01	PASSED	1	46
30	2025-10-01 10:35:27.914116+01	4	2025-10-01 10:35:27.914117+01	PASSED	1	47
31	2025-10-01 10:35:27.96463+01	4	2025-10-01 10:35:27.96463+01	PASSED	1	48
32	2025-10-01 10:35:28.011755+01	1.9344789110101117	2025-10-01 10:35:28.011755+01	FAILED	1	49
33	2025-10-01 10:35:28.078772+01	4	2025-10-01 10:35:28.078773+01	PASSED	1	50
34	2025-10-01 10:35:28.11972+01	4	2025-10-01 10:35:28.119721+01	PASSED	1	51
35	2025-10-01 10:35:28.162004+01	4	2025-10-01 10:35:28.162005+01	PASSED	1	52
36	2025-10-01 10:35:28.2059+01	0.5984156071297222	2025-10-01 10:35:28.2059+01	FAILED	1	53
37	2025-10-01 10:35:28.25621+01	4	2025-10-01 10:35:28.256211+01	PASSED	1	54
38	2025-10-01 10:35:28.297858+01	4	2025-10-01 10:35:28.297859+01	PASSED	1	55
39	2025-10-01 10:35:28.335893+01	0.1633225724073466	2025-10-01 10:35:28.335893+01	FAILED	1	56
40	2025-10-01 10:35:28.377722+01	4	2025-10-01 10:35:28.377723+01	PASSED	1	57
41	2025-10-01 10:35:28.419557+01	4	2025-10-01 10:35:28.419557+01	PASSED	1	58
42	2025-10-01 10:35:28.457677+01	4	2025-10-01 10:35:28.457678+01	PASSED	1	59
43	2025-10-01 10:35:28.50882+01	1.276723108591708	2025-10-01 10:35:28.50882+01	FAILED	1	60
44	2025-10-01 10:35:28.55098+01	4	2025-10-01 10:35:28.550981+01	PASSED	1	61
45	2025-10-01 10:35:28.591726+01	4	2025-10-01 10:35:28.591726+01	PASSED	1	62
46	2025-10-01 10:35:28.631193+01	4	2025-10-01 10:35:28.631193+01	PASSED	1	63
47	2025-10-01 10:35:28.670005+01	1.3691442896922177	2025-10-01 10:35:28.670006+01	FAILED	1	64
48	2025-10-01 10:35:28.708663+01	1.7999004224118398	2025-10-01 10:35:28.708664+01	FAILED	1	65
49	2025-10-01 10:35:28.757554+01	4	2025-10-01 10:35:28.757555+01	PASSED	1	66
50	2025-10-01 10:35:28.799851+01	4	2025-10-01 10:35:28.799852+01	PASSED	1	67
51	2025-10-01 10:35:28.839008+01	4	2025-10-01 10:35:28.839009+01	PASSED	1	68
\.


--
-- TOC entry 3605 (class 0 OID 60340)
-- Dependencies: 240
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, creation_date, email, first_name, is_active, last_modified_date, last_name, must_change_password, password, username, role_id) FROM stdin;
2	2025-10-01 10:34:54.226719+01	john.smith@university.edu	John	t	2025-10-01 10:34:54.22672+01	Smith	t	$2a$10$9HmAmUW1hMBkzeAnKK9sHOKvIBbU9cvkP9t7ULSV5Luyb/AP/OJMS	prof.smith	3
3	2025-10-01 10:34:54.364961+01	sarah.johnson@university.edu	Sarah	t	2025-10-01 10:34:54.364961+01	Johnson	t	$2a$10$XvaO2qSlrUWPNjKP693twuHE3tq/uv5eow7hT.qActu0zLxZhm.ny	prof.johnson	3
4	2025-10-01 10:34:54.502021+01	michael.williams@university.edu	Michael	t	2025-10-01 10:34:54.502022+01	Williams	t	$2a$10$lC10P56632iESdEUxgnXauzPnRH1Kwl3YClsybcx8BU3lDyMV8oCa	prof.williams	3
5	2025-10-01 10:34:54.633906+01	emily.brown@university.edu	Emily	t	2025-10-01 10:34:54.633906+01	Brown	t	$2a$10$qqUIt2aX.eNPk0JYyRrkb.UBPbcwIEJ3a3Mo.rknEVp5ct41JViS.	prof.brown	3
6	2025-10-01 10:34:54.774044+01	robert.davis@university.edu	Robert	t	2025-10-01 10:34:54.774047+01	Davis	t	$2a$10$oK/6GKnKMuEUogVV87INTuZX54UDvr3s5Sy/ZIrWTt7lbwlnI1HPi	prof.davis	3
7	2025-10-01 10:34:54.90881+01	jennifer.miller@university.edu	Jennifer	t	2025-10-01 10:34:54.90881+01	Miller	t	$2a$10$Rf6QB9WmTU1kSok2W2sZvuIiU3eeRz38Q/QVeYLx6IaVi5NRLSCNS	prof.miller	3
8	2025-10-01 10:34:55.046639+01	david.wilson@university.edu	David	t	2025-10-01 10:34:55.04664+01	Wilson	t	$2a$10$SnqB/8RmYrPQjKnTswUrzuOIIRkftB7wBj5yrC2W1voRYzBFFEuoK	prof.wilson	3
9	2025-10-01 10:34:55.1736+01	lisa.moore@university.edu	Lisa	t	2025-10-01 10:34:55.173601+01	Moore	t	$2a$10$kLWewuZDuuBmx/JCtg2.iu0zTBUXIUe6vwcMJn39GivgaFbzrDB/S	prof.moore	3
10	2025-10-01 10:34:55.310218+01	james.taylor@university.edu	James	t	2025-10-01 10:34:55.310219+01	Taylor	t	$2a$10$uF65nA5u0QDoir8tGYS73uCL0IcgRXzdr0RL2PzHdC/316g9RZKQu	prof.taylor	3
11	2025-10-01 10:34:55.437765+01	maria.anderson@university.edu	Maria	t	2025-10-01 10:34:55.437766+01	Anderson	t	$2a$10$ui2KPbYQqy6KhOd7U4v1ROQpme1RdDv51sszqwEN3k/lR7Ihz5t5i	prof.anderson	3
12	2025-10-01 10:34:55.569773+01	christopher.thomas@university.edu	Christopher	t	2025-10-01 10:34:55.569775+01	Thomas	t	$2a$10$WKUKN7bOCndkieYmdidgce6MGFJY5jyP5quZXEye02e.4UFXesnAe	prof.thomas	3
13	2025-10-01 10:34:55.708859+01	amanda.jackson@university.edu	Amanda	t	2025-10-01 10:34:55.70886+01	Jackson	t	$2a$10$VtM08UlVODh0UHOg9RZnM.uIbDW6RiZ6At3yzg2ajklPzIJeq9Ssy	prof.jackson	3
14	2025-10-01 10:34:55.851388+01	richard.white@university.edu	Richard	t	2025-10-01 10:34:55.851389+01	White	t	$2a$10$xlPfOXd4WyukHWrqm/qYAu9Fo2E7IV.YDPoaNhtrvIzrFTuFdtoVC	prof.white	3
15	2025-10-01 10:34:55.997288+01	michelle.harris@university.edu	Michelle	t	2025-10-01 10:34:55.99729+01	Harris	t	$2a$10$KDtb35nXKi2Ajhsa4f1cPOPp7nA0uc2m61WSnJU7/0G/xEANtQBSe	prof.harris	3
16	2025-10-01 10:34:56.14699+01	kevin.martin@university.edu	Kevin	t	2025-10-01 10:34:56.146993+01	Martin	t	$2a$10$qSVsgptn06Y4mWKRJdFt3OOVSMQn/tmJ.bbmCeLzUfCt/LR4S5RqW	prof.martin	3
17	2025-10-01 10:34:56.294936+01	carlos.garcia@university.edu	Carlos	t	2025-10-01 10:34:56.294939+01	Garcia	t	$2a$10$0bhuS2NEea8bvCZNH2yNvePz2j7XGzg0rk3vd7RceMQtmBwJlhZMW	prof.garcia	3
18	2025-10-01 10:34:56.455395+01	george.parker.24a0001@student.university.edu	George	t	2025-10-01 10:34:56.455405+01	Parker	t	$2a$10$raP37kysLP0NvZUOsZsxieHWq1uYL4nQh43UpTdx3KVCsw90vU7du	24a0001	2
19	2025-10-01 10:34:56.589317+01	julia.nelson.24a0002@student.university.edu	Julia	t	2025-10-01 10:34:56.589318+01	Nelson	t	$2a$10$iDI417Ee6fWFRBA9.tTFoOag81U12Xrv8zZjNWssKHCwdvqk1vpny	24a0002	2
20	2025-10-01 10:34:56.716299+01	george.parker.24a0003@student.university.edu	George	t	2025-10-01 10:34:56.7163+01	Parker	t	$2a$10$fngWPI5rTMVqXwzIHJ0lm.sjoidFA73OHUfIavvFipzu.QRpiro7O	24a0003	2
21	2025-10-01 10:34:56.839697+01	nina.nelson.24a0004@student.university.edu	Nina	t	2025-10-01 10:34:56.839698+01	Nelson	t	$2a$10$K9BHMHtmpBAx1L3.94SvBOhxx274htVdXz.rv67qoFfhpOBv8LWNy	24a0004	2
22	2025-10-01 10:34:56.964532+01	julia.foster.24a0005@student.university.edu	Julia	t	2025-10-01 10:34:56.964533+01	Foster	t	$2a$10$w/r2xGoEpWbS0/5yoN4K/e0gisED.XTFYc8nCx6XEWt6VXFrSTEJW	24a0005	2
23	2025-10-01 10:34:57.094154+01	oscar.brown.24a0006@student.university.edu	Oscar	t	2025-10-01 10:34:57.094154+01	Brown	t	$2a$10$/wngLUuw7KwH2gSSaEPTb.C9z5P/XgjGGJm5dDldWNC2vXwK/ze4S	24a0006	2
24	2025-10-01 10:34:57.226036+01	diana.lewis.24a0007@student.university.edu	Diana	t	2025-10-01 10:34:57.226037+01	Lewis	t	$2a$10$zJLPdidcsEPn4lWktx35W.Qe6qekZ3NkPaIHeKjaQQi5w.a1GntWW	24a0007	2
25	2025-10-01 10:34:57.348388+01	paula.davis.24a0008@student.university.edu	Paula	t	2025-10-01 10:34:57.348388+01	Davis	t	$2a$10$T1QyDS/YlgGFWoh0rfJGm.CU/bC/jyHbrRqeHxiOiPLeQ9Sm6r0I2	24a0008	2
26	2025-10-01 10:34:57.473123+01	charlie.jackson.24a0009@student.university.edu	Charlie	t	2025-10-01 10:34:57.473124+01	Jackson	t	$2a$10$plFGRZBAF4xE1wFrFWFnweWy89O9FgObt/SljPFJp8iPTJgueWkSy	24a0009	2
27	2025-10-01 10:34:57.595831+01	diana.roberts.24a0010@student.university.edu	Diana	t	2025-10-01 10:34:57.595831+01	Roberts	t	$2a$10$Cb4p.hpxXU6GozsyFYoGOuX6TZvlqxMwERycYyT92oiJMr6bgD7xm	24a0010	2
28	2025-10-01 10:34:57.719571+01	kevin.jackson.24a0011@student.university.edu	Kevin	t	2025-10-01 10:34:57.719572+01	Jackson	t	$2a$10$WHhvvm13h2Pe9vHrscBcXekEofGmKJhfSZ74QjnoUcv9F3lVaGN8q	24a0011	2
29	2025-10-01 10:34:57.847665+01	diana.king.24a0012@student.university.edu	Diana	t	2025-10-01 10:34:57.847666+01	King	t	$2a$10$/preQpDk3vihqMbUTAk..e8NuGNL/a6tuiN67kGLZwtyPMR2t76Km	24a0012	2
30	2025-10-01 10:34:57.974117+01	bob.king.24a0013@student.university.edu	Bob	t	2025-10-01 10:34:57.974118+01	King	t	$2a$10$GEWWte3JaF8XAlc61epM0uA/QX/.5avP76vD3gnatW36GaXf0QYHG	24a0013	2
31	2025-10-01 10:34:58.097289+01	kevin.roberts.24a0014@student.university.edu	Kevin	t	2025-10-01 10:34:58.09729+01	Roberts	t	$2a$10$wOl9R90ssehJxnLZkHTFQOcV77gdMA7EGLizHKaUauFUANsCqrovm	24a0014	2
32	2025-10-01 10:34:58.230516+01	oscar.nelson.24a0015@student.university.edu	Oscar	t	2025-10-01 10:34:58.230517+01	Nelson	t	$2a$10$222HQ98TPYRwONJCv4AQ9OrUWzIBMzJV2bpK.Kb9x9ZrhobDF9L1e	24a0015	2
33	2025-10-01 10:34:58.36087+01	bob.davis.24b0001@student.university.edu	Bob	t	2025-10-01 10:34:58.360871+01	Davis	t	$2a$10$XFwCCsLpXoMFOfHD9taO1ejt2YKECNJ.uNrQ3SDNQV3Ui29synrEm	24b0001	2
34	2025-10-01 10:34:58.488833+01	charlie.clark.24b0002@student.university.edu	Charlie	t	2025-10-01 10:34:58.488834+01	Clark	t	$2a$10$SXPlcscIvBYHLIsOLhQLUO9vZLFq5wyEp1q96I5j01JDHumdoA3Xy	24b0002	2
35	2025-10-01 10:34:58.614088+01	edward.hall.24b0003@student.university.edu	Edward	t	2025-10-01 10:34:58.614089+01	Hall	t	$2a$10$BZUNQa9HIXieS2e1D0RZ9.3Bzs0s5mwtTfdZgRPfiLj0a2ioRoUcu	24b0003	2
36	2025-10-01 10:34:58.738647+01	edward.evans.24b0004@student.university.edu	Edward	t	2025-10-01 10:34:58.738648+01	Evans	t	$2a$10$lDnljDPMKIebh58Crh7SZuWikzXBupmAyapeJULikSbHqh15UyP8W	24b0004	2
37	2025-10-01 10:34:58.860487+01	diana.clark.24b0005@student.university.edu	Diana	t	2025-10-01 10:34:58.860488+01	Clark	t	$2a$10$dcAgjJVjgQQWrFeAddUTBeHZb5cCvQPR8Mp9ZNal1oRuX8yjwBvlK	24b0005	2
38	2025-10-01 10:34:58.983339+01	fiona.parker.24b0006@student.university.edu	Fiona	t	2025-10-01 10:34:58.98334+01	Parker	t	$2a$10$h8kEMB7f4PmdPQjW2H9ow.vlnIWFR9rrlnTBzog2K2rPQhkeMm7LO	24b0006	2
39	2025-10-01 10:34:59.102966+01	fiona.brown.24b0007@student.university.edu	Fiona	t	2025-10-01 10:34:59.102967+01	Brown	t	$2a$10$PQPIvkmr9P1ja3ZhPb8kUO5o4vvpMB/eS/ZD0jclOg4EXR0/Hg4i.	24b0007	2
40	2025-10-01 10:34:59.22521+01	oscar.lewis.24b0008@student.university.edu	Oscar	t	2025-10-01 10:34:59.225211+01	Lewis	t	$2a$10$faOND2sUaW9GZ5HhCFd3cux0THkwYFRLhaE3sFQyyLpstRNp5NEhq	24b0008	2
41	2025-10-01 10:34:59.345198+01	george.anderson.24b0009@student.university.edu	George	t	2025-10-01 10:34:59.345198+01	Anderson	t	$2a$10$LlpKeU/rf6Dm/mH119m8vewT3ubslFbqC3f8biMhR8T4ziUSsiyg6	24b0009	2
42	2025-10-01 10:34:59.466556+01	diana.foster.24b0010@student.university.edu	Diana	t	2025-10-01 10:34:59.466556+01	Foster	t	$2a$10$9FP3cw70qGjWoIzBIHc8U.hVtix596plyOzn.IVPXT.L7qFHLcZ/6	24b0010	2
43	2025-10-01 10:34:59.593301+01	bob.parker.24b0011@student.university.edu	Bob	t	2025-10-01 10:34:59.593302+01	Parker	t	$2a$10$3OAmyc2gcy3SYthtPmO9EuTlQ9sPID.05AtNty5AGh9a59pXvT44i	24b0011	2
44	2025-10-01 10:34:59.716647+01	fiona.foster.24b0012@student.university.edu	Fiona	t	2025-10-01 10:34:59.716648+01	Foster	t	$2a$10$7jcek1l59R9GOun/oZ9KOOiA9TXhncMqNHVrxzwrM2Gf2GY2PplHe	24b0012	2
45	2025-10-01 10:34:59.838494+01	george.smith.24c0001@student.university.edu	George	t	2025-10-01 10:34:59.838495+01	Smith	t	$2a$10$LEg6YWowq18m6HskS7Jge.cxT/Z.2tnq2RPGiHRkDbgqHzeDa42MG	24c0001	2
46	2025-10-01 10:34:59.961546+01	diana.brown.24c0002@student.university.edu	Diana	t	2025-10-01 10:34:59.961547+01	Brown	t	$2a$10$b/C78RMt..yKXHW4ALo..eP2kbkorNbeyUmizv02BeTX6uHTrPRg6	24c0002	2
47	2025-10-01 10:35:00.104171+01	michael.davis.24c0003@student.university.edu	Michael	t	2025-10-01 10:35:00.104172+01	Davis	t	$2a$10$oKM9bAz/f/0nmN1kzBJifOGI0sopEqt/MxTlC7ORQAtDNgVoI5LbC	24c0003	2
48	2025-10-01 10:35:00.267293+01	ivan.foster.24c0004@student.university.edu	Ivan	t	2025-10-01 10:35:00.267293+01	Foster	t	$2a$10$hi43E7sPfdRyTaJOLJa36O8GZPmIemEI0r6WMtYNszdtrrTQYdI/W	24c0004	2
49	2025-10-01 10:35:00.390817+01	ivan.miller.24c0005@student.university.edu	Ivan	t	2025-10-01 10:35:00.390818+01	Miller	t	$2a$10$ImpRWv1Mv9W.KanRrUpNNeWTV8.X/OCVu45jUKPNfQZ5y3LpAdbQq	24c0005	2
50	2025-10-01 10:35:00.511425+01	nina.evans.24c0006@student.university.edu	Nina	t	2025-10-01 10:35:00.511426+01	Evans	t	$2a$10$mdnTnA2xqAQQT.in1ZRlNOHk5dmGuWdXJauw7yef.kA2k1/IPEf8C	24c0006	2
51	2025-10-01 10:35:00.628766+01	julia.lewis.24c0007@student.university.edu	Julia	t	2025-10-01 10:35:00.628766+01	Lewis	t	$2a$10$qY5epiDSGZrzttno7Tx.M.XI361jlvH4jg1WEXWEEbUvDuvhDdgRm	24c0007	2
52	2025-10-01 10:35:00.751346+01	ivan.miller.24c0008@student.university.edu	Ivan	t	2025-10-01 10:35:00.751346+01	Miller	t	$2a$10$i23WxS10XoA6nYGUQdCIt.1I9vUp76keGqMWUK7RBB9m794uctesW	24c0008	2
53	2025-10-01 10:35:00.872182+01	laura.parker.24c0009@student.university.edu	Laura	t	2025-10-01 10:35:00.872183+01	Parker	t	$2a$10$7HHCDWqV2Bkee2drSr1dc.9CHrJJsIhJN1zTO3yKV6utZDU1bvmv2	24c0009	2
54	2025-10-01 10:35:00.993408+01	george.parker.24c0010@student.university.edu	George	t	2025-10-01 10:35:00.993408+01	Parker	t	$2a$10$34lHSMv5v76WYGYHWWp2vuBmiEJuEO7ConCS4zMobwmFzwU7oU3ku	24c0010	2
55	2025-10-01 10:35:01.111917+01	alice.hall.24d0001@student.university.edu	Alice	t	2025-10-01 10:35:01.111918+01	Hall	t	$2a$10$xlG8Y4e5G3Y5BF1dcIlFCO4A0bfw07n.C6OMTD/c2aJB6oAv95S.W	24d0001	2
56	2025-10-01 10:35:01.237797+01	oscar.smith.24d0002@student.university.edu	Oscar	t	2025-10-01 10:35:01.237798+01	Smith	t	$2a$10$aOssYG7G3cbQZaLOU48JA.y09Z7AMOkJ4tukQudLvvAZh8O4/8i6K	24d0002	2
57	2025-10-01 10:35:01.358095+01	charlie.king.24d0003@student.university.edu	Charlie	t	2025-10-01 10:35:01.358095+01	King	t	$2a$10$4SbYpDQgPYHTEXQGYxQlQ.hLs0fVGvSkGWhRV/ORAafbe/Evzzeje	24d0003	2
58	2025-10-01 10:35:01.480918+01	george.nelson.24d0004@student.university.edu	George	t	2025-10-01 10:35:01.480918+01	Nelson	t	$2a$10$EcNuK/us3XVt3HqA7DrBCuyd4QbsiWVOhaNkxydR9eDZVfS49H9Te	24d0004	2
59	2025-10-01 10:35:01.59886+01	diana.foster.24d0005@student.university.edu	Diana	t	2025-10-01 10:35:01.598861+01	Foster	t	$2a$10$gaoO1VMthCyloUdFwGJJKe261rdy4YmvjOUtDCBtKPS4wWW9m9mX2	24d0005	2
60	2025-10-01 10:35:01.723494+01	oscar.clark.24d0006@student.university.edu	Oscar	t	2025-10-01 10:35:01.723495+01	Clark	t	$2a$10$gUlmZTnM9vmHjJs9d5i0f.rB9cKWVum1YsvXF9rjTEDjr1lqsu3A6	24d0006	2
61	2025-10-01 10:35:01.844263+01	nina.davis.24d0007@student.university.edu	Nina	t	2025-10-01 10:35:01.844265+01	Davis	t	$2a$10$8qXDRfKLnBGMCfRaPjldleEtH4fbCw9fvNzNhLpJe/u7dRbotG06O	24d0007	2
62	2025-10-01 10:35:01.964613+01	nina.nelson.24d0008@student.university.edu	Nina	t	2025-10-01 10:35:01.964614+01	Nelson	t	$2a$10$awRH4hDV2bfxPbLlaCHcJeMgZQFkcDLXKrSyvN2uYSZHphb6dWXxK	24d0008	2
63	2025-10-01 10:35:02.083844+01	michael.jackson.24e0001@student.university.edu	Michael	t	2025-10-01 10:35:02.083845+01	Jackson	t	$2a$10$ccyczq6hGzRIv3mnqp3JYOC.1My/soMltIUCMk1RC3phzL7ZrT6B2	24e0001	2
64	2025-10-01 10:35:02.205453+01	hannah.hall.24e0002@student.university.edu	Hannah	t	2025-10-01 10:35:02.205454+01	Hall	t	$2a$10$CGwEkKPRiICl7.ztFjRb/ub.ih0eM/7Ajg0gpG6C4H0Gc7Wq.nYfu	24e0002	2
65	2025-10-01 10:35:02.324235+01	kevin.davis.24e0003@student.university.edu	Kevin	t	2025-10-01 10:35:02.324236+01	Davis	t	$2a$10$0nCJh2yS4hepAjlcgACY9O7vsu7QrNX.o.s68TMnp6R3838MTUXRu	24e0003	2
66	2025-10-01 10:35:02.445587+01	ivan.nelson.24e0004@student.university.edu	Ivan	t	2025-10-01 10:35:02.445588+01	Nelson	t	$2a$10$c0BJNufnY2kU3E7TlVyoBOgvYAFVvmgoB7V4EE0UbkHhSLiDBpMOq	24e0004	2
67	2025-10-01 10:35:02.569155+01	edward.roberts.24e0005@student.university.edu	Edward	t	2025-10-01 10:35:02.569156+01	Roberts	t	$2a$10$cwpAoJhQLyRpBlvgbvN3UOe0NoybhTJA1dkgL3xKoX7C9OXTc7z8y	24e0005	2
68	2025-10-01 10:35:02.690905+01	charlie.anderson.24e0006@student.university.edu	Charlie	t	2025-10-01 10:35:02.690906+01	Anderson	t	$2a$10$RaWFsGQrxgFNQjgx8VPRquUYNNivvk2jJ3IXOAjV.hTSjsS3gIbXu	24e0006	2
1	2025-10-01 10:34:53.87162+01	admin@university.edu	System	t	2025-10-01 12:28:12.720033+01	Administrator	t	$2a$10$9bl77Fg1EBkOchXn6vZMgOb7Zjjhm9Osu/qiBhvk.GlBEhka7sr4e	admin	1
\.


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 217
-- Name: departments_department_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.departments_department_id_seq', 7, true);


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 219
-- Name: exam_periods_exam_period_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.exam_periods_exam_period_id_seq', 4, true);


--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 221
-- Name: grades_grade_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grades_grade_id_seq', 2040, true);


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 225
-- Name: revendication_period_revendication_period_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.revendication_period_revendication_period_id_seq', 8, true);


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 223
-- Name: revendication_revendication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.revendication_revendication_id_seq', 1, false);


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 227
-- Name: roles_role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_role_id_seq', 3, true);


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 229
-- Name: semester_semester_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.semester_semester_id_seq', 10, true);


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 232
-- Name: subjects_subject_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.subjects_subject_id_seq', 26, true);


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 235
-- Name: teaching_levels_teaching_level_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.teaching_levels_teaching_level_id_seq', 35, true);


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 237
-- Name: transcript_transcript_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transcript_transcript_id_seq', 51, true);


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 239
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 68, true);


--
-- TOC entry 3368 (class 2606 OID 60251)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (department_id);


--
-- TOC entry 3372 (class 2606 OID 60259)
-- Name: exam_periods exam_periods_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.exam_periods
    ADD CONSTRAINT exam_periods_pkey PRIMARY KEY (exam_period_id);


--
-- TOC entry 3374 (class 2606 OID 60266)
-- Name: grades grades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT grades_pkey PRIMARY KEY (grade_id);


--
-- TOC entry 3378 (class 2606 OID 60284)
-- Name: revendication_period revendication_period_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication_period
    ADD CONSTRAINT revendication_period_pkey PRIMARY KEY (revendication_period_id);


--
-- TOC entry 3376 (class 2606 OID 60277)
-- Name: revendication revendication_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication
    ADD CONSTRAINT revendication_pkey PRIMARY KEY (revendication_id);


--
-- TOC entry 3382 (class 2606 OID 60292)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


--
-- TOC entry 3384 (class 2606 OID 60299)
-- Name: semester semester_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semester
    ADD CONSTRAINT semester_pkey PRIMARY KEY (semester_id);


--
-- TOC entry 3388 (class 2606 OID 60307)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- TOC entry 3392 (class 2606 OID 60317)
-- Name: subjects subjects_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT subjects_pkey PRIMARY KEY (subject_id);


--
-- TOC entry 3400 (class 2606 OID 60322)
-- Name: teachers teachers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (id);


--
-- TOC entry 3402 (class 2606 OID 60330)
-- Name: teaching_levels teaching_levels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching_levels
    ADD CONSTRAINT teaching_levels_pkey PRIMARY KEY (teaching_level_id);


--
-- TOC entry 3404 (class 2606 OID 60338)
-- Name: transcript transcript_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transcript
    ADD CONSTRAINT transcript_pkey PRIMARY KEY (transcript_id);


--
-- TOC entry 3390 (class 2606 OID 60355)
-- Name: students uk1psraxut9bwn2why6ex4xf1en; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT uk1psraxut9bwn2why6ex4xf1en UNIQUE (matricule);


--
-- TOC entry 3408 (class 2606 OID 60365)
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- TOC entry 3406 (class 2606 OID 60361)
-- Name: transcript uk_farnolrju3u3di6kgpbndfkm4; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transcript
    ADD CONSTRAINT uk_farnolrju3u3di6kgpbndfkm4 UNIQUE (student_id);


--
-- TOC entry 3386 (class 2606 OID 60353)
-- Name: semester uk_i2h7dsgrb3fkhvq8oxbquhkiq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semester
    ADD CONSTRAINT uk_i2h7dsgrb3fkhvq8oxbquhkiq UNIQUE (name);


--
-- TOC entry 3370 (class 2606 OID 60349)
-- Name: departments uk_j6cwks7xecs5jov19ro8ge3qk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT uk_j6cwks7xecs5jov19ro8ge3qk UNIQUE (name);


--
-- TOC entry 3380 (class 2606 OID 60351)
-- Name: revendication_period ukan8y49fp477i4arugtxql6pf4; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication_period
    ADD CONSTRAINT ukan8y49fp477i4arugtxql6pf4 UNIQUE (exam_period_id, semester_id);


--
-- TOC entry 3394 (class 2606 OID 60488)
-- Name: subjects ukdyt73b4xnxtg7w01cslgn89lp; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT ukdyt73b4xnxtg7w01cslgn89lp UNIQUE (teacher_id, teaching_level_id);


--
-- TOC entry 3396 (class 2606 OID 60357)
-- Name: subjects ukgaix2pna1ulbxhdl4kbq9yglt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT ukgaix2pna1ulbxhdl4kbq9yglt UNIQUE (subject_name);


--
-- TOC entry 3398 (class 2606 OID 60359)
-- Name: subjects ukqt734ivq9gq4yo4p1j1lhhk8l; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT ukqt734ivq9gq4yo4p1j1lhhk8l UNIQUE (subject_code);


--
-- TOC entry 3410 (class 2606 OID 60363)
-- Name: users ukr43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 3412 (class 2606 OID 60347)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3413 (class 2606 OID 60381)
-- Name: grades fk13a16545m7vvrcspc999r15s9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fk13a16545m7vvrcspc999r15s9 FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3424 (class 2606 OID 60421)
-- Name: students fk1uhh702qpyjcs42pkjt5u1gb5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fk1uhh702qpyjcs42pkjt5u1gb5 FOREIGN KEY (level_id) REFERENCES public.teaching_levels(teaching_level_id);


--
-- TOC entry 3422 (class 2606 OID 60411)
-- Name: revendication_period fk2fbsebdugwifijrej89ov59it; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication_period
    ADD CONSTRAINT fk2fbsebdugwifijrej89ov59it FOREIGN KEY (exam_period_id) REFERENCES public.exam_periods(exam_period_id);


--
-- TOC entry 3423 (class 2606 OID 60416)
-- Name: revendication_period fk320hya4n5m4250refwetwmelx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication_period
    ADD CONSTRAINT fk320hya4n5m4250refwetwmelx FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);


--
-- TOC entry 3426 (class 2606 OID 60451)
-- Name: subjects fk4qnc6bunl6i48ba4qc61gy43u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fk4qnc6bunl6i48ba4qc61gy43u FOREIGN KEY (transcript_id) REFERENCES public.transcript(transcript_id);


--
-- TOC entry 3418 (class 2606 OID 60391)
-- Name: revendication fk6e37rmbhq7jykinc3ltojq99a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication
    ADD CONSTRAINT fk6e37rmbhq7jykinc3ltojq99a FOREIGN KEY (grade_id) REFERENCES public.grades(grade_id);


--
-- TOC entry 3419 (class 2606 OID 60406)
-- Name: revendication fk78ww5idtw4frlobtur0w3a4eg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication
    ADD CONSTRAINT fk78ww5idtw4frlobtur0w3a4eg FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3425 (class 2606 OID 60426)
-- Name: students fk7xqmtv7r2eb5axni3jm0a80su; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT fk7xqmtv7r2eb5axni3jm0a80su FOREIGN KEY (id) REFERENCES public.users(id);


--
-- TOC entry 3420 (class 2606 OID 60396)
-- Name: revendication fk8yfutyxxvm45gqc0uxu3q8l7n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication
    ADD CONSTRAINT fk8yfutyxxvm45gqc0uxu3q8l7n FOREIGN KEY (period_id) REFERENCES public.exam_periods(exam_period_id);


--
-- TOC entry 3433 (class 2606 OID 60466)
-- Name: teaching_levels fk9sh3gvxx1vc53b6g91bur4nud; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teaching_levels
    ADD CONSTRAINT fk9sh3gvxx1vc53b6g91bur4nud FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


--
-- TOC entry 3414 (class 2606 OID 60376)
-- Name: grades fka0rr74yvwh7vxnakovikvji2r; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fka0rr74yvwh7vxnakovikvji2r FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);


--
-- TOC entry 3415 (class 2606 OID 60366)
-- Name: grades fkc7jiagpd3275r3mwus32yibto; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkc7jiagpd3275r3mwus32yibto FOREIGN KEY (exam_id) REFERENCES public.exam_periods(exam_period_id);


--
-- TOC entry 3434 (class 2606 OID 60471)
-- Name: transcript fkfi7g7qljp8xan3ycxof00phgy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transcript
    ADD CONSTRAINT fkfi7g7qljp8xan3ycxof00phgy FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);


--
-- TOC entry 3427 (class 2606 OID 60431)
-- Name: subjects fkgh0j5ejuox2kr2av0l8158c0a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fkgh0j5ejuox2kr2av0l8158c0a FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3421 (class 2606 OID 60401)
-- Name: revendication fkhnnitmelp21gg9gbwnti9vf4o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.revendication
    ADD CONSTRAINT fkhnnitmelp21gg9gbwnti9vf4o FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);


--
-- TOC entry 3416 (class 2606 OID 60371)
-- Name: grades fkjkankww1vg2lw4ysxo90qp51h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkjkankww1vg2lw4ysxo90qp51h FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


--
-- TOC entry 3428 (class 2606 OID 60441)
-- Name: subjects fkl1uivqumvjygd0iu4kugka7f8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fkl1uivqumvjygd0iu4kugka7f8 FOREIGN KEY (teaching_level_id) REFERENCES public.teaching_levels(teaching_level_id);


--
-- TOC entry 3436 (class 2606 OID 60481)
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles(role_id);


--
-- TOC entry 3431 (class 2606 OID 60461)
-- Name: teachers fkpavufmal5lbtc60csriy8sx3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT fkpavufmal5lbtc60csriy8sx3 FOREIGN KEY (id) REFERENCES public.users(id);


--
-- TOC entry 3429 (class 2606 OID 60436)
-- Name: subjects fkqd9uhkd0igk5brwbva8f2y4w1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fkqd9uhkd0igk5brwbva8f2y4w1 FOREIGN KEY (semester_id) REFERENCES public.semester(semester_id);


--
-- TOC entry 3417 (class 2606 OID 60386)
-- Name: grades fkrc0s5tgvm9r4ccxitaqtu88k5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkrc0s5tgvm9r4ccxitaqtu88k5 FOREIGN KEY (subject_id) REFERENCES public.subjects(subject_id);


--
-- TOC entry 3432 (class 2606 OID 60456)
-- Name: teachers fkrgr03njnvpwuktc0mntf8t6o0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT fkrgr03njnvpwuktc0mntf8t6o0 FOREIGN KEY (department_id) REFERENCES public.departments(department_id);


--
-- TOC entry 3435 (class 2606 OID 60476)
-- Name: transcript fksa6xsl78sfqgfdxsoe174r5e3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transcript
    ADD CONSTRAINT fksa6xsl78sfqgfdxsoe174r5e3 FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3430 (class 2606 OID 60446)
-- Name: subjects fksjy6ghvvelraa2w9mhv3bbnys; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subjects
    ADD CONSTRAINT fksjy6ghvvelraa2w9mhv3bbnys FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);


-- Completed on 2025-10-06 11:08:49 WAT

--
-- PostgreSQL database dump complete
--

\unrestrict iTntDtaZsMxGXSk999iBUjZwJVfwRIjaLwdFqqvT5nLPnKqfGYqhj64ELciBakq


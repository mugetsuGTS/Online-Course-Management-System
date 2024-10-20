create table sys_user
(
    id         bigint primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar      not null,
    password   varchar      not null,
    role       varchar(255) not null,
    constraint uq_email unique (email)
);

create sequence seq_sys_user start with 100;
create sequence seq_token start with 100;

-- in this case join table mey not be best but in real life it would be more useful
create table student
(
    id bigint not null references sys_user (id),
    primary key (id)
);

create table instructor
(
    id bigint not null references sys_user (id),
    primary key (id)
);

create table token
(
    id          bigint primary key,
    token       varchar(max) not null,
    token_type  varchar(255) not null,
    revoked     boolean      not null,
    expired     boolean      not null,
    sys_user_id bigint       not null,
    constraint uq_token unique (token),
    constraint fk_sys_user_id foreign key (sys_user_id) references sys_user (id)
);
create sequence seq_category start with 100;
create sequence seq_course start with 100;

create table category
(
    id   bigint primary key,
    name varchar(255) not null
        constraint uk_category_name unique
);

create table course
(
    id                  bigint primary key,
    title               varchar(255) not null,
    description         varchar(511) not null,
    duration_in_minutes bigint       not null,
    publish_date        timestamp    not null,
    difficulty_level    varchar(255),
    instructor_id       bigint       not null,
    constraint fk_course_instructor_id foreign key (instructor_id) references sys_user (id)
);

create sequence seq_course_review start with 100;

create table course_review
(
    id         bigint primary key,
    rating     int    not null,
    comment    varchar(255),
    course_id  bigint not null,
    student_id bigint not null,
    constraint fk_course_review_course_id foreign key (course_id) references course (id),
    constraint fk_course_review_student_id foreign key (student_id) references student (id)
);

create table course_category
(
    course_id   bigint not null
        constraint fk_course_category_course_id references course (id),
    category_id bigint not null
        constraint fk_course_category_category_id references category (id),
    primary key (course_id, category_id)
);

create sequence seq_lesson start with 100;

create table lesson
(
    id                bigint primary key,
    title             varchar(255)  not null,
    short_description varchar(511)  not null,
    content           varchar(5000) not null,
    course_id         bigint        not null,
    constraint fk_lesson_course_id foreign key (course_id) references course (id)
);


create sequence enrollment start with 100;
create table enrollment
(
    course_completed boolean not null,
    course_id        bigint  not null,
    student_id       bigint  not null,
    constraint fk_enrollment_course_id foreign key (course_id) references course (id),
    constraint fk_enrollment_student_id foreign key (student_id) references student (id),
    constraint pk_enrollment_student_course primary key (course_id, student_id)
);

create table enrolment_completed_lesson
(
    lesson_id  bigint not null,
    course_id  bigint not null,
    student_id bigint not null,
    constraint fk_enrollment_competed_enrollment_id foreign key (course_id, student_id)
        references enrollment (course_id, student_id),
    constraint fk_enrollment_competed_lesson_id foreign key (lesson_id) references lesson (id),
    primary key (lesson_id, course_id, student_id)
)
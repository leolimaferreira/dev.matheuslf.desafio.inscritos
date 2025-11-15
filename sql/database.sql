CREATE TYPE public.status_enum AS ENUM
    ('TODO', 'DOING', 'DONE');

ALTER TYPE public.status_enum
    OWNER TO postgres;

CREATE TYPE public.role_enum AS ENUM
    ('ADMIN', 'USER');

ALTER TYPE public.role_enum
    OWNER TO postgres;

CREATE TYPE public.priority_enum AS ENUM
    ('LOW', 'MEDIUM', 'HIGH');

ALTER TYPE public.priority_enum
    OWNER TO postgres;

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email character varying(150) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    enabled boolean NOT NULL DEFAULT true,
    role role_enum NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.projects
(
    id uuid NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default" NOT NULL,
    start_date date NOT NULL,
    end_date date,
    created_at time without time zone NOT NULL,
    updated_at time without time zone NOT NULL,
    owner_id uuid NOT NULL,
    CONSTRAINT projects_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user FOREIGN KEY (owner_id)
    REFERENCES public.users (id) MATCH SIMPLE
                    ON UPDATE NO ACTION
                    ON DELETE NO ACTION
    NOT VALID
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.projects
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.tasks
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    title character varying(150) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status status_enum,
    priority priority_enum,
    due_date date,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    project_id uuid NOT NULL,
    assignee_id uuid NOT NULL,
    CONSTRAINT tasks_pkey PRIMARY KEY (id),
    CONSTRAINT fk_project FOREIGN KEY (project_id)
    REFERENCES public.projects (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE NO ACTION,
    CONSTRAINT fk_user FOREIGN KEY (assignee_id)
    REFERENCES public.users (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE NO ACTION
    NOT VALID
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tasks
    OWNER to postgres;

CREATE INDEX IF NOT EXISTS idx_tasks_project_id
    ON public.tasks USING btree
    (project_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

CREATE TABLE IF NOT EXISTS public.password_recovery_tokens
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    expiration_date timestamp without time zone NOT NULL,
    user_id uuid NOT NULL,
    used boolean NOT NULL,
    CONSTRAINT password_recovery_tokens_pk PRIMARY KEY (id)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.password_recovery_tokens
    OWNER to postgres;

CREATE TABLE user_projects (
                               project_id UUID NOT NULL,
                               user_id UUID NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               PRIMARY KEY (project_id, user_id),
                               CONSTRAINT fk_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
                               CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
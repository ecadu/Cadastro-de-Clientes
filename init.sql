
CREATE DATABASE desafioclients;
\c meu_banco;

-- Criação das sequências
CREATE SEQUENCE IF NOT EXISTS public.clients_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS public.colors_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Criação da tabela colors
CREATE TABLE IF NOT EXISTS public.colors
(
    id integer NOT NULL DEFAULT nextval('colors_id_seq'::regclass),
    colorname character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT colors_pkey PRIMARY KEY (id),
    CONSTRAINT colors_colorname_key UNIQUE (colorname)
)
TABLESPACE pg_default;

-- Inserção de cores na tabela colors
INSERT INTO colors (id, colorname) VALUES
  (1, 'Vermelho'),
  (2, 'Laranja'),
  (3, 'Amarelo'),
  (4, 'Verde'),
  (5, 'Azul'),
  (6, 'Anil'),
  (7, 'Violeta')
ON CONFLICT (id) DO NOTHING;

-- Criação da tabela clients
CREATE TABLE IF NOT EXISTS public.clients
(
    id bigint NOT NULL DEFAULT nextval('clients_id_seq'::regclass),
    clientname character varying(255) COLLATE pg_catalog."default" NOT NULL,
    cpf character varying(30) COLLATE pg_catalog."default" NOT NULL,
    isactive boolean NOT NULL DEFAULT true,
    colorid integer,
    favoritecolor character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT clients_pkey PRIMARY KEY (id),
    CONSTRAINT clients_cpf_key UNIQUE (cpf),
    CONSTRAINT fk_color FOREIGN KEY (colorid)
        REFERENCES public.colors (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;

-- Criação da view clientdetails
CREATE OR REPLACE VIEW public.clientdetails AS
SELECT c.id,
    c.email,
    c.clientname,
    c.cpf,
    c.isactive,
    c.colorid,
    col.colorname AS favoritecolor
FROM clients c
LEFT JOIN colors col ON c.colorid = col.id
WHERE c.isactive = true;

-- Criação do procedimento upsert_client
CREATE OR REPLACE PROCEDURE public.upsert_client(
    IN p_id bigint DEFAULT NULL,
    IN p_clientname character varying DEFAULT NULL,
    IN p_cpf character varying DEFAULT NULL,
    IN p_isactive boolean DEFAULT NULL,
    IN p_colorid integer DEFAULT NULL,
    IN p_email character varying DEFAULT NULL)
LANGUAGE 'plpgsql'
AS $$
DECLARE
    v_existing_id_cpf bigint;
    v_existing_id_email bigint;
BEGIN
    -- Verifica se o CPF já existe para outro cliente
    SELECT id INTO v_existing_id_cpf FROM Clients WHERE cpf = p_cpf AND (id <> p_id OR p_id IS NULL);
    
    IF v_existing_id_cpf IS NOT NULL THEN
        RAISE EXCEPTION 'CPF já cadastrado para outro cliente' USING ERRCODE = '23505';
    END IF;

    -- Verifica se o e-mail já existe para outro cliente
    SELECT id INTO v_existing_id_email FROM Clients WHERE email = p_email AND (id <> p_id OR p_id IS NULL);
    
    IF v_existing_id_email IS NOT NULL THEN
        RAISE EXCEPTION 'E-mail já cadastrado para outro cliente' USING ERRCODE = '23505';
    END IF;

    -- Verifica se o cliente já existe pelo ID
    IF EXISTS (SELECT 1 FROM Clients WHERE id = p_id) THEN
        -- Se existir, faz UPDATE
        UPDATE Clients
        SET clientname = p_clientname,
            cpf = p_cpf,
            isactive = p_isactive,
            colorId = p_colorId,
            email = p_email
        WHERE id = p_id;
    ELSE
        -- Se não existir, faz INSERT
        INSERT INTO Clients (clientname, cpf, isactive, colorId, email)
        VALUES (p_clientname, p_cpf, p_isactive, p_colorId, p_email);
    END IF;
END;
$$;

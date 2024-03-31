CREATE TABLE usuario
(
    id       BIGSERIAL PRIMARY KEY,
    nome     VARCHAR(125) NOT NULL,
    email    VARCHAR(125) NOT NULL,
    telefone VARCHAR(11)  NOT NULL,
    status   BOOLEAN      NOT NULL,
    role     VARCHAR(63)  NOT NULL,
    login    VARCHAR(100) NOT NULL,
    senha    VARCHAR(255) NOT NULL
);

CREATE TABLE reitoria
(
    id          BIGSERIAL PRIMARY KEY,
    cnpj        VARCHAR(14) UNIQUE NOT NULL,
    responsavel VARCHAR(100)       NOT NULL,
    verba       DECIMAL(18, 2)     NOT NULL,
    gasto       DECIMAL(18, 2)     NOT NULL,

    FOREIGN KEY (id) REFERENCES usuario (id)
);

CREATE TABLE departamento
(
    id          BIGSERIAL PRIMARY KEY,
    responsavel VARCHAR(100)   NOT NULL,
    verba       DECIMAL(18, 2) NOT NULL,
    gasto       DECIMAL(18, 2) NOT NULL,

    FOREIGN KEY (id) REFERENCES usuario (id)
);

CREATE TABLE servidor
(
    id        BIGSERIAL PRIMARY KEY,
    matricula VARCHAR(7) UNIQUE NOT NULL,

    FOREIGN KEY (id) REFERENCES usuario (id)
);

CREATE SEQUENCE repasse_seq START 1;

CREATE TABLE repasse_reitoria
(
    id          BIGINT DEFAULT nextval('repasse_seq') PRIMARY KEY,
    data_tempo  TIMESTAMP      NOT NULL,
    valor       DECIMAL(14, 2) NOT NULL,
    fk_reitoria BIGINT         NOT NULL,

    FOREIGN KEY (fk_reitoria) REFERENCES reitoria (id)
);

CREATE TABLE repasse_departamento
(
    id              BIGINT DEFAULT nextval('repasse_seq') PRIMARY KEY,
    data_tempo      TIMESTAMP      NOT NULL,
    valor           DECIMAL(14, 2) NOT NULL,
    fk_reitoria     BIGINT         NOT NULL,
    fk_departamento BIGINT         NOT NULL,

    FOREIGN KEY (fk_reitoria) REFERENCES reitoria (id),
    FOREIGN KEY (fk_departamento) REFERENCES departamento (id)
);

CREATE SEQUENCE documento_seq START 1;

CREATE TABLE documento_servidor
(
    id          BIGINT DEFAULT nextval('documento_seq') PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL,
    tipo        VARCHAR(63)  NOT NULL,
    doc         BYTEA        NOT NULL,
    fk_servidor BIGINT       NOT NULL,

    FOREIGN KEY (fk_servidor) REFERENCES servidor (id)
);

CREATE TABLE documento_departamento
(
    id              BIGINT DEFAULT nextval('documento_seq') PRIMARY KEY,
    nome            VARCHAR(100) NOT NULL,
    tipo            VARCHAR(63)  NOT NULL,
    doc             BYTEA        NOT NULL,
    fk_departamento BIGINT       NOT NULL,

    FOREIGN KEY (fk_departamento) REFERENCES departamento (id)
);

CREATE TABLE evento
(
    id              BIGSERIAL PRIMARY KEY,
    nome            VARCHAR(125)   NOT NULL,
    tipo            VARCHAR(63)    NOT NULL,
    periodicidade   VARCHAR(63)    NOT NULL,
    data_inicio     DATE           NOT NULL,
    data_fim        DATE           NOT NULL,
    data_ida        DATE           NOT NULL,
    data_volta      DATE           NOT NULL,
    objetivo        TEXT           NOT NULL,
    participantes   INT            NOT NULL,
    custo           DECIMAL(12, 2) NOT NULL,
    aporte_dep      DECIMAL(12, 2) NOT NULL,
    aporte_reit     DECIMAL(12, 2) NOT NULL,
    arquivado       BOOLEAN        NOT NULL,
    status          VARCHAR(63)    NOT NULL,
    pais            VARCHAR(100)   NOT NULL,
    estado          VARCHAR(8)     NOT NULL,
    cidade          VARCHAR(100)   NOT NULL,
    bairro          VARCHAR(100)   NOT NULL,
    rua             VARCHAR(100)   NOT NULL,
    numero          VARCHAR(8)     NOT NULL,
    complemento     VARCHAR(255),
    fk_servidor     BIGINT         NOT NULL,
    fk_departamento BIGINT         NOT NULL,
    fk_reitoria     BIGINT,

    FOREIGN KEY (fk_servidor) REFERENCES servidor (id),
    FOREIGN KEY (fk_departamento) REFERENCES departamento (id),
    FOREIGN KEY (fk_reitoria) REFERENCES reitoria (id)
);

CREATE TABLE documento_evento
(
    id        BIGINT DEFAULT nextval('documento_seq') PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    tipo      VARCHAR(63)  NOT NULL,
    doc       BYTEA        NOT NULL,
    fk_evento BIGINT       NOT NULL,

    FOREIGN KEY (fk_evento) REFERENCES evento (id)
);

CREATE TABLE tramite
(
    id         BIGSERIAL PRIMARY KEY,
    data_tempo TIMESTAMP   NOT NULL,
    status     VARCHAR(63) NOT NULL,
    fk_origem  BIGINT      NOT NULL,
    fk_destino BIGINT      NOT NULL,
    fk_evento  BIGINT      NOT NULL,

    FOREIGN KEY (fk_evento) REFERENCES evento (id),
    FOREIGN KEY (fk_origem) REFERENCES usuario (id),
    FOREIGN KEY (fk_destino) REFERENCES usuario (id)
);

CREATE TABLE usuario_tramite
(
    fk_usuario BIGINT NOT NULL,
    fk_tramite BIGINT NOT NULL,
    CONSTRAINT pk_usuario_tramite PRIMARY KEY (fk_usuario, fk_tramite),
    FOREIGN KEY (fk_tramite) REFERENCES tramite (id),
    FOREIGN KEY (fk_usuario) REFERENCES usuario (id)
);

CREATE TABLE despesa
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    tipo      VARCHAR(63)  NOT NULL,
    descricao TEXT         NOT NULL
);

CREATE TABLE despesa_evento
(
    fk_despesa    BIGINT         NOT NULL,
    fk_evento     BIGINT         NOT NULL,
    valor         DECIMAL(12, 2) NOT NULL,
    data_tempo    TIMESTAMP      NOT NULL,
    justificativa TEXT,

    CONSTRAINT pk_despesa_evento PRIMARY KEY (fk_evento, fk_despesa),
    FOREIGN KEY (fk_despesa) REFERENCES despesa (id),
    FOREIGN KEY (fk_evento) REFERENCES evento (id)
);

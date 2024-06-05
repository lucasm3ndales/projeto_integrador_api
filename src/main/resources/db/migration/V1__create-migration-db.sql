CREATE TABLE usuario
(
    id        BIGSERIAL PRIMARY KEY,
    nome      VARCHAR(125) NOT NULL,
    email     VARCHAR(125) NOT NULL UNIQUE,
    telefone  VARCHAR(11)  NOT NULL,
    matricula VARCHAR(7)   NOT NULL UNIQUE,
    ativo     BOOLEAN      NOT NULL,
    role      VARCHAR(63)  NOT NULL,
    login     VARCHAR(100) NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL
);

CREATE TABLE unidade_administrativa
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL UNIQUE,
    tipo       VARCHAR(63)  NOT NULL
);

CREATE TABLE gestor_unidade
(
    id BIGSERIAL PRIMARY KEY,
    fk_usuario BIGINT    NOT NULL,
    fk_unidade BIGINT    NOT NULL,
    assumiu_em TIMESTAMP NOT NULL,

    FOREIGN KEY (fk_unidade) REFERENCES unidade_administrativa (id),
    FOREIGN KEY (fk_usuario) REFERENCES usuario (id)
);

CREATE TABLE orcamento
(
    id    BIGSERIAL PRIMARY KEY,
    verba DECIMAL(18, 2) NOT NULL,
    saldo DECIMAL(18, 2) NOT NULL,
    ano   VARCHAR(4)     NOT NULL,
    fk_unidade BIGINT NOT NULL,

    FOREIGN KEY (fk_unidade) REFERENCES unidade_administrativa(id)
);

CREATE TABLE repasse
(
    id           BIGSERIAL PRIMARY KEY,
    repassado_em TIMESTAMP      NOT NULL,
    valor        DECIMAL(14, 2) NOT NULL,
    fk_origem  BIGINT         NOT NULL,
    fk_destino BIGINT         NOT NULL,

    FOREIGN KEY (fk_origem) REFERENCES unidade_administrativa (id),
    FOREIGN KEY (fk_destino) REFERENCES unidade_administrativa (id)
);

CREATE TABLE endereco
(
    id          BIGSERIAL PRIMARY KEY,
    pais        VARCHAR(100) NOT NULL,
    estado      VARCHAR(8)   NOT NULL,
    cidade      VARCHAR(100) NOT NULL,
    bairro      VARCHAR(100) NOT NULL,
    rua         VARCHAR(100) NOT NULL,
    numero      VARCHAR(8),
    complemento VARCHAR(255)
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
    n_participantes INT            NOT NULL,
    custo           DECIMAL(12, 2) NOT NULL,
    aporte_dep      DECIMAL(12, 2) NOT NULL,
    aporte_reit     DECIMAL(12, 2) NOT NULL,
    arquivado       BOOLEAN        NOT NULL,
    status          VARCHAR(63)    NOT NULL,
    fk_endereco     BIGINT         NOT NULL,

    FOREIGN KEY (fk_endereco) REFERENCES endereco (id)
);

CREATE TABLE tramite
(
    id           BIGSERIAL PRIMARY KEY,
    tramitado_em TIMESTAMP NOT NULL,
    fk_origem    BIGINT    NOT NULL,
    fk_destino   BIGINT    NOT NULL,
    fk_evento    BIGINT    NOT NULL,

    FOREIGN KEY (fk_evento) REFERENCES evento (id),
    FOREIGN KEY (fk_origem) REFERENCES usuario (id),
    FOREIGN KEY (fk_destino) REFERENCES usuario (id)
);

CREATE TABLE documento
(
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    tipo       VARCHAR(63)  NOT NULL,
    doc        BYTEA        NOT NULL,
    extensao   VARCHAR(5)   NOT NULL,
    criado_em  TIMESTAMP    NOT NULL,
    fk_tramite BIGINT       NOT NULL,

    FOREIGN KEY (fk_tramite) REFERENCES tramite (id)
);

CREATE TABLE despesa
(
    id   BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    tipo VARCHAR(63)  NOT NULL
);

CREATE TABLE despesa_evento
(
    id BIGSERIAL PRIMARY KEY,
    fk_despesa    BIGINT         NOT NULL,
    fk_evento     BIGINT         NOT NULL,
    valor         DECIMAL(12, 2) NOT NULL,
    criado_em     TIMESTAMP      NOT NULL,
    atualizado_em TIMESTAMP,
    justificativa TEXT,

    FOREIGN KEY (fk_despesa) REFERENCES despesa (id),
    FOREIGN KEY (fk_evento) REFERENCES evento (id)
);

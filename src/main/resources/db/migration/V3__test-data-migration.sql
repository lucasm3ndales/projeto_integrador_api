-- Inserindo dados na tabela usuario
INSERT INTO usuario (nome, email, telefone, matricula, ativo, role, login, senha)
VALUES ('João Silva', 'joao.silva@example.com', '12345678901', '1234567', TRUE, 'CHEFE_DEPARTAMENTO', 'departamento1',
        '$2a$12$xLPsr/cJa4.9kLOcrFXfIOY4Cvwo.lArEJFoDD37jpfysUu4IPs86'),
       ('Maria Oliveira', 'maria.oliveira@example.com', '10987654321', '7654321', TRUE, 'PRO_REITOR', 'reitor1',
        '$2a$12$xLPsr/cJa4.9kLOcrFXfIOY4Cvwo.lArEJFoDD37jpfysUu4IPs86'),
       ('Carlos Eduardo', 'cadu@example.com', '55916227738', '3847586', TRUE, 'SERVIDOR', 'servidor1',
        '$2a$12$xLPsr/cJa4.9kLOcrFXfIOY4Cvwo.lArEJFoDD37jpfysUu4IPs86');

-- Inserindo dados na tabela unidade_administrativa
INSERT INTO unidade_administrativa (nome, tipo)
VALUES ('Centro de Tecnologia', 'DEPARTAMENTO'),
       ('Pro Reitoria de Inovação', 'REITORIA');

-- Inserindo dados na tabela gestor_unidade
INSERT INTO gestor_unidade (fk_usuario, fk_unidade, assumiu_em, saiu_em)
VALUES (1, 1, '2022-01-01 10:00:00', NULL),
       (2, 2, '2022-01-01 10:00:00', NULL);

-- Inserindo dados na tabela orcamento
INSERT INTO orcamento (verba, saldo, ano, fk_unidade)
VALUES (2000000.00, 200000.00, '2024', 1),
       (5000000.00, 150000.00, '2024', 2);

-- Inserindo dados na tabela repasse
INSERT INTO repasse (repassado_em, valor, fk_origem, fk_destino)
VALUES ('2024-01-15 15:00:00', 50000.00, 1, 2);

-- Inserindo dados na tabela endereco
INSERT INTO endereco (pais, estado, cidade, bairro, rua, numero, complemento)
VALUES ('Brasil', 'SP', 'São Paulo', 'Centro', 'Av. Paulista', '1000', 'Apto 101'),
       ('Brasil', 'RJ', 'Rio de Janeiro', 'Copacabana', 'Rua Atlântica', '2000', NULL);

-- Inserindo dados na tabela evento
INSERT INTO evento (nome, tipo, periodicidade, data_inicio, data_fim, data_ida, data_volta, objetivo, n_participantes,
                    custo, aporte_dep, aporte_reit, arquivado, status, fk_endereco)
VALUES ('Congresso de Tecnologia', 'CONGRESSO', 'ANUALMENTE', '2024-07-10', '2024-07-12', '2024-07-09', '2024-07-13',
        'Disseminar conhecimento tecnológico', 200, 50000.00, 20000.00, 30000.00, FALSE, 'RECUSADO', 1);

-- Inserindo dados na tabela tramite
INSERT INTO tramite (tramitado_em, fk_origem, fk_destino, fk_evento)
VALUES ('2024-06-01 08:00:00', 3, 1, 1),
       ('2024-07-01 12:00:00', 1, 2, 1),
       ('2024-08-01 15:00:00', 2, 3, 1);

-- Inserindo dados na tabela documento
INSERT INTO documento (nome, tipo, doc, extensao, criado_em, fk_tramite)
VALUES ('Relatório Inicial', 'OUTROS', decode('255044462d312e350a25d0d4c5d80a34', 'hex'), 'PDF', '2024-06-01 12:00:00',1);

-- Inserindo dados na tabela despesa
INSERT INTO despesa (nome, tipo)
VALUES ('Aluguel de Equipamentos', 'OUTROS'),
       ('Catering Hotel', 'HOSPEDAGEM');

-- Inserindo dados na tabela despesa_evento
INSERT INTO despesa_evento (fk_despesa, fk_evento, valor, criado_em, atualizado_em, justificativa)
VALUES (1, 1, 15000.00, '2024-07-01 10:00:00', NULL, 'Aluguel de equipamentos para o congresso'),
       (2, 1, 5000.00, '2024-07-01 10:30:00', NULL, 'Serviço de catering para o congresso');

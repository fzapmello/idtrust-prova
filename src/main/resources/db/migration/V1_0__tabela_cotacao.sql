CREATE TABLE public.TB_COTACAO (
	id bigint NOT NULL,
	cotation_date timestamp NOT NULL,
	culture varchar(50) NOT NULL,
	price_USD numeric NOT NULL,
	dolar_BRL numeric NOT NULL,
	price_BRL numeric NOT NULL,
	CONSTRAINT tb_cotacao_pk PRIMARY KEY (id)
);
COMMENT ON TABLE public.TB_COTACAO IS 'Tabela de cotações';
CREATE SEQUENCE seq_tb_cotacao;

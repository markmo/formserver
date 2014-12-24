
CREATE SEQUENCE form_form_id_seq;

CREATE TABLE Form (
                form_id INTEGER NOT NULL DEFAULT nextval('form_form_id_seq'),
                form_name VARCHAR(100) NOT NULL,
                description VARCHAR(255) NULL,
                schema JSON NULL,
                CONSTRAINT form_pk PRIMARY KEY (form_id)
);


ALTER SEQUENCE form_form_id_seq OWNED BY Form.form_id;
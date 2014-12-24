# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "Form" ("form_id" SERIAL PRIMARY KEY,"form_name" VARCHAR(254) NOT NULL,"description" VARCHAR(254),"schema" json);

# --- !Downs

drop table "Form";


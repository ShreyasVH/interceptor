# --- !Ups

create table requests (
  id                            bigint auto_increment not null,
  host                          varchar(255),
  port                          varchar(255),
  path                          varchar(255),
  method                        varchar(255),
  payload                       longtext,
  headers                       longtext,
  response_id                   bigint,
  constraint uq_requests_response_id unique (response_id),
  constraint pk_requests primary key (id)
);

create table responses (
  id                            bigint auto_increment not null,
  headers                       longtext,
  status                        integer not null,
  body                          text,
  duration                      bigint not null,
  constraint pk_responses primary key (id)
);

alter table requests add constraint fk_requests_response_id foreign key (response_id) references responses (id) on delete restrict on update restrict;


# --- !Downs

alter table requests drop foreign key fk_requests_response_id;

drop table if exists requests;

drop table if exists responses;


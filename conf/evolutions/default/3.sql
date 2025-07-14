# --- !Ups

CREATE ALGORITHM=UNDEFINED DEFINER=`shreyas`@`%` SQL SECURITY DEFINER VIEW `All`  AS  select `rq`.`host` AS `host`,`rq`.`port` AS `port`,`rq`.`path` AS `path`,`rq`.`method` AS `method`,`rq`.`payload` AS `payload`,`rq`.`headers` AS `requestHeaders`,`rs`.`status` AS `status`,`rs`.`body` AS `body`,`rs`.`headers` AS `responseHeaders`,`rs`.`duration` AS `duration` from (`interceptor`.`requests` `rq` join `interceptor`.`responses` `rs` on((`rs`.`id` = `rq`.`response_id`))) order by `rq`.`id`;

# --- !Downs

DROP VIEW `All`;
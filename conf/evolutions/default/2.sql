# --- !Ups

CREATE ALGORITHM=UNDEFINED DEFINER=`shreyas`@`%` SQL SECURITY DEFINER VIEW `Errors`  AS  SELECT rq.id AS requestId, rq.host, rq.port, rq.path, rq.method, rq.payload, rq.headers AS requestHeaders, rs.id AS responseId, rs.headers AS responseHeaders, rs.`status`, rs.body, rs.duration FROM requests rq INNER JOIN responses rs ON rs.id = rq.response_id WHERE (rs.`status` = 0 OR (rs.`status` >= 400 AND rs.`status` < 600));

# --- !Downs


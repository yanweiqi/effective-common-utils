DELETE FROM user;
INSERT INTO user (id, name, age, email)
VALUES (1, 'Jone', 18, 'test1@baomidou.com'),
       (2, 'Jack', 20, 'test2@baomidou.com'),
       (3, 'Tom', 28, 'test3@baomidou.com'),
       (4, 'Sandy', 21, 'test4@baomidou.com'),
       (5, 'Billie', 24, 'test5@baomidou.com');

INSERT INTO user2 (id, name, age)
VALUES (1, 'Jone', 18),
       (2, 'Jack', 20),
       (3, 'Tom', 28),
       (4, 'Sandy', 21),
       (5, 'Billie', 24);

DELETE
FROM app;

INSERT INTO app (app_name,
                 os_switch,
                 os_version,
                 app_switch,
                 app_version,
                 push_protocol_version,
                 subscribe_switch,
                 frequency_id,
                 keyword_id,
                 black_list_id,
                 white_list_id,
                 yn,
                 erp
                 )
VALUES ('jdt', 0, '1.0.1', 1, '2.0.0','3.0.0', 1, 11, 12, 13, 14, 1,'ywq');

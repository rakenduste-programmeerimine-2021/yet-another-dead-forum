INSERT INTO public.role(name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_PREMIUM') ON CONFLICT DO NOTHING;

-- username 'Testing', password is 'test'
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', null, 'test@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', null, 'Testing') ON CONFLICT DO NOTHING;
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', null, 'foo@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', null, 'Foo') ON CONFLICT DO NOTHING;
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', null, 'bar@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', null, 'Bar') ON CONFLICT DO NOTHING;

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    1, 1
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 1 and roles_id = 1
    );

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    2, 1
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 2 and roles_id = 1
    );

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    3, 1
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 3 and roles_id = 1
    );

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    3, 3
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 3 and roles_id = 3
    );

-- threads
INSERT INTO public.thread(text, title, user_id) VALUES (
    'Somebody please elaborate, I''m tired of the double standards.',
    'Why do people say they “slept like a baby” when babies often wake up like every two hours?',
    1) ON CONFLICT DO NOTHING;

INSERT INTO public.thread(text, title, user_id) VALUES (
   'I think it''s not really the sound, but the feeling of clicking a pen that is actually satisfying. It''s like working a fidget spinner; there''s something really soothing about it. When someone else is doing it, you hear the sound and expect the satisfying click-click feeling on your fingers but don''t, hence the annoyance.',
   'You only like the pen clicking noise when you are the one making it',
   1) ON CONFLICT DO NOTHING;

INSERT INTO public.thread(text, title, user_id) VALUES (
   'It''s over there by the baby food',
   'If you don''t know what it is, toothpaste sounds pretty terrifying',
   2) ON CONFLICT DO NOTHING;

INSERT INTO public.thread(text, title, user_id) VALUES (
   'Yup. Also, the long title is on purpose :). No posts here!',
   'As data showing the effects of sugar on the human body become more well known, future generations will look back with horror on our practice of sending children out to trick-or-treat.',
   3) ON CONFLICT DO NOTHING;

INSERT INTO public.thread(text, title, user_id) VALUES (
   'A perpetual stew, also known as hunter''s pot or hunter''s stew, is a pot into which whatever one can find is placed and cooked. The pot is never or rarely emptied all the way, and ingredients and liquid are replenished as necessary. The concept is often a common element in descriptions of medieval inns. Foods prepared in a perpetual stew have been described as being flavorful due to the manner in which the ingredients blend together, in which the flavor may improve with age.',
   'A soup can''t go bad if you just never stop boiling it...',
   2) ON CONFLICT DO NOTHING;



-- posts

-- posts for thread 1
INSERT INTO public.post(text, user_id, thread_id) VALUES (
  'Basically my university sleep schedule lol',
  2,
  1);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Eh.. I most likely get less sleep as a parent.',
     3,
     1);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'You guys get sleep???.',
     1,
     1);

-- posts for thread 2
INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Ticking of a clock is the same as clicking pens, they seem to get louder when you pay attention and then it is all you can hear.',
     2,
     2);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'There are tons of noises that are only okay if you’re the one making the noise',
     3,
     2);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Humming. Whistling. Doopdeedoopin.',
     1,
     2);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'elaborate on "Doopdeedoopin"',
     3,
     2);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'doop dee doop de dadla do',
     1,
     2);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     '@Testing gets it.',
     2,
     2);

-- posts for thread 3
INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Not at all.. it sounds like something you could buy at a hardware store to repair your teeth with.. Broke a tooth? Get some toothpaste and patch it up.',
     1,
     3);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     '@Testing Maybe it''s a paste made out of tooth',
     2,
     3);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Not as bad as baby oil...',
     3,
     3);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     '@Bar That''s just crude',
     2,
     3);

INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Now I hate baby oil.',
     1,
     3);

-- thread 4 is empty

-- posts for thread 5
INSERT INTO public.post(text, user_id, thread_id) VALUES (
     'Reserved.',
     2,
     5);

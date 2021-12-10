INSERT INTO public.role(name, body_css, text_css) VALUES ('ROLE_USER', '#5D90CB', '#FFFFFF') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name, body_css, text_css) VALUES ('ROLE_ADMIN', '#C33131', '#FFFFFF') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name, body_css, text_css) VALUES ('ROLE_MODERATOR', '#3CAD39', '#FFFFFF') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name, body_css, text_css) VALUES ('ROLE_PREMIUM', '#9C59C1', '#FFFFFF') ON CONFLICT DO NOTHING;

-- username 'Testing', password is 'test'
-- username 'imincharge', password is 'test' - admin
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username, display_name, visits) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', '', 'test@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', '', 'testing', 'TesTinG', 0) ON CONFLICT DO NOTHING;
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username, display_name, visits) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', '', 'foo@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', '', 'foo', 'Foo', 0) ON CONFLICT DO NOTHING;
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username, display_name, visits) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', '', 'bar@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', '', 'bar', 'BAR', 0) ON CONFLICT DO NOTHING;
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username, display_name, visits) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', '', 'boss@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', '', 'imincharge', 'ImInCharge', 0) ON CONFLICT DO NOTHING;

INSERT INTO public.maintenance (id, message)
SELECT 1, ''
WHERE NOT EXISTS (SELECT 1 FROM public.maintenance WHERE id = 1)
ON CONFLICT DO NOTHING;

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

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    4, 1
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 4 and roles_id = 1
    );

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    4, 2
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 4 and roles_id = 2
    );

-- threads
INSERT INTO public.thread(text, title, user_id) SELECT
    'Somebody please elaborate, I''m tired of the double standards.',
    'Why do people say they “slept like a baby” when babies often wake up like every two hours?',
    1
WHERE NOT EXISTS (
        SELECT 1 FROM public.thread WHERE thread.id = 1
    );

INSERT INTO public.thread(text, title, user_id) SELECT
   'I think it''s not really the sound, but the feeling of clicking a pen that is actually satisfying. It''s like working a fidget spinner; there''s something really soothing about it. When someone else is doing it, you hear the sound and expect the satisfying click-click feeling on your fingers but don''t, hence the annoyance.',
   'You only like the pen clicking noise when you are the one making it',
   1
WHERE NOT EXISTS (
        SELECT 1 FROM public.thread WHERE thread.id = 2
    );

INSERT INTO public.thread(text, title, user_id) SELECT
   'It''s over there by the baby food',
   'If you don''t know what it is, toothpaste sounds pretty terrifying',
   2
WHERE NOT EXISTS (
        SELECT 1 FROM public.thread WHERE thread.id = 3
    );

INSERT INTO public.thread(text, title, user_id) SELECT
   'Yup. Also, the long title is on purpose :). No posts here!',
   'As data showing the effects of sugar on the human body become more well known, future generations will look back with horror on our practice of sending children out to trick-or-treat.',
   3
WHERE NOT EXISTS (
        SELECT 1 FROM public.thread WHERE thread.id = 4
    );

INSERT INTO public.thread(text, title, user_id) SELECT
   'A perpetual stew, also known as hunter''s pot or hunter''s stew, is a pot into which whatever one can find is placed and cooked. The pot is never or rarely emptied all the way, and ingredients and liquid are replenished as necessary. The concept is often a common element in descriptions of medieval inns. Foods prepared in a perpetual stew have been described as being flavorful due to the manner in which the ingredients blend together, in which the flavor may improve with age.',
   'A soup can''t go bad if you just never stop boiling it...',
   2
WHERE NOT EXISTS (
        SELECT 1 FROM public.thread WHERE thread.id = 5
);



-- posts

-- posts for thread 1
INSERT INTO public.post(text, user_id, thread_id) SELECT
  'Basically my university sleep schedule lol',
  2,
  1
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 1
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Eh.. I most likely get less sleep as a parent.',
     3,
     1
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 2
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'You guys get sleep???.',
     1,
     1
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 3
    );

-- posts for thread 2
INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Ticking of a clock is the same as clicking pens, they seem to get louder when you pay attention and then it is all you can hear.',
     2,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 4
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'There are tons of noises that are only okay if you’re the one making the noise',
     3,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 5
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Humming. Whistling. Doopdeedoopin.',
     1,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 6
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'elaborate on "Doopdeedoopin"',
     3,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 7
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'doop dee doop de dadla do',
     1,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 8
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     '@Testing gets it.',
     2,
     2
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 9
    );

-- posts for thread 3
INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Not at all.. it sounds like something you could buy at a hardware store to repair your teeth with.. Broke a tooth? Get some toothpaste and patch it up.',
     1,
     3
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 10
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     '@Testing Maybe it''s a paste made out of tooth',
     2,
     3
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 11
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Not as bad as baby oil...',
     3,
     3
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 12
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     '@Bar That''s just crude',
     2,
     3
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 13
    );

INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Now I hate baby oil.',
     1,
     3
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 14
    );

-- thread 4 is empty

-- posts for thread 5
INSERT INTO public.post(text, user_id, thread_id) SELECT
     'Reserved.',
     2,
     5
WHERE NOT EXISTS (
        SELECT 1 FROM public.post WHERE post.id = 15
    );

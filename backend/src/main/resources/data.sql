INSERT INTO public.role(name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO public.role(name) VALUES ('ROLE_PREMIUM') ON CONFLICT DO NOTHING;

-- username 'Testing', password is 'test'
INSERT INTO public.users (created_at, updated_at, about, email, password, signature, username) VALUES ('2021-11-20 21:22:36.115454', '2021-11-20 21:22:36.116453', null, 'test@test.com', '$2a$10$xnUvv01NPMsvYQEYINiACOZTADPCoD9N6jjd7LSS4tYoYPZNdmXEG', null, 'Testing') ON CONFLICT DO NOTHING;

INSERT INTO public.users_roles (user_id, roles_id)
SELECT
    1, 1
WHERE NOT EXISTS (
        SELECT 1 FROM public.users_roles WHERE user_id = 1 and roles_id = 1
    );
-- 初始化用户数据
INSERT INTO "user" (id, username, password, roles) VALUES (1, 'user', '{noop}lishirui', 'ROLE_USER');
INSERT INTO "user" (id, username, password, roles) VALUES (2, 'admin', '{noop}lishirui', 'ROLE_ADMIN,ROLE_USER');
-- {noop} 前缀表示密码是明文，Spring Security 不会对其进行编码。生产环境请使用BCryptPasswordEncoder等加密。
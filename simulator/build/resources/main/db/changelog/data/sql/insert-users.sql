INSERT INTO users (username, password) VALUES ('admin', '{bcrypt}$2y$10$qlw57MYBm6gKLbeggue8Z.BKDJ6E19wbSc/dIeMwS.IWtRSgAMMOO');
INSERT INTO users_authority (users_username, authorities) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO users (username, password) VALUES ('user', '{bcrypt}$2y$10$zHdGbDX0vAohNGUa7LgQQeVbbecwreQ7LSxyM5Uiv8W6B32QsN/vy');
INSERT INTO users_authority (users_username, authorities) VALUES ('user', 'ROLE_USER');
INSERT INTO users (username, password) VALUES ('manager', '{bcrypt}$2y$10$CUxrYxRThlhSC5/0Rj3hKejXrMYAPZYGihktqMdCGF9RpDcXQE7gC');
INSERT INTO users_authority (users_username, authorities) VALUES ('manager', 'ROLE_MANAGER');

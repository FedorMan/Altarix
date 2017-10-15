CREATE TABLE departments(
  id BIGINT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  create_by DATE NOT NULL,
  parent_department_id BIGINT REFERENCES departments(id)
);

CREATE TABLE positions(
  id BIGINT PRIMARY KEY,
  lastname VARCHAR(50) NOT NULL
);

CREATE TABLE employes(
  id BIGINT PRIMARY KEY,
  lastname VARCHAR(30) NOT NULL,
  firstname VARCHAR(30) NOT NULL,
  patronymic VARCHAR(30) NOT NULL,
  sex VARCHAR(5) NOT NULL,
  birthday DATE NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  position_id BIGINT NOT NULL REFERENCES positions(id),
  salary DOUBLE PRECISION NOT NULL,
  department_id BIGINT NOT NULL REFERENCES departments(id),
  main BOOLEAN
);

CREATE TABLE history_departments(
  id BIGINT PRIMARY KEY,
  department_id BIGINT NOT NULL,
  old_name VARCHAR(100),
  old_parent_department_id BIGINT,
  new_name VARCHAR(100),
  new_parent_department_id BIGINT,
  time DATE NOT NULL
);

CREATE TABLE salary_departments(
  id BIGINT PRIMARY KEY,
  department_id BIGINT NOT NULL REFERENCES departments(id) ON DELETE CASCADE,
  salary DOUBLE PRECISION NOT NULL,
  time DATE NOT NULL
);
CREATE TABLE departments(
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  create_by DATE,
  parent_department_id BIGINT REFERENCES departments(id)
);

CREATE TABLE positions(
  id BIGINT PRIMARY KEY,
  lastname VARCHAR(50)
);

CREATE TABLE employes(
  id BIGINT PRIMARY KEY,
  lastname VARCHAR(30),
  firstname VARCHAR(30),
  patronymic VARCHAR(30),
  sex VARCHAR(5),
  birthday DATE,
  start_date DATE,
  end_date DATE,
  position_id BIGINT REFERENCES positions(id),
  salary DOUBLE PRECISION,
  department_id BIGINT REFERENCES departments(id),
  main BOOLEAN
);
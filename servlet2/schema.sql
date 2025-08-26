-- Create the car table
CREATE TABLE IF NOT EXISTS car (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    make TEXT NOT NULL,
    model TEXT NOT NULL,
    year INTEGER NOT NULL
);

-- Insert 3 example cars
INSERT INTO car (make, model, year) VALUES ('Toyota', 'Corolla', 2019);
INSERT INTO car (make, model, year) VALUES ('Honda', 'Civic', 2020);
INSERT INTO car (make, model, year) VALUES ('Ford', 'Mustang', 2018);

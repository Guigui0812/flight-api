INSERT INTO flights (id, number, origin, destination, departure_date, departure_time, arrival_date, arrival_time, planeId)
VALUES (NEXTVAL('flights_sequence_in_database'), 'AF101', 'Paris', 'New York', '2023-12-01', '07:00', '2023-12-01', '10:00', 1);

INSERT INTO flights (id, number, origin, destination, departure_date, departure_time, arrival_date, arrival_time, planeId)
VALUES (NEXTVAL('flights_sequence_in_database'), 'BA202', 'London', 'Tokyo', '2023-12-02', '09:00', '2023-12-03', '09:00', 2);
-- Insert into user_ table
INSERT INTO user_ (
    last_name, first_name, email, status, date_of_birth,
    phone, password, role, last_connected, created_by
) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', 'ACTIVE', '1985-04-15', '+33612345678', 'Mdp123!', 'ADMIN', '2025-05-08 14:30:00', 'system'),
('Martin', 'Sophie', 'sophie.martin@email.com', 'ACTIVE', '1990-07-22', '+33623456789', 'Password456!', 'CLIENT', '2025-05-07 09:15:00', 'system'),
('Dubois', 'Pierre', 'pierre.dubois@email.com', 'INACTIVE', '1978-11-30', '+33634567890', 'Secret789!', 'CLIENT', '2025-04-20 17:45:00', 'system'),
('Lefebvre', 'Marie', 'marie.lefebvre@email.com', 'BANNED', '1995-02-18', '+33645678901', 'Block321!', 'CLIENT', NULL, 'system'),
('Moreau', 'Thomas', 'thomas.moreau@email.com', 'ACTIVE', '1982-09-05', '+33656789012', 'Space2025!', 'ADMIN', '2025-05-09 08:30:00', 'system');

-- Insert into planet table
INSERT INTO planet (name, created_by)
VALUES
('MERCURY', 'system'),
('VENUS', 'system'),
('EARTH', 'system'),
('MARS', 'system'),
('JUPITER', 'system'),
('SATURN', 'system'),
('URANUS', 'system'),
('NEPTUNE', 'system'),
('PLUTO', 'system');

-- Insert into spaceport table
INSERT INTO spaceport (name, created_by, id_planet)
VALUES
('Caloris Port', 'system', 1),
('Aphrodite Port', 'system', 2),
('Kennedy Space Center', 'system', 3),
('Beijing-Luna Port', 'system', 3),
('Olympus Mons Base', 'system', 4),
('Elysium Station', 'system', 4),
('Europa Orbital', 'system', 5),
('North Ring', 'system', 6),
('Titania Station', 'system', 7),
('Triton Bay', 'system', 8),
('Charon Outpost', 'system', 9);

-- Insert into flight table
INSERT INTO flight (
    num_flight, date_departure, date_arrival, seats, seats_available,
    status, price, created_by, departure_spaceport_id, arrival_spaceport_id
) VALUES
('EM-1001', '2025-06-15 08:00:00', '2025-06-15 14:30:00', 150, 147, 'SCHEDULED', 1200.00, 'system', 4, 6),
('MS-2030', '2025-06-20 10:15:00', '2025-06-20 16:45:00', 150, 150, 'SCHEDULED', 1300.00, 'system', 6, 4),
('TJ-4501', '2025-06-18 07:30:00', '2025-06-19 18:45:00', 200, 185, 'SCHEDULED', 3500.00, 'system', 3, 7),
('JT-4502', '2025-06-22 09:00:00', '2025-06-23 20:15:00', 200, 200, 'SCHEDULED', 3700.00, 'system', 7, 3),
('TS-6070', '2025-07-01 11:45:00', '2025-07-02 13:30:00', 180, 176, 'SCHEDULED', 4200.00, 'system', 4, 8),
('VM-3010', '2025-06-25 06:00:00', '2025-06-25 09:15:00', 120, 118, 'SCHEDULED', 900.00, 'system', 2, 5),
('MV-3011', '2025-06-30 14:30:00', '2025-06-30 17:45:00', 120, 120, 'SCHEDULED', 950.00, 'system', 5, 2),
('TM-1005', '2025-06-17 09:45:00', '2025-06-17 16:15:00', 150, 148, 'SCHEDULED', 1250.00, 'system', 3, 5);

-- Insert into payment_card table
INSERT INTO payment_card (
    id_stripe, last_digit, brand, exp_month, exp_year, is_default,
    created_by, last_modification_date, last_modification_by,
    version, id_user
) VALUES
('card_stripe_001', '1234', 'Visa', 12, 2026, true, 'system', NOW(), 'admin', 0, 1),
('card_stripe_002', '5678', 'MasterCard', 5, 2025, false, 'system', NULL, NULL, 0, 1),
('card_stripe_003', '4321', 'American Express', 7, 2027, false, 'admin_user', NOW(), 'admin_user', 0, 2);

-- Insert into booking table
INSERT INTO booking (
    seat_number, price, first_name_passenger, number_passenger,
    passenger_age, gender, status, created_by, id_flight, id_user
) VALUES
(15, 1200.00, 'Sophie', 'PASS-001', 35, 0, 'CONFIRMED', 'system', 1, 2),
(16, 1200.00, 'Marc', 'PASS-002', 37, 1, 'CONFIRMED', 'system', 1, 2),
(42, 1200.00, 'Pierre', 'PASS-003', 47, 1, 'CONFIRMED', 'system', 1, 3),
(25, 3500.00, 'Pierre', 'PASS-004', 47, 1, 'CANCELLED', 'system', 3, 3),
(10, 900.00, 'Sophie', 'PASS-005', 35, 0, 'CONFIRMED', 'system', 6, 2),
(11, 900.00, 'Marc', 'PASS-006', 37, 1, 'CONFIRMED', 'system', 6, 2),
(50, 1250.00, 'Thomas', 'PASS-007', 43, 1, 'CONFIRMED', 'system', 8, 5),
(51, 1250.00, 'Camille', 'PASS-008', 40, 0, 'CONFIRMED', 'system', 8, 5);

-- Insert into invoice table
INSERT INTO invoice (
    invoice_number, invoice_data, date_creation, created_by, id_booking, id_user
) VALUES
('INV-2025-001', '{"total": 2400.00, "tax": 400.00, "items": [{"description": "Flight ticket EM-1001", "quantity": 2, "price": 1200.00}]}', '2025-05-01 10:30:00', 'system', 1, 2),
('INV-2025-002', '{"total": 1200.00, "tax": 200.00, "items": [{"description": "Flight ticket EM-1001", "quantity": 1, "price": 1200.00}]}', '2025-05-02 14:15:00', 'system', 3, 3),
('INV-2025-003', '{"total": 1800.00, "tax": 300.00, "items": [{"description": "Flight ticket VM-3010", "quantity": 2, "price": 900.00}]}', '2025-05-05 09:45:00', 'system', 5, 2),
('INV-2025-004', '{"total": 2500.00, "tax": 416.67, "items": [{"description": "Flight ticket TM-1005", "quantity": 2, "price": 1250.00}]}', '2025-05-08 11:20:00', 'system', 7, 5);

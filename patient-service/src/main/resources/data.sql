INSERT IGNORE INTO gender (id, code, label) VALUES
    (1, 'M', 'Masculin'),
    (2, 'F', 'Féminin');

INSERT IGNORE INTO patient (id, first_name, last_name, birth_date, gender_id, address, phone) VALUES
    (1, 'Test', 'TestNone',       '1966-12-31', 2, '1 Brookside St', '100-222-3333'),
    (2, 'Test', 'TestBorderline', '1945-06-24', 1, '2 High St',      '200-333-4444'),
    (3, 'Test', 'TestInDanger',   '2004-06-18', 1, '3 Club Road',    '300-444-5555'),
    (4, 'Test', 'TestEarlyOnset', '2002-06-28', 2, '4 Valley Dr',    '400-555-6666');

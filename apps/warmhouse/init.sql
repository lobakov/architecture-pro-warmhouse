CREATE DATABASE smart_home;

CREATE TABLE IF NOT EXISTS public.sensors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(100) NOT NULL,
    value FLOAT DEFAULT 0,
    unit VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'inactive',
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_sensors_type ON public.sensors(type);
CREATE INDEX IF NOT EXISTS idx_sensors_location ON public.sensors(location);
CREATE INDEX IF NOT EXISTS idx_sensors_status ON public.sensors(status);


CREATE SCHEMA IF NOT EXISTS warmhouse;

CREATE TABLE IF NOT EXISTS warmhouse.sensor_type (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS warmhouse.sensor_status (
    id BIGSERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS warmhouse.sensor (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    home_id UUID NOT NULL,
    sensor_type_id BIGINT NOT NULL REFERENCES warmhouse.sensor_type(id),
    sensor_status_id BIGINT NOT NULL REFERENCES warmhouse.sensor_status(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sensor_home_id ON warmhouse.sensor(home_id);
CREATE INDEX idx_sensor_type_id ON warmhouse.sensor(sensor_type_id);

CREATE TABLE IF NOT EXISTS warmhouse.sensor_command (
    id BIGSERIAL PRIMARY KEY,
    sensor_type_id BIGINT NOT NULL REFERENCES warmhouse.sensor_type(id),
    command VARCHAR(50) NOT NULL,
    value TEXT
);

CREATE TABLE IF NOT EXISTS warmhouse.sensor_type_indicator (
    id BIGSERIAL PRIMARY KEY,
    sensor_type_id BIGINT NOT NULL REFERENCES warmhouse.sensor_type(id),
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS warmhouse.sensor_indicator (
    id BIGSERIAL PRIMARY KEY,
    sensor_id UUID NOT NULL REFERENCES warmhouse.sensor(id) ON DELETE CASCADE,
    sensor_type_id BIGINT NOT NULL REFERENCES warmhouse.sensor_type(id),
    name VARCHAR(50) NOT NULL
);

CREATE INDEX idx_sensor_indicator_sensor_id ON warmhouse.sensor_indicator(sensor_id);

CREATE TABLE IF NOT EXISTS warmhouse.sensor_telemetry (
    id BIGSERIAL PRIMARY KEY,
    sensor_indicator_id BIGINT NOT NULL REFERENCES warmhouse.sensor_indicator(id) ON DELETE CASCADE,
    value TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sensor_telemetry_indicator_id ON warmhouse.sensor_telemetry(sensor_indicator_id);
CREATE INDEX idx_sensor_telemetry_timestamp ON warmhouse.sensor_telemetry(timestamp);

INSERT INTO warmhouse.sensor_type (id, type) VALUES
(1, 'temperature'),
(2, 'humidity'),
(3, 'movement'),
(4, 'smoke');

INSERT INTO warmhouse.sensor_status (id, status) VALUES
(1, 'on'),
(2, 'off'),
(3, 'failure'),
(4, 'no_connection');

INSERT INTO warmhouse.sensor_command (sensor_type_id, command, value) VALUES
(1, 'calibrate', NULL),
(1, 'read', NULL),
(3, 'arm', NULL),
(3, 'disarm', NULL),
(4, 'test', NULL),
(4, 'silence', NULL);

INSERT INTO warmhouse.sensor_type_indicator (sensor_type_id, name) VALUES
(1, 'temperature'),
(2, 'humidity'),
(3, 'movement'),
(4, 'smoke'),
(4, 'battery');

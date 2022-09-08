CREATE TABLE IF NOT EXISTS translate_requests(
    id IDENTITY,
    request_date timestamp not null,
    source_language_code varchar,
    target_language_code varchar not null,
    input_text varchar not null,
    output_text varchar not null,
    ip varchar(15)
);
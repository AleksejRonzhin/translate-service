CREATE TABLE IF NOT EXISTS translate_requests(
    id identity,
    request_date timestamp not null,
    source_language_code varchar,
    target_language_code varchar not null,
    input_text varchar not null,
    output_text varchar not null,
    ip varchar(15)
);

CREATE TABLE IF NOT EXISTS translations(
    id identity,
    request_id int not null,
    word varchar,
    translated_word varchar
)
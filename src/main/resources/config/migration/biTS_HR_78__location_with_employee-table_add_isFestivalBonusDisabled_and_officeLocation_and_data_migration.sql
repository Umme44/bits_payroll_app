-- add column in Employee table
ALTER TABLE EMPLOYEE
    ADD COLUMN IF NOT EXISTS IS_FESTIVAL_BONUS_DISABLED BOOLEAN default false;


-- add new column in attendance_entry table
ALTER TABLE attendance_entry
    ADD COLUMN IF NOT EXISTS punch_in_device_origin VARCHAR(255);

ALTER TABLE attendance_entry
    ADD COLUMN IF NOT EXISTS punch_out_device_origin VARCHAR(255);

ALTER TABLE attendance_entry
    ADD COLUMN IF NOT EXISTS is_auto_punch_out BOOLEAN default false;


-- create new table location
CREATE TABLE IF NOT EXISTS location (
    id BIGINT not null primary key,
    location_type VARCHAR(255),
    location_code VARCHAR(250),
    location_name VARCHAR(250),
    has_parent BOOLEAN DEFAULT FALSE,
    parent_id BIGINT,
    is_last_child BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    update_at TIMESTAMP,
    created_by_id BIGINT,
    updated_by_id BIGINT,
    FOREIGN KEY (parent_id) REFERENCES location (id)
    );


--Add relation employee to location
alter table employee
    ADD COLUMN IF NOT EXISTS office_location_id BIGINT,

    add constraint FK_EMPLOYEE_OFFICE_LOCATION
    foreign key (office_location_id) references LOCATION;


--Data migrations

--1. 11:59(23:59:00.000000) -> autoPunchOut
update attendance_entry
set is_auto_punch_out=true
where out_time:: timestamp:: time='23:59:00.000000';

--2. Note=‘System’ -> origin=DEVICE (IN/OUT)
update attendance_entry
set punch_in_device_origin='DEVICE'
where in_note='System';

update attendance_entry
set punch_out_device_origin='DEVICE'
where out_note='System';

--3. Time!=null and Note!=’System’ -> origin=WEB (IN/OUT)
update attendance_entry
set punch_in_device_origin='WEB'
where in_time is not null
  and (in_note is null or in_note <> 'System');

update attendance_entry
set punch_out_device_origin='WEB'
where out_time is not null
  and (out_note is null or out_note <> 'System');

--4. FestivalBonusDisable -> false
update employee
set is_festival_bonus_disabled=false
where is_festival_bonus_disabled is null;


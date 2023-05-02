/**
 * Author:  TEGA
 * Created: Apr 29, 2023
 */
CREATE  TABLE password_reset_token ( 
	id                   bigserial PRIMARY KEY NOT NULL  ,
	created_at           timestamptz  NOT NULL  ,
	email                varchar(255)  NOT NULL UNIQUE ,
	expires_at           timestamp  NOT NULL  ,
	reset_token          varchar(255)  NOT NULL UNIQUE
 );

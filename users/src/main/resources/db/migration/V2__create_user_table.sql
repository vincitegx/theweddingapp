/**
 * Author:  TEGA
 * Created: Apr 29, 2023
 */
CREATE  TABLE users ( 
	id                   bigserial PRIMARY KEY NOT NULL  ,
	created_at           timestamp  NOT NULL  ,
	email                varchar(255)  NOT NULL UNIQUE ,
	enabled              boolean  NOT NULL  ,
	non_locked           boolean  NOT NULL  ,
	password             varchar(255)  NOT NULL
 );

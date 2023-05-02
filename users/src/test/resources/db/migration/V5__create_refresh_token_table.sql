/**
 * Author:  TEGA
 * Created: Apr 29, 2023
 */
CREATE  TABLE refresh_tokens ( 
	id                   bigserial PRIMARY KEY NOT NULL  ,
	created_at           timestamp  NOT NULL  ,
	expiry_at            timestamp  NOT NULL  ,
	token                varchar(255)  NOT NULL UNIQUE ,
	user_id              bigint  NOT NULL  REFERENCES users(id)
 );

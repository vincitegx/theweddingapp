/**
 * Author:  TEGA
 * Created: Apr 29, 2023
 */
CREATE  TABLE email_verification_token ( 
	id                   bigserial PRIMARY KEY NOT NULL  ,
	created_at           timestamp  NOT NULL  ,
	expires_at           timestamp  NOT NULL  ,
	token                varchar(255)  NOT NULL UNIQUE ,
	user_id              bigint  NOT NULL REFERENCES users(id)
 );

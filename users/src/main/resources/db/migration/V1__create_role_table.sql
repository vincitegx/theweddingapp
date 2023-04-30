/**
 * Author:  TEGA
 * Created: Apr 28, 2023
 */
CREATE  TABLE roles ( 
	role_id              bigserial PRIMARY KEY NOT NULL ,
	name                 varchar(255)  NOT NULL UNIQUE 
 );

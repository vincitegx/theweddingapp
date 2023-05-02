/**
 * Author:  TEGA
 * Created: Apr 29, 2023
 */
CREATE  TABLE users_role ( 
	user_id              bigint  NOT NULL REFERENCES users(id) ,
	role_id              bigint  NOT NULL REFERENCES roles(role_id)
);

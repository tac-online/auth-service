#!/bin/sh
#/tac-auth-service/bin/tac-auth-service db status config.yml
#mkdir dumps
#/tac-auth-service/bin/tac-auth-service db dump config.yml > "dumps/$(date +%Y-%m-%d_%H-%M-%S)"
#/tac-auth-service/bin/tac-auth-service db tag config.yml "$(date +%Y-%m-%d_%H-%M-%S)"
/tac-auth-service/bin/tac-auth-service db migrate config.yml
#/tac-auth-service/bin/tac-auth-service db status config.yml
/tac-auth-service/bin/tac-auth-service server config.yml


Response Parsing Story

Narrative:
In order to communicate with the recognizer server
As a developer
I want to ensure that the response correctly parsed

Scenario: A response of succesful recognition arrives
Given that the response arrives
When the response is
OK result_string="       52        52	GRM_ID	# GRM_ID=lex_sp_00149.flx	#	null_lk= 0.000000 edge_lk= 0.000000 end_lk= 0.000000
       52      1182	szellozes	#	frm_lk= -52.803795 edge_lk= -5966.828786 end_lk= -5966.828786
     1182      1512	<NEVER_PRINT_THIS>	#	frm_lk= -45.697297 edge_lk= -1508.010794 end_lk= -7474.839580 "
Then the match decoded is szellozes

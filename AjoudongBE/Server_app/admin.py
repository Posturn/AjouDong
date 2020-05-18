from django.contrib import admin
from Server_app.models import userAccount
from Server_app.models import managerAccount
from Server_app.models import club
from Server_app.models import clubPromotion
from Server_app.models import clubActivity
from Server_app.models import major_Affiliation
# Register your models here.


admin.site.register(userAccount)
admin.site.register(managerAccount)
admin.site.register(club)
admin.site.register(clubPromotion)
admin.site.register(clubActivity)
admin.site.register(major_Affiliation)

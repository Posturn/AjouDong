from django.contrib import admin
from Server_app.models import UserAccount
from Server_app.models import ManagerAccount
from Server_app.models import Club
from Server_app.models import ClubPromotion
from Server_app.models import ClubActivity
from Server_app.models import Major_Affiliation
from Server_app.models import MarkedClubList
# Register your models here.


admin.site.register(UserAccount)
admin.site.register(ManagerAccount)
admin.site.register(Club)
admin.site.register(ClubPromotion)
admin.site.register(ClubActivity)
admin.site.register(Major_Affiliation)
admin.site.register(MarkedClubList)

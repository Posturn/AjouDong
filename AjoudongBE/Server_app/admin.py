from django.contrib import admin
from Server_app.models import UserAccount
from Server_app.models import ManagerAccount
from Server_app.models import Club
from Server_app.models import ClubPromotion
from Server_app.models import ClubActivity
from Server_app.models import Major_Affiliation
from Server_app.models import ClubMember
from Server_app.models import AppliedClubList
from Server_app.models import Apply
from Server_app.models import MarkedClubList
from Server_app.models import Tag
from Server_app.models import TaggedClubList
from Server_app.models import Event
# Register your models here.


admin.site.register(UserAccount)
admin.site.register(ManagerAccount)
admin.site.register(Club)
admin.site.register(ClubPromotion)
admin.site.register(ClubActivity)
admin.site.register(Major_Affiliation)
admin.site.register(ClubMember)
admin.site.register(AppliedClubList)
admin.site.register(Apply)
admin.site.register(MarkedClubList)
admin.site.register(Tag)
admin.site.register(TaggedClubList)
admin.site.register(Event)

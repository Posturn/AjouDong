from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt
from Server_app import views
from rest_framework import routers


from Server_app.login import login
from Server_app.signup import signup 
from Server_app.alarm import alarm

router=routers.DefaultRouter()
router.register(r'SERVER_APP/Major_affiliations', views.MajorViewSet)
router.register(r'promotions', views.promotionViewSet)
router.register(r'activities', views.clubActivityDetailViewSet)
router.register(r'clubs', views.ClubsViewSet)
router.register(r'bookmarksaerch', views.BookmarkSearchViewSet)
router.register(r'userInformation', views.userAccountViewset)
router.register(r'manageraccount', views.managerAccountViewset)
router.register(r'tags', views.TagViewSet)
router.register(r'statistic', views.StatisticsViewSet)
router.register(r'event',views.EventViewset)


urlpatterns = [
    path('login', include('Server_app.login.urls')),
    path('sign-up', include('Server_app.signup.urls')),
    path('alarm', include('Server_app.alarm.urls')),
    #path('useraccount/', include('Server_app.urls')),
    path('manageraccount/', include('Server_app.urls')),
    path('management', include('Server_app.management.urls')),
    path('application', include('Server_app.application.urls')),
    path('promotions/', include('Server_app.urls')),
    path('activities/', include('Server_app.urls')),
    path('tags/', include('Server_app.urls')),
    path('activities/grid/<int:clubID>/', views.clubActivityViewSet.as_view({"get": "list"}), name="activitygrid"),
    path('', include(router.urls)),
    path('clubmembers/<int:uSchoolID>/', views.ClubMemberViewset.as_view({"get": "list"}), name="clubmembergrid"),
    path('clubs/', include('Server_app.urls')),
    path('clublist/<int:club>/<str:category>/<int:sort>/', views.ClubViewSet.as_view({"get": "list"}), name="clublist"),
    path('clubsearch/<int:club>/<str:category>/<int:sort>/<str:search>/', views.ClubSearchViewSet.as_view({"get": "list"}), name="clublistsearch"),
    path('clubfiltering/', csrf_exempt(views.ClubFilter.as_view()), name="clubfiltering"),
    path('bookmarksearch/<int:schoolID>/', views.BookmarkSearchViewSet.as_view({"get":"list"}), name="bookmarklist"),
    path('postbookmark/', csrf_exempt(views.PostBookmark.as_view())),
    path('deletebookmark/<int:clubID>/<int:schoolID>', csrf_exempt(views.DeleteBookmark.as_view())),
    path('userInformation/', include('Server_app.urls')),
    path('clubApply/',csrf_exempt(views.UserClubApply.as_view()), name="clubapply"),
    path('managerfilter/<int:clubID>/', views.ManagerFilterViewset.as_view({"get": "list"}), name ="clubTag"),
    path('postfilter/', csrf_exempt(views.PostFilter.as_view())),
    path('deletefilter/<int:clubID>/', csrf_exempt(views.DeleteFilter.as_view())),
    path('statisticSearch/<int:clubID>/', views.ClubStatisticsViewSet.as_view({"get":"retrieve"}), name="statisticlist"),
    path('clubquestion/<int:pk>/',views.ClubQuestionViewSet.as_view({"get": "retrieve"}), name="clubquestion"),
    path('nrecruitclubs/',views.NRecruitViewSet.as_view({"get": "list"}), name="nrecruitclubs"),
    path('event/', include('Server_app.urls')),
    path('eventlist', include('Server_app.event.urls')),
    path('ajoudongAdmin/', include('Server_admin.urls')),
    path('userfromdevice/<str:token>/',views.UserFromDeviceViewset.as_view({"get":"retrieve"}), name="userfromdevice"),
    re_path('admin/', admin.site.urls),
]

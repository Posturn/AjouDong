"""Server URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt
from Server_app import views
from rest_framework import routers


from Server_app.login import login
from Server_app.signup import signup 


router=routers.DefaultRouter()
router.register(r'SERVER_APP/Major_affiliations', views.MajorViewSet)
router.register(r'promotions', views.promotionViewSet)
router.register(r'activities', views.clubActivityDetailViewSet)
router.register(r'clubs', views.ClubsViewSet)
router.register(r'bookmarksaerch', views.BookmarkSearchViewSet)
router.register(r'useraccount', views.userAccountViewset)
router.register(r'manageraccount', views.managerAccountViewset)
router.register(r'statistic', views.StatisticsViewSet)

urlpatterns = [
    path('login', include('Server_app.login.urls')),
    path('sign-up', include('Server_app.signup.urls')),
    path('useraccount/', include('Server_app.urls')),
    path('manageraccount/', include('Server_app.urls')),
    path('management', include('Server_app.management.urls')),
    path('promotions/', include('Server_app.urls')),
    path('activities/', include('Server_app.urls')),
    path('activities/grid/<int:clubID>/', views.clubActivityViewSet.as_view({"get": "list"}), name="activitygrid"),
    path('', include(router.urls)),
    path('clublist/<int:club>/<str:category>/<int:sort>/', views.ClubViewSet.as_view({"get": "list"}), name="clublist"),
    path('clubsearch/<int:club>/<str:category>/<int:sort>/<str:search>/', views.ClubSearchViewSet.as_view({"get": "list"}), name="clublistsearch"),
    path('clubfiltering/', csrf_exempt(views.ClubFilter.as_view()), name="clubfiltering"),
    path('bookmarksearch/<int:schoolID>/', views.BookmarkSearchViewSet.as_view({"get":"list"}), name="bookmarklist"),
    path('postbookmark/', csrf_exempt(views.PostBookmark.as_view())),
    path('deletebookmark/<int:clubID>/<int:schoolID>', csrf_exempt(views.DeleteBookmark.as_view())),
    path('userInformation/<int:pk>/', views.UserInfoViewSet.as_view({"get": "retrieve"}), name="userinformation"),
    path('clubApply/',csrf_exempt(views.UserClubApply.as_view()), name="clubapply"),
    path('statisticSearch/<int:clubID>/', views.ClubStatisticsViewSet.as_view({"get":"retrieve"}), name="statisticlist"),
    re_path('admin/', admin.site.urls),
]

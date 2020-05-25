from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt
from Server_app import views
from rest_framework import routers
from Server_app.login import login
from django.conf.urls import url
# from Server_app.signup import signup



router=routers.DefaultRouter()
router.register(r'SERVER_APP/Major_affiliations', views.MajorViewSet)
router.register(r'promotions', views.promotionViewSet)
router.register(r'activities', views.clubActivityDetailViewSet)

urlpatterns = [
    path('login', include('Server_app.login.urls')),
    path('sign-up', include('Server_app.signup.urls')),
    path('promotions/', include('Server_app.urls')),
    path('activities/', include('Server_app.urls')),
    path('activities/grid/<int:clubID>/', views.clubActivityViewSet.as_view({"get": "list"}), name="activitygrid"),
    path('', include(router.urls)),
    path('clublist/<int:sort>/', views.ClubViewSet.as_view({"get": "list"}), name="clublist"),
    path('clubsearch/<int:sort>/<str:search>/', views.ClubSearchViewSet.as_view({"get": "list"}), name="clublistsearch"),
    re_path('admin/', admin.site.urls),
]

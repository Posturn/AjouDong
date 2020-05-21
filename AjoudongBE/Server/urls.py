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
# router.register(r'SERVER_APP/club', views.ClubViewSet)
router.register(r'promotions', views.promotionViewSet)
router.register(r'activities', views.clubActivityViewSet)

urlpatterns = [
    path('login', include('Server_app.login.urls')),
    path('sign-up', include('Server_app.signup.urls')),
    path('promotions/', include('Server_app.urls')),
    path('activities/', include('Server_app.urls')),
    #path('/promotions/<int:promotion_id>', views.getPromotion.as_view()),
    path('', include(router.urls)),
    path('clublist/<int:sort>/', views.ClubViewSet.as_view({"get": "list"}), name="clublist"),
    re_path('admin/', admin.site.urls),
]

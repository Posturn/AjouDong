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
router.register(r'SERVER_APP/club', views.ClubViewSet)
router.register(r'promotions', views.promotionViewSet)
router.register(r'activities', views.clubActivityViewSet)

urlpatterns = [
    path('login', csrf_exempt(login.login.as_view())),
    path('sign-up', csrf_exempt(signup.signup.as_view())),
    path('sign-up/sameID', csrf_exempt(signup.checkSameID.as_view())),
    path('promotions/', include('Server_app.urls')),
    path('activities/', include('Server_app.urls')),
    #path('/promotions/<int:promotion_id>', views.getPromotion.as_view()),
    path('', include(router.urls)),
    re_path('admin/', admin.site.urls),
]

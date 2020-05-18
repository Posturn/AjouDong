from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import signup 

urlpatterns = [
    path('', csrf_exempt(signup.signup.as_view())),
    path('/sameID', csrf_exempt(signup.checkSameID.as_view()))
    
]

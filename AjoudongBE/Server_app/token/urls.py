from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import token

urlpatterns = [
    path('/getidbytoken/<str:token>', csrf_exempt(token.getIDbyToken.as_view())),
    
]

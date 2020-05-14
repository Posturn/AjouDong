from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import login

urlpatterns = [
    path('', csrf_exempt(login.login.as_view()))
]

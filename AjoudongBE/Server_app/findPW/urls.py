from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from . import findPW

urlpatterns = [
    path('/gettemppw', csrf_exempt(findPW.getTempPW.as_view())),
]
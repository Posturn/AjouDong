from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from . import findID

urlpatterns = [
    path('/getmaskedid', csrf_exempt(findID.getMaskedID.as_view())),
]
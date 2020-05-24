from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import management

urlpatterns = [
    path('/memberlist/<int:clubID>', csrf_exempt(management.memberlist.as_view())),
    path('/applieduserlist/<int:clubID>', csrf_exempt(management.applieduserlist.as_view())),
    path('/applieduserlist/newmember', csrf_exempt(management.newmember.as_view())),
    path('/memberlist/newmember', csrf_exempt(management.memberlist.as_view())),
    path('/memberlist/deletemember', csrf_exempt(management.memberlist.as_view())),
]
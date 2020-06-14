from django.contrib import admin
from django.urls import path, include, re_path
from django.views.decorators.csrf import csrf_exempt

from rest_framework import routers

from . import event

urlpatterns = [
    path('/<int:clubID>', csrf_exempt(event.getEvent.as_view())),
    path('/', csrf_exempt(event.getEventAll.as_view())),
]